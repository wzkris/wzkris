package com.demo.mq.controller;

import com.demo.mq.producer.OrderProducer;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/order")
@RequiredArgsConstructor
public class OrderSendController {

    private final OrderProducer orderProducer;

    @GetMapping("/send")
    public String send(@RequestParam("msg") String msg) {
        boolean ok = orderProducer.send("222", "tagA", msg);
        return ok ? "OK" : "FAIL";
    }

}


