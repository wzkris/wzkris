package com.wzkris.equipment.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.wzkris.equipment.domain.Device;
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
        DeviceVO deviceVO = deviceMapper.selectOne2VO(Wrappers.lambdaQuery(Device.class)
                        .eq(Device::getSerialNo, sno)
                , DeviceVO.class);
        NetworkVO networkVO = this.getNetInfoBySno(sno);
        deviceVO.setNet(networkVO);
        return deviceVO;
    }

    @Override
    public NetworkVO getNetInfoBySno(String sno) {
        // 查询入网后的信息
        return null;
    }

}
