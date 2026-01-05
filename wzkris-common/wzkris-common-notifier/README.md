# Notifier 通知模块

统一的通知发送模块，提供统一接口，支持多种通知渠道（钉钉、邮件，后续可扩展）。

## 功能特性

- ✅ **统一接口**：标准化 `Notifier<T>` 接口，业务侧零侵入接入
- ✅ **多渠道支持**：内置钉钉、邮件；企业微信/短信/Webhook 可扩展
- ✅ **自动管理**：`NotifierManager` 自动收集并路由到对应渠道
- ✅ **Spring Boot 自动配置**：上电即用（基于 `@AutoConfiguration`）
- ✅ **易扩展**：新增渠道仅需实现接口并声明为 Bean
- ✅ **错误日志自动通知**：自动捕获 ERROR 级别日志并发送通知（可选）
- ✅ **统一上下文**：通过 `NotificationContext` 统一消息构建，简化使用

## 快速开始

### 1) 引入模块

本仓内模块，业务服务引入 `wzkris-common-notifier` 即可（无需额外配置文件）。

### 2) 配置钉钉

开启并配置钉钉参数（必须开启 `notifier.enabled=true` 且 `notifier.channel=DINGTALK` 才会装配钉钉通知器）：

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
    templateKey: MARKDOWN  # 消息模板类型：TEXT、MARKDOWN、LINK、ACTION_CARD（默认 TEXT）
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

notifier:
  enabled: true
  channel: EMAIL
  email:
    recipients:  # 接收人列表（邮箱地址）
      - "admin@example.com"
      - "dev@example.com"
    fromEmail: "no-reply@example.com"      # 发件人邮箱（可选）
    fromName: "系统通知"                     # 发件人名称（默认 "系统异常通知"）
    templateKey: PLAINTEXT                  # 邮件模板类型：PLAINTEXT 或 HTML（默认 PLAINTEXT）
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
    templateKey: MARKDOWN  # 消息模板类型（TEXT、MARKDOWN、LINK、ACTION_CARD，默认 TEXT）
  email:         # 邮件通知配置（使用邮件渠道时配置）
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
- `ErrorNotifierListener` 监听事件并根据配置的渠道发送通知

**配置说明**：
- `enabled`: 是否启用错误日志通知（默认 false）
- `channel`: 发送渠道，必须显式配置，支持 `DINGTALK`、`EMAIL` 等
- `dingtalk.webhooks`: 钉钉 Webhook 配置（使用钉钉渠道时必填）
- `dingtalk.templateKey`: 钉钉消息模板类型，支持 `TEXT`、`MARKDOWN`、`LINK`、`ACTION_CARD`（默认 `TEXT`）
- `email.recipients`: 邮件接收人列表（使用邮件渠道时必填）
- `email.fromEmail`: 发件人邮箱（可选，建议配置）
- `email.fromName`: 发件人名称（默认 "系统异常通知"）
- `email.templateKey`: 邮件模板类型，支持 `PLAINTEXT`、`HTML`（默认 `PLAINTEXT`）

**注意事项**：
- 必须配置 `enabled: true` 才会启用错误日志通知
- 必须显式配置 `channel` 指定发送渠道
- 根据配置的渠道，必须配置对应的参数（钉钉需要 `webhooks`，邮件需要 `recipients`）
- 通知发送失败不会影响主流程，仅记录警告日志

## 使用方式

> **推荐使用方式**：通过 `NotifierManager.send(NotificationContext)` 方法发送，系统会根据配置的渠道自动选择通知器并构建消息。

### 1) 推荐方式：使用 NotificationContext（统一上下文）

这是最便捷的使用方式，只需要构建 `NotificationContext`，系统会根据配置自动选择渠道并构建对应的消息：

