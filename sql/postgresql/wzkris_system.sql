--
-- PostgreSQL database dump
--

-- Dumped from database version 15.5
-- Dumped by pg_dump version 16.0

-- Started on 2025-04-11 14:10:16

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
-- TOC entry 215 (class 1259 OID 34677)
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
-- TOC entry 3369 (class 0 OID 0)
-- Dependencies: 215
-- Name: TABLE sys_config; Type: COMMENT; Schema: biz_sys; Owner: wz
--

COMMENT ON TABLE biz_sys.sys_config IS '参数配置表';


--
-- TOC entry 3370 (class 0 OID 0)
-- Dependencies: 215
-- Name: COLUMN sys_config.config_id; Type: COMMENT; Schema: biz_sys; Owner: wz
--

COMMENT ON COLUMN biz_sys.sys_config.config_id IS '参数主键';


--
-- TOC entry 3371 (class 0 OID 0)
-- Dependencies: 215
-- Name: COLUMN sys_config.config_name; Type: COMMENT; Schema: biz_sys; Owner: wz
--

COMMENT ON COLUMN biz_sys.sys_config.config_name IS '参数名称';


--
-- TOC entry 3372 (class 0 OID 0)
-- Dependencies: 215
-- Name: COLUMN sys_config.config_key; Type: COMMENT; Schema: biz_sys; Owner: wz
--

COMMENT ON COLUMN biz_sys.sys_config.config_key IS '参数键名';


--
-- TOC entry 3373 (class 0 OID 0)
-- Dependencies: 215
-- Name: COLUMN sys_config.config_value; Type: COMMENT; Schema: biz_sys; Owner: wz
--

COMMENT ON COLUMN biz_sys.sys_config.config_value IS '参数键值';


--
-- TOC entry 3374 (class 0 OID 0)
-- Dependencies: 215
-- Name: COLUMN sys_config.config_type; Type: COMMENT; Schema: biz_sys; Owner: wz
--

COMMENT ON COLUMN biz_sys.sys_config.config_type IS '系统内置（Y是 N否）';


--
-- TOC entry 3375 (class 0 OID 0)
-- Dependencies: 215
-- Name: COLUMN sys_config.creator_id; Type: COMMENT; Schema: biz_sys; Owner: wz
--

COMMENT ON COLUMN biz_sys.sys_config.creator_id IS '创建者';


--
-- TOC entry 3376 (class 0 OID 0)
-- Dependencies: 215
-- Name: COLUMN sys_config.updater_id; Type: COMMENT; Schema: biz_sys; Owner: wz
--

COMMENT ON COLUMN biz_sys.sys_config.updater_id IS '更新者';


--
-- TOC entry 221 (class 1259 OID 35418)
-- Name: sys_dict; Type: TABLE; Schema: biz_sys; Owner: wz
--

CREATE TABLE biz_sys.sys_dict (
    dict_id bigint NOT NULL,
    dict_key character varying(32) NOT NULL,
    dict_name character varying(32) NOT NULL,
    dict_value jsonb NOT NULL,
    remark text,
    creator_id bigint NOT NULL,
    updater_id bigint,
    create_at timestamp(6) with time zone NOT NULL,
    update_at timestamp(6) with time zone
);


ALTER TABLE biz_sys.sys_dict OWNER TO wz;

--
-- TOC entry 3377 (class 0 OID 0)
-- Dependencies: 221
-- Name: COLUMN sys_dict.dict_id; Type: COMMENT; Schema: biz_sys; Owner: wz
--

COMMENT ON COLUMN biz_sys.sys_dict.dict_id IS '字典主键';


--
-- TOC entry 3378 (class 0 OID 0)
-- Dependencies: 221
-- Name: COLUMN sys_dict.dict_key; Type: COMMENT; Schema: biz_sys; Owner: wz
--

COMMENT ON COLUMN biz_sys.sys_dict.dict_key IS '字典键';


--
-- TOC entry 3379 (class 0 OID 0)
-- Dependencies: 221
-- Name: COLUMN sys_dict.dict_name; Type: COMMENT; Schema: biz_sys; Owner: wz
--

COMMENT ON COLUMN biz_sys.sys_dict.dict_name IS '字典名称';


--
-- TOC entry 3380 (class 0 OID 0)
-- Dependencies: 221
-- Name: COLUMN sys_dict.dict_value; Type: COMMENT; Schema: biz_sys; Owner: wz
--

COMMENT ON COLUMN biz_sys.sys_dict.dict_value IS '字典键值';


--
-- TOC entry 3381 (class 0 OID 0)
-- Dependencies: 221
-- Name: COLUMN sys_dict.remark; Type: COMMENT; Schema: biz_sys; Owner: wz
--

COMMENT ON COLUMN biz_sys.sys_dict.remark IS '备注';


--
-- TOC entry 3382 (class 0 OID 0)
-- Dependencies: 221
-- Name: COLUMN sys_dict.creator_id; Type: COMMENT; Schema: biz_sys; Owner: wz
--

COMMENT ON COLUMN biz_sys.sys_dict.creator_id IS '创建者';


--
-- TOC entry 3383 (class 0 OID 0)
-- Dependencies: 221
-- Name: COLUMN sys_dict.updater_id; Type: COMMENT; Schema: biz_sys; Owner: wz
--

COMMENT ON COLUMN biz_sys.sys_dict.updater_id IS '更新者';


--
-- TOC entry 3384 (class 0 OID 0)
-- Dependencies: 221
-- Name: COLUMN sys_dict.create_at; Type: COMMENT; Schema: biz_sys; Owner: wz
--

COMMENT ON COLUMN biz_sys.sys_dict.create_at IS '创建时间';


--
-- TOC entry 3385 (class 0 OID 0)
-- Dependencies: 221
-- Name: COLUMN sys_dict.update_at; Type: COMMENT; Schema: biz_sys; Owner: wz
--

COMMENT ON COLUMN biz_sys.sys_dict.update_at IS '更新时间';


--
-- TOC entry 216 (class 1259 OID 34682)
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
-- TOC entry 3386 (class 0 OID 0)
-- Dependencies: 216
-- Name: TABLE sys_login_log; Type: COMMENT; Schema: biz_sys; Owner: wz
--

COMMENT ON TABLE biz_sys.sys_login_log IS '后台登录日志';


--
-- TOC entry 3387 (class 0 OID 0)
-- Dependencies: 216
-- Name: COLUMN sys_login_log.tenant_id; Type: COMMENT; Schema: biz_sys; Owner: wz
--

COMMENT ON COLUMN biz_sys.sys_login_log.tenant_id IS '租户ID';


--
-- TOC entry 3388 (class 0 OID 0)
-- Dependencies: 216
-- Name: COLUMN sys_login_log.user_id; Type: COMMENT; Schema: biz_sys; Owner: wz
--

COMMENT ON COLUMN biz_sys.sys_login_log.user_id IS '用户ID';


--
-- TOC entry 3389 (class 0 OID 0)
-- Dependencies: 216
-- Name: COLUMN sys_login_log.username; Type: COMMENT; Schema: biz_sys; Owner: wz
--

COMMENT ON COLUMN biz_sys.sys_login_log.username IS '用户名';


--
-- TOC entry 3390 (class 0 OID 0)
-- Dependencies: 216
-- Name: COLUMN sys_login_log.grant_type; Type: COMMENT; Schema: biz_sys; Owner: wz
--

COMMENT ON COLUMN biz_sys.sys_login_log.grant_type IS '授权类型';


--
-- TOC entry 3391 (class 0 OID 0)
-- Dependencies: 216
-- Name: COLUMN sys_login_log.status; Type: COMMENT; Schema: biz_sys; Owner: wz
--

COMMENT ON COLUMN biz_sys.sys_login_log.status IS '登录状态（0正常 1异常）';


--
-- TOC entry 3392 (class 0 OID 0)
-- Dependencies: 216
-- Name: COLUMN sys_login_log.error_msg; Type: COMMENT; Schema: biz_sys; Owner: wz
--

COMMENT ON COLUMN biz_sys.sys_login_log.error_msg IS '失败信息';


--
-- TOC entry 3393 (class 0 OID 0)
-- Dependencies: 216
-- Name: COLUMN sys_login_log.login_ip; Type: COMMENT; Schema: biz_sys; Owner: wz
--

COMMENT ON COLUMN biz_sys.sys_login_log.login_ip IS '登录ip';


--
-- TOC entry 3394 (class 0 OID 0)
-- Dependencies: 216
-- Name: COLUMN sys_login_log.login_location; Type: COMMENT; Schema: biz_sys; Owner: wz
--

COMMENT ON COLUMN biz_sys.sys_login_log.login_location IS '登录地址';


--
-- TOC entry 3395 (class 0 OID 0)
-- Dependencies: 216
-- Name: COLUMN sys_login_log.browser; Type: COMMENT; Schema: biz_sys; Owner: wz
--

COMMENT ON COLUMN biz_sys.sys_login_log.browser IS '浏览器类型';


--
-- TOC entry 3396 (class 0 OID 0)
-- Dependencies: 216
-- Name: COLUMN sys_login_log.os; Type: COMMENT; Schema: biz_sys; Owner: wz
--

COMMENT ON COLUMN biz_sys.sys_login_log.os IS '操作系统';


--
-- TOC entry 3397 (class 0 OID 0)
-- Dependencies: 216
-- Name: COLUMN sys_login_log.login_time; Type: COMMENT; Schema: biz_sys; Owner: wz
--

COMMENT ON COLUMN biz_sys.sys_login_log.login_time IS '登录时间';


--
-- TOC entry 217 (class 1259 OID 34685)
-- Name: sys_message; Type: TABLE; Schema: biz_sys; Owner: wz
--

CREATE TABLE biz_sys.sys_message (
    msg_id bigint NOT NULL,
    title character varying(30) NOT NULL,
    content text NOT NULL,
    status character(1) NOT NULL,
    creator_id bigint NOT NULL,
    updater_id bigint,
    create_at timestamp with time zone,
    update_at timestamp with time zone
);


ALTER TABLE biz_sys.sys_message OWNER TO wz;

--
-- TOC entry 3398 (class 0 OID 0)
-- Dependencies: 217
-- Name: TABLE sys_message; Type: COMMENT; Schema: biz_sys; Owner: wz
--

COMMENT ON TABLE biz_sys.sys_message IS '系统消息表';


