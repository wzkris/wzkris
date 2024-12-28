package com.wzkris.system.api.domain.request;

import lombok.Data;

import java.util.List;

@Data
public class SendNotifyReq {

    private List<Long> userIds;

    private String title;

    private String content;
}
