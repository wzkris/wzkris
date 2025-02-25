--
-- PostgreSQL database dump
--

-- Dumped from database version 15.5
-- Dumped by pg_dump version 16.0

-- Started on 2025-03-03 13:47:22

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
-- TOC entry 6 (class 2615 OID 33061)
-- Name: biz_app; Type: SCHEMA; Schema: -; Owner: wz
--

CREATE SCHEMA biz_app;


ALTER SCHEMA biz_app OWNER TO wz;

--
-- TOC entry 7 (class 2615 OID 34565)
-- Name: biz_sys; Type: SCHEMA; Schema: -; Owner: wz
--

CREATE SCHEMA biz_sys;


ALTER SCHEMA biz_sys OWNER TO wz;

SET default_tablespace = '';

SET default_table_access_method = heap;

--
-- TOC entry 216 (class 1259 OID 33062)
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
-- TOC entry 3472 (class 0 OID 0)
-- Dependencies: 216
-- Name: TABLE app_user; Type: COMMENT; Schema: biz_app; Owner: wz
--

COMMENT ON TABLE biz_app.app_user IS '用户信息表';


--
-- TOC entry 3473 (class 0 OID 0)
-- Dependencies: 216
-- Name: COLUMN app_user.user_id; Type: COMMENT; Schema: biz_app; Owner: wz
--

COMMENT ON COLUMN biz_app.app_user.user_id IS '用户ID';


--
-- TOC entry 3474 (class 0 OID 0)
-- Dependencies: 216
-- Name: COLUMN app_user.nickname; Type: COMMENT; Schema: biz_app; Owner: wz
--

COMMENT ON COLUMN biz_app.app_user.nickname IS '用户昵称';


--
-- TOC entry 3475 (class 0 OID 0)
-- Dependencies: 216
-- Name: COLUMN app_user.phone_number; Type: COMMENT; Schema: biz_app; Owner: wz
--

COMMENT ON COLUMN biz_app.app_user.phone_number IS '手机号码';


--
-- TOC entry 3476 (class 0 OID 0)
-- Dependencies: 216
-- Name: COLUMN app_user.status; Type: COMMENT; Schema: biz_app; Owner: wz
--

COMMENT ON COLUMN biz_app.app_user.status IS '状态值';


--
-- TOC entry 3477 (class 0 OID 0)
-- Dependencies: 216
-- Name: COLUMN app_user.gender; Type: COMMENT; Schema: biz_app; Owner: wz
--

COMMENT ON COLUMN biz_app.app_user.gender IS '用户性别（0男 1女 2未知）';


--
-- TOC entry 3478 (class 0 OID 0)
-- Dependencies: 216
-- Name: COLUMN app_user.avatar; Type: COMMENT; Schema: biz_app; Owner: wz
--

COMMENT ON COLUMN biz_app.app_user.avatar IS '头像地址';


--
-- TOC entry 3479 (class 0 OID 0)
-- Dependencies: 216
-- Name: COLUMN app_user.login_ip; Type: COMMENT; Schema: biz_app; Owner: wz
--

COMMENT ON COLUMN biz_app.app_user.login_ip IS '登录ip';


--
-- TOC entry 3480 (class 0 OID 0)
-- Dependencies: 216
-- Name: COLUMN app_user.login_date; Type: COMMENT; Schema: biz_app; Owner: wz
--

COMMENT ON COLUMN biz_app.app_user.login_date IS '登录时间';


--
-- TOC entry 3481 (class 0 OID 0)
-- Dependencies: 216
-- Name: COLUMN app_user.creator_id; Type: COMMENT; Schema: biz_app; Owner: wz
--

COMMENT ON COLUMN biz_app.app_user.creator_id IS '创建者';


--
-- TOC entry 3482 (class 0 OID 0)
-- Dependencies: 216
-- Name: COLUMN app_user.updater_id; Type: COMMENT; Schema: biz_app; Owner: wz
--

COMMENT ON COLUMN biz_app.app_user.updater_id IS '更新者';


--
-- TOC entry 217 (class 1259 OID 33067)
-- Name: app_user_thirdinfo; Type: TABLE; Schema: biz_app; Owner: wz
--

CREATE TABLE biz_app.app_user_thirdinfo (
    user_id bigint NOT NULL,
    identifier character varying(32) NOT NULL,
    identifier_type character varying(10) NOT NULL
);


ALTER TABLE biz_app.app_user_thirdinfo OWNER TO wz;

--
-- TOC entry 3483 (class 0 OID 0)
-- Dependencies: 217
-- Name: TABLE app_user_thirdinfo; Type: COMMENT; Schema: biz_app; Owner: wz
--

COMMENT ON TABLE biz_app.app_user_thirdinfo IS '第三方信息';


--
-- TOC entry 3484 (class 0 OID 0)
-- Dependencies: 217
-- Name: COLUMN app_user_thirdinfo.identifier; Type: COMMENT; Schema: biz_app; Owner: wz
--

COMMENT ON COLUMN biz_app.app_user_thirdinfo.identifier IS '三方唯一标识符';


--
-- TOC entry 3485 (class 0 OID 0)
-- Dependencies: 217
-- Name: COLUMN app_user_thirdinfo.identifier_type; Type: COMMENT; Schema: biz_app; Owner: wz
--

COMMENT ON COLUMN biz_app.app_user_thirdinfo.identifier_type IS '三方渠道';


--
-- TOC entry 218 (class 1259 OID 33073)
-- Name: app_user_wallet; Type: TABLE; Schema: biz_app; Owner: wz
--

CREATE TABLE biz_app.app_user_wallet (
    user_id bigint NOT NULL,
    balance numeric(10,2) NOT NULL,
    status character(1) NOT NULL
);


ALTER TABLE biz_app.app_user_wallet OWNER TO wz;

--
-- TOC entry 3486 (class 0 OID 0)
-- Dependencies: 218
-- Name: TABLE app_user_wallet; Type: COMMENT; Schema: biz_app; Owner: wz
--

COMMENT ON TABLE biz_app.app_user_wallet IS '用户钱包';


--
-- TOC entry 3487 (class 0 OID 0)
-- Dependencies: 218
-- Name: COLUMN app_user_wallet.balance; Type: COMMENT; Schema: biz_app; Owner: wz
--

COMMENT ON COLUMN biz_app.app_user_wallet.balance IS '余额, 元';


--
-- TOC entry 3488 (class 0 OID 0)
-- Dependencies: 218
-- Name: COLUMN app_user_wallet.status; Type: COMMENT; Schema: biz_app; Owner: wz
--

COMMENT ON COLUMN biz_app.app_user_wallet.status IS '状态';


--
-- TOC entry 219 (class 1259 OID 33078)
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
-- TOC entry 3489 (class 0 OID 0)
-- Dependencies: 219
-- Name: TABLE app_user_wallet_record; Type: COMMENT; Schema: biz_app; Owner: wz
--

COMMENT ON TABLE biz_app.app_user_wallet_record IS '用户钱包记录';


--
-- TOC entry 3490 (class 0 OID 0)
-- Dependencies: 219
-- Name: COLUMN app_user_wallet_record.user_id; Type: COMMENT; Schema: biz_app; Owner: wz
--

COMMENT ON COLUMN biz_app.app_user_wallet_record.user_id IS '用户ID';


--
-- TOC entry 3491 (class 0 OID 0)
-- Dependencies: 219
-- Name: COLUMN app_user_wallet_record.amount; Type: COMMENT; Schema: biz_app; Owner: wz
--

COMMENT ON COLUMN biz_app.app_user_wallet_record.amount IS '金额, 元';


--
-- TOC entry 3492 (class 0 OID 0)
-- Dependencies: 219
-- Name: COLUMN app_user_wallet_record.record_type; Type: COMMENT; Schema: biz_app; Owner: wz
--

COMMENT ON COLUMN biz_app.app_user_wallet_record.record_type IS '记录类型';


--
-- TOC entry 3493 (class 0 OID 0)
-- Dependencies: 219
-- Name: COLUMN app_user_wallet_record.create_at; Type: COMMENT; Schema: biz_app; Owner: wz
--

COMMENT ON COLUMN biz_app.app_user_wallet_record.create_at IS '创建时间';


--
-- TOC entry 3494 (class 0 OID 0)
-- Dependencies: 219
-- Name: COLUMN app_user_wallet_record.remark; Type: COMMENT; Schema: biz_app; Owner: wz
--

COMMENT ON COLUMN biz_app.app_user_wallet_record.remark IS '备注';


--
-- TOC entry 220 (class 1259 OID 34566)
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
-- TOC entry 3495 (class 0 OID 0)
-- Dependencies: 220
-- Name: TABLE oauth2_client; Type: COMMENT; Schema: biz_sys; Owner: wz
--

COMMENT ON TABLE biz_sys.oauth2_client IS 'OAUTH2客户端';


--
-- TOC entry 3496 (class 0 OID 0)
-- Dependencies: 220
-- Name: COLUMN oauth2_client.client_name; Type: COMMENT; Schema: biz_sys; Owner: wz
--

COMMENT ON COLUMN biz_sys.oauth2_client.client_name IS '客户端名称';


--
-- TOC entry 3497 (class 0 OID 0)
-- Dependencies: 220
-- Name: COLUMN oauth2_client.scopes; Type: COMMENT; Schema: biz_sys; Owner: wz
--

COMMENT ON COLUMN biz_sys.oauth2_client.scopes IS '权限域';


--
-- TOC entry 3498 (class 0 OID 0)
-- Dependencies: 220
-- Name: COLUMN oauth2_client.authorization_grant_types; Type: COMMENT; Schema: biz_sys; Owner: wz
--

COMMENT ON COLUMN biz_sys.oauth2_client.authorization_grant_types IS '授权类型';


--
-- TOC entry 3499 (class 0 OID 0)
-- Dependencies: 220
-- Name: COLUMN oauth2_client.redirect_uris; Type: COMMENT; Schema: biz_sys; Owner: wz
--

COMMENT ON COLUMN biz_sys.oauth2_client.redirect_uris IS '回调地址';


--
-- TOC entry 3500 (class 0 OID 0)
-- Dependencies: 220
-- Name: COLUMN oauth2_client.status; Type: COMMENT; Schema: biz_sys; Owner: wz
--

COMMENT ON COLUMN biz_sys.oauth2_client.status IS '客户端状态';


--
-- TOC entry 3501 (class 0 OID 0)
-- Dependencies: 220
-- Name: COLUMN oauth2_client.auto_approve; Type: COMMENT; Schema: biz_sys; Owner: wz
--

COMMENT ON COLUMN biz_sys.oauth2_client.auto_approve IS '是否自动放行';


--
-- TOC entry 221 (class 1259 OID 34574)
-- Name: sys_dept; Type: TABLE; Schema: biz_sys; Owner: wz
--

