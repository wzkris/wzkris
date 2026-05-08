# wzkris-common-redis

## 通用幂等注解

在需要防重复提交的方法上添加 `@Idempotent` 即可启用幂等控制。

```java
@Idempotent(ttlSeconds = 120)
public Result<Long> createOrder(String payloadJson) {
    return Result.ok(orderService.create(payloadJson));
}
```

```java
@Idempotent(key = "#p0 + ':' + #p1")
public Result<Long> createOrder(Long userId, String bizNo) {
    return Result.ok(orderService.create(userId, bizNo));
}
```

### 规则说明

- 幂等键格式：`idem:{businessKey}:{argsHash}`
- `businessKey` 优先使用注解 `key` 的 SpEL 结果（支持 `#p0/#a0/参数名`）
- `key` 为空时默认使用方法全限定名（`包名.类名.方法名`）
- `argsHash` 为请求参数规范化后的 SHA-256 摘要，自动消除字段顺序、空格、换行差异
- 首次请求写入 `PROCESSING` 占位并执行业务，完成后写入 `DONE + result`
- 重复请求命中 `DONE` 直接返回首次结果；命中 `PROCESSING` 立即返回处理中状态

### 参数

- `key`：幂等键SpEL表达式（为空时走方法全限定名）
- `ttlSeconds`：结果缓存时长（秒）
