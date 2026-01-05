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

支持配置多个webhook，对应不同的群聊，可以发送不同消息到不同的群聊：

```yaml
notifier:
  enabled: true
  channel: DINGTALK
  dingtalk:
    webhooks:
      default: https://oapi.dingtalk.com/robot/send?access_token=xxx  # 默认群聊
      alarm: https://oapi.dingtalk.com/robot/send?access_token=yyy   # 告警群聊
      business: https://oapi.dingtalk.com/robot/send?access_token=zzz # 业务群聊
    defaultWebhookKey: default  # 默认webhook标识（可选，不配置则使用第一个）
    templateKey: MARKDOWN
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
notifier:
  enabled: true  # 启用错误日志通知
  channel: DINGTALK  # 指定发送渠道（必须显式配置，如：DINGTALK, EMAIL）
  dingtalk:      # 钉钉通知配置
    webhooks:    # Webhook配置
      default: https://oapi.dingtalk.com/robot/send?access_token=xxx
    templateKey: MARKDOWN  # 消息模板类型（TEXT 或 MARKDOWN，默认 MARKDOWN）
  email:         # 邮件通知配置
    recipients:  # 接收人列表（邮箱地址）
      - "admin@example.com"
      - "dev@example.com"
    fromEmail: "no-reply@example.com"      # 发件人邮箱
    fromName: "系统通知"                     # 发件人名称
    templateKey: PLAINTEXT                  # 邮件模板类型（PLAINTEXT 或 HTML，默认 PLAINTEXT）
```

**工作原理**：
- 自动创建自定义 Logback Appender (`ErrorLogEventAppender`)
- 捕获所有 ERROR 级别的日志
- 发布 `ErrorLogEvent` Spring 事件
- `ErrorLogNotifierListener` 监听事件并根据配置的渠道发送通知

**配置说明**：
- `enabled`: 是否启用错误日志通知（默认 false）
- `channel`: 发送渠道，必须显式配置，支持 `DINGTALK`、`EMAIL` 等
- `dingtalk.recipients`: 钉钉接收人列表（使用钉钉渠道时必填）
- `dingtalk.templateKey`: 钉钉消息模板类型，支持 `TEXT`、`MARKDOWN`（默认 `MARKDOWN`）
- `email.recipients`: 邮件接收人列表（使用邮件渠道时必填）
- `email.subject`: 邮件主题，支持占位符替换（默认 "系统错误通知"）
- `email.fromEmail`: 发件人邮箱（可选，建议配置）
- `email.fromName`: 发件人名称（默认 "系统通知"）
- `email.templateKey`: 邮件模板类型，支持 `PLAINTEXT`、`HTML`（默认 `PLAINTEXT`）

**注意事项**：
- 必须配置 `enabled: true` 才会启用错误日志通知
- 必须显式配置 `channel` 指定发送渠道
- 根据配置的渠道，必须配置对应的接收人（`recipients`）
- 通知发送失败不会影响主流程，仅记录警告日志

## 使用方式

> 推荐通过 `NotifierManager` 发送，按消息类型自动路由到对应渠道。

### 1) 发送钉钉消息

**发送到默认webhook（不指定webhookKey）**

```java
import com.wzkris.common.notifier.domain.DingtalkMessage;
import com.wzkris.common.notifier.domain.NotificationResult;
import com.wzkris.common.notifier.enums.DingtalkTemplateKeyEnum;
import com.wzkris.common.notifier.core.NotifierManager;
import com.wzkris.common.notifier.enums.NotificationChannelEnum;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NoticeService {

  private final NotifierManager notifierManager;

  public NotificationResult sendToDingtalk() {
    DingtalkMessage message = DingtalkMessage.builder()
            .templateKey(DingtalkTemplateKeyEnum.MARKDOWN)
            .templateParams(java.util.Map.of(
                    "title", "系统通知",
                    "text", "这是一个 Markdown 模板内容"
            ))
            .build();

    return notifierManager.send(NotificationChannelEnum.DINGTALK, message);
  }

}
```

**发送到指定webhook（多webhook场景）**

```java
  // 发送到告警群聊
  public NotificationResult sendToAlarmGroup() {
    DingtalkMessage message = DingtalkMessage.builder()
            .templateKey(DingtalkTemplateKeyEnum.MARKDOWN)
            .webhookKey("alarm")  // 指定webhook标识
            .templateParams(java.util.Map.of(
                    "title", "🚨 系统告警",
                    "text", "系统出现异常，请及时处理"
            ))
            .build();

    return notifierManager.send(NotificationChannelEnum.DINGTALK, message);
  }

  // 发送到业务群聊
  public NotificationResult sendToBusinessGroup() {
    DingtalkMessage message = DingtalkMessage.builder()
            .templateKey(DingtalkTemplateKeyEnum.MARKDOWN)
            .webhookKey("business")  // 指定webhook标识
            .templateParams(java.util.Map.of(
                    "title", "业务通知",
                    "text", "业务处理完成"
            ))
            .build();

    return notifierManager.send(NotificationChannelEnum.DINGTALK, message);
  }
```

### 2) 发送邮件

```java
import com.wzkris.common.notifier.domain.EmailMessage;
import com.wzkris.common.notifier.domain.NotificationResult;
import com.wzkris.common.notifier.enums.EmailTemplateKeyEnum;
import com.wzkris.common.notifier.core.NotifierManager;
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
import com.wzkris.common.notifier.core.Notifier;
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
- **多webhook支持**：钉钉通知器支持配置多个webhook，可以通过 `DingtalkMessage.webhookKey()` 指定发送到不同的群聊

## 组件概览

- `Notifier<T>`：通知器接口
- `NotifierManager`：通知路由管理器，提供 `send(...)` 重载
- `DingtalkNotifier` / `EmailNotifier`：内置渠道实现
- `NotificationResult`：发送结果模型
- `DingtalkMessage` / `EmailMessage`：渠道消息模型

## 配置字段（钉钉）

| 配置项 | 说明 | 是否必填 |
|---|---|---|
| `notifier.dingtalk.webhooks` | 多个机器人 Webhook 配置（Map结构，key为webhook标识，value为webhook URL） | 是 |
| `notifier.dingtalk.defaultWebhookKey` | 默认webhook标识（当消息未指定webhookKey时使用） | 否（不配置则使用webhooks中的第一个） |
| `notifier.dingtalk.templateKey` | 消息模板类型（TEXT 或 MARKDOWN） | 否（默认TEXT） |

**注意**：
- `webhooks` 必须配置，至少包含一个webhook
- 使用多webhook时，可以通过 `DingtalkMessage.webhookKey()` 指定发送到哪个webhook
- 也可以通过 `DingtalkMessage.webhookUrl()` 直接指定webhook URL发送消息


