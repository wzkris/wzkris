--
-- PostgreSQL database dump
--

-- Dumped from database version 15.5
-- Dumped by pg_dump version 16.0

-- Started on 2025-04-11 14:10:05

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

--
-- TOC entry 6 (class 2615 OID 35445)
-- Name: biz_app; Type: SCHEMA; Schema: -; Owner: wz
--

CREATE SCHEMA biz_app;


ALTER SCHEMA biz_app OWNER TO wz;

--
-- TOC entry 7 (class 2615 OID 35446)
-- Name: biz_sys; Type: SCHEMA; Schema: -; Owner: wz
--

CREATE SCHEMA biz_sys;


ALTER SCHEMA biz_sys OWNER TO wz;

SET default_tablespace = '';

SET default_table_access_method = heap;

--
-- TOC entry 216 (class 1259 OID 35447)
-- Name: app_user; Type: TABLE; Schema: biz_app; Owner: wz
--

CREATE TABLE biz_app.app_user (
    user_id bigint NOT NULL,
    nickname character varying(30),
    phone_number character varying(16),
    status character(1) NOT NULL,
    gender character(1) NOT NULL,
    avatar character varying(150),
    login_ip inet,
    login_date timestamp with time zone,
    creator_id bigint NOT NULL,
    updater_id bigint,
    create_at timestamp(6) with time zone NOT NULL,
    update_at timestamp(6) with time zone
);


ALTER TABLE biz_app.app_user OWNER TO wz;

--
-- TOC entry 3474 (class 0 OID 0)
-- Dependencies: 216
-- Name: TABLE app_user; Type: COMMENT; Schema: biz_app; Owner: wz
--

COMMENT ON TABLE biz_app.app_user IS '用户信息表';


--
-- TOC entry 3475 (class 0 OID 0)
-- Dependencies: 216
-- Name: COLUMN app_user.user_id; Type: COMMENT; Schema: biz_app; Owner: wz
--

COMMENT ON COLUMN biz_app.app_user.user_id IS '用户ID';


--
-- TOC entry 3476 (class 0 OID 0)
-- Dependencies: 216
-- Name: COLUMN app_user.nickname; Type: COMMENT; Schema: biz_app; Owner: wz
--

COMMENT ON COLUMN biz_app.app_user.nickname IS '用户昵称';


--
-- TOC entry 3477 (class 0 OID 0)
-- Dependencies: 216
-- Name: COLUMN app_user.phone_number; Type: COMMENT; Schema: biz_app; Owner: wz
--

COMMENT ON COLUMN biz_app.app_user.phone_number IS '手机号码';


--
-- TOC entry 3478 (class 0 OID 0)
-- Dependencies: 216
-- Name: COLUMN app_user.status; Type: COMMENT; Schema: biz_app; Owner: wz
--

COMMENT ON COLUMN biz_app.app_user.status IS '状态值';


--
-- TOC entry 3479 (class 0 OID 0)
-- Dependencies: 216
-- Name: COLUMN app_user.gender; Type: COMMENT; Schema: biz_app; Owner: wz
--

COMMENT ON COLUMN biz_app.app_user.gender IS '用户性别（0男 1女 2未知）';


--
-- TOC entry 3480 (class 0 OID 0)
-- Dependencies: 216
-- Name: COLUMN app_user.avatar; Type: COMMENT; Schema: biz_app; Owner: wz
--

COMMENT ON COLUMN biz_app.app_user.avatar IS '头像地址';


--
-- TOC entry 3481 (class 0 OID 0)
-- Dependencies: 216
-- Name: COLUMN app_user.login_ip; Type: COMMENT; Schema: biz_app; Owner: wz
--

COMMENT ON COLUMN biz_app.app_user.login_ip IS '登录ip';


--
-- TOC entry 3482 (class 0 OID 0)
-- Dependencies: 216
-- Name: COLUMN app_user.login_date; Type: COMMENT; Schema: biz_app; Owner: wz
--

COMMENT ON COLUMN biz_app.app_user.login_date IS '登录时间';


--
-- TOC entry 3483 (class 0 OID 0)
-- Dependencies: 216
-- Name: COLUMN app_user.creator_id; Type: COMMENT; Schema: biz_app; Owner: wz
--

COMMENT ON COLUMN biz_app.app_user.creator_id IS '创建者';


--
-- TOC entry 3484 (class 0 OID 0)
-- Dependencies: 216
-- Name: COLUMN app_user.updater_id; Type: COMMENT; Schema: biz_app; Owner: wz
--

COMMENT ON COLUMN biz_app.app_user.updater_id IS '更新者';


--
-- TOC entry 217 (class 1259 OID 35452)
-- Name: app_user_thirdinfo; Type: TABLE; Schema: biz_app; Owner: wz
--

CREATE TABLE biz_app.app_user_thirdinfo (
    user_id bigint NOT NULL,
    identifier character varying(32) NOT NULL,
    identifier_type character varying(10) NOT NULL
);


ALTER TABLE biz_app.app_user_thirdinfo OWNER TO wz;

--
-- TOC entry 3485 (class 0 OID 0)
-- Dependencies: 217
-- Name: TABLE app_user_thirdinfo; Type: COMMENT; Schema: biz_app; Owner: wz
--

COMMENT ON TABLE biz_app.app_user_thirdinfo IS '第三方信息';


--
-- TOC entry 3486 (class 0 OID 0)
-- Dependencies: 217
-- Name: COLUMN app_user_thirdinfo.identifier; Type: COMMENT; Schema: biz_app; Owner: wz
--

COMMENT ON COLUMN biz_app.app_user_thirdinfo.identifier IS '三方唯一标识符';


--
-- TOC entry 3487 (class 0 OID 0)
-- Dependencies: 217
-- Name: COLUMN app_user_thirdinfo.identifier_type; Type: COMMENT; Schema: biz_app; Owner: wz
--

COMMENT ON COLUMN biz_app.app_user_thirdinfo.identifier_type IS '三方渠道';


--
-- TOC entry 218 (class 1259 OID 35455)
-- Name: app_user_wallet; Type: TABLE; Schema: biz_app; Owner: wz
--

CREATE TABLE biz_app.app_user_wallet (
    user_id bigint NOT NULL,
    balance numeric(10,2) NOT NULL,
    status character(1) NOT NULL
);


ALTER TABLE biz_app.app_user_wallet OWNER TO wz;

--
-- TOC entry 3488 (class 0 OID 0)
-- Dependencies: 218
-- Name: TABLE app_user_wallet; Type: COMMENT; Schema: biz_app; Owner: wz
--

COMMENT ON TABLE biz_app.app_user_wallet IS '用户钱包';


--
-- TOC entry 3489 (class 0 OID 0)
-- Dependencies: 218
-- Name: COLUMN app_user_wallet.balance; Type: COMMENT; Schema: biz_app; Owner: wz
--

COMMENT ON COLUMN biz_app.app_user_wallet.balance IS '余额, 元';


--
-- TOC entry 3490 (class 0 OID 0)
-- Dependencies: 218
-- Name: COLUMN app_user_wallet.status; Type: COMMENT; Schema: biz_app; Owner: wz
--

COMMENT ON COLUMN biz_app.app_user_wallet.status IS '状态';


--
-- TOC entry 219 (class 1259 OID 35458)
-- Name: app_user_wallet_record; Type: TABLE; Schema: biz_app; Owner: wz
--

CREATE TABLE biz_app.app_user_wallet_record (
    record_id bigint NOT NULL,
    user_id bigint NOT NULL,
    amount numeric(10,2) NOT NULL,
    record_type character(1) NOT NULL,
    create_at timestamp with time zone NOT NULL,
    remark character varying(100)
);


ALTER TABLE biz_app.app_user_wallet_record OWNER TO wz;

--
-- TOC entry 3491 (class 0 OID 0)
-- Dependencies: 219
-- Name: TABLE app_user_wallet_record; Type: COMMENT; Schema: biz_app; Owner: wz
--

COMMENT ON TABLE biz_app.app_user_wallet_record IS '用户钱包记录';


--
-- TOC entry 3492 (class 0 OID 0)
-- Dependencies: 219
-- Name: COLUMN app_user_wallet_record.user_id; Type: COMMENT; Schema: biz_app; Owner: wz
--

COMMENT ON COLUMN biz_app.app_user_wallet_record.user_id IS '用户ID';


--
-- TOC entry 3493 (class 0 OID 0)
-- Dependencies: 219
-- Name: COLUMN app_user_wallet_record.amount; Type: COMMENT; Schema: biz_app; Owner: wz
--

COMMENT ON COLUMN biz_app.app_user_wallet_record.amount IS '金额, 元';


--
-- TOC entry 3494 (class 0 OID 0)
-- Dependencies: 219
-- Name: COLUMN app_user_wallet_record.record_type; Type: COMMENT; Schema: biz_app; Owner: wz
--

COMMENT ON COLUMN biz_app.app_user_wallet_record.record_type IS '记录类型';


--
-- TOC entry 3495 (class 0 OID 0)
-- Dependencies: 219
-- Name: COLUMN app_user_wallet_record.create_at; Type: COMMENT; Schema: biz_app; Owner: wz
--

COMMENT ON COLUMN biz_app.app_user_wallet_record.create_at IS '创建时间';


--
-- TOC entry 3496 (class 0 OID 0)
-- Dependencies: 219
-- Name: COLUMN app_user_wallet_record.remark; Type: COMMENT; Schema: biz_app; Owner: wz
--

COMMENT ON COLUMN biz_app.app_user_wallet_record.remark IS '备注';


--
-- TOC entry 220 (class 1259 OID 35461)
-- Name: oauth2_client; Type: TABLE; Schema: biz_sys; Owner: wz
--

CREATE TABLE biz_sys.oauth2_client (
    id bigint NOT NULL,
    client_name character varying(32),
    client_id character varying(32) NOT NULL,
    client_secret character varying(200) NOT NULL,
    scopes text[] DEFAULT '{}'::text[] NOT NULL,
    authorization_grant_types text[] DEFAULT '{}'::text[] NOT NULL,
    redirect_uris text[] DEFAULT '{}'::text[] NOT NULL,
    status character(1) NOT NULL,
    auto_approve boolean NOT NULL,
    create_at timestamp with time zone NOT NULL,
    creator_id bigint NOT NULL,
    update_at timestamp with time zone,
    updater_id bigint
);


ALTER TABLE biz_sys.oauth2_client OWNER TO wz;

--
-- TOC entry 3497 (class 0 OID 0)
-- Dependencies: 220
-- Name: TABLE oauth2_client; Type: COMMENT; Schema: biz_sys; Owner: wz
--

COMMENT ON TABLE biz_sys.oauth2_client IS 'OAUTH2客户端';


--
-- TOC entry 3498 (class 0 OID 0)
-- Dependencies: 220
-- Name: COLUMN oauth2_client.client_name; Type: COMMENT; Schema: biz_sys; Owner: wz
--

COMMENT ON COLUMN biz_sys.oauth2_client.client_name IS '客户端名称';


--
-- TOC entry 3499 (class 0 OID 0)
-- Dependencies: 220
-- Name: COLUMN oauth2_client.scopes; Type: COMMENT; Schema: biz_sys; Owner: wz
--

COMMENT ON COLUMN biz_sys.oauth2_client.scopes IS '权限域';


--
-- TOC entry 3500 (class 0 OID 0)
-- Dependencies: 220
-- Name: COLUMN oauth2_client.authorization_grant_types; Type: COMMENT; Schema: biz_sys; Owner: wz
--

COMMENT ON COLUMN biz_sys.oauth2_client.authorization_grant_types IS '授权类型';


--
-- TOC entry 3501 (class 0 OID 0)
-- Dependencies: 220
-- Name: COLUMN oauth2_client.redirect_uris; Type: COMMENT; Schema: biz_sys; Owner: wz
--

COMMENT ON COLUMN biz_sys.oauth2_client.redirect_uris IS '回调地址';


--
-- TOC entry 3502 (class 0 OID 0)
-- Dependencies: 220
-- Name: COLUMN oauth2_client.status; Type: COMMENT; Schema: biz_sys; Owner: wz
--

COMMENT ON COLUMN biz_sys.oauth2_client.status IS '客户端状态';


--
-- TOC entry 3503 (class 0 OID 0)
-- Dependencies: 220
-- Name: COLUMN oauth2_client.auto_approve; Type: COMMENT; Schema: biz_sys; Owner: wz
--

COMMENT ON COLUMN biz_sys.oauth2_client.auto_approve IS '是否自动放行';


--
-- TOC entry 221 (class 1259 OID 35469)
-- Name: sys_dept; Type: TABLE; Schema: biz_sys; Owner: wz
--

