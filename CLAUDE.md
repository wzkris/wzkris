# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

**wzkris-cloud** is an enterprise multi-tenant microservice platform on Spring Boot 3.5.0 / Spring Cloud 2025.0.0 / Spring Cloud Alibaba 2025.0.0.0, **JDK 21**, PostgreSQL (schema `biz`), Redis/Redisson, Nacos. OAuth2.1 auth via Spring Authorization Server. The `README.md` is stale (claims JDK 17 / Spring Boot 3.4.4 / gateway 8080) — trust `pom.xml` and this file over the README.

## Build & Run

**JDK 21 is mandatory.** The system default `JAVA_HOME` may point to JDK 1.8, which fails with `无效的目标发行版: 21`. Always set JDK 21 first:

```bash
export JAVA_HOME="C:/Program Files/Java/jdk-21.0.8"
```

```bash
# Full build (skip tests)
mvn clean package -DskipTests

# Build one biz module WITH its api + upstream dependencies (use -am, not -rf)
mvn -pl wzkris-modules/wzkris-user-center/wzkris-user-center-biz -am clean package -DskipTests

# Run a service
mvn -pl wzkris-modules/wzkris-user-center/wzkris-user-center-biz spring-boot:run

# Tests (surefire). Single test:
mvn -pl wzkris-common/wzkris-common-redis test -Dtest=IdempotentAspectTest
```

Maven profiles: `dev` (default, active by default) and `prod`, selected via `@profiles.active@` resource filtering. Versioning uses `${revision}` (4.1.0) flattened by `flatten-maven-plugin`.

**No checkstyle/spotless plugin is bound to the build** — `codestyle/checkstyle.xml` and the `palantirJavaFormat` version exist only as properties. Formatting is enforced by IDE, not `mvn`. Follow palantir style (4-space indent, the existing code is the reference).

## Service Ports & External Dependencies

| Service | Port | Module |
|---|---|---|
| Gateway | 10001 | `wzkris-gateway` (Spring Cloud Gateway MVC) |
| Auth | 9000 | `wzkris-auth` |
| User-Center | 8000 | `wzkris-modules/wzkris-user-center` |
| Captcha | 9300 | `wzkris-captcha` |
| Payment | 8001 | `wzkris-modules/wzkris-payment` |

Requires running **Nacos** (8848, dev namespace `application-dev`, groups `APPLICATION_GROUP` / `COMMON_GROUP`), **PostgreSQL** (schema `biz`), **Redis**. Per-service config lives in Nacos (`common.yml`, `redis.yml` in `COMMON_GROUP`; `<service>.yml` in `APPLICATION_GROUP`), imported via `spring.config.import: optional:nacos:...`. Local `application.yml` only sets port/context-path/name; `application-dev.yml` sets Nacos addresses.

## Module Topology

```
wzkris (root pom, packaging=pom)
├── wzkris-bom              # dependencyManagement only (all com.wzkris versions)
├── wzkris-common           # 15 reusable libs (core/orm/redis/security/web/log/remote/...)
├── wzkris-auth             # OAuth2.1 server (api + biz)
├── wzkris-gateway          # API gateway (api + biz)
├── wzkris-captcha          # captcha service
├── wzkris-modules          # business domains aggregator
│   ├── wzkris-user-center  # api + biz (system mgmt: user/role/menu/dept/dict/config/log)
│   └── wzkris-payment      # api + biz (payment gateway)
├── wzkris-extends          # monitor-admin etc.
└── wzkris-demo             # examples (oauth2-client, mq, pg-bus)
```

### The `api + biz` dual-module pattern (business domains)

Every business domain under `wzkris-modules` splits into two modules with a **one-way dependency**: `*-biz` depends on `*-api`, never the reverse.

- **`*-api`** (contract layer): request/response models, remote-call interfaces, events, enums, config properties. Depends only on `common-swagger/validator/remote/orm/excel`. No DOs, no DB access.
- **`*-biz`** (runtime layer): `UserCenterApplication` startup, controllers, `impl/*` (ApiImpl), `service/*`, `mapper/*`, `domain/*` (DOs). Depends on nacos/sentinel/web/log/security/redis + its own `*-api`.