```java
import com.wzkris.common.notifier.core.NotificationContext;
import com.wzkris.common.notifier.core.NotificationResult;
import com.wzkris.common.notifier.core.NotifierManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NoticeService {

  private final NotifierManager notifierManager;

  // 发送钉钉消息（必须指定webhookKey）
  public NotificationResult sendToDingtalk() {
    NotificationContext context = NotificationContext.builder()
            .title("系统通知")
            .content("这是一个 Markdown 格式的通知内容")
            .webhookKey("default")  // 必填：指定webhook标识，必须在配置的webhooks中存在
            .build();

    return notifierManager.send(context);
  }

  // 发送到指定webhook（多webhook场景）
  public NotificationResult sendToAlarmGroup() {
    NotificationContext context = NotificationContext.builder()
            .title("🚨 系统告警")
            .content("系统出现异常，请及时处理")
            .webhookKey("alarm")  // 指定webhook标识
            .build();

    return notifierManager.send(context);
  }

  // 发送邮件
  public NotificationResult sendEmail() {
    NotificationContext context = NotificationContext.builder()
            .title("系统通知")
            .content("这是一封系统通知邮件")
            .recipients(java.util.List.of("admin@example.com", "dev@example.com"))  // 可选，不指定则使用配置的recipients
            .build();

    return notifierManager.send(context);
  }

  // 使用扩展字段传递额外参数（如钉钉 LINK 或 ACTION_CARD 模板）
  public NotificationResult sendLinkMessage() {
    NotificationContext context = NotificationContext.builder()
            .title("链接消息")
            .content("点击查看详情")
            .webhookKey("default")
            .extras(java.util.Map.of(
                    "picUrl", "https://example.com/image.png",
                    "messageUrl", "https://example.com/detail"
            ))
            .build();

    return notifierManager.send(context);
  }
}
```

### 2) 直接使用渠道消息对象（高级用法）

如果需要更精细的控制，可以直接构建渠道特定的消息对象：

**发送钉钉消息**

```java
import com.wzkris.common.notifier.domain.DingtalkMessage;
import com.wzkris.common.notifier.core.NotificationResult;
import com.wzkris.common.notifier.enums.DingtalkTemplateKeyEnum;
import com.wzkris.common.notifier.core.impl.DingtalkNotifier;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DingtalkService {

  private final DingtalkNotifier dingtalkNotifier;

  public NotificationResult sendToDingtalk() {
    DingtalkMessage message = DingtalkMessage.builder()
            .templateKey(DingtalkTemplateKeyEnum.MARKDOWN)
            .webhookKey("default")  // 必填：指定webhook标识
            .templateParams(java.util.Map.of(
                    "title", "系统通知",
                    "text", "这是一个 Markdown 模板内容"
            ))
            .build();

    return dingtalkNotifier.send(message);
  }

  // 发送 LINK 类型消息
  public NotificationResult sendLinkMessage() {
    DingtalkMessage message = DingtalkMessage.builder()
            .templateKey(DingtalkTemplateKeyEnum.LINK)
            .webhookKey("default")
            .templateParams(java.util.Map.of(
                    "title", "链接消息",
                    "text", "点击查看详情",
                    "picUrl", "https://example.com/image.png",
                    "messageUrl", "https://example.com/detail"
            ))
            .build();

    return dingtalkNotifier.send(message);
  }
}
```

**发送邮件**

```java
import com.wzkris.common.notifier.core.NotificationResult;
import com.wzkris.common.notifier.domain.EmailMessage;
import com.wzkris.common.notifier.enums.EmailTemplateKeyEnum;
import com.wzkris.common.notifier.core.impl.EmailNotifier;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MailService {

  private final EmailNotifier emailNotifier;

  public NotificationResult sendMail() {
    EmailMessage message = EmailMessage.builder()
            .templateKey(EmailTemplateKeyEnum.PLAINTEXT) // 或 HTML
            .recipients(java.util.List.of("to1@example.com", "to2@example.com"))
            .subject("主题")
            .content("正文内容")
            .fromEmail("no-reply@example.com")
            .fromName("系统通知")
            .build();

    return emailNotifier.send(message);
  }
}
```

### 3) 异步/重试（在业务层实现）

模块不内置异步与重试，建议在业务层结合 `@Async`、重试框架或消息队列实现：

```java
import org.springframework.scheduling.annotation.Async;
import org.springframework.retry.annotation.Retryable;

public class Test {

    @Async
    @Retryable(value = Exception.class, maxAttempts = 3)
    public NotificationResult sendAsync(NotificationContext context) {
        return notifierManager.send(context);
    }

}
```

## 扩展新的通知渠道

### 1) 定义消息模型

```java
package com.wzkris.common.notifier.domain;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class WechatWorkMessage {
    private String content;
    private String chatId;
    // ... 其他字段
}
```

### 2) 实现 Notifier 接口

```java
package com.wzkris.common.notifier.core.impl;

import com.wzkris.common.notifier.core.NotificationContext;
import com.wzkris.common.notifier.core.NotificationResult;
import com.wzkris.common.notifier.core.Notifier;
import com.wzkris.common.notifier.domain.WechatWorkMessage;
import com.wzkris.common.notifier.enums.NotificationChannelEnum;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

@Component
public class WechatWorkNotifier implements Notifier<WechatWorkMessage> {

  @Override
  public NotificationResult send(WechatWorkMessage message) {
    // TODO: 调用企业微信 API 发送消息
    return NotificationResult.success("message-id");
  }

  @Override
  public NotificationChannelEnum getChannel() {
    return NotificationChannelEnum.WECHAT_WORK;
  }

  @Override
  public WechatWorkMessage buildMessage(NotificationContext context) {
    Assert.notNull(context, "通知上下文不能为空");
    
    return WechatWorkMessage.builder()
            .content(context.getContent())
            .chatId((String) context.getExtras().get("chatId"))
            .build();
  }
}
```