--
-- TOC entry 3399 (class 0 OID 0)
-- Dependencies: 217
-- Name: COLUMN sys_message.msg_id; Type: COMMENT; Schema: biz_sys; Owner: wz
--

COMMENT ON COLUMN biz_sys.sys_message.msg_id IS '消息ID';


--
-- TOC entry 3400 (class 0 OID 0)
-- Dependencies: 217
-- Name: COLUMN sys_message.title; Type: COMMENT; Schema: biz_sys; Owner: wz
--

COMMENT ON COLUMN biz_sys.sys_message.title IS '消息标题';


--
-- TOC entry 3401 (class 0 OID 0)
-- Dependencies: 217
-- Name: COLUMN sys_message.content; Type: COMMENT; Schema: biz_sys; Owner: wz
--

COMMENT ON COLUMN biz_sys.sys_message.content IS '消息内容';


--
-- TOC entry 3402 (class 0 OID 0)
-- Dependencies: 217
-- Name: COLUMN sys_message.status; Type: COMMENT; Schema: biz_sys; Owner: wz
--

COMMENT ON COLUMN biz_sys.sys_message.status IS '消息状态（0草稿 1关闭 2已发送）';


--
-- TOC entry 3403 (class 0 OID 0)
-- Dependencies: 217
-- Name: COLUMN sys_message.creator_id; Type: COMMENT; Schema: biz_sys; Owner: wz
--

COMMENT ON COLUMN biz_sys.sys_message.creator_id IS '创建者ID';


--
-- TOC entry 3404 (class 0 OID 0)
-- Dependencies: 217
-- Name: COLUMN sys_message.updater_id; Type: COMMENT; Schema: biz_sys; Owner: wz
--

COMMENT ON COLUMN biz_sys.sys_message.updater_id IS '更新者ID';


--
-- TOC entry 3405 (class 0 OID 0)
-- Dependencies: 217
-- Name: COLUMN sys_message.create_at; Type: COMMENT; Schema: biz_sys; Owner: wz
--

COMMENT ON COLUMN biz_sys.sys_message.create_at IS '创建时间';


--
-- TOC entry 3406 (class 0 OID 0)
-- Dependencies: 217
-- Name: COLUMN sys_message.update_at; Type: COMMENT; Schema: biz_sys; Owner: wz
--

COMMENT ON COLUMN biz_sys.sys_message.update_at IS '更新时间';


--
-- TOC entry 218 (class 1259 OID 34690)
-- Name: sys_notice; Type: TABLE; Schema: biz_sys; Owner: wz
--

CREATE TABLE biz_sys.sys_notice (
    notice_id bigint NOT NULL,
    notice_type character(1) NOT NULL,
    title character varying(32) NOT NULL,
    content text NOT NULL,
    create_at timestamp with time zone NOT NULL,
    creator_id bigint NOT NULL
);


ALTER TABLE biz_sys.sys_notice OWNER TO wz;

--
-- TOC entry 3407 (class 0 OID 0)
-- Dependencies: 218
-- Name: TABLE sys_notice; Type: COMMENT; Schema: biz_sys; Owner: wz
--

COMMENT ON TABLE biz_sys.sys_notice IS '系统通知表';


--
-- TOC entry 3408 (class 0 OID 0)
-- Dependencies: 218
-- Name: COLUMN sys_notice.notice_type; Type: COMMENT; Schema: biz_sys; Owner: wz
--

COMMENT ON COLUMN biz_sys.sys_notice.notice_type IS '通知类型（0系统通知 1设备告警）';


--
-- TOC entry 3409 (class 0 OID 0)
-- Dependencies: 218
-- Name: COLUMN sys_notice.title; Type: COMMENT; Schema: biz_sys; Owner: wz
--

COMMENT ON COLUMN biz_sys.sys_notice.title IS '标题';


--
-- TOC entry 3410 (class 0 OID 0)
-- Dependencies: 218
-- Name: COLUMN sys_notice.content; Type: COMMENT; Schema: biz_sys; Owner: wz
--

COMMENT ON COLUMN biz_sys.sys_notice.content IS '通知内容';


--
-- TOC entry 3411 (class 0 OID 0)
-- Dependencies: 218
-- Name: COLUMN sys_notice.create_at; Type: COMMENT; Schema: biz_sys; Owner: wz
--

COMMENT ON COLUMN biz_sys.sys_notice.create_at IS '创建时间';


--
-- TOC entry 3412 (class 0 OID 0)
-- Dependencies: 218
-- Name: COLUMN sys_notice.creator_id; Type: COMMENT; Schema: biz_sys; Owner: wz
--

COMMENT ON COLUMN biz_sys.sys_notice.creator_id IS '创建者ID';


--
-- TOC entry 219 (class 1259 OID 34693)
-- Name: sys_notice_user; Type: TABLE; Schema: biz_sys; Owner: wz
--

CREATE TABLE biz_sys.sys_notice_user (
    notice_id bigint NOT NULL,
    user_id bigint NOT NULL,
    read_state character(1) NOT NULL
);


ALTER TABLE biz_sys.sys_notice_user OWNER TO wz;

--
-- TOC entry 3413 (class 0 OID 0)
-- Dependencies: 219
-- Name: TABLE sys_notice_user; Type: COMMENT; Schema: biz_sys; Owner: wz
--

COMMENT ON TABLE biz_sys.sys_notice_user IS '通知发送表';


--
-- TOC entry 3414 (class 0 OID 0)
-- Dependencies: 219
-- Name: COLUMN sys_notice_user.notice_id; Type: COMMENT; Schema: biz_sys; Owner: wz
--

COMMENT ON COLUMN biz_sys.sys_notice_user.notice_id IS '通知ID';


--
-- TOC entry 3415 (class 0 OID 0)
-- Dependencies: 219
-- Name: COLUMN sys_notice_user.user_id; Type: COMMENT; Schema: biz_sys; Owner: wz
--

COMMENT ON COLUMN biz_sys.sys_notice_user.user_id IS '接收用户ID';


--
-- TOC entry 3416 (class 0 OID 0)
-- Dependencies: 219
-- Name: COLUMN sys_notice_user.read_state; Type: COMMENT; Schema: biz_sys; Owner: wz
--

COMMENT ON COLUMN biz_sys.sys_notice_user.read_state IS '已读1 未读0';


--
-- TOC entry 220 (class 1259 OID 34696)
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
-- TOC entry 3417 (class 0 OID 0)
-- Dependencies: 220
-- Name: TABLE sys_oper_log; Type: COMMENT; Schema: biz_sys; Owner: wz
--

COMMENT ON TABLE biz_sys.sys_oper_log IS '操作日志记录';


--
-- TOC entry 3418 (class 0 OID 0)
-- Dependencies: 220
-- Name: COLUMN sys_oper_log.oper_id; Type: COMMENT; Schema: biz_sys; Owner: wz
--

COMMENT ON COLUMN biz_sys.sys_oper_log.oper_id IS '日志主键';


--
-- TOC entry 3419 (class 0 OID 0)
-- Dependencies: 220
-- Name: COLUMN sys_oper_log.tenant_id; Type: COMMENT; Schema: biz_sys; Owner: wz
--

COMMENT ON COLUMN biz_sys.sys_oper_log.tenant_id IS '租户ID';


--
-- TOC entry 3420 (class 0 OID 0)
-- Dependencies: 220
-- Name: COLUMN sys_oper_log.title; Type: COMMENT; Schema: biz_sys; Owner: wz
--

COMMENT ON COLUMN biz_sys.sys_oper_log.title IS '模块标题';


--
-- TOC entry 3421 (class 0 OID 0)
-- Dependencies: 220
-- Name: COLUMN sys_oper_log.sub_title; Type: COMMENT; Schema: biz_sys; Owner: wz
--

COMMENT ON COLUMN biz_sys.sys_oper_log.sub_title IS '子标题';


--
-- TOC entry 3422 (class 0 OID 0)
-- Dependencies: 220
-- Name: COLUMN sys_oper_log.oper_type; Type: COMMENT; Schema: biz_sys; Owner: wz
--

COMMENT ON COLUMN biz_sys.sys_oper_log.oper_type IS '操作类型（0其他 1新增 2修改 3删除）';


--
-- TOC entry 3423 (class 0 OID 0)
-- Dependencies: 220
-- Name: COLUMN sys_oper_log.method; Type: COMMENT; Schema: biz_sys; Owner: wz
--

COMMENT ON COLUMN biz_sys.sys_oper_log.method IS '方法名称';


--
-- TOC entry 3424 (class 0 OID 0)
-- Dependencies: 220
-- Name: COLUMN sys_oper_log.request_method; Type: COMMENT; Schema: biz_sys; Owner: wz
--

COMMENT ON COLUMN biz_sys.sys_oper_log.request_method IS '请求方式';


--
-- TOC entry 3425 (class 0 OID 0)
-- Dependencies: 220
-- Name: COLUMN sys_oper_log.user_id; Type: COMMENT; Schema: biz_sys; Owner: wz
--

COMMENT ON COLUMN biz_sys.sys_oper_log.user_id IS '操作人员ID';


--
-- TOC entry 3426 (class 0 OID 0)
-- Dependencies: 220
-- Name: COLUMN sys_oper_log.oper_name; Type: COMMENT; Schema: biz_sys; Owner: wz
--

COMMENT ON COLUMN biz_sys.sys_oper_log.oper_name IS '操作人员';


--
-- TOC entry 3427 (class 0 OID 0)
-- Dependencies: 220
-- Name: COLUMN sys_oper_log.oper_url; Type: COMMENT; Schema: biz_sys; Owner: wz
--

COMMENT ON COLUMN biz_sys.sys_oper_log.oper_url IS '请求URL';


--
-- TOC entry 3428 (class 0 OID 0)
-- Dependencies: 220
-- Name: COLUMN sys_oper_log.oper_ip; Type: COMMENT; Schema: biz_sys; Owner: wz
--

COMMENT ON COLUMN biz_sys.sys_oper_log.oper_ip IS '主机地址';


--
-- TOC entry 3429 (class 0 OID 0)
-- Dependencies: 220
-- Name: COLUMN sys_oper_log.oper_location; Type: COMMENT; Schema: biz_sys; Owner: wz
--

COMMENT ON COLUMN biz_sys.sys_oper_log.oper_location IS '操作地点';


--
-- TOC entry 3430 (class 0 OID 0)
-- Dependencies: 220
-- Name: COLUMN sys_oper_log.oper_param; Type: COMMENT; Schema: biz_sys; Owner: wz
--

