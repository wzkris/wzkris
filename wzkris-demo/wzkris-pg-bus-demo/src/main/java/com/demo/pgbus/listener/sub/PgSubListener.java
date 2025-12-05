package com.demo.pgbus.listener.sub;

import com.demo.pgbus.event.PgNotifyEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class PgSubListener {

    @EventListener
    public void testListener(PgNotifyEvent pgNotifyEvent) {
        log.info("{}", pgNotifyEvent);
    }

}