**Call chain (the canonical flow — do not bypass it):**
```
Controller (@CheckPerms, @OperateLog, returns Result<Page<Response>>)
  → Api interface (*-api)              // contract
  → ApiImpl (@Service, extends AbstractApi, implements Api)  // orchestration: validation, permission check, DO<->Response conversion
  → Service (IServicePlus<T>)          // domain logic
  → Mapper (BaseMapperPlus<T>)         // persistence, @Select text blocks
  → DO (extends BaseEntity)            // @TableName(schema="biz")
```

Controllers depend on the `Api` interface, **not** directly on Service/Mapper. External responses use `Response` types from `*-api`, never DOs. Convert with `BeanCopierUtil` (cglib, **not** MapStruct — enum fields are NOT auto-copied and must be `set` manually).

## Common Layer Conventions

### Persistence (`wzkris-common-orm`)
- **`BaseEntity`**: audit fields `createAt`/`creatorId`/`updateAt`/`updaterId`/`hint` (auto-filled by `BaseFieldFillHandler`) + `deleted` (`@TableLogic`). **No `version`, no `tenant_id` in base.** Tenant isolation is interceptor-injected per-table, not a base column.
- **`BaseMapperPlus<T>`** / **`IServicePlus<T>`**: extend MyBatis-Plus `BaseMapper`/`IService` with `selectOneByObj`, `selectById2VO`, `insertAndGet`, `getOneByObj`, etc. Always extend these, not the raw MP types.
- **Snowflake IDs**: `DefaultIdentifierGenerator` bound to local host IP (cluster-safe). `@TableId private Long xxxId`.
- **`@TableName(schema = "biz", value = "table_name", autoResultMap = true)`** on every DO.
- **No `mapper.xml` files** — custom SQL uses `@Select`/`@Update` with Java text blocks and `${ew.customSqlSegment}` for wrappers. Multi-statement SQL uses `<script>` + `<foreach>`.
- **`PagingRequest`**: base class for page requests; call `request.buildPage()` → MP `Page<T>`. Return `Page.of(iPage)` or `Page.of(iPage, voList)` (NOT raw `IPage`).
- **Partial unique indexes** on PostgreSQL: `UNIQUE (...) WHERE deleted = false` — soft-deleted rows don't collide.
- **`@DataScope`/`@DataPermission`**: row-level data permission on mapper methods (dept-scope filtering).

### Multi-tenancy
- `TenantProperties` (`tenant.includes` set in Nacos `common.yml`) is a **whitelist of table names** that get a `tenant_id` column auto-injected by `TenantLineInnerInterceptor`. Tables NOT in the set are platform-global (no tenant filtering). Platform-level services (e.g. payment) simply omit their tables from the whitelist.
- `AuthTypeEnum`: `ADMIN` / `TENANT` / `CUSTOMER` / `CLIENT` — multi-account-system login types.

### Security (`wzkris-common-security`)
- **`@CheckPerms(checkTypes = AuthTypeEnum.ADMIN, prefix = "user-mod:role-mng:", value = "page")`** on controller methods. `value` can be an array; `mode = CheckMode.OR|AND`. Public/callback endpoints omit the annotation (auth handled elsewhere).
- **`SecurityUtil`**: `getLoginUser()`, `getUid()`, `getAuthType()`, `isSuperUser()`, `getHint()`.
- Gateway authenticates JWT; downstream services are resource servers reading the security context propagated by the gateway.

### Cross-cutting annotations
- **`@OperateLog(title, subTitle, type = OperateTypeEnum.INSERT)`** (`wzkris-common-log`) — audit logging on mutating endpoints.
- **`@Idempotent(key = "SpEL", ttlSeconds = 15)`** (`wzkris-common-redis`) — Redis `setIfAbsent` idempotency.
- **`DistLockTemplate.lockAndExecute(lockKey, Supplier<T>)`** — static Redisson distributed lock. Has `Runnable`/`Supplier`/`ThrowableSupplier` overloads; a bare lambda is ambiguous → cast to `(Supplier<T>) () -> ...`.
- **`@RemoteInterface(serviceId = "...", path = "...")`** (`wzkris-common-remote`) — declarative HTTP clients via Spring 6 `HttpServiceProxyFactory` + `RestClient`. **NOT OpenFeign.** Enable with `@EnableRemoteInterfaces`.

