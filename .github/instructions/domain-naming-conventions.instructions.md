---
name: domain-naming-conventions
description: "Use when: creating or reviewing domain objects, request/response DTOs, or data model classes in wzkris. Enforces naming conventions for database entities (DO), request bodies (Request), response bodies (Response), and their proper package placement."
applyTo: "**/{domain,request,response,dto}/**/*.java"
---

# wzkris Domain 对象命名规范

## 对象命名速查表

| 对象 | 命名 | 包位置 | 用途 | 核心特点 |
|---|---|---|---|---|
| **DO** | `{Entity}DO` | `domain` | 数据库映射 | @TableName, @TableId, @TableField；包含所有DB字段(id, create_time等) |
| **Request** | `{Feature}Request` | `request` | HTTP请求体 | @NotNull, @Email等验证注解；不含id、create_time、update_time |
| **Response** | `{Feature}Response` | `response` | HTTP响应体 | @ApiModel, @ApiModelProperty；排除密码等敏感信息；可含计算字段 |

## 包结构

```
com.wzkris.{module}
├── domain/{Entity}DO.java
├── request/{Feature}Request.java
├── request/Create{Feature}Request.java
├── request/Update{Feature}Request.java
├── response/{Feature}Response.java
├── response/{Feature}ListResponse.java
└── mapper/{Entity}Mapper.java
```

## 字段对应关系

| 字段 | DO | Request | Response |
|---|---|---|---|
| id/userId | ✅ 有 | ❌ 无 | ✅ 有 |
| create_time/update_time | ✅ 有 | ❌ 无 | ✅ 有 |
| 密码/密钥 | ✅ 有 | ❌ 不应该 | ❌ 排除 |
| 验证注解 | ❌ 无 | ✅ 有 | ❌ 无 |
| 计算字段 | ❌ 无 | ❌ 无 | ✅ 有 |

## 对象转换流

```
Request (客户端提供)
    ↓ Converter / MapStruct
   DO (数据库存取)
    ↓ Converter / MapStruct
Response (返回客户端)
```

## 常见命名示例

| 场景 | DO | Request | Response |
|---|---|---|---|
| 用户管理 | `UserDO` | `CreateUserRequest` | `UserResponse` |
| 二维码登录 | `QrLoginDO` | `ScanQrCodeRequest` | `QrCodeResponse` |
| 字典管理 | `DictionaryDO` | `CreateDictionaryRequest` | `DictionaryResponse` |
| 租户管理 | `TenantDO` | `CreateTenantRequest` | `TenantResponse` |

## 创建检查清单

- [ ] 命名 - DO/Request/Response后缀正确
- [ ] 包位置 - 在对应的domain/request/response目录
- [ ] DO字段 - @TableName, @TableId, @TableField注解完整
- [ ] Request字段 - 有@NotNull, @NotBlank等验证注解
- [ ] Response字段 - 有@ApiModel, @ApiModelProperty文档注解
- [ ] Request字段 - 不含id、create_time、update_time
- [ ] Response字段 - 排除密码、token等敏感信息
- [ ] 转换工具 - 有对应的Converter/Mapper类
