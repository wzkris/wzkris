--
-- PostgreSQL database dump
--

-- Dumped from database version 15.5
-- Dumped by pg_dump version 16.0

-- Started on 2025-04-02 09:50:50

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
1906233858620850177	0	1	admin	password	0		192.168.0.112	内网IP	MSEdge	Windows 10 or Windows Server 2016	2025-03-30 14:35:42.665+08
1906269425115004930	1904406772188934146	1904406772188934144	dsadasd	password	1	商户已过期，请联系管理员	192.168.0.112	内网IP	MSEdge	Windows 10 or Windows Server 2016	2025-03-30 16:57:02.377+08
1906269566404329474	1904406772188934146	1904406772188934144	dsadasd	password	0		192.168.0.112	内网IP	MSEdge	Windows 10 or Windows Server 2016	2025-03-30 16:57:36.067+08
1906270400479428610	0	1	admin	password	0		192.168.0.112	内网IP	MSEdge	Windows 10 or Windows Server 2016	2025-03-30 17:00:54.924+08
1906510724325957634	0	1	admin	password	0		192.168.0.112	内网IP	MSEdge	Windows 10 or Windows Server 2016	2025-03-31 08:55:52.592+08
1906511033576185857	0	1	admin	password	0		192.168.0.112	内网IP	MSEdge	Windows 10 or Windows Server 2016	2025-03-31 08:57:06.306+08
1906511631633604610	0	1	admin	password	0		192.168.0.112	内网IP	MSEdge	Windows 10 or Windows Server 2016	2025-03-31 08:59:28.896+08
1906538592372252673	0	1	admin	password	0		192.168.0.112	内网IP	MSEdge	Windows 10 or Windows Server 2016	2025-03-31 10:46:36.849+08
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
1906268824285151233	1904406772188934146	1904406772188934144	dsadasd	password	1	商户已过期，请联系管理员	192.168.0.112	内网IP	MSEdge	Windows 10 or Windows Server 2016	2025-03-30 16:54:39.127+08
1906521965702406145	0	1	admin	password	0		192.168.0.112	内网IP	MSEdge	Windows 10 or Windows Server 2016	2025-03-31 09:40:32.748+08
1901803732877348866	0	1	admin	password	0		192.168.0.112	内网IP	MSEdge	Windows 10 or Windows Server 2016	2025-03-18 09:11:58.336+08
1901827128109469697	0	1	admin	password	0		192.168.0.112	内网IP	MSEdge	Windows 10 or Windows Server 2016	2025-03-18 10:44:56.224+08
1901862988754358273	0	1	admin	password	0		::1	内网IP	MSEdge	Windows 10 or Windows Server 2016	2025-03-18 13:07:26.033+08
1901886117375545346	0	1	admin	password	0		::1	内网IP	MSEdge	Windows 10 or Windows Server 2016	2025-03-18 14:39:20.252+08
1901897968285028354	0	1	admin	password	0		::1	内网IP	MSEdge	Windows 10 or Windows Server 2016	2025-03-18 15:26:25.832+08
1902155092781096961	0	1	admin	password	0		::1	内网IP	MSEdge	Windows 10 or Windows Server 2016	2025-03-19 08:28:08.923+08
1902177320377704449	0	1	admin	password	0		::1	内网IP	MSEdge	Windows 10 or Windows Server 2016	2025-03-19 09:56:28.531+08
1902195647292563457	0	1	admin	password	0		192.168.0.112	内网IP	MSEdge	Windows 10 or Windows Server 2016	2025-03-19 11:09:18.015+08
1902233065764872193	0	1	admin	password	0		::1	内网IP	MSEdge	Windows 10 or Windows Server 2016	2025-03-19 13:37:59.179+08
1902525776325701633	0	1	admin	password	0		::1	内网IP	MSEdge	Windows 10 or Windows Server 2016	2025-03-20 09:01:06.845+08
1902537089756098562	0	1	admin	password	0		192.168.0.112	内网IP	MSEdge	Windows 10 or Windows Server 2016	2025-03-20 09:46:04.237+08
1902901572785623042	0	1	admin	password	0		192.168.0.112	内网IP	MSEdge	Windows 10 or Windows Server 2016	2025-03-21 09:54:23.737+08
1902925212805713922	0	1	admin	password	0		::1	内网IP	MSEdge	Windows 10 or Windows Server 2016	2025-03-21 11:28:19.996+08
1903272403655184386	0	1	admin	password	0		192.168.0.112	内网IP	MSEdge	Windows 10 or Windows Server 2016	2025-03-22 10:27:56.675+08
1903303728529395714	0	1	admin	password	0		::1	内网IP	MSEdge	Windows 10 or Windows Server 2016	2025-03-22 12:32:25.195+08
1903308233404829698	0	1	admin	password	0		::1	内网IP	MSEdge	Windows 10 or Windows Server 2016	2025-03-22 12:50:19.257+08
1903962787192479745	0	1	admin	password	0		192.168.0.112	内网IP	MSEdge	Windows 10 or Windows Server 2016	2025-03-24 08:11:16.975+08
1904327937263022081	0	1	admin	password	0		192.168.0.112	内网IP	MSEdge	Windows 10 or Windows Server 2016	2025-03-25 08:22:15.535+08
1904328733056708610	0	1	admin	password	0		::1	内网IP	MSEdge	Windows 10 or Windows Server 2016	2025-03-25 08:25:25.334+08
1904690417051058178	0	1	admin	password	0		192.168.0.112	内网IP	MSEdge	Windows 10 or Windows Server 2016	2025-03-26 08:22:37.441+08
1904706285709508609	0	1	admin	password	0		::1	内网IP	MSEdge	Windows 10 or Windows Server 2016	2025-03-26 09:25:40.901+08
1904773532029239297	0	1	admin	password	0		::1	内网IP	MSEdge	Windows 10 or Windows Server 2016	2025-03-26 13:52:53.673+08
1905057289806258177	0	1	admin	password	0		192.168.0.112	内网IP	MSEdge	Windows 10 or Windows Server 2016	2025-03-27 08:40:26.726+08
1905087047688458242	0	1	admin	password	0		::1	内网IP	MSEdge	Windows 10 or Windows Server 2016	2025-03-27 10:38:41.568+08
1905095180464893953	0	1	admin	password	0		::1	内网IP	MSEdge	Windows 10 or Windows Server 2016	2025-03-27 11:11:00.528+08
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
1854445788424261634	0	测试通知	1111	2024-11-07 16:48:24+08	0
1873884393595662338	0	重要通知	zzzzzzzzzzzzzzzzzzzzzzzzzzzzzzz	2024-12-31 08:10:29+08	0
1873884472104644610	0	重要通知	zzzzzzzzzzzzzzzzzzzzzzzzzzzzzzz	2024-12-31 08:10:47+08	0
1873884863462567938	0	重要通知	zzzzzzzzzzzzzzzzzzzzzzzzzzzzzzz	2024-12-31 08:12:21+08	0
1896485559362093057	0	租户创建成功	租户：1111创建成功，超级管理员账号：111111，临时登录密码：81874519，临时操作密码：860038	2025-03-03 16:59:27.006+08	0
1902634418974683138	0	系统用户创建成功	用户账号：12312213创建成功，临时登录密码：84284311	2025-03-20 16:12:49.374+08	0
1902946091774201858	0	系统用户创建成功	用户账号：sadadsd创建成功，临时登录密码：20805783	2025-03-21 12:51:17.963+08	0
\.


--
-- TOC entry 3361 (class 0 OID 34693)
-- Dependencies: 219
-- Data for Name: sys_notice_user; Type: TABLE DATA; Schema: biz_sys; Owner: wz
--

COPY biz_sys.sys_notice_user (notice_id, user_id, read_state) FROM stdin;
1854445788424261634	1	0
1873884393595662338	1	0
1873884472104644610	1	1
1873884863462567938	1	1
1896485559362093057	1	0
1902634418974683138	1	0
1902946091774201858	1	0
\.


--
-- TOC entry 3362 (class 0 OID 34696)
-- Dependencies: 220
-- Data for Name: sys_oper_log; Type: TABLE DATA; Schema: biz_sys; Owner: wz
--

