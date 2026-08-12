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

DROP DATABASE IF EXISTS wzkris_payment;
--
-- Name: wzkris_payment; Type: DATABASE; Schema: -; Owner: postgres
--

CREATE DATABASE wzkris_payment WITH TEMPLATE = template0 ENCODING = 'UTF8' LOCALE_PROVIDER = libc LOCALE = 'Chinese (Simplified)_China.936';


ALTER DATABASE wzkris_payment OWNER TO postgres;

\connect wzkris_payment

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

SET default_tablespace = '';

SET default_table_access_method = heap;

--
-- Name: pay_channel_config; Type: TABLE; Schema: biz; Owner: postgres
--

CREATE TABLE biz.pay_channel_config (
    id bigint NOT NULL,
    channel character varying(16) NOT NULL,
    name character varying(64) NOT NULL,
    app_id character varying(64),
    mch_id character varying(64),
    sub_app_id character varying(64),
    sub_mch_id character varying(64),
    api_key character varying(512),
    private_key text,
    public_cert text,
    cert_serial_no character varying(128),
    notify_url character varying(256),
    refund_notify_url character varying(256),
    pay_modes character varying(128),
    status character varying(16) DEFAULT 'ENABLED'::character varying NOT NULL,
    remark character varying(256),
    creator_id bigint,
    create_at timestamp with time zone DEFAULT now() NOT NULL,
    updater_id bigint,
    update_at timestamp with time zone DEFAULT now() NOT NULL,
    hint character varying(64),
    deleted boolean DEFAULT false NOT NULL
);


ALTER TABLE biz.pay_channel_config OWNER TO postgres;

--
-- Name: TABLE pay_channel_config; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON TABLE biz.pay_channel_config IS '渠道商户配置';


--
-- Name: COLUMN pay_channel_config.id; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.pay_channel_config.id IS '配置ID';


--
-- Name: COLUMN pay_channel_config.channel; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.pay_channel_config.channel IS '支付渠道 wxpay/alipay';


--
-- Name: COLUMN pay_channel_config.name; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.pay_channel_config.name IS '配置名称(人工识别)';


--
-- Name: COLUMN pay_channel_config.app_id; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.pay_channel_config.app_id IS '应用ID';


--
-- Name: COLUMN pay_channel_config.mch_id; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.pay_channel_config.mch_id IS '商户号';


--
-- Name: COLUMN pay_channel_config.sub_app_id; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.pay_channel_config.sub_app_id IS '子应用ID(服务商模式)';


--
-- Name: COLUMN pay_channel_config.sub_mch_id; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.pay_channel_config.sub_mch_id IS '子商户号(服务商模式)';


--
-- Name: COLUMN pay_channel_config.api_key; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.pay_channel_config.api_key IS 'API密钥(微信APIv3密钥apiV3Key)';


--
-- Name: COLUMN pay_channel_config.private_key; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.pay_channel_config.private_key IS '商户私钥PEM(微信apiclient_key.pem)';


--
-- Name: COLUMN pay_channel_config.public_cert; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.pay_channel_config.public_cert IS '平台证书/公钥(微信v3平台证书可由SDK自动下载,可留空)';


--
-- Name: COLUMN pay_channel_config.cert_serial_no; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.pay_channel_config.cert_serial_no IS '商户证书序列号(微信v3签名必需)';


--
-- Name: COLUMN pay_channel_config.notify_url; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.pay_channel_config.notify_url IS '渠道支付回调地址';


--
-- Name: COLUMN pay_channel_config.refund_notify_url; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.pay_channel_config.refund_notify_url IS '渠道退款回调地址';


--
-- Name: COLUMN pay_channel_config.pay_modes; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.pay_channel_config.pay_modes IS '支持的支付方式,逗号分隔 JSAPI,NATIVE,APP,H5';


--
-- Name: COLUMN pay_channel_config.status; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.pay_channel_config.status IS '状态 ENABLED/DISABLED';


--
-- Name: pay_channel_log; Type: TABLE; Schema: biz; Owner: postgres
--