COMMENT ON COLUMN biz_sys.sys_oper_log.oper_param IS '请求参数';


--
-- TOC entry 3431 (class 0 OID 0)
-- Dependencies: 220
-- Name: COLUMN sys_oper_log.json_result; Type: COMMENT; Schema: biz_sys; Owner: wz
--

COMMENT ON COLUMN biz_sys.sys_oper_log.json_result IS '返回参数';


--
-- TOC entry 3432 (class 0 OID 0)
-- Dependencies: 220
-- Name: COLUMN sys_oper_log.status; Type: COMMENT; Schema: biz_sys; Owner: wz
--

COMMENT ON COLUMN biz_sys.sys_oper_log.status IS '操作状态（0正常 1异常）';


--
-- TOC entry 3433 (class 0 OID 0)
-- Dependencies: 220
-- Name: COLUMN sys_oper_log.error_msg; Type: COMMENT; Schema: biz_sys; Owner: wz
--

COMMENT ON COLUMN biz_sys.sys_oper_log.error_msg IS '错误消息';


--
-- TOC entry 3434 (class 0 OID 0)
-- Dependencies: 220
-- Name: COLUMN sys_oper_log.oper_time; Type: COMMENT; Schema: biz_sys; Owner: wz
--

COMMENT ON COLUMN biz_sys.sys_oper_log.oper_time IS '操作时间';


--
-- TOC entry 3357 (class 0 OID 34677)
-- Dependencies: 215
-- Data for Name: sys_config; Type: TABLE DATA; Schema: biz_sys; Owner: wz
--

COPY biz_sys.sys_config (config_id, config_name, config_key, config_value, config_type, creator_id, updater_id, create_at, update_at) FROM stdin;
1	用户管理-账号初始密码	sys.user.initPassword	123456	Y	1	1	2024-04-17 14:08:54.616+08	2025-04-01 15:41:18.055+08
\.


--
-- TOC entry 3363 (class 0 OID 35418)
-- Dependencies: 221
-- Data for Name: sys_dict; Type: TABLE DATA; Schema: biz_sys; Owner: wz
--

COPY biz_sys.sys_dict (dict_id, dict_key, dict_name, dict_value, remark, creator_id, updater_id, create_at, update_at) FROM stdin;
1905175932909101057	user_sex	用户性别	[{"label": "男", "value": "0", "tableCls": ""}, {"label": "女", "value": "1", "tableCls": ""}, {"label": "未知", "value": "2", "tableCls": ""}]	\N	1	1	2024-04-17 14:08:54.616+08	2024-11-20 14:29:20.647+08
1905175933034930178	menu_visible	菜单可见状态	[{"label": "显示", "value": "true", "tableCls": "primary"}, {"label": "隐藏", "value": "false", "tableCls": "danger"}]	\N	1	1	2024-04-17 14:08:54.616+08	2024-11-20 14:30:10.201+08
1905175933034930179	menu_type	菜单类型	[{"label": "目录", "value": "D", "tableCls": "info"}, {"label": "菜单", "value": "M", "tableCls": "primary"}, {"label": "按钮", "value": "B", "tableCls": "danger"}, {"label": "字段", "value": "F", "tableCls": "warning"}]	\N	1	1	2024-11-23 15:22:03.788+08	2024-11-23 15:22:03.788+08
1905175933097844737	common_disable	是否禁用	[{"label": "正常", "value": "0", "tableCls": "primary"}, {"label": "停用", "value": "1", "tableCls": "danger"}]	\N	1	1	2024-04-17 14:08:54.616+08	2024-11-20 14:34:03.416+08
1905175933097844738	sys_job_status	任务状态	[{"label": "正常", "value": "0", "tableCls": "primary"}, {"label": "暂停", "value": "1", "tableCls": "danger"}]	\N	1	1	2024-04-17 14:08:54.616+08	\N
1905175933097844739	sys_job_group	任务分组	[{"label": "默认", "value": "DEFAULT", "tableCls": ""}, {"label": "系统", "value": "SYSTEM", "tableCls": ""}]	\N	1	1	2024-04-17 14:08:54.616+08	\N
1905175933164953602	sys_yes_no	系统是否	[{"label": "是", "value": "Y", "tableCls": "primary"}, {"label": "否", "value": "N", "tableCls": "danger"}]	\N	1	1	2024-04-17 14:08:54.616+08	\N
1905175933164953603	msg_type	消息类型	[{"label": "系统公告", "value": "1", "tableCls": "primary"}, {"label": "APP公告", "value": "2", "tableCls": "success"}]	\N	1	1	2024-04-17 14:08:54.616+08	2024-12-16 10:30:13.003+08
1905175933164953604	msg_status	消息状态	[{"label": "已发布", "value": "2", "tableCls": "primary"}, {"label": "草稿", "value": "0", "tableCls": "info"}, {"label": "关闭", "value": "1", "tableCls": "danger"}]	\N	1	1	2024-04-17 14:08:54.616+08	2024-12-16 10:30:17.169+08
1905175933227868161	sys_oper_type	操作类型	[{"label": "其他", "value": "0", "tableCls": "info"}, {"label": "新增", "value": "1", "tableCls": "info"}, {"label": "修改", "value": "2", "tableCls": "info"}, {"label": "删除", "value": "3", "tableCls": "danger"}, {"label": "授权", "value": "4", "tableCls": "primary"}, {"label": "导出", "value": "5", "tableCls": "warning"}, {"label": "导入", "value": "6", "tableCls": "warning"}]	\N	1	1	2024-04-17 14:08:54.616+08	2024-11-20 14:26:29.997+08
1905175933227868162	sys_oper_status	操作状态	[{"label": "成功", "value": "0", "tableCls": "primary"}, {"label": "失败", "value": "1", "tableCls": "danger"}]	\N	1	1	2024-04-17 14:08:54.616+08	2024-11-20 14:13:46.583+08
1905175933227868163	authorization_grant_types	授权类型	[{"label": "刷新模式", "value": "refresh_token", "tableCls": "primary"}, {"label": "客户端模式", "value": "client_credentials", "tableCls": "primary"}, {"label": "授权码模式", "value": "authorization_code", "tableCls": "primary"}, {"label": "密码模式", "value": "password", "tableCls": "primary"}, {"label": "token交换模式", "value": "urn:ietf:params:oauth:grant-type:token-exchange", "tableCls": "primary"}, {"label": "设备码模式", "value": "urn:ietf:params:oauth:grant-type:device_code", "tableCls": "primary"}, {"label": "短信模式", "value": "sms", "tableCls": "primary"}]	\N	1	1	2024-04-17 14:08:54.616+08	2024-08-20 15:24:11.758+08
1905175933227868164	online_status	设备连接状态	[{"label": "在线", "value": "true", "tableCls": "success"}, {"label": "离线", "value": "false", "tableCls": "info"}]	\N	1	1	2024-04-17 14:08:54.616+08	2024-12-09 11:28:08.104+08
1905175933290782722	pay_certification_status	支付认证状态	[{"label": "未认证", "value": "NO", "tableCls": "info"}, {"label": "微信支付", "value": "WX", "tableCls": "success"}, {"label": "支付宝", "value": "ALI", "tableCls": "primary"}]	\N	1	1	2024-04-17 14:08:54.616+08	\N
1905175933290782723	data_scope	数据权限	[{"label": "全部数据权限", "value": "1", "tableCls": "default"}, {"label": "自定数据权限", "value": "2", "tableCls": "default"}, {"label": "本部门数据权限", "value": "3", "tableCls": "default"}, {"label": "本部门及以下数据权限", "value": "4", "tableCls": "default"}]	\N	1	1	2024-04-17 14:08:54.616+08	2024-11-20 14:27:19.645+08
1905175933290782724	pay_status	支付状态	[{"label": "支付成功", "value": "SUCCESS", "tableCls": "success"}, {"label": "订单关闭", "value": "CLOSED", "tableCls": "info"}, {"label": "未支付", "value": "NOTPAY", "tableCls": "primary"}, {"label": "支付异常", "value": "ERROR", "tableCls": "danger"}]	\N	1	1	2024-04-17 14:08:54.616+08	2024-11-20 14:03:25.137+08
1905175933357891585	pay_type	支付方式	[{"label": "钱包支付", "value": "WALLET", "tableCls": "info"}, {"label": "微信支付", "value": "WECHAT", "tableCls": "success"}, {"label": "支付宝", "value": "ZFB", "tableCls": "primary"}]	\N	1	1	2024-04-17 14:08:54.616+08	2024-11-20 14:27:24.525+08
1905175933425000449	protocol_type	协议类型	[{"label": "mqtt", "value": "MQTT", "tableCls": "primary"}, {"label": "http", "value": "HTTP", "tableCls": "primary"}]	\N	1	1	2024-04-17 14:08:54.616+08	2024-11-20 14:10:04.677+08
1905175933425000450	protocol_language	协议语言	[{"label": "java", "value": "java", "tableCls": "primary"}, {"label": "lua", "value": "lua", "tableCls": "primary"}]	\N	1	1	2024-11-20 14:12:01.999+08	2024-11-20 14:12:01.999+08
1905175933425000451	wallet_record_type	钱包记录类型	[{"label": "收入", "value": "0", "tableCls": "primary"}, {"label": "支出", "value": "1", "tableCls": "danger"}]	\N	1	1	2024-11-25 16:32:20.36+08	2024-11-25 16:32:20.36+08
1905175933425000452	command_type	设备指令类型	[{"label": "属性上报", "value": "1", "tableCls": "primary"}, {"label": "功能调用", "value": "2", "tableCls": "danger"}, {"label": "事件上报", "value": "3", "tableCls": "primary"}, {"label": "设备升级", "value": "4", "tableCls": "warning"}, {"label": "设备上线", "value": "5", "tableCls": "success"}, {"label": "设备离线", "value": "6", "tableCls": "info"}]	\N	1	1	2024-12-20 08:29:45.314+08	2024-12-21 16:52:53.275+08
1905175933492109314	product_type	产品类型	[{"label": "直连产品", "value": "0", "tableCls": "default"}, {"label": "网关产品", "value": "1", "tableCls": "default"}, {"label": "网关子产品", "value": "2", "tableCls": "default"}]	\N	1	1	2024-12-21 16:50:09.279+08	2024-12-21 16:50:09.279+08
1905175933492109315	things_model_type	物模型类型	[{"label": "属性", "value": "1", "tableCls": "default"}, {"label": "服务", "value": "2", "tableCls": "default"}, {"label": "事件", "value": "3", "tableCls": "default"}]	\N	1	1	2024-12-24 09:45:09.344+08	2024-12-24 09:45:09.344+08
1905175933492109316	things_model_datatype	物模型数据类型	[{"label": "整形", "value": "int32", "tableCls": "default"}, {"label": "小数", "value": "decimal", "tableCls": "default"}, {"label": "布尔", "value": "boolean", "tableCls": "default"}, {"label": "枚举", "value": "enum", "tableCls": "default"}, {"label": "字符串", "value": "string", "tableCls": "default"}, {"label": "数组", "value": "array", "tableCls": "default"}, {"label": "结构体", "value": "struct", "tableCls": "default"}]	\N	1	1	2024-12-24 10:27:20.157+08	2024-12-24 10:27:20.157+08
\.


