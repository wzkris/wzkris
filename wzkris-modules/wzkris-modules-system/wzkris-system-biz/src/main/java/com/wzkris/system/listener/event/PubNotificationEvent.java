package com.wzkris.system.listener.event;

import com.wzkris.system.domain.dto.SimpleMessageDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.lang.Nullable;

import java.io.Serializable;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "发布通知事件")
public class PubNotificationEvent {

    @Nullable
    private List<? extends Serializable> ids;

    private SimpleMessageDTO message;

}
