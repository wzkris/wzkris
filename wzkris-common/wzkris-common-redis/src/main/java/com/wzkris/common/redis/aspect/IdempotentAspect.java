package com.wzkris.common.redis.aspect;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.wzkris.common.core.exception.request.TooManyRequestException;
import com.wzkris.common.core.utils.JsonUtil;
import com.wzkris.common.redis.annotation.Idempotent;
import com.wzkris.common.redis.enums.IdempotentStatusEnum;
import com.wzkris.common.redis.model.IdempotentRecord;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.context.expression.MethodBasedEvaluationContext;
import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.util.DigestUtils;

import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

@Slf4j
@Aspect
public class IdempotentAspect {

    private static final String IDEMPOTENT_KEY_PREFIX = "idem:";

    private final RedisTemplate<String, Object> redisTemplate;

    private final ObjectMapper objectMapper;

    private final ObjectMapper canonicalMapper;

    private final ExpressionParser expressionParser = new SpelExpressionParser();

    private final DefaultParameterNameDiscoverer parameterNameDiscoverer = new DefaultParameterNameDiscoverer();

    public IdempotentAspect(RedisTemplate<String, Object> redisTemplate, ObjectMapper objectMapper) {
        this.redisTemplate = redisTemplate;
        this.objectMapper = objectMapper;
        this.canonicalMapper = objectMapper.copy().configure(MapperFeature.SORT_PROPERTIES_ALPHABETICALLY, true).configure(SerializationFeature.ORDER_MAP_ENTRIES_BY_KEYS, true);
    }

    @Around("@annotation(idempotent)")
    public Object around(ProceedingJoinPoint joinPoint, Idempotent idempotent) throws Throwable {
        final String key = buildIdempotentKey(joinPoint, idempotent);
        Duration ttl = Duration.ofSeconds(idempotent.ttlSeconds());
        ValueOperations<String, Object> valueOperations = redisTemplate.opsForValue();
        Boolean created = valueOperations.setIfAbsent(key, IdempotentRecord.processing(), ttl);
        if (Boolean.FALSE.equals(created)) {
            return idempotentHandle(valueOperations, key);
        }

        Object result;
        try {
            result = joinPoint.proceed();
        } catch (Throwable e) {
            redisTemplate.delete(key);
            throw e;
        }
        valueOperations.set(key, IdempotentRecord.done(result), ttl);
        return result;
    }

    private Object idempotentHandle(ValueOperations<String, Object> valueOperations, String key) {
        IdempotentRecord record = JsonUtil.convertValue(valueOperations.get(key), IdempotentRecord.class);
        if (IdempotentStatusEnum.DONE == record.getStatus()) {
            return record.getResult();
        }
        throw new TooManyRequestException();
    }

    private String buildIdempotentKey(ProceedingJoinPoint joinPoint, Idempotent idempotent) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        String businessKey = evaluateBusinessKey(joinPoint, signature, idempotent);
        String argsHash = DigestUtils.md5DigestAsHex(toCanonicalPayload(joinPoint.getArgs()).getBytes(StandardCharsets.UTF_8));
        return IDEMPOTENT_KEY_PREFIX + businessKey + ":" + argsHash;
    }

    private String evaluateBusinessKey(ProceedingJoinPoint joinPoint, MethodSignature signature, Idempotent idempotent) {
        String spel = idempotent.key();
        if (spel == null || spel.isBlank()) {
            return defaultBusinessKey(signature);
        }
        Method method = signature.getMethod();
        MethodBasedEvaluationContext context = new MethodBasedEvaluationContext(joinPoint.getTarget(), method, joinPoint.getArgs(), parameterNameDiscoverer);
        try {
            Object value = expressionParser.parseExpression(spel).getValue(context);
            if (value == null) {
                return defaultBusinessKey(signature);
            }
            return String.valueOf(value);
        } catch (Exception e) {
            throw new IllegalArgumentException("Failed to parse idempotent key SpEL: " + spel, e);
        }
    }

    private String defaultBusinessKey(MethodSignature signature) {
        return signature.getDeclaringTypeName() + "." + signature.getName();
    }

    private String toCanonicalPayload(Object[] args) {
        ArrayNode arrayNode = canonicalMapper.createArrayNode();
        if (args != null) {
            for (Object arg : args) {
                arrayNode.add(toCanonicalNode(arg));
            }
        }
        try {
            return canonicalMapper.writeValueAsString(arrayNode);
        } catch (Exception e) {
            throw new IllegalArgumentException("Failed to serialize idempotent args", e);
        }
    }

    private JsonNode toCanonicalNode(Object value) {
        if (value == null) {
            return JsonNodeFactory.instance.nullNode();
        }
        if (value instanceof String str && looksLikeJson(str)) {
            try {
                return canonicalize(canonicalMapper.readTree(str));
            } catch (Exception ignored) {
            }
        }
        return canonicalize(canonicalMapper.valueToTree(value));
    }

    private JsonNode canonicalize(JsonNode node) {
        if (node == null || node.isNull() || node.isValueNode()) {
            return node;
        }
        if (node.isArray()) {
            ArrayNode normalized = canonicalMapper.createArrayNode();
            for (JsonNode item : node) {
                normalized.add(canonicalize(item));
            }
            return normalized;
        }
        if (node.isObject()) {
            ObjectNode normalized = canonicalMapper.createObjectNode();
            Map<String, JsonNode> sorted = new TreeMap<>();
            Iterator<Map.Entry<String, JsonNode>> fields = node.fields();
            while (fields.hasNext()) {
                Map.Entry<String, JsonNode> field = fields.next();
                sorted.put(field.getKey(), field.getValue());
            }
            for (Map.Entry<String, JsonNode> entry : sorted.entrySet()) {
                normalized.set(entry.getKey(), canonicalize(entry.getValue()));
            }
            return normalized;
        }
        return node;
    }

    private boolean looksLikeJson(String value) {
        if (value == null) {
            return false;
        }
        String trimmed = value.trim();
        return (trimmed.startsWith("{") && trimmed.endsWith("}")) || (trimmed.startsWith("[") && trimmed.endsWith("]"));
    }

}
