package com.demo.pgbus.support;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCallback;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;

@Slf4j
@Component
public class PgNotifyClient {

    private final JdbcTemplate jdbcTemplate;

    public PgNotifyClient(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public void notifyChannel(String channel, String payload) {
        // pg_notify() 函数在事务提交时才会发送通知
        // 使用 execute() 执行 SELECT 语句，因为 pg_notify() 虽然返回 void，但语法上需要用 SELECT 调用
        jdbcTemplate.execute("SELECT pg_notify(?, ?)", (PreparedStatementCallback<Object>) ps -> {
            ps.setString(1, channel);
            ps.setString(2, payload);
            ps.execute();
            return null;
        });
        log.debug("📤 已执行 PG NOTIFY: channel={}, payload={}", channel, payload);
    }

}

