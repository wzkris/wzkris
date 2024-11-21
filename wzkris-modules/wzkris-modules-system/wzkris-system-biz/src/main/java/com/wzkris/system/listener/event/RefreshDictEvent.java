package com.wzkris.system.listener.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RefreshDictEvent {

    /**
     * 字典类型
     */
    private List<String> dictTypes;
}
