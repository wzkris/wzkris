--
-- PostgreSQL database dump
--

-- Dumped from database version 15.13
-- Dumped by pg_dump version 15.13

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

DROP DATABASE IF EXISTS wzkris_user_center;
--
-- Name: wzkris_user_center; Type: DATABASE; Schema: -; Owner: postgres
--

CREATE DATABASE wzkris_user_center WITH TEMPLATE = template0 ENCODING = 'UTF8' LOCALE_PROVIDER = libc LOCALE = 'Chinese (Simplified)_China.936';


ALTER DATABASE wzkris_user_center OWNER TO postgres;

\connect wzkris_user_center

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
-- Name: biz; Type: SCHEMA; Schema: -; Owner: postgres
--

CREATE SCHEMA biz;


ALTER SCHEMA biz OWNER TO postgres;

--
-- Name: SCHEMA biz; Type: COMMENT; Schema: -; Owner: postgres
--

COMMENT ON SCHEMA biz IS 'b端';


SET default_tablespace = '';

SET default_table_access_method = heap;

--
-- Name: admin_info; Type: TABLE; Schema: biz; Owner: postgres
--

CREATE TABLE biz.admin_info (
    id bigint NOT NULL,
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
-- Name: TABLE admin_info; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON TABLE biz.admin_info IS '管理员表';


--
-- Name: COLUMN admin_info.id; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.admin_info.id IS '管理员ID';


--
-- Name: COLUMN admin_info.dept_id; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.admin_info.dept_id IS '部门ID';


--
-- Name: COLUMN admin_info.username; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.admin_info.username IS '用户账号';


--
-- Name: COLUMN admin_info.email; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.admin_info.email IS '用户邮箱';


--
-- Name: COLUMN admin_info.nickname; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.admin_info.nickname IS '用户昵称';


--
-- Name: COLUMN admin_info.phone_number; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.admin_info.phone_number IS '手机号码';


--
-- Name: COLUMN admin_info.status; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.admin_info.status IS '状态值';


--
-- Name: COLUMN admin_info.gender; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.admin_info.gender IS '用户性别（0男 1女 2未知）';


--
-- Name: COLUMN admin_info.avatar; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.admin_info.avatar IS '头像地址';


--
-- Name: COLUMN admin_info.password; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.admin_info.password IS '密码';


--
-- Name: COLUMN admin_info.login_ip; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.admin_info.login_ip IS '登录ip';


--
-- Name: COLUMN admin_info.login_date; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.admin_info.login_date IS '登录时间';


--
-- Name: COLUMN admin_info.remark; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.admin_info.remark IS '备注';


--
-- Name: COLUMN admin_info.creator_id; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.admin_info.creator_id IS '创建者';


--
-- Name: COLUMN admin_info.updater_id; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.admin_info.updater_id IS '更新者';


--
-- Name: COLUMN admin_info.hint; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.admin_info.hint IS '标签';


--
-- Name: admin_login_log; Type: TABLE; Schema: biz; Owner: postgres
--

CREATE TABLE biz.admin_login_log (
    id bigint NOT NULL,
    admin_id bigint NOT NULL,
    username character varying(32) NOT NULL,
    login_type character varying(32) NOT NULL,
    success boolean NOT NULL,
    error_msg character varying(50) NOT NULL,
    login_ip inet NOT NULL,
    login_location character varying(50) NOT NULL,
    login_time timestamp(0) with time zone NOT NULL,
    trace_id character varying(64) NOT NULL,
    user_agent character varying(200) NOT NULL,
    creator_id bigint,
    updater_id bigint,
    create_at timestamp(0) with time zone,
    update_at timestamp(0) with time zone,
    hint character varying(10),
    deleted boolean DEFAULT false NOT NULL
);


ALTER TABLE biz.admin_login_log OWNER TO postgres;

--
-- Name: TABLE admin_login_log; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON TABLE biz.admin_login_log IS '后台登录日志';


--
-- Name: COLUMN admin_login_log.admin_id; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.admin_login_log.admin_id IS '用户ID';


--
-- Name: COLUMN admin_login_log.username; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.admin_login_log.username IS '用户名';


--
-- Name: COLUMN admin_login_log.login_type; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.admin_login_log.login_type IS '登录类型';


--
-- Name: COLUMN admin_login_log.success; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.admin_login_log.success IS '登录状态';


--
-- Name: COLUMN admin_login_log.error_msg; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.admin_login_log.error_msg IS '失败信息';


--
-- Name: COLUMN admin_login_log.login_ip; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.admin_login_log.login_ip IS '登录ip';


--
-- Name: COLUMN admin_login_log.login_location; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.admin_login_log.login_location IS '登录地址';


--
-- Name: COLUMN admin_login_log.login_time; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.admin_login_log.login_time IS '登录时间';


--
-- Name: admin_operate_log; Type: TABLE; Schema: biz; Owner: postgres
--

CREATE TABLE biz.admin_operate_log (
    id bigint NOT NULL,
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
    oper_time timestamp(0) with time zone NOT NULL,
    trace_id character varying(64) NOT NULL,
    creator_id bigint,
    updater_id bigint,
    create_at timestamp(0) with time zone,
    update_at timestamp(0) with time zone,
    hint character varying(10),
    deleted boolean DEFAULT false NOT NULL
);


ALTER TABLE biz.admin_operate_log OWNER TO postgres;

--
-- Name: TABLE admin_operate_log; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON TABLE biz.admin_operate_log IS '操作日志记录';


--
-- Name: COLUMN admin_operate_log.id; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.admin_operate_log.id IS '日志主键';


--
-- Name: COLUMN admin_operate_log.title; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.admin_operate_log.title IS '模块标题';


--
-- Name: COLUMN admin_operate_log.sub_title; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.admin_operate_log.sub_title IS '子标题';


--
-- Name: COLUMN admin_operate_log.oper_type; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.admin_operate_log.oper_type IS '操作类型（0其他 1新增 2修改 3删除 4授权 5导入导出）';


--
-- Name: COLUMN admin_operate_log.method; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.admin_operate_log.method IS '方法名称';


--
-- Name: COLUMN admin_operate_log.http_method; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.admin_operate_log.http_method IS '请求方式';


--
-- Name: COLUMN admin_operate_log.admin_id; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.admin_operate_log.admin_id IS '用户ID';


--
-- Name: COLUMN admin_operate_log.username; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.admin_operate_log.username IS '用户名';


--
-- Name: COLUMN admin_operate_log.http_url; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.admin_operate_log.http_url IS '请求URL';


--
-- Name: COLUMN admin_operate_log.oper_ip; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.admin_operate_log.oper_ip IS '主机地址';


--
-- Name: COLUMN admin_operate_log.oper_location; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.admin_operate_log.oper_location IS '操作地点';


--
-- Name: COLUMN admin_operate_log.oper_param; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.admin_operate_log.oper_param IS '请求参数';


--
-- Name: COLUMN admin_operate_log.json_result; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.admin_operate_log.json_result IS '返回参数';


--
-- Name: COLUMN admin_operate_log.success; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.admin_operate_log.success IS '操作状态';


--
-- Name: COLUMN admin_operate_log.error_msg; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.admin_operate_log.error_msg IS '错误消息';


--
-- Name: COLUMN admin_operate_log.oper_time; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.admin_operate_log.oper_time IS '操作时间';


--
-- Name: COLUMN admin_operate_log.trace_id; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.admin_operate_log.trace_id IS '链路追踪ID';


--
-- Name: admin_to_role; Type: TABLE; Schema: biz; Owner: postgres
--

CREATE TABLE biz.admin_to_role (
    id bigint NOT NULL,
    admin_id bigint NOT NULL,
    role_id bigint NOT NULL,
    creator_id bigint NOT NULL,
    updater_id bigint,
    create_at timestamp(0) with time zone NOT NULL,
    update_at timestamp(0) with time zone,
    hint character varying(10) DEFAULT ''::character varying NOT NULL,
    deleted boolean DEFAULT false NOT NULL
);


ALTER TABLE biz.admin_to_role OWNER TO postgres;

--
-- Name: TABLE admin_to_role; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON TABLE biz.admin_to_role IS '管理员和角色关联表';


--
-- Name: COLUMN admin_to_role.admin_id; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.admin_to_role.admin_id IS '管理员ID';


--
-- Name: COLUMN admin_to_role.role_id; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.admin_to_role.role_id IS '角色ID';


--
-- Name: announcement_info; Type: TABLE; Schema: biz; Owner: postgres
--

CREATE TABLE biz.announcement_info (
    id bigint NOT NULL,
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
-- Name: TABLE announcement_info; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON TABLE biz.announcement_info IS '系统消息表';


--
-- Name: COLUMN announcement_info.id; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.announcement_info.id IS '公告ID';


--
-- Name: COLUMN announcement_info.title; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.announcement_info.title IS '公告标题';


--
-- Name: COLUMN announcement_info.content; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.announcement_info.content IS '公告内容';


--
-- Name: COLUMN announcement_info.status; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.announcement_info.status IS '公告状态（0草稿 1关闭 2公开）';


--
-- Name: COLUMN announcement_info.creator_id; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.announcement_info.creator_id IS '创建者ID';


--
-- Name: COLUMN announcement_info.updater_id; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.announcement_info.updater_id IS '更新者ID';


--
-- Name: COLUMN announcement_info.create_at; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.announcement_info.create_at IS '创建时间';


--
-- Name: COLUMN announcement_info.update_at; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.announcement_info.update_at IS '更新时间';


--
-- Name: COLUMN announcement_info.hint; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.announcement_info.hint IS '标签';


--
-- Name: config_info; Type: TABLE; Schema: biz; Owner: postgres
--

CREATE TABLE biz.config_info (
    id bigint NOT NULL,
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
-- Name: TABLE config_info; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON TABLE biz.config_info IS '参数配置表';


--
-- Name: COLUMN config_info.id; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.config_info.id IS '参数主键';


--
-- Name: COLUMN config_info.config_name; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.config_info.config_name IS '参数名称';


--
-- Name: COLUMN config_info.config_key; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.config_info.config_key IS '参数键名';


--
-- Name: COLUMN config_info.config_value; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.config_info.config_value IS '参数键值';


--
-- Name: COLUMN config_info.config_type; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.config_info.config_type IS '配置类型';


--
-- Name: COLUMN config_info.built_in; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.config_info.built_in IS '是否内置';


--
-- Name: COLUMN config_info.hint; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.config_info.hint IS '标签';


--
-- Name: customer_info; Type: TABLE; Schema: biz; Owner: postgres
--

CREATE TABLE biz.customer_info (
    id bigint NOT NULL,
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
-- Name: TABLE customer_info; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON TABLE biz.customer_info IS '用户信息表';


--
-- Name: COLUMN customer_info.id; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.customer_info.id IS '用户ID';


--
-- Name: COLUMN customer_info.nickname; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.customer_info.nickname IS '用户昵称';


--
-- Name: COLUMN customer_info.phone_number; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.customer_info.phone_number IS '手机号码';


--
-- Name: COLUMN customer_info.status; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.customer_info.status IS '状态值';


--
-- Name: COLUMN customer_info.gender; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.customer_info.gender IS '用户性别（0男 1女 2未知）';


--
-- Name: COLUMN customer_info.avatar; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.customer_info.avatar IS '头像地址';


--
-- Name: COLUMN customer_info.login_ip; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.customer_info.login_ip IS '登录ip';


--
-- Name: COLUMN customer_info.login_date; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.customer_info.login_date IS '登录时间';


--
-- Name: COLUMN customer_info.creator_id; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.customer_info.creator_id IS '创建者';


--
-- Name: COLUMN customer_info.updater_id; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.customer_info.updater_id IS '更新者';


--
-- Name: COLUMN customer_info.hint; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.customer_info.hint IS '标签';


--
-- Name: customer_login_log; Type: TABLE; Schema: biz; Owner: postgres
--

CREATE TABLE biz.customer_login_log (
    id bigint NOT NULL,
    customer_id bigint NOT NULL,
    username character varying(32) NOT NULL,
    login_type character varying(32) NOT NULL,
    success boolean NOT NULL,
    error_msg character varying(50) NOT NULL,
    login_ip inet NOT NULL,
    login_location character varying(50) NOT NULL,
    login_time timestamp(0) with time zone NOT NULL,
    trace_id character varying(64) NOT NULL,
    user_agent character varying(200) NOT NULL,
    creator_id bigint,
    updater_id bigint,
    create_at timestamp(0) with time zone,
    update_at timestamp(0) with time zone,
    hint character varying(10),
    deleted boolean DEFAULT false NOT NULL
);


ALTER TABLE biz.customer_login_log OWNER TO postgres;

--
-- Name: TABLE customer_login_log; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON TABLE biz.customer_login_log IS '用户登录日志';


--
-- Name: COLUMN customer_login_log.id; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.customer_login_log.id IS '日志主键';


--
-- Name: COLUMN customer_login_log.customer_id; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.customer_login_log.customer_id IS '用户ID';


--
-- Name: COLUMN customer_login_log.username; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.customer_login_log.username IS '用户名';


--
-- Name: COLUMN customer_login_log.login_type; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.customer_login_log.login_type IS '登录类型';


--
-- Name: COLUMN customer_login_log.success; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.customer_login_log.success IS '登录状态';


--
-- Name: COLUMN customer_login_log.error_msg; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.customer_login_log.error_msg IS '失败信息';


--
-- Name: COLUMN customer_login_log.login_ip; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.customer_login_log.login_ip IS '登录ip';


--
-- Name: COLUMN customer_login_log.login_location; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.customer_login_log.login_location IS '登录地址';


--
-- Name: COLUMN customer_login_log.login_time; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.customer_login_log.login_time IS '登录时间';


--
-- Name: COLUMN customer_login_log.trace_id; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.customer_login_log.trace_id IS '链路追踪ID';


--
-- Name: COLUMN customer_login_log.user_agent; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.customer_login_log.user_agent IS '原始UA';


--
-- Name: customer_operate_log; Type: TABLE; Schema: biz; Owner: postgres
--

CREATE TABLE biz.customer_operate_log (
    id bigint NOT NULL,
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
    oper_time timestamp(0) with time zone NOT NULL,
    trace_id character varying(64) NOT NULL,
    creator_id bigint,
    updater_id bigint,
    create_at timestamp(0) with time zone,
    update_at timestamp(0) with time zone,
    hint character varying(10),
    deleted boolean DEFAULT false NOT NULL
);


ALTER TABLE biz.customer_operate_log OWNER TO postgres;

--
-- Name: TABLE customer_operate_log; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON TABLE biz.customer_operate_log IS '用户操作日志记录';


--
-- Name: COLUMN customer_operate_log.id; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.customer_operate_log.id IS '日志主键';


--
-- Name: COLUMN customer_operate_log.title; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.customer_operate_log.title IS '模块标题';


--
-- Name: COLUMN customer_operate_log.sub_title; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.customer_operate_log.sub_title IS '子标题';


--
-- Name: COLUMN customer_operate_log.oper_type; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.customer_operate_log.oper_type IS '操作类型（0其他 1新增 2修改 3删除 4授权 5导入导出）';


--
-- Name: COLUMN customer_operate_log.method; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.customer_operate_log.method IS '方法名称';


--
-- Name: COLUMN customer_operate_log.http_method; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.customer_operate_log.http_method IS '请求方式';


--
-- Name: COLUMN customer_operate_log.customer_id; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.customer_operate_log.customer_id IS '用户ID';


--
-- Name: COLUMN customer_operate_log.username; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.customer_operate_log.username IS '用户名';


--
-- Name: COLUMN customer_operate_log.http_url; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.customer_operate_log.http_url IS '请求URL';


--
-- Name: COLUMN customer_operate_log.oper_ip; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.customer_operate_log.oper_ip IS '主机地址';


--
-- Name: COLUMN customer_operate_log.oper_location; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.customer_operate_log.oper_location IS '操作地点';


--
-- Name: COLUMN customer_operate_log.oper_param; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.customer_operate_log.oper_param IS '请求参数';


--
-- Name: COLUMN customer_operate_log.json_result; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.customer_operate_log.json_result IS '返回参数';


--
-- Name: COLUMN customer_operate_log.success; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.customer_operate_log.success IS '操作状态';


--
-- Name: COLUMN customer_operate_log.error_msg; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.customer_operate_log.error_msg IS '错误消息';


--
-- Name: COLUMN customer_operate_log.cost_time; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.customer_operate_log.cost_time IS '耗时（毫秒）';


--
-- Name: COLUMN customer_operate_log.oper_time; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.customer_operate_log.oper_time IS '操作时间';


--
-- Name: COLUMN customer_operate_log.trace_id; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.customer_operate_log.trace_id IS '链路追踪ID';


--
-- Name: customer_social_info; Type: TABLE; Schema: biz; Owner: postgres
--

CREATE TABLE biz.customer_social_info (
    id bigint NOT NULL,
    customer_id bigint NOT NULL,
    social_uid character varying(64) NOT NULL,
    social_type character varying(10) NOT NULL,
    appid character varying(64),
    creator_id bigint NOT NULL,
    updater_id bigint,
    create_at timestamp(0) with time zone NOT NULL,
    update_at timestamp(0) with time zone,
    hint character varying(10) DEFAULT ''::character varying NOT NULL,
    deleted boolean DEFAULT false NOT NULL
);


ALTER TABLE biz.customer_social_info OWNER TO postgres;

--
-- Name: TABLE customer_social_info; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON TABLE biz.customer_social_info IS '第三方信息';


--
-- Name: COLUMN customer_social_info.social_uid; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.customer_social_info.social_uid IS '三方平台用户唯一标识';


--
-- Name: COLUMN customer_social_info.social_type; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.customer_social_info.social_type IS '三方渠道';


--
-- Name: COLUMN customer_social_info.appid; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.customer_social_info.appid IS '渠道应用标识(小程序/公众号appid)';



--
-- Name: dept_info; Type: TABLE; Schema: biz; Owner: postgres
--

CREATE TABLE biz.dept_info (
    id bigint NOT NULL,
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
-- Name: TABLE dept_info; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON TABLE biz.dept_info IS '部门表';


--
-- Name: COLUMN dept_info.id; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.dept_info.id IS '部门id';


--
-- Name: COLUMN dept_info.parent_id; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.dept_info.parent_id IS '父部门id';


--
-- Name: COLUMN dept_info.ancestors; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.dept_info.ancestors IS '祖级列表';


--
-- Name: COLUMN dept_info.dept_name; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.dept_info.dept_name IS '部门名称';


--
-- Name: COLUMN dept_info.status; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.dept_info.status IS '0代表正常 1代表停用';


--
-- Name: COLUMN dept_info.dept_sort; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.dept_info.dept_sort IS '显示顺序';


--
-- Name: COLUMN dept_info.contact; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.dept_info.contact IS '联系电话';


--
-- Name: COLUMN dept_info.email; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.dept_info.email IS '邮箱';


--
-- Name: COLUMN dept_info.creator_id; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.dept_info.creator_id IS '创建者';


--
-- Name: COLUMN dept_info.updater_id; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.dept_info.updater_id IS '更新者';


--
-- Name: COLUMN dept_info.hint; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.dept_info.hint IS '标签';


--
-- Name: dictionary_info; Type: TABLE; Schema: biz; Owner: postgres
--

CREATE TABLE biz.dictionary_info (
    id bigint NOT NULL,
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
-- Name: COLUMN dictionary_info.id; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.dictionary_info.id IS '字典主键';


--
-- Name: COLUMN dictionary_info.dict_key; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.dictionary_info.dict_key IS '字典键';


--
-- Name: COLUMN dictionary_info.dict_name; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.dictionary_info.dict_name IS '字典名称';


--
-- Name: COLUMN dictionary_info.dict_value; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.dictionary_info.dict_value IS '字典键值';


--
-- Name: COLUMN dictionary_info.remark; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.dictionary_info.remark IS '备注';


--
-- Name: COLUMN dictionary_info.creator_id; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.dictionary_info.creator_id IS '创建者';


--
-- Name: COLUMN dictionary_info.updater_id; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.dictionary_info.updater_id IS '更新者';


--
-- Name: COLUMN dictionary_info.create_at; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.dictionary_info.create_at IS '创建时间';


--
-- Name: COLUMN dictionary_info.update_at; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.dictionary_info.update_at IS '更新时间';


--
-- Name: COLUMN dictionary_info.hint; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.dictionary_info.hint IS '标签';


--
-- Name: tenant_user; Type: TABLE; Schema: biz; Owner: postgres
--

CREATE TABLE biz.tenant_user (
    id bigint NOT NULL,
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


ALTER TABLE biz.tenant_user OWNER TO postgres;

--
-- Name: TABLE tenant_user; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON TABLE biz.tenant_user IS '租户用户表';


--
-- Name: COLUMN tenant_user.id; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.tenant_user.id IS 'ID';


--
-- Name: COLUMN tenant_user.tenant_id; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.tenant_user.tenant_id IS '租户ID';


--
-- Name: COLUMN tenant_user.username; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.tenant_user.username IS '用户名';


--
-- Name: COLUMN tenant_user.phone_number; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.tenant_user.phone_number IS '手机号码';


--
-- Name: COLUMN tenant_user.status; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.tenant_user.status IS '状态值';


--
-- Name: COLUMN tenant_user.gender; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.tenant_user.gender IS '性别（0男 1女 2未知）';


--
-- Name: COLUMN tenant_user.avatar; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.tenant_user.avatar IS '头像地址';


--
-- Name: COLUMN tenant_user.password; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.tenant_user.password IS '密码';


--
-- Name: COLUMN tenant_user.login_ip; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.tenant_user.login_ip IS '登录ip';


--
-- Name: COLUMN tenant_user.login_date; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.tenant_user.login_date IS '登录时间';


--
-- Name: COLUMN tenant_user.remark; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.tenant_user.remark IS '备注';


--
-- Name: COLUMN tenant_user.creator_id; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.tenant_user.creator_id IS '创建者';


--
-- Name: COLUMN tenant_user.updater_id; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.tenant_user.updater_id IS '更新者';


--
-- Name: COLUMN tenant_user.hint; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.tenant_user.hint IS '标签';


--
-- Name: tenant_user_social_info; Type: TABLE; Schema: biz; Owner: postgres
--

CREATE TABLE biz.tenant_user_social_info (
    id bigint NOT NULL,
    tenant_user_id bigint NOT NULL,
    social_uid character varying(32) NOT NULL,
    social_type character varying(10) NOT NULL,
    appid character varying(64),
    creator_id bigint NOT NULL,
    updater_id bigint,
    create_at timestamp(0) with time zone NOT NULL,
    update_at timestamp(0) with time zone,
    hint character varying(10) DEFAULT ''::character varying NOT NULL,
    deleted boolean DEFAULT false NOT NULL
);


ALTER TABLE biz.tenant_user_social_info OWNER TO postgres;

--
-- Name: TABLE tenant_user_social_info; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON TABLE biz.tenant_user_social_info IS '第三方信息';


--
-- Name: COLUMN tenant_user_social_info.social_uid; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.tenant_user_social_info.social_uid IS '三方平台用户唯一标识';


--
-- Name: COLUMN tenant_user_social_info.social_type; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.tenant_user_social_info.social_type IS '三方渠道';


--
-- Name: COLUMN tenant_user_social_info.appid; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.tenant_user_social_info.appid IS '渠道应用标识(小程序/公众号appid)';


--
-- Name: tenant_user_to_role; Type: TABLE; Schema: biz; Owner: postgres
--

CREATE TABLE biz.tenant_user_to_role (
    id bigint NOT NULL,
    tenant_user_id bigint NOT NULL,
    tenant_role_id bigint NOT NULL,
    creator_id bigint NOT NULL,
    updater_id bigint,
    create_at timestamp(0) with time zone NOT NULL,
    update_at timestamp(0) with time zone,
    hint character varying(10) DEFAULT ''::character varying NOT NULL,
    deleted boolean DEFAULT false NOT NULL
);


ALTER TABLE biz.tenant_user_to_role OWNER TO postgres;

--
-- Name: TABLE tenant_user_to_role; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON TABLE biz.tenant_user_to_role IS '租户用户和角色关联表';


--
-- Name: COLUMN tenant_user_to_role.tenant_user_id; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.tenant_user_to_role.tenant_user_id IS '租户用户ID';


--
-- Name: COLUMN tenant_user_to_role.tenant_role_id; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.tenant_user_to_role.tenant_role_id IS '角色ID';


--
-- Name: menu_info; Type: TABLE; Schema: biz; Owner: postgres
--

CREATE TABLE biz.menu_info (
    id bigint NOT NULL,
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
-- Name: TABLE menu_info; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON TABLE biz.menu_info IS '菜单权限表';


--
-- Name: COLUMN menu_info.id; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.menu_info.id IS '菜单ID';


--
-- Name: COLUMN menu_info.menu_name; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.menu_info.menu_name IS '菜单名称';


--
-- Name: COLUMN menu_info.parent_id; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.menu_info.parent_id IS '父菜单ID';


--
-- Name: COLUMN menu_info.menu_sort; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.menu_info.menu_sort IS '显示顺序';


--
-- Name: COLUMN menu_info.path; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.menu_info.path IS '路由地址';


--
-- Name: COLUMN menu_info.component; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.menu_info.component IS '组件路径';


--
-- Name: COLUMN menu_info.query; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.menu_info.query IS '路由参数';


--
-- Name: COLUMN menu_info.menu_type; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.menu_info.menu_type IS '菜单类型（D目录 M菜单 B按钮 I内链 O外链）';


--
-- Name: COLUMN menu_info.status; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.menu_info.status IS '菜单状态（0正常 1停用）';


--
-- Name: COLUMN menu_info.perms; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.menu_info.perms IS '权限标识';


--
-- Name: COLUMN menu_info.icon; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.menu_info.icon IS '菜单图标';


--
-- Name: COLUMN menu_info.cacheable; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.menu_info.cacheable IS '是否缓存';


--
-- Name: COLUMN menu_info.visible; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.menu_info.visible IS '是否显示';


--
-- Name: COLUMN menu_info.scope; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.menu_info.scope IS '菜单域';


--
-- Name: COLUMN menu_info.creator_id; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.menu_info.creator_id IS '创建者ID';


--
-- Name: COLUMN menu_info.hint; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.menu_info.hint IS '标签';


--
-- Name: notification_info; Type: TABLE; Schema: biz; Owner: postgres
--

CREATE TABLE biz.notification_info (
    id bigint NOT NULL,
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
-- Name: TABLE notification_info; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON TABLE biz.notification_info IS '系统通知表';


--
-- Name: COLUMN notification_info.notification_type; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.notification_info.notification_type IS '通知类型（0系统通知 1设备告警）';


--
-- Name: COLUMN notification_info.title; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.notification_info.title IS '标题';


--
-- Name: COLUMN notification_info.content; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.notification_info.content IS '通知内容';


--
-- Name: COLUMN notification_info.create_at; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.notification_info.create_at IS '创建时间';


--
-- Name: COLUMN notification_info.creator_id; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.notification_info.creator_id IS '创建者ID';


--
-- Name: notification_to_admin; Type: TABLE; Schema: biz; Owner: postgres
--

CREATE TABLE biz.notification_to_admin (
    id bigint NOT NULL,
    notification_id bigint NOT NULL,
    admin_id bigint NOT NULL,
    read boolean NOT NULL,
    creator_id bigint NOT NULL,
    updater_id bigint,
    create_at timestamp(0) with time zone NOT NULL,
    update_at timestamp(0) with time zone,
    hint character varying(10) DEFAULT ''::character varying NOT NULL,
    deleted boolean DEFAULT false NOT NULL
);


ALTER TABLE biz.notification_to_admin OWNER TO postgres;

--
-- Name: TABLE notification_to_admin; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON TABLE biz.notification_to_admin IS '通知发送表';


--
-- Name: COLUMN notification_to_admin.notification_id; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.notification_to_admin.notification_id IS '通知ID';


--
-- Name: COLUMN notification_to_admin.admin_id; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.notification_to_admin.admin_id IS '管理员ID';


--
-- Name: COLUMN notification_to_admin.read; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.notification_to_admin.read IS '是否已读';


--
-- Name: notification_to_tenant; Type: TABLE; Schema: biz; Owner: postgres
--

CREATE TABLE biz.notification_to_tenant (
    id bigint NOT NULL,
    notification_id bigint NOT NULL,
    tenant_user_id bigint NOT NULL,
    read boolean NOT NULL,
    creator_id bigint NOT NULL,
    updater_id bigint,
    create_at timestamp(0) with time zone NOT NULL,
    update_at timestamp(0) with time zone,
    hint character varying(10) DEFAULT ''::character varying NOT NULL,
    deleted boolean DEFAULT false NOT NULL
);


ALTER TABLE biz.notification_to_tenant OWNER TO postgres;

--
-- Name: TABLE notification_to_tenant; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON TABLE biz.notification_to_tenant IS '通知发送表';


--
-- Name: COLUMN notification_to_tenant.notification_id; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.notification_to_tenant.notification_id IS '通知ID';


--
-- Name: COLUMN notification_to_tenant.tenant_user_id; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.notification_to_tenant.tenant_user_id IS '租户用户ID';


--
-- Name: COLUMN notification_to_tenant.read; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.notification_to_tenant.read IS '是否已读';


--
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
-- Name: TABLE oauth2_client; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON TABLE biz.oauth2_client IS 'OAUTH2客户端';


--
-- Name: COLUMN oauth2_client.client_name; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.oauth2_client.client_name IS '客户端名称';


--
-- Name: COLUMN oauth2_client.client_id; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.oauth2_client.client_id IS 'APP_ID';


--
-- Name: COLUMN oauth2_client.client_secret; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.oauth2_client.client_secret IS 'APP密钥';


--
-- Name: COLUMN oauth2_client.scopes; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.oauth2_client.scopes IS '权限域';


--
-- Name: COLUMN oauth2_client.authorization_grant_types; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.oauth2_client.authorization_grant_types IS '授权类型';


--
-- Name: COLUMN oauth2_client.redirect_uris; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.oauth2_client.redirect_uris IS '回调地址';


--
-- Name: COLUMN oauth2_client.status; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.oauth2_client.status IS '客户端状态';


--
-- Name: COLUMN oauth2_client.auto_approve; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.oauth2_client.auto_approve IS '是否自动放行';


--
-- Name: COLUMN oauth2_client.hint; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.oauth2_client.hint IS '标签';


--
-- Name: tenant_role; Type: TABLE; Schema: biz; Owner: postgres
--

CREATE TABLE biz.tenant_role (
    id bigint NOT NULL,
    tenant_id bigint NOT NULL,
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


ALTER TABLE biz.tenant_role OWNER TO postgres;

--
-- Name: TABLE tenant_role; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON TABLE biz.tenant_role IS '租户角色信息';


--
-- Name: COLUMN tenant_role.id; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.tenant_role.id IS '角色ID';


--
-- Name: COLUMN tenant_role.tenant_id; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.tenant_role.tenant_id IS '租户ID';


--
-- Name: COLUMN tenant_role.role_name; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.tenant_role.role_name IS '角色名称';


--
-- Name: COLUMN tenant_role.status; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.tenant_role.status IS '状态（0代表正常 1代表停用）';


--
-- Name: COLUMN tenant_role.role_sort; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.tenant_role.role_sort IS '排序';


--
-- Name: COLUMN tenant_role.hint; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.tenant_role.hint IS '标签';


--
-- Name: tenant_role_to_menu; Type: TABLE; Schema: biz; Owner: postgres
--

CREATE TABLE biz.tenant_role_to_menu (
    id bigint NOT NULL,
    tenant_role_id bigint NOT NULL,
    menu_id bigint NOT NULL,
    creator_id bigint NOT NULL,
    updater_id bigint,
    create_at timestamp(0) with time zone NOT NULL,
    update_at timestamp(0) with time zone,
    hint character varying(10) DEFAULT ''::character varying NOT NULL,
    deleted boolean DEFAULT false NOT NULL
);


ALTER TABLE biz.tenant_role_to_menu OWNER TO postgres;

--
-- Name: TABLE tenant_role_to_menu; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON TABLE biz.tenant_role_to_menu IS '角色和菜单关联表';


--
-- Name: COLUMN tenant_role_to_menu.tenant_role_id; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.tenant_role_to_menu.tenant_role_id IS '角色ID';


--
-- Name: COLUMN tenant_role_to_menu.menu_id; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.tenant_role_to_menu.menu_id IS '菜单ID';


--
-- Name: role_info; Type: TABLE; Schema: biz; Owner: postgres
--

CREATE TABLE biz.role_info (
    id bigint NOT NULL,
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


ALTER TABLE biz.role_info OWNER TO postgres;

--
-- Name: COLUMN role_info.id; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.role_info.id IS '角色ID';


--
-- Name: COLUMN role_info.data_scope; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.role_info.data_scope IS '数据范围（1=所有数据权限,2=自定义数据权限,3=本部门数据权限,4=本部门及以下数据权限,5=仅本人数据权限）';


--
-- Name: COLUMN role_info.role_name; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.role_info.role_name IS '角色名称';


--
-- Name: COLUMN role_info.status; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.role_info.status IS '状态（0代表正常 1代表停用）';


--
-- Name: COLUMN role_info.role_sort; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.role_info.role_sort IS '排序';


--
-- Name: COLUMN role_info.hint; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.role_info.hint IS '标签';


--
-- Name: role_inheritance; Type: TABLE; Schema: biz; Owner: postgres
--

CREATE TABLE biz.role_inheritance (
    id bigint NOT NULL,
    role_id bigint NOT NULL,
    child_id bigint NOT NULL,
    creator_id bigint NOT NULL,
    updater_id bigint,
    create_at timestamp(0) with time zone NOT NULL,
    update_at timestamp(0) with time zone,
    hint character varying(10) DEFAULT ''::character varying NOT NULL,
    deleted boolean DEFAULT false NOT NULL
);


ALTER TABLE biz.role_inheritance OWNER TO postgres;

--
-- Name: TABLE role_inheritance; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON TABLE biz.role_inheritance IS '角色继承表';


--
-- Name: COLUMN role_inheritance.role_id; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.role_inheritance.role_id IS '父ID';


--
-- Name: COLUMN role_inheritance.child_id; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.role_inheritance.child_id IS '子ID';


--
-- Name: role_to_dept; Type: TABLE; Schema: biz; Owner: postgres
--

CREATE TABLE biz.role_to_dept (
    id bigint NOT NULL,
    role_id bigint NOT NULL,
    dept_id bigint NOT NULL,
    creator_id bigint NOT NULL,
    updater_id bigint,
    create_at timestamp(0) with time zone NOT NULL,
    update_at timestamp(0) with time zone,
    hint character varying(10) DEFAULT ''::character varying NOT NULL,
    deleted boolean DEFAULT false NOT NULL
);


ALTER TABLE biz.role_to_dept OWNER TO postgres;

--
-- Name: TABLE role_to_dept; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON TABLE biz.role_to_dept IS '角色数据权限关联表';


--
-- Name: COLUMN role_to_dept.role_id; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.role_to_dept.role_id IS '角色id';


--
-- Name: COLUMN role_to_dept.dept_id; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.role_to_dept.dept_id IS '部门id';


--
-- Name: role_to_menu; Type: TABLE; Schema: biz; Owner: postgres
--

CREATE TABLE biz.role_to_menu (
    id bigint NOT NULL,
    role_id bigint NOT NULL,
    menu_id bigint NOT NULL,
    creator_id bigint NOT NULL,
    updater_id bigint,
    create_at timestamp(0) with time zone NOT NULL,
    update_at timestamp(0) with time zone,
    hint character varying(10) DEFAULT ''::character varying NOT NULL,
    deleted boolean DEFAULT false NOT NULL
);


ALTER TABLE biz.role_to_menu OWNER TO postgres;

--
-- Name: TABLE role_to_menu; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON TABLE biz.role_to_menu IS '角色和菜单关联表';


--
-- Name: COLUMN role_to_menu.role_id; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.role_to_menu.role_id IS '角色ID';


--
-- Name: COLUMN role_to_menu.menu_id; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.role_to_menu.menu_id IS '菜单ID';


--
-- Name: tenant_info; Type: TABLE; Schema: biz; Owner: postgres
--

CREATE TABLE biz.tenant_info (
    id bigint NOT NULL,
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
-- Name: TABLE tenant_info; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON TABLE biz.tenant_info IS '租户表';


--
-- Name: COLUMN tenant_info.id; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.tenant_info.id IS '租户编号';


--
-- Name: COLUMN tenant_info.administrator; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.tenant_info.administrator IS '管理员ID';


--
-- Name: COLUMN tenant_info.tenant_type; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.tenant_info.tenant_type IS '租户类型';


--
-- Name: COLUMN tenant_info.contact_phone; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.tenant_info.contact_phone IS '联系电话';


--
-- Name: COLUMN tenant_info.tenant_name; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.tenant_info.tenant_name IS '租户名称';


--
-- Name: COLUMN tenant_info.oper_pwd; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.tenant_info.oper_pwd IS '操作密码';


--
-- Name: COLUMN tenant_info.status; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.tenant_info.status IS '租户状态';


--
-- Name: COLUMN tenant_info.domain; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.tenant_info.domain IS '域名';


--
-- Name: COLUMN tenant_info.remark; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.tenant_info.remark IS '备注';


--
-- Name: COLUMN tenant_info.package_id; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.tenant_info.package_id IS '租户套餐编号';


--
-- Name: COLUMN tenant_info.expire_time; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.tenant_info.expire_time IS '过期时间';


--
-- Name: COLUMN tenant_info.creator_id; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.tenant_info.creator_id IS '创建者';


--
-- Name: COLUMN tenant_info.create_at; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.tenant_info.create_at IS '创建时间';


--
-- Name: COLUMN tenant_info.updater_id; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.tenant_info.updater_id IS '更新者';


--
-- Name: COLUMN tenant_info.update_at; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.tenant_info.update_at IS '更新时间';


--
-- Name: COLUMN tenant_info.hint; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.tenant_info.hint IS '标签';


--
-- Name: tenant_login_log; Type: TABLE; Schema: biz; Owner: postgres
--

CREATE TABLE biz.tenant_login_log (
    id bigint NOT NULL,
    tenant_id bigint NOT NULL,
    tenant_user_id bigint NOT NULL,
    username character varying(32) NOT NULL,
    login_type character varying(32) NOT NULL,
    success boolean NOT NULL,
    error_msg character varying(50) NOT NULL,
    login_ip inet NOT NULL,
    login_location character varying(50) NOT NULL,
    login_time timestamp(0) with time zone NOT NULL,
    trace_id character varying(64) NOT NULL,
    user_agent character varying(200) NOT NULL,
    creator_id bigint,
    updater_id bigint,
    create_at timestamp(0) with time zone,
    update_at timestamp(0) with time zone,
    hint character varying(10),
    deleted boolean DEFAULT false NOT NULL
);


ALTER TABLE biz.tenant_login_log OWNER TO postgres;

--
-- Name: TABLE tenant_login_log; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON TABLE biz.tenant_login_log IS '租户登录日志';


--
-- Name: COLUMN tenant_login_log.tenant_id; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.tenant_login_log.tenant_id IS '租户ID';


--
-- Name: COLUMN tenant_login_log.tenant_user_id; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.tenant_login_log.tenant_user_id IS '用户ID';


--
-- Name: COLUMN tenant_login_log.username; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.tenant_login_log.username IS '用户名';


--
-- Name: COLUMN tenant_login_log.login_type; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.tenant_login_log.login_type IS '登录类型';


--
-- Name: COLUMN tenant_login_log.success; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.tenant_login_log.success IS '登录状态';


--
-- Name: COLUMN tenant_login_log.error_msg; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.tenant_login_log.error_msg IS '失败信息';


--
-- Name: COLUMN tenant_login_log.login_ip; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.tenant_login_log.login_ip IS '登录ip';


--
-- Name: COLUMN tenant_login_log.login_location; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.tenant_login_log.login_location IS '登录地址';


--
-- Name: COLUMN tenant_login_log.login_time; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.tenant_login_log.login_time IS '登录时间';


--
-- Name: tenant_operate_log; Type: TABLE; Schema: biz; Owner: postgres
--

CREATE TABLE biz.tenant_operate_log (
    id bigint NOT NULL,
    title character varying(50) NOT NULL,
    sub_title character varying(50) NOT NULL,
    oper_type character(1) NOT NULL,
    method character varying(200),
    http_method character varying(10),
    tenant_id bigint NOT NULL,
    tenant_user_id bigint NOT NULL,
    username character varying(50) NOT NULL,
    http_url character varying(500),
    oper_ip inet,
    oper_location character varying(100),
    oper_param text,
    json_result text,
    success boolean NOT NULL,
    error_msg text,
    cost_time bigint,
    oper_time timestamp(0) with time zone NOT NULL,
    trace_id character varying(64) NOT NULL,
    creator_id bigint,
    updater_id bigint,
    create_at timestamp(0) with time zone,
    update_at timestamp(0) with time zone,
    hint character varying(10),
    deleted boolean DEFAULT false NOT NULL
);


ALTER TABLE biz.tenant_operate_log OWNER TO postgres;

--
-- Name: TABLE tenant_operate_log; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON TABLE biz.tenant_operate_log IS '租户操作日志';


--
-- Name: COLUMN tenant_operate_log.id; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.tenant_operate_log.id IS '日志主键';


--
-- Name: COLUMN tenant_operate_log.title; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.tenant_operate_log.title IS '模块标题';


--
-- Name: COLUMN tenant_operate_log.sub_title; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.tenant_operate_log.sub_title IS '子标题';


--
-- Name: COLUMN tenant_operate_log.oper_type; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.tenant_operate_log.oper_type IS '操作类型（0其他 1新增 2修改 3删除 4授权 5导入导出）';


--
-- Name: COLUMN tenant_operate_log.method; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.tenant_operate_log.method IS '方法名称';


--
-- Name: COLUMN tenant_operate_log.http_method; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.tenant_operate_log.http_method IS '请求方式';


--
-- Name: COLUMN tenant_operate_log.tenant_id; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.tenant_operate_log.tenant_id IS '租户ID';


--
-- Name: COLUMN tenant_operate_log.tenant_user_id; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.tenant_operate_log.tenant_user_id IS '租户用户ID';


--
-- Name: COLUMN tenant_operate_log.username; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.tenant_operate_log.username IS '用户名';


--
-- Name: COLUMN tenant_operate_log.http_url; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.tenant_operate_log.http_url IS '请求URL';


--
-- Name: COLUMN tenant_operate_log.oper_ip; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.tenant_operate_log.oper_ip IS '主机地址';


--
-- Name: COLUMN tenant_operate_log.oper_location; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.tenant_operate_log.oper_location IS '操作地点';


--
-- Name: COLUMN tenant_operate_log.oper_param; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.tenant_operate_log.oper_param IS '请求参数';


--
-- Name: COLUMN tenant_operate_log.json_result; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.tenant_operate_log.json_result IS '返回参数';


--
-- Name: COLUMN tenant_operate_log.success; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.tenant_operate_log.success IS '操作状态';


--
-- Name: COLUMN tenant_operate_log.error_msg; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.tenant_operate_log.error_msg IS '错误消息';


--
-- Name: COLUMN tenant_operate_log.oper_time; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.tenant_operate_log.oper_time IS '操作时间';


--
-- Name: COLUMN tenant_operate_log.trace_id; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.tenant_operate_log.trace_id IS '链路追踪ID';


--
-- Name: tenant_package_info; Type: TABLE; Schema: biz; Owner: postgres
--

CREATE TABLE biz.tenant_package_info (
    id bigint NOT NULL,
    package_name character varying(20) NOT NULL,
    status character(1) NOT NULL,
    menu_ids bigint[] DEFAULT '{}'::bigint[] NOT NULL,
    remark character varying(200),
    creator_id bigint NOT NULL,
    create_at timestamp(0) with time zone NOT NULL,
    updater_id bigint,
    update_at timestamp(0) with time zone,
    hint character varying(10) DEFAULT ''::character varying NOT NULL,
    account_num_limit smallint DEFAULT 5 NOT NULL,
    role_num_limit smallint DEFAULT 5 NOT NULL,
    deleted boolean DEFAULT false NOT NULL
);


ALTER TABLE biz.tenant_package_info OWNER TO postgres;

--
-- Name: TABLE tenant_package_info; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON TABLE biz.tenant_package_info IS '租户套餐表';


--
-- Name: COLUMN tenant_package_info.id; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.tenant_package_info.id IS '租户套餐id';


--
-- Name: COLUMN tenant_package_info.package_name; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.tenant_package_info.package_name IS '套餐名称';


--
-- Name: COLUMN tenant_package_info.status; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.tenant_package_info.status IS '状态（0正常 1停用）';


--
-- Name: COLUMN tenant_package_info.menu_ids; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.tenant_package_info.menu_ids IS '套餐绑定的菜单';


--
-- Name: COLUMN tenant_package_info.remark; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.tenant_package_info.remark IS '备注';


--
-- Name: COLUMN tenant_package_info.creator_id; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.tenant_package_info.creator_id IS '创建者';


--
-- Name: COLUMN tenant_package_info.create_at; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.tenant_package_info.create_at IS '创建时间';


--
-- Name: COLUMN tenant_package_info.updater_id; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.tenant_package_info.updater_id IS '更新者';


--
-- Name: COLUMN tenant_package_info.update_at; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.tenant_package_info.update_at IS '更新时间';


--
-- Name: COLUMN tenant_package_info.hint; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.tenant_package_info.hint IS '标签';


--
-- Name: COLUMN tenant_package_info.account_num_limit; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.tenant_package_info.account_num_limit IS '租户账号数量限制';


--
-- Name: COLUMN tenant_package_info.role_num_limit; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.tenant_package_info.role_num_limit IS '租户角色数量限制';


-- Data for Name: admin_info; Type: TABLE DATA; Schema: biz; Owner: postgres
--

INSERT INTO biz.admin_info (id, dept_id, username, email, nickname, phone_number, status, gender, avatar, password, login_ip, login_date, remark, creator_id, updater_id, create_at, update_at, hint, deleted) VALUES (100, NULL, 'super', NULL, 'nick_a', '13512312311', '0', '1', 'https://img-s-msn-com.akamaized.net/tenant/amp/entityid/AA1B91c8.img?w=660&h=648&m=6&x=219&y=147&s=204&d=204', '{bcrypt}$2a$10$7JB720yubVSZvUI0rEqK/.VqGOZTH.ulu33dHOiBE8ByOhJIrdAu2', '172.16.8.59', '2026-04-14 09:14:57+08', NULL, 1, 0, '2024-04-17 14:08:55+08', '2026-04-14 09:14:58+08', '', false);


--
-- Data for Name: admin_login_log; Type: TABLE DATA; Schema: biz; Owner: postgres
--



--
-- Data for Name: admin_operate_log; Type: TABLE DATA; Schema: biz; Owner: postgres
--



--
-- Data for Name: admin_to_role; Type: TABLE DATA; Schema: biz; Owner: postgres
--



--
-- Data for Name: announcement_info; Type: TABLE DATA; Schema: biz; Owner: postgres
--



--
-- Data for Name: config_info; Type: TABLE DATA; Schema: biz; Owner: postgres
--

INSERT INTO biz.config_info (id, config_name, config_key, config_value, config_type, built_in, creator_id, create_at, updater_id, update_at, hint, deleted) VALUES (1, '用户管理-账号初始密码', 'sys.user.initPassword', '123456', 'Y', false, 1, '2025-09-17 17:31:01+08', 1, '2025-09-25 15:44:21+08', '', false);


--
-- Data for Name: customer_info; Type: TABLE DATA; Schema: biz; Owner: postgres
--

INSERT INTO biz.customer_info (id, nickname, phone_number, status, gender, avatar, login_ip, login_date, creator_id, updater_id, create_at, update_at, hint, deleted) VALUES (1988138628742279170, '123', NULL, '0', '0', 'http://tmp/f0iJwZfGvBx9bd2d939bf0fbdab283f01e98a4d9bc31.png', '172.16.8.131', '2025-11-20 10:16:17+08', 0, 0, '2025-11-11 14:56:02+08', '2025-11-20 10:16:17+08', '', false);


--
-- Data for Name: customer_login_log; Type: TABLE DATA; Schema: biz; Owner: postgres
--



--
-- Data for Name: customer_operate_log; Type: TABLE DATA; Schema: biz; Owner: postgres
--



--
-- Data for Name: customer_social_info; Type: TABLE DATA; Schema: biz; Owner: postgres
--

INSERT INTO biz.customer_social_info (id, customer_id, social_uid, social_type, creator_id, updater_id, create_at, update_at, hint, deleted) VALUES (1988138628742279170, 1988138628742279170, 'ozNXO5eZpDZXZMInfjKhkkr7LQzs', 'we_xcx', 0, NULL, '2025-11-11 14:56:02+08', NULL, '', false);


--
-- Data for Name: dept_info; Type: TABLE DATA; Schema: biz; Owner: postgres
--



--
-- Data for Name: dictionary_info; Type: TABLE DATA; Schema: biz; Owner: postgres
--

INSERT INTO biz.dictionary_info (id, dict_key, dict_name, dict_value, remark, creator_id, updater_id, create_at, update_at, hint, deleted) VALUES (1905175932909101057, 'user_sex', '用户性别', '[{"label": "男", "value": "0", "tableCls": ""}, {"label": "女", "value": "1", "tableCls": ""}, {"label": "未知", "value": "2", "tableCls": ""}]', NULL, 1, 1, '2024-04-17 14:08:55+08', '2024-11-20 14:29:21+08', '', false);
INSERT INTO biz.dictionary_info (id, dict_key, dict_name, dict_value, remark, creator_id, updater_id, create_at, update_at, hint, deleted) VALUES (1905175933034930178, 'menu_visible', '菜单可见状态', '[{"label": "显示", "value": "true", "tableCls": "primary"}, {"label": "隐藏", "value": "false", "tableCls": "danger"}]', NULL, 1, 1, '2024-04-17 14:08:55+08', '2024-11-20 14:30:10+08', '', false);
INSERT INTO biz.dictionary_info (id, dict_key, dict_name, dict_value, remark, creator_id, updater_id, create_at, update_at, hint, deleted) VALUES (1905175933097844737, 'common_disable', '是否禁用', '[{"label": "正常", "value": "0", "tableCls": "primary"}, {"label": "停用", "value": "1", "tableCls": "danger"}]', NULL, 1, 1, '2024-04-17 14:08:55+08', '2024-11-20 14:34:03+08', '', false);
INSERT INTO biz.dictionary_info (id, dict_key, dict_name, dict_value, remark, creator_id, updater_id, create_at, update_at, hint, deleted) VALUES (1905175933164953603, 'msg_type', '消息类型', '[{"label": "系统公告", "value": "1", "tableCls": "primary"}, {"label": "APP公告", "value": "2", "tableCls": "success"}]', NULL, 1, 1, '2024-04-17 14:08:55+08', '2024-12-16 10:30:13+08', '', false);
INSERT INTO biz.dictionary_info (id, dict_key, dict_name, dict_value, remark, creator_id, updater_id, create_at, update_at, hint, deleted) VALUES (1905175933164953604, 'msg_status', '消息状态', '[{"label": "已发布", "value": "2", "tableCls": "primary"}, {"label": "草稿", "value": "0", "tableCls": "info"}, {"label": "关闭", "value": "1", "tableCls": "danger"}]', NULL, 1, 1, '2024-04-17 14:08:55+08', '2024-12-16 10:30:17+08', '', false);
INSERT INTO biz.dictionary_info (id, dict_key, dict_name, dict_value, remark, creator_id, updater_id, create_at, update_at, hint, deleted) VALUES (1905175933227868164, 'online_status', '设备连接状态', '[{"label": "在线", "value": "true", "tableCls": "success"}, {"label": "离线", "value": "false", "tableCls": "info"}]', NULL, 1, 1, '2024-04-17 14:08:55+08', '2024-12-09 11:28:08+08', '', false);
INSERT INTO biz.dictionary_info (id, dict_key, dict_name, dict_value, remark, creator_id, updater_id, create_at, update_at, hint, deleted) VALUES (1905175933290782723, 'data_scope', '数据权限', '[{"label": "全部数据权限", "value": "1", "tableCls": "default"}, {"label": "自定数据权限", "value": "2", "tableCls": "default"}, {"label": "本部门数据权限", "value": "3", "tableCls": "default"}, {"label": "本部门及以下数据权限", "value": "4", "tableCls": "default"}]', NULL, 1, 1, '2024-04-17 14:08:55+08', '2024-11-20 14:27:20+08', '', false);
INSERT INTO biz.dictionary_info (id, dict_key, dict_name, dict_value, remark, creator_id, updater_id, create_at, update_at, hint, deleted) VALUES (1905175933425000451, 'balance_record_type', '余额变动类型', '[{"label": "收入", "value": "0", "tableCls": "primary"}, {"label": "支出", "value": "1", "tableCls": "danger"}]', NULL, 1, 1, '2024-11-25 16:32:20+08', '2024-11-25 16:32:20+08', '', false);
INSERT INTO biz.dictionary_info (id, dict_key, dict_name, dict_value, remark, creator_id, updater_id, create_at, update_at, hint, deleted) VALUES (1905175933357891585, 'pay_type', '支付方式', '[{"label": "钱包支付", "value": "0", "tableCls": "info"}, {"label": "微信支付", "value": "1", "tableCls": "success"}, {"label": "支付宝", "value": "2", "tableCls": "primary"}]', NULL, 1, 1, '2024-04-17 14:08:55+08', '2025-07-15 17:00:24+08', '', false);
INSERT INTO biz.dictionary_info (id, dict_key, dict_name, dict_value, remark, creator_id, updater_id, create_at, update_at, hint, deleted) VALUES (1905175933290782722, 'pay_certification_status', '支付认证状态', '[{"label": "未认证", "value": "0", "tableCls": "info"}, {"label": "微信支付", "value": "1", "tableCls": "success"}, {"label": "支付宝", "value": "2", "tableCls": "primary"}]', NULL, 1, 1, '2024-04-17 14:08:55+08', '2025-07-15 17:00:46+08', '', false);
INSERT INTO biz.dictionary_info (id, dict_key, dict_name, dict_value, remark, creator_id, updater_id, create_at, update_at, hint, deleted) VALUES (1905175933290782724, 'pay_status', '支付状态', '[{"label": "支付成功", "value": "1", "tableCls": "success"}, {"label": "订单关闭", "value": "2", "tableCls": "info"}, {"label": "未支付", "value": "0", "tableCls": "primary"}, {"label": "支付异常", "value": "3", "tableCls": "danger"}]', NULL, 1, 1, '2024-04-17 14:08:55+08', '2025-07-15 17:01:14+08', '', false);
INSERT INTO biz.dictionary_info (id, dict_key, dict_name, dict_value, remark, creator_id, updater_id, create_at, update_at, hint, deleted) VALUES (1905175933227868163, 'authorization_grant_types', '授权类型', '[{"label": "刷新模式", "value": "refresh_token", "tableCls": "primary"}, {"label": "客户端模式", "value": "client_credentials", "tableCls": "primary"}, {"label": "授权码模式", "value": "authorization_code", "tableCls": "primary"}, {"label": "token交换模式", "value": "urn:ietf:params:oauth:grant-type:token-exchange", "tableCls": "primary"}, {"label": "设备码模式", "value": "urn:ietf:params:oauth:grant-type:device_code", "tableCls": "primary"}]', NULL, 1, 1, '2024-04-17 14:08:55+08', '2025-06-23 16:08:48+08', '', false);
INSERT INTO biz.dictionary_info (id, dict_key, dict_name, dict_value, remark, creator_id, updater_id, create_at, update_at, hint, deleted) VALUES (1905175933034930179, 'menu_type', '菜单类型', '[{"label": "目录", "value": "D", "tableCls": "info"}, {"label": "菜单", "value": "M", "tableCls": "primary"}, {"label": "按钮", "value": "B", "tableCls": "danger"}, {"label": "字段", "value": "F", "tableCls": "warning"}, {"label": "内链", "value": "I", "tableCls": ""}, {"label": "外链", "value": "O", "tableCls": ""}]', NULL, 1, 1, '2024-11-23 15:22:04+08', '2025-09-05 16:08:01+08', '', false);
INSERT INTO biz.dictionary_info (id, dict_key, dict_name, dict_value, remark, creator_id, updater_id, create_at, update_at, hint, deleted) VALUES (1976213210015690754, 'menu_scope', '菜单域', '[{"label": "系统域", "value": "system", "tableCls": ""}, {"label": "租户域", "value": "tenant", "tableCls": ""}]', NULL, 1, 1, '2025-10-09 17:08:40+08', '2025-10-09 17:08:40+08', '', false);
INSERT INTO biz.dictionary_info (id, dict_key, dict_name, dict_value, remark, creator_id, updater_id, create_at, update_at, hint, deleted) VALUES (1905175933227868161, 'operate_type', '操作类型', '[{"label": "其他", "value": "0", "tableCls": "info"}, {"label": "新增", "value": "1", "tableCls": "info"}, {"label": "修改", "value": "2", "tableCls": "info"}, {"label": "删除", "value": "3", "tableCls": "danger"}, {"label": "授权", "value": "4", "tableCls": "primary"}, {"label": "导出", "value": "5", "tableCls": "warning"}, {"label": "导入", "value": "6", "tableCls": "warning"}]', NULL, 1, 1, '2024-04-17 14:08:55+08', '2024-11-20 14:26:30+08', '', false);
INSERT INTO biz.dictionary_info (id, dict_key, dict_name, dict_value, remark, creator_id, updater_id, create_at, update_at, hint, deleted) VALUES (1905175933227868162, 'operate_status', '操作状态', '[{"label": "成功", "value": "0", "tableCls": "primary"}, {"label": "失败", "value": "1", "tableCls": "danger"}]', NULL, 1, 1, '2024-04-17 14:08:55+08', '2024-11-20 14:13:47+08', '', false);


--
-- Data for Name: tenant_user; Type: TABLE DATA; Schema: biz; Owner: postgres
--

INSERT INTO biz.tenant_user (id, tenant_id, username, phone_number, status, gender, avatar, password, login_ip, login_date, remark, creator_id, updater_id, create_at, update_at, hint, deleted) VALUES (1910557183820165120, 1910557183820165122, 'testadmin', NULL, '0', '0', 'http://tmp/WK0iX8BuChGpbd2d939bf0fbdab283f01e98a4d9bc31.png', '{bcrypt}$2a$10$7JB720yubVSZvUI0rEqK/.VqGOZTH.ulu33dHOiBE8ByOhJIrdAu2', '172.16.8.59', '2026-04-14 14:09:01+08', NULL, 1, 0, '2025-04-11 12:55:04+08', '2026-04-14 14:09:01+08', '', false);


--
-- Data for Name: tenant_user_social_info; Type: TABLE DATA; Schema: biz; Owner: postgres
--

INSERT INTO biz.tenant_user_social_info (id, tenant_user_id, social_uid, social_type, appid, creator_id, updater_id, create_at, update_at, hint, deleted) VALUES (1910557183820165120, 1910557183820165120, 'ozNXO5eZpDZXZMInfjKhkkr7LQzs', 'we_xcx', NULL, 1, NULL, '2025-04-11 12:55:04+08', NULL, '', false);


--
-- Data for Name: tenant_user_to_role; Type: TABLE DATA; Schema: biz; Owner: postgres
--



--
-- Data for Name: menu_info; Type: TABLE DATA; Schema: biz; Owner: postgres
--

INSERT INTO biz.menu_info (id, menu_name, parent_id, menu_sort, path, component, query, menu_type, status, perms, icon, cacheable, visible, scope, creator_id, updater_id, create_at, update_at, hint, deleted) VALUES (1980906033277222913, '日志审计', 0, 0, 'audit-log', NULL, NULL, 'D', '0', NULL, 'carbon:catalog-publish', false, true, 'tenant', 1, 1, '2025-10-22 15:56:16+08', '2025-10-22 15:58:02+08', '', false);
INSERT INTO biz.menu_info (id, menu_name, parent_id, menu_sort, path, component, query, menu_type, status, perms, icon, cacheable, visible, scope, creator_id, updater_id, create_at, update_at, hint, deleted) VALUES (1906263415450001129, '重置租户操作密码', 1906263415450000601, 11, '#', NULL, NULL, 'B', '0', 'user-mod:tenant-mng:reset-operpwd', '#', false, true, 'system', 1, 1, '2024-05-26 12:30:16+08', '2025-09-03 16:17:24+08', '', false);
INSERT INTO biz.menu_info (id, menu_name, parent_id, menu_sort, path, component, query, menu_type, status, perms, icon, cacheable, visible, scope, creator_id, updater_id, create_at, update_at, hint, deleted) VALUES (1906263415450000104, '日志审计', 0, 1, 'audit-log', NULL, NULL, 'D', '0', NULL, 'carbon:ibm-knowledge-catalog-premium', false, true, 'system', 1, 1, '2024-05-26 12:30:16+08', '2025-10-22 15:59:16+08', '', false);
INSERT INTO biz.menu_info (id, menu_name, parent_id, menu_sort, path, component, query, menu_type, status, perms, icon, cacheable, visible, scope, creator_id, updater_id, create_at, update_at, hint, deleted) VALUES (1906263415450000001, '消息管理', 0, 80, 'system-mng', NULL, NULL, 'D', '0', NULL, 'carbon:z-systems', false, true, 'system', 1, 1, '2024-05-26 12:30:16+08', '2025-10-30 11:35:27+08', '', false);
INSERT INTO biz.menu_info (id, menu_name, parent_id, menu_sort, path, component, query, menu_type, status, perms, icon, cacheable, visible, scope, creator_id, updater_id, create_at, update_at, hint, deleted) VALUES (1906263415450000302, 'Sentinel控制台', 1906263415450000101, 3, 'http://localhost:8718', NULL, NULL, 'O', '0', '', 'carbon:link', false, true, 'system', 1, 1, '2024-05-26 12:30:16+08', '2025-09-03 13:54:06+08', '', false);
INSERT INTO biz.menu_info (id, menu_name, parent_id, menu_sort, path, component, query, menu_type, status, perms, icon, cacheable, visible, scope, creator_id, updater_id, create_at, update_at, hint, deleted) VALUES (1906263415450000301, '系统接口', 1906263415450000101, 2, 'http://localhost:8080/doc.html', NULL, NULL, 'I', '0', '', 'carbon:link', false, true, 'system', 1, 1, '2024-05-26 12:30:16+08', '2025-09-03 13:54:15+08', '', false);
INSERT INTO biz.menu_info (id, menu_name, parent_id, menu_sort, path, component, query, menu_type, status, perms, icon, cacheable, visible, scope, creator_id, updater_id, create_at, update_at, hint, deleted) VALUES (1906263415450000003, '平台管理', 0, 60, 'platform-mng', NULL, NULL, 'D', '0', NULL, 'carbon:platforms', false, true, 'system', 1, 1, '2024-05-26 12:30:16+08', '2025-10-30 11:35:32+08', '', false);
INSERT INTO biz.menu_info (id, menu_name, parent_id, menu_sort, path, component, query, menu_type, status, perms, icon, cacheable, visible, scope, creator_id, updater_id, create_at, update_at, hint, deleted) VALUES (1983742775738974209, 'pv/uv统计', 1983739365543329794, 0, 'pageview', 'statistics/pageview/index', NULL, 'M', '0', NULL, 'carbon:activity', false, true, 'system', 1, 1, '2025-10-30 11:48:29+08', '2025-10-31 09:23:35+08', '', false);
INSERT INTO biz.menu_info (id, menu_name, parent_id, menu_sort, path, component, query, menu_type, status, perms, icon, cacheable, visible, scope, creator_id, updater_id, create_at, update_at, hint, deleted) VALUES (1963871785836048386, '平台配置', 0, 0, 'develop', NULL, NULL, 'D', '0', NULL, 'carbon:tool-kit', false, true, 'system', 1, 1, '2025-09-05 15:48:15+08', '2025-10-11 09:28:34+08', '', false);
INSERT INTO biz.menu_info (id, menu_name, parent_id, menu_sort, path, component, query, menu_type, status, perms, icon, cacheable, visible, scope, creator_id, updater_id, create_at, update_at, hint, deleted) VALUES (1906263415450001052, '新增参数', 1906263415450000103, 2, '#', NULL, NULL, 'B', '0', 'system-mod:config-mng:add', '#', false, true, 'system', 1, 1, '2024-05-26 12:30:16+08', '2025-10-10 09:11:56+08', '', false);
INSERT INTO biz.menu_info (id, menu_name, parent_id, menu_sort, path, component, query, menu_type, status, perms, icon, cacheable, visible, scope, creator_id, updater_id, create_at, update_at, hint, deleted) VALUES (1906263415450002207, '权限授予', 1906263415450000206, 6, '#', NULL, NULL, 'B', '0', 'user-mod:role-mng:grant-user', '#', false, true, 'system', 1, 1, '2024-05-26 12:30:16+08', '2025-09-03 15:40:20+08', '', false);
INSERT INTO biz.menu_info (id, menu_name, parent_id, menu_sort, path, component, query, menu_type, status, perms, icon, cacheable, visible, scope, creator_id, updater_id, create_at, update_at, hint, deleted) VALUES (1976223833424326657, '修改角色', 1906263415450000206, 4, '#', NULL, NULL, 'B', '0', 'user-mod:role-mng:edit', '#', false, true, 'system', 1, 1, '2025-10-09 17:50:53+08', '2025-10-09 17:50:53+08', '', false);
INSERT INTO biz.menu_info (id, menu_name, parent_id, menu_sort, path, component, query, menu_type, status, perms, icon, cacheable, visible, scope, creator_id, updater_id, create_at, update_at, hint, deleted) VALUES (1976565556872667137, '组织管理', 0, 50, 'organization-mng', NULL, NULL, 'D', '0', NULL, 'carbon:user', false, true, 'tenant', 1, 1, '2025-10-10 16:28:46+08', '2025-10-22 15:56:23+08', '', false);
INSERT INTO biz.menu_info (id, menu_name, parent_id, menu_sort, path, component, query, menu_type, status, perms, icon, cacheable, visible, scope, creator_id, updater_id, create_at, update_at, hint, deleted) VALUES (1976455457936171010, '修改参数', 1906263415450000103, 4, '#', NULL, NULL, 'B', '0', 'system-mod:config-mng:edit', '#', false, true, 'system', 1, 1, '2025-10-10 09:11:17+08', '2025-10-10 09:11:17+08', '', false);
INSERT INTO biz.menu_info (id, menu_name, parent_id, menu_sort, path, component, query, menu_type, status, perms, icon, cacheable, visible, scope, creator_id, updater_id, create_at, update_at, hint, deleted) VALUES (1976455537858633730, '删除参数', 1906263415450000103, 5, '#', NULL, NULL, 'B', '0', 'system-mod:config-mng:remove', '#', false, true, 'system', 1, 1, '2025-10-10 09:11:36+08', '2025-10-10 09:11:36+08', '', false);
INSERT INTO biz.menu_info (id, menu_name, parent_id, menu_sort, path, component, query, menu_type, status, perms, icon, cacheable, visible, scope, creator_id, updater_id, create_at, update_at, hint, deleted) VALUES (1976455824778387458, '添加字典', 1906263415450000102, 0, '#', NULL, NULL, 'B', '0', 'system-mod:dictionary-mng:add', '#', false, true, 'system', 1, 1, '2025-10-10 09:12:44+08', '2025-10-10 09:12:44+08', '', false);
INSERT INTO biz.menu_info (id, menu_name, parent_id, menu_sort, path, component, query, menu_type, status, perms, icon, cacheable, visible, scope, creator_id, updater_id, create_at, update_at, hint, deleted) VALUES (1976455887881691138, '修改字典', 1906263415450000102, 3, '#', NULL, NULL, 'B', '0', 'system-mod:dictionary-mng:edit', '#', false, true, 'system', 1, 1, '2025-10-10 09:12:59+08', '2025-10-10 09:12:59+08', '', false);
INSERT INTO biz.menu_info (id, menu_name, parent_id, menu_sort, path, component, query, menu_type, status, perms, icon, cacheable, visible, scope, creator_id, updater_id, create_at, update_at, hint, deleted) VALUES (1976455969624481794, '删除字典', 1906263415450000102, 4, '#', NULL, NULL, 'B', '0', 'system-mod:dictionary-mng:remove', '#', false, true, 'system', 1, 1, '2025-10-10 09:13:19+08', '2025-10-10 09:13:19+08', '', false);
INSERT INTO biz.menu_info (id, menu_name, parent_id, menu_sort, path, component, query, menu_type, status, perms, icon, cacheable, visible, scope, creator_id, updater_id, create_at, update_at, hint, deleted) VALUES (1976456608446341121, '添加草稿', 1906263415450000100, 0, '#', NULL, NULL, 'B', '0', 'system-mod:announcement-mng:add', '#', false, true, 'system', 1, 1, '2025-10-10 09:15:51+08', '2025-10-10 09:15:51+08', '', false);
INSERT INTO biz.menu_info (id, menu_name, parent_id, menu_sort, path, component, query, menu_type, status, perms, icon, cacheable, visible, scope, creator_id, updater_id, create_at, update_at, hint, deleted) VALUES (1976456667900600322, '修改草稿', 1906263415450000100, 0, '#', NULL, NULL, 'B', '0', 'system-mod:announcement-mng:edit', '#', false, true, 'system', 1, 1, '2025-10-10 09:16:05+08', '2025-10-10 09:16:05+08', '', false);
INSERT INTO biz.menu_info (id, menu_name, parent_id, menu_sort, path, component, query, menu_type, status, perms, icon, cacheable, visible, scope, creator_id, updater_id, create_at, update_at, hint, deleted) VALUES (1976223921466961921, '删除角色', 1906263415450000206, 0, '#', NULL, NULL, 'B', '0', 'user-mod:role-mng:remove', '#', false, true, 'system', 1, 1, '2025-10-09 17:51:14+08', '2025-10-09 17:51:14+08', '', false);
INSERT INTO biz.menu_info (id, menu_name, parent_id, menu_sort, path, component, query, menu_type, status, perms, icon, cacheable, visible, scope, creator_id, updater_id, create_at, update_at, hint, deleted) VALUES (1976224202049122306, '新增部门', 1906263415450000205, 0, '#', NULL, NULL, 'B', '0', 'user-mod:dept-mng:add', '#', false, true, 'system', 1, 1, '2025-10-09 17:52:21+08', '2025-10-09 17:52:21+08', '', false);
INSERT INTO biz.menu_info (id, menu_name, parent_id, menu_sort, path, component, query, menu_type, status, perms, icon, cacheable, visible, scope, creator_id, updater_id, create_at, update_at, hint, deleted) VALUES (1906263415450002039, '修改部门', 1906263415450000205, 3, '#', NULL, NULL, 'B', '0', 'user-mod:dept-mng:edit', '#', false, true, 'system', 1, 1, '2024-05-26 12:30:16+08', '2025-10-09 17:52:34+08', '', false);
INSERT INTO biz.menu_info (id, menu_name, parent_id, menu_sort, path, component, query, menu_type, status, perms, icon, cacheable, visible, scope, creator_id, updater_id, create_at, update_at, hint, deleted) VALUES (1976224332491976706, '删除部门', 1906263415450000205, 5, '#', NULL, NULL, 'B', '0', 'user-mod:dept-mng:remove', '#', false, true, 'system', 1, 1, '2025-10-09 17:52:52+08', '2025-10-09 17:52:52+08', '', false);
INSERT INTO biz.menu_info (id, menu_name, parent_id, menu_sort, path, component, query, menu_type, status, perms, icon, cacheable, visible, scope, creator_id, updater_id, create_at, update_at, hint, deleted) VALUES (1976224466323828738, '部门详细', 1906263415450000205, 7, '#', NULL, NULL, 'B', '0', 'user-mod:dept-mng:query', '#', false, true, 'system', 1, 1, '2025-10-09 17:53:24+08', '2025-10-09 17:53:24+08', '', false);
INSERT INTO biz.menu_info (id, menu_name, parent_id, menu_sort, path, component, query, menu_type, status, perms, icon, cacheable, visible, scope, creator_id, updater_id, create_at, update_at, hint, deleted) VALUES (1976224939848167426, '修改终端', 1906263415450000700, 7, '#', NULL, NULL, 'B', '0', 'user-mod:oauth2client-mng:edit', '#', false, true, 'system', 1, 1, '2025-10-09 17:55:17+08', '2025-10-09 17:55:17+08', '', false);
INSERT INTO biz.menu_info (id, menu_name, parent_id, menu_sort, path, component, query, menu_type, status, perms, icon, cacheable, visible, scope, creator_id, updater_id, create_at, update_at, hint, deleted) VALUES (1976225090838917122, '添加终端', 1906263415450000700, 0, '#', NULL, NULL, 'B', '0', 'user-mod:oauth2client-mng:add', '#', false, true, 'system', 1, 1, '2025-10-09 17:55:53+08', '2025-10-09 17:55:53+08', '', false);
INSERT INTO biz.menu_info (id, menu_name, parent_id, menu_sort, path, component, query, menu_type, status, perms, icon, cacheable, visible, scope, creator_id, updater_id, create_at, update_at, hint, deleted) VALUES (1976225224402333698, '删除终端', 1906263415450000700, 0, '#', NULL, NULL, 'B', '0', 'user-mod:oauth2client-mng:remove', '#', false, true, 'system', 1, 1, '2025-10-09 17:56:25+08', '2025-10-09 17:56:25+08', '', false);
INSERT INTO biz.menu_info (id, menu_name, parent_id, menu_sort, path, component, query, menu_type, status, perms, icon, cacheable, visible, scope, creator_id, updater_id, create_at, update_at, hint, deleted) VALUES (1976225825756475393, '新增菜单', 1906263415450000207, 0, '#', NULL, NULL, 'B', '0', 'user-mod:menu-mng:add', '#', false, true, 'system', 1, 1, '2025-10-09 17:58:48+08', '2025-10-09 17:58:48+08', '', false);
INSERT INTO biz.menu_info (id, menu_name, parent_id, menu_sort, path, component, query, menu_type, status, perms, icon, cacheable, visible, scope, creator_id, updater_id, create_at, update_at, hint, deleted) VALUES (1910569625749024770, '授权角色', 1906263415450000203, 0, '#', NULL, NULL, 'B', '0', 'user-mod:admin-mng:grant-role', '#', false, true, 'system', 1, 100, '2025-04-11 13:44:30+08', '2025-11-07 13:58:46+08', '', false);
INSERT INTO biz.menu_info (id, menu_name, parent_id, menu_sort, path, component, query, menu_type, status, perms, icon, cacheable, visible, scope, creator_id, updater_id, create_at, update_at, hint, deleted) VALUES (1980906374936838146, '登录日志', 1980906033277222913, 10, 'login', 'loginlog-tenant/mng/index', NULL, 'M', '0', 'system-mod:tenant-loginlog-mng:page', 'carbon:login', false, true, 'tenant', 1, 100, '2025-10-22 15:57:38+08', '2025-11-10 11:08:27+08', '', false);
INSERT INTO biz.menu_info (id, menu_name, parent_id, menu_sort, path, component, query, menu_type, status, perms, icon, cacheable, visible, scope, creator_id, updater_id, create_at, update_at, hint, deleted) VALUES (1906263415450000700, '终端管理', 1906263415450000003, 3, 'oauth2client', 'oauth2client/mng/index', NULL, 'M', '0', 'user-mod:oauth2client-mng:page', 'carbon:application', false, true, 'system', 1, 1, '2024-05-26 12:30:16+08', '2025-09-03 17:29:48+08', '', false);
INSERT INTO biz.menu_info (id, menu_name, parent_id, menu_sort, path, component, query, menu_type, status, perms, icon, cacheable, visible, scope, creator_id, updater_id, create_at, update_at, hint, deleted) VALUES (1906263415450000103, '配置管理', 1963871785836048386, 7, 'config', 'config/mng/index', NULL, 'M', '0', 'system-mod:config-mng:page', 'carbon:parameter', false, true, 'system', 1, 1, '2024-05-26 12:30:16+08', '2025-10-14 10:09:25+08', '', false);
INSERT INTO biz.menu_info (id, menu_name, parent_id, menu_sort, path, component, query, menu_type, status, perms, icon, cacheable, visible, scope, creator_id, updater_id, create_at, update_at, hint, deleted) VALUES (1906263415450000151, '登录日志', 1906263415450000104, 2, 'login', 'loginlog-admin/mng/index', NULL, 'M', '0', 'system-mod:admin-loginlog-mng:page', 'carbon:login', false, true, 'system', 1, 100, '2024-05-26 12:30:16+08', '2025-11-07 15:00:45+08', '', false);
INSERT INTO biz.menu_info (id, menu_name, parent_id, menu_sort, path, component, query, menu_type, status, perms, icon, cacheable, visible, scope, creator_id, updater_id, create_at, update_at, hint, deleted) VALUES (1976585906620653569, '角色管理', 1976565556872667137, 0, 'tenant-role', 'tenant-role/mng/index', NULL, 'M', '0', 'user-mod:tenant-role-mng:page', 'carbon:load-balancer-classic', false, true, 'tenant', 1, 1, '2025-10-10 17:49:38+08', '2025-10-15 14:55:02+08', '', false);
INSERT INTO biz.menu_info (id, menu_name, parent_id, menu_sort, path, component, query, menu_type, status, perms, icon, cacheable, visible, scope, creator_id, updater_id, create_at, update_at, hint, deleted) VALUES (1976570103963770881, '租户用户管理', 1976565556872667137, 8, 'tenant-user', 'tenant-user/mng/index', NULL, 'M', '0', 'user-mod:tenant-user-mng:page', 'carbon:user-identification', false, true, 'tenant', 1, 100, '2025-10-10 16:46:50+08', '2025-11-10 11:11:21+08', '', false);
INSERT INTO biz.menu_info (id, menu_name, parent_id, menu_sort, path, component, query, menu_type, status, perms, icon, cacheable, visible, scope, creator_id, updater_id, create_at, update_at, hint, deleted) VALUES (1906263415450000100, '公告管理', 1906263415450000001, 15, 'announcement', 'announcement/mng/index', NULL, 'M', '0', 'system-mod:announcement-mng:page', 'carbon:message-queue', false, true, 'system', 1, 1, '2024-05-26 12:30:16+08', '2025-10-14 10:06:06+08', '', false);
INSERT INTO biz.menu_info (id, menu_name, parent_id, menu_sort, path, component, query, menu_type, status, perms, icon, cacheable, visible, scope, creator_id, updater_id, create_at, update_at, hint, deleted) VALUES (1980906706949554177, '操作日志', 1980906033277222913, 0, 'operate', 'operatelog-tenant/mng/index', NULL, 'M', '0', 'system-mod:tenant-operatelog-mng:page', 'carbon:touch-interaction', false, true, 'tenant', 1, 100, '2025-10-22 15:58:57+08', '2025-11-10 11:08:35+08', '', false);
INSERT INTO biz.menu_info (id, menu_name, parent_id, menu_sort, path, component, query, menu_type, status, perms, icon, cacheable, visible, scope, creator_id, updater_id, create_at, update_at, hint, deleted) VALUES (1976456851288154113, '删除公告', 1906263415450000100, 0, '#', NULL, NULL, 'B', '0', 'system-mod:announcement-mng:remove', '#', false, true, 'system', 1, 1, '2025-10-10 09:16:49+08', '2025-10-10 09:16:49+08', '', false);
INSERT INTO biz.menu_info (id, menu_name, parent_id, menu_sort, path, component, query, menu_type, status, perms, icon, cacheable, visible, scope, creator_id, updater_id, create_at, update_at, hint, deleted) VALUES (1906263415450001210, '终端详情', 1906263415450000700, 1, '#', NULL, NULL, 'B', '0', 'user-mod:oauth2client-mng:query', '#', false, true, 'system', 1, 1, '2024-05-26 12:30:16+08', '2025-09-03 12:42:07+08', '', false);
INSERT INTO biz.menu_info (id, menu_name, parent_id, menu_sort, path, component, query, menu_type, status, perms, icon, cacheable, visible, scope, creator_id, updater_id, create_at, update_at, hint, deleted) VALUES (1976220001646616577, '新增租户套餐', 1906263415450000602, 1, '#', NULL, NULL, 'B', '0', 'user-mod:tenantpackage-mng:add', '#', false, true, 'system', 1, 1, '2025-10-09 17:35:39+08', '2025-10-09 17:35:39+08', '', false);
INSERT INTO biz.menu_info (id, menu_name, parent_id, menu_sort, path, component, query, menu_type, status, perms, icon, cacheable, visible, scope, creator_id, updater_id, create_at, update_at, hint, deleted) VALUES (1976220107171110913, '修改租户套餐', 1906263415450000602, 3, '#', NULL, NULL, 'B', '0', 'user-mod:tenantpackage-mng:edit', '#', false, true, 'system', 1, 1, '2025-10-09 17:36:05+08', '2025-10-09 17:36:05+08', '', false);
INSERT INTO biz.menu_info (id, menu_name, parent_id, menu_sort, path, component, query, menu_type, status, perms, icon, cacheable, visible, scope, creator_id, updater_id, create_at, update_at, hint, deleted) VALUES (1976220234040418306, '删除租户套餐', 1906263415450000602, 0, '#', NULL, NULL, 'B', '0', 'user-mod:tenantpackage-mng:remove', '#', false, true, 'system', 1, 1, '2025-10-09 17:36:35+08', '2025-10-09 17:36:35+08', '', false);
INSERT INTO biz.menu_info (id, menu_name, parent_id, menu_sort, path, component, query, menu_type, status, perms, icon, cacheable, visible, scope, creator_id, updater_id, create_at, update_at, hint, deleted) VALUES (1915322746249367554, '修改信息', 1906272182215585793, 0, '#', NULL, NULL, 'B', '0', 'user-mod:tenant-info:edit', '#', false, true, 'tenant', 1, 1, '2025-04-24 16:31:42+08', '2025-10-09 17:38:05+08', '', false);
INSERT INTO biz.menu_info (id, menu_name, parent_id, menu_sort, path, component, query, menu_type, status, perms, icon, cacheable, visible, scope, creator_id, updater_id, create_at, update_at, hint, deleted) VALUES (1976225899114852354, '修改菜单', 1906263415450000207, 0, '#', NULL, NULL, 'B', '0', 'user-mod:menu-mng:edit', '#', false, true, 'system', 1, 1, '2025-10-09 17:59:06+08', '2025-10-09 17:59:06+08', '', false);
INSERT INTO biz.menu_info (id, menu_name, parent_id, menu_sort, path, component, query, menu_type, status, perms, icon, cacheable, visible, scope, creator_id, updater_id, create_at, update_at, hint, deleted) VALUES (1976226216250372098, '新增租户', 1906263415450000601, 5, '#', NULL, NULL, 'B', '0', 'user-mod:tenant-mng:add', '#', false, true, 'system', 1, 1, '2025-10-09 18:00:21+08', '2025-10-09 18:00:21+08', '', false);
INSERT INTO biz.menu_info (id, menu_name, parent_id, menu_sort, path, component, query, menu_type, status, perms, icon, cacheable, visible, scope, creator_id, updater_id, create_at, update_at, hint, deleted) VALUES (1906263415450001215, '修改密钥', 1906263415450000700, 5, '#', NULL, NULL, 'B', '0', 'user-mod:oauth2client-mng:edit-secret', '#', false, true, 'system', 1, 1, '2024-05-26 12:30:16+08', '2025-09-03 16:16:08+08', '', false);
INSERT INTO biz.menu_info (id, menu_name, parent_id, menu_sort, path, component, query, menu_type, status, perms, icon, cacheable, visible, scope, creator_id, updater_id, create_at, update_at, hint, deleted) VALUES (1983741300921024514, 'api分析', 1983739365543329794, 10, 'apicall', 'statistics/apicall/index', NULL, 'M', '0', NULL, 'carbon:api-1', false, true, 'system', 1, 1, '2025-10-30 11:42:37+08', '2025-10-31 09:22:48+08', '', false);
INSERT INTO biz.menu_info (id, menu_name, parent_id, menu_sort, path, component, query, menu_type, status, perms, icon, cacheable, visible, scope, creator_id, updater_id, create_at, update_at, hint, deleted) VALUES (1983739365543329794, '统计分析', 0, 100, 'statistics', NULL, NULL, 'D', '0', 'gateway-mod:statistics:pvuv', 'carbon:chart-dual-y-axis', false, true, 'system', 1, 1, '2025-10-30 11:34:55+08', '2025-10-31 09:22:59+08', '', false);
INSERT INTO biz.menu_info (id, menu_name, parent_id, menu_sort, path, component, query, menu_type, status, perms, icon, cacheable, visible, scope, creator_id, updater_id, create_at, update_at, hint, deleted) VALUES (1906263415450000304, '服务监控', 1906263415450000101, 5, 'http://localhost:9100/', NULL, NULL, 'O', '0', '', 'carbon:link', false, true, 'system', 1, 1, '2024-05-26 12:30:16+08', '2025-09-03 13:53:38+08', '', false);
INSERT INTO biz.menu_info (id, menu_name, parent_id, menu_sort, path, component, query, menu_type, status, perms, icon, cacheable, visible, scope, creator_id, updater_id, create_at, update_at, hint, deleted) VALUES (1906263415450000101, '控制台入口', 1963871785836048386, 0, 'controller', NULL, NULL, 'D', '0', NULL, 'carbon:dashboard', false, true, 'system', 1, 1, '2024-05-26 12:30:16+08', '2025-09-05 15:53:53+08', '', false);
INSERT INTO biz.menu_info (id, menu_name, parent_id, menu_sort, path, component, query, menu_type, status, perms, icon, cacheable, visible, scope, creator_id, updater_id, create_at, update_at, hint, deleted) VALUES (1906263415450000300, '定时任务', 1906263415450000101, 20, 'http://localhost:9200/xxl-job-admin', NULL, NULL, 'O', '0', '', 'carbon:link', false, true, 'system', 1, 1, '2024-05-26 12:30:16+08', '2025-09-05 16:16:22+08', '', false);
INSERT INTO biz.menu_info (id, menu_name, parent_id, menu_sort, path, component, query, menu_type, status, perms, icon, cacheable, visible, scope, creator_id, updater_id, create_at, update_at, hint, deleted) VALUES (1906263415450000303, 'Nacos控制台', 1906263415450000101, 4, 'http://localhost:8848/nacos', NULL, NULL, 'O', '0', '', 'carbon:link', false, true, 'system', 1, 1, '2024-05-26 12:30:16+08', '2025-09-05 16:16:29+08', '', false);
INSERT INTO biz.menu_info (id, menu_name, parent_id, menu_sort, path, component, query, menu_type, status, perms, icon, cacheable, visible, scope, creator_id, updater_id, create_at, update_at, hint, deleted) VALUES (1906263415450000207, '菜单管理', 1906263415450000003, 50, 'menu', 'menu/mng/index', NULL, 'M', '0', 'user-mod:menu-mng:list', 'carbon:menu', false, true, 'system', 1, 1, '2024-05-26 12:30:16+08', '2025-09-03 13:36:05+08', '', false);
INSERT INTO biz.menu_info (id, menu_name, parent_id, menu_sort, path, component, query, menu_type, status, perms, icon, cacheable, visible, scope, creator_id, updater_id, create_at, update_at, hint, deleted) VALUES (1906263415450000002, '组织管理', 0, 50, 'organization-mng', NULL, NULL, 'D', '0', NULL, 'carbon:user', false, true, 'system', 1, 1, '2024-05-26 12:30:16+08', '2025-10-22 15:09:41+08', '', false);
INSERT INTO biz.menu_info (id, menu_name, parent_id, menu_sort, path, component, query, menu_type, status, perms, icon, cacheable, visible, scope, creator_id, updater_id, create_at, update_at, hint, deleted) VALUES (1976226385717030913, '删除租户', 1906263415450000601, 8, '#', NULL, NULL, 'B', '0', 'user-mod:tenant-mng:remove', '#', false, true, 'system', 1, 1, '2025-10-09 18:01:02+08', '2025-10-09 18:01:02+08', '', false);
INSERT INTO biz.menu_info (id, menu_name, parent_id, menu_sort, path, component, query, menu_type, status, perms, icon, cacheable, visible, scope, creator_id, updater_id, create_at, update_at, hint, deleted) VALUES (1976586082013863937, '新增角色', 1976585906620653569, 0, '#', NULL, NULL, 'B', '0', 'user-mod:tenant-role-mng:add', '#', false, true, 'tenant', 1, 1, '2025-10-10 17:50:20+08', '2025-10-10 17:50:20+08', '', false);
INSERT INTO biz.menu_info (id, menu_name, parent_id, menu_sort, path, component, query, menu_type, status, perms, icon, cacheable, visible, scope, creator_id, updater_id, create_at, update_at, hint, deleted) VALUES (1906263415450001126, '商户提现', 1906263415450001127, 1, '#', NULL, NULL, 'B', '0', 'payment-mod:tenant-balance-info:withdrawal', '#', false, true, 'tenant', 1, 1, '2024-05-26 12:30:16+08', '2025-10-09 17:38:23+08', '', false);
INSERT INTO biz.menu_info (id, menu_name, parent_id, menu_sort, path, component, query, menu_type, status, perms, icon, cacheable, visible, scope, creator_id, updater_id, create_at, update_at, hint, deleted) VALUES (1976223757310291969, '新增角色', 1906263415450000206, 0, '#', NULL, NULL, 'B', '0', 'user-mod:role-mng:add', '#', false, true, 'system', 1, 1, '2025-10-09 17:50:35+08', '2025-10-09 17:50:35+08', '', false);
INSERT INTO biz.menu_info (id, menu_name, parent_id, menu_sort, path, component, query, menu_type, status, perms, icon, cacheable, visible, scope, creator_id, updater_id, create_at, update_at, hint, deleted) VALUES (1906263415450002016, '删除菜单', 1906263415450000207, 4, '#', NULL, NULL, 'B', '0', 'user-mod:menu-mng:remove', '#', false, true, 'system', 1, 1, '2024-05-26 12:30:16+08', '2025-10-09 17:59:20+08', '', false);
INSERT INTO biz.menu_info (id, menu_name, parent_id, menu_sort, path, component, query, menu_type, status, perms, icon, cacheable, visible, scope, creator_id, updater_id, create_at, update_at, hint, deleted) VALUES (1906263415450001133, '修改租户', 1906263415450000601, 2, '#', NULL, NULL, 'B', '0', 'user-mod:tenant-mng:edit', '#', false, true, 'system', 1, 1, '2024-05-26 12:30:16+08', '2025-10-09 18:00:31+08', '', false);
INSERT INTO biz.menu_info (id, menu_name, parent_id, menu_sort, path, component, query, menu_type, status, perms, icon, cacheable, visible, scope, creator_id, updater_id, create_at, update_at, hint, deleted) VALUES (1976586292211408897, '删除角色', 1976585906620653569, 5, '#', NULL, NULL, 'B', '0', 'user-mod:tenant-role-mng:remove', '#', false, true, 'tenant', 1, 1, '2025-10-10 17:51:10+08', '2025-10-10 17:51:10+08', '', false);
INSERT INTO biz.menu_info (id, menu_name, parent_id, menu_sort, path, component, query, menu_type, status, perms, icon, cacheable, visible, scope, creator_id, updater_id, create_at, update_at, hint, deleted) VALUES (1976586196090544129, '修改角色', 1976585906620653569, 3, '#', NULL, NULL, 'B', '0', 'user-mod:tenant-role-mng:edit', '#', false, true, 'tenant', 1, 1, '2025-10-10 17:50:47+08', '2025-10-10 17:51:16+08', '', false);
INSERT INTO biz.menu_info (id, menu_name, parent_id, menu_sort, path, component, query, menu_type, status, perms, icon, cacheable, visible, scope, creator_id, updater_id, create_at, update_at, hint, deleted) VALUES (1906272182215585793, '租户信息', 0, 100, 'tenant-info', 'tenant/info/index', NULL, 'M', '0', 'user-mod:tenant-info', 'carbon:information-filled', false, true, 'tenant', 1, 1, '2025-03-30 17:08:00+08', '2025-10-22 15:11:17+08', '', false);
INSERT INTO biz.menu_info (id, menu_name, parent_id, menu_sort, path, component, query, menu_type, status, perms, icon, cacheable, visible, scope, creator_id, updater_id, create_at, update_at, hint, deleted) VALUES (1906263415450000205, '部门管理', 1906263415450000002, 70, 'dept', 'dept/mng/index', NULL, 'M', '0', 'user-mod:dept-mng:list', 'carbon:departure', false, true, 'system', 1, 1, '2024-05-26 12:30:16+08', '2025-10-11 11:27:19+08', '', false);
INSERT INTO biz.menu_info (id, menu_name, parent_id, menu_sort, path, component, query, menu_type, status, perms, icon, cacheable, visible, scope, creator_id, updater_id, create_at, update_at, hint, deleted) VALUES (1906263415450002062, '重置密码', 1906263415450000203, 7, '#', NULL, NULL, 'B', '0', 'user-mod:admin-mng:resetPwd', '#', false, true, 'system', 1, 100, '2024-05-26 12:30:16+08', '2025-11-07 13:57:48+08', '', false);
INSERT INTO biz.menu_info (id, menu_name, parent_id, menu_sort, path, component, query, menu_type, status, perms, icon, cacheable, visible, scope, creator_id, updater_id, create_at, update_at, hint, deleted) VALUES (1976586772002037762, '删除', 1976570103963770881, 7, '#', NULL, NULL, 'B', '0', 'user-mod:tenant-user-mng:remove', '#', false, true, 'tenant', 1, 100, '2025-10-10 17:53:04+08', '2025-11-10 11:06:54+08', '', false);
INSERT INTO biz.menu_info (id, menu_name, parent_id, menu_sort, path, component, query, menu_type, status, perms, icon, cacheable, visible, scope, creator_id, updater_id, create_at, update_at, hint, deleted) VALUES (1976586698882736130, '授权角色', 1976570103963770881, 5, '#', NULL, NULL, 'B', '0', 'user-mod:tenant-user-mng:grant-role', '#', false, true, 'tenant', 1, 100, '2025-10-10 17:52:47+08', '2025-11-10 11:07:04+08', '', false);
INSERT INTO biz.menu_info (id, menu_name, parent_id, menu_sort, path, component, query, menu_type, status, perms, icon, cacheable, visible, scope, creator_id, updater_id, create_at, update_at, hint, deleted) VALUES (1976586612681400321, '修改', 1976570103963770881, 3, '#', NULL, NULL, 'B', '0', 'user-mod:tenant-user-mng:edit', '#', false, true, 'tenant', 1, 100, '2025-10-10 17:52:26+08', '2025-11-10 11:07:13+08', '', false);
INSERT INTO biz.menu_info (id, menu_name, parent_id, menu_sort, path, component, query, menu_type, status, perms, icon, cacheable, visible, scope, creator_id, updater_id, create_at, update_at, hint, deleted) VALUES (1976586554187636737, '新增', 1976570103963770881, 0, '#', NULL, NULL, 'B', '0', 'user-mod:tenant-user-mng:add', '#', false, true, 'tenant', 1, 100, '2025-10-10 17:52:12+08', '2025-11-10 11:07:25+08', '', false);
INSERT INTO biz.menu_info (id, menu_name, parent_id, menu_sort, path, component, query, menu_type, status, perms, icon, cacheable, visible, scope, creator_id, updater_id, create_at, update_at, hint, deleted) VALUES (1906263415450000102, '字典管理', 1963871785836048386, 6, 'dictionary', 'dictionary/mng/index', NULL, 'M', '0', 'system-mod:dictionary-mng:page', 'carbon:text-vertical-alignment', false, true, 'system', 1, 1, '2024-05-26 12:30:16+08', '2025-10-14 10:09:30+08', '', false);
INSERT INTO biz.menu_info (id, menu_name, parent_id, menu_sort, path, component, query, menu_type, status, perms, icon, cacheable, visible, scope, creator_id, updater_id, create_at, update_at, hint, deleted) VALUES (1906263415450000601, '租户管理', 1906263415450000003, 100, 'tenant', 'tenant/mng/index', NULL, 'M', '0', 'user-mod:tenant-mng:page', 'carbon:id-management', false, true, 'system', 1, 1, '2024-05-26 12:30:16+08', '2025-09-03 17:29:31+08', '', false);
INSERT INTO biz.menu_info (id, menu_name, parent_id, menu_sort, path, component, query, menu_type, status, perms, icon, cacheable, visible, scope, creator_id, updater_id, create_at, update_at, hint, deleted) VALUES (1906263415450002064, '修改账号', 1906263415450000203, 3, '#', NULL, NULL, 'B', '0', 'user-mod:admin-mng:edit', '#', false, true, 'system', 1, 100, '2024-05-26 12:30:16+08', '2025-11-07 14:03:09+08', '', false);
INSERT INTO biz.menu_info (id, menu_name, parent_id, menu_sort, path, component, query, menu_type, status, perms, icon, cacheable, visible, scope, creator_id, updater_id, create_at, update_at, hint, deleted) VALUES (1906263415450002077, '导出', 1906263415450000203, 1, '#', NULL, NULL, 'B', '0', 'user-mod:admin-mng:export', '#', false, true, 'system', 1, 100, '2024-05-26 12:30:16+08', '2025-11-07 14:03:17+08', '', false);
INSERT INTO biz.menu_info (id, menu_name, parent_id, menu_sort, path, component, query, menu_type, status, perms, icon, cacheable, visible, scope, creator_id, updater_id, create_at, update_at, hint, deleted) VALUES (1906263415450002071, '查询账号', 1906263415450000203, 0, '#', NULL, NULL, 'B', '0', 'user-mod:admin-mng:query', '#', false, true, 'system', 1, 100, '2024-05-26 12:30:16+08', '2025-11-07 14:03:33+08', '', false);
INSERT INTO biz.menu_info (id, menu_name, parent_id, menu_sort, path, component, query, menu_type, status, perms, icon, cacheable, visible, scope, creator_id, updater_id, create_at, update_at, hint, deleted) VALUES (1906263415450002072, '新增账号', 1906263415450000203, 1, '#', NULL, NULL, 'B', '0', 'user-mod:admin-mng:add', '#', false, true, 'system', 1, 100, '2024-05-26 12:30:16+08', '2025-11-07 14:04:08+08', '', false);
INSERT INTO biz.menu_info (id, menu_name, parent_id, menu_sort, path, component, query, menu_type, status, perms, icon, cacheable, visible, scope, creator_id, updater_id, create_at, update_at, hint, deleted) VALUES (1906263415450000602, '租户套餐管理', 1906263415450000003, 50, 'tenant-package', 'tenant-package/mng/index', NULL, 'M', '0', 'user-mod:tenantpackage-mng:page', 'carbon:package', false, true, 'system', 1, 1, '2024-05-26 12:30:16+08', '2025-09-03 17:29:37+08', '', false);
INSERT INTO biz.menu_info (id, menu_name, parent_id, menu_sort, path, component, query, menu_type, status, perms, icon, cacheable, visible, scope, creator_id, updater_id, create_at, update_at, hint, deleted) VALUES (1906263415450000206, '角色管理', 1906263415450000002, 99, 'role', 'role/mng/index', NULL, 'M', '0', 'user-mod:role-mng:page', 'carbon:user-role', false, true, 'system', 1, 1, '2024-05-26 12:30:16+08', '2025-10-11 11:27:10+08', '', false);
INSERT INTO biz.menu_info (id, menu_name, parent_id, menu_sort, path, component, query, menu_type, status, perms, icon, cacheable, visible, scope, creator_id, updater_id, create_at, update_at, hint, deleted) VALUES (1906263415450000203, '账号管理', 1906263415450000002, 100, 'admin', 'admin/mng/index', NULL, 'M', '0', 'user-mod:admin-mng:page', 'carbon:user-admin', true, true, 'system', 1, 100, '2024-05-26 12:30:16+08', '2025-11-07 14:01:25+08', '', false);
INSERT INTO biz.menu_info (id, menu_name, parent_id, menu_sort, path, component, query, menu_type, status, perms, icon, cacheable, visible, scope, creator_id, updater_id, create_at, update_at, hint, deleted) VALUES (1906263415450000201, '顾客管理', 1906263415450000003, 1, 'customer', 'customer/mng/index', NULL, 'M', '0', 'user-mod:customer-mng:page', 'carbon:customer', false, true, 'system', 1, 1, '2024-05-26 12:30:16+08', '2025-09-03 17:29:53+08', '', false);
INSERT INTO biz.menu_info (id, menu_name, parent_id, menu_sort, path, component, query, menu_type, status, perms, icon, cacheable, visible, scope, creator_id, updater_id, create_at, update_at, hint, deleted) VALUES (1906263415450000150, '操作日志', 1906263415450000104, 1, 'operate', 'operatelog-admin/mng/index', NULL, 'M', '0', 'system-mod:admin-operatelog-mng:page', 'carbon:touch-interaction', false, true, 'system', 1, 100, '2024-05-26 12:30:16+08', '2025-11-07 15:01:01+08', '', false);
INSERT INTO biz.menu_info (id, menu_name, parent_id, menu_sort, path, component, query, menu_type, status, perms, icon, cacheable, visible, scope, creator_id, updater_id, create_at, update_at, hint, deleted) VALUES (1906263415450001125, '余额流水', 1906263415450000601, 3, '#', NULL, NULL, 'B', '0', 'payment-mod:tenant-balance-mng:record-page', '#', false, true, 'system', 1, 1, '2024-05-26 12:30:16+08', '2025-09-03 16:17:56+08', '', false);
INSERT INTO biz.menu_info (id, menu_name, parent_id, menu_sort, path, component, query, menu_type, status, perms, icon, cacheable, visible, scope, creator_id, updater_id, create_at, update_at, hint, deleted) VALUES (1906263415450001127, '商户余额', 0, 85, '/tenant-balance', 'tenant/balance/index', '', 'M', '0', 'payment-mod:tenant-balance-info', 'carbon:wallet', false, true, 'tenant', 1, 100, '2024-05-26 12:30:16+08', '2026-04-14 10:20:48+08', '', false);
INSERT INTO biz.menu_info (id, menu_name, parent_id, menu_sort, path, component, query, menu_type, status, perms, icon, cacheable, visible, scope, creator_id, updater_id, create_at, update_at, hint, deleted) VALUES (2043932492313976834, '套餐信息', 1906272182215585793, 0, '#', NULL, NULL, 'M', '0', 'user-mod:tenant-package-info', 'carbon:package-node', false, true, 'tenant', 100, 100, '2026-04-14 14:00:55+08', '2026-04-14 14:00:55+08', '', false);


--
-- Data for Name: notification_info; Type: TABLE DATA; Schema: biz; Owner: postgres
--



--
-- Data for Name: notification_to_admin; Type: TABLE DATA; Schema: biz; Owner: postgres
--



--
-- Data for Name: notification_to_tenant; Type: TABLE DATA; Schema: biz; Owner: postgres
--



--
-- Data for Name: oauth2_client; Type: TABLE DATA; Schema: biz; Owner: postgres
--

INSERT INTO biz.oauth2_client (id, client_name, client_id, client_secret, scopes, authorization_grant_types, redirect_uris, status, auto_approve, create_at, creator_id, update_at, updater_id, hint, deleted) VALUES (2, 'oauth2客户端demo', 'oauth_client_demo', '{bcrypt}$2a$10$hK9Sv9kAvXE00fWtkWxzI.Ns4.5SuQteTJAnsFWXChlOWIUZSFYL2', '{read}', '{authorization_code,client_credentials}', '{http://127.0.0.1:3342/login/oauth2/code/auth-center}', '0', false, '2025-05-21 14:13:49+08', 1, '2025-09-01 15:53:53+08', 1, '', false);
INSERT INTO biz.oauth2_client (id, client_name, client_id, client_secret, scopes, authorization_grant_types, redirect_uris, status, auto_approve, create_at, creator_id, update_at, updater_id, hint, deleted) VALUES (1, '系统', 'server', '{bcrypt}$2a$10$hK9Sv9kAvXE00fWtkWxzI.Ns4.5SuQteTJAnsFWXChlOWIUZSFYL2', '{openid,read}', '{authorization_code,urn:ietf:params:oauth:grant-type:device_code,refresh_token}', '{http://localhost:9000/oauth2/authorization_code_callback}', '0', false, '2024-04-17 14:08:54+08', 1, '2025-09-03 11:28:18+08', 1, '', false);


--
-- Data for Name: tenant_role; Type: TABLE DATA; Schema: biz; Owner: postgres
--

INSERT INTO biz.tenant_role (id, tenant_id, role_name, status, role_sort, create_at, creator_id, update_at, updater_id, hint, deleted) VALUES (1978377271113371649, 1910557183820165122, 'CEO', '0', 0, '2025-10-15 16:27:53+08', 1910557183820165120, '2025-10-15 16:27:53+08', 1910557183820165120, '', false);
INSERT INTO biz.tenant_role (id, tenant_id, role_name, status, role_sort, create_at, creator_id, update_at, updater_id, hint, deleted) VALUES (1978377302486765569, 1910557183820165122, 'CFO', '0', 0, '2025-10-15 16:28:00+08', 1910557183820165120, '2025-10-15 16:44:51+08', 1910557183820165120, '', false);


--
-- Data for Name: tenant_role_to_menu; Type: TABLE DATA; Schema: biz; Owner: postgres
--

INSERT INTO biz.tenant_role_to_menu (id, tenant_role_id, menu_id, creator_id, updater_id, create_at, update_at, hint, deleted) VALUES (1, 1978377271113371649, 1906272182215585793, 1910557183820165120, NULL, '2025-10-15 16:27:53+08', NULL, '', false);
INSERT INTO biz.tenant_role_to_menu (id, tenant_role_id, menu_id, creator_id, updater_id, create_at, update_at, hint, deleted) VALUES (2, 1978377271113371649, 1915322746249367554, 1910557183820165120, NULL, '2025-10-15 16:27:53+08', NULL, '', false);
INSERT INTO biz.tenant_role_to_menu (id, tenant_role_id, menu_id, creator_id, updater_id, create_at, update_at, hint, deleted) VALUES (3, 1978377271113371649, 1906263415450001127, 1910557183820165120, NULL, '2025-10-15 16:27:53+08', NULL, '', false);
INSERT INTO biz.tenant_role_to_menu (id, tenant_role_id, menu_id, creator_id, updater_id, create_at, update_at, hint, deleted) VALUES (4, 1978377271113371649, 1906263415450001126, 1910557183820165120, NULL, '2025-10-15 16:27:53+08', NULL, '', false);
INSERT INTO biz.tenant_role_to_menu (id, tenant_role_id, menu_id, creator_id, updater_id, create_at, update_at, hint, deleted) VALUES (5, 1978377302486765569, 1976565556872667137, 1910557183820165120, NULL, '2025-10-15 16:27:53+08', NULL, '', false);
INSERT INTO biz.tenant_role_to_menu (id, tenant_role_id, menu_id, creator_id, updater_id, create_at, update_at, hint, deleted) VALUES (6, 1978377302486765569, 1976570103963770881, 1910557183820165120, NULL, '2025-10-15 16:27:53+08', NULL, '', false);
INSERT INTO biz.tenant_role_to_menu (id, tenant_role_id, menu_id, creator_id, updater_id, create_at, update_at, hint, deleted) VALUES (7, 1978377302486765569, 1976586772002037762, 1910557183820165120, NULL, '2025-10-15 16:27:53+08', NULL, '', false);
INSERT INTO biz.tenant_role_to_menu (id, tenant_role_id, menu_id, creator_id, updater_id, create_at, update_at, hint, deleted) VALUES (8, 1978377302486765569, 1976586698882736130, 1910557183820165120, NULL, '2025-10-15 16:27:53+08', NULL, '', false);
INSERT INTO biz.tenant_role_to_menu (id, tenant_role_id, menu_id, creator_id, updater_id, create_at, update_at, hint, deleted) VALUES (9, 1978377302486765569, 1976586612681400321, 1910557183820165120, NULL, '2025-10-15 16:27:53+08', NULL, '', false);
INSERT INTO biz.tenant_role_to_menu (id, tenant_role_id, menu_id, creator_id, updater_id, create_at, update_at, hint, deleted) VALUES (10, 1978377302486765569, 1976586554187636737, 1910557183820165120, NULL, '2025-10-15 16:27:53+08', NULL, '', false);


--
-- Data for Name: role_info; Type: TABLE DATA; Schema: biz; Owner: postgres
--



--
-- Data for Name: role_inheritance; Type: TABLE DATA; Schema: biz; Owner: postgres
--



--
-- Data for Name: role_to_dept; Type: TABLE DATA; Schema: biz; Owner: postgres
--



--
-- Data for Name: role_to_menu; Type: TABLE DATA; Schema: biz; Owner: postgres
--



--
-- Data for Name: tenant_info; Type: TABLE DATA; Schema: biz; Owner: postgres
--

INSERT INTO biz.tenant_info (id, administrator, tenant_type, contact_phone, tenant_name, oper_pwd, status, domain, remark, package_id, expire_time, creator_id, create_at, updater_id, update_at, hint, deleted) VALUES (1910557183820165122, 1910557183820165120, '0', '', 'test1', '{bcrypt}$2a$10$1UJgROjrOvMKJD4way7dKeBsJuLGVLWGy/pBGooa.sFqfsP3Vrupm', '0', '', NULL, 1773625804122202113, '2026-05-01 00:00:00+08', 1, '2025-04-11 12:55:04+08', 1910557183820165120, '2025-11-18 15:02:28+08', '', false);


--
-- Data for Name: tenant_login_log; Type: TABLE DATA; Schema: biz; Owner: postgres
--

INSERT INTO biz.tenant_login_log (id, tenant_id, tenant_user_id, username, login_type, success, error_msg, login_ip, login_location, login_time, trace_id, user_agent, creator_id, updater_id, create_at, update_at, hint, deleted) VALUES (2039616960859942914, 1910557183820165122, 1910557183820165120, 'testadmin', 'password', true, '', '172.16.8.59', ' 局域网', '2026-04-02 16:12:31+08', '20260402161230015-2-5218330', '{"User-Agent":"Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/146.0.0.0 Safari/537.36 Edg/146.0.0.0"}', NULL, NULL, NULL, NULL, NULL, false);
INSERT INTO biz.tenant_login_log (id, tenant_id, tenant_user_id, username, login_type, success, error_msg, login_ip, login_location, login_time, trace_id, user_agent, creator_id, updater_id, create_at, update_at, hint, deleted) VALUES (2039619680941584385, 1910557183820165122, 1910557183820165120, 'testadmin', 'password', false, '商户已过期，请联系管理员', '172.16.8.59', ' 局域网', '2026-04-02 16:23:20+08', '20260402162319581-24-4431240', '{"User-Agent":"Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/146.0.0.0 Safari/537.36 Edg/146.0.0.0"}', NULL, NULL, NULL, NULL, NULL, false);
INSERT INTO biz.tenant_login_log (id, tenant_id, tenant_user_id, username, login_type, success, error_msg, login_ip, login_location, login_time, trace_id, user_agent, creator_id, updater_id, create_at, update_at, hint, deleted) VALUES (2039875760288346113, 1910557183820165122, 1910557183820165120, 'testadmin', 'password', false, '商户已过期，请联系管理员', '172.16.8.59', ' 局域网', '2026-04-03 09:20:54+08', '20260403092053754-3-1020537', '{"User-Agent":"Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/146.0.0.0 Safari/537.36 Edg/146.0.0.0"}', NULL, NULL, NULL, NULL, NULL, false);
INSERT INTO biz.tenant_login_log (id, tenant_id, tenant_user_id, username, login_type, success, error_msg, login_ip, login_location, login_time, trace_id, user_agent, creator_id, updater_id, create_at, update_at, hint, deleted) VALUES (2039876053499555842, 1910557183820165122, 1910557183820165120, 'testadmin', 'password', true, '', '172.16.8.59', ' 局域网', '2026-04-03 09:22:04+08', '20260403092204414-6-9873025', '{"User-Agent":"Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/146.0.0.0 Safari/537.36 Edg/146.0.0.0"}', NULL, NULL, NULL, NULL, NULL, false);
INSERT INTO biz.tenant_login_log (id, tenant_id, tenant_user_id, username, login_type, success, error_msg, login_ip, login_location, login_time, trace_id, user_agent, creator_id, updater_id, create_at, update_at, hint, deleted) VALUES (2039991299685998593, 1910557183820165122, 1910557183820165120, 'testadmin', 'password', true, '', '172.16.8.59', ' 局域网', '2026-04-03 17:00:01+08', '20260403170000730-9-3938299', '{"User-Agent":"Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/146.0.0.0 Safari/537.36 Edg/146.0.0.0"}', NULL, NULL, NULL, NULL, NULL, false);
INSERT INTO biz.tenant_login_log (id, tenant_id, tenant_user_id, username, login_type, success, error_msg, login_ip, login_location, login_time, trace_id, user_agent, creator_id, updater_id, create_at, update_at, hint, deleted) VALUES (2043603881446875137, 1910557183820165122, 1910557183820165120, 'testadmin', 'password', true, '', '172.16.8.59', ' 局域网', '2026-04-13 16:15:07+08', '20260413161506773-10-5080856', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/146.0.0.0 Safari/537.36 Edg/146.0.0.0', NULL, NULL, NULL, NULL, NULL, false);
INSERT INTO biz.tenant_login_log (id, tenant_id, tenant_user_id, username, login_type, success, error_msg, login_ip, login_location, login_time, trace_id, user_agent, creator_id, updater_id, create_at, update_at, hint, deleted) VALUES (2043605997569712130, 1910557183820165122, 1910557183820165120, 'testadmin', 'password', true, '', '172.16.8.59', ' 局域网', '2026-04-13 16:23:32+08', '20260413162332423-28-8325869', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/146.0.0.0 Safari/537.36 Edg/146.0.0.0', NULL, NULL, NULL, NULL, NULL, false);
INSERT INTO biz.tenant_login_log (id, tenant_id, tenant_user_id, username, login_type, success, error_msg, login_ip, login_location, login_time, trace_id, user_agent, creator_id, updater_id, create_at, update_at, hint, deleted) VALUES (2043607731578871810, 1910557183820165122, 1910557183820165120, 'testadmin', 'password', true, '', '172.16.8.59', ' 局域网', '2026-04-13 16:30:26+08', '20260413163025840-41-9816497', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/146.0.0.0 Safari/537.36 Edg/146.0.0.0', NULL, NULL, NULL, NULL, NULL, false);
INSERT INTO biz.tenant_login_log (id, tenant_id, tenant_user_id, username, login_type, success, error_msg, login_ip, login_location, login_time, trace_id, user_agent, creator_id, updater_id, create_at, update_at, hint, deleted) VALUES (2043627964460965889, 1910557183820165122, 1910557183820165120, 'testadmin', 'password', true, '', '172.16.8.59', ' 局域网', '2026-04-13 17:50:50+08', '20260413175049718-196-4803573', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/146.0.0.0 Safari/537.36 Edg/146.0.0.0', NULL, NULL, NULL, NULL, NULL, false);
INSERT INTO biz.tenant_login_log (id, tenant_id, tenant_user_id, username, login_type, success, error_msg, login_ip, login_location, login_time, trace_id, user_agent, creator_id, updater_id, create_at, update_at, hint, deleted) VALUES (2043860782856294401, 1910557183820165122, 1910557183820165120, 'testadmin', 'password', true, '', '172.16.8.59', ' 局域网', '2026-04-14 09:15:58+08', '20260414091557958-93-6827529', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/146.0.0.0 Safari/537.36 Edg/146.0.0.0', NULL, NULL, NULL, NULL, NULL, false);
INSERT INTO biz.tenant_login_log (id, tenant_id, tenant_user_id, username, login_type, success, error_msg, login_ip, login_location, login_time, trace_id, user_agent, creator_id, updater_id, create_at, update_at, hint, deleted) VALUES (2043892825145315330, 1910557183820165122, 1910557183820165120, 'testadmin', 'password', true, '', '172.16.8.59', ' 局域网', '2026-04-14 11:23:17+08', '20260414112316085-637-4873955', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/146.0.0.0 Safari/537.36 Edg/146.0.0.0', NULL, NULL, NULL, NULL, NULL, false);
INSERT INTO biz.tenant_login_log (id, tenant_id, tenant_user_id, username, login_type, success, error_msg, login_ip, login_location, login_time, trace_id, user_agent, creator_id, updater_id, create_at, update_at, hint, deleted) VALUES (2043898960040529921, 1910557183820165122, 1910557183820165120, 'testadmin', 'password', true, '', '172.16.8.59', ' 局域网', '2026-04-14 11:47:40+08', '20260414114736872-828-8753834', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/146.0.0.0 Safari/537.36 Edg/146.0.0.0', NULL, NULL, NULL, NULL, NULL, false);
INSERT INTO biz.tenant_login_log (id, tenant_id, tenant_user_id, username, login_type, success, error_msg, login_ip, login_location, login_time, trace_id, user_agent, creator_id, updater_id, create_at, update_at, hint, deleted) VALUES (2043934154957017090, 1910557183820165122, 1910557183820165120, 'testadmin', 'password', true, '', '172.16.8.59', ' 局域网', '2026-04-14 14:07:30+08', '20260414140728845-2-4192942', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/146.0.0.0 Safari/537.36 Edg/146.0.0.0', NULL, NULL, NULL, NULL, NULL, false);
INSERT INTO biz.tenant_login_log (id, tenant_id, tenant_user_id, username, login_type, success, error_msg, login_ip, login_location, login_time, trace_id, user_agent, creator_id, updater_id, create_at, update_at, hint, deleted) VALUES (2043934531546796033, 1910557183820165122, 1910557183820165120, 'testadmin', 'password', true, '', '172.16.8.59', ' 局域网', '2026-04-14 14:09:01+08', '20260414140900999-28-6897970', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/146.0.0.0 Safari/537.36 Edg/146.0.0.0', NULL, NULL, NULL, NULL, NULL, false);


--
-- Data for Name: tenant_operate_log; Type: TABLE DATA; Schema: biz; Owner: postgres
--



--
-- Data for Name: tenant_package_info; Type: TABLE DATA; Schema: biz; Owner: postgres
--

INSERT INTO biz.tenant_package_info (id, package_name, status, menu_ids, remark, creator_id, create_at, updater_id, update_at, hint, account_num_limit, role_num_limit, deleted) VALUES (1773625804122202113, '默认套餐', '0', '{1906272182215585793,2043932492313976834,1915322746249367554,1906263415450001127,1906263415450001126,1976565556872667137,1976570103963770881,1976586772002037762,1976586698882736130,1976586612681400321,1976586554187636737,1976585906620653569,1976586292211408897,1976586196090544129,1976586082013863937,1980906033277222913,1980906374936838146,1980906706949554177}', '通用租户套餐', 1, '2024-04-17 14:08:54+08', 100, '2026-04-14 14:07:58+08', '', 5, 5, false);


--
-- Name: admin_info admin_info_pkey; Type: CONSTRAINT; Schema: biz; Owner: postgres
--

ALTER TABLE ONLY biz.admin_info
    ADD CONSTRAINT admin_info_pkey PRIMARY KEY (id);


--
-- Name: admin_login_log admin_login_log_pkey; Type: CONSTRAINT; Schema: biz; Owner: postgres
--

ALTER TABLE ONLY biz.admin_login_log
    ADD CONSTRAINT admin_login_log_pkey PRIMARY KEY (id);


--
-- Name: admin_operate_log admin_operate_log_pkey; Type: CONSTRAINT; Schema: biz; Owner: postgres
--

ALTER TABLE ONLY biz.admin_operate_log
    ADD CONSTRAINT admin_operate_log_pkey PRIMARY KEY (id);


--
-- Name: admin_to_role admin_to_role_pkey; Type: CONSTRAINT; Schema: biz; Owner: postgres
--

ALTER TABLE ONLY biz.admin_to_role
    ADD CONSTRAINT admin_to_role_pkey PRIMARY KEY (id);


--
-- Name: announcement_info announcement_info_pkey; Type: CONSTRAINT; Schema: biz; Owner: postgres
--

ALTER TABLE ONLY biz.announcement_info
    ADD CONSTRAINT announcement_info_pkey PRIMARY KEY (id);


--
-- Name: config_info config_info_pkey; Type: CONSTRAINT; Schema: biz; Owner: postgres
--

ALTER TABLE ONLY biz.config_info
    ADD CONSTRAINT config_info_pkey PRIMARY KEY (id);


--
-- Name: customer_info customer_info_pkey; Type: CONSTRAINT; Schema: biz; Owner: postgres
--

ALTER TABLE ONLY biz.customer_info
    ADD CONSTRAINT customer_info_pkey PRIMARY KEY (id);


--
-- Name: customer_login_log customer_login_log_pkey; Type: CONSTRAINT; Schema: biz; Owner: postgres
--

ALTER TABLE ONLY biz.customer_login_log
    ADD CONSTRAINT customer_login_log_pkey PRIMARY KEY (id);


--
-- Name: customer_operate_log customer_operate_log_pkey; Type: CONSTRAINT; Schema: biz; Owner: postgres
--

ALTER TABLE ONLY biz.customer_operate_log
    ADD CONSTRAINT customer_operate_log_pkey PRIMARY KEY (id);


--
-- Name: customer_social_info customer_social_info_pkey; Type: CONSTRAINT; Schema: biz; Owner: postgres
--

ALTER TABLE ONLY biz.customer_social_info
    ADD CONSTRAINT customer_social_info_pkey PRIMARY KEY (id);



--
-- Name: dept_info dept_info_pkey; Type: CONSTRAINT; Schema: biz; Owner: postgres
--

ALTER TABLE ONLY biz.dept_info
    ADD CONSTRAINT dept_info_pkey PRIMARY KEY (id);


--
-- Name: dictionary_info dictionary_info_pkey; Type: CONSTRAINT; Schema: biz; Owner: postgres
--

ALTER TABLE ONLY biz.dictionary_info
    ADD CONSTRAINT dictionary_info_pkey PRIMARY KEY (id);


--
-- Name: tenant_user tenant_user_pkey; Type: CONSTRAINT; Schema: biz; Owner: postgres
--

ALTER TABLE ONLY biz.tenant_user
    ADD CONSTRAINT tenant_user_pkey PRIMARY KEY (id);


--
-- Name: tenant_user_social_info tenant_user_social_info_pkey; Type: CONSTRAINT; Schema: biz; Owner: postgres
--

ALTER TABLE ONLY biz.tenant_user_social_info
    ADD CONSTRAINT tenant_user_social_info_pkey PRIMARY KEY (id);


--
-- Name: tenant_user_to_role tenant_user_to_role_pkey; Type: CONSTRAINT; Schema: biz; Owner: postgres
--

ALTER TABLE ONLY biz.tenant_user_to_role
    ADD CONSTRAINT tenant_user_to_role_pkey PRIMARY KEY (id);


--
-- Name: menu_info menu_info_pkey; Type: CONSTRAINT; Schema: biz; Owner: postgres
--

ALTER TABLE ONLY biz.menu_info
    ADD CONSTRAINT menu_info_pkey PRIMARY KEY (id);


--
-- Name: notification_info notification_info_pkey; Type: CONSTRAINT; Schema: biz; Owner: postgres
--

ALTER TABLE ONLY biz.notification_info
    ADD CONSTRAINT notification_info_pkey PRIMARY KEY (id);


--
-- Name: notification_to_admin notification_to_admin_pkey; Type: CONSTRAINT; Schema: biz; Owner: postgres
--

ALTER TABLE ONLY biz.notification_to_admin
    ADD CONSTRAINT notification_to_admin_pkey PRIMARY KEY (id);


--
-- Name: notification_to_tenant notification_to_tenant_pkey; Type: CONSTRAINT; Schema: biz; Owner: postgres
--

ALTER TABLE ONLY biz.notification_to_tenant
    ADD CONSTRAINT notification_to_tenant_pkey PRIMARY KEY (id);


--
-- Name: oauth2_client oauth2_client_pkey; Type: CONSTRAINT; Schema: biz; Owner: postgres
--

ALTER TABLE ONLY biz.oauth2_client
    ADD CONSTRAINT oauth2_client_pkey PRIMARY KEY (id);


--
-- Name: tenant_role tenant_role_pkey; Type: CONSTRAINT; Schema: biz; Owner: postgres
--

ALTER TABLE ONLY biz.tenant_role
    ADD CONSTRAINT tenant_role_pkey PRIMARY KEY (id);


--
-- Name: tenant_role_to_menu tenant_role_to_menu_pkey; Type: CONSTRAINT; Schema: biz; Owner: postgres
--

ALTER TABLE ONLY biz.tenant_role_to_menu
    ADD CONSTRAINT tenant_role_to_menu_pkey PRIMARY KEY (id);


--
-- Name: role_info role_info_pkey; Type: CONSTRAINT; Schema: biz; Owner: postgres
--

ALTER TABLE ONLY biz.role_info
    ADD CONSTRAINT role_info_pkey PRIMARY KEY (id);


--
-- Name: role_inheritance role_inheritance_pkey; Type: CONSTRAINT; Schema: biz; Owner: postgres
--

ALTER TABLE ONLY biz.role_inheritance
    ADD CONSTRAINT role_inheritance_pkey PRIMARY KEY (id);


--
-- Name: role_to_dept role_to_dept_pkey; Type: CONSTRAINT; Schema: biz; Owner: postgres
--

ALTER TABLE ONLY biz.role_to_dept
    ADD CONSTRAINT role_to_dept_pkey PRIMARY KEY (id);


--
-- Name: role_to_menu role_to_menu_pkey; Type: CONSTRAINT; Schema: biz; Owner: postgres
--

ALTER TABLE ONLY biz.role_to_menu
    ADD CONSTRAINT role_to_menu_pkey PRIMARY KEY (id);


--
-- Name: tenant_info tenant_info_pkey; Type: CONSTRAINT; Schema: biz; Owner: postgres
--

ALTER TABLE ONLY biz.tenant_info
    ADD CONSTRAINT tenant_info_pkey PRIMARY KEY (id);


--
-- Name: tenant_login_log tenant_login_log_pkey; Type: CONSTRAINT; Schema: biz; Owner: postgres
--

ALTER TABLE ONLY biz.tenant_login_log
    ADD CONSTRAINT tenant_login_log_pkey PRIMARY KEY (id);


--
-- Name: tenant_operate_log tenant_operate_log_pkey; Type: CONSTRAINT; Schema: biz; Owner: postgres
--

ALTER TABLE ONLY biz.tenant_operate_log
    ADD CONSTRAINT tenant_operate_log_pkey PRIMARY KEY (id);


--
-- Name: tenant_package_info tenant_package_info_pkey; Type: CONSTRAINT; Schema: biz; Owner: postgres
--

ALTER TABLE ONLY biz.tenant_package_info
    ADD CONSTRAINT tenant_package_info_pkey PRIMARY KEY (id);



--
-- Name: idx_admin_login_log_login_time; Type: INDEX; Schema: biz; Owner: postgres
--

CREATE INDEX idx_admin_login_log_login_time ON biz.admin_login_log USING brin (login_time);


--
-- Name: idx_admin_operate_log_oper_time; Type: INDEX; Schema: biz; Owner: postgres
--

CREATE INDEX idx_admin_operate_log_oper_time ON biz.admin_operate_log USING brin (oper_time);


--
-- Name: idx_customer_login_log_login_time; Type: INDEX; Schema: biz; Owner: postgres
--

CREATE INDEX idx_customer_login_log_login_time ON biz.customer_login_log USING brin (login_time);


--
-- Name: idx_customer_operate_log_oper_time; Type: INDEX; Schema: biz; Owner: postgres
--

CREATE INDEX idx_customer_operate_log_oper_time ON biz.customer_operate_log USING brin (oper_time);


--

--
-- Name: idx_dept_info_ancestors; Type: INDEX; Schema: biz; Owner: postgres
--

CREATE INDEX idx_dept_info_ancestors ON biz.dept_info USING btree (ancestors) WHERE (deleted = false);


--
-- Name: idx_dept_info_parent_id; Type: INDEX; Schema: biz; Owner: postgres
--

CREATE INDEX idx_dept_info_parent_id ON biz.dept_info USING btree (parent_id) WHERE (deleted = false);


--
-- Name: idx_menu_info_parent_id; Type: INDEX; Schema: biz; Owner: postgres
--

CREATE INDEX idx_menu_info_parent_id ON biz.menu_info USING btree (parent_id) WHERE (deleted = false);


--
-- Name: idx_tenant_login_log_login_time; Type: INDEX; Schema: biz; Owner: postgres
--

CREATE INDEX idx_tenant_login_log_login_time ON biz.tenant_login_log USING brin (login_time);


--
-- Name: idx_tenant_operate_log_oper_time; Type: INDEX; Schema: biz; Owner: postgres
--

CREATE INDEX idx_tenant_operate_log_oper_time ON biz.tenant_operate_log USING brin (oper_time);


--

--

--
-- Name: uk_admin_info_phone_number; Type: INDEX; Schema: biz; Owner: postgres
--

CREATE UNIQUE INDEX uk_admin_info_phone_number ON biz.admin_info USING btree (phone_number) WHERE (deleted = false);


--
-- Name: uk_admin_info_username; Type: INDEX; Schema: biz; Owner: postgres
--

CREATE UNIQUE INDEX uk_admin_info_username ON biz.admin_info USING btree (username) WHERE (deleted = false);


--
-- Name: uk_admin_to_role; Type: INDEX; Schema: biz; Owner: postgres
--

CREATE UNIQUE INDEX uk_admin_to_role ON biz.admin_to_role USING btree (admin_id, role_id) WHERE (deleted = false);


--
-- Name: uk_config_info_config_key; Type: INDEX; Schema: biz; Owner: postgres
--

CREATE UNIQUE INDEX uk_config_info_config_key ON biz.config_info USING btree (config_key) WHERE (deleted = false);


--
-- Name: uk_customer_info_phone_number; Type: INDEX; Schema: biz; Owner: postgres
--

CREATE UNIQUE INDEX uk_customer_info_phone_number ON biz.customer_info USING btree (phone_number) WHERE (deleted = false);


--
-- Name: idx_customer_social_info_customer; Type: INDEX; Schema: biz; Owner: postgres
--

CREATE INDEX idx_customer_social_info_customer ON biz.customer_social_info USING btree (customer_id) WHERE (deleted = false);


--
-- Name: uk_customer_social_info_type_appid_uid; Type: INDEX; Schema: biz; Owner: postgres
--

CREATE UNIQUE INDEX uk_customer_social_info_type_appid_uid ON biz.customer_social_info USING btree (social_type, appid, social_uid) WHERE (deleted = false);


--

--
-- Name: uk_dictionary_info_dict_key; Type: INDEX; Schema: biz; Owner: postgres
--

CREATE UNIQUE INDEX uk_dictionary_info_dict_key ON biz.dictionary_info USING btree (dict_key) WHERE (deleted = false);


--
-- Name: uk_tenant_user_phone_number; Type: INDEX; Schema: biz; Owner: postgres
--

CREATE UNIQUE INDEX uk_tenant_user_phone_number ON biz.tenant_user USING btree (phone_number) WHERE (deleted = false);


--
-- Name: uk_tenant_user_username; Type: INDEX; Schema: biz; Owner: postgres
--

CREATE UNIQUE INDEX uk_tenant_user_username ON biz.tenant_user USING btree (username) WHERE (deleted = false);


--
-- Name: idx_tenant_user_social_info_tenant_user; Type: INDEX; Schema: biz; Owner: postgres
--

CREATE INDEX idx_tenant_user_social_info_tenant_user ON biz.tenant_user_social_info USING btree (tenant_user_id) WHERE (deleted = false);


--
-- Name: uk_tenant_user_social_info_type_appid_uid; Type: INDEX; Schema: biz; Owner: postgres
--

CREATE UNIQUE INDEX uk_tenant_user_social_info_type_appid_uid ON biz.tenant_user_social_info USING btree (social_type, appid, social_uid) WHERE (deleted = false);


--
-- Name: uk_tenant_user_to_role; Type: INDEX; Schema: biz; Owner: postgres
--

CREATE UNIQUE INDEX uk_tenant_user_to_role ON biz.tenant_user_to_role USING btree (tenant_user_id, tenant_role_id) WHERE (deleted = false);


--
-- Name: uk_notification_to_admin; Type: INDEX; Schema: biz; Owner: postgres
--

CREATE UNIQUE INDEX uk_notification_to_admin ON biz.notification_to_admin USING btree (notification_id, admin_id) WHERE (deleted = false);


--
-- Name: uk_notification_to_tenant; Type: INDEX; Schema: biz; Owner: postgres
--

CREATE UNIQUE INDEX uk_notification_to_tenant ON biz.notification_to_tenant USING btree (notification_id, tenant_user_id) WHERE (deleted = false);


--
-- Name: uk_oauth2_client_client_id; Type: INDEX; Schema: biz; Owner: postgres
--

CREATE UNIQUE INDEX uk_oauth2_client_client_id ON biz.oauth2_client USING btree (client_id) WHERE (deleted = false);


--
-- Name: uk_tenant_role_to_menu; Type: INDEX; Schema: biz; Owner: postgres
--

CREATE UNIQUE INDEX uk_tenant_role_to_menu ON biz.tenant_role_to_menu USING btree (tenant_role_id, menu_id) WHERE (deleted = false);


--
-- Name: uk_role_inheritance; Type: INDEX; Schema: biz; Owner: postgres
--

CREATE UNIQUE INDEX uk_role_inheritance ON biz.role_inheritance USING btree (role_id, child_id) WHERE (deleted = false);


--
-- Name: uk_role_to_dept; Type: INDEX; Schema: biz; Owner: postgres
--

CREATE UNIQUE INDEX uk_role_to_dept ON biz.role_to_dept USING btree (role_id, dept_id) WHERE (deleted = false);


--
-- Name: uk_role_to_menu; Type: INDEX; Schema: biz; Owner: postgres
--

CREATE UNIQUE INDEX uk_role_to_menu ON biz.role_to_menu USING btree (role_id, menu_id) WHERE (deleted = false);


--
-- Name: uk_tenant_info_administrator; Type: INDEX; Schema: biz; Owner: postgres
--

CREATE UNIQUE INDEX uk_tenant_info_administrator ON biz.tenant_info USING btree (administrator) WHERE (deleted = false);


--

--
-- PostgreSQL database dump complete
--

