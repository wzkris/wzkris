package com.wzkris.system.listener.event;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "刷新系统配置事件")
public class RefreshConfigEvent {

    private String key;

    private String value;
}
