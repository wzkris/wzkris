--
-- PostgreSQL database dump
--

-- Dumped from database version 15.13
-- Dumped by pg_dump version 15.13

-- Started on 2025-12-26 09:39:41

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
-- TOC entry 6 (class 2615 OID 16527)
-- Name: biz; Type: SCHEMA; Schema: -; Owner: postgres
--

CREATE SCHEMA biz;


ALTER SCHEMA biz OWNER TO postgres;

SET default_tablespace = '';

SET default_table_access_method = heap;

--
-- TOC entry 217 (class 1259 OID 16538)
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
    browser character varying(50) NOT NULL,
    os character varying(50) NOT NULL,
    login_time timestamp with time zone NOT NULL
);


ALTER TABLE biz.admin_login_log OWNER TO postgres;

--
-- TOC entry 3404 (class 0 OID 0)
-- Dependencies: 217
-- Name: TABLE admin_login_log; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON TABLE biz.admin_login_log IS '后台登录日志';


--
-- TOC entry 3405 (class 0 OID 0)
-- Dependencies: 217
-- Name: COLUMN admin_login_log.admin_id; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.admin_login_log.admin_id IS '用户ID';


--
-- TOC entry 3406 (class 0 OID 0)
-- Dependencies: 217
-- Name: COLUMN admin_login_log.username; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.admin_login_log.username IS '用户名';


--
-- TOC entry 3407 (class 0 OID 0)
-- Dependencies: 217
-- Name: COLUMN admin_login_log.login_type; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.admin_login_log.login_type IS '登录类型';


--
-- TOC entry 3408 (class 0 OID 0)
-- Dependencies: 217
-- Name: COLUMN admin_login_log.success; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.admin_login_log.success IS '登录状态';


--
-- TOC entry 3409 (class 0 OID 0)
-- Dependencies: 217
-- Name: COLUMN admin_login_log.error_msg; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.admin_login_log.error_msg IS '失败信息';


--
-- TOC entry 3410 (class 0 OID 0)
-- Dependencies: 217
-- Name: COLUMN admin_login_log.login_ip; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.admin_login_log.login_ip IS '登录ip';


--
-- TOC entry 3411 (class 0 OID 0)
-- Dependencies: 217
-- Name: COLUMN admin_login_log.login_location; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.admin_login_log.login_location IS '登录地址';


--
-- TOC entry 3412 (class 0 OID 0)
-- Dependencies: 217
-- Name: COLUMN admin_login_log.browser; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.admin_login_log.browser IS '浏览器类型';


--
-- TOC entry 3413 (class 0 OID 0)
-- Dependencies: 217
-- Name: COLUMN admin_login_log.os; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.admin_login_log.os IS '操作系统';


--
-- TOC entry 3414 (class 0 OID 0)
-- Dependencies: 217
-- Name: COLUMN admin_login_log.login_time; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.admin_login_log.login_time IS '登录时间';


--
-- TOC entry 221 (class 1259 OID 16556)
-- Name: admin_operate_log; Type: TABLE; Schema: biz; Owner: postgres
--

CREATE TABLE biz.admin_operate_log (
    oper_id bigint NOT NULL,
    title character varying(20) NOT NULL,
    sub_title character varying(20) NOT NULL,
    oper_type character(1) NOT NULL,
    method character varying(100),
    request_method character varying(10),
    admin_id bigint NOT NULL,
    username character varying(50) NOT NULL,
    oper_url character varying(200) NOT NULL,
    oper_ip inet NOT NULL,
    oper_location character varying(100),
    oper_param text,
    json_result text,
    success boolean NOT NULL,
    error_msg text,
    oper_time timestamp with time zone NOT NULL
);


ALTER TABLE biz.admin_operate_log OWNER TO postgres;

--
-- TOC entry 3415 (class 0 OID 0)
-- Dependencies: 221
-- Name: TABLE admin_operate_log; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON TABLE biz.admin_operate_log IS '操作日志记录';


--
-- TOC entry 3416 (class 0 OID 0)
-- Dependencies: 221
-- Name: COLUMN admin_operate_log.oper_id; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.admin_operate_log.oper_id IS '日志主键';


--
-- TOC entry 3417 (class 0 OID 0)
-- Dependencies: 221
-- Name: COLUMN admin_operate_log.title; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.admin_operate_log.title IS '模块标题';


--
-- TOC entry 3418 (class 0 OID 0)
-- Dependencies: 221
-- Name: COLUMN admin_operate_log.sub_title; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.admin_operate_log.sub_title IS '子标题';


--
-- TOC entry 3419 (class 0 OID 0)
-- Dependencies: 221
-- Name: COLUMN admin_operate_log.oper_type; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.admin_operate_log.oper_type IS '操作类型（0其他 1新增 2修改 3删除）';


--
-- TOC entry 3420 (class 0 OID 0)
-- Dependencies: 221
-- Name: COLUMN admin_operate_log.method; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.admin_operate_log.method IS '方法名称';


--
-- TOC entry 3421 (class 0 OID 0)
-- Dependencies: 221
-- Name: COLUMN admin_operate_log.request_method; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.admin_operate_log.request_method IS '请求方式';


--
-- TOC entry 3422 (class 0 OID 0)
-- Dependencies: 221
-- Name: COLUMN admin_operate_log.admin_id; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.admin_operate_log.admin_id IS '用户ID';


--
-- TOC entry 3423 (class 0 OID 0)
-- Dependencies: 221
-- Name: COLUMN admin_operate_log.username; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.admin_operate_log.username IS '用户名';


