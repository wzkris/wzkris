package com.thingslink.equipment.controller;

import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.thingslink.equipment.domain.Device;
import com.thingslink.equipment.domain.dto.EmqxAuthRequest;
import com.thingslink.equipment.domain.dto.EmqxAuthResult;
import com.thingslink.equipment.mapper.DeviceMapper;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "emqx验证接口")
@Slf4j
@RestController
@RequestMapping("/emqx")
@RequiredArgsConstructor
public class EmqxAuthorizeController {

    private final DeviceMapper deviceMapper;

    @PostMapping("/authorize")
    public ResponseEntity<EmqxAuthResult> device_authorize(@RequestBody EmqxAuthRequest authDTO) {
        EmqxAuthResult result = new EmqxAuthResult();
        // 根据clientid查询设备是否可入网
        Device device = new LambdaQueryChainWrapper<>(deviceMapper)
                .eq(Device::getSerialNo, authDTO.getClientid())
                .one();
        if (device != null) {
            result.setResult("allow");
        }
        return ResponseEntity.ok(result);
    }
}
