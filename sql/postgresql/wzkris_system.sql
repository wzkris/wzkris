--
-- PostgreSQL database dump
--

-- Dumped from database version 15.5
-- Dumped by pg_dump version 16.0

-- Started on 2025-03-03 14:07:12

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
-- TOC entry 6 (class 2615 OID 34670)
-- Name: biz_sys; Type: SCHEMA; Schema: -; Owner: wz
--

CREATE SCHEMA biz_sys;


ALTER SCHEMA biz_sys OWNER TO wz;

SET default_tablespace = '';

SET default_table_access_method = heap;

--
-- TOC entry 215 (class 1259 OID 34671)
-- Name: global_dict_data; Type: TABLE; Schema: biz_sys; Owner: wz
--

CREATE TABLE biz_sys.global_dict_data (
    data_id bigint NOT NULL,
    dict_sort integer NOT NULL,
    dict_label character varying(50) NOT NULL,
    dict_value character varying(50) NOT NULL,
    dict_type character varying(50) NOT NULL,
    css_class character varying(100),
    list_class character varying(100),
    is_default character(1) NOT NULL,
    creator_id bigint NOT NULL,
    updater_id bigint,
    create_at timestamp with time zone,
    update_at timestamp with time zone
);


ALTER TABLE biz_sys.global_dict_data OWNER TO wz;

--
-- TOC entry 3377 (class 0 OID 0)
-- Dependencies: 215
-- Name: TABLE global_dict_data; Type: COMMENT; Schema: biz_sys; Owner: wz
--

COMMENT ON TABLE biz_sys.global_dict_data IS '字典数据表';


--
-- TOC entry 3378 (class 0 OID 0)
-- Dependencies: 215
-- Name: COLUMN global_dict_data.data_id; Type: COMMENT; Schema: biz_sys; Owner: wz
--

COMMENT ON COLUMN biz_sys.global_dict_data.data_id IS '字典编码';


--
-- TOC entry 3379 (class 0 OID 0)
-- Dependencies: 215
-- Name: COLUMN global_dict_data.dict_sort; Type: COMMENT; Schema: biz_sys; Owner: wz
--

COMMENT ON COLUMN biz_sys.global_dict_data.dict_sort IS '字典排序';


--
-- TOC entry 3380 (class 0 OID 0)
-- Dependencies: 215
-- Name: COLUMN global_dict_data.dict_label; Type: COMMENT; Schema: biz_sys; Owner: wz
--

COMMENT ON COLUMN biz_sys.global_dict_data.dict_label IS '字典标签';


--
-- TOC entry 3381 (class 0 OID 0)
-- Dependencies: 215
-- Name: COLUMN global_dict_data.dict_value; Type: COMMENT; Schema: biz_sys; Owner: wz
--

COMMENT ON COLUMN biz_sys.global_dict_data.dict_value IS '字典键值';


--
-- TOC entry 3382 (class 0 OID 0)
-- Dependencies: 215
-- Name: COLUMN global_dict_data.dict_type; Type: COMMENT; Schema: biz_sys; Owner: wz
--

COMMENT ON COLUMN biz_sys.global_dict_data.dict_type IS '字典类型';


--
-- TOC entry 3383 (class 0 OID 0)
-- Dependencies: 215
-- Name: COLUMN global_dict_data.css_class; Type: COMMENT; Schema: biz_sys; Owner: wz
--

COMMENT ON COLUMN biz_sys.global_dict_data.css_class IS '样式属性（其他样式扩展）';


--
-- TOC entry 3384 (class 0 OID 0)
-- Dependencies: 215
-- Name: COLUMN global_dict_data.list_class; Type: COMMENT; Schema: biz_sys; Owner: wz
--

COMMENT ON COLUMN biz_sys.global_dict_data.list_class IS '表格回显样式';


--
-- TOC entry 3385 (class 0 OID 0)
-- Dependencies: 215
-- Name: COLUMN global_dict_data.is_default; Type: COMMENT; Schema: biz_sys; Owner: wz
--

COMMENT ON COLUMN biz_sys.global_dict_data.is_default IS '是否默认（Y是 N否）';


--
-- TOC entry 3386 (class 0 OID 0)
-- Dependencies: 215
-- Name: COLUMN global_dict_data.creator_id; Type: COMMENT; Schema: biz_sys; Owner: wz
--

COMMENT ON COLUMN biz_sys.global_dict_data.creator_id IS '创建者';


--
-- TOC entry 3387 (class 0 OID 0)
-- Dependencies: 215
-- Name: COLUMN global_dict_data.updater_id; Type: COMMENT; Schema: biz_sys; Owner: wz
--

COMMENT ON COLUMN biz_sys.global_dict_data.updater_id IS '更新者';


--
-- TOC entry 216 (class 1259 OID 34674)
-- Name: global_dict_type; Type: TABLE; Schema: biz_sys; Owner: wz
--

CREATE TABLE biz_sys.global_dict_type (
    type_id bigint NOT NULL,
    dict_name character varying(50) NOT NULL,
    dict_type character varying(50) NOT NULL,
    creator_id bigint NOT NULL,
    updater_id bigint,
    create_at timestamp with time zone,
    update_at timestamp with time zone
);


ALTER TABLE biz_sys.global_dict_type OWNER TO wz;

--
-- TOC entry 3388 (class 0 OID 0)
-- Dependencies: 216
-- Name: TABLE global_dict_type; Type: COMMENT; Schema: biz_sys; Owner: wz
--

COMMENT ON TABLE biz_sys.global_dict_type IS '字典类型表';


--
-- TOC entry 3389 (class 0 OID 0)
-- Dependencies: 216
-- Name: COLUMN global_dict_type.type_id; Type: COMMENT; Schema: biz_sys; Owner: wz
--

COMMENT ON COLUMN biz_sys.global_dict_type.type_id IS '字典主键';


--
-- TOC entry 3390 (class 0 OID 0)
-- Dependencies: 216
-- Name: COLUMN global_dict_type.dict_name; Type: COMMENT; Schema: biz_sys; Owner: wz
--

COMMENT ON COLUMN biz_sys.global_dict_type.dict_name IS '字典名称';


--
-- TOC entry 3391 (class 0 OID 0)
-- Dependencies: 216
-- Name: COLUMN global_dict_type.dict_type; Type: COMMENT; Schema: biz_sys; Owner: wz
--

COMMENT ON COLUMN biz_sys.global_dict_type.dict_type IS '字典类型';


--
-- TOC entry 3392 (class 0 OID 0)
-- Dependencies: 216
-- Name: COLUMN global_dict_type.creator_id; Type: COMMENT; Schema: biz_sys; Owner: wz
--

COMMENT ON COLUMN biz_sys.global_dict_type.creator_id IS '创建者';


--
-- TOC entry 3393 (class 0 OID 0)
-- Dependencies: 216
-- Name: COLUMN global_dict_type.updater_id; Type: COMMENT; Schema: biz_sys; Owner: wz
--

COMMENT ON COLUMN biz_sys.global_dict_type.updater_id IS '更新者';


--
-- TOC entry 217 (class 1259 OID 34677)
-- Name: sys_config; Type: TABLE; Schema: biz_sys; Owner: wz
--

CREATE TABLE biz_sys.sys_config (
    config_id bigint NOT NULL,
    config_name character varying(50) NOT NULL,
    config_key character varying(50) NOT NULL,
    config_value text NOT NULL,
    config_type character(1) NOT NULL,
    creator_id bigint NOT NULL,
    updater_id bigint,
    create_at timestamp with time zone,
    update_at timestamp with time zone
);


ALTER TABLE biz_sys.sys_config OWNER TO wz;

--
-- TOC entry 3394 (class 0 OID 0)
-- Dependencies: 217
-- Name: TABLE sys_config; Type: COMMENT; Schema: biz_sys; Owner: wz
--

COMMENT ON TABLE biz_sys.sys_config IS '参数配置表';


--
-- TOC entry 3395 (class 0 OID 0)
-- Dependencies: 217
-- Name: COLUMN sys_config.config_id; Type: COMMENT; Schema: biz_sys; Owner: wz
--

COMMENT ON COLUMN biz_sys.sys_config.config_id IS '参数主键';


--
-- TOC entry 3396 (class 0 OID 0)
-- Dependencies: 217
-- Name: COLUMN sys_config.config_name; Type: COMMENT; Schema: biz_sys; Owner: wz
--

COMMENT ON COLUMN biz_sys.sys_config.config_name IS '参数名称';


--
-- TOC entry 3397 (class 0 OID 0)
-- Dependencies: 217
-- Name: COLUMN sys_config.config_key; Type: COMMENT; Schema: biz_sys; Owner: wz
--

COMMENT ON COLUMN biz_sys.sys_config.config_key IS '参数键名';


--
-- TOC entry 3398 (class 0 OID 0)
-- Dependencies: 217
-- Name: COLUMN sys_config.config_value; Type: COMMENT; Schema: biz_sys; Owner: wz
--

COMMENT ON COLUMN biz_sys.sys_config.config_value IS '参数键值';


--
-- TOC entry 3399 (class 0 OID 0)
-- Dependencies: 217
-- Name: COLUMN sys_config.config_type; Type: COMMENT; Schema: biz_sys; Owner: wz
--

COMMENT ON COLUMN biz_sys.sys_config.config_type IS '系统内置（Y是 N否）';


--
-- TOC entry 3400 (class 0 OID 0)
-- Dependencies: 217
-- Name: COLUMN sys_config.creator_id; Type: COMMENT; Schema: biz_sys; Owner: wz
--

COMMENT ON COLUMN biz_sys.sys_config.creator_id IS '创建者';


--
-- TOC entry 3401 (class 0 OID 0)
-- Dependencies: 217
-- Name: COLUMN sys_config.updater_id; Type: COMMENT; Schema: biz_sys; Owner: wz
--

COMMENT ON COLUMN biz_sys.sys_config.updater_id IS '更新者';


--
-- TOC entry 218 (class 1259 OID 34682)
-- Name: sys_login_log; Type: TABLE; Schema: biz_sys; Owner: wz
--