--
-- TOC entry 3358 (class 0 OID 34682)
-- Dependencies: 216
-- Data for Name: sys_login_log; Type: TABLE DATA; Schema: biz_sys; Owner: wz
--

COPY biz_sys.sys_login_log (log_id, tenant_id, user_id, username, grant_type, status, error_msg, login_ip, login_location, browser, os, login_time) FROM stdin;
1905419785788334082	0	1	admin	password	0		192.168.0.112	内网IP	MSEdge	Windows 10 or Windows Server 2016	2025-03-28 08:40:52.521+08
1906137247701991425	0	1	admin	password	0		192.168.0.112	内网IP	MSEdge	Windows 10 or Windows Server 2016	2025-03-30 08:11:48.76+08
1906139358355456002	0	1	admin	password	0		::1	内网IP	MSEdge	Windows 10 or Windows Server 2016	2025-03-30 08:20:12.045+08
1906268889586270209	1904406772188934146	1904406772188934144	dsadasd	password	1	商户已过期，请联系管理员	192.168.0.112	内网IP	MSEdge	Windows 10 or Windows Server 2016	2025-03-30 16:54:54.646+08
1906504701292830722	0	1	admin	password	0		192.168.0.112	内网IP	MSEdge	Windows 10 or Windows Server 2016	2025-03-31 08:31:56.529+08
1906505000006967298	0	1	admin	password	0		192.168.0.112	内网IP	MSEdge	Windows 10 or Windows Server 2016	2025-03-31 08:33:07.808+08
1906505350080356353	0	1	admin	password	0		192.168.0.112	内网IP	MSEdge	Windows 10 or Windows Server 2016	2025-03-31 08:34:31.255+08
1906505513377193985	0	1	admin	password	0		192.168.0.112	内网IP	MSEdge	Windows 10 or Windows Server 2016	2025-03-31 08:35:10.187+08
1906506336547102721	0	1	admin	password	0		192.168.0.112	内网IP	MSEdge	Windows 10 or Windows Server 2016	2025-03-31 08:38:26.464+08
1906524887773339650	0	1	admin	password	0		192.168.0.112	内网IP	MSEdge	Windows 10 or Windows Server 2016	2025-03-31 09:52:09.422+08
1906903940460171266	0	1	admin	password	0		192.168.0.112	内网IP	MSEdge	Windows 10 or Windows Server 2016	2025-04-01 10:58:22.575+08
1907269772399026177	0	1	admin	password	0		192.168.0.112	内网IP	MSEdge	Windows 10 or Windows Server 2016	2025-04-02 11:12:03.505+08
1907656288048447490	0	1	admin	password	0		192.168.0.112	内网IP	MSEdge	Windows 10 or Windows Server 2016	2025-04-03 12:47:55.319+08
1909040075093245954	0	1	admin	password	0		192.168.0.112	内网IP	MSEdge	Windows 10 or Windows Server 2016	2025-04-07 08:26:36.731+08
1909118848824180738	0	1	admin	password	0		192.168.0.112	内网IP	MSEdge	Windows 10 or Windows Server 2016	2025-04-07 13:39:37.932+08
1909126434940190721	0	1	admin	password	0		192.168.0.112	内网IP	MSEdge	Windows 10 or Windows Server 2016	2025-04-07 14:09:46.585+08
1909149819464253441	0	1	admin	password	0		192.168.0.112	内网IP	MSEdge	Windows 10 or Windows Server 2016	2025-04-07 15:42:41.918+08
1909151512872550402	0	1	admin	password	0		192.168.0.112	内网IP	MSEdge	Windows 10 or Windows Server 2016	2025-04-07 15:49:25.646+08
1909153597231923201	0	1	admin	password	0		192.168.0.112	内网IP	MSEdge	Windows 10 or Windows Server 2016	2025-04-07 15:57:42.579+08
1909464816253870082	0	1909109150746140674	111111	password	1	登录失败，用户名或密码错误	192.168.0.112	内网IP	MSEdge	Windows 10 or Windows Server 2016	2025-04-08 12:34:22.608+08
1909464934751346689	0	1909109150746140674	111111	password	0		192.168.0.112	内网IP	MSEdge	Windows 10 or Windows Server 2016	2025-04-08 12:34:51.241+08
1909465068532867073	1907662334942928897	1907662334930345984	222222	password	1	商户已过期，请联系管理员	192.168.0.112	内网IP	MSEdge	Windows 10 or Windows Server 2016	2025-04-08 12:35:23.14+08
1909466494550089729	0	1	admin	password	0		192.168.0.112	内网IP	MSEdge	Windows 10 or Windows Server 2016	2025-04-08 12:41:03.129+08
1909841145260380162	0	1	admin	password	0		192.168.0.112	内网IP	MSEdge	Windows 10 or Windows Server 2016	2025-04-09 13:29:45.28+08
1909841860405989377	0	1	admin	password	0		192.168.0.112	内网IP	MSEdge	Windows 10 or Windows Server 2016	2025-04-09 13:32:37.306+08
1910208393242132481	0	1	admin	password	0		192.168.0.112	内网IP	MSEdge	Windows 10 or Windows Server 2016	2025-04-10 13:49:05.529+08
1910531116979941378	0	1	admin	password	0		192.168.0.112	内网IP	MSEdge	Windows 10 or Windows Server 2016	2025-04-11 11:11:28.663+08
1910531593675173889	0	1	admin	password	0		192.168.0.112	内网IP	MSEdge	Windows 10 or Windows Server 2016	2025-04-11 11:12:14.692+08
1906233858620850177	0	1	admin	password	0		192.168.0.112	内网IP	MSEdge	Windows 10 or Windows Server 2016	2025-03-30 14:35:42.665+08
1906269425115004930	1904406772188934146	1904406772188934144	dsadasd	password	1	商户已过期，请联系管理员	192.168.0.112	内网IP	MSEdge	Windows 10 or Windows Server 2016	2025-03-30 16:57:02.377+08
1906269566404329474	1904406772188934146	1904406772188934144	dsadasd	password	0		192.168.0.112	内网IP	MSEdge	Windows 10 or Windows Server 2016	2025-03-30 16:57:36.067+08
1906270400479428610	0	1	admin	password	0		192.168.0.112	内网IP	MSEdge	Windows 10 or Windows Server 2016	2025-03-30 17:00:54.924+08
1906510724325957634	0	1	admin	password	0		192.168.0.112	内网IP	MSEdge	Windows 10 or Windows Server 2016	2025-03-31 08:55:52.592+08
1906511033576185857	0	1	admin	password	0		192.168.0.112	内网IP	MSEdge	Windows 10 or Windows Server 2016	2025-03-31 08:57:06.306+08
1906511631633604610	0	1	admin	password	0		192.168.0.112	内网IP	MSEdge	Windows 10 or Windows Server 2016	2025-03-31 08:59:28.896+08
1906538592372252673	0	1	admin	password	0		192.168.0.112	内网IP	MSEdge	Windows 10 or Windows Server 2016	2025-03-31 10:46:36.849+08
1907661878871130113	0	1	admin	password	0		192.168.0.112	内网IP	MSEdge	Windows 10 or Windows Server 2016	2025-04-03 13:10:09.218+08
1907662601239330817	1907662334942928897	1907662334930345984	222222	password	0		192.168.0.112	内网IP	MSEdge	Windows 10 or Windows Server 2016	2025-04-03 13:13:01.449+08
1907664370119933953	0	1	admin	password	0		192.168.0.112	内网IP	MSEdge	Windows 10 or Windows Server 2016	2025-04-03 13:20:03.183+08
1909080937533734914	1907662334942928897	1907662334930345984	222222	password	1	登录失败，用户名或密码错误	192.168.0.112	内网IP	MSEdge	Windows 10 or Windows Server 2016	2025-04-07 11:08:59.173+08
1909081111664459777	1907662334942928897	1907662334930345984	222222	password	1	商户已过期，请联系管理员	192.168.0.112	内网IP	MSEdge	Windows 10 or Windows Server 2016	2025-04-07 11:09:40.689+08
1909081201070243842	1907662334942928897	1907662334930345984	222222	password	0		192.168.0.112	内网IP	MSEdge	Windows 10 or Windows Server 2016	2025-04-07 11:10:02.005+08
1909119155725598722	0	1	admin	password	0		192.168.0.112	内网IP	MSEdge	Windows 10 or Windows Server 2016	2025-04-07 13:40:51.098+08
1909128462152171522	0	1	admin	password	0		192.168.0.112	内网IP	MSEdge	Windows 10 or Windows Server 2016	2025-04-07 14:17:49.93+08
1909128882102665218	0	1	admin	password	0		192.168.0.112	内网IP	MSEdge	Windows 10 or Windows Server 2016	2025-04-07 14:19:30.049+08
1909471144326172673	1907662334942928897	1907662334930345984	222222	password	0		192.168.0.112	内网IP	MSEdge	Windows 10 or Windows Server 2016	2025-04-08 12:59:31.697+08
1909472030054125570	0	1	admin	password	0		192.168.0.112	内网IP	MSEdge	Windows 10 or Windows Server 2016	2025-04-08 13:03:02.898+08
1910555514755543041	0	1	admin	password	0		192.168.0.112	内网IP	MSEdge	Windows 10 or Windows Server 2016	2025-04-11 12:48:25.528+08
1906264141852246017	0	1	admin	password	0		192.168.0.112	内网IP	MSEdge	Windows 10 or Windows Server 2016	2025-03-30 16:36:02.742+08
1906515398923902978	0	1	admin	password	0		192.168.0.112	内网IP	MSEdge	Windows 10 or Windows Server 2016	2025-03-31 09:14:27.102+08
1906515553609834497	0	1	admin	password	0		192.168.0.112	内网IP	MSEdge	Windows 10 or Windows Server 2016	2025-03-31 09:15:03.968+08
1906516597614043137	0	1	admin	password	0		192.168.0.112	内网IP	MSEdge	Windows 10 or Windows Server 2016	2025-03-31 09:19:12.876+08
1906517898842333186	0	1	admin	password	0		192.168.0.112	内网IP	MSEdge	Windows 10 or Windows Server 2016	2025-03-31 09:24:23.131+08
1906518730128220161	0	1	admin	password	0		192.168.0.112	内网IP	MSEdge	Windows 10 or Windows Server 2016	2025-03-31 09:27:41.324+08
1906518861896474625	0	1	admin	password	0		192.168.0.112	内网IP	MSEdge	Windows 10 or Windows Server 2016	2025-03-31 09:28:12.74+08
1906520235350679553	0	1	admin	password	0		192.168.0.112	内网IP	MSEdge	Windows 10 or Windows Server 2016	2025-03-31 09:33:40.206+08
1906521526529417218	0	1	admin	password	0		192.168.0.112	内网IP	MSEdge	Windows 10 or Windows Server 2016	2025-03-31 09:38:48.032+08
1906521699938721794	0	1	admin	password	0		192.168.0.112	内网IP	MSEdge	Windows 10 or Windows Server 2016	2025-03-31 09:39:29.38+08
1909109316551114753	0	1909109150746140674	111111	password	0		192.168.0.112	内网IP	MSEdge	Windows 10 or Windows Server 2016	2025-04-07 13:01:45.259+08
1909119735793647618	0	1	admin	password	0		192.168.0.112	内网IP	MSEdge	Windows 10 or Windows Server 2016	2025-04-07 13:43:09.391+08
1909123633837178881	0	1	admin	password	0		192.168.0.112	内网IP	MSEdge	Windows 10 or Windows Server 2016	2025-04-07 13:58:38.759+08
1909136724083802114	0	1	admin	password	0		192.168.0.112	内网IP	MSEdge	Windows 10 or Windows Server 2016	2025-04-07 14:50:39.568+08
1909138584945491969	0	1	admin	password	0		192.168.0.112	内网IP	MSEdge	Windows 10 or Windows Server 2016	2025-04-07 14:58:03.387+08
1910557547508199426	0	1	admin	password	0		192.168.0.112	内网IP	MSEdge	Windows 10 or Windows Server 2016	2025-04-11 12:56:30.409+08
1906268824285151233	1904406772188934146	1904406772188934144	dsadasd	password	1	商户已过期，请联系管理员	192.168.0.112	内网IP	MSEdge	Windows 10 or Windows Server 2016	2025-03-30 16:54:39.127+08
1906521965702406145	0	1	admin	password	0		192.168.0.112	内网IP	MSEdge	Windows 10 or Windows Server 2016	2025-03-31 09:40:32.748+08
1909113381196595202	0	1	admin	password	0		192.168.0.112	内网IP	MSEdge	Windows 10 or Windows Server 2016	2025-04-07 13:17:54.345+08
1909113615360393217	0	1	admin	password	0		192.168.0.112	内网IP	MSEdge	Windows 10 or Windows Server 2016	2025-04-07 13:18:50.174+08
1909113764987994114	0	1	admin	password	0		192.168.0.112	内网IP	MSEdge	Windows 10 or Windows Server 2016	2025-04-07 13:19:25.847+08
1909114256099049473	0	1	admin	password	0		192.168.0.112	内网IP	MSEdge	Windows 10 or Windows Server 2016	2025-04-07 13:21:22.942+08
1909115051909513217	0	1	admin	password	0		192.168.0.112	内网IP	MSEdge	Windows 10 or Windows Server 2016	2025-04-07 13:24:32.678+08
1909117115272200194	0	1	admin	password	0		192.168.0.112	内网IP	MSEdge	Windows 10 or Windows Server 2016	2025-04-07 13:32:44.624+08
1909117784112693249	0	1	admin	password	0		192.168.0.112	内网IP	MSEdge	Windows 10 or Windows Server 2016	2025-04-07 13:35:24.079+08
1909118199873077250	0	1	admin	password	0		192.168.0.112	内网IP	MSEdge	Windows 10 or Windows Server 2016	2025-04-07 13:37:03.212+08
1909123816788525058	1907662334942928897	1907662334930345984	222222	password	1	登录失败，用户名或密码错误	192.168.0.112	内网IP	MSEdge	Windows 10 or Windows Server 2016	2025-04-07 13:59:22.388+08
1909123862690988034	0	1909109150746140674	111111	password	0		192.168.0.112	内网IP	MSEdge	Windows 10 or Windows Server 2016	2025-04-07 13:59:33.325+08
1909124013316833282	0	1909109150746140674	111111	password	0		192.168.0.112	内网IP	MSEdge	Windows 10 or Windows Server 2016	2025-04-07 14:00:09.24+08
1909124702407426049	0	1909109150746140674	111111	password	0		192.168.0.112	内网IP	MSEdge	Windows 10 or Windows Server 2016	2025-04-07 14:02:53.538+08
1910574564126683138	1910557183820165122	1910557183820165120	testadmin	password	0		192.168.0.112	内网IP	MSEdge	Windows 10 or Windows Server 2016	2025-04-11 14:04:07.486+08
1910575733649633282	0	1	admin	password	0		192.168.0.112	内网IP	MSEdge	Windows 10 or Windows Server 2016	2025-04-11 14:08:46.348+08
\.


