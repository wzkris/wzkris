package com.thingslink.equipment.mapper;

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
public interface DeviceMapper {

    /**
     * 通过ID查询单条数据
     *
     * @param deviceId 主键
     * @return 实例对象
     */
    Device getById(Long deviceId);

    /**
     * 查询指定行数据
     *
     * @param device 查询条件
     * @return 对象列表
     */
    List<Device> list(@Param("condition") Device device);


    List<DeviceVO> listVO(@Param("condition")Device device);

    /**
     * 新增数据
     *
     * @param device 实例对象
     * @return 影响行数
     */
    int insert(Device device);

    /**
     * 批量新增数据（MyBatis原生foreach方法）
     *
     * @param entities List<Device> 实例对象列表
     * @return 影响行数
     */
    int insertBatch(@Param("entities") List<Device> entities);

    /**
     * 修改数据
     *
     * @param device 实例对象
     * @return 影响行数
     */
    int updateById(Device device);

    // 修改数据
    int updateBySerialNo(Device device);

    /**
     * 批量修改数据
     *
     * @param entities 实例对象
     * @return 影响行数
     */
    int updateBatchById(@Param("entities") List<Device> entities);

    /**
     * 通过主键删除数据
     *
     * @param deviceId 主键
     * @return 影响行数
     */
    int deleteById(Long deviceId);

}
