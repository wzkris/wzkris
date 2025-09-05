package com.wzkris.system.rmi.domain.req;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * 发送通知请求体
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class NotificationReq implements Serializable {

    private List<Long> userIds;

    private String title;

    private String content;

}
