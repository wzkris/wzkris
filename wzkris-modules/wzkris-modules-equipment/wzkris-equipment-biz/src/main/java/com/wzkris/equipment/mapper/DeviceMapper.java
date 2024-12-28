package com.wzkris.equipment.mapper;

import com.wzkris.common.orm.plus.BaseMapperPlus;
import com.wzkris.equipment.domain.Device;
import com.wzkris.equipment.domain.vo.DeviceVO;
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
     * @param deviceId 设备ID
     */
    DeviceVO selectVOById(Long deviceId);
}
