package com.wzkris.usercenter.mapper;

import com.wzkris.common.orm.plus.BaseMapperPlus;
import com.wzkris.usercenter.domain.CustomerInfoDO;
import com.wzkris.usercenter.domain.req.customer.CustomerIncryQueryReq;
import com.wzkris.usercenter.domain.resp.customer.CustomerIncryResp;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface CustomerInfoMapper extends BaseMapperPlus<CustomerInfoDO> {

    @Select("SELECT * FROM biz.customer_info WHERE phone_number = #{phoneNumber}")
    CustomerInfoDO selectByPhoneNumber(String phoneNumber);

    List<CustomerIncryResp> listIncryVO(CustomerIncryQueryReq queryReq);

}