--
-- TOC entry 3424 (class 0 OID 0)
-- Dependencies: 221
-- Name: COLUMN admin_operate_log.oper_url; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.admin_operate_log.oper_url IS '请求URL';


--
-- TOC entry 3425 (class 0 OID 0)
-- Dependencies: 221
-- Name: COLUMN admin_operate_log.oper_ip; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.admin_operate_log.oper_ip IS '主机地址';


--
-- TOC entry 3426 (class 0 OID 0)
-- Dependencies: 221
-- Name: COLUMN admin_operate_log.oper_location; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.admin_operate_log.oper_location IS '操作地点';


--
-- TOC entry 3427 (class 0 OID 0)
-- Dependencies: 221
-- Name: COLUMN admin_operate_log.oper_param; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.admin_operate_log.oper_param IS '请求参数';


--
-- TOC entry 3428 (class 0 OID 0)
-- Dependencies: 221
-- Name: COLUMN admin_operate_log.json_result; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.admin_operate_log.json_result IS '返回参数';


--
-- TOC entry 3429 (class 0 OID 0)
-- Dependencies: 221
-- Name: COLUMN admin_operate_log.success; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.admin_operate_log.success IS '操作状态';


--
-- TOC entry 3430 (class 0 OID 0)
-- Dependencies: 221
-- Name: COLUMN admin_operate_log.error_msg; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.admin_operate_log.error_msg IS '错误消息';


--
-- TOC entry 3431 (class 0 OID 0)
-- Dependencies: 221
-- Name: COLUMN admin_operate_log.oper_time; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.admin_operate_log.oper_time IS '操作时间';


--
-- TOC entry 218 (class 1259 OID 16543)
-- Name: announcement_info; Type: TABLE; Schema: biz; Owner: postgres
--

CREATE TABLE biz.announcement_info (
    announcement_id bigint NOT NULL,
    title character varying(30) NOT NULL,
    content text NOT NULL,
    status character(1) NOT NULL,
    creator_id bigint NOT NULL,
    updater_id bigint,
    create_at timestamp with time zone,
    update_at timestamp with time zone,
    hint character varying(10) DEFAULT ''::character varying NOT NULL
);


ALTER TABLE biz.announcement_info OWNER TO postgres;

--
-- TOC entry 3432 (class 0 OID 0)
-- Dependencies: 218
-- Name: TABLE announcement_info; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON TABLE biz.announcement_info IS '系统消息表';


--
-- TOC entry 3433 (class 0 OID 0)
-- Dependencies: 218
-- Name: COLUMN announcement_info.announcement_id; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.announcement_info.announcement_id IS '公告ID';


--
-- TOC entry 3434 (class 0 OID 0)
-- Dependencies: 218
-- Name: COLUMN announcement_info.title; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.announcement_info.title IS '公告标题';


--
-- TOC entry 3435 (class 0 OID 0)
-- Dependencies: 218
-- Name: COLUMN announcement_info.content; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.announcement_info.content IS '公告内容';


--
-- TOC entry 3436 (class 0 OID 0)
-- Dependencies: 218
-- Name: COLUMN announcement_info.status; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.announcement_info.status IS '公告状态（0草稿 1关闭 2公开）';


--
-- TOC entry 3437 (class 0 OID 0)
-- Dependencies: 218
-- Name: COLUMN announcement_info.creator_id; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.announcement_info.creator_id IS '创建者ID';


--
-- TOC entry 3438 (class 0 OID 0)
-- Dependencies: 218
-- Name: COLUMN announcement_info.updater_id; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.announcement_info.updater_id IS '更新者ID';


--
-- TOC entry 3439 (class 0 OID 0)
-- Dependencies: 218
-- Name: COLUMN announcement_info.create_at; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.announcement_info.create_at IS '创建时间';


--
-- TOC entry 3440 (class 0 OID 0)
-- Dependencies: 218
-- Name: COLUMN announcement_info.update_at; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.announcement_info.update_at IS '更新时间';


--
-- TOC entry 3441 (class 0 OID 0)
-- Dependencies: 218
-- Name: COLUMN announcement_info.hint; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.announcement_info.hint IS '标签';


--
-- TOC entry 215 (class 1259 OID 16528)
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
    create_at timestamp with time zone NOT NULL,
    updater_id bigint,
    update_at timestamp with time zone,
    hint character varying(10) DEFAULT ''::character varying NOT NULL
);


ALTER TABLE biz.config_info OWNER TO postgres;

--
-- TOC entry 3442 (class 0 OID 0)
-- Dependencies: 215
-- Name: TABLE config_info; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON TABLE biz.config_info IS '参数配置表';


--
-- TOC entry 3443 (class 0 OID 0)
-- Dependencies: 215
-- Name: COLUMN config_info.config_id; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.config_info.config_id IS '参数主键';


--
-- TOC entry 3444 (class 0 OID 0)
-- Dependencies: 215
-- Name: COLUMN config_info.config_name; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.config_info.config_name IS '参数名称';


--
-- TOC entry 3445 (class 0 OID 0)
-- Dependencies: 215
-- Name: COLUMN config_info.config_key; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.config_info.config_key IS '参数键名';


--
-- TOC entry 3446 (class 0 OID 0)
-- Dependencies: 215
-- Name: COLUMN config_info.config_value; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.config_info.config_value IS '参数键值';