CREATE TABLE biz.pay_channel_log (
    id bigint NOT NULL,
    pay_order_id bigint NOT NULL,
    channel character varying(16) NOT NULL,
    config_id bigint NOT NULL,
    channel_prepay_no character varying(128),
    pay_mode character varying(16),
    request_params text,
    response_params text,
    status character varying(16) NOT NULL,
    creator_id bigint,
    create_at timestamp with time zone DEFAULT now() NOT NULL,
    updater_id bigint,
    update_at timestamp with time zone DEFAULT now() NOT NULL,
    hint character varying(64),
    deleted boolean DEFAULT false NOT NULL
);


ALTER TABLE biz.pay_channel_log OWNER TO postgres;

--
-- Name: TABLE pay_channel_log; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON TABLE biz.pay_channel_log IS '渠道交互留痕';


--
-- Name: COLUMN pay_channel_log.pay_order_id; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.pay_channel_log.pay_order_id IS '支付订单ID';


--
-- Name: COLUMN pay_channel_log.config_id; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.pay_channel_log.config_id IS '本次交互所用配置ID';


--
-- Name: COLUMN pay_channel_log.channel_prepay_no; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.pay_channel_log.channel_prepay_no IS '渠道预下单号(如prepay_id)';


--
-- Name: COLUMN pay_channel_log.request_params; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.pay_channel_log.request_params IS '渠道请求参数';


--
-- Name: COLUMN pay_channel_log.response_params; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.pay_channel_log.response_params IS '渠道返回参数';


--
-- Name: COLUMN pay_channel_log.status; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.pay_channel_log.status IS '交互状态 PENDING/SUCCESS/FAILED';


--
-- Name: channel_notify_log; Type: TABLE; Schema: biz; Owner: postgres
--

CREATE TABLE biz.channel_notify_log (
    id bigint NOT NULL,
    channel character varying(16) NOT NULL,
    notify_type character varying(8),
    out_business_no character varying(64),
    channel_no character varying(64),
    notify_data text NOT NULL,
    verify_result boolean NOT NULL,
    processed boolean DEFAULT false NOT NULL,
    processed_at timestamp with time zone,
    error_msg character varying(256),
    creator_id bigint,
    create_at timestamp with time zone DEFAULT now() NOT NULL,
    updater_id bigint,
    update_at timestamp with time zone DEFAULT now() NOT NULL,
    hint character varying(64),
    deleted boolean DEFAULT false NOT NULL
);


ALTER TABLE biz.channel_notify_log OWNER TO postgres;

--
-- Name: TABLE channel_notify_log; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON TABLE biz.channel_notify_log IS '渠道回调记录(渠道->网关)';


--
-- Name: COLUMN channel_notify_log.notify_type; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.channel_notify_log.notify_type IS '回调类型 PAY/REFUND(验签失败时可为空)';


--
-- Name: COLUMN channel_notify_log.out_business_no; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.channel_notify_log.out_business_no IS '我方业务号(PAY=order_no/REFUND=refund_no),幂等键(验签失败时可为空)';


--
-- Name: COLUMN channel_notify_log.channel_no; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.channel_notify_log.channel_no IS '渠道侧号(transaction_id/trade_no)';


--
-- Name: COLUMN channel_notify_log.notify_data; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.channel_notify_log.notify_data IS '回调原始报文';


--
-- Name: COLUMN channel_notify_log.verify_result; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.channel_notify_log.verify_result IS '验签结果';


--
-- Name: COLUMN channel_notify_log.processed; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.channel_notify_log.processed IS '是否已处理(幂等标记)';


--
-- Name: COLUMN channel_notify_log.error_msg; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.channel_notify_log.error_msg IS '处理错误信息';


--
-- Name: notify_task; Type: TABLE; Schema: biz; Owner: postgres
--

CREATE TABLE biz.notify_task (
    id bigint NOT NULL,
    notify_type character varying(8) NOT NULL,
    pay_order_id bigint NOT NULL,
    refund_order_id bigint,
    target_url character varying(256),
    payload text NOT NULL,
    http_status integer,
    retry_count integer DEFAULT 0 NOT NULL,
    max_retry integer DEFAULT 8 NOT NULL,
    next_retry_at timestamp with time zone,
    status character varying(16) NOT NULL,
    error_msg character varying(256),
    creator_id bigint,
    create_at timestamp with time zone DEFAULT now() NOT NULL,
    updater_id bigint,
    update_at timestamp with time zone DEFAULT now() NOT NULL,
    hint character varying(64),
    deleted boolean DEFAULT false NOT NULL
);


