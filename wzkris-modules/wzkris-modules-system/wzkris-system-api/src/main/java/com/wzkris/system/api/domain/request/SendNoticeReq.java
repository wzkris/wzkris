package com.wzkris.system.api.domain.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SendNoticeReq implements Serializable {

    private List<Long> userIds;

    private String title;

    private String content;
}
