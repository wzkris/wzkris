# wzkris 当前代码架构说明

## 1. 文档范围与判定依据

本文描述仓库 `wzkris` 在当前工作区状态下的代码架构，包含：

- 构建与模块拓扑
- 跨模块依赖方向
- 业务分层与职责边界
- `user-center` 核心业务域的现状

判定依据为当前仓库文件结构、`pom.xml` 依赖声明、以及对应实现代码，不引用历史分支信息。

## 2. 全局模块拓扑

根聚合工程 `wzkris` 采用 Maven 多模块结构，`packaging = pom`，统一管理版本、插件和依赖基线。

顶层模块如下：

- 基础能力：`wzkris-common`
- 认证域：`wzkris-auth`
- 验证码域：`wzkris-captcha`
- 网关：`wzkris-gateway`
- 业务域聚合：`wzkris-modules`
- 扩展服务：`wzkris-extends`
- 示例工程：`wzkris-demo/*`
- 依赖管理：`wzkris-bom`

其中，`wzkris-modules` 当前聚合一个业务域：

- `wzkris-user-center`（原 `wzkris-system` 已整体迁入）

## 3. 统一技术基线

全局技术基线由根 `pom.xml` 定义：

- Java 版本：`21`
- Spring Boot：`3.5.0`
- Spring Cloud：`2025.0.0`
- Spring Cloud Alibaba：`2025.0.0.0`
- MapStruct Plus：`1.4.6`
- 代码校验：`maven-checkstyle-plugin`（validate 阶段执行）

该基线意味着：

- 任何子模块的完整编译均受 JDK 21 约束
- 静态风格检查在构建早期即生效

## 4. 领域内"双子模块"组织模式

`wzkris-user-center` 采用 `api + biz` 双模块组织：

- `*-api`：契约层（请求/响应模型、远程契约、事件、枚举、配置属性）
- `*-biz`：运行层（Controller、Service、Mapper、Domain、启动类）

依赖方向为单向：

- `*-biz` 依赖 `*-api`
- `*-api` 不依赖 `*-biz`

该方向在 Maven 依赖声明上已固化，保证编译期边界可验证。

## 5. 分层职责约束（当前已形成的事实）

### 5.1 `*-api` 职责

- 提供入参与出参类型（`request/*`、`response/*`）
- 提供跨服务远程接口与模型（`remote/*`）
- 不承载数据库实体与持久化行为

### 5.2 `*-biz` 职责

- `controller/*`：HTTP 接入与权限注解
- `service/*`：领域行为与业务规则
- `mapper/*`：持久化访问
- `domain/*`：DO 实体（数据库映射）
- `impl/*`：API 实现层

## 6. user-center 架构现状

`wzkris-user-center` 当前呈现出较清晰的"契约实现分离"形态：

- `wzkris-user-center-api` 提供 `api/*` 接口与 `response/*`
- `wzkris-user-center-biz` 中 `impl/*` 实现 `api/*` 接口
- `controller/*` 依赖 `api/*` 接口而非直接编排 Mapper/Service
- 外部返回模型已从 DO 收敛为 Response

该模式的核心价值：

- 外部契约稳定且可独立演进
- `biz` 内部 DO 可变更而不直接破坏外部调用方
- 分层关系在代码与依赖层面保持一致

原 `wzkris-system` 模块（通知/公告/字典/配置/登录日志/操作日志）已整体迁入 `user-center`，统一包名为 `com.wzkris.usercenter`，遵循相同的分层约束。

## 7. 数据模型分层规范（当前可执行规则）

为保持一致性，当前代码已经体现并应继续遵循以下规则：

- DO 仅位于 `*-biz/domain`
- Response 仅位于 `*-api/response`
- Controller 对外返回类型使用 Response，不返回 DO
- 领域内部转换通过 `BeanUtil.convert(...)` 等映射工具执行

## 8. 架构收益与风险面

### 8.1 收益

- 降低跨模块耦合（api 不依赖 biz）
- 外部契约语义更明确（Response 命名直观）
- 实体变更的外部影响可控

### 8.2 风险面

- 后续新增业务能力需持续遵循"Controller 仅依赖 API"的约束，避免回流到直连 Service/Mapper
- `api` 层规模增长后需控制接口颗粒度，避免接口过度膨胀

## 9. 结论

当前仓库总体架构可定义为：

- **全局层面**：标准 Maven 多模块单向依赖架构
- **业务层面**：以 `api + biz` 为基本组织单元
- **落地成熟度**：`user-center` 已实现高一致性的同构分层

该状态表明核心业务域已完成分层收敛，具备一致的扩展与演进路径。

## 10. 其他模块与 user-center 架构一致性判定

针对 `wzkris-modules` 之外的模块，当前代码结构与 `user-center` 不同，但这属于职责差异而非架构偏差：

- `wzkris-auth`、`wzkris-captcha`、`wzkris-gateway`、`wzkris-extends` 主要承担认证、验证码、网关、扩展能力，不是"业务域双子模块"，不以 `api + biz` 为目标形态
- `wzkris-common`、`wzkris-bom` 属于基础与依赖管理层，不承载业务域 Controller/Impl 调用链