--
-- TOC entry 3359 (class 0 OID 34685)
-- Dependencies: 217
-- Data for Name: sys_message; Type: TABLE DATA; Schema: biz_sys; Owner: wz
--

COPY biz_sys.sys_message (msg_id, title, content, status, creator_id, updater_id, create_at, update_at) FROM stdin;
1867761637258874881	测试标题	测试内容xxxx	2	1	1	2024-12-14 10:40:50.316+08	2024-12-14 10:40:50.316+08
1867761817215488002	测试标题5	内容aauuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuqqqq	2	1	1	2024-12-14 10:41:33.219+08	2024-12-14 10:41:33.219+08
1867761817215488005	测试通知7	测测测测测测测测测测测测测测测测测测测测测测测测测测测测测测测测测测测测测测测测测测测测测测测测测测测测测测测测测测测测测测测测测测测测测测测测测测测测测测测测测测测测	2	1	1	2024-12-14 10:41:34.219+08	2025-04-02 09:47:56.302+08
\.


--
-- TOC entry 3360 (class 0 OID 34690)
-- Dependencies: 218
-- Data for Name: sys_notice; Type: TABLE DATA; Schema: biz_sys; Owner: wz
--

COPY biz_sys.sys_notice (notice_id, notice_type, title, content, create_at, creator_id) FROM stdin;
1910557184449245186	0	租户创建成功	租户：test创建成功，超级管理员账号：testadmin，临时登录密码：74181699，临时操作密码：431865	2025-04-11 12:55:03.849+08	1
\.


--
-- TOC entry 3361 (class 0 OID 34693)
-- Dependencies: 219
-- Data for Name: sys_notice_user; Type: TABLE DATA; Schema: biz_sys; Owner: wz
--

COPY biz_sys.sys_notice_user (notice_id, user_id, read_state) FROM stdin;
1910557184449245186	1	0
\.


--
-- TOC entry 3362 (class 0 OID 34696)
-- Dependencies: 220
-- Data for Name: sys_oper_log; Type: TABLE DATA; Schema: biz_sys; Owner: wz
--

