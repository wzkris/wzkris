package com.wzkris.common.log.annotation.aspect;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.text.StrPool;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.wzkris.common.core.domain.Result;
import com.wzkris.common.core.threads.TracingIdRunnable;
import com.wzkris.common.core.utils.AddressUtil;
import com.wzkris.common.core.utils.JsonUtil;
import com.wzkris.common.core.utils.ServletUtil;
import com.wzkris.common.core.utils.StringUtil;
import com.wzkris.common.log.annotation.OperateLog;
import com.wzkris.common.log.enums.OperateStatus;
import com.wzkris.common.security.utils.SystemUserUtil;
import com.wzkris.system.rmi.RmiSysLogFeign;
import com.wzkris.system.rmi.domain.req.OperLogReq;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.http.HttpMethod;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.validation.BindingResult;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;
import java.util.concurrent.*;
import java.util.stream.Stream;

/**
 * 日志切面
 *
 * @author wzkris
 */
@Slf4j
@Aspect
public class OperateLogAspect implements ApplicationRunner {

    /**
     * 敏感属性字段
     */
    public final String[] EXCLUDE_PROPERTIES = {
            "pwd", "passwd", "password", "oldPassword", "newPassword", "confirmPassword"
    };

    private final ObjectMapper objectMapper = JsonUtil.getObjectMapper().copy();

    private final ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();

    private final RmiSysLogFeign rmiSysLogFeign;

    private boolean shutdown = false;

