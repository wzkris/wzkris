-- ----------------------------
-- 支付网关 wzkris_payment
-- 平台统一基础设施
-- 审计字段对齐 BaseEntity：creator_id/create_at/updater_id/update_at/hint/deleted
-- 主键统一为 id（bigint，应用层雪花生成）
-- ----------------------------

-- ----------------------------
-- 1、渠道商户配置（每渠道可有多商户，由调用方通过 configId 指定使用的配置）
-- ----------------------------
CREATE TABLE IF NOT EXISTS biz.pay_channel_config (
    id             bigint           NOT NULL,
    channel        varchar(16)      NOT NULL,
    name           varchar(64)      NOT NULL,
    app_id         varchar(64),
    mch_id         varchar(64),
    sub_app_id     varchar(64),
    sub_mch_id     varchar(64),
    api_key        varchar(512),
    private_key    text,
    public_cert    text,
    cert_serial_no varchar(128),
    notify_url     varchar(256),
    pay_modes      varchar(128),
    status         varchar(16)      DEFAULT 'ENABLED' NOT NULL,
    remark         varchar(256),
    creator_id     bigint,
    create_at      timestamptz      DEFAULT now() NOT NULL,
    updater_id     bigint,
    update_at      timestamptz      DEFAULT now() NOT NULL,
    hint           varchar(64),
    deleted        boolean          DEFAULT false NOT NULL,
    CONSTRAINT pay_channel_config_pkey PRIMARY KEY (id)
);
COMMENT ON TABLE  biz.pay_channel_config IS '渠道商户配置';
COMMENT ON COLUMN biz.pay_channel_config.id IS '配置ID';
COMMENT ON COLUMN biz.pay_channel_config.channel IS '支付渠道 WXPAY/ALIPAY';
COMMENT ON COLUMN biz.pay_channel_config.name IS '配置名称(人工识别)';
COMMENT ON COLUMN biz.pay_channel_config.app_id IS '应用ID';
COMMENT ON COLUMN biz.pay_channel_config.mch_id IS '商户号';
COMMENT ON COLUMN biz.pay_channel_config.sub_app_id IS '子应用ID(服务商模式)';
COMMENT ON COLUMN biz.pay_channel_config.sub_mch_id IS '子商户号(服务商模式)';
COMMENT ON COLUMN biz.pay_channel_config.api_key IS 'API密钥(微信APIv3密钥apiV3Key)';
COMMENT ON COLUMN biz.pay_channel_config.private_key IS '商户私钥PEM(微信apiclient_key.pem)';
COMMENT ON COLUMN biz.pay_channel_config.public_cert IS '平台证书/公钥(微信v3平台证书可由SDK自动下载,可留空)';
COMMENT ON COLUMN biz.pay_channel_config.cert_serial_no IS '商户证书序列号(微信v3签名必需)';
COMMENT ON COLUMN biz.pay_channel_config.notify_url IS '渠道回调地址(需含configId路由商户,如 /pay/notify/WXPAY/{config_id})';
COMMENT ON COLUMN biz.pay_channel_config.pay_modes IS '支持的支付方式,逗号分隔 JSAPI,NATIVE,APP,H5';
COMMENT ON COLUMN biz.pay_channel_config.status IS '状态 ENABLED/DISABLED';
CREATE UNIQUE INDEX uk_pay_channel_config_channel_mch ON biz.pay_channel_config (channel, mch_id) WHERE deleted = false;

