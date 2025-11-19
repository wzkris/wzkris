-- demo_bus_message DDL
CREATE TABLE IF NOT EXISTS demo_bus_message
(
    id         BIGINT PRIMARY KEY,
    channel    VARCHAR(63) NOT NULL,
    title      VARCHAR(128) NOT NULL,
    payload    TEXT         NOT NULL,
    status     VARCHAR(32)  NOT NULL,
    create_at  TIMESTAMP    NOT NULL DEFAULT NOW(),
    update_at  TIMESTAMP    NOT NULL DEFAULT NOW()
);

COMMENT ON TABLE demo_bus_message IS 'PG bus 消息队列表';
COMMENT ON COLUMN demo_bus_message.channel IS 'NOTIFY/LISTEN channel';

CREATE INDEX IF NOT EXISTS idx_demo_bus_message_channel ON demo_bus_message (channel);
CREATE INDEX IF NOT EXISTS idx_demo_bus_message_status ON demo_bus_message (status);

-- 准备一个触发器示例：入库时自动调用 pg_notify，供 SQL Console 验证
CREATE OR REPLACE FUNCTION demo_bus_notify_trigger() RETURNS trigger AS
$BODY$
BEGIN
    PERFORM pg_notify(NEW.channel, json_build_object('id', NEW.id, 'title', NEW.title, 'payload', NEW.payload)::text);
    RETURN NEW;
END;
$BODY$ LANGUAGE plpgsql;

DROP TRIGGER IF EXISTS trg_demo_bus_message_notify ON demo_bus_message;
CREATE TRIGGER trg_demo_bus_message_notify
    AFTER INSERT
    ON demo_bus_message
    FOR EACH ROW
EXECUTE FUNCTION demo_bus_notify_trigger();

