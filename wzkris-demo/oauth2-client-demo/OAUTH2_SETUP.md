# OAuth2 客户端与授权服务器调通手册

## 一、架构说明

```
┌─────────────────┐         ┌──────────────────┐         ┌─────────────────┐
│   客户端应用     │ ──────> │   授权服务器     │ ──────> │   用户登录       │
│  (3342端口)     │ <────── │   (9000端口)     │ <────── │                 │
└─────────────────┘         └──────────────────┘         └─────────────────┘
```

**流程**：客户端 → 授权服务器登录 → 授权 → 回调客户端 → 获取 Token

---

## 二、授权服务器配置（必须）

### 2.1 在数据库中注册客户端

在 `biz.oauth2_client` 表中插入或更新客户端记录：

```sql
INSERT INTO biz.oauth2_client (
    id, client_name, client_id, client_secret,
    scopes, authorization_grant_types, redirect_uris,
    status, auto_approve, create_at, creator_id
) VALUES (
    1,
    'OAuth2客户端Demo',
    'oauth_client_demo',           -- 必须与客户端配置一致
    'secret',                      -- 必须与客户端配置一致
    ARRAY['read'],                 -- scope 列表
    ARRAY['authorization_code'],   -- 授权类型
    ARRAY['http://127.0.0.1:3342/login/oauth2/code/auth-center'],  -- 回调地址
    '1',                           -- 1=启用，0=禁用
    false,                         -- 是否需要用户确认
    NOW(),
    1
);
```

**关键字段说明**：
- `client_id`: 必须与客户端配置的 `client-id` 完全一致
- `client_secret`: 必须与客户端配置的 `client-secret` 完全一致
- `redirect_uris`: 必须包含完整的回调地址（包括协议、域名、端口、路径）
- `authorization_grant_types`: 必须包含 `authorization_code`
- `scopes`: 必须包含客户端请求的 scope（如 `read`）
- `status`: 必须为 `'1'`（启用状态）

---

## 三、客户端配置

### 3.1 依赖配置（pom.xml）

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-security</artifactId>
</dependency>
<dependency>
    <groupId>org.springframework.security</groupId>
    <artifactId>spring-security-oauth2-client</artifactId>
</dependency>
```

### 3.2 应用配置（application.yml）

```yaml
server:
  port: 3342

spring:
  security:
    oauth2:
      client:
        registration:
          auth-center:                    # registrationId（可自定义）
            provider: wzkris              # 对应 provider 名称
            client-id: oauth_client_demo   # 必须与数据库一致
            client-secret: secret          # 必须与数据库一致
            authorization-grant-type: authorization_code
            redirect-uri: "http://127.0.0.1:3342/login/oauth2/code/{registrationId}"
            scope: read                    # 必须与数据库一致
            client-name: 系统
        provider:
          wzkris:
            issuer-uri: http://127.0.0.1:9000  # 授权服务器地址
```

**重要说明**：
- `{registrationId}` 会自动替换为 `auth-center`
- 最终回调地址：`http://127.0.0.1:3342/login/oauth2/code/auth-center`
- `issuer-uri` 会自动发现端点（推荐）或手动指定端点

### 3.3 安全配置（SecurityConfig.java）

```java
@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Bean
    public SecurityFilterChain oauth2ClientFilterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/actuator/**", "/swagger-ui/**", "/v3/api-docs/**")
                .permitAll()
                .requestMatchers("/login/oauth2/code/**")
                .permitAll()
                .requestMatchers("/login")
                .permitAll()
                .anyRequest()
                .authenticated()
            )
            .oauth2Login(oauth2 -> oauth2
                .loginPage("/oauth2/authorization/auth-center")
                .defaultSuccessUrl("/messages", true)
                .failureUrl("/oauth2/authorization/auth-center?error")
            )
            .csrf(AbstractHttpConfigurer::disable);
        return http.build();
    }
}
```

### 3.4 登录控制器（LoginController.java）

```java
@Controller
public class LoginController {
    @GetMapping("/login")
    public String login() {
        return "redirect:/oauth2/authorization/auth-center";
    }
}
```

---

## 四、调通步骤

### 4.1 启动服务

