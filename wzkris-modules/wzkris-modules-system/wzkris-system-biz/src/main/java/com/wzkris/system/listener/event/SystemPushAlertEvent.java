package com.wzkris.system.listener.event;

import com.wzkris.system.domain.dto.SimpleMessageDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "系统推送弹窗事件")
public class SystemPushAlertEvent {

    private List<?> ids;

    private SimpleMessageDTO messageDTO;
}
