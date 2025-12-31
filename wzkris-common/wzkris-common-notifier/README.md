# Notifier 通知模块

统一的通知发送模块，提供统一接口，支持多种通知渠道（钉钉、邮件，后续可扩展）。

## 功能特性

- ✅ **统一接口**：标准化 `Notifier<T>` 接口，业务侧零侵入接入
- ✅ **多渠道支持**：内置钉钉、邮件；企业微信/短信/Webhook 可扩展
- ✅ **自动管理**：`NotifierManager` 自动收集并路由到对应渠道
- ✅ **Spring Boot 自动配置**：上电即用（基于 `@AutoConfiguration`）
- ✅ **易扩展**：新增渠道仅需实现接口并声明为 Bean
- ✅ **错误日志自动通知**：自动捕获 ERROR 级别日志并发送通知（可选）

## 快速开始

### 1) 引入模块

本仓内模块，业务服务引入 `wzkris-common-notifier` 即可（无需额外配置文件）。

### 2) 配置钉钉

开启并配置钉钉参数（必须开启 `dingtalk.enabled` 才会装配钉钉通知器）：

```yaml
dingtalk:
  enabled: true
  appKey: your-app-key
  appSecret: your-app-secret
  robotCode: your-robot-code
  # 可选：自定义 AccessToken 获取地址
  # accessTokenUrl: https://example.com/custom/token
  # 可选：自定义机器人 webhook（若走自有机器人）
  # robotWebhook: https://oapi.dingtalk.com/robot/send?access_token=...
```

### 3) 配置邮件（可选）

模块在存在 `JavaMailSender` Bean 时自动启用邮件通知器。可通过 Spring Boot 邮件配置快速获得：

```yaml
spring:
  mail:
    host: smtp.example.com
    port: 465
    username: no-reply@example.com
    password: your-pass
    properties:
      mail:
        smtp:
          auth: true
          ssl:
            enable: true
```

### 4) 启用错误日志自动通知（可选）

开启错误日志自动通知功能，系统会自动捕获 ERROR 级别的日志并触发通知：

```yaml
wzkris:
  notifier:
    error-log:
      enabled: true  # 启用错误日志通知
```

**工作原理**：
- 自动创建自定义 Logback Appender (`ErrorLogEventAppender`)
- 捕获所有 ERROR 级别的日志
- 发布 `ErrorLogEvent` Spring 事件
- `ErrorLogNotifierListener` 监听事件并处理通知

**注意**：
- 需要在 `ErrorLogNotifierListener` 中实现具体的通知逻辑（发送钉钉、邮件等）
- 默认情况下仅记录日志，不会实际发送通知
- 可通过修改 `ErrorLogNotifierListener.onErrorLogEvent()` 方法来实现具体的通知发送

## 使用方式

> 推荐通过 `NotifierManager` 发送，按消息类型自动路由到对应渠道。

### 1) 发送钉钉消息

```java
import com.wzkris.common.notifier.domain.DingtalkMessage;
import com.wzkris.common.notifier.domain.NotificationResult;
import com.wzkris.common.notifier.enums.DingtalkTemplateKeyEnum;
import com.wzkris.common.notifier.manager.NotifierManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NoticeService {

    private final NotifierManager notifierManager;

    public NotificationResult sendToDingtalk() {
        DingtalkMessage message = DingtalkMessage.builder()
                .templateKey(DingtalkTemplateKeyEnum.MARKDOWN)
                .recipients(java.util.List.of("user1", "user2"))
                .templateParams(java.util.Map.of(
                        "title", "系统通知",
                        "content", "这是一个 Markdown 模板内容"
                ))
                .build();

        return notifierManager.send(message);
    }

}
```

### 2) 发送邮件

```java
import com.wzkris.common.notifier.domain.EmailMessage;
import com.wzkris.common.notifier.domain.NotificationResult;
import com.wzkris.common.notifier.enums.EmailTemplateKeyEnum;
import com.wzkris.common.notifier.manager.NotifierManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MailService {

  private final NotifierManager notifierManager;

  public NotificationResult sendMail() {
    EmailMessage message = EmailMessage.builder()
            .templateKey(EmailTemplateKeyEnum.PLAINTEXT) // 或 HTML
            .recipients(java.util.List.of("to1@example.com", "to2@example.com"))
            .subject("主题")
            .content("正文内容")
            .fromEmail("no-reply@example.com")
            .fromName("系统通知")
            .build();

    return notifierManager.send(message);
  }

}
```

### 3) 异步/重试（在业务层实现）

模块不内置异步与重试，建议在业务层结合 `@Async`、重试框架或消息队列实现。

## 扩展新的通知渠道

1) 定义实现类（示例：企业微信）

```java
import com.wzkris.common.notifier.api.Notifier;
import com.wzkris.common.notifier.domain.NotificationResult;
import com.wzkris.common.notifier.enums.NotificationChannelEnum;
import org.springframework.stereotype.Component;

@Component
public class WechatWorkNotifier implements Notifier<YourWechatMessage> {

  @Override
  public NotificationResult send(YourWechatMessage message) {
    // TODO: 调用企业微信发送
    return NotificationResult.success("message-id");
  }

  @Override
  public NotificationChannelEnum getChannel() {
    return NotificationChannelEnum.WECHAT_WORK;
  }

}
```

2) 声明为 Spring Bean 后，`NotifierManager` 将自动发现并按渠道路由。

## 重要说明与注意事项

- **装配条件**：
  - 钉钉通知器需 `dingtalk.enabled=true` 且提供 `appKey/appSecret/robotCode`
  - 邮件通知器需存在 `JavaMailSender` Bean（通常由 `spring-boot-starter-mail` 提供）
- **类型安全**：`Notifier<T>` 为泛型，不同渠道使用各自的消息模型（如 `DingtalkMessage`、`EmailMessage`）
- **异常与结果**：统一返回 `NotificationResult`，包含是否成功、消息ID与错误信息
- **多实例路由**：当前钉钉配置为单应用参数集；如需多应用能力，可在业务侧维护多套 `NotifierManager` 或扩展属性结构

## 组件概览

- `Notifier<T>`：通知器接口
- `NotifierManager`：通知路由管理器，提供 `send(...)` 重载
- `DingtalkNotifier` / `EmailNotifier`：内置渠道实现
- `NotificationResult`：发送结果模型
- `DingtalkMessage` / `EmailMessage`：渠道消息模型

## 配置字段（钉钉）

| 配置项 | 说明 | 是否必填 |
|---|---|---|
| `dingtalk.enabled` | 是否启用钉钉通知 | 否（默认不装配） |
| `dingtalk.appKey` | 应用 Key | 是 |
| `dingtalk.appSecret` | 应用 Secret | 是 |
| `dingtalk.robotCode` | 机器人 Code | 是 |
| `dingtalk.accessTokenUrl` | 自定义 AccessToken 地址 | 否 |
| `dingtalk.robotWebhook` | 自定义机器人 Webhook | 否 |