COPY biz_sys.sys_oper_log (oper_id, tenant_id, title, sub_title, oper_type, method, request_method, user_id, oper_name, oper_url, oper_ip, oper_location, oper_param, json_result, status, error_msg, oper_time) FROM stdin;
1910575665987121153	1910557183820165122	个人信息	修改密码	2	com.wzkris.user.controller.SysUserProfileController.editPwd()	POST	1910557183820165120	testadmin	/user_profile/edit_password	192.168.0.112	内网IP	{"oldPassword":"**","newPassword":"**"}	{"code":0,"message":"Success","timestamp":1744351710074}	0	\N	2025-04-11 14:08:30.075+08
1905449595973541889	0	菜单管理	修改菜单	2	com.wzkris.user.controller.SysMenuController.edit()	POST	1	admin	/sys_menu/edit	192.168.0.112	内网IP	{"icon":"carbon:parameter","menuName":"参数管理","isVisible":true,"parentId":1,"path":"config","isCache":false,"component":"system/config/index","isFrame":false,"menuId":103,"menuType":"M","perms":"sys_config:list","menuSort":7,"status":"0"}	{"code":0,"message":"Success","timestamp":1743129559875}	0	\N	2025-03-28 10:39:19.876+08
1905449974975045634	0	菜单管理	修改菜单	2	com.wzkris.user.controller.SysMenuController.edit()	POST	1	admin	/sys_menu/edit	192.168.0.112	内网IP	{"path":"log","isCache":false,"isFrame":false,"icon":"carbon:ibm-knowledge-catalog-premium","menuId":104,"menuName":"日志管理","menuType":"D","isVisible":true,"parentId":1,"menuSort":1,"status":"0"}	\N	1	\r\n### Error updating database.  Cause: org.postgresql.util.PSQLException: 错误: 对于可变字符类型来说，值太长了(30)\r\n### The error may exist in com/wzkris/user/mapper/SysMenuMapper.java (best guess)\r\n### The error may involve com.wzkris.user.mapper.SysMenuMapper.updateById-Inline\r\n### The error occurred while setting parameters\r\n### SQL: UPDATE biz_sys.sys_menu SET menu_name = ?, parent_id = ?, menu_sort = ?, path = ?, is_frame = ?, is_cache = ?, is_visible = ?, menu_type = ?, status = ?, icon = ?, update_at = ?, updater_id = ? WHERE menu_id = ?\r\n### Cause: org.postgresql.util.PSQLException: 错误: 对于可变字符类型来说，值太长了(30)\n; 错误: 对于可变字符类型来说，值太长了(30)	2025-03-28 10:40:50.237+08
1905450163286712321	0	菜单管理	修改菜单	2	com.wzkris.user.controller.SysMenuController.edit()	POST	1	admin	/sys_menu/edit	192.168.0.112	内网IP	{"path":"log","isCache":false,"isFrame":false,"icon":"carbon:ibm-knowledge-catalog-premium","menuId":104,"menuName":"日志管理","menuType":"D","isVisible":true,"parentId":1,"menuSort":1,"status":"0"}	{"code":0,"message":"Success","timestamp":1743129695139}	0	\N	2025-03-28 10:41:35.139+08
1905450256605782017	0	菜单管理	修改菜单	2	com.wzkris.user.controller.SysMenuController.edit()	POST	1	admin	/sys_menu/edit	192.168.0.112	内网IP	{"path":"controller","isCache":false,"isFrame":false,"icon":"carbon:dashboard","menuId":101,"menuName":"控制台入口","menuType":"D","isVisible":true,"parentId":1,"menuSort":0,"status":"0"}	{"code":0,"message":"Success","timestamp":1743129717385}	0	\N	2025-03-28 10:41:57.385+08
1905453027543359489	0	菜单管理	修改菜单	2	com.wzkris.user.controller.SysMenuController.edit()	POST	1	admin	/sys_menu/edit	192.168.0.112	内网IP	{"path":"system","isCache":false,"isFrame":false,"icon":"carbon:tool-kit","menuId":1,"menuName":"系统管理","menuType":"D","isVisible":true,"parentId":0,"menuSort":100,"status":"0"}	{"code":0,"message":"Success","timestamp":1743130378029}	0	\N	2025-03-28 10:52:58.029+08
1905453300517052417	0	菜单管理	修改菜单	2	com.wzkris.user.controller.SysMenuController.edit()	POST	1	admin	/sys_menu/edit	192.168.0.112	内网IP	{"icon":"carbon:text-vertical-alignment","menuName":"字典管理","isVisible":true,"parentId":1,"path":"dict","isCache":false,"component":"system/dict/index","isFrame":false,"menuId":102,"menuType":"M","perms":"sys_dict:list","menuSort":6,"status":"0"}	{"code":0,"message":"Success","timestamp":1743130443111}	0	\N	2025-03-28 10:54:03.111+08
1905453455630802945	0	菜单管理	修改菜单	2	com.wzkris.user.controller.SysMenuController.edit()	POST	1	admin	/sys_menu/edit	192.168.0.112	内网IP	{"icon":"carbon:login","menuName":"登录日志","isVisible":true,"parentId":104,"path":"loginlog","isCache":false,"component":"system/loginlog/index","isFrame":false,"menuId":151,"menuType":"M","perms":"loginlog:list","menuSort":2,"status":"0"}	{"code":0,"message":"Success","timestamp":1743130480092}	0	\N	2025-03-28 10:54:40.092+08
1905453651915841537	0	菜单管理	修改菜单	2	com.wzkris.user.controller.SysMenuController.edit()	POST	1	admin	/sys_menu/edit	192.168.0.112	内网IP	{"icon":"carbon:touch-interaction","menuName":"操作日志","isVisible":true,"parentId":104,"path":"operlog","isCache":false,"component":"system/operlog/index","isFrame":false,"menuId":150,"menuType":"M","perms":"operlog:list","menuSort":1,"status":"0"}	{"code":0,"message":"Success","timestamp":1743130526891}	0	\N	2025-03-28 10:55:26.891+08
1905453775782027266	0	菜单管理	修改菜单	2	com.wzkris.user.controller.SysMenuController.edit()	POST	1	admin	/sys_menu/edit	192.168.0.112	内网IP	{"path":"http://localhost:9200/xxl-job-admin","isCache":false,"isFrame":true,"icon":"carbon:job-run","menuId":300,"menuName":"定时任务","menuType":"M","perms":"job:list","isVisible":true,"parentId":101,"menuSort":20,"status":"0"}	{"code":0,"message":"Success","timestamp":1743130556423}	0	\N	2025-03-28 10:55:56.423+08
1906172064653049858	0	部门管理	删除部门	3	com.wzkris.user.controller.SysDeptController.remove()	POST	1	admin	/sys_dept/remove	192.168.0.112	内网IP	1904075235413127170	{"code":0,"message":"Success","timestamp":1743301809834}	0	\N	2025-03-30 10:30:09.834+08
1906172079328919554	0	部门管理	删除部门	3	com.wzkris.user.controller.SysDeptController.remove()	POST	1	admin	/sys_dept/remove	192.168.0.112	内网IP	1904075222259789826	{"code":0,"message":"Success","timestamp":1743301813336}	0	\N	2025-03-30 10:30:13.336+08
1906172086274686978	0	部门管理	删除部门	3	com.wzkris.user.controller.SysDeptController.remove()	POST	1	admin	/sys_dept/remove	192.168.0.112	内网IP	1904075204320751617	{"code":0,"message":"Success","timestamp":1743301814989}	0	\N	2025-03-30 10:30:14.989+08
1906269641788555266	1904406772188934146	角色管理	新增角色	1	com.wzkris.user.controller.SysRoleController.add()	POST	1904406772188934144	dsadasd	/sys_role/add	192.168.0.112	内网IP	{"roleName":"asdasd ","deptIds":[],"dataScope":"1","menuIds":[1906263415450000203,1906263415450002078,1906263415450002062,1906263415450002064,1906263415450002077,1906263415450002072,1906263415450002071],"status":"0","roleSort":0}	{"code":0,"message":"Success","timestamp":1743325074042}	0	\N	2025-03-30 16:57:54.042+08
1906269690861912065	1904406772188934146	部门管理	新增部门	1	com.wzkris.user.controller.SysDeptController.add()	POST	1904406772188934144	dsadasd	/sys_dept/add	192.168.0.112	内网IP	{"deptSort":0,"deptName":"asdad","status":"0"}	{"code":0,"message":"Success","timestamp":1743325085742}	0	\N	2025-03-30 16:58:05.742+08
1906269712705847298	1904406772188934146	角色管理	修改角色	2	com.wzkris.user.controller.SysRoleController.edit()	POST	1904406772188934144	dsadasd	/sys_role/edit	192.168.0.112	内网IP	{"isMenuDisplay":false,"roleId":1906269641721450498,"roleName":"asdasd ","isDeptDisplay":false,"deptIds":[1906269690861916162],"dataScope":"2","menuIds":[1906263415450000203,1906263415450002078,1906263415450002062,1906263415450002064,1906263415450002077,1906263415450002072,1906263415450002071],"status":"0","roleSort":0}	{"code":0,"message":"Success","timestamp":1743325090950}	0	\N	2025-03-30 16:58:10.95+08
1906269723090944002	1904406772188934146	角色管理	删除角色	3	com.wzkris.user.controller.SysRoleController.remove()	POST	1904406772188934144	dsadasd	/sys_role/remove	192.168.0.112	内网IP	[1906269641721450498]	{"code":0,"message":"Success","timestamp":1743325093430}	0	\N	2025-03-30 16:58:13.43+08
1905453828353433601	0	菜单管理	修改菜单	2	com.wzkris.user.controller.SysMenuController.edit()	POST	1	admin	/sys_menu/edit	192.168.0.112	内网IP	{"path":"http://localhost:9100/","isCache":false,"isFrame":true,"icon":"carbon:cloud-monitoring","menuId":304,"menuName":"服务监控","menuType":"M","perms":"monitor:server:list","isVisible":true,"parentId":101,"menuSort":5,"status":"0"}	{"code":0,"message":"Success","timestamp":1743130568956}	0	\N	2025-03-28 10:56:08.956+08
1905454507109261314	0	菜单管理	修改菜单	2	com.wzkris.user.controller.SysMenuController.edit()	POST	1	admin	/sys_menu/edit	192.168.0.112	内网IP	{"path":"http://127.0.0.1:8848/nacos","isCache":false,"isFrame":true,"icon":"carbon:link","menuId":303,"menuName":"Nacos控制台","menuType":"M","perms":"monitor:nacos:list","isVisible":true,"parentId":101,"menuSort":4,"status":"0"}	{"code":0,"message":"Success","timestamp":1743130730767}	0	\N	2025-03-28 10:58:50.767+08
1905454540957294594	0	菜单管理	修改菜单	2	com.wzkris.user.controller.SysMenuController.edit()	POST	1	admin	/sys_menu/edit	192.168.0.112	内网IP	{"path":"http://localhost:8718","isCache":false,"isFrame":true,"icon":"carbon:link","menuId":302,"menuName":"Sentinel控制台","menuType":"M","perms":"monitor:sentinel:list","isVisible":true,"parentId":101,"menuSort":3,"status":"0"}	{"code":0,"message":"Success","timestamp":1743130738854}	0	\N	2025-03-28 10:58:58.854+08
1905454570875265026	0	菜单管理	修改菜单	2	com.wzkris.user.controller.SysMenuController.edit()	POST	1	admin	/sys_menu/edit	192.168.0.112	内网IP	{"path":"http://localhost:8080/doc.html","isCache":false,"isFrame":false,"icon":"carbon:link","menuId":301,"menuName":"系统接口","menuType":"M","perms":"tool:swagger:list","isVisible":true,"parentId":101,"menuSort":2,"status":"0"}	{"code":0,"message":"Success","timestamp":1743130745987}	0	\N	2025-03-28 10:59:05.987+08
1905454888493129730	0	菜单管理	修改菜单	2	com.wzkris.user.controller.SysMenuController.edit()	POST	1	admin	/sys_menu/edit	192.168.0.112	内网IP	{"path":"user","isCache":false,"isFrame":false,"icon":"carbon:user","menuId":2,"menuName":"用户权限管理","menuType":"D","isVisible":true,"parentId":0,"menuSort":99,"status":"0"}	{"code":0,"message":"Success","timestamp":1743130821714}	0	\N	2025-03-28 11:00:21.714+08
1905454953798443010	0	菜单管理	修改菜单	2	com.wzkris.user.controller.SysMenuController.edit()	POST	1	admin	/sys_menu/edit	192.168.0.112	内网IP	{"icon":"carbon:user-admin","menuName":"系统用户","isVisible":true,"parentId":2,"path":"sysuser","isCache":true,"component":"user/sysuser/index","isFrame":false,"menuId":203,"menuType":"M","perms":"sys_user:list","menuSort":100,"status":"0"}	{"code":0,"message":"Success","timestamp":1743130837283}	0	\N	2025-03-28 11:00:37.283+08
1905454987965243393	0	菜单管理	修改菜单	2	com.wzkris.user.controller.SysMenuController.edit()	POST	1	admin	/sys_menu/edit	192.168.0.112	内网IP	{"icon":"carbon:user-role","menuName":"角色管理","isVisible":true,"parentId":2,"path":"sysrole","isCache":false,"component":"user/sysrole/index","isFrame":false,"menuId":206,"menuType":"M","perms":"sys_role:list","menuSort":99,"status":"0"}	{"code":0,"message":"Success","timestamp":1743130845429}	0	\N	2025-03-28 11:00:45.429+08
1905455056122683393	0	菜单管理	修改菜单	2	com.wzkris.user.controller.SysMenuController.edit()	POST	1	admin	/sys_menu/edit	192.168.0.112	内网IP	{"icon":"carbon:customer","menuName":"顾客管理","isVisible":true,"parentId":2,"path":"appuser","isCache":false,"component":"user/appuser/index","isFrame":false,"menuId":201,"menuType":"M","perms":"app_user:list","menuSort":1,"status":"0"}	{"code":0,"message":"Success","timestamp":1743130861679}	0	\N	2025-03-28 11:01:01.679+08
1905455465054740481	0	菜单管理	修改菜单	2	com.wzkris.user.controller.SysMenuController.edit()	POST	1	admin	/sys_menu/edit	192.168.0.112	内网IP	{"icon":"carbon:menu","menuName":"菜单管理","isVisible":true,"parentId":2,"path":"sysmenu","isCache":false,"component":"user/sysmenu/index","isFrame":false,"menuId":207,"menuType":"M","perms":"sys_menu:list","menuSort":50,"status":"0"}	{"code":0,"message":"Success","timestamp":1743130959176}	0	\N	2025-03-28 11:02:39.176+08
1905455738095542274	0	菜单管理	修改菜单	2	com.wzkris.user.controller.SysMenuController.edit()	POST	1	admin	/sys_menu/edit	192.168.0.112	内网IP	{"icon":"carbon:departure","menuName":"部门管理","isVisible":true,"parentId":2,"path":"sysdept","isCache":false,"component":"user/sysdept/index","isFrame":false,"menuId":205,"menuType":"M","perms":"sys_dept:list","menuSort":70,"status":"0"}	{"code":0,"message":"Success","timestamp":1743131024275}	0	\N	2025-03-28 11:03:44.275+08
1905456097320902657	0	菜单管理	修改菜单	2	com.wzkris.user.controller.SysMenuController.edit()	POST	1	admin	/sys_menu/edit	192.168.0.112	内网IP	{"icon":"carbon:user-service-desk","menuName":"岗位管理","isVisible":true,"parentId":2,"path":"syspost","isCache":false,"component":"user/syspost/index","isFrame":false,"menuId":208,"menuType":"M","perms":"sys_post:list","menuSort":8,"status":"0"}	{"code":0,"message":"Success","timestamp":1743131109921}	0	\N	2025-03-28 11:05:09.921+08
1905456184717615105	0	菜单管理	修改菜单	2	com.wzkris.user.controller.SysMenuController.edit()	POST	1	admin	/sys_menu/edit	192.168.0.112	内网IP	{"icon":"carbon:application","menuName":"客户端管理","isVisible":true,"parentId":2,"path":"client","isCache":false,"component":"user/client/index","isFrame":false,"menuId":700,"menuType":"M","perms":"oauth2_client:list","menuSort":3,"status":"0"}	{"code":0,"message":"Success","timestamp":1743131130757}	0	\N	2025-03-28 11:05:30.757+08
1905456475915558914	0	菜单管理	修改菜单	2	com.wzkris.user.controller.SysMenuController.edit()	POST	1	admin	/sys_menu/edit	192.168.0.112	内网IP	{"path":"tenant","isCache":false,"isFrame":false,"icon":"carbon:business-processes","menuId":3,"menuName":"商户管理","menuType":"D","isVisible":true,"parentId":0,"menuSort":50,"status":"0"}	{"code":0,"message":"Success","timestamp":1743131200184}	0	\N	2025-03-28 11:06:40.184+08
1905456561210925057	0	菜单管理	修改菜单	2	com.wzkris.user.controller.SysMenuController.edit()	POST	1	admin	/sys_menu/edit	192.168.0.112	内网IP	{"icon":"carbon:information","menuName":"商户信息","isVisible":true,"parentId":3,"path":"info","isCache":false,"component":"user/tenant/index","isFrame":false,"menuId":601,"menuType":"M","perms":"tenant:information","menuSort":100,"status":"0"}	{"code":0,"message":"Success","timestamp":1743131220520}	0	\N	2025-03-28 11:07:00.52+08
1906215140847190017	0	菜单管理	修改菜单	2	com.wzkris.user.controller.SysMenuController.edit()	POST	1	admin	/sys_menu/edit	192.168.0.112	内网IP	{"path":"tenant","isCache":false,"isFrame":false,"icon":"carbon:platforms","menuId":3,"menuName":"平台管理","menuType":"D","isVisible":true,"parentId":0,"menuSort":50,"status":"0"}	{"code":0,"message":"Success","timestamp":1743312079994}	0	\N	2025-03-30 13:21:19.994+08
1906215718755172354	0	菜单管理	新增菜单	1	com.wzkris.user.controller.SysMenuController.add()	POST	1	admin	/sys_menu/add	192.168.0.112	内网IP	{"isCache":false,"isFrame":false,"menuName":"asd","menuType":"D","isVisible":true,"parentId":0,"menuSort":0,"status":"0"}	{"code":0,"message":"Success","timestamp":1743312217782}	0	\N	2025-03-30 13:23:37.782+08
1906215728615981058	0	菜单管理	删除菜单	3	com.wzkris.user.controller.SysMenuController.remove()	POST	1	admin	/sys_menu/remove	192.168.0.112	内网IP	1906215718688059393	{"code":0,"message":"Success","timestamp":1743312220134}	0	\N	2025-03-30 13:23:40.134+08
1905456614252093441	0	菜单管理	修改菜单	2	com.wzkris.user.controller.SysMenuController.edit()	POST	1	admin	/sys_menu/edit	192.168.0.112	内网IP	{"icon":"carbon:package","menuName":"租户套餐管理","isVisible":true,"parentId":3,"path":"package","isCache":false,"component":"user/tenant/package/index","isFrame":false,"menuId":602,"menuType":"M","perms":"tenant_package:list","menuSort":50,"status":"0"}	{"code":0,"message":"Success","timestamp":1743131233166}	0	\N	2025-03-28 11:07:13.166+08
1906233540080238594	0	菜单管理	修改菜单	2	com.wzkris.user.controller.SysMenuController.edit()	POST	1	admin	/sys_menu/edit	192.168.0.112	内网IP	{"icon":"carbon:id-management","menuName":"商户管理","isVisible":true,"parentId":3,"path":"info","isCache":false,"component":"user/tenant/index","isFrame":false,"menuId":601,"menuType":"M","perms":"tenant:information","menuSort":100,"status":"0"}	{"code":0,"message":"Success","timestamp":1743316466703}	0	\N	2025-03-30 14:34:26.703+08
1906237700129357826	0	菜单管理	修改菜单	2	com.wzkris.user.controller.SysMenuController.edit()	POST	1	admin	/sys_menu/edit	192.168.0.112	内网IP	{"icon":"carbon:menu","menuName":"菜单管理","isVisible":true,"parentId":3,"path":"sysmenu","isCache":false,"component":"user/sysmenu/index","isFrame":false,"menuId":207,"menuType":"M","perms":"sys_menu:list","menuSort":50,"status":"0"}	{"code":0,"message":"Success","timestamp":1743317458551}	0	\N	2025-03-30 14:50:58.551+08
1906237879498768385	0	菜单管理	修改菜单	2	com.wzkris.user.controller.SysMenuController.edit()	POST	1	admin	/sys_menu/edit	192.168.0.112	内网IP	{"path":"platform","isCache":false,"isFrame":false,"icon":"carbon:platforms","menuId":3,"menuName":"平台管理","menuType":"D","isVisible":true,"parentId":0,"menuSort":50,"status":"0"}	{"code":0,"message":"Success","timestamp":1743317501316}	0	\N	2025-03-30 14:51:41.316+08
1906238163415400449	0	菜单管理	修改菜单	2	com.wzkris.user.controller.SysMenuController.edit()	POST	1	admin	/sys_menu/edit	192.168.0.112	内网IP	{"icon":"carbon:id-management","menuName":"租户管理","isVisible":true,"parentId":3,"path":"info","isCache":false,"component":"user/tenant/index","isFrame":false,"menuId":601,"menuType":"M","perms":"tenant:information","menuSort":100,"status":"0"}	{"code":0,"message":"Success","timestamp":1743317569013}	0	\N	2025-03-30 14:52:49.013+08
1906269736009400322	1904406772188934146	部门管理	删除部门	3	com.wzkris.user.controller.SysDeptController.remove()	POST	1904406772188934144	dsadasd	/sys_dept/remove	192.168.0.112	内网IP	1906269690861916162	{"code":0,"message":"Success","timestamp":1743325096503}	0	\N	2025-03-30 16:58:16.504+08
1910232749636763649	0	系统用户	导出用户数据	5	com.wzkris.user.controller.SysUserController.export()	GET	1	admin	/sys_user/export	192.168.0.112	内网IP		\N	0	\N	2025-04-10 15:25:52.575+08
1910237582682931202	0	系统用户	导出用户数据	5	com.wzkris.user.controller.SysUserController.export()	GET	1	admin	/sys_user/export	192.168.0.112	内网IP		\N	0	\N	2025-04-10 15:45:04.878+08
1910237725117300738	0	系统用户	导出用户数据	5	com.wzkris.user.controller.SysUserController.export()	GET	1	admin	/sys_user/export	192.168.0.112	内网IP		\N	0	\N	2025-04-10 15:45:38.835+08
1910237906135072769	0	系统用户	导出用户数据	5	com.wzkris.user.controller.SysUserController.export()	GET	1	admin	/sys_user/export	192.168.0.112	内网IP		\N	0	\N	2025-04-10 15:46:21.983+08
1910237927026900993	0	系统用户	导出用户数据	5	com.wzkris.user.controller.SysUserController.export()	GET	1	admin	/sys_user/export	192.168.0.112	内网IP		\N	0	\N	2025-04-10 15:46:26.962+08
1910238041019695105	0	系统用户	导出用户数据	5	com.wzkris.user.controller.SysUserController.export()	GET	1	admin	/sys_user/export	192.168.0.112	内网IP		\N	0	\N	2025-04-10 15:46:54.149+08
1910238109680451586	0	系统用户	导出用户数据	5	com.wzkris.user.controller.SysUserController.export()	GET	1	admin	/sys_user/export	192.168.0.112	内网IP		\N	0	\N	2025-04-10 15:47:10.518+08
1910238154781802498	0	系统用户	导出用户数据	5	com.wzkris.user.controller.SysUserController.export()	GET	1	admin	/sys_user/export	192.168.0.112	内网IP		\N	0	\N	2025-04-10 15:47:21.263+08
1910238279650426881	0	系统用户	导出用户数据	5	com.wzkris.user.controller.SysUserController.export()	GET	1	admin	/sys_user/export	192.168.0.112	内网IP		\N	0	\N	2025-04-10 15:47:51.036+08
1910238542515847170	0	系统用户	导出用户数据	5	com.wzkris.user.controller.SysUserController.export()	GET	1	admin	/sys_user/export	192.168.0.112	内网IP		\N	0	\N	2025-04-10 15:48:53.706+08
1910238762918133761	0	系统用户	导出用户数据	5	com.wzkris.user.controller.SysUserController.export()	GET	1	admin	/sys_user/export	192.168.0.112	内网IP		\N	0	\N	2025-04-10 15:49:46.259+08
1910238878928388097	0	系统用户	导出用户数据	5	com.wzkris.user.controller.SysUserController.export()	GET	1	admin	/sys_user/export	192.168.0.112	内网IP		\N	0	\N	2025-04-10 15:50:13.923+08
1910557184793178114	0	租户管理	新增租户	1	com.wzkris.user.controller.SysTenantController.add()	POST	1	admin	/sys_tenant/add	192.168.0.112	内网IP	{"tenantType":"0","expireTime":1745078400000,"accountLimit":5,"roleLimit":5,"tenantName":"test","packageId":1773625804122202113,"deptLimit":5,"postLimit":5,"status":"0","username":"testadmin"}	{"code":0,"message":"Success","timestamp":1744347303816}	0	\N	2025-04-11 12:55:03.816+08
1910566622220447746	0	角色管理	新增角色	1	com.wzkris.user.controller.SysRoleController.add()	POST	1	admin	/sys_role/add	192.168.0.112	内网IP	{"roleName":"aa","deptIds":[],"dataScope":"1","menuIds":[],"status":"0","roleSort":0}	{"code":0,"message":"Success","timestamp":1744349554005}	0	\N	2025-04-11 13:32:34.005+08
1910566638980886530	0	角色管理	删除角色	3	com.wzkris.user.controller.SysRoleController.remove()	POST	1	admin	/sys_role/remove	192.168.0.112	内网IP	[1910566622220513282]	{"code":0,"message":"Success","timestamp":1744349558002}	0	\N	2025-04-11 13:32:38.002+08
1910569625748959233	0	菜单管理	新增菜单	1	com.wzkris.user.controller.SysMenuController.add()	POST	1	admin	/sys_menu/add	192.168.0.112	内网IP	{"isCache":false,"menuName":"授权角色","menuType":"B","perms":"sys_user:grant_role","isVisible":true,"parentId":1906263415450000203,"menuSort":0,"status":"0"}	{"code":0,"message":"Success","timestamp":1744350270111}	0	\N	2025-04-11 13:44:30.111+08
1910572305682722817	0	菜单管理	修改菜单	2	com.wzkris.user.controller.SysMenuController.edit()	POST	1	admin	/sys_menu/edit	192.168.0.112	内网IP	{"path":"#","isCache":false,"icon":"#","menuId":1906263415450002207,"menuName":"权限授予","menuType":"B","perms":"sys_role:grant_user","isVisible":true,"parentId":1906263415450000206,"menuSort":6,"status":"0"}	{"code":0,"message":"Success","timestamp":1744350909045}	0	\N	2025-04-11 13:55:09.045+08
1910574078870876161	0	菜单管理	修改菜单	2	com.wzkris.user.controller.SysMenuController.edit()	POST	1	admin	/sys_menu/edit	192.168.0.112	内网IP	{"path":"#","isCache":false,"icon":"#","menuId":1906263415450001214,"menuName":"终端导出","menuType":"B","perms":"oauth2_client:export","isVisible":true,"parentId":1906263415450000700,"menuSort":4,"status":"0"}	{"code":0,"message":"Success","timestamp":1744351331688}	0	\N	2025-04-11 14:02:11.69+08
1910574106159017986	0	菜单管理	修改菜单	2	com.wzkris.user.controller.SysMenuController.edit()	POST	1	admin	/sys_menu/edit	192.168.0.112	内网IP	{"path":"#","isCache":false,"icon":"#","menuId":1906263415450001212,"menuName":"终端添加","menuType":"B","perms":"oauth2_client:add","isVisible":true,"parentId":1906263415450000700,"menuSort":3,"status":"0"}	{"code":0,"message":"Success","timestamp":1744351338318}	0	\N	2025-04-11 14:02:18.318+08
1910574136362201090	0	菜单管理	修改菜单	2	com.wzkris.user.controller.SysMenuController.edit()	POST	1	admin	/sys_menu/edit	192.168.0.112	内网IP	{"path":"#","isCache":false,"icon":"#","menuId":1906263415450001213,"menuName":"终端删除","menuType":"B","perms":"oauth2_client:remove","isVisible":true,"parentId":1906263415450000700,"menuSort":2,"status":"0"}	{"code":0,"message":"Success","timestamp":1744351345514}	0	\N	2025-04-11 14:02:25.514+08
1910574159628005378	0	菜单管理	修改菜单	2	com.wzkris.user.controller.SysMenuController.edit()	POST	1	admin	/sys_menu/edit	192.168.0.112	内网IP	{"path":"#","isCache":false,"icon":"#","menuId":1906263415450001211,"menuName":"终端修改","menuType":"B","perms":"oauth2_client:edit","isVisible":true,"parentId":1906263415450000700,"menuSort":2,"status":"0"}	{"code":0,"message":"Success","timestamp":1744351351068}	0	\N	2025-04-11 14:02:31.068+08
1910574189759885314	0	菜单管理	修改菜单	2	com.wzkris.user.controller.SysMenuController.edit()	POST	1	admin	/sys_menu/edit	192.168.0.112	内网IP	{"path":"#","isCache":false,"icon":"#","menuId":1906263415450001210,"menuName":"终端详情","menuType":"B","perms":"oauth2_client:query","isVisible":true,"parentId":1906263415450000700,"menuSort":1,"status":"0"}	{"code":0,"message":"Success","timestamp":1744351358258}	0	\N	2025-04-11 14:02:38.258+08
\.


