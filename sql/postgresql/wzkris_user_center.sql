--
-- PostgreSQL database dump
--

-- Dumped from database version 15.13
-- Dumped by pg_dump version 15.13

-- Started on 2026-04-14 14:09:27

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
-- TOC entry 6 (class 2615 OID 25948)
-- Name: biz; Type: SCHEMA; Schema: -; Owner: postgres
--

CREATE SCHEMA biz;


ALTER SCHEMA biz OWNER TO postgres;

--
-- TOC entry 3519 (class 0 OID 0)
-- Dependencies: 6
-- Name: SCHEMA biz; Type: COMMENT; Schema: -; Owner: postgres
--

COMMENT ON SCHEMA biz IS 'b端';


SET default_tablespace = '';

SET default_table_access_method = heap;

--
-- TOC entry 215 (class 1259 OID 25949)
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
    login_date timestamp(0) with time zone,
    remark character varying(64),
    creator_id bigint NOT NULL,
    updater_id bigint,
    create_at timestamp(0) with time zone NOT NULL,
    update_at timestamp(0) with time zone,
    hint character varying(10) DEFAULT ''::character varying NOT NULL,
    deleted boolean DEFAULT false NOT NULL
);


ALTER TABLE biz.admin_info OWNER TO postgres;

--
-- TOC entry 3520 (class 0 OID 0)
-- Dependencies: 215
-- Name: TABLE admin_info; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON TABLE biz.admin_info IS '管理员表';


--
-- TOC entry 3521 (class 0 OID 0)
-- Dependencies: 215
-- Name: COLUMN admin_info.admin_id; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.admin_info.admin_id IS '管理员ID';


--
-- TOC entry 3522 (class 0 OID 0)
-- Dependencies: 215
-- Name: COLUMN admin_info.dept_id; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.admin_info.dept_id IS '部门ID';


--
-- TOC entry 3523 (class 0 OID 0)
-- Dependencies: 215
-- Name: COLUMN admin_info.username; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.admin_info.username IS '用户账号';


--
-- TOC entry 3524 (class 0 OID 0)
-- Dependencies: 215
-- Name: COLUMN admin_info.email; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.admin_info.email IS '用户邮箱';


--
-- TOC entry 3525 (class 0 OID 0)
-- Dependencies: 215
-- Name: COLUMN admin_info.nickname; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.admin_info.nickname IS '用户昵称';


--
-- TOC entry 3526 (class 0 OID 0)
-- Dependencies: 215
-- Name: COLUMN admin_info.phone_number; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.admin_info.phone_number IS '手机号码';


--
-- TOC entry 3527 (class 0 OID 0)
-- Dependencies: 215
-- Name: COLUMN admin_info.status; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.admin_info.status IS '状态值';


--
-- TOC entry 3528 (class 0 OID 0)
-- Dependencies: 215
-- Name: COLUMN admin_info.gender; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.admin_info.gender IS '用户性别（0男 1女 2未知）';


--
-- TOC entry 3529 (class 0 OID 0)
-- Dependencies: 215
-- Name: COLUMN admin_info.avatar; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.admin_info.avatar IS '头像地址';


--
-- TOC entry 3530 (class 0 OID 0)
-- Dependencies: 215
-- Name: COLUMN admin_info.password; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.admin_info.password IS '密码';


--
-- TOC entry 3531 (class 0 OID 0)
-- Dependencies: 215
-- Name: COLUMN admin_info.login_ip; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.admin_info.login_ip IS '登录ip';


--
-- TOC entry 3532 (class 0 OID 0)
-- Dependencies: 215
-- Name: COLUMN admin_info.login_date; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.admin_info.login_date IS '登录时间';


--
-- TOC entry 3533 (class 0 OID 0)
-- Dependencies: 215
-- Name: COLUMN admin_info.remark; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.admin_info.remark IS '备注';


--
-- TOC entry 3534 (class 0 OID 0)
-- Dependencies: 215
-- Name: COLUMN admin_info.creator_id; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.admin_info.creator_id IS '创建者';


--
-- TOC entry 3535 (class 0 OID 0)
-- Dependencies: 215
-- Name: COLUMN admin_info.updater_id; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.admin_info.updater_id IS '更新者';


--
-- TOC entry 3536 (class 0 OID 0)
-- Dependencies: 215
-- Name: COLUMN admin_info.hint; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.admin_info.hint IS '标签';


--
-- TOC entry 216 (class 1259 OID 25957)
-- Name: admin_to_role; Type: TABLE; Schema: biz; Owner: postgres
--

CREATE TABLE biz.admin_to_role (
    admin_id bigint NOT NULL,
    role_id bigint NOT NULL
);


ALTER TABLE biz.admin_to_role OWNER TO postgres;

--
-- TOC entry 3537 (class 0 OID 0)
-- Dependencies: 216
-- Name: TABLE admin_to_role; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON TABLE biz.admin_to_role IS '管理员和角色关联表';


--
-- TOC entry 3538 (class 0 OID 0)
-- Dependencies: 216
-- Name: COLUMN admin_to_role.admin_id; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.admin_to_role.admin_id IS '管理员ID';


--
-- TOC entry 3539 (class 0 OID 0)
-- Dependencies: 216
-- Name: COLUMN admin_to_role.role_id; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.admin_to_role.role_id IS '角色ID';


--
-- TOC entry 217 (class 1259 OID 25960)
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
    login_date timestamp(0) with time zone,
    creator_id bigint NOT NULL,
    updater_id bigint,
    create_at timestamp(0) with time zone NOT NULL,
    update_at timestamp(0) with time zone,
    hint character varying(10) DEFAULT ''::character varying NOT NULL,
    deleted boolean DEFAULT false NOT NULL
);


ALTER TABLE biz.customer_info OWNER TO postgres;

--
-- TOC entry 3540 (class 0 OID 0)
-- Dependencies: 217
-- Name: TABLE customer_info; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON TABLE biz.customer_info IS '用户信息表';


--
-- TOC entry 3541 (class 0 OID 0)
-- Dependencies: 217
-- Name: COLUMN customer_info.customer_id; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.customer_info.customer_id IS '用户ID';


--
-- TOC entry 3542 (class 0 OID 0)
-- Dependencies: 217
-- Name: COLUMN customer_info.nickname; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.customer_info.nickname IS '用户昵称';


--
-- TOC entry 3543 (class 0 OID 0)
-- Dependencies: 217
-- Name: COLUMN customer_info.phone_number; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.customer_info.phone_number IS '手机号码';


--
-- TOC entry 3544 (class 0 OID 0)
-- Dependencies: 217
-- Name: COLUMN customer_info.status; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.customer_info.status IS '状态值';


--
-- TOC entry 3545 (class 0 OID 0)
-- Dependencies: 217
-- Name: COLUMN customer_info.gender; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.customer_info.gender IS '用户性别（0男 1女 2未知）';


--
-- TOC entry 3546 (class 0 OID 0)
-- Dependencies: 217
-- Name: COLUMN customer_info.avatar; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.customer_info.avatar IS '头像地址';


--
-- TOC entry 3547 (class 0 OID 0)
-- Dependencies: 217
-- Name: COLUMN customer_info.login_ip; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.customer_info.login_ip IS '登录ip';


--
-- TOC entry 3548 (class 0 OID 0)
-- Dependencies: 217
-- Name: COLUMN customer_info.login_date; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.customer_info.login_date IS '登录时间';


--
-- TOC entry 3549 (class 0 OID 0)
-- Dependencies: 217
-- Name: COLUMN customer_info.creator_id; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.customer_info.creator_id IS '创建者';


--
-- TOC entry 3550 (class 0 OID 0)
-- Dependencies: 217
-- Name: COLUMN customer_info.updater_id; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.customer_info.updater_id IS '更新者';


--
-- TOC entry 3551 (class 0 OID 0)
-- Dependencies: 217
-- Name: COLUMN customer_info.hint; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.customer_info.hint IS '标签';


--
-- TOC entry 218 (class 1259 OID 25968)
-- Name: customer_social_info; Type: TABLE; Schema: biz; Owner: postgres
--

CREATE TABLE biz.customer_social_info (
    customer_id bigint NOT NULL,
    identifier character varying(32) NOT NULL,
    identifier_type character varying(10) NOT NULL,
    creator_id bigint NOT NULL,
    updater_id bigint,
    create_at timestamp(0) with time zone NOT NULL,
    update_at timestamp(0) with time zone,
    hint character varying(10) DEFAULT ''::character varying NOT NULL,
    deleted boolean DEFAULT false NOT NULL
);


ALTER TABLE biz.customer_social_info OWNER TO postgres;

--
-- TOC entry 3552 (class 0 OID 0)
-- Dependencies: 218
-- Name: TABLE customer_social_info; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON TABLE biz.customer_social_info IS '第三方信息';


--
-- TOC entry 3553 (class 0 OID 0)
-- Dependencies: 218
-- Name: COLUMN customer_social_info.identifier; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.customer_social_info.identifier IS '三方唯一标识符';


--
-- TOC entry 3554 (class 0 OID 0)
-- Dependencies: 218
-- Name: COLUMN customer_social_info.identifier_type; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.customer_social_info.identifier_type IS '三方渠道';


--
-- TOC entry 219 (class 1259 OID 25971)
-- Name: customer_wallet_info; Type: TABLE; Schema: biz; Owner: postgres
--

CREATE TABLE biz.customer_wallet_info (
    customer_id bigint NOT NULL,
    balance numeric(10,2) NOT NULL,
    status character(1) NOT NULL,
    creator_id bigint NOT NULL,
    updater_id bigint,
    create_at timestamp(0) with time zone NOT NULL,
    update_at timestamp(0) with time zone,
    hint character varying(10) DEFAULT ''::character varying NOT NULL,
    deleted boolean DEFAULT false NOT NULL
);


ALTER TABLE biz.customer_wallet_info OWNER TO postgres;

--
-- TOC entry 3555 (class 0 OID 0)
-- Dependencies: 219
-- Name: TABLE customer_wallet_info; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON TABLE biz.customer_wallet_info IS '用户钱包';


--
-- TOC entry 3556 (class 0 OID 0)
-- Dependencies: 219
-- Name: COLUMN customer_wallet_info.balance; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.customer_wallet_info.balance IS '余额, 元';


--
-- TOC entry 3557 (class 0 OID 0)
-- Dependencies: 219
-- Name: COLUMN customer_wallet_info.status; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.customer_wallet_info.status IS '状态';


--
-- TOC entry 220 (class 1259 OID 25974)
-- Name: customer_wallet_record; Type: TABLE; Schema: biz; Owner: postgres
--

CREATE TABLE biz.customer_wallet_record (
    record_id bigint NOT NULL,
    customer_id bigint NOT NULL,
    amount numeric(10,2) NOT NULL,
    record_type character(1) NOT NULL,
    create_at timestamp(0) with time zone NOT NULL,
    remark character varying(100),
    creator_id bigint NOT NULL,
    updater_id bigint,
    update_at timestamp(0) with time zone,
    hint character varying(10) DEFAULT ''::character varying NOT NULL,
    deleted boolean DEFAULT false NOT NULL
);


ALTER TABLE biz.customer_wallet_record OWNER TO postgres;

--
-- TOC entry 3558 (class 0 OID 0)
-- Dependencies: 220
-- Name: TABLE customer_wallet_record; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON TABLE biz.customer_wallet_record IS '用户钱包记录';


--
-- TOC entry 3559 (class 0 OID 0)
-- Dependencies: 220
-- Name: COLUMN customer_wallet_record.customer_id; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.customer_wallet_record.customer_id IS '客户ID';


--
-- TOC entry 3560 (class 0 OID 0)
-- Dependencies: 220
-- Name: COLUMN customer_wallet_record.amount; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.customer_wallet_record.amount IS '金额, 元';


--
-- TOC entry 3561 (class 0 OID 0)
-- Dependencies: 220
-- Name: COLUMN customer_wallet_record.record_type; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.customer_wallet_record.record_type IS '记录类型';


--
-- TOC entry 3562 (class 0 OID 0)
-- Dependencies: 220
-- Name: COLUMN customer_wallet_record.create_at; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.customer_wallet_record.create_at IS '创建时间';


--
-- TOC entry 3563 (class 0 OID 0)
-- Dependencies: 220
-- Name: COLUMN customer_wallet_record.remark; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.customer_wallet_record.remark IS '备注';


--
-- TOC entry 221 (class 1259 OID 25977)
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
    create_at timestamp(0) with time zone NOT NULL,
    update_at timestamp(0) with time zone,
    hint character varying(10) DEFAULT ''::character varying NOT NULL,
    deleted boolean DEFAULT false NOT NULL
);


ALTER TABLE biz.dept_info OWNER TO postgres;

--
-- TOC entry 3564 (class 0 OID 0)
-- Dependencies: 221
-- Name: TABLE dept_info; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON TABLE biz.dept_info IS '部门表';


--
-- TOC entry 3565 (class 0 OID 0)
-- Dependencies: 221
-- Name: COLUMN dept_info.dept_id; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.dept_info.dept_id IS '部门id';


--
-- TOC entry 3566 (class 0 OID 0)
-- Dependencies: 221
-- Name: COLUMN dept_info.parent_id; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.dept_info.parent_id IS '父部门id';


--
-- TOC entry 3567 (class 0 OID 0)
-- Dependencies: 221
-- Name: COLUMN dept_info.ancestors; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.dept_info.ancestors IS '祖级列表';


--
-- TOC entry 3568 (class 0 OID 0)
-- Dependencies: 221
-- Name: COLUMN dept_info.dept_name; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.dept_info.dept_name IS '部门名称';


--
-- TOC entry 3569 (class 0 OID 0)
-- Dependencies: 221
-- Name: COLUMN dept_info.status; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.dept_info.status IS '0代表正常 1代表停用';


--
-- TOC entry 3570 (class 0 OID 0)
-- Dependencies: 221
-- Name: COLUMN dept_info.dept_sort; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.dept_info.dept_sort IS '显示顺序';


--
-- TOC entry 3571 (class 0 OID 0)
-- Dependencies: 221
-- Name: COLUMN dept_info.contact; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.dept_info.contact IS '联系电话';


--
-- TOC entry 3572 (class 0 OID 0)
-- Dependencies: 221
-- Name: COLUMN dept_info.email; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.dept_info.email IS '邮箱';


--
-- TOC entry 3573 (class 0 OID 0)
-- Dependencies: 221
-- Name: COLUMN dept_info.creator_id; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.dept_info.creator_id IS '创建者';


--
-- TOC entry 3574 (class 0 OID 0)
-- Dependencies: 221
-- Name: COLUMN dept_info.updater_id; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.dept_info.updater_id IS '更新者';


--
-- TOC entry 3575 (class 0 OID 0)
-- Dependencies: 221
-- Name: COLUMN dept_info.hint; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.dept_info.hint IS '标签';


--
-- TOC entry 222 (class 1259 OID 25985)
-- Name: member_info; Type: TABLE; Schema: biz; Owner: postgres
--

CREATE TABLE biz.member_info (
    member_id bigint NOT NULL,
    tenant_id bigint NOT NULL,
    username character varying(30) NOT NULL,
    phone_number character varying(16),
    status character(1) DEFAULT 0 NOT NULL,
    gender character(1) DEFAULT 2 NOT NULL,
    avatar character varying(150),
    password character varying(100),
    login_ip inet,
    login_date timestamp(0) with time zone,
    remark character varying(64),
    creator_id bigint NOT NULL,
    updater_id bigint,
    create_at timestamp(0) with time zone NOT NULL,
    update_at timestamp(0) with time zone,
    hint character varying(10) DEFAULT ''::character varying NOT NULL,
    deleted boolean DEFAULT false NOT NULL
);


ALTER TABLE biz.member_info OWNER TO postgres;

--
-- TOC entry 3576 (class 0 OID 0)
-- Dependencies: 222
-- Name: TABLE member_info; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON TABLE biz.member_info IS '租户成员表';


--
-- TOC entry 3577 (class 0 OID 0)
-- Dependencies: 222
-- Name: COLUMN member_info.member_id; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.member_info.member_id IS 'ID';


--
-- TOC entry 3578 (class 0 OID 0)
-- Dependencies: 222
-- Name: COLUMN member_info.tenant_id; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.member_info.tenant_id IS '租户ID';


--
-- TOC entry 3579 (class 0 OID 0)
-- Dependencies: 222
-- Name: COLUMN member_info.username; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.member_info.username IS '用户名';


--
-- TOC entry 3580 (class 0 OID 0)
-- Dependencies: 222
-- Name: COLUMN member_info.phone_number; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.member_info.phone_number IS '手机号码';


--
-- TOC entry 3581 (class 0 OID 0)
-- Dependencies: 222
-- Name: COLUMN member_info.status; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.member_info.status IS '状态值';


--
-- TOC entry 3582 (class 0 OID 0)
-- Dependencies: 222
-- Name: COLUMN member_info.gender; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.member_info.gender IS '性别（0男 1女 2未知）';


--
-- TOC entry 3583 (class 0 OID 0)
-- Dependencies: 222
-- Name: COLUMN member_info.avatar; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.member_info.avatar IS '头像地址';


--
-- TOC entry 3584 (class 0 OID 0)
-- Dependencies: 222
-- Name: COLUMN member_info.password; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.member_info.password IS '密码';


--
-- TOC entry 3585 (class 0 OID 0)
-- Dependencies: 222
-- Name: COLUMN member_info.login_ip; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.member_info.login_ip IS '登录ip';


--
-- TOC entry 3586 (class 0 OID 0)
-- Dependencies: 222
-- Name: COLUMN member_info.login_date; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.member_info.login_date IS '登录时间';


--
-- TOC entry 3587 (class 0 OID 0)
-- Dependencies: 222
-- Name: COLUMN member_info.remark; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.member_info.remark IS '备注';


--
-- TOC entry 3588 (class 0 OID 0)
-- Dependencies: 222
-- Name: COLUMN member_info.creator_id; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.member_info.creator_id IS '创建者';


--
-- TOC entry 3589 (class 0 OID 0)
-- Dependencies: 222
-- Name: COLUMN member_info.updater_id; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.member_info.updater_id IS '更新者';


