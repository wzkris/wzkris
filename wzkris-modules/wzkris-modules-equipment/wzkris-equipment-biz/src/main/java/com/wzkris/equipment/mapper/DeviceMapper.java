package com.wzkris.equipment.mapper;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.wzkris.common.orm.plus.BaseMapperPlus;
import com.wzkris.equipment.domain.Device;
import com.wzkris.equipment.domain.vo.DeviceVO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * (Device)表数据库访问层
 *
 * @author wzkris
 * @since 2023-08-21 09:34:39
 */
@Repository
public interface DeviceMapper extends BaseMapperPlus<Device> {

    /**
     * 根据设备号修改数据
     *
     * @param device 更新的数据
     */
    default int updateBySerialNo(Device device) {
        LambdaUpdateWrapper<Device> luw = new LambdaUpdateWrapper<Device>()
                .eq(Device::getSerialNo, device.getSerialNo());
        device.setDeviceId(null);
        device.setSerialNo(null);
        return this.update(device, luw);
    }

    /**
     * @param sno 设备号
     */
    DeviceVO selectVOBySno(@Param("sno") String sno);
}