ALTER TABLE biz.notify_task OWNER TO postgres;

--
-- Name: TABLE notify_task; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON TABLE biz.notify_task IS '业务方通知任务';


--
-- Name: COLUMN notify_task.notify_type; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.notify_task.notify_type IS '通知类型 PAY/REFUND';


--
-- Name: COLUMN notify_task.pay_order_id; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.notify_task.pay_order_id IS '支付订单ID';


--
-- Name: COLUMN notify_task.refund_order_id; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.notify_task.refund_order_id IS '退款订单ID';


--
-- Name: COLUMN notify_task.target_url; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.notify_task.target_url IS '业务方通知地址';


--
-- Name: COLUMN notify_task.payload; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.notify_task.payload IS '通知报文';


--
-- Name: COLUMN notify_task.http_status; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.notify_task.http_status IS '最近一次HTTP状态码';


--
-- Name: COLUMN notify_task.retry_count; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.notify_task.retry_count IS '已重试次数';


--
-- Name: COLUMN notify_task.max_retry; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.notify_task.max_retry IS '最大重试次数';


--
-- Name: COLUMN notify_task.next_retry_at; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.notify_task.next_retry_at IS '下次重试时间';


--
-- Name: COLUMN notify_task.status; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.notify_task.status IS '任务状态 PENDING/SENDING/SUCCESS/FAILED';


--
-- Name: pay_order; Type: TABLE; Schema: biz; Owner: postgres
--

CREATE TABLE biz.pay_order (
    id bigint NOT NULL,
    order_no character varying(32) NOT NULL,
    channel character varying(16) NOT NULL,
    config_id bigint NOT NULL,
    pay_mode character varying(16),
    subject character varying(128),
    amount numeric(18,2) NOT NULL,
    currency character varying(8) DEFAULT 'CNY'::character varying NOT NULL,
    refunded_amount numeric(18,2) DEFAULT 0 NOT NULL,
    status character varying(16) NOT NULL,
    payer_id character varying(64),
    client_ip character varying(64),
    expire_at timestamp with time zone,
    channel_prepay_data character varying(500),
    channel_prepay_time timestamp with time zone,
    channel_order_no character varying(64),
    pay_at timestamp with time zone,
    fail_reason character varying(256),
    notify_url character varying(256) NOT NULL,
    creator_id bigint,
    create_at timestamp with time zone DEFAULT now() NOT NULL,
    updater_id bigint,
    update_at timestamp with time zone DEFAULT now() NOT NULL,
    hint character varying(64),
    deleted boolean DEFAULT false NOT NULL
);


ALTER TABLE biz.pay_order OWNER TO postgres;

--
-- Name: TABLE pay_order; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON TABLE biz.pay_order IS '支付订单';


--
-- Name: COLUMN pay_order.order_no; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.pay_order.order_no IS '业务可读订单号';


--
-- Name: COLUMN pay_order.channel; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.pay_order.channel IS '支付渠道';


--
-- Name: COLUMN pay_order.config_id; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.pay_order.config_id IS '成交时渠道配置ID快照';


--
-- Name: COLUMN pay_order.pay_mode; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.pay_order.pay_mode IS '支付方式 JSAPI/NATIVE/APP/H5';


--
-- Name: COLUMN pay_order.amount; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.pay_order.amount IS '支付金额(元)';


--
-- Name: COLUMN pay_order.currency; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.pay_order.currency IS '币种(默认CNY)';


--
-- Name: COLUMN pay_order.refunded_amount; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.pay_order.refunded_amount IS '已退款金额(含退款中,退款失败回退)';


--
-- Name: COLUMN pay_order.status; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.pay_order.status IS '订单状态 PENDING/SUCCESS/CLOSED/FAILED';


--
-- Name: COLUMN pay_order.payer_id; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.pay_order.payer_id IS '支付者标识(微信open_id/支付宝buyer_id)';


--
-- Name: COLUMN pay_order.expire_at; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.pay_order.expire_at IS '过期时间';