CREATE TABLE biz_sys.sys_dept (
    dept_id bigint NOT NULL,
    tenant_id bigint NOT NULL,
    parent_id bigint NOT NULL,
    ancestors character varying(500) NOT NULL,
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
-- TOC entry 3502 (class 0 OID 0)
-- Dependencies: 221
-- Name: TABLE sys_dept; Type: COMMENT; Schema: biz_sys; Owner: wz
--

COMMENT ON TABLE biz_sys.sys_dept IS '部门表';


--
-- TOC entry 3503 (class 0 OID 0)
-- Dependencies: 221
-- Name: COLUMN sys_dept.dept_id; Type: COMMENT; Schema: biz_sys; Owner: wz
--

COMMENT ON COLUMN biz_sys.sys_dept.dept_id IS '部门id';


--
-- TOC entry 3504 (class 0 OID 0)
-- Dependencies: 221
-- Name: COLUMN sys_dept.tenant_id; Type: COMMENT; Schema: biz_sys; Owner: wz
--

COMMENT ON COLUMN biz_sys.sys_dept.tenant_id IS '租户ID';


--
-- TOC entry 3505 (class 0 OID 0)
-- Dependencies: 221
-- Name: COLUMN sys_dept.parent_id; Type: COMMENT; Schema: biz_sys; Owner: wz
--

COMMENT ON COLUMN biz_sys.sys_dept.parent_id IS '父部门id';


--
-- TOC entry 3506 (class 0 OID 0)
-- Dependencies: 221
-- Name: COLUMN sys_dept.ancestors; Type: COMMENT; Schema: biz_sys; Owner: wz
--

COMMENT ON COLUMN biz_sys.sys_dept.ancestors IS '祖级列表';


--
-- TOC entry 3507 (class 0 OID 0)
-- Dependencies: 221
-- Name: COLUMN sys_dept.dept_name; Type: COMMENT; Schema: biz_sys; Owner: wz
--

COMMENT ON COLUMN biz_sys.sys_dept.dept_name IS '部门名称';


--
-- TOC entry 3508 (class 0 OID 0)
-- Dependencies: 221
-- Name: COLUMN sys_dept.status; Type: COMMENT; Schema: biz_sys; Owner: wz
--

COMMENT ON COLUMN biz_sys.sys_dept.status IS '0代表正常 1代表停用';


--
-- TOC entry 3509 (class 0 OID 0)
-- Dependencies: 221
-- Name: COLUMN sys_dept.dept_sort; Type: COMMENT; Schema: biz_sys; Owner: wz
--

COMMENT ON COLUMN biz_sys.sys_dept.dept_sort IS '显示顺序';


--
-- TOC entry 3510 (class 0 OID 0)
-- Dependencies: 221
-- Name: COLUMN sys_dept.contact; Type: COMMENT; Schema: biz_sys; Owner: wz
--

COMMENT ON COLUMN biz_sys.sys_dept.contact IS '联系电话';


--
-- TOC entry 3511 (class 0 OID 0)
-- Dependencies: 221
-- Name: COLUMN sys_dept.email; Type: COMMENT; Schema: biz_sys; Owner: wz
--

COMMENT ON COLUMN biz_sys.sys_dept.email IS '邮箱';


--
-- TOC entry 3512 (class 0 OID 0)
-- Dependencies: 221
-- Name: COLUMN sys_dept.creator_id; Type: COMMENT; Schema: biz_sys; Owner: wz
--

COMMENT ON COLUMN biz_sys.sys_dept.creator_id IS '创建者';


--
-- TOC entry 3513 (class 0 OID 0)
-- Dependencies: 221
-- Name: COLUMN sys_dept.updater_id; Type: COMMENT; Schema: biz_sys; Owner: wz
--

COMMENT ON COLUMN biz_sys.sys_dept.updater_id IS '更新者';


--
-- TOC entry 234 (class 1259 OID 35226)
-- Name: sys_menu; Type: TABLE; Schema: biz_sys; Owner: wz
--

CREATE TABLE biz_sys.sys_menu (
    menu_id bigint NOT NULL,
    menu_name character varying(30) NOT NULL,
    parent_id bigint NOT NULL,
    menu_sort integer NOT NULL,
    path character varying(50) NOT NULL,
    component character varying(50),
    query character varying(50),
    menu_type character(1) NOT NULL,
    status character(1) NOT NULL,
    perms character varying(30),
    icon character varying(30) DEFAULT '#'::character varying NOT NULL,
    is_frame boolean NOT NULL,
    is_cache boolean NOT NULL,
    is_visible boolean NOT NULL,
    create_at timestamp(6) with time zone NOT NULL,
    creator_id bigint NOT NULL,
    update_at timestamp(6) with time zone,
    updater_id bigint
);


ALTER TABLE biz_sys.sys_menu OWNER TO wz;

--
-- TOC entry 3514 (class 0 OID 0)
-- Dependencies: 234
-- Name: TABLE sys_menu; Type: COMMENT; Schema: biz_sys; Owner: wz
--

COMMENT ON TABLE biz_sys.sys_menu IS '菜单权限表';


--
-- TOC entry 3515 (class 0 OID 0)
-- Dependencies: 234
-- Name: COLUMN sys_menu.menu_id; Type: COMMENT; Schema: biz_sys; Owner: wz
--

COMMENT ON COLUMN biz_sys.sys_menu.menu_id IS '菜单ID';


--
-- TOC entry 3516 (class 0 OID 0)
-- Dependencies: 234
-- Name: COLUMN sys_menu.menu_name; Type: COMMENT; Schema: biz_sys; Owner: wz
--

COMMENT ON COLUMN biz_sys.sys_menu.menu_name IS '菜单名称';


--
-- TOC entry 3517 (class 0 OID 0)
-- Dependencies: 234
-- Name: COLUMN sys_menu.parent_id; Type: COMMENT; Schema: biz_sys; Owner: wz
--

COMMENT ON COLUMN biz_sys.sys_menu.parent_id IS '父菜单ID';


--
-- TOC entry 3518 (class 0 OID 0)
-- Dependencies: 234
-- Name: COLUMN sys_menu.menu_sort; Type: COMMENT; Schema: biz_sys; Owner: wz
--

COMMENT ON COLUMN biz_sys.sys_menu.menu_sort IS '显示顺序';


--
-- TOC entry 3519 (class 0 OID 0)
-- Dependencies: 234
-- Name: COLUMN sys_menu.path; Type: COMMENT; Schema: biz_sys; Owner: wz
--

COMMENT ON COLUMN biz_sys.sys_menu.path IS '路由地址';


--
-- TOC entry 3520 (class 0 OID 0)
-- Dependencies: 234
-- Name: COLUMN sys_menu.component; Type: COMMENT; Schema: biz_sys; Owner: wz
--

COMMENT ON COLUMN biz_sys.sys_menu.component IS '组件路径';


--
-- TOC entry 3521 (class 0 OID 0)
-- Dependencies: 234
-- Name: COLUMN sys_menu.query; Type: COMMENT; Schema: biz_sys; Owner: wz
--

COMMENT ON COLUMN biz_sys.sys_menu.query IS '路由参数';


--
-- TOC entry 3522 (class 0 OID 0)
-- Dependencies: 234
-- Name: COLUMN sys_menu.menu_type; Type: COMMENT; Schema: biz_sys; Owner: wz
--

COMMENT ON COLUMN biz_sys.sys_menu.menu_type IS '菜单类型（M目录 C菜单 F按钮）';


--
-- TOC entry 3523 (class 0 OID 0)
-- Dependencies: 234
-- Name: COLUMN sys_menu.status; Type: COMMENT; Schema: biz_sys; Owner: wz
--

COMMENT ON COLUMN biz_sys.sys_menu.status IS '菜单状态（0正常 1停用）';


--
-- TOC entry 3524 (class 0 OID 0)
-- Dependencies: 234
-- Name: COLUMN sys_menu.perms; Type: COMMENT; Schema: biz_sys; Owner: wz
--

COMMENT ON COLUMN biz_sys.sys_menu.perms IS '权限标识';


--
-- TOC entry 3525 (class 0 OID 0)
-- Dependencies: 234
-- Name: COLUMN sys_menu.icon; Type: COMMENT; Schema: biz_sys; Owner: wz
--

COMMENT ON COLUMN biz_sys.sys_menu.icon IS '菜单图标';


--
-- TOC entry 3526 (class 0 OID 0)
-- Dependencies: 234
-- Name: COLUMN sys_menu.is_frame; Type: COMMENT; Schema: biz_sys; Owner: wz
--

COMMENT ON COLUMN biz_sys.sys_menu.is_frame IS '是否为外链';


--
-- TOC entry 3527 (class 0 OID 0)
-- Dependencies: 234
-- Name: COLUMN sys_menu.is_cache; Type: COMMENT; Schema: biz_sys; Owner: wz
--

COMMENT ON COLUMN biz_sys.sys_menu.is_cache IS '是否缓存';


--
-- TOC entry 3528 (class 0 OID 0)
-- Dependencies: 234
-- Name: COLUMN sys_menu.is_visible; Type: COMMENT; Schema: biz_sys; Owner: wz
--

COMMENT ON COLUMN biz_sys.sys_menu.is_visible IS '是否显示';


--
-- TOC entry 3529 (class 0 OID 0)
-- Dependencies: 234
-- Name: COLUMN sys_menu.create_at; Type: COMMENT; Schema: biz_sys; Owner: wz
--

COMMENT ON COLUMN biz_sys.sys_menu.create_at IS '创建时间';


--
-- TOC entry 3530 (class 0 OID 0)
-- Dependencies: 234
-- Name: COLUMN sys_menu.creator_id; Type: COMMENT; Schema: biz_sys; Owner: wz
--

COMMENT ON COLUMN biz_sys.sys_menu.creator_id IS '创建者';


--
-- TOC entry 3531 (class 0 OID 0)
-- Dependencies: 234
-- Name: COLUMN sys_menu.update_at; Type: COMMENT; Schema: biz_sys; Owner: wz
--

COMMENT ON COLUMN biz_sys.sys_menu.update_at IS '更新时间';


--
-- TOC entry 3532 (class 0 OID 0)
-- Dependencies: 234
-- Name: COLUMN sys_menu.updater_id; Type: COMMENT; Schema: biz_sys; Owner: wz
--

COMMENT ON COLUMN biz_sys.sys_menu.updater_id IS '更新者';


--
-- TOC entry 222 (class 1259 OID 34583)
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
-- TOC entry 3533 (class 0 OID 0)
-- Dependencies: 222
-- Name: TABLE sys_post; Type: COMMENT; Schema: biz_sys; Owner: wz
--

COMMENT ON TABLE biz_sys.sys_post IS '岗位信息表';


--
-- TOC entry 3534 (class 0 OID 0)
-- Dependencies: 222
-- Name: COLUMN sys_post.post_id; Type: COMMENT; Schema: biz_sys; Owner: wz
--

COMMENT ON COLUMN biz_sys.sys_post.post_id IS '岗位ID';


--
-- TOC entry 3535 (class 0 OID 0)
-- Dependencies: 222
-- Name: COLUMN sys_post.tenant_id; Type: COMMENT; Schema: biz_sys; Owner: wz
--

COMMENT ON COLUMN biz_sys.sys_post.tenant_id IS '租户ID';


--
-- TOC entry 3536 (class 0 OID 0)
-- Dependencies: 222
-- Name: COLUMN sys_post.post_code; Type: COMMENT; Schema: biz_sys; Owner: wz
--

COMMENT ON COLUMN biz_sys.sys_post.post_code IS '岗位编码';


--
-- TOC entry 3537 (class 0 OID 0)
-- Dependencies: 222
-- Name: COLUMN sys_post.post_name; Type: COMMENT; Schema: biz_sys; Owner: wz
--

COMMENT ON COLUMN biz_sys.sys_post.post_name IS '岗位名称';


--
-- TOC entry 3538 (class 0 OID 0)
-- Dependencies: 222
-- Name: COLUMN sys_post.status; Type: COMMENT; Schema: biz_sys; Owner: wz
--

COMMENT ON COLUMN biz_sys.sys_post.status IS '状态（0正常 1停用）';


--
-- TOC entry 3539 (class 0 OID 0)
-- Dependencies: 222
-- Name: COLUMN sys_post.post_sort; Type: COMMENT; Schema: biz_sys; Owner: wz
--

COMMENT ON COLUMN biz_sys.sys_post.post_sort IS '显示顺序';


--
-- TOC entry 3540 (class 0 OID 0)
-- Dependencies: 222
-- Name: COLUMN sys_post.creator_id; Type: COMMENT; Schema: biz_sys; Owner: wz
--

COMMENT ON COLUMN biz_sys.sys_post.creator_id IS '创建者';


--
-- TOC entry 3541 (class 0 OID 0)
-- Dependencies: 222
-- Name: COLUMN sys_post.updater_id; Type: COMMENT; Schema: biz_sys; Owner: wz
--

COMMENT ON COLUMN biz_sys.sys_post.updater_id IS '更新者';


--
-- TOC entry 223 (class 1259 OID 34586)
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
-- TOC entry 3542 (class 0 OID 0)
-- Dependencies: 223
-- Name: TABLE sys_role; Type: COMMENT; Schema: biz_sys; Owner: wz
--

COMMENT ON TABLE biz_sys.sys_role IS '角色信息表';


--
-- TOC entry 3543 (class 0 OID 0)
-- Dependencies: 223
-- Name: COLUMN sys_role.role_id; Type: COMMENT; Schema: biz_sys; Owner: wz
--

COMMENT ON COLUMN biz_sys.sys_role.role_id IS '角色ID';


--
-- TOC entry 3544 (class 0 OID 0)
-- Dependencies: 223
-- Name: COLUMN sys_role.tenant_id; Type: COMMENT; Schema: biz_sys; Owner: wz
--

COMMENT ON COLUMN biz_sys.sys_role.tenant_id IS '租户ID';


--
-- TOC entry 3545 (class 0 OID 0)
-- Dependencies: 223
-- Name: COLUMN sys_role.data_scope; Type: COMMENT; Schema: biz_sys; Owner: wz
--

COMMENT ON COLUMN biz_sys.sys_role.data_scope IS '数据范围（1：全部数据权限 2：自定数据权限 3：本部门数据权限 4：本部门及以下数据权限）';


--
-- TOC entry 3546 (class 0 OID 0)
-- Dependencies: 223
-- Name: COLUMN sys_role.role_name; Type: COMMENT; Schema: biz_sys; Owner: wz
--

COMMENT ON COLUMN biz_sys.sys_role.role_name IS '角色名称';


--
-- TOC entry 3547 (class 0 OID 0)
-- Dependencies: 223
-- Name: COLUMN sys_role.status; Type: COMMENT; Schema: biz_sys; Owner: wz
--

COMMENT ON COLUMN biz_sys.sys_role.status IS '状态（0正常，1停用）';


--
-- TOC entry 3548 (class 0 OID 0)
-- Dependencies: 223
-- Name: COLUMN sys_role.role_sort; Type: COMMENT; Schema: biz_sys; Owner: wz
--

COMMENT ON COLUMN biz_sys.sys_role.role_sort IS '显示顺序';


--
-- TOC entry 3549 (class 0 OID 0)
-- Dependencies: 223
-- Name: COLUMN sys_role.is_menu_display; Type: COMMENT; Schema: biz_sys; Owner: wz
--

COMMENT ON COLUMN biz_sys.sys_role.is_menu_display IS '菜单选项是否关联显示';


--
-- TOC entry 3550 (class 0 OID 0)
-- Dependencies: 223
-- Name: COLUMN sys_role.is_dept_display; Type: COMMENT; Schema: biz_sys; Owner: wz
--

COMMENT ON COLUMN biz_sys.sys_role.is_dept_display IS '部门选项是否关联显示';


--
-- TOC entry 3551 (class 0 OID 0)
-- Dependencies: 223
-- Name: COLUMN sys_role.create_at; Type: COMMENT; Schema: biz_sys; Owner: wz
--

COMMENT ON COLUMN biz_sys.sys_role.create_at IS '创建时间';


--
-- TOC entry 3552 (class 0 OID 0)
-- Dependencies: 223
-- Name: COLUMN sys_role.creator_id; Type: COMMENT; Schema: biz_sys; Owner: wz
--

COMMENT ON COLUMN biz_sys.sys_role.creator_id IS '创建者';


--
-- TOC entry 3553 (class 0 OID 0)
-- Dependencies: 223
-- Name: COLUMN sys_role.update_at; Type: COMMENT; Schema: biz_sys; Owner: wz
--

COMMENT ON COLUMN biz_sys.sys_role.update_at IS '更新时间';


--
-- TOC entry 3554 (class 0 OID 0)
-- Dependencies: 223
-- Name: COLUMN sys_role.updater_id; Type: COMMENT; Schema: biz_sys; Owner: wz
--

COMMENT ON COLUMN biz_sys.sys_role.updater_id IS '更新者';


--
-- TOC entry 224 (class 1259 OID 34591)
-- Name: sys_role_dept; Type: TABLE; Schema: biz_sys; Owner: wz
--

CREATE TABLE biz_sys.sys_role_dept (
    role_id bigint NOT NULL,
    dept_id bigint NOT NULL
);


ALTER TABLE biz_sys.sys_role_dept OWNER TO wz;

--
-- TOC entry 3555 (class 0 OID 0)
-- Dependencies: 224
-- Name: TABLE sys_role_dept; Type: COMMENT; Schema: biz_sys; Owner: wz
--

COMMENT ON TABLE biz_sys.sys_role_dept IS '角色数据权限关联表';


--
-- TOC entry 3556 (class 0 OID 0)
-- Dependencies: 224
-- Name: COLUMN sys_role_dept.role_id; Type: COMMENT; Schema: biz_sys; Owner: wz
--

COMMENT ON COLUMN biz_sys.sys_role_dept.role_id IS '角色id';


--
-- TOC entry 3557 (class 0 OID 0)
-- Dependencies: 224
-- Name: COLUMN sys_role_dept.dept_id; Type: COMMENT; Schema: biz_sys; Owner: wz
--

COMMENT ON COLUMN biz_sys.sys_role_dept.dept_id IS '部门id';


--
-- TOC entry 225 (class 1259 OID 34594)
-- Name: sys_role_menu; Type: TABLE; Schema: biz_sys; Owner: wz
--

CREATE TABLE biz_sys.sys_role_menu (
    role_id bigint NOT NULL,
    menu_id bigint NOT NULL
);


ALTER TABLE biz_sys.sys_role_menu OWNER TO wz;

--
-- TOC entry 3558 (class 0 OID 0)
-- Dependencies: 225
-- Name: TABLE sys_role_menu; Type: COMMENT; Schema: biz_sys; Owner: wz
--

COMMENT ON TABLE biz_sys.sys_role_menu IS '角色和菜单关联表';


--
-- TOC entry 3559 (class 0 OID 0)
-- Dependencies: 225
-- Name: COLUMN sys_role_menu.role_id; Type: COMMENT; Schema: biz_sys; Owner: wz
--

COMMENT ON COLUMN biz_sys.sys_role_menu.role_id IS '角色ID';


--
-- TOC entry 3560 (class 0 OID 0)
-- Dependencies: 225
-- Name: COLUMN sys_role_menu.menu_id; Type: COMMENT; Schema: biz_sys; Owner: wz
--

COMMENT ON COLUMN biz_sys.sys_role_menu.menu_id IS '菜单ID';


--
-- TOC entry 226 (class 1259 OID 34597)
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
    expire_time bigint NOT NULL,
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
-- TOC entry 3561 (class 0 OID 0)
-- Dependencies: 226
-- Name: TABLE sys_tenant; Type: COMMENT; Schema: biz_sys; Owner: wz
--

COMMENT ON TABLE biz_sys.sys_tenant IS '租户表';


--
-- TOC entry 3562 (class 0 OID 0)
-- Dependencies: 226
-- Name: COLUMN sys_tenant.tenant_id; Type: COMMENT; Schema: biz_sys; Owner: wz
--

COMMENT ON COLUMN biz_sys.sys_tenant.tenant_id IS '租户编号';


--
-- TOC entry 3563 (class 0 OID 0)
-- Dependencies: 226
-- Name: COLUMN sys_tenant.administrator; Type: COMMENT; Schema: biz_sys; Owner: wz
--

COMMENT ON COLUMN biz_sys.sys_tenant.administrator IS '管理员ID';


--
-- TOC entry 3564 (class 0 OID 0)
-- Dependencies: 226
-- Name: COLUMN sys_tenant.tenant_type; Type: COMMENT; Schema: biz_sys; Owner: wz
--

COMMENT ON COLUMN biz_sys.sys_tenant.tenant_type IS '租户类型';


--
-- TOC entry 3565 (class 0 OID 0)
-- Dependencies: 226
-- Name: COLUMN sys_tenant.contact_phone; Type: COMMENT; Schema: biz_sys; Owner: wz
--

COMMENT ON COLUMN biz_sys.sys_tenant.contact_phone IS '联系电话';


--
-- TOC entry 3566 (class 0 OID 0)
-- Dependencies: 226
-- Name: COLUMN sys_tenant.tenant_name; Type: COMMENT; Schema: biz_sys; Owner: wz
--

COMMENT ON COLUMN biz_sys.sys_tenant.tenant_name IS '租户名称';


--
-- TOC entry 3567 (class 0 OID 0)
-- Dependencies: 226
-- Name: COLUMN sys_tenant.oper_pwd; Type: COMMENT; Schema: biz_sys; Owner: wz
--

COMMENT ON COLUMN biz_sys.sys_tenant.oper_pwd IS '操作密码';


--
-- TOC entry 3568 (class 0 OID 0)
-- Dependencies: 226
-- Name: COLUMN sys_tenant.status; Type: COMMENT; Schema: biz_sys; Owner: wz
--

COMMENT ON COLUMN biz_sys.sys_tenant.status IS '租户状态';


--
-- TOC entry 3569 (class 0 OID 0)
-- Dependencies: 226
-- Name: COLUMN sys_tenant.domain; Type: COMMENT; Schema: biz_sys; Owner: wz
--

COMMENT ON COLUMN biz_sys.sys_tenant.domain IS '域名';


--
-- TOC entry 3570 (class 0 OID 0)
-- Dependencies: 226
-- Name: COLUMN sys_tenant.remark; Type: COMMENT; Schema: biz_sys; Owner: wz
--

COMMENT ON COLUMN biz_sys.sys_tenant.remark IS '备注';


--
-- TOC entry 3571 (class 0 OID 0)
-- Dependencies: 226
-- Name: COLUMN sys_tenant.package_id; Type: COMMENT; Schema: biz_sys; Owner: wz
--

COMMENT ON COLUMN biz_sys.sys_tenant.package_id IS '租户套餐编号';


--
-- TOC entry 3572 (class 0 OID 0)
-- Dependencies: 226
-- Name: COLUMN sys_tenant.expire_time; Type: COMMENT; Schema: biz_sys; Owner: wz
--

COMMENT ON COLUMN biz_sys.sys_tenant.expire_time IS '过期时间（-1不限制）';


--
-- TOC entry 3573 (class 0 OID 0)
-- Dependencies: 226
-- Name: COLUMN sys_tenant.account_limit; Type: COMMENT; Schema: biz_sys; Owner: wz
--

COMMENT ON COLUMN biz_sys.sys_tenant.account_limit IS '账号数量（-1不限制）';


--
-- TOC entry 3574 (class 0 OID 0)
-- Dependencies: 226
-- Name: COLUMN sys_tenant.role_limit; Type: COMMENT; Schema: biz_sys; Owner: wz
--

COMMENT ON COLUMN biz_sys.sys_tenant.role_limit IS '角色数量（-1不限制）';


--
-- TOC entry 3575 (class 0 OID 0)
-- Dependencies: 226
-- Name: COLUMN sys_tenant.post_limit; Type: COMMENT; Schema: biz_sys; Owner: wz
--

COMMENT ON COLUMN biz_sys.sys_tenant.post_limit IS '岗位数量（-1不限制）';


--
-- TOC entry 3576 (class 0 OID 0)
-- Dependencies: 226
-- Name: COLUMN sys_tenant.dept_limit; Type: COMMENT; Schema: biz_sys; Owner: wz
--

COMMENT ON COLUMN biz_sys.sys_tenant.dept_limit IS '部门数量（-1不限制）';


--
-- TOC entry 3577 (class 0 OID 0)
-- Dependencies: 226
-- Name: COLUMN sys_tenant.creator_id; Type: COMMENT; Schema: biz_sys; Owner: wz
--

COMMENT ON COLUMN biz_sys.sys_tenant.creator_id IS '创建者';


--
-- TOC entry 3578 (class 0 OID 0)
-- Dependencies: 226
-- Name: COLUMN sys_tenant.create_at; Type: COMMENT; Schema: biz_sys; Owner: wz
--

COMMENT ON COLUMN biz_sys.sys_tenant.create_at IS '创建时间';


--
-- TOC entry 3579 (class 0 OID 0)
-- Dependencies: 226
-- Name: COLUMN sys_tenant.updater_id; Type: COMMENT; Schema: biz_sys; Owner: wz
--

COMMENT ON COLUMN biz_sys.sys_tenant.updater_id IS '更新者';


--
-- TOC entry 3580 (class 0 OID 0)
-- Dependencies: 226
-- Name: COLUMN sys_tenant.update_at; Type: COMMENT; Schema: biz_sys; Owner: wz
--

COMMENT ON COLUMN biz_sys.sys_tenant.update_at IS '更新时间';


--
-- TOC entry 227 (class 1259 OID 34600)
-- Name: sys_tenant_package; Type: TABLE; Schema: biz_sys; Owner: wz
--

CREATE TABLE biz_sys.sys_tenant_package (
    package_id bigint NOT NULL,
    package_name character varying(20) NOT NULL,
    status character(1) NOT NULL,
    menu_ids bigint[] DEFAULT '{}'::bigint[] NOT NULL,
    is_menu_display boolean DEFAULT false NOT NULL,
    remark character varying(200),
    creator_id bigint NOT NULL,
    create_at timestamp with time zone NOT NULL,
    updater_id bigint,
    update_at timestamp with time zone
);


ALTER TABLE biz_sys.sys_tenant_package OWNER TO wz;

--
-- TOC entry 3581 (class 0 OID 0)
-- Dependencies: 227
-- Name: TABLE sys_tenant_package; Type: COMMENT; Schema: biz_sys; Owner: wz
--

COMMENT ON TABLE biz_sys.sys_tenant_package IS '租户套餐表';


--
-- TOC entry 3582 (class 0 OID 0)
-- Dependencies: 227
-- Name: COLUMN sys_tenant_package.package_id; Type: COMMENT; Schema: biz_sys; Owner: wz
--

COMMENT ON COLUMN biz_sys.sys_tenant_package.package_id IS '租户套餐id';


--
-- TOC entry 3583 (class 0 OID 0)
-- Dependencies: 227
-- Name: COLUMN sys_tenant_package.package_name; Type: COMMENT; Schema: biz_sys; Owner: wz
--

COMMENT ON COLUMN biz_sys.sys_tenant_package.package_name IS '套餐名称';


--
-- TOC entry 3584 (class 0 OID 0)
-- Dependencies: 227
-- Name: COLUMN sys_tenant_package.status; Type: COMMENT; Schema: biz_sys; Owner: wz
--

COMMENT ON COLUMN biz_sys.sys_tenant_package.status IS '状态（0正常 1停用）';


--
-- TOC entry 3585 (class 0 OID 0)
-- Dependencies: 227
-- Name: COLUMN sys_tenant_package.menu_ids; Type: COMMENT; Schema: biz_sys; Owner: wz
--

COMMENT ON COLUMN biz_sys.sys_tenant_package.menu_ids IS '套餐绑定的菜单';


--
-- TOC entry 3586 (class 0 OID 0)
-- Dependencies: 227
-- Name: COLUMN sys_tenant_package.is_menu_display; Type: COMMENT; Schema: biz_sys; Owner: wz
--

COMMENT ON COLUMN biz_sys.sys_tenant_package.is_menu_display IS '菜单树选择项是否关联显示';


--
-- TOC entry 3587 (class 0 OID 0)
-- Dependencies: 227
-- Name: COLUMN sys_tenant_package.remark; Type: COMMENT; Schema: biz_sys; Owner: wz
--

COMMENT ON COLUMN biz_sys.sys_tenant_package.remark IS '备注';


--
-- TOC entry 3588 (class 0 OID 0)
-- Dependencies: 227
-- Name: COLUMN sys_tenant_package.creator_id; Type: COMMENT; Schema: biz_sys; Owner: wz
--

COMMENT ON COLUMN biz_sys.sys_tenant_package.creator_id IS '创建者';


--
-- TOC entry 3589 (class 0 OID 0)
-- Dependencies: 227
-- Name: COLUMN sys_tenant_package.create_at; Type: COMMENT; Schema: biz_sys; Owner: wz
--

COMMENT ON COLUMN biz_sys.sys_tenant_package.create_at IS '创建时间';


--
-- TOC entry 3590 (class 0 OID 0)
-- Dependencies: 227
-- Name: COLUMN sys_tenant_package.updater_id; Type: COMMENT; Schema: biz_sys; Owner: wz
--

COMMENT ON COLUMN biz_sys.sys_tenant_package.updater_id IS '更新者';


--
-- TOC entry 3591 (class 0 OID 0)
-- Dependencies: 227
-- Name: COLUMN sys_tenant_package.update_at; Type: COMMENT; Schema: biz_sys; Owner: wz
--

COMMENT ON COLUMN biz_sys.sys_tenant_package.update_at IS '更新时间';


--
-- TOC entry 228 (class 1259 OID 34607)
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
-- Dependencies: 228
-- Name: TABLE sys_tenant_wallet; Type: COMMENT; Schema: biz_sys; Owner: wz
--

COMMENT ON TABLE biz_sys.sys_tenant_wallet IS '租户钱包';


--
-- TOC entry 3593 (class 0 OID 0)
-- Dependencies: 228
-- Name: COLUMN sys_tenant_wallet.balance; Type: COMMENT; Schema: biz_sys; Owner: wz
--

COMMENT ON COLUMN biz_sys.sys_tenant_wallet.balance IS '余额, 元';


--
-- TOC entry 3594 (class 0 OID 0)
-- Dependencies: 228
-- Name: COLUMN sys_tenant_wallet.status; Type: COMMENT; Schema: biz_sys; Owner: wz
--

COMMENT ON COLUMN biz_sys.sys_tenant_wallet.status IS '状态';


--
-- TOC entry 229 (class 1259 OID 34610)
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
-- Dependencies: 229
-- Name: TABLE sys_tenant_wallet_record; Type: COMMENT; Schema: biz_sys; Owner: wz
--

COMMENT ON TABLE biz_sys.sys_tenant_wallet_record IS '租户钱包记录';


--
-- TOC entry 3596 (class 0 OID 0)
-- Dependencies: 229
-- Name: COLUMN sys_tenant_wallet_record.tenant_id; Type: COMMENT; Schema: biz_sys; Owner: wz
--

COMMENT ON COLUMN biz_sys.sys_tenant_wallet_record.tenant_id IS '租户ID';


--
-- TOC entry 3597 (class 0 OID 0)
-- Dependencies: 229
-- Name: COLUMN sys_tenant_wallet_record.amount; Type: COMMENT; Schema: biz_sys; Owner: wz
--

COMMENT ON COLUMN biz_sys.sys_tenant_wallet_record.amount IS '金额, 单位元';


--
-- TOC entry 3598 (class 0 OID 0)
-- Dependencies: 229
-- Name: COLUMN sys_tenant_wallet_record.record_type; Type: COMMENT; Schema: biz_sys; Owner: wz
--

COMMENT ON COLUMN biz_sys.sys_tenant_wallet_record.record_type IS '记录类型';


--
-- TOC entry 3599 (class 0 OID 0)
-- Dependencies: 229
-- Name: COLUMN sys_tenant_wallet_record.create_at; Type: COMMENT; Schema: biz_sys; Owner: wz
--

COMMENT ON COLUMN biz_sys.sys_tenant_wallet_record.create_at IS '创建时间';


--
-- TOC entry 3600 (class 0 OID 0)
-- Dependencies: 229
-- Name: COLUMN sys_tenant_wallet_record.remark; Type: COMMENT; Schema: biz_sys; Owner: wz
--

COMMENT ON COLUMN biz_sys.sys_tenant_wallet_record.remark IS '备注';


--
-- TOC entry 230 (class 1259 OID 34613)
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
-- Dependencies: 230
-- Name: TABLE sys_user; Type: COMMENT; Schema: biz_sys; Owner: wz
--

COMMENT ON TABLE biz_sys.sys_user IS '用户信息表';


--
-- TOC entry 3602 (class 0 OID 0)
-- Dependencies: 230
-- Name: COLUMN sys_user.user_id; Type: COMMENT; Schema: biz_sys; Owner: wz
--

COMMENT ON COLUMN biz_sys.sys_user.user_id IS '管理员ID';


--
-- TOC entry 3603 (class 0 OID 0)
-- Dependencies: 230
-- Name: COLUMN sys_user.tenant_id; Type: COMMENT; Schema: biz_sys; Owner: wz
--

COMMENT ON COLUMN biz_sys.sys_user.tenant_id IS '租户ID';


--
-- TOC entry 3604 (class 0 OID 0)
-- Dependencies: 230
-- Name: COLUMN sys_user.dept_id; Type: COMMENT; Schema: biz_sys; Owner: wz
--

COMMENT ON COLUMN biz_sys.sys_user.dept_id IS '部门ID';


--
-- TOC entry 3605 (class 0 OID 0)
-- Dependencies: 230
-- Name: COLUMN sys_user.username; Type: COMMENT; Schema: biz_sys; Owner: wz
--

COMMENT ON COLUMN biz_sys.sys_user.username IS '用户账号';


--
-- TOC entry 3606 (class 0 OID 0)
-- Dependencies: 230
-- Name: COLUMN sys_user.email; Type: COMMENT; Schema: biz_sys; Owner: wz
--

COMMENT ON COLUMN biz_sys.sys_user.email IS '用户邮箱';


--
-- TOC entry 3607 (class 0 OID 0)
-- Dependencies: 230
-- Name: COLUMN sys_user.nickname; Type: COMMENT; Schema: biz_sys; Owner: wz
--

COMMENT ON COLUMN biz_sys.sys_user.nickname IS '用户昵称';


--
-- TOC entry 3608 (class 0 OID 0)
-- Dependencies: 230
-- Name: COLUMN sys_user.phone_number; Type: COMMENT; Schema: biz_sys; Owner: wz
--

COMMENT ON COLUMN biz_sys.sys_user.phone_number IS '手机号码';


--
-- TOC entry 3609 (class 0 OID 0)
-- Dependencies: 230
-- Name: COLUMN sys_user.status; Type: COMMENT; Schema: biz_sys; Owner: wz
--

COMMENT ON COLUMN biz_sys.sys_user.status IS '状态值';


--
-- TOC entry 3610 (class 0 OID 0)
-- Dependencies: 230
-- Name: COLUMN sys_user.gender; Type: COMMENT; Schema: biz_sys; Owner: wz
--

COMMENT ON COLUMN biz_sys.sys_user.gender IS '用户性别（0男 1女 2未知）';


--
-- TOC entry 3611 (class 0 OID 0)
-- Dependencies: 230
-- Name: COLUMN sys_user.avatar; Type: COMMENT; Schema: biz_sys; Owner: wz
--

COMMENT ON COLUMN biz_sys.sys_user.avatar IS '头像地址';


--
-- TOC entry 3612 (class 0 OID 0)
-- Dependencies: 230
-- Name: COLUMN sys_user.password; Type: COMMENT; Schema: biz_sys; Owner: wz
--

COMMENT ON COLUMN biz_sys.sys_user.password IS '密码';


--
-- TOC entry 3613 (class 0 OID 0)
-- Dependencies: 230
-- Name: COLUMN sys_user.login_ip; Type: COMMENT; Schema: biz_sys; Owner: wz
--

COMMENT ON COLUMN biz_sys.sys_user.login_ip IS '登录ip';


--
-- TOC entry 3614 (class 0 OID 0)
-- Dependencies: 230
-- Name: COLUMN sys_user.login_date; Type: COMMENT; Schema: biz_sys; Owner: wz
--

COMMENT ON COLUMN biz_sys.sys_user.login_date IS '登录时间';


--
-- TOC entry 3615 (class 0 OID 0)
-- Dependencies: 230
-- Name: COLUMN sys_user.remark; Type: COMMENT; Schema: biz_sys; Owner: wz
--

COMMENT ON COLUMN biz_sys.sys_user.remark IS '备注';


--
-- TOC entry 3616 (class 0 OID 0)
-- Dependencies: 230
-- Name: COLUMN sys_user.creator_id; Type: COMMENT; Schema: biz_sys; Owner: wz
--

COMMENT ON COLUMN biz_sys.sys_user.creator_id IS '创建者';


--
-- TOC entry 3617 (class 0 OID 0)
-- Dependencies: 230
-- Name: COLUMN sys_user.updater_id; Type: COMMENT; Schema: biz_sys; Owner: wz
--

COMMENT ON COLUMN biz_sys.sys_user.updater_id IS '更新者';


--
-- TOC entry 231 (class 1259 OID 34618)
-- Name: sys_user_post; Type: TABLE; Schema: biz_sys; Owner: wz
--

CREATE TABLE biz_sys.sys_user_post (
    user_id bigint NOT NULL,
    post_id bigint NOT NULL
);


ALTER TABLE biz_sys.sys_user_post OWNER TO wz;

--
-- TOC entry 3618 (class 0 OID 0)
-- Dependencies: 231
-- Name: TABLE sys_user_post; Type: COMMENT; Schema: biz_sys; Owner: wz
--

COMMENT ON TABLE biz_sys.sys_user_post IS '用户与岗位关联表';


--
-- TOC entry 3619 (class 0 OID 0)
-- Dependencies: 231
-- Name: COLUMN sys_user_post.user_id; Type: COMMENT; Schema: biz_sys; Owner: wz
--

COMMENT ON COLUMN biz_sys.sys_user_post.user_id IS '管理员ID';


--
-- TOC entry 3620 (class 0 OID 0)
-- Dependencies: 231
-- Name: COLUMN sys_user_post.post_id; Type: COMMENT; Schema: biz_sys; Owner: wz
--

COMMENT ON COLUMN biz_sys.sys_user_post.post_id IS '岗位ID';


--
-- TOC entry 232 (class 1259 OID 34621)
-- Name: sys_user_role; Type: TABLE; Schema: biz_sys; Owner: wz
--

CREATE TABLE biz_sys.sys_user_role (
    user_id bigint NOT NULL,
    role_id bigint NOT NULL
);


ALTER TABLE biz_sys.sys_user_role OWNER TO wz;

--
-- TOC entry 3621 (class 0 OID 0)
-- Dependencies: 232
-- Name: TABLE sys_user_role; Type: COMMENT; Schema: biz_sys; Owner: wz
--

COMMENT ON TABLE biz_sys.sys_user_role IS '用户和角色关联表';


--
-- TOC entry 3622 (class 0 OID 0)
-- Dependencies: 232
-- Name: COLUMN sys_user_role.user_id; Type: COMMENT; Schema: biz_sys; Owner: wz
--

COMMENT ON COLUMN biz_sys.sys_user_role.user_id IS '管理员ID';


--
-- TOC entry 3623 (class 0 OID 0)
-- Dependencies: 232
-- Name: COLUMN sys_user_role.role_id; Type: COMMENT; Schema: biz_sys; Owner: wz
--

COMMENT ON COLUMN biz_sys.sys_user_role.role_id IS '角色ID';


--
-- TOC entry 233 (class 1259 OID 34624)
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
-- Dependencies: 233
-- Name: TABLE sys_withdrawal_record; Type: COMMENT; Schema: biz_sys; Owner: wz
--

COMMENT ON TABLE biz_sys.sys_withdrawal_record IS '系统提现记录';


--
-- TOC entry 3625 (class 0 OID 0)
-- Dependencies: 233
-- Name: COLUMN sys_withdrawal_record.withdrawal_id; Type: COMMENT; Schema: biz_sys; Owner: wz
--

COMMENT ON COLUMN biz_sys.sys_withdrawal_record.withdrawal_id IS 'id';


--
-- TOC entry 3626 (class 0 OID 0)
-- Dependencies: 233
-- Name: COLUMN sys_withdrawal_record.order_no; Type: COMMENT; Schema: biz_sys; Owner: wz
--

COMMENT ON COLUMN biz_sys.sys_withdrawal_record.order_no IS '订单号';


--
-- TOC entry 3627 (class 0 OID 0)
-- Dependencies: 233
-- Name: COLUMN sys_withdrawal_record.status; Type: COMMENT; Schema: biz_sys; Owner: wz
--

COMMENT ON COLUMN biz_sys.sys_withdrawal_record.status IS '状态
''0'' 处理中
''1'' 成功
''2'' 失败';


--
-- TOC entry 3628 (class 0 OID 0)
-- Dependencies: 233
-- Name: COLUMN sys_withdrawal_record.tenant_id; Type: COMMENT; Schema: biz_sys; Owner: wz
--

COMMENT ON COLUMN biz_sys.sys_withdrawal_record.tenant_id IS '租户id';


--
-- TOC entry 3629 (class 0 OID 0)
-- Dependencies: 233
-- Name: COLUMN sys_withdrawal_record.request_params; Type: COMMENT; Schema: biz_sys; Owner: wz
--

COMMENT ON COLUMN biz_sys.sys_withdrawal_record.request_params IS '第三方请求参数';


--
-- TOC entry 3630 (class 0 OID 0)
-- Dependencies: 233
-- Name: COLUMN sys_withdrawal_record.amount; Type: COMMENT; Schema: biz_sys; Owner: wz
--

COMMENT ON COLUMN biz_sys.sys_withdrawal_record.amount IS '金额, 单位元';


--
-- TOC entry 3631 (class 0 OID 0)
-- Dependencies: 233
-- Name: COLUMN sys_withdrawal_record.error_msg; Type: COMMENT; Schema: biz_sys; Owner: wz
--

COMMENT ON COLUMN biz_sys.sys_withdrawal_record.error_msg IS '错误信息';


--
-- TOC entry 3632 (class 0 OID 0)
-- Dependencies: 233
-- Name: COLUMN sys_withdrawal_record.creator_id; Type: COMMENT; Schema: biz_sys; Owner: wz
--

COMMENT ON COLUMN biz_sys.sys_withdrawal_record.creator_id IS '创建者';


--
-- TOC entry 3633 (class 0 OID 0)
-- Dependencies: 233
-- Name: COLUMN sys_withdrawal_record.create_at; Type: COMMENT; Schema: biz_sys; Owner: wz
--

COMMENT ON COLUMN biz_sys.sys_withdrawal_record.create_at IS '创建时间';


--
-- TOC entry 3634 (class 0 OID 0)
-- Dependencies: 233
-- Name: COLUMN sys_withdrawal_record.complete_at; Type: COMMENT; Schema: biz_sys; Owner: wz
--

COMMENT ON COLUMN biz_sys.sys_withdrawal_record.complete_at IS '完成时间';


--
-- TOC entry 3635 (class 0 OID 0)
-- Dependencies: 233
-- Name: COLUMN sys_withdrawal_record.remark; Type: COMMENT; Schema: biz_sys; Owner: wz
--

COMMENT ON COLUMN biz_sys.sys_withdrawal_record.remark IS '备注';


--
-- TOC entry 3448 (class 0 OID 33062)
-- Dependencies: 216
-- Data for Name: app_user; Type: TABLE DATA; Schema: biz_app; Owner: wz
--

COPY biz_app.app_user (user_id, nickname, phone_number, status, gender, avatar, login_ip, login_date, creator_id, updater_id, create_at, update_at) FROM stdin;
1826896461245968384	\N	15888888888	0	2	\N	127.0.0.1	2024-11-30 13:53:49+08	1	\N	2024-04-17 14:08:54.616+08	\N
\.


--
-- TOC entry 3449 (class 0 OID 33067)
-- Dependencies: 217
-- Data for Name: app_user_thirdinfo; Type: TABLE DATA; Schema: biz_app; Owner: wz
--

COPY biz_app.app_user_thirdinfo (user_id, identifier, identifier_type) FROM stdin;
\.


--
-- TOC entry 3450 (class 0 OID 33073)
-- Dependencies: 218
-- Data for Name: app_user_wallet; Type: TABLE DATA; Schema: biz_app; Owner: wz
--

COPY biz_app.app_user_wallet (user_id, balance, status) FROM stdin;
1826896461245968384	0.00	0
\.


--
-- TOC entry 3451 (class 0 OID 33078)
-- Dependencies: 219
-- Data for Name: app_user_wallet_record; Type: TABLE DATA; Schema: biz_app; Owner: wz
--

COPY biz_app.app_user_wallet_record (record_id, user_id, amount, record_type, create_at, remark) FROM stdin;
\.


--
-- TOC entry 3452 (class 0 OID 34566)
-- Dependencies: 220
-- Data for Name: oauth2_client; Type: TABLE DATA; Schema: biz_sys; Owner: wz
--

COPY biz_sys.oauth2_client (id, client_name, client_id, client_secret, scopes, authorization_grant_types, redirect_uris, status, auto_approve, create_at, creator_id, update_at, updater_id) FROM stdin;
1	系统	server	{bcrypt}$2a$10$hK9Sv9kAvXE00fWtkWxzI.Ns4.5SuQteTJAnsFWXChlOWIUZSFYL2	{openid}	{password,sms,refresh_token}	{http://localhost:9001/oauth2/authorization_code_callback}	0	f	2024-04-17 14:08:54+08	1	2025-01-10 09:21:55+08	1
\.


--
-- TOC entry 3453 (class 0 OID 34574)
-- Dependencies: 221
-- Data for Name: sys_dept; Type: TABLE DATA; Schema: biz_sys; Owner: wz
--

COPY biz_sys.sys_dept (dept_id, tenant_id, parent_id, ancestors, dept_name, status, dept_sort, contact, email, creator_id, updater_id, create_at, update_at) FROM stdin;
\.


--
-- TOC entry 3466 (class 0 OID 35226)
-- Dependencies: 234
-- Data for Name: sys_menu; Type: TABLE DATA; Schema: biz_sys; Owner: wz
--

COPY biz_sys.sys_menu (menu_id, menu_name, parent_id, menu_sort, path, component, query, menu_type, status, perms, icon, is_frame, is_cache, is_visible, create_at, creator_id, update_at, updater_id) FROM stdin;
1	系统管理	0	100	system	\N	\N	D	0	\N	system	f	f	t	2024-05-26 12:30:16+08	1	2024-05-26 12:30:16+08	1
2	用户权限管理	0	99	user	\N	\N	D	0	\N	peoples	f	f	t	2024-05-26 12:30:16+08	1	2024-05-26 12:30:16+08	1
3	商户管理	0	50	tenant	\N	\N	D	0	\N	merchant	f	f	t	2024-05-26 12:30:16+08	1	2024-05-26 12:30:16+08	1
4	设备管理	0	10	equipment	\N	\N	D	0	\N	device	f	f	t	2024-05-26 12:30:16+08	1	2024-05-26 12:30:16+08	1
5	交易订单	0	4	trade	\N	\N	D	0	\N	order	f	f	t	2024-05-26 12:30:16+08	1	2024-05-26 12:30:16+08	1
100	系统消息	1	15	sys_message	system/sysmessage/index	\N	M	0	sys_message:list	guide	f	f	t	2024-05-26 12:30:16+08	1	2024-05-26 12:30:16+08	1
101	控制台入口	1	0	controller	\N	\N	D	0	\N	dashboard	f	f	t	2024-05-26 12:30:16+08	1	2024-05-26 12:30:16+08	1
102	字典管理	1	6	dict	system/dict/index	\N	M	0	dict:list	dict	f	f	t	2024-05-26 12:30:16+08	1	2024-05-26 12:30:16+08	\N
103	参数设置	1	7	config	system/config/index	\N	M	0	config:list	edit	f	f	t	2024-05-26 12:30:16+08	1	2024-05-26 12:30:16+08	\N
104	日志管理	1	1	log	\N	\N	D	0	\N	log	f	f	t	2024-05-26 12:30:16+08	1	2024-05-26 12:30:16+08	1
150	操作日志	104	1	operlog	system/operlog/index	\N	M	0	operlog:list	form	f	f	t	2024-05-26 12:30:16+08	1	2024-05-26 12:30:16+08	\N
151	登录日志	104	2	loginlog	system/loginlog/index	\N	M	0	loginlog:list	logininfor	f	f	t	2024-05-26 12:30:16+08	1	2024-05-26 12:30:16+08	1
201	顾客管理	2	1	appuser	user/appuser/index	\N	M	0	app_user:list	user2	f	f	t	2024-05-26 12:30:16+08	1	2024-05-26 12:30:16+08	1
202	会员体系	2	2	vip	user/vip/index	\N	M	0	vip:list	build	f	f	t	2024-05-26 12:30:16+08	1	2024-05-26 12:30:16+08	1
203	系统用户	2	100	sysuser	user/sysuser/index	\N	M	0	sys_user:list	user	f	t	t	2024-05-26 12:30:16+08	1	2024-05-26 12:30:16+08	1
205	部门管理	2	70	dept	user/dept/index	\N	M	0	dept:list	tree	f	f	t	2024-05-26 12:30:16+08	1	2024-05-26 12:30:16+08	1
206	角色管理	2	99	role	user/role/index	\N	M	0	sys_role:list	role	f	f	t	2024-05-26 12:30:16+08	1	2024-05-26 12:30:16+08	1
207	菜单管理	2	50	menu	user/menu/index	\N	M	0	menu:list	tree-table	f	f	t	2024-05-26 12:30:16+08	1	2024-05-26 12:30:16+08	1
208	岗位管理	2	8	post	user/post/index	\N	M	0	post:list	post	f	f	t	2024-05-26 12:30:16+08	1	2024-05-26 12:30:16+08	1
300	定时任务	101	20	http://localhost:9200/xxl-job-admin	\N	\N	M	0	job:list	job	t	f	t	2024-05-26 12:30:16+08	1	2024-05-26 12:30:16+08	1
301	系统接口	101	2	http://localhost:8080/doc.html	\N	\N	M	0	tool:swagger:list	swagger	f	f	t	2024-05-26 12:30:16+08	1	2024-05-26 12:30:16+08	\N
302	Sentinel控制台	101	3	http://localhost:8718	\N	\N	M	0	monitor:sentinel:list	sentinel	t	f	t	2024-05-26 12:30:16+08	1	2024-05-26 12:30:16+08	\N
303	Nacos控制台	101	4	http://127.0.0.1:8848/nacos	\N	\N	M	0	monitor:nacos:list	nacos	t	f	t	2024-05-26 12:30:16+08	1	2024-05-26 12:30:16+08	\N
304	服务监控	101	5	http://localhost:9100/	\N	\N	M	0	monitor:server:list	server	t	f	t	2024-05-26 12:30:16+08	1	2024-05-26 12:30:16+08	1
400	站点管理	4	10	station	equipment/station/index	\N	M	0	station:list	location	f	f	t	2024-05-26 12:30:16+08	1	2024-05-26 12:30:16+08	1
401	设备列表	4	50	device	equipment/device/index	\N	M	0	device:list	chargePile	f	t	t	2024-05-26 12:30:16+08	1	2024-05-26 12:30:16+08	1
402	协议管理	4	30	protocol	equipment/protocol/index	\N	M	0	protocol:list	protocol	f	f	t	2024-05-26 12:30:16+08	1	2024-05-26 12:30:16+08	1
403	产品管理	4	20	product	equipment/product/index	\N	M	0	product:list	product	f	f	t	2024-05-26 12:30:16+08	1	2024-05-26 12:30:16+08	1
404	设备地图	4	10	map	equipment/map/index	\N	M	0	\N	map	f	f	t	2024-05-26 12:30:16+08	1	2024-05-26 12:30:16+08	1
500	订单管理	5	100	order	trade/order/index	\N	M	0	order:list	list	f	f	t	2024-05-26 12:30:16+08	1	2024-05-26 12:30:16+08	1
501	优惠券管理	5	1	coupon	trade/coupon/index	\N	M	0	coupon:list	coupon	f	f	t	2024-05-26 12:30:16+08	1	2024-05-26 12:30:16+08	1
502	交易投诉	5	5	feedback	trade/feedback/index	\N	M	0	\N	feedback	f	f	t	2024-05-26 12:30:16+08	1	2024-05-26 12:30:16+08	1
601	商户信息	3	100	info	user/tenant/index	\N	M	0	tenant:information	information	f	f	t	2024-05-26 12:30:16+08	1	2024-05-26 12:30:16+08	1
602	租户套餐管理	3	50	package	user/tenant/package/index	\N	M	0	tenant_package:list	package	f	f	t	2024-05-26 12:30:16+08	1	2024-05-26 12:30:16+08	1
700	客户端管理	2	3	client	user/client/index	\N	M	0	oauth2_client:list	wechat	f	f	t	2024-05-26 12:30:16+08	1	2024-05-26 12:30:16+08	1
1046	字典查询	102	1	#	\N	\N	B	0	dict:query	#	f	f	t	2024-05-26 12:30:16+08	1	2024-05-26 12:30:16+08	\N
1047	字典新增	102	2	#	\N	\N	B	0	dict:add	#	f	f	t	2024-05-26 12:30:16+08	1	2024-05-26 12:30:16+08	\N
1048	字典修改	102	3	#	\N	\N	B	0	dict:edit	#	f	f	t	2024-05-26 12:30:16+08	1	2024-05-26 12:30:16+08	\N
1049	字典删除	102	4	#	\N	\N	B	0	dict:remove	#	f	f	t	2024-05-26 12:30:16+08	1	2024-05-26 12:30:16+08	\N
1050	字典导出	102	5	#	\N	\N	B	0	dict:export	#	f	f	t	2024-05-26 12:30:16+08	1	2024-05-26 12:30:16+08	\N
1051	参数查询	103	1	#	\N	\N	B	0	config:query	#	f	f	t	2024-05-26 12:30:16+08	1	2024-05-26 12:30:16+08	\N
1052	参数新增	103	2	#	\N	\N	B	0	config:add	#	f	f	t	2024-05-26 12:30:16+08	1	2024-05-26 12:30:16+08	\N
1053	参数修改	103	3	#	\N	\N	B	0	config:edit	#	f	f	t	2024-05-26 12:30:16+08	1	2024-05-26 12:30:16+08	\N
1054	参数删除	103	4	#	\N	\N	B	0	config:remove	#	f	f	t	2024-05-26 12:30:16+08	1	2024-05-26 12:30:16+08	\N
1055	参数导出	103	5	#	\N	\N	B	0	config:export	#	f	f	t	2024-05-26 12:30:16+08	1	2024-05-26 12:30:16+08	\N
1056	公告查询	100	1	#	\N	\N	B	0	sys_message:query	#	f	f	t	2024-05-26 12:30:16+08	1	2024-05-26 12:30:16+08	1
1057	公告新增	100	2	#	\N	\N	B	0	sys_message:add	#	f	f	t	2024-05-26 12:30:16+08	1	2024-05-26 12:30:16+08	1
1058	公告修改	100	3	#	\N	\N	B	0	sys_message:edit	#	f	f	t	2024-05-26 12:30:16+08	1	2024-05-26 12:30:16+08	1
1059	公告删除	100	4	#	\N	\N	B	0	sys_message:remove	#	f	f	t	2024-05-26 12:30:16+08	1	2024-05-26 12:30:16+08	1
1060	发布公告	100	10	#	\N	\N	B	0	sys_message:publish	#	f	f	t	2024-05-26 12:30:16+08	1	2024-05-26 12:30:16+08	1
1061	操作删除	150	2	#	\N	\N	B	0	operlog:remove	#	f	f	t	2024-05-26 12:30:16+08	1	2024-05-26 12:30:16+08	\N
1062	操作日志敏感字段	150	10	#	\N	\N	F	0	operlog:field	#	f	f	t	2024-05-26 12:30:16+08	1	2024-05-26 12:30:16+08	1
1064	删除记录	151	2	#	\N	\N	B	0	loginlog:remove	#	f	f	t	2024-05-26 12:30:16+08	1	2024-05-26 12:30:16+08	1
1125	余额记录	601	3	#	\N	\N	B	0	wallet_record:list	#	f	f	t	2024-05-26 12:30:16+08	1	2024-05-26 12:30:16+08	2
1126	商户提现	601	1	#	\N	\N	B	0	tenant:withdrawal	#	f	f	t	2024-05-26 12:30:16+08	1	2024-05-26 12:30:16+08	1
1127	商户余额信息	601	0	#	\N	\N	B	0	tenant:wallet_info	#	f	f	t	2024-05-26 12:30:16+08	1	2024-05-26 12:30:16+08	1
1129	重置租户操作密码	601	11	#	\N	\N	B	0	tenant:reset_operpwd	#	f	f	t	2024-05-26 12:30:16+08	1	2024-05-26 12:30:16+08	1
1131	租户详情	601	9	#	\N	\N	B	0	tenant:query	#	f	f	t	2024-05-26 12:30:16+08	1	2024-05-26 12:30:16+08	1
1132	租户新增	601	4	#	\N	\N	B	0	tenant:add	#	f	f	t	2024-05-26 12:30:16+08	1	2024-05-26 12:30:16+08	1
1133	租户修改	601	2	#	\N	\N	B	0	tenant:edit	#	f	f	t	2024-05-26 12:30:16+08	1	2024-05-26 12:30:16+08	1
1134	租户删除	601	2	#	\N	\N	B	0	tenant:remove	#	f	f	t	2024-05-26 12:30:16+08	1	2024-05-26 12:30:16+08	1
1135	套餐详情	602	9	#	\N	\N	B	0	tenant_package:query	#	f	f	t	2024-05-26 12:30:16+08	1	2024-05-26 12:30:16+08	1
1136	套餐新增	602	5	#	\N	\N	B	0	tenant_package:add	#	f	f	t	2024-05-26 12:30:16+08	1	2024-05-26 12:30:16+08	1
1137	套餐修改	602	4	#	\N	\N	B	0	tenant_package:edit	#	f	f	t	2024-05-26 12:30:16+08	1	2024-05-26 12:30:16+08	1
1138	套餐删除	602	2	#	\N	\N	B	0	tenant_package:remove	#	f	f	t	2024-05-26 12:30:16+08	1	2024-05-26 12:30:16+08	1
1210	客户端详情	700	1	#	\N	\N	B	0	oauth2_client:query	#	f	f	t	2024-05-26 12:30:16+08	1	2024-05-26 12:30:16+08	1
1211	客户端修改	700	2	#	\N	\N	B	0	oauth2_client:edit	#	f	f	t	2024-05-26 12:30:16+08	1	2024-05-26 12:30:16+08	1
1212	客户端添加	700	3	#	\N	\N	B	0	oauth2_client:add	#	f	f	t	2024-05-26 12:30:16+08	1	2024-05-26 12:30:16+08	1
1213	客户端删除	700	2	#	\N	\N	B	0	oauth2_client:remove	#	f	f	t	2024-05-26 12:30:16+08	1	2024-05-26 12:30:16+08	1
1214	客户端导出	700	4	#	\N	\N	B	0	oauth2_client:export	#	f	f	t	2024-05-26 12:30:16+08	1	2024-05-26 12:30:16+08	1
1215	修改密钥	700	5	#	\N	\N	B	0	oauth2_client:edit_secret	#	f	f	t	2024-05-26 12:30:16+08	1	2024-05-26 12:30:16+08	1
2001	用户查询	201	1	#	\N	\N	B	0	app_user:query	#	f	f	t	2024-05-26 12:30:16+08	1	2024-05-26 12:30:16+08	1
2002	用户新增	201	2	#	\N	\N	B	0	app_user:add	#	f	f	t	2024-05-26 12:30:16+08	1	2024-05-26 12:30:16+08	1
2003	用户修改	201	3	#	\N	\N	B	0	app_user:edit	#	f	f	t	2024-05-26 12:30:16+08	1	2024-05-26 12:30:16+08	1
2004	用户删除	201	4	#	\N	\N	B	0	app_user:remove	#	f	f	t	2024-05-26 12:30:16+08	1	2024-05-26 12:30:16+08	1
2005	用户导出	201	5	#	\N	\N	B	0	app_user:export	#	f	f	t	2024-05-26 12:30:16+08	1	2024-05-26 12:30:16+08	1
2013	菜单查询	207	1	#	\N	\N	B	0	menu:query	#	f	f	t	2024-05-26 12:30:16+08	1	2024-05-26 12:30:16+08	\N
2014	菜单新增	207	2	#	\N	\N	B	0	menu:add	#	f	f	t	2024-05-26 12:30:16+08	1	2024-05-26 12:30:16+08	\N
2015	菜单修改	207	3	#	\N	\N	B	0	menu:edit	#	f	f	t	2024-05-26 12:30:16+08	1	2024-05-26 12:30:16+08	\N
2016	菜单删除	207	4	#	\N	\N	B	0	menu:remove	#	f	f	t	2024-05-26 12:30:16+08	1	2024-05-26 12:30:16+08	\N
2037	部门查询	205	1	#	\N	\N	B	0	dept:query	#	f	f	t	2024-05-26 12:30:16+08	1	2024-05-26 12:30:16+08	1
2038	部门新增	205	2	#	\N	\N	B	0	dept:add	#	f	f	t	2024-05-26 12:30:16+08	1	2024-05-26 12:30:16+08	1
2039	部门修改	205	3	#	\N	\N	B	0	dept:edit	#	f	f	t	2024-05-26 12:30:16+08	1	2024-05-26 12:30:16+08	1
2040	部门删除	205	4	#	\N	\N	B	0	dept:remove	#	f	f	t	2024-05-26 12:30:16+08	1	2024-05-26 12:30:16+08	1
2062	重置密码	203	7	#	\N	\N	B	0	sys_user:resetPwd	#	f	f	t	2024-05-26 12:30:16+08	1	2024-05-26 12:30:16+08	1
2064	用户修改	203	3	#	\N	\N	B	0	sys_user:edit	#	f	f	t	2024-05-26 12:30:16+08	1	2024-05-26 12:30:16+08	1
2071	用户查询	203	0	#	\N	\N	B	0	sys_user:query	#	f	f	t	2024-05-26 12:30:16+08	1	2024-05-26 12:30:16+08	1
2072	用户添加	203	1	#	\N	\N	B	0	sys_user:add	#	f	f	t	2024-05-26 12:30:16+08	1	2024-05-26 12:30:16+08	1
2077	用户导出	203	1	#	\N	\N	B	0	sys_user:export	#	f	f	t	2024-05-26 12:30:16+08	1	2024-05-26 12:30:16+08	1
2078	用户删除	203	8	#	\N	\N	B	0	sys_user:remove	#	f	f	t	2024-05-26 12:30:16+08	1	2024-05-26 12:30:16+08	1
2112	等级查询	202	0	#	\N	\N	B	0	level:query	#	f	f	t	2024-05-26 12:30:16+08	1	2024-05-26 12:30:16+08	1
2113	等级修改	202	1	#	\N	\N	B	0	level:edit	#	f	f	t	2024-05-26 12:30:16+08	1	2024-05-26 12:30:16+08	1
2114	等级添加	202	2	#	\N	\N	B	0	level:add	#	f	f	t	2024-05-26 12:30:16+08	1	2024-05-26 12:30:16+08	1
2115	等级删除	202	3	#	\N	\N	B	0	level:remove	#	f	f	t	2024-05-26 12:30:16+08	1	2024-05-26 12:30:16+08	1
2141	岗位查询	208	1	#	\N	\N	B	0	post:query	#	f	f	t	2024-05-26 12:30:16+08	1	2024-05-26 12:30:16+08	1
2142	岗位新增	208	2	#	\N	\N	B	0	post:add	#	f	f	t	2024-05-26 12:30:16+08	1	2024-05-26 12:30:16+08	1
2143	岗位修改	208	3	#	\N	\N	B	0	post:edit	#	f	f	t	2024-05-26 12:30:16+08	1	2024-05-26 12:30:16+08	1
2144	岗位删除	208	4	#	\N	\N	B	0	post:remove	#	f	f	t	2024-05-26 12:30:16+08	1	2024-05-26 12:30:16+08	1
2145	岗位导出	208	5	#	\N	\N	B	0	post:export	#	f	f	t	2024-05-26 12:30:16+08	1	2024-05-26 12:30:16+08	1
2207	权限授予	206	6	#	\N	\N	B	0	sys_role:auth	#	f	f	t	2024-05-26 12:30:16+08	1	2024-05-26 12:30:16+08	1
2208	角色查询	206	1	#	\N	\N	B	0	sys_role:query	#	f	f	t	2024-05-26 12:30:16+08	1	2024-05-26 12:30:16+08	1
2209	角色新增	206	2	#	\N	\N	B	0	sys_role:add	#	f	f	t	2024-05-26 12:30:16+08	1	2024-05-26 12:30:16+08	1
2210	角色修改	206	3	#	\N	\N	B	0	sys_role:edit	#	f	f	t	2024-05-26 12:30:16+08	1	2024-05-26 12:30:16+08	1
2211	角色删除	206	4	#	\N	\N	B	0	sys_role:remove	#	f	f	t	2024-05-26 12:30:16+08	1	2024-05-26 12:30:16+08	1
3121	站点查询	400	0	#	\N	\N	B	0	station:query	#	f	f	t	2024-05-26 12:30:16+08	1	2024-05-26 12:30:16+08	\N
3122	站点添加	400	1	#	\N	\N	B	0	station:add	#	f	f	t	2024-05-26 12:30:16+08	1	2024-05-26 12:30:16+08	\N
3123	站点修改	400	2	#	\N	\N	B	0	station:edit	#	f	f	t	2024-05-26 12:30:16+08	1	2024-05-26 12:30:16+08	\N
3124	站点删除	400	3	#	\N	\N	B	0	station:remove	#	f	f	t	2024-05-26 12:30:16+08	1	2024-05-26 12:30:16+08	\N
3125	站点导出	400	5	#	\N	\N	B	0	station:export	#	f	f	t	2024-05-26 12:30:16+08	1	2024-05-26 12:30:16+08	\N
3126	绑定解绑设备	400	10	#	\N	\N	B	0	station:bind	#	f	f	t	2024-05-26 12:30:16+08	1	2024-05-26 12:30:16+08	1
3226	设备查询	401	0	#	\N	\N	B	0	device:query	#	f	f	t	2024-05-26 12:30:16+08	1	2024-05-26 12:30:16+08	1
3227	设备添加	401	1	#	\N	\N	B	0	device:add	#	f	f	t	2024-05-26 12:30:16+08	1	2024-05-26 12:30:16+08	1
3228	设备修改	401	2	#	\N	\N	B	0	device:edit	#	f	f	t	2024-05-26 12:30:16+08	1	2024-05-26 12:30:16+08	1
3229	设备删除	401	3	#	\N	\N	B	0	device:remove	#	f	f	t	2024-05-26 12:30:16+08	1	2024-05-26 12:30:16+08	1
3230	设备导出	401	4	#	\N	\N	B	0	device:export	#	f	f	t	2024-05-26 12:30:16+08	1	2024-05-26 12:30:16+08	1
3231	设备敏感字段	3228	10	#	\N	\N	F	0	device:field	#	f	f	t	2024-05-26 12:30:16+08	1	2024-05-26 12:30:16+08	1
3301	协议详情	402	10	#	\N	\N	B	0	protocol:query	#	f	f	t	2024-05-26 12:30:16+08	1	2024-05-26 12:30:16+08	1
3302	协议添加	402	5	#	\N	\N	B	0	protocol:add	#	f	f	t	2024-05-26 12:30:16+08	1	2024-05-26 12:30:16+08	1
3303	协议修改	402	4	#	\N	\N	B	0	protocol:edit	#	f	f	t	2024-05-26 12:30:16+08	1	2024-05-26 12:30:16+08	1
3304	协议删除	402	1	#	\N	\N	B	0	protocol:remove	#	f	f	t	2024-05-26 12:30:16+08	1	2024-05-26 12:30:16+08	1
3401	产品详情	403	10	#	\N	\N	B	0	product:query	#	f	f	t	2024-05-26 12:30:16+08	1	2024-05-26 12:30:16+08	1
3402	产品添加	403	8	#	\N	\N	B	0	product:add	#	f	f	t	2024-05-26 12:30:16+08	1	2024-05-26 12:30:16+08	1
3403	产品修改	403	4	#	\N	\N	B	0	product:edit	#	f	f	t	2024-05-26 12:30:16+08	1	2024-05-26 12:30:16+08	1
3404	产品删除	403	1	#	\N	\N	B	0	product:remove	#	f	f	t	2024-05-26 12:30:16+08	1	2024-05-26 12:30:16+08	1
4102	交易查询	500	0	#	\N	\N	B	0	order:query	#	f	f	t	2024-05-26 12:30:16+08	1	2024-05-26 12:30:16+08	\N
4204	优惠券查询	501	0	#	\N	\N	B	0	coupon:query	#	f	f	t	2024-05-26 12:30:16+08	1	2024-05-26 12:30:16+08	\N
4205	优惠券编辑	501	1	#	\N	\N	B	0	coupon:edit	#	f	f	t	2024-05-26 12:30:16+08	1	2024-05-26 12:30:16+08	\N
4206	优惠券添加	501	1	#	\N	\N	B	0	coupon:add	#	f	f	t	2024-05-26 12:30:16+08	1	2024-05-26 12:30:16+08	\N
4207	优惠券删除	501	2	#	\N	\N	B	0	coupon:remove	#	f	f	t	2024-05-26 12:30:16+08	1	2024-05-26 12:30:16+08	\N
\.


--
-- TOC entry 3454 (class 0 OID 34583)
-- Dependencies: 222
-- Data for Name: sys_post; Type: TABLE DATA; Schema: biz_sys; Owner: wz
--

COPY biz_sys.sys_post (post_id, tenant_id, post_code, post_name, status, post_sort, creator_id, updater_id, create_at, update_at) FROM stdin;
1	0	CEO	执行总裁	0	1	1	1	2024-04-17 14:08:54.616+08	2024-06-22 15:36:26.749+08
4	0	CTO	首席技术官	1	3	1	2	2024-04-17 14:08:54.616+08	2024-08-27 14:26:42.367+08
\.


--
-- TOC entry 3455 (class 0 OID 34586)
-- Dependencies: 223
-- Data for Name: sys_role; Type: TABLE DATA; Schema: biz_sys; Owner: wz
--

COPY biz_sys.sys_role (role_id, tenant_id, data_scope, role_name, status, role_sort, is_menu_display, is_dept_display, create_at, creator_id, update_at, updater_id) FROM stdin;
\.


--
-- TOC entry 3456 (class 0 OID 34591)
-- Dependencies: 224
-- Data for Name: sys_role_dept; Type: TABLE DATA; Schema: biz_sys; Owner: wz
--

COPY biz_sys.sys_role_dept (role_id, dept_id) FROM stdin;
\.


--
-- TOC entry 3457 (class 0 OID 34594)
-- Dependencies: 225
-- Data for Name: sys_role_menu; Type: TABLE DATA; Schema: biz_sys; Owner: wz
--

COPY biz_sys.sys_role_menu (role_id, menu_id) FROM stdin;
\.


--
-- TOC entry 3458 (class 0 OID 34597)
-- Dependencies: 226
-- Data for Name: sys_tenant; Type: TABLE DATA; Schema: biz_sys; Owner: wz
--

COPY biz_sys.sys_tenant (tenant_id, administrator, tenant_type, contact_phone, tenant_name, oper_pwd, status, domain, remark, package_id, expire_time, account_limit, role_limit, post_limit, dept_limit, creator_id, create_at, updater_id, update_at) FROM stdin;
\.


--
-- TOC entry 3459 (class 0 OID 34600)
-- Dependencies: 227
-- Data for Name: sys_tenant_package; Type: TABLE DATA; Schema: biz_sys; Owner: wz
--

COPY biz_sys.sys_tenant_package (package_id, package_name, status, menu_ids, is_menu_display, remark, creator_id, create_at, updater_id, update_at) FROM stdin;
1773625804122202113	默认套餐	0	{1,2,3,601,4,401,104,151,1064,150,203,2078,2062,2064,2077,2072,2071,206,2207,2211,2210,2209,2208,205,2040,2039,2038,2037,208,2145,2144,2143,2142,2141,1125,1126,1127,3228,3226,400,3126,3125,3124,3123,3122,3121}	f	通用租户套餐	1	2024-04-17 14:08:54+08	1	2025-01-28 15:27:30+08
\.


--
-- TOC entry 3460 (class 0 OID 34607)
-- Dependencies: 228
-- Data for Name: sys_tenant_wallet; Type: TABLE DATA; Schema: biz_sys; Owner: wz
--

COPY biz_sys.sys_tenant_wallet (tenant_id, balance, status) FROM stdin;
\.


--
-- TOC entry 3461 (class 0 OID 34610)
-- Dependencies: 229
-- Data for Name: sys_tenant_wallet_record; Type: TABLE DATA; Schema: biz_sys; Owner: wz
--

COPY biz_sys.sys_tenant_wallet_record (record_id, tenant_id, amount, record_type, create_at, remark) FROM stdin;
\.


--
-- TOC entry 3462 (class 0 OID 34613)
-- Dependencies: 230
-- Data for Name: sys_user; Type: TABLE DATA; Schema: biz_sys; Owner: wz
--

COPY biz_sys.sys_user (user_id, tenant_id, dept_id, username, email, nickname, phone_number, status, gender, avatar, password, login_ip, login_date, remark, creator_id, updater_id, create_at, update_at) FROM stdin;
1	0	\N	admin	xxxxx@163.com	nick_admin	15888888888	0	1	\N	{bcrypt}$2a$10$7JB720yubVSZvUI0rEqK/.VqGOZTH.ulu33dHOiBE8ByOhJIrdAu2	::1	2025-03-02 10:19:06.324+08	\N	1	\N	2024-04-17 14:08:54.616+08	\N
\.


--
-- TOC entry 3463 (class 0 OID 34618)
-- Dependencies: 231
-- Data for Name: sys_user_post; Type: TABLE DATA; Schema: biz_sys; Owner: wz
--

COPY biz_sys.sys_user_post (user_id, post_id) FROM stdin;
\.


--
-- TOC entry 3464 (class 0 OID 34621)
-- Dependencies: 232
-- Data for Name: sys_user_role; Type: TABLE DATA; Schema: biz_sys; Owner: wz
--

COPY biz_sys.sys_user_role (user_id, role_id) FROM stdin;
\.


--
-- TOC entry 3465 (class 0 OID 34624)
-- Dependencies: 233
-- Data for Name: sys_withdrawal_record; Type: TABLE DATA; Schema: biz_sys; Owner: wz
--

COPY biz_sys.sys_withdrawal_record (withdrawal_id, order_no, status, tenant_id, request_params, amount, error_msg, creator_id, create_at, complete_at, remark) FROM stdin;
\.


--
-- TOC entry 3257 (class 2606 OID 33066)
-- Name: app_user app_user_pkey; Type: CONSTRAINT; Schema: biz_app; Owner: wz
--

ALTER TABLE ONLY biz_app.app_user
    ADD CONSTRAINT app_user_pkey PRIMARY KEY (user_id);


--
-- TOC entry 3260 (class 2606 OID 33071)
-- Name: app_user_thirdinfo app_user_thirdinfo_pkey; Type: CONSTRAINT; Schema: biz_app; Owner: wz
--

ALTER TABLE ONLY biz_app.app_user_thirdinfo
    ADD CONSTRAINT app_user_thirdinfo_pkey PRIMARY KEY (user_id);


--
-- TOC entry 3263 (class 2606 OID 33077)
-- Name: app_user_wallet app_user_wallet_pkey; Type: CONSTRAINT; Schema: biz_app; Owner: wz
--

ALTER TABLE ONLY biz_app.app_user_wallet
    ADD CONSTRAINT app_user_wallet_pkey PRIMARY KEY (user_id);


--
-- TOC entry 3265 (class 2606 OID 33082)
-- Name: app_user_wallet_record app_user_wallet_record_pkey; Type: CONSTRAINT; Schema: biz_app; Owner: wz
--

ALTER TABLE ONLY biz_app.app_user_wallet_record
    ADD CONSTRAINT app_user_wallet_record_pkey PRIMARY KEY (record_id);


--
-- TOC entry 3268 (class 2606 OID 34630)
-- Name: oauth2_client oauth2_client_pkey; Type: CONSTRAINT; Schema: biz_sys; Owner: wz
--

ALTER TABLE ONLY biz_sys.oauth2_client
    ADD CONSTRAINT oauth2_client_pkey PRIMARY KEY (id);


--
-- TOC entry 3273 (class 2606 OID 34632)
-- Name: sys_dept sys_dept_pkey; Type: CONSTRAINT; Schema: biz_sys; Owner: wz
--

ALTER TABLE ONLY biz_sys.sys_dept
    ADD CONSTRAINT sys_dept_pkey PRIMARY KEY (dept_id);


--
-- TOC entry 3304 (class 2606 OID 35231)
-- Name: sys_menu sys_menu_copy1_pkey; Type: CONSTRAINT; Schema: biz_sys; Owner: wz
--

ALTER TABLE ONLY biz_sys.sys_menu
    ADD CONSTRAINT sys_menu_copy1_pkey PRIMARY KEY (menu_id);


--
-- TOC entry 3275 (class 2606 OID 34636)
-- Name: sys_post sys_post_pkey; Type: CONSTRAINT; Schema: biz_sys; Owner: wz
--

ALTER TABLE ONLY biz_sys.sys_post
    ADD CONSTRAINT sys_post_pkey PRIMARY KEY (post_id);


--
-- TOC entry 3279 (class 2606 OID 34638)
-- Name: sys_role_dept sys_role_dept_pkey; Type: CONSTRAINT; Schema: biz_sys; Owner: wz
--

ALTER TABLE ONLY biz_sys.sys_role_dept
    ADD CONSTRAINT sys_role_dept_pkey PRIMARY KEY (role_id, dept_id);


--
-- TOC entry 3281 (class 2606 OID 34640)
-- Name: sys_role_menu sys_role_menu_pkey; Type: CONSTRAINT; Schema: biz_sys; Owner: wz
--

ALTER TABLE ONLY biz_sys.sys_role_menu
    ADD CONSTRAINT sys_role_menu_pkey PRIMARY KEY (role_id, menu_id);


--
-- TOC entry 3277 (class 2606 OID 34642)
-- Name: sys_role sys_role_pkey; Type: CONSTRAINT; Schema: biz_sys; Owner: wz
--

ALTER TABLE ONLY biz_sys.sys_role
    ADD CONSTRAINT sys_role_pkey PRIMARY KEY (role_id);


--
-- TOC entry 3286 (class 2606 OID 34644)
-- Name: sys_tenant_package sys_tenant_package_pkey; Type: CONSTRAINT; Schema: biz_sys; Owner: wz
--

ALTER TABLE ONLY biz_sys.sys_tenant_package
    ADD CONSTRAINT sys_tenant_package_pkey PRIMARY KEY (package_id);


--
-- TOC entry 3283 (class 2606 OID 34646)
-- Name: sys_tenant sys_tenant_pkey; Type: CONSTRAINT; Schema: biz_sys; Owner: wz
--

ALTER TABLE ONLY biz_sys.sys_tenant
    ADD CONSTRAINT sys_tenant_pkey PRIMARY KEY (tenant_id);


--
-- TOC entry 3288 (class 2606 OID 34648)
-- Name: sys_tenant_wallet sys_tenant_wallet_pkey; Type: CONSTRAINT; Schema: biz_sys; Owner: wz
--

ALTER TABLE ONLY biz_sys.sys_tenant_wallet
    ADD CONSTRAINT sys_tenant_wallet_pkey PRIMARY KEY (tenant_id);


--
-- TOC entry 3291 (class 2606 OID 34650)
-- Name: sys_tenant_wallet_record sys_tenant_wallet_record_pkey; Type: CONSTRAINT; Schema: biz_sys; Owner: wz
--

ALTER TABLE ONLY biz_sys.sys_tenant_wallet_record
    ADD CONSTRAINT sys_tenant_wallet_record_pkey PRIMARY KEY (record_id);


--
-- TOC entry 3293 (class 2606 OID 34652)
-- Name: sys_user sys_user_pkey; Type: CONSTRAINT; Schema: biz_sys; Owner: wz
--

ALTER TABLE ONLY biz_sys.sys_user
    ADD CONSTRAINT sys_user_pkey PRIMARY KEY (user_id);


--
-- TOC entry 3297 (class 2606 OID 34654)
-- Name: sys_user_post sys_user_post_pkey; Type: CONSTRAINT; Schema: biz_sys; Owner: wz
--

ALTER TABLE ONLY biz_sys.sys_user_post
    ADD CONSTRAINT sys_user_post_pkey PRIMARY KEY (user_id, post_id);


--
-- TOC entry 3299 (class 2606 OID 34656)
-- Name: sys_user_role sys_user_role_pkey; Type: CONSTRAINT; Schema: biz_sys; Owner: wz
--

ALTER TABLE ONLY biz_sys.sys_user_role
    ADD CONSTRAINT sys_user_role_pkey PRIMARY KEY (user_id, role_id);


--
-- TOC entry 3302 (class 2606 OID 34658)
-- Name: sys_withdrawal_record sys_withdrawal_record_pkey; Type: CONSTRAINT; Schema: biz_sys; Owner: wz
--

ALTER TABLE ONLY biz_sys.sys_withdrawal_record
    ADD CONSTRAINT sys_withdrawal_record_pkey PRIMARY KEY (withdrawal_id);


--
-- TOC entry 3266 (class 1259 OID 34722)
-- Name: i_app_user_wallet_record_user_id; Type: INDEX; Schema: biz_app; Owner: wz
--

CREATE INDEX i_app_user_wallet_record_user_id ON biz_app.app_user_wallet_record USING btree (user_id);


--
-- TOC entry 3258 (class 1259 OID 34167)
-- Name: u_i_app_user_phone_number; Type: INDEX; Schema: biz_app; Owner: wz
--

CREATE UNIQUE INDEX u_i_app_user_phone_number ON biz_app.app_user USING btree (phone_number);


--
-- TOC entry 3261 (class 1259 OID 33072)
-- Name: u_i_identifier; Type: INDEX; Schema: biz_app; Owner: wz
--

CREATE UNIQUE INDEX u_i_identifier ON biz_app.app_user_thirdinfo USING btree (identifier);


--
-- TOC entry 3270 (class 1259 OID 34659)
-- Name: sys_dept_i_ancestors; Type: INDEX; Schema: biz_sys; Owner: wz
--

CREATE INDEX sys_dept_i_ancestors ON biz_sys.sys_dept USING btree (ancestors);


--
-- TOC entry 3271 (class 1259 OID 34660)
-- Name: sys_dept_i_parent_id; Type: INDEX; Schema: biz_sys; Owner: wz
--

CREATE INDEX sys_dept_i_parent_id ON biz_sys.sys_dept USING btree (parent_id);


--
-- TOC entry 3305 (class 1259 OID 35232)
-- Name: sys_menu_i_parent_id_copy1; Type: INDEX; Schema: biz_sys; Owner: wz
--

CREATE INDEX sys_menu_i_parent_id_copy1 ON biz_sys.sys_menu USING btree (parent_id);


--
-- TOC entry 3289 (class 1259 OID 34662)
-- Name: sys_tenant_wallet_record_i_tenant_id; Type: INDEX; Schema: biz_sys; Owner: wz
--

CREATE INDEX sys_tenant_wallet_record_i_tenant_id ON biz_sys.sys_tenant_wallet_record USING btree (tenant_id);


--
-- TOC entry 3300 (class 1259 OID 34663)
-- Name: sys_withdrawal_record_i_order_no; Type: INDEX; Schema: biz_sys; Owner: wz
--

CREATE UNIQUE INDEX sys_withdrawal_record_i_order_no ON biz_sys.sys_withdrawal_record USING btree (order_no);


--
-- TOC entry 3284 (class 1259 OID 34664)
-- Name: u_i_administrator; Type: INDEX; Schema: biz_sys; Owner: wz
--

CREATE UNIQUE INDEX u_i_administrator ON biz_sys.sys_tenant USING btree (administrator);


--
-- TOC entry 3269 (class 1259 OID 34665)
-- Name: u_i_client_id; Type: INDEX; Schema: biz_sys; Owner: wz
--

CREATE UNIQUE INDEX u_i_client_id ON biz_sys.oauth2_client USING btree (client_id);


--
-- TOC entry 3294 (class 1259 OID 34666)
-- Name: u_i_sys_user_phone_number; Type: INDEX; Schema: biz_sys; Owner: wz
--

CREATE UNIQUE INDEX u_i_sys_user_phone_number ON biz_sys.sys_user USING btree (phone_number);


--
-- TOC entry 3295 (class 1259 OID 34667)
-- Name: u_i_sys_user_username; Type: INDEX; Schema: biz_sys; Owner: wz
--

CREATE UNIQUE INDEX u_i_sys_user_username ON biz_sys.sys_user USING btree (username);


-- Completed on 2025-03-03 13:47:22

--
-- PostgreSQL database dump complete
--

