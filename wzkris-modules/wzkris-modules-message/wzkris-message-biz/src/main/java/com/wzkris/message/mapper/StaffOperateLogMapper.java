package com.wzkris.message.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.wzkris.common.orm.plus.BaseMapperPlus;
import com.wzkris.message.domain.StaffOperateLogDO;
import com.wzkris.message.domain.vo.stafflog.StaffOperateLogInfoVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface StaffOperateLogMapper extends BaseMapperPlus<StaffOperateLogDO> {

    @Select("""
                SELECT oper_id, title, sub_title, oper_type, username, oper_ip, oper_location, success, error_msg, oper_time
                FROM biz.staff_operate_log
                        ${ew.customSqlSegment}
            """)
    List<StaffOperateLogInfoVO> selectListInfoVO(@Param(Constants.WRAPPER) Wrapper<StaffOperateLogDO> wrapper);

}