-- ----------------------------
-- 2、支付订单（网关统一订单，快照 config_id）
-- ----------------------------
CREATE TABLE IF NOT EXISTS biz.pay_order (
    id               bigint           NOT NULL,
    order_no         varchar(32)      NOT NULL,
    biz_type         varchar(32)      NOT NULL,
    biz_no           varchar(64)      NOT NULL,
    channel          varchar(16)      NOT NULL,
    config_id        bigint           NOT NULL,
    pay_mode         varchar(16),
    subject          varchar(128),
    amount           numeric(18,2)    NOT NULL,
    currency         varchar(8)       DEFAULT 'CNY' NOT NULL,
    refunded_amount  numeric(18,2)    DEFAULT 0 NOT NULL,
    status           varchar(16)      NOT NULL,
    payer_id         varchar(64),
    client_ip        varchar(64),
    expire_at        timestamptz,
    channel_order_no varchar(64),
    pay_at           timestamptz,
    fail_reason      varchar(256),
    notify_url       varchar(256),
    creator_id       bigint,
    create_at        timestamptz      DEFAULT now() NOT NULL,
    updater_id       bigint,
    update_at        timestamptz      DEFAULT now() NOT NULL,
    hint             varchar(64),
    deleted          boolean          DEFAULT false NOT NULL,
    CONSTRAINT pay_order_pkey PRIMARY KEY (id)
);
COMMENT ON TABLE  biz.pay_order IS '支付订单';
COMMENT ON COLUMN biz.pay_order.order_no IS '业务可读订单号';
COMMENT ON COLUMN biz.pay_order.biz_type IS '业务类型(标识业务方,回调路由用)';
COMMENT ON COLUMN biz.pay_order.biz_no IS '业务方订单号(与biz_type组成幂等键)';
COMMENT ON COLUMN biz.pay_order.channel IS '支付渠道';
COMMENT ON COLUMN biz.pay_order.config_id IS '成交时渠道配置ID快照(配置变更后仍可溯源)';
COMMENT ON COLUMN biz.pay_order.pay_mode IS '支付方式 JSAPI/NATIVE/APP/H5';
COMMENT ON COLUMN biz.pay_order.amount IS '支付金额(元)';
COMMENT ON COLUMN biz.pay_order.currency IS '币种(默认CNY)';
COMMENT ON COLUMN biz.pay_order.refunded_amount IS '已退款金额(含退款中,作超额退款原子护栏;退款失败回退)';
COMMENT ON COLUMN biz.pay_order.status IS '订单状态 PENDING/SUCCESS/CLOSED/FAILED';
COMMENT ON COLUMN biz.pay_order.payer_id IS '支付者标识(微信open_id/支付宝buyer_id)';
COMMENT ON COLUMN biz.pay_order.expire_at IS '过期时间';
COMMENT ON COLUMN biz.pay_order.channel_order_no IS '渠道侧交易号';
COMMENT ON COLUMN biz.pay_order.pay_at IS '支付成功时间';
COMMENT ON COLUMN biz.pay_order.fail_reason IS '失败原因';
COMMENT ON COLUMN biz.pay_order.notify_url IS '业务方通知地址';
CREATE UNIQUE INDEX uk_pay_order_biz      ON biz.pay_order (biz_type, biz_no) WHERE deleted = false;
CREATE UNIQUE INDEX uk_pay_order_no       ON biz.pay_order (order_no) WHERE deleted = false;
CREATE INDEX        idx_pay_order_status  ON biz.pay_order (status, expire_at) WHERE deleted = false;
CREATE INDEX        idx_pay_order_chno    ON biz.pay_order (channel_order_no) WHERE deleted = false AND channel_order_no IS NOT NULL;

-- ----------------------------
-- 3、渠道交互留痕（一次下单/查单的请求响应留痕，重试可能换配置）
-- ----------------------------
CREATE TABLE IF NOT EXISTS biz.pay_channel_log (
    id               bigint           NOT NULL,
    pay_order_id     bigint           NOT NULL,
    channel          varchar(16)      NOT NULL,
    config_id        bigint           NOT NULL,
    channel_prepay_no varchar(128),
    pay_mode         varchar(16),
    request_params   text,
    response_params  text,
    status           varchar(16)      NOT NULL,
    creator_id       bigint,
    create_at        timestamptz      DEFAULT now() NOT NULL,
    updater_id       bigint,
    update_at        timestamptz      DEFAULT now() NOT NULL,
    hint             varchar(64),
    deleted          boolean          DEFAULT false NOT NULL,
    CONSTRAINT pay_channel_log_pkey PRIMARY KEY (id)
);
COMMENT ON TABLE  biz.pay_channel_log IS '渠道交互留痕';
COMMENT ON COLUMN biz.pay_channel_log.pay_order_id IS '支付订单ID';
COMMENT ON COLUMN biz.pay_channel_log.config_id IS '本次交互所用配置ID';
COMMENT ON COLUMN biz.pay_channel_log.channel_prepay_no IS '渠道预下单号(如prepay_id)';
COMMENT ON COLUMN biz.pay_channel_log.request_params IS '渠道请求参数';
COMMENT ON COLUMN biz.pay_channel_log.response_params IS '渠道返回参数';
COMMENT ON COLUMN biz.pay_channel_log.status IS '交互状态 PENDING/SUCCESS/FAILED';
CREATE INDEX idx_pay_channel_log_order ON biz.pay_channel_log (pay_order_id) WHERE deleted = false;

