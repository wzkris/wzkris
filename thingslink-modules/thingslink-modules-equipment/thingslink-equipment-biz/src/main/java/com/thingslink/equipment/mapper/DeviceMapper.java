package com.thingslink.equipment.mapper;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.thingslink.common.orm.plus.BaseMapperPlus;
import com.thingslink.equipment.domain.Device;
import com.thingslink.equipment.domain.vo.DeviceVO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * (Device)表数据库访问层
 *
 * @author wzkris
 * @since 2023-08-21 09:34:39
 */
@Repository
public interface DeviceMapper extends BaseMapperPlus<Device> {

    List<DeviceVO> listVO(@Param("condition") Device device);

    // 修改数据
    default int updateBySerialNo(Device device) {
        LambdaUpdateWrapper<Device> luw = new LambdaUpdateWrapper<Device>()
                .eq(Device::getSerialNo, device.getSerialNo());
        device.setSerialNo(null);
        return this.update(device, luw);
    }
}
