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

DROP DATABASE IF EXISTS config_db;
--
-- Name: config_db; Type: DATABASE; Schema: -; Owner: root
--

CREATE DATABASE config_db WITH TEMPLATE = template0 ENCODING = 'UTF8' LOCALE_PROVIDER = libc LOCALE = 'Chinese (Simplified)_China.936';


ALTER DATABASE config_db OWNER TO root;

\connect config_db

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
-- Name: nacos; Type: SCHEMA; Schema: -; Owner: root
--

CREATE SCHEMA nacos;


ALTER SCHEMA nacos OWNER TO root;

SET default_tablespace = '';

SET default_table_access_method = heap;

--
-- Name: ai_resource; Type: TABLE; Schema: nacos; Owner: root
--

CREATE TABLE nacos.ai_resource (
    id bigint NOT NULL,
    gmt_create timestamp(6) without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
    gmt_modified timestamp(6) without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
    name character varying(256) NOT NULL,
    type character varying(32) NOT NULL,
    c_desc character varying(1024),
    status character varying(32),
    namespace_id character varying(128) DEFAULT ''::character varying NOT NULL,
    biz_tags character varying(1024),
    ext text,
    c_from character varying(256) DEFAULT 'local'::character varying NOT NULL,
    version_info text,
    meta_version bigint DEFAULT 1 NOT NULL,
    scope character varying(16) DEFAULT 'PRIVATE'::character varying NOT NULL,
    owner character varying(128) DEFAULT ''::character varying NOT NULL,
    download_count bigint DEFAULT 0 NOT NULL
);


ALTER TABLE nacos.ai_resource OWNER TO root;

--
-- Name: TABLE ai_resource; Type: COMMENT; Schema: nacos; Owner: root
--

COMMENT ON TABLE nacos.ai_resource IS 'AI资源元数据表';


--
-- Name: COLUMN ai_resource.id; Type: COMMENT; Schema: nacos; Owner: root
--

COMMENT ON COLUMN nacos.ai_resource.id IS 'id';


--
-- Name: COLUMN ai_resource.gmt_create; Type: COMMENT; Schema: nacos; Owner: root
--

COMMENT ON COLUMN nacos.ai_resource.gmt_create IS '创建时间';


--
-- Name: COLUMN ai_resource.gmt_modified; Type: COMMENT; Schema: nacos; Owner: root
--

COMMENT ON COLUMN nacos.ai_resource.gmt_modified IS '修改时间';


--
-- Name: COLUMN ai_resource.name; Type: COMMENT; Schema: nacos; Owner: root
--

COMMENT ON COLUMN nacos.ai_resource.name IS '资源名称';


--
-- Name: COLUMN ai_resource.type; Type: COMMENT; Schema: nacos; Owner: root
--

COMMENT ON COLUMN nacos.ai_resource.type IS '资源类型';


--
-- Name: COLUMN ai_resource.c_desc; Type: COMMENT; Schema: nacos; Owner: root
--

COMMENT ON COLUMN nacos.ai_resource.c_desc IS '资源描述';


--
-- Name: COLUMN ai_resource.status; Type: COMMENT; Schema: nacos; Owner: root
--

COMMENT ON COLUMN nacos.ai_resource.status IS '资源状态';


--
-- Name: COLUMN ai_resource.namespace_id; Type: COMMENT; Schema: nacos; Owner: root
--

COMMENT ON COLUMN nacos.ai_resource.namespace_id IS '命名空间ID';


--
-- Name: COLUMN ai_resource.biz_tags; Type: COMMENT; Schema: nacos; Owner: root
--

COMMENT ON COLUMN nacos.ai_resource.biz_tags IS '业务标签';


--
-- Name: COLUMN ai_resource.ext; Type: COMMENT; Schema: nacos; Owner: root
--

COMMENT ON COLUMN nacos.ai_resource.ext IS '扩展信息(JSON)';


--
-- Name: COLUMN ai_resource.c_from; Type: COMMENT; Schema: nacos; Owner: root
--

COMMENT ON COLUMN nacos.ai_resource.c_from IS '来源标识(导入/同步来源)';


--
-- Name: COLUMN ai_resource.version_info; Type: COMMENT; Schema: nacos; Owner: root
--

COMMENT ON COLUMN nacos.ai_resource.version_info IS '版本信息(JSON)';


--
-- Name: COLUMN ai_resource.meta_version; Type: COMMENT; Schema: nacos; Owner: root
--

COMMENT ON COLUMN nacos.ai_resource.meta_version IS '元数据版本(乐观锁)';


--
-- Name: COLUMN ai_resource.scope; Type: COMMENT; Schema: nacos; Owner: root
--

COMMENT ON COLUMN nacos.ai_resource.scope IS '可见性: PUBLIC/PRIVATE';


--
-- Name: COLUMN ai_resource.owner; Type: COMMENT; Schema: nacos; Owner: root
--

COMMENT ON COLUMN nacos.ai_resource.owner IS '创建者用户名';


--
-- Name: COLUMN ai_resource.download_count; Type: COMMENT; Schema: nacos; Owner: root
--

COMMENT ON COLUMN nacos.ai_resource.download_count IS '下载次数';


--
-- Name: ai_resource_id_seq; Type: SEQUENCE; Schema: nacos; Owner: root
--

CREATE SEQUENCE nacos.ai_resource_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE nacos.ai_resource_id_seq OWNER TO root;

--
-- Name: ai_resource_id_seq; Type: SEQUENCE OWNED BY; Schema: nacos; Owner: root
--

ALTER SEQUENCE nacos.ai_resource_id_seq OWNED BY nacos.ai_resource.id;


--
-- Name: ai_resource_version; Type: TABLE; Schema: nacos; Owner: root
--

CREATE TABLE nacos.ai_resource_version (
    id bigint NOT NULL,
    gmt_create timestamp(6) without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
    gmt_modified timestamp(6) without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
    type character varying(32) NOT NULL,
    author character varying(128),
    name character varying(256) NOT NULL,
    c_desc character varying(1024),
    status character varying(32) NOT NULL,
    version character varying(64) NOT NULL,
    namespace_id character varying(128) DEFAULT ''::character varying NOT NULL,
    storage text,
    publish_pipeline_info text,
    download_count bigint DEFAULT 0 NOT NULL
);


ALTER TABLE nacos.ai_resource_version OWNER TO root;

--
-- Name: TABLE ai_resource_version; Type: COMMENT; Schema: nacos; Owner: root
--

COMMENT ON TABLE nacos.ai_resource_version IS 'AI资源版本表';


--
-- Name: COLUMN ai_resource_version.id; Type: COMMENT; Schema: nacos; Owner: root
--

COMMENT ON COLUMN nacos.ai_resource_version.id IS 'id';


--
-- Name: COLUMN ai_resource_version.gmt_create; Type: COMMENT; Schema: nacos; Owner: root
--

COMMENT ON COLUMN nacos.ai_resource_version.gmt_create IS '创建时间';


--
-- Name: COLUMN ai_resource_version.gmt_modified; Type: COMMENT; Schema: nacos; Owner: root
--

COMMENT ON COLUMN nacos.ai_resource_version.gmt_modified IS '修改时间';


--
-- Name: COLUMN ai_resource_version.type; Type: COMMENT; Schema: nacos; Owner: root
--

COMMENT ON COLUMN nacos.ai_resource_version.type IS '资源类型';


--
-- Name: COLUMN ai_resource_version.author; Type: COMMENT; Schema: nacos; Owner: root
--

COMMENT ON COLUMN nacos.ai_resource_version.author IS '作者';


--
-- Name: COLUMN ai_resource_version.name; Type: COMMENT; Schema: nacos; Owner: root
--

COMMENT ON COLUMN nacos.ai_resource_version.name IS '资源名称';


--
-- Name: COLUMN ai_resource_version.c_desc; Type: COMMENT; Schema: nacos; Owner: root
--

COMMENT ON COLUMN nacos.ai_resource_version.c_desc IS '版本描述';


--
-- Name: COLUMN ai_resource_version.status; Type: COMMENT; Schema: nacos; Owner: root
--

COMMENT ON COLUMN nacos.ai_resource_version.status IS '版本状态';


--
-- Name: COLUMN ai_resource_version.version; Type: COMMENT; Schema: nacos; Owner: root
--

COMMENT ON COLUMN nacos.ai_resource_version.version IS '版本号';


--
-- Name: COLUMN ai_resource_version.namespace_id; Type: COMMENT; Schema: nacos; Owner: root
--

COMMENT ON COLUMN nacos.ai_resource_version.namespace_id IS '命名空间ID';


--
-- Name: COLUMN ai_resource_version.storage; Type: COMMENT; Schema: nacos; Owner: root
--

COMMENT ON COLUMN nacos.ai_resource_version.storage IS '存储信息(JSON)';


--
-- Name: COLUMN ai_resource_version.publish_pipeline_info; Type: COMMENT; Schema: nacos; Owner: root
--

COMMENT ON COLUMN nacos.ai_resource_version.publish_pipeline_info IS '发布流水线信息(JSON)';


--
-- Name: COLUMN ai_resource_version.download_count; Type: COMMENT; Schema: nacos; Owner: root
--

COMMENT ON COLUMN nacos.ai_resource_version.download_count IS '下载次数';


--
-- Name: ai_resource_version_id_seq; Type: SEQUENCE; Schema: nacos; Owner: root
--

CREATE SEQUENCE nacos.ai_resource_version_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE nacos.ai_resource_version_id_seq OWNER TO root;

--
-- Name: ai_resource_version_id_seq; Type: SEQUENCE OWNED BY; Schema: nacos; Owner: root
--

ALTER SEQUENCE nacos.ai_resource_version_id_seq OWNED BY nacos.ai_resource_version.id;


--
-- Name: config_info; Type: TABLE; Schema: nacos; Owner: root
--

CREATE TABLE nacos.config_info (
    id bigint NOT NULL,
    data_id character varying(255) NOT NULL,
    group_id character varying(255),
    content text NOT NULL,
    md5 character varying(32),
    gmt_create timestamp(6) without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
    gmt_modified timestamp(6) without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
    src_user text,
    src_ip character varying(20),
    app_name character varying(128),
    tenant_id character varying(128) DEFAULT ''::character varying NOT NULL,
    c_desc character varying(256),
    c_use character varying(64),
    effect character varying(64),
    type character varying(64),
    c_schema text,
    encrypted_data_key text NOT NULL
);


ALTER TABLE nacos.config_info OWNER TO root;

--
-- Name: TABLE config_info; Type: COMMENT; Schema: nacos; Owner: root
--

COMMENT ON TABLE nacos.config_info IS 'config_info';


--
-- Name: COLUMN config_info.id; Type: COMMENT; Schema: nacos; Owner: root
--

COMMENT ON COLUMN nacos.config_info.id IS 'id';


--
-- Name: COLUMN config_info.data_id; Type: COMMENT; Schema: nacos; Owner: root
--

COMMENT ON COLUMN nacos.config_info.data_id IS 'data_id';


--
-- Name: COLUMN config_info.content; Type: COMMENT; Schema: nacos; Owner: root
--

COMMENT ON COLUMN nacos.config_info.content IS 'content';


--
-- Name: COLUMN config_info.md5; Type: COMMENT; Schema: nacos; Owner: root
--

COMMENT ON COLUMN nacos.config_info.md5 IS 'md5';


--
-- Name: COLUMN config_info.gmt_create; Type: COMMENT; Schema: nacos; Owner: root
--

COMMENT ON COLUMN nacos.config_info.gmt_create IS '创建时间';