CREATE TABLE biz_sys.sys_login_log (
    log_id bigint NOT NULL,
    tenant_id bigint NOT NULL,
    user_id bigint NOT NULL,
    username character varying(32) NOT NULL,
    grant_type character varying(32) NOT NULL,
    status character(1) NOT NULL,
    error_msg character varying(50) NOT NULL,
    login_ip inet NOT NULL,
    login_location character varying(50) NOT NULL,
    browser character varying(50) NOT NULL,
    os character varying(50) NOT NULL,
    login_time timestamp with time zone NOT NULL
);


ALTER TABLE biz_sys.sys_login_log OWNER TO wz;

--
-- TOC entry 3402 (class 0 OID 0)
-- Dependencies: 218
-- Name: TABLE sys_login_log; Type: COMMENT; Schema: biz_sys; Owner: wz
--

COMMENT ON TABLE biz_sys.sys_login_log IS '后台登录日志';


--
-- TOC entry 3403 (class 0 OID 0)
-- Dependencies: 218
-- Name: COLUMN sys_login_log.tenant_id; Type: COMMENT; Schema: biz_sys; Owner: wz
--

COMMENT ON COLUMN biz_sys.sys_login_log.tenant_id IS '租户ID';


--
-- TOC entry 3404 (class 0 OID 0)
-- Dependencies: 218
-- Name: COLUMN sys_login_log.user_id; Type: COMMENT; Schema: biz_sys; Owner: wz
--

COMMENT ON COLUMN biz_sys.sys_login_log.user_id IS '用户ID';


--
-- TOC entry 3405 (class 0 OID 0)
-- Dependencies: 218
-- Name: COLUMN sys_login_log.username; Type: COMMENT; Schema: biz_sys; Owner: wz
--

COMMENT ON COLUMN biz_sys.sys_login_log.username IS '用户名';


--
-- TOC entry 3406 (class 0 OID 0)
-- Dependencies: 218
-- Name: COLUMN sys_login_log.grant_type; Type: COMMENT; Schema: biz_sys; Owner: wz
--

COMMENT ON COLUMN biz_sys.sys_login_log.grant_type IS '授权类型';


--
-- TOC entry 3407 (class 0 OID 0)
-- Dependencies: 218
-- Name: COLUMN sys_login_log.status; Type: COMMENT; Schema: biz_sys; Owner: wz
--

COMMENT ON COLUMN biz_sys.sys_login_log.status IS '登录状态（0正常 1异常）';


--
-- TOC entry 3408 (class 0 OID 0)
-- Dependencies: 218
-- Name: COLUMN sys_login_log.error_msg; Type: COMMENT; Schema: biz_sys; Owner: wz
--

COMMENT ON COLUMN biz_sys.sys_login_log.error_msg IS '失败信息';


--
-- TOC entry 3409 (class 0 OID 0)
-- Dependencies: 218
-- Name: COLUMN sys_login_log.login_ip; Type: COMMENT; Schema: biz_sys; Owner: wz
--

COMMENT ON COLUMN biz_sys.sys_login_log.login_ip IS '登录ip';


--
-- TOC entry 3410 (class 0 OID 0)
-- Dependencies: 218
-- Name: COLUMN sys_login_log.login_location; Type: COMMENT; Schema: biz_sys; Owner: wz
--

COMMENT ON COLUMN biz_sys.sys_login_log.login_location IS '登录地址';


--
-- TOC entry 3411 (class 0 OID 0)
-- Dependencies: 218
-- Name: COLUMN sys_login_log.browser; Type: COMMENT; Schema: biz_sys; Owner: wz
--

COMMENT ON COLUMN biz_sys.sys_login_log.browser IS '浏览器类型';


--
-- TOC entry 3412 (class 0 OID 0)
-- Dependencies: 218
-- Name: COLUMN sys_login_log.os; Type: COMMENT; Schema: biz_sys; Owner: wz
--

COMMENT ON COLUMN biz_sys.sys_login_log.os IS '操作系统';


--
-- TOC entry 3413 (class 0 OID 0)
-- Dependencies: 218
-- Name: COLUMN sys_login_log.login_time; Type: COMMENT; Schema: biz_sys; Owner: wz
--

COMMENT ON COLUMN biz_sys.sys_login_log.login_time IS '登录时间';


--
-- TOC entry 219 (class 1259 OID 34685)
-- Name: sys_message; Type: TABLE; Schema: biz_sys; Owner: wz
--

CREATE TABLE biz_sys.sys_message (
    msg_id bigint NOT NULL,
    title character varying(30) NOT NULL,
    msg_type character(1) NOT NULL,
    content text NOT NULL,
    status character(1) NOT NULL,
    creator_id bigint NOT NULL,
    updater_id bigint,
    create_at timestamp with time zone,
    update_at timestamp with time zone
);


ALTER TABLE biz_sys.sys_message OWNER TO wz;

--
-- TOC entry 3414 (class 0 OID 0)
-- Dependencies: 219
-- Name: TABLE sys_message; Type: COMMENT; Schema: biz_sys; Owner: wz
--

COMMENT ON TABLE biz_sys.sys_message IS '系统消息表';


--
-- TOC entry 3415 (class 0 OID 0)
-- Dependencies: 219
-- Name: COLUMN sys_message.msg_id; Type: COMMENT; Schema: biz_sys; Owner: wz
--

COMMENT ON COLUMN biz_sys.sys_message.msg_id IS '消息ID';


--
-- TOC entry 3416 (class 0 OID 0)
-- Dependencies: 219
-- Name: COLUMN sys_message.title; Type: COMMENT; Schema: biz_sys; Owner: wz
--

COMMENT ON COLUMN biz_sys.sys_message.title IS '消息标题';


--
-- TOC entry 3417 (class 0 OID 0)
-- Dependencies: 219
-- Name: COLUMN sys_message.msg_type; Type: COMMENT; Schema: biz_sys; Owner: wz
--

COMMENT ON COLUMN biz_sys.sys_message.msg_type IS '消息类型（1系统公告 2APP公告）';


--
-- TOC entry 3418 (class 0 OID 0)
-- Dependencies: 219
-- Name: COLUMN sys_message.content; Type: COMMENT; Schema: biz_sys; Owner: wz
--

COMMENT ON COLUMN biz_sys.sys_message.content IS '消息内容';


--
-- TOC entry 3419 (class 0 OID 0)
-- Dependencies: 219
-- Name: COLUMN sys_message.status; Type: COMMENT; Schema: biz_sys; Owner: wz
--

COMMENT ON COLUMN biz_sys.sys_message.status IS '消息状态（0草稿 1关闭 2已发送）';


--
-- TOC entry 3420 (class 0 OID 0)
-- Dependencies: 219
-- Name: COLUMN sys_message.creator_id; Type: COMMENT; Schema: biz_sys; Owner: wz
--

COMMENT ON COLUMN biz_sys.sys_message.creator_id IS '创建时间';


--
-- TOC entry 3421 (class 0 OID 0)
-- Dependencies: 219
-- Name: COLUMN sys_message.updater_id; Type: COMMENT; Schema: biz_sys; Owner: wz
--

COMMENT ON COLUMN biz_sys.sys_message.updater_id IS '更新时间';


--
-- TOC entry 220 (class 1259 OID 34690)
-- Name: sys_notify; Type: TABLE; Schema: biz_sys; Owner: wz
--

CREATE TABLE biz_sys.sys_notify (
    notify_id bigint NOT NULL,
    notify_type character(1) NOT NULL,
    title character varying(32) NOT NULL,
    content character varying(200) NOT NULL,
    create_at timestamp with time zone NOT NULL
);


ALTER TABLE biz_sys.sys_notify OWNER TO wz;

--
-- TOC entry 3422 (class 0 OID 0)
-- Dependencies: 220
-- Name: TABLE sys_notify; Type: COMMENT; Schema: biz_sys; Owner: wz
--

COMMENT ON TABLE biz_sys.sys_notify IS '系统通知表';


--
-- TOC entry 3423 (class 0 OID 0)
-- Dependencies: 220
-- Name: COLUMN sys_notify.notify_type; Type: COMMENT; Schema: biz_sys; Owner: wz
--

COMMENT ON COLUMN biz_sys.sys_notify.notify_type IS '通知类型（0系统通知 1设备告警）';


--
-- TOC entry 3424 (class 0 OID 0)
-- Dependencies: 220
-- Name: COLUMN sys_notify.title; Type: COMMENT; Schema: biz_sys; Owner: wz
--

COMMENT ON COLUMN biz_sys.sys_notify.title IS '标题';


--
-- TOC entry 3425 (class 0 OID 0)
-- Dependencies: 220
-- Name: COLUMN sys_notify.content; Type: COMMENT; Schema: biz_sys; Owner: wz
--

COMMENT ON COLUMN biz_sys.sys_notify.content IS '通知内容';


--
-- TOC entry 3426 (class 0 OID 0)
-- Dependencies: 220
-- Name: COLUMN sys_notify.create_at; Type: COMMENT; Schema: biz_sys; Owner: wz
--

COMMENT ON COLUMN biz_sys.sys_notify.create_at IS '创建时间';


--
-- TOC entry 221 (class 1259 OID 34693)
-- Name: sys_notify_send; Type: TABLE; Schema: biz_sys; Owner: wz
--

CREATE TABLE biz_sys.sys_notify_send (
    notify_id bigint NOT NULL,
    user_id bigint NOT NULL,
    read_state character(1) NOT NULL
);


ALTER TABLE biz_sys.sys_notify_send OWNER TO wz;

--
-- TOC entry 3427 (class 0 OID 0)
-- Dependencies: 221
-- Name: TABLE sys_notify_send; Type: COMMENT; Schema: biz_sys; Owner: wz
--

COMMENT ON TABLE biz_sys.sys_notify_send IS '通知发送表';


--
-- TOC entry 3428 (class 0 OID 0)
-- Dependencies: 221
-- Name: COLUMN sys_notify_send.notify_id; Type: COMMENT; Schema: biz_sys; Owner: wz
--

COMMENT ON COLUMN biz_sys.sys_notify_send.notify_id IS '系统消息ID';


--
-- TOC entry 3429 (class 0 OID 0)
-- Dependencies: 221
-- Name: COLUMN sys_notify_send.user_id; Type: COMMENT; Schema: biz_sys; Owner: wz
--

COMMENT ON COLUMN biz_sys.sys_notify_send.user_id IS '接收用户ID';


--
-- TOC entry 3430 (class 0 OID 0)
-- Dependencies: 221
-- Name: COLUMN sys_notify_send.read_state; Type: COMMENT; Schema: biz_sys; Owner: wz
--