--
-- Name: COLUMN pay_order.channel_prepay_data; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.pay_order.channel_prepay_data IS '渠道预下单返回数据';


--
-- Name: COLUMN pay_order.channel_prepay_time; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.pay_order.channel_prepay_time IS '渠道预下单时间';


--
-- Name: COLUMN pay_order.channel_order_no; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.pay_order.channel_order_no IS '渠道侧交易号';


--
-- Name: COLUMN pay_order.pay_at; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.pay_order.pay_at IS '支付成功时间';


--
-- Name: COLUMN pay_order.fail_reason; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.pay_order.fail_reason IS '失败原因';


--
-- Name: COLUMN pay_order.notify_url; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.pay_order.notify_url IS '业务方通知地址';


--
-- Name: refund_order; Type: TABLE; Schema: biz; Owner: postgres
--

CREATE TABLE biz.refund_order (
    id bigint NOT NULL,
    refund_no character varying(32) NOT NULL,
    pay_order_id bigint NOT NULL,
    order_no character varying(32) NOT NULL,
    channel character varying(16) NOT NULL,
    config_id bigint NOT NULL,
    refund_amount numeric(18,2) NOT NULL,
    status character varying(16) NOT NULL,
    reason character varying(256),
    channel_refund_no character varying(64),
    refund_at timestamp with time zone,
    fail_reason character varying(256),
    notify_url character varying(256) NOT NULL,
    creator_id bigint,
    create_at timestamp with time zone DEFAULT now() NOT NULL,
    updater_id bigint,
    update_at timestamp with time zone DEFAULT now() NOT NULL,
    hint character varying(64),
    deleted boolean DEFAULT false NOT NULL
);


ALTER TABLE biz.refund_order OWNER TO postgres;

--
-- Name: TABLE refund_order; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON TABLE biz.refund_order IS '退款订单';


--
-- Name: COLUMN refund_order.refund_no; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.refund_order.refund_no IS '退款单号';


--
-- Name: COLUMN refund_order.pay_order_id; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.refund_order.pay_order_id IS '原支付订单ID';


--
-- Name: COLUMN refund_order.order_no; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.refund_order.order_no IS '原支付订单号';


--
-- Name: COLUMN refund_order.config_id; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.refund_order.config_id IS '退款所用配置ID快照';


--
-- Name: COLUMN refund_order.refund_amount; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.refund_order.refund_amount IS '退款金额(元)';


--
-- Name: COLUMN refund_order.status; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.refund_order.status IS '退款状态 REFUNDING/SUCCESS/FAILED';


--
-- Name: COLUMN refund_order.channel_refund_no; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.refund_order.channel_refund_no IS '渠道侧退款单号';


--
-- Name: COLUMN refund_order.refund_at; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.refund_order.refund_at IS '退款成功时间';


--
-- Name: COLUMN refund_order.notify_url; Type: COMMENT; Schema: biz; Owner: postgres
--

COMMENT ON COLUMN biz.refund_order.notify_url IS '退款业务方通知地址';


--
-- Data for Name: pay_channel_config; Type: TABLE DATA; Schema: biz; Owner: postgres
--



--
-- Data for Name: pay_channel_log; Type: TABLE DATA; Schema: biz; Owner: postgres
--



--
-- Data for Name: channel_notify_log; Type: TABLE DATA; Schema: biz; Owner: postgres
--



--
-- Data for Name: notify_task; Type: TABLE DATA; Schema: biz; Owner: postgres
--



--
-- Data for Name: pay_order; Type: TABLE DATA; Schema: biz; Owner: postgres
--



--
-- Data for Name: refund_order; Type: TABLE DATA; Schema: biz; Owner: postgres
--



--
-- Name: pay_channel_config pay_channel_config_pkey; Type: CONSTRAINT; Schema: biz; Owner: postgres
--

ALTER TABLE ONLY biz.pay_channel_config
    ADD CONSTRAINT pay_channel_config_pkey PRIMARY KEY (id);


--
-- Name: pay_channel_log pay_channel_log_pkey; Type: CONSTRAINT; Schema: biz; Owner: postgres
--

ALTER TABLE ONLY biz.pay_channel_log
    ADD CONSTRAINT pay_channel_log_pkey PRIMARY KEY (id);


