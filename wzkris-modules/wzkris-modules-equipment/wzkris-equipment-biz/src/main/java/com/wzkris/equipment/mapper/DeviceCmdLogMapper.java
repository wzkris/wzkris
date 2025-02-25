package com.wzkris.equipment.mapper;

import com.wzkris.equipment.domain.DeviceCmdLog;
import com.wzkris.equipment.domain.req.DeviceCmdLogQueryReq;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * (DeviceCommandRecord)表数据库访问层
 *
 * @author wzkris
 * @since 2024-12-19 15:30:40
 */
@Repository
public interface DeviceCmdLogMapper {

    @Select("""
            <script>
                    SELECT * FROM biz_sys.device_cmd_log
                        <where>
                            <if test="null != deviceId and '' != deviceId">
                                AND device_id = #{deviceId}
                            </if>
                            <if test="null != cmd and '' != cmd">
                                AND cmd = #{cmd}
                            </if>
                            <if test="null != cmdType and '' != cmdType">
                                AND cmd_type = #{cmdType}
                            </if>
                            <if test="null != beginTime and '' != endTime">
                                AND receive_time BETWEEN #{beginTime} AND #{endTime}
                            </if>
                        </where>
                    ORDER BY receive_time DESC
            </script>
            """)
    List<DeviceCmdLog> selectList(DeviceCmdLogQueryReq queryReq);

    /**
     * 新增
     */
    @Insert("""
            <script>
                INSERT INTO biz_sys.device_cmd_log(device_id, receive_time, cmd, tenant_id, cmd_type, cmd_data) VALUES
                    <foreach collection="list" item="item" index="index" separator=",">
                        (#{item.deviceId}, #{item.receiveTime}, #{item.cmd}, #{item.tenantId}, #{item.cmdType}, #{item.cmdData})
                    </foreach>
            </script>
            """)
    int insert(List<DeviceCmdLog> list);
}
