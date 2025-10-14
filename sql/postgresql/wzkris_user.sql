--
-- PostgreSQL database dump
--

-- Dumped from database version 15.13
-- Dumped by pg_dump version 15.13

-- Started on 2025-09-23 10:07:12

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
-- TOC entry 6 (class 2615 OID 16581)
-- Name: biz; Type: SCHEMA; Schema: -; Owner: postgres
--

CREATE SCHEMA biz;


ALTER SCHEMA biz OWNER TO postgres;

SET default_tablespace = '';

SET default_table_access_method = heap;

--
-- TOC entry 215 (class 1259 OID 16582)
-- Name: customer_info; Type: TABLE; Schema: biz; Owner: postgres
--

CREATE TABLE biz.customer_info (
    customer_id bigint NOT NULL,
    nickname character varying(30),
    phone_number character varying(16),
    status character(1) NOT NULL,
    gender character(1) NOT NULL,
    avatar character varying(150),
    login_ip inet,
    login_date timestamp(6) with time zone,
    creator_id bigint NOT NULL,
    updater_id bigint,
    create_at timestamp(6) with time zone NOT NULL,
    update_at timestamp(6) with time zone
);


ALTER TABLE biz.customer_info OWNER TO postgres;

--
-- TOC entry 3465 (class 0 OID 0)
-- Dependencies: 215
-- Name: TABLE customer_info; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON TABLE biz.customer_info IS '用户信息表';


--
-- TOC entry 3466 (class 0 OID 0)
-- Dependencies: 215
-- Name: COLUMN customer_info.customer_id; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.customer_info.customer_id IS '用户ID';


--
-- TOC entry 3467 (class 0 OID 0)
-- Dependencies: 215
-- Name: COLUMN customer_info.nickname; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.customer_info.nickname IS '用户昵称';


--
-- TOC entry 3468 (class 0 OID 0)
-- Dependencies: 215
-- Name: COLUMN customer_info.phone_number; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.customer_info.phone_number IS '手机号码';


--
-- TOC entry 3469 (class 0 OID 0)
-- Dependencies: 215
-- Name: COLUMN customer_info.status; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.customer_info.status IS '状态值';


--
-- TOC entry 3470 (class 0 OID 0)
-- Dependencies: 215
-- Name: COLUMN customer_info.gender; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.customer_info.gender IS '用户性别（0男 1女 2未知）';


--
-- TOC entry 3471 (class 0 OID 0)
-- Dependencies: 215
-- Name: COLUMN customer_info.avatar; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.customer_info.avatar IS '头像地址';


--
-- TOC entry 3472 (class 0 OID 0)
-- Dependencies: 215
-- Name: COLUMN customer_info.login_ip; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.customer_info.login_ip IS '登录ip';


--
-- TOC entry 3473 (class 0 OID 0)
-- Dependencies: 215
-- Name: COLUMN customer_info.login_date; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.customer_info.login_date IS '登录时间';


--
-- TOC entry 3474 (class 0 OID 0)
-- Dependencies: 215
-- Name: COLUMN customer_info.creator_id; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.customer_info.creator_id IS '创建者';


--
-- TOC entry 3475 (class 0 OID 0)
-- Dependencies: 215
-- Name: COLUMN customer_info.updater_id; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.customer_info.updater_id IS '更新者';


--
-- TOC entry 216 (class 1259 OID 16587)
-- Name: customer_social_info; Type: TABLE; Schema: biz; Owner: postgres
--

CREATE TABLE biz.customer_social_info (
    customer_id bigint NOT NULL,
    identifier character varying(32) NOT NULL,
    identifier_type character varying(10) NOT NULL
);


ALTER TABLE biz.customer_social_info OWNER TO postgres;

--
-- TOC entry 3476 (class 0 OID 0)
-- Dependencies: 216
-- Name: TABLE customer_social_info; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON TABLE biz.customer_social_info IS '第三方信息';


--
-- TOC entry 3477 (class 0 OID 0)
-- Dependencies: 216
-- Name: COLUMN customer_social_info.identifier; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.customer_social_info.identifier IS '三方唯一标识符';


--
-- TOC entry 3478 (class 0 OID 0)
-- Dependencies: 216
-- Name: COLUMN customer_social_info.identifier_type; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.customer_social_info.identifier_type IS '三方渠道';


--
-- TOC entry 217 (class 1259 OID 16590)
-- Name: customer_wallet_info; Type: TABLE; Schema: biz; Owner: postgres
--

CREATE TABLE biz.customer_wallet_info (
    customer_id bigint NOT NULL,
    balance numeric(10,2) NOT NULL,
    status character(1) NOT NULL
);


ALTER TABLE biz.customer_wallet_info OWNER TO postgres;

--
-- TOC entry 3479 (class 0 OID 0)
-- Dependencies: 217
-- Name: TABLE customer_wallet_info; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON TABLE biz.customer_wallet_info IS '用户钱包';


--
-- TOC entry 3480 (class 0 OID 0)
-- Dependencies: 217
-- Name: COLUMN customer_wallet_info.balance; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.customer_wallet_info.balance IS '余额, 元';


--
-- TOC entry 3481 (class 0 OID 0)
-- Dependencies: 217
-- Name: COLUMN customer_wallet_info.status; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.customer_wallet_info.status IS '状态';


--
-- TOC entry 218 (class 1259 OID 16593)
-- Name: customer_wallet_record; Type: TABLE; Schema: biz; Owner: postgres
--

CREATE TABLE biz.customer_wallet_record (
    record_id bigint NOT NULL,
    customer_id bigint NOT NULL,
    amount numeric(10,2) NOT NULL,
    record_type character(1) NOT NULL,
    create_at timestamp(6) with time zone NOT NULL,
    remark character varying(100)
);


ALTER TABLE biz.customer_wallet_record OWNER TO postgres;

--
-- TOC entry 3482 (class 0 OID 0)
-- Dependencies: 218
-- Name: TABLE customer_wallet_record; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON TABLE biz.customer_wallet_record IS '用户钱包记录';


--
-- TOC entry 3483 (class 0 OID 0)
-- Dependencies: 218
-- Name: COLUMN customer_wallet_record.customer_id; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.customer_wallet_record.customer_id IS '客户ID';


--
-- TOC entry 3484 (class 0 OID 0)
-- Dependencies: 218
-- Name: COLUMN customer_wallet_record.amount; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.customer_wallet_record.amount IS '金额, 元';


--
-- TOC entry 3485 (class 0 OID 0)
-- Dependencies: 218
-- Name: COLUMN customer_wallet_record.record_type; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.customer_wallet_record.record_type IS '记录类型';


--
-- TOC entry 3486 (class 0 OID 0)
-- Dependencies: 218
-- Name: COLUMN customer_wallet_record.create_at; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.customer_wallet_record.create_at IS '创建时间';


--
-- TOC entry 3487 (class 0 OID 0)
-- Dependencies: 218
-- Name: COLUMN customer_wallet_record.remark; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.customer_wallet_record.remark IS '备注';


--
-- TOC entry 220 (class 1259 OID 16604)
-- Name: dept_info; Type: TABLE; Schema: biz; Owner: postgres
--