--
-- TOC entry 3447 (class 0 OID 0)
-- Dependencies: 215
-- Name: COLUMN config_info.config_type; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.config_info.config_type IS '配置类型';


--
-- TOC entry 3448 (class 0 OID 0)
-- Dependencies: 215
-- Name: COLUMN config_info.built_in; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.config_info.built_in IS '是否内置';


--
-- TOC entry 3449 (class 0 OID 0)
-- Dependencies: 215
-- Name: COLUMN config_info.hint; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.config_info.hint IS '标签';


--
-- TOC entry 216 (class 1259 OID 16533)
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
    create_at timestamp(6) with time zone NOT NULL,
    update_at timestamp(6) with time zone,
    hint character varying(10) DEFAULT ''::character varying NOT NULL
);


ALTER TABLE biz.dictionary_info OWNER TO postgres;

--
-- TOC entry 3450 (class 0 OID 0)
-- Dependencies: 216
-- Name: COLUMN dictionary_info.dict_id; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.dictionary_info.dict_id IS '字典主键';


--
-- TOC entry 3451 (class 0 OID 0)
-- Dependencies: 216
-- Name: COLUMN dictionary_info.dict_key; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.dictionary_info.dict_key IS '字典键';


--
-- TOC entry 3452 (class 0 OID 0)
-- Dependencies: 216
-- Name: COLUMN dictionary_info.dict_name; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.dictionary_info.dict_name IS '字典名称';


--
-- TOC entry 3453 (class 0 OID 0)
-- Dependencies: 216
-- Name: COLUMN dictionary_info.dict_value; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.dictionary_info.dict_value IS '字典键值';


--
-- TOC entry 3454 (class 0 OID 0)
-- Dependencies: 216
-- Name: COLUMN dictionary_info.remark; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.dictionary_info.remark IS '备注';


--
-- TOC entry 3455 (class 0 OID 0)
-- Dependencies: 216
-- Name: COLUMN dictionary_info.creator_id; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.dictionary_info.creator_id IS '创建者';


--
-- TOC entry 3456 (class 0 OID 0)
-- Dependencies: 216
-- Name: COLUMN dictionary_info.updater_id; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.dictionary_info.updater_id IS '更新者';


--
-- TOC entry 3457 (class 0 OID 0)
-- Dependencies: 216
-- Name: COLUMN dictionary_info.create_at; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.dictionary_info.create_at IS '创建时间';


--
-- TOC entry 3458 (class 0 OID 0)
-- Dependencies: 216
-- Name: COLUMN dictionary_info.update_at; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.dictionary_info.update_at IS '更新时间';


--
-- TOC entry 3459 (class 0 OID 0)
-- Dependencies: 216
-- Name: COLUMN dictionary_info.hint; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.dictionary_info.hint IS '标签';


--
-- TOC entry 219 (class 1259 OID 16548)
-- Name: notification_info; Type: TABLE; Schema: biz; Owner: postgres
--

CREATE TABLE biz.notification_info (
    notification_id bigint NOT NULL,
    notification_type character(1) NOT NULL,
    title character varying(32) NOT NULL,
    content text NOT NULL,
    create_at timestamp with time zone NOT NULL,
    creator_id bigint NOT NULL
);


ALTER TABLE biz.notification_info OWNER TO postgres;

--
-- TOC entry 3460 (class 0 OID 0)
-- Dependencies: 219
-- Name: TABLE notification_info; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON TABLE biz.notification_info IS '系统通知表';


--
-- TOC entry 3461 (class 0 OID 0)
-- Dependencies: 219
-- Name: COLUMN notification_info.notification_type; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.notification_info.notification_type IS '通知类型（0系统通知 1设备告警）';


--
-- TOC entry 3462 (class 0 OID 0)
-- Dependencies: 219
-- Name: COLUMN notification_info.title; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.notification_info.title IS '标题';


--
-- TOC entry 3463 (class 0 OID 0)
-- Dependencies: 219
-- Name: COLUMN notification_info.content; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.notification_info.content IS '通知内容';


--
-- TOC entry 3464 (class 0 OID 0)
-- Dependencies: 219
-- Name: COLUMN notification_info.create_at; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.notification_info.create_at IS '创建时间';


--
-- TOC entry 3465 (class 0 OID 0)
-- Dependencies: 219
-- Name: COLUMN notification_info.creator_id; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.notification_info.creator_id IS '创建者ID';


--
-- TOC entry 220 (class 1259 OID 16553)
-- Name: notification_to_admin; Type: TABLE; Schema: biz; Owner: postgres
--

CREATE TABLE biz.notification_to_admin (
    notification_id bigint NOT NULL,
    admin_id bigint NOT NULL,
    read boolean NOT NULL
);


ALTER TABLE biz.notification_to_admin OWNER TO postgres;

--
-- TOC entry 3466 (class 0 OID 0)
-- Dependencies: 220
-- Name: TABLE notification_to_admin; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON TABLE biz.notification_to_admin IS '通知发送表';


--
-- TOC entry 3467 (class 0 OID 0)
-- Dependencies: 220
-- Name: COLUMN notification_to_admin.notification_id; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.notification_to_admin.notification_id IS '通知ID';


--
-- TOC entry 3468 (class 0 OID 0)
-- Dependencies: 220
-- Name: COLUMN notification_to_admin.admin_id; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.notification_to_admin.admin_id IS '管理员ID';


--
-- TOC entry 3469 (class 0 OID 0)
-- Dependencies: 220
-- Name: COLUMN notification_to_admin.read; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.notification_to_admin.read IS '是否已读';