### 3) 声明为 Spring Bean

实现类使用 `@Component` 或 `@Service` 注解后，`NotifierManager` 将自动发现并按渠道路由。

## 重要说明与注意事项

- **装配条件**：
  - 钉钉通知器：需 `notifier.enabled=true` 且 `notifier.channel=DINGTALK`，并配置 `notifier.dingtalk.webhooks`
  - 邮件通知器：需存在 `JavaMailSender` Bean（通常由 `spring-boot-starter-mail` 提供），并配置 `notifier.email.recipients`
- **类型安全**：`Notifier<T>` 为泛型，不同渠道使用各自的消息模型（如 `DingtalkMessage`、`EmailMessage`）
- **异常与结果**：统一返回 `NotificationResult`，包含是否成功、消息ID与错误信息
- **多webhook支持**：钉钉通知器支持配置多个webhook，可以通过 `NotificationContext.webhookKey` 或 `DingtalkMessage.webhookKey` 指定发送到不同的群聊
- **推荐使用方式**：优先使用 `NotifierManager.send(NotificationContext)` 方法，系统会根据配置自动选择渠道并构建消息

## 组件概览

- `Notifier<T>`：通知器接口，定义了 `send(T message)`、`getChannel()` 和 `buildMessage(NotificationContext context)` 方法
- `NotifierManager`：通知路由管理器，提供 `send(NotificationContext context)` 方法，根据配置的渠道自动选择通知器
- `NotificationContext`：通用通知上下文，包含 title、content、recipients、webhookKey、extras 等字段
- `NotificationResult`：发送结果模型，包含 success、messageId、errorMessage、data
- `DingtalkNotifier` / `EmailNotifier`：内置渠道实现
- `DingtalkMessage` / `EmailMessage`：渠道消息模型

## 配置字段说明

### 钉钉配置

| 配置项 | 说明 | 是否必填 | 默认值 |
|---|---|---|---|
| `notifier.enabled` | 是否启用通知模块 | 是 | false |
| `notifier.channel` | 通知渠道（使用钉钉时设置为 DINGTALK） | 是 | - |
| `notifier.dingtalk.webhooks` | 多个机器人 Webhook 配置（Map结构，key为webhook标识，value为webhook URL） | 是 | - |
| `notifier.dingtalk.templateKey` | 消息模板类型（TEXT、MARKDOWN、LINK、ACTION_CARD） | 否 | TEXT |

**注意**：
- `webhooks` 必须配置，至少包含一个webhook
- 使用 `NotificationContext` 方式发送钉钉消息时，**必须设置 `webhookKey`**，且该值必须在配置的 `webhooks` 中存在
- 使用 `DingtalkMessage` 方式发送时，`webhookKey` 也是必填的
- 钉钉消息模板类型说明：
  - `TEXT`：文本消息，需要 `content` 参数
  - `MARKDOWN`：Markdown 消息，需要 `title` 和 `text` 参数
  - `LINK`：链接消息，需要 `title`、`text`、`picUrl`、`messageUrl` 参数
  - `ACTION_CARD`：卡片消息，需要 `title`、`text`、`singleTitle`、`singleURL` 参数

### 邮件配置

| 配置项 | 说明 | 是否必填 | 默认值 |
|---|---|---|---|
| `notifier.enabled` | 是否启用通知模块 | 是 | false |
| `notifier.channel` | 通知渠道（使用邮件时设置为 EMAIL） | 是 | - |
| `notifier.email.recipients` | 接收人列表（邮箱地址） | 是 | - |
| `notifier.email.fromEmail` | 发件人邮箱 | 否 | - |
| `notifier.email.fromName` | 发件人名称 | 否 | 系统异常通知 |
| `notifier.email.templateKey` | 邮件模板类型（PLAINTEXT 或 HTML） | 否 | PLAINTEXT |

**注意**：
- 邮件通知器需要配置 Spring Boot 邮件相关配置（`spring.mail.*`）
- `recipients` 必须配置，至少包含一个接收人
- 可以通过 `NotificationContext.recipients` 动态指定接收人，会覆盖配置的接收人列表