--
-- Name: COLUMN config_info.gmt_modified; Type: COMMENT; Schema: nacos; Owner: root
--

COMMENT ON COLUMN nacos.config_info.gmt_modified IS '修改时间';


--
-- Name: COLUMN config_info.src_user; Type: COMMENT; Schema: nacos; Owner: root
--

COMMENT ON COLUMN nacos.config_info.src_user IS 'source user';


--
-- Name: COLUMN config_info.src_ip; Type: COMMENT; Schema: nacos; Owner: root
--

COMMENT ON COLUMN nacos.config_info.src_ip IS 'source ip';


--
-- Name: COLUMN config_info.tenant_id; Type: COMMENT; Schema: nacos; Owner: root
--

COMMENT ON COLUMN nacos.config_info.tenant_id IS '租户字段';


--
-- Name: COLUMN config_info.encrypted_data_key; Type: COMMENT; Schema: nacos; Owner: root
--

COMMENT ON COLUMN nacos.config_info.encrypted_data_key IS '秘钥';


--
-- Name: config_info_gray; Type: TABLE; Schema: nacos; Owner: root
--

CREATE TABLE nacos.config_info_gray (
    id bigint NOT NULL,
    data_id character varying(255) NOT NULL,
    group_id character varying(128) NOT NULL,
    content text NOT NULL,
    md5 character varying(32),
    src_user text,
    src_ip character varying(100),
    gmt_create timestamp(3) without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
    gmt_modified timestamp(3) without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
    app_name character varying(128),
    tenant_id character varying(128) DEFAULT ''::character varying NOT NULL,
    gray_name character varying(128) NOT NULL,
    gray_rule text NOT NULL,
    encrypted_data_key character varying(256) DEFAULT ''::character varying NOT NULL
);


ALTER TABLE nacos.config_info_gray OWNER TO root;

--
-- Name: TABLE config_info_gray; Type: COMMENT; Schema: nacos; Owner: root
--

COMMENT ON TABLE nacos.config_info_gray IS 'config_info_gray';


--
-- Name: COLUMN config_info_gray.id; Type: COMMENT; Schema: nacos; Owner: root
--

COMMENT ON COLUMN nacos.config_info_gray.id IS 'id';


--
-- Name: COLUMN config_info_gray.data_id; Type: COMMENT; Schema: nacos; Owner: root
--

COMMENT ON COLUMN nacos.config_info_gray.data_id IS 'data_id';


--
-- Name: COLUMN config_info_gray.group_id; Type: COMMENT; Schema: nacos; Owner: root
--

COMMENT ON COLUMN nacos.config_info_gray.group_id IS 'group_id';


--
-- Name: COLUMN config_info_gray.content; Type: COMMENT; Schema: nacos; Owner: root
--

COMMENT ON COLUMN nacos.config_info_gray.content IS 'content';


--
-- Name: COLUMN config_info_gray.md5; Type: COMMENT; Schema: nacos; Owner: root
--

COMMENT ON COLUMN nacos.config_info_gray.md5 IS 'md5';


--
-- Name: COLUMN config_info_gray.src_user; Type: COMMENT; Schema: nacos; Owner: root
--

COMMENT ON COLUMN nacos.config_info_gray.src_user IS 'source user';


--
-- Name: COLUMN config_info_gray.src_ip; Type: COMMENT; Schema: nacos; Owner: root
--

COMMENT ON COLUMN nacos.config_info_gray.src_ip IS 'source ip';


--
-- Name: COLUMN config_info_gray.gmt_create; Type: COMMENT; Schema: nacos; Owner: root
--

COMMENT ON COLUMN nacos.config_info_gray.gmt_create IS '创建时间';


--
-- Name: COLUMN config_info_gray.gmt_modified; Type: COMMENT; Schema: nacos; Owner: root
--

COMMENT ON COLUMN nacos.config_info_gray.gmt_modified IS '修改时间';


--
-- Name: COLUMN config_info_gray.app_name; Type: COMMENT; Schema: nacos; Owner: root
--

COMMENT ON COLUMN nacos.config_info_gray.app_name IS 'app_name';


--
-- Name: COLUMN config_info_gray.tenant_id; Type: COMMENT; Schema: nacos; Owner: root
--

COMMENT ON COLUMN nacos.config_info_gray.tenant_id IS '租户字段';


--
-- Name: COLUMN config_info_gray.gray_name; Type: COMMENT; Schema: nacos; Owner: root
--

COMMENT ON COLUMN nacos.config_info_gray.gray_name IS '灰度名称';


--
-- Name: COLUMN config_info_gray.gray_rule; Type: COMMENT; Schema: nacos; Owner: root
--

COMMENT ON COLUMN nacos.config_info_gray.gray_rule IS '灰度规则';


--
-- Name: COLUMN config_info_gray.encrypted_data_key; Type: COMMENT; Schema: nacos; Owner: root
--

COMMENT ON COLUMN nacos.config_info_gray.encrypted_data_key IS '秘钥';


--
-- Name: config_info_gray_id_seq; Type: SEQUENCE; Schema: nacos; Owner: root
--

CREATE SEQUENCE nacos.config_info_gray_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE nacos.config_info_gray_id_seq OWNER TO root;

--
-- Name: config_info_gray_id_seq; Type: SEQUENCE OWNED BY; Schema: nacos; Owner: root
--

ALTER SEQUENCE nacos.config_info_gray_id_seq OWNED BY nacos.config_info_gray.id;


--
-- Name: config_info_id_seq; Type: SEQUENCE; Schema: nacos; Owner: root
--

CREATE SEQUENCE nacos.config_info_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE nacos.config_info_id_seq OWNER TO root;

--
-- Name: config_info_id_seq; Type: SEQUENCE OWNED BY; Schema: nacos; Owner: root
--

ALTER SEQUENCE nacos.config_info_id_seq OWNED BY nacos.config_info.id;


--
-- Name: config_tags_relation; Type: TABLE; Schema: nacos; Owner: root
--

CREATE TABLE nacos.config_tags_relation (
    id bigint NOT NULL,
    tag_name character varying(128) NOT NULL,
    tag_type character varying(64),
    data_id character varying(255) NOT NULL,
    group_id character varying(128) NOT NULL,
    tenant_id character varying(128) DEFAULT ''::character varying NOT NULL,
    nid bigint NOT NULL
);


ALTER TABLE nacos.config_tags_relation OWNER TO root;

--
-- Name: TABLE config_tags_relation; Type: COMMENT; Schema: nacos; Owner: root
--

COMMENT ON TABLE nacos.config_tags_relation IS 'config_tag_relation';


--
-- Name: COLUMN config_tags_relation.id; Type: COMMENT; Schema: nacos; Owner: root
--

COMMENT ON COLUMN nacos.config_tags_relation.id IS 'id';


--
-- Name: COLUMN config_tags_relation.tag_name; Type: COMMENT; Schema: nacos; Owner: root
--

COMMENT ON COLUMN nacos.config_tags_relation.tag_name IS 'tag_name';


--
-- Name: COLUMN config_tags_relation.tag_type; Type: COMMENT; Schema: nacos; Owner: root
--

COMMENT ON COLUMN nacos.config_tags_relation.tag_type IS 'tag_type';


--
-- Name: COLUMN config_tags_relation.data_id; Type: COMMENT; Schema: nacos; Owner: root
--

COMMENT ON COLUMN nacos.config_tags_relation.data_id IS 'data_id';


--
-- Name: COLUMN config_tags_relation.group_id; Type: COMMENT; Schema: nacos; Owner: root
--

COMMENT ON COLUMN nacos.config_tags_relation.group_id IS 'group_id';


--
-- Name: COLUMN config_tags_relation.tenant_id; Type: COMMENT; Schema: nacos; Owner: root
--

COMMENT ON COLUMN nacos.config_tags_relation.tenant_id IS 'tenant_id';


--
-- Name: config_tags_relation_nid_seq; Type: SEQUENCE; Schema: nacos; Owner: root
--

CREATE SEQUENCE nacos.config_tags_relation_nid_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE nacos.config_tags_relation_nid_seq OWNER TO root;

--
-- Name: config_tags_relation_nid_seq; Type: SEQUENCE OWNED BY; Schema: nacos; Owner: root
--

ALTER SEQUENCE nacos.config_tags_relation_nid_seq OWNED BY nacos.config_tags_relation.nid;


--
-- Name: group_capacity; Type: TABLE; Schema: nacos; Owner: root
--

CREATE TABLE nacos.group_capacity (
    id bigint NOT NULL,
    group_id character varying(128) NOT NULL,
    quota integer NOT NULL,
    usage integer NOT NULL,
    max_size integer NOT NULL,
    max_aggr_count integer NOT NULL,
    max_aggr_size integer NOT NULL,
    max_history_count integer DEFAULT 0 NOT NULL,
    gmt_create timestamp(6) without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
    gmt_modified timestamp(6) without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL
);


ALTER TABLE nacos.group_capacity OWNER TO root;

--
-- Name: TABLE group_capacity; Type: COMMENT; Schema: nacos; Owner: root
--

COMMENT ON TABLE nacos.group_capacity IS '集群、各Group容量信息表';


--
-- Name: COLUMN group_capacity.id; Type: COMMENT; Schema: nacos; Owner: root
--

COMMENT ON COLUMN nacos.group_capacity.id IS '主键ID';


--
-- Name: COLUMN group_capacity.group_id; Type: COMMENT; Schema: nacos; Owner: root
--

COMMENT ON COLUMN nacos.group_capacity.group_id IS 'Group ID，空字符表示整个集群';


--
-- Name: COLUMN group_capacity.quota; Type: COMMENT; Schema: nacos; Owner: root
--

COMMENT ON COLUMN nacos.group_capacity.quota IS '配额，0表示使用默认值';


--
-- Name: COLUMN group_capacity.usage; Type: COMMENT; Schema: nacos; Owner: root
--

COMMENT ON COLUMN nacos.group_capacity.usage IS '使用量';


--
-- Name: COLUMN group_capacity.max_size; Type: COMMENT; Schema: nacos; Owner: root
--

COMMENT ON COLUMN nacos.group_capacity.max_size IS '单个配置大小上限，单位为字节，0表示使用默认值';


--
-- Name: COLUMN group_capacity.max_aggr_count; Type: COMMENT; Schema: nacos; Owner: root
--

COMMENT ON COLUMN nacos.group_capacity.max_aggr_count IS '聚合子配置最大个数，，0表示使用默认值';


--
-- Name: COLUMN group_capacity.max_aggr_size; Type: COMMENT; Schema: nacos; Owner: root
--

COMMENT ON COLUMN nacos.group_capacity.max_aggr_size IS '单个聚合数据的子配置大小上限，单位为字节，0表示使用默认值';


--
-- Name: COLUMN group_capacity.max_history_count; Type: COMMENT; Schema: nacos; Owner: root
--

COMMENT ON COLUMN nacos.group_capacity.max_history_count IS '最大变更历史数量';


--
-- Name: COLUMN group_capacity.gmt_create; Type: COMMENT; Schema: nacos; Owner: root
--

COMMENT ON COLUMN nacos.group_capacity.gmt_create IS '创建时间';


--
-- Name: COLUMN group_capacity.gmt_modified; Type: COMMENT; Schema: nacos; Owner: root
--

COMMENT ON COLUMN nacos.group_capacity.gmt_modified IS '修改时间';


--
-- Name: group_capacity_id_seq; Type: SEQUENCE; Schema: nacos; Owner: root
--