CREATE TABLE biz_sys.sys_dept (
    dept_id bigint NOT NULL,
    tenant_id bigint NOT NULL,
    parent_id bigint DEFAULT 0 NOT NULL,
    ancestors bigint[] DEFAULT '{}'::bigint[] NOT NULL,
    dept_name character varying(30),
    status character(1),
    dept_sort integer,
    contact character varying(15),
    email character varying(50),
    creator_id bigint NOT NULL,
    updater_id bigint,
    create_at timestamp with time zone NOT NULL,
    update_at timestamp with time zone
);


ALTER TABLE biz_sys.sys_dept OWNER TO wz;

--
-- TOC entry 3504 (class 0 OID 0)
-- Dependencies: 221
-- Name: TABLE sys_dept; Type: COMMENT; Schema: biz_sys; Owner: wz
--

COMMENT ON TABLE biz_sys.sys_dept IS '部门表';


--
-- TOC entry 3505 (class 0 OID 0)
-- Dependencies: 221
-- Name: COLUMN sys_dept.dept_id; Type: COMMENT; Schema: biz_sys; Owner: wz
--

COMMENT ON COLUMN biz_sys.sys_dept.dept_id IS '部门id';


--
-- TOC entry 3506 (class 0 OID 0)
-- Dependencies: 221
-- Name: COLUMN sys_dept.tenant_id; Type: COMMENT; Schema: biz_sys; Owner: wz
--

COMMENT ON COLUMN biz_sys.sys_dept.tenant_id IS '租户ID';


--
-- TOC entry 3507 (class 0 OID 0)
-- Dependencies: 221
-- Name: COLUMN sys_dept.parent_id; Type: COMMENT; Schema: biz_sys; Owner: wz
--

COMMENT ON COLUMN biz_sys.sys_dept.parent_id IS '父部门id';


--
-- TOC entry 3508 (class 0 OID 0)
-- Dependencies: 221
-- Name: COLUMN sys_dept.ancestors; Type: COMMENT; Schema: biz_sys; Owner: wz
--

COMMENT ON COLUMN biz_sys.sys_dept.ancestors IS '祖级列表';


--
-- TOC entry 3509 (class 0 OID 0)
-- Dependencies: 221
-- Name: COLUMN sys_dept.dept_name; Type: COMMENT; Schema: biz_sys; Owner: wz
--

COMMENT ON COLUMN biz_sys.sys_dept.dept_name IS '部门名称';


--
-- TOC entry 3510 (class 0 OID 0)
-- Dependencies: 221
-- Name: COLUMN sys_dept.status; Type: COMMENT; Schema: biz_sys; Owner: wz
--

COMMENT ON COLUMN biz_sys.sys_dept.status IS '0代表正常 1代表停用';


--
-- TOC entry 3511 (class 0 OID 0)
-- Dependencies: 221
-- Name: COLUMN sys_dept.dept_sort; Type: COMMENT; Schema: biz_sys; Owner: wz
--

COMMENT ON COLUMN biz_sys.sys_dept.dept_sort IS '显示顺序';


--
-- TOC entry 3512 (class 0 OID 0)
-- Dependencies: 221
-- Name: COLUMN sys_dept.contact; Type: COMMENT; Schema: biz_sys; Owner: wz
--

COMMENT ON COLUMN biz_sys.sys_dept.contact IS '联系电话';


--
-- TOC entry 3513 (class 0 OID 0)
-- Dependencies: 221
-- Name: COLUMN sys_dept.email; Type: COMMENT; Schema: biz_sys; Owner: wz
--

COMMENT ON COLUMN biz_sys.sys_dept.email IS '邮箱';


--
-- TOC entry 3514 (class 0 OID 0)
-- Dependencies: 221
-- Name: COLUMN sys_dept.creator_id; Type: COMMENT; Schema: biz_sys; Owner: wz
--

COMMENT ON COLUMN biz_sys.sys_dept.creator_id IS '创建者';


--
-- TOC entry 3515 (class 0 OID 0)
-- Dependencies: 221
-- Name: COLUMN sys_dept.updater_id; Type: COMMENT; Schema: biz_sys; Owner: wz
--

COMMENT ON COLUMN biz_sys.sys_dept.updater_id IS '更新者';


--
-- TOC entry 222 (class 1259 OID 35476)
-- Name: sys_menu; Type: TABLE; Schema: biz_sys; Owner: wz
--

CREATE TABLE biz_sys.sys_menu (
    menu_id bigint NOT NULL,
    menu_name character varying(30) NOT NULL,
    parent_id bigint NOT NULL,
    menu_sort integer NOT NULL,
    path character varying(50) DEFAULT '#'::character varying NOT NULL,
    component character varying(50),
    query character varying(50),
    menu_type character(1) NOT NULL,
    status character(1) NOT NULL,
    perms character varying(30),
    icon character varying(50) DEFAULT '#'::character varying NOT NULL,
    is_cache boolean NOT NULL,
    is_visible boolean NOT NULL,
    create_at timestamp(6) with time zone NOT NULL,
    creator_id bigint NOT NULL,
    update_at timestamp(6) with time zone,
    updater_id bigint
);


ALTER TABLE biz_sys.sys_menu OWNER TO wz;

--
-- TOC entry 3516 (class 0 OID 0)
-- Dependencies: 222
-- Name: TABLE sys_menu; Type: COMMENT; Schema: biz_sys; Owner: wz
--

COMMENT ON TABLE biz_sys.sys_menu IS '菜单权限表';


--
-- TOC entry 3517 (class 0 OID 0)
-- Dependencies: 222
-- Name: COLUMN sys_menu.menu_id; Type: COMMENT; Schema: biz_sys; Owner: wz
--

COMMENT ON COLUMN biz_sys.sys_menu.menu_id IS '菜单ID';


--
-- TOC entry 3518 (class 0 OID 0)
-- Dependencies: 222
-- Name: COLUMN sys_menu.menu_name; Type: COMMENT; Schema: biz_sys; Owner: wz
--

COMMENT ON COLUMN biz_sys.sys_menu.menu_name IS '菜单名称';


--
-- TOC entry 3519 (class 0 OID 0)
-- Dependencies: 222
-- Name: COLUMN sys_menu.parent_id; Type: COMMENT; Schema: biz_sys; Owner: wz
--

COMMENT ON COLUMN biz_sys.sys_menu.parent_id IS '父菜单ID';


--
-- TOC entry 3520 (class 0 OID 0)
-- Dependencies: 222
-- Name: COLUMN sys_menu.menu_sort; Type: COMMENT; Schema: biz_sys; Owner: wz
--

COMMENT ON COLUMN biz_sys.sys_menu.menu_sort IS '显示顺序';


--
-- TOC entry 3521 (class 0 OID 0)
-- Dependencies: 222
-- Name: COLUMN sys_menu.path; Type: COMMENT; Schema: biz_sys; Owner: wz
--

COMMENT ON COLUMN biz_sys.sys_menu.path IS '路由地址';


--
-- TOC entry 3522 (class 0 OID 0)
-- Dependencies: 222
-- Name: COLUMN sys_menu.component; Type: COMMENT; Schema: biz_sys; Owner: wz
--

COMMENT ON COLUMN biz_sys.sys_menu.component IS '组件路径';


--
-- TOC entry 3523 (class 0 OID 0)
-- Dependencies: 222
-- Name: COLUMN sys_menu.query; Type: COMMENT; Schema: biz_sys; Owner: wz
--

COMMENT ON COLUMN biz_sys.sys_menu.query IS '路由参数';


--
-- TOC entry 3524 (class 0 OID 0)
-- Dependencies: 222
-- Name: COLUMN sys_menu.menu_type; Type: COMMENT; Schema: biz_sys; Owner: wz
--

COMMENT ON COLUMN biz_sys.sys_menu.menu_type IS '菜单类型（D目录 M菜单 B按钮 I内链 O外链）';


--
-- TOC entry 3525 (class 0 OID 0)
-- Dependencies: 222
-- Name: COLUMN sys_menu.status; Type: COMMENT; Schema: biz_sys; Owner: wz
--

COMMENT ON COLUMN biz_sys.sys_menu.status IS '菜单状态（0正常 1停用）';


--
-- TOC entry 3526 (class 0 OID 0)
-- Dependencies: 222
-- Name: COLUMN sys_menu.perms; Type: COMMENT; Schema: biz_sys; Owner: wz
--

COMMENT ON COLUMN biz_sys.sys_menu.perms IS '权限标识';


--
-- TOC entry 3527 (class 0 OID 0)
-- Dependencies: 222
-- Name: COLUMN sys_menu.icon; Type: COMMENT; Schema: biz_sys; Owner: wz
--

COMMENT ON COLUMN biz_sys.sys_menu.icon IS '菜单图标';


--
-- TOC entry 3528 (class 0 OID 0)
-- Dependencies: 222
-- Name: COLUMN sys_menu.is_cache; Type: COMMENT; Schema: biz_sys; Owner: wz
--

COMMENT ON COLUMN biz_sys.sys_menu.is_cache IS '是否缓存';


--
-- TOC entry 3529 (class 0 OID 0)
-- Dependencies: 222
-- Name: COLUMN sys_menu.is_visible; Type: COMMENT; Schema: biz_sys; Owner: wz
--

COMMENT ON COLUMN biz_sys.sys_menu.is_visible IS '是否显示';


--
-- TOC entry 3530 (class 0 OID 0)
-- Dependencies: 222
-- Name: COLUMN sys_menu.create_at; Type: COMMENT; Schema: biz_sys; Owner: wz
--

COMMENT ON COLUMN biz_sys.sys_menu.create_at IS '创建时间';


--
-- TOC entry 3531 (class 0 OID 0)
-- Dependencies: 222
-- Name: COLUMN sys_menu.creator_id; Type: COMMENT; Schema: biz_sys; Owner: wz
--

COMMENT ON COLUMN biz_sys.sys_menu.creator_id IS '创建者';


--
-- TOC entry 3532 (class 0 OID 0)
-- Dependencies: 222
-- Name: COLUMN sys_menu.update_at; Type: COMMENT; Schema: biz_sys; Owner: wz
--

COMMENT ON COLUMN biz_sys.sys_menu.update_at IS '更新时间';


--
-- TOC entry 3533 (class 0 OID 0)
-- Dependencies: 222
-- Name: COLUMN sys_menu.updater_id; Type: COMMENT; Schema: biz_sys; Owner: wz
--

COMMENT ON COLUMN biz_sys.sys_menu.updater_id IS '更新者';


--
-- TOC entry 223 (class 1259 OID 35481)
-- Name: sys_post; Type: TABLE; Schema: biz_sys; Owner: wz
--

CREATE TABLE biz_sys.sys_post (
    post_id bigint NOT NULL,
    tenant_id bigint NOT NULL,
    post_code character varying(30),
    post_name character varying(30) NOT NULL,
    status character(1) NOT NULL,
    post_sort integer NOT NULL,
    creator_id bigint NOT NULL,
    updater_id bigint,
    create_at timestamp with time zone NOT NULL,
    update_at timestamp with time zone
);


ALTER TABLE biz_sys.sys_post OWNER TO wz;

--
-- TOC entry 3534 (class 0 OID 0)
-- Dependencies: 223
-- Name: TABLE sys_post; Type: COMMENT; Schema: biz_sys; Owner: wz
--

COMMENT ON TABLE biz_sys.sys_post IS '岗位信息表';


--
-- TOC entry 3535 (class 0 OID 0)
-- Dependencies: 223
-- Name: COLUMN sys_post.post_id; Type: COMMENT; Schema: biz_sys; Owner: wz
--

COMMENT ON COLUMN biz_sys.sys_post.post_id IS '岗位ID';


--
-- TOC entry 3536 (class 0 OID 0)
-- Dependencies: 223
-- Name: COLUMN sys_post.tenant_id; Type: COMMENT; Schema: biz_sys; Owner: wz
--

COMMENT ON COLUMN biz_sys.sys_post.tenant_id IS '租户ID';


--
-- TOC entry 3537 (class 0 OID 0)
-- Dependencies: 223
-- Name: COLUMN sys_post.post_code; Type: COMMENT; Schema: biz_sys; Owner: wz
--

COMMENT ON COLUMN biz_sys.sys_post.post_code IS '岗位编码';


--
-- TOC entry 3538 (class 0 OID 0)
-- Dependencies: 223
-- Name: COLUMN sys_post.post_name; Type: COMMENT; Schema: biz_sys; Owner: wz
--

COMMENT ON COLUMN biz_sys.sys_post.post_name IS '岗位名称';