COMMENT ON COLUMN biz_sys.sys_notify_send.read_state IS '已读1 未读0';


--
-- TOC entry 222 (class 1259 OID 34696)
-- Name: sys_oper_log; Type: TABLE; Schema: biz_sys; Owner: wz
--

CREATE TABLE biz_sys.sys_oper_log (
    oper_id bigint NOT NULL,
    tenant_id bigint NOT NULL,
    title character varying(20) NOT NULL,
    sub_title character varying(20) NOT NULL,
    oper_type character(1) NOT NULL,
    method character varying(100),
    request_method character varying(10),
    user_id bigint NOT NULL,
    oper_name character varying(50) NOT NULL,
    oper_url character varying(200) NOT NULL,
    oper_ip inet NOT NULL,
    oper_location character varying(100),
    oper_param text,
    json_result text,
    status character(1) NOT NULL,
    error_msg text,
    oper_time timestamp with time zone NOT NULL
);


ALTER TABLE biz_sys.sys_oper_log OWNER TO wz;

--
-- TOC entry 3431 (class 0 OID 0)
-- Dependencies: 222
-- Name: TABLE sys_oper_log; Type: COMMENT; Schema: biz_sys; Owner: wz
--

COMMENT ON TABLE biz_sys.sys_oper_log IS '操作日志记录';


--
-- TOC entry 3432 (class 0 OID 0)
-- Dependencies: 222
-- Name: COLUMN sys_oper_log.oper_id; Type: COMMENT; Schema: biz_sys; Owner: wz
--

COMMENT ON COLUMN biz_sys.sys_oper_log.oper_id IS '日志主键';


--
-- TOC entry 3433 (class 0 OID 0)
-- Dependencies: 222
-- Name: COLUMN sys_oper_log.tenant_id; Type: COMMENT; Schema: biz_sys; Owner: wz
--

COMMENT ON COLUMN biz_sys.sys_oper_log.tenant_id IS '租户ID';


--
-- TOC entry 3434 (class 0 OID 0)
-- Dependencies: 222
-- Name: COLUMN sys_oper_log.title; Type: COMMENT; Schema: biz_sys; Owner: wz
--

COMMENT ON COLUMN biz_sys.sys_oper_log.title IS '模块标题';


--
-- TOC entry 3435 (class 0 OID 0)
-- Dependencies: 222
-- Name: COLUMN sys_oper_log.sub_title; Type: COMMENT; Schema: biz_sys; Owner: wz
--

COMMENT ON COLUMN biz_sys.sys_oper_log.sub_title IS '子标题';


--
-- TOC entry 3436 (class 0 OID 0)
-- Dependencies: 222
-- Name: COLUMN sys_oper_log.oper_type; Type: COMMENT; Schema: biz_sys; Owner: wz
--

COMMENT ON COLUMN biz_sys.sys_oper_log.oper_type IS '操作类型（0其他 1新增 2修改 3删除）';


--
-- TOC entry 3437 (class 0 OID 0)
-- Dependencies: 222
-- Name: COLUMN sys_oper_log.method; Type: COMMENT; Schema: biz_sys; Owner: wz
--

COMMENT ON COLUMN biz_sys.sys_oper_log.method IS '方法名称';


--
-- TOC entry 3438 (class 0 OID 0)
-- Dependencies: 222
-- Name: COLUMN sys_oper_log.request_method; Type: COMMENT; Schema: biz_sys; Owner: wz
--

COMMENT ON COLUMN biz_sys.sys_oper_log.request_method IS '请求方式';


--
-- TOC entry 3439 (class 0 OID 0)
-- Dependencies: 222
-- Name: COLUMN sys_oper_log.user_id; Type: COMMENT; Schema: biz_sys; Owner: wz
--

COMMENT ON COLUMN biz_sys.sys_oper_log.user_id IS '操作人员ID';


--
-- TOC entry 3440 (class 0 OID 0)
-- Dependencies: 222
-- Name: COLUMN sys_oper_log.oper_name; Type: COMMENT; Schema: biz_sys; Owner: wz
--

COMMENT ON COLUMN biz_sys.sys_oper_log.oper_name IS '操作人员';


--
-- TOC entry 3441 (class 0 OID 0)
-- Dependencies: 222
-- Name: COLUMN sys_oper_log.oper_url; Type: COMMENT; Schema: biz_sys; Owner: wz
--

COMMENT ON COLUMN biz_sys.sys_oper_log.oper_url IS '请求URL';


--
-- TOC entry 3442 (class 0 OID 0)
-- Dependencies: 222
-- Name: COLUMN sys_oper_log.oper_ip; Type: COMMENT; Schema: biz_sys; Owner: wz
--

COMMENT ON COLUMN biz_sys.sys_oper_log.oper_ip IS '主机地址';


--
-- TOC entry 3443 (class 0 OID 0)
-- Dependencies: 222
-- Name: COLUMN sys_oper_log.oper_location; Type: COMMENT; Schema: biz_sys; Owner: wz
--

COMMENT ON COLUMN biz_sys.sys_oper_log.oper_location IS '操作地点';


--
-- TOC entry 3444 (class 0 OID 0)
-- Dependencies: 222
-- Name: COLUMN sys_oper_log.oper_param; Type: COMMENT; Schema: biz_sys; Owner: wz
--

COMMENT ON COLUMN biz_sys.sys_oper_log.oper_param IS '请求参数';


--
-- TOC entry 3445 (class 0 OID 0)
-- Dependencies: 222
-- Name: COLUMN sys_oper_log.json_result; Type: COMMENT; Schema: biz_sys; Owner: wz
--

COMMENT ON COLUMN biz_sys.sys_oper_log.json_result IS '返回参数';


--
-- TOC entry 3446 (class 0 OID 0)
-- Dependencies: 222
-- Name: COLUMN sys_oper_log.status; Type: COMMENT; Schema: biz_sys; Owner: wz
--

COMMENT ON COLUMN biz_sys.sys_oper_log.status IS '操作状态（0正常 1异常）';


--
-- TOC entry 3447 (class 0 OID 0)
-- Dependencies: 222
-- Name: COLUMN sys_oper_log.error_msg; Type: COMMENT; Schema: biz_sys; Owner: wz
--

COMMENT ON COLUMN biz_sys.sys_oper_log.error_msg IS '错误消息';


--
-- TOC entry 3448 (class 0 OID 0)
-- Dependencies: 222
-- Name: COLUMN sys_oper_log.oper_time; Type: COMMENT; Schema: biz_sys; Owner: wz
--

COMMENT ON COLUMN biz_sys.sys_oper_log.oper_time IS '操作时间';


--
-- TOC entry 3364 (class 0 OID 34671)
-- Dependencies: 215
-- Data for Name: global_dict_data; Type: TABLE DATA; Schema: biz_sys; Owner: wz
--