### Response & enums
- **`Result<T>`**: `ok(data)` / `requestFail(msg)` / `accessDenied(msg)` / `toRes(rows|boolean)`. `AbstractApi` base class provides `ok`/`requestFail`/`accessDenied`/`toRes` to ApiImpls.
- **Enums**: `@EnumValue` on the MyBatis-Plus persisted field + `@JsonValue` on the Jackson field + `@JsonCreator static fromValue(String)`. See `AuthTypeEnum`.

### API 实体命名规范

**核心原则：层级标记命名“调用方”（who），不命名“数据”（what）。** 调用方词 `Mng`/`Public` 与表名后缀数据词（`_info`/`_log`/`_record`）词表不相交；`Info` 与 `_info` 同形但分处不同层级——api 层 `XxxInfoApi`/`XxxInfoQueryResponse`（自助轨，`Info`=轨标、`Query`=单对象动作词）vs 持久层 `XxxInfoDO`/`XxxInfoService`/`XxxInfoMapper`（回显表 `xxx_info`），由层级后缀结构性区分不混淆。读/写差异由动作词承载，不切层级。

三轨制，按调用方划分：

| 轨 | 前缀 | 调用方 | 典型场景 |
|---|---|---|---|
| 管理端 | `Mng` | 操作员（管他人/全局） | 后台 CRUD（`XxxMngApi`） |
| 自助端 | `Info` | 已认证主体（管自己） | 个人中心、我的钱包、查自己日志/通知、我的套餐/路由 |
| 公开端 | `Public` | 匿名访客（读参考数据） | 登录前读字典/公告 |

