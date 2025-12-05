package com.demo.pgbus.controller;

import com.demo.pgbus.controller.request.PublishMessageRequest;
import com.demo.pgbus.dal.entity.BusMessage;
import com.demo.pgbus.service.BusMessageService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "消息")
@RestController
@RequestMapping("/messages")
@RequiredArgsConstructor
public class BusMessageController {

    private final BusMessageService busMessageService;

    @PostMapping
    public BusMessage publish(@Validated @RequestBody PublishMessageRequest request) {
        return busMessageService.publish(request);
    }

    @GetMapping
    public List<BusMessage> listAll() {
        return busMessageService.list();
    }

}

