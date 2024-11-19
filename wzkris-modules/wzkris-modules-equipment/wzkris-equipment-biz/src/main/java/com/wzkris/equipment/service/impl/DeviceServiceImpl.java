package com.wzkris.equipment.service.impl;

import cn.hutool.core.util.IdUtil;
import com.wzkris.common.redis.util.RedisUtil;
import com.wzkris.equipment.constants.DeviceConstant;
import com.wzkris.equipment.domain.vo.DeviceVO;
import com.wzkris.equipment.domain.vo.NetworkVO;
import com.wzkris.equipment.mapper.DeviceMapper;
import com.wzkris.equipment.service.DeviceService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * (Device)表服务实现类
 *
 * @author wzkris
 * @since 2023-06-05 10:38:53
 */
@Service
@RequiredArgsConstructor
public class DeviceServiceImpl implements DeviceService {

    private final DeviceMapper deviceMapper;

    @Override
    public DeviceVO getVOById(Long deviceId) {
        return deviceMapper.selectById2VO(deviceId, DeviceVO.class);
    }

    @Override
    public NetworkVO getNetworkVOBySerialNo(String serialNo) {
        // 查询入网后的信息
        return null;
    }

    @Override
    public String subDevice(String serialNo) {
        String roomNo = IdUtil.fastSimpleUUID();
        RedisUtil.setObj(DeviceConstant.ROOM_PREFIX + roomNo, serialNo, 60 * 60);// 默认一个小时
        return roomNo;
    }

}
