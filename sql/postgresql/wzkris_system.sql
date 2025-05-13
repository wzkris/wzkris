--
-- PostgreSQL database dump
--

-- Dumped from database version 15.5
-- Dumped by pg_dump version 16.0

-- Started on 2025-05-13 17:26:03

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
\.


--
-- TOC entry 3359 (class 0 OID 34685)
-- Dependencies: 217
-- Data for Name: sys_message; Type: TABLE DATA; Schema: biz_sys; Owner: wz
--

COPY biz_sys.sys_message (msg_id, title, content, status, creator_id, updater_id, create_at, update_at) FROM stdin;
\.


--
-- TOC entry 3360 (class 0 OID 34690)
-- Dependencies: 218
-- Data for Name: sys_notice; Type: TABLE DATA; Schema: biz_sys; Owner: wz
--

COPY biz_sys.sys_notice (notice_id, notice_type, title, content, create_at, creator_id) FROM stdin;
\.


--
-- TOC entry 3361 (class 0 OID 34693)
-- Dependencies: 219
-- Data for Name: sys_notice_user; Type: TABLE DATA; Schema: biz_sys; Owner: wz
--

COPY biz_sys.sys_notice_user (notice_id, user_id, read_state) FROM stdin;
\.


--
-- TOC entry 3362 (class 0 OID 34696)
-- Dependencies: 220
-- Data for Name: sys_oper_log; Type: TABLE DATA; Schema: biz_sys; Owner: wz
--

COPY biz_sys.sys_oper_log (oper_id, tenant_id, title, sub_title, oper_type, method, request_method, user_id, oper_name, oper_url, oper_ip, oper_location, oper_param, json_result, status, error_msg, oper_time) FROM stdin;
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


-- Completed on 2025-05-13 17:26:03

--
-- PostgreSQL database dump complete
--

