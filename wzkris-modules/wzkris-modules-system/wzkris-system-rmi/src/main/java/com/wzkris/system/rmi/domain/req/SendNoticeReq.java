package com.wzkris.system.rmi.domain.req;

import java.io.Serializable;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SendNoticeReq implements Serializable {

    private List<Long> userIds;

    private String title;

    private String content;
}