--
-- TOC entry 225 (class 1259 OID 17018)
-- Name: notification_to_tenant; Type: TABLE; Schema: biz; Owner: postgres
--

CREATE TABLE biz.notification_to_tenant (
    notification_id bigint NOT NULL,
    member_id bigint NOT NULL,
    read boolean NOT NULL
);


ALTER TABLE biz.notification_to_tenant OWNER TO postgres;

--
-- TOC entry 3470 (class 0 OID 0)
-- Dependencies: 225
-- Name: TABLE notification_to_tenant; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON TABLE biz.notification_to_tenant IS '通知发送表';


--
-- TOC entry 3471 (class 0 OID 0)
-- Dependencies: 225
-- Name: COLUMN notification_to_tenant.notification_id; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.notification_to_tenant.notification_id IS '通知ID';


--
-- TOC entry 3472 (class 0 OID 0)
-- Dependencies: 225
-- Name: COLUMN notification_to_tenant.member_id; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.notification_to_tenant.member_id IS '租户成员ID';


--
-- TOC entry 3473 (class 0 OID 0)
-- Dependencies: 225
-- Name: COLUMN notification_to_tenant.read; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.notification_to_tenant.read IS '是否已读';


--
-- TOC entry 223 (class 1259 OID 16824)
-- Name: tenant_login_log; Type: TABLE; Schema: biz; Owner: postgres
--

CREATE TABLE biz.tenant_login_log (
    log_id bigint NOT NULL,
    member_id bigint NOT NULL,
    username character varying(32) NOT NULL,
    login_type character varying(32) NOT NULL,
    success boolean NOT NULL,
    error_msg character varying(50) NOT NULL,
    login_ip inet NOT NULL,
    login_location character varying(50) NOT NULL,
    browser character varying(50) NOT NULL,
    os character varying(50) NOT NULL,
    login_time timestamp(6) with time zone NOT NULL,
    tenant_id bigint NOT NULL
);


ALTER TABLE biz.tenant_login_log OWNER TO postgres;

--
-- TOC entry 3474 (class 0 OID 0)
-- Dependencies: 223
-- Name: TABLE tenant_login_log; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON TABLE biz.tenant_login_log IS '租户登录日志';


--
-- TOC entry 3475 (class 0 OID 0)
-- Dependencies: 223
-- Name: COLUMN tenant_login_log.member_id; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.tenant_login_log.member_id IS '用户ID';


--
-- TOC entry 3476 (class 0 OID 0)
-- Dependencies: 223
-- Name: COLUMN tenant_login_log.username; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.tenant_login_log.username IS '用户名';


--
-- TOC entry 3477 (class 0 OID 0)
-- Dependencies: 223
-- Name: COLUMN tenant_login_log.login_type; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.tenant_login_log.login_type IS '登录类型';


--
-- TOC entry 3478 (class 0 OID 0)
-- Dependencies: 223
-- Name: COLUMN tenant_login_log.success; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.tenant_login_log.success IS '登录状态';


--
-- TOC entry 3479 (class 0 OID 0)
-- Dependencies: 223
-- Name: COLUMN tenant_login_log.error_msg; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.tenant_login_log.error_msg IS '失败信息';


--
-- TOC entry 3480 (class 0 OID 0)
-- Dependencies: 223
-- Name: COLUMN tenant_login_log.login_ip; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.tenant_login_log.login_ip IS '登录ip';


--
-- TOC entry 3481 (class 0 OID 0)
-- Dependencies: 223
-- Name: COLUMN tenant_login_log.login_location; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.tenant_login_log.login_location IS '登录地址';


--
-- TOC entry 3482 (class 0 OID 0)
-- Dependencies: 223
-- Name: COLUMN tenant_login_log.browser; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.tenant_login_log.browser IS '浏览器类型';


--
-- TOC entry 3483 (class 0 OID 0)
-- Dependencies: 223
-- Name: COLUMN tenant_login_log.os; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.tenant_login_log.os IS '操作系统';


--
-- TOC entry 3484 (class 0 OID 0)
-- Dependencies: 223
-- Name: COLUMN tenant_login_log.login_time; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.tenant_login_log.login_time IS '登录时间';


--
-- TOC entry 3485 (class 0 OID 0)
-- Dependencies: 223
-- Name: COLUMN tenant_login_log.tenant_id; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.tenant_login_log.tenant_id IS '租户ID';


--
-- TOC entry 224 (class 1259 OID 16832)
-- Name: tenant_operate_log; Type: TABLE; Schema: biz; Owner: postgres
--

CREATE TABLE biz.tenant_operate_log (
    oper_id bigint NOT NULL,
    title character varying(20) NOT NULL,
    sub_title character varying(20) NOT NULL,
    oper_type character(1) NOT NULL,
    method character varying(100),
    request_method character varying(10),
    member_id bigint NOT NULL,
    username character varying(50) NOT NULL,
    oper_url character varying(200) NOT NULL,
    oper_ip inet NOT NULL,
    oper_location character varying(100),
    oper_param text,
    json_result text,
    success boolean NOT NULL,
    error_msg text,
    oper_time timestamp(6) with time zone NOT NULL,
    tenant_id bigint NOT NULL
);


ALTER TABLE biz.tenant_operate_log OWNER TO postgres;

--
-- TOC entry 3486 (class 0 OID 0)
-- Dependencies: 224
-- Name: TABLE tenant_operate_log; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON TABLE biz.tenant_operate_log IS '租户操作日志';