-- ----------------------------
-- 4、退款订单（快照 config_id）
-- ----------------------------
CREATE TABLE IF NOT EXISTS biz.pay_refund_order (
    id               bigint           NOT NULL,
    refund_no        varchar(32)      NOT NULL,
    pay_order_id     bigint           NOT NULL,
    channel          varchar(16)      NOT NULL,
    config_id        bigint           NOT NULL,
    refund_amount    numeric(18,2)    NOT NULL,
    status           varchar(16)      NOT NULL,
    reason           varchar(256),
    channel_refund_no varchar(64),
    refund_at        timestamptz,
    fail_reason      varchar(256),
    creator_id       bigint,
    create_at        timestamptz      DEFAULT now() NOT NULL,
    updater_id       bigint,
    update_at        timestamptz      DEFAULT now() NOT NULL,
    hint             varchar(64),
    deleted          boolean          DEFAULT false NOT NULL,
    CONSTRAINT pay_refund_order_pkey PRIMARY KEY (id)
);
COMMENT ON TABLE  biz.pay_refund_order IS '退款订单';
COMMENT ON COLUMN biz.pay_refund_order.refund_no IS '退款单号';
COMMENT ON COLUMN biz.pay_refund_order.pay_order_id IS '原支付订单ID';
COMMENT ON COLUMN biz.pay_refund_order.config_id IS '退款所用配置ID快照';
COMMENT ON COLUMN biz.pay_refund_order.refund_amount IS '退款金额(元)';
COMMENT ON COLUMN biz.pay_refund_order.status IS '退款状态 REFUNDING/SUCCESS/FAILED';
COMMENT ON COLUMN biz.pay_refund_order.channel_refund_no IS '渠道侧退款单号';
COMMENT ON COLUMN biz.pay_refund_order.refund_at IS '退款成功时间';
CREATE UNIQUE INDEX uk_pay_refund_no        ON biz.pay_refund_order (refund_no) WHERE deleted = false;
CREATE INDEX        idx_pay_refund_payorder  ON biz.pay_refund_order (pay_order_id) WHERE deleted = false;

-- ----------------------------
-- 5、渠道回调记录（渠道->网关，验签+幂等，notify_type 区分支付/退款，我方业务号做幂等键）
--    命名 pay_channel_notify 以区分 网关->业务方 的 pay_notify_task
-- ----------------------------
CREATE TABLE IF NOT EXISTS biz.pay_channel_notify (
    id             bigint           NOT NULL,
    channel        varchar(16)      NOT NULL,
    notify_type    varchar(8),
    out_business_no varchar(64),
    channel_no     varchar(64),
    notify_data    text             NOT NULL,
    verify_result  boolean          NOT NULL,
    processed      boolean          DEFAULT false NOT NULL,
    processed_at   timestamptz,
    error_msg      varchar(256),
    creator_id     bigint,
    create_at      timestamptz      DEFAULT now() NOT NULL,
    updater_id     bigint,
    update_at      timestamptz      DEFAULT now() NOT NULL,
    hint           varchar(64),
    deleted        boolean          DEFAULT false NOT NULL,
    CONSTRAINT pay_channel_notify_pkey PRIMARY KEY (id)
);
COMMENT ON TABLE  biz.pay_channel_notify IS '渠道回调记录(渠道->网关)';
COMMENT ON COLUMN biz.pay_channel_notify.notify_type IS '回调类型 PAY/REFUND(验签失败时可为空)';
COMMENT ON COLUMN biz.pay_channel_notify.out_business_no IS '我方业务号(PAY=order_no/REFUND=refund_no),幂等键(验签失败时可为空)';
COMMENT ON COLUMN biz.pay_channel_notify.channel_no IS '渠道侧号(transaction_id/refund_id/trade_no),仅存档';
COMMENT ON COLUMN biz.pay_channel_notify.notify_data IS '回调原始报文';
COMMENT ON COLUMN biz.pay_channel_notify.verify_result IS '验签结果';
COMMENT ON COLUMN biz.pay_channel_notify.processed IS '是否已处理(幂等标记)';
COMMENT ON COLUMN biz.pay_channel_notify.error_msg IS '处理错误信息';
-- 渠道可能重发多次回调，按渠道+类型+我方业务号幂等；验签失败记录此二列为空(不参与幂等)；回调记录永不软删，故不带 WHERE deleted=false
CREATE UNIQUE INDEX uk_pay_channel_notify ON biz.pay_channel_notify (channel, notify_type, out_business_no);

