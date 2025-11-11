--
-- PostgreSQL database dump
--

-- Dumped from database version 15.13
-- Dumped by pg_dump version 15.13

-- Started on 2025-11-11 18:06:55

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
-- TOC entry 6 (class 2615 OID 16843)
-- Name: biz; Type: SCHEMA; Schema: -; Owner: postgres
--

CREATE SCHEMA biz;


ALTER SCHEMA biz OWNER TO postgres;

--
-- TOC entry 3499 (class 0 OID 0)
-- Dependencies: 6
-- Name: SCHEMA biz; Type: COMMENT; Schema: -; Owner: postgres
--

COMMENT ON SCHEMA biz IS 'b端';


SET default_tablespace = '';

SET default_table_access_method = heap;

--
-- TOC entry 235 (class 1259 OID 16929)
-- Name: admin_info; Type: TABLE; Schema: biz; Owner: postgres
--

CREATE TABLE biz.admin_info (
    admin_id bigint NOT NULL,
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


ALTER TABLE biz.admin_info OWNER TO postgres;

--
-- TOC entry 3500 (class 0 OID 0)
-- Dependencies: 235
-- Name: TABLE admin_info; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON TABLE biz.admin_info IS '管理员表';


--
-- TOC entry 3501 (class 0 OID 0)
-- Dependencies: 235
-- Name: COLUMN admin_info.admin_id; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.admin_info.admin_id IS '管理员ID';


--
-- TOC entry 3502 (class 0 OID 0)
-- Dependencies: 235
-- Name: COLUMN admin_info.dept_id; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.admin_info.dept_id IS '部门ID';


--
-- TOC entry 3503 (class 0 OID 0)
-- Dependencies: 235
-- Name: COLUMN admin_info.username; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.admin_info.username IS '用户账号';


--
-- TOC entry 3504 (class 0 OID 0)
-- Dependencies: 235
-- Name: COLUMN admin_info.email; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.admin_info.email IS '用户邮箱';


--
-- TOC entry 3505 (class 0 OID 0)
-- Dependencies: 235
-- Name: COLUMN admin_info.nickname; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.admin_info.nickname IS '用户昵称';


--
-- TOC entry 3506 (class 0 OID 0)
-- Dependencies: 235
-- Name: COLUMN admin_info.phone_number; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.admin_info.phone_number IS '手机号码';


--
-- TOC entry 3507 (class 0 OID 0)
-- Dependencies: 235
-- Name: COLUMN admin_info.status; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.admin_info.status IS '状态值';


--
-- TOC entry 3508 (class 0 OID 0)
-- Dependencies: 235
-- Name: COLUMN admin_info.gender; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.admin_info.gender IS '用户性别（0男 1女 2未知）';


--
-- TOC entry 3509 (class 0 OID 0)
-- Dependencies: 235
-- Name: COLUMN admin_info.avatar; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.admin_info.avatar IS '头像地址';


--
-- TOC entry 3510 (class 0 OID 0)
-- Dependencies: 235
-- Name: COLUMN admin_info.password; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.admin_info.password IS '密码';


--
-- TOC entry 3511 (class 0 OID 0)
-- Dependencies: 235
-- Name: COLUMN admin_info.login_ip; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.admin_info.login_ip IS '登录ip';


--
-- TOC entry 3512 (class 0 OID 0)
-- Dependencies: 235
-- Name: COLUMN admin_info.login_date; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.admin_info.login_date IS '登录时间';


--
-- TOC entry 3513 (class 0 OID 0)
-- Dependencies: 235
-- Name: COLUMN admin_info.remark; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.admin_info.remark IS '备注';


--
-- TOC entry 3514 (class 0 OID 0)
-- Dependencies: 235
-- Name: COLUMN admin_info.creator_id; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.admin_info.creator_id IS '创建者';


--
-- TOC entry 3515 (class 0 OID 0)
-- Dependencies: 235
-- Name: COLUMN admin_info.updater_id; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.admin_info.updater_id IS '更新者';


--
-- TOC entry 236 (class 1259 OID 16936)
-- Name: admin_to_role; Type: TABLE; Schema: biz; Owner: postgres
--

CREATE TABLE biz.admin_to_role (
    admin_id bigint NOT NULL,
    role_id bigint NOT NULL
);


ALTER TABLE biz.admin_to_role OWNER TO postgres;

--
-- TOC entry 3516 (class 0 OID 0)
-- Dependencies: 236
-- Name: TABLE admin_to_role; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON TABLE biz.admin_to_role IS '管理员和角色关联表';


--
-- TOC entry 3517 (class 0 OID 0)
-- Dependencies: 236
-- Name: COLUMN admin_to_role.admin_id; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.admin_to_role.admin_id IS '管理员ID';


--
-- TOC entry 3518 (class 0 OID 0)
-- Dependencies: 236
-- Name: COLUMN admin_to_role.role_id; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.admin_to_role.role_id IS '角色ID';


--
-- TOC entry 215 (class 1259 OID 16846)
-- Name: customer_info; Type: TABLE; Schema: biz; Owner: postgres
--

CREATE TABLE biz.customer_info (
    customer_id bigint NOT NULL,
    nickname character varying(30),
    phone_number character varying(16),
    status character(1) DEFAULT 0 NOT NULL,
    gender character(1) DEFAULT 2 NOT NULL,
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
-- TOC entry 3519 (class 0 OID 0)
-- Dependencies: 215
-- Name: TABLE customer_info; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON TABLE biz.customer_info IS '用户信息表';


--
-- TOC entry 3520 (class 0 OID 0)
-- Dependencies: 215
-- Name: COLUMN customer_info.customer_id; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.customer_info.customer_id IS '用户ID';


--
-- TOC entry 3521 (class 0 OID 0)
-- Dependencies: 215
-- Name: COLUMN customer_info.nickname; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.customer_info.nickname IS '用户昵称';


--
-- TOC entry 3522 (class 0 OID 0)
-- Dependencies: 215
-- Name: COLUMN customer_info.phone_number; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.customer_info.phone_number IS '手机号码';


--
-- TOC entry 3523 (class 0 OID 0)
-- Dependencies: 215
-- Name: COLUMN customer_info.status; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.customer_info.status IS '状态值';


--
-- TOC entry 3524 (class 0 OID 0)
-- Dependencies: 215
-- Name: COLUMN customer_info.gender; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.customer_info.gender IS '用户性别（0男 1女 2未知）';


--
-- TOC entry 3525 (class 0 OID 0)
-- Dependencies: 215
-- Name: COLUMN customer_info.avatar; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.customer_info.avatar IS '头像地址';


--
-- TOC entry 3526 (class 0 OID 0)
-- Dependencies: 215
-- Name: COLUMN customer_info.login_ip; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.customer_info.login_ip IS '登录ip';


--
-- TOC entry 3527 (class 0 OID 0)
-- Dependencies: 215
-- Name: COLUMN customer_info.login_date; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.customer_info.login_date IS '登录时间';


--
-- TOC entry 3528 (class 0 OID 0)
-- Dependencies: 215
-- Name: COLUMN customer_info.creator_id; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.customer_info.creator_id IS '创建者';


--
-- TOC entry 3529 (class 0 OID 0)
-- Dependencies: 215
-- Name: COLUMN customer_info.updater_id; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.customer_info.updater_id IS '更新者';


--
-- TOC entry 216 (class 1259 OID 16851)
-- Name: customer_social_info; Type: TABLE; Schema: biz; Owner: postgres
--

CREATE TABLE biz.customer_social_info (
    customer_id bigint NOT NULL,
    identifier character varying(32) NOT NULL,
    identifier_type character varying(10) NOT NULL
);


ALTER TABLE biz.customer_social_info OWNER TO postgres;

--
-- TOC entry 3530 (class 0 OID 0)
-- Dependencies: 216
-- Name: TABLE customer_social_info; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON TABLE biz.customer_social_info IS '第三方信息';


--
-- TOC entry 3531 (class 0 OID 0)
-- Dependencies: 216
-- Name: COLUMN customer_social_info.identifier; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.customer_social_info.identifier IS '三方唯一标识符';


--
-- TOC entry 3532 (class 0 OID 0)
-- Dependencies: 216
-- Name: COLUMN customer_social_info.identifier_type; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.customer_social_info.identifier_type IS '三方渠道';


--
-- TOC entry 217 (class 1259 OID 16854)
-- Name: customer_wallet_info; Type: TABLE; Schema: biz; Owner: postgres
--

CREATE TABLE biz.customer_wallet_info (
    customer_id bigint NOT NULL,
    balance numeric(10,2) NOT NULL,
    status character(1) NOT NULL
);


ALTER TABLE biz.customer_wallet_info OWNER TO postgres;

--
-- TOC entry 3533 (class 0 OID 0)
-- Dependencies: 217
-- Name: TABLE customer_wallet_info; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON TABLE biz.customer_wallet_info IS '用户钱包';


--
-- TOC entry 3534 (class 0 OID 0)
-- Dependencies: 217
-- Name: COLUMN customer_wallet_info.balance; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.customer_wallet_info.balance IS '余额, 元';


--
-- TOC entry 3535 (class 0 OID 0)
-- Dependencies: 217
-- Name: COLUMN customer_wallet_info.status; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.customer_wallet_info.status IS '状态';


--
-- TOC entry 218 (class 1259 OID 16857)
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
-- TOC entry 3536 (class 0 OID 0)
-- Dependencies: 218
-- Name: TABLE customer_wallet_record; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON TABLE biz.customer_wallet_record IS '用户钱包记录';


--
-- TOC entry 3537 (class 0 OID 0)
-- Dependencies: 218
-- Name: COLUMN customer_wallet_record.customer_id; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.customer_wallet_record.customer_id IS '客户ID';


--
-- TOC entry 3538 (class 0 OID 0)
-- Dependencies: 218
-- Name: COLUMN customer_wallet_record.amount; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.customer_wallet_record.amount IS '金额, 元';


--
-- TOC entry 3539 (class 0 OID 0)
-- Dependencies: 218
-- Name: COLUMN customer_wallet_record.record_type; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.customer_wallet_record.record_type IS '记录类型';


--
-- TOC entry 3540 (class 0 OID 0)
-- Dependencies: 218
-- Name: COLUMN customer_wallet_record.create_at; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.customer_wallet_record.create_at IS '创建时间';


--
-- TOC entry 3541 (class 0 OID 0)
-- Dependencies: 218
-- Name: COLUMN customer_wallet_record.remark; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.customer_wallet_record.remark IS '备注';


--
-- TOC entry 219 (class 1259 OID 16860)
-- Name: dept_info; Type: TABLE; Schema: biz; Owner: postgres
--

CREATE TABLE biz.dept_info (
    dept_id bigint NOT NULL,
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
-- TOC entry 3542 (class 0 OID 0)
-- Dependencies: 219
-- Name: TABLE dept_info; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON TABLE biz.dept_info IS '部门表';


--
-- TOC entry 3543 (class 0 OID 0)
-- Dependencies: 219
-- Name: COLUMN dept_info.dept_id; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.dept_info.dept_id IS '部门id';


--
-- TOC entry 3544 (class 0 OID 0)
-- Dependencies: 219
-- Name: COLUMN dept_info.parent_id; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.dept_info.parent_id IS '父部门id';


--
-- TOC entry 3545 (class 0 OID 0)
-- Dependencies: 219
-- Name: COLUMN dept_info.ancestors; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.dept_info.ancestors IS '祖级列表';


--
-- TOC entry 3546 (class 0 OID 0)
-- Dependencies: 219
-- Name: COLUMN dept_info.dept_name; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.dept_info.dept_name IS '部门名称';


--
-- TOC entry 3547 (class 0 OID 0)
-- Dependencies: 219
-- Name: COLUMN dept_info.status; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.dept_info.status IS '0代表正常 1代表停用';


--
-- TOC entry 3548 (class 0 OID 0)
-- Dependencies: 219
-- Name: COLUMN dept_info.dept_sort; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.dept_info.dept_sort IS '显示顺序';


--
-- TOC entry 3549 (class 0 OID 0)
-- Dependencies: 219
-- Name: COLUMN dept_info.contact; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.dept_info.contact IS '联系电话';


--
-- TOC entry 3550 (class 0 OID 0)
-- Dependencies: 219
-- Name: COLUMN dept_info.email; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.dept_info.email IS '邮箱';


--
-- TOC entry 3551 (class 0 OID 0)
-- Dependencies: 219
-- Name: COLUMN dept_info.creator_id; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.dept_info.creator_id IS '创建者';


--
-- TOC entry 3552 (class 0 OID 0)
-- Dependencies: 219
-- Name: COLUMN dept_info.updater_id; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.dept_info.updater_id IS '更新者';


--
-- TOC entry 220 (class 1259 OID 16867)
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
    scope character varying(10) NOT NULL,
    creator_id bigint,
    updater_id bigint,
    create_at timestamp with time zone,
    update_at timestamp with time zone
);


ALTER TABLE biz.menu_info OWNER TO postgres;

--
-- TOC entry 3553 (class 0 OID 0)
-- Dependencies: 220
-- Name: TABLE menu_info; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON TABLE biz.menu_info IS '菜单权限表';


--
-- TOC entry 3554 (class 0 OID 0)
-- Dependencies: 220
-- Name: COLUMN menu_info.menu_id; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.menu_info.menu_id IS '菜单ID';


--
-- TOC entry 3555 (class 0 OID 0)
-- Dependencies: 220
-- Name: COLUMN menu_info.menu_name; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.menu_info.menu_name IS '菜单名称';


--
-- TOC entry 3556 (class 0 OID 0)
-- Dependencies: 220
-- Name: COLUMN menu_info.parent_id; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.menu_info.parent_id IS '父菜单ID';


--
-- TOC entry 3557 (class 0 OID 0)
-- Dependencies: 220
-- Name: COLUMN menu_info.menu_sort; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.menu_info.menu_sort IS '显示顺序';


--
-- TOC entry 3558 (class 0 OID 0)
-- Dependencies: 220
-- Name: COLUMN menu_info.path; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.menu_info.path IS '路由地址';


--
-- TOC entry 3559 (class 0 OID 0)
-- Dependencies: 220
-- Name: COLUMN menu_info.component; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.menu_info.component IS '组件路径';


--
-- TOC entry 3560 (class 0 OID 0)
-- Dependencies: 220
-- Name: COLUMN menu_info.query; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.menu_info.query IS '路由参数';


--
-- TOC entry 3561 (class 0 OID 0)
-- Dependencies: 220
-- Name: COLUMN menu_info.menu_type; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.menu_info.menu_type IS '菜单类型（D目录 M菜单 B按钮 I内链 O外链）';


--
-- TOC entry 3562 (class 0 OID 0)
-- Dependencies: 220
-- Name: COLUMN menu_info.status; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.menu_info.status IS '菜单状态（0正常 1停用）';


--
-- TOC entry 3563 (class 0 OID 0)
-- Dependencies: 220
-- Name: COLUMN menu_info.perms; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.menu_info.perms IS '权限标识';


--
-- TOC entry 3564 (class 0 OID 0)
-- Dependencies: 220
-- Name: COLUMN menu_info.icon; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.menu_info.icon IS '菜单图标';


--
-- TOC entry 3565 (class 0 OID 0)
-- Dependencies: 220
-- Name: COLUMN menu_info.cacheable; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.menu_info.cacheable IS '是否缓存';


--
-- TOC entry 3566 (class 0 OID 0)
-- Dependencies: 220
-- Name: COLUMN menu_info.visible; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.menu_info.visible IS '是否显示';


--
-- TOC entry 3567 (class 0 OID 0)
-- Dependencies: 220
-- Name: COLUMN menu_info.scope; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.menu_info.scope IS '菜单域';


--
-- TOC entry 3568 (class 0 OID 0)
-- Dependencies: 220
-- Name: COLUMN menu_info.creator_id; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.menu_info.creator_id IS '创建者ID';


--
-- TOC entry 221 (class 1259 OID 16872)
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
-- TOC entry 3569 (class 0 OID 0)
-- Dependencies: 221
-- Name: TABLE oauth2_client; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON TABLE biz.oauth2_client IS 'OAUTH2客户端';


--
-- TOC entry 3570 (class 0 OID 0)
-- Dependencies: 221
-- Name: COLUMN oauth2_client.client_name; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.oauth2_client.client_name IS '客户端名称';


--
-- TOC entry 3571 (class 0 OID 0)
-- Dependencies: 221
-- Name: COLUMN oauth2_client.client_id; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.oauth2_client.client_id IS 'APP_ID';


--
-- TOC entry 3572 (class 0 OID 0)
-- Dependencies: 221
-- Name: COLUMN oauth2_client.client_secret; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.oauth2_client.client_secret IS 'APP密钥';


--
-- TOC entry 3573 (class 0 OID 0)
-- Dependencies: 221
-- Name: COLUMN oauth2_client.scopes; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.oauth2_client.scopes IS '权限域';


--
-- TOC entry 3574 (class 0 OID 0)
-- Dependencies: 221
-- Name: COLUMN oauth2_client.authorization_grant_types; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.oauth2_client.authorization_grant_types IS '授权类型';


--
-- TOC entry 3575 (class 0 OID 0)
-- Dependencies: 221
-- Name: COLUMN oauth2_client.redirect_uris; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.oauth2_client.redirect_uris IS '回调地址';


--
-- TOC entry 3576 (class 0 OID 0)
-- Dependencies: 221
-- Name: COLUMN oauth2_client.status; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.oauth2_client.status IS '客户端状态';


--
-- TOC entry 3577 (class 0 OID 0)
-- Dependencies: 221
-- Name: COLUMN oauth2_client.auto_approve; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.oauth2_client.auto_approve IS '是否自动放行';


--
-- TOC entry 222 (class 1259 OID 16880)
-- Name: role_info; Type: TABLE; Schema: biz; Owner: root
--

CREATE TABLE biz.role_info (
    role_id bigint NOT NULL,
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
-- TOC entry 3578 (class 0 OID 0)
-- Dependencies: 222
-- Name: COLUMN role_info.role_id; Type: COMMENT; Schema: biz; Owner: root
--

COMMENT ON COLUMN biz.role_info.role_id IS '角色ID';


--
-- TOC entry 3579 (class 0 OID 0)
-- Dependencies: 222
-- Name: COLUMN role_info.data_scope; Type: COMMENT; Schema: biz; Owner: root
--

COMMENT ON COLUMN biz.role_info.data_scope IS '数据范围（1=所有数据权限,2=自定义数据权限,3=本部门数据权限,4=本部门及以下数据权限,5=仅本人数据权限）';


--
-- TOC entry 3580 (class 0 OID 0)
-- Dependencies: 222
-- Name: COLUMN role_info.role_name; Type: COMMENT; Schema: biz; Owner: root
--

COMMENT ON COLUMN biz.role_info.role_name IS '角色名称';


--
-- TOC entry 3581 (class 0 OID 0)
-- Dependencies: 222
-- Name: COLUMN role_info.status; Type: COMMENT; Schema: biz; Owner: root
--

COMMENT ON COLUMN biz.role_info.status IS '状态（0代表正常 1代表停用）';


--
-- TOC entry 3582 (class 0 OID 0)
-- Dependencies: 222
-- Name: COLUMN role_info.inherited; Type: COMMENT; Schema: biz; Owner: root
--

COMMENT ON COLUMN biz.role_info.inherited IS '继承关系';


--
-- TOC entry 3583 (class 0 OID 0)
-- Dependencies: 222
-- Name: COLUMN role_info.role_sort; Type: COMMENT; Schema: biz; Owner: root
--

COMMENT ON COLUMN biz.role_info.role_sort IS '排序';


--
-- TOC entry 223 (class 1259 OID 16884)
-- Name: role_to_dept; Type: TABLE; Schema: biz; Owner: postgres
--

CREATE TABLE biz.role_to_dept (
    role_id bigint NOT NULL,
    dept_id bigint NOT NULL
);


ALTER TABLE biz.role_to_dept OWNER TO postgres;

--
-- TOC entry 3584 (class 0 OID 0)
-- Dependencies: 223
-- Name: TABLE role_to_dept; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON TABLE biz.role_to_dept IS '角色数据权限关联表';


--
-- TOC entry 3585 (class 0 OID 0)
-- Dependencies: 223
-- Name: COLUMN role_to_dept.role_id; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.role_to_dept.role_id IS '角色id';


--
-- TOC entry 3586 (class 0 OID 0)
-- Dependencies: 223
-- Name: COLUMN role_to_dept.dept_id; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.role_to_dept.dept_id IS '部门id';


--
-- TOC entry 224 (class 1259 OID 16887)
-- Name: role_to_hierarchy; Type: TABLE; Schema: biz; Owner: root
--

CREATE TABLE biz.role_to_hierarchy (
    role_id bigint NOT NULL,
    inherited_id bigint NOT NULL
);


ALTER TABLE biz.role_to_hierarchy OWNER TO root;

--
-- TOC entry 3587 (class 0 OID 0)
-- Dependencies: 224
-- Name: TABLE role_to_hierarchy; Type: COMMENT; Schema: biz; Owner: root
--

COMMENT ON TABLE biz.role_to_hierarchy IS '角色继承表';


--
-- TOC entry 3588 (class 0 OID 0)
-- Dependencies: 224
-- Name: COLUMN role_to_hierarchy.role_id; Type: COMMENT; Schema: biz; Owner: root
--

COMMENT ON COLUMN biz.role_to_hierarchy.role_id IS '角色id';


--
-- TOC entry 3589 (class 0 OID 0)
-- Dependencies: 224
-- Name: COLUMN role_to_hierarchy.inherited_id; Type: COMMENT; Schema: biz; Owner: root
--

COMMENT ON COLUMN biz.role_to_hierarchy.inherited_id IS '继承角色id';


--
-- TOC entry 225 (class 1259 OID 16890)
-- Name: role_to_menu; Type: TABLE; Schema: biz; Owner: postgres
--

CREATE TABLE biz.role_to_menu (
    role_id bigint NOT NULL,
    menu_id bigint NOT NULL
);


ALTER TABLE biz.role_to_menu OWNER TO postgres;

--
-- TOC entry 3590 (class 0 OID 0)
-- Dependencies: 225
-- Name: TABLE role_to_menu; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON TABLE biz.role_to_menu IS '角色和菜单关联表';


--
-- TOC entry 3591 (class 0 OID 0)
-- Dependencies: 225
-- Name: COLUMN role_to_menu.role_id; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.role_to_menu.role_id IS '角色ID';


--
-- TOC entry 3592 (class 0 OID 0)
-- Dependencies: 225
-- Name: COLUMN role_to_menu.menu_id; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.role_to_menu.menu_id IS '菜单ID';


--
-- TOC entry 228 (class 1259 OID 16899)
-- Name: t_member_info; Type: TABLE; Schema: biz; Owner: postgres
--

CREATE TABLE biz.t_member_info (
    member_id bigint NOT NULL,
    tenant_id bigint NOT NULL,
    username character varying(30) NOT NULL,
    phone_number character varying(16),
    status character(1) DEFAULT 0 NOT NULL,
    gender character(1) DEFAULT 2 NOT NULL,
    avatar character varying(150),
    password character varying(100),
    login_ip inet,
    login_date timestamp(6) with time zone,
    remark character varying(64),
    creator_id bigint NOT NULL,
    updater_id bigint,
    create_at timestamp(6) with time zone NOT NULL,
    update_at timestamp(6) with time zone
);


ALTER TABLE biz.t_member_info OWNER TO postgres;

--
-- TOC entry 3593 (class 0 OID 0)
-- Dependencies: 228
-- Name: TABLE t_member_info; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON TABLE biz.t_member_info IS '租户成员表';


--
-- TOC entry 3594 (class 0 OID 0)
-- Dependencies: 228
-- Name: COLUMN t_member_info.member_id; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.t_member_info.member_id IS 'ID';


--
-- TOC entry 3595 (class 0 OID 0)
-- Dependencies: 228
-- Name: COLUMN t_member_info.tenant_id; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.t_member_info.tenant_id IS '租户ID';


--
-- TOC entry 3596 (class 0 OID 0)
-- Dependencies: 228
-- Name: COLUMN t_member_info.username; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.t_member_info.username IS '用户名';


--
-- TOC entry 3597 (class 0 OID 0)
-- Dependencies: 228
-- Name: COLUMN t_member_info.phone_number; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.t_member_info.phone_number IS '手机号码';


--
-- TOC entry 3598 (class 0 OID 0)
-- Dependencies: 228
-- Name: COLUMN t_member_info.status; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.t_member_info.status IS '状态值';


--
-- TOC entry 3599 (class 0 OID 0)
-- Dependencies: 228
-- Name: COLUMN t_member_info.gender; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.t_member_info.gender IS '性别（0男 1女 2未知）';


--
-- TOC entry 3600 (class 0 OID 0)
-- Dependencies: 228
-- Name: COLUMN t_member_info.avatar; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.t_member_info.avatar IS '头像地址';


--
-- TOC entry 3601 (class 0 OID 0)
-- Dependencies: 228
-- Name: COLUMN t_member_info.password; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.t_member_info.password IS '密码';


--
-- TOC entry 3602 (class 0 OID 0)
-- Dependencies: 228
-- Name: COLUMN t_member_info.login_ip; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.t_member_info.login_ip IS '登录ip';


--
-- TOC entry 3603 (class 0 OID 0)
-- Dependencies: 228
-- Name: COLUMN t_member_info.login_date; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.t_member_info.login_date IS '登录时间';


--
-- TOC entry 3604 (class 0 OID 0)
-- Dependencies: 228
-- Name: COLUMN t_member_info.remark; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.t_member_info.remark IS '备注';


--
-- TOC entry 3605 (class 0 OID 0)
-- Dependencies: 228
-- Name: COLUMN t_member_info.creator_id; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.t_member_info.creator_id IS '创建者';


--
-- TOC entry 3606 (class 0 OID 0)
-- Dependencies: 228
-- Name: COLUMN t_member_info.updater_id; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.t_member_info.updater_id IS '更新者';


--
-- TOC entry 229 (class 1259 OID 16906)
-- Name: t_member_to_post; Type: TABLE; Schema: biz; Owner: postgres
--

CREATE TABLE biz.t_member_to_post (
    member_id bigint NOT NULL,
    post_id bigint NOT NULL
);


ALTER TABLE biz.t_member_to_post OWNER TO postgres;

--
-- TOC entry 3607 (class 0 OID 0)
-- Dependencies: 229
-- Name: TABLE t_member_to_post; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON TABLE biz.t_member_to_post IS '租户成员和职位关联表';


--
-- TOC entry 3608 (class 0 OID 0)
-- Dependencies: 229
-- Name: COLUMN t_member_to_post.member_id; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.t_member_to_post.member_id IS '租户成员ID';


--
-- TOC entry 3609 (class 0 OID 0)
-- Dependencies: 229
-- Name: COLUMN t_member_to_post.post_id; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.t_member_to_post.post_id IS '职位ID';


--
-- TOC entry 226 (class 1259 OID 16893)
-- Name: t_post_info; Type: TABLE; Schema: biz; Owner: root
--

CREATE TABLE biz.t_post_info (
    post_id bigint NOT NULL,
    tenant_id bigint NOT NULL,
    post_name character varying(20) NOT NULL,
    status character(1) NOT NULL,
    post_sort smallint NOT NULL,
    create_at timestamp(6) with time zone NOT NULL,
    creator_id bigint NOT NULL,
    update_at timestamp(6) with time zone,
    updater_id bigint
);


ALTER TABLE biz.t_post_info OWNER TO root;

--
-- TOC entry 3610 (class 0 OID 0)
-- Dependencies: 226
-- Name: TABLE t_post_info; Type: COMMENT; Schema: biz; Owner: root
--

COMMENT ON TABLE biz.t_post_info IS '租户职位信息';


--
-- TOC entry 3611 (class 0 OID 0)
-- Dependencies: 226
-- Name: COLUMN t_post_info.post_id; Type: COMMENT; Schema: biz; Owner: root
--

COMMENT ON COLUMN biz.t_post_info.post_id IS '职位ID';


--
-- TOC entry 3612 (class 0 OID 0)
-- Dependencies: 226
-- Name: COLUMN t_post_info.tenant_id; Type: COMMENT; Schema: biz; Owner: root
--

COMMENT ON COLUMN biz.t_post_info.tenant_id IS '租户ID';


--
-- TOC entry 3613 (class 0 OID 0)
-- Dependencies: 226
-- Name: COLUMN t_post_info.post_name; Type: COMMENT; Schema: biz; Owner: root
--

COMMENT ON COLUMN biz.t_post_info.post_name IS '职位名称';


--
-- TOC entry 3614 (class 0 OID 0)
-- Dependencies: 226
-- Name: COLUMN t_post_info.status; Type: COMMENT; Schema: biz; Owner: root
--

COMMENT ON COLUMN biz.t_post_info.status IS '状态（0代表正常 1代表停用）';


--
-- TOC entry 3615 (class 0 OID 0)
-- Dependencies: 226
-- Name: COLUMN t_post_info.post_sort; Type: COMMENT; Schema: biz; Owner: root
--

COMMENT ON COLUMN biz.t_post_info.post_sort IS '排序';


--
-- TOC entry 227 (class 1259 OID 16896)
-- Name: t_post_to_menu; Type: TABLE; Schema: biz; Owner: postgres
--

CREATE TABLE biz.t_post_to_menu (
    post_id bigint NOT NULL,
    menu_id bigint NOT NULL
);


ALTER TABLE biz.t_post_to_menu OWNER TO postgres;

--
-- TOC entry 3616 (class 0 OID 0)
-- Dependencies: 227
-- Name: TABLE t_post_to_menu; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON TABLE biz.t_post_to_menu IS '职位和菜单关联表';


--
-- TOC entry 3617 (class 0 OID 0)
-- Dependencies: 227
-- Name: COLUMN t_post_to_menu.post_id; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.t_post_to_menu.post_id IS '职位ID';


--
-- TOC entry 3618 (class 0 OID 0)
-- Dependencies: 227
-- Name: COLUMN t_post_to_menu.menu_id; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.t_post_to_menu.menu_id IS '菜单ID';


--
-- TOC entry 230 (class 1259 OID 16909)
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
    post_limit smallint NOT NULL,
    creator_id bigint NOT NULL,
    create_at timestamp with time zone NOT NULL,
    updater_id bigint,
    update_at timestamp with time zone
);


ALTER TABLE biz.tenant_info OWNER TO postgres;

--
-- TOC entry 3619 (class 0 OID 0)
-- Dependencies: 230
-- Name: TABLE tenant_info; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON TABLE biz.tenant_info IS '租户表';


--
-- TOC entry 3620 (class 0 OID 0)
-- Dependencies: 230
-- Name: COLUMN tenant_info.tenant_id; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.tenant_info.tenant_id IS '租户编号';


--
-- TOC entry 3621 (class 0 OID 0)
-- Dependencies: 230
-- Name: COLUMN tenant_info.administrator; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.tenant_info.administrator IS '管理员ID';


--
-- TOC entry 3622 (class 0 OID 0)
-- Dependencies: 230
-- Name: COLUMN tenant_info.tenant_type; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.tenant_info.tenant_type IS '租户类型';


--
-- TOC entry 3623 (class 0 OID 0)
-- Dependencies: 230
-- Name: COLUMN tenant_info.contact_phone; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.tenant_info.contact_phone IS '联系电话';


--
-- TOC entry 3624 (class 0 OID 0)
-- Dependencies: 230
-- Name: COLUMN tenant_info.tenant_name; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.tenant_info.tenant_name IS '租户名称';


--
-- TOC entry 3625 (class 0 OID 0)
-- Dependencies: 230
-- Name: COLUMN tenant_info.oper_pwd; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.tenant_info.oper_pwd IS '操作密码';


--
-- TOC entry 3626 (class 0 OID 0)
-- Dependencies: 230
-- Name: COLUMN tenant_info.status; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.tenant_info.status IS '租户状态';


--
-- TOC entry 3627 (class 0 OID 0)
-- Dependencies: 230
-- Name: COLUMN tenant_info.domain; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.tenant_info.domain IS '域名';


--
-- TOC entry 3628 (class 0 OID 0)
-- Dependencies: 230
-- Name: COLUMN tenant_info.remark; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.tenant_info.remark IS '备注';


--
-- TOC entry 3629 (class 0 OID 0)
-- Dependencies: 230
-- Name: COLUMN tenant_info.package_id; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.tenant_info.package_id IS '租户套餐编号';


--
-- TOC entry 3630 (class 0 OID 0)
-- Dependencies: 230
-- Name: COLUMN tenant_info.expire_time; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.tenant_info.expire_time IS '过期时间';


--
-- TOC entry 3631 (class 0 OID 0)
-- Dependencies: 230
-- Name: COLUMN tenant_info.account_limit; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.tenant_info.account_limit IS '账号数量（-1不限制）';


--
-- TOC entry 3632 (class 0 OID 0)
-- Dependencies: 230
-- Name: COLUMN tenant_info.post_limit; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.tenant_info.post_limit IS '职位数量（-1不限制）';


--
-- TOC entry 3633 (class 0 OID 0)
-- Dependencies: 230
-- Name: COLUMN tenant_info.creator_id; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.tenant_info.creator_id IS '创建者';


--
-- TOC entry 3634 (class 0 OID 0)
-- Dependencies: 230
-- Name: COLUMN tenant_info.create_at; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.tenant_info.create_at IS '创建时间';


--
-- TOC entry 3635 (class 0 OID 0)
-- Dependencies: 230
-- Name: COLUMN tenant_info.updater_id; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.tenant_info.updater_id IS '更新者';


--
-- TOC entry 3636 (class 0 OID 0)
-- Dependencies: 230
-- Name: COLUMN tenant_info.update_at; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.tenant_info.update_at IS '更新时间';


--
-- TOC entry 231 (class 1259 OID 16912)
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
-- TOC entry 3637 (class 0 OID 0)
-- Dependencies: 231
-- Name: TABLE tenant_package_info; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON TABLE biz.tenant_package_info IS '租户套餐表';


--
-- TOC entry 3638 (class 0 OID 0)
-- Dependencies: 231
-- Name: COLUMN tenant_package_info.package_id; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.tenant_package_info.package_id IS '租户套餐id';


--
-- TOC entry 3639 (class 0 OID 0)
-- Dependencies: 231
-- Name: COLUMN tenant_package_info.package_name; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.tenant_package_info.package_name IS '套餐名称';


--
-- TOC entry 3640 (class 0 OID 0)
-- Dependencies: 231
-- Name: COLUMN tenant_package_info.status; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.tenant_package_info.status IS '状态（0正常 1停用）';


--
-- TOC entry 3641 (class 0 OID 0)
-- Dependencies: 231
-- Name: COLUMN tenant_package_info.menu_ids; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.tenant_package_info.menu_ids IS '套餐绑定的菜单';


--
-- TOC entry 3642 (class 0 OID 0)
-- Dependencies: 231
-- Name: COLUMN tenant_package_info.remark; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.tenant_package_info.remark IS '备注';


--
-- TOC entry 3643 (class 0 OID 0)
-- Dependencies: 231
-- Name: COLUMN tenant_package_info.creator_id; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.tenant_package_info.creator_id IS '创建者';


--
-- TOC entry 3644 (class 0 OID 0)
-- Dependencies: 231
-- Name: COLUMN tenant_package_info.create_at; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.tenant_package_info.create_at IS '创建时间';


--
-- TOC entry 3645 (class 0 OID 0)
-- Dependencies: 231
-- Name: COLUMN tenant_package_info.updater_id; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.tenant_package_info.updater_id IS '更新者';


--
-- TOC entry 3646 (class 0 OID 0)
-- Dependencies: 231
-- Name: COLUMN tenant_package_info.update_at; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.tenant_package_info.update_at IS '更新时间';


--
-- TOC entry 232 (class 1259 OID 16918)
-- Name: tenant_wallet_info; Type: TABLE; Schema: biz; Owner: postgres
--

CREATE TABLE biz.tenant_wallet_info (
    tenant_id bigint NOT NULL,
    balance numeric(10,2) NOT NULL,
    status character(1) NOT NULL
);


ALTER TABLE biz.tenant_wallet_info OWNER TO postgres;

--
-- TOC entry 3647 (class 0 OID 0)
-- Dependencies: 232
-- Name: TABLE tenant_wallet_info; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON TABLE biz.tenant_wallet_info IS '租户钱包';


--
-- TOC entry 3648 (class 0 OID 0)
-- Dependencies: 232
-- Name: COLUMN tenant_wallet_info.balance; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.tenant_wallet_info.balance IS '余额, 元';


--
-- TOC entry 3649 (class 0 OID 0)
-- Dependencies: 232
-- Name: COLUMN tenant_wallet_info.status; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.tenant_wallet_info.status IS '状态';


--
-- TOC entry 233 (class 1259 OID 16921)
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
-- TOC entry 3650 (class 0 OID 0)
-- Dependencies: 233
-- Name: TABLE tenant_wallet_record; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON TABLE biz.tenant_wallet_record IS '租户钱包记录';


--
-- TOC entry 3651 (class 0 OID 0)
-- Dependencies: 233
-- Name: COLUMN tenant_wallet_record.tenant_id; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.tenant_wallet_record.tenant_id IS '租户ID';


--
-- TOC entry 3652 (class 0 OID 0)
-- Dependencies: 233
-- Name: COLUMN tenant_wallet_record.amount; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.tenant_wallet_record.amount IS '金额, 单位元';


--
-- TOC entry 3653 (class 0 OID 0)
-- Dependencies: 233
-- Name: COLUMN tenant_wallet_record.record_type; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.tenant_wallet_record.record_type IS '记录类型';


--
-- TOC entry 3654 (class 0 OID 0)
-- Dependencies: 233
-- Name: COLUMN tenant_wallet_record.biz_type; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.tenant_wallet_record.biz_type IS '业务类型';


--
-- TOC entry 3655 (class 0 OID 0)
-- Dependencies: 233
-- Name: COLUMN tenant_wallet_record.biz_no; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.tenant_wallet_record.biz_no IS '业务编号';


--
-- TOC entry 3656 (class 0 OID 0)
-- Dependencies: 233
-- Name: COLUMN tenant_wallet_record.create_at; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.tenant_wallet_record.create_at IS '创建时间';


--
-- TOC entry 3657 (class 0 OID 0)
-- Dependencies: 233
-- Name: COLUMN tenant_wallet_record.remark; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.tenant_wallet_record.remark IS '备注';


--
-- TOC entry 234 (class 1259 OID 16924)
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
-- TOC entry 3658 (class 0 OID 0)
-- Dependencies: 234
-- Name: TABLE tenant_wallet_withdrawal_record; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON TABLE biz.tenant_wallet_withdrawal_record IS '系统提现记录';


--
-- TOC entry 3659 (class 0 OID 0)
-- Dependencies: 234
-- Name: COLUMN tenant_wallet_withdrawal_record.withdrawal_id; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.tenant_wallet_withdrawal_record.withdrawal_id IS 'id';


--
-- TOC entry 3660 (class 0 OID 0)
-- Dependencies: 234
-- Name: COLUMN tenant_wallet_withdrawal_record.order_no; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.tenant_wallet_withdrawal_record.order_no IS '订单号';


--
-- TOC entry 3661 (class 0 OID 0)
-- Dependencies: 234
-- Name: COLUMN tenant_wallet_withdrawal_record.status; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.tenant_wallet_withdrawal_record.status IS '状态
''0'' 处理中
''1'' 成功
''2'' 失败';


--
-- TOC entry 3662 (class 0 OID 0)
-- Dependencies: 234
-- Name: COLUMN tenant_wallet_withdrawal_record.tenant_id; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.tenant_wallet_withdrawal_record.tenant_id IS '租户id';


--
-- TOC entry 3663 (class 0 OID 0)
-- Dependencies: 234
-- Name: COLUMN tenant_wallet_withdrawal_record.request_params; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.tenant_wallet_withdrawal_record.request_params IS '第三方请求参数';


--
-- TOC entry 3664 (class 0 OID 0)
-- Dependencies: 234
-- Name: COLUMN tenant_wallet_withdrawal_record.amount; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.tenant_wallet_withdrawal_record.amount IS '金额, 单位元';


--
-- TOC entry 3665 (class 0 OID 0)
-- Dependencies: 234
-- Name: COLUMN tenant_wallet_withdrawal_record.error_msg; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.tenant_wallet_withdrawal_record.error_msg IS '错误信息';


--
-- TOC entry 3666 (class 0 OID 0)
-- Dependencies: 234
-- Name: COLUMN tenant_wallet_withdrawal_record.creator_id; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.tenant_wallet_withdrawal_record.creator_id IS '创建者';


--
-- TOC entry 3667 (class 0 OID 0)
-- Dependencies: 234
-- Name: COLUMN tenant_wallet_withdrawal_record.create_at; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.tenant_wallet_withdrawal_record.create_at IS '创建时间';


--
-- TOC entry 3668 (class 0 OID 0)
-- Dependencies: 234
-- Name: COLUMN tenant_wallet_withdrawal_record.complete_at; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.tenant_wallet_withdrawal_record.complete_at IS '完成时间';


--
-- TOC entry 3669 (class 0 OID 0)
-- Dependencies: 234
-- Name: COLUMN tenant_wallet_withdrawal_record.remark; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.tenant_wallet_withdrawal_record.remark IS '备注';


--
-- TOC entry 3492 (class 0 OID 16929)
-- Dependencies: 235
-- Data for Name: admin_info; Type: TABLE DATA; Schema: biz; Owner: postgres
--

COPY biz.admin_info (admin_id, dept_id, username, email, nickname, phone_number, status, gender, avatar, password, login_ip, login_date, remark, creator_id, updater_id, create_at, update_at) FROM stdin;
100	\N	super	\N	nick_a	13512312311	0	1	https://img-s-msn-com.akamaized.net/tenant/amp/entityid/AA1B91c8.img?w=660&h=648&m=6&x=219&y=147&s=204&d=204	{bcrypt}$2a$10$7JB720yubVSZvUI0rEqK/.VqGOZTH.ulu33dHOiBE8ByOhJIrdAu2	172.16.8.131	2025-11-10 11:18:00+08	\N	1	\N	2024-04-17 14:08:54.616+08	\N
\.


--
-- TOC entry 3493 (class 0 OID 16936)
-- Dependencies: 236
-- Data for Name: admin_to_role; Type: TABLE DATA; Schema: biz; Owner: postgres
--

COPY biz.admin_to_role (admin_id, role_id) FROM stdin;
\.


--
-- TOC entry 3472 (class 0 OID 16846)
-- Dependencies: 215
-- Data for Name: customer_info; Type: TABLE DATA; Schema: biz; Owner: postgres
--

COPY biz.customer_info (customer_id, nickname, phone_number, status, gender, avatar, login_ip, login_date, creator_id, updater_id, create_at, update_at) FROM stdin;
1988138628742279170	123	\N	0	0	http://tmp/f0iJwZfGvBx9bd2d939bf0fbdab283f01e98a4d9bc31.png	172.16.8.131	2025-11-11 17:58:46+08	0	0	2025-11-11 14:56:01.579+08	2025-11-11 17:58:46.741+08
\.


--
-- TOC entry 3473 (class 0 OID 16851)
-- Dependencies: 216
-- Data for Name: customer_social_info; Type: TABLE DATA; Schema: biz; Owner: postgres
--

COPY biz.customer_social_info (customer_id, identifier, identifier_type) FROM stdin;
1988138628742279170	ozNXO5eZpDZXZMInfjKhkkr7LQzs	we_xcx
\.


--
-- TOC entry 3474 (class 0 OID 16854)
-- Dependencies: 217
-- Data for Name: customer_wallet_info; Type: TABLE DATA; Schema: biz; Owner: postgres
--

COPY biz.customer_wallet_info (customer_id, balance, status) FROM stdin;
1988138628742279170	0.00	0
\.


--
-- TOC entry 3475 (class 0 OID 16857)
-- Dependencies: 218
-- Data for Name: customer_wallet_record; Type: TABLE DATA; Schema: biz; Owner: postgres
--

COPY biz.customer_wallet_record (record_id, customer_id, amount, record_type, create_at, remark) FROM stdin;
\.


--
-- TOC entry 3476 (class 0 OID 16860)
-- Dependencies: 219
-- Data for Name: dept_info; Type: TABLE DATA; Schema: biz; Owner: postgres
--

COPY biz.dept_info (dept_id, parent_id, ancestors, dept_name, status, dept_sort, contact, email, creator_id, updater_id, create_at, update_at) FROM stdin;
\.


--
-- TOC entry 3477 (class 0 OID 16867)
-- Dependencies: 220
-- Data for Name: menu_info; Type: TABLE DATA; Schema: biz; Owner: postgres
--

COPY biz.menu_info (menu_id, menu_name, parent_id, menu_sort, path, component, query, menu_type, status, perms, icon, cacheable, visible, scope, creator_id, updater_id, create_at, update_at) FROM stdin;
1980906033277222913	日志审计	0	0	audit-log	\N	\N	D	0	\N	carbon:catalog-publish	f	t	tenant	1	1	2025-10-22 15:56:16.449+08	2025-10-22 15:58:02.486+08
1906263415450001129	重置租户操作密码	1906263415450000601	11	#	\N	\N	B	0	prin-mod:tenant-mng:reset-operpwd	#	f	t	system	1	1	2024-05-26 12:30:16+08	2025-09-03 16:17:23.785+08
1906263415450001215	修改密钥	1906263415450000700	5	#	\N	\N	B	0	prin-mod:oauth2client-mng:edit-secret	#	f	t	system	1	1	2024-05-26 12:30:16+08	2025-09-03 16:16:07.901+08
1983741300921024514	api分析	1983739365543329794	10	apicall	statistics/apicall/index	\N	M	0	\N	carbon:api-1	f	t	system	1	1	2025-10-30 11:42:36.932+08	2025-10-31 09:22:48.476+08
1983739365543329794	统计分析	0	100	statistics	\N	\N	D	0	gateway-mod:statistics:pvuv	carbon:chart-dual-y-axis	f	t	system	1	1	2025-10-30 11:34:55.498+08	2025-10-31 09:22:59.26+08
1906263415450000304	服务监控	1906263415450000101	5	http://localhost:9100/	\N	\N	O	0		carbon:link	f	t	system	1	1	2024-05-26 12:30:16+08	2025-09-03 13:53:37.79+08
1906263415450000101	控制台入口	1963871785836048386	0	controller	\N	\N	D	0	\N	carbon:dashboard	f	t	system	1	1	2024-05-26 12:30:16+08	2025-09-05 15:53:53.291+08
1906263415450000300	定时任务	1906263415450000101	20	http://localhost:9200/xxl-job-admin	\N	\N	O	0		carbon:link	f	t	system	1	1	2024-05-26 12:30:16+08	2025-09-05 16:16:21.958+08
1906263415450000303	Nacos控制台	1906263415450000101	4	http://localhost:8848/nacos	\N	\N	O	0		carbon:link	f	t	system	1	1	2024-05-26 12:30:16+08	2025-09-05 16:16:29.014+08
1906263415450000207	菜单管理	1906263415450000003	50	menu	menu/mng/index	\N	M	0	prin-mod:menu-mng:list	carbon:menu	f	t	system	1	1	2024-05-26 12:30:16+08	2025-09-03 13:36:05.116+08
1906263415450000700	终端管理	1906263415450000003	3	oauth2client	oauth2client/mng/index	\N	M	0	prin-mod:oauth2client-mng:list	carbon:application	f	t	system	1	1	2024-05-26 12:30:16+08	2025-09-03 17:29:48.237+08
1906263415450000002	组织管理	0	50	organization-mng	\N	\N	D	0	\N	carbon:user	f	t	system	1	1	2024-05-26 12:30:16+08	2025-10-22 15:09:40.54+08
1906263415450000104	日志审计	0	1	audit-log	\N	\N	D	0	\N	carbon:ibm-knowledge-catalog-premium	f	t	system	1	1	2024-05-26 12:30:16+08	2025-10-22 15:59:16.035+08
1906263415450000001	消息管理	0	80	system-mng	\N	\N	D	0	\N	carbon:z-systems	f	t	system	1	1	2024-05-26 12:30:16+08	2025-10-30 11:35:26.814+08
1906263415450000302	Sentinel控制台	1906263415450000101	3	http://localhost:8718	\N	\N	O	0		carbon:link	f	t	system	1	1	2024-05-26 12:30:16+08	2025-09-03 13:54:06.192+08
1906263415450000301	系统接口	1906263415450000101	2	http://localhost:8080/doc.html	\N	\N	I	0		carbon:link	f	t	system	1	1	2024-05-26 12:30:16+08	2025-09-03 13:54:14.675+08
1906263415450000003	平台管理	0	60	platform-mng	\N	\N	D	0	\N	carbon:platforms	f	t	system	1	1	2024-05-26 12:30:16+08	2025-10-30 11:35:31.843+08
1983742775738974209	pv/uv统计	1983739365543329794	0	pageview	statistics/pageview/index	\N	M	0	\N	carbon:activity	f	t	system	1	1	2025-10-30 11:48:28.558+08	2025-10-31 09:23:34.75+08
1963871785836048386	平台配置	0	0	develop	\N	\N	D	0	\N	carbon:tool-kit	f	t	system	1	1	2025-09-05 15:48:15.373+08	2025-10-11 09:28:34.482+08
1906263415450001052	新增参数	1906263415450000103	2	#	\N	\N	B	0	msg-mod:config-mng:add	#	f	t	system	1	1	2024-05-26 12:30:16+08	2025-10-10 09:11:56.276+08
1906263415450002207	权限授予	1906263415450000206	6	#	\N	\N	B	0	prin-mod:role-mng:grant-user	#	f	t	system	1	1	2024-05-26 12:30:16+08	2025-09-03 15:40:20.269+08
1906263415450000100	公告管理	1906263415450000001	15	announcement	announcement/mng/index	\N	M	0	msg-mod:announcement-mng:list	carbon:message-queue	f	t	system	1	1	2024-05-26 12:30:16+08	2025-10-14 10:06:06.315+08
1906263415450000103	配置管理	1963871785836048386	7	config	config/mng/index	\N	M	0	msg-mod:config-mng:list	carbon:parameter	f	t	system	1	1	2024-05-26 12:30:16+08	2025-10-14 10:09:25.438+08
1906263415450000102	字典管理	1963871785836048386	6	dictionary	dictionary/mng/index	\N	M	0	msg-mod:dictionary-mng:list	carbon:text-vertical-alignment	f	t	system	1	1	2024-05-26 12:30:16+08	2025-10-14 10:09:30.223+08
1976223833424326657	修改角色	1906263415450000206	4	#	\N	\N	B	0	prin-mod:role-mng:edit	#	f	t	system	1	1	2025-10-09 17:50:53.008+08	2025-10-09 17:50:53.008+08
1976565556872667137	组织管理	0	50	organization-mng	\N	\N	D	0	\N	carbon:user	f	t	tenant	1	1	2025-10-10 16:28:46.235+08	2025-10-22 15:56:22.969+08
1976585906620653569	职位管理	1976565556872667137	0	post	post/mng/index	\N	M	0	prin-mod:post-mng:list	carbon:load-balancer-classic	f	t	tenant	1	1	2025-10-10 17:49:37.998+08	2025-10-15 14:55:01.703+08
1976455457936171010	修改参数	1906263415450000103	4	#	\N	\N	B	0	msg-mod:config-mng:edit	#	f	t	system	1	1	2025-10-10 09:11:16.596+08	2025-10-10 09:11:16.596+08
1976455537858633730	删除参数	1906263415450000103	5	#	\N	\N	B	0	msg-mod:config-mng:remove	#	f	t	system	1	1	2025-10-10 09:11:35.655+08	2025-10-10 09:11:35.655+08
1976455824778387458	添加字典	1906263415450000102	0	#	\N	\N	B	0	msg-mod:dictionary-mng:add	#	f	t	system	1	1	2025-10-10 09:12:44.068+08	2025-10-10 09:12:44.068+08
1976455887881691138	修改字典	1906263415450000102	3	#	\N	\N	B	0	msg-mod:dictionary-mng:edit	#	f	t	system	1	1	2025-10-10 09:12:59.112+08	2025-10-10 09:12:59.112+08
1976455969624481794	删除字典	1906263415450000102	4	#	\N	\N	B	0	msg-mod:dictionary-mng:remove	#	f	t	system	1	1	2025-10-10 09:13:18.603+08	2025-10-10 09:13:18.603+08
1976456608446341121	添加草稿	1906263415450000100	0	#	\N	\N	B	0	msg-mod:announcement-mng:add	#	f	t	system	1	1	2025-10-10 09:15:50.91+08	2025-10-10 09:15:50.91+08
1976456667900600322	修改草稿	1906263415450000100	0	#	\N	\N	B	0	msg-mod:announcement-mng:edit	#	f	t	system	1	1	2025-10-10 09:16:05.073+08	2025-10-10 09:16:05.073+08
1976223921466961921	删除角色	1906263415450000206	0	#	\N	\N	B	0	prin-mod:role-mng:remove	#	f	t	system	1	1	2025-10-09 17:51:14+08	2025-10-09 17:51:14+08
1976224202049122306	新增部门	1906263415450000205	0	#	\N	\N	B	0	prin-mod:dept-mng:add	#	f	t	system	1	1	2025-10-09 17:52:20.902+08	2025-10-09 17:52:20.902+08
1906263415450002039	修改部门	1906263415450000205	3	#	\N	\N	B	0	prin-mod:dept-mng:edit	#	f	t	system	1	1	2024-05-26 12:30:16+08	2025-10-09 17:52:34.297+08
1976224332491976706	删除部门	1906263415450000205	5	#	\N	\N	B	0	prin-mod:dept-mng:remove	#	f	t	system	1	1	2025-10-09 17:52:52.004+08	2025-10-09 17:52:52.004+08
1976224466323828738	部门详细	1906263415450000205	7	#	\N	\N	B	0	prin-mod:dept-mng:query	#	f	t	system	1	1	2025-10-09 17:53:23.903+08	2025-10-09 17:53:23.903+08
1976224939848167426	修改终端	1906263415450000700	7	#	\N	\N	B	0	prin-mod:oauth2client-mng:edit	#	f	t	system	1	1	2025-10-09 17:55:16.805+08	2025-10-09 17:55:16.805+08
1976225090838917122	添加终端	1906263415450000700	0	#	\N	\N	B	0	prin-mod:oauth2client-mng:add	#	f	t	system	1	1	2025-10-09 17:55:52.805+08	2025-10-09 17:55:52.805+08
1976225224402333698	删除终端	1906263415450000700	0	#	\N	\N	B	0	prin-mod:oauth2client-mng:remove	#	f	t	system	1	1	2025-10-09 17:56:24.65+08	2025-10-09 17:56:24.65+08
1976225825756475393	新增菜单	1906263415450000207	0	#	\N	\N	B	0	prin-mod:menu-mng:add	#	f	t	system	1	1	2025-10-09 17:58:48.025+08	2025-10-09 17:58:48.025+08
1910569625749024770	授权角色	1906263415450000203	0	#	\N	\N	B	0	prin-mod:admin-mng:grant-role	#	f	t	system	1	100	2025-04-11 13:44:30.104+08	2025-11-07 13:58:45.924+08
1906263415450000151	登录日志	1906263415450000104	2	login	loginlog-admin/mng/index	\N	M	0	msg-mod:admin-loginlog-mng:list	carbon:login	f	t	system	1	100	2024-05-26 12:30:16+08	2025-11-07 15:00:44.821+08
1980906706949554177	操作日志	1980906033277222913	0	operate	operatelog-tenant/mng/index	\N	M	0	msg-mod:tenant-operatelog-mng:list	carbon:touch-interaction	f	t	tenant	1	100	2025-10-22 15:58:57.063+08	2025-11-10 11:08:34.818+08
1976570103963770881	成员管理	1976565556872667137	8	member	member/mng/index	\N	M	0	prin-mod:member-mng:list	carbon:user-identification	f	t	tenant	1	100	2025-10-10 16:46:50.34+08	2025-11-10 11:11:21.068+08
1980906374936838146	登录日志	1980906033277222913	10	login	loginlog-tenant/mng/index	\N	M	0	msg-mod:tenant-loginlog-mng:list	carbon:login	f	t	tenant	1	100	2025-10-22 15:57:37.907+08	2025-11-10 11:08:26.941+08
1976225899114852354	修改菜单	1906263415450000207	0	#	\N	\N	B	0	prin-mod:menu-mng:edit	#	f	t	system	1	1	2025-10-09 17:59:05.507+08	2025-10-09 17:59:05.507+08
1976226216250372098	新增租户	1906263415450000601	5	#	\N	\N	B	0	prin-mod:tenant-mng:add	#	f	t	system	1	1	2025-10-09 18:00:21.123+08	2025-10-09 18:00:21.123+08
1976226385717030913	删除租户	1906263415450000601	8	#	\N	\N	B	0	prin-mod:tenant-mng:remove	#	f	t	system	1	1	2025-10-09 18:01:01.527+08	2025-10-09 18:01:01.527+08
1976586082013863937	新增职位	1976585906620653569	0	#	\N	\N	B	0	prin-mod:post-mng:add	#	f	t	tenant	1	1	2025-10-10 17:50:19.807+08	2025-10-10 17:50:19.807+08
1976456851288154113	删除公告	1906263415450000100	0	#	\N	\N	B	0	msg-mod:announcement-mng:remove	#	f	t	system	1	1	2025-10-10 09:16:48.797+08	2025-10-10 09:16:48.797+08
1906263415450001210	终端详情	1906263415450000700	1	#	\N	\N	B	0	prin-mod:oauth2client-mng:query	#	f	t	system	1	1	2024-05-26 12:30:16+08	2025-09-03 12:42:06.554+08
1906263415450001125	钱包记录	1906263415450000601	3	#	\N	\N	B	0	prin-mod:tenant-wallet-mng:list	#	f	t	system	1	1	2024-05-26 12:30:16+08	2025-09-03 16:17:56.038+08
1976220001646616577	新增租户套餐	1906263415450000602	1	#	\N	\N	B	0	prin-mod:tenantpackage-mng:add	#	f	t	system	1	1	2025-10-09 17:35:39.449+08	2025-10-09 17:35:39.449+08
1976220107171110913	修改租户套餐	1906263415450000602	3	#	\N	\N	B	0	prin-mod:tenantpackage-mng:edit	#	f	t	system	1	1	2025-10-09 17:36:04.605+08	2025-10-09 17:36:04.605+08
1976220234040418306	删除租户套餐	1906263415450000602	0	#	\N	\N	B	0	prin-mod:tenantpackage-mng:remove	#	f	t	system	1	1	2025-10-09 17:36:34.853+08	2025-10-09 17:36:34.853+08
1915322746249367554	修改信息	1906272182215585793	0	#	\N	\N	B	0	prin-mod:tenant-info:edit	#	f	t	tenant	1	1	2025-04-24 16:31:42.342+08	2025-10-09 17:38:05.31+08
1906263415450001127	商户钱包	1906272182215585793	0	#	\N	\N	M	0	prin-mod:tenant-wallet-info	carbon:wallet	f	t	tenant	1	1	2024-05-26 12:30:16+08	2025-10-09 17:38:18.649+08
1906263415450001126	商户提现	1906263415450001127	1	#	\N	\N	B	0	prin-mod:tenant-wallet-info:withdrawal	#	f	t	tenant	1	1	2024-05-26 12:30:16+08	2025-10-09 17:38:23.095+08
1976223757310291969	新增角色	1906263415450000206	0	#	\N	\N	B	0	prin-mod:role-mng:add	#	f	t	system	1	1	2025-10-09 17:50:34.861+08	2025-10-09 17:50:34.861+08
1906263415450002016	删除菜单	1906263415450000207	4	#	\N	\N	B	0	prin-mod:menu-mng:remove	#	f	t	system	1	1	2024-05-26 12:30:16+08	2025-10-09 17:59:19.71+08
1906263415450001133	修改租户	1906263415450000601	2	#	\N	\N	B	0	prin-mod:tenant-mng:edit	#	f	t	system	1	1	2024-05-26 12:30:16+08	2025-10-09 18:00:31.48+08
1976586292211408897	删除职位	1976585906620653569	5	#	\N	\N	B	0	prin-mod:post-mng:remove	#	f	t	tenant	1	1	2025-10-10 17:51:09.924+08	2025-10-10 17:51:09.924+08
1976586196090544129	修改职位	1976585906620653569	3	#	\N	\N	B	0	prin-mod:post-mng:edit	#	f	t	tenant	1	1	2025-10-10 17:50:47.01+08	2025-10-10 17:51:16.27+08
1906272182215585793	租户信息	0	100	tenant-info	tenant/info/index	\N	M	0	prin-mod:tenant-info	carbon:information-filled	f	t	tenant	1	1	2025-03-30 17:07:59.723+08	2025-10-22 15:11:16.513+08
1906263415450000201	顾客管理	1906263415450000003	1	customer	customer/mng/index	\N	M	0	prin-mod:customer-mng:list	carbon:customer	f	t	system	1	1	2024-05-26 12:30:16+08	2025-09-03 17:29:53.092+08
1906263415450000601	租户管理	1906263415450000003	100	tenant	tenant/mng/index	\N	M	0	prin-mod:tenant-mng:list	carbon:id-management	f	t	system	1	1	2024-05-26 12:30:16+08	2025-09-03 17:29:30.704+08
1906263415450000602	租户套餐管理	1906263415450000003	50	tenant-package	tenant-package/mng/index	\N	M	0	prin-mod:tenantpackage-mng:list	carbon:package	f	t	system	1	1	2024-05-26 12:30:16+08	2025-09-03 17:29:37.386+08
1906263415450000206	角色管理	1906263415450000002	99	role	role/mng/index	\N	M	0	prin-mod:role-mng:list	carbon:user-role	f	t	system	1	1	2024-05-26 12:30:16+08	2025-10-11 11:27:10.108+08
1906263415450000205	部门管理	1906263415450000002	70	dept	dept/mng/index	\N	M	0	prin-mod:dept-mng:list	carbon:departure	f	t	system	1	1	2024-05-26 12:30:16+08	2025-10-11 11:27:19.213+08
1906263415450000150	操作日志	1906263415450000104	1	operate	operatelog-admin/mng/index	\N	M	0	msg-mod:admin-operatelog-mng:list	carbon:touch-interaction	f	t	system	1	100	2024-05-26 12:30:16+08	2025-11-07 15:01:00.731+08
1906263415450002062	重置密码	1906263415450000203	7	#	\N	\N	B	0	prin-mod:admin-mng:resetPwd	#	f	t	system	1	100	2024-05-26 12:30:16+08	2025-11-07 13:57:48.304+08
1976586772002037762	删除	1976570103963770881	7	#	\N	\N	B	0	prin-mod:member-mng:remove	#	f	t	tenant	1	100	2025-10-10 17:53:04.318+08	2025-11-10 11:06:54.2+08
1976586698882736130	授权职位	1976570103963770881	5	#	\N	\N	B	0	prin-mod:member-mng:grant-post	#	f	t	tenant	1	100	2025-10-10 17:52:46.886+08	2025-11-10 11:07:03.634+08
1976586612681400321	修改	1976570103963770881	3	#	\N	\N	B	0	prin-mod:member-mng:edit	#	f	t	tenant	1	100	2025-10-10 17:52:26.329+08	2025-11-10 11:07:13.481+08
1976586554187636737	新增	1976570103963770881	0	#	\N	\N	B	0	prin-mod:member-mng:add	#	f	t	tenant	1	100	2025-10-10 17:52:12.388+08	2025-11-10 11:07:25.231+08
1906263415450000203	账号管理	1906263415450000002	100	admin	admin/mng/index	\N	M	0	prin-mod:admin-mng:list	carbon:user-admin	t	t	system	1	100	2024-05-26 12:30:16+08	2025-11-07 14:01:24.632+08
1906263415450002064	修改账号	1906263415450000203	3	#	\N	\N	B	0	prin-mod:admin-mng:edit	#	f	t	system	1	100	2024-05-26 12:30:16+08	2025-11-07 14:03:08.592+08
1906263415450002077	导出	1906263415450000203	1	#	\N	\N	B	0	prin-mod:admin-mng:export	#	f	t	system	1	100	2024-05-26 12:30:16+08	2025-11-07 14:03:17.222+08
1906263415450002071	查询账号	1906263415450000203	0	#	\N	\N	B	0	prin-mod:admin-mng:query	#	f	t	system	1	100	2024-05-26 12:30:16+08	2025-11-07 14:03:32.812+08
1906263415450002072	新增账号	1906263415450000203	1	#	\N	\N	B	0	prin-mod:admin-mng:add	#	f	t	system	1	100	2024-05-26 12:30:16+08	2025-11-07 14:04:07.749+08
\.


--
-- TOC entry 3478 (class 0 OID 16872)
-- Dependencies: 221
-- Data for Name: oauth2_client; Type: TABLE DATA; Schema: biz; Owner: postgres
--

COPY biz.oauth2_client (id, client_name, client_id, client_secret, scopes, authorization_grant_types, redirect_uris, status, auto_approve, create_at, creator_id, update_at, updater_id) FROM stdin;
2	服务监控	server_monitor	{bcrypt}$2a$10$hK9Sv9kAvXE00fWtkWxzI.Ns4.5SuQteTJAnsFWXChlOWIUZSFYL2	{monitor}	{client_credentials}	{}	0	t	2025-05-21 14:13:48.523+08	1	2025-09-01 15:53:53.377+08	1
1	系统	server	{bcrypt}$2a$10$hK9Sv9kAvXE00fWtkWxzI.Ns4.5SuQteTJAnsFWXChlOWIUZSFYL2	{openid,read}	{authorization_code,urn:ietf:params:oauth:grant-type:device_code,refresh_token}	{http://localhost:9000/oauth2/authorization_code_callback}	0	f	2024-04-17 14:08:54+08	1	2025-09-03 11:28:18.451+08	1
\.


--
-- TOC entry 3479 (class 0 OID 16880)
-- Dependencies: 222
-- Data for Name: role_info; Type: TABLE DATA; Schema: biz; Owner: root
--

COPY biz.role_info (role_id, data_scope, role_name, status, inherited, role_sort, create_at, creator_id, update_at, updater_id) FROM stdin;
\.


--
-- TOC entry 3480 (class 0 OID 16884)
-- Dependencies: 223
-- Data for Name: role_to_dept; Type: TABLE DATA; Schema: biz; Owner: postgres
--

COPY biz.role_to_dept (role_id, dept_id) FROM stdin;
\.


--
-- TOC entry 3481 (class 0 OID 16887)
-- Dependencies: 224
-- Data for Name: role_to_hierarchy; Type: TABLE DATA; Schema: biz; Owner: root
--

COPY biz.role_to_hierarchy (role_id, inherited_id) FROM stdin;
\.


--
-- TOC entry 3482 (class 0 OID 16890)
-- Dependencies: 225
-- Data for Name: role_to_menu; Type: TABLE DATA; Schema: biz; Owner: postgres
--

COPY biz.role_to_menu (role_id, menu_id) FROM stdin;
\.


--
-- TOC entry 3485 (class 0 OID 16899)
-- Dependencies: 228
-- Data for Name: t_member_info; Type: TABLE DATA; Schema: biz; Owner: postgres
--

COPY biz.t_member_info (member_id, tenant_id, username, phone_number, status, gender, avatar, password, login_ip, login_date, remark, creator_id, updater_id, create_at, update_at) FROM stdin;
1910557183820165120	1910557183820165122	testadmin	\N	0	2	\N	{bcrypt}$2a$10$7JB720yubVSZvUI0rEqK/.VqGOZTH.ulu33dHOiBE8ByOhJIrdAu2	172.16.8.131	2025-11-10 15:24:46+08	\N	1	\N	2025-04-11 12:55:03.816+08	\N
1987788971839262722	1910557183820165122	111111	\N	0	2	\N	{bcrypt}$2a$10$V4O.Wuo2Fo4lhtXyqSrWSu/TCuk6ACD9sVKAGRUWmCGJ2.UrXfb3W	\N	\N	\N	1910557183820165120	1910557183820165120	2025-11-10 15:46:36.876+08	2025-11-10 15:46:36.876+08
\.


--
-- TOC entry 3486 (class 0 OID 16906)
-- Dependencies: 229
-- Data for Name: t_member_to_post; Type: TABLE DATA; Schema: biz; Owner: postgres
--

COPY biz.t_member_to_post (member_id, post_id) FROM stdin;
\.


--
-- TOC entry 3483 (class 0 OID 16893)
-- Dependencies: 226
-- Data for Name: t_post_info; Type: TABLE DATA; Schema: biz; Owner: root
--

COPY biz.t_post_info (post_id, tenant_id, post_name, status, post_sort, create_at, creator_id, update_at, updater_id) FROM stdin;
1978377271113371649	1910557183820165122	CEO	0	0	2025-10-15 16:27:52.562+08	1910557183820165120	2025-10-15 16:27:52.562+08	1910557183820165120
1978377302486765569	1910557183820165122	CFO	0	0	2025-10-15 16:28:00.049+08	1910557183820165120	2025-10-15 16:44:50.974+08	1910557183820165120
\.


--
-- TOC entry 3484 (class 0 OID 16896)
-- Dependencies: 227
-- Data for Name: t_post_to_menu; Type: TABLE DATA; Schema: biz; Owner: postgres
--

COPY biz.t_post_to_menu (post_id, menu_id) FROM stdin;
1978377271113371649	1906272182215585793
1978377271113371649	1915322746249367554
1978377271113371649	1906263415450001127
1978377271113371649	1906263415450001126
1978377302486765569	1976565556872667137
1978377302486765569	1976570103963770881
1978377302486765569	1976586772002037762
1978377302486765569	1976586698882736130
1978377302486765569	1976586612681400321
1978377302486765569	1976586554187636737
\.


--
-- TOC entry 3487 (class 0 OID 16909)
-- Dependencies: 230
-- Data for Name: tenant_info; Type: TABLE DATA; Schema: biz; Owner: postgres
--

COPY biz.tenant_info (tenant_id, administrator, tenant_type, contact_phone, tenant_name, oper_pwd, status, domain, remark, package_id, expire_time, account_limit, post_limit, creator_id, create_at, updater_id, update_at) FROM stdin;
1910557183820165122	1910557183820165120	0	\N	test1	{bcrypt}$2a$10$1UJgROjrOvMKJD4way7dKeBsJuLGVLWGy/pBGooa.sFqfsP3Vrupm	0	\N	\N	1773625804122202113	2025-11-11 00:00:00+08	5	5	1	2025-04-11 12:55:03.715+08	100	2025-11-10 09:35:50.537+08
\.


--
-- TOC entry 3488 (class 0 OID 16912)
-- Dependencies: 231
-- Data for Name: tenant_package_info; Type: TABLE DATA; Schema: biz; Owner: postgres
--

COPY biz.tenant_package_info (package_id, package_name, status, menu_ids, remark, creator_id, create_at, updater_id, update_at) FROM stdin;
1773625804122202113	默认套餐	0	{1906272182215585793,1915322746249367554,1906263415450001127,1906263415450001126,1976565556872667137,1976585906620653569,1976586292211408897,1976586196090544129,1976586082013863937,1976570103963770881,1976586772002037762,1976586698882736130,1976586612681400321,1976586554187636737,1980906033277222913,1980906374936838146,1980906706949554177}	通用租户套餐	1	2024-04-17 14:08:54+08	1	2025-10-22 15:59:39.192+08
\.


--
-- TOC entry 3489 (class 0 OID 16918)
-- Dependencies: 232
-- Data for Name: tenant_wallet_info; Type: TABLE DATA; Schema: biz; Owner: postgres
--

COPY biz.tenant_wallet_info (tenant_id, balance, status) FROM stdin;
1910557183820165122	0.00	0
\.


--
-- TOC entry 3490 (class 0 OID 16921)
-- Dependencies: 233
-- Data for Name: tenant_wallet_record; Type: TABLE DATA; Schema: biz; Owner: postgres
--

COPY biz.tenant_wallet_record (record_id, tenant_id, amount, record_type, biz_type, biz_no, create_at, remark) FROM stdin;
\.


--
-- TOC entry 3491 (class 0 OID 16924)
-- Dependencies: 234
-- Data for Name: tenant_wallet_withdrawal_record; Type: TABLE DATA; Schema: biz; Owner: postgres
--

COPY biz.tenant_wallet_withdrawal_record (withdrawal_id, order_no, status, tenant_id, request_params, amount, error_msg, creator_id, create_at, complete_at, remark) FROM stdin;
\.


--
-- TOC entry 3325 (class 2606 OID 16980)
-- Name: admin_info admin_info_pkey; Type: CONSTRAINT; Schema: biz; Owner: postgres
--

ALTER TABLE ONLY biz.admin_info
    ADD CONSTRAINT admin_info_pkey PRIMARY KEY (admin_id);


--
-- TOC entry 3329 (class 2606 OID 16982)
-- Name: admin_to_role admin_to_role_pkey; Type: CONSTRAINT; Schema: biz; Owner: postgres
--

ALTER TABLE ONLY biz.admin_to_role
    ADD CONSTRAINT admin_to_role_pkey PRIMARY KEY (admin_id, role_id);


--
-- TOC entry 3273 (class 2606 OID 16940)
-- Name: customer_info customer_info_pkey; Type: CONSTRAINT; Schema: biz; Owner: postgres
--

ALTER TABLE ONLY biz.customer_info
    ADD CONSTRAINT customer_info_pkey PRIMARY KEY (customer_id);


--
-- TOC entry 3276 (class 2606 OID 16942)
-- Name: customer_social_info customer_social_info_pkey; Type: CONSTRAINT; Schema: biz; Owner: postgres
--

ALTER TABLE ONLY biz.customer_social_info
    ADD CONSTRAINT customer_social_info_pkey PRIMARY KEY (customer_id);


--
-- TOC entry 3279 (class 2606 OID 16944)
-- Name: customer_wallet_info customer_wallet_info_pkey; Type: CONSTRAINT; Schema: biz; Owner: postgres
--

ALTER TABLE ONLY biz.customer_wallet_info
    ADD CONSTRAINT customer_wallet_info_pkey PRIMARY KEY (customer_id);


--
-- TOC entry 3281 (class 2606 OID 16946)
-- Name: customer_wallet_record customer_wallet_record_pkey; Type: CONSTRAINT; Schema: biz; Owner: postgres
--

ALTER TABLE ONLY biz.customer_wallet_record
    ADD CONSTRAINT customer_wallet_record_pkey PRIMARY KEY (record_id);


--
-- TOC entry 3284 (class 2606 OID 16948)
-- Name: dept_info dept_info_pkey; Type: CONSTRAINT; Schema: biz; Owner: postgres
--

ALTER TABLE ONLY biz.dept_info
    ADD CONSTRAINT dept_info_pkey PRIMARY KEY (dept_id);


--
-- TOC entry 3289 (class 2606 OID 16950)
-- Name: menu_info menu_info_pkey; Type: CONSTRAINT; Schema: biz; Owner: postgres
--

ALTER TABLE ONLY biz.menu_info
    ADD CONSTRAINT menu_info_pkey PRIMARY KEY (menu_id);


--
-- TOC entry 3291 (class 2606 OID 16952)
-- Name: oauth2_client oauth2_client_pkey; Type: CONSTRAINT; Schema: biz; Owner: postgres
--

ALTER TABLE ONLY biz.oauth2_client
    ADD CONSTRAINT oauth2_client_pkey PRIMARY KEY (id);


--
-- TOC entry 3294 (class 2606 OID 16954)
-- Name: role_info role_info_pkey; Type: CONSTRAINT; Schema: biz; Owner: root
--

ALTER TABLE ONLY biz.role_info
    ADD CONSTRAINT role_info_pkey PRIMARY KEY (role_id);


--
-- TOC entry 3296 (class 2606 OID 16956)
-- Name: role_to_dept role_to_dept_pkey; Type: CONSTRAINT; Schema: biz; Owner: postgres
--

ALTER TABLE ONLY biz.role_to_dept
    ADD CONSTRAINT role_to_dept_pkey PRIMARY KEY (role_id, dept_id);


--
-- TOC entry 3298 (class 2606 OID 16958)
-- Name: role_to_hierarchy role_to_hierarchy_pkey; Type: CONSTRAINT; Schema: biz; Owner: root
--

ALTER TABLE ONLY biz.role_to_hierarchy
    ADD CONSTRAINT role_to_hierarchy_pkey PRIMARY KEY (role_id, inherited_id);


--
-- TOC entry 3300 (class 2606 OID 16960)
-- Name: role_to_menu role_to_menu_pkey; Type: CONSTRAINT; Schema: biz; Owner: postgres
--

ALTER TABLE ONLY biz.role_to_menu
    ADD CONSTRAINT role_to_menu_pkey PRIMARY KEY (role_id, menu_id);


--
-- TOC entry 3306 (class 2606 OID 16966)
-- Name: t_member_info t_member_info_pkey; Type: CONSTRAINT; Schema: biz; Owner: postgres
--

ALTER TABLE ONLY biz.t_member_info
    ADD CONSTRAINT t_member_info_pkey PRIMARY KEY (member_id);


--
-- TOC entry 3310 (class 2606 OID 16968)
-- Name: t_member_to_post t_member_to_post_pkey; Type: CONSTRAINT; Schema: biz; Owner: postgres
--

ALTER TABLE ONLY biz.t_member_to_post
    ADD CONSTRAINT t_member_to_post_pkey PRIMARY KEY (member_id, post_id);


--
-- TOC entry 3302 (class 2606 OID 16962)
-- Name: t_post_info t_post_info_pkey; Type: CONSTRAINT; Schema: biz; Owner: root
--

ALTER TABLE ONLY biz.t_post_info
    ADD CONSTRAINT t_post_info_pkey PRIMARY KEY (post_id);


--
-- TOC entry 3304 (class 2606 OID 16964)
-- Name: t_post_to_menu t_post_to_menu_pkey; Type: CONSTRAINT; Schema: biz; Owner: postgres
--

ALTER TABLE ONLY biz.t_post_to_menu
    ADD CONSTRAINT t_post_to_menu_pkey PRIMARY KEY (post_id, menu_id);


--
-- TOC entry 3312 (class 2606 OID 16970)
-- Name: tenant_info tenant_info_pkey; Type: CONSTRAINT; Schema: biz; Owner: postgres
--

ALTER TABLE ONLY biz.tenant_info
    ADD CONSTRAINT tenant_info_pkey PRIMARY KEY (tenant_id);


--
-- TOC entry 3315 (class 2606 OID 16972)
-- Name: tenant_package_info tenant_package_info_pkey; Type: CONSTRAINT; Schema: biz; Owner: postgres
--

ALTER TABLE ONLY biz.tenant_package_info
    ADD CONSTRAINT tenant_package_info_pkey PRIMARY KEY (package_id);


--
-- TOC entry 3317 (class 2606 OID 16974)
-- Name: tenant_wallet_info tenant_wallet_info_pkey; Type: CONSTRAINT; Schema: biz; Owner: postgres
--

ALTER TABLE ONLY biz.tenant_wallet_info
    ADD CONSTRAINT tenant_wallet_info_pkey PRIMARY KEY (tenant_id);


--
-- TOC entry 3320 (class 2606 OID 16976)
-- Name: tenant_wallet_record tenant_wallet_record_pkey; Type: CONSTRAINT; Schema: biz; Owner: postgres
--

ALTER TABLE ONLY biz.tenant_wallet_record
    ADD CONSTRAINT tenant_wallet_record_pkey PRIMARY KEY (record_id);


--
-- TOC entry 3323 (class 2606 OID 16978)
-- Name: tenant_wallet_withdrawal_record tenant_wallet_withdrawal_record_pkey; Type: CONSTRAINT; Schema: biz; Owner: postgres
--

ALTER TABLE ONLY biz.tenant_wallet_withdrawal_record
    ADD CONSTRAINT tenant_wallet_withdrawal_record_pkey PRIMARY KEY (withdrawal_id);


--
-- TOC entry 3274 (class 1259 OID 16983)
-- Name: idx_customer_info_phone_number; Type: INDEX; Schema: biz; Owner: postgres
--

CREATE UNIQUE INDEX idx_customer_info_phone_number ON biz.customer_info USING btree (phone_number);


--
-- TOC entry 3282 (class 1259 OID 16984)
-- Name: idx_customer_wallet_record_customer_id; Type: INDEX; Schema: biz; Owner: postgres
--

CREATE INDEX idx_customer_wallet_record_customer_id ON biz.customer_wallet_record USING btree (customer_id);


--
-- TOC entry 3285 (class 1259 OID 16985)
-- Name: idx_dept_info_ancestors; Type: INDEX; Schema: biz; Owner: postgres
--

CREATE INDEX idx_dept_info_ancestors ON biz.dept_info USING btree (ancestors);


--
-- TOC entry 3286 (class 1259 OID 16986)
-- Name: idx_dept_info_parent_id; Type: INDEX; Schema: biz; Owner: postgres
--

CREATE INDEX idx_dept_info_parent_id ON biz.dept_info USING btree (parent_id);


--
-- TOC entry 3287 (class 1259 OID 16987)
-- Name: idx_menu_info_parent_id; Type: INDEX; Schema: biz; Owner: postgres
--

CREATE INDEX idx_menu_info_parent_id ON biz.menu_info USING btree (parent_id);


--
-- TOC entry 3318 (class 1259 OID 16988)
-- Name: idx_tenant_wallet_record_tenant_id; Type: INDEX; Schema: biz; Owner: postgres
--

CREATE INDEX idx_tenant_wallet_record_tenant_id ON biz.tenant_wallet_record USING btree (tenant_id);


--
-- TOC entry 3321 (class 1259 OID 16989)
-- Name: idx_tenant_wallet_withdrawal_record_order_no; Type: INDEX; Schema: biz; Owner: postgres
--

CREATE UNIQUE INDEX idx_tenant_wallet_withdrawal_record_order_no ON biz.tenant_wallet_withdrawal_record USING btree (order_no);


--
-- TOC entry 3277 (class 1259 OID 16990)
-- Name: uk_customer_social_info_identifier; Type: INDEX; Schema: biz; Owner: postgres
--

CREATE UNIQUE INDEX uk_customer_social_info_identifier ON biz.customer_social_info USING btree (identifier);


--
-- TOC entry 3292 (class 1259 OID 16991)
-- Name: uk_oauth2_client_client_id; Type: INDEX; Schema: biz; Owner: postgres
--

CREATE UNIQUE INDEX uk_oauth2_client_client_id ON biz.oauth2_client USING btree (client_id);


--
-- TOC entry 3313 (class 1259 OID 16992)
-- Name: uk_tenant_info_administrator; Type: INDEX; Schema: biz; Owner: postgres
--

CREATE UNIQUE INDEX uk_tenant_info_administrator ON biz.tenant_info USING btree (administrator);


--
-- TOC entry 3307 (class 1259 OID 16993)
-- Name: uk_tenant_staff_info_phone_number; Type: INDEX; Schema: biz; Owner: postgres
--

CREATE UNIQUE INDEX uk_tenant_staff_info_phone_number ON biz.t_member_info USING btree (phone_number);


--
-- TOC entry 3308 (class 1259 OID 16994)
-- Name: uk_tenant_staff_info_username; Type: INDEX; Schema: biz; Owner: postgres
--

CREATE UNIQUE INDEX uk_tenant_staff_info_username ON biz.t_member_info USING btree (username);


--
-- TOC entry 3326 (class 1259 OID 16995)
-- Name: uk_user_info_phone_number; Type: INDEX; Schema: biz; Owner: postgres
--

CREATE UNIQUE INDEX uk_user_info_phone_number ON biz.admin_info USING btree (phone_number);


--
-- TOC entry 3327 (class 1259 OID 16996)
-- Name: uk_user_info_username; Type: INDEX; Schema: biz; Owner: postgres
--

CREATE UNIQUE INDEX uk_user_info_username ON biz.admin_info USING btree (username);


-- Completed on 2025-11-11 18:06:56

--
-- PostgreSQL database dump complete
--