--
-- Name: channel_notify_log channel_notify_log_pkey; Type: CONSTRAINT; Schema: biz; Owner: postgres
--

ALTER TABLE ONLY biz.channel_notify_log
    ADD CONSTRAINT channel_notify_log_pkey PRIMARY KEY (id);


--
-- Name: notify_task notify_task_pkey; Type: CONSTRAINT; Schema: biz; Owner: postgres
--

ALTER TABLE ONLY biz.notify_task
    ADD CONSTRAINT notify_task_pkey PRIMARY KEY (id);


--
-- Name: pay_order pay_order_pkey; Type: CONSTRAINT; Schema: biz; Owner: postgres
--

ALTER TABLE ONLY biz.pay_order
    ADD CONSTRAINT pay_order_pkey PRIMARY KEY (id);


--
-- Name: refund_order refund_order_pkey; Type: CONSTRAINT; Schema: biz; Owner: postgres
--

ALTER TABLE ONLY biz.refund_order
    ADD CONSTRAINT refund_order_pkey PRIMARY KEY (id);


--
-- Name: idx_pay_channel_log_order; Type: INDEX; Schema: biz; Owner: postgres
--

CREATE INDEX idx_pay_channel_log_order ON biz.pay_channel_log USING btree (pay_order_id) WHERE (deleted = false);


--
-- Name: idx_notify_task_retry; Type: INDEX; Schema: biz; Owner: postgres
--

CREATE INDEX idx_notify_task_retry ON biz.notify_task USING btree (status, next_retry_at) WHERE (deleted = false);


--
-- Name: idx_pay_order_chno; Type: INDEX; Schema: biz; Owner: postgres
--

CREATE INDEX idx_pay_order_chno ON biz.pay_order USING btree (channel_order_no) WHERE ((deleted = false) AND (channel_order_no IS NOT NULL));


--
-- Name: idx_pay_order_status; Type: INDEX; Schema: biz; Owner: postgres
--

CREATE INDEX idx_pay_order_status ON biz.pay_order USING btree (status, expire_at) WHERE (deleted = false);


--
-- Name: idx_refund_payorder; Type: INDEX; Schema: biz; Owner: postgres
--

CREATE INDEX idx_refund_payorder ON biz.refund_order USING btree (pay_order_id) WHERE (deleted = false);


--
-- Name: uk_pay_channel_config_channel_mch; Type: INDEX; Schema: biz; Owner: postgres
--

CREATE UNIQUE INDEX uk_pay_channel_config_channel_mch ON biz.pay_channel_config USING btree (channel, mch_id) WHERE (deleted = false);


--
-- Name: uk_channel_notify_log; Type: INDEX; Schema: biz; Owner: postgres
--

CREATE UNIQUE INDEX uk_channel_notify_log ON biz.channel_notify_log USING btree (channel, notify_type, out_business_no) WHERE (deleted = false);


--
-- Name: uk_notify_task_pay; Type: INDEX; Schema: biz; Owner: postgres
--

CREATE UNIQUE INDEX uk_notify_task_pay ON biz.notify_task USING btree (pay_order_id) WHERE (((notify_type)::text = 'PAY'::text) AND (deleted = false));


--
-- Name: uk_notify_task_refund; Type: INDEX; Schema: biz; Owner: postgres
--

CREATE UNIQUE INDEX uk_notify_task_refund ON biz.notify_task USING btree (refund_order_id) WHERE (((notify_type)::text = 'REFUND'::text) AND (deleted = false));


--
-- Name: uk_pay_order_no; Type: INDEX; Schema: biz; Owner: postgres
--

CREATE UNIQUE INDEX uk_pay_order_no ON biz.pay_order USING btree (order_no) WHERE (deleted = false);


--
-- Name: uk_refund_no; Type: INDEX; Schema: biz; Owner: postgres
--

CREATE UNIQUE INDEX uk_refund_no ON biz.refund_order USING btree (refund_no) WHERE (deleted = false);


--
-- Name: customer_balance_info; Type: TABLE; Schema: biz; Owner: postgres
--