-- ----------------------------
-- 6、业务方通知任务（网关->业务方，带重试，支持 PAY/REFUND）
-- ----------------------------
CREATE TABLE IF NOT EXISTS biz.pay_notify_task (
    id              bigint           NOT NULL,
    notify_type     varchar(8)       NOT NULL,
    pay_order_id    bigint           NOT NULL,
    refund_order_id bigint,
    biz_type        varchar(32)      NOT NULL,
    target_url      varchar(256),
    payload         text             NOT NULL,
    http_status     integer,
    retry_count     integer          DEFAULT 0 NOT NULL,
    max_retry       integer          DEFAULT 8 NOT NULL,
    next_retry_at   timestamptz,
    status          varchar(16)      NOT NULL,
    error_msg       varchar(256),
    creator_id      bigint,
    create_at       timestamptz      DEFAULT now() NOT NULL,
    updater_id      bigint,
    update_at       timestamptz      DEFAULT now() NOT NULL,
    hint            varchar(64),
    deleted         boolean          DEFAULT false NOT NULL,
    CONSTRAINT pay_notify_task_pkey PRIMARY KEY (id)
);
COMMENT ON TABLE  biz.pay_notify_task IS '业务方通知任务';
COMMENT ON COLUMN biz.pay_notify_task.notify_type IS '通知类型 PAY/REFUND';
COMMENT ON COLUMN biz.pay_notify_task.pay_order_id IS '支付订单ID';
COMMENT ON COLUMN biz.pay_notify_task.refund_order_id IS '退款订单ID(REFUND类型时填)';
COMMENT ON COLUMN biz.pay_notify_task.biz_type IS '业务类型(路由业务方)';
COMMENT ON COLUMN biz.pay_notify_task.target_url IS '业务方通知地址';
COMMENT ON COLUMN biz.pay_notify_task.payload IS '通知报文';
COMMENT ON COLUMN biz.pay_notify_task.http_status IS '最近一次HTTP状态码';
COMMENT ON COLUMN biz.pay_notify_task.retry_count IS '已重试次数';
COMMENT ON COLUMN biz.pay_notify_task.max_retry IS '最大重试次数';
COMMENT ON COLUMN biz.pay_notify_task.next_retry_at IS '下次重试时间';
COMMENT ON COLUMN biz.pay_notify_task.status IS '任务状态 PENDING/SENDING/SUCCESS/FAILED';
CREATE INDEX        idx_pay_notify_task_retry   ON biz.pay_notify_task (status, next_retry_at) WHERE deleted = false;
CREATE UNIQUE INDEX uk_pay_notify_task_pay      ON biz.pay_notify_task (pay_order_id) WHERE notify_type = 'PAY' AND deleted = false;
CREATE UNIQUE INDEX uk_pay_notify_task_refund   ON biz.pay_notify_task (refund_order_id) WHERE notify_type = 'REFUND' AND deleted = false;