COPY biz_sys.global_dict_data (data_id, dict_sort, dict_label, dict_value, dict_type, css_class, list_class, is_default, creator_id, updater_id, create_at, update_at) FROM stdin;
1	1	男	0	user_sex			Y	1	\N	2024-04-17 14:08:54.616+08	\N
2	2	女	1	user_sex			N	1	\N	2024-04-17 14:08:54.616+08	\N
3	3	未知	2	user_sex			N	1	\N	2024-04-17 14:08:54.616+08	\N
4	1	显示	true	menu_visible		primary	Y	1	1	2024-04-17 14:08:54.616+08	2024-11-23 15:16:11.15+08
5	2	隐藏	false	menu_visible		danger	N	1	1	2024-04-17 14:08:54.616+08	2024-11-23 15:16:16.502+08
6	1	正常	0	common_disable		primary	Y	1	\N	2024-04-17 14:08:54.616+08	\N
7	2	停用	1	common_disable		danger	N	1	\N	2024-04-17 14:08:54.616+08	\N
8	1	正常	0	sys_job_status		primary	Y	1	\N	2024-04-17 14:08:54.616+08	\N
9	2	暂停	1	sys_job_status		danger	N	1	\N	2024-04-17 14:08:54.616+08	\N
10	1	默认	DEFAULT	sys_job_group			Y	1	\N	2024-04-17 14:08:54.616+08	\N
11	2	系统	SYSTEM	sys_job_group			N	1	\N	2024-04-17 14:08:54.616+08	\N
12	1	是	Y	sys_yes_no		primary	Y	1	\N	2024-04-17 14:08:54.616+08	\N
13	2	否	N	sys_yes_no		danger	N	1	\N	2024-04-17 14:08:54.616+08	\N
14	1	系统公告	1	msg_type		primary	Y	1	1	2024-04-17 14:08:54.616+08	2024-12-16 09:29:20.477+08
15	2	APP公告	2	msg_type		success	N	1	1	2024-04-17 14:08:54.616+08	2024-12-16 09:29:27.234+08
16	1	草稿	0	msg_status		info	Y	1	1	2024-04-17 14:08:54.616+08	2024-12-12 16:19:42.786+08
17	2	关闭	1	msg_status		danger	N	1	\N	2024-04-17 14:08:54.616+08	\N
18	0	已发布	2	msg_status	\N	primary	N	1	1	2024-12-12 16:19:37.087+08	2024-12-16 10:29:59.926+08
19	0	其他	0	sys_oper_type		info	N	1	1	2024-04-17 14:08:54.616+08	2611-05-25 08:41:43.337+08
20	1	新增	1	sys_oper_type		info	N	1	\N	2024-04-17 14:08:54.616+08	\N
21	2	修改	2	sys_oper_type		info	N	1	\N	2024-04-17 14:08:54.616+08	\N
22	3	删除	3	sys_oper_type		danger	N	1	\N	2024-04-17 14:08:54.616+08	\N
23	4	授权	4	sys_oper_type		primary	N	1	\N	2024-04-17 14:08:54.616+08	\N
24	5	导出	5	sys_oper_type		warning	N	1	\N	2024-04-17 14:08:54.616+08	\N
25	6	导入	6	sys_oper_type		warning	N	1	\N	2024-04-17 14:08:54.616+08	\N
29	1	成功	0	sys_oper_status		primary	N	1	\N	2024-04-17 14:08:54.616+08	\N
30	2	失败	1	sys_oper_status		danger	N	1	\N	2024-04-17 14:08:54.616+08	\N
31	0	密码模式	password	authorization_grant_types	\N	primary	N	1	1	2024-04-17 14:08:54.616+08	2024-08-20 15:25:11.36+08
32	0	客户端模式	client_credentials	authorization_grant_types	\N	primary	N	1	1	2024-04-17 14:08:54.616+08	2024-08-20 15:25:30.253+08
33	0	授权码模式	authorization_code	authorization_grant_types	\N	primary	N	1	1	2024-04-17 14:08:54.616+08	2024-08-20 15:25:42.722+08
34	0	在线	true	online_status	\N	success	N	1	1	2024-04-17 14:08:54.616+08	2024-12-09 11:28:16.206+08
35	0	离线	false	online_status	\N	info	N	1	1	2024-04-17 14:08:54.616+08	2024-12-09 11:28:23.414+08
38	0	未认证	NO	pay_certification_status	\N	info	N	1	\N	2024-04-17 14:08:54.616+08	2611-01-27 05:42:52.127+08
39	0	微信支付	WX	pay_certification_status	\N	success	N	1	\N	2024-04-17 14:08:54.616+08	2611-01-27 05:42:52.134+08
40	0	支付宝	ALI	pay_certification_status	\N	primary	N	1	\N	2024-04-17 14:08:54.616+08	2611-01-27 05:42:52.147+08
42	0	全部数据权限	1	data_scope	\N	default	N	1	\N	2024-04-17 14:08:54.616+08	\N
43	0	自定数据权限	2	data_scope	\N	default	N	1	\N	2024-04-17 14:08:54.616+08	\N
44	0	本部门数据权限	3	data_scope	\N	default	N	1	\N	2024-04-17 14:08:54.616+08	\N
45	0	本部门及以下数据权限	4	data_scope	\N	default	N	1	\N	2024-04-17 14:08:54.616+08	\N
46	0	支付成功	SUCCESS	pay_status	\N	success	N	1	\N	2024-04-17 14:08:54.616+08	2611-02-07 18:05:53.601+08
47	0	订单关闭	CLOSED	pay_status	\N	info	N	1	\N	2024-04-17 14:08:54.616+08	2611-02-07 18:05:53.606+08
50	0	未支付	NOTPAY	pay_status	\N	primary	N	1	\N	2024-04-17 14:08:54.616+08	2611-02-07 18:05:53.556+08
51	0	支付异常	ERROR	pay_status	\N	danger	N	1	\N	2024-04-17 14:08:54.616+08	2611-02-07 18:05:53.755+08
52	0	钱包支付	WALLET	pay_type	\N	info	Y	1	\N	2024-04-17 14:08:54.616+08	2611-02-07 18:05:54.027+08
53	0	微信支付	WECHAT	pay_type	\N	success	N	1	\N	2024-04-17 14:08:54.616+08	2611-02-07 18:05:54.033+08
54	0	支付宝	ZFB	pay_type	\N	primary	N	1	\N	2024-04-17 14:08:54.616+08	2611-02-07 18:05:54.038+08
55	0	mqtt	MQTT	protocol_type	\N	primary	N	1	1	2024-04-17 14:08:54.616+08	2024-11-20 14:12:34.01+08
56	0	http	HTTP	protocol_type	\N	primary	N	1	1	2024-04-17 14:08:54.616+08	2024-11-20 14:10:38.284+08
57	0	刷新模式	refresh_token	authorization_grant_types	\N	primary	N	1	1	2024-08-20 15:26:05.564+08	2024-08-20 15:26:05.564+08
58	1	短信模式	sms	authorization_grant_types	\N	primary	N	1	1	2024-08-20 15:26:16.721+08	2024-08-20 15:37:20.459+08
59	0	java	java	protocol_language	\N	primary	Y	1	1	2024-11-20 14:12:12.495+08	2024-11-20 14:18:07.053+08
60	0	lua	lua	protocol_language	\N	primary	N	1	1	2024-11-20 14:12:18.284+08	2024-11-20 14:18:09.674+08
61	0	目录	D	menu_type	\N	info	N	1	1	2024-11-23 15:23:15.006+08	2024-12-18 14:19:29.62+08
62	0	菜单	M	menu_type	\N	primary	N	1	1	2024-11-23 15:23:38.297+08	2024-12-18 14:19:34.651+08
63	0	按钮	B	menu_type	\N	danger	N	1	1	2024-11-23 15:23:51.024+08	2024-12-18 14:19:40.059+08
64	0	字段	F	menu_type	\N	warning	N	1	1	2024-12-18 14:20:37.728+08	2024-12-18 14:20:37.728+08
65	0	收入	0	wallet_record_type	\N	primary	N	1	1	2024-11-25 16:32:32.276+08	2024-11-25 16:32:32.276+08
66	0	支出	1	wallet_record_type	\N	danger	N	1	1	2024-11-25 16:32:40.627+08	2024-11-25 16:32:40.627+08
67	0	属性上报	1	command_type	\N	primary	N	1	1	2024-12-20 08:30:28.03+08	2024-12-20 08:30:28.03+08
68	0	功能调用	2	command_type	\N	danger	N	1	1	2024-12-20 08:30:33.496+08	2024-12-20 08:31:44.876+08
69	0	事件上报	3	command_type	\N	primary	N	1	1	2024-12-20 08:30:41.481+08	2024-12-20 08:30:41.481+08
70	0	设备升级	4	command_type	\N	warning	N	1	1	2024-12-20 08:31:05.369+08	2024-12-20 08:31:05.369+08
71	0	设备上线	5	command_type	\N	success	N	1	1	2024-12-20 08:31:17.241+08	2024-12-20 08:31:17.241+08
72	0	设备离线	6	command_type	\N	info	N	1	1	2024-12-20 08:31:25.085+08	2024-12-20 08:31:25.085+08
73	0	直连产品	0	product_type	\N	default	N	1	1	2024-12-21 16:50:47.695+08	2024-12-21 16:50:47.695+08
74	0	网关产品	1	product_type	\N	default	N	1	1	2024-12-21 16:50:55.66+08	2024-12-21 16:50:55.66+08
75	0	网关子产品	2	product_type	\N	default	N	1	1	2024-12-21 16:51:01.676+08	2024-12-21 16:51:01.676+08
76	0	属性	1	things_model_type	\N	default	N	1	1	2024-12-24 09:45:17.937+08	2024-12-24 09:45:17.937+08
77	0	服务	2	things_model_type	\N	default	N	1	1	2024-12-24 09:46:39.852+08	2024-12-24 16:19:46.611+08
78	0	事件	3	things_model_type	\N	default	N	1	1	2024-12-24 09:46:51.706+08	2024-12-24 09:46:51.706+08
79	0	整形	int32	things_model_datatype	\N	default	N	1	1	2024-12-24 10:27:49.709+08	2024-12-24 10:27:49.709+08
80	0	小数	decimal	things_model_datatype	\N	default	N	1	1	2024-12-24 10:29:08.398+08	2024-12-24 10:29:08.398+08
81	0	布尔	boolean	things_model_datatype	\N	default	N	1	1	2024-12-24 10:31:01.44+08	2024-12-24 10:31:01.44+08
82	0	枚举	enum	things_model_datatype	\N	default	N	1	1	2024-12-24 10:31:14.501+08	2024-12-24 10:31:14.501+08
83	0	字符串	string	things_model_datatype	\N	default	N	1	1	2024-12-24 10:31:23.437+08	2024-12-24 10:31:23.437+08
84	0	数组	array	things_model_datatype	\N	default	N	1	1	2024-12-24 10:31:31.547+08	2024-12-24 10:31:31.547+08
85	0	结构体	struct	things_model_datatype	\N	default	N	1	1	2024-12-24 10:31:48.886+08	2024-12-24 10:31:48.886+08
86	0	设备码模式	urn:ietf:params:oauth:grant-type:device_code	authorization_grant_types	\N	primary	N	1	1	2025-01-04 09:34:46.738+08	2025-01-04 09:34:54.338+08
87	0	token交换模式	urn:ietf:params:oauth:grant-type:token-exchange	authorization_grant_types	\N	primary	N	1	1	2025-01-04 09:35:18.903+08	2025-01-04 09:35:22.611+08
\.


--
-- TOC entry 3365 (class 0 OID 34674)
-- Dependencies: 216
-- Data for Name: global_dict_type; Type: TABLE DATA; Schema: biz_sys; Owner: wz
--

COPY biz_sys.global_dict_type (type_id, dict_name, dict_type, creator_id, updater_id, create_at, update_at) FROM stdin;
1	用户性别	user_sex	1	1	2024-04-17 14:08:54.616+08	2024-11-20 14:29:20.647+08
2	菜单可见状态	menu_visible	1	1	2024-04-17 14:08:54.616+08	2024-11-20 14:30:10.201+08
3	菜单类型	menu_type	1	1	2024-11-23 15:22:03.788+08	2024-11-23 15:22:03.788+08
4	是否禁用	common_disable	1	1	2024-04-17 14:08:54.616+08	2024-11-20 14:34:03.416+08
5	任务状态	sys_job_status	1	1	2024-04-17 14:08:54.616+08	\N
6	任务分组	sys_job_group	1	1	2024-04-17 14:08:54.616+08	\N
7	系统是否	sys_yes_no	1	1	2024-04-17 14:08:54.616+08	\N
8	消息类型	msg_type	1	1	2024-04-17 14:08:54.616+08	2024-12-16 10:30:13.003+08
9	消息状态	msg_status	1	1	2024-04-17 14:08:54.616+08	2024-12-16 10:30:17.169+08
10	操作类型	sys_oper_type	1	1	2024-04-17 14:08:54.616+08	2024-11-20 14:26:29.997+08
11	操作状态	sys_oper_status	1	1	2024-04-17 14:08:54.616+08	2024-11-20 14:13:46.583+08
12	授权类型	authorization_grant_types	1	1	2024-04-17 14:08:54.616+08	2024-08-20 15:24:11.758+08
13	设备连接状态	online_status	1	1	2024-04-17 14:08:54.616+08	2024-12-09 11:28:08.104+08
14	支付认证状态	pay_certification_status	1	1	2024-04-17 14:08:54.616+08	\N
15	数据权限	data_scope	1	1	2024-04-17 14:08:54.616+08	2024-11-20 14:27:19.645+08
16	支付状态	pay_status	1	1	2024-04-17 14:08:54.616+08	2024-11-20 14:03:25.137+08
17	支付方式	pay_type	1	1	2024-04-17 14:08:54.616+08	2024-11-20 14:27:24.525+08
18	协议类型	protocol_type	1	1	2024-04-17 14:08:54.616+08	2024-11-20 14:10:04.677+08
19	协议语言	protocol_language	1	1	2024-11-20 14:12:01.999+08	2024-11-20 14:12:01.999+08
20	钱包记录类型	wallet_record_type	1	1	2024-11-25 16:32:20.36+08	2024-11-25 16:32:20.36+08
21	设备指令类型	command_type	1	1	2024-12-20 08:29:45.314+08	2024-12-21 16:52:53.275+08
22	产品类型	product_type	1	1	2024-12-21 16:50:09.279+08	2024-12-21 16:50:09.279+08
23	物模型类型	things_model_type	1	1	2024-12-24 09:45:09.344+08	2024-12-24 09:45:09.344+08
24	物模型数据类型	things_model_datatype	1	1	2024-12-24 10:27:20.157+08	2024-12-24 10:27:20.157+08
\.