CREATE TABLE biz.customer_balance_info (
    id bigint NOT NULL,
    customer_id bigint NOT NULL,
    currency varchar(8) DEFAULT 'CNY'::character varying NOT NULL,
    balance numeric(10,2) NOT NULL,
    frozen numeric(10,2) DEFAULT 0 NOT NULL,
    total_in numeric(10,2) DEFAULT 0 NOT NULL,
    total_out numeric(10,2) DEFAULT 0 NOT NULL,
    status character(1) NOT NULL,
    creator_id bigint NOT NULL,
    updater_id bigint,
    create_at timestamp(0) with time zone NOT NULL,
    update_at timestamp(0) with time zone,
    hint character varying(10) DEFAULT ''::character varying NOT NULL,
    deleted boolean DEFAULT false NOT NULL
);


ALTER TABLE biz.customer_balance_info OWNER TO postgres;

COMMENT ON TABLE biz.customer_balance_info IS '用户账户';

COMMENT ON COLUMN biz.customer_balance_info.currency IS '币种';

COMMENT ON COLUMN biz.customer_balance_info.balance IS '可用余额, 元';

COMMENT ON COLUMN biz.customer_balance_info.frozen IS '冻结余额, 元(预留)';

COMMENT ON COLUMN biz.customer_balance_info.total_in IS '累计入账, 元';

COMMENT ON COLUMN biz.customer_balance_info.total_out IS '累计支出, 元';

COMMENT ON COLUMN biz.customer_balance_info.status IS '状态';


--
-- Name: customer_balance_transaction_log; Type: TABLE; Schema: biz; Owner: postgres
--

CREATE TABLE biz.customer_balance_transaction_log (
    id bigint NOT NULL,
    customer_id bigint NOT NULL,
    amount numeric(10,2) NOT NULL,
    record_type character(1) NOT NULL,
    biz_type character(1) NOT NULL,
    biz_no character varying(32) NOT NULL,
    before_balance numeric(10,2),
    after_balance numeric(10,2),
    create_at timestamp(0) with time zone NOT NULL,
    remark character varying(100),
    creator_id bigint NOT NULL,
    updater_id bigint,
    update_at timestamp(0) with time zone,
    hint character varying(10) DEFAULT ''::character varying NOT NULL,
    deleted boolean DEFAULT false NOT NULL
);


ALTER TABLE biz.customer_balance_transaction_log OWNER TO postgres;

COMMENT ON TABLE biz.customer_balance_transaction_log IS '用户资金流水';

COMMENT ON COLUMN biz.customer_balance_transaction_log.customer_id IS '客户ID';

COMMENT ON COLUMN biz.customer_balance_transaction_log.amount IS '金额, 元';

COMMENT ON COLUMN biz.customer_balance_transaction_log.record_type IS '记录类型';

COMMENT ON COLUMN biz.customer_balance_transaction_log.biz_type IS '业务类型';

COMMENT ON COLUMN biz.customer_balance_transaction_log.biz_no IS '业务编号';

COMMENT ON COLUMN biz.customer_balance_transaction_log.before_balance IS '变动前余额';

COMMENT ON COLUMN biz.customer_balance_transaction_log.after_balance IS '变动后余额';

COMMENT ON COLUMN biz.customer_balance_transaction_log.create_at IS '创建时间';

COMMENT ON COLUMN biz.customer_balance_transaction_log.remark IS '备注';


--
-- Name: tenant_balance_info; Type: TABLE; Schema: biz; Owner: postgres
--

CREATE TABLE biz.tenant_balance_info (
    id bigint NOT NULL,
    tenant_id bigint NOT NULL,
    currency varchar(8) DEFAULT 'CNY'::character varying NOT NULL,
    balance numeric(10,2) NOT NULL,
    frozen numeric(10,2) DEFAULT 0 NOT NULL,
    total_in numeric(10,2) DEFAULT 0 NOT NULL,
    total_out numeric(10,2) DEFAULT 0 NOT NULL,
    status character(1) NOT NULL,
    pay_password character varying(100),
    creator_id bigint NOT NULL,
    updater_id bigint,
    create_at timestamp(0) with time zone NOT NULL,
    update_at timestamp(0) with time zone,
    hint character varying(10) DEFAULT ''::character varying NOT NULL,
    deleted boolean DEFAULT false NOT NULL
);


