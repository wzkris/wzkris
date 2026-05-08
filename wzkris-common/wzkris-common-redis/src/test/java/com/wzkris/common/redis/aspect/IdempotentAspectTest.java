package com.wzkris.common.redis.aspect;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wzkris.common.core.exception.request.TooManyRequestException;
import com.wzkris.common.redis.annotation.Idempotent;
import com.wzkris.common.redis.model.IdempotentRecord;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import java.lang.annotation.Annotation;
import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class IdempotentAspectTest {

    @Mock
    private RedisTemplate<String, Object> redisTemplate;

    @Mock
    private ValueOperations<String, Object> valueOperations;

    @Mock
    private ProceedingJoinPoint joinPoint;

    @Mock
    private MethodSignature signature;

    private IdempotentAspect idempotentAspect;

    private Map<String, Object> redisStore;

    @BeforeEach
    void setUp() {
        idempotentAspect = new IdempotentAspect(redisTemplate, new ObjectMapper());
        redisStore = new ConcurrentHashMap<>();

        when(redisTemplate.opsForValue()).thenReturn(valueOperations);
        when(joinPoint.getSignature()).thenReturn(signature);
        when(signature.getDeclaringTypeName()).thenReturn("com.wzkris.demo.TestService");
        when(signature.getName()).thenReturn("submit");

        when(valueOperations.setIfAbsent(anyString(), any(), any(Duration.class)))
                .thenAnswer(invocation -> {
                    String key = invocation.getArgument(0);
                    Object value = invocation.getArgument(1);
                    return redisStore.putIfAbsent(key, value) == null;
                });
        doAnswer(invocation -> {
            String key = invocation.getArgument(0);
            Object value = invocation.getArgument(1);
            redisStore.put(key, value);
            return null;
        }).when(valueOperations).set(anyString(), any(), any(Duration.class));
        when(valueOperations.get(anyString())).thenAnswer(invocation -> redisStore.get(invocation.getArgument(0)));
        when(redisTemplate.delete(anyString())).thenAnswer(invocation -> redisStore.remove(invocation.getArgument(0)) != null);
    }

    @Test
    void shouldReuseCachedResultWhenJsonSemanticIsSame() throws Throwable {
        when(joinPoint.getArgs()).thenReturn(new Object[]{"{\"a\":1,\"b\":2}"});
        when(joinPoint.proceed()).thenReturn("ok");

        Object firstResult = idempotentAspect.around(joinPoint, idempotent());

        ProceedingJoinPoint secondJoinPoint = mock(ProceedingJoinPoint.class);
        when(secondJoinPoint.getSignature()).thenReturn(signature);
        when(secondJoinPoint.getArgs()).thenReturn(new Object[]{"{ \n\"b\": 2,\n\"a\":1 }"});

        Object secondResult = idempotentAspect.around(secondJoinPoint, idempotent());

        assertEquals("ok", firstResult);
        assertEquals("ok", secondResult);
        verify(secondJoinPoint, never()).proceed();
    }

    @Test
    void shouldUseSpelKeyWhenSpecified() throws Throwable {
        when(joinPoint.getArgs()).thenReturn(new Object[]{"A"});
        when(joinPoint.proceed()).thenReturn("ok-A");

        Object firstResult = idempotentAspect.around(joinPoint, idempotent(60, "#p0"));

        ProceedingJoinPoint secondJoinPoint = mock(ProceedingJoinPoint.class);
        when(secondJoinPoint.getSignature()).thenReturn(signature);
        when(secondJoinPoint.getArgs()).thenReturn(new Object[]{"B"});
        when(secondJoinPoint.proceed()).thenReturn("ok-B");

        Object secondResult = idempotentAspect.around(secondJoinPoint, idempotent(60, "#p0"));

        assertEquals("ok-A", firstResult);
        assertEquals("ok-B", secondResult);
        verify(secondJoinPoint).proceed();
    }

    @Test
    void shouldUseArgsHashWithDefaultMethodKey() throws Throwable {
        when(joinPoint.getArgs()).thenReturn(new Object[]{"X"});
        when(joinPoint.proceed()).thenReturn("ok-X");

        Object firstResult = idempotentAspect.around(joinPoint, idempotent());

        ProceedingJoinPoint secondJoinPoint = mock(ProceedingJoinPoint.class);
        when(secondJoinPoint.getSignature()).thenReturn(signature);
        when(secondJoinPoint.getArgs()).thenReturn(new Object[]{"Y"});
        when(secondJoinPoint.proceed()).thenReturn("ok-Y");

        Object secondResult = idempotentAspect.around(secondJoinPoint, idempotent());

        assertEquals("ok-X", firstResult);
        assertEquals("ok-Y", secondResult);
        verify(secondJoinPoint).proceed();
    }

    @Test
    void shouldDeleteProcessingRecordWhenBusinessThrows() throws Throwable {
        when(joinPoint.getArgs()).thenReturn(new Object[]{"{\"a\":1}"});
        when(joinPoint.proceed()).thenThrow(new IllegalStateException("boom"));

        assertThrows(IllegalStateException.class, () -> idempotentAspect.around(joinPoint, idempotent()));
        assertTrue(redisStore.isEmpty());
    }

    @Test
    void shouldThrowTooManyRequestWhenProcessingNotDoneWithinTimeout() {
        when(valueOperations.setIfAbsent(anyString(), any(), any(Duration.class))).thenReturn(false);
        when(valueOperations.get(anyString())).thenReturn(IdempotentRecord.processing());
        when(joinPoint.getArgs()).thenReturn(new Object[]{"{\"a\":1}"});

        assertThrows(TooManyRequestException.class,
                () -> idempotentAspect.around(joinPoint, idempotent(60)));
    }

    private Idempotent idempotent() {
        return idempotent(60, "");
    }

    private Idempotent idempotent(long ttlSeconds) {
        return idempotent(ttlSeconds, "");
    }

    private Idempotent idempotent(long ttlSeconds, String key) {
        return new Idempotent() {
            @Override
            public String key() {
                return key;
            }

            @Override
            public long ttlSeconds() {
                return ttlSeconds;
            }

            @Override
            public Class<? extends Annotation> annotationType() {
                return Idempotent.class;
            }
        };
    }
}