--
-- TOC entry 3366 (class 0 OID 34677)
-- Dependencies: 217
-- Data for Name: sys_config; Type: TABLE DATA; Schema: biz_sys; Owner: wz
--

COPY biz_sys.sys_config (config_id, config_name, config_key, config_value, config_type, creator_id, updater_id, create_at, update_at) FROM stdin;
1	用户管理-账号初始密码	sys.user.initPassword	123456	Y	1	1	2024-04-17 14:08:54.616+08	2024-06-22 14:07:16.632+08
\.


--
-- TOC entry 3367 (class 0 OID 34682)
-- Dependencies: 218
-- Data for Name: sys_login_log; Type: TABLE DATA; Schema: biz_sys; Owner: wz
--

COPY biz_sys.sys_login_log (log_id, tenant_id, user_id, username, grant_type, status, error_msg, login_ip, login_location, browser, os, login_time) FROM stdin;
1878725689120645122	0	1	admin	password	0		127.0.0.1	内网IP	MSEdge	Windows 10 or Windows Server 2016	2025-01-13 16:48:03+08
1878726101143904257	0	1	admin	password	1	密码错误	127.0.0.1	内网IP	MSEdge	Windows 10 or Windows Server 2016	2025-01-13 16:49:42+08
1878965120473382914	0	1	admin	password	0		127.0.0.1	内网IP	MSEdge	Windows 10 or Windows Server 2016	2025-01-14 08:39:28+08
1879068177995546626	0	1879065533604380673	aaaaaa	password	0		127.0.0.1	内网IP	MSEdge	Windows 10 or Windows Server 2016	2025-01-14 15:28:59+08
1879068390428655617	0	1	admin	password	0		127.0.0.1	内网IP	MSEdge	Windows 10 or Windows Server 2016	2025-01-14 15:29:50+08
1879069035697160193	0	1879065533604380673	aaaaaa	password	1	登录失败，用户名或密码错误	127.0.0.1	内网IP	MSEdge	Windows 10 or Windows Server 2016	2025-01-14 15:32:24+08
1879069062951747585	0	1879065533604380673	aaaaaa	password	1	登录失败，用户名或密码错误	127.0.0.1	内网IP	MSEdge	Windows 10 or Windows Server 2016	2025-01-14 15:32:30+08
1879069239200595970	0	1879065533604380673	aaaaaa	password	1	登录失败，用户名或密码错误	127.0.0.1	内网IP	MSEdge	Windows 10 or Windows Server 2016	2025-01-14 15:33:12+08
1879069272524341250	0	1879065533604380673	aaaaaa	password	1	登录失败，用户名或密码错误	127.0.0.1	内网IP	MSEdge	Windows 10 or Windows Server 2016	2025-01-14 15:33:20+08
1879069621209415681	0	1879065533604380673	aaaaaa	password	1	登录失败，用户名或密码错误	127.0.0.1	内网IP	MSEdge	Windows 10 or Windows Server 2016	2025-01-14 15:34:43+08
1879069711890268162	0	1879065533604380673	aaaaaa	password	1	账号被禁用，请联系管理员	127.0.0.1	内网IP	MSEdge	Windows 10 or Windows Server 2016	2025-01-14 15:35:05+08
1879069743938945025	0	1	admin	password	0		127.0.0.1	内网IP	MSEdge	Windows 10 or Windows Server 2016	2025-01-14 15:35:13+08
1879069960931262465	0	1879065533604380673	aaaaaa	password	0		127.0.0.1	内网IP	MSEdge	Windows 10 or Windows Server 2016	2025-01-14 15:36:04+08
1879072794057785345	1774671331416821762	1774671331412627456	testtt	password	0		127.0.0.1	内网IP	MSEdge	Windows 10 or Windows Server 2016	2025-01-14 15:47:20+08
1879078669031981058	0	1	admin	password	0		127.0.0.1	内网IP	MSEdge	Windows 10 or Windows Server 2016	2025-01-14 16:10:40+08
1879079137745453058	1774671331416821762	1774671331412627456	testtt	password	0		127.0.0.1	内网IP	MSEdge	Windows 10 or Windows Server 2016	2025-01-14 16:12:32+08
1879080785192566785	1774671331416821762	1856251200466030593	___sub_	password	1	登录失败，用户名或密码错误	127.0.0.1	内网IP	MSEdge	Windows 10 or Windows Server 2016	2025-01-14 16:19:05+08
1879080802523430913	1774671331416821762	1856251200466030593	___sub_	password	1	登录失败，用户名或密码错误	127.0.0.1	内网IP	MSEdge	Windows 10 or Windows Server 2016	2025-01-14 16:19:09+08
1879080862518755329	1774671331416821762	1856251200466030593	___sub_	password	0		127.0.0.1	内网IP	MSEdge	Windows 10 or Windows Server 2016	2025-01-14 16:19:23+08
1879081108212695041	1774671331416821762	1856251200466030593	___sub_	password	0		127.0.0.1	内网IP	MSEdge	Windows 10 or Windows Server 2016	2025-01-14 16:20:22+08
1879335021226659841	0	1	admin	password	0		127.0.0.1	内网IP	MSEdge	Windows 10 or Windows Server 2016	2025-01-15 09:09:17+08
1879335080223739905	1774671331416821762	1774671331412627456	testtt	password	0		127.0.0.1	内网IP	MSEdge	Windows 10 or Windows Server 2016	2025-01-15 09:09:34+08
1879343620673933313	1774671331416821762	1856251200466030593	___sub_	password	0		127.0.0.1	内网IP	MSEdge	Windows 10 or Windows Server 2016	2025-01-15 09:43:30+08
1879343860781060098	1774671331416821762	1774671331412627456	testtt	password	0		127.0.0.1	内网IP	MSEdge	Windows 10 or Windows Server 2016	2025-01-15 09:44:27+08
1879344051051466753	1774671331416821762	1856251200466030593	___sub_	password	0		127.0.0.1	内网IP	MSEdge	Windows 10 or Windows Server 2016	2025-01-15 09:45:12+08
1879345755956350977	1774671331416821762	1856251200466030593	___sub_	password	0		127.0.0.1	内网IP	MSEdge	Windows 10 or Windows Server 2016	2025-01-15 09:51:59+08
1879347169319358466	0	2	wzkris	password	0		127.0.0.1	内网IP	MSEdge	Windows 10 or Windows Server 2016	2025-01-15 09:57:36+08
1879350439844679681	0	1	admin	password	0		127.0.0.1	内网IP	MSEdge	Windows 10 or Windows Server 2016	2025-01-15 10:10:36+08
1891405734091632641	0	1	admin	password	0		::1	内网IP	MSEdge	Windows 10 or Windows Server 2016	2025-02-17 16:34:02+08
1891407556315394050	0	1	admin	password	0		::1	内网IP	MSEdge	Windows 10 or Windows Server 2016	2025-02-17 16:41:16+08
1891751684630863873	0	1	admin	password	0		::1	内网IP	MSEdge	Windows 10 or Windows Server 2016	2025-02-18 15:28:43+08
1891751876448968706	1774671331416821762	1774671331412627456	testtt	password	0		::1	内网IP	MSEdge	Windows 10 or Windows Server 2016	2025-02-18 15:29:29+08
1892044795118800897	1774671331416821762	1774671331412627456	testtt	password	0		::1	内网IP	MSEdge	Windows 10 or Windows Server 2016	2025-02-19 10:53:26+08
1892088769313447937	1774671331416821762	1774671331412627456	testtt	password	0		::1	内网IP	MSEdge	Windows 10 or Windows Server 2016	2025-02-19 13:48:10+08
1892089328233816066	1774671331416821762	1774671331412627456	testtt	password	0		::1	内网IP	MSEdge	Windows 10 or Windows Server 2016	2025-02-19 13:50:23+08
1892089636531937281	1774671331416821762	1774671331412627456	testtt	password	0		::1	内网IP	MSEdge	Windows 10 or Windows Server 2016	2025-02-19 13:51:37+08
1892089947740905474	1774671331416821762	1774671331412627456	testtt	password	0		::1	内网IP	MSEdge	Windows 10 or Windows Server 2016	2025-02-19 13:52:51+08
1892126225995882498	1774671331416821762	1774671331412627456	testtt	password	0		::1	内网IP	MSEdge	Windows 10 or Windows Server 2016	2025-02-19 16:17:00+08
1892130041822621697	0	1	admin	password	0		::1	内网IP	MSEdge	Windows 10 or Windows Server 2016	2025-02-19 16:32:10+08
1892821588901519362	0	1	admin	password	0		::1	内网IP	MSEdge	Windows 10 or Windows Server 2016	2025-02-21 14:20:08+08
1892831113419317249	0	1	admin	password	0		::1	内网IP	MSEdge	Windows 10 or Windows Server 2016	2025-02-21 14:57:59+08
1893127782929235969	0	1	admin	password	0		::1	内网IP	MSEdge	Windows 10 or Windows Server 2016	2025-02-22 10:36:50+08
1893128099938926593	0	1	admin	password	0		::1	内网IP	MSEdge	Windows 10 or Windows Server 2016	2025-02-22 10:38:06+08
1893129694210654210	0	1	admin	password	0		::1	内网IP	MSEdge	Windows 10 or Windows Server 2016	2025-02-22 10:44:26+08
1893130012206006274	0	1	admin	password	0		::1	内网IP	MSEdge	Windows 10 or Windows Server 2016	2025-02-22 10:45:42+08
1893130611542687745	0	1	admin	password	0		::1	内网IP	MSEdge	Windows 10 or Windows Server 2016	2025-02-22 10:48:05+08
1894669618206437378	0	1	admin	password	0		::1	内网IP	MSEdge	Windows 10 or Windows Server 2016	2025-02-26 16:43:32.829+08
1895017495038423042	0	1	admin	password	0		::1	内网IP	MSEdge	Windows 10 or Windows Server 2016	2025-02-27 15:45:53.103+08
1895017578119196673	0	1	admin	password	0		::1	内网IP	MSEdge	Windows 10 or Windows Server 2016	2025-02-27 15:46:13.004+08
1895017644137541634	0	1	admin	password	0		::1	内网IP	MSEdge	Windows 10 or Windows Server 2016	2025-02-27 15:46:28.741+08
1895017860223889410	0	1	admin	password	0		::1	内网IP	MSEdge	Windows 10 or Windows Server 2016	2025-02-27 15:47:20.26+08
1895030301494243329	0	1	admin	password	0		::1	内网IP	MSEdge	Windows 10 or Windows Server 2016	2025-02-27 16:36:46.477+08
1895274344463699969	0	1	admin	password	0		::1	内网IP	MSEdge	Windows 10 or Windows Server 2016	2025-02-28 08:46:30.804+08
\.