--
-- TOC entry 3539 (class 0 OID 0)
-- Dependencies: 223
-- Name: COLUMN sys_post.status; Type: COMMENT; Schema: biz_sys; Owner: wz
--

COMMENT ON COLUMN biz_sys.sys_post.status IS '状态（0正常 1停用）';


--
-- TOC entry 3540 (class 0 OID 0)
-- Dependencies: 223
-- Name: COLUMN sys_post.post_sort; Type: COMMENT; Schema: biz_sys; Owner: wz
--

COMMENT ON COLUMN biz_sys.sys_post.post_sort IS '显示顺序';


--
-- TOC entry 3541 (class 0 OID 0)
-- Dependencies: 223
-- Name: COLUMN sys_post.creator_id; Type: COMMENT; Schema: biz_sys; Owner: wz
--

COMMENT ON COLUMN biz_sys.sys_post.creator_id IS '创建者';


--
-- TOC entry 3542 (class 0 OID 0)
-- Dependencies: 223
-- Name: COLUMN sys_post.updater_id; Type: COMMENT; Schema: biz_sys; Owner: wz
--

COMMENT ON COLUMN biz_sys.sys_post.updater_id IS '更新者';


--
-- TOC entry 224 (class 1259 OID 35484)
-- Name: sys_role; Type: TABLE; Schema: biz_sys; Owner: wz
--

CREATE TABLE biz_sys.sys_role (
    role_id bigint NOT NULL,
    tenant_id bigint NOT NULL,
    data_scope character(1) NOT NULL,
    role_name character varying(20) NOT NULL,
    status character(1) NOT NULL,
    role_sort integer NOT NULL,
    is_menu_display boolean DEFAULT false NOT NULL,
    is_dept_display boolean DEFAULT false NOT NULL,
    create_at timestamp with time zone NOT NULL,
    creator_id bigint NOT NULL,
    update_at timestamp with time zone,
    updater_id bigint
);


ALTER TABLE biz_sys.sys_role OWNER TO wz;

--
-- TOC entry 3543 (class 0 OID 0)
-- Dependencies: 224
-- Name: TABLE sys_role; Type: COMMENT; Schema: biz_sys; Owner: wz
--

COMMENT ON TABLE biz_sys.sys_role IS '角色信息表';


--
-- TOC entry 3544 (class 0 OID 0)
-- Dependencies: 224
-- Name: COLUMN sys_role.role_id; Type: COMMENT; Schema: biz_sys; Owner: wz
--

COMMENT ON COLUMN biz_sys.sys_role.role_id IS '角色ID';


--
-- TOC entry 3545 (class 0 OID 0)
-- Dependencies: 224
-- Name: COLUMN sys_role.tenant_id; Type: COMMENT; Schema: biz_sys; Owner: wz
--

COMMENT ON COLUMN biz_sys.sys_role.tenant_id IS '租户ID';


--
-- TOC entry 3546 (class 0 OID 0)
-- Dependencies: 224
-- Name: COLUMN sys_role.data_scope; Type: COMMENT; Schema: biz_sys; Owner: wz
--

COMMENT ON COLUMN biz_sys.sys_role.data_scope IS '数据范围（1：全部数据权限 2：自定数据权限 3：本部门数据权限 4：本部门及以下数据权限）';


--
-- TOC entry 3547 (class 0 OID 0)
-- Dependencies: 224
-- Name: COLUMN sys_role.role_name; Type: COMMENT; Schema: biz_sys; Owner: wz
--

COMMENT ON COLUMN biz_sys.sys_role.role_name IS '角色名称';


--
-- TOC entry 3548 (class 0 OID 0)
-- Dependencies: 224
-- Name: COLUMN sys_role.status; Type: COMMENT; Schema: biz_sys; Owner: wz
--

COMMENT ON COLUMN biz_sys.sys_role.status IS '状态（0正常，1停用）';


--
-- TOC entry 3549 (class 0 OID 0)
-- Dependencies: 224
-- Name: COLUMN sys_role.role_sort; Type: COMMENT; Schema: biz_sys; Owner: wz
--

COMMENT ON COLUMN biz_sys.sys_role.role_sort IS '显示顺序';


--
-- TOC entry 3550 (class 0 OID 0)
-- Dependencies: 224
-- Name: COLUMN sys_role.is_menu_display; Type: COMMENT; Schema: biz_sys; Owner: wz
--

COMMENT ON COLUMN biz_sys.sys_role.is_menu_display IS '菜单选项是否关联显示';


--
-- TOC entry 3551 (class 0 OID 0)
-- Dependencies: 224
-- Name: COLUMN sys_role.is_dept_display; Type: COMMENT; Schema: biz_sys; Owner: wz
--

COMMENT ON COLUMN biz_sys.sys_role.is_dept_display IS '部门选项是否关联显示';


--
-- TOC entry 3552 (class 0 OID 0)
-- Dependencies: 224
-- Name: COLUMN sys_role.create_at; Type: COMMENT; Schema: biz_sys; Owner: wz
--

COMMENT ON COLUMN biz_sys.sys_role.create_at IS '创建时间';


--
-- TOC entry 3553 (class 0 OID 0)
-- Dependencies: 224
-- Name: COLUMN sys_role.creator_id; Type: COMMENT; Schema: biz_sys; Owner: wz
--

COMMENT ON COLUMN biz_sys.sys_role.creator_id IS '创建者';


--
-- TOC entry 3554 (class 0 OID 0)
-- Dependencies: 224
-- Name: COLUMN sys_role.update_at; Type: COMMENT; Schema: biz_sys; Owner: wz
--

COMMENT ON COLUMN biz_sys.sys_role.update_at IS '更新时间';


--
-- TOC entry 3555 (class 0 OID 0)
-- Dependencies: 224
-- Name: COLUMN sys_role.updater_id; Type: COMMENT; Schema: biz_sys; Owner: wz
--

COMMENT ON COLUMN biz_sys.sys_role.updater_id IS '更新者';


--
-- TOC entry 225 (class 1259 OID 35489)
-- Name: sys_role_dept; Type: TABLE; Schema: biz_sys; Owner: wz
--

CREATE TABLE biz_sys.sys_role_dept (
    role_id bigint NOT NULL,
    dept_id bigint NOT NULL
);


ALTER TABLE biz_sys.sys_role_dept OWNER TO wz;

--
-- TOC entry 3556 (class 0 OID 0)
-- Dependencies: 225
-- Name: TABLE sys_role_dept; Type: COMMENT; Schema: biz_sys; Owner: wz
--

COMMENT ON TABLE biz_sys.sys_role_dept IS '角色数据权限关联表';


--
-- TOC entry 3557 (class 0 OID 0)
-- Dependencies: 225
-- Name: COLUMN sys_role_dept.role_id; Type: COMMENT; Schema: biz_sys; Owner: wz
--

COMMENT ON COLUMN biz_sys.sys_role_dept.role_id IS '角色id';


--
-- TOC entry 3558 (class 0 OID 0)
-- Dependencies: 225
-- Name: COLUMN sys_role_dept.dept_id; Type: COMMENT; Schema: biz_sys; Owner: wz
--

COMMENT ON COLUMN biz_sys.sys_role_dept.dept_id IS '部门id';


--
-- TOC entry 226 (class 1259 OID 35492)
-- Name: sys_role_menu; Type: TABLE; Schema: biz_sys; Owner: wz
--

CREATE TABLE biz_sys.sys_role_menu (
    role_id bigint NOT NULL,
    menu_id bigint NOT NULL
);


ALTER TABLE biz_sys.sys_role_menu OWNER TO wz;

--
-- TOC entry 3559 (class 0 OID 0)
-- Dependencies: 226
-- Name: TABLE sys_role_menu; Type: COMMENT; Schema: biz_sys; Owner: wz
--

COMMENT ON TABLE biz_sys.sys_role_menu IS '角色和菜单关联表';


--
-- TOC entry 3560 (class 0 OID 0)
-- Dependencies: 226
-- Name: COLUMN sys_role_menu.role_id; Type: COMMENT; Schema: biz_sys; Owner: wz
--

COMMENT ON COLUMN biz_sys.sys_role_menu.role_id IS '角色ID';


--
-- TOC entry 3561 (class 0 OID 0)
-- Dependencies: 226
-- Name: COLUMN sys_role_menu.menu_id; Type: COMMENT; Schema: biz_sys; Owner: wz
--

COMMENT ON COLUMN biz_sys.sys_role_menu.menu_id IS '菜单ID';


--
-- TOC entry 227 (class 1259 OID 35495)
-- Name: sys_tenant; Type: TABLE; Schema: biz_sys; Owner: wz
--

CREATE TABLE biz_sys.sys_tenant (
    tenant_id bigint NOT NULL,
    administrator bigint NOT NULL,
    tenant_type character(1) NOT NULL,
    contact_phone character varying(20),
    tenant_name character varying(32) NOT NULL,
    oper_pwd character varying(100) NOT NULL,
    status character(1) NOT NULL,
    domain character varying(100),
    remark character varying(200),
    package_id bigint NOT NULL,
    expire_time timestamp(6) with time zone NOT NULL,
    account_limit smallint NOT NULL,
    role_limit smallint NOT NULL,
    post_limit smallint NOT NULL,
    dept_limit smallint NOT NULL,
    creator_id bigint NOT NULL,
    create_at timestamp with time zone NOT NULL,
    updater_id bigint,
    update_at timestamp with time zone
);


ALTER TABLE biz_sys.sys_tenant OWNER TO wz;

--
-- TOC entry 3562 (class 0 OID 0)
-- Dependencies: 227
-- Name: TABLE sys_tenant; Type: COMMENT; Schema: biz_sys; Owner: wz
--

COMMENT ON TABLE biz_sys.sys_tenant IS '租户表';


--
-- TOC entry 3563 (class 0 OID 0)
-- Dependencies: 227
-- Name: COLUMN sys_tenant.tenant_id; Type: COMMENT; Schema: biz_sys; Owner: wz
--

COMMENT ON COLUMN biz_sys.sys_tenant.tenant_id IS '租户编号';


--
-- TOC entry 3564 (class 0 OID 0)
-- Dependencies: 227
-- Name: COLUMN sys_tenant.administrator; Type: COMMENT; Schema: biz_sys; Owner: wz
--

COMMENT ON COLUMN biz_sys.sys_tenant.administrator IS '管理员ID';


--
-- TOC entry 3565 (class 0 OID 0)
-- Dependencies: 227
-- Name: COLUMN sys_tenant.tenant_type; Type: COMMENT; Schema: biz_sys; Owner: wz
--

COMMENT ON COLUMN biz_sys.sys_tenant.tenant_type IS '租户类型';


--
-- TOC entry 3566 (class 0 OID 0)
-- Dependencies: 227
-- Name: COLUMN sys_tenant.contact_phone; Type: COMMENT; Schema: biz_sys; Owner: wz
--

COMMENT ON COLUMN biz_sys.sys_tenant.contact_phone IS '联系电话';


--
-- TOC entry 3567 (class 0 OID 0)
-- Dependencies: 227
-- Name: COLUMN sys_tenant.tenant_name; Type: COMMENT; Schema: biz_sys; Owner: wz
--

COMMENT ON COLUMN biz_sys.sys_tenant.tenant_name IS '租户名称';


--
-- TOC entry 3568 (class 0 OID 0)
-- Dependencies: 227
-- Name: COLUMN sys_tenant.oper_pwd; Type: COMMENT; Schema: biz_sys; Owner: wz
--

COMMENT ON COLUMN biz_sys.sys_tenant.oper_pwd IS '操作密码';


--
-- TOC entry 3569 (class 0 OID 0)
-- Dependencies: 227
-- Name: COLUMN sys_tenant.status; Type: COMMENT; Schema: biz_sys; Owner: wz
--

COMMENT ON COLUMN biz_sys.sys_tenant.status IS '租户状态';


--
-- TOC entry 3570 (class 0 OID 0)
-- Dependencies: 227
-- Name: COLUMN sys_tenant.domain; Type: COMMENT; Schema: biz_sys; Owner: wz
--

COMMENT ON COLUMN biz_sys.sys_tenant.domain IS '域名';


--
-- TOC entry 3571 (class 0 OID 0)
-- Dependencies: 227
-- Name: COLUMN sys_tenant.remark; Type: COMMENT; Schema: biz_sys; Owner: wz
--

COMMENT ON COLUMN biz_sys.sys_tenant.remark IS '备注';


--
-- TOC entry 3572 (class 0 OID 0)
-- Dependencies: 227
-- Name: COLUMN sys_tenant.package_id; Type: COMMENT; Schema: biz_sys; Owner: wz
--