CREATE TABLE biz.dept_info (
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


ALTER TABLE biz.dept_info OWNER TO postgres;

--
-- TOC entry 3488 (class 0 OID 0)
-- Dependencies: 220
-- Name: TABLE dept_info; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON TABLE biz.dept_info IS '部门表';


--
-- TOC entry 3489 (class 0 OID 0)
-- Dependencies: 220
-- Name: COLUMN dept_info.dept_id; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.dept_info.dept_id IS '部门id';


--
-- TOC entry 3490 (class 0 OID 0)
-- Dependencies: 220
-- Name: COLUMN dept_info.tenant_id; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.dept_info.tenant_id IS '租户ID';


--
-- TOC entry 3491 (class 0 OID 0)
-- Dependencies: 220
-- Name: COLUMN dept_info.parent_id; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.dept_info.parent_id IS '父部门id';


--
-- TOC entry 3492 (class 0 OID 0)
-- Dependencies: 220
-- Name: COLUMN dept_info.ancestors; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.dept_info.ancestors IS '祖级列表';


--
-- TOC entry 3493 (class 0 OID 0)
-- Dependencies: 220
-- Name: COLUMN dept_info.dept_name; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.dept_info.dept_name IS '部门名称';


--
-- TOC entry 3494 (class 0 OID 0)
-- Dependencies: 220
-- Name: COLUMN dept_info.status; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.dept_info.status IS '0代表正常 1代表停用';


--
-- TOC entry 3495 (class 0 OID 0)
-- Dependencies: 220
-- Name: COLUMN dept_info.dept_sort; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.dept_info.dept_sort IS '显示顺序';


--
-- TOC entry 3496 (class 0 OID 0)
-- Dependencies: 220
-- Name: COLUMN dept_info.contact; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.dept_info.contact IS '联系电话';


--
-- TOC entry 3497 (class 0 OID 0)
-- Dependencies: 220
-- Name: COLUMN dept_info.email; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.dept_info.email IS '邮箱';


--
-- TOC entry 3498 (class 0 OID 0)
-- Dependencies: 220
-- Name: COLUMN dept_info.creator_id; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.dept_info.creator_id IS '创建者';


--
-- TOC entry 3499 (class 0 OID 0)
-- Dependencies: 220
-- Name: COLUMN dept_info.updater_id; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.dept_info.updater_id IS '更新者';


--
-- TOC entry 221 (class 1259 OID 16611)
-- Name: menu_info; Type: TABLE; Schema: biz; Owner: postgres
--

CREATE TABLE biz.menu_info (
    menu_id bigint NOT NULL,
    menu_name character varying(30) NOT NULL,
    parent_id bigint NOT NULL,
    menu_sort integer NOT NULL,
    path character varying(50) DEFAULT '#'::character varying NOT NULL,
    component character varying(50),
    query character varying(50),
    menu_type character(1) NOT NULL,
    status character(1) NOT NULL,
    perms character varying(50),
    icon character varying(50) DEFAULT '#'::character varying NOT NULL,
    cacheable boolean NOT NULL,
    visible boolean NOT NULL,
    create_at timestamp(6) with time zone NOT NULL,
    creator_id bigint NOT NULL,
    update_at timestamp(6) with time zone,
    updater_id bigint
);


ALTER TABLE biz.menu_info OWNER TO postgres;

--
-- TOC entry 3500 (class 0 OID 0)
-- Dependencies: 221
-- Name: TABLE menu_info; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON TABLE biz.menu_info IS '菜单权限表';


--
-- TOC entry 3501 (class 0 OID 0)
-- Dependencies: 221
-- Name: COLUMN menu_info.menu_id; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.menu_info.menu_id IS '菜单ID';


--
-- TOC entry 3502 (class 0 OID 0)
-- Dependencies: 221
-- Name: COLUMN menu_info.menu_name; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.menu_info.menu_name IS '菜单名称';


--
-- TOC entry 3503 (class 0 OID 0)
-- Dependencies: 221
-- Name: COLUMN menu_info.parent_id; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.menu_info.parent_id IS '父菜单ID';


--
-- TOC entry 3504 (class 0 OID 0)
-- Dependencies: 221
-- Name: COLUMN menu_info.menu_sort; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.menu_info.menu_sort IS '显示顺序';


--
-- TOC entry 3505 (class 0 OID 0)
-- Dependencies: 221
-- Name: COLUMN menu_info.path; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.menu_info.path IS '路由地址';


--
-- TOC entry 3506 (class 0 OID 0)
-- Dependencies: 221
-- Name: COLUMN menu_info.component; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.menu_info.component IS '组件路径';


--
-- TOC entry 3507 (class 0 OID 0)
-- Dependencies: 221
-- Name: COLUMN menu_info.query; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.menu_info.query IS '路由参数';


--
-- TOC entry 3508 (class 0 OID 0)
-- Dependencies: 221
-- Name: COLUMN menu_info.menu_type; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.menu_info.menu_type IS '菜单类型（D目录 M菜单 B按钮 I内链 O外链）';


--
-- TOC entry 3509 (class 0 OID 0)
-- Dependencies: 221
-- Name: COLUMN menu_info.status; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.menu_info.status IS '菜单状态（0正常 1停用）';


--
-- TOC entry 3510 (class 0 OID 0)
-- Dependencies: 221
-- Name: COLUMN menu_info.perms; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.menu_info.perms IS '权限标识';


--
-- TOC entry 3511 (class 0 OID 0)
-- Dependencies: 221
-- Name: COLUMN menu_info.icon; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.menu_info.icon IS '菜单图标';


--
-- TOC entry 3512 (class 0 OID 0)
-- Dependencies: 221
-- Name: COLUMN menu_info.cacheable; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.menu_info.cacheable IS '是否缓存';


--
-- TOC entry 3513 (class 0 OID 0)
-- Dependencies: 221
-- Name: COLUMN menu_info.visible; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.menu_info.visible IS '是否显示';


--
-- TOC entry 3514 (class 0 OID 0)
-- Dependencies: 221
-- Name: COLUMN menu_info.create_at; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.menu_info.create_at IS '创建时间';


--
-- TOC entry 3515 (class 0 OID 0)
-- Dependencies: 221
-- Name: COLUMN menu_info.creator_id; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.menu_info.creator_id IS '创建者';


--
-- TOC entry 3516 (class 0 OID 0)
-- Dependencies: 221
-- Name: COLUMN menu_info.update_at; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.menu_info.update_at IS '更新时间';


--
-- TOC entry 3517 (class 0 OID 0)
-- Dependencies: 221
-- Name: COLUMN menu_info.updater_id; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.menu_info.updater_id IS '更新者';


--
-- TOC entry 219 (class 1259 OID 16596)
-- Name: oauth2_client; Type: TABLE; Schema: biz; Owner: postgres
--

CREATE TABLE biz.oauth2_client (
    id bigint NOT NULL,
    client_name character varying(32),
    client_id character varying(32) NOT NULL,
    client_secret character varying(200) NOT NULL,
    scopes text[] DEFAULT '{}'::text[] NOT NULL,
    authorization_grant_types text[] DEFAULT '{}'::text[] NOT NULL,
    redirect_uris text[] DEFAULT '{}'::text[] NOT NULL,
    status character(1) NOT NULL,
    auto_approve boolean NOT NULL,
    create_at timestamp(6) with time zone NOT NULL,
    creator_id bigint NOT NULL,
    update_at timestamp(6) with time zone,
    updater_id bigint
);


ALTER TABLE biz.oauth2_client OWNER TO postgres;

--
-- TOC entry 3518 (class 0 OID 0)
-- Dependencies: 219
-- Name: TABLE oauth2_client; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON TABLE biz.oauth2_client IS 'OAUTH2客户端';


--
-- TOC entry 3519 (class 0 OID 0)
-- Dependencies: 219
-- Name: COLUMN oauth2_client.client_name; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.oauth2_client.client_name IS '客户端名称';


--
-- TOC entry 3520 (class 0 OID 0)
-- Dependencies: 219
-- Name: COLUMN oauth2_client.client_id; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.oauth2_client.client_id IS 'APP_ID';


--
-- TOC entry 3521 (class 0 OID 0)
-- Dependencies: 219
-- Name: COLUMN oauth2_client.client_secret; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.oauth2_client.client_secret IS 'APP密钥';


--
-- TOC entry 3522 (class 0 OID 0)
-- Dependencies: 219
-- Name: COLUMN oauth2_client.scopes; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.oauth2_client.scopes IS '权限域';


--
-- TOC entry 3523 (class 0 OID 0)
-- Dependencies: 219
-- Name: COLUMN oauth2_client.authorization_grant_types; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.oauth2_client.authorization_grant_types IS '授权类型';


--
-- TOC entry 3524 (class 0 OID 0)
-- Dependencies: 219
-- Name: COLUMN oauth2_client.redirect_uris; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.oauth2_client.redirect_uris IS '回调地址';


--
-- TOC entry 3525 (class 0 OID 0)
-- Dependencies: 219
-- Name: COLUMN oauth2_client.status; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.oauth2_client.status IS '客户端状态';


--
-- TOC entry 3526 (class 0 OID 0)
-- Dependencies: 219
-- Name: COLUMN oauth2_client.auto_approve; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.oauth2_client.auto_approve IS '是否自动放行';


--
-- TOC entry 222 (class 1259 OID 16616)
-- Name: role_info; Type: TABLE; Schema: biz; Owner: root
--

CREATE TABLE biz.role_info (
    role_id bigint NOT NULL,
    tenant_id bigint NOT NULL,
    data_scope character(1) NOT NULL,
    role_name character varying(20) NOT NULL,
    status character(1) NOT NULL,
    inherited boolean DEFAULT false NOT NULL,
    role_sort smallint NOT NULL,
    create_at timestamp(6) with time zone NOT NULL,
    creator_id bigint NOT NULL,
    update_at timestamp(6) with time zone,
    updater_id bigint
);


ALTER TABLE biz.role_info OWNER TO root;

--
-- TOC entry 3527 (class 0 OID 0)
-- Dependencies: 222
-- Name: COLUMN role_info.role_id; Type: COMMENT; Schema: biz; Owner: root
--

COMMENT ON COLUMN biz.role_info.role_id IS '角色ID';


--
-- TOC entry 3528 (class 0 OID 0)
-- Dependencies: 222
-- Name: COLUMN role_info.tenant_id; Type: COMMENT; Schema: biz; Owner: root
--

COMMENT ON COLUMN biz.role_info.tenant_id IS '租户ID';


--
-- TOC entry 3529 (class 0 OID 0)
-- Dependencies: 222
-- Name: COLUMN role_info.data_scope; Type: COMMENT; Schema: biz; Owner: root
--

COMMENT ON COLUMN biz.role_info.data_scope IS '数据范围（1=所有数据权限,2=自定义数据权限,3=本部门数据权限,4=本部门及以下数据权限,5=仅本人数据权限）';


--
-- TOC entry 3530 (class 0 OID 0)
-- Dependencies: 222
-- Name: COLUMN role_info.role_name; Type: COMMENT; Schema: biz; Owner: root
--

COMMENT ON COLUMN biz.role_info.role_name IS '角色名称';


--
-- TOC entry 3531 (class 0 OID 0)
-- Dependencies: 222
-- Name: COLUMN role_info.status; Type: COMMENT; Schema: biz; Owner: root
--

COMMENT ON COLUMN biz.role_info.status IS '状态（0代表正常 1代表停用）';


--
-- TOC entry 3532 (class 0 OID 0)
-- Dependencies: 222
-- Name: COLUMN role_info.inherited; Type: COMMENT; Schema: biz; Owner: root
--

COMMENT ON COLUMN biz.role_info.inherited IS '继承关系';


--
-- TOC entry 3533 (class 0 OID 0)
-- Dependencies: 222
-- Name: COLUMN role_info.role_sort; Type: COMMENT; Schema: biz; Owner: root
--

COMMENT ON COLUMN biz.role_info.role_sort IS '排序';


--
-- TOC entry 223 (class 1259 OID 16620)
-- Name: role_to_dept; Type: TABLE; Schema: biz; Owner: postgres
--

CREATE TABLE biz.role_to_dept (
    role_id bigint NOT NULL,
    dept_id bigint NOT NULL
);


ALTER TABLE biz.role_to_dept OWNER TO postgres;

--
-- TOC entry 3534 (class 0 OID 0)
-- Dependencies: 223
-- Name: TABLE role_to_dept; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON TABLE biz.role_to_dept IS '角色数据权限关联表';


--
-- TOC entry 3535 (class 0 OID 0)
-- Dependencies: 223
-- Name: COLUMN role_to_dept.role_id; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.role_to_dept.role_id IS '角色id';


--
-- TOC entry 3536 (class 0 OID 0)
-- Dependencies: 223
-- Name: COLUMN role_to_dept.dept_id; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.role_to_dept.dept_id IS '部门id';


--
-- TOC entry 224 (class 1259 OID 16623)
-- Name: role_to_hierarchy; Type: TABLE; Schema: biz; Owner: root
--

CREATE TABLE biz.role_to_hierarchy (
    role_id bigint NOT NULL,
    inherited_id bigint NOT NULL
);


ALTER TABLE biz.role_to_hierarchy OWNER TO root;

--
-- TOC entry 3537 (class 0 OID 0)
-- Dependencies: 224
-- Name: TABLE role_to_hierarchy; Type: COMMENT; Schema: biz; Owner: root
--

COMMENT ON TABLE biz.role_to_hierarchy IS '角色继承表';


--
-- TOC entry 3538 (class 0 OID 0)
-- Dependencies: 224
-- Name: COLUMN role_to_hierarchy.role_id; Type: COMMENT; Schema: biz; Owner: root
--

COMMENT ON COLUMN biz.role_to_hierarchy.role_id IS '角色id';


--
-- TOC entry 3539 (class 0 OID 0)
-- Dependencies: 224
-- Name: COLUMN role_to_hierarchy.inherited_id; Type: COMMENT; Schema: biz; Owner: root
--

COMMENT ON COLUMN biz.role_to_hierarchy.inherited_id IS '继承角色id';


--
-- TOC entry 225 (class 1259 OID 16626)
-- Name: role_to_menu; Type: TABLE; Schema: biz; Owner: postgres
--

CREATE TABLE biz.role_to_menu (
    role_id bigint NOT NULL,
    menu_id bigint NOT NULL
);


ALTER TABLE biz.role_to_menu OWNER TO postgres;

--
-- TOC entry 3540 (class 0 OID 0)
-- Dependencies: 225
-- Name: TABLE role_to_menu; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON TABLE biz.role_to_menu IS '角色和菜单关联表';


--
-- TOC entry 3541 (class 0 OID 0)
-- Dependencies: 225
-- Name: COLUMN role_to_menu.role_id; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.role_to_menu.role_id IS '角色ID';


--
-- TOC entry 3542 (class 0 OID 0)
-- Dependencies: 225
-- Name: COLUMN role_to_menu.menu_id; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.role_to_menu.menu_id IS '菜单ID';


--
-- TOC entry 226 (class 1259 OID 16629)
-- Name: tenant_info; Type: TABLE; Schema: biz; Owner: postgres
--

CREATE TABLE biz.tenant_info (
    tenant_id bigint NOT NULL,
    administrator bigint NOT NULL,
    tenant_type character(1) NOT NULL,
    contact_phone character varying(20),
    tenant_name character varying(32) NOT NULL,
    oper_pwd character varying(100) NOT NULL,
    status character(1) NOT NULL,
    domain character varying(100),
    remark character varying(200),
    package_id bigint,
    expire_time timestamp(6) with time zone NOT NULL,
    account_limit smallint NOT NULL,
    role_limit smallint NOT NULL,
    dept_limit smallint NOT NULL,
    creator_id bigint NOT NULL,
    create_at timestamp with time zone NOT NULL,
    updater_id bigint,
    update_at timestamp with time zone
);


ALTER TABLE biz.tenant_info OWNER TO postgres;

--
-- TOC entry 3543 (class 0 OID 0)
-- Dependencies: 226
-- Name: TABLE tenant_info; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON TABLE biz.tenant_info IS '租户表';


--
-- TOC entry 3544 (class 0 OID 0)
-- Dependencies: 226
-- Name: COLUMN tenant_info.tenant_id; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.tenant_info.tenant_id IS '租户编号';


--
-- TOC entry 3545 (class 0 OID 0)
-- Dependencies: 226
-- Name: COLUMN tenant_info.administrator; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.tenant_info.administrator IS '管理员ID';


--
-- TOC entry 3546 (class 0 OID 0)
-- Dependencies: 226
-- Name: COLUMN tenant_info.tenant_type; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.tenant_info.tenant_type IS '租户类型';


--
-- TOC entry 3547 (class 0 OID 0)
-- Dependencies: 226
-- Name: COLUMN tenant_info.contact_phone; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.tenant_info.contact_phone IS '联系电话';


--
-- TOC entry 3548 (class 0 OID 0)
-- Dependencies: 226
-- Name: COLUMN tenant_info.tenant_name; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.tenant_info.tenant_name IS '租户名称';


--
-- TOC entry 3549 (class 0 OID 0)
-- Dependencies: 226
-- Name: COLUMN tenant_info.oper_pwd; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.tenant_info.oper_pwd IS '操作密码';


--
-- TOC entry 3550 (class 0 OID 0)
-- Dependencies: 226
-- Name: COLUMN tenant_info.status; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.tenant_info.status IS '租户状态';


--
-- TOC entry 3551 (class 0 OID 0)
-- Dependencies: 226
-- Name: COLUMN tenant_info.domain; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.tenant_info.domain IS '域名';


--
-- TOC entry 3552 (class 0 OID 0)
-- Dependencies: 226
-- Name: COLUMN tenant_info.remark; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.tenant_info.remark IS '备注';


--
-- TOC entry 3553 (class 0 OID 0)
-- Dependencies: 226
-- Name: COLUMN tenant_info.package_id; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.tenant_info.package_id IS '租户套餐编号';


--
-- TOC entry 3554 (class 0 OID 0)
-- Dependencies: 226
-- Name: COLUMN tenant_info.expire_time; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.tenant_info.expire_time IS '过期时间';


--
-- TOC entry 3555 (class 0 OID 0)
-- Dependencies: 226
-- Name: COLUMN tenant_info.account_limit; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.tenant_info.account_limit IS '账号数量（-1不限制）';


--
-- TOC entry 3556 (class 0 OID 0)
-- Dependencies: 226
-- Name: COLUMN tenant_info.role_limit; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.tenant_info.role_limit IS '角色数量（-1不限制）';


--
-- TOC entry 3557 (class 0 OID 0)
-- Dependencies: 226
-- Name: COLUMN tenant_info.dept_limit; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.tenant_info.dept_limit IS '部门数量（-1不限制）';


--
-- TOC entry 3558 (class 0 OID 0)
-- Dependencies: 226
-- Name: COLUMN tenant_info.creator_id; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.tenant_info.creator_id IS '创建者';


--
-- TOC entry 3559 (class 0 OID 0)
-- Dependencies: 226
-- Name: COLUMN tenant_info.create_at; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.tenant_info.create_at IS '创建时间';


--
-- TOC entry 3560 (class 0 OID 0)
-- Dependencies: 226
-- Name: COLUMN tenant_info.updater_id; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.tenant_info.updater_id IS '更新者';


--
-- TOC entry 3561 (class 0 OID 0)
-- Dependencies: 226
-- Name: COLUMN tenant_info.update_at; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.tenant_info.update_at IS '更新时间';


--
-- TOC entry 227 (class 1259 OID 16632)
-- Name: tenant_package_info; Type: TABLE; Schema: biz; Owner: postgres
--

CREATE TABLE biz.tenant_package_info (
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


ALTER TABLE biz.tenant_package_info OWNER TO postgres;

--
-- TOC entry 3562 (class 0 OID 0)
-- Dependencies: 227
-- Name: TABLE tenant_package_info; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON TABLE biz.tenant_package_info IS '租户套餐表';


--
-- TOC entry 3563 (class 0 OID 0)
-- Dependencies: 227
-- Name: COLUMN tenant_package_info.package_id; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.tenant_package_info.package_id IS '租户套餐id';


--
-- TOC entry 3564 (class 0 OID 0)
-- Dependencies: 227
-- Name: COLUMN tenant_package_info.package_name; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.tenant_package_info.package_name IS '套餐名称';


--
-- TOC entry 3565 (class 0 OID 0)
-- Dependencies: 227
-- Name: COLUMN tenant_package_info.status; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.tenant_package_info.status IS '状态（0正常 1停用）';


--
-- TOC entry 3566 (class 0 OID 0)
-- Dependencies: 227
-- Name: COLUMN tenant_package_info.menu_ids; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.tenant_package_info.menu_ids IS '套餐绑定的菜单';


--
-- TOC entry 3567 (class 0 OID 0)
-- Dependencies: 227
-- Name: COLUMN tenant_package_info.remark; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.tenant_package_info.remark IS '备注';


--
-- TOC entry 3568 (class 0 OID 0)
-- Dependencies: 227
-- Name: COLUMN tenant_package_info.creator_id; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.tenant_package_info.creator_id IS '创建者';


--
-- TOC entry 3569 (class 0 OID 0)
-- Dependencies: 227
-- Name: COLUMN tenant_package_info.create_at; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.tenant_package_info.create_at IS '创建时间';


--
-- TOC entry 3570 (class 0 OID 0)
-- Dependencies: 227
-- Name: COLUMN tenant_package_info.updater_id; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.tenant_package_info.updater_id IS '更新者';


--
-- TOC entry 3571 (class 0 OID 0)
-- Dependencies: 227
-- Name: COLUMN tenant_package_info.update_at; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.tenant_package_info.update_at IS '更新时间';


--
-- TOC entry 228 (class 1259 OID 16638)
-- Name: tenant_wallet_info; Type: TABLE; Schema: biz; Owner: postgres
--

CREATE TABLE biz.tenant_wallet_info (
    tenant_id bigint NOT NULL,
    balance numeric(10,2) NOT NULL,
    status character(1) NOT NULL
);


ALTER TABLE biz.tenant_wallet_info OWNER TO postgres;

--
-- TOC entry 3572 (class 0 OID 0)
-- Dependencies: 228
-- Name: TABLE tenant_wallet_info; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON TABLE biz.tenant_wallet_info IS '租户钱包';


--
-- TOC entry 3573 (class 0 OID 0)
-- Dependencies: 228
-- Name: COLUMN tenant_wallet_info.balance; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.tenant_wallet_info.balance IS '余额, 元';


--
-- TOC entry 3574 (class 0 OID 0)
-- Dependencies: 228
-- Name: COLUMN tenant_wallet_info.status; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.tenant_wallet_info.status IS '状态';


--
-- TOC entry 229 (class 1259 OID 16641)
-- Name: tenant_wallet_record; Type: TABLE; Schema: biz; Owner: postgres
--

CREATE TABLE biz.tenant_wallet_record (
    record_id bigint NOT NULL,
    tenant_id bigint NOT NULL,
    amount numeric(10,2) NOT NULL,
    record_type character(1) NOT NULL,
    biz_type character(1) NOT NULL,
    biz_no character varying(32) NOT NULL,
    create_at timestamp with time zone NOT NULL,
    remark character varying(100)
);


ALTER TABLE biz.tenant_wallet_record OWNER TO postgres;

--
-- TOC entry 3575 (class 0 OID 0)
-- Dependencies: 229
-- Name: TABLE tenant_wallet_record; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON TABLE biz.tenant_wallet_record IS '租户钱包记录';


--
-- TOC entry 3576 (class 0 OID 0)
-- Dependencies: 229
-- Name: COLUMN tenant_wallet_record.tenant_id; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.tenant_wallet_record.tenant_id IS '租户ID';


--
-- TOC entry 3577 (class 0 OID 0)
-- Dependencies: 229
-- Name: COLUMN tenant_wallet_record.amount; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.tenant_wallet_record.amount IS '金额, 单位元';


--
-- TOC entry 3578 (class 0 OID 0)
-- Dependencies: 229
-- Name: COLUMN tenant_wallet_record.record_type; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.tenant_wallet_record.record_type IS '记录类型';


--
-- TOC entry 3579 (class 0 OID 0)
-- Dependencies: 229
-- Name: COLUMN tenant_wallet_record.biz_type; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.tenant_wallet_record.biz_type IS '业务类型';


--
-- TOC entry 3580 (class 0 OID 0)
-- Dependencies: 229
-- Name: COLUMN tenant_wallet_record.biz_no; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.tenant_wallet_record.biz_no IS '业务编号';


--
-- TOC entry 3581 (class 0 OID 0)
-- Dependencies: 229
-- Name: COLUMN tenant_wallet_record.create_at; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.tenant_wallet_record.create_at IS '创建时间';


--
-- TOC entry 3582 (class 0 OID 0)
-- Dependencies: 229
-- Name: COLUMN tenant_wallet_record.remark; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.tenant_wallet_record.remark IS '备注';


--
-- TOC entry 232 (class 1259 OID 16654)
-- Name: tenant_wallet_withdrawal_record; Type: TABLE; Schema: biz; Owner: postgres
--

CREATE TABLE biz.tenant_wallet_withdrawal_record (
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


ALTER TABLE biz.tenant_wallet_withdrawal_record OWNER TO postgres;

--
-- TOC entry 3583 (class 0 OID 0)
-- Dependencies: 232
-- Name: TABLE tenant_wallet_withdrawal_record; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON TABLE biz.tenant_wallet_withdrawal_record IS '系统提现记录';


--
-- TOC entry 3584 (class 0 OID 0)
-- Dependencies: 232
-- Name: COLUMN tenant_wallet_withdrawal_record.withdrawal_id; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.tenant_wallet_withdrawal_record.withdrawal_id IS 'id';


--
-- TOC entry 3585 (class 0 OID 0)
-- Dependencies: 232
-- Name: COLUMN tenant_wallet_withdrawal_record.order_no; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.tenant_wallet_withdrawal_record.order_no IS '订单号';


--
-- TOC entry 3586 (class 0 OID 0)
-- Dependencies: 232
-- Name: COLUMN tenant_wallet_withdrawal_record.status; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.tenant_wallet_withdrawal_record.status IS '状态
''0'' 处理中
''1'' 成功
''2'' 失败';


--
-- TOC entry 3587 (class 0 OID 0)
-- Dependencies: 232
-- Name: COLUMN tenant_wallet_withdrawal_record.tenant_id; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.tenant_wallet_withdrawal_record.tenant_id IS '租户id';


--
-- TOC entry 3588 (class 0 OID 0)
-- Dependencies: 232
-- Name: COLUMN tenant_wallet_withdrawal_record.request_params; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.tenant_wallet_withdrawal_record.request_params IS '第三方请求参数';


--
-- TOC entry 3589 (class 0 OID 0)
-- Dependencies: 232
-- Name: COLUMN tenant_wallet_withdrawal_record.amount; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.tenant_wallet_withdrawal_record.amount IS '金额, 单位元';


--
-- TOC entry 3590 (class 0 OID 0)
-- Dependencies: 232
-- Name: COLUMN tenant_wallet_withdrawal_record.error_msg; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.tenant_wallet_withdrawal_record.error_msg IS '错误信息';


--
-- TOC entry 3591 (class 0 OID 0)
-- Dependencies: 232
-- Name: COLUMN tenant_wallet_withdrawal_record.creator_id; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.tenant_wallet_withdrawal_record.creator_id IS '创建者';


--
-- TOC entry 3592 (class 0 OID 0)
-- Dependencies: 232
-- Name: COLUMN tenant_wallet_withdrawal_record.create_at; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.tenant_wallet_withdrawal_record.create_at IS '创建时间';


--
-- TOC entry 3593 (class 0 OID 0)
-- Dependencies: 232
-- Name: COLUMN tenant_wallet_withdrawal_record.complete_at; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.tenant_wallet_withdrawal_record.complete_at IS '完成时间';


--
-- TOC entry 3594 (class 0 OID 0)
-- Dependencies: 232
-- Name: COLUMN tenant_wallet_withdrawal_record.remark; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.tenant_wallet_withdrawal_record.remark IS '备注';


--
-- TOC entry 230 (class 1259 OID 16644)
-- Name: user_info; Type: TABLE; Schema: biz; Owner: postgres
--

CREATE TABLE biz.user_info (
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


ALTER TABLE biz.user_info OWNER TO postgres;

--
-- TOC entry 3595 (class 0 OID 0)
-- Dependencies: 230
-- Name: TABLE user_info; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON TABLE biz.user_info IS '用户信息表';


--
-- TOC entry 3596 (class 0 OID 0)
-- Dependencies: 230
-- Name: COLUMN user_info.user_id; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.user_info.user_id IS '管理员ID';


--
-- TOC entry 3597 (class 0 OID 0)
-- Dependencies: 230
-- Name: COLUMN user_info.tenant_id; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.user_info.tenant_id IS '租户ID';


--
-- TOC entry 3598 (class 0 OID 0)
-- Dependencies: 230
-- Name: COLUMN user_info.dept_id; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.user_info.dept_id IS '部门ID';


--
-- TOC entry 3599 (class 0 OID 0)
-- Dependencies: 230
-- Name: COLUMN user_info.username; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.user_info.username IS '用户账号';


--
-- TOC entry 3600 (class 0 OID 0)
-- Dependencies: 230
-- Name: COLUMN user_info.email; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.user_info.email IS '用户邮箱';


--
-- TOC entry 3601 (class 0 OID 0)
-- Dependencies: 230
-- Name: COLUMN user_info.nickname; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.user_info.nickname IS '用户昵称';


--
-- TOC entry 3602 (class 0 OID 0)
-- Dependencies: 230
-- Name: COLUMN user_info.phone_number; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.user_info.phone_number IS '手机号码';


--
-- TOC entry 3603 (class 0 OID 0)
-- Dependencies: 230
-- Name: COLUMN user_info.status; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.user_info.status IS '状态值';


--
-- TOC entry 3604 (class 0 OID 0)
-- Dependencies: 230
-- Name: COLUMN user_info.gender; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.user_info.gender IS '用户性别（0男 1女 2未知）';


--
-- TOC entry 3605 (class 0 OID 0)
-- Dependencies: 230
-- Name: COLUMN user_info.avatar; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.user_info.avatar IS '头像地址';


--
-- TOC entry 3606 (class 0 OID 0)
-- Dependencies: 230
-- Name: COLUMN user_info.password; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.user_info.password IS '密码';


--
-- TOC entry 3607 (class 0 OID 0)
-- Dependencies: 230
-- Name: COLUMN user_info.login_ip; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.user_info.login_ip IS '登录ip';


--
-- TOC entry 3608 (class 0 OID 0)
-- Dependencies: 230
-- Name: COLUMN user_info.login_date; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.user_info.login_date IS '登录时间';


--
-- TOC entry 3609 (class 0 OID 0)
-- Dependencies: 230
-- Name: COLUMN user_info.remark; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.user_info.remark IS '备注';


--
-- TOC entry 3610 (class 0 OID 0)
-- Dependencies: 230
-- Name: COLUMN user_info.creator_id; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.user_info.creator_id IS '创建者';


--
-- TOC entry 3611 (class 0 OID 0)
-- Dependencies: 230
-- Name: COLUMN user_info.updater_id; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.user_info.updater_id IS '更新者';


--
-- TOC entry 231 (class 1259 OID 16651)
-- Name: user_to_role; Type: TABLE; Schema: biz; Owner: postgres
--

CREATE TABLE biz.user_to_role (
    user_id bigint NOT NULL,
    role_id bigint NOT NULL
);


ALTER TABLE biz.user_to_role OWNER TO postgres;

--
-- TOC entry 3612 (class 0 OID 0)
-- Dependencies: 231
-- Name: TABLE user_to_role; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON TABLE biz.user_to_role IS '用户和角色关联表';


--
-- TOC entry 3613 (class 0 OID 0)
-- Dependencies: 231
-- Name: COLUMN user_to_role.user_id; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.user_to_role.user_id IS '管理员ID';


--
-- TOC entry 3614 (class 0 OID 0)
-- Dependencies: 231
-- Name: COLUMN user_to_role.role_id; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.user_to_role.role_id IS '角色ID';


--
-- TOC entry 3442 (class 0 OID 16582)
-- Dependencies: 215
-- Data for Name: customer_info; Type: TABLE DATA; Schema: biz; Owner: postgres
--

COPY biz.customer_info (customer_id, nickname, phone_number, status, gender, avatar, login_ip, login_date, creator_id, updater_id, create_at, update_at) FROM stdin;
1826896461245968383	\N	15888888888	0	2	\N	172.16.8.76	2025-09-09 11:18:32+08	1	\N	2024-04-17 14:08:54.616+08	\N
\.


--
-- TOC entry 3443 (class 0 OID 16587)
-- Dependencies: 216
-- Data for Name: customer_social_info; Type: TABLE DATA; Schema: biz; Owner: postgres
--

COPY biz.customer_social_info (customer_id, identifier, identifier_type) FROM stdin;
\.


--
-- TOC entry 3444 (class 0 OID 16590)
-- Dependencies: 217
-- Data for Name: customer_wallet_info; Type: TABLE DATA; Schema: biz; Owner: postgres
--

COPY biz.customer_wallet_info (customer_id, balance, status) FROM stdin;
1826896461245968384	0.00	0
\.


--
-- TOC entry 3445 (class 0 OID 16593)
-- Dependencies: 218
-- Data for Name: customer_wallet_record; Type: TABLE DATA; Schema: biz; Owner: postgres
--

COPY biz.customer_wallet_record (record_id, customer_id, amount, record_type, create_at, remark) FROM stdin;
\.


--
-- TOC entry 3447 (class 0 OID 16604)
-- Dependencies: 220
-- Data for Name: dept_info; Type: TABLE DATA; Schema: biz; Owner: postgres
--

COPY biz.dept_info (dept_id, tenant_id, parent_id, ancestors, dept_name, status, dept_sort, contact, email, creator_id, updater_id, create_at, update_at) FROM stdin;
\.


--
-- TOC entry 3448 (class 0 OID 16611)
-- Dependencies: 221
-- Data for Name: menu_info; Type: TABLE DATA; Schema: biz; Owner: postgres
--

COPY biz.menu_info (menu_id, menu_name, parent_id, menu_sort, path, component, query, menu_type, status, perms, icon, cacheable, visible, create_at, creator_id, update_at, updater_id) FROM stdin;
1906263415450002211	角色删除	1906263415450000206	4	#	\N	\N	B	0	prin-mod:role-mng:remove	#	f	t	2024-05-26 12:30:16+08	1	2025-09-03 11:46:49.431+08	1
1906263415450002210	角色修改	1906263415450000206	3	#	\N	\N	B	0	prin-mod:role-mng:edit	#	f	t	2024-05-26 12:30:16+08	1	2025-09-03 11:46:54.384+08	1
1906263415450002209	角色新增	1906263415450000206	2	#	\N	\N	B	0	prin-mod:role-mng:add	#	f	t	2024-05-26 12:30:16+08	1	2025-09-03 11:47:04.26+08	1
1906263415450002208	角色查询	1906263415450000206	1	#	\N	\N	B	0	prin-mod:role-mng:query	#	f	t	2024-05-26 12:30:16+08	1	2025-09-03 11:47:10.179+08	1
1906263415450002040	部门删除	1906263415450000205	4	#	\N	\N	B	0	prin-mod:dept-mng:remove	#	f	t	2024-05-26 12:30:16+08	1	2025-09-03 11:47:52.874+08	1
1906263415450002038	部门新增	1906263415450000205	2	#	\N	\N	B	0	prin-mod:dept-mng:add	#	f	t	2024-05-26 12:30:16+08	1	2025-09-03 11:48:05.392+08	1
1906263415450002037	部门查询	1906263415450000205	1	#	\N	\N	B	0	prin-mod:dept-mng:query	#	f	t	2024-05-26 12:30:16+08	1	2025-09-03 11:48:10.516+08	1
1906263415450000101	控制台入口	1963871785836048386	0	controller	\N	\N	D	0	\N	carbon:dashboard	f	t	2024-05-26 12:30:16+08	1	2025-09-05 15:53:53.291+08	1
1906263415450001131	租户详情	1906263415450000601	9	#	\N	\N	B	0	prin-mod:tenant-mng:query	#	f	t	2024-05-26 12:30:16+08	1	2025-09-03 11:49:47.592+08	1
1906263415450001132	租户新增	1906263415450000601	4	#	\N	\N	B	0	prin-mod:tenant-mng:add	#	f	t	2024-05-26 12:30:16+08	1	2025-09-03 11:50:27.453+08	1
1906263415450001134	租户删除	1906263415450000601	2	#	\N	\N	B	0	prin-mod:tenant-mng:remove	#	f	t	2024-05-26 12:30:16+08	1	2025-09-03 11:50:56.759+08	1
1906263415450001135	套餐详情	1906263415450000602	9	#	\N	\N	B	0	prin-mod:tenantpackage-mng:query	#	f	t	2024-05-26 12:30:16+08	1	2025-09-03 11:52:16.703+08	1
1906263415450001136	套餐新增	1906263415450000602	5	#	\N	\N	B	0	prin-mod:tenantpackage-mng:add	#	f	t	2024-05-26 12:30:16+08	1	2025-09-03 11:52:23.001+08	1
1906263415450001137	套餐修改	1906263415450000602	4	#	\N	\N	B	0	prin-mod:tenantpackage-mng:edit	#	f	t	2024-05-26 12:30:16+08	1	2025-09-03 11:52:28.986+08	1
1906263415450001138	套餐删除	1906263415450000602	2	#	\N	\N	B	0	prin-mod:tenantpackage-mng:remove	#	f	t	2024-05-26 12:30:16+08	1	2025-09-03 11:52:34.862+08	1
1906263415450002015	菜单修改	1906263415450000207	3	#	\N	\N	B	0	prin-mod:menu-mng:edit	#	f	t	2024-05-26 12:30:16+08	1	2025-09-03 12:37:43.042+08	1
1906263415450002014	菜单新增	1906263415450000207	2	#	\N	\N	B	0	prin-mod:menu-mng:add	#	f	t	2024-05-26 12:30:16+08	1	2025-09-03 12:37:48.557+08	1
1906263415450002013	菜单查询	1906263415450000207	1	#	\N	\N	B	0	prin-mod:menu-mng:query	#	f	t	2024-05-26 12:30:16+08	1	2025-09-03 12:37:54.592+08	1
1906263415450002005	用户导出	1906263415450000201	5	#	\N	\N	B	0	prin-mod:customer-mng:export	#	f	t	2024-05-26 12:30:16+08	1	2025-09-03 12:38:50.214+08	1
1906263415450002004	用户删除	1906263415450000201	4	#	\N	\N	B	0	prin-mod:customer-mng:remove	#	f	t	2024-05-26 12:30:16+08	1	2025-09-03 12:38:55.619+08	1
1906263415450002003	用户修改	1906263415450000201	3	#	\N	\N	B	0	prin-mod:customer-mng:edit	#	f	t	2024-05-26 12:30:16+08	1	2025-09-03 12:39:02.351+08	1
1906263415450002002	用户新增	1906263415450000201	2	#	\N	\N	B	0	prin-mod:customer-mng:add	#	f	t	2024-05-26 12:30:16+08	1	2025-09-03 12:39:08.752+08	1
1906263415450002001	用户查询	1906263415450000201	1	#	\N	\N	B	0	prin-mod:customer-mng:query	#	f	t	2024-05-26 12:30:16+08	1	2025-09-03 12:39:15.698+08	1
1906263415450001059	公告删除	1906263415450000100	4	#	\N	\N	B	0	system-mod:announcement-mng:remove	#	f	t	2024-05-26 12:30:16+08	1	2025-09-03 13:47:11.038+08	1
1906263415450001058	公告修改	1906263415450000100	3	#	\N	\N	B	0	system-mod:announcement-mng:edit	#	f	t	2024-05-26 12:30:16+08	1	2025-09-03 13:47:20.836+08	1
1906263415450001057	公告新增	1906263415450000100	2	#	\N	\N	B	0	system-mod:announcement-mng:add	#	f	t	2024-05-26 12:30:16+08	1	2025-09-03 13:47:26.705+08	1
1906263415450001056	公告查询	1906263415450000100	1	#	\N	\N	B	0	system-mod:announcement-mng:query	#	f	t	2024-05-26 12:30:16+08	1	2025-09-03 13:47:35.523+08	1
1906263415450001055	参数导出	1906263415450000103	5	#	\N	\N	B	0	system-mod:config-mng:export	#	f	t	2024-05-26 12:30:16+08	1	2025-09-03 13:48:22.018+08	1
1906263415450001054	参数删除	1906263415450000103	4	#	\N	\N	B	0	system-mod:config-mng:remove	#	f	t	2024-05-26 12:30:16+08	1	2025-09-03 13:48:27.486+08	1
1906263415450001215	修改密钥	1906263415450000700	5	#	\N	\N	B	0	prin-mod:oauth2client-mng:edit-secret	#	f	t	2024-05-26 12:30:16+08	1	2025-09-03 16:16:07.901+08	1
1906263415450001129	重置租户操作密码	1906263415450000601	11	#	\N	\N	B	0	prin-mod:tenant-mng:reset-operpwd	#	f	t	2024-05-26 12:30:16+08	1	2025-09-03 16:17:23.785+08	1
1906263415450000001	系统管理	0	100	system-mng	\N	\N	D	0	\N	carbon:z-systems	f	t	2024-05-26 12:30:16+08	1	2025-09-05 15:29:24.914+08	1
1906263415450000300	定时任务	1906263415450000101	20	http://localhost:9200/xxl-job-admin	\N		O	0		carbon:link	f	t	2024-05-26 12:30:16+08	1	2025-09-05 16:16:21.958+08	1
1906263415450001053	参数修改	1906263415450000103	3	#	\N	\N	B	0	system-mod:config-mng:edit	#	f	t	2024-05-26 12:30:16+08	1	2025-09-03 13:48:33.283+08	1
1906263415450001051	参数查询	1906263415450000103	1	#	\N	\N	B	0	system-mod:config-mng:query	#	f	t	2024-05-26 12:30:16+08	1	2025-09-03 13:48:44.872+08	1
1906263415450001049	字典删除	1906263415450000102	4	#	\N	\N	B	0	system-mod:dictionary-mng:remove	#	f	t	2024-05-26 12:30:16+08	1	2025-09-03 13:49:54.585+08	1
1906263415450001048	字典修改	1906263415450000102	3	#	\N	\N	B	0	system-mod:dictionary-mng:edit	#	f	t	2024-05-26 12:30:16+08	1	2025-09-03 13:50:03.127+08	1
1906263415450001047	字典新增	1906263415450000102	2	#	\N	\N	B	0	system-mod:dictionary-mng:add	#	f	t	2024-05-26 12:30:16+08	1	2025-09-03 13:50:09.283+08	1
1906263415450001046	字典查询	1906263415450000102	1	#	\N	\N	B	0	system-mod:dictionary-mng:query	#	f	t	2024-05-26 12:30:16+08	1	2025-09-03 13:50:16.494+08	1
1906263415450001064	删除记录	1906263415450000151	2	#	\N	\N	B	0	system-mod:loginlog-mng:remove	#	f	t	2024-05-26 12:30:16+08	1	2025-09-03 13:51:16.57+08	1
1906263415450001062	操作日志敏感字段	1906263415450000150	10	#	\N	\N	B	0	system-mod:operatelog-mng:field	#	f	t	2024-05-26 12:30:16+08	1	2025-09-03 13:51:47.406+08	1
1906263415450001214	终端导出	1906263415450000700	4	#	\N	\N	B	0	prin-mod:oauth2client-mng:export	#	f	t	2024-05-26 12:30:16+08	1	2025-09-03 12:39:58.868+08	1
1906263415450001212	终端添加	1906263415450000700	3	#	\N	\N	B	0	prin-mod:oauth2client-mng:add	#	f	t	2024-05-26 12:30:16+08	1	2025-09-03 12:40:06.215+08	1
1906263415450001213	终端删除	1906263415450000700	2	#	\N	\N	B	0	prin-mod:oauth2client-mng:remove	#	f	t	2024-05-26 12:30:16+08	1	2025-09-03 12:40:23.623+08	1
1906263415450001211	终端修改	1906263415450000700	2	#	\N	\N	B	0	prin-mod:oauth2client-mng:edit	#	f	t	2024-05-26 12:30:16+08	1	2025-09-03 12:40:37.962+08	1
1906263415450000304	服务监控	1906263415450000101	5	http://localhost:9100/	\N	\N	O	0		carbon:link	f	t	2024-05-26 12:30:16+08	1	2025-09-03 13:53:37.79+08	1
1906263415450002078	用户删除	1906263415450000203	8	#	\N	\N	B	0	prin-mod:user-mng:remove	#	f	t	2024-05-26 12:30:16+08	1	2025-09-03 15:32:01.169+08	1
1906263415450002062	重置密码	1906263415450000203	7	#	\N	\N	B	0	prin-mod:user-mng:resetPwd	#	f	t	2024-05-26 12:30:16+08	1	2025-09-03 15:31:48.181+08	1
1906263415450002207	权限授予	1906263415450000206	6	#	\N	\N	B	0	prin-mod:role-mng:grant-user	#	f	t	2024-05-26 12:30:16+08	1	2025-09-03 15:40:20.269+08	1
1906263415450000700	终端管理	1906263415450000003	3	oauth2client	platform-mng/oauth2client/index	\N	M	0	prin-mod:oauth2client-mng:list	carbon:application	f	t	2024-05-26 12:30:16+08	1	2025-09-03 17:29:48.237+08	1
1910569625749024770	授权角色	1906263415450000203	0	#	\N	\N	B	0	prin-mod:user-mng:grant-role	#	f	t	2025-04-11 13:44:30.104+08	1	2025-09-03 15:40:54.248+08	1
1906263415450002016	菜单删除	1906263415450000207	4	#	\N	\N	B	0	prin-mod:menu-mng:remove	#	f	t	2024-05-26 12:30:16+08	1	2025-09-03 12:37:37.428+08	1
1906263415450000201	顾客管理	1906263415450000003	1	customer	platform-mng/customer/index	\N	M	0	prin-mod:customer-mng:list	carbon:customer	f	t	2024-05-26 12:30:16+08	1	2025-09-03 17:29:53.092+08	1
1906263415450002039	部门修改	1906263415450000205	3	#	\N	\N	B	0	prin-mod:dept-mng:edit	#	f	t	2024-05-26 12:30:16+08	1	2025-09-03 11:47:58.758+08	1
1906263415450000205	部门管理	1906263415450000002	70	dept	user-mng/dept/index	\N	M	0	prin-mod:dept-mng:list	carbon:departure	f	t	2024-05-26 12:30:16+08	1	2025-09-03 17:33:50.403+08	1
1906263415450000100	公告管理	1906263415450000001	15	announcement	system-mng/announcement/index	\N	M	0	system-mod:announcement-mng:list	carbon:message-queue	f	t	2024-05-26 12:30:16+08	1	2025-09-03 17:38:04.6+08	1
1906263415450000102	字典管理	1963871785836048386	6	dictionary	develop/dictionary/index	\N	M	0	system-mod:dictionary-mng:list	carbon:text-vertical-alignment	f	t	2024-05-26 12:30:16+08	1	2025-09-05 16:04:34.878+08	1
1906263415450001133	租户修改	1906263415450000601	2	#	\N	\N	B	0	prin-mod:tenant-mng:edit	#	f	t	2024-05-26 12:30:16+08	1	2025-09-03 11:51:04.394+08	1
1906263415450001052	参数新增	1906263415450000103	2	#	\N	\N	B	0	system-mod:config-mng:add	#	f	t	2024-05-26 12:30:16+08	1	2025-09-03 13:48:39.112+08	1
1906263415450000302	Sentinel控制台	1906263415450000101	3	http://localhost:8718	\N	\N	O	0		carbon:link	f	t	2024-05-26 12:30:16+08	1	2025-09-03 13:54:06.192+08	1
1906263415450000301	系统接口	1906263415450000101	2	http://localhost:8080/doc.html	\N	\N	I	0		carbon:link	f	t	2024-05-26 12:30:16+08	1	2025-09-03 13:54:14.675+08	1
1906263415450001061	操作删除	1906263415450000150	2	#	\N	\N	B	0	system-mod:operatelog-mng:remove	#	f	t	2024-05-26 12:30:16+08	1	2025-09-03 13:52:02.66+08	1
1906263415450000207	菜单管理	1906263415450000003	50	menu	platform-mng/menu/index		M	0	prin-mod:menu-mng:list	carbon:menu	f	t	2024-05-26 12:30:16+08	1	2025-09-03 13:36:05.116+08	1
1906272182215585793	租户信息	0	100	tenant-info	tenant-info/index	\N	M	0	prin-mod:tenant-info	carbon:information-filled	f	t	2025-03-30 17:07:59.723+08	1	2025-09-04 16:02:05.63+08	1
1906263415450000303	Nacos控制台	1906263415450000101	4	http://localhost:8848/nacos	\N		O	0		carbon:link	f	t	2024-05-26 12:30:16+08	1	2025-09-05 16:16:29.014+08	1
1963871785836048386	开发管理	0	0	develop	\N	\N	D	0	\N	carbon:tool-kit	f	t	2025-09-05 15:48:15.373+08	1	2025-09-05 15:48:15.373+08	1
1915322746249367554	修改信息	1906272182215585793	0	#	\N	\N	B	0	prin-mod:tenant-info:edit	#	f	t	2025-04-24 16:31:42.342+08	1	2025-09-03 15:57:01.24+08	1
1906263415450002064	用户修改	1906263415450000203	3	#	\N	\N	B	0	prin-mod:user-mng:edit	#	f	t	2024-05-26 12:30:16+08	1	2025-09-03 11:45:30.193+08	1
1906263415450001127	商户钱包	1906272182215585793	0	#	\N	\N	M	0	prin-mod:tenant-wallet-info	carbon:wallet	f	t	2024-05-26 12:30:16+08	1	2025-09-03 15:58:25.94+08	1
1906263415450001126	商户提现	1906263415450001127	1	#	\N	\N	B	0	prin-mod:tenant-wallet-info:withdrawal	#	f	t	2024-05-26 12:30:16+08	1	2025-09-03 15:58:39.276+08	1
1906263415450000104	日志管理	1906263415450000001	1	user-log	\N	\N	D	0	\N	carbon:ibm-knowledge-catalog-premium	f	t	2024-05-26 12:30:16+08	1	2025-09-03 13:57:09.619+08	1
1906263415450001210	终端详情	1906263415450000700	1	#	\N	\N	B	0	prin-mod:oauth2client-mng:query	#	f	t	2024-05-26 12:30:16+08	1	2025-09-03 12:42:06.554+08	1
1906263415450000151	登录日志	1906263415450000104	2	login	system-mng/user-log/login/index	\N	M	0	system-mod:loginlog-mng:list	carbon:login	f	t	2024-05-26 12:30:16+08	1	2025-09-03 17:38:55.27+08	1
1906263415450002077	用户导出	1906263415450000203	1	#	\N	\N	B	0	prin-mod:user-mng:export	#	f	t	2024-05-26 12:30:16+08	1	2025-09-03 11:45:37.592+08	1
1906263415450002072	用户添加	1906263415450000203	1	#	\N	\N	B	0	prin-mod:user-mng:add	#	f	t	2024-05-26 12:30:16+08	1	2025-09-03 11:45:46.262+08	1
1906263415450002071	用户查询	1906263415450000203	0	#	\N	\N	B	0	prin-mod:user-mng:query	#	f	t	2024-05-26 12:30:16+08	1	2025-09-03 11:44:36.097+08	1
1906263415450000103	配置管理	1963871785836048386	7	config	develop/config/index	\N	M	0	system-mod:config-mng:list	carbon:parameter	f	t	2024-05-26 12:30:16+08	1	2025-09-05 16:04:26.005+08	1
1906263415450000203	员工管理	1906263415450000002	100	user	user-mng/user/index		M	0	prin-mod:user-mng:list	carbon:user-admin	t	t	2024-05-26 12:30:16+08	1	2025-09-03 17:33:41.092+08	1
1906263415450000002	用户管理	0	50	user-mng	\N	\N	D	0	\N	carbon:user	f	t	2024-05-26 12:30:16+08	1	2025-09-03 17:28:58.268+08	1
1906263415450000003	平台管理	0	80	platform-mng	\N	\N	D	0	\N	carbon:platforms	f	t	2024-05-26 12:30:16+08	1	2025-09-03 17:29:11.126+08	1
1906263415450000601	租户管理	1906263415450000003	100	tenant	platform-mng/tenant/index	\N	M	0	prin-mod:tenant-mng:list	carbon:id-management	f	t	2024-05-26 12:30:16+08	1	2025-09-03 17:29:30.704+08	1
1906263415450000150	操作日志	1906263415450000104	1	operate	system-mng/user-log/operate/index	\N	M	0	system-mod:operatelog-mng:list	carbon:touch-interaction	f	t	2024-05-26 12:30:16+08	1	2025-09-03 17:39:02.401+08	1
1906263415450001125	钱包记录	1906263415450000601	3	#	\N	\N	B	0	prin-mod:tenant-wallet-mng:list	#	f	t	2024-05-26 12:30:16+08	1	2025-09-03 16:17:56.038+08	1
1906263415450000602	租户套餐管理	1906263415450000003	50	tenant-package	platform-mng/tenant-package/index	\N	M	0	prin-mod:tenantpackage-mng:list	carbon:package	f	t	2024-05-26 12:30:16+08	1	2025-09-03 17:29:37.386+08	1
1906263415450000206	角色管理	1906263415450000002	99	role	user-mng/role/index	\N	M	0	prin-mod:role-mng:list	carbon:user-role	f	t	2024-05-26 12:30:16+08	1	2025-09-03 17:33:45.353+08	1
\.


--
-- TOC entry 3446 (class 0 OID 16596)
-- Dependencies: 219
-- Data for Name: oauth2_client; Type: TABLE DATA; Schema: biz; Owner: postgres
--

COPY biz.oauth2_client (id, client_name, client_id, client_secret, scopes, authorization_grant_types, redirect_uris, status, auto_approve, create_at, creator_id, update_at, updater_id) FROM stdin;
2	服务监控	server_monitor	{bcrypt}$2a$10$hK9Sv9kAvXE00fWtkWxzI.Ns4.5SuQteTJAnsFWXChlOWIUZSFYL2	{monitor}	{client_credentials}	{}	0	t	2025-05-21 14:13:48.523+08	1	2025-09-01 15:53:53.377+08	1
1	系统	server	{bcrypt}$2a$10$hK9Sv9kAvXE00fWtkWxzI.Ns4.5SuQteTJAnsFWXChlOWIUZSFYL2	{openid,read}	{authorization_code,urn:ietf:params:oauth:grant-type:device_code,refresh_token}	{http://localhost:9000/oauth2/authorization_code_callback}	0	f	2024-04-17 14:08:54+08	1	2025-09-03 11:28:18.451+08	1
\.


--
-- TOC entry 3449 (class 0 OID 16616)
-- Dependencies: 222
-- Data for Name: role_info; Type: TABLE DATA; Schema: biz; Owner: root
--

COPY biz.role_info (role_id, tenant_id, data_scope, role_name, status, inherited, role_sort, create_at, creator_id, update_at, updater_id) FROM stdin;
\.


--
-- TOC entry 3450 (class 0 OID 16620)
-- Dependencies: 223
-- Data for Name: role_to_dept; Type: TABLE DATA; Schema: biz; Owner: postgres
--

COPY biz.role_to_dept (role_id, dept_id) FROM stdin;
\.


--
-- TOC entry 3451 (class 0 OID 16623)
-- Dependencies: 224
-- Data for Name: role_to_hierarchy; Type: TABLE DATA; Schema: biz; Owner: root
--

COPY biz.role_to_hierarchy (role_id, inherited_id) FROM stdin;
\.


--
-- TOC entry 3452 (class 0 OID 16626)
-- Dependencies: 225
-- Data for Name: role_to_menu; Type: TABLE DATA; Schema: biz; Owner: postgres
--

COPY biz.role_to_menu (role_id, menu_id) FROM stdin;
\.


--
-- TOC entry 3453 (class 0 OID 16629)
-- Dependencies: 226
-- Data for Name: tenant_info; Type: TABLE DATA; Schema: biz; Owner: postgres
--

COPY biz.tenant_info (tenant_id, administrator, tenant_type, contact_phone, tenant_name, oper_pwd, status, domain, remark, package_id, expire_time, account_limit, role_limit, dept_limit, creator_id, create_at, updater_id, update_at) FROM stdin;
1	1	1	13665656563	supertenant	{bcrypt}$2a$10$1UJgROjrOvMKJD4way7dKeBsJuLGVLWGy/pBGooa.sFqfsP3Vrupm	0	\N	\N	\N	2099-12-31 00:00:00+08	-1	-1	-1	1	2025-04-22 13:47:13+08	1	2025-04-27 16:10:30.389+08
1910557183820165122	1910557183820165120	0	\N	test1	{bcrypt}$2a$10$1UJgROjrOvMKJD4way7dKeBsJuLGVLWGy/pBGooa.sFqfsP3Vrupm	0	\N	\N	1773625804122202113	2025-09-30 00:00:00+08	5	5	5	1	2025-04-11 12:55:03.715+08	1	2025-09-17 13:55:48.224+08
\.


--
-- TOC entry 3454 (class 0 OID 16632)
-- Dependencies: 227
-- Data for Name: tenant_package_info; Type: TABLE DATA; Schema: biz; Owner: postgres
--

COPY biz.tenant_package_info (package_id, package_name, status, menu_ids, remark, creator_id, create_at, updater_id, update_at) FROM stdin;
1773625804122202113	默认套餐	0	{1906272182215585793,1915322746249367554,1906263415450001127,1906263415450001126,1906263415450000001,1906263415450000100,1906263415450001059,1906263415450001058,1906263415450001057,1906263415450001056,1906263415450000104,1906263415450000151,1906263415450001064,1906263415450000150,1906263415450001061,1906263415450000002,1906263415450000203,1906263415450002078,1906263415450002062,1906263415450002064,1906263415450002077,1906263415450002072,1910569625749024770,1906263415450002071,1906263415450000206,1906263415450002207,1906263415450002211,1906263415450002210,1906263415450002209,1906263415450002208,1906263415450000205,1906263415450002040,1906263415450002039,1906263415450002038,1906263415450002037}	通用租户套餐	1	2024-04-17 14:08:54+08	1	2025-09-08 09:41:49.754+08
\.


--
-- TOC entry 3455 (class 0 OID 16638)
-- Dependencies: 228
-- Data for Name: tenant_wallet_info; Type: TABLE DATA; Schema: biz; Owner: postgres
--

COPY biz.tenant_wallet_info (tenant_id, balance, status) FROM stdin;
1910557183820165122	0.00	0
1	0.00	0
\.


--
-- TOC entry 3456 (class 0 OID 16641)
-- Dependencies: 229
-- Data for Name: tenant_wallet_record; Type: TABLE DATA; Schema: biz; Owner: postgres
--

COPY biz.tenant_wallet_record (record_id, tenant_id, amount, record_type, biz_type, biz_no, create_at, remark) FROM stdin;
\.


--
-- TOC entry 3459 (class 0 OID 16654)
-- Dependencies: 232
-- Data for Name: tenant_wallet_withdrawal_record; Type: TABLE DATA; Schema: biz; Owner: postgres
--

COPY biz.tenant_wallet_withdrawal_record (withdrawal_id, order_no, status, tenant_id, request_params, amount, error_msg, creator_id, create_at, complete_at, remark) FROM stdin;
\.


--
-- TOC entry 3457 (class 0 OID 16644)
-- Dependencies: 230
-- Data for Name: user_info; Type: TABLE DATA; Schema: biz; Owner: postgres
--

COPY biz.user_info (user_id, tenant_id, dept_id, username, email, nickname, phone_number, status, gender, avatar, password, login_ip, login_date, remark, creator_id, updater_id, create_at, update_at) FROM stdin;
1	1	\N	admin	\N	nick_a	15888888888	0	1	https://img-s-msn-com.akamaized.net/tenant/amp/entityid/AA1B91c8.img?w=660&h=648&m=6&x=219&y=147&s=204&d=204	{bcrypt}$2a$10$7JB720yubVSZvUI0rEqK/.VqGOZTH.ulu33dHOiBE8ByOhJIrdAu2	172.16.8.76	2025-09-09 13:35:04+08	\N	1	\N	2024-04-17 14:08:54.616+08	\N
1910557183820165120	1910557183820165122	\N	testadmin	\N	\N	\N	0	2	\N	{bcrypt}$2a$10$7JB720yubVSZvUI0rEqK/.VqGOZTH.ulu33dHOiBE8ByOhJIrdAu2	172.16.8.76	2025-09-17 13:55:55+08	\N	1	\N	2025-04-11 12:55:03.816+08	\N
1968228585288978433	1	\N	111111	\N	\N	\N	0	2	\N	{bcrypt}$2a$10$ZFJAnKSfvfYWuktcoJ/.6ueVPe825x3wnjtdYJCvSCL7SdmL0z8em	172.16.8.76	2025-09-17 16:21:03+08	\N	1	\N	2025-09-17 16:20:37.308+08	\N
\.


--
-- TOC entry 3458 (class 0 OID 16651)
-- Dependencies: 231
-- Data for Name: user_to_role; Type: TABLE DATA; Schema: biz; Owner: postgres
--

COPY biz.user_to_role (user_id, role_id) FROM stdin;
\.


--
-- TOC entry 3253 (class 2606 OID 16713)
-- Name: customer_info customer_info_pkey; Type: CONSTRAINT; Schema: biz; Owner: postgres
--

ALTER TABLE ONLY biz.customer_info
    ADD CONSTRAINT customer_info_pkey PRIMARY KEY (customer_id);


--
-- TOC entry 3256 (class 2606 OID 16720)
-- Name: customer_social_info customer_social_info_pkey; Type: CONSTRAINT; Schema: biz; Owner: postgres
--

ALTER TABLE ONLY biz.customer_social_info
    ADD CONSTRAINT customer_social_info_pkey PRIMARY KEY (customer_id);


--
-- TOC entry 3259 (class 2606 OID 16718)
-- Name: customer_wallet_info customer_wallet_info_pkey; Type: CONSTRAINT; Schema: biz; Owner: postgres
--

ALTER TABLE ONLY biz.customer_wallet_info
    ADD CONSTRAINT customer_wallet_info_pkey PRIMARY KEY (customer_id);


--
-- TOC entry 3261 (class 2606 OID 16666)
-- Name: customer_wallet_record customer_wallet_record_pkey; Type: CONSTRAINT; Schema: biz; Owner: postgres
--

ALTER TABLE ONLY biz.customer_wallet_record
    ADD CONSTRAINT customer_wallet_record_pkey PRIMARY KEY (record_id);


--
-- TOC entry 3267 (class 2606 OID 16711)
-- Name: dept_info dept_info_pkey; Type: CONSTRAINT; Schema: biz; Owner: postgres
--

ALTER TABLE ONLY biz.dept_info
    ADD CONSTRAINT dept_info_pkey PRIMARY KEY (dept_id);


--
-- TOC entry 3272 (class 2606 OID 16672)
-- Name: menu_info menu_info_pkey; Type: CONSTRAINT; Schema: biz; Owner: postgres
--

ALTER TABLE ONLY biz.menu_info
    ADD CONSTRAINT menu_info_pkey PRIMARY KEY (menu_id);


--
-- TOC entry 3264 (class 2606 OID 16668)
-- Name: oauth2_client oauth2_client_pkey; Type: CONSTRAINT; Schema: biz; Owner: postgres
--

ALTER TABLE ONLY biz.oauth2_client
    ADD CONSTRAINT oauth2_client_pkey PRIMARY KEY (id);


--
-- TOC entry 3274 (class 2606 OID 16680)
-- Name: role_info role_info_pkey; Type: CONSTRAINT; Schema: biz; Owner: root
--

ALTER TABLE ONLY biz.role_info
    ADD CONSTRAINT role_info_pkey PRIMARY KEY (role_id);


--
-- TOC entry 3276 (class 2606 OID 16674)
-- Name: role_to_dept role_to_dept_pkey; Type: CONSTRAINT; Schema: biz; Owner: postgres
--

ALTER TABLE ONLY biz.role_to_dept
    ADD CONSTRAINT role_to_dept_pkey PRIMARY KEY (role_id, dept_id);


--
-- TOC entry 3278 (class 2606 OID 16676)
-- Name: role_to_hierarchy role_to_hierarchy_pkey; Type: CONSTRAINT; Schema: biz; Owner: root
--

ALTER TABLE ONLY biz.role_to_hierarchy
    ADD CONSTRAINT role_to_hierarchy_pkey PRIMARY KEY (role_id, inherited_id);


--
-- TOC entry 3280 (class 2606 OID 16678)
-- Name: role_to_menu role_to_menu_pkey; Type: CONSTRAINT; Schema: biz; Owner: postgres
--

ALTER TABLE ONLY biz.role_to_menu
    ADD CONSTRAINT role_to_menu_pkey PRIMARY KEY (role_id, menu_id);


--
-- TOC entry 3282 (class 2606 OID 16684)
-- Name: tenant_info tenant_info_pkey; Type: CONSTRAINT; Schema: biz; Owner: postgres
--

ALTER TABLE ONLY biz.tenant_info
    ADD CONSTRAINT tenant_info_pkey PRIMARY KEY (tenant_id);


--
-- TOC entry 3285 (class 2606 OID 16682)
-- Name: tenant_package_info tenant_package_info_pkey; Type: CONSTRAINT; Schema: biz; Owner: postgres
--

ALTER TABLE ONLY biz.tenant_package_info
    ADD CONSTRAINT tenant_package_info_pkey PRIMARY KEY (package_id);


--
-- TOC entry 3287 (class 2606 OID 16686)
-- Name: tenant_wallet_info tenant_wallet_info_pkey; Type: CONSTRAINT; Schema: biz; Owner: postgres
--

ALTER TABLE ONLY biz.tenant_wallet_info
    ADD CONSTRAINT tenant_wallet_info_pkey PRIMARY KEY (tenant_id);


--
-- TOC entry 3290 (class 2606 OID 16688)
-- Name: tenant_wallet_record tenant_wallet_record_pkey; Type: CONSTRAINT; Schema: biz; Owner: postgres
--

ALTER TABLE ONLY biz.tenant_wallet_record
    ADD CONSTRAINT tenant_wallet_record_pkey PRIMARY KEY (record_id);


--
-- TOC entry 3299 (class 2606 OID 16694)
-- Name: tenant_wallet_withdrawal_record tenant_wallet_withdrawal_record_pkey; Type: CONSTRAINT; Schema: biz; Owner: postgres
--

ALTER TABLE ONLY biz.tenant_wallet_withdrawal_record
    ADD CONSTRAINT tenant_wallet_withdrawal_record_pkey PRIMARY KEY (withdrawal_id);


--
-- TOC entry 3294 (class 2606 OID 16690)
-- Name: user_info user_info_pkey; Type: CONSTRAINT; Schema: biz; Owner: postgres
--

ALTER TABLE ONLY biz.user_info
    ADD CONSTRAINT user_info_pkey PRIMARY KEY (user_id);


--
-- TOC entry 3296 (class 2606 OID 16692)
-- Name: user_to_role user_to_role_pkey; Type: CONSTRAINT; Schema: biz; Owner: postgres
--

ALTER TABLE ONLY biz.user_to_role
    ADD CONSTRAINT user_to_role_pkey PRIMARY KEY (user_id, role_id);


--
-- TOC entry 3254 (class 1259 OID 16702)
-- Name: idx_customer_info_phone_number; Type: INDEX; Schema: biz; Owner: postgres
--

CREATE UNIQUE INDEX idx_customer_info_phone_number ON biz.customer_info USING btree (phone_number);


--
-- TOC entry 3262 (class 1259 OID 16695)
-- Name: idx_customer_wallet_record_customer_id; Type: INDEX; Schema: biz; Owner: postgres
--

CREATE INDEX idx_customer_wallet_record_customer_id ON biz.customer_wallet_record USING btree (customer_id);


--
-- TOC entry 3268 (class 1259 OID 16696)
-- Name: idx_dept_info_ancestors; Type: INDEX; Schema: biz; Owner: postgres
--

CREATE INDEX idx_dept_info_ancestors ON biz.dept_info USING btree (ancestors);


--
-- TOC entry 3269 (class 1259 OID 16697)
-- Name: idx_dept_info_parent_id; Type: INDEX; Schema: biz; Owner: postgres
--

CREATE INDEX idx_dept_info_parent_id ON biz.dept_info USING btree (parent_id);


--
-- TOC entry 3270 (class 1259 OID 16698)
-- Name: idx_menu_info_parent_id; Type: INDEX; Schema: biz; Owner: postgres
--

CREATE INDEX idx_menu_info_parent_id ON biz.menu_info USING btree (parent_id);


--
-- TOC entry 3288 (class 1259 OID 16699)
-- Name: idx_tenant_wallet_record_tenant_id; Type: INDEX; Schema: biz; Owner: postgres
--

CREATE INDEX idx_tenant_wallet_record_tenant_id ON biz.tenant_wallet_record USING btree (tenant_id);


--
-- TOC entry 3297 (class 1259 OID 16700)
-- Name: idx_tenant_wallet_withdrawal_record_order_no; Type: INDEX; Schema: biz; Owner: postgres
--

CREATE UNIQUE INDEX idx_tenant_wallet_withdrawal_record_order_no ON biz.tenant_wallet_withdrawal_record USING btree (order_no);


--
-- TOC entry 3257 (class 1259 OID 16704)
-- Name: uk_customer_social_info_identifier; Type: INDEX; Schema: biz; Owner: postgres
--

CREATE UNIQUE INDEX uk_customer_social_info_identifier ON biz.customer_social_info USING btree (identifier);


--
-- TOC entry 3265 (class 1259 OID 16703)
-- Name: uk_oauth2_client_client_id; Type: INDEX; Schema: biz; Owner: postgres
--

CREATE UNIQUE INDEX uk_oauth2_client_client_id ON biz.oauth2_client USING btree (client_id);


--
-- TOC entry 3283 (class 1259 OID 16701)
-- Name: uk_tenant_info_administrator; Type: INDEX; Schema: biz; Owner: postgres
--

CREATE UNIQUE INDEX uk_tenant_info_administrator ON biz.tenant_info USING btree (administrator);


--
-- TOC entry 3291 (class 1259 OID 16705)
-- Name: uk_user_info_phone_number; Type: INDEX; Schema: biz; Owner: postgres
--

CREATE UNIQUE INDEX uk_user_info_phone_number ON biz.user_info USING btree (phone_number);


--
-- TOC entry 3292 (class 1259 OID 16706)
-- Name: uk_user_info_username; Type: INDEX; Schema: biz; Owner: postgres
--

CREATE UNIQUE INDEX uk_user_info_username ON biz.user_info USING btree (username);


-- Completed on 2025-09-23 10:07:12

--
-- PostgreSQL database dump complete
--