ALTER TABLE biz.tenant_balance_info OWNER TO postgres;

COMMENT ON TABLE biz.tenant_balance_info IS '租户账户';

COMMENT ON COLUMN biz.tenant_balance_info.currency IS '币种';

COMMENT ON COLUMN biz.tenant_balance_info.balance IS '可用余额, 元';

COMMENT ON COLUMN biz.tenant_balance_info.frozen IS '冻结余额, 元(预留)';

COMMENT ON COLUMN biz.tenant_balance_info.total_in IS '累计入账, 元';

COMMENT ON COLUMN biz.tenant_balance_info.total_out IS '累计支出, 元';

COMMENT ON COLUMN biz.tenant_balance_info.status IS '状态';

COMMENT ON COLUMN biz.tenant_balance_info.pay_password IS '提现支付密码（bcrypt）';


--
-- Name: tenant_balance_transaction_log; Type: TABLE; Schema: biz; Owner: postgres
--

CREATE TABLE biz.tenant_balance_transaction_log (
    id bigint NOT NULL,
    tenant_id bigint NOT NULL,
    amount numeric(10,2) NOT NULL,
    record_type character(1) NOT NULL,
    biz_type character(1) NOT NULL,
    biz_no character varying(32) NOT NULL,
    before_balance numeric(10,2),
    after_balance numeric(10,2),
    create_at timestamp(0) with time zone NOT NULL,
    remark character varying(100),
    creator_id bigint NOT NULL,
    updater_id bigint,
    update_at timestamp(0) with time zone,
    hint character varying(10) DEFAULT ''::character varying NOT NULL,
    deleted boolean DEFAULT false NOT NULL
);


ALTER TABLE biz.tenant_balance_transaction_log OWNER TO postgres;

COMMENT ON TABLE biz.tenant_balance_transaction_log IS '租户资金流水';

COMMENT ON COLUMN biz.tenant_balance_transaction_log.tenant_id IS '租户ID';

COMMENT ON COLUMN biz.tenant_balance_transaction_log.amount IS '金额, 单位元';

COMMENT ON COLUMN biz.tenant_balance_transaction_log.record_type IS '记录类型';

COMMENT ON COLUMN biz.tenant_balance_transaction_log.biz_type IS '业务类型';

COMMENT ON COLUMN biz.tenant_balance_transaction_log.biz_no IS '业务编号';

COMMENT ON COLUMN biz.tenant_balance_transaction_log.create_at IS '创建时间';

COMMENT ON COLUMN biz.tenant_balance_transaction_log.remark IS '备注';


--
-- Name: tenant_balance_withdrawal_log; Type: TABLE; Schema: biz; Owner: postgres
--

CREATE TABLE biz.tenant_balance_withdrawal_log (
    id bigint NOT NULL,
    withdrawal_no character varying(32) NOT NULL,
    tenant_id bigint NOT NULL,
    channel character varying(16),
    config_id bigint,
    withdrawal_amount numeric(18,2) NOT NULL,
    currency character varying(8) DEFAULT 'CNY'::character varying NOT NULL,
    status character varying(16) NOT NULL,
    fail_reason character varying(256),
    channel_withdrawal_no character varying(64),
    withdrawal_at timestamp(0) with time zone,
    creator_id bigint,
    create_at timestamp(0) with time zone DEFAULT now() NOT NULL,
    updater_id bigint,
    update_at timestamp(0) with time zone DEFAULT now() NOT NULL,
    hint character varying(64),
    deleted boolean DEFAULT false NOT NULL
);


ALTER TABLE biz.tenant_balance_withdrawal_log OWNER TO postgres;

COMMENT ON TABLE biz.tenant_balance_withdrawal_log IS '系统提现记录';

COMMENT ON COLUMN biz.tenant_balance_withdrawal_log.withdrawal_no IS '提现单号';

COMMENT ON COLUMN biz.tenant_balance_withdrawal_log.tenant_id IS '租户id';

COMMENT ON COLUMN biz.tenant_balance_withdrawal_log.channel IS '打款渠道';

COMMENT ON COLUMN biz.tenant_balance_withdrawal_log.config_id IS '渠道配置id';

