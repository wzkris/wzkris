package com.wzkris.system.api.domain.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SendNotifyReq {

    private List<Long> userIds;

    private String title;

    private String content;
}