COMMENT ON COLUMN biz_sys.sys_tenant.package_id IS '租户套餐编号';


--
-- TOC entry 3573 (class 0 OID 0)
-- Dependencies: 227
-- Name: COLUMN sys_tenant.expire_time; Type: COMMENT; Schema: biz_sys; Owner: wz
--

COMMENT ON COLUMN biz_sys.sys_tenant.expire_time IS '过期时间';


--
-- TOC entry 3574 (class 0 OID 0)
-- Dependencies: 227
-- Name: COLUMN sys_tenant.account_limit; Type: COMMENT; Schema: biz_sys; Owner: wz
--

COMMENT ON COLUMN biz_sys.sys_tenant.account_limit IS '账号数量（-1不限制）';


--
-- TOC entry 3575 (class 0 OID 0)
-- Dependencies: 227
-- Name: COLUMN sys_tenant.role_limit; Type: COMMENT; Schema: biz_sys; Owner: wz
--

COMMENT ON COLUMN biz_sys.sys_tenant.role_limit IS '角色数量（-1不限制）';


--
-- TOC entry 3576 (class 0 OID 0)
-- Dependencies: 227
-- Name: COLUMN sys_tenant.post_limit; Type: COMMENT; Schema: biz_sys; Owner: wz
--

COMMENT ON COLUMN biz_sys.sys_tenant.post_limit IS '岗位数量（-1不限制）';


--
-- TOC entry 3577 (class 0 OID 0)
-- Dependencies: 227
-- Name: COLUMN sys_tenant.dept_limit; Type: COMMENT; Schema: biz_sys; Owner: wz
--

COMMENT ON COLUMN biz_sys.sys_tenant.dept_limit IS '部门数量（-1不限制）';


--
-- TOC entry 3578 (class 0 OID 0)
-- Dependencies: 227
-- Name: COLUMN sys_tenant.creator_id; Type: COMMENT; Schema: biz_sys; Owner: wz
--

COMMENT ON COLUMN biz_sys.sys_tenant.creator_id IS '创建者';


--
-- TOC entry 3579 (class 0 OID 0)
-- Dependencies: 227
-- Name: COLUMN sys_tenant.create_at; Type: COMMENT; Schema: biz_sys; Owner: wz
--

COMMENT ON COLUMN biz_sys.sys_tenant.create_at IS '创建时间';


--
-- TOC entry 3580 (class 0 OID 0)
-- Dependencies: 227
-- Name: COLUMN sys_tenant.updater_id; Type: COMMENT; Schema: biz_sys; Owner: wz
--

COMMENT ON COLUMN biz_sys.sys_tenant.updater_id IS '更新者';


--
-- TOC entry 3581 (class 0 OID 0)
-- Dependencies: 227
-- Name: COLUMN sys_tenant.update_at; Type: COMMENT; Schema: biz_sys; Owner: wz
--

COMMENT ON COLUMN biz_sys.sys_tenant.update_at IS '更新时间';


--
-- TOC entry 228 (class 1259 OID 35498)
-- Name: sys_tenant_package; Type: TABLE; Schema: biz_sys; Owner: wz
--

CREATE TABLE biz_sys.sys_tenant_package (
    package_id bigint NOT NULL,
    package_name character varying(20) NOT NULL,
    status character(1) NOT NULL,
    menu_ids bigint[] DEFAULT '{}'::bigint[] NOT NULL,
    remark character varying(200),
    creator_id bigint NOT NULL,
    create_at timestamp with time zone NOT NULL,
    updater_id bigint,
    update_at timestamp with time zone
);


ALTER TABLE biz_sys.sys_tenant_package OWNER TO wz;

--
-- TOC entry 3582 (class 0 OID 0)
-- Dependencies: 228
-- Name: TABLE sys_tenant_package; Type: COMMENT; Schema: biz_sys; Owner: wz
--

COMMENT ON TABLE biz_sys.sys_tenant_package IS '租户套餐表';


--
-- TOC entry 3583 (class 0 OID 0)
-- Dependencies: 228
-- Name: COLUMN sys_tenant_package.package_id; Type: COMMENT; Schema: biz_sys; Owner: wz
--

COMMENT ON COLUMN biz_sys.sys_tenant_package.package_id IS '租户套餐id';


--
-- TOC entry 3584 (class 0 OID 0)
-- Dependencies: 228
-- Name: COLUMN sys_tenant_package.package_name; Type: COMMENT; Schema: biz_sys; Owner: wz
--

COMMENT ON COLUMN biz_sys.sys_tenant_package.package_name IS '套餐名称';


--
-- TOC entry 3585 (class 0 OID 0)
-- Dependencies: 228
-- Name: COLUMN sys_tenant_package.status; Type: COMMENT; Schema: biz_sys; Owner: wz
--

COMMENT ON COLUMN biz_sys.sys_tenant_package.status IS '状态（0正常 1停用）';


--
-- TOC entry 3586 (class 0 OID 0)
-- Dependencies: 228
-- Name: COLUMN sys_tenant_package.menu_ids; Type: COMMENT; Schema: biz_sys; Owner: wz
--

COMMENT ON COLUMN biz_sys.sys_tenant_package.menu_ids IS '套餐绑定的菜单';


--
-- TOC entry 3587 (class 0 OID 0)
-- Dependencies: 228
-- Name: COLUMN sys_tenant_package.remark; Type: COMMENT; Schema: biz_sys; Owner: wz
--

COMMENT ON COLUMN biz_sys.sys_tenant_package.remark IS '备注';


--
-- TOC entry 3588 (class 0 OID 0)
-- Dependencies: 228
-- Name: COLUMN sys_tenant_package.creator_id; Type: COMMENT; Schema: biz_sys; Owner: wz
--

COMMENT ON COLUMN biz_sys.sys_tenant_package.creator_id IS '创建者';


--
-- TOC entry 3589 (class 0 OID 0)
-- Dependencies: 228
-- Name: COLUMN sys_tenant_package.create_at; Type: COMMENT; Schema: biz_sys; Owner: wz
--

COMMENT ON COLUMN biz_sys.sys_tenant_package.create_at IS '创建时间';


--
-- TOC entry 3590 (class 0 OID 0)
-- Dependencies: 228
-- Name: COLUMN sys_tenant_package.updater_id; Type: COMMENT; Schema: biz_sys; Owner: wz
--

COMMENT ON COLUMN biz_sys.sys_tenant_package.updater_id IS '更新者';


--
-- TOC entry 3591 (class 0 OID 0)
-- Dependencies: 228
-- Name: COLUMN sys_tenant_package.update_at; Type: COMMENT; Schema: biz_sys; Owner: wz
--

COMMENT ON COLUMN biz_sys.sys_tenant_package.update_at IS '更新时间';


--
-- TOC entry 229 (class 1259 OID 35505)
-- Name: sys_tenant_wallet; Type: TABLE; Schema: biz_sys; Owner: wz
--

CREATE TABLE biz_sys.sys_tenant_wallet (
    tenant_id bigint NOT NULL,
    balance numeric(10,2) NOT NULL,
    status character(1) NOT NULL
);


ALTER TABLE biz_sys.sys_tenant_wallet OWNER TO wz;

--
-- TOC entry 3592 (class 0 OID 0)
-- Dependencies: 229
-- Name: TABLE sys_tenant_wallet; Type: COMMENT; Schema: biz_sys; Owner: wz
--

COMMENT ON TABLE biz_sys.sys_tenant_wallet IS '租户钱包';


--
-- TOC entry 3593 (class 0 OID 0)
-- Dependencies: 229
-- Name: COLUMN sys_tenant_wallet.balance; Type: COMMENT; Schema: biz_sys; Owner: wz
--

COMMENT ON COLUMN biz_sys.sys_tenant_wallet.balance IS '余额, 元';


--
-- TOC entry 3594 (class 0 OID 0)
-- Dependencies: 229
-- Name: COLUMN sys_tenant_wallet.status; Type: COMMENT; Schema: biz_sys; Owner: wz
--

COMMENT ON COLUMN biz_sys.sys_tenant_wallet.status IS '状态';


--
-- TOC entry 230 (class 1259 OID 35508)
-- Name: sys_tenant_wallet_record; Type: TABLE; Schema: biz_sys; Owner: wz
--

CREATE TABLE biz_sys.sys_tenant_wallet_record (
    record_id bigint NOT NULL,
    tenant_id bigint NOT NULL,
    amount numeric(10,2) NOT NULL,
    record_type character(1) NOT NULL,
    create_at timestamp with time zone NOT NULL,
    remark character varying(100)
);


ALTER TABLE biz_sys.sys_tenant_wallet_record OWNER TO wz;

--
-- TOC entry 3595 (class 0 OID 0)
-- Dependencies: 230
-- Name: TABLE sys_tenant_wallet_record; Type: COMMENT; Schema: biz_sys; Owner: wz
--

COMMENT ON TABLE biz_sys.sys_tenant_wallet_record IS '租户钱包记录';


--
-- TOC entry 3596 (class 0 OID 0)
-- Dependencies: 230
-- Name: COLUMN sys_tenant_wallet_record.tenant_id; Type: COMMENT; Schema: biz_sys; Owner: wz
--

COMMENT ON COLUMN biz_sys.sys_tenant_wallet_record.tenant_id IS '租户ID';


--
-- TOC entry 3597 (class 0 OID 0)
-- Dependencies: 230
-- Name: COLUMN sys_tenant_wallet_record.amount; Type: COMMENT; Schema: biz_sys; Owner: wz
--

COMMENT ON COLUMN biz_sys.sys_tenant_wallet_record.amount IS '金额, 单位元';


--
-- TOC entry 3598 (class 0 OID 0)
-- Dependencies: 230
-- Name: COLUMN sys_tenant_wallet_record.record_type; Type: COMMENT; Schema: biz_sys; Owner: wz
--

COMMENT ON COLUMN biz_sys.sys_tenant_wallet_record.record_type IS '记录类型';


--
-- TOC entry 3599 (class 0 OID 0)
-- Dependencies: 230
-- Name: COLUMN sys_tenant_wallet_record.create_at; Type: COMMENT; Schema: biz_sys; Owner: wz
--

COMMENT ON COLUMN biz_sys.sys_tenant_wallet_record.create_at IS '创建时间';


--
-- TOC entry 3600 (class 0 OID 0)
-- Dependencies: 230
-- Name: COLUMN sys_tenant_wallet_record.remark; Type: COMMENT; Schema: biz_sys; Owner: wz
--

COMMENT ON COLUMN biz_sys.sys_tenant_wallet_record.remark IS '备注';


--
-- TOC entry 231 (class 1259 OID 35511)
-- Name: sys_user; Type: TABLE; Schema: biz_sys; Owner: wz
--

CREATE TABLE biz_sys.sys_user (
    user_id bigint NOT NULL,
    tenant_id bigint NOT NULL,
    dept_id bigint,
    username character varying(30) NOT NULL,
    email character varying(50),
    nickname character varying(30),
    phone_number character varying(16),
    status character(1) DEFAULT 0 NOT NULL,
    gender character(1) DEFAULT 2 NOT NULL,
    avatar character varying(150),
    password character varying(100),
    login_ip inet,
    login_date timestamp with time zone,
    remark character varying(64),
    creator_id bigint NOT NULL,
    updater_id bigint,
    create_at timestamp with time zone NOT NULL,
    update_at timestamp with time zone
);


ALTER TABLE biz_sys.sys_user OWNER TO wz;

--
-- TOC entry 3601 (class 0 OID 0)
-- Dependencies: 231
-- Name: TABLE sys_user; Type: COMMENT; Schema: biz_sys; Owner: wz
--

COMMENT ON TABLE biz_sys.sys_user IS '用户信息表';


--
-- TOC entry 3602 (class 0 OID 0)
-- Dependencies: 231
-- Name: COLUMN sys_user.user_id; Type: COMMENT; Schema: biz_sys; Owner: wz
--

COMMENT ON COLUMN biz_sys.sys_user.user_id IS '管理员ID';


--
-- TOC entry 3603 (class 0 OID 0)
-- Dependencies: 231
-- Name: COLUMN sys_user.tenant_id; Type: COMMENT; Schema: biz_sys; Owner: wz
--

COMMENT ON COLUMN biz_sys.sys_user.tenant_id IS '租户ID';


--
-- TOC entry 3604 (class 0 OID 0)
-- Dependencies: 231
-- Name: COLUMN sys_user.dept_id; Type: COMMENT; Schema: biz_sys; Owner: wz
--

COMMENT ON COLUMN biz_sys.sys_user.dept_id IS '部门ID';