- **每域恰好两轨**（按调用方决定，无三轨并存）：个人数据域（Admin/Tenant/Customer/Member/Wallet/Log/Notification/Menu/TenantPackage）= `Mng` + `Info`；参考数据域 = `Mng` + `Public`（匿名读，如 Dictionary/Announcement）或 `Mng` + `Info`（读需认证，如 Config）。
- **Request**: `{域}{Mng|Info|Public}{动作}Request`。动作 ∈ {`Page`,`List`,`Tree`,`Save`,`Update`,`Grant`,`Select`,`Query`,`Export`,`Withdrawal`}。
- **Response**: `{域}{Mng|Info|Public}{动作}Response`。**动作由返回形状决定**：`Page<X>` 列表元素->`PageResponse`、`List<X>` 列表元素->`ListResponse`、单对象 `Result<X>`->`QueryResponse`。**前缀须与返回它的 Api 一致**：`XxxMngApi`→`XxxMng{动作}Response`、`XxxInfoApi`→`XxxInfo{动作}Response`、`XxxPublicApi`→`XxxPublic{动作}Response`。**`Info` 仅为自助轨轨标，不再是单对象动作词**；所有轨单对象均带 `Query` 动作词（`AdminMngQueryResponse`、`AdminInfoQueryResponse`），集合响应带形状词（`AdminInfoPageResponse`）。导出 VO 用 `XxxMngExportResponse`。
- **前缀即鉴权信号**：`Public`=匿名可调、`Info`=需登录、`Mng`=需操作员权限。**命名仅表意，鉴权由 `@CheckPerms` 强制**——`Info`/`Mng` 轨方法必须标注 `@CheckPerms`（否则前缀虽为 `Info` 仍被匿名放行），`Public` 轨省略。
- **持久层 `Info` 与自助轨 `Info` 同形异义**：`XxxInfoDO`/`XxxInfoService`/`XxxInfoMapper` 的 `Info` 回显表名 `xxx_info`（数据 what）；`XxxInfoApi`/`XxxInfoQueryResponse` 的 `Info` 标自助轨（调用方 who），单对象动作词为 `Query`。由层级后缀（DO/Service/Mapper vs Api/Request/Response）区分，不混淆。
- **mapper/service 统一走 DO，DO 只含表字段**：mapper 单表查询返回 `XxxInfoDO`（不返回 api 层 Response），service 亦返回 DO；DO→Response 转换（`BeanCopierUtil.copy`/`copyList`）统一在 ApiImpl 完成。**跨表 join 的展示字段不得以 `@TableField(exist = false)` 挂在 DO 上**（DO 保持纯表字段），改由 mapper 直接返回独立 VO/Response（如 Notification `SELECT n.id,n.title,n.content,s.read,n.create_at` 直接返回 `NotificationInfoResponse`）。
- **特殊 VO**（`RouterResponse`/`MetaResponse`/`SelectResponse` 等跨切面或结构化 VO）可保留专有名，不强制域前缀。
- **remote 层**（`remote.api.*` 服务端契约 / `remote.interfaces.*` 客户端 `@RemoteInterface` 声明，二者为同一契约的重复 DTO，类名须一致；**不分 Mng/Info/Public 轨**）：`{域}{动作}Request` / `{域}{动作}Response`。**响应动作由返回形状决定**（同 api 层规则）：`List<X>` 元素->`{域}ListResponse`、单对象 `Result<X>`->`{域}QueryResponse`；同一 DTO 兼作 List 元素与单对象时按返回形状拆分（`{域}ListResponse extends {域}QueryResponse`，字段相同亦拆）。**请求动作按操作**：`Query`/`Save`/`Update`/`Event`/`Login`/`Issue`/`Check` 等。**特殊 VO 保留专名**（结构化操作结果/会话包）：`{域}PermissionResponse`、`LoginUserResponse`、`ServiceJwtIssueResponse`；单值参数载体（`StringValueRequest`/`TenantIdRequest`）不强制动作词。**接口名/HTTP 路径不带 Info**：服务端契约 `XxxRemoteApi`/`XxxRemoteApiImpl`/`XxxRemoteController`、客户端 `IXxxRemote`（`@RemoteInterface`），统一去 Info（`AdminRemoteApi`/`IAdminRemote`，非 `AdminInfoRemoteApi`/`IAdminInfoRemote`）；类级 HTTP 路径 `/xxx-remote`（客户端 `@HttpExchange(url="/xxx-remote")` 与服务端 `@RequestMapping("/xxx-remote")` 两端一致，非 `/xxx-info-remote`）。

### Utilities
- **`StringUtil`** extends commons-lang3 `StringUtils` → has `isEmpty`/`isNotEmpty`/`isBlank`/`isNotBlank`. **No `hasText`** (that's Spring's `StringUtils`). Don't mix them up.
- **`BeanCopierUtil.copy`/`copyList`** for DO↔VO/Response. Enum-typed fields are skipped by cglib — set them explicitly.

## Adding a New Business Domain

1. Under `wzkris-modules/`, create `<name>/<name>-api` and `<name>/<name>-biz`; register `<module>` in `wzkris-modules/pom.xml`.
2. `*-api` pom: depend on `common-swagger/validator/remote/orm/excel`. Define enums, request/response, remote interfaces, events.
3. `*-biz` pom: depend on nacos-discovery/config, `common-sentinel/web/log/security/redis`, and its own `*-api`. Add `spring-boot-maven-plugin` `repackage`. Define `XxxApplication` (`@SpringBootApplication` + `ApplicationPidFileWriter`).
4. `application.yml` (port + `context-path: /wzkris-<name>-api` + name), `application-dev.yml` (Nacos config import block, mirroring `wzkris-user-center`).
5. DDL in `sql/postgresql/wzkris_<name>.sql` (schema `biz`, no `tenant_id` unless whitelisted).
6. Gateway route in Nacos `wzkris-gateway.yml`: `Path=/wzkris-<name>-api/** → lb://wzkris-<name>`.

## SQL DDL location

`sql/postgresql/` for all business table scripts (PostgreSQL is the only app DB; MySQL is Nacos-only).