    public OperateLogAspect(RmiSysLogFeign rmiSysLogFeign) {
        this.rmiSysLogFeign = rmiSysLogFeign;
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL); // 配置null不序列化, 避免大量无用参数存入DB
        executor.setThreadNamePrefix("OperateLogAspectTask-");
        executor.setCorePoolSize(2);
        executor.setMaxPoolSize(5);
        executor.setQueueCapacity(500);
        executor.setKeepAliveSeconds(180);
        // 线程池对拒绝任务的处理策略
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        executor.setThreadFactory(new ThreadFactory() {
            @Override
            public Thread newThread(Runnable r) {
                return executor.newThread(new TracingIdRunnable(r));
            }
        });
        executor.setDaemon(true);
        executor.initialize();
    }

    private final BlockingQueue<OperLogReq> bufferQ = new LinkedBlockingQueue<>();

    private final List<OperLogReq> batchQ = new ArrayList<>();

    private static final int BATCH_SIZE = 10;

    private static final int MAX_PARAM_LENGTH = 1000;

    private static final int MAX_ERROR_LENGTH = 1000;

    private static final int MAX_URL_LENGTH = 150;

    public void run() {
        for (; ; ) {
            try {
                OperLogReq operLogReq = bufferQ.poll(60 * 3, TimeUnit.SECONDS);
                if (operLogReq == null) {
                    flushBatchQ();
                } else {
                    batchQ.add(operLogReq);
                    if (batchQ.size() >= BATCH_SIZE) {
                        flushBatchQ();
                    }
                }
            } catch (InterruptedException e) {
                if (!batchQ.isEmpty()) {
                    rmiSysLogFeign.saveOperlogs(batchQ);
                }
                if (shutdown) {
                    break;
                } else {
                    log.error("操作日志的bufferQ发生异常中断：{}", e.getMessage(), e);
                }
            }
        }
    }

    /**
     * 刷新batchQ队列
     */
    private synchronized void flushBatchQ() {
        if (!batchQ.isEmpty()) {
            ArrayList<OperLogReq> operLogReqs = new ArrayList<>(batchQ);
            executor.execute(() -> {
                rmiSysLogFeign.saveOperlogs(operLogReqs);
            });
            batchQ.clear();
        }
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        executor.execute(this::run);
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            log.info("[{}] Run shutdown hook now", this.getClass().getSimpleName());
            this.shutdown = true;
            executor.shutdown();
        }));
    }

    /**
     * 处理完请求后执行
     *
     * @param joinPoint 切点
     */
    @AfterReturning(pointcut = "@annotation(operateLog)", returning = "jsonResult")
    public void doAfterReturning(JoinPoint joinPoint, OperateLog operateLog, Object jsonResult) {
        handleLog(joinPoint, operateLog, jsonResult, null);
    }

    /**
     * 拦截异常操作
     *
     * @param joinPoint 切点
     */
    @AfterThrowing(pointcut = "@annotation(operateLog)", throwing = "exception")
    public void doAfterThrowing(JoinPoint joinPoint, OperateLog operateLog, Exception exception) {
        handleLog(joinPoint, operateLog, null, exception);
    }

    protected void handleLog(final JoinPoint joinPoint, OperateLog operateLog, Object jsonResult, final Exception exception) {
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes)
                RequestContextHolder.getRequestAttributes();
        if (requestAttributes == null) {
            return;
        }

        HttpServletRequest request = requestAttributes.getRequest();
        try {
            OperLogReq operLogReq = buildOperLog(joinPoint, operateLog, request, jsonResult, exception);

            bufferQ.add(operLogReq);
        } catch (Exception e) {
            log.error("日志切面发生异常，异常信息:{}", e.getMessage(), e);
        }
    }

    /**
     * 获取注解中对方法的描述信息 用于Controller层注解
     */
    private OperLogReq buildOperLog(JoinPoint joinPoint, OperateLog operateLog,
                                    HttpServletRequest request, Object jsonResult, Exception exception) throws JsonProcessingException {
        OperLogReq operLogReq = new OperLogReq();
        operLogReq.setUserId(SystemUserUtil.getUserId());
        operLogReq.setOperName(SystemUserUtil.getUsername());
        operLogReq.setTenantId(SystemUserUtil.getTenantId());
        operLogReq.setOperType(operateLog.operateType().getValue());
        operLogReq.setStatus(OperateStatus.SUCCESS.value());
        operLogReq.setOperTime(DateUtil.date());

        String ip = ServletUtil.getClientIP(request);
        operLogReq.setOperIp(ip);
        operLogReq.setOperLocation(AddressUtil.getRealAddressByIp(ip));
        operLogReq.setOperUrl(StringUtil.sub(request.getRequestURI(), 0, MAX_URL_LENGTH));

        String className = joinPoint.getTarget().getClass().getName();
        String methodName = joinPoint.getSignature().getName();
        operLogReq.setMethod(className + StrPool.DOT + methodName + "()");
        operLogReq.setRequestMethod(request.getMethod());

        if (exception != null) {
            operLogReq.setStatus(OperateStatus.FAIL.value());
            operLogReq.setErrorMsg(StringUtil.sub(exception.getMessage(), 0, MAX_ERROR_LENGTH));
        } else if (jsonResult instanceof Result<?> result && !result.isSuccess()) {
            operLogReq.setStatus(OperateStatus.FAIL.value());
            operLogReq.setErrorMsg(StringUtil.sub(result.getMessage(), 0, MAX_ERROR_LENGTH));
        }

        operLogReq.setOperType(operateLog.operateType().getValue());
        operLogReq.setTitle(operateLog.title());
        operLogReq.setSubTitle(operateLog.subTitle());
        operLogReq.setOperatorType(operateLog.operateType().getValue());

        if (operateLog.isSaveRequestData()) {
            setRequestValue(joinPoint, request, operateLog.excludeRequestParam(), operLogReq);
        }

        if (operateLog.isSaveResponseData() && jsonResult != null) {
            operLogReq.setJsonResult(StringUtil.sub(
                    objectMapper.writeValueAsString(jsonResult), 0, MAX_PARAM_LENGTH));
        }

        return operLogReq;
    }

    private void setRequestValue(JoinPoint joinPoint, HttpServletRequest request,
                                 String[] excludeRequestParam, OperLogReq operLogReq)
            throws JsonProcessingException {
        Map<String, String> paramsMap = new HashMap<>();
        String operParams = "";
        final String requestMethod = operLogReq.getRequestMethod();

        if (HttpMethod.PUT.name().equals(requestMethod) ||
                HttpMethod.POST.name().equals(requestMethod)) {
            String params = argsArrayToString(joinPoint.getArgs());
            if (StringUtil.isNotBlank(params) && params.startsWith("{")) {
                paramsMap = objectMapper.readValue(
                        params,
                        TypeFactory.defaultInstance().constructMapType(
                                HashMap.class, String.class, Object.class));
            } else {
                operParams = params;
            }
        } else {
            paramsMap = ServletUtil.getParamMap(request);
        }

        if (!paramsMap.isEmpty()) {
            fuzzyParams(paramsMap, excludeRequestParam);
            operParams = StringUtil.sub(
                    objectMapper.writeValueAsString(paramsMap), 0, MAX_PARAM_LENGTH);
        }
        operLogReq.setOperParam(operParams);
    }

    private void fuzzyParams(Map<String, String> paramsMap, String[] excludeRequestParam) {
        Stream.concat(
                        Arrays.stream(EXCLUDE_PROPERTIES),
                        Arrays.stream(excludeRequestParam != null ? excludeRequestParam : new String[0]))
                .forEach(property -> {
                    if (paramsMap.containsKey(property)) {
                        paramsMap.put(property, "*");
                    }
                });
    }

    private String argsArrayToString(Object[] paramsArray) {
        if (paramsArray == null || paramsArray.length == 0) {
            return StringUtil.EMPTY;
        }

        StringBuilder params = new StringBuilder();
        for (Object o : paramsArray) {
            if (o != null && !isFilterObject(o)) {
                try {
                    String jsonObj = objectMapper.writeValueAsString(o);
                    params.append(jsonObj).append(StrPool.C_SPACE);
                } catch (Exception ignored) {
                }
            }
        }
        return params.toString().trim();
    }

    private boolean isFilterObject(final Object o) {
        if (o instanceof MultipartFile ||
                o instanceof HttpServletRequest ||
                o instanceof HttpServletResponse ||
                o instanceof BindingResult) {
            return true;
        }

        Class<?> clazz = o.getClass();
        if (clazz.isArray()) {
            return clazz.getComponentType().isAssignableFrom(MultipartFile.class);
        } else if (Collection.class.isAssignableFrom(clazz)) {
            Collection<?> collection = (Collection<?>) o;
            return !collection.isEmpty() && collection.iterator().next() instanceof MultipartFile;
        } else if (Map.class.isAssignableFrom(clazz)) {
            Map<?, ?> map = (Map<?, ?>) o;
            return !map.isEmpty() && map.values().iterator().next() instanceof MultipartFile;
        }

        return false;
    }

}