--
-- TOC entry 3605 (class 0 OID 0)
-- Dependencies: 231
-- Name: COLUMN sys_user.username; Type: COMMENT; Schema: biz_sys; Owner: wz
--

COMMENT ON COLUMN biz_sys.sys_user.username IS '用户账号';


--
-- TOC entry 3606 (class 0 OID 0)
-- Dependencies: 231
-- Name: COLUMN sys_user.email; Type: COMMENT; Schema: biz_sys; Owner: wz
--

COMMENT ON COLUMN biz_sys.sys_user.email IS '用户邮箱';


--
-- TOC entry 3607 (class 0 OID 0)
-- Dependencies: 231
-- Name: COLUMN sys_user.nickname; Type: COMMENT; Schema: biz_sys; Owner: wz
--

COMMENT ON COLUMN biz_sys.sys_user.nickname IS '用户昵称';


--
-- TOC entry 3608 (class 0 OID 0)
-- Dependencies: 231
-- Name: COLUMN sys_user.phone_number; Type: COMMENT; Schema: biz_sys; Owner: wz
--

COMMENT ON COLUMN biz_sys.sys_user.phone_number IS '手机号码';


--
-- TOC entry 3609 (class 0 OID 0)
-- Dependencies: 231
-- Name: COLUMN sys_user.status; Type: COMMENT; Schema: biz_sys; Owner: wz
--

COMMENT ON COLUMN biz_sys.sys_user.status IS '状态值';


--
-- TOC entry 3610 (class 0 OID 0)
-- Dependencies: 231
-- Name: COLUMN sys_user.gender; Type: COMMENT; Schema: biz_sys; Owner: wz
--

COMMENT ON COLUMN biz_sys.sys_user.gender IS '用户性别（0男 1女 2未知）';


--
-- TOC entry 3611 (class 0 OID 0)
-- Dependencies: 231
-- Name: COLUMN sys_user.avatar; Type: COMMENT; Schema: biz_sys; Owner: wz
--

COMMENT ON COLUMN biz_sys.sys_user.avatar IS '头像地址';


--
-- TOC entry 3612 (class 0 OID 0)
-- Dependencies: 231
-- Name: COLUMN sys_user.password; Type: COMMENT; Schema: biz_sys; Owner: wz
--

COMMENT ON COLUMN biz_sys.sys_user.password IS '密码';


--
-- TOC entry 3613 (class 0 OID 0)
-- Dependencies: 231
-- Name: COLUMN sys_user.login_ip; Type: COMMENT; Schema: biz_sys; Owner: wz
--

COMMENT ON COLUMN biz_sys.sys_user.login_ip IS '登录ip';


--
-- TOC entry 3614 (class 0 OID 0)
-- Dependencies: 231
-- Name: COLUMN sys_user.login_date; Type: COMMENT; Schema: biz_sys; Owner: wz
--

COMMENT ON COLUMN biz_sys.sys_user.login_date IS '登录时间';


--
-- TOC entry 3615 (class 0 OID 0)
-- Dependencies: 231
-- Name: COLUMN sys_user.remark; Type: COMMENT; Schema: biz_sys; Owner: wz
--

COMMENT ON COLUMN biz_sys.sys_user.remark IS '备注';


--
-- TOC entry 3616 (class 0 OID 0)
-- Dependencies: 231
-- Name: COLUMN sys_user.creator_id; Type: COMMENT; Schema: biz_sys; Owner: wz
--

COMMENT ON COLUMN biz_sys.sys_user.creator_id IS '创建者';


--
-- TOC entry 3617 (class 0 OID 0)
-- Dependencies: 231
-- Name: COLUMN sys_user.updater_id; Type: COMMENT; Schema: biz_sys; Owner: wz
--

COMMENT ON COLUMN biz_sys.sys_user.updater_id IS '更新者';


--
-- TOC entry 232 (class 1259 OID 35518)
-- Name: sys_user_post; Type: TABLE; Schema: biz_sys; Owner: wz
--

CREATE TABLE biz_sys.sys_user_post (
    user_id bigint NOT NULL,
    post_id bigint NOT NULL
);


ALTER TABLE biz_sys.sys_user_post OWNER TO wz;

--
-- TOC entry 3618 (class 0 OID 0)
-- Dependencies: 232
-- Name: TABLE sys_user_post; Type: COMMENT; Schema: biz_sys; Owner: wz
--

COMMENT ON TABLE biz_sys.sys_user_post IS '用户与岗位关联表';


--
-- TOC entry 3619 (class 0 OID 0)
-- Dependencies: 232
-- Name: COLUMN sys_user_post.user_id; Type: COMMENT; Schema: biz_sys; Owner: wz
--

COMMENT ON COLUMN biz_sys.sys_user_post.user_id IS '管理员ID';


--
-- TOC entry 3620 (class 0 OID 0)
-- Dependencies: 232
-- Name: COLUMN sys_user_post.post_id; Type: COMMENT; Schema: biz_sys; Owner: wz
--

COMMENT ON COLUMN biz_sys.sys_user_post.post_id IS '岗位ID';


--
-- TOC entry 233 (class 1259 OID 35521)
-- Name: sys_user_role; Type: TABLE; Schema: biz_sys; Owner: wz
--

CREATE TABLE biz_sys.sys_user_role (
    user_id bigint NOT NULL,
    role_id bigint NOT NULL
);


ALTER TABLE biz_sys.sys_user_role OWNER TO wz;

--
-- TOC entry 3621 (class 0 OID 0)
-- Dependencies: 233
-- Name: TABLE sys_user_role; Type: COMMENT; Schema: biz_sys; Owner: wz
--

COMMENT ON TABLE biz_sys.sys_user_role IS '用户和角色关联表';


--
-- TOC entry 3622 (class 0 OID 0)
-- Dependencies: 233
-- Name: COLUMN sys_user_role.user_id; Type: COMMENT; Schema: biz_sys; Owner: wz
--

COMMENT ON COLUMN biz_sys.sys_user_role.user_id IS '管理员ID';


--
-- TOC entry 3623 (class 0 OID 0)
-- Dependencies: 233
-- Name: COLUMN sys_user_role.role_id; Type: COMMENT; Schema: biz_sys; Owner: wz
--

COMMENT ON COLUMN biz_sys.sys_user_role.role_id IS '角色ID';


--
-- TOC entry 234 (class 1259 OID 35524)
-- Name: sys_withdrawal_record; Type: TABLE; Schema: biz_sys; Owner: wz
--

CREATE TABLE biz_sys.sys_withdrawal_record (
    withdrawal_id bigint NOT NULL,
    order_no character varying(32) NOT NULL,
    status character(1) NOT NULL,
    tenant_id bigint NOT NULL,
    request_params character varying(300) NOT NULL,
    amount money NOT NULL,
    error_msg character varying(100),
    creator_id bigint NOT NULL,
    create_at timestamp(6) with time zone NOT NULL,
    complete_at timestamp with time zone,
    remark character varying(100)
);


ALTER TABLE biz_sys.sys_withdrawal_record OWNER TO wz;

--
-- TOC entry 3624 (class 0 OID 0)
-- Dependencies: 234
-- Name: TABLE sys_withdrawal_record; Type: COMMENT; Schema: biz_sys; Owner: wz
--

COMMENT ON TABLE biz_sys.sys_withdrawal_record IS '系统提现记录';


--
-- TOC entry 3625 (class 0 OID 0)
-- Dependencies: 234
-- Name: COLUMN sys_withdrawal_record.withdrawal_id; Type: COMMENT; Schema: biz_sys; Owner: wz
--

COMMENT ON COLUMN biz_sys.sys_withdrawal_record.withdrawal_id IS 'id';


--
-- TOC entry 3626 (class 0 OID 0)
-- Dependencies: 234
-- Name: COLUMN sys_withdrawal_record.order_no; Type: COMMENT; Schema: biz_sys; Owner: wz
--

COMMENT ON COLUMN biz_sys.sys_withdrawal_record.order_no IS '订单号';


--
-- TOC entry 3627 (class 0 OID 0)
-- Dependencies: 234
-- Name: COLUMN sys_withdrawal_record.status; Type: COMMENT; Schema: biz_sys; Owner: wz
--

COMMENT ON COLUMN biz_sys.sys_withdrawal_record.status IS '状态
''0'' 处理中
''1'' 成功
''2'' 失败';


--
-- TOC entry 3628 (class 0 OID 0)
-- Dependencies: 234
-- Name: COLUMN sys_withdrawal_record.tenant_id; Type: COMMENT; Schema: biz_sys; Owner: wz
--

COMMENT ON COLUMN biz_sys.sys_withdrawal_record.tenant_id IS '租户id';


--
-- TOC entry 3629 (class 0 OID 0)
-- Dependencies: 234
-- Name: COLUMN sys_withdrawal_record.request_params; Type: COMMENT; Schema: biz_sys; Owner: wz
--

COMMENT ON COLUMN biz_sys.sys_withdrawal_record.request_params IS '第三方请求参数';


--
-- TOC entry 3630 (class 0 OID 0)
-- Dependencies: 234
-- Name: COLUMN sys_withdrawal_record.amount; Type: COMMENT; Schema: biz_sys; Owner: wz
--

COMMENT ON COLUMN biz_sys.sys_withdrawal_record.amount IS '金额, 单位元';


--
-- TOC entry 3631 (class 0 OID 0)
-- Dependencies: 234
-- Name: COLUMN sys_withdrawal_record.error_msg; Type: COMMENT; Schema: biz_sys; Owner: wz
--

COMMENT ON COLUMN biz_sys.sys_withdrawal_record.error_msg IS '错误信息';


--
-- TOC entry 3632 (class 0 OID 0)
-- Dependencies: 234
-- Name: COLUMN sys_withdrawal_record.creator_id; Type: COMMENT; Schema: biz_sys; Owner: wz
--

COMMENT ON COLUMN biz_sys.sys_withdrawal_record.creator_id IS '创建者';


--
-- TOC entry 3633 (class 0 OID 0)
-- Dependencies: 234
-- Name: COLUMN sys_withdrawal_record.create_at; Type: COMMENT; Schema: biz_sys; Owner: wz
--

COMMENT ON COLUMN biz_sys.sys_withdrawal_record.create_at IS '创建时间';


--
-- TOC entry 3634 (class 0 OID 0)
-- Dependencies: 234
-- Name: COLUMN sys_withdrawal_record.complete_at; Type: COMMENT; Schema: biz_sys; Owner: wz
--

COMMENT ON COLUMN biz_sys.sys_withdrawal_record.complete_at IS '完成时间';


--
-- TOC entry 3635 (class 0 OID 0)
-- Dependencies: 234
-- Name: COLUMN sys_withdrawal_record.remark; Type: COMMENT; Schema: biz_sys; Owner: wz
--

COMMENT ON COLUMN biz_sys.sys_withdrawal_record.remark IS '备注';


--
-- TOC entry 3450 (class 0 OID 35447)
-- Dependencies: 216
-- Data for Name: app_user; Type: TABLE DATA; Schema: biz_app; Owner: wz
--

COPY biz_app.app_user (user_id, nickname, phone_number, status, gender, avatar, login_ip, login_date, creator_id, updater_id, create_at, update_at) FROM stdin;
1826896461245968384	\N	15888888888	0	2	\N	127.0.0.1	2024-11-30 13:53:49+08	1	1	2024-04-17 14:08:54.616+08	2025-03-28 08:46:42.496+08
\.


--
-- TOC entry 3451 (class 0 OID 35452)
-- Dependencies: 217
-- Data for Name: app_user_thirdinfo; Type: TABLE DATA; Schema: biz_app; Owner: wz
--

COPY biz_app.app_user_thirdinfo (user_id, identifier, identifier_type) FROM stdin;
\.


--
-- TOC entry 3452 (class 0 OID 35455)
-- Dependencies: 218
-- Data for Name: app_user_wallet; Type: TABLE DATA; Schema: biz_app; Owner: wz
--

COPY biz_app.app_user_wallet (user_id, balance, status) FROM stdin;
1826896461245968384	0.00	0
\.


--
-- TOC entry 3453 (class 0 OID 35458)
-- Dependencies: 219
-- Data for Name: app_user_wallet_record; Type: TABLE DATA; Schema: biz_app; Owner: wz
--

COPY biz_app.app_user_wallet_record (record_id, user_id, amount, record_type, create_at, remark) FROM stdin;
\.


--
-- TOC entry 3454 (class 0 OID 35461)
-- Dependencies: 220
-- Data for Name: oauth2_client; Type: TABLE DATA; Schema: biz_sys; Owner: wz
--

