---
name: java-layer-conventions
description: "Use when: creating or reviewing Java classes in wzkris, especially Controller, Api, Service layers. Enforces naming rules, package placement, and layered architecture patterns (Controller → Api → Service). Prevents business logic in controllers, context access outside service parameters, and exception throwing in interface code."
applyTo: "**/*.java"
---

# wzkris Java 分层编码规范

## 分层结构速查表

| 层 | 命名 | 包位置 | 职责 | 核心约束 |
|---|---|---|---|---|
| **Controller** | `{Feature}Controller` | `com.wzkris.{module}.controller` | HTTP入口、透传参数 | ❌ 无业务逻辑、验证、异常 |
| **Api** | `{Feature}Api` + impl | `api` + `api.impl` | 业务编排、上下文获取 | ❌ 接口不throws；impl捕获异常→Result |
| **Service** | `{Feature}Service` + impl | `service` + `service.impl` | 复用逻辑、数据访问 | ❌ 参数只来自方法签名（不从全局获取）；可throw异常 |

## 包结构

```
com.wzkris.{module}
├── controller/{Feature}Controller.java
├── api/{Feature}Api.java
├── api/impl/{Feature}ApiImpl.java
├── service/{Feature}Service.java
├── service/impl/{Feature}ServiceImpl.java
├── domain/{Entity}DO.java
├── mapper/{Entity}Mapper.java
├── request/{Feature}Request.java
└── response/{Feature}Response.java
```

## 关键约束

### ❌ 禁止

1. **Service获取上下文** - 参数只从方法签名取，不从ThreadLocal/Request获取
2. **Api接口throws异常** - 异常在impl层捕获，转为Result返回
3. **Controller业务逻辑** - 只透传请求参数到Api层
4. **跨层调用** - Controller→Api→Service→Mapper，不允许逆向或跳层

### 异常流向

```
Service throws BusinessException
    ↓
Api impl catches → Result.fail()
    ↓
Controller returns Result
    ↓
全局异常处理器（如有其他异常）
```

## 创建检查清单

- [ ] 类名 + 包位置是否正确
- [ ] Service参数来自方法签名，非全局上下文
- [ ] Api接口无throws声明，impl捕获异常
- [ ] Controller只透传参数，无业务逻辑