--
-- TOC entry 3368 (class 0 OID 34685)
-- Dependencies: 219
-- Data for Name: sys_message; Type: TABLE DATA; Schema: biz_sys; Owner: wz
--

COPY biz_sys.sys_message (msg_id, title, msg_type, content, status, creator_id, updater_id, create_at, update_at) FROM stdin;
1867761637258874881	测试标题	2	测试内容xxxx	2	1	1	2024-12-14 10:40:50.316+08	2024-12-14 10:40:50.316+08
1867761817215488002	测试标题5	2	内容aauuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuqqqq	2	1	1	2024-12-14 10:41:33.219+08	2024-12-14 10:41:33.219+08
1867761817215488005	测试通知7	1	测测测测测测测测测测测测测测测测测测测测测测测测测测测测测测测测测测测测测测测测测测测测测测测测测测测测测测测测测测测测测测测测测测测测测测测测测测测测测测测测测测测测	2	1	1	2024-12-14 10:41:34.219+08	2024-12-14 10:41:33.219+08
1868480718916243457	冲钱充钱	1	<h1>充充充充充充充充充充充充充充充充充充充充充充充充充充充充充</h1>	1	1	1	2024-12-16 10:18:12.74+08	2025-02-17 16:34:29.135+08
\.


--
-- TOC entry 3369 (class 0 OID 34690)
-- Dependencies: 220
-- Data for Name: sys_notify; Type: TABLE DATA; Schema: biz_sys; Owner: wz
--

COPY biz_sys.sys_notify (notify_id, notify_type, title, content, create_at) FROM stdin;
1854445788424261634	0	测试通知	1111	2024-11-07 16:48:24+08
1873884393595662338	0	重要通知	zzzzzzzzzzzzzzzzzzzzzzzzzzzzzzz	2024-12-31 08:10:29+08
1873884472104644610	0	重要通知	zzzzzzzzzzzzzzzzzzzzzzzzzzzzzzz	2024-12-31 08:10:47+08
1873884863462567938	0	重要通知	zzzzzzzzzzzzzzzzzzzzzzzzzzzzzzz	2024-12-31 08:12:21+08
\.


--
-- TOC entry 3370 (class 0 OID 34693)
-- Dependencies: 221
-- Data for Name: sys_notify_send; Type: TABLE DATA; Schema: biz_sys; Owner: wz
--

COPY biz_sys.sys_notify_send (notify_id, user_id, read_state) FROM stdin;
1854445788424261634	1	0
1873884393595662338	1	0
1873884472104644610	1	1
1873884863462567938	1	1
\.


--
-- TOC entry 3371 (class 0 OID 34696)
-- Dependencies: 222
-- Data for Name: sys_oper_log; Type: TABLE DATA; Schema: biz_sys; Owner: wz
--