CREATE SEQUENCE nacos.group_capacity_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE nacos.group_capacity_id_seq OWNER TO root;

--
-- Name: group_capacity_id_seq; Type: SEQUENCE OWNED BY; Schema: nacos; Owner: root
--

ALTER SEQUENCE nacos.group_capacity_id_seq OWNED BY nacos.group_capacity.id;


--
-- Name: his_config_info; Type: TABLE; Schema: nacos; Owner: root
--

CREATE TABLE nacos.his_config_info (
    id bigint NOT NULL,
    nid bigint NOT NULL,
    data_id character varying(255) NOT NULL,
    group_id character varying(128) NOT NULL,
    app_name character varying(128),
    content text NOT NULL,
    md5 character varying(32),
    gmt_create timestamp(6) without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
    gmt_modified timestamp(6) without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
    src_user text,
    src_ip character varying(20),
    op_type character(10),
    tenant_id character varying(128) DEFAULT ''::character varying NOT NULL,
    encrypted_data_key text NOT NULL,
    publish_type character varying(50) DEFAULT 'formal'::character varying,
    gray_name character varying(50),
    ext_info text
);


ALTER TABLE nacos.his_config_info OWNER TO root;

--
-- Name: TABLE his_config_info; Type: COMMENT; Schema: nacos; Owner: root
--

COMMENT ON TABLE nacos.his_config_info IS '多租户改造';


--
-- Name: COLUMN his_config_info.app_name; Type: COMMENT; Schema: nacos; Owner: root
--

COMMENT ON COLUMN nacos.his_config_info.app_name IS 'app_name';


--
-- Name: COLUMN his_config_info.tenant_id; Type: COMMENT; Schema: nacos; Owner: root
--

COMMENT ON COLUMN nacos.his_config_info.tenant_id IS '租户字段';


--
-- Name: COLUMN his_config_info.encrypted_data_key; Type: COMMENT; Schema: nacos; Owner: root
--

COMMENT ON COLUMN nacos.his_config_info.encrypted_data_key IS '秘钥';


--
-- Name: COLUMN his_config_info.publish_type; Type: COMMENT; Schema: nacos; Owner: root
--

COMMENT ON COLUMN nacos.his_config_info.publish_type IS 'publish type gray or formal';


--
-- Name: COLUMN his_config_info.gray_name; Type: COMMENT; Schema: nacos; Owner: root
--

COMMENT ON COLUMN nacos.his_config_info.gray_name IS 'gray name';


--
-- Name: COLUMN his_config_info.ext_info; Type: COMMENT; Schema: nacos; Owner: root
--

COMMENT ON COLUMN nacos.his_config_info.ext_info IS 'ext info';


--
-- Name: his_config_info_nid_seq; Type: SEQUENCE; Schema: nacos; Owner: root
--

CREATE SEQUENCE nacos.his_config_info_nid_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE nacos.his_config_info_nid_seq OWNER TO root;

--
-- Name: his_config_info_nid_seq; Type: SEQUENCE OWNED BY; Schema: nacos; Owner: root
--

ALTER SEQUENCE nacos.his_config_info_nid_seq OWNED BY nacos.his_config_info.nid;


--
-- Name: permissions; Type: TABLE; Schema: nacos; Owner: root
--

CREATE TABLE nacos.permissions (
    role character varying(50) NOT NULL,
    resource character varying(512) NOT NULL,
    action character varying(8) NOT NULL
);


ALTER TABLE nacos.permissions OWNER TO root;

--
-- Name: pipeline_execution; Type: TABLE; Schema: nacos; Owner: root
--

CREATE TABLE nacos.pipeline_execution (
    execution_id character varying(64) NOT NULL,
    resource_type character varying(32) NOT NULL,
    resource_name character varying(256) NOT NULL,
    namespace_id character varying(128) DEFAULT NULL::character varying,
    version character varying(64) DEFAULT NULL::character varying,
    status character varying(32) NOT NULL,
    pipeline text NOT NULL,
    create_time bigint NOT NULL,
    update_time bigint NOT NULL
);


ALTER TABLE nacos.pipeline_execution OWNER TO root;

--
-- Name: TABLE pipeline_execution; Type: COMMENT; Schema: nacos; Owner: root
--

COMMENT ON TABLE nacos.pipeline_execution IS 'AI资源发布审核Pipeline执行记录';


--
-- Name: COLUMN pipeline_execution.execution_id; Type: COMMENT; Schema: nacos; Owner: root
--

COMMENT ON COLUMN nacos.pipeline_execution.execution_id IS '执行ID';


--
-- Name: COLUMN pipeline_execution.resource_type; Type: COMMENT; Schema: nacos; Owner: root
--

COMMENT ON COLUMN nacos.pipeline_execution.resource_type IS '资源类型';


--
-- Name: COLUMN pipeline_execution.resource_name; Type: COMMENT; Schema: nacos; Owner: root
--

COMMENT ON COLUMN nacos.pipeline_execution.resource_name IS '资源名称';


--
-- Name: COLUMN pipeline_execution.namespace_id; Type: COMMENT; Schema: nacos; Owner: root
--

COMMENT ON COLUMN nacos.pipeline_execution.namespace_id IS '命名空间ID';


--
-- Name: COLUMN pipeline_execution.version; Type: COMMENT; Schema: nacos; Owner: root
--

COMMENT ON COLUMN nacos.pipeline_execution.version IS '版本';


--
-- Name: COLUMN pipeline_execution.status; Type: COMMENT; Schema: nacos; Owner: root
--

COMMENT ON COLUMN nacos.pipeline_execution.status IS '执行状态';


--
-- Name: COLUMN pipeline_execution.pipeline; Type: COMMENT; Schema: nacos; Owner: root
--

COMMENT ON COLUMN nacos.pipeline_execution.pipeline IS 'pipeline节点结果JSON';


--
-- Name: COLUMN pipeline_execution.create_time; Type: COMMENT; Schema: nacos; Owner: root
--

COMMENT ON COLUMN nacos.pipeline_execution.create_time IS '创建时间';


--
-- Name: COLUMN pipeline_execution.update_time; Type: COMMENT; Schema: nacos; Owner: root
--

COMMENT ON COLUMN nacos.pipeline_execution.update_time IS '修改时间';


--
-- Name: roles; Type: TABLE; Schema: nacos; Owner: root
--

CREATE TABLE nacos.roles (
    username character varying(50) NOT NULL,
    role character varying(50) NOT NULL
);


ALTER TABLE nacos.roles OWNER TO root;

--
-- Name: tenant_capacity; Type: TABLE; Schema: nacos; Owner: root
--

CREATE TABLE nacos.tenant_capacity (
    id bigint NOT NULL,
    tenant_id character varying(128) NOT NULL,
    quota integer NOT NULL,
    usage integer NOT NULL,
    max_size integer NOT NULL,
    max_aggr_count integer NOT NULL,
    max_aggr_size integer NOT NULL,
    max_history_count integer DEFAULT 0 NOT NULL,
    gmt_create timestamp(6) without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
    gmt_modified timestamp(6) without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL
);


ALTER TABLE nacos.tenant_capacity OWNER TO root;

--
-- Name: TABLE tenant_capacity; Type: COMMENT; Schema: nacos; Owner: root
--

COMMENT ON TABLE nacos.tenant_capacity IS '租户容量信息表';


--
-- Name: COLUMN tenant_capacity.id; Type: COMMENT; Schema: nacos; Owner: root
--

COMMENT ON COLUMN nacos.tenant_capacity.id IS '主键ID';


--
-- Name: COLUMN tenant_capacity.tenant_id; Type: COMMENT; Schema: nacos; Owner: root
--

COMMENT ON COLUMN nacos.tenant_capacity.tenant_id IS 'Tenant ID';


--
-- Name: COLUMN tenant_capacity.quota; Type: COMMENT; Schema: nacos; Owner: root
--

COMMENT ON COLUMN nacos.tenant_capacity.quota IS '配额，0表示使用默认值';


--
-- Name: COLUMN tenant_capacity.usage; Type: COMMENT; Schema: nacos; Owner: root
--

COMMENT ON COLUMN nacos.tenant_capacity.usage IS '使用量';


--
-- Name: COLUMN tenant_capacity.max_size; Type: COMMENT; Schema: nacos; Owner: root
--

COMMENT ON COLUMN nacos.tenant_capacity.max_size IS '单个配置大小上限，单位为字节，0表示使用默认值';


--
-- Name: COLUMN tenant_capacity.max_aggr_count; Type: COMMENT; Schema: nacos; Owner: root
--

COMMENT ON COLUMN nacos.tenant_capacity.max_aggr_count IS '聚合子配置最大个数';


--
-- Name: COLUMN tenant_capacity.max_aggr_size; Type: COMMENT; Schema: nacos; Owner: root
--

COMMENT ON COLUMN nacos.tenant_capacity.max_aggr_size IS '单个聚合数据的子配置大小上限，单位为字节，0表示使用默认值';


--
-- Name: COLUMN tenant_capacity.max_history_count; Type: COMMENT; Schema: nacos; Owner: root
--

COMMENT ON COLUMN nacos.tenant_capacity.max_history_count IS '最大变更历史数量';


--
-- Name: COLUMN tenant_capacity.gmt_create; Type: COMMENT; Schema: nacos; Owner: root
--

COMMENT ON COLUMN nacos.tenant_capacity.gmt_create IS '创建时间';


--
-- Name: COLUMN tenant_capacity.gmt_modified; Type: COMMENT; Schema: nacos; Owner: root
--

COMMENT ON COLUMN nacos.tenant_capacity.gmt_modified IS '修改时间';


--
-- Name: tenant_capacity_id_seq; Type: SEQUENCE; Schema: nacos; Owner: root
--

CREATE SEQUENCE nacos.tenant_capacity_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE nacos.tenant_capacity_id_seq OWNER TO root;

--
-- Name: tenant_capacity_id_seq; Type: SEQUENCE OWNED BY; Schema: nacos; Owner: root
--

ALTER SEQUENCE nacos.tenant_capacity_id_seq OWNED BY nacos.tenant_capacity.id;


--
-- Name: tenant_info; Type: TABLE; Schema: nacos; Owner: root
--

CREATE TABLE nacos.tenant_info (
    id bigint NOT NULL,
    kp character varying(128) NOT NULL,
    tenant_id character varying(128),
    tenant_name character varying(128),
    tenant_desc character varying(256),
    create_source character varying(32),
    gmt_create timestamp(6) without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
    gmt_modified timestamp(6) without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL
);


ALTER TABLE nacos.tenant_info OWNER TO root;

--
-- Name: TABLE tenant_info; Type: COMMENT; Schema: nacos; Owner: root
--

COMMENT ON TABLE nacos.tenant_info IS 'tenant_info';


--
-- Name: COLUMN tenant_info.id; Type: COMMENT; Schema: nacos; Owner: root
--

COMMENT ON COLUMN nacos.tenant_info.id IS 'id';


--
-- Name: COLUMN tenant_info.kp; Type: COMMENT; Schema: nacos; Owner: root
--

COMMENT ON COLUMN nacos.tenant_info.kp IS 'kp';


--
-- Name: COLUMN tenant_info.tenant_id; Type: COMMENT; Schema: nacos; Owner: root
--

COMMENT ON COLUMN nacos.tenant_info.tenant_id IS 'tenant_id';


--
-- Name: COLUMN tenant_info.tenant_name; Type: COMMENT; Schema: nacos; Owner: root
--

COMMENT ON COLUMN nacos.tenant_info.tenant_name IS 'tenant_name';