--
-- TOC entry 3198 (class 2606 OID 34706)
-- Name: sys_config sys_config_pkey; Type: CONSTRAINT; Schema: biz_sys; Owner: wz
--

ALTER TABLE ONLY biz_sys.sys_config
    ADD CONSTRAINT sys_config_pkey PRIMARY KEY (config_id);


--
-- TOC entry 3213 (class 2606 OID 35424)
-- Name: sys_dict sys_dict_pkey; Type: CONSTRAINT; Schema: biz_sys; Owner: wz
--

ALTER TABLE ONLY biz_sys.sys_dict
    ADD CONSTRAINT sys_dict_pkey PRIMARY KEY (dict_id);


--
-- TOC entry 3202 (class 2606 OID 34708)
-- Name: sys_login_log sys_login_log_pkey; Type: CONSTRAINT; Schema: biz_sys; Owner: wz
--

ALTER TABLE ONLY biz_sys.sys_login_log
    ADD CONSTRAINT sys_login_log_pkey PRIMARY KEY (log_id);


--
-- TOC entry 3204 (class 2606 OID 34710)
-- Name: sys_message sys_message_pkey; Type: CONSTRAINT; Schema: biz_sys; Owner: wz
--

ALTER TABLE ONLY biz_sys.sys_message
    ADD CONSTRAINT sys_message_pkey PRIMARY KEY (msg_id);