COPY biz_sys.sys_oper_log (oper_id, tenant_id, title, sub_title, oper_type, method, request_method, user_id, oper_name, oper_url, oper_ip, oper_location, oper_param, json_result, status, error_msg, oper_time) FROM stdin;
1877963169153585154	0	系统用户	修改用户	2	com.wzkris.user.controller.SysUserController.edit()	POST	1	admin	/sys_user/edit	127.0.0.1	内网IP	{"roleIds":[2],"gender":"0","postIds":[1],"deptId":100,"nickname":"nick_kris","userId":2,"email":"111111@1.com","username":"wzkris","status":"0"}	{"code":0,"message":"Success","timestamp":1736576284910}	0	\N	2025-01-11 14:18:04+08
1878984731398914049	0	角色管理	批量用户授权	4	com.wzkris.user.controller.SysRoleController.batchAuth()	POST	1	admin	/sys_role/authorize_user	127.0.0.1	内网IP	{"roleId":1858701878891327490,"userIds":[2]}	{"code":0,"message":"Success","timestamp":1736819844423}	0	\N	2025-01-14 09:57:24+08
1878984767071469570	0	角色管理	取消授权	4	com.wzkris.user.controller.SysRoleController.cancelAuth()	POST	1	admin	/sys_role/authorize/cancel	127.0.0.1	内网IP	{"roleId":1858701878891327490,"userId":2}	{"code":0,"message":"Success","timestamp":1736819852971}	0	\N	2025-01-14 09:57:32+08
1878985433231802369	0	角色管理	新增角色	1	com.wzkris.user.controller.SysRoleController.add()	POST	1	admin	/sys_role/add	127.0.0.1	内网IP	{"isMenuDisplay":true,"roleName":"123123","isDeptDisplay":true,"deptIds":[],"menuIds":[],"roleSort":0}	{"code":0,"message":"Success","timestamp":1736820011800}	0	\N	2025-01-14 10:00:11+08
1878985443369435138	0	角色管理	新增角色	1	com.wzkris.user.controller.SysRoleController.add()	POST	1	admin	/sys_role/add	127.0.0.1	内网IP	{"isMenuDisplay":true,"roleName":"123","isDeptDisplay":true,"deptIds":[],"menuIds":[],"roleSort":0}	{"code":0,"message":"Success","timestamp":1736820014217}	0	\N	2025-01-14 10:00:14+08
1878986055477772290	0	角色管理	删除角色	3	com.wzkris.user.controller.SysRoleController.remove()	POST	1	admin	/sys_role/remove	127.0.0.1	内网IP	[1878985433164722178]	{"code":0,"message":"Success","timestamp":1736820102512}	0	\N	2025-01-14 10:01:42+08
1878986238873714689	0	角色管理	删除角色	3	com.wzkris.user.controller.SysRoleController.remove()	POST	1	admin	/sys_role/remove	127.0.0.1	内网IP	[1878985443293966338]	{"code":0,"message":"Success","timestamp":1736820198461}	0	\N	2025-01-14 10:03:18+08
1878986473595355138	0	岗位管理	新增岗位	1	com.wzkris.user.controller.SysPostController.add()	POST	1	admin	/sys_post/add	127.0.0.1	内网IP	{"postSort":0,"postName":"111","status":"0"}	{"code":0,"message":"Success","timestamp":1736820259836}	0	\N	2025-01-14 10:04:19+08
1878986484710260737	0	岗位管理	新增岗位	1	com.wzkris.user.controller.SysPostController.add()	POST	1	admin	/sys_post/add	127.0.0.1	内网IP	{"postSort":0,"postName":"1111","status":"0"}	{"code":0,"message":"Success","timestamp":1736820262492}	0	\N	2025-01-14 10:04:22+08
1878986516897349634	0	岗位管理	删除岗位	3	com.wzkris.user.controller.SysPostController.remove()	POST	1	admin	/sys_post/remove	127.0.0.1	内网IP	[1878986473524035585,1878986484630552578]	{"code":0,"message":"Success","timestamp":1736820266892}	0	\N	2025-01-14 10:04:26+08
1878986736771153922	0	部门管理	修改部门	2	com.wzkris.user.controller.SysDeptController.edit()	POST	1	admin	/sys_dept/edit	127.0.0.1	内网IP	{"deptSort":0,"deptName":"最高部门","contact":"15888888888","deptId":100,"tenantId":0,"ancestors":"0","parentId":0,"email":"","status":"0"}	{"code":0,"message":"Success","timestamp":1736820295452}	0	\N	2025-01-14 10:04:55+08
1878988058882883586	0	部门管理	修改部门	2	com.wzkris.user.controller.SysDeptController.edit()	POST	1	admin	/sys_dept/edit	127.0.0.1	内网IP	{"deptSort":0,"deptName":"最高部门","contact":"15888888888","deptId":100,"tenantId":0,"ancestors":"0","parentId":0,"email":"","status":"0"}	{"code":0,"message":"Success","timestamp":1736820637760}	0	\N	2025-01-14 10:10:37+08
1878988117913518082	0	岗位管理	新增岗位	1	com.wzkris.user.controller.SysPostController.add()	POST	1	admin	/sys_post/add	127.0.0.1	内网IP	{"postSort":0,"postName":"111","status":"0"}	{"code":0,"message":"Success","timestamp":1736820651861}	0	\N	2025-01-14 10:10:51+08
1878988129095532545	0	岗位管理	新增岗位	1	com.wzkris.user.controller.SysPostController.add()	POST	1	admin	/sys_post/add	127.0.0.1	内网IP	{"postSort":0,"postName":"111","status":"0"}	{"code":0,"message":"Success","timestamp":1736820654544}	0	\N	2025-01-14 10:10:54+08
1878988143423275010	0	岗位管理	删除岗位	3	com.wzkris.user.controller.SysPostController.remove()	POST	1	admin	/sys_post/remove	127.0.0.1	内网IP	{}	{"code":0,"message":"Success","timestamp":1736820657961}	0	\N	2025-01-14 10:10:57+08
1878988205008240641	0	岗位管理	新增岗位	1	com.wzkris.user.controller.SysPostController.add()	POST	1	admin	/sys_post/add	127.0.0.1	内网IP	{"postSort":0,"postName":"11","status":"0"}	{"code":0,"message":"Success","timestamp":1736820672637}	0	\N	2025-01-14 10:11:12+08
1878988213870804993	0	岗位管理	新增岗位	1	com.wzkris.user.controller.SysPostController.add()	POST	1	admin	/sys_post/add	127.0.0.1	内网IP	{"postSort":0,"postName":"11","status":"0"}	{"code":0,"message":"Success","timestamp":1736820674758}	0	\N	2025-01-14 10:11:14+08
1878988812167299073	0	岗位管理	删除岗位	3	com.wzkris.user.controller.SysPostController.remove()	POST	1	admin	/sys_post/remove	127.0.0.1	内网IP	[1878988204932780033,1878988213807927297]	{"code":0,"message":"Success","timestamp":1736820817345}	0	\N	2025-01-14 10:13:37+08
1878991661873905666	0	菜单管理	修改菜单	2	com.wzkris.user.controller.SysMenuController.edit()	POST	1	admin	/sys_menu/edit	127.0.0.1	内网IP	{"path":"#","isCache":false,"isFrame":false,"icon":"#","menuId":1129,"menuName":"重置租户操作密码","menuType":"B","perms":"tenant:reset_operpwd","isVisible":true,"parentId":601,"menuSort":11,"status":"0"}	{"code":0,"message":"Success","timestamp":1736821496833}	0	\N	2025-01-14 10:24:56+08
1878992217501745153	0	租户管理	重置操作密码	2	com.wzkris.user.controller.SysTenantController.resetOperPwd()	POST	1	admin	/sys_tenant/reset_operpwd	127.0.0.1	内网IP		{"code":1,"message":"操作密码必须为6位数字","timestamp":1736821629256}	1	操作密码必须为6位数字	2025-01-14 10:27:09+08
1878992269699858434	0	租户管理	重置操作密码	2	com.wzkris.user.controller.SysTenantController.resetOperPwd()	POST	1	admin	/sys_tenant/reset_operpwd	127.0.0.1	内网IP		{"code":1,"message":"操作密码必须为6位数字","timestamp":1736821641735}	1	操作密码必须为6位数字	2025-01-14 10:27:21+08
1878995390715019265	0	租户管理	重置操作密码	2	com.wzkris.user.controller.SysTenantController.resetOperPwd()	POST	1	admin	/sys_tenant/reset_operpwd	127.0.0.1	内网IP	{"password":"**","id":1853719125330489346}	{"code":0,"message":"Success","timestamp":1736822385846}	0	\N	2025-01-14 10:39:45+08
1878996024373690369	0	租户管理	重置操作密码	2	com.wzkris.user.controller.SysTenantController.resetOperPwd()	POST	1	admin	/sys_tenant/reset_operpwd	127.0.0.1	内网IP	{"password":"**","id":1853719125330489346}	{"code":0,"message":"Success","timestamp":1736822536925}	0	\N	2025-01-14 10:42:16+08
1878996100881989633	0	租户管理	重置操作密码	2	com.wzkris.user.controller.SysTenantController.resetOperPwd()	POST	1	admin	/sys_tenant/reset_operpwd	127.0.0.1	内网IP	{"password":"**","id":1853719125330489346}	{"code":0,"message":"Success","timestamp":1736822555162}	0	\N	2025-01-14 10:42:35+08
1878996785455316994	0	租户管理	重置操作密码	2	com.wzkris.user.controller.SysTenantController.resetOperPwd()	POST	1	admin	/sys_tenant/reset_operpwd	127.0.0.1	内网IP	{"password":"**","id":1853719125330489346}	{"code":0,"message":"Success","timestamp":1736822718377}	0	\N	2025-01-14 10:45:18+08
1878996887423041538	0	租户管理	重置操作密码	2	com.wzkris.user.controller.SysTenantController.resetOperPwd()	POST	1	admin	/sys_tenant/reset_operpwd	127.0.0.1	内网IP	{"password":"**","id":1853719125330489346}	{"code":0,"message":"Success","timestamp":1736822742689}	0	\N	2025-01-14 10:45:42+08
1878997168202334210	0	系统用户	重置密码	2	com.wzkris.user.controller.SysUserController.resetPwd()	POST	1	admin	/sys_user/reset_password	127.0.0.1	内网IP	{"password":"**","id":1856251200466030593}	{"code":0,"message":"Success","timestamp":1736822809630}	0	\N	2025-01-14 10:46:49+08
1879034323901497346	0	系统用户	修改用户	2	com.wzkris.user.controller.SysUserController.edit()	POST	1	admin	/sys_user/edit	127.0.0.1	内网IP	{"roleIds":[1775445330027577345],"gender":"2","postIds":[],"deptId":1775382319191453698,"nickname":"xxxxxx","userId":1856251200466030593,"username":"___sub_","status":"0"}	{"code":0,"message":"Success","timestamp":1736831668211}	0	\N	2025-01-14 13:14:28+08
1879056027897970690	0	租户管理	修改租户	2	com.wzkris.user.controller.SysTenantController.edit()	POST	1	admin	/sys_tenant/edit	127.0.0.1	内网IP	{"tenantType":"0","expireTime":1733896268000,"accountLimit":5,"roleLimit":5,"tenantName":"租户2","tenantId":1853719125330489346,"packageId":1773620875265482754,"deptLimit":5,"postLimit":5,"remark":"第二个","contactPhone":"00","status":"1"}	{"code":0,"message":"Success","timestamp":1736836842790}	0	\N	2025-01-14 14:40:42+08
1879056738295627778	0	租户管理	新增租户	1	com.wzkris.user.controller.SysTenantController.add()	POST	1	admin	/sys_tenant/add	127.0.0.1	内网IP	{"expireTime":-1,"tenantName":"1111","packageId":1773625804122202113,"contactPhone":"1111","status":"0","username":"1111"}	{"code":0,"message":"Success","timestamp":1736837012258}	0	\N	2025-01-14 14:43:32+08
1879059894253535234	0	租户管理	新增租户	1	com.wzkris.user.controller.SysTenantController.add()	POST	1	admin	/sys_tenant/add	127.0.0.1	内网IP	{"expireTime":-1,"tenantName":"222","packageId":1773625804122202113,"status":"0","username":"222"}	{"code":0,"message":"Success","timestamp":1736837764652}	0	\N	2025-01-14 14:56:04+08
1879065533864361985	0	系统用户	新增用户	1	com.wzkris.user.controller.SysUserController.add()	POST	1	admin	/sys_user/add	127.0.0.1	内网IP	{"roleIds":[],"postIds":[],"username":"aaaaaa"}	{"code":0,"message":"Success","timestamp":1736839109236}	0	\N	2025-01-14 15:18:29+08
1879065889595867138	0	系统账户		2	com.wzkris.user.controller.SysUserOwnController.editPwd()	POST	1	admin	/user/account/edit_password	127.0.0.1	内网IP	{"oldPassword":"**","newPassword":"**"}	{"code":1,"message":"修改密码失败，旧密码错误","timestamp":1736839194092}	1	修改密码失败，旧密码错误	2025-01-14 15:19:54+08
1879065918331043841	0	系统账户		2	com.wzkris.user.controller.SysUserOwnController.editPwd()	POST	1	admin	/user/account/edit_password	127.0.0.1	内网IP	{"oldPassword":"**","newPassword":"**"}	{"code":1,"message":"新密码不能包含空格","timestamp":1736839200939}	1	新密码不能包含空格	2025-01-14 15:20:00+08
1879065927751450625	0	系统账户		2	com.wzkris.user.controller.SysUserOwnController.editPwd()	POST	1	admin	/user/account/edit_password	127.0.0.1	内网IP	{"oldPassword":"**","newPassword":"**"}	{"code":1,"message":"新密码不能包含空格","timestamp":1736839203185}	1	新密码不能包含空格	2025-01-14 15:20:03+08
1879068282488242178	0	系统账户		2	com.wzkris.user.controller.SysUserOwnController.editPwd()	POST	1879065533604380673	aaaaaa	/user/account/edit_password	127.0.0.1	内网IP	{"oldPassword":"**","newPassword":"**"}	{"code":0,"message":"Success","timestamp":1736839764608}	0	\N	2025-01-14 15:29:24+08
1879068914691489794	0	租户管理	删除租户	3	com.wzkris.user.controller.SysTenantController.remove()	POST	1	admin	/sys_tenant/remove	127.0.0.1	内网IP	[1879056737943396354]	{"code":0,"message":"Success","timestamp":1736839915326}	0	\N	2025-01-14 15:31:55+08
1879068924007038977	0	租户管理	删除租户	3	com.wzkris.user.controller.SysTenantController.remove()	POST	1	admin	/sys_tenant/remove	127.0.0.1	内网IP	[1879059893762899969]	{"code":0,"message":"Success","timestamp":1736839917546}	0	\N	2025-01-14 15:31:57+08
1879068982572105730	0	系统用户	状态修改	2	com.wzkris.user.controller.SysUserController.editStatus()	POST	1	admin	/sys_user/edit_status	127.0.0.1	内网IP	{"id":1879065533604380673,"status":"1"}	{"code":0,"message":"Success","timestamp":1736839931499}	0	\N	2025-01-14 15:32:11+08
1879069852865019905	0	系统用户	状态修改	2	com.wzkris.user.controller.SysUserController.editStatus()	POST	1	admin	/sys_user/edit_status	127.0.0.1	内网IP	{"id":1879065533604380673,"status":"0"}	{"code":0,"message":"Success","timestamp":1736840139009}	0	\N	2025-01-14 15:35:39+08
1879070021035638785	0	系统账户		2	com.wzkris.user.controller.SysUserOwnController.editPwd()	POST	1879065533604380673	aaaaaa	/user/account/edit_password	127.0.0.1	内网IP	{"oldPassword":"**","newPassword":"**"}	{"code":0,"message":"Success","timestamp":1736840179112}	0	\N	2025-01-14 15:36:19+08
1879070403480666113	0	系统账户		2	com.wzkris.user.controller.SysUserOwnController.editPwd()	POST	1879065533604380673	aaaaaa	/user/account/edit_password	127.0.0.1	内网IP	{"oldPassword":"**","newPassword":"**"}	{"code":1,"message":"修改密码失败，旧密码错误","timestamp":1736840270280}	1	修改密码失败，旧密码错误	2025-01-14 15:37:50+08
1879070436183654402	0	系统账户		2	com.wzkris.user.controller.SysUserOwnController.editPwd()	POST	1879065533604380673	aaaaaa	/user/account/edit_password	127.0.0.1	内网IP	{"oldPassword":"**","newPassword":"**"}	{"code":1,"message":"修改密码失败，旧密码错误","timestamp":1736840278084}	1	修改密码失败，旧密码错误	2025-01-14 15:37:58+08
1879070511110701058	0	系统账户		2	com.wzkris.user.controller.SysUserOwnController.editPwd()	POST	1879065533604380673	aaaaaa	/user/account/edit_password	127.0.0.1	内网IP	{"oldPassword":"**","newPassword":"**"}	{"code":0,"message":"Success","timestamp":1736840295943}	0	\N	2025-01-14 15:38:15+08
1879070972651913217	0	系统账户		2	com.wzkris.user.controller.SysUserOwnController.editPwd()	POST	1879065533604380673	aaaaaa	/user/account/edit_password	127.0.0.1	内网IP	{"oldPassword":"**","newPassword":"**"}	{"code":1,"message":"新密码长度至少要8位且不能包含空格","timestamp":1736840405927}	1	新密码长度至少要8位且不能包含空格	2025-01-14 15:40:05+08
1879070986094657538	0	系统账户		2	com.wzkris.user.controller.SysUserOwnController.editPwd()	POST	1879065533604380673	aaaaaa	/user/account/edit_password	127.0.0.1	内网IP	{"oldPassword":"**","newPassword":"**"}	{"code":1,"message":"修改密码失败，旧密码错误","timestamp":1736840409192}	1	修改密码失败，旧密码错误	2025-01-14 15:40:09+08
1879071004079833090	0	系统账户		2	com.wzkris.user.controller.SysUserOwnController.editPwd()	POST	1879065533604380673	aaaaaa	/user/account/edit_password	127.0.0.1	内网IP	{"oldPassword":"**","newPassword":"**"}	{"code":1,"message":"修改密码失败，旧密码错误","timestamp":1736840413480}	1	修改密码失败，旧密码错误	2025-01-14 15:40:13+08
1879071213765672962	0	系统账户		2	com.wzkris.user.controller.SysUserOwnController.editPwd()	POST	1879065533604380673	aaaaaa	/user/account/edit_password	127.0.0.1	内网IP	{"oldPassword":"**","newPassword":"**"}	{"code":0,"message":"Success","timestamp":1736840463468}	0	\N	2025-01-14 15:41:03+08
1895274433869484033	0	系统用户	修改用户	2	com.wzkris.user.controller.SysUserController.edit()	POST	1	admin	/sys_user/edit	::1	内网IP	{"roleIds":[],"gender":"2","postIds":[],"userId":1895034552073261057,"username":"asdfasdfaedf","status":"0"}	{"code":0,"message":"Success","timestamp":1740703612188}	0	\N	2025-02-28 08:46:52.188+08
1895274820521398274	0	系统用户	修改用户	2	com.wzkris.user.controller.SysUserController.edit()	POST	1	admin	/sys_user/edit	::1	内网IP	{"roleIds":[],"gender":"2","postIds":[],"deptId":1895034475141337089,"userId":1895034552073261057,"username":"asdfasdfaedf","status":"0"}	{"code":0,"message":"Success","timestamp":1740703704368}	0	\N	2025-02-28 08:48:24.368+08
1895274879111630850	0	系统用户	修改用户	2	com.wzkris.user.controller.SysUserController.edit()	POST	1	admin	/sys_user/edit	::1	内网IP	{"roleIds":[],"gender":"2","postIds":[],"deptId":1895034475141337089,"userId":1895034552073261057,"username":"asdfasdfaedf","status":"0"}	{"code":0,"message":"Success","timestamp":1740703718337}	0	\N	2025-02-28 08:48:38.337+08
1895274960460156929	0	系统用户	修改用户	2	com.wzkris.user.controller.SysUserController.edit()	POST	1	admin	/sys_user/edit	::1	内网IP	{"roleIds":[],"gender":"2","postIds":[],"userId":1895034552073261057,"username":"asdfasdfaedf","status":"0"}	{"code":0,"message":"Success","timestamp":1740703737736}	0	\N	2025-02-28 08:48:57.736+08
1895275135127752706	0	系统用户	修改用户	2	com.wzkris.user.controller.SysUserController.edit()	POST	1	admin	/sys_user/edit	::1	内网IP	{"roleIds":[],"gender":"2","postIds":[],"userId":1895034552073261057,"username":"asdfasdfaedf","status":"0"}	{"code":0,"message":"Success","timestamp":1740703779376}	0	\N	2025-02-28 08:49:39.376+08
1895275513693048833	0	系统用户	修改用户	2	com.wzkris.user.controller.SysUserController.edit()	POST	1	admin	/sys_user/edit	::1	内网IP	{"roleIds":[],"gender":"2","postIds":[],"userId":1895034552073261057,"username":"asdfasdfaedf","status":"0"}	{"code":0,"message":"Success","timestamp":1740703869634}	0	\N	2025-02-28 08:51:09.634+08
1895281164297105409	0	系统用户	删除用户	3	com.wzkris.user.controller.SysUserController.remove()	POST	1	admin	/sys_user/remove	::1	内网IP	[1895034552073261057]	{"code":0,"message":"Success","timestamp":1740705216781}	0	\N	2025-02-28 09:13:36.781+08
1895281184501067778	0	部门管理	删除部门	3	com.wzkris.user.controller.SysDeptController.remove()	POST	1	admin	/sys_dept/remove	::1	内网IP	1895034495743758338	{"code":0,"message":"Success","timestamp":1740705221663}	0	\N	2025-02-28 09:13:41.663+08
1895281193653039105	0	部门管理	删除部门	3	com.wzkris.user.controller.SysDeptController.remove()	POST	1	admin	/sys_dept/remove	::1	内网IP	1895034475141337089	{"code":0,"message":"Success","timestamp":1740705223843}	0	\N	2025-02-28 09:13:43.843+08
\.