COPY biz_sys.oauth2_client (id, client_name, client_id, client_secret, scopes, authorization_grant_types, redirect_uris, status, auto_approve, create_at, creator_id, update_at, updater_id) FROM stdin;
1	系统	server	{bcrypt}$2a$10$hK9Sv9kAvXE00fWtkWxzI.Ns4.5SuQteTJAnsFWXChlOWIUZSFYL2	{openid}	{password,sms,refresh_token}	{http://localhost:9001/oauth2/authorization_code_callback}	0	f	2024-04-17 14:08:54+08	1	2025-03-30 16:07:17.752+08	1
\.


--
-- TOC entry 3455 (class 0 OID 35469)
-- Dependencies: 221
-- Data for Name: sys_dept; Type: TABLE DATA; Schema: biz_sys; Owner: wz
--

COPY biz_sys.sys_dept (dept_id, tenant_id, parent_id, ancestors, dept_name, status, dept_sort, contact, email, creator_id, updater_id, create_at, update_at) FROM stdin;
\.


--
-- TOC entry 3456 (class 0 OID 35476)
-- Dependencies: 222
-- Data for Name: sys_menu; Type: TABLE DATA; Schema: biz_sys; Owner: wz
--

COPY biz_sys.sys_menu (menu_id, menu_name, parent_id, menu_sort, path, component, query, menu_type, status, perms, icon, is_cache, is_visible, create_at, creator_id, update_at, updater_id) FROM stdin;
1906263415450000001	系统管理	0	100	system	\N	\N	D	0	\N	carbon:tool-kit	f	t	2024-05-26 12:30:16+08	1	2025-03-28 10:52:58+08	1
1906272685360099330	钱包记录	1906272182215585793	0	#	\N	\N	B	0	tenant:wallet_record:list	#	f	t	2025-03-30 17:09:59.695+08	1	2025-03-30 17:10:43.013+08	1
1906263415450000203	系统用户	1906263415450000002	100	sysuser	user/sysuser/index		M	0	sys_user:list	carbon:user-admin	t	t	2024-05-26 12:30:16+08	1	2025-03-31 12:42:29.349+08	1
1906263415450000101	控制台入口	1906263415450000001	0	controller	\N	\N	D	0	\N	carbon:dashboard	f	t	2024-05-26 12:30:16+08	1	2025-03-28 10:41:57+08	1
1906263415450001049	字典删除	1906263415450000102	4	#	\N	\N	B	0	sys_dict:remove	#	f	t	2024-05-26 12:30:16+08	1	2025-03-28 09:13:19+08	1
1906263415450000700	终端管理	1906263415450000003	3	client	platform/client/index	\N	M	0	oauth2_client:list	carbon:application	f	t	2024-05-26 12:30:16+08	1	2025-03-30 16:35:42.841+08	1
1906263415450000601	租户管理	1906263415450000003	100	systenant	platform/systenant/index	\N	M	0	sys_tenant:list	carbon:id-management	f	t	2024-05-26 12:30:16+08	1	2025-03-30 17:01:58.956+08	1
1906263415450001062	操作日志敏感字段	1906263415450000150	10	#	\N	\N	B	0	operlog:field	#	f	t	2024-05-26 12:30:16+08	1	2024-05-26 12:30:16+08	1
1906263415450001051	参数查询	1906263415450000103	1	#	\N	\N	B	0	sys_config:query	#	f	t	2024-05-26 12:30:16+08	1	2025-03-28 09:18:05+08	1
1906263415450001052	参数新增	1906263415450000103	2	#	\N	\N	B	0	sys_config:add	#	f	t	2024-05-26 12:30:16+08	1	2025-03-28 09:17:59+08	1
1906263415450001053	参数修改	1906263415450000103	3	#	\N	\N	B	0	sys_config:edit	#	f	t	2024-05-26 12:30:16+08	1	2025-03-28 09:17:52+08	1
1906263415450001054	参数删除	1906263415450000103	4	#	\N	\N	B	0	sys_config:remove	#	f	t	2024-05-26 12:30:16+08	1	2025-03-28 09:17:47+08	1
1906263415450001055	参数导出	1906263415450000103	5	#	\N	\N	B	0	sys_config:export	#	f	t	2024-05-26 12:30:16+08	1	2025-03-28 09:17:42+08	1
1906263415450001056	公告查询	1906263415450000100	1	#	\N	\N	B	0	sys_message:query	#	f	t	2024-05-26 12:30:16+08	1	2024-05-26 12:30:16+08	1
1906263415450001057	公告新增	1906263415450000100	2	#	\N	\N	B	0	sys_message:add	#	f	t	2024-05-26 12:30:16+08	1	2024-05-26 12:30:16+08	1
1906263415450001058	公告修改	1906263415450000100	3	#	\N	\N	B	0	sys_message:edit	#	f	t	2024-05-26 12:30:16+08	1	2024-05-26 12:30:16+08	1
1906263415450001059	公告删除	1906263415450000100	4	#	\N	\N	B	0	sys_message:remove	#	f	t	2024-05-26 12:30:16+08	1	2024-05-26 12:30:16+08	1
1906263415450001061	操作删除	1906263415450000150	2	#	\N	\N	B	0	operlog:remove	#	f	t	2024-05-26 12:30:16+08	1	2024-05-26 12:30:16+08	\N
1906263415450001064	删除记录	1906263415450000151	2	#	\N	\N	B	0	loginlog:remove	#	f	t	2024-05-26 12:30:16+08	1	2024-05-26 12:30:16+08	1
1906263415450000201	顾客管理	1906263415450000003	1	appuser	platform/appuser/index	\N	M	0	app_user:list	carbon:customer	f	t	2024-05-26 12:30:16+08	1	2025-03-30 15:58:04.043+08	1
1906263415450001135	套餐详情	1906263415450000602	9	#	\N	\N	B	0	tenant_package:query	#	f	t	2024-05-26 12:30:16+08	1	2024-05-26 12:30:16+08	1
1906263415450001136	套餐新增	1906263415450000602	5	#	\N	\N	B	0	tenant_package:add	#	f	t	2024-05-26 12:30:16+08	1	2024-05-26 12:30:16+08	1
1906263415450001137	套餐修改	1906263415450000602	4	#	\N	\N	B	0	tenant_package:edit	#	f	t	2024-05-26 12:30:16+08	1	2024-05-26 12:30:16+08	1
1906263415450001138	套餐删除	1906263415450000602	2	#	\N	\N	B	0	tenant_package:remove	#	f	t	2024-05-26 12:30:16+08	1	2024-05-26 12:30:16+08	1
1906263415450000205	部门管理	1906263415450000002	70	sysdept	user/sysdept/index	\N	M	0	sys_dept:list	carbon:departure	f	t	2024-05-26 12:30:16+08	1	2025-03-28 11:03:44+08	1
1906263415450000206	角色管理	1906263415450000002	99	sysrole	user/sysrole/index	\N	M	0	sys_role:list	carbon:user-role	f	t	2024-05-26 12:30:16+08	1	2025-03-28 11:00:45+08	1
1906263415450000208	岗位管理	1906263415450000002	8	syspost	user/syspost/index	\N	M	0	sys_post:list	carbon:user-service-desk	f	t	2024-05-26 12:30:16+08	1	2025-03-28 11:05:10+08	1
1906263415450001046	字典查询	1906263415450000102	1	#	\N	\N	B	0	sys_dict:query	#	f	t	2024-05-26 12:30:16+08	1	2025-03-28 09:13:44+08	1
1906263415450001047	字典新增	1906263415450000102	2	#	\N	\N	B	0	sys_dict:add	#	f	t	2024-05-26 12:30:16+08	1	2025-03-28 09:13:34+08	1
1906263415450001048	字典修改	1906263415450000102	3	#	\N	\N	B	0	sys_dict:edit	#	f	t	2024-05-26 12:30:16+08	1	2025-03-28 09:13:28+08	1
1906263415450001215	修改密钥	1906263415450000700	5	#	\N	\N	B	0	oauth2_client:edit_secret	#	f	t	2024-05-26 12:30:16+08	1	2024-05-26 12:30:16+08	1
1906263415450002001	用户查询	1906263415450000201	1	#	\N	\N	B	0	app_user:query	#	f	t	2024-05-26 12:30:16+08	1	2024-05-26 12:30:16+08	1
1906263415450002002	用户新增	1906263415450000201	2	#	\N	\N	B	0	app_user:add	#	f	t	2024-05-26 12:30:16+08	1	2024-05-26 12:30:16+08	1
1906263415450002003	用户修改	1906263415450000201	3	#	\N	\N	B	0	app_user:edit	#	f	t	2024-05-26 12:30:16+08	1	2024-05-26 12:30:16+08	1
1906263415450002004	用户删除	1906263415450000201	4	#	\N	\N	B	0	app_user:remove	#	f	t	2024-05-26 12:30:16+08	1	2024-05-26 12:30:16+08	1
1906263415450002005	用户导出	1906263415450000201	5	#	\N	\N	B	0	app_user:export	#	f	t	2024-05-26 12:30:16+08	1	2024-05-26 12:30:16+08	1
1906263415450002013	菜单查询	1906263415450000207	1	#	\N	\N	B	0	sys_menu:query	#	f	t	2024-05-26 12:30:16+08	1	2025-03-28 09:30:22+08	1
1906263415450000207	菜单管理	1906263415450000003	50	sysmenu	platform/sysmenu/index		M	0	sys_menu:list	carbon:menu	f	t	2024-05-26 12:30:16+08	1	2025-04-01 13:01:05.19+08	1
1906263415450000100	消息管理	1906263415450000001	15	sysmessage	system/sysmessage/index	\N	M	0	sys_message:list	carbon:message-queue	f	t	2024-05-26 12:30:16+08	1	2025-04-01 14:20:11.099+08	1
1906263415450000103	参数管理	1906263415450000001	7	sysconfig	system/sysconfig/index	\N	M	0	sys_config:list	carbon:parameter	f	t	2024-05-26 12:30:16+08	1	2025-04-01 14:20:18.372+08	1
1906263415450000602	租户套餐管理	1906263415450000003	50	systenant/package	platform/systenant/package/index	\N	M	0	tenant_package:list	carbon:package	f	t	2024-05-26 12:30:16+08	1	2025-04-01 14:21:55.071+08	1
1906263415450000104	日志管理	1906263415450000001	1	syslog	\N	\N	D	0	\N	carbon:ibm-knowledge-catalog-premium	f	t	2024-05-26 12:30:16+08	1	2025-04-01 14:33:36.928+08	1
1906263415450000150	操作日志	1906263415450000104	1	oper	system/syslog/oper/index	\N	M	0	operlog:list	carbon:touch-interaction	f	t	2024-05-26 12:30:16+08	1	2025-04-01 14:35:38.829+08	1
1906263415450000151	登录日志	1906263415450000104	2	login	system/syslog/login/index	\N	M	0	loginlog:list	carbon:login	f	t	2024-05-26 12:30:16+08	1	2025-04-01 14:35:23.263+08	1
1906272182215585793	商户信息	0	100	tenant	\N	\N	M	0	tenant:info	carbon:information-filled	f	f	2025-03-30 17:07:59.723+08	1	2025-04-07 09:09:08.254+08	1
1906263415450000003	平台管理	0	80	platform	\N	\N	D	0	\N	carbon:platforms	f	t	2024-05-26 12:30:16+08	1	2025-04-03 15:03:24.342+08	1
1906263415450000002	用户管理	0	50	user	\N	\N	D	0	\N	carbon:user	f	t	2024-05-26 12:30:16+08	1	2025-04-03 15:03:20.926+08	1
1906263415450001214	终端导出	1906263415450000700	4	#	\N	\N	B	0	oauth2_client:export	#	f	t	2024-05-26 12:30:16+08	1	2025-04-11 14:02:11.674+08	1
1906263415450001212	终端添加	1906263415450000700	3	#	\N	\N	B	0	oauth2_client:add	#	f	t	2024-05-26 12:30:16+08	1	2025-04-11 14:02:18.31+08	1
1906263415450001213	终端删除	1906263415450000700	2	#	\N	\N	B	0	oauth2_client:remove	#	f	t	2024-05-26 12:30:16+08	1	2025-04-11 14:02:25.507+08	1
1906263415450001211	终端修改	1906263415450000700	2	#	\N	\N	B	0	oauth2_client:edit	#	f	t	2024-05-26 12:30:16+08	1	2025-04-11 14:02:31.062+08	1
1906263415450001210	终端详情	1906263415450000700	1	#	\N	\N	B	0	oauth2_client:query	#	f	t	2024-05-26 12:30:16+08	1	2025-04-11 14:02:38.251+08	1
1906263415450002014	菜单新增	1906263415450000207	2	#	\N	\N	B	0	sys_menu:add	#	f	t	2024-05-26 12:30:16+08	1	2025-03-28 09:30:17+08	1
1906263415450002015	菜单修改	1906263415450000207	3	#	\N	\N	B	0	sys_menu:edit	#	f	t	2024-05-26 12:30:16+08	1	2025-03-28 09:30:10+08	1
1906263415450002016	菜单删除	1906263415450000207	4	#	\N	\N	B	0	sys_menu:remove	#	f	t	2024-05-26 12:30:16+08	1	2025-03-28 09:30:05+08	1
1906263415450002037	部门查询	1906263415450000205	1	#	\N	\N	B	0	sys_dept:query	#	f	t	2024-05-26 12:30:16+08	1	2025-03-28 09:28:16+08	1
1906263415450002038	部门新增	1906263415450000205	2	#	\N	\N	B	0	sys_dept:add	#	f	t	2024-05-26 12:30:16+08	1	2025-03-28 09:28:09+08	1
1906263415450002039	部门修改	1906263415450000205	3	#	\N	\N	B	0	sys_dept:edit	#	f	t	2024-05-26 12:30:16+08	1	2025-03-28 09:28:02+08	1
1906263415450002040	部门删除	1906263415450000205	4	#	\N	\N	B	0	sys_dept:remove	#	f	t	2024-05-26 12:30:16+08	1	2025-03-28 09:27:57+08	1
1906263415450002062	重置密码	1906263415450000203	7	#	\N	\N	B	0	sys_user:resetPwd	#	f	t	2024-05-26 12:30:16+08	1	2024-05-26 12:30:16+08	1
1906263415450002064	用户修改	1906263415450000203	3	#	\N	\N	B	0	sys_user:edit	#	f	t	2024-05-26 12:30:16+08	1	2024-05-26 12:30:16+08	1
1906263415450002071	用户查询	1906263415450000203	0	#	\N	\N	B	0	sys_user:query	#	f	t	2024-05-26 12:30:16+08	1	2024-05-26 12:30:16+08	1
1906263415450002072	用户添加	1906263415450000203	1	#	\N	\N	B	0	sys_user:add	#	f	t	2024-05-26 12:30:16+08	1	2024-05-26 12:30:16+08	1
1906263415450002077	用户导出	1906263415450000203	1	#	\N	\N	B	0	sys_user:export	#	f	t	2024-05-26 12:30:16+08	1	2024-05-26 12:30:16+08	1
1906263415450002078	用户删除	1906263415450000203	8	#	\N	\N	B	0	sys_user:remove	#	f	t	2024-05-26 12:30:16+08	1	2024-05-26 12:30:16+08	1
1906263415450002141	岗位查询	1906263415450000208	1	#	\N	\N	B	0	sys_post:query	#	f	t	2024-05-26 12:30:16+08	1	2025-03-28 09:32:52+08	1
1906263415450002142	岗位新增	1906263415450000208	2	#	\N	\N	B	0	sys_post:add	#	f	t	2024-05-26 12:30:16+08	1	2025-03-28 09:32:47+08	1
1906263415450001129	重置租户操作密码	1906263415450000601	11	#	\N	\N	B	0	sys_tenant:reset_operpwd	#	f	t	2024-05-26 12:30:16+08	1	2025-03-30 17:02:06.416+08	1
1906263415450001131	租户详情	1906263415450000601	9	#	\N	\N	B	0	sys_tenant:query	#	f	t	2024-05-26 12:30:16+08	1	2025-03-30 17:02:12.375+08	1
1906263415450001132	租户新增	1906263415450000601	4	#	\N	\N	B	0	sys_tenant:add	#	f	t	2024-05-26 12:30:16+08	1	2025-03-30 17:02:33.503+08	1
1906263415450001134	租户删除	1906263415450000601	2	#	\N	\N	B	0	sys_tenant:remove	#	f	t	2024-05-26 12:30:16+08	1	2025-03-30 17:02:42.95+08	1
1906263415450001133	租户修改	1906263415450000601	2	#	\N	\N	B	0	sys_tenant:edit	#	f	t	2024-05-26 12:30:16+08	1	2025-03-30 17:02:51.756+08	1
1906263415450001125	余额记录	1906263415450000601	3	#	\N	\N	B	0	sys_tenant:wallet_record:list	#	f	t	2024-05-26 12:30:16+08	1	2025-03-30 17:05:07.775+08	1
1906263415450001126	商户提现	1906272182215585793	1	#	\N	\N	B	0	tenant:withdrawal	#	f	t	2024-05-26 12:30:16+08	1	2025-03-30 17:08:30.431+08	1
1906263415450001127	商户钱包	1906272182215585793	0	#	\N	\N	B	0	tenant:wallet_info	#	f	t	2024-05-26 12:30:16+08	1	2025-03-30 17:09:33.83+08	1
1906263415450000302	Sentinel控制台	1906263415450000101	3	http://localhost:8718	\N	\N	O	0	monitor:sentinel:list	carbon:link	f	t	2024-05-26 12:30:16+08	1	2025-03-28 10:58:59+08	1
1906263415450000304	服务监控	1906263415450000101	5	http://localhost:9100/	\N	\N	O	0	monitor:server:list	carbon:link	f	t	2024-05-26 12:30:16+08	1	2025-03-30 10:08:19+08	1
1906263415450002143	岗位修改	1906263415450000208	3	#	\N	\N	B	0	sys_post:edit	#	f	t	2024-05-26 12:30:16+08	1	2025-03-28 09:32:42+08	1
1906263415450002144	岗位删除	1906263415450000208	4	#	\N	\N	B	0	sys_post:remove	#	f	t	2024-05-26 12:30:16+08	1	2025-03-28 09:32:37+08	1
1906263415450002208	角色查询	1906263415450000206	1	#	\N	\N	B	0	sys_role:query	#	f	t	2024-05-26 12:30:16+08	1	2024-05-26 12:30:16+08	1
1906263415450002209	角色新增	1906263415450000206	2	#	\N	\N	B	0	sys_role:add	#	f	t	2024-05-26 12:30:16+08	1	2024-05-26 12:30:16+08	1
1906263415450002210	角色修改	1906263415450000206	3	#	\N	\N	B	0	sys_role:edit	#	f	t	2024-05-26 12:30:16+08	1	2024-05-26 12:30:16+08	1
1906263415450002211	角色删除	1906263415450000206	4	#	\N	\N	B	0	sys_role:remove	#	f	t	2024-05-26 12:30:16+08	1	2024-05-26 12:30:16+08	1
1906263415450000300	定时任务	1906263415450000101	20	http://localhost:9200/xxl-job-admin	\N	\N	I	0	tool:job:list	carbon:link	f	t	2024-05-26 12:30:16+08	1	2025-04-01 08:47:28.417+08	1
1906263415450000301	系统接口	1906263415450000101	2	http://localhost:8080/doc.html	\N	\N	I	0	tool:swagger:list	carbon:link	f	t	2024-05-26 12:30:16+08	1	2025-04-01 12:38:18.133+08	1
1906263415450000303	Nacos控制台	1906263415450000101	4	http://localhost:8848/nacos	\N	\N	O	0	monitor:nacos:list	carbon:link	f	t	2024-05-26 12:30:16+08	1	2025-04-01 12:39:17.991+08	1
1906263415450000102	字典管理	1906263415450000001	6	sysdict	system/sysdict/index	\N	M	0	sys_dict:list	carbon:text-vertical-alignment	f	t	2024-05-26 12:30:16+08	1	2025-04-01 14:20:22.958+08	1
1910569625749024770	授权角色	1906263415450000203	0	#	\N	\N	B	0	sys_user:grant_role	#	f	t	2025-04-11 13:44:30.104+08	1	2025-04-11 13:44:30.104+08	1
1906263415450002207	权限授予	1906263415450000206	6	#	\N	\N	B	0	sys_role:grant_user	#	f	t	2024-05-26 12:30:16+08	1	2025-04-11 13:55:09.036+08	1
\.


--
-- TOC entry 3457 (class 0 OID 35481)
-- Dependencies: 223
-- Data for Name: sys_post; Type: TABLE DATA; Schema: biz_sys; Owner: wz
--

COPY biz_sys.sys_post (post_id, tenant_id, post_code, post_name, status, post_sort, creator_id, updater_id, create_at, update_at) FROM stdin;
4	0	CTO	首席技术官	0	3	1	1	2024-04-17 14:08:54.616+08	2025-03-28 10:10:31.986+08
1	0	CEO	执行总裁	0	1	1	1	2024-04-17 14:08:54.616+08	2025-03-24 14:27:35.367+08
\.


--
-- TOC entry 3458 (class 0 OID 35484)
-- Dependencies: 224
-- Data for Name: sys_role; Type: TABLE DATA; Schema: biz_sys; Owner: wz
--

COPY biz_sys.sys_role (role_id, tenant_id, data_scope, role_name, status, role_sort, is_menu_display, is_dept_display, create_at, creator_id, update_at, updater_id) FROM stdin;
\.


--
-- TOC entry 3459 (class 0 OID 35489)
-- Dependencies: 225
-- Data for Name: sys_role_dept; Type: TABLE DATA; Schema: biz_sys; Owner: wz
--

COPY biz_sys.sys_role_dept (role_id, dept_id) FROM stdin;
\.


--
-- TOC entry 3460 (class 0 OID 35492)
-- Dependencies: 226
-- Data for Name: sys_role_menu; Type: TABLE DATA; Schema: biz_sys; Owner: wz
--

COPY biz_sys.sys_role_menu (role_id, menu_id) FROM stdin;
\.


--
-- TOC entry 3461 (class 0 OID 35495)
-- Dependencies: 227
-- Data for Name: sys_tenant; Type: TABLE DATA; Schema: biz_sys; Owner: wz
--

COPY biz_sys.sys_tenant (tenant_id, administrator, tenant_type, contact_phone, tenant_name, oper_pwd, status, domain, remark, package_id, expire_time, account_limit, role_limit, post_limit, dept_limit, creator_id, create_at, updater_id, update_at) FROM stdin;
1910557183820165122	1910557183820165120	0	\N	test	{bcrypt}$2a$10$1UJgROjrOvMKJD4way7dKeBsJuLGVLWGy/pBGooa.sFqfsP3Vrupm	0	\N	\N	1773625804122202113	2025-04-20 00:00:00+08	5	5	5	5	1	2025-04-11 12:55:03.715+08	1	2025-04-11 12:55:03.715+08
\.


--
-- TOC entry 3462 (class 0 OID 35498)
-- Dependencies: 228
-- Data for Name: sys_tenant_package; Type: TABLE DATA; Schema: biz_sys; Owner: wz
--

COPY biz_sys.sys_tenant_package (package_id, package_name, status, menu_ids, remark, creator_id, create_at, updater_id, update_at) FROM stdin;
1773625804122202113	默认套餐	0	{1906263415450000002,1906272182215585793,1906263415450001126,1906272685360099330,1906263415450001127,1906263415450000203,1906263415450002078,1906263415450002062,1906263415450002064,1906263415450002077,1906263415450002072,1906263415450002071,1906263415450000206,1906263415450002207,1906263415450002211,1906263415450002210,1906263415450002209,1906263415450002208,1906263415450000205,1906263415450002040,1906263415450002039,1906263415450002038,1906263415450002037,1906263415450000208,1906263415450002144,1906263415450002143,1906263415450002142,1906263415450002141}	通用租户套餐	1	2024-04-17 14:08:54+08	1	2025-03-31 15:57:51.642+08
\.


--
-- TOC entry 3463 (class 0 OID 35505)
-- Dependencies: 229
-- Data for Name: sys_tenant_wallet; Type: TABLE DATA; Schema: biz_sys; Owner: wz
--

COPY biz_sys.sys_tenant_wallet (tenant_id, balance, status) FROM stdin;
1910557183820165122	0.00	0
\.


--
-- TOC entry 3464 (class 0 OID 35508)
-- Dependencies: 230
-- Data for Name: sys_tenant_wallet_record; Type: TABLE DATA; Schema: biz_sys; Owner: wz
--

COPY biz_sys.sys_tenant_wallet_record (record_id, tenant_id, amount, record_type, create_at, remark) FROM stdin;
\.


--
-- TOC entry 3465 (class 0 OID 35511)
-- Dependencies: 231
-- Data for Name: sys_user; Type: TABLE DATA; Schema: biz_sys; Owner: wz
--

COPY biz_sys.sys_user (user_id, tenant_id, dept_id, username, email, nickname, phone_number, status, gender, avatar, password, login_ip, login_date, remark, creator_id, updater_id, create_at, update_at) FROM stdin;
1	0	\N	admin	xxxxx@163.com	nick_a	15888888888	0	1	https://img-s-msn-com.akamaized.net/tenant/amp/entityid/AA1B91c8.img?w=660&h=648&m=6&x=219&y=147&s=204&d=204	{bcrypt}$2a$10$7JB720yubVSZvUI0rEqK/.VqGOZTH.ulu33dHOiBE8ByOhJIrdAu2	192.168.0.112	2025-04-11 14:08:46.335+08	\N	1	\N	2024-04-17 14:08:54.616+08	\N
1910557183820165120	1910557183820165122	\N	testadmin	\N	\N	\N	0	2	\N	{bcrypt}$2a$10$7JB720yubVSZvUI0rEqK/.VqGOZTH.ulu33dHOiBE8ByOhJIrdAu2	192.168.0.112	2025-04-11 14:04:07.486+08	\N	1	1910557183820165120	2025-04-11 12:55:03.816+08	2025-04-11 14:08:30.064+08
\.


--
-- TOC entry 3466 (class 0 OID 35518)
-- Dependencies: 232
-- Data for Name: sys_user_post; Type: TABLE DATA; Schema: biz_sys; Owner: wz
--

COPY biz_sys.sys_user_post (user_id, post_id) FROM stdin;
\.


--
-- TOC entry 3467 (class 0 OID 35521)
-- Dependencies: 233
-- Data for Name: sys_user_role; Type: TABLE DATA; Schema: biz_sys; Owner: wz
--

COPY biz_sys.sys_user_role (user_id, role_id) FROM stdin;
\.


--
-- TOC entry 3468 (class 0 OID 35524)
-- Dependencies: 234
-- Data for Name: sys_withdrawal_record; Type: TABLE DATA; Schema: biz_sys; Owner: wz
--

COPY biz_sys.sys_withdrawal_record (withdrawal_id, order_no, status, tenant_id, request_params, amount, error_msg, creator_id, create_at, complete_at, remark) FROM stdin;
\.


--
-- TOC entry 3259 (class 2606 OID 35530)
-- Name: app_user app_user_pkey; Type: CONSTRAINT; Schema: biz_app; Owner: wz
--

ALTER TABLE ONLY biz_app.app_user
    ADD CONSTRAINT app_user_pkey PRIMARY KEY (user_id);


--
-- TOC entry 3262 (class 2606 OID 35532)
-- Name: app_user_thirdinfo app_user_thirdinfo_pkey; Type: CONSTRAINT; Schema: biz_app; Owner: wz
--

ALTER TABLE ONLY biz_app.app_user_thirdinfo
    ADD CONSTRAINT app_user_thirdinfo_pkey PRIMARY KEY (user_id);


--
-- TOC entry 3265 (class 2606 OID 35534)
-- Name: app_user_wallet app_user_wallet_pkey; Type: CONSTRAINT; Schema: biz_app; Owner: wz
--

ALTER TABLE ONLY biz_app.app_user_wallet
    ADD CONSTRAINT app_user_wallet_pkey PRIMARY KEY (user_id);


--
-- TOC entry 3267 (class 2606 OID 35536)
-- Name: app_user_wallet_record app_user_wallet_record_pkey; Type: CONSTRAINT; Schema: biz_app; Owner: wz
--

ALTER TABLE ONLY biz_app.app_user_wallet_record
    ADD CONSTRAINT app_user_wallet_record_pkey PRIMARY KEY (record_id);


--
-- TOC entry 3270 (class 2606 OID 35538)
-- Name: oauth2_client oauth2_client_pkey; Type: CONSTRAINT; Schema: biz_sys; Owner: wz
--

ALTER TABLE ONLY biz_sys.oauth2_client
    ADD CONSTRAINT oauth2_client_pkey PRIMARY KEY (id);


--
-- TOC entry 3275 (class 2606 OID 35540)
-- Name: sys_dept sys_dept_pkey; Type: CONSTRAINT; Schema: biz_sys; Owner: wz
--

ALTER TABLE ONLY biz_sys.sys_dept
    ADD CONSTRAINT sys_dept_pkey PRIMARY KEY (dept_id);


--
-- TOC entry 3277 (class 2606 OID 35542)
-- Name: sys_menu sys_menu_copy1_pkey1; Type: CONSTRAINT; Schema: biz_sys; Owner: wz
--

ALTER TABLE ONLY biz_sys.sys_menu
    ADD CONSTRAINT sys_menu_copy1_pkey1 PRIMARY KEY (menu_id);


--
-- TOC entry 3280 (class 2606 OID 35544)
-- Name: sys_post sys_post_pkey; Type: CONSTRAINT; Schema: biz_sys; Owner: wz
--

ALTER TABLE ONLY biz_sys.sys_post
    ADD CONSTRAINT sys_post_pkey PRIMARY KEY (post_id);


--
-- TOC entry 3284 (class 2606 OID 35546)
-- Name: sys_role_dept sys_role_dept_pkey; Type: CONSTRAINT; Schema: biz_sys; Owner: wz
--

ALTER TABLE ONLY biz_sys.sys_role_dept
    ADD CONSTRAINT sys_role_dept_pkey PRIMARY KEY (role_id, dept_id);


--
-- TOC entry 3286 (class 2606 OID 35548)
-- Name: sys_role_menu sys_role_menu_pkey; Type: CONSTRAINT; Schema: biz_sys; Owner: wz
--

ALTER TABLE ONLY biz_sys.sys_role_menu
    ADD CONSTRAINT sys_role_menu_pkey PRIMARY KEY (role_id, menu_id);


--
-- TOC entry 3282 (class 2606 OID 35550)
-- Name: sys_role sys_role_pkey; Type: CONSTRAINT; Schema: biz_sys; Owner: wz
--

ALTER TABLE ONLY biz_sys.sys_role
    ADD CONSTRAINT sys_role_pkey PRIMARY KEY (role_id);


--
-- TOC entry 3291 (class 2606 OID 35552)
-- Name: sys_tenant_package sys_tenant_package_pkey; Type: CONSTRAINT; Schema: biz_sys; Owner: wz
--

ALTER TABLE ONLY biz_sys.sys_tenant_package
    ADD CONSTRAINT sys_tenant_package_pkey PRIMARY KEY (package_id);


--
-- TOC entry 3288 (class 2606 OID 35554)
-- Name: sys_tenant sys_tenant_pkey; Type: CONSTRAINT; Schema: biz_sys; Owner: wz
--

ALTER TABLE ONLY biz_sys.sys_tenant
    ADD CONSTRAINT sys_tenant_pkey PRIMARY KEY (tenant_id);


--
-- TOC entry 3293 (class 2606 OID 35556)
-- Name: sys_tenant_wallet sys_tenant_wallet_pkey; Type: CONSTRAINT; Schema: biz_sys; Owner: wz
--

ALTER TABLE ONLY biz_sys.sys_tenant_wallet
    ADD CONSTRAINT sys_tenant_wallet_pkey PRIMARY KEY (tenant_id);


--
-- TOC entry 3296 (class 2606 OID 35558)
-- Name: sys_tenant_wallet_record sys_tenant_wallet_record_pkey; Type: CONSTRAINT; Schema: biz_sys; Owner: wz
--

ALTER TABLE ONLY biz_sys.sys_tenant_wallet_record
    ADD CONSTRAINT sys_tenant_wallet_record_pkey PRIMARY KEY (record_id);


--
-- TOC entry 3298 (class 2606 OID 35560)
-- Name: sys_user sys_user_pkey; Type: CONSTRAINT; Schema: biz_sys; Owner: wz
--

ALTER TABLE ONLY biz_sys.sys_user
    ADD CONSTRAINT sys_user_pkey PRIMARY KEY (user_id);


--
-- TOC entry 3302 (class 2606 OID 35562)
-- Name: sys_user_post sys_user_post_pkey; Type: CONSTRAINT; Schema: biz_sys; Owner: wz
--

ALTER TABLE ONLY biz_sys.sys_user_post
    ADD CONSTRAINT sys_user_post_pkey PRIMARY KEY (user_id, post_id);


--
-- TOC entry 3304 (class 2606 OID 35564)
-- Name: sys_user_role sys_user_role_pkey; Type: CONSTRAINT; Schema: biz_sys; Owner: wz
--

ALTER TABLE ONLY biz_sys.sys_user_role
    ADD CONSTRAINT sys_user_role_pkey PRIMARY KEY (user_id, role_id);


--
-- TOC entry 3307 (class 2606 OID 35566)
-- Name: sys_withdrawal_record sys_withdrawal_record_pkey; Type: CONSTRAINT; Schema: biz_sys; Owner: wz
--

ALTER TABLE ONLY biz_sys.sys_withdrawal_record
    ADD CONSTRAINT sys_withdrawal_record_pkey PRIMARY KEY (withdrawal_id);


--
-- TOC entry 3268 (class 1259 OID 35567)
-- Name: i_app_user_wallet_record_user_id; Type: INDEX; Schema: biz_app; Owner: wz
--

CREATE INDEX i_app_user_wallet_record_user_id ON biz_app.app_user_wallet_record USING btree (user_id);


--
-- TOC entry 3260 (class 1259 OID 35568)
-- Name: u_i_app_user_phone_number; Type: INDEX; Schema: biz_app; Owner: wz
--

CREATE UNIQUE INDEX u_i_app_user_phone_number ON biz_app.app_user USING btree (phone_number);


--
-- TOC entry 3263 (class 1259 OID 35569)
-- Name: u_i_identifier; Type: INDEX; Schema: biz_app; Owner: wz
--

CREATE UNIQUE INDEX u_i_identifier ON biz_app.app_user_thirdinfo USING btree (identifier);


--
-- TOC entry 3272 (class 1259 OID 35570)
-- Name: sys_dept_i_ancestors; Type: INDEX; Schema: biz_sys; Owner: wz
--

CREATE INDEX sys_dept_i_ancestors ON biz_sys.sys_dept USING btree (ancestors);


--
-- TOC entry 3273 (class 1259 OID 35571)
-- Name: sys_dept_i_parent_id; Type: INDEX; Schema: biz_sys; Owner: wz
--

CREATE INDEX sys_dept_i_parent_id ON biz_sys.sys_dept USING btree (parent_id);


--
-- TOC entry 3278 (class 1259 OID 35572)
-- Name: sys_menu_i_parent_id_copy1_copy1; Type: INDEX; Schema: biz_sys; Owner: wz
--

CREATE INDEX sys_menu_i_parent_id_copy1_copy1 ON biz_sys.sys_menu USING btree (parent_id);


--
-- TOC entry 3294 (class 1259 OID 35573)
-- Name: sys_tenant_wallet_record_i_tenant_id; Type: INDEX; Schema: biz_sys; Owner: wz
--

CREATE INDEX sys_tenant_wallet_record_i_tenant_id ON biz_sys.sys_tenant_wallet_record USING btree (tenant_id);


--
-- TOC entry 3305 (class 1259 OID 35574)
-- Name: sys_withdrawal_record_i_order_no; Type: INDEX; Schema: biz_sys; Owner: wz
--

CREATE UNIQUE INDEX sys_withdrawal_record_i_order_no ON biz_sys.sys_withdrawal_record USING btree (order_no);


--
-- TOC entry 3289 (class 1259 OID 35575)
-- Name: u_i_administrator; Type: INDEX; Schema: biz_sys; Owner: wz
--

CREATE UNIQUE INDEX u_i_administrator ON biz_sys.sys_tenant USING btree (administrator);


--
-- TOC entry 3271 (class 1259 OID 35576)
-- Name: u_i_client_id; Type: INDEX; Schema: biz_sys; Owner: wz
--

CREATE UNIQUE INDEX u_i_client_id ON biz_sys.oauth2_client USING btree (client_id);


--
-- TOC entry 3299 (class 1259 OID 35577)
-- Name: u_i_sys_user_phone_number; Type: INDEX; Schema: biz_sys; Owner: wz
--

CREATE UNIQUE INDEX u_i_sys_user_phone_number ON biz_sys.sys_user USING btree (phone_number);


--
-- TOC entry 3300 (class 1259 OID 35578)
-- Name: u_i_sys_user_username; Type: INDEX; Schema: biz_sys; Owner: wz
--

CREATE UNIQUE INDEX u_i_sys_user_username ON biz_sys.sys_user USING btree (username);


-- Completed on 2025-04-11 14:10:05

--
-- PostgreSQL database dump complete
--