--
-- TOC entry 3206 (class 2606 OID 35587)
-- Name: sys_notice sys_notify_pkey; Type: CONSTRAINT; Schema: biz_sys; Owner: wz
--

ALTER TABLE ONLY biz_sys.sys_notice
    ADD CONSTRAINT sys_notify_pkey PRIMARY KEY (notice_id);


--
-- TOC entry 3208 (class 2606 OID 35585)
-- Name: sys_notice_user sys_notify_send_pkey; Type: CONSTRAINT; Schema: biz_sys; Owner: wz
--

ALTER TABLE ONLY biz_sys.sys_notice_user
    ADD CONSTRAINT sys_notify_send_pkey PRIMARY KEY (notice_id, user_id);


--
-- TOC entry 3211 (class 2606 OID 34716)
-- Name: sys_oper_log sys_oper_log_pkey; Type: CONSTRAINT; Schema: biz_sys; Owner: wz
--

ALTER TABLE ONLY biz_sys.sys_oper_log
    ADD CONSTRAINT sys_oper_log_pkey PRIMARY KEY (oper_id);


--
-- TOC entry 3200 (class 1259 OID 34718)
-- Name: i_sys_login_log_login_time; Type: INDEX; Schema: biz_sys; Owner: wz
--

CREATE INDEX i_sys_login_log_login_time ON biz_sys.sys_login_log USING brin (login_time);


--
-- TOC entry 3209 (class 1259 OID 34719)
-- Name: i_sys_oper_log_oper_time; Type: INDEX; Schema: biz_sys; Owner: wz
--

CREATE INDEX i_sys_oper_log_oper_time ON biz_sys.sys_oper_log USING brin (oper_time);


--
-- TOC entry 3199 (class 1259 OID 34720)
-- Name: u_i_config_key; Type: INDEX; Schema: biz_sys; Owner: wz
--

CREATE UNIQUE INDEX u_i_config_key ON biz_sys.sys_config USING btree (config_key);


--
-- TOC entry 3214 (class 1259 OID 35425)
-- Name: u_i_dict_key; Type: INDEX; Schema: biz_sys; Owner: wz
--

CREATE UNIQUE INDEX u_i_dict_key ON biz_sys.sys_dict USING btree (dict_key);


-- Completed on 2025-04-11 14:10:16

--
-- PostgreSQL database dump complete
--

