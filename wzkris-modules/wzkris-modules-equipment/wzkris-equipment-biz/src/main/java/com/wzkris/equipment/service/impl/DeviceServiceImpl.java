package com.wzkris.equipment.service.impl;

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
    public DeviceVO getVOBySno(String sno) {
        DeviceVO deviceVO = deviceMapper.selectVOBySno(sno);
        NetworkVO networkVO = this.getNetInfoBySno(sno);
        if (deviceVO == null) return null;
        deviceVO.setNet(networkVO);
        return deviceVO;
    }

    @Override
    public NetworkVO getNetInfoBySno(String sno) {
        // 查询入网后的信息
        return new NetworkVO("192.168.0.1", 30069, 18, 35);
    }

}