--
-- Name: COLUMN tenant_info.tenant_desc; Type: COMMENT; Schema: nacos; Owner: root
--

COMMENT ON COLUMN nacos.tenant_info.tenant_desc IS 'tenant_desc';


--
-- Name: COLUMN tenant_info.create_source; Type: COMMENT; Schema: nacos; Owner: root
--

COMMENT ON COLUMN nacos.tenant_info.create_source IS 'create_source';


--
-- Name: COLUMN tenant_info.gmt_create; Type: COMMENT; Schema: nacos; Owner: root
--

COMMENT ON COLUMN nacos.tenant_info.gmt_create IS '创建时间';


--
-- Name: COLUMN tenant_info.gmt_modified; Type: COMMENT; Schema: nacos; Owner: root
--

COMMENT ON COLUMN nacos.tenant_info.gmt_modified IS '修改时间';


--
-- Name: tenant_info_id_seq; Type: SEQUENCE; Schema: nacos; Owner: root
--

CREATE SEQUENCE nacos.tenant_info_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE nacos.tenant_info_id_seq OWNER TO root;

--
-- Name: tenant_info_id_seq; Type: SEQUENCE OWNED BY; Schema: nacos; Owner: root
--

ALTER SEQUENCE nacos.tenant_info_id_seq OWNED BY nacos.tenant_info.id;


--
-- Name: users; Type: TABLE; Schema: nacos; Owner: root
--

CREATE TABLE nacos.users (
    username character varying(50) NOT NULL,
    password character varying(500) NOT NULL,
    enabled boolean NOT NULL
);


ALTER TABLE nacos.users OWNER TO root;

--
-- Name: ai_resource id; Type: DEFAULT; Schema: nacos; Owner: root
--

ALTER TABLE ONLY nacos.ai_resource ALTER COLUMN id SET DEFAULT nextval('nacos.ai_resource_id_seq'::regclass);


--
-- Name: ai_resource_version id; Type: DEFAULT; Schema: nacos; Owner: root
--

ALTER TABLE ONLY nacos.ai_resource_version ALTER COLUMN id SET DEFAULT nextval('nacos.ai_resource_version_id_seq'::regclass);


--
-- Name: config_info id; Type: DEFAULT; Schema: nacos; Owner: root
--

ALTER TABLE ONLY nacos.config_info ALTER COLUMN id SET DEFAULT nextval('nacos.config_info_id_seq'::regclass);


--
-- Name: config_info_gray id; Type: DEFAULT; Schema: nacos; Owner: root
--

ALTER TABLE ONLY nacos.config_info_gray ALTER COLUMN id SET DEFAULT nextval('nacos.config_info_gray_id_seq'::regclass);


--
-- Name: config_tags_relation nid; Type: DEFAULT; Schema: nacos; Owner: root
--

ALTER TABLE ONLY nacos.config_tags_relation ALTER COLUMN nid SET DEFAULT nextval('nacos.config_tags_relation_nid_seq'::regclass);


--
-- Name: group_capacity id; Type: DEFAULT; Schema: nacos; Owner: root
--

ALTER TABLE ONLY nacos.group_capacity ALTER COLUMN id SET DEFAULT nextval('nacos.group_capacity_id_seq'::regclass);


--
-- Name: his_config_info nid; Type: DEFAULT; Schema: nacos; Owner: root
--

ALTER TABLE ONLY nacos.his_config_info ALTER COLUMN nid SET DEFAULT nextval('nacos.his_config_info_nid_seq'::regclass);


--
-- Name: tenant_capacity id; Type: DEFAULT; Schema: nacos; Owner: root
--

ALTER TABLE ONLY nacos.tenant_capacity ALTER COLUMN id SET DEFAULT nextval('nacos.tenant_capacity_id_seq'::regclass);


--
-- Name: tenant_info id; Type: DEFAULT; Schema: nacos; Owner: root
--

ALTER TABLE ONLY nacos.tenant_info ALTER COLUMN id SET DEFAULT nextval('nacos.tenant_info_id_seq'::regclass);


--
-- Data for Name: ai_resource; Type: TABLE DATA; Schema: nacos; Owner: root
--



--
-- Data for Name: ai_resource_version; Type: TABLE DATA; Schema: nacos; Owner: root
--



--
-- Data for Name: config_info; Type: TABLE DATA; Schema: nacos; Owner: root
--