--
-- TOC entry 3202 (class 2606 OID 34702)
-- Name: global_dict_data global_dict_data_pkey; Type: CONSTRAINT; Schema: biz_sys; Owner: wz
--

ALTER TABLE ONLY biz_sys.global_dict_data
    ADD CONSTRAINT global_dict_data_pkey PRIMARY KEY (data_id);


--
-- TOC entry 3205 (class 2606 OID 34704)
-- Name: global_dict_type global_dict_type_pkey; Type: CONSTRAINT; Schema: biz_sys; Owner: wz
--

ALTER TABLE ONLY biz_sys.global_dict_type
    ADD CONSTRAINT global_dict_type_pkey PRIMARY KEY (type_id);


--
-- TOC entry 3208 (class 2606 OID 34706)
-- Name: sys_config sys_config_pkey; Type: CONSTRAINT; Schema: biz_sys; Owner: wz
--

ALTER TABLE ONLY biz_sys.sys_config
    ADD CONSTRAINT sys_config_pkey PRIMARY KEY (config_id);


--
-- TOC entry 3212 (class 2606 OID 34708)
-- Name: sys_login_log sys_login_log_pkey; Type: CONSTRAINT; Schema: biz_sys; Owner: wz
--

ALTER TABLE ONLY biz_sys.sys_login_log
    ADD CONSTRAINT sys_login_log_pkey PRIMARY KEY (log_id);


--
-- TOC entry 3214 (class 2606 OID 34710)
-- Name: sys_message sys_message_pkey; Type: CONSTRAINT; Schema: biz_sys; Owner: wz
--

ALTER TABLE ONLY biz_sys.sys_message
    ADD CONSTRAINT sys_message_pkey PRIMARY KEY (msg_id);


--
-- TOC entry 3216 (class 2606 OID 34712)
-- Name: sys_notify sys_notify_pkey; Type: CONSTRAINT; Schema: biz_sys; Owner: wz
--

ALTER TABLE ONLY biz_sys.sys_notify
    ADD CONSTRAINT sys_notify_pkey PRIMARY KEY (notify_id);


--
-- TOC entry 3218 (class 2606 OID 34714)
-- Name: sys_notify_send sys_notify_send_pkey; Type: CONSTRAINT; Schema: biz_sys; Owner: wz
--

ALTER TABLE ONLY biz_sys.sys_notify_send
    ADD CONSTRAINT sys_notify_send_pkey PRIMARY KEY (notify_id, user_id);


--
-- TOC entry 3221 (class 2606 OID 34716)
-- Name: sys_oper_log sys_oper_log_pkey; Type: CONSTRAINT; Schema: biz_sys; Owner: wz
--

ALTER TABLE ONLY biz_sys.sys_oper_log
    ADD CONSTRAINT sys_oper_log_pkey PRIMARY KEY (oper_id);


--
-- TOC entry 3203 (class 1259 OID 34717)
-- Name: i_dict_type; Type: INDEX; Schema: biz_sys; Owner: wz
--

CREATE INDEX i_dict_type ON biz_sys.global_dict_data USING btree (dict_type);


--
-- TOC entry 3210 (class 1259 OID 34718)
-- Name: i_sys_login_log_login_time; Type: INDEX; Schema: biz_sys; Owner: wz
--

CREATE INDEX i_sys_login_log_login_time ON biz_sys.sys_login_log USING brin (login_time);


--
-- TOC entry 3219 (class 1259 OID 34719)
-- Name: i_sys_oper_log_oper_time; Type: INDEX; Schema: biz_sys; Owner: wz
--

CREATE INDEX i_sys_oper_log_oper_time ON biz_sys.sys_oper_log USING brin (oper_time);


--
-- TOC entry 3209 (class 1259 OID 34720)
-- Name: u_i_config_key; Type: INDEX; Schema: biz_sys; Owner: wz
--

CREATE UNIQUE INDEX u_i_config_key ON biz_sys.sys_config USING btree (config_key);


--
-- TOC entry 3206 (class 1259 OID 34721)
-- Name: u_i_dict_type; Type: INDEX; Schema: biz_sys; Owner: wz
--

CREATE UNIQUE INDEX u_i_dict_type ON biz_sys.global_dict_type USING btree (dict_type);


-- Completed on 2025-03-03 14:07:12

--
-- PostgreSQL database dump complete
--