COMMENT ON COLUMN biz.tenant_balance_withdrawal_log.withdrawal_amount IS '提现金额, 单位元';

COMMENT ON COLUMN biz.tenant_balance_withdrawal_log.currency IS '币种';

COMMENT ON COLUMN biz.tenant_balance_withdrawal_log.status IS '状态
''PROCESSING'' 处理中
''SUCCESS'' 成功
''FAILED'' 失败';

COMMENT ON COLUMN biz.tenant_balance_withdrawal_log.fail_reason IS '失败原因';

COMMENT ON COLUMN biz.tenant_balance_withdrawal_log.channel_withdrawal_no IS '渠道提现单号';

COMMENT ON COLUMN biz.tenant_balance_withdrawal_log.withdrawal_at IS '提现完成时间';

COMMENT ON COLUMN biz.tenant_balance_withdrawal_log.creator_id IS '创建者';

COMMENT ON COLUMN biz.tenant_balance_withdrawal_log.create_at IS '创建时间';

COMMENT ON COLUMN biz.tenant_balance_withdrawal_log.hint IS '标签';


--
-- Name: customer_balance_info customer_balance_info_pkey; Type: CONSTRAINT; Schema: biz; Owner: postgres
--

ALTER TABLE ONLY biz.customer_balance_info
    ADD CONSTRAINT customer_balance_info_pkey PRIMARY KEY (id);


--
-- Name: customer_balance_transaction_log customer_balance_transaction_log_pkey; Type: CONSTRAINT; Schema: biz; Owner: postgres
--

ALTER TABLE ONLY biz.customer_balance_transaction_log
    ADD CONSTRAINT customer_balance_transaction_log_pkey PRIMARY KEY (id);


--
-- Name: tenant_balance_info tenant_balance_info_pkey; Type: CONSTRAINT; Schema: biz; Owner: postgres
--

ALTER TABLE ONLY biz.tenant_balance_info
    ADD CONSTRAINT tenant_balance_info_pkey PRIMARY KEY (id);


--
-- Name: tenant_balance_transaction_log tenant_balance_transaction_log_pkey; Type: CONSTRAINT; Schema: biz; Owner: postgres
--

ALTER TABLE ONLY biz.tenant_balance_transaction_log
    ADD CONSTRAINT tenant_balance_transaction_log_pkey PRIMARY KEY (id);


--
-- Name: tenant_balance_withdrawal_log tenant_balance_withdrawal_log_pkey; Type: CONSTRAINT; Schema: biz; Owner: postgres
--

ALTER TABLE ONLY biz.tenant_balance_withdrawal_log
    ADD CONSTRAINT tenant_balance_withdrawal_log_pkey PRIMARY KEY (id);


--
-- Name: idx_customer_balance_transaction_log_customer_id; Type: INDEX; Schema: biz; Owner: postgres
--

CREATE INDEX idx_customer_balance_transaction_log_customer_id ON biz.customer_balance_transaction_log USING btree (customer_id) WHERE (deleted = false);


--
-- Name: idx_tenant_balance_transaction_log_tenant_id; Type: INDEX; Schema: biz; Owner: postgres
--

CREATE INDEX idx_tenant_balance_transaction_log_tenant_id ON biz.tenant_balance_transaction_log USING btree (tenant_id) WHERE (deleted = false);


--
-- Name: idx_tenant_balance_withdrawal_log_withdrawal_no; Type: INDEX; Schema: biz; Owner: postgres
--

CREATE UNIQUE INDEX idx_tenant_balance_withdrawal_log_withdrawal_no ON biz.tenant_balance_withdrawal_log USING btree (withdrawal_no) WHERE (deleted = false);


--
-- Name: uk_customer_balance_info_owner; Type: INDEX; Schema: biz; Owner: postgres
--

CREATE UNIQUE INDEX uk_customer_balance_info_owner ON biz.customer_balance_info USING btree (customer_id) WHERE (deleted = false);


--
-- Name: uk_tenant_balance_info_owner; Type: INDEX; Schema: biz; Owner: postgres
--

CREATE UNIQUE INDEX uk_tenant_balance_info_owner ON biz.tenant_balance_info USING btree (tenant_id) WHERE (deleted = false);


--
-- PostgreSQL database dump complete
--