--
-- TOC entry 3590 (class 0 OID 0)
-- Dependencies: 222
-- Name: COLUMN member_info.hint; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.member_info.hint IS '标签';


--
-- TOC entry 223 (class 1259 OID 25993)
-- Name: member_social_info; Type: TABLE; Schema: biz; Owner: postgres
--

CREATE TABLE biz.member_social_info (
    member_id bigint NOT NULL,
    identifier character varying(32) NOT NULL,
    identifier_type character varying(10) NOT NULL,
    creator_id bigint NOT NULL,
    updater_id bigint,
    create_at timestamp(0) with time zone NOT NULL,
    update_at timestamp(0) with time zone,
    hint character varying(10) DEFAULT ''::character varying NOT NULL,
    deleted boolean DEFAULT false NOT NULL
);


ALTER TABLE biz.member_social_info OWNER TO postgres;

--
-- TOC entry 3591 (class 0 OID 0)
-- Dependencies: 223
-- Name: TABLE member_social_info; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON TABLE biz.member_social_info IS '第三方信息';


--
-- TOC entry 3592 (class 0 OID 0)
-- Dependencies: 223
-- Name: COLUMN member_social_info.identifier; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.member_social_info.identifier IS '三方唯一标识符';


--
-- TOC entry 3593 (class 0 OID 0)
-- Dependencies: 223
-- Name: COLUMN member_social_info.identifier_type; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.member_social_info.identifier_type IS '三方渠道';


--
-- TOC entry 224 (class 1259 OID 25996)
-- Name: member_to_post; Type: TABLE; Schema: biz; Owner: postgres
--

CREATE TABLE biz.member_to_post (
    member_id bigint NOT NULL,
    post_id bigint NOT NULL
);


ALTER TABLE biz.member_to_post OWNER TO postgres;

--
-- TOC entry 3594 (class 0 OID 0)
-- Dependencies: 224
-- Name: TABLE member_to_post; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON TABLE biz.member_to_post IS '租户成员和职位关联表';


--
-- TOC entry 3595 (class 0 OID 0)
-- Dependencies: 224
-- Name: COLUMN member_to_post.member_id; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.member_to_post.member_id IS '租户成员ID';


--
-- TOC entry 3596 (class 0 OID 0)
-- Dependencies: 224
-- Name: COLUMN member_to_post.post_id; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.member_to_post.post_id IS '职位ID';


--
-- TOC entry 225 (class 1259 OID 25999)
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
    create_at timestamp(0) with time zone,
    update_at timestamp(0) with time zone,
    hint character varying(10) DEFAULT ''::character varying NOT NULL,
    deleted boolean DEFAULT false NOT NULL
);


ALTER TABLE biz.menu_info OWNER TO postgres;

--
-- TOC entry 3597 (class 0 OID 0)
-- Dependencies: 225
-- Name: TABLE menu_info; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON TABLE biz.menu_info IS '菜单权限表';


--
-- TOC entry 3598 (class 0 OID 0)
-- Dependencies: 225
-- Name: COLUMN menu_info.menu_id; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.menu_info.menu_id IS '菜单ID';


--
-- TOC entry 3599 (class 0 OID 0)
-- Dependencies: 225
-- Name: COLUMN menu_info.menu_name; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.menu_info.menu_name IS '菜单名称';


--
-- TOC entry 3600 (class 0 OID 0)
-- Dependencies: 225
-- Name: COLUMN menu_info.parent_id; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.menu_info.parent_id IS '父菜单ID';


--
-- TOC entry 3601 (class 0 OID 0)
-- Dependencies: 225
-- Name: COLUMN menu_info.menu_sort; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.menu_info.menu_sort IS '显示顺序';


--
-- TOC entry 3602 (class 0 OID 0)
-- Dependencies: 225
-- Name: COLUMN menu_info.path; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.menu_info.path IS '路由地址';


--
-- TOC entry 3603 (class 0 OID 0)
-- Dependencies: 225
-- Name: COLUMN menu_info.component; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.menu_info.component IS '组件路径';


--
-- TOC entry 3604 (class 0 OID 0)
-- Dependencies: 225
-- Name: COLUMN menu_info.query; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.menu_info.query IS '路由参数';


--
-- TOC entry 3605 (class 0 OID 0)
-- Dependencies: 225
-- Name: COLUMN menu_info.menu_type; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.menu_info.menu_type IS '菜单类型（D目录 M菜单 B按钮 I内链 O外链）';


--
-- TOC entry 3606 (class 0 OID 0)
-- Dependencies: 225
-- Name: COLUMN menu_info.status; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.menu_info.status IS '菜单状态（0正常 1停用）';


--
-- TOC entry 3607 (class 0 OID 0)
-- Dependencies: 225
-- Name: COLUMN menu_info.perms; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.menu_info.perms IS '权限标识';


--
-- TOC entry 3608 (class 0 OID 0)
-- Dependencies: 225
-- Name: COLUMN menu_info.icon; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.menu_info.icon IS '菜单图标';


--
-- TOC entry 3609 (class 0 OID 0)
-- Dependencies: 225
-- Name: COLUMN menu_info.cacheable; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.menu_info.cacheable IS '是否缓存';


--
-- TOC entry 3610 (class 0 OID 0)
-- Dependencies: 225
-- Name: COLUMN menu_info.visible; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.menu_info.visible IS '是否显示';


--
-- TOC entry 3611 (class 0 OID 0)
-- Dependencies: 225
-- Name: COLUMN menu_info.scope; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.menu_info.scope IS '菜单域';


--
-- TOC entry 3612 (class 0 OID 0)
-- Dependencies: 225
-- Name: COLUMN menu_info.creator_id; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.menu_info.creator_id IS '创建者ID';


--
-- TOC entry 3613 (class 0 OID 0)
-- Dependencies: 225
-- Name: COLUMN menu_info.hint; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.menu_info.hint IS '标签';


--
-- TOC entry 226 (class 1259 OID 26005)
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
    create_at timestamp(0) with time zone NOT NULL,
    creator_id bigint NOT NULL,
    update_at timestamp(0) with time zone,
    updater_id bigint,
    hint character varying(10) DEFAULT ''::character varying NOT NULL,
    deleted boolean DEFAULT false NOT NULL
);


ALTER TABLE biz.oauth2_client OWNER TO postgres;

--
-- TOC entry 3614 (class 0 OID 0)
-- Dependencies: 226
-- Name: TABLE oauth2_client; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON TABLE biz.oauth2_client IS 'OAUTH2客户端';


--
-- TOC entry 3615 (class 0 OID 0)
-- Dependencies: 226
-- Name: COLUMN oauth2_client.client_name; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.oauth2_client.client_name IS '客户端名称';


--
-- TOC entry 3616 (class 0 OID 0)
-- Dependencies: 226
-- Name: COLUMN oauth2_client.client_id; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.oauth2_client.client_id IS 'APP_ID';


--
-- TOC entry 3617 (class 0 OID 0)
-- Dependencies: 226
-- Name: COLUMN oauth2_client.client_secret; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.oauth2_client.client_secret IS 'APP密钥';


--
-- TOC entry 3618 (class 0 OID 0)
-- Dependencies: 226
-- Name: COLUMN oauth2_client.scopes; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.oauth2_client.scopes IS '权限域';


--
-- TOC entry 3619 (class 0 OID 0)
-- Dependencies: 226
-- Name: COLUMN oauth2_client.authorization_grant_types; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.oauth2_client.authorization_grant_types IS '授权类型';


--
-- TOC entry 3620 (class 0 OID 0)
-- Dependencies: 226
-- Name: COLUMN oauth2_client.redirect_uris; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.oauth2_client.redirect_uris IS '回调地址';


--
-- TOC entry 3621 (class 0 OID 0)
-- Dependencies: 226
-- Name: COLUMN oauth2_client.status; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.oauth2_client.status IS '客户端状态';


--
-- TOC entry 3622 (class 0 OID 0)
-- Dependencies: 226
-- Name: COLUMN oauth2_client.auto_approve; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.oauth2_client.auto_approve IS '是否自动放行';


--
-- TOC entry 3623 (class 0 OID 0)
-- Dependencies: 226
-- Name: COLUMN oauth2_client.hint; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.oauth2_client.hint IS '标签';


--
-- TOC entry 227 (class 1259 OID 26014)
-- Name: post_info; Type: TABLE; Schema: biz; Owner: root
--

CREATE TABLE biz.post_info (
    post_id bigint NOT NULL,
    tenant_id bigint NOT NULL,
    post_name character varying(20) NOT NULL,
    status character(1) NOT NULL,
    post_sort smallint NOT NULL,
    create_at timestamp(0) with time zone NOT NULL,
    creator_id bigint NOT NULL,
    update_at timestamp(0) with time zone,
    updater_id bigint,
    hint character varying(10) DEFAULT ''::character varying NOT NULL,
    deleted boolean DEFAULT false NOT NULL
);


ALTER TABLE biz.post_info OWNER TO root;

--
-- TOC entry 3624 (class 0 OID 0)
-- Dependencies: 227
-- Name: TABLE post_info; Type: COMMENT; Schema: biz; Owner: root
--

COMMENT ON TABLE biz.post_info IS '租户职位信息';


--
-- TOC entry 3625 (class 0 OID 0)
-- Dependencies: 227
-- Name: COLUMN post_info.post_id; Type: COMMENT; Schema: biz; Owner: root
--

COMMENT ON COLUMN biz.post_info.post_id IS '职位ID';


--
-- TOC entry 3626 (class 0 OID 0)
-- Dependencies: 227
-- Name: COLUMN post_info.tenant_id; Type: COMMENT; Schema: biz; Owner: root
--

COMMENT ON COLUMN biz.post_info.tenant_id IS '租户ID';


--
-- TOC entry 3627 (class 0 OID 0)
-- Dependencies: 227
-- Name: COLUMN post_info.post_name; Type: COMMENT; Schema: biz; Owner: root
--

COMMENT ON COLUMN biz.post_info.post_name IS '职位名称';


--
-- TOC entry 3628 (class 0 OID 0)
-- Dependencies: 227
-- Name: COLUMN post_info.status; Type: COMMENT; Schema: biz; Owner: root
--

COMMENT ON COLUMN biz.post_info.status IS '状态（0代表正常 1代表停用）';


--
-- TOC entry 3629 (class 0 OID 0)
-- Dependencies: 227
-- Name: COLUMN post_info.post_sort; Type: COMMENT; Schema: biz; Owner: root
--

COMMENT ON COLUMN biz.post_info.post_sort IS '排序';


--
-- TOC entry 3630 (class 0 OID 0)
-- Dependencies: 227
-- Name: COLUMN post_info.hint; Type: COMMENT; Schema: biz; Owner: root
--

COMMENT ON COLUMN biz.post_info.hint IS '标签';


--
-- TOC entry 228 (class 1259 OID 26018)
-- Name: post_to_menu; Type: TABLE; Schema: biz; Owner: postgres
--

CREATE TABLE biz.post_to_menu (
    post_id bigint NOT NULL,
    menu_id bigint NOT NULL
);


ALTER TABLE biz.post_to_menu OWNER TO postgres;

--
-- TOC entry 3631 (class 0 OID 0)
-- Dependencies: 228
-- Name: TABLE post_to_menu; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON TABLE biz.post_to_menu IS '职位和菜单关联表';


--
-- TOC entry 3632 (class 0 OID 0)
-- Dependencies: 228
-- Name: COLUMN post_to_menu.post_id; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.post_to_menu.post_id IS '职位ID';


--
-- TOC entry 3633 (class 0 OID 0)
-- Dependencies: 228
-- Name: COLUMN post_to_menu.menu_id; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.post_to_menu.menu_id IS '菜单ID';


--
-- TOC entry 229 (class 1259 OID 26021)
-- Name: role_info; Type: TABLE; Schema: biz; Owner: root
--

CREATE TABLE biz.role_info (
    role_id bigint NOT NULL,
    data_scope character(1) NOT NULL,
    role_name character varying(20) NOT NULL,
    status character(1) NOT NULL,
    role_sort smallint NOT NULL,
    create_at timestamp(0) with time zone NOT NULL,
    creator_id bigint NOT NULL,
    update_at timestamp(0) with time zone,
    updater_id bigint,
    hint character varying(10) DEFAULT ''::character varying NOT NULL,
    deleted boolean DEFAULT false NOT NULL
);


ALTER TABLE biz.role_info OWNER TO root;

--
-- TOC entry 3634 (class 0 OID 0)
-- Dependencies: 229
-- Name: COLUMN role_info.role_id; Type: COMMENT; Schema: biz; Owner: root
--

COMMENT ON COLUMN biz.role_info.role_id IS '角色ID';


--
-- TOC entry 3635 (class 0 OID 0)
-- Dependencies: 229
-- Name: COLUMN role_info.data_scope; Type: COMMENT; Schema: biz; Owner: root
--

COMMENT ON COLUMN biz.role_info.data_scope IS '数据范围（1=所有数据权限,2=自定义数据权限,3=本部门数据权限,4=本部门及以下数据权限,5=仅本人数据权限）';


--
-- TOC entry 3636 (class 0 OID 0)
-- Dependencies: 229
-- Name: COLUMN role_info.role_name; Type: COMMENT; Schema: biz; Owner: root
--

COMMENT ON COLUMN biz.role_info.role_name IS '角色名称';


--
-- TOC entry 3637 (class 0 OID 0)
-- Dependencies: 229
-- Name: COLUMN role_info.status; Type: COMMENT; Schema: biz; Owner: root
--

COMMENT ON COLUMN biz.role_info.status IS '状态（0代表正常 1代表停用）';


--
-- TOC entry 3638 (class 0 OID 0)
-- Dependencies: 229
-- Name: COLUMN role_info.role_sort; Type: COMMENT; Schema: biz; Owner: root
--

COMMENT ON COLUMN biz.role_info.role_sort IS '排序';


--
-- TOC entry 3639 (class 0 OID 0)
-- Dependencies: 229
-- Name: COLUMN role_info.hint; Type: COMMENT; Schema: biz; Owner: root
--

COMMENT ON COLUMN biz.role_info.hint IS '标签';


--
-- TOC entry 230 (class 1259 OID 26025)
-- Name: role_inheritance; Type: TABLE; Schema: biz; Owner: postgres
--

CREATE TABLE biz.role_inheritance (
    role_id bigint NOT NULL,
    child_id bigint NOT NULL
);


ALTER TABLE biz.role_inheritance OWNER TO postgres;

--
-- TOC entry 3640 (class 0 OID 0)
-- Dependencies: 230
-- Name: TABLE role_inheritance; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON TABLE biz.role_inheritance IS '角色继承表';


--
-- TOC entry 3641 (class 0 OID 0)
-- Dependencies: 230
-- Name: COLUMN role_inheritance.role_id; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.role_inheritance.role_id IS '父ID';


--
-- TOC entry 3642 (class 0 OID 0)
-- Dependencies: 230
-- Name: COLUMN role_inheritance.child_id; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.role_inheritance.child_id IS '子ID';


--
-- TOC entry 231 (class 1259 OID 26028)
-- Name: role_to_dept; Type: TABLE; Schema: biz; Owner: postgres
--

CREATE TABLE biz.role_to_dept (
    role_id bigint NOT NULL,
    dept_id bigint NOT NULL
);


ALTER TABLE biz.role_to_dept OWNER TO postgres;

--
-- TOC entry 3643 (class 0 OID 0)
-- Dependencies: 231
-- Name: TABLE role_to_dept; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON TABLE biz.role_to_dept IS '角色数据权限关联表';


--
-- TOC entry 3644 (class 0 OID 0)
-- Dependencies: 231
-- Name: COLUMN role_to_dept.role_id; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.role_to_dept.role_id IS '角色id';


--
-- TOC entry 3645 (class 0 OID 0)
-- Dependencies: 231
-- Name: COLUMN role_to_dept.dept_id; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.role_to_dept.dept_id IS '部门id';


--
-- TOC entry 232 (class 1259 OID 26031)
-- Name: role_to_menu; Type: TABLE; Schema: biz; Owner: postgres
--

CREATE TABLE biz.role_to_menu (
    role_id bigint NOT NULL,
    menu_id bigint NOT NULL
);


ALTER TABLE biz.role_to_menu OWNER TO postgres;

--
-- TOC entry 3646 (class 0 OID 0)
-- Dependencies: 232
-- Name: TABLE role_to_menu; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON TABLE biz.role_to_menu IS '角色和菜单关联表';


--
-- TOC entry 3647 (class 0 OID 0)
-- Dependencies: 232
-- Name: COLUMN role_to_menu.role_id; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.role_to_menu.role_id IS '角色ID';


--
-- TOC entry 3648 (class 0 OID 0)
-- Dependencies: 232
-- Name: COLUMN role_to_menu.menu_id; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.role_to_menu.menu_id IS '菜单ID';


--
-- TOC entry 233 (class 1259 OID 26034)
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
    expire_time timestamp(0) with time zone NOT NULL,
    creator_id bigint NOT NULL,
    create_at timestamp(0) with time zone NOT NULL,
    updater_id bigint,
    update_at timestamp(0) with time zone,
    hint character varying(10) DEFAULT ''::character varying NOT NULL,
    deleted boolean DEFAULT false NOT NULL
);


ALTER TABLE biz.tenant_info OWNER TO postgres;

--
-- TOC entry 3649 (class 0 OID 0)
-- Dependencies: 233
-- Name: TABLE tenant_info; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON TABLE biz.tenant_info IS '租户表';


--
-- TOC entry 3650 (class 0 OID 0)
-- Dependencies: 233
-- Name: COLUMN tenant_info.tenant_id; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.tenant_info.tenant_id IS '租户编号';


--
-- TOC entry 3651 (class 0 OID 0)
-- Dependencies: 233
-- Name: COLUMN tenant_info.administrator; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.tenant_info.administrator IS '管理员ID';


--
-- TOC entry 3652 (class 0 OID 0)
-- Dependencies: 233
-- Name: COLUMN tenant_info.tenant_type; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.tenant_info.tenant_type IS '租户类型';