--
-- TOC entry 3487 (class 0 OID 0)
-- Dependencies: 224
-- Name: COLUMN tenant_operate_log.oper_id; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.tenant_operate_log.oper_id IS '日志主键';


--
-- TOC entry 3488 (class 0 OID 0)
-- Dependencies: 224
-- Name: COLUMN tenant_operate_log.title; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.tenant_operate_log.title IS '模块标题';


--
-- TOC entry 3489 (class 0 OID 0)
-- Dependencies: 224
-- Name: COLUMN tenant_operate_log.sub_title; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.tenant_operate_log.sub_title IS '子标题';


--
-- TOC entry 3490 (class 0 OID 0)
-- Dependencies: 224
-- Name: COLUMN tenant_operate_log.oper_type; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.tenant_operate_log.oper_type IS '操作类型（0其他 1新增 2修改 3删除）';


--
-- TOC entry 3491 (class 0 OID 0)
-- Dependencies: 224
-- Name: COLUMN tenant_operate_log.method; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.tenant_operate_log.method IS '方法名称';


--
-- TOC entry 3492 (class 0 OID 0)
-- Dependencies: 224
-- Name: COLUMN tenant_operate_log.request_method; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.tenant_operate_log.request_method IS '请求方式';


--
-- TOC entry 3493 (class 0 OID 0)
-- Dependencies: 224
-- Name: COLUMN tenant_operate_log.member_id; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.tenant_operate_log.member_id IS '职工ID';


--
-- TOC entry 3494 (class 0 OID 0)
-- Dependencies: 224
-- Name: COLUMN tenant_operate_log.username; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.tenant_operate_log.username IS '用户名';


--
-- TOC entry 3495 (class 0 OID 0)
-- Dependencies: 224
-- Name: COLUMN tenant_operate_log.oper_url; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.tenant_operate_log.oper_url IS '请求URL';


--
-- TOC entry 3496 (class 0 OID 0)
-- Dependencies: 224
-- Name: COLUMN tenant_operate_log.oper_ip; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.tenant_operate_log.oper_ip IS '主机地址';


--
-- TOC entry 3497 (class 0 OID 0)
-- Dependencies: 224
-- Name: COLUMN tenant_operate_log.oper_location; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.tenant_operate_log.oper_location IS '操作地点';


--
-- TOC entry 3498 (class 0 OID 0)
-- Dependencies: 224
-- Name: COLUMN tenant_operate_log.oper_param; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.tenant_operate_log.oper_param IS '请求参数';


--
-- TOC entry 3499 (class 0 OID 0)
-- Dependencies: 224
-- Name: COLUMN tenant_operate_log.json_result; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.tenant_operate_log.json_result IS '返回参数';


--
-- TOC entry 3500 (class 0 OID 0)
-- Dependencies: 224
-- Name: COLUMN tenant_operate_log.success; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.tenant_operate_log.success IS '操作状态';


--
-- TOC entry 3501 (class 0 OID 0)
-- Dependencies: 224
-- Name: COLUMN tenant_operate_log.error_msg; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.tenant_operate_log.error_msg IS '错误消息';


--
-- TOC entry 3502 (class 0 OID 0)
-- Dependencies: 224
-- Name: COLUMN tenant_operate_log.oper_time; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.tenant_operate_log.oper_time IS '操作时间';


--
-- TOC entry 3503 (class 0 OID 0)
-- Dependencies: 224
-- Name: COLUMN tenant_operate_log.tenant_id; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.tenant_operate_log.tenant_id IS '租户ID';


--
-- TOC entry 222 (class 1259 OID 16728)
-- Name: user_chat_message; Type: TABLE; Schema: biz; Owner: postgres
--

CREATE TABLE biz.user_chat_message (
    chat_id bigint NOT NULL,
    receiver_id bigint NOT NULL,
    sender_id bigint NOT NULL,
    send_time timestamp with time zone NOT NULL,
    receive_time timestamp with time zone,
    read boolean DEFAULT false NOT NULL,
    message_type character varying(10) NOT NULL,
    content bytea NOT NULL,
    media_format character varying(10) NOT NULL
);


ALTER TABLE biz.user_chat_message OWNER TO postgres;

--
-- TOC entry 3504 (class 0 OID 0)
-- Dependencies: 222
-- Name: TABLE user_chat_message; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON TABLE biz.user_chat_message IS '用户聊天消息';


--
-- TOC entry 3505 (class 0 OID 0)
-- Dependencies: 222
-- Name: COLUMN user_chat_message.receiver_id; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.user_chat_message.receiver_id IS '接收者ID';


--
-- TOC entry 3506 (class 0 OID 0)
-- Dependencies: 222
-- Name: COLUMN user_chat_message.sender_id; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.user_chat_message.sender_id IS '发送者ID';


--
-- TOC entry 3507 (class 0 OID 0)
-- Dependencies: 222
-- Name: COLUMN user_chat_message.send_time; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.user_chat_message.send_time IS '发送时间';


--
-- TOC entry 3508 (class 0 OID 0)
-- Dependencies: 222
-- Name: COLUMN user_chat_message.receive_time; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.user_chat_message.receive_time IS '接收时间';


--
-- TOC entry 3509 (class 0 OID 0)
-- Dependencies: 222
-- Name: COLUMN user_chat_message.read; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.user_chat_message.read IS '是否已读';


--
-- TOC entry 3510 (class 0 OID 0)
-- Dependencies: 222
-- Name: COLUMN user_chat_message.message_type; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.user_chat_message.message_type IS ' text/image/video/file';


