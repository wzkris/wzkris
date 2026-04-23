---
name: code-comment-conventions
description: "Use when: generating or reviewing code comments in wzkris. Enforces concise, clear comments that avoid verbosity. Comments should explain 'why', not repeat 'what'. Supports JavaDoc, inline comments, and block comments."
applyTo: "**/*.java"
---

# wzkris 代码注释规范

## 核心原则

**方法内若无必要就不要注释。** 注释解释"**为什么**"，不重复"**是什么**"。

## 1. 注释类型速查

| **JavaDoc** | ✅ 必需 | public/protected 方法和类必须有 |
| **方法内注释** | ⚠️ 极少 | 仅业务复杂、非直观时；默认无注释 |
| **块注释** | ⚠️ 偶尔 | 解释复杂代码块逻辑 |

## 2. JavaDoc 格式速查

### 方法
```java
/**
 * 简短描述（一句话）。
 *
 * @param 参数名 简短说明
 * @return 返回值说明
 * @throws 异常类型 异常说明（仅Service层）
 */
public Result<?> generateQrCode(String userId) { ... }
```

### 类
```java
/**
 * 简短描述。
 *
 * 可选：一句话补充说明职责。
 */
@Service
public class QrLoginServiceImpl { ... }
```

## 3. 方法内注释判断

### ❌ 不要注释（代码已经清晰）
```java
// 获取活跃用户列表
List<User> activeUsers = userService.listActive();

// 遍历用户
for (User user : activeUsers) {
    // 发送邮件
    sendEmail(user);
}
```

### ✅ 推荐做法（代码自解释）
```java
List<User> activeUsers = userService.listActive();

for (User user : activeUsers) {
    sendEmail(user);
}
```

## 4. 何时需要方法内注释

| 场景 | 示例 |
|---|---|
| **业务约定** | `// 与前端约定：二维码5分钟有效期` |
| **非直观算法** | `// 优先级 = 活跃度 * 0.6 + 等级 * 0.4` |
| **重要警告** | `// 此方法非线程安全，必须在synchronized块中调用` |

## 5. 用代码替代注释

**❌ 需要注释才能理解**
```java
if (user.getQuotaUsage() > user.getQuotaLimit() * 0.9) {
    notifyUser(user);
}
```

**✅ 代码自解释**
```java
if (isQuotaAlmostFull(user)) {
    notifyUser(user);
}

private boolean isQuotaAlmostFull(User user) {
    return user.getQuotaUsage() > user.getQuotaLimit() * 0.9;
}
```

## 6. 避免的注释

❌ 重复代码逻辑的注释  
❌ 过时注释  
❌ 调试语句注释（应删除或用logger）  
❌ 个人主观评价  

## 7. 其他规范

- **中文注释** - 项目推荐使用中文（标识符为英文，注释为中文）
- **删除注释代码** - 用Git history查看，不留死代码注释
- **TODO/FIXME** - `// TODO: 内容描述 (YYYY-MM-DD)` 或 `// FIXME: 问题描述`

## 8. 检查清单

生成或审查代码时检查注释：

- [ ] **必要性** - 注释是否真正必要（方法内默认无注释）
- [ ] **代码表达** - 是否可以通过更清晰的代码变量名/方法名来代替注释
- [ ] **简洁性** - 注释是否简洁（通常一行足够）
- [ ] **清晰性** - 注释是否清晰易懂
- [ ] **"为什么"** - 注释是否在解释"为什么"而不是"是什么"
- [ ] **准确性** - 注释是否与代码实际逻辑一致
- [ ] **JavaDoc** - public 方法是否有 JavaDoc（必须）
- [ ] **冗余性** - 是否有重复的注释
- [ ] **过时性** - 是否有过时的注释
- [ ] **风格** - 注释风格是否与项目一致

---

**相关文档**: [Java 分层编码规范](java-layer-conventions.instructions.md)