INSERT INTO nacos.config_info (id, data_id, group_id, content, md5, gmt_create, gmt_modified, src_user, src_ip, app_name, tenant_id, c_desc, c_use, effect, type, c_schema, encrypted_data_key) VALUES (1, 'common.yml', 'COMMON_GROUP', 'server:
  # 优雅停机
  shutdown: graceful
  # 服务器配置
  tomcat:
    # 监控
    mbeanregistry:
      enabled: true
    ## 开启虚拟线程后，此配置无效
    # accept-count: 100
    # threads:
    #   max: 1000
    #   min-spare: 200
    # connection-timeout: 10000

spring:
  threads:
    virtual:
      enabled: true
  ## 开启虚拟线程后，此配置无效
  # task:
  #   execution:
  #     thread-name-prefix: async-threadPool-
  #     pool:
  #       core-size: 15
  #       max-size: 30
  #       queue-capacity: 1000
  #       keep-alive: 120s
  #       allow-core-thread-timeout: false
  #     shutdown:
  #       await-termination: true
  #       await-termination-period: 60s
  #   scheduling:
  #     thread-name-prefix: scheduling-threadPool-
  #     pool:
  #       size: 10
  #     shutdown:
  #       await-termination: true
  #       await-termination-period: 60s
  jackson:
    time-zone: GMT+8
    # 日期格式化
    date-format: yyyy-MM-dd HH:mm:ss
    serialization:
      # 格式化输出
      INDENT_OUTPUT: false
      # 忽略空Bean转json的错误
      FAIL_ON_EMPTY_BEANS: false
      # 关闭日期转换成时间戳
      WRITE_DATES_AS_TIMESTAMPS: false
    # 设置空如何序列化
    defaultPropertyInclusion: ALWAYS
    deserialization:
      #json中不存在的属性就报错
      fail_on_unknown_properties: false
    parser:
      # 允许使用无引号字段
      ALLOW_UNQUOTED_FIELD_NAMES: true
      # 忽略未定义的属性
      IGNORE_UNDEFINED: true
      # 忽略json最后的逗号
      ALLOW_TRAILING_COMMA: true
      # 允许反斜杠
      ALLOW_BACKSLASH_ESCAPING_ANY_CHARACTER: true
      # 允许出现特殊字符和转义符
      ALLOW_UNQUOTED_CONTROL_CHARS: true
      # 允许出现单引号
      ALLOW_SINGLE_QUOTES: true
      # 是否允许使用注释
      ALLOW_COMMENTS: true
    mapper:
      # 使用getter取代setter探测属性，如类中含getName()但不包含name属性与setName()，传输的vo json格式模板中依旧含name属性
      USE_GETTERS_AS_SETTERS: true
  mvc:
    # 关闭DispatcherServlet懒加载
    servlet:
      load-on-startup: 0
  messages:
    # 国际化资源文件路径
    basename: i18n/common,i18n/local

# 暴露监控端点
management:
  endpoints:
    web:
      exposure:
        include: "*"
  endpoint:
    env:
      access: read_only
      show-values: always
    configprops:
      access: read_only
      show-values: always
    beans:
      access: read_only

mybatis-plus:
  # 搜索指定包别名
  typeAliasesPackage: com.wzkris.**.domain
  # 配置mapper的扫描，找到所有的mapper.xml映射文件
  mapperLocations: classpath:mapper/*/*.xml
  configuration:
    log-impl: org.apache.ibatis.logging.stdout.StdOutImpl
  global-config:
    db-config:
      logic-delete-field: deleted
      logic-delete-value: ''true''
      logic-not-delete-value: ''false''', '01186ebcb5e117d89d2ac71d4c902aa6', '2023-06-19 02:28:00', '2026-07-14 19:30:31', NULL, '0:0:0:0:0:0:0:1', '', 'application-prod', '公共配置', '', '', 'yaml', '', '');
INSERT INTO nacos.config_info (id, data_id, group_id, content, md5, gmt_create, gmt_modified, src_user, src_ip, app_name, tenant_id, c_desc, c_use, effect, type, c_schema, encrypted_data_key) VALUES (5, 'wzkris-monitor-admin.yml', 'APPLICATION_GROUP', '# spring
spring:
  security:
    user:
      name: admin
      password: admin123
  boot:
    admin:
      ui:
        title: 服务状态监控
', 'dd19c14e3cebc473140e1fc8733a339d', '2023-06-19 02:28:00', '2023-06-19 02:28:00', NULL, '0:0:0:0:0:0:0:1', '', 'application-prod', '', NULL, NULL, 'yaml', NULL, '');
INSERT INTO nacos.config_info (id, data_id, group_id, content, md5, gmt_create, gmt_modified, src_user, src_ip, app_name, tenant_id, c_desc, c_use, effect, type, c_schema, encrypted_data_key) VALUES (10, 'redis.yml', 'COMMON_GROUP', 'spring:
  redis:
    redisson:
      config: |
        # 集群配置
        clusterServersConfig: 
          # Redis集群节点地址列表
          nodeAddresses:
            - "redis://127.0.0.1:6379"
            - "redis://127.0.0.1:6380"
            - "redis://127.0.0.1:6381"
          # 集群拓扑扫描间隔 ms
          scanInterval: 5000
          # 无密码则设置 null
          password: null
          # 客户端名称
          clientName: ${spring.application.name}
          # 主节点最小连接数
          masterConnectionMinimumIdleSize: 6
          # 主节点最大连接数
          masterConnectionPoolSize: 18
          # 从节点最小连接数
          slaveConnectionMinimumIdleSize: 6
          # 从节点最大连接数
          slaveConnectionPoolSize: 18
          # 命令等待超时,单位:毫秒
          timeout: 3000
          # 发布和订阅连接池大小
          subscriptionConnectionPoolSize: 30
        # 线程池数量
        threads: 8
        # Netty线程池数量
        nettyThreads: 8
        codec: !<org.redisson.codec.JsonJacksonCodec> {}
        transportMode: "NIO"


# spring:
#   redis:
#     redisson: 
#       config: |
#         # 单节点配置
#         singleServerConfig: 
#           # redis 节点地址
#           address: "redis://127.0.0.1:6379"
#           # 无密码则设置 null
#           password: null
#           # 客户端名称
#           clientName: ${spring.application.name}
#           # 最小空闲连接数
#           connectionMinimumIdleSize: 32
#           # 连接池大小
#           connectionPoolSize: 64
#           # 连接空闲超时,单位:毫秒
#           idleConnectionTimeout: 10000
#           # 命令等待超时,单位:毫秒
#           timeout: 3000
#           # 发布和订阅连接池大小
#           subscriptionConnectionPoolSize: 50
#         # 线程池数量
#         threads: 8
#         # Netty线程池数量
#         nettyThreads: 8
#         codec: !<org.redisson.codec.JsonJacksonCodec> {}
#         transportMode: "NIO"
', 'd19d9cfcd786295407bfb79f4365c633', '2025-08-04 15:19:50', '2025-08-04 15:19:50', NULL, '0:0:0:0:0:0:0:1', '', 'application-prod', NULL, NULL, NULL, 'yaml', NULL, '');
INSERT INTO nacos.config_info (id, data_id, group_id, content, md5, gmt_create, gmt_modified, src_user, src_ip, app_name, tenant_id, c_desc, c_use, effect, type, c_schema, encrypted_data_key) VALUES (12, 'common.yml', 'COMMON_GROUP', 'server:
  # 优雅停机
  shutdown: graceful
  # 服务器配置
  tomcat:
    # 监控
    mbeanregistry:
      enabled: true
    ## 开启虚拟线程后，此配置无效
    # accept-count: 100
    # threads:
    #   max: 1000
    #   min-spare: 200
    # connection-timeout: 10000

spring:
  threads:
    virtual:
      enabled: true
  ## 开启虚拟线程后，此配置无效
  # task:
  #   execution:
  #     thread-name-prefix: async-threadPool-
  #     pool:
  #       core-size: 15
  #       max-size: 30
  #       queue-capacity: 1000
  #       keep-alive: 120s
  #       allow-core-thread-timeout: false
  #     shutdown:
  #       await-termination: true
  #       await-termination-period: 60s
  #   scheduling:
  #     thread-name-prefix: scheduling-threadPool-
  #     pool:
  #       size: 10
  #     shutdown:
  #       await-termination: true
  #       await-termination-period: 60s
  jackson:
    time-zone: GMT+8
    # 日期格式化
    date-format: yyyy-MM-dd HH:mm:ss
    serialization:
      # 格式化输出
      INDENT_OUTPUT: false
      # 忽略空Bean转json的错误
      FAIL_ON_EMPTY_BEANS: false
      # 关闭日期转换成时间戳
      WRITE_DATES_AS_TIMESTAMPS: false
    # 设置空如何序列化
    defaultPropertyInclusion: ALWAYS
    deserialization:
      #json中不存在的属性就报错
      fail_on_unknown_properties: false
    parser:
      # 允许使用无引号字段
      ALLOW_UNQUOTED_FIELD_NAMES: true
      # 忽略未定义的属性
      IGNORE_UNDEFINED: true
      # 忽略json最后的逗号
      ALLOW_TRAILING_COMMA: true
      # 允许反斜杠
      ALLOW_BACKSLASH_ESCAPING_ANY_CHARACTER: true
      # 允许出现特殊字符和转义符
      ALLOW_UNQUOTED_CONTROL_CHARS: true
      # 允许出现单引号
      ALLOW_SINGLE_QUOTES: true
      # 是否允许使用注释
      ALLOW_COMMENTS: true
    mapper:
      # 使用getter取代setter探测属性，如类中含getName()但不包含name属性与setName()，传输的vo json格式模板中依旧含name属性
      USE_GETTERS_AS_SETTERS: true      
  mvc:
    # 关闭DispatcherServlet懒加载
    servlet:
      load-on-startup: 0
  messages:
    # 国际化资源文件路径
    basename: i18n/common,i18n/local

# 暴露监控端点
management:
  endpoints:
    web:
      exposure:
        include: "*"
  endpoint:
    env:
      access: read_only
      show-values: always
    configprops:
      access: read_only
      show-values: always
    beans:
      access: read_only

mybatis-plus:
  # 搜索指定包别名
  typeAliasesPackage: com.wzkris.**.domain
  # 配置mapper的扫描，找到所有的mapper.xml映射文件
  mapperLocations: classpath:mapper/*/*.xml
  configuration:
    log-impl: org.apache.ibatis.logging.stdout.StdOutImpl
  global-config:
    db-config:
      logic-delete-field: deleted
      logic-delete-value: ''true''
      logic-not-delete-value: ''false''
', '874d10bfc0e95bdebce974e5ef4671d4', '2023-06-19 02:28:00', '2026-07-17 14:47:25', NULL, '0:0:0:0:0:0:0:1', '', 'application-dev', '公共配置', '', '', 'yaml', '', '');
INSERT INTO nacos.config_info (id, data_id, group_id, content, md5, gmt_create, gmt_modified, src_user, src_ip, app_name, tenant_id, c_desc, c_use, effect, type, c_schema, encrypted_data_key) VALUES (2, 'wzkris-gateway.yml', 'APPLICATION_GROUP', 'spring:
  cloud:
    gateway:
      server:
        webmvc:
          routes:
            # 认证中心
            - id: wzkris-auth
              uri: lb://wzkris-auth
              predicates:
                - Path=/wzkris-auth-api/**
              # 用户中心服务
            - id: wzkris-user-center
              uri: lb://wzkris-user-center
              predicates:
                - Path=/wzkris-user-center-api/**
            # 验证码模块
            - id: wzkris-captcha
              uri: lb://wzkris-captcha
              predicates:
                - Path=/wzkris-captcha-api/**

# 路由策略
route-decision:
  policy: OPEN
  forceConfig: 
    hintValue: 0.1
  openConfig: 
    defaultHintValue: ""

knife4j:
  # 聚合swagger文档
  gateway:
    enabled: false

security:
  risk-captcha:
    enabled: true
    enforcedPaths:
      - /wzkris-auth-api/login', '5715d84ce8f69bb8a11ccd05ebdcb66c', '2023-06-19 02:28:00', '2026-07-31 18:20:14.410662', 'nacos', '0:0:0:0:0:0:0:1', '', 'application-prod', '', NULL, NULL, 'yaml', NULL, '');
INSERT INTO nacos.config_info (id, data_id, group_id, content, md5, gmt_create, gmt_modified, src_user, src_ip, app_name, tenant_id, c_desc, c_use, effect, type, c_schema, encrypted_data_key) VALUES (14, 'wzkris-auth.yml', 'APPLICATION_GROUP', '
# springdoc配置
springdoc:
  enabled: true
  title: 认证模块接口文档
  license: Powered By wzkris
  version: v1.0.0
  description: ---

# 打印忽略
controller-log:
  ignoreUrls: /qr-code/**

jwt-rs256:
  previousPublicKey: 
  previousPrivateKey: 
  publicKey: -----BEGIN PUBLIC KEY-----MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEA1cdDeVUgQzB22IcCAHVKij1Csn+tOWIGu2deRxSWQU1CIkrrKyB2zWJ8RX4lp8ssxspnybsuycHmvavNBE7EBVW3bEYIN2ebOFFdOxZPnCC7mBikQBtag3TfmXSO3Mcg/rN4ulqWloDX1Wv3vdAh/eCxYlDNATAAFxEoEdzEe3MapdOygRZWbj9DEfEF1bU3ObxrBV9ExFnPLAUx0CE0MDLhAF3s+qtkcFlpG1h+Q/XDxF8wp53bTqsgFslqFCsJXL8GqFTOnCTPlUybwfa8Mtos9s/djJpm9KZWndrXlDshysnd7bQqG5HGh6Y5AHIfwcUxKFdSMk7jVI472aY4CwIDAQAB-----END PUBLIC KEY-----
  privateKey: -----BEGIN PRIVATE KEY-----MIIEvgIBADANBgkqhkiG9w0BAQEFAASCBKgwggSkAgEAAoIBAQDVx0N5VSBDMHbYhwIAdUqKPUKyf605Yga7Z15HFJZBTUIiSusrIHbNYnxFfiWnyyzGymfJuy7Jwea9q80ETsQFVbdsRgg3Z5s4UV07Fk+cILuYGKRAG1qDdN+ZdI7cxyD+s3i6WpaWgNfVa/e90CH94LFiUM0BMAAXESgR3MR7cxql07KBFlZuP0MR8QXVtTc5vGsFX0TEWc8sBTHQITQwMuEAXez6q2RwWWkbWH5D9cPEXzCnndtOqyAWyWoUKwlcvwaoVM6cJM+VTJvB9rwy2iz2z92Mmmb0plad2teUOyHKyd3ttCobkcaHpjkAch/BxTEoV1IyTuNUjjvZpjgLAgMBAAECggEBAMgCerqWTm0OduL2zYSoOGlGD5T5p5Q8hpfnimludXX7VpjHB2d+JCjcr/BEqe5nRSloTdqL6qaRZ9SlXFdfaj6jh80haKaNpMf4OAYERc+JQHp485OXBARh4KGuT8t38wLZ32ZbQvDk8wqWzV8lz+e7xbp6ZpNp2Wu7fYXYy2vC+7Zje6qYCYi+JMF6a2ujKEflLI9dDl2fkDyS7P4O2bcdbXCVV6SkaeNCNk5ZAbbRgA5wdCQE/z45cJaciTKwah+pHcN4ytcy8I5zbg410CrA9z5PWAfFxdjQ1EyuvqheDGFbnFDsZJsBP3/7P6/JU3qgep2uM6YYbY8dVpVuf6ECgYEA6xAo1O6U6gW5T7qpZ0CX3MOJVw1aFSTYoa8Rrnyq/ljLGkmNtnp9K+DDGdETAlBemfEc3sntqubIxC0/twQfErFdn0gLnS0gLBbBeYPZUmmAtIyR3dCmwlaJmok+YCTNDgz+RsbFRrS6V9WVREguEpY5NsTGltrmOLZ7WlWmsWcCgYEA6NHGg0PDuTF/MoUgL6ZnzdCtEoN+uV+NAq85C/smngbfFNR+ttFteKzLsbyJydfBFgTS2cnPXyuK/NY6Qa/lXZfxxcfmqv7mreBKc3usSqveXRRbqm6wMz6kSXWk8/2HzywRX9JPNauW13Otyl8l/myC69nAHzVbQey8sy1eab0CgYBb08o/tJxT97x22xLGlUM+KN0ENuEUFXrTXtLneShLiGB/enBz8tHnTDyrXzOv2bm7JagDmJrSAqo3iP20/1UsNkG+saRn2HMTBii60bkaKsDux2NMZfBfRvMmfaryYC4C6SyEda4nev64xWU0cYYeGLVtId36nLUHPrJdjcw6zQKBgQC74zc6DcDxPpGxGqBb9AYHoeVacIYfYY4x4Wi/U4LZux/i9o4AScj3vzNvj4D/REAN3fyvR98zpbc3zkcbZbFLs+iovWdZDfbp0X0j72WeqU79fQVw6H0IDgCVS/y/7xXfymeHFflYjc5gt3lEPT8zMS0C2yrhxLSN4lhynSV5XQKBgA2jWpc40dHIKJaIYgJp1wS4QCP21J06EdYdu3bHARPXP75nm8L6m3nh9iQRWanx1x2Fulgq3rVbX/Re0xT7zVaKaH3iaUwZmnbP+LF0/2exXw+FNlSTfA7tR0eXhYtg6CBapQs9+Wc40wYK93xsROMoGEGrwFzuBiPV7H7N9VBP-----END PRIVATE KEY-----
', '3578fad72ecdc89e0da921303fafbbab', '2023-06-19 02:28:00', '2026-07-31 16:04:03.408637', 'nacos', '0:0:0:0:0:0:0:1', '', 'application-dev', '', NULL, NULL, 'yaml', NULL, '');
INSERT INTO nacos.config_info (id, data_id, group_id, content, md5, gmt_create, gmt_modified, src_user, src_ip, app_name, tenant_id, c_desc, c_use, effect, type, c_schema, encrypted_data_key) VALUES (3, 'wzkris-auth.yml', 'APPLICATION_GROUP', '
# springdoc配置
springdoc:
  enabled: false

# 打印忽略
controller-log:
  ignoreUrls: /qr-code/**

jwt-rs256:
  previousPublicKey: 
  previousPrivateKey: 
  publicKey: -----BEGIN PUBLIC KEY-----MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEA1cdDeVUgQzB22IcCAHVKij1Csn+tOWIGu2deRxSWQU1CIkrrKyB2zWJ8RX4lp8ssxspnybsuycHmvavNBE7EBVW3bEYIN2ebOFFdOxZPnCC7mBikQBtag3TfmXSO3Mcg/rN4ulqWloDX1Wv3vdAh/eCxYlDNATAAFxEoEdzEe3MapdOygRZWbj9DEfEF1bU3ObxrBV9ExFnPLAUx0CE0MDLhAF3s+qtkcFlpG1h+Q/XDxF8wp53bTqsgFslqFCsJXL8GqFTOnCTPlUybwfa8Mtos9s/djJpm9KZWndrXlDshysnd7bQqG5HGh6Y5AHIfwcUxKFdSMk7jVI472aY4CwIDAQAB-----END PUBLIC KEY-----
  privateKey: -----BEGIN PRIVATE KEY-----MIIEvgIBADANBgkqhkiG9w0BAQEFAASCBKgwggSkAgEAAoIBAQDVx0N5VSBDMHbYhwIAdUqKPUKyf605Yga7Z15HFJZBTUIiSusrIHbNYnxFfiWnyyzGymfJuy7Jwea9q80ETsQFVbdsRgg3Z5s4UV07Fk+cILuYGKRAG1qDdN+ZdI7cxyD+s3i6WpaWgNfVa/e90CH94LFiUM0BMAAXESgR3MR7cxql07KBFlZuP0MR8QXVtTc5vGsFX0TEWc8sBTHQITQwMuEAXez6q2RwWWkbWH5D9cPEXzCnndtOqyAWyWoUKwlcvwaoVM6cJM+VTJvB9rwy2iz2z92Mmmb0plad2teUOyHKyd3ttCobkcaHpjkAch/BxTEoV1IyTuNUjjvZpjgLAgMBAAECggEBAMgCerqWTm0OduL2zYSoOGlGD5T5p5Q8hpfnimludXX7VpjHB2d+JCjcr/BEqe5nRSloTdqL6qaRZ9SlXFdfaj6jh80haKaNpMf4OAYERc+JQHp485OXBARh4KGuT8t38wLZ32ZbQvDk8wqWzV8lz+e7xbp6ZpNp2Wu7fYXYy2vC+7Zje6qYCYi+JMF6a2ujKEflLI9dDl2fkDyS7P4O2bcdbXCVV6SkaeNCNk5ZAbbRgA5wdCQE/z45cJaciTKwah+pHcN4ytcy8I5zbg410CrA9z5PWAfFxdjQ1EyuvqheDGFbnFDsZJsBP3/7P6/JU3qgep2uM6YYbY8dVpVuf6ECgYEA6xAo1O6U6gW5T7qpZ0CX3MOJVw1aFSTYoa8Rrnyq/ljLGkmNtnp9K+DDGdETAlBemfEc3sntqubIxC0/twQfErFdn0gLnS0gLBbBeYPZUmmAtIyR3dCmwlaJmok+YCTNDgz+RsbFRrS6V9WVREguEpY5NsTGltrmOLZ7WlWmsWcCgYEA6NHGg0PDuTF/MoUgL6ZnzdCtEoN+uV+NAq85C/smngbfFNR+ttFteKzLsbyJydfBFgTS2cnPXyuK/NY6Qa/lXZfxxcfmqv7mreBKc3usSqveXRRbqm6wMz6kSXWk8/2HzywRX9JPNauW13Otyl8l/myC69nAHzVbQey8sy1eab0CgYBb08o/tJxT97x22xLGlUM+KN0ENuEUFXrTXtLneShLiGB/enBz8tHnTDyrXzOv2bm7JagDmJrSAqo3iP20/1UsNkG+saRn2HMTBii60bkaKsDux2NMZfBfRvMmfaryYC4C6SyEda4nev64xWU0cYYeGLVtId36nLUHPrJdjcw6zQKBgQC74zc6DcDxPpGxGqBb9AYHoeVacIYfYY4x4Wi/U4LZux/i9o4AScj3vzNvj4D/REAN3fyvR98zpbc3zkcbZbFLs+iovWdZDfbp0X0j72WeqU79fQVw6H0IDgCVS/y/7xXfymeHFflYjc5gt3lEPT8zMS0C2yrhxLSN4lhynSV5XQKBgA2jWpc40dHIKJaIYgJp1wS4QCP21J06EdYdu3bHARPXP75nm8L6m3nh9iQRWanx1x2Fulgq3rVbX/Re0xT7zVaKaH3iaUwZmnbP+LF0/2exXw+FNlSTfA7tR0eXhYtg6CBapQs9+Wc40wYK93xsROMoGEGrwFzuBiPV7H7N9VBP-----END PRIVATE KEY-----
', 'e39123674820d34d196f74e8cf65fee2', '2023-06-19 02:28:00', '2026-07-31 16:12:49.374121', 'nacos', '0:0:0:0:0:0:0:1', '', 'application-prod', '', NULL, NULL, 'yaml', NULL, '');
INSERT INTO nacos.config_info (id, data_id, group_id, content, md5, gmt_create, gmt_modified, src_user, src_ip, app_name, tenant_id, c_desc, c_use, effect, type, c_schema, encrypted_data_key) VALUES (7, 'sentinel-gateway', 'APPLICATION_GROUP', '[
    {
        "resource": "wzkris-auth",
        "count": 2000,
        "grade": 1,
        "limitApp": "default",
        "strategy": 0,
        "controlBehavior": 0,
        "clusterMode": false
    },
    {
        "resource": "wzkris-user-center",
        "count": 1000,
        "grade": 1,
        "limitApp": "default",
        "strategy": 0,
        "controlBehavior": 0,
        "clusterMode": false
    },
    {
        "resource": "wzkris-monitor-admin",
        "count": 300,
        "grade": 1,
        "limitApp": "default",
        "strategy": 0,
        "controlBehavior": 0,
        "clusterMode": false
    }
]', 'f83470a74550f4705cc975aaa2bcd7c4', '2023-06-19 02:28:00', '2026-07-31 16:16:52.562724', 'nacos', '0:0:0:0:0:0:0:1', '', 'application-prod', '网关限流策略', NULL, NULL, 'json', NULL, '');
INSERT INTO nacos.config_info (id, data_id, group_id, content, md5, gmt_create, gmt_modified, src_user, src_ip, app_name, tenant_id, c_desc, c_use, effect, type, c_schema, encrypted_data_key) VALUES (4, 'wzkris-user-center.yml', 'APPLICATION_GROUP', '# spring配置
spring:
  # datasource:
  #   driver-class-name: org.apache.shardingsphere.driver.ShardingSphereDriver
  #   url: jdbc:shardingsphere:classpath:sharding-${spring.profiles.active}.yml
  datasource:
    url: jdbc:postgresql://localhost:5432/wzkris_user_center?ssl=false&reWriteBatchedInserts=true&stringtype=unspecified
    username: root
    password: root
    driver-class-name: org.postgresql.Driver
    hikari:
      connection-timeout: 30000 
      maximum-pool-size: 10       
      minimum-idle: 5             
      idle-timeout: 600000        
      pool-name: hikari-pool

# springdoc配置
springdoc:
  enabled: false

# 租户隔离表
tenant:
  includes:
    - member_info
    - post_info
    - tenant_login_log
    - tenant_operate_log
    - tenant_wallet_info
    - tenant_wallet_record
    - tenant_wallet_withdrawal_record', 'd54a11e22b0e43d9d8e528e1e90d4874', '2024-04-16 01:03:03', '2026-08-05 18:12:51.171991', 'nacos', '0:0:0:0:0:0:0:1', '', 'application-prod', '', NULL, NULL, 'yaml', NULL, '');
INSERT INTO nacos.config_info (id, data_id, group_id, content, md5, gmt_create, gmt_modified, src_user, src_ip, app_name, tenant_id, c_desc, c_use, effect, type, c_schema, encrypted_data_key) VALUES (16, 'wzkris-monitor-admin.yml', 'APPLICATION_GROUP', '# spring
spring:
  security:
    user:
      name: admin
      password: admin123
  boot:
    admin:
      ui:
        title: 服务状态监控
', 'dd19c14e3cebc473140e1fc8733a339d', '2023-06-19 02:28:00', '2025-09-29 11:55:07', NULL, '0:0:0:0:0:0:0:1', '', 'application-dev', '', '', '', 'yaml', '', '');
INSERT INTO nacos.config_info (id, data_id, group_id, content, md5, gmt_create, gmt_modified, src_user, src_ip, app_name, tenant_id, c_desc, c_use, effect, type, c_schema, encrypted_data_key) VALUES (21, 'redis.yml', 'COMMON_GROUP', 'spring:
  redis:
    redisson:
      config: |
        # 集群配置
        clusterServersConfig: 
          # Redis集群节点地址列表
          nodeAddresses:
            - "redis://127.0.0.1:6379"
            - "redis://127.0.0.1:6380"
            - "redis://127.0.0.1:6381"
          # 集群拓扑扫描间隔 ms
          scanInterval: 5000
          # 无密码则设置 null
          password: null
          # 客户端名称
          clientName: ${spring.application.name}
          # 主节点最小连接数
          masterConnectionMinimumIdleSize: 6
          # 主节点最大连接数
          masterConnectionPoolSize: 18
          # 从节点最小连接数
          slaveConnectionMinimumIdleSize: 6
          # 从节点最大连接数
          slaveConnectionPoolSize: 18
          # 命令等待超时,单位:毫秒
          timeout: 3000
          # 发布和订阅连接池大小
          subscriptionConnectionPoolSize: 30
        # 线程池数量
        threads: 8
        # Netty线程池数量
        nettyThreads: 8
        codec: !<org.redisson.codec.JsonJacksonCodec> {}
        transportMode: "NIO"


# spring:
#   redis:
#     redisson: 
#       config: |
#         # 单节点配置
#         singleServerConfig: 
#           # redis 节点地址
#           address: "redis://127.0.0.1:6379"
#           # 无密码则设置 null
#           password: null
#           # 客户端名称
#           clientName: ${spring.application.name}
#           # 最小空闲连接数
#           connectionMinimumIdleSize: 32
#           # 连接池大小
#           connectionPoolSize: 64
#           # 连接空闲超时,单位:毫秒
#           idleConnectionTimeout: 10000
#           # 命令等待超时,单位:毫秒
#           timeout: 3000
#           # 发布和订阅连接池大小
#           subscriptionConnectionPoolSize: 50
#         # 线程池数量
#         threads: 8
#         # Netty线程池数量
#         nettyThreads: 8
#         codec: !<org.redisson.codec.JsonJacksonCodec> {}
#         transportMode: "NIO"
', '33047a58e1d443074cbc95257bdb4216', '2025-08-04 15:12:22', '2026-01-15 16:44:10', NULL, '0:0:0:0:0:0:0:1', '', 'application-dev', 'redis公共配置', '', '', 'yaml', '', '');
INSERT INTO nacos.config_info (id, data_id, group_id, content, md5, gmt_create, gmt_modified, src_user, src_ip, app_name, tenant_id, c_desc, c_use, effect, type, c_schema, encrypted_data_key) VALUES (22, 'wzkris-captcha.yml', 'APPLICATION_GROUP', 'risk-captcha:
  passTtlSeconds: 180', 'a0887872d4f7f6d5aef5923322591441', '2026-05-21 11:25:06', '2026-05-21 11:27:38', NULL, '0:0:0:0:0:0:0:1', '', 'application-dev', '', '', '', 'yaml', '', '');
INSERT INTO nacos.config_info (id, data_id, group_id, content, md5, gmt_create, gmt_modified, src_user, src_ip, app_name, tenant_id, c_desc, c_use, effect, type, c_schema, encrypted_data_key) VALUES (18, 'sentinel-gateway', 'APPLICATION_GROUP', '[
    {
        "resource": "wzkris-auth",
        "count": 2000,
        "grade": 1,
        "limitApp": "default",
        "strategy": 0,
        "controlBehavior": 0,
        "clusterMode": false
    },
    {
        "resource": "wzkris-user-center",
        "count": 1000,
        "grade": 1,
        "limitApp": "default",
        "strategy": 0,
        "controlBehavior": 0,
        "clusterMode": false
    },
    {
        "resource": "wzkris-monitor-admin",
        "count": 300,
        "grade": 1,
        "limitApp": "default",
        "strategy": 0,
        "controlBehavior": 0,
        "clusterMode": false
    }
]', 'f83470a74550f4705cc975aaa2bcd7c4', '2023-06-19 02:28:00', '2026-07-31 16:17:02.693587', 'nacos', '0:0:0:0:0:0:0:1', '', 'application-dev', '网关限流策略', NULL, NULL, 'json', NULL, '');
INSERT INTO nacos.config_info (id, data_id, group_id, content, md5, gmt_create, gmt_modified, src_user, src_ip, app_name, tenant_id, c_desc, c_use, effect, type, c_schema, encrypted_data_key) VALUES (15, 'wzkris-user-center.yml', 'APPLICATION_GROUP', '# spring配置
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/wzkris_user_center?ssl=false&reWriteBatchedInserts=true&stringtype=unspecified
    username: root
    password: root
    driver-class-name: org.postgresql.Driver
    hikari:
      connection-timeout: 30000 
      maximum-pool-size: 10       
      minimum-idle: 5             
      idle-timeout: 600000        
      pool-name: hikari-pool

# springdoc配置
springdoc:
  enabled: true
  title: 用户中心接口文档
  license: Powered By wzkris
  version: v1.0.0
  description: ---

# 租户隔离表
tenant:
  includes:
    - member_info
    - post_info
    - tenant_login_log
    - tenant_operate_log
    - tenant_wallet_info
    - tenant_wallet_record
    - tenant_wallet_withdrawal_record', 'd453b2ff812e7745f85983267b3f93ec', '2024-04-16 06:36:22', '2026-08-11 17:16:22.208968', 'nacos', '0:0:0:0:0:0:0:1', '', 'application-dev', '', NULL, NULL, 'yaml', NULL, '');
INSERT INTO nacos.config_info (id, data_id, group_id, content, md5, gmt_create, gmt_modified, src_user, src_ip, app_name, tenant_id, c_desc, c_use, effect, type, c_schema, encrypted_data_key) VALUES (17, 'wzkris-payment.yml', 'APPLICATION_GROUP', '# spring配置
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/wzkris_payment?ssl=false&reWriteBatchedInserts=true&stringtype=unspecified
    username: root
    password: root
    driver-class-name: org.postgresql.Driver
    hikari:
      connection-timeout: 30000
      maximum-pool-size: 10
      minimum-idle: 5
      idle-timeout: 600000
      pool-name: hikari-pool

# springdoc配置
springdoc:
  enabled: true
  title: 支付网关接口文档
  license: Powered By wzkris
  version: v1.0.0
  description: ---
', '31c7da71ad47e6551a9e9d34ab8845e5', '2024-04-16 06:36:22', '2026-08-11 17:15:48.541618', 'nacos', '0:0:0:0:0:0:0:1', '', 'application-dev', '', NULL, NULL, 'yaml', NULL, '');
INSERT INTO nacos.config_info (id, data_id, group_id, content, md5, gmt_create, gmt_modified, src_user, src_ip, app_name, tenant_id, c_desc, c_use, effect, type, c_schema, encrypted_data_key) VALUES (19, 'wzkris-payment.yml', 'APPLICATION_GROUP', '# spring配置
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/wzkris_payment?ssl=false&reWriteBatchedInserts=true&stringtype=unspecified
    username: root
    password: root
    driver-class-name: org.postgresql.Driver
    hikari:
      connection-timeout: 30000
      maximum-pool-size: 10
      minimum-idle: 5
      idle-timeout: 600000
      pool-name: hikari-pool

# springdoc配置
springdoc:
  enabled: true
  title: 支付网关接口文档
  license: Powered By wzkris
  version: v1.0.0
  description: ---
', '31c7da71ad47e6551a9e9d34ab8845e5', '2024-04-16 06:36:22', '2026-08-11 17:15:48.541618', 'nacos', '0:0:0:0:0:0:0:1', '', 'application-prod', '', NULL, NULL, 'yaml', NULL, '');
INSERT INTO nacos.config_info (id, data_id, group_id, content, md5, gmt_create, gmt_modified, src_user, src_ip, app_name, tenant_id, c_desc, c_use, effect, type, c_schema, encrypted_data_key) VALUES (13, 'wzkris-gateway.yml', 'APPLICATION_GROUP', 'spring:
  cloud:
    gateway:
      server:
        webmvc:
          routes:
            # 认证中心
            - id: wzkris-auth
              uri: lb://wzkris-auth
              predicates:
                - Path=/wzkris-auth-api/**
              # 用户中心服务
            - id: wzkris-user-center
              uri: lb://wzkris-user-center
              predicates:
                - Path=/wzkris-user-center-api/**
            # 验证码模块
            - id: wzkris-captcha
              uri: lb://wzkris-captcha
              predicates:
                - Path=/wzkris-captcha-api/**

# 路由策略
route-decision:
  policy: OPEN
  forceConfig: 
    hintValue: 0.1
  openConfig: 
    defaultHintValue: ""

knife4j:
  gateway:
    enabled: true
    strategy: discover
    discover:
      version: openapi3
      enabled: true
      oas3:
        url: /v3/api-docs/default
    routes:
      - name: 认证服务-远程接口
        url: /wzkris-auth-api/v3/api-docs/remote
        context-path: /wzkris-auth-api
        order: 2
      - name: 用户中心-远程接口
        url: /wzkris-user-center-api/v3/api-docs/remote
        context-path: /wzkris-user-center-api
        order: 4
    tags-sorter: order
    operations-sorter: order

security:
  risk-captcha:
    enabled: true
    enforcedPaths:
      - /wzkris-auth-api/login', 'eaa692ac2d9c9ac50f7cade55c6bd0a3', '2023-06-19 02:28:00', '2026-08-07 10:44:52.583763', 'nacos', '0:0:0:0:0:0:0:1', '', 'application-dev', '', NULL, NULL, 'yaml', NULL, '');
INSERT INTO nacos.config_info (id, data_id, group_id, content, md5, gmt_create, gmt_modified, src_user, src_ip, app_name, tenant_id, c_desc, c_use, effect, type, c_schema, encrypted_data_key) VALUES (6, 'wzkris-captcha.yml', 'APPLICATION_GROUP', 'risk-captcha:
  passTtlSeconds: 180', 'a0887872d4f7f6d5aef5923322591441', '2026-05-21 11:25:06', '2026-05-21 11:27:38', NULL, '0:0:0:0:0:0:0:1', '', 'application-prod', '', '', '', 'yaml', '', '');


--
-- Data for Name: config_info_gray; Type: TABLE DATA; Schema: nacos; Owner: root
--



--
-- Data for Name: config_tags_relation; Type: TABLE DATA; Schema: nacos; Owner: root
--



--
-- Data for Name: group_capacity; Type: TABLE DATA; Schema: nacos; Owner: root
--



--
-- Data for Name: his_config_info; Type: TABLE DATA; Schema: nacos; Owner: root
--

INSERT INTO nacos.his_config_info (id, nid, data_id, group_id, app_name, content, md5, gmt_create, gmt_modified, src_user, src_ip, op_type, tenant_id, encrypted_data_key, publish_type, gray_name, ext_info) VALUES (17, 23, 'wzkris-payment.yml', 'APPLICATION_GROUP', '', '# spring配置
spring:
  # datasource:
  #   driver-class-name: org.apache.shardingsphere.driver.ShardingSphereDriver
  #   url: jdbc:shardingsphere:classpath:sharding-${spring.profiles.active}.yml
  datasource:
    url: jdbc:postgresql://localhost:5432/wzkris_user_center?ssl=false&reWriteBatchedInserts=true&stringtype=unspecified
    username: root
    password: root
    driver-class-name: org.postgresql.Driver
    hikari:
      connection-timeout: 30000
      maximum-pool-size: 10
      minimum-idle: 5
      idle-timeout: 600000
      pool-name: hikari-pool

# springdoc配置
springdoc:
  enabled: true
  title: 用户中心接口文档
  license: Powered By wzkris
  version: v1.0.0
  description: ---

# 租户隔离表
tenant:
  includes:
    - member_info
    - post_info
    - tenant_login_log
    - tenant_operate_log
    - tenant_wallet_info
    - tenant_wallet_record
    - tenant_wallet_withdrawal_record', 'a4b2ec61487b8940aaa240221f6400dc', '2026-08-11 17:15:48.541618', '2026-08-11 17:15:48.555', 'nacos', '0:0:0:0:0:0:0:1', 'U         ', 'application-dev', '', 'formal', '', '{"type":"yaml","src_user":"nacos"}');
INSERT INTO nacos.his_config_info (id, nid, data_id, group_id, app_name, content, md5, gmt_create, gmt_modified, src_user, src_ip, op_type, tenant_id, encrypted_data_key, publish_type, gray_name, ext_info) VALUES (15, 24, 'wzkris-user-center.yml', 'APPLICATION_GROUP', '', '# spring配置
spring:
  # datasource:
  #   driver-class-name: org.apache.shardingsphere.driver.ShardingSphereDriver
  #   url: jdbc:shardingsphere:classpath:sharding-${spring.profiles.active}.yml
  datasource:
    url: jdbc:postgresql://localhost:5432/wzkris_user_center?ssl=false&reWriteBatchedInserts=true&stringtype=unspecified
    username: root
    password: root
    driver-class-name: org.postgresql.Driver
    hikari:
      connection-timeout: 30000 
      maximum-pool-size: 10       
      minimum-idle: 5             
      idle-timeout: 600000        
      pool-name: hikari-pool

# springdoc配置
springdoc:
  enabled: true
  title: 用户中心接口文档
  license: Powered By wzkris
  version: v1.0.0
  description: ---

# 租户隔离表
tenant:
  includes:
    - member_info
    - post_info
    - tenant_login_log
    - tenant_operate_log
    - tenant_wallet_info
    - tenant_wallet_record
    - tenant_wallet_withdrawal_record', '241915b88b9b832e165f323a2e5b1a2d', '2026-08-11 17:16:22.208968', '2026-08-11 17:16:22.209', 'nacos', '0:0:0:0:0:0:0:1', 'U         ', 'application-dev', '', 'formal', '', '{"type":"yaml","src_user":"nacos"}');


--
-- Data for Name: permissions; Type: TABLE DATA; Schema: nacos; Owner: root
--

INSERT INTO nacos.permissions (role, resource, action) VALUES ('prod', 'application-prod:*:*', 'rw');
INSERT INTO nacos.permissions (role, resource, action) VALUES ('prod', ':*:*', 'rw');
INSERT INTO nacos.permissions (role, resource, action) VALUES ('dev', 'application-dev:*:*', 'rw');
INSERT INTO nacos.permissions (role, resource, action) VALUES ('dev', ':*:*', 'rw');


--
-- Data for Name: pipeline_execution; Type: TABLE DATA; Schema: nacos; Owner: root
--



--
-- Data for Name: roles; Type: TABLE DATA; Schema: nacos; Owner: root
--

INSERT INTO nacos.roles (username, role) VALUES ('dev', 'dev');
INSERT INTO nacos.roles (username, role) VALUES ('nacos', 'ROLE_ADMIN');
INSERT INTO nacos.roles (username, role) VALUES ('prod', 'prod');


--
-- Data for Name: tenant_capacity; Type: TABLE DATA; Schema: nacos; Owner: root
--



--
-- Data for Name: tenant_info; Type: TABLE DATA; Schema: nacos; Owner: root
--

INSERT INTO nacos.tenant_info (id, kp, tenant_id, tenant_name, tenant_desc, create_source, gmt_create, gmt_modified) VALUES (1, '1', 'application-dev', 'application-dev', '应用开发环境', 'nacos', '2023-05-26 03:16:31.448', '2023-05-26 03:16:49.622');
INSERT INTO nacos.tenant_info (id, kp, tenant_id, tenant_name, tenant_desc, create_source, gmt_create, gmt_modified) VALUES (2, '1', 'application-prod', 'application-prod', '应用生产环境', 'nacos', '2023-06-19 02:26:36.216', '2023-06-19 02:26:36.216');
INSERT INTO nacos.tenant_info (id, kp, tenant_id, tenant_name, tenant_desc, create_source, gmt_create, gmt_modified) VALUES (5, '2', 'nacos-default-mcp', 'nacos-default-mcp', 'Nacos default AI MCP module.', 'nacos', '2025-06-09 07:11:52.939', '2025-06-09 07:11:52.939');


--
-- Data for Name: users; Type: TABLE DATA; Schema: nacos; Owner: root
--

INSERT INTO nacos.users (username, password, enabled) VALUES ('dev', '$2a$10$zOHL8QGqN6PNlYRluJpvluKDP16Add3ywu00K2O70klCmSnj8gKzm', false);
INSERT INTO nacos.users (username, password, enabled) VALUES ('nacos', '$2a$10$h05FUQ0x5eypjsF02DWyd.2PXBDgXb7GIXNZCjRim6EORDlTLTRLu', false);
INSERT INTO nacos.users (username, password, enabled) VALUES ('prod', '$2a$10$AdFVZMyO8R4ZbjIVSlQWBukjx83Buc.x54x1fmwDyLDhT.uWSXi9S', true);


--
-- Name: ai_resource_id_seq; Type: SEQUENCE SET; Schema: nacos; Owner: root
--

SELECT pg_catalog.setval('nacos.ai_resource_id_seq', 1, false);


--
-- Name: ai_resource_version_id_seq; Type: SEQUENCE SET; Schema: nacos; Owner: root
--

SELECT pg_catalog.setval('nacos.ai_resource_version_id_seq', 1, false);


--
-- Name: config_info_gray_id_seq; Type: SEQUENCE SET; Schema: nacos; Owner: root
--

SELECT pg_catalog.setval('nacos.config_info_gray_id_seq', 1, false);


--
-- Name: config_info_id_seq; Type: SEQUENCE SET; Schema: nacos; Owner: root
--

SELECT pg_catalog.setval('nacos.config_info_id_seq', 1, false);


--
-- Name: config_tags_relation_nid_seq; Type: SEQUENCE SET; Schema: nacos; Owner: root
--

SELECT pg_catalog.setval('nacos.config_tags_relation_nid_seq', 1, false);


--
-- Name: group_capacity_id_seq; Type: SEQUENCE SET; Schema: nacos; Owner: root
--

SELECT pg_catalog.setval('nacos.group_capacity_id_seq', 1, false);


--
-- Name: his_config_info_nid_seq; Type: SEQUENCE SET; Schema: nacos; Owner: root
--

SELECT pg_catalog.setval('nacos.his_config_info_nid_seq', 24, true);


--
-- Name: tenant_capacity_id_seq; Type: SEQUENCE SET; Schema: nacos; Owner: root
--

SELECT pg_catalog.setval('nacos.tenant_capacity_id_seq', 1, false);


--
-- Name: tenant_info_id_seq; Type: SEQUENCE SET; Schema: nacos; Owner: root
--

SELECT pg_catalog.setval('nacos.tenant_info_id_seq', 1, false);


--
-- Name: ai_resource ai_resource_pkey; Type: CONSTRAINT; Schema: nacos; Owner: root
--

ALTER TABLE ONLY nacos.ai_resource
    ADD CONSTRAINT ai_resource_pkey PRIMARY KEY (id);


--
-- Name: ai_resource_version ai_resource_version_pkey; Type: CONSTRAINT; Schema: nacos; Owner: root
--

ALTER TABLE ONLY nacos.ai_resource_version
    ADD CONSTRAINT ai_resource_version_pkey PRIMARY KEY (id);


--
-- Name: config_info_gray config_info_gray_pkey; Type: CONSTRAINT; Schema: nacos; Owner: root
--

ALTER TABLE ONLY nacos.config_info_gray
    ADD CONSTRAINT config_info_gray_pkey PRIMARY KEY (id);


--
-- Name: config_info config_info_pkey; Type: CONSTRAINT; Schema: nacos; Owner: root
--

ALTER TABLE ONLY nacos.config_info
    ADD CONSTRAINT config_info_pkey PRIMARY KEY (id);


--
-- Name: config_tags_relation config_tags_relation_pkey; Type: CONSTRAINT; Schema: nacos; Owner: root
--

ALTER TABLE ONLY nacos.config_tags_relation
    ADD CONSTRAINT config_tags_relation_pkey PRIMARY KEY (nid);


--
-- Name: group_capacity group_capacity_pkey; Type: CONSTRAINT; Schema: nacos; Owner: root
--

ALTER TABLE ONLY nacos.group_capacity
    ADD CONSTRAINT group_capacity_pkey PRIMARY KEY (id);


--
-- Name: his_config_info his_config_info_pkey; Type: CONSTRAINT; Schema: nacos; Owner: root
--

ALTER TABLE ONLY nacos.his_config_info
    ADD CONSTRAINT his_config_info_pkey PRIMARY KEY (nid);


--
-- Name: pipeline_execution pipeline_execution_pkey; Type: CONSTRAINT; Schema: nacos; Owner: root
--

ALTER TABLE ONLY nacos.pipeline_execution
    ADD CONSTRAINT pipeline_execution_pkey PRIMARY KEY (execution_id);


--
-- Name: tenant_capacity tenant_capacity_pkey; Type: CONSTRAINT; Schema: nacos; Owner: root
--

ALTER TABLE ONLY nacos.tenant_capacity
    ADD CONSTRAINT tenant_capacity_pkey PRIMARY KEY (id);


--
-- Name: idx_ai_resource_gmt_modified; Type: INDEX; Schema: nacos; Owner: root
--

CREATE INDEX idx_ai_resource_gmt_modified ON nacos.ai_resource USING btree (gmt_modified);


--
-- Name: idx_ai_resource_name; Type: INDEX; Schema: nacos; Owner: root
--

CREATE INDEX idx_ai_resource_name ON nacos.ai_resource USING btree (name);


--
-- Name: idx_ai_resource_type; Type: INDEX; Schema: nacos; Owner: root
--

CREATE INDEX idx_ai_resource_type ON nacos.ai_resource USING btree (type);


--
-- Name: idx_ai_resource_ver_gmt_modified; Type: INDEX; Schema: nacos; Owner: root
--

CREATE INDEX idx_ai_resource_ver_gmt_modified ON nacos.ai_resource_version USING btree (gmt_modified);


--
-- Name: idx_ai_resource_ver_name; Type: INDEX; Schema: nacos; Owner: root
--

CREATE INDEX idx_ai_resource_ver_name ON nacos.ai_resource_version USING btree (name);


--
-- Name: idx_ai_resource_ver_status; Type: INDEX; Schema: nacos; Owner: root
--

CREATE INDEX idx_ai_resource_ver_status ON nacos.ai_resource_version USING btree (status);


--
-- Name: idx_dataid_gmt_modified_gray; Type: INDEX; Schema: nacos; Owner: root
--

CREATE INDEX idx_dataid_gmt_modified_gray ON nacos.config_info_gray USING btree (data_id, gmt_modified);


--
-- Name: idx_did; Type: INDEX; Schema: nacos; Owner: root
--

CREATE INDEX idx_did ON nacos.his_config_info USING btree (data_id);


--
-- Name: idx_gmt_create; Type: INDEX; Schema: nacos; Owner: root
--

CREATE INDEX idx_gmt_create ON nacos.his_config_info USING btree (gmt_create);


--
-- Name: idx_gmt_modified; Type: INDEX; Schema: nacos; Owner: root
--

CREATE INDEX idx_gmt_modified ON nacos.his_config_info USING btree (gmt_modified);


--
-- Name: idx_gmt_modified_gray; Type: INDEX; Schema: nacos; Owner: root
--

CREATE INDEX idx_gmt_modified_gray ON nacos.config_info_gray USING btree (gmt_modified);


--
-- Name: idx_tenant_id; Type: INDEX; Schema: nacos; Owner: root
--

CREATE INDEX idx_tenant_id ON nacos.config_tags_relation USING btree (tenant_id);


--
-- Name: uk_ai_resource_ns_name_type; Type: INDEX; Schema: nacos; Owner: root
--

CREATE UNIQUE INDEX uk_ai_resource_ns_name_type ON nacos.ai_resource USING btree (namespace_id, name, type, c_from);


--
-- Name: uk_ai_resource_ver_ns_name_type_ver; Type: INDEX; Schema: nacos; Owner: root
--

CREATE UNIQUE INDEX uk_ai_resource_ver_ns_name_type_ver ON nacos.ai_resource_version USING btree (namespace_id, name, type, version);


--
-- Name: uk_configinfo_datagrouptenant; Type: INDEX; Schema: nacos; Owner: root
--

CREATE UNIQUE INDEX uk_configinfo_datagrouptenant ON nacos.config_info USING btree (data_id, group_id, tenant_id);


--
-- Name: uk_configinfogray_datagrouptenantgray; Type: INDEX; Schema: nacos; Owner: root
--

CREATE UNIQUE INDEX uk_configinfogray_datagrouptenantgray ON nacos.config_info_gray USING btree (data_id, group_id, tenant_id, gray_name);


--
-- Name: uk_configtagrelation_configidtag; Type: INDEX; Schema: nacos; Owner: root
--

CREATE UNIQUE INDEX uk_configtagrelation_configidtag ON nacos.config_tags_relation USING btree (id, tag_name, tag_type);


--
-- Name: uk_group_id; Type: INDEX; Schema: nacos; Owner: root
--

CREATE UNIQUE INDEX uk_group_id ON nacos.group_capacity USING btree (group_id);


--
-- Name: uk_role_permission; Type: INDEX; Schema: nacos; Owner: root
--

CREATE UNIQUE INDEX uk_role_permission ON nacos.permissions USING btree (role, resource, action);


--
-- Name: uk_tenant_id; Type: INDEX; Schema: nacos; Owner: root
--

CREATE UNIQUE INDEX uk_tenant_id ON nacos.tenant_capacity USING btree (tenant_id);


--
-- Name: uk_tenant_info_kptenantid; Type: INDEX; Schema: nacos; Owner: root
--

CREATE UNIQUE INDEX uk_tenant_info_kptenantid ON nacos.tenant_info USING btree (kp, tenant_id);


--
-- Name: uk_username_role; Type: INDEX; Schema: nacos; Owner: root
--

CREATE UNIQUE INDEX uk_username_role ON nacos.roles USING btree (username, role);


--
-- PostgreSQL database dump complete
--