--
-- TOC entry 3653 (class 0 OID 0)
-- Dependencies: 233
-- Name: COLUMN tenant_info.contact_phone; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.tenant_info.contact_phone IS '联系电话';


--
-- TOC entry 3654 (class 0 OID 0)
-- Dependencies: 233
-- Name: COLUMN tenant_info.tenant_name; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.tenant_info.tenant_name IS '租户名称';


--
-- TOC entry 3655 (class 0 OID 0)
-- Dependencies: 233
-- Name: COLUMN tenant_info.oper_pwd; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.tenant_info.oper_pwd IS '操作密码';


--
-- TOC entry 3656 (class 0 OID 0)
-- Dependencies: 233
-- Name: COLUMN tenant_info.status; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.tenant_info.status IS '租户状态';


--
-- TOC entry 3657 (class 0 OID 0)
-- Dependencies: 233
-- Name: COLUMN tenant_info.domain; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.tenant_info.domain IS '域名';


--
-- TOC entry 3658 (class 0 OID 0)
-- Dependencies: 233
-- Name: COLUMN tenant_info.remark; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.tenant_info.remark IS '备注';


--
-- TOC entry 3659 (class 0 OID 0)
-- Dependencies: 233
-- Name: COLUMN tenant_info.package_id; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.tenant_info.package_id IS '租户套餐编号';


--
-- TOC entry 3660 (class 0 OID 0)
-- Dependencies: 233
-- Name: COLUMN tenant_info.expire_time; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.tenant_info.expire_time IS '过期时间';


--
-- TOC entry 3661 (class 0 OID 0)
-- Dependencies: 233
-- Name: COLUMN tenant_info.creator_id; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.tenant_info.creator_id IS '创建者';


--
-- TOC entry 3662 (class 0 OID 0)
-- Dependencies: 233
-- Name: COLUMN tenant_info.create_at; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.tenant_info.create_at IS '创建时间';


--
-- TOC entry 3663 (class 0 OID 0)
-- Dependencies: 233
-- Name: COLUMN tenant_info.updater_id; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.tenant_info.updater_id IS '更新者';


--
-- TOC entry 3664 (class 0 OID 0)
-- Dependencies: 233
-- Name: COLUMN tenant_info.update_at; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.tenant_info.update_at IS '更新时间';


--
-- TOC entry 3665 (class 0 OID 0)
-- Dependencies: 233
-- Name: COLUMN tenant_info.hint; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.tenant_info.hint IS '标签';


--
-- TOC entry 234 (class 1259 OID 26038)
-- Name: tenant_package_info; Type: TABLE; Schema: biz; Owner: postgres
--

CREATE TABLE biz.tenant_package_info (
    package_id bigint NOT NULL,
    package_name character varying(20) NOT NULL,
    status character(1) NOT NULL,
    menu_ids bigint[] DEFAULT '{}'::bigint[] NOT NULL,
    remark character varying(200),
    creator_id bigint NOT NULL,
    create_at timestamp(0) with time zone NOT NULL,
    updater_id bigint,
    update_at timestamp(0) with time zone,
    hint character varying(10) DEFAULT ''::character varying NOT NULL,
    member_num_limit smallint DEFAULT 5 NOT NULL,
    post_num_limit smallint DEFAULT 5 NOT NULL,
    deleted boolean DEFAULT false NOT NULL
);


ALTER TABLE biz.tenant_package_info OWNER TO postgres;

--
-- TOC entry 3666 (class 0 OID 0)
-- Dependencies: 234
-- Name: TABLE tenant_package_info; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON TABLE biz.tenant_package_info IS '租户套餐表';


--
-- TOC entry 3667 (class 0 OID 0)
-- Dependencies: 234
-- Name: COLUMN tenant_package_info.package_id; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.tenant_package_info.package_id IS '租户套餐id';


--
-- TOC entry 3668 (class 0 OID 0)
-- Dependencies: 234
-- Name: COLUMN tenant_package_info.package_name; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.tenant_package_info.package_name IS '套餐名称';


--
-- TOC entry 3669 (class 0 OID 0)
-- Dependencies: 234
-- Name: COLUMN tenant_package_info.status; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.tenant_package_info.status IS '状态（0正常 1停用）';


--
-- TOC entry 3670 (class 0 OID 0)
-- Dependencies: 234
-- Name: COLUMN tenant_package_info.menu_ids; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.tenant_package_info.menu_ids IS '套餐绑定的菜单';


--
-- TOC entry 3671 (class 0 OID 0)
-- Dependencies: 234
-- Name: COLUMN tenant_package_info.remark; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.tenant_package_info.remark IS '备注';


--
-- TOC entry 3672 (class 0 OID 0)
-- Dependencies: 234
-- Name: COLUMN tenant_package_info.creator_id; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.tenant_package_info.creator_id IS '创建者';


--
-- TOC entry 3673 (class 0 OID 0)
-- Dependencies: 234
-- Name: COLUMN tenant_package_info.create_at; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.tenant_package_info.create_at IS '创建时间';


--
-- TOC entry 3674 (class 0 OID 0)
-- Dependencies: 234
-- Name: COLUMN tenant_package_info.updater_id; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.tenant_package_info.updater_id IS '更新者';


--
-- TOC entry 3675 (class 0 OID 0)
-- Dependencies: 234
-- Name: COLUMN tenant_package_info.update_at; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.tenant_package_info.update_at IS '更新时间';


--
-- TOC entry 3676 (class 0 OID 0)
-- Dependencies: 234
-- Name: COLUMN tenant_package_info.hint; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.tenant_package_info.hint IS '标签';


--
-- TOC entry 3677 (class 0 OID 0)
-- Dependencies: 234
-- Name: COLUMN tenant_package_info.member_num_limit; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.tenant_package_info.member_num_limit IS '租户成员数量限制';


--
-- TOC entry 3678 (class 0 OID 0)
-- Dependencies: 234
-- Name: COLUMN tenant_package_info.post_num_limit; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.tenant_package_info.post_num_limit IS '租户职位数量限制';


--
-- TOC entry 235 (class 1259 OID 26045)
-- Name: tenant_wallet_info; Type: TABLE; Schema: biz; Owner: postgres
--

CREATE TABLE biz.tenant_wallet_info (
    tenant_id bigint NOT NULL,
    balance numeric(10,2) NOT NULL,
    status character(1) NOT NULL,
    creator_id bigint NOT NULL,
    updater_id bigint,
    create_at timestamp(0) with time zone NOT NULL,
    update_at timestamp(0) with time zone,
    hint character varying(10) DEFAULT ''::character varying NOT NULL,
    deleted boolean DEFAULT false NOT NULL
);


ALTER TABLE biz.tenant_wallet_info OWNER TO postgres;

--
-- TOC entry 3679 (class 0 OID 0)
-- Dependencies: 235
-- Name: TABLE tenant_wallet_info; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON TABLE biz.tenant_wallet_info IS '租户钱包';


--
-- TOC entry 3680 (class 0 OID 0)
-- Dependencies: 235
-- Name: COLUMN tenant_wallet_info.balance; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.tenant_wallet_info.balance IS '余额, 元';


--
-- TOC entry 3681 (class 0 OID 0)
-- Dependencies: 235
-- Name: COLUMN tenant_wallet_info.status; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.tenant_wallet_info.status IS '状态';


--
-- TOC entry 236 (class 1259 OID 26048)
-- Name: tenant_wallet_record; Type: TABLE; Schema: biz; Owner: postgres
--

CREATE TABLE biz.tenant_wallet_record (
    record_id bigint NOT NULL,
    tenant_id bigint NOT NULL,
    amount numeric(10,2) NOT NULL,
    record_type character(1) NOT NULL,
    biz_type character(1) NOT NULL,
    biz_no character varying(32) NOT NULL,
    create_at timestamp(0) with time zone NOT NULL,
    remark character varying(100),
    creator_id bigint NOT NULL,
    updater_id bigint,
    update_at timestamp(0) with time zone,
    hint character varying(10) DEFAULT ''::character varying NOT NULL,
    deleted boolean DEFAULT false NOT NULL
);


ALTER TABLE biz.tenant_wallet_record OWNER TO postgres;

--
-- TOC entry 3682 (class 0 OID 0)
-- Dependencies: 236
-- Name: TABLE tenant_wallet_record; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON TABLE biz.tenant_wallet_record IS '租户钱包记录';


--
-- TOC entry 3683 (class 0 OID 0)
-- Dependencies: 236
-- Name: COLUMN tenant_wallet_record.tenant_id; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.tenant_wallet_record.tenant_id IS '租户ID';


--
-- TOC entry 3684 (class 0 OID 0)
-- Dependencies: 236
-- Name: COLUMN tenant_wallet_record.amount; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.tenant_wallet_record.amount IS '金额, 单位元';


--
-- TOC entry 3685 (class 0 OID 0)
-- Dependencies: 236
-- Name: COLUMN tenant_wallet_record.record_type; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.tenant_wallet_record.record_type IS '记录类型';


--
-- TOC entry 3686 (class 0 OID 0)
-- Dependencies: 236
-- Name: COLUMN tenant_wallet_record.biz_type; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.tenant_wallet_record.biz_type IS '业务类型';


--
-- TOC entry 3687 (class 0 OID 0)
-- Dependencies: 236
-- Name: COLUMN tenant_wallet_record.biz_no; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.tenant_wallet_record.biz_no IS '业务编号';


--
-- TOC entry 3688 (class 0 OID 0)
-- Dependencies: 236
-- Name: COLUMN tenant_wallet_record.create_at; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.tenant_wallet_record.create_at IS '创建时间';


--
-- TOC entry 3689 (class 0 OID 0)
-- Dependencies: 236
-- Name: COLUMN tenant_wallet_record.remark; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.tenant_wallet_record.remark IS '备注';


--
-- TOC entry 237 (class 1259 OID 26051)
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
    create_at timestamp(0) with time zone NOT NULL,
    complete_at timestamp(0) with time zone,
    remark character varying(100),
    hint character varying(10) DEFAULT ''::character varying NOT NULL,
    updater_id bigint,
    update_at timestamp(0) with time zone,
    deleted boolean DEFAULT false NOT NULL
);


ALTER TABLE biz.tenant_wallet_withdrawal_record OWNER TO postgres;

--
-- TOC entry 3690 (class 0 OID 0)
-- Dependencies: 237
-- Name: TABLE tenant_wallet_withdrawal_record; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON TABLE biz.tenant_wallet_withdrawal_record IS '系统提现记录';


--
-- TOC entry 3691 (class 0 OID 0)
-- Dependencies: 237
-- Name: COLUMN tenant_wallet_withdrawal_record.withdrawal_id; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.tenant_wallet_withdrawal_record.withdrawal_id IS 'id';


--
-- TOC entry 3692 (class 0 OID 0)
-- Dependencies: 237
-- Name: COLUMN tenant_wallet_withdrawal_record.order_no; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.tenant_wallet_withdrawal_record.order_no IS '订单号';


--
-- TOC entry 3693 (class 0 OID 0)
-- Dependencies: 237
-- Name: COLUMN tenant_wallet_withdrawal_record.status; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.tenant_wallet_withdrawal_record.status IS '状态
''0'' 处理中
''1'' 成功
''2'' 失败';


--
-- TOC entry 3694 (class 0 OID 0)
-- Dependencies: 237
-- Name: COLUMN tenant_wallet_withdrawal_record.tenant_id; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.tenant_wallet_withdrawal_record.tenant_id IS '租户id';


--
-- TOC entry 3695 (class 0 OID 0)
-- Dependencies: 237
-- Name: COLUMN tenant_wallet_withdrawal_record.request_params; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.tenant_wallet_withdrawal_record.request_params IS '第三方请求参数';


--
-- TOC entry 3696 (class 0 OID 0)
-- Dependencies: 237
-- Name: COLUMN tenant_wallet_withdrawal_record.amount; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.tenant_wallet_withdrawal_record.amount IS '金额, 单位元';


--
-- TOC entry 3697 (class 0 OID 0)
-- Dependencies: 237
-- Name: COLUMN tenant_wallet_withdrawal_record.error_msg; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.tenant_wallet_withdrawal_record.error_msg IS '错误信息';


--
-- TOC entry 3698 (class 0 OID 0)
-- Dependencies: 237
-- Name: COLUMN tenant_wallet_withdrawal_record.creator_id; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.tenant_wallet_withdrawal_record.creator_id IS '创建者';


--
-- TOC entry 3699 (class 0 OID 0)
-- Dependencies: 237
-- Name: COLUMN tenant_wallet_withdrawal_record.create_at; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.tenant_wallet_withdrawal_record.create_at IS '创建时间';


--
-- TOC entry 3700 (class 0 OID 0)
-- Dependencies: 237
-- Name: COLUMN tenant_wallet_withdrawal_record.complete_at; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.tenant_wallet_withdrawal_record.complete_at IS '完成时间';


--
-- TOC entry 3701 (class 0 OID 0)
-- Dependencies: 237
-- Name: COLUMN tenant_wallet_withdrawal_record.remark; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.tenant_wallet_withdrawal_record.remark IS '备注';


--
-- TOC entry 3702 (class 0 OID 0)
-- Dependencies: 237
-- Name: COLUMN tenant_wallet_withdrawal_record.hint; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.tenant_wallet_withdrawal_record.hint IS '标签';


--
-- TOC entry 3491 (class 0 OID 25949)
-- Dependencies: 215
-- Data for Name: admin_info; Type: TABLE DATA; Schema: biz; Owner: postgres
--

COPY biz.admin_info (admin_id, dept_id, username, email, nickname, phone_number, status, gender, avatar, password, login_ip, login_date, remark, creator_id, updater_id, create_at, update_at, hint, deleted) FROM stdin;
100	\N	super	\N	nick_a	13512312311	0	1	https://img-s-msn-com.akamaized.net/tenant/amp/entityid/AA1B91c8.img?w=660&h=648&m=6&x=219&y=147&s=204&d=204	{bcrypt}$2a$10$7JB720yubVSZvUI0rEqK/.VqGOZTH.ulu33dHOiBE8ByOhJIrdAu2	172.16.8.59	2026-04-14 09:14:57+08	\N	1	0	2024-04-17 14:08:55+08	2026-04-14 09:14:58+08		f
\.


--
-- TOC entry 3492 (class 0 OID 25957)
-- Dependencies: 216
-- Data for Name: admin_to_role; Type: TABLE DATA; Schema: biz; Owner: postgres
--

COPY biz.admin_to_role (admin_id, role_id) FROM stdin;
\.


--
-- TOC entry 3493 (class 0 OID 25960)
-- Dependencies: 217
-- Data for Name: customer_info; Type: TABLE DATA; Schema: biz; Owner: postgres
--

COPY biz.customer_info (customer_id, nickname, phone_number, status, gender, avatar, login_ip, login_date, creator_id, updater_id, create_at, update_at, hint, deleted) FROM stdin;
1988138628742279170	123	\N	0	0	http://tmp/f0iJwZfGvBx9bd2d939bf0fbdab283f01e98a4d9bc31.png	172.16.8.131	2025-11-20 10:16:17+08	0	0	2025-11-11 14:56:02+08	2025-11-20 10:16:17+08		f
\.


--
-- TOC entry 3494 (class 0 OID 25968)
-- Dependencies: 218
-- Data for Name: customer_social_info; Type: TABLE DATA; Schema: biz; Owner: postgres
--

COPY biz.customer_social_info (customer_id, identifier, identifier_type, creator_id, updater_id, create_at, update_at, hint, deleted) FROM stdin;
1988138628742279170	ozNXO5eZpDZXZMInfjKhkkr7LQzs	we_xcx	0	\N	2025-11-11 14:56:02+08	\N		f
\.


--
-- TOC entry 3495 (class 0 OID 25971)
-- Dependencies: 219
-- Data for Name: customer_wallet_info; Type: TABLE DATA; Schema: biz; Owner: postgres
--

COPY biz.customer_wallet_info (customer_id, balance, status, creator_id, updater_id, create_at, update_at, hint, deleted) FROM stdin;
1988138628742279170	0.00	0	0	\N	2025-11-11 14:56:02+08	\N		f
\.


--
-- TOC entry 3496 (class 0 OID 25974)
-- Dependencies: 220
-- Data for Name: customer_wallet_record; Type: TABLE DATA; Schema: biz; Owner: postgres
--

COPY biz.customer_wallet_record (record_id, customer_id, amount, record_type, create_at, remark, creator_id, updater_id, update_at, hint, deleted) FROM stdin;
\.


--
-- TOC entry 3497 (class 0 OID 25977)
-- Dependencies: 221
-- Data for Name: dept_info; Type: TABLE DATA; Schema: biz; Owner: postgres
--

COPY biz.dept_info (dept_id, parent_id, ancestors, dept_name, status, dept_sort, contact, email, creator_id, updater_id, create_at, update_at, hint, deleted) FROM stdin;
\.


--
-- TOC entry 3498 (class 0 OID 25985)
-- Dependencies: 222
-- Data for Name: member_info; Type: TABLE DATA; Schema: biz; Owner: postgres
--

COPY biz.member_info (member_id, tenant_id, username, phone_number, status, gender, avatar, password, login_ip, login_date, remark, creator_id, updater_id, create_at, update_at, hint, deleted) FROM stdin;
1910557183820165120	1910557183820165122	testadmin	\N	0	0	http://tmp/WK0iX8BuChGpbd2d939bf0fbdab283f01e98a4d9bc31.png	{bcrypt}$2a$10$7JB720yubVSZvUI0rEqK/.VqGOZTH.ulu33dHOiBE8ByOhJIrdAu2	172.16.8.59	2026-04-14 14:09:01+08	\N	1	0	2025-04-11 12:55:04+08	2026-04-14 14:09:01+08		f
\.


--
-- TOC entry 3499 (class 0 OID 25993)
-- Dependencies: 223
-- Data for Name: member_social_info; Type: TABLE DATA; Schema: biz; Owner: postgres
--

