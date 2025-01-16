package com.wzkris.system.controller;

import com.wzkris.common.core.domain.Result;
import com.wzkris.common.web.model.BaseController;
import com.wzkris.system.domain.dto.SimpleMessageDTO;
import com.wzkris.system.service.SysNotifyService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 系统消息 操作处理
 *
 * @author wzkris
 */
@Validated
@RestController
@RequestMapping("/sys_notify")
@RequiredArgsConstructor
public class SysNotifySendController extends BaseController {

    private final SysNotifyService notifyService;

    @PostMapping("/send")
    public Result<Void> add(@Valid @RequestBody List<Long> ids) {
        SimpleMessageDTO messageDTO = new SimpleMessageDTO();
        messageDTO.setTitle("重要通知");
        messageDTO.setType("0");
        messageDTO.setContent("zzzzzzzzzzzzzzzzzzzzzzzzzzzzzzz");
        notifyService.sendNotify(ids, messageDTO);
        return ok();
    }

}
