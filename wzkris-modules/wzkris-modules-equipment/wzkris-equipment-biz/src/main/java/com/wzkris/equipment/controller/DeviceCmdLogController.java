package com.wzkris.equipment.controller;

import com.wzkris.common.core.domain.Result;
import com.wzkris.common.orm.page.Page;
import com.wzkris.common.security.oauth2.annotation.CheckPerms;
import com.wzkris.common.web.model.BaseController;
import com.wzkris.equipment.domain.DeviceCmdLog;
import com.wzkris.equipment.domain.req.DeviceCmdLogQueryReq;
import com.wzkris.equipment.mapper.DeviceCmdLogMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * (DeviceCommandRecord)表控制层
 *
 * @author wzkris
 * @since 2024-12-19 15:30:40
 */
@Tag(name = "设备日志管理")
@RestController
@RequestMapping("/device/command_log")
@RequiredArgsConstructor
public class DeviceCmdLogController extends BaseController {
    private final DeviceCmdLogMapper deviceCmdLogMapper;

    @Operation(summary = "分页查询")
    @GetMapping("/list")
    @CheckPerms("device:list")
    public Result<Page<DeviceCmdLog>> listPage(DeviceCmdLogQueryReq queryReq) {
        startPage();
        List<DeviceCmdLog> list = deviceCmdLogMapper.selectList(queryReq);
        return getDataTable(list);
    }

}