COPY biz.member_social_info (member_id, identifier, identifier_type, creator_id, updater_id, create_at, update_at, hint, deleted) FROM stdin;
1910557183820165120	ozNXO5eZpDZXZMInfjKhkkr7LQzs	we_xcx	1	\N	2025-04-11 12:55:04+08	\N		f
\.


--
-- TOC entry 3500 (class 0 OID 25996)
-- Dependencies: 224
-- Data for Name: member_to_post; Type: TABLE DATA; Schema: biz; Owner: postgres
--

COPY biz.member_to_post (member_id, post_id) FROM stdin;
\.


--
-- TOC entry 3501 (class 0 OID 25999)
-- Dependencies: 225
-- Data for Name: menu_info; Type: TABLE DATA; Schema: biz; Owner: postgres
--

COPY biz.menu_info (menu_id, menu_name, parent_id, menu_sort, path, component, query, menu_type, status, perms, icon, cacheable, visible, scope, creator_id, updater_id, create_at, update_at, hint, deleted) FROM stdin;
1980906033277222913	日志审计	0	0	audit-log	\N	\N	D	0	\N	carbon:catalog-publish	f	t	tenant	1	1	2025-10-22 15:56:16+08	2025-10-22 15:58:02+08		f
1906263415450001129	重置租户操作密码	1906263415450000601	11	#	\N	\N	B	0	user-mod:tenant-mng:reset-operpwd	#	f	t	system	1	1	2024-05-26 12:30:16+08	2025-09-03 16:17:24+08		f
1906263415450000104	日志审计	0	1	audit-log	\N	\N	D	0	\N	carbon:ibm-knowledge-catalog-premium	f	t	system	1	1	2024-05-26 12:30:16+08	2025-10-22 15:59:16+08		f
1906263415450000001	消息管理	0	80	system-mng	\N	\N	D	0	\N	carbon:z-systems	f	t	system	1	1	2024-05-26 12:30:16+08	2025-10-30 11:35:27+08		f
1906263415450000302	Sentinel控制台	1906263415450000101	3	http://localhost:8718	\N	\N	O	0		carbon:link	f	t	system	1	1	2024-05-26 12:30:16+08	2025-09-03 13:54:06+08		f
1906263415450000301	系统接口	1906263415450000101	2	http://localhost:8080/doc.html	\N	\N	I	0		carbon:link	f	t	system	1	1	2024-05-26 12:30:16+08	2025-09-03 13:54:15+08		f
1906263415450000003	平台管理	0	60	platform-mng	\N	\N	D	0	\N	carbon:platforms	f	t	system	1	1	2024-05-26 12:30:16+08	2025-10-30 11:35:32+08		f
1983742775738974209	pv/uv统计	1983739365543329794	0	pageview	statistics/pageview/index	\N	M	0	\N	carbon:activity	f	t	system	1	1	2025-10-30 11:48:29+08	2025-10-31 09:23:35+08		f
1963871785836048386	平台配置	0	0	develop	\N	\N	D	0	\N	carbon:tool-kit	f	t	system	1	1	2025-09-05 15:48:15+08	2025-10-11 09:28:34+08		f
1906263415450001052	新增参数	1906263415450000103	2	#	\N	\N	B	0	system-mod:config-mng:add	#	f	t	system	1	1	2024-05-26 12:30:16+08	2025-10-10 09:11:56+08		f
1906263415450002207	权限授予	1906263415450000206	6	#	\N	\N	B	0	user-mod:role-mng:grant-user	#	f	t	system	1	1	2024-05-26 12:30:16+08	2025-09-03 15:40:20+08		f
1976223833424326657	修改角色	1906263415450000206	4	#	\N	\N	B	0	user-mod:role-mng:edit	#	f	t	system	1	1	2025-10-09 17:50:53+08	2025-10-09 17:50:53+08		f
1976565556872667137	组织管理	0	50	organization-mng	\N	\N	D	0	\N	carbon:user	f	t	tenant	1	1	2025-10-10 16:28:46+08	2025-10-22 15:56:23+08		f
1976455457936171010	修改参数	1906263415450000103	4	#	\N	\N	B	0	system-mod:config-mng:edit	#	f	t	system	1	1	2025-10-10 09:11:17+08	2025-10-10 09:11:17+08		f
1976455537858633730	删除参数	1906263415450000103	5	#	\N	\N	B	0	system-mod:config-mng:remove	#	f	t	system	1	1	2025-10-10 09:11:36+08	2025-10-10 09:11:36+08		f
1976455824778387458	添加字典	1906263415450000102	0	#	\N	\N	B	0	system-mod:dictionary-mng:add	#	f	t	system	1	1	2025-10-10 09:12:44+08	2025-10-10 09:12:44+08		f
1976455887881691138	修改字典	1906263415450000102	3	#	\N	\N	B	0	system-mod:dictionary-mng:edit	#	f	t	system	1	1	2025-10-10 09:12:59+08	2025-10-10 09:12:59+08		f
1976455969624481794	删除字典	1906263415450000102	4	#	\N	\N	B	0	system-mod:dictionary-mng:remove	#	f	t	system	1	1	2025-10-10 09:13:19+08	2025-10-10 09:13:19+08		f
1976456608446341121	添加草稿	1906263415450000100	0	#	\N	\N	B	0	system-mod:announcement-mng:add	#	f	t	system	1	1	2025-10-10 09:15:51+08	2025-10-10 09:15:51+08		f
1976456667900600322	修改草稿	1906263415450000100	0	#	\N	\N	B	0	system-mod:announcement-mng:edit	#	f	t	system	1	1	2025-10-10 09:16:05+08	2025-10-10 09:16:05+08		f
1976223921466961921	删除角色	1906263415450000206	0	#	\N	\N	B	0	user-mod:role-mng:remove	#	f	t	system	1	1	2025-10-09 17:51:14+08	2025-10-09 17:51:14+08		f
1976224202049122306	新增部门	1906263415450000205	0	#	\N	\N	B	0	user-mod:dept-mng:add	#	f	t	system	1	1	2025-10-09 17:52:21+08	2025-10-09 17:52:21+08		f
1906263415450002039	修改部门	1906263415450000205	3	#	\N	\N	B	0	user-mod:dept-mng:edit	#	f	t	system	1	1	2024-05-26 12:30:16+08	2025-10-09 17:52:34+08		f
1976224332491976706	删除部门	1906263415450000205	5	#	\N	\N	B	0	user-mod:dept-mng:remove	#	f	t	system	1	1	2025-10-09 17:52:52+08	2025-10-09 17:52:52+08		f
1976224466323828738	部门详细	1906263415450000205	7	#	\N	\N	B	0	user-mod:dept-mng:query	#	f	t	system	1	1	2025-10-09 17:53:24+08	2025-10-09 17:53:24+08		f
1976224939848167426	修改终端	1906263415450000700	7	#	\N	\N	B	0	user-mod:oauth2client-mng:edit	#	f	t	system	1	1	2025-10-09 17:55:17+08	2025-10-09 17:55:17+08		f
1976225090838917122	添加终端	1906263415450000700	0	#	\N	\N	B	0	user-mod:oauth2client-mng:add	#	f	t	system	1	1	2025-10-09 17:55:53+08	2025-10-09 17:55:53+08		f
1976225224402333698	删除终端	1906263415450000700	0	#	\N	\N	B	0	user-mod:oauth2client-mng:remove	#	f	t	system	1	1	2025-10-09 17:56:25+08	2025-10-09 17:56:25+08		f
1976225825756475393	新增菜单	1906263415450000207	0	#	\N	\N	B	0	user-mod:menu-mng:add	#	f	t	system	1	1	2025-10-09 17:58:48+08	2025-10-09 17:58:48+08		f
1910569625749024770	授权角色	1906263415450000203	0	#	\N	\N	B	0	user-mod:admin-mng:grant-role	#	f	t	system	1	100	2025-04-11 13:44:30+08	2025-11-07 13:58:46+08		f
1980906374936838146	登录日志	1980906033277222913	10	login	loginlog-tenant/mng/index	\N	M	0	system-mod:tenant-loginlog-mng:page	carbon:login	f	t	tenant	1	100	2025-10-22 15:57:38+08	2025-11-10 11:08:27+08		f
1906263415450000700	终端管理	1906263415450000003	3	oauth2client	oauth2client/mng/index	\N	M	0	user-mod:oauth2client-mng:page	carbon:application	f	t	system	1	1	2024-05-26 12:30:16+08	2025-09-03 17:29:48+08		f
1906263415450000103	配置管理	1963871785836048386	7	config	config/mng/index	\N	M	0	system-mod:config-mng:page	carbon:parameter	f	t	system	1	1	2024-05-26 12:30:16+08	2025-10-14 10:09:25+08		f
1906263415450000151	登录日志	1906263415450000104	2	login	loginlog-admin/mng/index	\N	M	0	system-mod:admin-loginlog-mng:page	carbon:login	f	t	system	1	100	2024-05-26 12:30:16+08	2025-11-07 15:00:45+08		f
1976585906620653569	职位管理	1976565556872667137	0	post	post/mng/index	\N	M	0	user-mod:post-mng:page	carbon:load-balancer-classic	f	t	tenant	1	1	2025-10-10 17:49:38+08	2025-10-15 14:55:02+08		f
1976570103963770881	成员管理	1976565556872667137	8	member	member/mng/index	\N	M	0	user-mod:member-mng:page	carbon:user-identification	f	t	tenant	1	100	2025-10-10 16:46:50+08	2025-11-10 11:11:21+08		f
1906263415450000100	公告管理	1906263415450000001	15	announcement	announcement/mng/index	\N	M	0	system-mod:announcement-mng:page	carbon:message-queue	f	t	system	1	1	2024-05-26 12:30:16+08	2025-10-14 10:06:06+08		f
1980906706949554177	操作日志	1980906033277222913	0	operate	operatelog-tenant/mng/index	\N	M	0	system-mod:tenant-operatelog-mng:page	carbon:touch-interaction	f	t	tenant	1	100	2025-10-22 15:58:57+08	2025-11-10 11:08:35+08		f
1976456851288154113	删除公告	1906263415450000100	0	#	\N	\N	B	0	system-mod:announcement-mng:remove	#	f	t	system	1	1	2025-10-10 09:16:49+08	2025-10-10 09:16:49+08		f
1906263415450001210	终端详情	1906263415450000700	1	#	\N	\N	B	0	user-mod:oauth2client-mng:query	#	f	t	system	1	1	2024-05-26 12:30:16+08	2025-09-03 12:42:07+08		f
1976220001646616577	新增租户套餐	1906263415450000602	1	#	\N	\N	B	0	user-mod:tenantpackage-mng:add	#	f	t	system	1	1	2025-10-09 17:35:39+08	2025-10-09 17:35:39+08		f
1976220107171110913	修改租户套餐	1906263415450000602	3	#	\N	\N	B	0	user-mod:tenantpackage-mng:edit	#	f	t	system	1	1	2025-10-09 17:36:05+08	2025-10-09 17:36:05+08		f
1976220234040418306	删除租户套餐	1906263415450000602	0	#	\N	\N	B	0	user-mod:tenantpackage-mng:remove	#	f	t	system	1	1	2025-10-09 17:36:35+08	2025-10-09 17:36:35+08		f
1915322746249367554	修改信息	1906272182215585793	0	#	\N	\N	B	0	user-mod:tenant-info:edit	#	f	t	tenant	1	1	2025-04-24 16:31:42+08	2025-10-09 17:38:05+08		f
1976225899114852354	修改菜单	1906263415450000207	0	#	\N	\N	B	0	user-mod:menu-mng:edit	#	f	t	system	1	1	2025-10-09 17:59:06+08	2025-10-09 17:59:06+08		f
1976226216250372098	新增租户	1906263415450000601	5	#	\N	\N	B	0	user-mod:tenant-mng:add	#	f	t	system	1	1	2025-10-09 18:00:21+08	2025-10-09 18:00:21+08		f
1906263415450001215	修改密钥	1906263415450000700	5	#	\N	\N	B	0	user-mod:oauth2client-mng:edit-secret	#	f	t	system	1	1	2024-05-26 12:30:16+08	2025-09-03 16:16:08+08		f
1983741300921024514	api分析	1983739365543329794	10	apicall	statistics/apicall/index	\N	M	0	\N	carbon:api-1	f	t	system	1	1	2025-10-30 11:42:37+08	2025-10-31 09:22:48+08		f
1983739365543329794	统计分析	0	100	statistics	\N	\N	D	0	gateway-mod:statistics:pvuv	carbon:chart-dual-y-axis	f	t	system	1	1	2025-10-30 11:34:55+08	2025-10-31 09:22:59+08		f
1906263415450000304	服务监控	1906263415450000101	5	http://localhost:9100/	\N	\N	O	0		carbon:link	f	t	system	1	1	2024-05-26 12:30:16+08	2025-09-03 13:53:38+08		f
1906263415450000101	控制台入口	1963871785836048386	0	controller	\N	\N	D	0	\N	carbon:dashboard	f	t	system	1	1	2024-05-26 12:30:16+08	2025-09-05 15:53:53+08		f
1906263415450000300	定时任务	1906263415450000101	20	http://localhost:9200/xxl-job-admin	\N	\N	O	0		carbon:link	f	t	system	1	1	2024-05-26 12:30:16+08	2025-09-05 16:16:22+08		f
1906263415450000303	Nacos控制台	1906263415450000101	4	http://localhost:8848/nacos	\N	\N	O	0		carbon:link	f	t	system	1	1	2024-05-26 12:30:16+08	2025-09-05 16:16:29+08		f
1906263415450000207	菜单管理	1906263415450000003	50	menu	menu/mng/index	\N	M	0	user-mod:menu-mng:list	carbon:menu	f	t	system	1	1	2024-05-26 12:30:16+08	2025-09-03 13:36:05+08		f
1906263415450000002	组织管理	0	50	organization-mng	\N	\N	D	0	\N	carbon:user	f	t	system	1	1	2024-05-26 12:30:16+08	2025-10-22 15:09:41+08		f
1976226385717030913	删除租户	1906263415450000601	8	#	\N	\N	B	0	user-mod:tenant-mng:remove	#	f	t	system	1	1	2025-10-09 18:01:02+08	2025-10-09 18:01:02+08		f
1976586082013863937	新增职位	1976585906620653569	0	#	\N	\N	B	0	user-mod:post-mng:add	#	f	t	tenant	1	1	2025-10-10 17:50:20+08	2025-10-10 17:50:20+08		f
1906263415450001126	商户提现	1906263415450001127	1	#	\N	\N	B	0	user-mod:tenant-wallet-info:withdrawal	#	f	t	tenant	1	1	2024-05-26 12:30:16+08	2025-10-09 17:38:23+08		f
1976223757310291969	新增角色	1906263415450000206	0	#	\N	\N	B	0	user-mod:role-mng:add	#	f	t	system	1	1	2025-10-09 17:50:35+08	2025-10-09 17:50:35+08		f
1906263415450002016	删除菜单	1906263415450000207	4	#	\N	\N	B	0	user-mod:menu-mng:remove	#	f	t	system	1	1	2024-05-26 12:30:16+08	2025-10-09 17:59:20+08		f
1906263415450001133	修改租户	1906263415450000601	2	#	\N	\N	B	0	user-mod:tenant-mng:edit	#	f	t	system	1	1	2024-05-26 12:30:16+08	2025-10-09 18:00:31+08		f
1976586292211408897	删除职位	1976585906620653569	5	#	\N	\N	B	0	user-mod:post-mng:remove	#	f	t	tenant	1	1	2025-10-10 17:51:10+08	2025-10-10 17:51:10+08		f
1976586196090544129	修改职位	1976585906620653569	3	#	\N	\N	B	0	user-mod:post-mng:edit	#	f	t	tenant	1	1	2025-10-10 17:50:47+08	2025-10-10 17:51:16+08		f
1906272182215585793	租户信息	0	100	tenant-info	tenant/info/index	\N	M	0	user-mod:tenant-info	carbon:information-filled	f	t	tenant	1	1	2025-03-30 17:08:00+08	2025-10-22 15:11:17+08		f
1906263415450000205	部门管理	1906263415450000002	70	dept	dept/mng/index	\N	M	0	user-mod:dept-mng:list	carbon:departure	f	t	system	1	1	2024-05-26 12:30:16+08	2025-10-11 11:27:19+08		f
1906263415450002062	重置密码	1906263415450000203	7	#	\N	\N	B	0	user-mod:admin-mng:resetPwd	#	f	t	system	1	100	2024-05-26 12:30:16+08	2025-11-07 13:57:48+08		f
1976586772002037762	删除	1976570103963770881	7	#	\N	\N	B	0	user-mod:member-mng:remove	#	f	t	tenant	1	100	2025-10-10 17:53:04+08	2025-11-10 11:06:54+08		f
1976586698882736130	授权职位	1976570103963770881	5	#	\N	\N	B	0	user-mod:member-mng:grant-post	#	f	t	tenant	1	100	2025-10-10 17:52:47+08	2025-11-10 11:07:04+08		f
1976586612681400321	修改	1976570103963770881	3	#	\N	\N	B	0	user-mod:member-mng:edit	#	f	t	tenant	1	100	2025-10-10 17:52:26+08	2025-11-10 11:07:13+08		f
1976586554187636737	新增	1976570103963770881	0	#	\N	\N	B	0	user-mod:member-mng:add	#	f	t	tenant	1	100	2025-10-10 17:52:12+08	2025-11-10 11:07:25+08		f
1906263415450000102	字典管理	1963871785836048386	6	dictionary	dictionary/mng/index	\N	M	0	system-mod:dictionary-mng:page	carbon:text-vertical-alignment	f	t	system	1	1	2024-05-26 12:30:16+08	2025-10-14 10:09:30+08		f
1906263415450000601	租户管理	1906263415450000003	100	tenant	tenant/mng/index	\N	M	0	user-mod:tenant-mng:page	carbon:id-management	f	t	system	1	1	2024-05-26 12:30:16+08	2025-09-03 17:29:31+08		f
1906263415450002064	修改账号	1906263415450000203	3	#	\N	\N	B	0	user-mod:admin-mng:edit	#	f	t	system	1	100	2024-05-26 12:30:16+08	2025-11-07 14:03:09+08		f
1906263415450002077	导出	1906263415450000203	1	#	\N	\N	B	0	user-mod:admin-mng:export	#	f	t	system	1	100	2024-05-26 12:30:16+08	2025-11-07 14:03:17+08		f
1906263415450002071	查询账号	1906263415450000203	0	#	\N	\N	B	0	user-mod:admin-mng:query	#	f	t	system	1	100	2024-05-26 12:30:16+08	2025-11-07 14:03:33+08		f
1906263415450002072	新增账号	1906263415450000203	1	#	\N	\N	B	0	user-mod:admin-mng:add	#	f	t	system	1	100	2024-05-26 12:30:16+08	2025-11-07 14:04:08+08		f
1906263415450000602	租户套餐管理	1906263415450000003	50	tenant-package	tenant-package/mng/index	\N	M	0	user-mod:tenantpackage-mng:page	carbon:package	f	t	system	1	1	2024-05-26 12:30:16+08	2025-09-03 17:29:37+08		f
1906263415450000206	角色管理	1906263415450000002	99	role	role/mng/index	\N	M	0	user-mod:role-mng:page	carbon:user-role	f	t	system	1	1	2024-05-26 12:30:16+08	2025-10-11 11:27:10+08		f
1906263415450000203	账号管理	1906263415450000002	100	admin	admin/mng/index	\N	M	0	user-mod:admin-mng:page	carbon:user-admin	t	t	system	1	100	2024-05-26 12:30:16+08	2025-11-07 14:01:25+08		f
1906263415450000201	顾客管理	1906263415450000003	1	customer	customer/mng/index	\N	M	0	user-mod:customer-mng:page	carbon:customer	f	t	system	1	1	2024-05-26 12:30:16+08	2025-09-03 17:29:53+08		f
1906263415450000150	操作日志	1906263415450000104	1	operate	operatelog-admin/mng/index	\N	M	0	system-mod:admin-operatelog-mng:page	carbon:touch-interaction	f	t	system	1	100	2024-05-26 12:30:16+08	2025-11-07 15:01:01+08		f
1906263415450001125	钱包记录	1906263415450000601	3	#	\N	\N	B	0	user-mod:tenant-wallet-mng:record-page	#	f	t	system	1	1	2024-05-26 12:30:16+08	2025-09-03 16:17:56+08		f
1906263415450001127	商户钱包	0	85	/tenant-wallet	tenant/wallet/index		M	0	user-mod:tenant-wallet-info	carbon:wallet	f	t	tenant	1	100	2024-05-26 12:30:16+08	2026-04-14 10:20:48+08		f
2043932492313976834	套餐信息	1906272182215585793	0	#	\N	\N	M	0	user-mod:tenant-package-info	carbon:package-node	f	t	tenant	100	100	2026-04-14 14:00:55+08	2026-04-14 14:00:55+08		f
\.


