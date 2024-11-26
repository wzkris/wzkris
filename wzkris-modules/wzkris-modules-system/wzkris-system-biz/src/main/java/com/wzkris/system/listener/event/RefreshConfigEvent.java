package com.wzkris.system.listener.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RefreshConfigEvent {

    private String key;

    private String value;
}
