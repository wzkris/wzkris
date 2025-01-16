package com.wzkris.system.listener.event;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "刷新字典事件")
public class RefreshDictEvent {

    /**
     * 字典类型
     */
    private List<String> dictTypes;
}