COPY biz_sys.sys_oper_log (oper_id, tenant_id, title, sub_title, oper_type, method, request_method, user_id, oper_name, oper_url, oper_ip, oper_location, oper_param, json_result, status, error_msg, oper_time) FROM stdin;
1905421124756971521	0	参数管理	修改参数	2	com.wzkris.system.controller.SysConfigController.edit()	POST	1	admin	/sys_config/edit	192.168.0.112	内网IP	{"configName":"用户管理-账号初始密码","configKey":"sys.user.initPassword","configId":1,"configValue":"123456","configType":"Y"}	{"code":0,"message":"Success","timestamp":1743122771778}	0	\N	2025-03-28 08:46:11.778+08
1905421225554485250	0	系统用户	状态修改	2	com.wzkris.user.controller.AppUserController.editStatus()	POST	1	admin	/app_user/edit_status	192.168.0.112	内网IP	{"id":1826896461245968384,"status":"1"}	{"code":0,"message":"Success","timestamp":1743122795814}	0	\N	2025-03-28 08:46:35.824+08
1905421253450801153	0	系统用户	状态修改	2	com.wzkris.user.controller.AppUserController.editStatus()	POST	1	admin	/app_user/edit_status	192.168.0.112	内网IP	{"id":1826896461245968384,"status":"0"}	{"code":0,"message":"Success","timestamp":1743122802496}	0	\N	2025-03-28 08:46:42.496+08
1905422266610098177	0	菜单管理	删除菜单	3	com.wzkris.user.controller.SysMenuController.remove()	POST	1	admin	/sys_menu/remove	192.168.0.112	内网IP	2115	{"code":0,"message":"Success","timestamp":1743123044051}	0	\N	2025-03-28 08:50:44.051+08
1905422347233009666	0	菜单管理	删除菜单	3	com.wzkris.user.controller.SysMenuController.remove()	POST	1	admin	/sys_menu/remove	192.168.0.112	内网IP	2114	{"code":0,"message":"Success","timestamp":1743123063272}	0	\N	2025-03-28 08:51:03.272+08
1905422371404783618	0	菜单管理	删除菜单	3	com.wzkris.user.controller.SysMenuController.remove()	POST	1	admin	/sys_menu/remove	192.168.0.112	内网IP	2113	{"code":0,"message":"Success","timestamp":1743123069040}	0	\N	2025-03-28 08:51:09.04+08
1905422396784517122	0	菜单管理	删除菜单	3	com.wzkris.user.controller.SysMenuController.remove()	POST	1	admin	/sys_menu/remove	192.168.0.112	内网IP	2112	{"code":0,"message":"Success","timestamp":1743123075091}	0	\N	2025-03-28 08:51:15.091+08
1905422415293980674	0	菜单管理	删除菜单	3	com.wzkris.user.controller.SysMenuController.remove()	POST	1	admin	/sys_menu/remove	192.168.0.112	内网IP	202	{"code":0,"message":"Success","timestamp":1743123079501}	0	\N	2025-03-28 08:51:19.501+08
1906149834644717569	0	用户信息		2	com.wzkris.user.controller.SysUserProfileController.editInfo()	POST	1	admin	/user_profile	192.168.0.112	内网IP	{"gender":"1","nickname":"nick_a"}	{"code":0,"message":"Success","timestamp":1743296509749}	0	\N	2025-03-30 09:01:49.75+08
1906149856195051522	0	用户信息		2	com.wzkris.user.controller.SysUserProfileController.editInfo()	POST	1	admin	/user_profile	192.168.0.112	内网IP	{"gender":"0","nickname":"nick_a"}	{"code":0,"message":"Success","timestamp":1743296514922}	0	\N	2025-03-30 09:01:54.922+08
1906252652995084289	0	菜单管理	修改菜单	2	com.wzkris.user.controller.SysMenuController.edit()	POST	1	admin	/sys_menu/edit	192.168.0.112	内网IP	{"icon":"carbon:customer","menuName":"顾客管理","isVisible":true,"parentId":3,"path":"appuser","isCache":false,"component":"user/appuser/index","isFrame":false,"menuId":201,"menuType":"M","perms":"app_user:list","menuSort":1,"status":"0"}	{"code":0,"message":"Success","timestamp":1743321023592}	0	\N	2025-03-30 15:50:23.592+08
1906272358103711745	0	菜单管理	修改菜单	2	com.wzkris.user.controller.SysMenuController.edit()	POST	1	admin	/sys_menu/edit	192.168.0.112	内网IP	{"path":"#","isCache":false,"isFrame":false,"icon":"#","menuId":1906263415450001127,"menuName":"商户余额信息","menuType":"B","perms":"tenant:wallet_info","isVisible":true,"parentId":1906272182215585793,"menuSort":0,"status":"0"}	{"code":0,"message":"Success","timestamp":1743325721654}	0	\N	2025-03-30 17:08:41.654+08
1906272576920551426	0	菜单管理	修改菜单	2	com.wzkris.user.controller.SysMenuController.edit()	POST	1	admin	/sys_menu/edit	192.168.0.112	内网IP	{"path":"#","isCache":false,"isFrame":false,"icon":"#","menuId":1906263415450001127,"menuName":"商户钱包","menuType":"B","perms":"tenant:wallet_info","isVisible":true,"parentId":1906272182215585793,"menuSort":0,"status":"0"}	{"code":0,"message":"Success","timestamp":1743325773837}	0	\N	2025-03-30 17:09:33.837+08
1906272685427195905	0	菜单管理	新增菜单	1	com.wzkris.user.controller.SysMenuController.add()	POST	1	admin	/sys_menu/add	192.168.0.112	内网IP	{"isCache":false,"isFrame":false,"menuName":"钱包记录","menuType":"B","perms":"wallet_record:list","isVisible":true,"parentId":1906272182215585793,"menuSort":0,"status":"0"}	{"code":0,"message":"Success","timestamp":1743325799700}	0	\N	2025-03-30 17:09:59.7+08
1906272867120250882	0	菜单管理	修改菜单	2	com.wzkris.user.controller.SysMenuController.edit()	POST	1	admin	/sys_menu/edit	192.168.0.112	内网IP	{"path":"#","isCache":false,"isFrame":false,"icon":"#","menuId":1906272685360099330,"menuName":"钱包记录","menuType":"B","perms":"tenant:wallet_record:list","isVisible":true,"parentId":1906272182215585793,"menuSort":0,"status":"0"}	{"code":0,"message":"Success","timestamp":1743325843021}	0	\N	2025-03-30 17:10:43.021+08
1906273182313807874	0	菜单管理	修改菜单	2	com.wzkris.user.controller.SysMenuController.edit()	POST	1	admin	/sys_menu/edit	192.168.0.112	内网IP	{"path":"tenant","isCache":false,"isFrame":false,"icon":"carbon:information-filled","menuId":1906272182215585793,"menuName":"基础信息","menuType":"M","perms":"tenant:info","isVisible":true,"parentId":1906263415450000002,"menuSort":100,"status":"0"}	{"code":0,"message":"Success","timestamp":1743325918174}	0	\N	2025-03-30 17:11:58.174+08
1906567469081550849	0	菜单管理	修改菜单	2	com.wzkris.user.controller.SysMenuController.edit()	POST	1	admin	/sys_menu/edit	192.168.0.112	内网IP	{"query":"status=1","icon":"carbon:user-admin","menuName":"系统用户","isVisible":true,"parentId":1906263415450000002,"path":"sysuser","isCache":true,"component":"user/sysuser/index","isFrame":false,"menuId":1906263415450000203,"menuType":"M","perms":"sys_user:list","menuSort":100,"status":"0"}	{"code":0,"message":"Success","timestamp":1743396081596}	0	\N	2025-03-31 12:41:21.596+08
1906567676766707714	0	菜单管理	修改菜单	2	com.wzkris.user.controller.SysMenuController.edit()	POST	1	admin	/sys_menu/edit	192.168.0.112	内网IP	{"query":"a=1","icon":"carbon:user-admin","menuName":"系统用户","isVisible":true,"parentId":1906263415450000002,"path":"sysuser","isCache":true,"component":"user/sysuser/index","isFrame":false,"menuId":1906263415450000203,"menuType":"M","perms":"sys_user:list","menuSort":100,"status":"0"}	{"code":0,"message":"Success","timestamp":1743396131112}	0	\N	2025-03-31 12:42:11.112+08
1906567753304367106	0	菜单管理	修改菜单	2	com.wzkris.user.controller.SysMenuController.edit()	POST	1	admin	/sys_menu/edit	192.168.0.112	内网IP	{"query":"","icon":"carbon:user-admin","menuName":"系统用户","isVisible":true,"parentId":1906263415450000002,"path":"sysuser","isCache":true,"component":"user/sysuser/index","isFrame":false,"menuId":1906263415450000203,"menuType":"M","perms":"sys_user:list","menuSort":100,"status":"0"}	{"code":0,"message":"Success","timestamp":1743396149360}	0	\N	2025-03-31 12:42:29.36+08
1906628344450637825	0	菜单管理	修改菜单	2	com.wzkris.user.controller.SysMenuController.edit()	POST	1	admin	/sys_menu/edit	192.168.0.112	内网IP	{"path":"http://localhost:9200/xxl-job-admin","isCache":false,"icon":"carbon:link","menuId":1906263415450000300,"menuName":"定时任务","menuType":"O","perms":"tool:job:list","isVisible":true,"parentId":1906263415450000101,"menuSort":20,"status":"0"}	\N	1	Cannot invoke "java.lang.Boolean.booleanValue()" because the return value of "com.wzkris.user.domain.req.SysMenuReq.getIsFrame()" is null	2025-03-31 16:43:15.373+08
1905427836733890561	0	菜单管理	修改菜单	2	com.wzkris.user.controller.SysMenuController.edit()	POST	1	admin	/sys_menu/edit	192.168.0.112	内网IP	{"icon":"dict","menuName":"字典管理","isVisible":true,"parentId":1,"path":"dict","isCache":false,"component":"system/dict/index","isFrame":false,"menuId":102,"menuType":"M","perms":"sys_dict:list","menuSort":6,"status":"0"}	{"code":0,"message":"Success","timestamp":1743124372055}	0	\N	2025-03-28 09:12:52.055+08
1905427951104172034	0	菜单管理	修改菜单	2	com.wzkris.user.controller.SysMenuController.edit()	POST	1	admin	/sys_menu/edit	192.168.0.112	内网IP	{"path":"#","isCache":false,"isFrame":false,"icon":"#","menuId":1049,"menuName":"字典删除","menuType":"B","perms":"sys_dict:remove","isVisible":true,"parentId":102,"menuSort":4,"status":"0"}	{"code":0,"message":"Success","timestamp":1743124399339}	0	\N	2025-03-28 09:13:19.339+08
1905427987733028866	0	菜单管理	修改菜单	2	com.wzkris.user.controller.SysMenuController.edit()	POST	1	admin	/sys_menu/edit	192.168.0.112	内网IP	{"path":"#","isCache":false,"isFrame":false,"icon":"#","menuId":1048,"menuName":"字典修改","menuType":"B","perms":"sys_dict:edit","isVisible":true,"parentId":102,"menuSort":3,"status":"0"}	{"code":0,"message":"Success","timestamp":1743124408072}	0	\N	2025-03-28 09:13:28.072+08
1905428012823355393	0	菜单管理	修改菜单	2	com.wzkris.user.controller.SysMenuController.edit()	POST	1	admin	/sys_menu/edit	192.168.0.112	内网IP	{"path":"#","isCache":false,"isFrame":false,"icon":"#","menuId":1047,"menuName":"字典新增","menuType":"B","perms":"sys_dict:add","isVisible":true,"parentId":102,"menuSort":2,"status":"0"}	{"code":0,"message":"Success","timestamp":1743124414054}	0	\N	2025-03-28 09:13:34.054+08
1905428052602134530	0	菜单管理	修改菜单	2	com.wzkris.user.controller.SysMenuController.edit()	POST	1	admin	/sys_menu/edit	192.168.0.112	内网IP	{"path":"#","isCache":false,"isFrame":false,"icon":"#","menuId":1046,"menuName":"字典查询","menuType":"B","perms":"sys_dict:query","isVisible":true,"parentId":102,"menuSort":1,"status":"0"}	{"code":0,"message":"Success","timestamp":1743124423538}	0	\N	2025-03-28 09:13:43.538+08
1905428073158418433	0	菜单管理	删除菜单	3	com.wzkris.user.controller.SysMenuController.remove()	POST	1	admin	/sys_menu/remove	192.168.0.112	内网IP	1050	{"code":412,"message":"菜单已分配,不允许删除","timestamp":1743124428447}	1	菜单已分配,不允许删除	2025-03-28 09:13:48.447+08
1905428088589262849	0	菜单管理	删除菜单	3	com.wzkris.user.controller.SysMenuController.remove()	POST	1	admin	/sys_menu/remove	192.168.0.112	内网IP	1050	{"code":412,"message":"菜单已分配,不允许删除","timestamp":1743124432122}	1	菜单已分配,不允许删除	2025-03-28 09:13:52.122+08
1905428126417690626	0	租户套餐	修改套餐	2	com.wzkris.user.controller.SysTenantPackageController.edit()	POST	1	admin	/sys_tenant/package/edit	192.168.0.112	内网IP	{"isMenuDisplay":false,"packageId":1773625804122202113,"remark":"通用租户套餐","packageName":"默认套餐","menuIds":[2,203,2078,2062,2064,2077,2072,2071,206,2207,2211,2210,2209,2208,205,2040,2039,2038,2037,207,2016,2015,2014,2013,208,2145,2144,2143,2142,2141,700,1215,1214,1212,1213,1211,1210,201,2005,2004,2003,2002,2001,3,601,1129,1131,1132,1125,1134,1133,1126,1127,602,1135,1136,1137,1138],"status":"0"}	{"code":0,"message":"Success","timestamp":1743124441140}	0	\N	2025-03-28 09:14:01.14+08
1905428179429498882	0	菜单管理	删除菜单	3	com.wzkris.user.controller.SysMenuController.remove()	POST	1	admin	/sys_menu/remove	192.168.0.112	内网IP	1050	{"code":412,"message":"菜单已分配,不允许删除","timestamp":1743124453772}	1	菜单已分配,不允许删除	2025-03-28 09:14:13.772+08
1905428246584500225	0	角色管理	修改角色	2	com.wzkris.user.controller.SysRoleController.edit()	POST	1	admin	/sys_role/edit	192.168.0.112	内网IP	{"isMenuDisplay":false,"roleId":1904060860556046337,"roleName":"021042","isDeptDisplay":false,"deptIds":[],"dataScope":"1","menuIds":[],"status":"0","roleSort":0}	{"code":0,"message":"Success","timestamp":1743124469787}	0	\N	2025-03-28 09:14:29.787+08
1905428283200774145	0	菜单管理	删除菜单	3	com.wzkris.user.controller.SysMenuController.remove()	POST	1	admin	/sys_menu/remove	192.168.0.112	内网IP	1050	{"code":0,"message":"Success","timestamp":1743124478520}	0	\N	2025-03-28 09:14:38.52+08
1905429028511817730	0	菜单管理	修改菜单	2	com.wzkris.user.controller.SysMenuController.edit()	POST	1	admin	/sys_menu/edit	192.168.0.112	内网IP	{"icon":"edit","menuName":"参数设置","isVisible":true,"parentId":1,"path":"config","isCache":false,"component":"system/config/index","isFrame":false,"menuId":103,"menuType":"M","perms":"sys_config:list","menuSort":7,"status":"0"}	{"code":0,"message":"Success","timestamp":1743124656214}	0	\N	2025-03-28 09:17:36.214+08
1905429051098144770	0	菜单管理	修改菜单	2	com.wzkris.user.controller.SysMenuController.edit()	POST	1	admin	/sys_menu/edit	192.168.0.112	内网IP	{"path":"#","isCache":false,"isFrame":false,"icon":"#","menuId":1055,"menuName":"参数导出","menuType":"B","perms":"sys_config:export","isVisible":true,"parentId":103,"menuSort":5,"status":"0"}	{"code":0,"message":"Success","timestamp":1743124661598}	0	\N	2025-03-28 09:17:41.598+08
1905429072824639489	0	菜单管理	修改菜单	2	com.wzkris.user.controller.SysMenuController.edit()	POST	1	admin	/sys_menu/edit	192.168.0.112	内网IP	{"path":"#","isCache":false,"isFrame":false,"icon":"#","menuId":1054,"menuName":"参数删除","menuType":"B","perms":"sys_config:remove","isVisible":true,"parentId":103,"menuSort":4,"status":"0"}	{"code":0,"message":"Success","timestamp":1743124666778}	0	\N	2025-03-28 09:17:46.778+08
1905429094072983554	0	菜单管理	修改菜单	2	com.wzkris.user.controller.SysMenuController.edit()	POST	1	admin	/sys_menu/edit	192.168.0.112	内网IP	{"path":"#","isCache":false,"isFrame":false,"icon":"#","menuId":1053,"menuName":"参数修改","menuType":"B","perms":"sys_config:edit","isVisible":true,"parentId":103,"menuSort":3,"status":"0"}	{"code":0,"message":"Success","timestamp":1743124671831}	0	\N	2025-03-28 09:17:51.831+08
1905429123298893825	0	菜单管理	修改菜单	2	com.wzkris.user.controller.SysMenuController.edit()	POST	1	admin	/sys_menu/edit	192.168.0.112	内网IP	{"path":"#","isCache":false,"isFrame":false,"icon":"#","menuId":1052,"menuName":"参数新增","menuType":"B","perms":"sys_config:add","isVisible":true,"parentId":103,"menuSort":2,"status":"0"}	{"code":0,"message":"Success","timestamp":1743124678812}	0	\N	2025-03-28 09:17:58.812+08
1905429149022560257	0	菜单管理	修改菜单	2	com.wzkris.user.controller.SysMenuController.edit()	POST	1	admin	/sys_menu/edit	192.168.0.112	内网IP	{"path":"#","isCache":false,"isFrame":false,"icon":"#","menuId":1051,"menuName":"参数查询","menuType":"B","perms":"sys_config:query","isVisible":true,"parentId":103,"menuSort":1,"status":"0"}	{"code":0,"message":"Success","timestamp":1743124684945}	0	\N	2025-03-28 09:18:04.945+08
1905431489813291010	0	菜单管理	修改菜单	2	com.wzkris.user.controller.SysMenuController.edit()	POST	1	admin	/sys_menu/edit	192.168.0.112	内网IP	{"icon":"tree","menuName":"部门管理","isVisible":true,"parentId":2,"path":"dept","isCache":false,"component":"user/sys_dept/index","isFrame":false,"menuId":205,"menuType":"M","perms":"dept:list","menuSort":70,"status":"0"}	{"code":0,"message":"Success","timestamp":1743125243036}	0	\N	2025-03-28 09:27:23.036+08
1905431595392311297	0	菜单管理	修改菜单	2	com.wzkris.user.controller.SysMenuController.edit()	POST	1	admin	/sys_menu/edit	192.168.0.112	内网IP	{"icon":"tree","menuName":"部门管理","isVisible":true,"parentId":2,"path":"dept","isCache":false,"component":"user/dept/index","isFrame":false,"menuId":205,"menuType":"M","perms":"sys_dept:list","menuSort":70,"status":"0"}	{"code":0,"message":"Success","timestamp":1743125268212}	0	\N	2025-03-28 09:27:48.212+08
1905431631853395969	0	菜单管理	修改菜单	2	com.wzkris.user.controller.SysMenuController.edit()	POST	1	admin	/sys_menu/edit	192.168.0.112	内网IP	{"path":"#","isCache":false,"isFrame":false,"icon":"#","menuId":2040,"menuName":"部门删除","menuType":"B","perms":"sys_dept:remove","isVisible":true,"parentId":205,"menuSort":4,"status":"0"}	{"code":0,"message":"Success","timestamp":1743125276896}	0	\N	2025-03-28 09:27:56.896+08
1905431652912996354	0	菜单管理	修改菜单	2	com.wzkris.user.controller.SysMenuController.edit()	POST	1	admin	/sys_menu/edit	192.168.0.112	内网IP	{"path":"#","isCache":false,"isFrame":false,"icon":"#","menuId":2039,"menuName":"部门修改","menuType":"B","perms":"sys_dept:edit","isVisible":true,"parentId":205,"menuSort":3,"status":"0"}	{"code":0,"message":"Success","timestamp":1743125281930}	0	\N	2025-03-28 09:28:01.93+08
1905431683179094018	0	菜单管理	修改菜单	2	com.wzkris.user.controller.SysMenuController.edit()	POST	1	admin	/sys_menu/edit	192.168.0.112	内网IP	{"path":"#","isCache":false,"isFrame":false,"icon":"#","menuId":2038,"menuName":"部门新增","menuType":"B","perms":"sys_dept:add","isVisible":true,"parentId":205,"menuSort":2,"status":"0"}	{"code":0,"message":"Success","timestamp":1743125289145}	0	\N	2025-03-28 09:28:09.145+08
1905431713491329026	0	菜单管理	修改菜单	2	com.wzkris.user.controller.SysMenuController.edit()	POST	1	admin	/sys_menu/edit	192.168.0.112	内网IP	{"path":"#","isCache":false,"isFrame":false,"icon":"#","menuId":2037,"menuName":"部门查询","menuType":"B","perms":"sys_dept:query","isVisible":true,"parentId":205,"menuSort":1,"status":"0"}	{"code":0,"message":"Success","timestamp":1743125296369}	0	\N	2025-03-28 09:28:16.369+08
1905432147295608833	0	菜单管理	修改菜单	2	com.wzkris.user.controller.SysMenuController.edit()	POST	1	admin	/sys_menu/edit	192.168.0.112	内网IP	{"icon":"tree-table","menuName":"菜单管理","isVisible":true,"parentId":2,"path":"menu","isCache":false,"component":"user/menu/index","isFrame":false,"menuId":207,"menuType":"M","perms":"sys_menu:list","menuSort":50,"status":"0"}	{"code":0,"message":"Success","timestamp":1743125399797}	0	\N	2025-03-28 09:29:59.797+08
1905432170511081474	0	菜单管理	修改菜单	2	com.wzkris.user.controller.SysMenuController.edit()	POST	1	admin	/sys_menu/edit	192.168.0.112	内网IP	{"path":"#","isCache":false,"isFrame":false,"icon":"#","menuId":2016,"menuName":"菜单删除","menuType":"B","perms":"sys_menu:remove","isVisible":true,"parentId":207,"menuSort":4,"status":"0"}	{"code":0,"message":"Success","timestamp":1743125405334}	0	\N	2025-03-28 09:30:05.334+08
1905432190966702082	0	菜单管理	修改菜单	2	com.wzkris.user.controller.SysMenuController.edit()	POST	1	admin	/sys_menu/edit	192.168.0.112	内网IP	{"path":"#","isCache":false,"isFrame":false,"icon":"#","menuId":2015,"menuName":"菜单修改","menuType":"B","perms":"sys_menu:edit","isVisible":true,"parentId":207,"menuSort":3,"status":"0"}	{"code":0,"message":"Success","timestamp":1743125410201}	0	\N	2025-03-28 09:30:10.201+08
1905432218191929345	0	菜单管理	修改菜单	2	com.wzkris.user.controller.SysMenuController.edit()	POST	1	admin	/sys_menu/edit	192.168.0.112	内网IP	{"path":"#","isCache":false,"isFrame":false,"icon":"#","menuId":2014,"menuName":"菜单新增","menuType":"B","perms":"sys_menu:add","isVisible":true,"parentId":207,"menuSort":2,"status":"0"}	{"code":0,"message":"Success","timestamp":1743125416696}	0	\N	2025-03-28 09:30:16.696+08
1905432239842926594	0	菜单管理	修改菜单	2	com.wzkris.user.controller.SysMenuController.edit()	POST	1	admin	/sys_menu/edit	192.168.0.112	内网IP	{"path":"#","isCache":false,"isFrame":false,"icon":"#","menuId":2013,"menuName":"菜单查询","menuType":"B","perms":"sys_menu:query","isVisible":true,"parentId":207,"menuSort":1,"status":"0"}	{"code":0,"message":"Success","timestamp":1743125421855}	0	\N	2025-03-28 09:30:21.855+08
1905432751652872194	0	菜单管理	修改菜单	2	com.wzkris.user.controller.SysMenuController.edit()	POST	1	admin	/sys_menu/edit	192.168.0.112	内网IP	{"icon":"post","menuName":"岗位管理","isVisible":true,"parentId":2,"path":"post","isCache":false,"component":"user/post/index","isFrame":false,"menuId":208,"menuType":"M","perms":"sys_post:list","menuSort":8,"status":"0"}	{"code":0,"message":"Success","timestamp":1743125543882}	0	\N	2025-03-28 09:32:23.882+08
1905432780476129281	0	菜单管理	删除菜单	3	com.wzkris.user.controller.SysMenuController.remove()	POST	1	admin	/sys_menu/remove	192.168.0.112	内网IP	2145	{"code":0,"message":"Success","timestamp":1743125550753}	0	\N	2025-03-28 09:32:30.753+08
1905432805352546306	0	菜单管理	修改菜单	2	com.wzkris.user.controller.SysMenuController.edit()	POST	1	admin	/sys_menu/edit	192.168.0.112	内网IP	{"path":"#","isCache":false,"isFrame":false,"icon":"#","menuId":2144,"menuName":"岗位删除","menuType":"B","perms":"sys_post:remove","isVisible":true,"parentId":208,"menuSort":4,"status":"0"}	{"code":0,"message":"Success","timestamp":1743125556679}	0	\N	2025-03-28 09:32:36.679+08
1905432827787878401	0	菜单管理	修改菜单	2	com.wzkris.user.controller.SysMenuController.edit()	POST	1	admin	/sys_menu/edit	192.168.0.112	内网IP	{"path":"#","isCache":false,"isFrame":false,"icon":"#","menuId":2143,"menuName":"岗位修改","menuType":"B","perms":"sys_post:edit","isVisible":true,"parentId":208,"menuSort":3,"status":"0"}	{"code":0,"message":"Success","timestamp":1743125562031}	0	\N	2025-03-28 09:32:42.031+08
1905432849258520577	0	菜单管理	修改菜单	2	com.wzkris.user.controller.SysMenuController.edit()	POST	1	admin	/sys_menu/edit	192.168.0.112	内网IP	{"path":"#","isCache":false,"isFrame":false,"icon":"#","menuId":2142,"menuName":"岗位新增","menuType":"B","perms":"sys_post:add","isVisible":true,"parentId":208,"menuSort":2,"status":"0"}	{"code":0,"message":"Success","timestamp":1743125567150}	0	\N	2025-03-28 09:32:47.15+08
1905432870494281730	0	菜单管理	修改菜单	2	com.wzkris.user.controller.SysMenuController.edit()	POST	1	admin	/sys_menu/edit	192.168.0.112	内网IP	{"path":"#","isCache":false,"isFrame":false,"icon":"#","menuId":2141,"menuName":"岗位查询","menuType":"B","perms":"sys_post:query","isVisible":true,"parentId":208,"menuSort":1,"status":"0"}	{"code":0,"message":"Success","timestamp":1743125572213}	0	\N	2025-03-28 09:32:52.213+08
1906162376846802946	0	参数管理	修改参数	2	com.wzkris.system.controller.SysConfigController.edit()	POST	1	admin	/sys_config/edit	192.168.0.112	内网IP	{"configName":"用户管理-账号初始密码","configKey":"sys.user.initPassword","configId":1,"configValue":"123456","configType":"Y"}	{"code":0,"message":"Success","timestamp":1743299500046}	0	\N	2025-03-30 09:51:40.047+08
1906254478532345857	0	菜单管理	修改菜单	2	com.wzkris.user.controller.SysMenuController.edit()	POST	1	admin	/sys_menu/edit	192.168.0.112	内网IP	{"icon":"carbon:id-management","menuName":"租户管理","isVisible":true,"parentId":3,"path":"info","isCache":false,"component":"platform/tenant/index","isFrame":false,"menuId":601,"menuType":"M","perms":"tenant:information","menuSort":100,"status":"0"}	{"code":0,"message":"Success","timestamp":1743321458845}	0	\N	2025-03-30 15:57:38.845+08
1905439056249626626	0	菜单管理	修改菜单	2	com.wzkris.user.controller.SysMenuController.edit()	POST	1	admin	/sys_menu/edit	192.168.0.112	内网IP	{"icon":"role","menuName":"角色管理","isVisible":true,"parentId":2,"path":"sysrole","isCache":false,"component":"user/sysrole/index","isFrame":false,"menuId":206,"menuType":"M","perms":"sys_role:list","menuSort":99,"status":"0"}	{"code":0,"message":"Success","timestamp":1743127046978}	0	\N	2025-03-28 09:57:26.978+08
1906164268062670849	0	菜单管理	修改菜单	2	com.wzkris.user.controller.SysMenuController.edit()	POST	1	admin	/sys_menu/edit	192.168.0.112	内网IP	{"path":"user","isCache":false,"isFrame":false,"icon":"carbon:user","menuId":2,"menuName":"平台管理","menuType":"D","isVisible":true,"parentId":0,"menuSort":99,"status":"0"}	{"code":0,"message":"Success","timestamp":1743299950965}	0	\N	2025-03-30 09:59:10.965+08
1906254512929832962	0	菜单管理	修改菜单	2	com.wzkris.user.controller.SysMenuController.edit()	POST	1	admin	/sys_menu/edit	192.168.0.112	内网IP	{"icon":"carbon:package","menuName":"租户套餐管理","isVisible":true,"parentId":3,"path":"package","isCache":false,"component":"platform/tenant/package/index","isFrame":false,"menuId":602,"menuType":"M","perms":"tenant_package:list","menuSort":50,"status":"0"}	{"code":0,"message":"Success","timestamp":1743321467040}	0	\N	2025-03-30 15:57:47.04+08
1906254535218364418	0	菜单管理	修改菜单	2	com.wzkris.user.controller.SysMenuController.edit()	POST	1	admin	/sys_menu/edit	192.168.0.112	内网IP	{"icon":"carbon:application","menuName":"客户端管理","isVisible":true,"parentId":3,"path":"client","isCache":false,"component":"platform/client/index","isFrame":false,"menuId":700,"menuType":"M","perms":"oauth2_client:list","menuSort":3,"status":"0"}	{"code":0,"message":"Success","timestamp":1743321472352}	0	\N	2025-03-30 15:57:52.352+08
1906254584228806657	0	菜单管理	修改菜单	2	com.wzkris.user.controller.SysMenuController.edit()	POST	1	admin	/sys_menu/edit	192.168.0.112	内网IP	{"icon":"carbon:customer","menuName":"顾客管理","isVisible":true,"parentId":3,"path":"appuser","isCache":false,"component":"platform/appuser/index","isFrame":false,"menuId":201,"menuType":"M","perms":"app_user:list","menuSort":1,"status":"0"}	{"code":0,"message":"Success","timestamp":1743321484045}	0	\N	2025-03-30 15:58:04.045+08
1906254691263250434	0	菜单管理	修改菜单	2	com.wzkris.user.controller.SysMenuController.edit()	POST	1	admin	/sys_menu/edit	192.168.0.112	内网IP	{"icon":"carbon:menu","menuName":"菜单管理","isVisible":true,"parentId":3,"path":"sysmenu","isCache":false,"component":"platform/sysmenu/index","isFrame":false,"menuId":207,"menuType":"M","perms":"sys_menu:list","menuSort":50,"status":"0"}	{"code":0,"message":"Success","timestamp":1743321509551}	0	\N	2025-03-30 15:58:29.551+08
1906256852554547202	0	租户套餐	修改套餐	2	com.wzkris.user.controller.SysTenantPackageController.edit()	POST	1	admin	/sys_tenant/package/edit	192.168.0.112	内网IP	{"isMenuDisplay":false,"packageId":1773625804122202113,"remark":"通用租户套餐","packageName":"默认套餐","menuIds":[2,203,2078,2062,2064,2077,2072,2071,206,2207,2211,2210,2209,2208,205,2040,2039,2038,2037,208,2144,2143,2142,2141,3,601,1129,1131,1132,1125,1134,1133,1126,1127,602,1135,1136,1137,1138,207,2016,2015,2014,2013,700,1215,1214,1212,1213,1211,1210,201,2005,2004,2003,2002,2001],"status":"0"}	{"code":0,"message":"Success","timestamp":1743322024841}	0	\N	2025-03-30 16:07:04.841+08
1906256880018849794	0	租户管理	修改租户	2	com.wzkris.user.controller.SysTenantController.edit()	POST	1	admin	/sys_tenant/edit	192.168.0.112	内网IP	{"tenantType":"0","expireTime":1740758400000,"accountLimit":5,"roleLimit":5,"tenantName":"sadasdas","tenantId":1904406772188934146,"packageId":1773625804122202113,"deptLimit":5,"postLimit":5,"status":"0","username":""}	{"code":0,"message":"Success","timestamp":1743322031404}	0	\N	2025-03-30 16:07:11.404+08
1906256906673651713	0	OAuth2客户端管理	修改客户端	2	com.wzkris.user.controller.OAuth2ClientController.edit()	POST	1	admin	/oauth2_client/edit	192.168.0.112	内网IP	{"clientId":"server","clientName":"系统","authorizationGrantTypes":["password","sms","refresh_token"],"id":1,"scopes":["openid"],"redirectUris":["http://localhost:9001/oauth2/authorization_code_callback"],"autoApprove":false,"status":"0"}	{"code":0,"message":"Success","timestamp":1743322037754}	0	\N	2025-03-30 16:07:17.754+08
1906256973430194177	0	菜单管理	修改菜单	2	com.wzkris.user.controller.SysMenuController.edit()	POST	1	admin	/sys_menu/edit	192.168.0.112	内网IP	{"icon":"carbon:menu","menuName":"菜单管理","isVisible":true,"parentId":3,"path":"sysmenu","isCache":false,"component":"platform/sysmenu/index","isFrame":false,"menuId":207,"menuType":"M","perms":"sys_menu:list","menuSort":50,"status":"0"}	{"code":0,"message":"Success","timestamp":1743322053662}	0	\N	2025-03-30 16:07:33.662+08
1906257718049177602	0	租户管理	修改租户	2	com.wzkris.user.controller.SysTenantController.edit()	POST	1	admin	/sys_tenant/edit	192.168.0.112	内网IP	{"tenantType":"1","expireTime":1740758400000,"accountLimit":5,"roleLimit":5,"tenantName":"sadasdas","tenantId":1904406772188934146,"packageId":1773625804122202113,"deptLimit":5,"postLimit":5,"status":"0","username":""}	{"code":0,"message":"Success","timestamp":1743322231192}	0	\N	2025-03-30 16:10:31.192+08
1906549355056979970	0	菜单管理	修改菜单	2	com.wzkris.user.controller.SysMenuController.edit()	POST	1	admin	/sys_menu/edit	192.168.0.112	内网IP	{"path":"http://localhost:8080/doc.html","isCache":false,"isFrame":true,"icon":"carbon:link","menuId":1906263415450000301,"menuName":"系统接口","menuType":"M","perms":"tool:swagger:list","isVisible":true,"parentId":1906263415450000101,"menuSort":2,"status":"0"}	{"code":0,"message":"Success","timestamp":1743391762835}	0	\N	2025-03-31 11:29:22.836+08
1906582163234217985	0	菜单管理	修改菜单	2	com.wzkris.user.controller.SysMenuController.edit()	POST	1	admin	/sys_menu/edit	192.168.0.112	内网IP	{"path":"http://localhost:9200/xxl-job-admin","isCache":false,"isFrame":false,"icon":"carbon:link","menuId":1906263415450000300,"menuName":"定时任务","menuType":"M","perms":"tool:job:list","isVisible":true,"parentId":1906263415450000101,"menuSort":20,"status":"0"}	{"code":0,"message":"Success","timestamp":1743399584905}	0	\N	2025-03-31 13:39:44.922+08
1906586993193377793	0	菜单管理	修改菜单	2	com.wzkris.user.controller.SysMenuController.edit()	POST	1	admin	/sys_menu/edit	192.168.0.112	内网IP	{"path":"user","isCache":false,"isFrame":true,"icon":"carbon:user","menuId":1906263415450000002,"menuName":"用户管理","menuType":"D","isVisible":true,"parentId":0,"menuSort":99,"status":"0"}	{"code":412,"message":"修改菜单'用户管理'失败，地址必须以http(s)://开头","timestamp":1743400736513}	1	修改菜单'用户管理'失败，地址必须以http(s)://开头	2025-03-31 13:58:56.513+08
1906628378336419841	0	菜单管理	修改菜单	2	com.wzkris.user.controller.SysMenuController.edit()	POST	1	admin	/sys_menu/edit	192.168.0.112	内网IP	{"path":"http://localhost:9200/xxl-job-admin","isCache":false,"icon":"carbon:link","menuId":1906263415450000300,"menuName":"定时任务","menuType":"O","perms":"tool:job:list","isVisible":true,"parentId":1906263415450000101,"menuSort":20,"status":"0"}	\N	1	Cannot invoke "java.lang.Boolean.booleanValue()" because the return value of "com.wzkris.user.domain.req.SysMenuReq.getIsFrame()" is null	2025-03-31 16:43:23.491+08
1905441984955949058	0	菜单管理	修改菜单	2	com.wzkris.user.controller.SysMenuController.edit()	POST	1	admin	/sys_menu/edit	192.168.0.112	内网IP	{"icon":"tree","menuName":"部门管理","isVisible":true,"parentId":2,"path":"sysdept","isCache":false,"component":"user/sysdept/index","isFrame":false,"menuId":205,"menuType":"M","perms":"sys_dept:list","menuSort":70,"status":"0"}	{"code":0,"message":"Success","timestamp":1743127745271}	0	\N	2025-03-28 10:09:05.271+08
1906166565618515970	0	菜单管理	修改菜单	2	com.wzkris.user.controller.SysMenuController.edit()	POST	1	admin	/sys_menu/edit	192.168.0.112	内网IP	{"path":"http://localhost:9100/","isCache":false,"isFrame":true,"icon":"carbon:link","menuId":304,"menuName":"服务监控","menuType":"M","perms":"monitor:server:list","isVisible":true,"parentId":101,"menuSort":5,"status":"0"}	{"code":0,"message":"Success","timestamp":1743300498774}	0	\N	2025-03-30 10:08:18.774+08
1906166598413778945	0	菜单管理	修改菜单	2	com.wzkris.user.controller.SysMenuController.edit()	POST	1	admin	/sys_menu/edit	192.168.0.112	内网IP	{"path":"http://localhost:9200/xxl-job-admin","isCache":false,"isFrame":true,"icon":"carbon:link","menuId":300,"menuName":"定时任务","menuType":"M","perms":"tool:job:list","isVisible":true,"parentId":101,"menuSort":20,"status":"0"}	{"code":0,"message":"Success","timestamp":1743300506584}	0	\N	2025-03-30 10:08:26.584+08
1906167545571508225	0	菜单管理	修改菜单	2	com.wzkris.user.controller.SysMenuController.edit()	POST	1	admin	/sys_menu/edit	192.168.0.112	内网IP	{"icon":"carbon:information","menuName":"商户信息","isVisible":true,"parentId":2,"path":"info","isCache":false,"component":"user/tenant/index","isFrame":false,"menuId":601,"menuType":"M","perms":"tenant:information","menuSort":100,"status":"0"}	{"code":0,"message":"Success","timestamp":1743300732397}	0	\N	2025-03-30 10:12:12.397+08
1906167947201282050	0	菜单管理	修改菜单	2	com.wzkris.user.controller.SysMenuController.edit()	POST	1	admin	/sys_menu/edit	192.168.0.112	内网IP	{"icon":"carbon:information","menuName":"商户信息","isVisible":true,"parentId":3,"path":"info","isCache":false,"component":"user/tenant/index","isFrame":false,"menuId":601,"menuType":"M","perms":"tenant:information","menuSort":100,"status":"0"}	{"code":0,"message":"Success","timestamp":1743300828155}	0	\N	2025-03-30 10:13:48.155+08
1906168127443107841	0	菜单管理	修改菜单	2	com.wzkris.user.controller.SysMenuController.edit()	POST	1	admin	/sys_menu/edit	192.168.0.112	内网IP	{"path":"user","isCache":false,"isFrame":false,"icon":"carbon:user","menuId":2,"menuName":"用户管理","menuType":"D","isVisible":true,"parentId":0,"menuSort":99,"status":"0"}	{"code":0,"message":"Success","timestamp":1743300871128}	0	\N	2025-03-30 10:14:31.128+08
1906168174595473410	0	菜单管理	修改菜单	2	com.wzkris.user.controller.SysMenuController.edit()	POST	1	admin	/sys_menu/edit	192.168.0.112	内网IP	{"icon":"carbon:application","menuName":"客户端管理","isVisible":true,"parentId":3,"path":"client","isCache":false,"component":"user/client/index","isFrame":false,"menuId":700,"menuType":"M","perms":"oauth2_client:list","menuSort":3,"status":"0"}	{"code":0,"message":"Success","timestamp":1743300882379}	0	\N	2025-03-30 10:14:42.379+08
1906168227024273409	0	菜单管理	修改菜单	2	com.wzkris.user.controller.SysMenuController.edit()	POST	1	admin	/sys_menu/edit	192.168.0.112	内网IP	{"path":"tenant","isCache":false,"isFrame":false,"icon":"carbon:business-processes","menuId":3,"menuName":"平台管理","menuType":"D","isVisible":true,"parentId":0,"menuSort":50,"status":"0"}	{"code":0,"message":"Success","timestamp":1743300894870}	0	\N	2025-03-30 10:14:54.87+08
1906168292761600001	0	菜单管理	修改菜单	2	com.wzkris.user.controller.SysMenuController.edit()	POST	1	admin	/sys_menu/edit	192.168.0.112	内网IP	{"icon":"carbon:information","menuName":"商户管理","isVisible":true,"parentId":3,"path":"info","isCache":false,"component":"user/tenant/index","isFrame":false,"menuId":601,"menuType":"M","perms":"tenant:information","menuSort":100,"status":"0"}	{"code":0,"message":"Success","timestamp":1743300910546}	0	\N	2025-03-30 10:15:10.546+08
1906262891945459714	0	部门管理	新增部门	1	com.wzkris.user.controller.SysDeptController.add()	POST	1	admin	/sys_dept/add	192.168.0.112	内网IP	{"deptSort":0,"deptName":"心房颤动在","status":"0"}	{"code":0,"message":"Success","timestamp":1743323464755}	0	\N	2025-03-30 16:31:04.755+08
1906262945305395201	0	部门管理	删除部门	3	com.wzkris.user.controller.SysDeptController.remove()	POST	1	admin	/sys_dept/remove	192.168.0.112	内网IP	1906262891949649921	{"code":0,"message":"Success","timestamp":1743323477482}	0	\N	2025-03-30 16:31:17.482+08
1906263415457513473	0	部门管理	新增部门	1	com.wzkris.user.controller.SysDeptController.add()	POST	1	admin	/sys_dept/add	192.168.0.112	内网IP	{"deptSort":0,"deptName":"11","status":"0"}	{"code":0,"message":"Success","timestamp":1743323589576}	0	\N	2025-03-30 16:33:09.576+08
1906263815002718209	0	部门管理	删除部门	3	com.wzkris.user.controller.SysDeptController.remove()	POST	1	admin	/sys_dept/remove	192.168.0.112	内网IP	1906263415457509377	{"code":0,"message":"Success","timestamp":1743323684829}	0	\N	2025-03-30 16:34:44.829+08
1906263891318079490	0	租户套餐	修改套餐	2	com.wzkris.user.controller.SysTenantPackageController.edit()	POST	1	admin	/sys_tenant/package/edit	192.168.0.112	内网IP	{"isMenuDisplay":false,"packageId":1773625804122202113,"remark":"通用租户套餐","packageName":"默认套餐","menuIds":[1906263415450000002,1906263415450000203,1906263415450002078,1906263415450002062,1906263415450002064,1906263415450002077,1906263415450002072,1906263415450002071,1906263415450000206,1906263415450002207,1906263415450002211,1906263415450002210,1906263415450002209,1906263415450002208,1906263415450000205,1906263415450002040,1906263415450002039,1906263415450002038,1906263415450002037,1906263415450000208,1906263415450002144,1906263415450002143,1906263415450002142,1906263415450002141],"status":"0"}	{"code":0,"message":"Success","timestamp":1743323703024}	0	\N	2025-03-30 16:35:03.024+08
1906264058326876161	0	菜单管理	修改菜单	2	com.wzkris.user.controller.SysMenuController.edit()	POST	1	admin	/sys_menu/edit	192.168.0.112	内网IP	{"icon":"carbon:application","menuName":"终端管理","isVisible":true,"parentId":1906263415450000003,"path":"client","isCache":false,"component":"platform/client/index","isFrame":false,"menuId":1906263415450000700,"menuType":"M","perms":"oauth2_client:list","menuSort":3,"status":"0"}	{"code":0,"message":"Success","timestamp":1743323742844}	0	\N	2025-03-30 16:35:42.844+08
1906265147390492674	0	租户套餐	修改套餐	2	com.wzkris.user.controller.SysTenantPackageController.edit()	POST	1	admin	/sys_tenant/package/edit	192.168.0.112	内网IP	{"isMenuDisplay":false,"packageId":1773625804122202113,"remark":"通用租户套餐","packageName":"默认套餐","menuIds":[1906263415450000002,1906263415450000203,1906263415450002078,1906263415450002062,1906263415450002064,1906263415450002077,1906263415450002072,1906263415450002071,1906263415450000206,1906263415450002207,1906263415450002211,1906263415450002210,1906263415450002209,1906263415450002208,1906263415450000205,1906263415450002040,1906263415450002039,1906263415450002038,1906263415450002037,1906263415450000208,1906263415450002144,1906263415450002143,1906263415450002142,1906263415450002141],"status":"0"}	{"code":0,"message":"Success","timestamp":1743324002453}	0	\N	2025-03-30 16:40:02.454+08
1905442048151527426	0	菜单管理	修改菜单	2	com.wzkris.user.controller.SysMenuController.edit()	POST	1	admin	/sys_menu/edit	192.168.0.112	内网IP	{"icon":"post","menuName":"岗位管理","isVisible":true,"parentId":2,"path":"syspost","isCache":false,"component":"user/syspost/index","isFrame":false,"menuId":208,"menuType":"M","perms":"sys_post:list","menuSort":8,"status":"0"}	{"code":0,"message":"Success","timestamp":1743127760337}	0	\N	2025-03-28 10:09:20.337+08
1905442271003287553	0	角色管理	修改角色	2	com.wzkris.user.controller.SysRoleController.edit()	POST	1	admin	/sys_role/edit	192.168.0.112	内网IP	{"isMenuDisplay":false,"roleId":1904060860556046337,"roleName":"021042","isDeptDisplay":false,"deptIds":[],"dataScope":"1","menuIds":[],"status":"0","roleSort":0}	{"code":0,"message":"Success","timestamp":1743127813470}	0	\N	2025-03-28 10:10:13.47+08
1905442284852879361	0	部门管理	修改部门	2	com.wzkris.user.controller.SysDeptController.edit()	POST	1	admin	/sys_dept/edit	192.168.0.112	内网IP	{"deptSort":0,"deptName":"fvfg","deptId":1904075235413127170,"ancestors":[],"status":"0"}	{"code":0,"message":"Success","timestamp":1743127816771}	0	\N	2025-03-28 10:10:16.771+08
1905442316863807489	0	系统用户	状态修改	2	com.wzkris.user.controller.OAuth2ClientController.editStatus()	POST	1	admin	/oauth2_client/edit_status	192.168.0.112	内网IP	{"id":1,"status":"1"}	{"code":0,"message":"Success","timestamp":1743127824403}	0	\N	2025-03-28 10:10:24.403+08
1905442322039578626	0	系统用户	状态修改	2	com.wzkris.user.controller.OAuth2ClientController.editStatus()	POST	1	admin	/oauth2_client/edit_status	192.168.0.112	内网IP	{"id":1,"status":"0"}	{"code":0,"message":"Success","timestamp":1743127825637}	0	\N	2025-03-28 10:10:25.637+08
1905442348669214721	0	岗位管理	修改岗位	2	com.wzkris.user.controller.SysPostController.edit()	POST	1	admin	/sys_post/edit	192.168.0.112	内网IP	{"postSort":3,"postName":"首席技术官","postCode":"CTO","postId":4,"status":"0"}	{"code":0,"message":"Success","timestamp":1743127831986}	0	\N	2025-03-28 10:10:31.986+08
1905442850773540865	0	菜单管理	新增菜单	1	com.wzkris.user.controller.SysMenuController.add()	POST	1	admin	/sys_menu/add	192.168.0.112	内网IP	{"isCache":false,"isFrame":false,"menuName":"qweqwe","menuType":"D","isVisible":true,"menuSort":0,"status":"0"}	\N	1	\r\n### Error updating database.  Cause: org.postgresql.util.PSQLException: 错误: null value in column "parent_id" of relation "sys_menu" violates not-null constraint\n  详细：失败, 行包含(1905442850081521666, qweqwe, null, 0, null, null, null, D, 0, null, #, f, f, t, 2025-03-28 10:12:31.532+08, 1, 2025-03-28 10:12:31.532+08, 1).\r\n### The error may exist in com/wzkris/user/mapper/SysMenuMapper.java (best guess)\r\n### The error may involve com.wzkris.user.mapper.SysMenuMapper.insert-Inline\r\n### The error occurred while setting parameters\r\n### SQL: INSERT INTO biz_sys.sys_menu (menu_id, menu_name, menu_sort, is_frame, is_cache, is_visible, menu_type, status, create_at, creator_id, update_at, updater_id) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)\r\n### Cause: org.postgresql.util.PSQLException: 错误: null value in column "parent_id" of relation "sys_menu" violates not-null constraint\n  详细：失败, 行包含(1905442850081521666, qweqwe, null, 0, null, null, null, D, 0, null, #, f, f, t, 2025-03-28 10:12:31.532+08, 1, 2025-03-28 10:12:31.532+08, 1).\n; 错误: null value in column "parent_id" of relation "sys_menu" violates not-null constraint\n  详细：失败, 行包含(1905442850081521666, qweqwe, null, 0, null, null, null, D, 0, null, #, f, f, t, 2025-03-28 10:12:31.532+08, 1, 2025-03-28 10:12:31.532+08, 1).	2025-03-28 10:12:31.698+08
1905442881744281602	0	菜单管理	新增菜单	1	com.wzkris.user.controller.SysMenuController.add()	POST	1	admin	/sys_menu/add	192.168.0.112	内网IP	{"isCache":false,"isFrame":false,"menuName":"qweqwe","menuType":"D","isVisible":true,"menuSort":0,"status":"0"}	\N	1	\r\n### Error updating database.  Cause: org.postgresql.util.PSQLException: 错误: null value in column "parent_id" of relation "sys_menu" violates not-null constraint\n  详细：失败, 行包含(1905442881744322561, qweqwe, null, 0, null, null, null, D, 0, null, #, f, f, t, 2025-03-28 10:12:39.081+08, 1, 2025-03-28 10:12:39.081+08, 1).\r\n### The error may exist in com/wzkris/user/mapper/SysMenuMapper.java (best guess)\r\n### The error may involve com.wzkris.user.mapper.SysMenuMapper.insert-Inline\r\n### The error occurred while setting parameters\r\n### SQL: INSERT INTO biz_sys.sys_menu (menu_id, menu_name, menu_sort, is_frame, is_cache, is_visible, menu_type, status, create_at, creator_id, update_at, updater_id) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)\r\n### Cause: org.postgresql.util.PSQLException: 错误: null value in column "parent_id" of relation "sys_menu" violates not-null constraint\n  详细：失败, 行包含(1905442881744322561, qweqwe, null, 0, null, null, null, D, 0, null, #, f, f, t, 2025-03-28 10:12:39.081+08, 1, 2025-03-28 10:12:39.081+08, 1).\n; 错误: null value in column "parent_id" of relation "sys_menu" violates not-null constraint\n  详细：失败, 行包含(1905442881744322561, qweqwe, null, 0, null, null, null, D, 0, null, #, f, f, t, 2025-03-28 10:12:39.081+08, 1, 2025-03-28 10:12:39.081+08, 1).	2025-03-28 10:12:39.081+08
1905444054920470530	0	菜单管理	新增菜单	1	com.wzkris.user.controller.SysMenuController.add()	POST	1	admin	/sys_menu/add	192.168.0.112	内网IP	{"isCache":false,"isFrame":false,"menuName":"asdasd","menuType":"D","isVisible":true,"parentId":0,"menuSort":0,"status":"0"}	\N	1	\r\n### Error updating database.  Cause: org.postgresql.util.PSQLException: 错误: null value in column "path" of relation "sys_menu" violates not-null constraint\n  详细：失败, 行包含(1905444054203277314, asdasd, 0, 0, null, null, null, D, 0, null, #, f, f, t, 2025-03-28 10:17:18.621+08, 1, 2025-03-28 10:17:18.621+08, 1).\r\n### The error may exist in com/wzkris/user/mapper/SysMenuMapper.java (best guess)\r\n### The error may involve com.wzkris.user.mapper.SysMenuMapper.insert-Inline\r\n### The error occurred while setting parameters\r\n### SQL: INSERT INTO biz_sys.sys_menu (menu_id, menu_name, parent_id, menu_sort, is_frame, is_cache, is_visible, menu_type, status, create_at, creator_id, update_at, updater_id) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)\r\n### Cause: org.postgresql.util.PSQLException: 错误: null value in column "path" of relation "sys_menu" violates not-null constraint\n  详细：失败, 行包含(1905444054203277314, asdasd, 0, 0, null, null, null, D, 0, null, #, f, f, t, 2025-03-28 10:17:18.621+08, 1, 2025-03-28 10:17:18.621+08, 1).\n; 错误: null value in column "path" of relation "sys_menu" violates not-null constraint\n  详细：失败, 行包含(1905444054203277314, asdasd, 0, 0, null, null, null, D, 0, null, #, f, f, t, 2025-03-28 10:17:18.621+08, 1, 2025-03-28 10:17:18.621+08, 1).	2025-03-28 10:17:18.741+08
1905444375239467010	0	菜单管理	新增菜单	1	com.wzkris.user.controller.SysMenuController.add()	POST	1	admin	/sys_menu/add	192.168.0.112	内网IP	{"isCache":false,"isFrame":false,"menuName":"asdasd","menuType":"D","isVisible":true,"parentId":0,"menuSort":0,"status":"0"}	{"code":0,"message":"Success","timestamp":1743128315162}	0	\N	2025-03-28 10:18:35.162+08
1905444430109351938	0	菜单管理	删除菜单	3	com.wzkris.user.controller.SysMenuController.remove()	POST	1	admin	/sys_menu/remove	192.168.0.112	内网IP	1905444375172390913	{"code":0,"message":"Success","timestamp":1743128328241}	0	\N	2025-03-28 10:18:48.241+08
1905446823626678273	0	租户套餐	修改租户套餐状态	2	com.wzkris.user.controller.SysTenantPackageController.editStatus()	POST	1	admin	/sys_tenant/package/edit_status	192.168.0.112	内网IP	{"id":1773625804122202113,"status":"1"}	{"code":0,"message":"Success","timestamp":1743128898865}	0	\N	2025-03-28 10:28:18.865+08
1905446828311715842	0	租户套餐	修改租户套餐状态	2	com.wzkris.user.controller.SysTenantPackageController.editStatus()	POST	1	admin	/sys_tenant/package/edit_status	192.168.0.112	内网IP	{"id":1773625804122202113,"status":"0"}	{"code":0,"message":"Success","timestamp":1743128900015}	0	\N	2025-03-28 10:28:20.016+08
1905448004696547330	0	菜单管理	修改菜单	2	com.wzkris.user.controller.SysMenuController.edit()	POST	1	admin	/sys_menu/edit	192.168.0.112	内网IP	{"icon":"guide","menuName":"消息管理","isVisible":true,"parentId":1,"path":"sys_message","isCache":false,"component":"system/sysmessage/index","isFrame":false,"menuId":100,"menuType":"M","perms":"sys_message:list","menuSort":15,"status":"0"}	{"code":0,"message":"Success","timestamp":1743129180488}	0	\N	2025-03-28 10:33:00.488+08
1905448052016685058	0	菜单管理	修改菜单	2	com.wzkris.user.controller.SysMenuController.edit()	POST	1	admin	/sys_menu/edit	192.168.0.112	内网IP	{"icon":"edit","menuName":"参数管理","isVisible":true,"parentId":1,"path":"config","isCache":false,"component":"system/config/index","isFrame":false,"menuId":103,"menuType":"M","perms":"sys_config:list","menuSort":7,"status":"0"}	{"code":0,"message":"Success","timestamp":1743129191770}	0	\N	2025-03-28 10:33:11.77+08
1906172035364225026	0	角色管理	删除角色	3	com.wzkris.user.controller.SysRoleController.remove()	POST	1	admin	/sys_role/remove	192.168.0.112	内网IP	[1904060860556046337]	{"code":0,"message":"Success","timestamp":1743301802852}	0	\N	2025-03-30 10:30:02.852+08
1906265956069081089	0	菜单管理	修改菜单	2	com.wzkris.user.controller.SysMenuController.edit()	POST	1	admin	/sys_menu/edit	192.168.0.112	内网IP	{"icon":"carbon:id-management","menuName":"租户管理","isVisible":true,"parentId":1906263415450000003,"path":"systenant","isCache":false,"component":"platform/tenant/index","isFrame":false,"menuId":1906263415450000601,"menuType":"M","perms":"tenant:information","menuSort":100,"status":"0"}	{"code":0,"message":"Success","timestamp":1743324195297}	0	\N	2025-03-30 16:43:15.297+08
1906266189826031617	0	菜单管理	修改菜单	2	com.wzkris.user.controller.SysMenuController.edit()	POST	1	admin	/sys_menu/edit	192.168.0.112	内网IP	{"icon":"carbon:id-management","menuName":"租户管理","isVisible":true,"parentId":1906263415450000003,"path":"systenant","isCache":false,"component":"platform/systenant/index","isFrame":false,"menuId":1906263415450000601,"menuType":"M","perms":"tenant:information","menuSort":100,"status":"0"}	{"code":0,"message":"Success","timestamp":1743324251032}	0	\N	2025-03-30 16:44:11.032+08
1906267298468659201	0	菜单管理	修改菜单	2	com.wzkris.user.controller.SysMenuController.edit()	POST	1	admin	/sys_menu/edit	192.168.0.112	内网IP	{"icon":"carbon:package","menuName":"租户套餐管理","isVisible":true,"parentId":1906263415450000003,"path":"package","isCache":false,"component":"platform/systenant/package/index","isFrame":false,"menuId":1906263415450000602,"menuType":"M","perms":"tenant_package:list","menuSort":50,"status":"0"}	{"code":0,"message":"Success","timestamp":1743324515354}	0	\N	2025-03-30 16:48:35.355+08
1906267331796598785	0	租户套餐	修改套餐	2	com.wzkris.user.controller.SysTenantPackageController.edit()	POST	1	admin	/sys_tenant/package/edit	192.168.0.112	内网IP	{"isMenuDisplay":false,"packageId":1773625804122202113,"remark":"通用租户套餐","packageName":"默认套餐","menuIds":[1906263415450000002,1906263415450000203,1906263415450002078,1906263415450002062,1906263415450002064,1906263415450002077,1906263415450002072,1906263415450002071,1906263415450000206,1906263415450002207,1906263415450002211,1906263415450002210,1906263415450002209,1906263415450002208,1906263415450000205,1906263415450002040,1906263415450002039,1906263415450002038,1906263415450002037,1906263415450000208,1906263415450002144,1906263415450002143,1906263415450002142,1906263415450002141],"status":"0"}	{"code":0,"message":"Success","timestamp":1743324523302}	0	\N	2025-03-30 16:48:43.302+08
1906267344874438657	0	租户管理	修改租户	2	com.wzkris.user.controller.SysTenantController.edit()	POST	1	admin	/sys_tenant/edit	192.168.0.112	内网IP	{"tenantType":"1","expireTime":1740758400000,"accountLimit":5,"roleLimit":5,"tenantName":"sadasdas","tenantId":1904406772188934146,"packageId":1773625804122202113,"deptLimit":5,"postLimit":5,"status":"0","username":""}	{"code":0,"message":"Success","timestamp":1743324526409}	0	\N	2025-03-30 16:48:46.409+08
1906267446003302401	0	菜单管理	修改菜单	2	com.wzkris.user.controller.SysMenuController.edit()	POST	1	admin	/sys_menu/edit	192.168.0.112	内网IP	{"icon":"carbon:package","menuName":"租户套餐管理","isVisible":true,"parentId":1906263415450000003,"path":"/systenant/package","isCache":false,"component":"platform/systenant/package/index","isFrame":false,"menuId":1906263415450000602,"menuType":"M","perms":"tenant_package:list","menuSort":50,"status":"0"}	{"code":0,"message":"Success","timestamp":1743324550529}	0	\N	2025-03-30 16:49:10.529+08
1906267509999992834	0	菜单管理	修改菜单	2	com.wzkris.user.controller.SysMenuController.edit()	POST	1	admin	/sys_menu/edit	192.168.0.112	内网IP	{"icon":"carbon:package","menuName":"租户套餐管理","isVisible":true,"parentId":1906263415450000003,"path":"systenant/package","isCache":false,"component":"platform/systenant/package/index","isFrame":false,"menuId":1906263415450000602,"menuType":"M","perms":"tenant_package:list","menuSort":50,"status":"0"}	{"code":0,"message":"Success","timestamp":1743324565777}	0	\N	2025-03-30 16:49:25.777+08
1906567248532463618	0	菜单管理	修改菜单	2	com.wzkris.user.controller.SysMenuController.edit()	POST	1	admin	/sys_menu/edit	192.168.0.112	内网IP	{"query":"{status:1}","icon":"carbon:user-admin","menuName":"系统用户","isVisible":true,"parentId":1906263415450000002,"path":"sysuser","isCache":true,"component":"user/sysuser/index","isFrame":false,"menuId":1906263415450000203,"menuType":"M","perms":"sys_user:list","menuSort":100,"status":"0"}	{"code":0,"message":"Success","timestamp":1743396028981}	0	\N	2025-03-31 12:40:28.987+08
1906616920328364034	0	租户套餐	修改套餐	2	com.wzkris.user.controller.SysTenantPackageController.edit()	POST	1	admin	/sys_tenant/package/edit	192.168.0.112	内网IP	{"isMenuDisplay":false,"packageId":1773625804122202113,"remark":"通用租户套餐","packageName":"默认套餐","menuIds":[1906263415450000002,1906272182215585793,1906263415450001126,1906272685360099330,1906263415450001127,1906263415450000203,1906263415450002078,1906263415450002062,1906263415450002064,1906263415450002077,1906263415450002072,1906263415450002071,1906263415450000206,1906263415450002207,1906263415450002211,1906263415450002210,1906263415450002209,1906263415450002208,1906263415450000205,1906263415450002040,1906263415450002039,1906263415450002038,1906263415450002037,1906263415450000208,1906263415450002144,1906263415450002143,1906263415450002142,1906263415450002141],"status":"0"}	{"code":0,"message":"Success","timestamp":1743407871659}	0	\N	2025-03-31 15:57:51.659+08
1906628450059018241	0	菜单管理	修改菜单	2	com.wzkris.user.controller.SysMenuController.edit()	POST	1	admin	/sys_menu/edit	192.168.0.112	内网IP	{"path":"http://localhost:9200/xxl-job-admin","isCache":false,"icon":"carbon:link","menuId":1906263415450000300,"menuName":"定时任务","menuType":"O","perms":"tool:job:list","isVisible":true,"parentId":1906263415450000101,"menuSort":20,"status":"0"}	\N	1	Cannot invoke "java.lang.Boolean.booleanValue()" because the return value of "com.wzkris.user.domain.req.SysMenuReq.getIsFrame()" is null	2025-03-31 16:43:40.604+08
1905449532358533121	0	菜单管理	修改菜单	2	com.wzkris.user.controller.SysMenuController.edit()	POST	1	admin	/sys_menu/edit	192.168.0.112	内网IP	{"icon":"carbon:message-queue","menuName":"消息管理","isVisible":true,"parentId":1,"path":"sys_message","isCache":false,"component":"system/sysmessage/index","isFrame":false,"menuId":100,"menuType":"M","perms":"sys_message:list","menuSort":15,"status":"0"}	{"code":0,"message":"Success","timestamp":1743129544710}	0	\N	2025-03-28 10:39:04.71+08
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
1906270669057490946	0	菜单管理	修改菜单	2	com.wzkris.user.controller.SysMenuController.edit()	POST	1	admin	/sys_menu/edit	192.168.0.112	内网IP	{"icon":"carbon:id-management","menuName":"租户管理","isVisible":true,"parentId":1906263415450000003,"path":"systenant","isCache":false,"component":"platform/systenant/index","isFrame":false,"menuId":1906263415450000601,"menuType":"M","perms":"sys_tenant:list","menuSort":100,"status":"0"}	{"code":0,"message":"Success","timestamp":1743325318964}	0	\N	2025-03-30 17:01:58.964+08
1906270700376358914	0	菜单管理	修改菜单	2	com.wzkris.user.controller.SysMenuController.edit()	POST	1	admin	/sys_menu/edit	192.168.0.112	内网IP	{"path":"#","isCache":false,"isFrame":false,"icon":"#","menuId":1906263415450001129,"menuName":"重置租户操作密码","menuType":"B","perms":"sys_tenant:reset_operpwd","isVisible":true,"parentId":1906263415450000601,"menuSort":11,"status":"0"}	{"code":0,"message":"Success","timestamp":1743325326422}	0	\N	2025-03-30 17:02:06.422+08
1906270725382799361	0	菜单管理	修改菜单	2	com.wzkris.user.controller.SysMenuController.edit()	POST	1	admin	/sys_menu/edit	192.168.0.112	内网IP	{"path":"#","isCache":false,"isFrame":false,"icon":"#","menuId":1906263415450001131,"menuName":"租户详情","menuType":"B","perms":"sys_tenant:query","isVisible":true,"parentId":1906263415450000601,"menuSort":9,"status":"0"}	{"code":0,"message":"Success","timestamp":1743325332382}	0	\N	2025-03-30 17:02:12.382+08
1906270813945528322	0	菜单管理	修改菜单	2	com.wzkris.user.controller.SysMenuController.edit()	POST	1	admin	/sys_menu/edit	192.168.0.112	内网IP	{"path":"#","isCache":false,"isFrame":false,"icon":"#","menuId":1906263415450001132,"menuName":"租户新增","menuType":"B","perms":"sys_tenant:add","isVisible":true,"parentId":1906263415450000601,"menuSort":4,"status":"0"}	{"code":0,"message":"Success","timestamp":1743325353510}	0	\N	2025-03-30 17:02:33.51+08
1906270853615255553	0	菜单管理	修改菜单	2	com.wzkris.user.controller.SysMenuController.edit()	POST	1	admin	/sys_menu/edit	192.168.0.112	内网IP	{"path":"#","isCache":false,"isFrame":false,"icon":"#","menuId":1906263415450001134,"menuName":"租户删除","menuType":"B","perms":"sys_tenant:remove","isVisible":true,"parentId":1906263415450000601,"menuSort":2,"status":"0"}	{"code":0,"message":"Success","timestamp":1743325362956}	0	\N	2025-03-30 17:02:42.956+08
1906270890537713665	0	菜单管理	修改菜单	2	com.wzkris.user.controller.SysMenuController.edit()	POST	1	admin	/sys_menu/edit	192.168.0.112	内网IP	{"path":"#","isCache":false,"isFrame":false,"icon":"#","menuId":1906263415450001133,"menuName":"租户修改","menuType":"B","perms":"sys_tenant:edit","isVisible":true,"parentId":1906263415450000601,"menuSort":2,"status":"0"}	{"code":0,"message":"Success","timestamp":1743325371764}	0	\N	2025-03-30 17:02:51.764+08
1906271461231493122	0	菜单管理	修改菜单	2	com.wzkris.user.controller.SysMenuController.edit()	POST	1	admin	/sys_menu/edit	192.168.0.112	内网IP	{"path":"#","isCache":false,"isFrame":false,"icon":"#","menuId":1906263415450001125,"menuName":"余额记录","menuType":"B","perms":"sys_tenant:wallet_record:list","isVisible":true,"parentId":1906263415450000601,"menuSort":3,"status":"0"}	{"code":0,"message":"Success","timestamp":1743325507797}	0	\N	2025-03-30 17:05:07.797+08
1906272182244933633	0	菜单管理	新增菜单	1	com.wzkris.user.controller.SysMenuController.add()	POST	1	admin	/sys_menu/add	192.168.0.112	内网IP	{"path":"tenant","isCache":false,"isFrame":false,"icon":"carbon:information-filled","menuName":"基础信息","menuType":"M","isVisible":true,"parentId":1906263415450000002,"menuSort":0,"status":"0"}	{"code":0,"message":"Success","timestamp":1743325679732}	0	\N	2025-03-30 17:07:59.732+08
1906272253095116801	0	菜单管理	修改菜单	2	com.wzkris.user.controller.SysMenuController.edit()	POST	1	admin	/sys_menu/edit	192.168.0.112	内网IP	{"path":"tenant","isCache":false,"isFrame":false,"icon":"carbon:information-filled","menuId":1906272182215585793,"menuName":"基础信息","menuType":"M","isVisible":true,"parentId":1906263415450000002,"menuSort":100,"status":"0"}	{"code":0,"message":"Success","timestamp":1743325696627}	0	\N	2025-03-30 17:08:16.627+08
1906272311010066433	0	菜单管理	修改菜单	2	com.wzkris.user.controller.SysMenuController.edit()	POST	1	admin	/sys_menu/edit	192.168.0.112	内网IP	{"path":"#","isCache":false,"isFrame":false,"icon":"#","menuId":1906263415450001126,"menuName":"商户提现","menuType":"B","perms":"tenant:withdrawal","isVisible":true,"parentId":1906272182215585793,"menuSort":1,"status":"0"}	{"code":0,"message":"Success","timestamp":1743325710439}	0	\N	2025-03-30 17:08:30.439+08
1906975113524998145	0	参数管理	修改参数	2	com.wzkris.system.controller.SysConfigController.edit()	POST	1	admin	/sys_config/edit	192.168.0.112	内网IP	{"configName":"用户管理-账号初始密码","configKey":"sys.user.initPassword","configId":1,"configValue":"123456","configType":"Y"}	{"code":0,"message":"Success","timestamp":1743493271554}	0	\N	2025-04-01 15:41:11.555+08
1905542398711832578	0	用户信息		2	com.wzkris.user.controller.SysUserProfileController.editInfo()	POST	1	admin	/user_profile	192.168.0.112	内网IP	{"gender":"1","nickname":"nick_admin"}	{"code":0,"message":"Success","timestamp":1743151685508}	0	\N	2025-03-28 16:48:05.514+08
1905542415174475778	0	用户信息		2	com.wzkris.user.controller.SysUserProfileController.editInfo()	POST	1	admin	/user_profile	192.168.0.112	内网IP	{"gender":"1","nickname":"nick_admin"}	{"code":0,"message":"Success","timestamp":1743151689701}	0	\N	2025-03-28 16:48:09.701+08
1905542990918197249	0	用户信息		2	com.wzkris.user.controller.SysUserProfileController.editInfo()	POST	1	admin	/user_profile	192.168.0.112	内网IP	{"gender":"1","nickname":"nick_admin"}	{"code":0,"message":"Success","timestamp":1743151826969}	0	\N	2025-03-28 16:50:26.969+08
1903309921276645378	0	系统用户	修改用户	2	com.wzkris.user.controller.SysUserController.edit()	POST	1	admin	/sys_user/edit	::1	内网IP	{"roleIds":[1902555457271816194],"gender":"0","postIds":[1],"deptId":1902914157417271297,"nickname":"111","userId":1902946091493138434,"username":"sa0077","status":"0"}	{"code":0,"message":"Success","timestamp":1742619421674}	0	\N	2025-03-22 12:57:01.674+08
1903309958945689602	0	系统用户	修改用户	2	com.wzkris.user.controller.SysUserController.edit()	POST	1	admin	/sys_user/edit	::1	内网IP	{"roleIds":[1902555457271816194],"gender":"0","deptId":1902914157417271297,"nickname":"111","userId":1902946091493138434,"username":"sa0077","status":"0"}	{"code":0,"message":"Success","timestamp":1742619430649}	0	\N	2025-03-22 12:57:10.649+08
1903311381812989954	0	系统用户	修改用户	2	com.wzkris.user.controller.SysUserController.edit()	POST	1	admin	/sys_user/edit	::1	内网IP	{"gender":"0","deptId":1902914157417271297,"nickname":"111","userId":1902946091493138434,"username":"sa0077","status":"0"}	{"code":0,"message":"Success","timestamp":1742619769896}	0	\N	2025-03-22 13:02:49.896+08
1903311579712835585	0	系统用户	修改用户	2	com.wzkris.user.controller.SysUserController.edit()	POST	1	admin	/sys_user/edit	::1	内网IP	{"roleIds":[1902555457271816194],"gender":"0","postIds":[1,4],"deptId":1902914157417271297,"nickname":"111","userId":1902946091493138434,"username":"sa0077","status":"0"}	{"code":0,"message":"Success","timestamp":1742619817079}	0	\N	2025-03-22 13:03:37.079+08
1903314262549057537	0	系统用户	修改用户	2	com.wzkris.user.controller.SysUserController.edit()	POST	1	admin	/sys_user/edit	192.168.0.112	内网IP	{"roleIds":[1902555457271816194],"gender":"0","postIds":[1,4],"deptId":1902916770657415170,"nickname":"111","userId":1902946091493138434,"username":"sa0077","status":"0"}	{"code":0,"message":"Success","timestamp":1742620456680}	0	\N	2025-03-22 13:14:16.681+08
1903327804782268417	0	系统用户	删除用户	3	com.wzkris.user.controller.SysUserController.remove()	POST	1	admin	/sys_user/remove	192.168.0.112	内网IP	[1902946091493138434]	{"code":0,"message":"Success","timestamp":1742623685396}	0	\N	2025-03-22 14:08:05.4+08
1903327838789685249	0	角色管理	删除角色	3	com.wzkris.user.controller.SysRoleController.remove()	POST	1	admin	/sys_role/remove	192.168.0.112	内网IP	[1902555457271816194]	{"code":0,"message":"Success","timestamp":1742623693555}	0	\N	2025-03-22 14:08:13.555+08
1903327954149822465	0	部门管理	删除部门	3	com.wzkris.user.controller.SysDeptController.remove()	POST	1	admin	/sys_dept/remove	192.168.0.112	内网IP	1902916722653605889	{"code":0,"message":"Success","timestamp":1742623721060}	0	\N	2025-03-22 14:08:41.06+08
1903327969651970049	0	部门管理	删除部门	3	com.wzkris.user.controller.SysDeptController.remove()	POST	1	admin	/sys_dept/remove	192.168.0.112	内网IP	1902916770657415170	{"code":0,"message":"Success","timestamp":1742623724743}	0	\N	2025-03-22 14:08:44.743+08
1903327982603980802	0	部门管理	删除部门	3	com.wzkris.user.controller.SysDeptController.remove()	POST	1	admin	/sys_dept/remove	192.168.0.112	内网IP	1902914157417271297	{"code":0,"message":"Success","timestamp":1742623727830}	0	\N	2025-03-22 14:08:47.83+08
1903327989256146945	0	部门管理	删除部门	3	com.wzkris.user.controller.SysDeptController.remove()	POST	1	admin	/sys_dept/remove	192.168.0.112	内网IP	1902916843768328193	{"code":0,"message":"Success","timestamp":1742623729420}	0	\N	2025-03-22 14:08:49.42+08
1903331334201004033	0	系统用户	新增用户	1	com.wzkris.user.controller.SysUserController.add()	POST	1	admin	/sys_user/add	192.168.0.112	内网IP	{"roleIds":[],"gender":"2","postIds":[],"username":"111111","status":"0"}	{"code":0,"message":"Success","timestamp":1742624526922}	0	\N	2025-03-22 14:22:06.922+08
1903331353880678402	0	系统用户	删除用户	3	com.wzkris.user.controller.SysUserController.remove()	POST	1	admin	/sys_user/remove	192.168.0.112	内网IP	[1903331334163238913]	{"code":0,"message":"Success","timestamp":1742624531605}	0	\N	2025-03-22 14:22:11.606+08
1903973504113156097	0	系统用户	状态修改	2	com.wzkris.user.controller.AppUserController.editStatus()	POST	1	admin	/app_user/edit_status	192.168.0.112	内网IP	{"id":1826896461245968384,"status":"0"}	{"code":0,"message":"Success","timestamp":1742777631966}	0	\N	2025-03-24 08:53:51.966+08
1903973538577752065	0	系统用户	状态修改	2	com.wzkris.user.controller.AppUserController.editStatus()	POST	1	admin	/app_user/edit_status	192.168.0.112	内网IP	{"id":1826896461245968384,"status":"0"}	{"code":0,"message":"Success","timestamp":1742777640367}	0	\N	2025-03-24 08:54:00.367+08
1903973558458753025	0	系统用户	状态修改	2	com.wzkris.user.controller.AppUserController.editStatus()	POST	1	admin	/app_user/edit_status	192.168.0.112	内网IP	{"id":1826896461245968384,"status":"0"}	{"code":0,"message":"Success","timestamp":1742777645108}	0	\N	2025-03-24 08:54:05.108+08
1903973568487333890	0	系统用户	状态修改	2	com.wzkris.user.controller.AppUserController.editStatus()	POST	1	admin	/app_user/edit_status	192.168.0.112	内网IP	{"id":1826896461245968384,"status":"1"}	{"code":0,"message":"Success","timestamp":1742777647486}	0	\N	2025-03-24 08:54:07.486+08
1903973580025864193	0	系统用户	状态修改	2	com.wzkris.user.controller.AppUserController.editStatus()	POST	1	admin	/app_user/edit_status	192.168.0.112	内网IP	{"id":1826896461245968384,"status":"0"}	{"code":0,"message":"Success","timestamp":1742777650253}	0	\N	2025-03-24 08:54:10.253+08
1903973723043241985	0	系统用户	状态修改	2	com.wzkris.user.controller.AppUserController.editStatus()	POST	1	admin	/app_user/edit_status	192.168.0.112	内网IP	{"id":1826896461245968384,"status":"1"}	{"code":0,"message":"Success","timestamp":1742777684335}	0	\N	2025-03-24 08:54:44.335+08
1903973879700496385	0	系统用户	状态修改	2	com.wzkris.user.controller.AppUserController.editStatus()	POST	1	admin	/app_user/edit_status	192.168.0.112	内网IP	{"id":1826896461245968384,"status":"0"}	{"code":0,"message":"Success","timestamp":1742777721701}	0	\N	2025-03-24 08:55:21.701+08
1903973903675138049	0	系统用户	状态修改	2	com.wzkris.user.controller.AppUserController.editStatus()	POST	1	admin	/app_user/edit_status	192.168.0.112	内网IP	{"id":1826896461245968384,"status":"1"}	{"code":0,"message":"Success","timestamp":1742777727417}	0	\N	2025-03-24 08:55:27.417+08
1905543009100505090	0	用户信息		2	com.wzkris.user.controller.SysUserProfileController.editInfo()	POST	1	admin	/user_profile	192.168.0.112	内网IP	{"gender":"1","nickname":"nick_admin"}	{"code":0,"message":"Success","timestamp":1743151831304}	0	\N	2025-03-28 16:50:31.304+08
1903974019630866434	0	系统用户	状态修改	2	com.wzkris.user.controller.AppUserController.editStatus()	POST	1	admin	/app_user/edit_status	192.168.0.112	内网IP	{"id":1826896461245968384,"status":"0"}	{"code":0,"message":"Success","timestamp":1742777755065}	0	\N	2025-03-24 08:55:55.065+08
1903974450461384706	0	系统用户	状态修改	2	com.wzkris.user.controller.AppUserController.editStatus()	POST	1	admin	/app_user/edit_status	192.168.0.112	内网IP	{"id":1826896461245968384,"status":"1"}	{"code":0,"message":"Success","timestamp":1742777857781}	0	\N	2025-03-24 08:57:37.781+08
1903974459336531969	0	系统用户	状态修改	2	com.wzkris.user.controller.AppUserController.editStatus()	POST	1	admin	/app_user/edit_status	192.168.0.112	内网IP	{"id":1826896461245968384,"status":"0"}	{"code":0,"message":"Success","timestamp":1742777859897}	0	\N	2025-03-24 08:57:39.897+08
1903975161932783617	0	系统用户	状态修改	2	com.wzkris.user.controller.AppUserController.editStatus()	POST	1	admin	/app_user/edit_status	192.168.0.112	内网IP	{"id":1826896461245968384,"status":"1"}	{"code":0,"message":"Success","timestamp":1742778027407}	0	\N	2025-03-24 09:00:27.407+08
1903975331944701954	0	系统用户	状态修改	2	com.wzkris.user.controller.AppUserController.editStatus()	POST	1	admin	/app_user/edit_status	192.168.0.112	内网IP	{"id":1826896461245968384,"status":"0"}	{"code":0,"message":"Success","timestamp":1742778067927}	0	\N	2025-03-24 09:01:07.927+08
1903975376454656002	0	系统用户	状态修改	2	com.wzkris.user.controller.AppUserController.editStatus()	POST	1	admin	/app_user/edit_status	192.168.0.112	内网IP	{"id":1826896461245968384,"status":"1"}	{"code":0,"message":"Success","timestamp":1742778078545}	0	\N	2025-03-24 09:01:18.545+08
1903975473485684737	0	系统用户	状态修改	2	com.wzkris.user.controller.AppUserController.editStatus()	POST	1	admin	/app_user/edit_status	192.168.0.112	内网IP	{"id":1826896461245968384,"status":"0"}	{"code":0,"message":"Success","timestamp":1742778101656}	0	\N	2025-03-24 09:01:41.656+08
1903977765253390337	0	系统用户	状态修改	2	com.wzkris.user.controller.AppUserController.editStatus()	POST	1	admin	/app_user/edit_status	192.168.0.112	内网IP	{"id":1826896461245968384,"status":"1"}	{"code":0,"message":"Success","timestamp":1742778648055}	0	\N	2025-03-24 09:10:48.055+08
1903980674661720066	0	系统用户	状态修改	2	com.wzkris.user.controller.AppUserController.editStatus()	POST	1	admin	/app_user/edit_status	192.168.0.112	内网IP	{"id":1826896461245968384,"status":"0"}	{"code":0,"message":"Success","timestamp":1742779341746}	0	\N	2025-03-24 09:22:21.746+08
1903980893528891394	0	系统用户	状态修改	2	com.wzkris.user.controller.AppUserController.editStatus()	POST	1	admin	/app_user/edit_status	192.168.0.112	内网IP	{"id":1826896461245968384,"status":"1"}	{"code":0,"message":"Success","timestamp":1742779393928}	0	\N	2025-03-24 09:23:13.928+08
1903982265087270914	0	系统用户	状态修改	2	com.wzkris.user.controller.SysUserController.editStatus()	POST	1	admin	/sys_user/edit_status	192.168.0.112	内网IP	{"id":1,"status":"1"}	\N	1	禁止访问超级管理员数据	2025-03-24 09:28:40.931+08
1903982428879036418	0	系统用户	状态修改	2	com.wzkris.user.controller.SysUserController.editStatus()	POST	1	admin	/sys_user/edit_status	192.168.0.112	内网IP	{"id":1,"status":"1"}	\N	1	禁止访问超级管理员数据	2025-03-24 09:29:19.984+08
1903983079096819714	0	角色管理	新增角色	1	com.wzkris.user.controller.SysRoleController.add()	POST	1	admin	/sys_role/add	192.168.0.112	内网IP	{"roleName":"111","dataScope":"1","status":"0","roleSort":0}	{"code":0,"message":"Success","timestamp":1742779915008}	0	\N	2025-03-24 09:31:55.008+08
1903983598737530882	0	系统用户	状态修改	2	com.wzkris.user.controller.SysRoleController.editStatus()	POST	1	admin	/sys_role/edit_status	192.168.0.112	内网IP	{"id":1903983079025520642,"status":"1"}	{"code":0,"message":"Success","timestamp":1742780038910}	0	\N	2025-03-24 09:33:58.91+08
1903983611853119490	0	系统用户	状态修改	2	com.wzkris.user.controller.SysRoleController.editStatus()	POST	1	admin	/sys_role/edit_status	192.168.0.112	内网IP	{"id":1903983079025520642,"status":"0"}	{"code":0,"message":"Success","timestamp":1742780042025}	0	\N	2025-03-24 09:34:02.025+08
1903984584868732929	0	角色管理	删除角色	3	com.wzkris.user.controller.SysRoleController.remove()	POST	1	admin	/sys_role/remove	192.168.0.112	内网IP	[1903983079025520642]	{"code":0,"message":"Success","timestamp":1742780274012}	0	\N	2025-03-24 09:37:54.012+08
1904001036447358977	0	系统用户	状态修改	2	com.wzkris.user.controller.OAuth2ClientController.editStatus()	POST	1	admin	/oauth2_client/edit_status	192.168.0.112	内网IP	{"id":1,"status":"1"}	{"code":0,"message":"Success","timestamp":1742784196343}	0	\N	2025-03-24 10:43:16.359+08
1904001046803095554	0	系统用户	状态修改	2	com.wzkris.user.controller.OAuth2ClientController.editStatus()	POST	1	admin	/oauth2_client/edit_status	192.168.0.112	内网IP	{"id":1,"status":"0"}	{"code":0,"message":"Success","timestamp":1742784198843}	0	\N	2025-03-24 10:43:18.843+08
1904009929412026369	0	OAuth2客户端管理	添加客户端	1	com.wzkris.user.controller.OAuth2ClientController.add()	POST	1	admin	/oauth2_client/add	192.168.0.112	内网IP	{"clientId":"wqeqwe","clientName":"2321","clientSecret":"{bcrypt}$2a$10$hVpm2KWOkxJVKZt4EcEKxehIa1/sALj83u4xDDaiRneV9oQuWL4ze","autoApprove":false,"status":"0"}	{"code":0,"message":"Success","timestamp":1742786316588}	0	\N	2025-03-24 11:18:36.589+08
1904010339933724674	0	OAuth2客户端管理	修改客户端	2	com.wzkris.user.controller.OAuth2ClientController.edit()	POST	1	admin	/oauth2_client/edit	192.168.0.112	内网IP	{"clientId":"wqeqwe","clientName":"2321","authorizationGrantTypes":[],"id":1904009928849944578,"scopes":[],"redirectUris":[],"autoApprove":false,"status":"0"}	{"code":0,"message":"Success","timestamp":1742786414498}	0	\N	2025-03-24 11:20:14.498+08
1904010529772118018	0	OAuth2客户端管理	修改客户端	2	com.wzkris.user.controller.OAuth2ClientController.edit()	POST	1	admin	/oauth2_client/edit	192.168.0.112	内网IP	{"clientId":"wqeqwe","clientName":"2321","authorizationGrantTypes":[],"id":1904009928849944578,"scopes":[],"redirectUris":[],"autoApprove":false,"status":"0"}	{"code":0,"message":"Success","timestamp":1742786459766}	0	\N	2025-03-24 11:20:59.766+08
1904010660089143298	0	OAuth2客户端管理	修改客户端	2	com.wzkris.user.controller.OAuth2ClientController.edit()	POST	1	admin	/oauth2_client/edit	192.168.0.112	内网IP	{"clientId":"wqeqwe","clientName":"2321","authorizationGrantTypes":["refresh_token","password"],"id":1904009928849944578,"scopes":[],"redirectUris":[],"autoApprove":false,"status":"0"}	{"code":0,"message":"Success","timestamp":1742786490829}	0	\N	2025-03-24 11:21:30.829+08
1904010687150792705	0	OAuth2客户端管理	修改客户端	2	com.wzkris.user.controller.OAuth2ClientController.edit()	POST	1	admin	/oauth2_client/edit	192.168.0.112	内网IP	{"clientId":"wqeqwe","clientName":"2321","authorizationGrantTypes":["refresh_token","password","sms","wechat","client_credentials","urn:ietf:params:oauth:grant-type:device_code"],"id":1904009928849944578,"scopes":[],"redirectUris":[],"autoApprove":false,"status":"0"}	{"code":0,"message":"Success","timestamp":1742786497281}	0	\N	2025-03-24 11:21:37.281+08
1904419091975385090	0	租户套餐	修改租户套餐状态	2	com.wzkris.user.controller.SysTenantPackageController.editStatus()	POST	1	admin	/sys_tenant/package/edit_status	192.168.0.112	内网IP	{"id":1773625804122202113,"status":"1"}	{"code":0,"message":"Success","timestamp":1742883868579}	0	\N	2025-03-25 14:24:28.579+08
1904010730142408705	0	OAuth2客户端管理	修改客户端	2	com.wzkris.user.controller.OAuth2ClientController.edit()	POST	1	admin	/oauth2_client/edit	192.168.0.112	内网IP	{"clientId":"wqeqwe","clientName":"2321","authorizationGrantTypes":["refresh_token","password","sms","wechat","client_credentials","urn:ietf:params:oauth:grant-type:device_code"],"id":1904009928849944578,"scopes":[],"redirectUris":[],"autoApprove":true,"status":"0"}	{"code":0,"message":"Success","timestamp":1742786507531}	0	\N	2025-03-24 11:21:47.531+08
1904029126309421057	0	OAuth2客户端管理	修改客户端	2	com.wzkris.user.controller.OAuth2ClientController.edit()	POST	1	admin	/oauth2_client/edit	192.168.0.112	内网IP	{"clientId":"wqeqwe","clientName":"2321","authorizationGrantTypes":["refresh_token","password","sms","wechat","client_credentials","urn:ietf:params:oauth:grant-type:device_code"],"id":1904009928849944578,"scopes":[],"redirectUris":[],"autoApprove":true,"status":"0"}	{"code":0,"message":"Success","timestamp":1742790893506}	0	\N	2025-03-24 12:34:53.506+08
1904029165601660930	0	OAuth2客户端管理	修改客户端	2	com.wzkris.user.controller.OAuth2ClientController.edit()	POST	1	admin	/oauth2_client/edit	192.168.0.112	内网IP	{"clientId":"wqeqq","clientName":"2321","authorizationGrantTypes":["refresh_token","password","sms","wechat","client_credentials","urn:ietf:params:oauth:grant-type:device_code"],"id":1904009928849944578,"scopes":[],"redirectUris":[],"autoApprove":true,"status":"0"}	{"code":0,"message":"Success","timestamp":1742790902887}	0	\N	2025-03-24 12:35:02.887+08
1904029250238521346	0	系统用户	状态修改	2	com.wzkris.user.controller.OAuth2ClientController.editStatus()	POST	1	admin	/oauth2_client/edit_status	192.168.0.112	内网IP	{"id":1904009928849944578,"status":"1"}	{"code":0,"message":"Success","timestamp":1742790923052}	0	\N	2025-03-24 12:35:23.052+08
1904029255980523521	0	系统用户	状态修改	2	com.wzkris.user.controller.OAuth2ClientController.editStatus()	POST	1	admin	/oauth2_client/edit_status	192.168.0.112	内网IP	{"id":1904009928849944578,"status":"0"}	{"code":0,"message":"Success","timestamp":1742790924435}	0	\N	2025-03-24 12:35:24.435+08
1904035021193977858	0	OAuth2客户端管理	修改客户端	2	com.wzkris.user.controller.OAuth2ClientController.edit()	POST	1	admin	/oauth2_client/edit	192.168.0.112	内网IP	{"clientId":"wqeqq","clientName":"2321","authorizationGrantTypes":["refresh_token","password","sms","wechat","client_credentials","urn:ietf:params:oauth:grant-type:device_code"],"id":1904009928849944578,"scopes":[],"redirectUris":["",""],"autoApprove":true,"status":"0"}	{"code":0,"message":"Success","timestamp":1742792298964}	0	\N	2025-03-24 12:58:18.964+08
1904039191326203906	0	OAuth2客户端管理	修改客户端	2	com.wzkris.user.controller.OAuth2ClientController.edit()	POST	1	admin	/oauth2_client/edit	192.168.0.112	内网IP	{"clientId":"wqeqq","clientName":"2321","authorizationGrantTypes":["refresh_token","password","sms","wechat","client_credentials","urn:ietf:params:oauth:grant-type:device_code"],"id":1904009928849944578,"scopes":[],"redirectUris":["恶趣味","委屈恶气","请问请问"],"autoApprove":true,"status":"0"}	{"code":0,"message":"Success","timestamp":1742793293217}	0	\N	2025-03-24 13:14:53.217+08
1904039904521461761	0	OAuth2客户端管理	修改客户端	2	com.wzkris.user.controller.OAuth2ClientController.edit()	POST	1	admin	/oauth2_client/edit	192.168.0.112	内网IP	{"clientId":"wqeqq","clientName":"2321","authorizationGrantTypes":["refresh_token","password","sms","wechat","client_credentials","urn:ietf:params:oauth:grant-type:device_code"],"id":1904009928849944578,"scopes":[],"redirectUris":["恶趣味","委屈恶气","请问请问",""],"autoApprove":true,"status":"0"}	{"code":0,"message":"Success","timestamp":1742793463246}	0	\N	2025-03-24 13:17:43.246+08
1904040187565678593	0	OAuth2客户端管理	修改客户端	2	com.wzkris.user.controller.OAuth2ClientController.edit()	POST	1	admin	/oauth2_client/edit	192.168.0.112	内网IP	{"clientId":"wqeqq","clientName":"2321","authorizationGrantTypes":["refresh_token","password","sms","wechat","client_credentials","urn:ietf:params:oauth:grant-type:device_code"],"id":1904009928849944578,"scopes":[],"redirectUris":["恶趣味","委屈恶气","请问请问","的答复"],"autoApprove":true,"status":"0"}	{"code":0,"message":"Success","timestamp":1742793530728}	0	\N	2025-03-24 13:18:50.728+08
1904045610461011969	0	OAuth2客户端管理	修改客户端	2	com.wzkris.user.controller.OAuth2ClientController.edit()	POST	1	admin	/oauth2_client/edit	192.168.0.112	内网IP	{"clientId":"wqeqq","clientName":"2321","authorizationGrantTypes":["refresh_token","password","sms","wechat","client_credentials","urn:ietf:params:oauth:grant-type:device_code"],"id":1904009928849944578,"scopes":[],"redirectUris":[],"autoApprove":true,"status":"0"}	{"code":0,"message":"Success","timestamp":1742794823648}	0	\N	2025-03-24 13:40:23.648+08
1904050686739062785	0	系统用户	状态修改	2	com.wzkris.user.controller.OAuth2ClientController.editStatus()	POST	1	admin	/oauth2_client/edit_status	192.168.0.112	内网IP	{"id":1904009928849944578,"status":"1"}	{"code":0,"message":"Success","timestamp":1742796033925}	0	\N	2025-03-24 14:00:33.926+08
1904050820365393922	0	OAuth2客户端管理	删除客户端	3	com.wzkris.user.controller.OAuth2ClientController.remove()	POST	1	admin	/oauth2_client/remove	192.168.0.112	内网IP	1904009928849944578	{"code":0,"message":"Success","timestamp":1742796065785}	0	\N	2025-03-24 14:01:05.785+08
1904051454602878977	0	OAuth2客户端管理	添加客户端	1	com.wzkris.user.controller.OAuth2ClientController.add()	POST	1	admin	/oauth2_client/add	192.168.0.112	内网IP	{"clientId":"请问请问的","clientName":"请问请问","clientSecret":"{bcrypt}$2a$10$9kmTnXuxZFh1TuefI1hgGO.T3JaaXkf4Jptv/XGSAxsvlg5ujaCxO","autoApprove":false,"status":"0"}	{"code":0,"message":"Success","timestamp":1742796217000}	0	\N	2025-03-24 14:03:37+08
1904052230934994945	0	OAuth2客户端管理	删除客户端	3	com.wzkris.user.controller.OAuth2ClientController.remove()	POST	1	admin	/oauth2_client/remove	192.168.0.112	内网IP	1904051454539919362	{"code":0,"message":"Success","timestamp":1742796402088}	0	\N	2025-03-24 14:06:42.088+08
1904052268331409410	0	OAuth2客户端管理	添加客户端	1	com.wzkris.user.controller.OAuth2ClientController.add()	POST	1	admin	/oauth2_client/add	192.168.0.112	内网IP	{"clientId":"为请问ww","clientName":"驱蚊器额外","clientSecret":"{bcrypt}$2a$10$62e2pcgiw9nJ4VTGE8/.FeaCoaeKNYSUJgOG./6RxayP/2y7z5MtW","autoApprove":false,"status":"0"}	{"code":0,"message":"Success","timestamp":1742796411008}	0	\N	2025-03-24 14:06:51.008+08
1904052516638400513	0	OAuth2客户端管理	修改客户端	2	com.wzkris.user.controller.OAuth2ClientController.edit()	POST	1	admin	/oauth2_client/edit	192.168.0.112	内网IP	{"clientId":"为请问ww","clientName":"驱蚊器额外","authorizationGrantTypes":[],"id":1904052268331364353,"scopes":[],"redirectUris":[],"autoApprove":false,"status":"0"}	{"code":0,"message":"Success","timestamp":1742796470193}	0	\N	2025-03-24 14:07:50.193+08
1904052871874977794	0	OAuth2客户端管理	删除客户端	3	com.wzkris.user.controller.OAuth2ClientController.remove()	POST	1	admin	/oauth2_client/remove	192.168.0.112	内网IP	1904052268331364353	{"code":0,"message":"Success","timestamp":1742796554910}	0	\N	2025-03-24 14:09:14.91+08
1904419099105701890	0	租户套餐	修改租户套餐状态	2	com.wzkris.user.controller.SysTenantPackageController.editStatus()	POST	1	admin	/sys_tenant/package/edit_status	192.168.0.112	内网IP	{"id":1773625804122202113,"status":"0"}	{"code":0,"message":"Success","timestamp":1742883870279}	0	\N	2025-03-25 14:24:30.279+08
1904053245289668610	0	OAuth2客户端管理	添加客户端	1	com.wzkris.user.controller.OAuth2ClientController.add()	POST	1	admin	/oauth2_client/add	192.168.0.112	内网IP	{"clientId":"wqeqeqwe","clientName":"weqweew","clientSecret":"{bcrypt}$2a$10$NSxSg.TX8v44gLI.KV3nnejfGdYoOLmzjpcPNQjvLEdWJozI7Lkfq","autoApprove":false,"status":"0"}	{"code":0,"message":"Success","timestamp":1742796643933}	0	\N	2025-03-24 14:10:43.933+08
1904054166677594114	0	OAuth2客户端管理	添加客户端	1	com.wzkris.user.controller.OAuth2ClientController.add()	POST	1	admin	/oauth2_client/add	192.168.0.112	内网IP	{"clientId":"qweqeww","clientName":"wqeqweq","clientSecret":"{bcrypt}$2a$10$Oyz258jNA5OC3CxhNoaTT.8BuqhilSxpuOcjK6pQTR79XZOapzufm","autoApprove":false,"status":"0"}	{"code":0,"message":"Success","timestamp":1742796863609}	0	\N	2025-03-24 14:14:23.609+08
1904054194712322049	0	OAuth2客户端管理	删除客户端	3	com.wzkris.user.controller.OAuth2ClientController.remove()	POST	1	admin	/oauth2_client/remove	192.168.0.112	内网IP	1904054166677549058	{"code":0,"message":"Success","timestamp":1742796870296}	0	\N	2025-03-24 14:14:30.296+08
1904054671252365314	0	OAuth2客户端管理	修改客户端	2	com.wzkris.user.controller.OAuth2ClientController.edit()	POST	1	admin	/oauth2_client/edit	192.168.0.112	内网IP	{"clientId":"wqeqeqwe","clientName":"weqweew","authorizationGrantTypes":[],"id":1904053245289623553,"scopes":[],"redirectUris":[],"autoApprove":false,"status":"0"}	{"code":0,"message":"Success","timestamp":1742796983908}	0	\N	2025-03-24 14:16:23.908+08
1904054789728870402	0	OAuth2客户端管理	修改客户端	2	com.wzkris.user.controller.OAuth2ClientController.edit()	POST	1	admin	/oauth2_client/edit	192.168.0.112	内网IP	{"clientId":"wqeqeqwe","clientName":"weqweew","authorizationGrantTypes":[],"id":1904053245289623553,"scopes":[],"redirectUris":[],"autoApprove":false,"status":"0"}	{"code":0,"message":"Success","timestamp":1742797012152}	0	\N	2025-03-24 14:16:52.152+08
1904054952048435202	0	OAuth2客户端管理	修改客户端	2	com.wzkris.user.controller.OAuth2ClientController.edit()	POST	1	admin	/oauth2_client/edit	192.168.0.112	内网IP	{"clientId":"wqeqeqwe","clientName":"weqweew","authorizationGrantTypes":[],"id":1904053245289623553,"scopes":[],"redirectUris":[],"autoApprove":false,"status":"0"}	{"code":0,"message":"Success","timestamp":1742797050855}	0	\N	2025-03-24 14:17:30.855+08
1904055528031232001	0	OAuth2客户端管理	删除客户端	3	com.wzkris.user.controller.OAuth2ClientController.remove()	POST	1	admin	/oauth2_client/remove	192.168.0.112	内网IP	1904053245289623553	{"code":0,"message":"Success","timestamp":1742797188177}	0	\N	2025-03-24 14:19:48.177+08
1904055620737933313	0	OAuth2客户端管理	添加客户端	1	com.wzkris.user.controller.OAuth2ClientController.add()	POST	1	admin	/oauth2_client/add	192.168.0.112	内网IP	{"clientId":"qwedqweqwe","clientName":"qeqawd","clientSecret":"{bcrypt}$2a$10$CetuduQJg8F2mCDFquI70.jP9PrPZSMz7h0zVDcfh4ZHfjjT1VCRW","autoApprove":false,"status":"0"}	{"code":0,"message":"Success","timestamp":1742797210266}	0	\N	2025-03-24 14:20:10.266+08
1904055651138248706	0	OAuth2客户端管理	修改客户端	2	com.wzkris.user.controller.OAuth2ClientController.edit()	POST	1	admin	/oauth2_client/edit	192.168.0.112	内网IP	{"clientId":"qwedqweqwe","clientName":"qeqawd","authorizationGrantTypes":[],"id":1904055620666585089,"scopes":[],"redirectUris":[],"autoApprove":false,"status":"0"}	{"code":0,"message":"Success","timestamp":1742797217531}	0	\N	2025-03-24 14:20:17.531+08
1904055663272370178	0	OAuth2客户端管理	删除客户端	3	com.wzkris.user.controller.OAuth2ClientController.remove()	POST	1	admin	/oauth2_client/remove	192.168.0.112	内网IP	1904055620666585089	{"code":0,"message":"Success","timestamp":1742797220422}	0	\N	2025-03-24 14:20:20.422+08
1904057487618449410	0	岗位管理	修改岗位	2	com.wzkris.user.controller.SysPostController.edit()	POST	1	admin	/sys_post/edit	192.168.0.112	内网IP	{"postSort":1,"postName":"执行总裁","postCode":"CEO","postId":1,"status":"0"}	{"code":0,"message":"Success","timestamp":1742797655383}	0	\N	2025-03-24 14:27:35.383+08
1904057507260375042	0	岗位管理	新增岗位	1	com.wzkris.user.controller.SysPostController.add()	POST	1	admin	/sys_post/add	192.168.0.112	内网IP	{"postSort":0,"postName":"asd","postCode":"sad","status":"0"}	{"code":0,"message":"Success","timestamp":1742797660065}	0	\N	2025-03-24 14:27:40.065+08
1904057517884547073	0	岗位管理	删除岗位	3	com.wzkris.user.controller.SysPostController.remove()	POST	1	admin	/sys_post/remove	192.168.0.112	内网IP	[1904057507260329985]	{"code":0,"message":"Success","timestamp":1742797662582}	0	\N	2025-03-24 14:27:42.582+08
1904060860627394562	0	角色管理	新增角色	1	com.wzkris.user.controller.SysRoleController.add()	POST	1	admin	/sys_role/add	192.168.0.112	内网IP	{"roleName":"021042","dataScope":"1","status":"0","roleSort":0}	{"code":0,"message":"Success","timestamp":1742798459570}	0	\N	2025-03-24 14:40:59.57+08
1904060870559506434	0	系统用户	状态修改	2	com.wzkris.user.controller.SysRoleController.editStatus()	POST	1	admin	/sys_role/edit_status	192.168.0.112	内网IP	{"id":1904060860556046337,"status":"1"}	{"code":0,"message":"Success","timestamp":1742798461938}	0	\N	2025-03-24 14:41:01.938+08
1904060877970841602	0	系统用户	状态修改	2	com.wzkris.user.controller.SysRoleController.editStatus()	POST	1	admin	/sys_role/edit_status	192.168.0.112	内网IP	{"id":1904060860556046337,"status":"0"}	{"code":0,"message":"Success","timestamp":1742798463705}	0	\N	2025-03-24 14:41:03.705+08
1904061674007797761	0	角色管理	修改角色	2	com.wzkris.user.controller.SysRoleController.edit()	POST	1	admin	/sys_role/edit	192.168.0.112	内网IP	{"isMenuDisplay":false,"roleId":1904060860556046337,"roleName":"021042","isDeptDisplay":false,"dataScope":"1","status":"0","roleSort":0}	{"code":0,"message":"Success","timestamp":1742798653495}	0	\N	2025-03-24 14:44:13.495+08
1904061737903824898	0	角色管理	修改角色	2	com.wzkris.user.controller.SysRoleController.edit()	POST	1	admin	/sys_role/edit	192.168.0.112	内网IP	{"isMenuDisplay":false,"roleId":1904060860556046337,"roleName":"021042","isDeptDisplay":false,"dataScope":"1","status":"0","roleSort":0}	{"code":0,"message":"Success","timestamp":1742798668730}	0	\N	2025-03-24 14:44:28.73+08
1904062172391776257	0	角色管理	修改角色	2	com.wzkris.user.controller.SysRoleController.edit()	POST	1	admin	/sys_role/edit	192.168.0.112	内网IP	{"isMenuDisplay":false,"roleId":1904060860556046337,"roleName":"021042","isDeptDisplay":false,"dataScope":"1","menuIds":[1,100,1060,1059,1058,1057,1056,103,1055,1054,1053,1052,1051,102,1050,1049,1048,1047,1046,104,151,1064,150,1062,1061,101,300,304,303,302,301],"status":"0","roleSort":0}	{"code":0,"message":"Success","timestamp":1742798772319}	0	\N	2025-03-24 14:46:12.319+08
1904065610311770113	0	角色管理	修改角色	2	com.wzkris.user.controller.SysRoleController.edit()	POST	1	admin	/sys_role/edit	192.168.0.112	内网IP	{"isMenuDisplay":false,"roleId":1904060860556046337,"roleName":"021042","isDeptDisplay":false,"deptIds":[],"dataScope":"1","menuIds":[1,100,1060,1059,1058,1057,1056,103,1055,1054,1053,1052,1051,102,1050,1049,1048,1047,1046,104,151,1064,150,1062,1061,101,300,304,303,302,301],"status":"0","roleSort":0}	{"code":0,"message":"Success","timestamp":1742799591987}	0	\N	2025-03-24 14:59:51.987+08
1905070221843734530	0	参数管理	添加参数	1	com.wzkris.system.controller.SysConfigController.add()	POST	1	admin	/sys_config/add	192.168.0.112	内网IP	{"configName":"eqwqwe","configKey":"qweqwe","configValue":"qwe","configType":"N"}	{"code":0,"message":"Success","timestamp":1743039110041}	0	\N	2025-03-27 09:31:50.041+08
1904066979072548865	0	角色管理	修改角色	2	com.wzkris.user.controller.SysRoleController.edit()	POST	1	admin	/sys_role/edit	192.168.0.112	内网IP	{"isMenuDisplay":false,"roleId":1904060860556046337,"roleName":"021042","isDeptDisplay":false,"deptIds":[],"dataScope":"1","menuIds":[1,100,1060,1059,1058,1057,1056,103,1055,1054,1053,1052,1051,102,1050,1049,1048,1047,1046,104,151,1064,150,1062,1061,101,300,304,303,302,301],"status":"0","roleSort":0}	{"code":0,"message":"Success","timestamp":1742799918320}	0	\N	2025-03-24 15:05:18.32+08
1904071001020014593	0	角色管理	修改角色	2	com.wzkris.user.controller.SysRoleController.edit()	POST	1	admin	/sys_role/edit	192.168.0.112	内网IP	{"isMenuDisplay":false,"roleId":1904060860556046337,"roleName":"021042","isDeptDisplay":false,"deptIds":[],"dataScope":"1","menuIds":[1,100,1060,1059,1058,1057,1056,103,1055,1054,1053,1052,1051,102,1050,1049,1048,1047,1046,104,151,1064,150,1062,1061,101,300,304,303,302,301],"status":"0","roleSort":0}	{"code":0,"message":"Success","timestamp":1742800877222}	0	\N	2025-03-24 15:21:17.223+08
1904071229810909186	0	角色管理	修改角色	2	com.wzkris.user.controller.SysRoleController.edit()	POST	1	admin	/sys_role/edit	192.168.0.112	内网IP	{"isMenuDisplay":false,"roleId":1904060860556046337,"roleName":"021042","isDeptDisplay":false,"deptIds":[],"dataScope":"1","menuIds":[1,100,1060,1059,1058,1057,1056,103,1055,1054,1053,1052,1051,102,1050,1049,1048,1047,1046,104,151,1064,150,1062,1061,101,300,304,303,302,301],"status":"0","roleSort":0}	{"code":0,"message":"Success","timestamp":1742800931781}	0	\N	2025-03-24 15:22:11.781+08
1904072116067344386	0	角色管理	修改角色	2	com.wzkris.user.controller.SysRoleController.edit()	POST	1	admin	/sys_role/edit	192.168.0.112	内网IP	{"isMenuDisplay":false,"roleId":1904060860556046337,"roleName":"021042","isDeptDisplay":false,"deptIds":[],"dataScope":"1","menuIds":[1,100,1060,1059,1058,1057,1056,103,1055,1054,1053,1052,1051,102,1050,1049,1048,1047,1046,104,151,1064,150,1062,1061,101,300,304,303,302,301],"status":"0","roleSort":0}	{"code":0,"message":"Success","timestamp":1742801143085}	0	\N	2025-03-24 15:25:43.085+08
1904072287895396353	0	角色管理	修改角色	2	com.wzkris.user.controller.SysRoleController.edit()	POST	1	admin	/sys_role/edit	192.168.0.112	内网IP	{"isMenuDisplay":false,"roleId":1904060860556046337,"roleName":"021042","isDeptDisplay":false,"deptIds":[],"dataScope":"1","menuIds":[1,100,1060,1059,1058,1057,1056,103,1055,1054,1053,1052,1051,102,1050,1049,1048,1047,1046,104,151,1064,150,1062,1061,101,300,304,303,302,301],"status":"0","roleSort":0}	{"code":0,"message":"Success","timestamp":1742801184040}	0	\N	2025-03-24 15:26:24.04+08
1904072311555465218	0	角色管理	修改角色	2	com.wzkris.user.controller.SysRoleController.edit()	POST	1	admin	/sys_role/edit	192.168.0.112	内网IP	{"isMenuDisplay":false,"roleId":1904060860556046337,"roleName":"021042","isDeptDisplay":false,"deptIds":[],"dataScope":"1","menuIds":[1,100,1060,1059,1058,1057,1056,103,1055,1054,1053,1052,1051,102,1050,1049,1048,1047,1046,104,151,1064,150,1062,1061,101,300,304,303,302,301],"status":"0","roleSort":0}	{"code":0,"message":"Success","timestamp":1742801189681}	0	\N	2025-03-24 15:26:29.681+08
1904073405669023746	0	角色管理	修改角色	2	com.wzkris.user.controller.SysRoleController.edit()	POST	1	admin	/sys_role/edit	192.168.0.112	内网IP	{"isMenuDisplay":false,"roleId":1904060860556046337,"roleName":"021042","isDeptDisplay":false,"deptIds":[],"dataScope":"1","menuIds":[1,100,1060,1059,1058,1057,1056,103,1055,1054,1053,1052,1051,102,1050,1049,1048,1047,1046,104,151,1064,150,1062,1061,101,300,304,303,302,301],"status":"0","roleSort":0}	{"code":0,"message":"Success","timestamp":1742801450539}	0	\N	2025-03-24 15:30:50.539+08
1904073636972306433	0	角色管理	修改角色	2	com.wzkris.user.controller.SysRoleController.edit()	POST	1	admin	/sys_role/edit	192.168.0.112	内网IP	{"isMenuDisplay":false,"roleId":1904060860556046337,"roleName":"021042","isDeptDisplay":false,"deptIds":[],"dataScope":"1","menuIds":[1,100,1060,1059,1058,1057,1056,103,1055,1054,1053,1052,1051,102,1050,1049,1048,1047,1046,104,151,1064,150,1062,1061,101,300,304,303,302,301],"status":"0","roleSort":0}	{"code":0,"message":"Success","timestamp":1742801505696}	0	\N	2025-03-24 15:31:45.696+08
1904073720166326274	0	角色管理	修改角色	2	com.wzkris.user.controller.SysRoleController.edit()	POST	1	admin	/sys_role/edit	192.168.0.112	内网IP	{"isMenuDisplay":false,"roleId":1904060860556046337,"roleName":"021042","isDeptDisplay":false,"deptIds":[],"dataScope":"1","menuIds":[1,100,1060,1059,1058,1057,1056,103,1055,1054,1053,1052,1051,102,1050,1049,1048,1047,1046,104,151,1064,150,1062,1061,101,300,304,303,302,301],"status":"0","roleSort":0}	{"code":0,"message":"Success","timestamp":1742801525524}	0	\N	2025-03-24 15:32:05.524+08
1904073848151318529	0	角色管理	修改角色	2	com.wzkris.user.controller.SysRoleController.edit()	POST	1	admin	/sys_role/edit	192.168.0.112	内网IP	{"isMenuDisplay":false,"roleId":1904060860556046337,"roleName":"021042","isDeptDisplay":false,"deptIds":[],"dataScope":"1","menuIds":[1,100,1060,1059,1058,1057,1056,103,1055,1054,1053,1052,1051,102,1050,1049,1048,1047,1046,104,151,1064,150,1062,1061,101,300,304,303,302,301],"status":"0","roleSort":0}	{"code":0,"message":"Success","timestamp":1742801556037}	0	\N	2025-03-24 15:32:36.037+08
1904074588525666305	0	角色管理	修改角色	2	com.wzkris.user.controller.SysRoleController.edit()	POST	1	admin	/sys_role/edit	192.168.0.112	内网IP	{"isMenuDisplay":false,"roleId":1904060860556046337,"roleName":"021042","isDeptDisplay":false,"deptIds":[],"dataScope":"1","menuIds":[1060],"status":"0","roleSort":0}	{"code":0,"message":"Success","timestamp":1742801732551}	0	\N	2025-03-24 15:35:32.551+08
1904075170795724802	0	角色管理	修改角色	2	com.wzkris.user.controller.SysRoleController.edit()	POST	1	admin	/sys_role/edit	192.168.0.112	内网IP	{"isMenuDisplay":false,"roleId":1904060860556046337,"roleName":"021042","isDeptDisplay":false,"deptIds":[],"dataScope":"2","menuIds":[],"status":"0","roleSort":0}	{"code":0,"message":"Success","timestamp":1742801871380}	0	\N	2025-03-24 15:37:51.381+08
1904075204455014401	0	部门管理	新增部门	1	com.wzkris.user.controller.SysDeptController.add()	POST	1	admin	/sys_dept/add	192.168.0.112	内网IP	{"deptSort":0,"deptName":"请问请问","status":"0"}	{"code":0,"message":"Success","timestamp":1742801879389}	0	\N	2025-03-24 15:37:59.389+08
1904075222335332354	0	部门管理	新增部门	1	com.wzkris.user.controller.SysDeptController.add()	POST	1	admin	/sys_dept/add	192.168.0.112	内网IP	{"deptSort":0,"deptName":"请问请问","parentId":1904075204320751617,"status":"0"}	{"code":0,"message":"Success","timestamp":1742801883665}	0	\N	2025-03-24 15:38:03.665+08
1904075235421560834	0	部门管理	新增部门	1	com.wzkris.user.controller.SysDeptController.add()	POST	1	admin	/sys_dept/add	192.168.0.112	内网IP	{"deptSort":0,"deptName":"请问请问","status":"0"}	{"code":0,"message":"Success","timestamp":1742801886792}	0	\N	2025-03-24 15:38:06.792+08
1904075257622011906	0	部门管理	修改部门	2	com.wzkris.user.controller.SysDeptController.edit()	POST	1	admin	/sys_dept/edit	192.168.0.112	内网IP	{"deptSort":0,"deptName":"fvfg","deptId":1904075235413127170,"ancestors":[],"status":"0"}	{"code":0,"message":"Success","timestamp":1742801892081}	0	\N	2025-03-24 15:38:12.081+08
1905070234569252866	0	参数管理	删除参数	3	com.wzkris.system.controller.SysConfigController.remove()	POST	1	admin	/sys_config/remove	192.168.0.112	内网IP	[1905070221843734529]	{"code":0,"message":"Success","timestamp":1743039113079}	0	\N	2025-03-27 09:31:53.079+08
1904076857493135362	0	角色管理	修改角色	2	com.wzkris.user.controller.SysRoleController.edit()	POST	1	admin	/sys_role/edit	192.168.0.112	内网IP	{"isMenuDisplay":false,"roleId":1904060860556046337,"roleName":"021042","isDeptDisplay":false,"deptIds":[1904075204320751617,1904075222259789826],"dataScope":"2","menuIds":[],"status":"0","roleSort":0}	{"code":0,"message":"Success","timestamp":1742802273470}	0	\N	2025-03-24 15:44:33.47+08
1904076880624721922	0	角色管理	修改角色	2	com.wzkris.user.controller.SysRoleController.edit()	POST	1	admin	/sys_role/edit	192.168.0.112	内网IP	{"isMenuDisplay":false,"roleId":1904060860556046337,"roleName":"021042","isDeptDisplay":false,"deptIds":[1904075204320751617,1904075222259789826],"dataScope":"2","menuIds":[1,100,1060,1059,1058,1057,1056,103,1055,1054,1053,1052,1051,102,1050,1049,1048,1047,1046,104,151,1064,150,1062,1061,101,300,304,303,302,301],"status":"0","roleSort":0}	{"code":0,"message":"Success","timestamp":1742802279036}	0	\N	2025-03-24 15:44:39.036+08
1904076892792397826	0	角色管理	修改角色	2	com.wzkris.user.controller.SysRoleController.edit()	POST	1	admin	/sys_role/edit	192.168.0.112	内网IP	{"isMenuDisplay":false,"roleId":1904060860556046337,"roleName":"021042","isDeptDisplay":false,"deptIds":[1904075204320751617,1904075222259789826],"dataScope":"2","menuIds":[1,100,1060,1059,1058,1057,1056,103,1055,1054,1053,1052,1051,102,1050,1049,1048,1047,1046,104,151,1064,150,1062,1061,101,300,304,303,302,301],"status":"0","roleSort":0}	{"code":0,"message":"Success","timestamp":1742802281921}	0	\N	2025-03-24 15:44:41.921+08
1904077739802730497	0	角色管理	修改角色	2	com.wzkris.user.controller.SysRoleController.edit()	POST	1	admin	/sys_role/edit	192.168.0.112	内网IP	{"isMenuDisplay":false,"roleId":1904060860556046337,"roleName":"021042","isDeptDisplay":false,"deptIds":[1904075204320751617,1904075222259789826],"dataScope":"2","menuIds":[1,100,1060,1059,1058,1057,1056,103,1055,1054,1053,1052,1051,102,1050,1049,1048,1047,1046,104,151,1064,150,1062,1061,101,300,304,303,302,301],"status":"0","roleSort":0}	{"code":0,"message":"Success","timestamp":1742802483879}	0	\N	2025-03-24 15:48:03.879+08
1904077972506910721	0	角色管理	修改角色	2	com.wzkris.user.controller.SysRoleController.edit()	POST	1	admin	/sys_role/edit	192.168.0.112	内网IP	{"isMenuDisplay":false,"roleId":1904060860556046337,"roleName":"021042","isDeptDisplay":false,"deptIds":[1904075204320751617,1904075222259789826],"dataScope":"2","menuIds":[1,100,1060,1059,1058,1057,1056,103,1055,1054,1053,1052,1051,102,1050,1049,1048,1047,1046,104,151,1064,150,1062,1061,101,300,304,303,302,301],"status":"0","roleSort":0}	{"code":0,"message":"Success","timestamp":1742802539360}	0	\N	2025-03-24 15:48:59.36+08
1904078778140434434	0	角色管理	修改角色	2	com.wzkris.user.controller.SysRoleController.edit()	POST	1	admin	/sys_role/edit	192.168.0.112	内网IP	{"isMenuDisplay":false,"roleId":1904060860556046337,"roleName":"021042","isDeptDisplay":false,"deptIds":[1904075204320751617,1904075222259789826],"dataScope":"2","menuIds":[],"status":"0","roleSort":0}	{"code":0,"message":"Success","timestamp":1742802731444}	0	\N	2025-03-24 15:52:11.444+08
1904082073693167618	0	角色管理	修改角色	2	com.wzkris.user.controller.SysRoleController.edit()	POST	1	admin	/sys_role/edit	192.168.0.112	内网IP	{"isMenuDisplay":false,"roleId":1904060860556046337,"roleName":"021042","isDeptDisplay":false,"deptIds":[],"dataScope":"2","menuIds":[],"status":"0","roleSort":0}	{"code":0,"message":"Success","timestamp":1742803517160}	0	\N	2025-03-24 16:05:17.16+08
1904328040505815042	0	系统用户	状态修改	2	com.wzkris.user.controller.AppUserController.editStatus()	POST	1	admin	/app_user/edit_status	192.168.0.112	内网IP	{"id":1826896461245968384,"status":"0"}	{"code":0,"message":"Success","timestamp":1742862160171}	0	\N	2025-03-25 08:22:40.171+08
1904363004341399553	0	租户管理	新增租户	1	com.wzkris.user.controller.SysTenantController.add()	POST	1	admin	/sys_tenant/add	192.168.0.112	内网IP	{"tenantType":"0","accountLimit":5,"roleLimit":5,"tenantName":"qewqwe","deptLimit":5,"postLimit":5,"status":"0","username":"wqeqwe"}	\N	1	\r\n### Error updating database.  Cause: org.postgresql.util.PSQLException: 错误: null value in column "package_id" of relation "sys_tenant" violates not-null constraint\n  详细：失败, 行包含(1904363002873475074, 1904363002873475072, 0, null, qewqwe, {bcrypt}$2a$10$/CeUVR7p/s0YEYSkZMrgfu2gxgKUaFa/j7aWTyT/oo/eweP8x..., 0, null, null, null, null, 5, 5, 5, 5, 1, 2025-03-25 10:41:35.911+08, 1, 2025-03-25 10:41:35.911+08).\r\n### The error may exist in com/wzkris/user/mapper/SysTenantMapper.java (best guess)\r\n### The error may involve com.wzkris.user.mapper.SysTenantMapper.insert-Inline\r\n### The error occurred while setting parameters\r\n### SQL: INSERT INTO biz_sys.sys_tenant  ( tenant_id, administrator, tenant_type,  tenant_name, oper_pwd, status,     account_limit, role_limit, post_limit, dept_limit, create_at, creator_id, update_at, updater_id )  VALUES (  ?, ?, ?,  ?, ?, ?,     ?, ?, ?, ?, ?, ?, ?, ?  )\r\n### Cause: org.postgresql.util.PSQLException: 错误: null value in column "package_id" of relation "sys_tenant" violates not-null constraint\n  详细：失败, 行包含(1904363002873475074, 1904363002873475072, 0, null, qewqwe, {bcrypt}$2a$10$/CeUVR7p/s0YEYSkZMrgfu2gxgKUaFa/j7aWTyT/oo/eweP8x..., 0, null, null, null, null, 5, 5, 5, 5, 1, 2025-03-25 10:41:35.911+08, 1, 2025-03-25 10:41:35.911+08).\n; 错误: null value in column "package_id" of relation "sys_tenant" violates not-null constraint\n  详细：失败, 行包含(1904363002873475074, 1904363002873475072, 0, null, qewqwe, {bcrypt}$2a$10$/CeUVR7p/s0YEYSkZMrgfu2gxgKUaFa/j7aWTyT/oo/eweP8x..., 0, null, null, null, null, 5, 5, 5, 5, 1, 2025-03-25 10:41:35.911+08, 1, 2025-03-25 10:41:35.911+08).	2025-03-25 10:41:36.078+08
1904363030278975490	0	租户管理	新增租户	1	com.wzkris.user.controller.SysTenantController.add()	POST	1	admin	/sys_tenant/add	192.168.0.112	内网IP	{"tenantType":"0","accountLimit":5,"roleLimit":5,"tenantName":"qewqwe","deptLimit":5,"postLimit":5,"status":"0","username":"wqeqwe"}	\N	1	\r\n### Error updating database.  Cause: org.postgresql.util.PSQLException: 错误: null value in column "package_id" of relation "sys_tenant" violates not-null constraint\n  详细：失败, 行包含(1904363030279057409, 1904363030279057408, 0, null, qewqwe, {bcrypt}$2a$10$Gw4ygjHS.vioCi6ueOx33uhZOzUr2IFUbiKc89/IJ2YyBcnru..., 0, null, null, null, null, 5, 5, 5, 5, 1, 2025-03-25 10:41:42.429+08, 1, 2025-03-25 10:41:42.429+08).\r\n### The error may exist in com/wzkris/user/mapper/SysTenantMapper.java (best guess)\r\n### The error may involve com.wzkris.user.mapper.SysTenantMapper.insert-Inline\r\n### The error occurred while setting parameters\r\n### SQL: INSERT INTO biz_sys.sys_tenant  ( tenant_id, administrator, tenant_type,  tenant_name, oper_pwd, status,     account_limit, role_limit, post_limit, dept_limit, create_at, creator_id, update_at, updater_id )  VALUES (  ?, ?, ?,  ?, ?, ?,     ?, ?, ?, ?, ?, ?, ?, ?  )\r\n### Cause: org.postgresql.util.PSQLException: 错误: null value in column "package_id" of relation "sys_tenant" violates not-null constraint\n  详细：失败, 行包含(1904363030279057409, 1904363030279057408, 0, null, qewqwe, {bcrypt}$2a$10$Gw4ygjHS.vioCi6ueOx33uhZOzUr2IFUbiKc89/IJ2YyBcnru..., 0, null, null, null, null, 5, 5, 5, 5, 1, 2025-03-25 10:41:42.429+08, 1, 2025-03-25 10:41:42.429+08).\n; 错误: null value in column "package_id" of relation "sys_tenant" violates not-null constraint\n  详细：失败, 行包含(1904363030279057409, 1904363030279057408, 0, null, qewqwe, {bcrypt}$2a$10$Gw4ygjHS.vioCi6ueOx33uhZOzUr2IFUbiKc89/IJ2YyBcnru..., 0, null, null, null, null, 5, 5, 5, 5, 1, 2025-03-25 10:41:42.429+08, 1, 2025-03-25 10:41:42.429+08).	2025-03-25 10:41:42.429+08
1904367387745595394	0	租户管理	新增租户	1	com.wzkris.user.controller.SysTenantController.add()	POST	1	admin	/sys_tenant/add	192.168.0.112	内网IP	{"tenantType":"0","accountLimit":5,"roleLimit":5,"tenantName":"adasd","packageId":1773625804122202113,"deptLimit":5,"postLimit":5,"status":"0","username":"asdasd"}	\N	1	\r\n### Error updating database.  Cause: org.postgresql.util.PSQLException: 错误: null value in column "expire_time" of relation "sys_tenant" violates not-null constraint\n  详细：失败, 行包含(1904367386755821570, 1904367386692907008, 0, null, adasd, {bcrypt}$2a$10$LnbnawDLbVs.onf/qY/CMO/QiqbBRin6J8LkJTktvHY1ion6b..., 0, null, null, 1773625804122202113, null, 5, 5, 5, 5, 1, 2025-03-25 10:59:01.094+08, 1, 2025-03-25 10:59:01.094+08).\r\n### The error may exist in com/wzkris/user/mapper/SysTenantMapper.java (best guess)\r\n### The error may involve com.wzkris.user.mapper.SysTenantMapper.insert-Inline\r\n### The error occurred while setting parameters\r\n### SQL: INSERT INTO biz_sys.sys_tenant  ( tenant_id, administrator, tenant_type,  tenant_name, oper_pwd, status,   package_id,  account_limit, role_limit, post_limit, dept_limit, create_at, creator_id, update_at, updater_id )  VALUES (  ?, ?, ?,  ?, ?, ?,   ?,  ?, ?, ?, ?, ?, ?, ?, ?  )\r\n### Cause: org.postgresql.util.PSQLException: 错误: null value in column "expire_time" of relation "sys_tenant" violates not-null constraint\n  详细：失败, 行包含(1904367386755821570, 1904367386692907008, 0, null, adasd, {bcrypt}$2a$10$LnbnawDLbVs.onf/qY/CMO/QiqbBRin6J8LkJTktvHY1ion6b..., 0, null, null, 1773625804122202113, null, 5, 5, 5, 5, 1, 2025-03-25 10:59:01.094+08, 1, 2025-03-25 10:59:01.094+08).\n; 错误: null value in column "expire_time" of relation "sys_tenant" violates not-null constraint\n  详细：失败, 行包含(1904367386755821570, 1904367386692907008, 0, null, adasd, {bcrypt}$2a$10$LnbnawDLbVs.onf/qY/CMO/QiqbBRin6J8LkJTktvHY1ion6b..., 0, null, null, 1773625804122202113, null, 5, 5, 5, 5, 1, 2025-03-25 10:59:01.094+08, 1, 2025-03-25 10:59:01.094+08).	2025-03-25 10:59:01.283+08
1904367431852896258	0	租户管理	新增租户	1	com.wzkris.user.controller.SysTenantController.add()	POST	1	admin	/sys_tenant/add	192.168.0.112	内网IP	{"tenantType":"0","accountLimit":5,"roleLimit":5,"tenantName":"adasd","packageId":1773625804122202113,"deptLimit":5,"postLimit":5,"status":"0","username":"asdasd"}	\N	1	\r\n### Error updating database.  Cause: org.postgresql.util.PSQLException: 错误: null value in column "expire_time" of relation "sys_tenant" violates not-null constraint\n  详细：失败, 行包含(1904367431836200962, 1904367431844589568, 0, null, adasd, {bcrypt}$2a$10$gLLUnxOXN3GRMArRAXgOweLwwyd5u.FCSMqcOryDykdNwCUQb..., 0, null, null, 1773625804122202113, null, 5, 5, 5, 5, 1, 2025-03-25 10:59:11.845+08, 1, 2025-03-25 10:59:11.845+08).\r\n### The error may exist in com/wzkris/user/mapper/SysTenantMapper.java (best guess)\r\n### The error may involve com.wzkris.user.mapper.SysTenantMapper.insert-Inline\r\n### The error occurred while setting parameters\r\n### SQL: INSERT INTO biz_sys.sys_tenant  ( tenant_id, administrator, tenant_type,  tenant_name, oper_pwd, status,   package_id,  account_limit, role_limit, post_limit, dept_limit, create_at, creator_id, update_at, updater_id )  VALUES (  ?, ?, ?,  ?, ?, ?,   ?,  ?, ?, ?, ?, ?, ?, ?, ?  )\r\n### Cause: org.postgresql.util.PSQLException: 错误: null value in column "expire_time" of relation "sys_tenant" violates not-null constraint\n  详细：失败, 行包含(1904367431836200962, 1904367431844589568, 0, null, adasd, {bcrypt}$2a$10$gLLUnxOXN3GRMArRAXgOweLwwyd5u.FCSMqcOryDykdNwCUQb..., 0, null, null, 1773625804122202113, null, 5, 5, 5, 5, 1, 2025-03-25 10:59:11.845+08, 1, 2025-03-25 10:59:11.845+08).\n; 错误: null value in column "expire_time" of relation "sys_tenant" violates not-null constraint\n  详细：失败, 行包含(1904367431836200962, 1904367431844589568, 0, null, adasd, {bcrypt}$2a$10$gLLUnxOXN3GRMArRAXgOweLwwyd5u.FCSMqcOryDykdNwCUQb..., 0, null, null, 1773625804122202113, null, 5, 5, 5, 5, 1, 2025-03-25 10:59:11.845+08, 1, 2025-03-25 10:59:11.845+08).	2025-03-25 10:59:11.846+08
1904369067061030914	0	租户管理	新增租户	1	com.wzkris.user.controller.SysTenantController.add()	POST	1	admin	/sys_tenant/add	192.168.0.112	内网IP	{"tenantType":"0","expireTime":1740758400000,"accountLimit":5,"roleLimit":5,"tenantName":"asdsda","packageId":1773625804122202113,"deptLimit":5,"postLimit":5,"status":"0","username":"sadasd"}	{"code":0,"message":"Success","timestamp":1742871941576}	0	\N	2025-03-25 11:05:41.576+08
1904369516723974146	0	租户管理	修改租户状态	2	com.wzkris.user.controller.SysTenantController.editStatus()	POST	1	admin	/sys_tenant/edit_status	192.168.0.112	内网IP	{"id":1904369066113200130,"status":"1"}	{"code":0,"message":"Success","timestamp":1742872048916}	0	\N	2025-03-25 11:07:28.916+08
1904398342766080002	0	租户管理	修改租户状态	2	com.wzkris.user.controller.SysTenantController.editStatus()	POST	1	admin	/sys_tenant/edit_status	192.168.0.112	内网IP	{"id":1904369066113200130,"status":"0"}	{"code":0,"message":"Success","timestamp":1742878921501}	0	\N	2025-03-25 13:02:01.501+08
1904399486590529537	0	租户管理	删除租户	3	com.wzkris.user.controller.SysTenantController.remove()	POST	1	admin	/sys_tenant/remove	192.168.0.112	内网IP	[1904369066113200130]	{"code":0,"message":"Success","timestamp":1742879194274}	0	\N	2025-03-25 13:06:34.274+08
1904403498089984002	0	租户管理	新增租户	1	com.wzkris.user.controller.SysTenantController.add()	POST	1	admin	/sys_tenant/add	192.168.0.112	内网IP	{"tenantType":"0","accountLimit":5,"roleLimit":5,"tenantName":"asdasd","packageId":1773625804122202113,"deptLimit":5,"postLimit":5,"status":"0","username":"asdasd"}	\N	1	\r\n### Error updating database.  Cause: org.postgresql.util.PSQLException: 错误: null value in column "expire_time" of relation "sys_tenant" violates not-null constraint\n  详细：失败, 行包含(1904403497184075778, 1904403497108578304, 0, null, asdasd, {bcrypt}$2a$10$cGMamtmmfQKFeKVt8ExFPeiMtq2AxLTpQC4x6DCPfX2hrbwdL..., 0, null, null, 1773625804122202113, null, 5, 5, 5, 5, 1, 2025-03-25 13:22:30.491+08, 1, 2025-03-25 13:22:30.491+08).\r\n### The error may exist in com/wzkris/user/mapper/SysTenantMapper.java (best guess)\r\n### The error may involve com.wzkris.user.mapper.SysTenantMapper.insert-Inline\r\n### The error occurred while setting parameters\r\n### SQL: INSERT INTO biz_sys.sys_tenant  ( tenant_id, administrator, tenant_type,  tenant_name, oper_pwd, status,   package_id,  account_limit, role_limit, post_limit, dept_limit, create_at, creator_id, update_at, updater_id )  VALUES (  ?, ?, ?,  ?, ?, ?,   ?,  ?, ?, ?, ?, ?, ?, ?, ?  )\r\n### Cause: org.postgresql.util.PSQLException: 错误: null value in column "expire_time" of relation "sys_tenant" violates not-null constraint\n  详细：失败, 行包含(1904403497184075778, 1904403497108578304, 0, null, asdasd, {bcrypt}$2a$10$cGMamtmmfQKFeKVt8ExFPeiMtq2AxLTpQC4x6DCPfX2hrbwdL..., 0, null, null, 1773625804122202113, null, 5, 5, 5, 5, 1, 2025-03-25 13:22:30.491+08, 1, 2025-03-25 13:22:30.491+08).\n; 错误: null value in column "expire_time" of relation "sys_tenant" violates not-null constraint\n  详细：失败, 行包含(1904403497184075778, 1904403497108578304, 0, null, asdasd, {bcrypt}$2a$10$cGMamtmmfQKFeKVt8ExFPeiMtq2AxLTpQC4x6DCPfX2hrbwdL..., 0, null, null, 1773625804122202113, null, 5, 5, 5, 5, 1, 2025-03-25 13:22:30.491+08, 1, 2025-03-25 13:22:30.491+08).	2025-03-25 13:22:30.674+08
1904406772750888961	0	租户管理	新增租户	1	com.wzkris.user.controller.SysTenantController.add()	POST	1	admin	/sys_tenant/add	192.168.0.112	内网IP	{"tenantType":"0","expireTime":1740758400000,"accountLimit":5,"roleLimit":5,"tenantName":"sadasdas","packageId":1773625804122202113,"deptLimit":5,"postLimit":5,"status":"0","username":"dsadasd"}	{"code":0,"message":"Success","timestamp":1742880931413}	0	\N	2025-03-25 13:35:31.413+08
1904407339376193538	0	角色管理	修改角色	2	com.wzkris.user.controller.SysRoleController.edit()	POST	1	admin	/sys_role/edit	192.168.0.112	内网IP	{"isMenuDisplay":false,"roleId":1904060860556046337,"roleName":"021042","isDeptDisplay":false,"deptIds":[],"dataScope":"2","menuIds":[1,100,1060,1059,1058,1057,1056,103,1055,1054,1053,1052,1051,102,1050,1049,1048,1047,1046,104,151,1064,150,1062,1061,101,300,304,303,302,301],"status":"0","roleSort":0}	{"code":0,"message":"Success","timestamp":1742881066508}	0	\N	2025-03-25 13:37:46.508+08
1904413795727060994	0	租户管理	修改租户	2	com.wzkris.user.controller.SysTenantController.edit()	POST	1	admin	/sys_tenant/edit	::1	内网IP	{"tenantType":"0","expireTime":1740758400000,"accountLimit":5,"roleLimit":5,"tenantName":"sadasdas","tenantId":1904406772188934146,"packageId":1773625804122202113,"deptLimit":5,"postLimit":5,"status":"0"}	{"code":0,"message":"Success","timestamp":1742882605838}	0	\N	2025-03-25 14:03:25.838+08
1904415725874458625	0	租户管理	修改租户	2	com.wzkris.user.controller.SysTenantController.edit()	POST	1	admin	/sys_tenant/edit	192.168.0.112	内网IP	{"tenantType":"0","expireTime":1740758400000,"accountLimit":7,"roleLimit":5,"tenantName":"sadasdas","tenantId":1904406772188934146,"packageId":1773625804122202113,"deptLimit":5,"postLimit":5,"status":"0","username":""}	{"code":0,"message":"Success","timestamp":1742883066005}	0	\N	2025-03-25 14:11:06.005+08
1904415740416110594	0	租户管理	修改租户	2	com.wzkris.user.controller.SysTenantController.edit()	POST	1	admin	/sys_tenant/edit	192.168.0.112	内网IP	{"tenantType":"0","expireTime":1740758400000,"accountLimit":5,"roleLimit":5,"tenantName":"sadasdas","tenantId":1904406772188934146,"packageId":1773625804122202113,"deptLimit":5,"postLimit":5,"status":"0","username":""}	{"code":0,"message":"Success","timestamp":1742883069492}	0	\N	2025-03-25 14:11:09.492+08
1904418679687884801	0	租户套餐	修改租户套餐状态	2	com.wzkris.user.controller.SysTenantPackageController.editStatus()	POST	1	admin	/sys_tenant/package/edit_status	192.168.0.112	内网IP	{"id":1773625804122202113,"status":"1"}	{"code":0,"message":"Success","timestamp":1742883770282}	0	\N	2025-03-25 14:22:50.282+08
1904418691641651201	0	租户套餐	修改租户套餐状态	2	com.wzkris.user.controller.SysTenantPackageController.editStatus()	POST	1	admin	/sys_tenant/package/edit_status	192.168.0.112	内网IP	{"id":1773625804122202113,"status":"0"}	{"code":0,"message":"Success","timestamp":1742883773134}	0	\N	2025-03-25 14:22:53.134+08
1904425848135102465	0	租户套餐	修改套餐	2	com.wzkris.user.controller.SysTenantPackageController.edit()	POST	1	admin	/sys_tenant/package/edit	192.168.0.112	内网IP	{"isMenuDisplay":false,"packageId":1773625804122202113,"remark":"通用租户套餐","packageName":"默认套餐","menuIds":[1,100,1060,1059,1058,1057,1056,103,1055,1054,1053,1052,1051,102,1050,1049,1048,1047,1046,104,151,1064,150,1062,1061,101,300,304,303,302,301,2,203,2078,2062,2064,2077,2072,2071,206,2207,2211,2210,2209,2208,205,2040,2039,2038,2037,207,2016,2015,2014,2013,208,2145,2144,2143,2142,2141,700,1215,1214,1212,1213,1211,1210,202,2115,2114,2113,2112,201,2005,2004,2003,2002,2001,3,601,1129,1131,1132,1125,1134,1133,1126,1127,602,1135,1136,1137,1138],"status":"0"}	{"code":0,"message":"Success","timestamp":1742885479373}	0	\N	2025-03-25 14:51:19.373+08
1904425883719577601	0	租户套餐	修改租户套餐状态	2	com.wzkris.user.controller.SysTenantPackageController.editStatus()	POST	1	admin	/sys_tenant/package/edit_status	192.168.0.112	内网IP	{"id":1773625804122202113,"status":"1"}	{"code":0,"message":"Success","timestamp":1742885487857}	0	\N	2025-03-25 14:51:27.857+08
1904425892246597633	0	租户套餐	修改租户套餐状态	2	com.wzkris.user.controller.SysTenantPackageController.editStatus()	POST	1	admin	/sys_tenant/package/edit_status	192.168.0.112	内网IP	{"id":1773625804122202113,"status":"0"}	{"code":0,"message":"Success","timestamp":1742885489890}	0	\N	2025-03-25 14:51:29.89+08
1904426125382791170	0	租户套餐	修改套餐	2	com.wzkris.user.controller.SysTenantPackageController.edit()	POST	1	admin	/sys_tenant/package/edit	192.168.0.112	内网IP	{"isMenuDisplay":false,"packageId":1773625804122202113,"remark":"通用租户套餐","packageName":"默认套餐","menuIds":[1,100,1060,1059,1058,1057,1056,103,1055,1054,1053,1052,1051,102,1050,1049,1048,1047,1046,104,151,1064,150,1062,1061,101,300,304,303,302,301,2,203,2078,2062,2064,2077,2072,2071,206,2207,2211,2210,2209,2208,205,2040,2039,2038,2037,207,2016,2015,2014,2013,208,2145,2144,2143,2142,2141,700,1215,1214,1212,1213,1211,1210,202,2115,2114,2113,2112,201,2005,2004,2003,2002,2001,3,601,1129,1131,1132,1125,1134,1133,1126,1127,602,1135,1136,1137,1138],"status":"1"}	{"code":0,"message":"Success","timestamp":1742885545474}	0	\N	2025-03-25 14:52:25.474+08
1904426139635036161	0	租户套餐	修改租户套餐状态	2	com.wzkris.user.controller.SysTenantPackageController.editStatus()	POST	1	admin	/sys_tenant/package/edit_status	192.168.0.112	内网IP	{"id":1773625804122202113,"status":"0"}	{"code":0,"message":"Success","timestamp":1742885548872}	0	\N	2025-03-25 14:52:28.872+08
1904437209128906753	0	租户套餐	修改套餐	2	com.wzkris.user.controller.SysTenantPackageController.edit()	POST	1	admin	/sys_tenant/package/edit	192.168.0.112	内网IP	{"isMenuDisplay":false,"packageId":1773625804122202113,"remark":"通用租户套餐","packageName":"默认套餐","menuIds":[1,100,1060,1059,1058,1057,1056,103,1055,1054,1053,1052,1051,102,1050,1049,1048,1047,1046,104,151,1064,150,1062,1061,101,300,304,303,302,301,2,203,2078,2062,2064,2077,2072,2071,206,2207,2211,2210,2209,2208,205,2040,2039,2038,2037,207,2016,2015,2014,2013,208,2145,2144,2143,2142,2141,700,1215,1214,1212,1213,1211,1210,202,2115,2114,2113,2112,201,2005,2004,2003,2002,2001],"status":"0"}	{"code":0,"message":"Success","timestamp":1742888187996}	0	\N	2025-03-25 15:36:27.996+08
1904437222768783362	0	租户套餐	修改套餐	2	com.wzkris.user.controller.SysTenantPackageController.edit()	POST	1	admin	/sys_tenant/package/edit	192.168.0.112	内网IP	{"isMenuDisplay":false,"packageId":1773625804122202113,"remark":"通用租户套餐","packageName":"默认套餐","menuIds":[1,100,1060,1059,1058,1057,1056,103,1055,1054,1053,1052,1051,102,1050,1049,1048,1047,1046,104,151,1064,150,1062,1061,101,300,304,303,302,301,2,203,2078,2062,2064,2077,2072,2071,206,2207,2211,2210,2209,2208,205,2040,2039,2038,2037,207,2016,2015,2014,2013,208,2145,2144,2143,2142,2141,700,1215,1214,1212,1213,1211,1210,202,2115,2114,2113,2112,201,2005,2004,2003,2002,2001,3,601,1129,1131,1132,1125,1134,1133,1126,1127,602,1135,1136,1137,1138],"status":"0"}	{"code":0,"message":"Success","timestamp":1742888191297}	0	\N	2025-03-25 15:36:31.297+08
1904454242415665154	0	登录日志	删除日志	3	com.wzkris.system.controller.SysLoginlogController.remove()	POST	1	admin	/loginlog/remove	192.168.0.112	内网IP	[1903999409392951297]	{"code":0,"message":"Success","timestamp":1742892248983}	0	\N	2025-03-25 16:44:08.994+08
1904699631588818945	0	操作日志	删除日志	3	com.wzkris.system.controller.SysOperlogController.remove()	POST	1	admin	/operlog/remove	192.168.0.112	内网IP	[null]	\N	1	\r\n### Error updating database.  Cause: java.lang.NullPointerException: target is null for method getClass\r\n### The error may exist in com/wzkris/system/mapper/SysOperLogMapper.java (best guess)\r\n### The error may involve com.wzkris.system.mapper.SysOperLogMapper.deleteByIds\r\n### The error occurred while executing an update\r\n### Cause: java.lang.NullPointerException: target is null for method getClass	2025-03-26 08:59:14.448+08
1904699777961639937	0	操作日志	删除日志	3	com.wzkris.system.controller.SysOperlogController.remove()	POST	1	admin	/operlog/remove	192.168.0.112	内网IP	[1904699588890804225]	{"code":0,"message":"Success","timestamp":1742950789335}	0	\N	2025-03-26 08:59:49.335+08
1904699809507000322	0	操作日志	删除日志	3	com.wzkris.system.controller.SysOperlogController.remove()	POST	1	admin	/operlog/remove	192.168.0.112	内网IP	[1904454659115573250]	{"code":0,"message":"Success","timestamp":1742950796854}	0	\N	2025-03-26 08:59:56.854+08
1904704321927987201	0	登录日志	删除日志	3	com.wzkris.system.controller.SysLoginlogController.remove()	POST	1	admin	/loginlog/remove	192.168.0.112	内网IP	[]	{"code":1000,"message":"Invoke Fail","timestamp":1742951872698}	1	Invoke Fail	2025-03-26 09:17:52.698+08
1904704350650580993	0	登录日志	删除日志	3	com.wzkris.system.controller.SysLoginlogController.remove()	POST	1	admin	/loginlog/remove	192.168.0.112	内网IP	[]	{"code":1000,"message":"Invoke Fail","timestamp":1742951879556}	1	Invoke Fail	2025-03-26 09:17:59.556+08
1904704468317585410	0	登录日志	删除日志	3	com.wzkris.system.controller.SysLoginlogController.remove()	POST	1	admin	/loginlog/remove	192.168.0.112	内网IP	[]	{"code":1000,"message":"Invoke Fail","timestamp":1742951907603}	1	Invoke Fail	2025-03-26 09:18:27.603+08
1904704490601922562	0	登录日志	删除日志	3	com.wzkris.system.controller.SysLoginlogController.remove()	POST	1	admin	/loginlog/remove	192.168.0.112	内网IP	[]	{"code":1000,"message":"Invoke Fail","timestamp":1742951912917}	1	Invoke Fail	2025-03-26 09:18:32.917+08
1904704793141264386	0	操作日志	删除日志	3	com.wzkris.system.controller.SysOperlogController.remove()	POST	1	admin	/operlog/remove	192.168.0.112	内网IP	[]	{"code":1000,"message":"Invoke Fail","timestamp":1742951985054}	1	Invoke Fail	2025-03-26 09:19:45.054+08
1905069870247813121	0	参数管理	添加参数	1	com.wzkris.system.controller.SysConfigController.add()	POST	1	admin	/sys_config/add	192.168.0.112	内网IP	{"configName":"erse","configKey":"eeqwe","configValue":"qwe","configType":"Y"}	{"code":0,"message":"Success","timestamp":1743039026144}	0	\N	2025-03-27 09:30:26.146+08
1905070047541043202	0	参数管理	删除参数	3	com.wzkris.system.controller.SysConfigController.remove()	POST	1	admin	/sys_config/remove	192.168.0.112	内网IP	[1905069869761273858]	{"code":412,"message":"内置参数【eeqwe】不能删除 ","timestamp":1743039068489}	1	内置参数【eeqwe】不能删除 	2025-03-27 09:31:08.489+08
1905070247999414274	0	参数管理	删除参数	3	com.wzkris.system.controller.SysConfigController.remove()	POST	1	admin	/sys_config/remove	192.168.0.112	内网IP	[1]	{"code":412,"message":"内置参数【sys.user.initPassword】不能删除 ","timestamp":1743039116276}	1	内置参数【sys.user.initPassword】不能删除 	2025-03-27 09:31:56.276+08
1905073644450324481	0	参数管理	添加参数	1	com.wzkris.system.controller.SysConfigController.add()	POST	1	admin	/sys_config/add	192.168.0.112	内网IP	{"configName":"weda","configKey":"sad","configValue":"阿松大","configType":"N"}	{"code":0,"message":"Success","timestamp":1743039926042}	0	\N	2025-03-27 09:45:26.042+08
1905077334594686978	0	参数管理	修改参数	2	com.wzkris.system.controller.SysConfigController.edit()	POST	1	admin	/sys_config/edit	192.168.0.112	内网IP	{"configName":"用户管理-账号初始密码","configKey":"sys.user.initPassword","configId":1,"configValue":"1234561","configType":"Y"}	{"code":0,"message":"Success","timestamp":1743040805790}	0	\N	2025-03-27 10:00:05.79+08
1905077346753974274	0	参数管理	修改参数	2	com.wzkris.system.controller.SysConfigController.edit()	POST	1	admin	/sys_config/edit	192.168.0.112	内网IP	{"configName":"用户管理-账号初始密码","configKey":"sys.user.initPassword","configId":1,"configValue":"123456","configType":"Y"}	{"code":0,"message":"Success","timestamp":1743040808756}	0	\N	2025-03-27 10:00:08.756+08
1905079791722827777	0	角色管理	修改角色	2	com.wzkris.user.controller.SysRoleController.edit()	POST	1	admin	/sys_role/edit	192.168.0.112	内网IP	{"isMenuDisplay":false,"roleId":1904060860556046337,"roleName":"021042","isDeptDisplay":false,"deptIds":[],"dataScope":"1","menuIds":[1,100,1060,1059,1058,1057,1056,103,1055,1054,1053,1052,1051,102,1050,1049,1048,1047,1046,104,151,1064,150,1062,1061,101,300,304,303,302,301],"status":"0","roleSort":0}	{"code":0,"message":"Success","timestamp":1743041391639}	0	\N	2025-03-27 10:09:51.641+08
1905089522319429634	0	字典类型	删除字典	3	com.wzkris.system.controller.GlobalDictTypeController.remove()	POST	1	admin	/dict/type/remove	::1	内网IP	24	\N	1	\r\n### Error querying database.  Cause: org.postgresql.util.PSQLException: 错误: 无效的类型 bigint 输入语法: "things_model_datatype"\n  在位置：unnamed portal parameter $2 = '...'\r\n### The error may exist in com/wzkris/system/mapper/GlobalDictTypeMapper.java (best guess)\r\n### The error may involve defaultParameterMap\r\n### The error occurred while setting parameters\r\n### SQL: SELECT COUNT(*) AS total FROM biz_sys.global_dict_type WHERE (dict_type = ? AND type_id <> ?)\r\n### Cause: org.postgresql.util.PSQLException: 错误: 无效的类型 bigint 输入语法: "things_model_datatype"\n  在位置：unnamed portal parameter $2 = '...'\n; 错误: 无效的类型 bigint 输入语法: "things_model_datatype"\n  在位置：unnamed portal parameter $2 = '...'	2025-03-27 10:48:31.593+08
1905089550559678466	0	字典类型	删除字典	3	com.wzkris.system.controller.GlobalDictTypeController.remove()	POST	1	admin	/dict/type/remove	::1	内网IP	24	\N	1	\r\n### Error querying database.  Cause: org.postgresql.util.PSQLException: 错误: 无效的类型 bigint 输入语法: "things_model_datatype"\n  在位置：unnamed portal parameter $2 = '...'\r\n### The error may exist in com/wzkris/system/mapper/GlobalDictTypeMapper.java (best guess)\r\n### The error may involve defaultParameterMap\r\n### The error occurred while setting parameters\r\n### SQL: SELECT COUNT(*) AS total FROM biz_sys.global_dict_type WHERE (dict_type = ? AND type_id <> ?)\r\n### Cause: org.postgresql.util.PSQLException: 错误: 无效的类型 bigint 输入语法: "things_model_datatype"\n  在位置：unnamed portal parameter $2 = '...'\n; 错误: 无效的类型 bigint 输入语法: "things_model_datatype"\n  在位置：unnamed portal parameter $2 = '...'	2025-03-27 10:48:38.362+08
1905089825131405314	0	字典类型	删除字典	3	com.wzkris.system.controller.GlobalDictTypeController.remove()	POST	1	admin	/dict/type/remove	::1	内网IP	24	{"code":412,"message":"删除失败，该字典类型已被使用","timestamp":1743043783773}	1	删除失败，该字典类型已被使用	2025-03-27 10:49:43.777+08
1905089935785533441	0	字典类型	修改字典	2	com.wzkris.system.controller.GlobalDictTypeController.edit()	POST	1	admin	/dict/type/edit	::1	内网IP	{"typeId":24,"dictName":"物模型数据类型","dictType":"things_model_type"}	{"code":412,"message":"修改字典'物模型数据类型'失败，字典类型已存在","timestamp":1743043810207}	1	修改字典'物模型数据类型'失败，字典类型已存在	2025-03-27 10:50:10.207+08
1905089944945893377	0	字典类型	修改字典	2	com.wzkris.system.controller.GlobalDictTypeController.edit()	POST	1	admin	/dict/type/edit	::1	内网IP	{"typeId":24,"dictName":"物模型数据类型","dictType":"things_model_type"}	{"code":412,"message":"修改字典'物模型数据类型'失败，字典类型已存在","timestamp":1743043812391}	1	修改字典'物模型数据类型'失败，字典类型已存在	2025-03-27 10:50:12.391+08
1905091392836411393	0	字典类型	修改字典	2	com.wzkris.system.controller.GlobalDictTypeController.edit()	POST	1	admin	/dict/type/edit	::1	内网IP	{"typeId":24,"dictName":"物模型数据类型","dictType":"things_model_datatype"}	{"code":412,"message":"修改字典'物模型数据类型'失败，字典类型已存在","timestamp":1743044157592}	1	修改字典'物模型数据类型'失败，字典类型已存在	2025-03-27 10:55:57.592+08
1905091549346865153	0	字典类型	修改字典	2	com.wzkris.system.controller.GlobalDictTypeController.edit()	POST	1	admin	/dict/type/edit	::1	内网IP	{"typeId":24,"dictName":"物模型数据类型","dictType":"things_model_datatype"}	{"code":412,"message":"修改字典'物模型数据类型'失败，字典类型已存在","timestamp":1743044194913}	1	修改字典'物模型数据类型'失败，字典类型已存在	2025-03-27 10:56:34.913+08
1905141555969884161	0	数据字典	添加字典	1	com.wzkris.system.controller.SysDictController.add()	POST	1	admin	/sys_dict/add	192.168.0.112	内网IP	{"dictName":"阿斯顿撒旦","dictKey":"asdas"}	\N	1	map value can't be null	2025-03-27 14:15:17.334+08
1905141586508611585	0	数据字典	添加字典	1	com.wzkris.system.controller.SysDictController.add()	POST	1	admin	/sys_dict/add	192.168.0.112	内网IP	{"dictName":"阿斯顿撒旦","dictKey":"asdas"}	\N	1	\r\n### Error updating database.  Cause: org.postgresql.util.PSQLException: 错误: 重复键违反唯一约束"u_i_dict_key"\n  详细：键值"(dict_key)=(asdas)" 已经存在\r\n### The error may exist in com/wzkris/system/mapper/SysDictMapper.java (best guess)\r\n### The error may involve com.wzkris.system.mapper.SysDictMapper.insert-Inline\r\n### The error occurred while setting parameters\r\n### SQL: INSERT INTO biz_sys.sys_dict (dict_id, dict_key, dict_name, create_at, creator_id, update_at, updater_id) VALUES (?, ?, ?, ?, ?, ?, ?)\r\n### Cause: org.postgresql.util.PSQLException: 错误: 重复键违反唯一约束"u_i_dict_key"\n  详细：键值"(dict_key)=(asdas)" 已经存在\n; 错误: 重复键违反唯一约束"u_i_dict_key"\n  详细：键值"(dict_key)=(asdas)" 已经存在	2025-03-27 14:15:24.699+08
1905141665629962242	0	数据字典	添加字典	1	com.wzkris.system.controller.SysDictController.add()	POST	1	admin	/sys_dict/add	192.168.0.112	内网IP	{"dictName":"sdasd","dictKey":"sdsd"}	\N	1	map value can't be null	2025-03-27 14:15:43.571+08
1905141948917436417	0	数据字典	添加字典	1	com.wzkris.system.controller.SysDictController.add()	POST	1	admin	/sys_dict/add	192.168.0.112	内网IP	{"dictName":"sdasd","dictKey":"sdsd"}	\N	1	map value can't be null	2025-03-27 14:16:51.051+08
1905144158023790593	0	数据字典	添加字典	1	com.wzkris.system.controller.SysDictController.add()	POST	1	admin	/sys_dict/add	192.168.0.112	内网IP	{"dictName":"adff","dictKey":"dafad"}	\N	1	map value can't be null	2025-03-27 14:25:37.762+08
1905145785610874882	0	数据字典	添加字典	1	com.wzkris.system.controller.SysDictController.add()	POST	1	admin	/sys_dict/add	192.168.0.112	内网IP	{"dictName":"adff","dictKey":"dafad"}	\N	1	\r\n### Error updating database.  Cause: org.postgresql.util.PSQLException: 错误: null value in column "dict_value" of relation "sys_dict" violates not-null constraint\n  详细：失败, 行包含(1905145784780402690, dafad, adff, null, null, 1, 1, 2025-03-27 14:32:05.65+08, 2025-03-27 14:32:05.65+08).\r\n### The error may exist in com/wzkris/system/mapper/SysDictMapper.java (best guess)\r\n### The error may involve com.wzkris.system.mapper.SysDictMapper.insert-Inline\r\n### The error occurred while setting parameters\r\n### SQL: INSERT INTO biz_sys.sys_dict (dict_id, dict_key, dict_name, create_at, creator_id, update_at, updater_id) VALUES (?, ?, ?, ?, ?, ?, ?)\r\n### Cause: org.postgresql.util.PSQLException: 错误: null value in column "dict_value" of relation "sys_dict" violates not-null constraint\n  详细：失败, 行包含(1905145784780402690, dafad, adff, null, null, 1, 1, 2025-03-27 14:32:05.65+08, 2025-03-27 14:32:05.65+08).\n; 错误: null value in column "dict_value" of relation "sys_dict" violates not-null constraint\n  详细：失败, 行包含(1905145784780402690, dafad, adff, null, null, 1, 1, 2025-03-27 14:32:05.65+08, 2025-03-27 14:32:05.65+08).	2025-03-27 14:32:05.769+08
1905146373031518209	0	数据字典	添加字典	1	com.wzkris.system.controller.SysDictController.add()	POST	1	admin	/sys_dict/add	192.168.0.112	内网IP	{"dictName":"adff","dictKey":"dafad"}	\N	1	\r\n### Error updating database.  Cause: org.postgresql.util.PSQLException: 错误: null value in column "dict_value" of relation "sys_dict" violates not-null constraint\n  详细：失败, 行包含(1905146372381401090, dafad, adff, null, null, 1, 1, 2025-03-27 14:34:25.743+08, 2025-03-27 14:34:25.743+08).\r\n### The error may exist in com/wzkris/system/mapper/SysDictMapper.java (best guess)\r\n### The error may involve com.wzkris.system.mapper.SysDictMapper.insert-Inline\r\n### The error occurred while setting parameters\r\n### SQL: INSERT INTO biz_sys.sys_dict (dict_id, dict_key, dict_name, create_at, creator_id, update_at, updater_id) VALUES (?, ?, ?, ?, ?, ?, ?)\r\n### Cause: org.postgresql.util.PSQLException: 错误: null value in column "dict_value" of relation "sys_dict" violates not-null constraint\n  详细：失败, 行包含(1905146372381401090, dafad, adff, null, null, 1, 1, 2025-03-27 14:34:25.743+08, 2025-03-27 14:34:25.743+08).\n; 错误: null value in column "dict_value" of relation "sys_dict" violates not-null constraint\n  详细：失败, 行包含(1905146372381401090, dafad, adff, null, null, 1, 1, 2025-03-27 14:34:25.743+08, 2025-03-27 14:34:25.743+08).	2025-03-27 14:34:25.853+08
1905153039286349826	0	数据字典	添加字典	1	com.wzkris.system.controller.SysDictController.add()	POST	1	admin	/sys_dict/add	192.168.0.112	内网IP	{"dictValue":[{"value":"1","label":"1","tableCls":"1"}],"dictName":"哇大师","remark":"ss","dictKey":"asdasd"}	\N	1	\r\n### Error updating database.  Cause: org.apache.ibatis.type.TypeException: Could not set parameters for mapping: ParameterMapping{property='dictValue', mode=IN, javaType=class [Lcom.wzkris.system.domain.SysDict$DictData;, jdbcType=null, numericScale=null, resultMapId='null', jdbcTypeName='null', expression='null'}. Cause: org.apache.ibatis.type.TypeException: Error setting non null for parameter #4 with JdbcType null . Try setting a different JdbcType for this parameter or a different configuration property. Cause: org.postgresql.util.PSQLException: Unable to find server array type for provided name JAVA_OBJECT.\r\n### The error may exist in com/wzkris/system/mapper/SysDictMapper.java (best guess)\r\n### The error may involve com.wzkris.system.mapper.SysDictMapper.insert-Inline\r\n### The error occurred while setting parameters\r\n### SQL: INSERT INTO biz_sys.sys_dict (dict_id, dict_key, dict_name, dict_value, remark, create_at, creator_id, update_at, updater_id) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)\r\n### Cause: org.apache.ibatis.type.TypeException: Could not set parameters for mapping: ParameterMapping{property='dictValue', mode=IN, javaType=class [Lcom.wzkris.system.domain.SysDict$DictData;, jdbcType=null, numericScale=null, resultMapId='null', jdbcTypeName='null', expression='null'}. Cause: org.apache.ibatis.type.TypeException: Error setting non null for parameter #4 with JdbcType null . Try setting a different JdbcType for this parameter or a different configuration property. Cause: org.postgresql.util.PSQLException: Unable to find server array type for provided name JAVA_OBJECT.	2025-03-27 15:00:55.196+08
1905153507412619266	0	数据字典	添加字典	1	com.wzkris.system.controller.SysDictController.add()	POST	1	admin	/sys_dict/add	192.168.0.112	内网IP	{"dictValue":[{"value":"1","label":"1","tableCls":"1"}],"dictName":"哇大师","remark":"ss","dictKey":"asdasd"}	\N	1	\r\n### Error updating database.  Cause: org.apache.ibatis.type.TypeException: Could not set parameters for mapping: ParameterMapping{property='dictValue', mode=IN, javaType=class [Lcom.wzkris.system.domain.SysDict$DictData;, jdbcType=null, numericScale=null, resultMapId='null', jdbcTypeName='null', expression='null'}. Cause: org.apache.ibatis.type.TypeException: Error setting non null for parameter #4 with JdbcType null . Try setting a different JdbcType for this parameter or a different configuration property. Cause: org.postgresql.util.PSQLException: Unable to find server array type for provided name JAVA_OBJECT.\r\n### The error may exist in com/wzkris/system/mapper/SysDictMapper.java (best guess)\r\n### The error may involve com.wzkris.system.mapper.SysDictMapper.insert-Inline\r\n### The error occurred while setting parameters\r\n### SQL: INSERT INTO biz_sys.sys_dict (dict_id, dict_key, dict_name, dict_value, remark, create_at, creator_id, update_at, updater_id) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)\r\n### Cause: org.apache.ibatis.type.TypeException: Could not set parameters for mapping: ParameterMapping{property='dictValue', mode=IN, javaType=class [Lcom.wzkris.system.domain.SysDict$DictData;, jdbcType=null, numericScale=null, resultMapId='null', jdbcTypeName='null', expression='null'}. Cause: org.apache.ibatis.type.TypeException: Error setting non null for parameter #4 with JdbcType null . Try setting a different JdbcType for this parameter or a different configuration property. Cause: org.postgresql.util.PSQLException: Unable to find server array type for provided name JAVA_OBJECT.	2025-03-27 15:02:46.876+08
1905543015744282626	0	用户信息		2	com.wzkris.user.controller.SysUserProfileController.editInfo()	POST	1	admin	/user_profile	192.168.0.112	内网IP	{"gender":"0","nickname":"nick_admin"}	{"code":0,"message":"Success","timestamp":1743151832887}	0	\N	2025-03-28 16:50:32.887+08
1905543057544716290	0	用户信息		2	com.wzkris.user.controller.SysUserProfileController.editInfo()	POST	1	admin	/user_profile	192.168.0.112	内网IP	{"gender":"0","nickname":"nick_admin"}	{"code":0,"message":"Success","timestamp":1743151842853}	0	\N	2025-03-28 16:50:42.853+08
1905544389726969857	0	用户信息		2	com.wzkris.user.controller.SysUserProfileController.editInfo()	POST	1	admin	/user_profile	192.168.0.112	内网IP	{"gender":"0","nickname":"nick_admin"}	{"code":0,"message":"Success","timestamp":1743152160473}	0	\N	2025-03-28 16:56:00.473+08
1905155273092960258	0	数据字典	添加字典	1	com.wzkris.system.controller.SysDictController.add()	POST	1	admin	/sys_dict/add	192.168.0.112	内网IP	{"dictValue":[{"value":"1","label":"1","tableCls":"1"}],"dictName":"哇大师","remark":"ss","dictKey":"asdasd"}	\N	1	\r\n### Error updating database.  Cause: org.apache.ibatis.type.TypeException: Could not set parameters for mapping: ParameterMapping{property='dictValue', mode=IN, javaType=class [Lcom.wzkris.system.domain.SysDict$DictData;, jdbcType=ARRAY, numericScale=null, resultMapId='null', jdbcTypeName='null', expression='null'}. Cause: org.apache.ibatis.type.TypeException: Error setting non null for parameter #4 with JdbcType ARRAY . Try setting a different JdbcType for this parameter or a different configuration property. Cause: org.postgresql.util.PSQLException: Unable to find server array type for provided name JAVA_OBJECT.\r\n### The error may exist in com/wzkris/system/mapper/SysDictMapper.java (best guess)\r\n### The error may involve com.wzkris.system.mapper.SysDictMapper.insert-Inline\r\n### The error occurred while setting parameters\r\n### SQL: INSERT INTO biz_sys.sys_dict (dict_id, dict_key, dict_name, dict_value, remark, create_at, creator_id, update_at, updater_id) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)\r\n### Cause: org.apache.ibatis.type.TypeException: Could not set parameters for mapping: ParameterMapping{property='dictValue', mode=IN, javaType=class [Lcom.wzkris.system.domain.SysDict$DictData;, jdbcType=ARRAY, numericScale=null, resultMapId='null', jdbcTypeName='null', expression='null'}. Cause: org.apache.ibatis.type.TypeException: Error setting non null for parameter #4 with JdbcType ARRAY . Try setting a different JdbcType for this parameter or a different configuration property. Cause: org.postgresql.util.PSQLException: Unable to find server array type for provided name JAVA_OBJECT.	2025-03-27 15:09:47.794+08
1905155288578330626	0	数据字典	添加字典	1	com.wzkris.system.controller.SysDictController.add()	POST	1	admin	/sys_dict/add	192.168.0.112	内网IP	{"dictValue":[{"value":"1","label":"1","tableCls":"1"}],"dictName":"哇大师","remark":"ss","dictKey":"asdasd"}	\N	1	\r\n### Error updating database.  Cause: org.apache.ibatis.type.TypeException: Could not set parameters for mapping: ParameterMapping{property='dictValue', mode=IN, javaType=class [Lcom.wzkris.system.domain.SysDict$DictData;, jdbcType=ARRAY, numericScale=null, resultMapId='null', jdbcTypeName='null', expression='null'}. Cause: org.apache.ibatis.type.TypeException: Error setting non null for parameter #4 with JdbcType ARRAY . Try setting a different JdbcType for this parameter or a different configuration property. Cause: org.postgresql.util.PSQLException: Unable to find server array type for provided name JAVA_OBJECT.\r\n### The error may exist in com/wzkris/system/mapper/SysDictMapper.java (best guess)\r\n### The error may involve com.wzkris.system.mapper.SysDictMapper.insert-Inline\r\n### The error occurred while setting parameters\r\n### SQL: INSERT INTO biz_sys.sys_dict (dict_id, dict_key, dict_name, dict_value, remark, create_at, creator_id, update_at, updater_id) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)\r\n### Cause: org.apache.ibatis.type.TypeException: Could not set parameters for mapping: ParameterMapping{property='dictValue', mode=IN, javaType=class [Lcom.wzkris.system.domain.SysDict$DictData;, jdbcType=ARRAY, numericScale=null, resultMapId='null', jdbcTypeName='null', expression='null'}. Cause: org.apache.ibatis.type.TypeException: Error setting non null for parameter #4 with JdbcType ARRAY . Try setting a different JdbcType for this parameter or a different configuration property. Cause: org.postgresql.util.PSQLException: Unable to find server array type for provided name JAVA_OBJECT.	2025-03-27 15:09:51.526+08
1905155575493890051	0	数据字典	添加字典	1	com.wzkris.system.controller.SysDictController.add()	POST	1	admin	/sys_dict/add	192.168.0.112	内网IP	{"dictValue":[{"value":"1","label":"1","tableCls":"1"}],"dictName":"哇大师","remark":"ss","dictKey":"asdasd"}	\N	1	\r\n### Error updating database.  Cause: org.apache.ibatis.type.TypeException: Could not set parameters for mapping: ParameterMapping{property='dictValue', mode=IN, javaType=class [Lcom.wzkris.system.domain.SysDict$DictData;, jdbcType=ARRAY, numericScale=null, resultMapId='null', jdbcTypeName='null', expression='null'}. Cause: org.apache.ibatis.type.TypeException: Error setting non null for parameter #4 with JdbcType ARRAY . Try setting a different JdbcType for this parameter or a different configuration property. Cause: org.postgresql.util.PSQLException: Unable to find server array type for provided name JAVA_OBJECT.\r\n### The error may exist in com/wzkris/system/mapper/SysDictMapper.java (best guess)\r\n### The error may involve com.wzkris.system.mapper.SysDictMapper.insert-Inline\r\n### The error occurred while setting parameters\r\n### SQL: INSERT INTO biz_sys.sys_dict (dict_id, dict_key, dict_name, dict_value, remark, create_at, creator_id, update_at, updater_id) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)\r\n### Cause: org.apache.ibatis.type.TypeException: Could not set parameters for mapping: ParameterMapping{property='dictValue', mode=IN, javaType=class [Lcom.wzkris.system.domain.SysDict$DictData;, jdbcType=ARRAY, numericScale=null, resultMapId='null', jdbcTypeName='null', expression='null'}. Cause: org.apache.ibatis.type.TypeException: Error setting non null for parameter #4 with JdbcType ARRAY . Try setting a different JdbcType for this parameter or a different configuration property. Cause: org.postgresql.util.PSQLException: Unable to find server array type for provided name JAVA_OBJECT.	2025-03-27 15:10:59.944+08
1905155594854797314	0	数据字典	添加字典	1	com.wzkris.system.controller.SysDictController.add()	POST	1	admin	/sys_dict/add	192.168.0.112	内网IP	{"dictValue":[{"value":"1","label":"1","tableCls":"1"}],"dictName":"哇大师","remark":"ss","dictKey":"asdasd"}	\N	1	\r\n### Error updating database.  Cause: org.apache.ibatis.type.TypeException: Could not set parameters for mapping: ParameterMapping{property='dictValue', mode=IN, javaType=class [Lcom.wzkris.system.domain.SysDict$DictData;, jdbcType=ARRAY, numericScale=null, resultMapId='null', jdbcTypeName='null', expression='null'}. Cause: org.apache.ibatis.type.TypeException: Error setting non null for parameter #4 with JdbcType ARRAY . Try setting a different JdbcType for this parameter or a different configuration property. Cause: org.postgresql.util.PSQLException: Unable to find server array type for provided name JAVA_OBJECT.\r\n### The error may exist in com/wzkris/system/mapper/SysDictMapper.java (best guess)\r\n### The error may involve com.wzkris.system.mapper.SysDictMapper.insert-Inline\r\n### The error occurred while setting parameters\r\n### SQL: INSERT INTO biz_sys.sys_dict (dict_id, dict_key, dict_name, dict_value, remark, create_at, creator_id, update_at, updater_id) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)\r\n### Cause: org.apache.ibatis.type.TypeException: Could not set parameters for mapping: ParameterMapping{property='dictValue', mode=IN, javaType=class [Lcom.wzkris.system.domain.SysDict$DictData;, jdbcType=ARRAY, numericScale=null, resultMapId='null', jdbcTypeName='null', expression='null'}. Cause: org.apache.ibatis.type.TypeException: Error setting non null for parameter #4 with JdbcType ARRAY . Try setting a different JdbcType for this parameter or a different configuration property. Cause: org.postgresql.util.PSQLException: Unable to find server array type for provided name JAVA_OBJECT.	2025-03-27 15:11:04.55+08
1905544411663179777	0	用户信息		2	com.wzkris.user.controller.SysUserProfileController.editInfo()	POST	1	admin	/user_profile	192.168.0.112	内网IP	{"gender":"0","nickname":"nick_admin"}	{"code":0,"message":"Success","timestamp":1743152165712}	0	\N	2025-03-28 16:56:05.712+08
1905155921817616385	0	数据字典	添加字典	1	com.wzkris.system.controller.SysDictController.add()	POST	1	admin	/sys_dict/add	192.168.0.112	内网IP	{"dictValue":[{"value":"1","label":"1","tableCls":"1"}],"dictName":"哇大师","remark":"ss","dictKey":"asdasd"}	\N	1	\r\n### Error updating database.  Cause: org.apache.ibatis.type.TypeException: Could not set parameters for mapping: ParameterMapping{property='dictValue', mode=IN, javaType=class [Lcom.wzkris.system.domain.SysDict$DictData;, jdbcType=ARRAY, numericScale=null, resultMapId='null', jdbcTypeName='null', expression='null'}. Cause: org.apache.ibatis.type.TypeException: Error setting non null for parameter #4 with JdbcType ARRAY . Try setting a different JdbcType for this parameter or a different configuration property. Cause: org.postgresql.util.PSQLException: Unable to find server array type for provided name JAVA_OBJECT.\r\n### The error may exist in com/wzkris/system/mapper/SysDictMapper.java (best guess)\r\n### The error may involve com.wzkris.system.mapper.SysDictMapper.insert-Inline\r\n### The error occurred while setting parameters\r\n### SQL: INSERT INTO biz_sys.sys_dict (dict_id, dict_key, dict_name, dict_value, remark, create_at, creator_id, update_at, updater_id) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)\r\n### Cause: org.apache.ibatis.type.TypeException: Could not set parameters for mapping: ParameterMapping{property='dictValue', mode=IN, javaType=class [Lcom.wzkris.system.domain.SysDict$DictData;, jdbcType=ARRAY, numericScale=null, resultMapId='null', jdbcTypeName='null', expression='null'}. Cause: org.apache.ibatis.type.TypeException: Error setting non null for parameter #4 with JdbcType ARRAY . Try setting a different JdbcType for this parameter or a different configuration property. Cause: org.postgresql.util.PSQLException: Unable to find server array type for provided name JAVA_OBJECT.	2025-03-27 15:12:22.439+08
1905155935184863233	0	数据字典	添加字典	1	com.wzkris.system.controller.SysDictController.add()	POST	1	admin	/sys_dict/add	192.168.0.112	内网IP	{"dictValue":[{"value":"1","label":"1","tableCls":"1"}],"dictName":"哇大师","remark":"ss","dictKey":"asdasd"}	\N	1	\r\n### Error updating database.  Cause: org.apache.ibatis.type.TypeException: Could not set parameters for mapping: ParameterMapping{property='dictValue', mode=IN, javaType=class [Lcom.wzkris.system.domain.SysDict$DictData;, jdbcType=ARRAY, numericScale=null, resultMapId='null', jdbcTypeName='null', expression='null'}. Cause: org.apache.ibatis.type.TypeException: Error setting non null for parameter #4 with JdbcType ARRAY . Try setting a different JdbcType for this parameter or a different configuration property. Cause: org.postgresql.util.PSQLException: Unable to find server array type for provided name JAVA_OBJECT.\r\n### The error may exist in com/wzkris/system/mapper/SysDictMapper.java (best guess)\r\n### The error may involve com.wzkris.system.mapper.SysDictMapper.insert-Inline\r\n### The error occurred while setting parameters\r\n### SQL: INSERT INTO biz_sys.sys_dict (dict_id, dict_key, dict_name, dict_value, remark, create_at, creator_id, update_at, updater_id) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)\r\n### Cause: org.apache.ibatis.type.TypeException: Could not set parameters for mapping: ParameterMapping{property='dictValue', mode=IN, javaType=class [Lcom.wzkris.system.domain.SysDict$DictData;, jdbcType=ARRAY, numericScale=null, resultMapId='null', jdbcTypeName='null', expression='null'}. Cause: org.apache.ibatis.type.TypeException: Error setting non null for parameter #4 with JdbcType ARRAY . Try setting a different JdbcType for this parameter or a different configuration property. Cause: org.postgresql.util.PSQLException: Unable to find server array type for provided name JAVA_OBJECT.	2025-03-27 15:12:25.687+08
1905157069240729601	0	数据字典	添加字典	1	com.wzkris.system.controller.SysDictController.add()	POST	1	admin	/sys_dict/add	192.168.0.112	内网IP	{"dictValue":[{"value":"1","label":"1","tableCls":"1"}],"dictName":"哇大师","remark":"ss","dictKey":"asdasd"}	\N	1	\r\n### Error updating database.  Cause: org.apache.ibatis.type.TypeException: Could not set parameters for mapping: ParameterMapping{property='dictValue', mode=IN, javaType=class [Lcom.wzkris.system.domain.SysDict$DictData;, jdbcType=null, numericScale=null, resultMapId='null', jdbcTypeName='null', expression='null'}. Cause: org.apache.ibatis.type.TypeException: Error setting non null for parameter #4 with JdbcType null . Try setting a different JdbcType for this parameter or a different configuration property. Cause: org.postgresql.util.PSQLException: Unable to find server array type for provided name jsonb[].\r\n### The error may exist in com/wzkris/system/mapper/SysDictMapper.java (best guess)\r\n### The error may involve com.wzkris.system.mapper.SysDictMapper.insert-Inline\r\n### The error occurred while setting parameters\r\n### SQL: INSERT INTO biz_sys.sys_dict (dict_id, dict_key, dict_name, dict_value, remark, create_at, creator_id, update_at, updater_id) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)\r\n### Cause: org.apache.ibatis.type.TypeException: Could not set parameters for mapping: ParameterMapping{property='dictValue', mode=IN, javaType=class [Lcom.wzkris.system.domain.SysDict$DictData;, jdbcType=null, numericScale=null, resultMapId='null', jdbcTypeName='null', expression='null'}. Cause: org.apache.ibatis.type.TypeException: Error setting non null for parameter #4 with JdbcType null . Try setting a different JdbcType for this parameter or a different configuration property. Cause: org.postgresql.util.PSQLException: Unable to find server array type for provided name jsonb[].	2025-03-27 15:16:56.021+08
1905157122726494210	0	数据字典	添加字典	1	com.wzkris.system.controller.SysDictController.add()	POST	1	admin	/sys_dict/add	192.168.0.112	内网IP	{"dictValue":[{"value":"1","label":"1","tableCls":"1"}],"dictName":"哇大师","remark":"ss","dictKey":"asdasd"}	\N	1	\r\n### Error updating database.  Cause: org.apache.ibatis.type.TypeException: Could not set parameters for mapping: ParameterMapping{property='dictValue', mode=IN, javaType=class [Lcom.wzkris.system.domain.SysDict$DictData;, jdbcType=null, numericScale=null, resultMapId='null', jdbcTypeName='null', expression='null'}. Cause: org.apache.ibatis.type.TypeException: Error setting non null for parameter #4 with JdbcType null . Try setting a different JdbcType for this parameter or a different configuration property. Cause: org.postgresql.util.PSQLException: Unable to find server array type for provided name jsonb[].\r\n### The error may exist in com/wzkris/system/mapper/SysDictMapper.java (best guess)\r\n### The error may involve com.wzkris.system.mapper.SysDictMapper.insert-Inline\r\n### The error occurred while setting parameters\r\n### SQL: INSERT INTO biz_sys.sys_dict (dict_id, dict_key, dict_name, dict_value, remark, create_at, creator_id, update_at, updater_id) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)\r\n### Cause: org.apache.ibatis.type.TypeException: Could not set parameters for mapping: ParameterMapping{property='dictValue', mode=IN, javaType=class [Lcom.wzkris.system.domain.SysDict$DictData;, jdbcType=null, numericScale=null, resultMapId='null', jdbcTypeName='null', expression='null'}. Cause: org.apache.ibatis.type.TypeException: Error setting non null for parameter #4 with JdbcType null . Try setting a different JdbcType for this parameter or a different configuration property. Cause: org.postgresql.util.PSQLException: Unable to find server array type for provided name jsonb[].	2025-03-27 15:17:08.822+08
1905544435709124610	0	用户信息		2	com.wzkris.user.controller.SysUserProfileController.editInfo()	POST	1	admin	/user_profile	192.168.0.112	内网IP	{"gender":"0","nickname":"nick_admin"}	{"code":0,"message":"Success","timestamp":1743152171436}	0	\N	2025-03-28 16:56:11.436+08
1905157277714386945	0	数据字典	添加字典	1	com.wzkris.system.controller.SysDictController.add()	POST	1	admin	/sys_dict/add	192.168.0.112	内网IP	{"dictValue":[{"value":"1","label":"1","tableCls":"1"}],"dictName":"哇大师","remark":"ss","dictKey":"asdasd"}	\N	1	\r\n### Error updating database.  Cause: org.postgresql.util.PSQLException: 错误: 类型json的输入语法无效\n  详细：令牌 "SysDict" 无效.\n  在位置：JSON数据, 行 1: SysDict...\nunnamed portal parameter $4 = '...'\r\n### The error may exist in com/wzkris/system/mapper/SysDictMapper.java (best guess)\r\n### The error may involve com.wzkris.system.mapper.SysDictMapper.insert-Inline\r\n### The error occurred while setting parameters\r\n### SQL: INSERT INTO biz_sys.sys_dict (dict_id, dict_key, dict_name, dict_value, remark, create_at, creator_id, update_at, updater_id) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)\r\n### Cause: org.postgresql.util.PSQLException: 错误: 类型json的输入语法无效\n  详细：令牌 "SysDict" 无效.\n  在位置：JSON数据, 行 1: SysDict...\nunnamed portal parameter $4 = '...'\n; 错误: 类型json的输入语法无效\n  详细：令牌 "SysDict" 无效.\n  在位置：JSON数据, 行 1: SysDict...\nunnamed portal parameter $4 = '...'	2025-03-27 15:17:45.714+08
1905157290309881857	0	数据字典	添加字典	1	com.wzkris.system.controller.SysDictController.add()	POST	1	admin	/sys_dict/add	192.168.0.112	内网IP	{"dictValue":[{"value":"1","label":"1","tableCls":"1"}],"dictName":"哇大师","remark":"ss","dictKey":"asdasd"}	\N	1	\r\n### Error updating database.  Cause: org.postgresql.util.PSQLException: 错误: 类型json的输入语法无效\n  详细：令牌 "SysDict" 无效.\n  在位置：JSON数据, 行 1: SysDict...\nunnamed portal parameter $4 = '...'\r\n### The error may exist in com/wzkris/system/mapper/SysDictMapper.java (best guess)\r\n### The error may involve com.wzkris.system.mapper.SysDictMapper.insert-Inline\r\n### The error occurred while setting parameters\r\n### SQL: INSERT INTO biz_sys.sys_dict (dict_id, dict_key, dict_name, dict_value, remark, create_at, creator_id, update_at, updater_id) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)\r\n### Cause: org.postgresql.util.PSQLException: 错误: 类型json的输入语法无效\n  详细：令牌 "SysDict" 无效.\n  在位置：JSON数据, 行 1: SysDict...\nunnamed portal parameter $4 = '...'\n; 错误: 类型json的输入语法无效\n  详细：令牌 "SysDict" 无效.\n  在位置：JSON数据, 行 1: SysDict...\nunnamed portal parameter $4 = '...'	2025-03-27 15:17:48.777+08
1905157425978839041	0	数据字典	添加字典	1	com.wzkris.system.controller.SysDictController.add()	POST	1	admin	/sys_dict/add	192.168.0.112	内网IP	{"dictValue":[{"value":"1","label":"1","tableCls":"1"}],"dictName":"哇大师","remark":"ss","dictKey":"asdasd"}	\N	1	\r\n### Error updating database.  Cause: org.postgresql.util.PSQLException: 错误: 类型json的输入语法无效\n  详细：令牌 "SysDict" 无效.\n  在位置：JSON数据, 行 1: SysDict...\nunnamed portal parameter $4 = '...'\r\n### The error may exist in com/wzkris/system/mapper/SysDictMapper.java (best guess)\r\n### The error may involve com.wzkris.system.mapper.SysDictMapper.insert-Inline\r\n### The error occurred while setting parameters\r\n### SQL: INSERT INTO biz_sys.sys_dict (dict_id, dict_key, dict_name, dict_value, remark, create_at, creator_id, update_at, updater_id) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)\r\n### Cause: org.postgresql.util.PSQLException: 错误: 类型json的输入语法无效\n  详细：令牌 "SysDict" 无效.\n  在位置：JSON数据, 行 1: SysDict...\nunnamed portal parameter $4 = '...'\n; 错误: 类型json的输入语法无效\n  详细：令牌 "SysDict" 无效.\n  在位置：JSON数据, 行 1: SysDict...\nunnamed portal parameter $4 = '...'	2025-03-27 15:18:21.122+08
1905159114458902529	0	数据字典	添加字典	1	com.wzkris.system.controller.SysDictController.add()	POST	1	admin	/sys_dict/add	192.168.0.112	内网IP	{"dictValue":[{"value":"1","label":"1","tableCls":"1"}],"dictName":"哇大师","remark":"ss","dictKey":"asdasd"}	\N	1	\r\n### Error updating database.  Cause: java.lang.IllegalStateException: Type handler was null on parameter mapping for property 'dictValue'. It was either not specified and/or could not be found for the javaType ([Lcom.wzkris.system.domain.SysDict$DictData;) : jdbcType (null) combination.\r\n### The error may exist in com/wzkris/system/mapper/SysDictMapper.java (best guess)\r\n### The error may involve com.wzkris.system.mapper.SysDictMapper.insert\r\n### The error occurred while executing an update\r\n### Cause: java.lang.IllegalStateException: Type handler was null on parameter mapping for property 'dictValue'. It was either not specified and/or could not be found for the javaType ([Lcom.wzkris.system.domain.SysDict$DictData;) : jdbcType (null) combination.	2025-03-27 15:25:03.638+08
1905159143047278594	0	数据字典	添加字典	1	com.wzkris.system.controller.SysDictController.add()	POST	1	admin	/sys_dict/add	192.168.0.112	内网IP	{"dictValue":[{"value":"1","label":"1","tableCls":"1"}],"dictName":"哇大师","remark":"ss","dictKey":"asdasd"}	\N	1	\r\n### Error updating database.  Cause: java.lang.IllegalStateException: Type handler was null on parameter mapping for property 'dictValue'. It was either not specified and/or could not be found for the javaType ([Lcom.wzkris.system.domain.SysDict$DictData;) : jdbcType (null) combination.\r\n### The error may exist in com/wzkris/system/mapper/SysDictMapper.java (best guess)\r\n### The error may involve com.wzkris.system.mapper.SysDictMapper.insert\r\n### The error occurred while executing an update\r\n### Cause: java.lang.IllegalStateException: Type handler was null on parameter mapping for property 'dictValue'. It was either not specified and/or could not be found for the javaType ([Lcom.wzkris.system.domain.SysDict$DictData;) : jdbcType (null) combination.	2025-03-27 15:25:10.504+08
1905159744581763074	0	数据字典	添加字典	1	com.wzkris.system.controller.SysDictController.add()	POST	1	admin	/sys_dict/add	192.168.0.112	内网IP	{"dictValue":[{"value":"1","label":"1","tableCls":"1"}],"dictName":"哇大师","remark":"ss","dictKey":"asdasd"}	{"code":0,"message":"Success","timestamp":1743060453862}	0	\N	2025-03-27 15:27:33.863+08
1905162000328785922	0	数据字典	修改字典	2	com.wzkris.system.controller.SysDictController.edit()	POST	1	admin	/sys_dict/edit	192.168.0.112	内网IP	{"dictValue":[{"value":"111","label":"11","tableCls":"1"}],"dictName":"哇大师","remark":"ss","dictId":1905159744246218754,"dictKey":"asdasd"}	{"code":0,"message":"Success","timestamp":1743060991733}	0	\N	2025-03-27 15:36:31.733+08
1905162019962322945	0	数据字典	修改字典	2	com.wzkris.system.controller.SysDictController.edit()	POST	1	admin	/sys_dict/edit	192.168.0.112	内网IP	{"dictValue":[{"value":"111","label":"11","tableCls":"1"},{"value":"22","label":"22","tableCls":"3"}],"dictName":"哇大师","remark":"ss","dictId":1905159744246218754,"dictKey":"asdasd"}	{"code":0,"message":"Success","timestamp":1743060996414}	0	\N	2025-03-27 15:36:36.414+08
1905162349127106561	0	数据字典	删除字典	3	com.wzkris.system.controller.SysDictController.remove()	POST	1	admin	/sys_dict/remove	192.168.0.112	内网IP	1905159744246218754	{"code":0,"message":"Success","timestamp":1743061074883}	0	\N	2025-03-27 15:37:54.883+08
1905544612645838850	0	用户信息		2	com.wzkris.user.controller.SysUserProfileController.editInfo()	POST	1	admin	/user_profile	192.168.0.112	内网IP	{"gender":"0","nickname":"nick_a"}	{"code":0,"message":"Success","timestamp":1743152213623}	0	\N	2025-03-28 16:56:53.623+08
1905545073482407938	0	用户信息		2	com.wzkris.user.controller.SysUserProfileController.editInfo()	POST	1	admin	/user_profile	192.168.0.112	内网IP	{"gender":"0","nickname":"nick_a"}	{"code":0,"message":"Success","timestamp":1743152323501}	0	\N	2025-03-28 16:58:43.501+08
1906628752019546113	0	菜单管理	修改菜单	2	com.wzkris.user.controller.SysMenuController.edit()	POST	1	admin	/sys_menu/edit	192.168.0.112	内网IP	{"path":"http://localhost:9200/xxl-job-admin","isCache":false,"icon":"carbon:link","menuId":1906263415450000300,"menuName":"定时任务","menuType":"O","perms":"tool:job:list","isVisible":true,"parentId":1906263415450000101,"menuSort":20,"status":"0"}	{"code":0,"message":"Success","timestamp":1743410692563}	0	\N	2025-03-31 16:44:52.564+08
1906630730699235329	0	菜单管理	新增菜单	1	com.wzkris.user.controller.SysMenuController.add()	POST	1	admin	/sys_menu/add	192.168.0.112	内网IP	{"path":"www.baidu.com","isCache":false,"menuName":"111","menuType":"O","isVisible":true,"parentId":0,"menuSort":0,"status":"0"}	{"code":412,"message":"新增菜单'111'失败，地址必须以http(s)://开头","timestamp":1743411164341}	1	新增菜单'111'失败，地址必须以http(s)://开头	2025-03-31 16:52:44.341+08
1906630762089406466	0	菜单管理	新增菜单	1	com.wzkris.user.controller.SysMenuController.add()	POST	1	admin	/sys_menu/add	192.168.0.112	内网IP	{"path":"http://www.baidu.com","isCache":false,"menuName":"111","menuType":"O","isVisible":true,"parentId":0,"menuSort":0,"status":"0"}	{"code":0,"message":"Success","timestamp":1743411171823}	0	\N	2025-03-31 16:52:51.823+08
1906630829328293890	0	菜单管理	删除菜单	3	com.wzkris.user.controller.SysMenuController.remove()	POST	1	admin	/sys_menu/remove	192.168.0.112	内网IP	1906630762039042050	{"code":0,"message":"Success","timestamp":1743411187856}	0	\N	2025-03-31 16:53:07.856+08
1906870998157479937	0	菜单管理	修改菜单	2	com.wzkris.user.controller.SysMenuController.edit()	POST	1	admin	/sys_menu/edit	192.168.0.112	内网IP	{"path":"http://localhost:9200/xxl-job-admin","isCache":false,"icon":"carbon:link","menuId":1906263415450000300,"menuName":"定时任务","menuType":"I","perms":"tool:job:list","isVisible":true,"parentId":1906263415450000101,"menuSort":20,"status":"0"}	{"code":0,"message":"Success","timestamp":1743468448450}	0	\N	2025-04-01 08:47:28.45+08
1906898625765298177	0	菜单管理	修改菜单	2	com.wzkris.user.controller.SysMenuController.edit()	POST	1	admin	/sys_menu/edit	192.168.0.112	内网IP	{"path":"http://127.0.0.1:8848/nacos","isCache":false,"icon":"carbon:link","menuId":1906263415450000303,"menuName":"Nacos控制台","menuType":"I","perms":"monitor:nacos:list","isVisible":true,"parentId":1906263415450000101,"menuSort":4,"status":"0"}	{"code":0,"message":"Success","timestamp":1743475035417}	0	\N	2025-04-01 10:37:15.418+08
1906900205080137729	0	菜单管理	修改菜单	2	com.wzkris.user.controller.SysMenuController.edit()	POST	1	admin	/sys_menu/edit	192.168.0.112	内网IP	{"path":"http://localhost:8848/nacos/#/login","isCache":false,"icon":"carbon:link","menuId":1906263415450000303,"menuName":"Nacos控制台","menuType":"I","perms":"monitor:nacos:list","isVisible":true,"parentId":1906263415450000101,"menuSort":4,"status":"0"}	{"code":0,"message":"Success","timestamp":1743475412040}	0	\N	2025-04-01 10:43:32.04+08
1906900427017539585	0	菜单管理	修改菜单	2	com.wzkris.user.controller.SysMenuController.edit()	POST	1	admin	/sys_menu/edit	192.168.0.112	内网IP	{"path":"http://localhost:8848/nacos","isCache":false,"icon":"carbon:link","menuId":1906263415450000303,"menuName":"Nacos控制台","menuType":"I","perms":"monitor:nacos:list","isVisible":true,"parentId":1906263415450000101,"menuSort":4,"status":"0"}	{"code":0,"message":"Success","timestamp":1743475464954}	0	\N	2025-04-01 10:44:24.954+08
1906929087669850114	0	菜单管理	修改菜单	2	com.wzkris.user.controller.SysMenuController.edit()	POST	1	admin	/sys_menu/edit	192.168.0.112	内网IP	{"path":"http://localhost:8080/doc.html","isCache":false,"icon":"carbon:link","menuId":1906263415450000301,"menuName":"系统接口","menuType":"I","perms":"tool:swagger:list","isVisible":true,"parentId":1906263415450000101,"menuSort":2,"status":"0"}	{"code":0,"message":"Success","timestamp":1743482298140}	0	\N	2025-04-01 12:38:18.155+08
1906929338564726785	0	菜单管理	修改菜单	2	com.wzkris.user.controller.SysMenuController.edit()	POST	1	admin	/sys_menu/edit	192.168.0.112	内网IP	{"path":"http://localhost:8848/nacos","isCache":false,"icon":"carbon:link","menuId":1906263415450000303,"menuName":"Nacos控制台","menuType":"O","perms":"monitor:nacos:list","isVisible":true,"parentId":1906263415450000101,"menuSort":4,"status":"0"}	{"code":0,"message":"Success","timestamp":1743482358004}	0	\N	2025-04-01 12:39:18.004+08
1906929787627884545	0	菜单管理	修改菜单	2	com.wzkris.user.controller.SysMenuController.edit()	POST	1	admin	/sys_menu/edit	192.168.0.112	内网IP	{"query":"status=1","icon":"carbon:menu","menuName":"菜单管理","isVisible":true,"parentId":1906263415450000003,"path":"sysmenu","isCache":false,"component":"platform/sysmenu/index","menuId":1906263415450000207,"menuType":"M","perms":"sys_menu:list","menuSort":50,"status":"0"}	{"code":0,"message":"Success","timestamp":1743482465069}	0	\N	2025-04-01 12:41:05.069+08
1906930268261568514	0	菜单管理	修改菜单	2	com.wzkris.user.controller.SysMenuController.edit()	POST	1	admin	/sys_menu/edit	192.168.0.112	内网IP	{"query":"{status: 1}","icon":"carbon:menu","menuName":"菜单管理","isVisible":true,"parentId":1906263415450000003,"path":"sysmenu","isCache":false,"component":"platform/sysmenu/index","menuId":1906263415450000207,"menuType":"M","perms":"sys_menu:list","menuSort":50,"status":"0"}	{"code":0,"message":"Success","timestamp":1743482579661}	0	\N	2025-04-01 12:42:59.661+08
1906933336986341377	0	菜单管理	修改菜单	2	com.wzkris.user.controller.SysMenuController.edit()	POST	1	admin	/sys_menu/edit	192.168.0.112	内网IP	{"query":"status=1&type=2","icon":"carbon:menu","menuName":"菜单管理","isVisible":true,"parentId":1906263415450000003,"path":"sysmenu","isCache":false,"component":"platform/sysmenu/index","menuId":1906263415450000207,"menuType":"O","perms":"sys_menu:list","menuSort":50,"status":"0"}	{"code":412,"message":"修改菜单'菜单管理'失败，地址必须以http(s)://开头","timestamp":1743483311302}	1	修改菜单'菜单管理'失败，地址必须以http(s)://开头	2025-04-01 12:55:11.302+08
1906933371870367746	0	菜单管理	修改菜单	2	com.wzkris.user.controller.SysMenuController.edit()	POST	1	admin	/sys_menu/edit	192.168.0.112	内网IP	{"query":"status=1&type=2","icon":"carbon:menu","menuName":"菜单管理","isVisible":true,"parentId":1906263415450000003,"path":"sysmenu","isCache":false,"component":"platform/sysmenu/index","menuId":1906263415450000207,"menuType":"M","perms":"sys_menu:list","menuSort":50,"status":"0"}	{"code":0,"message":"Success","timestamp":1743483319619}	0	\N	2025-04-01 12:55:19.619+08
1906934747266535425	0	菜单管理	修改菜单	2	com.wzkris.user.controller.SysMenuController.edit()	POST	1	admin	/sys_menu/edit	192.168.0.112	内网IP	{"query":"&status=1&type=2","icon":"carbon:menu","menuName":"菜单管理","isVisible":true,"parentId":1906263415450000003,"path":"sysmenu","isCache":false,"component":"platform/sysmenu/index","menuId":1906263415450000207,"menuType":"M","perms":"sys_menu:list","menuSort":50,"status":"0"}	{"code":0,"message":"Success","timestamp":1743483647507}	0	\N	2025-04-01 13:00:47.507+08
1906934821300195330	0	菜单管理	修改菜单	2	com.wzkris.user.controller.SysMenuController.edit()	POST	1	admin	/sys_menu/edit	192.168.0.112	内网IP	{"query":"","icon":"carbon:menu","menuName":"菜单管理","isVisible":true,"parentId":1906263415450000003,"path":"sysmenu","isCache":false,"component":"platform/sysmenu/index","menuId":1906263415450000207,"menuType":"M","perms":"sys_menu:list","menuSort":50,"status":"0"}	{"code":0,"message":"Success","timestamp":1743483665200}	0	\N	2025-04-01 13:01:05.2+08
1906954031015325697	0	菜单管理	修改菜单	2	com.wzkris.user.controller.SysMenuController.edit()	POST	1	admin	/sys_menu/edit	192.168.0.112	内网IP	{"path":"config","isCache":false,"component":"system/sysconfig/index","icon":"carbon:parameter","menuId":1906263415450000103,"menuName":"参数管理","menuType":"M","perms":"sys_config:list","isVisible":true,"parentId":1906263415450000001,"menuSort":7,"status":"0"}	{"code":0,"message":"Success","timestamp":1743488245062}	0	\N	2025-04-01 14:17:25.064+08
1906954063298883585	0	菜单管理	修改菜单	2	com.wzkris.user.controller.SysMenuController.edit()	POST	1	admin	/sys_menu/edit	192.168.0.112	内网IP	{"path":"dict","isCache":false,"component":"system/sysdict/index","icon":"carbon:text-vertical-alignment","menuId":1906263415450000102,"menuName":"字典管理","menuType":"M","perms":"sys_dict:list","isVisible":true,"parentId":1906263415450000001,"menuSort":6,"status":"0"}	{"code":0,"message":"Success","timestamp":1743488252837}	0	\N	2025-04-01 14:17:32.837+08
1906954580792111106	0	菜单管理	修改菜单	2	com.wzkris.user.controller.SysMenuController.edit()	POST	1	admin	/sys_menu/edit	192.168.0.112	内网IP	{"path":"sys_config","isCache":false,"component":"system/sysconfig/index","icon":"carbon:parameter","menuId":1906263415450000103,"menuName":"参数管理","menuType":"M","perms":"sys_config:list","isVisible":true,"parentId":1906263415450000001,"menuSort":7,"status":"0"}	{"code":0,"message":"Success","timestamp":1743488376228}	0	\N	2025-04-01 14:19:36.228+08
1906954615361564673	0	菜单管理	修改菜单	2	com.wzkris.user.controller.SysMenuController.edit()	POST	1	admin	/sys_menu/edit	192.168.0.112	内网IP	{"path":"sys_dict","isCache":false,"component":"system/sysdict/index","icon":"carbon:text-vertical-alignment","menuId":1906263415450000102,"menuName":"字典管理","menuType":"M","perms":"sys_dict:list","isVisible":true,"parentId":1906263415450000001,"menuSort":6,"status":"0"}	{"code":0,"message":"Success","timestamp":1743488384468}	0	\N	2025-04-01 14:19:44.468+08
1906954727127183361	0	菜单管理	修改菜单	2	com.wzkris.user.controller.SysMenuController.edit()	POST	1	admin	/sys_menu/edit	192.168.0.112	内网IP	{"path":"sysmessage","isCache":false,"component":"system/sysmessage/index","icon":"carbon:message-queue","menuId":1906263415450000100,"menuName":"消息管理","menuType":"M","perms":"sys_message:list","isVisible":true,"parentId":1906263415450000001,"menuSort":15,"status":"0"}	{"code":0,"message":"Success","timestamp":1743488411107}	0	\N	2025-04-01 14:20:11.107+08
1906954757640744961	0	菜单管理	修改菜单	2	com.wzkris.user.controller.SysMenuController.edit()	POST	1	admin	/sys_menu/edit	192.168.0.112	内网IP	{"path":"sysconfig","isCache":false,"component":"system/sysconfig/index","icon":"carbon:parameter","menuId":1906263415450000103,"menuName":"参数管理","menuType":"M","perms":"sys_config:list","isVisible":true,"parentId":1906263415450000001,"menuSort":7,"status":"0"}	{"code":0,"message":"Success","timestamp":1743488418379}	0	\N	2025-04-01 14:20:18.379+08
1906954776859045890	0	菜单管理	修改菜单	2	com.wzkris.user.controller.SysMenuController.edit()	POST	1	admin	/sys_menu/edit	192.168.0.112	内网IP	{"path":"sysdict","isCache":false,"component":"system/sysdict/index","icon":"carbon:text-vertical-alignment","menuId":1906263415450000102,"menuName":"字典管理","menuType":"M","perms":"sys_dict:list","isVisible":true,"parentId":1906263415450000001,"menuSort":6,"status":"0"}	{"code":0,"message":"Success","timestamp":1743488422964}	0	\N	2025-04-01 14:20:22.964+08
1906954963216166913	0	菜单管理	修改菜单	2	com.wzkris.user.controller.SysMenuController.edit()	POST	1	admin	/sys_menu/edit	192.168.0.112	内网IP	{"path":"/systenant/package","isCache":false,"component":"platform/systenant/package/index","icon":"carbon:package","menuId":1906263415450000602,"menuName":"租户套餐管理","menuType":"M","perms":"tenant_package:list","isVisible":true,"parentId":1906263415450000003,"menuSort":50,"status":"0"}	{"code":0,"message":"Success","timestamp":1743488467397}	0	\N	2025-04-01 14:21:07.397+08
1906955045156089858	0	菜单管理	修改菜单	2	com.wzkris.user.controller.SysMenuController.edit()	POST	1	admin	/sys_menu/edit	192.168.0.112	内网IP	{"path":"/systenant/package","isCache":false,"component":"/platform/systenant/package/index","icon":"carbon:package","menuId":1906263415450000602,"menuName":"租户套餐管理","menuType":"M","perms":"tenant_package:list","isVisible":true,"parentId":1906263415450000003,"menuSort":50,"status":"0"}	{"code":0,"message":"Success","timestamp":1743488486939}	0	\N	2025-04-01 14:21:26.939+08
1906955163171221505	0	菜单管理	修改菜单	2	com.wzkris.user.controller.SysMenuController.edit()	POST	1	admin	/sys_menu/edit	192.168.0.112	内网IP	{"path":"systenant/package","isCache":false,"component":"platform/systenant/package/index","icon":"carbon:package","menuId":1906263415450000602,"menuName":"租户套餐管理","menuType":"M","perms":"tenant_package:list","isVisible":true,"parentId":1906263415450000003,"menuSort":50,"status":"0"}	{"code":0,"message":"Success","timestamp":1743488515078}	0	\N	2025-04-01 14:21:55.078+08
1906958106981232642	0	菜单管理	修改菜单	2	com.wzkris.user.controller.SysMenuController.edit()	POST	1	admin	/sys_menu/edit	192.168.0.112	内网IP	{"path":"syslog","isCache":false,"icon":"carbon:ibm-knowledge-catalog-premium","menuId":1906263415450000104,"menuName":"日志管理","menuType":"D","isVisible":true,"parentId":1906263415450000001,"menuSort":1,"status":"0"}	{"code":0,"message":"Success","timestamp":1743489216929}	0	\N	2025-04-01 14:33:36.929+08
1906958136832094209	0	菜单管理	修改菜单	2	com.wzkris.user.controller.SysMenuController.edit()	POST	1	admin	/sys_menu/edit	192.168.0.112	内网IP	{"path":"login","isCache":false,"component":"system/loginlog/index","icon":"carbon:login","menuId":1906263415450000151,"menuName":"登录日志","menuType":"M","perms":"loginlog:list","isVisible":true,"parentId":1906263415450000104,"menuSort":2,"status":"0"}	{"code":0,"message":"Success","timestamp":1743489224031}	0	\N	2025-04-01 14:33:44.031+08
1906958171221192706	0	菜单管理	修改菜单	2	com.wzkris.user.controller.SysMenuController.edit()	POST	1	admin	/sys_menu/edit	192.168.0.112	内网IP	{"path":"oper","isCache":false,"component":"system/operlog/index","icon":"carbon:touch-interaction","menuId":1906263415450000150,"menuName":"操作日志","menuType":"M","perms":"operlog:list","isVisible":true,"parentId":1906263415450000104,"menuSort":1,"status":"0"}	{"code":0,"message":"Success","timestamp":1743489232245}	0	\N	2025-04-01 14:33:52.245+08
1906958553032880130	0	菜单管理	修改菜单	2	com.wzkris.user.controller.SysMenuController.edit()	POST	1	admin	/sys_menu/edit	192.168.0.112	内网IP	{"path":"login","isCache":false,"component":"system/syslog/login/index","icon":"carbon:login","menuId":1906263415450000151,"menuName":"登录日志","menuType":"M","perms":"loginlog:list","isVisible":true,"parentId":1906263415450000104,"menuSort":2,"status":"0"}	{"code":0,"message":"Success","timestamp":1743489323263}	0	\N	2025-04-01 14:35:23.263+08
1906958618321416193	0	菜单管理	修改菜单	2	com.wzkris.user.controller.SysMenuController.edit()	POST	1	admin	/sys_menu/edit	192.168.0.112	内网IP	{"path":"oper","isCache":false,"component":"system/syslog/oper/index","icon":"carbon:touch-interaction","menuId":1906263415450000150,"menuName":"操作日志","menuType":"M","perms":"operlog:list","isVisible":true,"parentId":1906263415450000104,"menuSort":1,"status":"0"}	{"code":0,"message":"Success","timestamp":1743489338842}	0	\N	2025-04-01 14:35:38.842+08
1906975127714328578	0	参数管理	修改参数	2	com.wzkris.system.controller.SysConfigController.edit()	POST	1	admin	/sys_config/edit	192.168.0.112	内网IP	{"configName":"用户管理-账号初始密码","configKey":"sys.user.initPassword","configId":1,"configValue":"123456","configType":"N"}	{"code":0,"message":"Success","timestamp":1743493274988}	0	\N	2025-04-01 15:41:14.988+08
1906975140578258946	0	参数管理	修改参数	2	com.wzkris.system.controller.SysConfigController.edit()	POST	1	admin	/sys_config/edit	192.168.0.112	内网IP	{"configName":"用户管理-账号初始密码","configKey":"sys.user.initPassword","configId":1,"configValue":"123456","configType":"Y"}	{"code":0,"message":"Success","timestamp":1743493278055}	0	\N	2025-04-01 15:41:18.055+08
1906975158391468034	0	参数管理	删除参数	3	com.wzkris.system.controller.SysConfigController.remove()	POST	1	admin	/sys_config/remove	192.168.0.112	内网IP	1	{"code":412,"message":"内置参数'sys.user.initPassword'不能删除","timestamp":1743493282309}	1	内置参数'sys.user.initPassword'不能删除	2025-04-01 15:41:22.309+08
1907225924348063745	0	系统消息	修改草稿	2	com.wzkris.system.controller.SysMessageController.edit()	POST	1	admin	/sys_message/edit	192.168.0.112	内网IP	{"msgId":1868480718916243457,"title":"冲钱充钱","content":"<h1>充充充充充充充充充充充充充充充充充充充充充充充充充充充充充</h1>","status":"1"}	{"code":412,"message":"仅草稿可以修改","timestamp":1743553069502}	1	仅草稿可以修改	2025-04-02 08:17:49.506+08
1907226950773944321	0	系统消息	删除草稿	3	com.wzkris.system.controller.SysMessageController.remove()	POST	1	admin	/sys_message/remove	192.168.0.112	内网IP	[1868480718916243457]	{"code":0,"message":"Success","timestamp":1743553314284}	0	\N	2025-04-02 08:21:54.284+08
1907227073100820482	0	系统消息	修改草稿	2	com.wzkris.system.controller.SysMessageController.edit()	POST	1	admin	/sys_message/edit	192.168.0.112	内网IP	{"msgId":1867761817215488005,"title":"测试通知7","content":"测测测测测测测测测测测测测测测测测测测测测测测测测测测测测测测测测测测测测测测测测测测测测测测测测测测测测测测测测测测测测测测测测测测测测测测测测测测测测测测测测测测测","status":"2"}	{"code":412,"message":"仅草稿可以修改","timestamp":1743553343449}	1	仅草稿可以修改	2025-04-02 08:22:23.449+08
1907227749126156290	0	菜单管理	删除菜单	3	com.wzkris.user.controller.SysMenuController.remove()	POST	1	admin	/sys_menu/remove	192.168.0.112	内网IP	1906263415450001060	{"code":0,"message":"Success","timestamp":1743553504577}	0	\N	2025-04-02 08:25:04.577+08
1907241654082797570	0	系统消息	修改草稿	2	com.wzkris.system.controller.SysMessageController.edit()	POST	1	admin	/sys_message/edit	192.168.0.112	内网IP	{"msgId":1867761817215488005,"title":"测试通知7","content":"测测测测测测测测测测测测测测测测测测测测测测测测测测测测测测测测测测测测测测测测测测测测测测测测测测测测测测测测测测测测测测测测测测测测测测测测测测测测测测测测测测测测","status":"2"}	\N	1	\r\n### Error updating database.  Cause: org.postgresql.util.PSQLException: 错误: null value in column "creator_id" of relation "sys_notice" violates not-null constraint\n  详细：失败, 行包含(1907241653227159553, 1, 123, 222, 2025-04-02 09:20:19.618+08, null).\r\n### The error may exist in com/wzkris/system/mapper/SysNoticeMapper.java (best guess)\r\n### The error may involve com.wzkris.system.mapper.SysNoticeMapper.insert-Inline\r\n### The error occurred while setting parameters\r\n### SQL: INSERT INTO biz_sys.sys_notice (notice_id, notice_type, title, content, create_at) VALUES (?, ?, ?, ?, ?)\r\n### Cause: org.postgresql.util.PSQLException: 错误: null value in column "creator_id" of relation "sys_notice" violates not-null constraint\n  详细：失败, 行包含(1907241653227159553, 1, 123, 222, 2025-04-02 09:20:19.618+08, null).\n; 错误: null value in column "creator_id" of relation "sys_notice" violates not-null constraint\n  详细：失败, 行包含(1907241653227159553, 1, 123, 222, 2025-04-02 09:20:19.618+08, null).	2025-04-02 09:20:19.782+08
1907248602131869697	0	系统消息	修改草稿	2	com.wzkris.system.controller.SysMessageController.edit()	POST	1	admin	/sys_message/edit	192.168.0.112	内网IP	{"msgId":1867761817215488005,"title":"测试通知7","content":"测测测测测测测测测测测测测测测测测测测测测测测测测测测测测测测测测测测测测测测测测测测测测测测测测测测测测测测测测测测测测测测测测测测测测测测测测测测测测测测测测测测测","status":"2"}	{"code":0,"message":"Success","timestamp":1743558476316}	0	\N	2025-04-02 09:47:56.317+08
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


-- Completed on 2025-04-02 09:50:51

--
-- PostgreSQL database dump complete
--