1. **启动授权服务器**（端口 9000）
   ```bash
   # 确保授权服务器正常运行
   # 访问 http://127.0.0.1:9000/.well-known/openid-configuration 验证
   ```

2. **启动客户端应用**（端口 3342）
   ```bash
   # 启动 oauth2-client-demo
   ```

### 4.2 验证配置

**检查授权服务器端点**：
- OIDC 配置：`http://127.0.0.1:9000/.well-known/openid-configuration`
- JWK Set：`http://127.0.0.1:9000/oauth2/jwks`

**检查数据库客户端**：
```sql
SELECT client_id, client_secret, redirect_uris, authorization_grant_types, scopes, status
FROM biz.oauth2_client
WHERE client_id = 'oauth_client_demo';
```

### 4.3 测试登录

**方式一：直接访问授权端点**
```
http://127.0.0.1:3342/oauth2/authorization/auth-center
```

**方式二：访问受保护资源**
```
http://127.0.0.1:3342/messages
```

**方式三：访问登录页面**
```
http://127.0.0.1:3342/login
```

### 4.4 预期流程

1. 访问客户端 → 重定向到授权服务器登录页面
2. 用户登录 → 跳转到授权确认页面
3. 用户确认 → 回调到客户端：`http://127.0.0.1:3342/login/oauth2/code/auth-center?code=...`
4. 客户端用授权码换取 Token → 登录成功 → 跳转到 `/messages`

---

## 五、常见问题排查

### 问题1：invalid_client
**原因**：客户端 ID 或密钥不匹配
**解决**：检查数据库 `client_id` 和 `client_secret` 与配置是否完全一致

### 问题2：invalid_redirect_uri
**原因**：回调地址不匹配
**解决**：确保数据库 `redirect_uris` 包含：`http://127.0.0.1:3342/login/oauth2/code/auth-center`

### 问题3：invalid_scope
**原因**：请求的 scope 未在客户端注册
**解决**：检查数据库 `scopes` 是否包含 `read`

### 问题4：客户端未启用
**原因**：数据库 `status` 不是 `'1'`
**解决**：更新 `status = '1'`

### 问题5：无法访问授权服务器
**原因**：网络问题或授权服务器未启动
**解决**：
- 检查授权服务器是否运行在 9000 端口
- 检查 `issuer-uri` 配置是否正确
- 检查防火墙/网络配置

### 问题6：静态资源错误（default-ui.css）
**原因**：Spring Security 默认登录页面需要静态资源
**解决**：已通过 `LoginController` 重定向解决，无需额外配置

---

## 六、调试建议

### 启用调试日志

```yaml
logging:
  level:
    org.springframework.security: DEBUG
    org.springframework.security.oauth2: DEBUG
```

### 验证端点

- **授权服务器 OIDC 配置**：`http://127.0.0.1:9000/.well-known/openid-configuration`
- **JWK Set**：`http://127.0.0.1:9000/oauth2/jwks`
- **授权端点**：`http://127.0.0.1:9000/oauth2/authorize`
- **令牌端点**：`http://127.0.0.1:9000/oauth2/token`

---

## 七、关键检查清单

- [ ] 授权服务器运行在 9000 端口
- [ ] 客户端应用运行在 3342 端口
- [ ] 数据库中存在 `client_id = 'oauth_client_demo'` 的记录
- [ ] 数据库 `client_secret` 与配置一致
- [ ] 数据库 `redirect_uris` 包含完整回调地址
- [ ] 数据库 `authorization_grant_types` 包含 `authorization_code`
- [ ] 数据库 `scopes` 包含 `read`
- [ ] 数据库 `status = '1'`（启用）
- [ ] 客户端配置的 `issuer-uri` 正确
- [ ] 客户端配置的 `registrationId` 为 `auth-center`
- [ ] 网络连通性正常

---

## 八、快速参考

**客户端配置路径**：`/oauth2/authorization/auth-center`  
**回调地址**：`http://127.0.0.1:3342/login/oauth2/code/auth-center`  
**授权服务器**：`http://127.0.0.1:9000`  
**客户端ID**：`oauth_client_demo`  
**客户端密钥**：`secret`  
**Scope**：`read`

