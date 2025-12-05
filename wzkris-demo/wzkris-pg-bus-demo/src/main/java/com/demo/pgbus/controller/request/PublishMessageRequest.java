package com.demo.pgbus.controller.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class PublishMessageRequest {

    /**
     * 可选：覆盖默认 channel
     */
    private String channel;

    @NotBlank(message = "标题不能为空")
    private String title;

    @NotBlank(message = "消息内容不能为空")
    private String payload;

}