--
-- TOC entry 3511 (class 0 OID 0)
-- Dependencies: 222
-- Name: COLUMN user_chat_message.content; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.user_chat_message.content IS ' 统一的内容字段，存储文本或二进制数据';


--
-- TOC entry 3512 (class 0 OID 0)
-- Dependencies: 222
-- Name: COLUMN user_chat_message.media_format; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.user_chat_message.media_format IS '媒体格式(png/jpg/mp4/txt等)';


--
-- TOC entry 3390 (class 0 OID 16538)
-- Dependencies: 217
-- Data for Name: admin_login_log; Type: TABLE DATA; Schema: biz; Owner: postgres
--

COPY biz.admin_login_log (log_id, admin_id, username, login_type, success, error_msg, login_ip, login_location, browser, os, login_time) FROM stdin;
\.


--
-- TOC entry 3394 (class 0 OID 16556)
-- Dependencies: 221
-- Data for Name: admin_operate_log; Type: TABLE DATA; Schema: biz; Owner: postgres
--

COPY biz.admin_operate_log (oper_id, title, sub_title, oper_type, method, request_method, admin_id, username, oper_url, oper_ip, oper_location, oper_param, json_result, success, error_msg, oper_time) FROM stdin;
\.


--
-- TOC entry 3391 (class 0 OID 16543)
-- Dependencies: 218
-- Data for Name: announcement_info; Type: TABLE DATA; Schema: biz; Owner: postgres
--

COPY biz.announcement_info (announcement_id, title, content, status, creator_id, updater_id, create_at, update_at, hint) FROM stdin;
\.


--
-- TOC entry 3388 (class 0 OID 16528)
-- Dependencies: 215
-- Data for Name: config_info; Type: TABLE DATA; Schema: biz; Owner: postgres
--

COPY biz.config_info (config_id, config_name, config_key, config_value, config_type, built_in, creator_id, create_at, updater_id, update_at, hint) FROM stdin;
1	用户管理-账号初始密码	sys.user.initPassword	123456	Y	f	1	2025-09-17 17:31:01+08	1	2025-09-25 15:44:21.491+08	
\.


--
-- TOC entry 3389 (class 0 OID 16533)
-- Dependencies: 216
-- Data for Name: dictionary_info; Type: TABLE DATA; Schema: biz; Owner: postgres
--