--
-- TOC entry 3502 (class 0 OID 26005)
-- Dependencies: 226
-- Data for Name: oauth2_client; Type: TABLE DATA; Schema: biz; Owner: postgres
--

COPY biz.oauth2_client (id, client_name, client_id, client_secret, scopes, authorization_grant_types, redirect_uris, status, auto_approve, create_at, creator_id, update_at, updater_id, hint, deleted) FROM stdin;
2	oauth2客户端demo	oauth_client_demo	{bcrypt}$2a$10$hK9Sv9kAvXE00fWtkWxzI.Ns4.5SuQteTJAnsFWXChlOWIUZSFYL2	{read}	{authorization_code,client_credentials}	{http://127.0.0.1:3342/login/oauth2/code/auth-center}	0	f	2025-05-21 14:13:49+08	1	2025-09-01 15:53:53+08	1		f
1	系统	server	{bcrypt}$2a$10$hK9Sv9kAvXE00fWtkWxzI.Ns4.5SuQteTJAnsFWXChlOWIUZSFYL2	{openid,read}	{authorization_code,urn:ietf:params:oauth:grant-type:device_code,refresh_token}	{http://localhost:9000/oauth2/authorization_code_callback}	0	f	2024-04-17 14:08:54+08	1	2025-09-03 11:28:18+08	1		f
\.


--
-- TOC entry 3503 (class 0 OID 26014)
-- Dependencies: 227
-- Data for Name: post_info; Type: TABLE DATA; Schema: biz; Owner: root
--

COPY biz.post_info (post_id, tenant_id, post_name, status, post_sort, create_at, creator_id, update_at, updater_id, hint, deleted) FROM stdin;
1978377271113371649	1910557183820165122	CEO	0	0	2025-10-15 16:27:53+08	1910557183820165120	2025-10-15 16:27:53+08	1910557183820165120		f
1978377302486765569	1910557183820165122	CFO	0	0	2025-10-15 16:28:00+08	1910557183820165120	2025-10-15 16:44:51+08	1910557183820165120		f
\.


--
-- TOC entry 3504 (class 0 OID 26018)
-- Dependencies: 228
-- Data for Name: post_to_menu; Type: TABLE DATA; Schema: biz; Owner: postgres
--

COPY biz.post_to_menu (post_id, menu_id) FROM stdin;
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
-- TOC entry 3505 (class 0 OID 26021)
-- Dependencies: 229
-- Data for Name: role_info; Type: TABLE DATA; Schema: biz; Owner: root
--

COPY biz.role_info (role_id, data_scope, role_name, status, role_sort, create_at, creator_id, update_at, updater_id, hint, deleted) FROM stdin;
\.


--
-- TOC entry 3506 (class 0 OID 26025)
-- Dependencies: 230
-- Data for Name: role_inheritance; Type: TABLE DATA; Schema: biz; Owner: postgres
--

COPY biz.role_inheritance (role_id, child_id) FROM stdin;
\.


--
-- TOC entry 3507 (class 0 OID 26028)
-- Dependencies: 231
-- Data for Name: role_to_dept; Type: TABLE DATA; Schema: biz; Owner: postgres
--

COPY biz.role_to_dept (role_id, dept_id) FROM stdin;
\.


--
-- TOC entry 3508 (class 0 OID 26031)
-- Dependencies: 232
-- Data for Name: role_to_menu; Type: TABLE DATA; Schema: biz; Owner: postgres
--

COPY biz.role_to_menu (role_id, menu_id) FROM stdin;
\.


--
-- TOC entry 3509 (class 0 OID 26034)
-- Dependencies: 233
-- Data for Name: tenant_info; Type: TABLE DATA; Schema: biz; Owner: postgres
--

COPY biz.tenant_info (tenant_id, administrator, tenant_type, contact_phone, tenant_name, oper_pwd, status, domain, remark, package_id, expire_time, creator_id, create_at, updater_id, update_at, hint, deleted) FROM stdin;
1910557183820165122	1910557183820165120	0		test1	{bcrypt}$2a$10$1UJgROjrOvMKJD4way7dKeBsJuLGVLWGy/pBGooa.sFqfsP3Vrupm	0		\N	1773625804122202113	2026-05-01 00:00:00+08	1	2025-04-11 12:55:04+08	1910557183820165120	2025-11-18 15:02:28+08		f
\.


--
-- TOC entry 3510 (class 0 OID 26038)
-- Dependencies: 234
-- Data for Name: tenant_package_info; Type: TABLE DATA; Schema: biz; Owner: postgres
--

COPY biz.tenant_package_info (package_id, package_name, status, menu_ids, remark, creator_id, create_at, updater_id, update_at, hint, member_num_limit, post_num_limit, deleted) FROM stdin;
1773625804122202113	默认套餐	0	{1906272182215585793,2043932492313976834,1915322746249367554,1906263415450001127,1906263415450001126,1976565556872667137,1976570103963770881,1976586772002037762,1976586698882736130,1976586612681400321,1976586554187636737,1976585906620653569,1976586292211408897,1976586196090544129,1976586082013863937,1980906033277222913,1980906374936838146,1980906706949554177}	通用租户套餐	1	2024-04-17 14:08:54+08	100	2026-04-14 14:07:58+08		5	5	f
\.


--
-- TOC entry 3511 (class 0 OID 26045)
-- Dependencies: 235
-- Data for Name: tenant_wallet_info; Type: TABLE DATA; Schema: biz; Owner: postgres
--

COPY biz.tenant_wallet_info (tenant_id, balance, status, creator_id, updater_id, create_at, update_at, hint, deleted) FROM stdin;
1910557183820165122	0.00	0	1	\N	2025-04-11 12:55:04+08	\N		f
\.


--
-- TOC entry 3512 (class 0 OID 26048)
-- Dependencies: 236
-- Data for Name: tenant_wallet_record; Type: TABLE DATA; Schema: biz; Owner: postgres
--

COPY biz.tenant_wallet_record (record_id, tenant_id, amount, record_type, biz_type, biz_no, create_at, remark, creator_id, updater_id, update_at, hint, deleted) FROM stdin;
\.


--
-- TOC entry 3513 (class 0 OID 26051)
-- Dependencies: 237
-- Data for Name: tenant_wallet_withdrawal_record; Type: TABLE DATA; Schema: biz; Owner: postgres
--

COPY biz.tenant_wallet_withdrawal_record (withdrawal_id, order_no, status, tenant_id, request_params, amount, error_msg, creator_id, create_at, complete_at, remark, hint, updater_id, update_at, deleted) FROM stdin;
\.


--
-- TOC entry 3289 (class 2606 OID 26058)
-- Name: admin_info admin_info_pkey; Type: CONSTRAINT; Schema: biz; Owner: postgres
--

ALTER TABLE ONLY biz.admin_info
    ADD CONSTRAINT admin_info_pkey PRIMARY KEY (admin_id);


--
-- TOC entry 3293 (class 2606 OID 26060)
-- Name: admin_to_role admin_to_role_pkey; Type: CONSTRAINT; Schema: biz; Owner: postgres
--

ALTER TABLE ONLY biz.admin_to_role
    ADD CONSTRAINT admin_to_role_pkey PRIMARY KEY (admin_id, role_id);


--
-- TOC entry 3295 (class 2606 OID 26062)
-- Name: customer_info customer_info_pkey; Type: CONSTRAINT; Schema: biz; Owner: postgres
--

ALTER TABLE ONLY biz.customer_info
    ADD CONSTRAINT customer_info_pkey PRIMARY KEY (customer_id);


--
-- TOC entry 3298 (class 2606 OID 26064)
-- Name: customer_social_info customer_social_info_pkey; Type: CONSTRAINT; Schema: biz; Owner: postgres
--

ALTER TABLE ONLY biz.customer_social_info
    ADD CONSTRAINT customer_social_info_pkey PRIMARY KEY (customer_id);


--
-- TOC entry 3301 (class 2606 OID 26066)
-- Name: customer_wallet_info customer_wallet_info_pkey; Type: CONSTRAINT; Schema: biz; Owner: postgres
--

ALTER TABLE ONLY biz.customer_wallet_info
    ADD CONSTRAINT customer_wallet_info_pkey PRIMARY KEY (customer_id);


--
-- TOC entry 3303 (class 2606 OID 26068)
-- Name: customer_wallet_record customer_wallet_record_pkey; Type: CONSTRAINT; Schema: biz; Owner: postgres
--

ALTER TABLE ONLY biz.customer_wallet_record
    ADD CONSTRAINT customer_wallet_record_pkey PRIMARY KEY (record_id);


--
-- TOC entry 3306 (class 2606 OID 26070)
-- Name: dept_info dept_info_pkey; Type: CONSTRAINT; Schema: biz; Owner: postgres
--

ALTER TABLE ONLY biz.dept_info
    ADD CONSTRAINT dept_info_pkey PRIMARY KEY (dept_id);


--
-- TOC entry 3310 (class 2606 OID 26072)
-- Name: member_info member_info_pkey; Type: CONSTRAINT; Schema: biz; Owner: postgres
--

ALTER TABLE ONLY biz.member_info
    ADD CONSTRAINT member_info_pkey PRIMARY KEY (member_id);


--
-- TOC entry 3314 (class 2606 OID 26074)
-- Name: member_social_info member_social_info_pkey; Type: CONSTRAINT; Schema: biz; Owner: postgres
--

ALTER TABLE ONLY biz.member_social_info
    ADD CONSTRAINT member_social_info_pkey PRIMARY KEY (member_id);


--
-- TOC entry 3317 (class 2606 OID 26076)
-- Name: member_to_post member_to_post_pkey; Type: CONSTRAINT; Schema: biz; Owner: postgres
--

ALTER TABLE ONLY biz.member_to_post
    ADD CONSTRAINT member_to_post_pkey PRIMARY KEY (member_id, post_id);


--
-- TOC entry 3320 (class 2606 OID 26078)
-- Name: menu_info menu_info_pkey; Type: CONSTRAINT; Schema: biz; Owner: postgres
--

ALTER TABLE ONLY biz.menu_info
    ADD CONSTRAINT menu_info_pkey PRIMARY KEY (menu_id);


--
-- TOC entry 3322 (class 2606 OID 26080)
-- Name: oauth2_client oauth2_client_pkey; Type: CONSTRAINT; Schema: biz; Owner: postgres
--

ALTER TABLE ONLY biz.oauth2_client
    ADD CONSTRAINT oauth2_client_pkey PRIMARY KEY (id);


--
-- TOC entry 3325 (class 2606 OID 26082)
-- Name: post_info post_info_pkey; Type: CONSTRAINT; Schema: biz; Owner: root
--

ALTER TABLE ONLY biz.post_info
    ADD CONSTRAINT post_info_pkey PRIMARY KEY (post_id);


--
-- TOC entry 3327 (class 2606 OID 26084)
-- Name: post_to_menu post_to_menu_pkey; Type: CONSTRAINT; Schema: biz; Owner: postgres
--

ALTER TABLE ONLY biz.post_to_menu
    ADD CONSTRAINT post_to_menu_pkey PRIMARY KEY (post_id, menu_id);


--
-- TOC entry 3329 (class 2606 OID 26086)
-- Name: role_info role_info_pkey; Type: CONSTRAINT; Schema: biz; Owner: root
--

ALTER TABLE ONLY biz.role_info
    ADD CONSTRAINT role_info_pkey PRIMARY KEY (role_id);


--
-- TOC entry 3331 (class 2606 OID 26088)
-- Name: role_inheritance role_inheritance_pkey; Type: CONSTRAINT; Schema: biz; Owner: postgres
--

ALTER TABLE ONLY biz.role_inheritance
    ADD CONSTRAINT role_inheritance_pkey PRIMARY KEY (role_id, child_id);


--
-- TOC entry 3333 (class 2606 OID 26090)
-- Name: role_to_dept role_to_dept_pkey; Type: CONSTRAINT; Schema: biz; Owner: postgres
--

ALTER TABLE ONLY biz.role_to_dept
    ADD CONSTRAINT role_to_dept_pkey PRIMARY KEY (role_id, dept_id);


--
-- TOC entry 3335 (class 2606 OID 26092)
-- Name: role_to_menu role_to_menu_pkey; Type: CONSTRAINT; Schema: biz; Owner: postgres
--

ALTER TABLE ONLY biz.role_to_menu
    ADD CONSTRAINT role_to_menu_pkey PRIMARY KEY (role_id, menu_id);


--
-- TOC entry 3337 (class 2606 OID 26094)
-- Name: tenant_info tenant_info_pkey; Type: CONSTRAINT; Schema: biz; Owner: postgres
--

ALTER TABLE ONLY biz.tenant_info
    ADD CONSTRAINT tenant_info_pkey PRIMARY KEY (tenant_id);


--
-- TOC entry 3340 (class 2606 OID 26096)
-- Name: tenant_package_info tenant_package_info_pkey; Type: CONSTRAINT; Schema: biz; Owner: postgres
--

ALTER TABLE ONLY biz.tenant_package_info
    ADD CONSTRAINT tenant_package_info_pkey PRIMARY KEY (package_id);


--
-- TOC entry 3342 (class 2606 OID 26098)
-- Name: tenant_wallet_info tenant_wallet_info_pkey; Type: CONSTRAINT; Schema: biz; Owner: postgres
--

ALTER TABLE ONLY biz.tenant_wallet_info
    ADD CONSTRAINT tenant_wallet_info_pkey PRIMARY KEY (tenant_id);


--
-- TOC entry 3345 (class 2606 OID 26100)
-- Name: tenant_wallet_record tenant_wallet_record_pkey; Type: CONSTRAINT; Schema: biz; Owner: postgres
--

ALTER TABLE ONLY biz.tenant_wallet_record
    ADD CONSTRAINT tenant_wallet_record_pkey PRIMARY KEY (record_id);


--
-- TOC entry 3348 (class 2606 OID 26102)
-- Name: tenant_wallet_withdrawal_record tenant_wallet_withdrawal_record_pkey; Type: CONSTRAINT; Schema: biz; Owner: postgres
--

ALTER TABLE ONLY biz.tenant_wallet_withdrawal_record
    ADD CONSTRAINT tenant_wallet_withdrawal_record_pkey PRIMARY KEY (withdrawal_id);


--
-- TOC entry 3304 (class 1259 OID 26103)
-- Name: idx_customer_wallet_record_customer_id; Type: INDEX; Schema: biz; Owner: postgres
--

CREATE INDEX idx_customer_wallet_record_customer_id ON biz.customer_wallet_record USING btree (customer_id) WHERE deleted = false;


--
-- TOC entry 3307 (class 1259 OID 26104)
-- Name: idx_dept_info_ancestors; Type: INDEX; Schema: biz; Owner: postgres
--

CREATE INDEX idx_dept_info_ancestors ON biz.dept_info USING btree (ancestors) WHERE deleted = false;


--
-- TOC entry 3308 (class 1259 OID 26105)
-- Name: idx_dept_info_parent_id; Type: INDEX; Schema: biz; Owner: postgres
--

CREATE INDEX idx_dept_info_parent_id ON biz.dept_info USING btree (parent_id) WHERE deleted = false;


--
-- TOC entry 3318 (class 1259 OID 26106)
-- Name: idx_menu_info_parent_id; Type: INDEX; Schema: biz; Owner: postgres
--

CREATE INDEX idx_menu_info_parent_id ON biz.menu_info USING btree (parent_id) WHERE deleted = false;


--
-- TOC entry 3343 (class 1259 OID 26107)
-- Name: idx_tenant_wallet_record_tenant_id; Type: INDEX; Schema: biz; Owner: postgres
--

CREATE INDEX idx_tenant_wallet_record_tenant_id ON biz.tenant_wallet_record USING btree (tenant_id) WHERE deleted = false;


--
-- TOC entry 3346 (class 1259 OID 26108)
-- Name: idx_tenant_wallet_withdrawal_record_order_no; Type: INDEX; Schema: biz; Owner: postgres
--

CREATE UNIQUE INDEX idx_tenant_wallet_withdrawal_record_order_no ON biz.tenant_wallet_withdrawal_record USING btree (order_no) WHERE deleted = false;


--
-- TOC entry 3290 (class 1259 OID 26109)
-- Name: uk_admin_info_phone_number; Type: INDEX; Schema: biz; Owner: postgres
--

CREATE UNIQUE INDEX uk_admin_info_phone_number ON biz.admin_info USING btree (phone_number) WHERE deleted = false;


--
-- TOC entry 3291 (class 1259 OID 26110)
-- Name: uk_admin_info_username; Type: INDEX; Schema: biz; Owner: postgres
--

CREATE UNIQUE INDEX uk_admin_info_username ON biz.admin_info USING btree (username) WHERE deleted = false;


--
-- TOC entry 3296 (class 1259 OID 26111)
-- Name: uk_customer_info_phone_number; Type: INDEX; Schema: biz; Owner: postgres
--

CREATE UNIQUE INDEX uk_customer_info_phone_number ON biz.customer_info USING btree (phone_number) WHERE deleted = false;


--
-- TOC entry 3299 (class 1259 OID 26112)
-- Name: uk_customer_social_info_identifier; Type: INDEX; Schema: biz; Owner: postgres
--

CREATE UNIQUE INDEX uk_customer_social_info_identifier ON biz.customer_social_info USING btree (identifier) WHERE deleted = false;


--
-- TOC entry 3311 (class 1259 OID 26113)
-- Name: uk_member_info_phone_number; Type: INDEX; Schema: biz; Owner: postgres
--

CREATE UNIQUE INDEX uk_member_info_phone_number ON biz.member_info USING btree (phone_number) WHERE deleted = false;


--
-- TOC entry 3312 (class 1259 OID 26114)
-- Name: uk_member_info_username; Type: INDEX; Schema: biz; Owner: postgres
--

CREATE UNIQUE INDEX uk_member_info_username ON biz.member_info USING btree (username) WHERE deleted = false;


--
-- TOC entry 3315 (class 1259 OID 26115)
-- Name: uk_member_social_info_identifier; Type: INDEX; Schema: biz; Owner: postgres
--

CREATE UNIQUE INDEX uk_member_social_info_identifier ON biz.member_social_info USING btree (identifier) WHERE deleted = false;


--
-- TOC entry 3323 (class 1259 OID 26116)
-- Name: uk_oauth2_client_client_id; Type: INDEX; Schema: biz; Owner: postgres
--

CREATE UNIQUE INDEX uk_oauth2_client_client_id ON biz.oauth2_client USING btree (client_id) WHERE deleted = false;


--
-- TOC entry 3338 (class 1259 OID 26117)
-- Name: uk_tenant_info_administrator; Type: INDEX; Schema: biz; Owner: postgres
--

CREATE UNIQUE INDEX uk_tenant_info_administrator ON biz.tenant_info USING btree (administrator) WHERE deleted = false;


-- Completed on 2026-04-14 14:09:27

--
-- PostgreSQL database dump complete
--

-- TOC entry 215 (class 1259 OID 26120)
-- Name: admin_login_log; Type: TABLE; Schema: biz; Owner: postgres
--

CREATE TABLE biz.admin_login_log (
    log_id bigint NOT NULL,
    admin_id bigint NOT NULL,
    username character varying(32) NOT NULL,
    login_type character varying(32) NOT NULL,
    success boolean NOT NULL,
    error_msg character varying(50) NOT NULL,
    login_ip inet NOT NULL,
    login_location character varying(50) NOT NULL,
    login_time timestamp(0) with time zone NOT NULL,
    trace_id character varying(64) NOT NULL,
    user_agent character varying(200) NOT NULL
);


ALTER TABLE biz.admin_login_log OWNER TO postgres;

--
-- TOC entry 3404 (class 0 OID 0)
-- Dependencies: 215
-- Name: TABLE admin_login_log; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON TABLE biz.admin_login_log IS '后台登录日志';


--
-- TOC entry 3405 (class 0 OID 0)
-- Dependencies: 215
-- Name: COLUMN admin_login_log.admin_id; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.admin_login_log.admin_id IS '用户ID';


--
-- TOC entry 3406 (class 0 OID 0)
-- Dependencies: 215
-- Name: COLUMN admin_login_log.username; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.admin_login_log.username IS '用户名';


--
-- TOC entry 3407 (class 0 OID 0)
-- Dependencies: 215
-- Name: COLUMN admin_login_log.login_type; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.admin_login_log.login_type IS '登录类型';


--
-- TOC entry 3408 (class 0 OID 0)
-- Dependencies: 215
-- Name: COLUMN admin_login_log.success; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.admin_login_log.success IS '登录状态';


--
-- TOC entry 3409 (class 0 OID 0)
-- Dependencies: 215
-- Name: COLUMN admin_login_log.error_msg; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.admin_login_log.error_msg IS '失败信息';


--
-- TOC entry 3410 (class 0 OID 0)
-- Dependencies: 215
-- Name: COLUMN admin_login_log.login_ip; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.admin_login_log.login_ip IS '登录ip';


--
-- TOC entry 3411 (class 0 OID 0)
-- Dependencies: 215
-- Name: COLUMN admin_login_log.login_location; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.admin_login_log.login_location IS '登录地址';


--
-- TOC entry 3412 (class 0 OID 0)
-- Dependencies: 215
-- Name: COLUMN admin_login_log.login_time; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.admin_login_log.login_time IS '登录时间';


--
-- TOC entry 216 (class 1259 OID 26125)
-- Name: admin_operate_log; Type: TABLE; Schema: biz; Owner: postgres
--

CREATE TABLE biz.admin_operate_log (
    oper_id bigint NOT NULL,
    title character varying(50) NOT NULL,
    sub_title character varying(50) NOT NULL,
    oper_type character(1) NOT NULL,
    method character varying(200),
    http_method character varying(10),
    admin_id bigint NOT NULL,
    username character varying(50) NOT NULL,
    http_url character varying(500),
    oper_ip inet,
    oper_location character varying(100),
    oper_param text,
    json_result text,
    success boolean NOT NULL,
    error_msg text,
    cost_time bigint,
    oper_time timestamp(3) with time zone NOT NULL
);


ALTER TABLE biz.admin_operate_log OWNER TO postgres;

--
-- TOC entry 3413 (class 0 OID 0)
-- Dependencies: 216
-- Name: TABLE admin_operate_log; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON TABLE biz.admin_operate_log IS '操作日志记录';


--
-- TOC entry 3414 (class 0 OID 0)
-- Dependencies: 216
-- Name: COLUMN admin_operate_log.oper_id; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.admin_operate_log.oper_id IS '日志主键';


--
-- TOC entry 3415 (class 0 OID 0)
-- Dependencies: 216
-- Name: COLUMN admin_operate_log.title; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.admin_operate_log.title IS '模块标题';


--
-- TOC entry 3416 (class 0 OID 0)
-- Dependencies: 216
-- Name: COLUMN admin_operate_log.sub_title; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.admin_operate_log.sub_title IS '子标题';


--
-- TOC entry 3417 (class 0 OID 0)
-- Dependencies: 216
-- Name: COLUMN admin_operate_log.oper_type; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.admin_operate_log.oper_type IS '操作类型（0其他 1新增 2修改 3删除 4授权 5导入导出）';


--
-- TOC entry 3418 (class 0 OID 0)
-- Dependencies: 216
-- Name: COLUMN admin_operate_log.method; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.admin_operate_log.method IS '方法名称';


--
-- TOC entry 3419 (class 0 OID 0)
-- Dependencies: 216
-- Name: COLUMN admin_operate_log.http_method; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.admin_operate_log.http_method IS '请求方式';


--
-- TOC entry 3420 (class 0 OID 0)
-- Dependencies: 216
-- Name: COLUMN admin_operate_log.admin_id; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.admin_operate_log.admin_id IS '用户ID';


--
-- TOC entry 3421 (class 0 OID 0)
-- Dependencies: 216
-- Name: COLUMN admin_operate_log.username; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.admin_operate_log.username IS '用户名';


--
-- TOC entry 3422 (class 0 OID 0)
-- Dependencies: 216
-- Name: COLUMN admin_operate_log.http_url; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.admin_operate_log.http_url IS '请求URL';


--
-- TOC entry 3423 (class 0 OID 0)
-- Dependencies: 216
-- Name: COLUMN admin_operate_log.oper_ip; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.admin_operate_log.oper_ip IS '主机地址';


--
-- TOC entry 3424 (class 0 OID 0)
-- Dependencies: 216
-- Name: COLUMN admin_operate_log.oper_location; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.admin_operate_log.oper_location IS '操作地点';


--
-- TOC entry 3425 (class 0 OID 0)
-- Dependencies: 216
-- Name: COLUMN admin_operate_log.oper_param; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.admin_operate_log.oper_param IS '请求参数';


--
-- TOC entry 3426 (class 0 OID 0)
-- Dependencies: 216
-- Name: COLUMN admin_operate_log.json_result; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.admin_operate_log.json_result IS '返回参数';


--
-- TOC entry 3427 (class 0 OID 0)
-- Dependencies: 216
-- Name: COLUMN admin_operate_log.success; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.admin_operate_log.success IS '操作状态';


--
-- TOC entry 3428 (class 0 OID 0)
-- Dependencies: 216
-- Name: COLUMN admin_operate_log.error_msg; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.admin_operate_log.error_msg IS '错误消息';


--
-- TOC entry 3429 (class 0 OID 0)
-- Dependencies: 216
-- Name: COLUMN admin_operate_log.oper_time; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.admin_operate_log.oper_time IS '操作时间';


--
-- Name: customer_operate_log; Type: TABLE; Schema: biz; Owner: postgres
--

CREATE TABLE biz.customer_operate_log (
    oper_id bigint NOT NULL,
    title character varying(50) NOT NULL,
    sub_title character varying(50) NOT NULL,
    oper_type character(1) NOT NULL,
    method character varying(200),
    http_method character varying(10),
    customer_id bigint NOT NULL,
    username character varying(50) NOT NULL,
    http_url character varying(500),
    oper_ip inet,
    oper_location character varying(100),
    oper_param text,
    json_result text,
    success boolean NOT NULL,
    error_msg text,
    cost_time bigint,
    oper_time timestamp(3) with time zone NOT NULL
);

ALTER TABLE biz.customer_operate_log OWNER TO postgres;

COMMENT ON TABLE biz.customer_operate_log IS '用户操作日志记录';
COMMENT ON COLUMN biz.customer_operate_log.oper_id IS '日志主键';
COMMENT ON COLUMN biz.customer_operate_log.title IS '模块标题';
COMMENT ON COLUMN biz.customer_operate_log.sub_title IS '子标题';
COMMENT ON COLUMN biz.customer_operate_log.oper_type IS '操作类型（0其他 1新增 2修改 3删除 4授权 5导入导出）';
COMMENT ON COLUMN biz.customer_operate_log.method IS '方法名称';
COMMENT ON COLUMN biz.customer_operate_log.http_method IS '请求方式';
COMMENT ON COLUMN biz.customer_operate_log.customer_id IS '用户ID';
COMMENT ON COLUMN biz.customer_operate_log.username IS '用户名';
COMMENT ON COLUMN biz.customer_operate_log.http_url IS '请求URL';
COMMENT ON COLUMN biz.customer_operate_log.oper_ip IS '主机地址';
COMMENT ON COLUMN biz.customer_operate_log.oper_location IS '操作地点';
COMMENT ON COLUMN biz.customer_operate_log.oper_param IS '请求参数';
COMMENT ON COLUMN biz.customer_operate_log.json_result IS '返回参数';
COMMENT ON COLUMN biz.customer_operate_log.success IS '操作状态';
COMMENT ON COLUMN biz.customer_operate_log.error_msg IS '错误消息';
COMMENT ON COLUMN biz.customer_operate_log.cost_time IS '耗时（毫秒）';
COMMENT ON COLUMN biz.customer_operate_log.oper_time IS '操作时间';


--
-- TOC entry 217 (class 1259 OID 26130)
-- Name: announcement_info; Type: TABLE; Schema: biz; Owner: postgres
--

CREATE TABLE biz.announcement_info (
    announcement_id bigint NOT NULL,
    title character varying(30) NOT NULL,
    content text NOT NULL,
    status character(1) NOT NULL,
    creator_id bigint NOT NULL,
    updater_id bigint,
    create_at timestamp(0) with time zone,
    update_at timestamp(0) with time zone,
    hint character varying(10) DEFAULT ''::character varying NOT NULL,
    deleted boolean DEFAULT false NOT NULL
);


ALTER TABLE biz.announcement_info OWNER TO postgres;

--
-- TOC entry 3430 (class 0 OID 0)
-- Dependencies: 217
-- Name: TABLE announcement_info; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON TABLE biz.announcement_info IS '系统消息表';


--
-- TOC entry 3431 (class 0 OID 0)
-- Dependencies: 217
-- Name: COLUMN announcement_info.announcement_id; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.announcement_info.announcement_id IS '公告ID';


--
-- TOC entry 3432 (class 0 OID 0)
-- Dependencies: 217
-- Name: COLUMN announcement_info.title; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.announcement_info.title IS '公告标题';


--
-- TOC entry 3433 (class 0 OID 0)
-- Dependencies: 217
-- Name: COLUMN announcement_info.content; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.announcement_info.content IS '公告内容';


--
-- TOC entry 3434 (class 0 OID 0)
-- Dependencies: 217
-- Name: COLUMN announcement_info.status; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.announcement_info.status IS '公告状态（0草稿 1关闭 2公开）';


--
-- TOC entry 3435 (class 0 OID 0)
-- Dependencies: 217
-- Name: COLUMN announcement_info.creator_id; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.announcement_info.creator_id IS '创建者ID';


--
-- TOC entry 3436 (class 0 OID 0)
-- Dependencies: 217
-- Name: COLUMN announcement_info.updater_id; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.announcement_info.updater_id IS '更新者ID';


--
-- TOC entry 3437 (class 0 OID 0)
-- Dependencies: 217
-- Name: COLUMN announcement_info.create_at; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.announcement_info.create_at IS '创建时间';


--
-- TOC entry 3438 (class 0 OID 0)
-- Dependencies: 217
-- Name: COLUMN announcement_info.update_at; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.announcement_info.update_at IS '更新时间';


--
-- TOC entry 3439 (class 0 OID 0)
-- Dependencies: 217
-- Name: COLUMN announcement_info.hint; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.announcement_info.hint IS '标签';


--
-- TOC entry 225 (class 1259 OID 26170)
-- Name: chat_persist_info; Type: TABLE; Schema: biz; Owner: postgres
--

CREATE TABLE biz.chat_persist_info (
    chat_id bigint NOT NULL,
    receiver_id bigint NOT NULL,
    sender_id bigint NOT NULL,
    send_time timestamp(0) with time zone NOT NULL,
    receive_time timestamp(0) with time zone,
    read boolean DEFAULT false NOT NULL,
    resource_type character varying(10) NOT NULL,
    content bytea NOT NULL,
    media_format character varying(10) NOT NULL,
    creator_id bigint NOT NULL,
    updater_id bigint,
    create_at timestamp(0) with time zone NOT NULL,
    update_at timestamp(0) with time zone,
    hint character varying(10) DEFAULT ''::character varying NOT NULL,
    deleted boolean DEFAULT false NOT NULL
);


ALTER TABLE biz.chat_persist_info OWNER TO postgres;

--
-- TOC entry 3440 (class 0 OID 0)
-- Dependencies: 225
-- Name: TABLE chat_persist_info; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON TABLE biz.chat_persist_info IS '聊天持久化消息';


--
-- TOC entry 3441 (class 0 OID 0)
-- Dependencies: 225
-- Name: COLUMN chat_persist_info.receiver_id; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.chat_persist_info.receiver_id IS '接收者ID';


--
-- TOC entry 3442 (class 0 OID 0)
-- Dependencies: 225
-- Name: COLUMN chat_persist_info.sender_id; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.chat_persist_info.sender_id IS '发送者ID';


--
-- TOC entry 3443 (class 0 OID 0)
-- Dependencies: 225
-- Name: COLUMN chat_persist_info.send_time; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.chat_persist_info.send_time IS '发送时间';


--
-- TOC entry 3444 (class 0 OID 0)
-- Dependencies: 225
-- Name: COLUMN chat_persist_info.receive_time; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.chat_persist_info.receive_time IS '接收时间';


--
-- TOC entry 3445 (class 0 OID 0)
-- Dependencies: 225
-- Name: COLUMN chat_persist_info.read; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.chat_persist_info.read IS '是否已读';


--
-- TOC entry 3446 (class 0 OID 0)
-- Dependencies: 225
-- Name: COLUMN chat_persist_info.resource_type; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.chat_persist_info.resource_type IS ' text/image/video/file';


--
-- TOC entry 3447 (class 0 OID 0)
-- Dependencies: 225
-- Name: COLUMN chat_persist_info.content; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.chat_persist_info.content IS ' 统一的内容字段，存储文本或二进制数据';


--
-- TOC entry 3448 (class 0 OID 0)
-- Dependencies: 225
-- Name: COLUMN chat_persist_info.media_format; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.chat_persist_info.media_format IS '媒体格式(png/jpg/mp4/txt等)';


--
-- TOC entry 218 (class 1259 OID 26136)
-- Name: config_info; Type: TABLE; Schema: biz; Owner: postgres
--

CREATE TABLE biz.config_info (
    config_id bigint NOT NULL,
    config_name character varying(50) NOT NULL,
    config_key character varying(50) NOT NULL,
    config_value text NOT NULL,
    config_type character(1) NOT NULL,
    built_in boolean DEFAULT false NOT NULL,
    creator_id bigint NOT NULL,
    create_at timestamp(0) with time zone NOT NULL,
    updater_id bigint,
    update_at timestamp(0) with time zone,
    hint character varying(10) DEFAULT ''::character varying NOT NULL,
    deleted boolean DEFAULT false NOT NULL
);


ALTER TABLE biz.config_info OWNER TO postgres;

--
-- TOC entry 3449 (class 0 OID 0)
-- Dependencies: 218
-- Name: TABLE config_info; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON TABLE biz.config_info IS '参数配置表';


--
-- TOC entry 3450 (class 0 OID 0)
-- Dependencies: 218
-- Name: COLUMN config_info.config_id; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.config_info.config_id IS '参数主键';


--
-- TOC entry 3451 (class 0 OID 0)
-- Dependencies: 218
-- Name: COLUMN config_info.config_name; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.config_info.config_name IS '参数名称';


--
-- TOC entry 3452 (class 0 OID 0)
-- Dependencies: 218
-- Name: COLUMN config_info.config_key; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.config_info.config_key IS '参数键名';


--
-- TOC entry 3453 (class 0 OID 0)
-- Dependencies: 218
-- Name: COLUMN config_info.config_value; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.config_info.config_value IS '参数键值';


--
-- TOC entry 3454 (class 0 OID 0)
-- Dependencies: 218
-- Name: COLUMN config_info.config_type; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.config_info.config_type IS '配置类型';


--
-- TOC entry 3455 (class 0 OID 0)
-- Dependencies: 218
-- Name: COLUMN config_info.built_in; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.config_info.built_in IS '是否内置';


--
-- TOC entry 3456 (class 0 OID 0)
-- Dependencies: 218
-- Name: COLUMN config_info.hint; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.config_info.hint IS '标签';


--
-- TOC entry 219 (class 1259 OID 26143)
-- Name: dictionary_info; Type: TABLE; Schema: biz; Owner: postgres
--

CREATE TABLE biz.dictionary_info (
    dict_id bigint NOT NULL,
    dict_key character varying(32) NOT NULL,
    dict_name character varying(32) NOT NULL,
    dict_value jsonb NOT NULL,
    remark text,
    creator_id bigint NOT NULL,
    updater_id bigint,
    create_at timestamp(0) with time zone NOT NULL,
    update_at timestamp(0) with time zone,
    hint character varying(10) DEFAULT ''::character varying NOT NULL,
    deleted boolean DEFAULT false NOT NULL
);


ALTER TABLE biz.dictionary_info OWNER TO postgres;

--
-- TOC entry 3457 (class 0 OID 0)
-- Dependencies: 219
-- Name: COLUMN dictionary_info.dict_id; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.dictionary_info.dict_id IS '字典主键';


--
-- TOC entry 3458 (class 0 OID 0)
-- Dependencies: 219
-- Name: COLUMN dictionary_info.dict_key; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.dictionary_info.dict_key IS '字典键';


--
-- TOC entry 3459 (class 0 OID 0)
-- Dependencies: 219
-- Name: COLUMN dictionary_info.dict_name; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.dictionary_info.dict_name IS '字典名称';


--
-- TOC entry 3460 (class 0 OID 0)
-- Dependencies: 219
-- Name: COLUMN dictionary_info.dict_value; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.dictionary_info.dict_value IS '字典键值';


--
-- TOC entry 3461 (class 0 OID 0)
-- Dependencies: 219
-- Name: COLUMN dictionary_info.remark; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.dictionary_info.remark IS '备注';


--
-- TOC entry 3462 (class 0 OID 0)
-- Dependencies: 219
-- Name: COLUMN dictionary_info.creator_id; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.dictionary_info.creator_id IS '创建者';


--
-- TOC entry 3463 (class 0 OID 0)
-- Dependencies: 219
-- Name: COLUMN dictionary_info.updater_id; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.dictionary_info.updater_id IS '更新者';


--
-- TOC entry 3464 (class 0 OID 0)
-- Dependencies: 219
-- Name: COLUMN dictionary_info.create_at; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.dictionary_info.create_at IS '创建时间';


--
-- TOC entry 3465 (class 0 OID 0)
-- Dependencies: 219
-- Name: COLUMN dictionary_info.update_at; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.dictionary_info.update_at IS '更新时间';


--
-- TOC entry 3466 (class 0 OID 0)
-- Dependencies: 219
-- Name: COLUMN dictionary_info.hint; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.dictionary_info.hint IS '标签';


--
-- TOC entry 220 (class 1259 OID 26149)
-- Name: notification_info; Type: TABLE; Schema: biz; Owner: postgres
--

CREATE TABLE biz.notification_info (
    notification_id bigint NOT NULL,
    notification_type character(1) NOT NULL,
    title character varying(32) NOT NULL,
    content text NOT NULL,
    create_at timestamp(0) with time zone NOT NULL,
    creator_id bigint NOT NULL,
    updater_id bigint,
    update_at timestamp(0) with time zone,
    hint character varying(10) DEFAULT ''::character varying NOT NULL,
    deleted boolean DEFAULT false NOT NULL
);


ALTER TABLE biz.notification_info OWNER TO postgres;

--
-- TOC entry 3467 (class 0 OID 0)
-- Dependencies: 220
-- Name: TABLE notification_info; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON TABLE biz.notification_info IS '系统通知表';


--
-- TOC entry 3468 (class 0 OID 0)
-- Dependencies: 220
-- Name: COLUMN notification_info.notification_type; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.notification_info.notification_type IS '通知类型（0系统通知 1设备告警）';


--
-- TOC entry 3469 (class 0 OID 0)
-- Dependencies: 220
-- Name: COLUMN notification_info.title; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.notification_info.title IS '标题';


--
-- TOC entry 3470 (class 0 OID 0)
-- Dependencies: 220
-- Name: COLUMN notification_info.content; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.notification_info.content IS '通知内容';


--
-- TOC entry 3471 (class 0 OID 0)
-- Dependencies: 220
-- Name: COLUMN notification_info.create_at; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.notification_info.create_at IS '创建时间';


--
-- TOC entry 3472 (class 0 OID 0)
-- Dependencies: 220
-- Name: COLUMN notification_info.creator_id; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.notification_info.creator_id IS '创建者ID';


--
-- TOC entry 221 (class 1259 OID 26154)
-- Name: notification_to_admin; Type: TABLE; Schema: biz; Owner: postgres
--

CREATE TABLE biz.notification_to_admin (
    notification_id bigint NOT NULL,
    admin_id bigint NOT NULL,
    read boolean NOT NULL
);


ALTER TABLE biz.notification_to_admin OWNER TO postgres;

--
-- TOC entry 3473 (class 0 OID 0)
-- Dependencies: 221
-- Name: TABLE notification_to_admin; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON TABLE biz.notification_to_admin IS '通知发送表';


--
-- TOC entry 3474 (class 0 OID 0)
-- Dependencies: 221
-- Name: COLUMN notification_to_admin.notification_id; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.notification_to_admin.notification_id IS '通知ID';


--
-- TOC entry 3475 (class 0 OID 0)
-- Dependencies: 221
-- Name: COLUMN notification_to_admin.admin_id; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.notification_to_admin.admin_id IS '管理员ID';


--
-- TOC entry 3476 (class 0 OID 0)
-- Dependencies: 221
-- Name: COLUMN notification_to_admin.read; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.notification_to_admin.read IS '是否已读';


--
-- TOC entry 222 (class 1259 OID 26157)
-- Name: notification_to_tenant; Type: TABLE; Schema: biz; Owner: postgres
--

CREATE TABLE biz.notification_to_tenant (
    notification_id bigint NOT NULL,
    member_id bigint NOT NULL,
    read boolean NOT NULL
);


ALTER TABLE biz.notification_to_tenant OWNER TO postgres;

--
-- TOC entry 3477 (class 0 OID 0)
-- Dependencies: 222
-- Name: TABLE notification_to_tenant; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON TABLE biz.notification_to_tenant IS '通知发送表';


--
-- TOC entry 3478 (class 0 OID 0)
-- Dependencies: 222
-- Name: COLUMN notification_to_tenant.notification_id; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.notification_to_tenant.notification_id IS '通知ID';


--
-- TOC entry 3479 (class 0 OID 0)
-- Dependencies: 222
-- Name: COLUMN notification_to_tenant.member_id; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.notification_to_tenant.member_id IS '租户成员ID';


--
-- TOC entry 3480 (class 0 OID 0)
-- Dependencies: 222
-- Name: COLUMN notification_to_tenant.read; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.notification_to_tenant.read IS '是否已读';


--
-- TOC entry 223 (class 1259 OID 26160)
-- Name: tenant_login_log; Type: TABLE; Schema: biz; Owner: postgres
--

CREATE TABLE biz.tenant_login_log (
    log_id bigint NOT NULL,
    tenant_id bigint NOT NULL,
    member_id bigint NOT NULL,
    username character varying(32) NOT NULL,
    login_type character varying(32) NOT NULL,
    success boolean NOT NULL,
    error_msg character varying(50) NOT NULL,
    login_ip inet NOT NULL,
    login_location character varying(50) NOT NULL,
    login_time timestamp(0) with time zone NOT NULL,
    trace_id character varying(64) NOT NULL,
    user_agent character varying(200) NOT NULL
);


ALTER TABLE biz.tenant_login_log OWNER TO postgres;

--
-- TOC entry 3481 (class 0 OID 0)
-- Dependencies: 223
-- Name: TABLE tenant_login_log; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON TABLE biz.tenant_login_log IS '租户登录日志';


--
-- TOC entry 3482 (class 0 OID 0)
-- Dependencies: 223
-- Name: COLUMN tenant_login_log.tenant_id; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.tenant_login_log.tenant_id IS '租户ID';


--
-- TOC entry 3483 (class 0 OID 0)
-- Dependencies: 223
-- Name: COLUMN tenant_login_log.member_id; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.tenant_login_log.member_id IS '用户ID';


--
-- TOC entry 3484 (class 0 OID 0)
-- Dependencies: 223
-- Name: COLUMN tenant_login_log.username; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.tenant_login_log.username IS '用户名';


--
-- TOC entry 3485 (class 0 OID 0)
-- Dependencies: 223
-- Name: COLUMN tenant_login_log.login_type; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.tenant_login_log.login_type IS '登录类型';


--
-- TOC entry 3486 (class 0 OID 0)
-- Dependencies: 223
-- Name: COLUMN tenant_login_log.success; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.tenant_login_log.success IS '登录状态';


--
-- TOC entry 3487 (class 0 OID 0)
-- Dependencies: 223
-- Name: COLUMN tenant_login_log.error_msg; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.tenant_login_log.error_msg IS '失败信息';


--
-- TOC entry 3488 (class 0 OID 0)
-- Dependencies: 223
-- Name: COLUMN tenant_login_log.login_ip; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.tenant_login_log.login_ip IS '登录ip';


--
-- TOC entry 3489 (class 0 OID 0)
-- Dependencies: 223
-- Name: COLUMN tenant_login_log.login_location; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.tenant_login_log.login_location IS '登录地址';


--
-- TOC entry 3490 (class 0 OID 0)
-- Dependencies: 223
-- Name: COLUMN tenant_login_log.login_time; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.tenant_login_log.login_time IS '登录时间';


--
-- TOC entry 224 (class 1259 OID 26165)
-- Name: tenant_operate_log; Type: TABLE; Schema: biz; Owner: postgres
--

CREATE TABLE biz.tenant_operate_log (
    oper_id bigint NOT NULL,
    title character varying(50) NOT NULL,
    sub_title character varying(50) NOT NULL,
    oper_type character(1) NOT NULL,
    method character varying(200),
    http_method character varying(10),
    tenant_id bigint NOT NULL,
    member_id bigint NOT NULL,
    username character varying(50) NOT NULL,
    http_url character varying(500),
    oper_ip inet,
    oper_location character varying(100),
    oper_param text,
    json_result text,
    success boolean NOT NULL,
    error_msg text,
    cost_time bigint,
    oper_time timestamp(3) with time zone NOT NULL
);


ALTER TABLE biz.tenant_operate_log OWNER TO postgres;

--
-- TOC entry 3491 (class 0 OID 0)
-- Dependencies: 224
-- Name: TABLE tenant_operate_log; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON TABLE biz.tenant_operate_log IS '租户操作日志';


--
-- TOC entry 3492 (class 0 OID 0)
-- Dependencies: 224
-- Name: COLUMN tenant_operate_log.oper_id; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.tenant_operate_log.oper_id IS '日志主键';


--
-- TOC entry 3493 (class 0 OID 0)
-- Dependencies: 224
-- Name: COLUMN tenant_operate_log.title; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.tenant_operate_log.title IS '模块标题';


--
-- TOC entry 3494 (class 0 OID 0)
-- Dependencies: 224
-- Name: COLUMN tenant_operate_log.sub_title; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.tenant_operate_log.sub_title IS '子标题';


--
-- TOC entry 3495 (class 0 OID 0)
-- Dependencies: 224
-- Name: COLUMN tenant_operate_log.oper_type; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.tenant_operate_log.oper_type IS '操作类型（0其他 1新增 2修改 3删除 4授权 5导入导出）';


--
-- TOC entry 3496 (class 0 OID 0)
-- Dependencies: 224
-- Name: COLUMN tenant_operate_log.method; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.tenant_operate_log.method IS '方法名称';


--
-- TOC entry 3497 (class 0 OID 0)
-- Dependencies: 224
-- Name: COLUMN tenant_operate_log.http_method; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.tenant_operate_log.http_method IS '请求方式';


--
-- TOC entry 3498 (class 0 OID 0)
-- Dependencies: 224
-- Name: COLUMN tenant_operate_log.tenant_id; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.tenant_operate_log.tenant_id IS '租户ID';


--
-- TOC entry 3499 (class 0 OID 0)
-- Dependencies: 224
-- Name: COLUMN tenant_operate_log.member_id; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.tenant_operate_log.member_id IS '职工ID';


--
-- TOC entry 3500 (class 0 OID 0)
-- Dependencies: 224
-- Name: COLUMN tenant_operate_log.username; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.tenant_operate_log.username IS '用户名';


--
-- TOC entry 3501 (class 0 OID 0)
-- Dependencies: 224
-- Name: COLUMN tenant_operate_log.http_url; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.tenant_operate_log.http_url IS '请求URL';


--
-- TOC entry 3502 (class 0 OID 0)
-- Dependencies: 224
-- Name: COLUMN tenant_operate_log.oper_ip; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.tenant_operate_log.oper_ip IS '主机地址';


--
-- TOC entry 3503 (class 0 OID 0)
-- Dependencies: 224
-- Name: COLUMN tenant_operate_log.oper_location; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.tenant_operate_log.oper_location IS '操作地点';


--
-- TOC entry 3504 (class 0 OID 0)
-- Dependencies: 224
-- Name: COLUMN tenant_operate_log.oper_param; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.tenant_operate_log.oper_param IS '请求参数';


--
-- TOC entry 3505 (class 0 OID 0)
-- Dependencies: 224
-- Name: COLUMN tenant_operate_log.json_result; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.tenant_operate_log.json_result IS '返回参数';


--
-- TOC entry 3506 (class 0 OID 0)
-- Dependencies: 224
-- Name: COLUMN tenant_operate_log.success; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.tenant_operate_log.success IS '操作状态';


--
-- TOC entry 3507 (class 0 OID 0)
-- Dependencies: 224
-- Name: COLUMN tenant_operate_log.error_msg; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.tenant_operate_log.error_msg IS '错误消息';


--
-- TOC entry 3508 (class 0 OID 0)
-- Dependencies: 224
-- Name: COLUMN tenant_operate_log.oper_time; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.tenant_operate_log.oper_time IS '操作时间';


--
-- TOC entry 3388 (class 0 OID 26120)
-- Dependencies: 215
-- Data for Name: admin_login_log; Type: TABLE DATA; Schema: biz; Owner: postgres
--

COPY biz.admin_login_log (log_id, admin_id, username, login_type, success, error_msg, login_ip, login_location, login_time, trace_id, user_agent) FROM stdin;
\.


--
-- TOC entry 3389 (class 0 OID 26125)
-- Dependencies: 216
-- Data for Name: admin_operate_log; Type: TABLE DATA; Schema: biz; Owner: postgres
--

COPY biz.admin_operate_log (oper_id, title, sub_title, oper_type, method, http_method, admin_id, username, http_url, oper_ip, oper_location, oper_param, json_result, success, error_msg, oper_time) FROM stdin;
\.


--
-- Data for Name: customer_operate_log; Type: TABLE DATA; Schema: biz; Owner: postgres
--

COPY biz.customer_operate_log (oper_id, title, sub_title, oper_type, method, http_method, customer_id, username, http_url, oper_ip, oper_location, oper_param, json_result, success, error_msg, oper_time) FROM stdin;
\.


--
-- TOC entry 3390 (class 0 OID 26130)
-- Dependencies: 217
-- Data for Name: announcement_info; Type: TABLE DATA; Schema: biz; Owner: postgres
--

COPY biz.announcement_info (announcement_id, title, content, status, creator_id, updater_id, create_at, update_at, hint, deleted) FROM stdin;
\.


--
-- TOC entry 3398 (class 0 OID 26170)
-- Dependencies: 225
-- Data for Name: chat_persist_info; Type: TABLE DATA; Schema: biz; Owner: postgres
--

COPY biz.chat_persist_info (chat_id, receiver_id, sender_id, send_time, receive_time, read, resource_type, content, media_format, creator_id, updater_id, create_at, update_at, hint, deleted) FROM stdin;
\.


--
-- TOC entry 3391 (class 0 OID 26136)
-- Dependencies: 218
-- Data for Name: config_info; Type: TABLE DATA; Schema: biz; Owner: postgres
--

COPY biz.config_info (config_id, config_name, config_key, config_value, config_type, built_in, creator_id, create_at, updater_id, update_at, hint, deleted) FROM stdin;
1	用户管理-账号初始密码	sys.user.initPassword	123456	Y	f	1	2025-09-17 17:31:01+08	1	2025-09-25 15:44:21+08		f
\.


--
-- TOC entry 3392 (class 0 OID 26143)
-- Dependencies: 219
-- Data for Name: dictionary_info; Type: TABLE DATA; Schema: biz; Owner: postgres
--

COPY biz.dictionary_info (dict_id, dict_key, dict_name, dict_value, remark, creator_id, updater_id, create_at, update_at, hint, deleted) FROM stdin;
1905175932909101057	user_sex	用户性别	[{"label": "男", "value": "0", "tableCls": ""}, {"label": "女", "value": "1", "tableCls": ""}, {"label": "未知", "value": "2", "tableCls": ""}]	\N	1	1	2024-04-17 14:08:55+08	2024-11-20 14:29:21+08		f
1905175933034930178	menu_visible	菜单可见状态	[{"label": "显示", "value": "true", "tableCls": "primary"}, {"label": "隐藏", "value": "false", "tableCls": "danger"}]	\N	1	1	2024-04-17 14:08:55+08	2024-11-20 14:30:10+08		f
1905175933097844737	common_disable	是否禁用	[{"label": "正常", "value": "0", "tableCls": "primary"}, {"label": "停用", "value": "1", "tableCls": "danger"}]	\N	1	1	2024-04-17 14:08:55+08	2024-11-20 14:34:03+08		f
1905175933164953603	msg_type	消息类型	[{"label": "系统公告", "value": "1", "tableCls": "primary"}, {"label": "APP公告", "value": "2", "tableCls": "success"}]	\N	1	1	2024-04-17 14:08:55+08	2024-12-16 10:30:13+08		f
1905175933164953604	msg_status	消息状态	[{"label": "已发布", "value": "2", "tableCls": "primary"}, {"label": "草稿", "value": "0", "tableCls": "info"}, {"label": "关闭", "value": "1", "tableCls": "danger"}]	\N	1	1	2024-04-17 14:08:55+08	2024-12-16 10:30:17+08		f
1905175933227868164	online_status	设备连接状态	[{"label": "在线", "value": "true", "tableCls": "success"}, {"label": "离线", "value": "false", "tableCls": "info"}]	\N	1	1	2024-04-17 14:08:55+08	2024-12-09 11:28:08+08		f
1905175933290782723	data_scope	数据权限	[{"label": "全部数据权限", "value": "1", "tableCls": "default"}, {"label": "自定数据权限", "value": "2", "tableCls": "default"}, {"label": "本部门数据权限", "value": "3", "tableCls": "default"}, {"label": "本部门及以下数据权限", "value": "4", "tableCls": "default"}]	\N	1	1	2024-04-17 14:08:55+08	2024-11-20 14:27:20+08		f
1905175933425000451	wallet_record_type	钱包记录类型	[{"label": "收入", "value": "0", "tableCls": "primary"}, {"label": "支出", "value": "1", "tableCls": "danger"}]	\N	1	1	2024-11-25 16:32:20+08	2024-11-25 16:32:20+08		f
1905175933357891585	pay_type	支付方式	[{"label": "钱包支付", "value": "0", "tableCls": "info"}, {"label": "微信支付", "value": "1", "tableCls": "success"}, {"label": "支付宝", "value": "2", "tableCls": "primary"}]	\N	1	1	2024-04-17 14:08:55+08	2025-07-15 17:00:24+08		f
1905175933290782722	pay_certification_status	支付认证状态	[{"label": "未认证", "value": "0", "tableCls": "info"}, {"label": "微信支付", "value": "1", "tableCls": "success"}, {"label": "支付宝", "value": "2", "tableCls": "primary"}]	\N	1	1	2024-04-17 14:08:55+08	2025-07-15 17:00:46+08		f
1905175933290782724	pay_status	支付状态	[{"label": "支付成功", "value": "1", "tableCls": "success"}, {"label": "订单关闭", "value": "2", "tableCls": "info"}, {"label": "未支付", "value": "0", "tableCls": "primary"}, {"label": "支付异常", "value": "3", "tableCls": "danger"}]	\N	1	1	2024-04-17 14:08:55+08	2025-07-15 17:01:14+08		f
1905175933227868163	authorization_grant_types	授权类型	[{"label": "刷新模式", "value": "refresh_token", "tableCls": "primary"}, {"label": "客户端模式", "value": "client_credentials", "tableCls": "primary"}, {"label": "授权码模式", "value": "authorization_code", "tableCls": "primary"}, {"label": "token交换模式", "value": "urn:ietf:params:oauth:grant-type:token-exchange", "tableCls": "primary"}, {"label": "设备码模式", "value": "urn:ietf:params:oauth:grant-type:device_code", "tableCls": "primary"}]	\N	1	1	2024-04-17 14:08:55+08	2025-06-23 16:08:48+08		f
1905175933034930179	menu_type	菜单类型	[{"label": "目录", "value": "D", "tableCls": "info"}, {"label": "菜单", "value": "M", "tableCls": "primary"}, {"label": "按钮", "value": "B", "tableCls": "danger"}, {"label": "字段", "value": "F", "tableCls": "warning"}, {"label": "内链", "value": "I", "tableCls": ""}, {"label": "外链", "value": "O", "tableCls": ""}]	\N	1	1	2024-11-23 15:22:04+08	2025-09-05 16:08:01+08		f
1976213210015690754	menu_scope	菜单域	[{"label": "系统域", "value": "system", "tableCls": ""}, {"label": "租户域", "value": "tenant", "tableCls": ""}]	\N	1	1	2025-10-09 17:08:40+08	2025-10-09 17:08:40+08		f
1905175933227868161	operate_type	操作类型	[{"label": "其他", "value": "0", "tableCls": "info"}, {"label": "新增", "value": "1", "tableCls": "info"}, {"label": "修改", "value": "2", "tableCls": "info"}, {"label": "删除", "value": "3", "tableCls": "danger"}, {"label": "授权", "value": "4", "tableCls": "primary"}, {"label": "导出", "value": "5", "tableCls": "warning"}, {"label": "导入", "value": "6", "tableCls": "warning"}]	\N	1	1	2024-04-17 14:08:55+08	2024-11-20 14:26:30+08		f
1905175933227868162	operate_status	操作状态	[{"label": "成功", "value": "0", "tableCls": "primary"}, {"label": "失败", "value": "1", "tableCls": "danger"}]	\N	1	1	2024-04-17 14:08:55+08	2024-11-20 14:13:47+08		f
\.


--
-- TOC entry 3393 (class 0 OID 26149)
-- Dependencies: 220
-- Data for Name: notification_info; Type: TABLE DATA; Schema: biz; Owner: postgres
--

COPY biz.notification_info (notification_id, notification_type, title, content, create_at, creator_id, updater_id, update_at, hint, deleted) FROM stdin;
\.


--
-- TOC entry 3394 (class 0 OID 26154)
-- Dependencies: 221
-- Data for Name: notification_to_admin; Type: TABLE DATA; Schema: biz; Owner: postgres
--

COPY biz.notification_to_admin (notification_id, admin_id, read) FROM stdin;
\.


--
-- TOC entry 3395 (class 0 OID 26157)
-- Dependencies: 222
-- Data for Name: notification_to_tenant; Type: TABLE DATA; Schema: biz; Owner: postgres
--

COPY biz.notification_to_tenant (notification_id, member_id, read) FROM stdin;
\.


--
-- TOC entry 3396 (class 0 OID 26160)
-- Dependencies: 223
-- Data for Name: tenant_login_log; Type: TABLE DATA; Schema: biz; Owner: postgres
--

COPY biz.tenant_login_log (log_id, tenant_id, member_id, username, login_type, success, error_msg, login_ip, login_location, login_time, trace_id, user_agent) FROM stdin;
2039616960859942914	1910557183820165122	1910557183820165120	testadmin	password	t		172.16.8.59	 局域网	2026-04-02 16:12:31+08	20260402161230015-2-5218330	{"User-Agent":"Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/146.0.0.0 Safari/537.36 Edg/146.0.0.0"}
2039619680941584385	1910557183820165122	1910557183820165120	testadmin	password	f	商户已过期，请联系管理员	172.16.8.59	 局域网	2026-04-02 16:23:20+08	20260402162319581-24-4431240	{"User-Agent":"Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/146.0.0.0 Safari/537.36 Edg/146.0.0.0"}
2039875760288346113	1910557183820165122	1910557183820165120	testadmin	password	f	商户已过期，请联系管理员	172.16.8.59	 局域网	2026-04-03 09:20:54+08	20260403092053754-3-1020537	{"User-Agent":"Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/146.0.0.0 Safari/537.36 Edg/146.0.0.0"}
2039876053499555842	1910557183820165122	1910557183820165120	testadmin	password	t		172.16.8.59	 局域网	2026-04-03 09:22:04+08	20260403092204414-6-9873025	{"User-Agent":"Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/146.0.0.0 Safari/537.36 Edg/146.0.0.0"}
2039991299685998593	1910557183820165122	1910557183820165120	testadmin	password	t		172.16.8.59	 局域网	2026-04-03 17:00:01+08	20260403170000730-9-3938299	{"User-Agent":"Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/146.0.0.0 Safari/537.36 Edg/146.0.0.0"}
2043603881446875137	1910557183820165122	1910557183820165120	testadmin	password	t		172.16.8.59	 局域网	2026-04-13 16:15:07+08	20260413161506773-10-5080856	Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/146.0.0.0 Safari/537.36 Edg/146.0.0.0
2043605997569712130	1910557183820165122	1910557183820165120	testadmin	password	t		172.16.8.59	 局域网	2026-04-13 16:23:32+08	20260413162332423-28-8325869	Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/146.0.0.0 Safari/537.36 Edg/146.0.0.0
2043607731578871810	1910557183820165122	1910557183820165120	testadmin	password	t		172.16.8.59	 局域网	2026-04-13 16:30:26+08	20260413163025840-41-9816497	Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/146.0.0.0 Safari/537.36 Edg/146.0.0.0
2043627964460965889	1910557183820165122	1910557183820165120	testadmin	password	t		172.16.8.59	 局域网	2026-04-13 17:50:50+08	20260413175049718-196-4803573	Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/146.0.0.0 Safari/537.36 Edg/146.0.0.0
2043860782856294401	1910557183820165122	1910557183820165120	testadmin	password	t		172.16.8.59	 局域网	2026-04-14 09:15:58+08	20260414091557958-93-6827529	Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/146.0.0.0 Safari/537.36 Edg/146.0.0.0
2043892825145315330	1910557183820165122	1910557183820165120	testadmin	password	t		172.16.8.59	 局域网	2026-04-14 11:23:17+08	20260414112316085-637-4873955	Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/146.0.0.0 Safari/537.36 Edg/146.0.0.0
2043898960040529921	1910557183820165122	1910557183820165120	testadmin	password	t		172.16.8.59	 局域网	2026-04-14 11:47:40+08	20260414114736872-828-8753834	Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/146.0.0.0 Safari/537.36 Edg/146.0.0.0
2043934154957017090	1910557183820165122	1910557183820165120	testadmin	password	t		172.16.8.59	 局域网	2026-04-14 14:07:30+08	20260414140728845-2-4192942	Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/146.0.0.0 Safari/537.36 Edg/146.0.0.0
2043934531546796033	1910557183820165122	1910557183820165120	testadmin	password	t		172.16.8.59	 局域网	2026-04-14 14:09:01+08	20260414140900999-28-6897970	Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/146.0.0.0 Safari/537.36 Edg/146.0.0.0
\.


--
-- TOC entry 3397 (class 0 OID 26165)
-- Dependencies: 224
-- Data for Name: tenant_operate_log; Type: TABLE DATA; Schema: biz; Owner: postgres
--

COPY biz.tenant_operate_log (oper_id, title, sub_title, oper_type, method, http_method, tenant_id, member_id, username, http_url, oper_ip, oper_location, oper_param, json_result, success, error_msg, oper_time) FROM stdin;
\.


--
-- TOC entry 3219 (class 2606 OID 26177)
-- Name: admin_login_log admin_login_log_pkey; Type: CONSTRAINT; Schema: biz; Owner: postgres
--

ALTER TABLE ONLY biz.admin_login_log
    ADD CONSTRAINT admin_login_log_pkey PRIMARY KEY (log_id);


--
-- TOC entry 3222 (class 2606 OID 26179)
-- Name: admin_operate_log admin_operate_log_pkey; Type: CONSTRAINT; Schema: biz; Owner: postgres
--

ALTER TABLE ONLY biz.admin_operate_log
    ADD CONSTRAINT admin_operate_log_pkey PRIMARY KEY (oper_id);


--
-- Name: customer_operate_log customer_operate_log_pkey; Type: CONSTRAINT; Schema: biz; Owner: postgres
--

ALTER TABLE ONLY biz.customer_operate_log
    ADD CONSTRAINT customer_operate_log_pkey PRIMARY KEY (oper_id);


--
-- TOC entry 3225 (class 2606 OID 26181)
-- Name: announcement_info announcement_info_pkey; Type: CONSTRAINT; Schema: biz; Owner: postgres
--

ALTER TABLE ONLY biz.announcement_info
    ADD CONSTRAINT announcement_info_pkey PRIMARY KEY (announcement_id);


--
-- TOC entry 3245 (class 2606 OID 26346)
-- Name: chat_persist_info chat_persist_info_pkey; Type: CONSTRAINT; Schema: biz; Owner: postgres
--

ALTER TABLE ONLY biz.chat_persist_info
    ADD CONSTRAINT chat_persist_info_pkey PRIMARY KEY (chat_id);


--
-- TOC entry 3227 (class 2606 OID 26183)
-- Name: config_info config_info_pkey; Type: CONSTRAINT; Schema: biz; Owner: postgres
--

ALTER TABLE ONLY biz.config_info
    ADD CONSTRAINT config_info_pkey PRIMARY KEY (config_id);


--
-- TOC entry 3230 (class 2606 OID 26185)
-- Name: dictionary_info dictionary_info_pkey; Type: CONSTRAINT; Schema: biz; Owner: postgres
--

ALTER TABLE ONLY biz.dictionary_info
    ADD CONSTRAINT dictionary_info_pkey PRIMARY KEY (dict_id);


--
-- TOC entry 3233 (class 2606 OID 26187)
-- Name: notification_info notification_info_pkey; Type: CONSTRAINT; Schema: biz; Owner: postgres
--

ALTER TABLE ONLY biz.notification_info
    ADD CONSTRAINT notification_info_pkey PRIMARY KEY (notification_id);


--
-- TOC entry 3235 (class 2606 OID 26189)
-- Name: notification_to_admin notification_to_admin_pkey; Type: CONSTRAINT; Schema: biz; Owner: postgres
--

ALTER TABLE ONLY biz.notification_to_admin
    ADD CONSTRAINT notification_to_admin_pkey PRIMARY KEY (notification_id, admin_id);


--
-- TOC entry 3237 (class 2606 OID 26191)
-- Name: notification_to_tenant notification_to_tenant_pkey; Type: CONSTRAINT; Schema: biz; Owner: postgres
--

ALTER TABLE ONLY biz.notification_to_tenant
    ADD CONSTRAINT notification_to_tenant_pkey PRIMARY KEY (notification_id, member_id);


--
-- TOC entry 3240 (class 2606 OID 26193)
-- Name: tenant_login_log tenant_login_log_pkey; Type: CONSTRAINT; Schema: biz; Owner: postgres
--

ALTER TABLE ONLY biz.tenant_login_log
    ADD CONSTRAINT tenant_login_log_pkey PRIMARY KEY (log_id);


--
-- TOC entry 3243 (class 2606 OID 26195)
-- Name: tenant_operate_log tenant_operate_log_pkey; Type: CONSTRAINT; Schema: biz; Owner: postgres
--

ALTER TABLE ONLY biz.tenant_operate_log
    ADD CONSTRAINT tenant_operate_log_pkey PRIMARY KEY (oper_id);


--
-- TOC entry 3220 (class 1259 OID 26198)
-- Name: idx_admin_login_log_login_time; Type: INDEX; Schema: biz; Owner: postgres
--

CREATE INDEX idx_admin_login_log_login_time ON biz.admin_login_log USING brin (login_time);


--
-- TOC entry 3223 (class 1259 OID 26199)
-- Name: idx_admin_operate_log_oper_time; Type: INDEX; Schema: biz; Owner: postgres
--

CREATE INDEX idx_admin_operate_log_oper_time ON biz.admin_operate_log USING brin (oper_time);


--
-- Name: idx_customer_operate_log_oper_time; Type: INDEX; Schema: biz; Owner: postgres
--

CREATE INDEX idx_customer_operate_log_oper_time ON biz.customer_operate_log USING brin (oper_time);


--
-- TOC entry 3238 (class 1259 OID 26200)
-- Name: idx_tenant_login_log_login_time; Type: INDEX; Schema: biz; Owner: postgres
--

CREATE INDEX idx_tenant_login_log_login_time ON biz.tenant_login_log USING brin (login_time);


--
-- TOC entry 3241 (class 1259 OID 26201)
-- Name: idx_tenant_operate_log_oper_time; Type: INDEX; Schema: biz; Owner: postgres
--

CREATE INDEX idx_tenant_operate_log_oper_time ON biz.tenant_operate_log USING brin (oper_time);


--
-- TOC entry 3228 (class 1259 OID 26202)
-- Name: uk_config_info_config_key; Type: INDEX; Schema: biz; Owner: postgres
--

CREATE UNIQUE INDEX uk_config_info_config_key ON biz.config_info USING btree (config_key) WHERE deleted = false;


--
-- TOC entry 3231 (class 1259 OID 26203)
-- Name: uk_dictionary_info_dict_key; Type: INDEX; Schema: biz; Owner: postgres
--

CREATE UNIQUE INDEX uk_dictionary_info_dict_key ON biz.dictionary_info USING btree (dict_key) WHERE deleted = false;


-- Completed on 2026-05-07 10:10:36

--
-- PostgreSQL database dump complete
--
