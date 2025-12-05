package com.wzkris.common.notifier.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 通知结果DTO
 *
 * @author wzkris
 * @date 2025/11/06
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NotificationResult {

    /**
     * 是否成功
     */
    private Boolean success;

    /**
     * 消息ID（用于追踪、撤回等）
     */
    private String messageId;

    /**
     * 错误信息
     */
    private String errorMessage;

    /**
     * 扩展信息
     */
    private Object data;

    /**
     * 创建成功结果
     */
    public static NotificationResult success(String messageId) {
        return NotificationResult.builder()
                .success(true)
                .messageId(messageId)
                .build();
    }

    /**
     * 创建成功结果（带数据）
     */
    public static NotificationResult success(String messageId, Object data) {
        return NotificationResult.builder()
                .success(true)
                .messageId(messageId)
                .data(data)
                .build();
    }

    /**
     * 创建失败结果
     */
    public static NotificationResult failure(String errorMessage) {
        return NotificationResult.builder()
                .success(false)
                .errorMessage(errorMessage)
                .build();
    }

}