COPY biz.dictionary_info (dict_id, dict_key, dict_name, dict_value, remark, creator_id, updater_id, create_at, update_at, hint) FROM stdin;
1905175932909101057	user_sex	用户性别	[{"label": "男", "value": "0", "tableCls": ""}, {"label": "女", "value": "1", "tableCls": ""}, {"label": "未知", "value": "2", "tableCls": ""}]	\N	1	1	2024-04-17 14:08:54.616+08	2024-11-20 14:29:20.647+08	
1905175933034930178	menu_visible	菜单可见状态	[{"label": "显示", "value": "true", "tableCls": "primary"}, {"label": "隐藏", "value": "false", "tableCls": "danger"}]	\N	1	1	2024-04-17 14:08:54.616+08	2024-11-20 14:30:10.201+08	
1905175933097844737	common_disable	是否禁用	[{"label": "正常", "value": "0", "tableCls": "primary"}, {"label": "停用", "value": "1", "tableCls": "danger"}]	\N	1	1	2024-04-17 14:08:54.616+08	2024-11-20 14:34:03.416+08	
1905175933164953603	msg_type	消息类型	[{"label": "系统公告", "value": "1", "tableCls": "primary"}, {"label": "APP公告", "value": "2", "tableCls": "success"}]	\N	1	1	2024-04-17 14:08:54.616+08	2024-12-16 10:30:13.003+08	
1905175933164953604	msg_status	消息状态	[{"label": "已发布", "value": "2", "tableCls": "primary"}, {"label": "草稿", "value": "0", "tableCls": "info"}, {"label": "关闭", "value": "1", "tableCls": "danger"}]	\N	1	1	2024-04-17 14:08:54.616+08	2024-12-16 10:30:17.169+08	
1905175933227868164	online_status	设备连接状态	[{"label": "在线", "value": "true", "tableCls": "success"}, {"label": "离线", "value": "false", "tableCls": "info"}]	\N	1	1	2024-04-17 14:08:54.616+08	2024-12-09 11:28:08.104+08	
1905175933290782723	data_scope	数据权限	[{"label": "全部数据权限", "value": "1", "tableCls": "default"}, {"label": "自定数据权限", "value": "2", "tableCls": "default"}, {"label": "本部门数据权限", "value": "3", "tableCls": "default"}, {"label": "本部门及以下数据权限", "value": "4", "tableCls": "default"}]	\N	1	1	2024-04-17 14:08:54.616+08	2024-11-20 14:27:19.645+08	
1905175933425000451	wallet_record_type	钱包记录类型	[{"label": "收入", "value": "0", "tableCls": "primary"}, {"label": "支出", "value": "1", "tableCls": "danger"}]	\N	1	1	2024-11-25 16:32:20.36+08	2024-11-25 16:32:20.36+08	
1905175933357891585	pay_type	支付方式	[{"label": "钱包支付", "value": "0", "tableCls": "info"}, {"label": "微信支付", "value": "1", "tableCls": "success"}, {"label": "支付宝", "value": "2", "tableCls": "primary"}]	\N	1	1	2024-04-17 14:08:54.616+08	2025-07-15 17:00:24.272+08	
1905175933290782722	pay_certification_status	支付认证状态	[{"label": "未认证", "value": "0", "tableCls": "info"}, {"label": "微信支付", "value": "1", "tableCls": "success"}, {"label": "支付宝", "value": "2", "tableCls": "primary"}]	\N	1	1	2024-04-17 14:08:54.616+08	2025-07-15 17:00:45.836+08	
1905175933290782724	pay_status	支付状态	[{"label": "支付成功", "value": "1", "tableCls": "success"}, {"label": "订单关闭", "value": "2", "tableCls": "info"}, {"label": "未支付", "value": "0", "tableCls": "primary"}, {"label": "支付异常", "value": "3", "tableCls": "danger"}]	\N	1	1	2024-04-17 14:08:54.616+08	2025-07-15 17:01:13.675+08	
1905175933227868163	authorization_grant_types	授权类型	[{"label": "刷新模式", "value": "refresh_token", "tableCls": "primary"}, {"label": "客户端模式", "value": "client_credentials", "tableCls": "primary"}, {"label": "授权码模式", "value": "authorization_code", "tableCls": "primary"}, {"label": "token交换模式", "value": "urn:ietf:params:oauth:grant-type:token-exchange", "tableCls": "primary"}, {"label": "设备码模式", "value": "urn:ietf:params:oauth:grant-type:device_code", "tableCls": "primary"}]	\N	1	1	2024-04-17 14:08:54.616+08	2025-06-23 16:08:47.855+08	
1905175933034930179	menu_type	菜单类型	[{"label": "目录", "value": "D", "tableCls": "info"}, {"label": "菜单", "value": "M", "tableCls": "primary"}, {"label": "按钮", "value": "B", "tableCls": "danger"}, {"label": "字段", "value": "F", "tableCls": "warning"}, {"label": "内链", "value": "I", "tableCls": ""}, {"label": "外链", "value": "O", "tableCls": ""}]	\N	1	1	2024-11-23 15:22:03.788+08	2025-09-05 16:08:00.834+08	
1976213210015690754	menu_scope	菜单域	[{"label": "系统域", "value": "system", "tableCls": ""}, {"label": "租户域", "value": "tenant", "tableCls": ""}]	\N	1	1	2025-10-09 17:08:40.195+08	2025-10-09 17:08:40.195+08	
1905175933227868161	operate_type	操作类型	[{"label": "其他", "value": "0", "tableCls": "info"}, {"label": "新增", "value": "1", "tableCls": "info"}, {"label": "修改", "value": "2", "tableCls": "info"}, {"label": "删除", "value": "3", "tableCls": "danger"}, {"label": "授权", "value": "4", "tableCls": "primary"}, {"label": "导出", "value": "5", "tableCls": "warning"}, {"label": "导入", "value": "6", "tableCls": "warning"}]	\N	1	1	2024-04-17 14:08:54.616+08	2024-11-20 14:26:29.997+08	
1905175933227868162	operate_status	操作状态	[{"label": "成功", "value": "0", "tableCls": "primary"}, {"label": "失败", "value": "1", "tableCls": "danger"}]	\N	1	1	2024-04-17 14:08:54.616+08	2024-11-20 14:13:46.583+08	
\.


--
-- TOC entry 3392 (class 0 OID 16548)
-- Dependencies: 219
-- Data for Name: notification_info; Type: TABLE DATA; Schema: biz; Owner: postgres
--

COPY biz.notification_info (notification_id, notification_type, title, content, create_at, creator_id) FROM stdin;
\.


--
-- TOC entry 3393 (class 0 OID 16553)
-- Dependencies: 220
-- Data for Name: notification_to_admin; Type: TABLE DATA; Schema: biz; Owner: postgres
--

COPY biz.notification_to_admin (notification_id, admin_id, read) FROM stdin;
\.


--
-- TOC entry 3398 (class 0 OID 17018)
-- Dependencies: 225
-- Data for Name: notification_to_tenant; Type: TABLE DATA; Schema: biz; Owner: postgres
--

COPY biz.notification_to_tenant (notification_id, member_id, read) FROM stdin;
\.


--
-- TOC entry 3396 (class 0 OID 16824)
-- Dependencies: 223
-- Data for Name: tenant_login_log; Type: TABLE DATA; Schema: biz; Owner: postgres
--

COPY biz.tenant_login_log (log_id, member_id, username, login_type, success, error_msg, login_ip, login_location, browser, os, login_time, tenant_id) FROM stdin;
\.


--
-- TOC entry 3397 (class 0 OID 16832)
-- Dependencies: 224
-- Data for Name: tenant_operate_log; Type: TABLE DATA; Schema: biz; Owner: postgres
--

COPY biz.tenant_operate_log (oper_id, title, sub_title, oper_type, method, request_method, member_id, username, oper_url, oper_ip, oper_location, oper_param, json_result, success, error_msg, oper_time, tenant_id) FROM stdin;
\.


--
-- TOC entry 3395 (class 0 OID 16728)
-- Dependencies: 222
-- Data for Name: user_chat_message; Type: TABLE DATA; Schema: biz; Owner: postgres
--

COPY biz.user_chat_message (chat_id, receiver_id, sender_id, send_time, receive_time, read, message_type, content, media_format) FROM stdin;
\.


--
-- TOC entry 3225 (class 2606 OID 16566)
-- Name: admin_login_log admin_login_log_pkey; Type: CONSTRAINT; Schema: biz; Owner: postgres
--

