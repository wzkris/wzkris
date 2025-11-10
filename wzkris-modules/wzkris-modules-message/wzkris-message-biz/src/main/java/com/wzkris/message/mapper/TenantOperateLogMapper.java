package com.wzkris.message.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.wzkris.common.orm.plus.BaseMapperPlus;
import com.wzkris.message.domain.TenantOperateLogDO;
import com.wzkris.message.domain.vo.tenantlog.TenantOperateLogInfoVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface TenantOperateLogMapper extends BaseMapperPlus<TenantOperateLogDO> {

    @Select("""
                SELECT oper_id, title, sub_title, oper_type, username, oper_ip, oper_location, success, error_msg, oper_time
                FROM biz.tenant_operate_log
                        ${ew.customSqlSegment}
            """)
    List<TenantOperateLogInfoVO> selectListInfoVO(@Param(Constants.WRAPPER) Wrapper<TenantOperateLogDO> wrapper);

}