ALTER TABLE ONLY biz.admin_login_log
    ADD CONSTRAINT admin_login_log_pkey PRIMARY KEY (log_id);


--
-- TOC entry 3234 (class 2606 OID 16574)
-- Name: admin_operate_log admin_operate_log_pkey; Type: CONSTRAINT; Schema: biz; Owner: postgres
--

ALTER TABLE ONLY biz.admin_operate_log
    ADD CONSTRAINT admin_operate_log_pkey PRIMARY KEY (oper_id);


--
-- TOC entry 3228 (class 2606 OID 16722)
-- Name: announcement_info announcement_info_pkey; Type: CONSTRAINT; Schema: biz; Owner: postgres
--

ALTER TABLE ONLY biz.announcement_info
    ADD CONSTRAINT announcement_info_pkey PRIMARY KEY (announcement_id);


--
-- TOC entry 3219 (class 2606 OID 16562)
-- Name: config_info config_info_pkey; Type: CONSTRAINT; Schema: biz; Owner: postgres
--

ALTER TABLE ONLY biz.config_info
    ADD CONSTRAINT config_info_pkey PRIMARY KEY (config_id);


--
-- TOC entry 3222 (class 2606 OID 16564)
-- Name: dictionary_info dictionary_info_pkey; Type: CONSTRAINT; Schema: biz; Owner: postgres
--

ALTER TABLE ONLY biz.dictionary_info
    ADD CONSTRAINT dictionary_info_pkey PRIMARY KEY (dict_id);


--
-- TOC entry 3230 (class 2606 OID 16724)
-- Name: notification_info notification_info_pkey; Type: CONSTRAINT; Schema: biz; Owner: postgres
--

ALTER TABLE ONLY biz.notification_info
    ADD CONSTRAINT notification_info_pkey PRIMARY KEY (notification_id);


--
-- TOC entry 3232 (class 2606 OID 16726)
-- Name: notification_to_admin notification_to_admin_pkey; Type: CONSTRAINT; Schema: biz; Owner: postgres
--

ALTER TABLE ONLY biz.notification_to_admin
    ADD CONSTRAINT notification_to_admin_pkey PRIMARY KEY (notification_id, admin_id);


--
-- TOC entry 3245 (class 2606 OID 17024)
-- Name: notification_to_tenant notification_to_tenant_pkey; Type: CONSTRAINT; Schema: biz; Owner: postgres
--

ALTER TABLE ONLY biz.notification_to_tenant
    ADD CONSTRAINT notification_to_tenant_pkey PRIMARY KEY (notification_id, member_id);


--
-- TOC entry 3240 (class 2606 OID 16830)
-- Name: tenant_login_log tenant_login_log_pkey; Type: CONSTRAINT; Schema: biz; Owner: postgres
--

ALTER TABLE ONLY biz.tenant_login_log
    ADD CONSTRAINT tenant_login_log_pkey PRIMARY KEY (log_id);


--
-- TOC entry 3243 (class 2606 OID 16838)
-- Name: tenant_operate_log tenant_operate_log_pkey; Type: CONSTRAINT; Schema: biz; Owner: postgres
--

ALTER TABLE ONLY biz.tenant_operate_log
    ADD CONSTRAINT tenant_operate_log_pkey PRIMARY KEY (oper_id);


--
-- TOC entry 3237 (class 2606 OID 16732)
-- Name: user_chat_message user_chat_message_pkey; Type: CONSTRAINT; Schema: biz; Owner: postgres
--

ALTER TABLE ONLY biz.user_chat_message
    ADD CONSTRAINT user_chat_message_pkey PRIMARY KEY (chat_id);


--
-- TOC entry 3226 (class 1259 OID 16575)
-- Name: idx_admin_login_log_login_time; Type: INDEX; Schema: biz; Owner: postgres
--

CREATE INDEX idx_admin_login_log_login_time ON biz.admin_login_log USING brin (login_time);


--
-- TOC entry 3235 (class 1259 OID 16576)
-- Name: idx_admin_operate_log_oper_time; Type: INDEX; Schema: biz; Owner: postgres
--

CREATE INDEX idx_admin_operate_log_oper_time ON biz.admin_operate_log USING brin (oper_time);


--
-- TOC entry 3238 (class 1259 OID 16831)
-- Name: idx_tenant_login_log_login_time; Type: INDEX; Schema: biz; Owner: postgres
--

CREATE INDEX idx_tenant_login_log_login_time ON biz.tenant_login_log USING brin (login_time);


--
-- TOC entry 3241 (class 1259 OID 16839)
-- Name: idx_tenant_operate_log_oper_time; Type: INDEX; Schema: biz; Owner: postgres
--

CREATE INDEX idx_tenant_operate_log_oper_time ON biz.tenant_operate_log USING brin (oper_time);


--
-- TOC entry 3220 (class 1259 OID 16577)
-- Name: uk_config_info_config_key; Type: INDEX; Schema: biz; Owner: postgres
--

CREATE UNIQUE INDEX uk_config_info_config_key ON biz.config_info USING btree (config_key);


--
-- TOC entry 3223 (class 1259 OID 16578)
-- Name: uk_dictionary_info_dict_key; Type: INDEX; Schema: biz; Owner: postgres
--

CREATE UNIQUE INDEX uk_dictionary_info_dict_key ON biz.dictionary_info USING btree (dict_key);


-- Completed on 2025-12-26 09:39:41

--
-- PostgreSQL database dump complete
--

