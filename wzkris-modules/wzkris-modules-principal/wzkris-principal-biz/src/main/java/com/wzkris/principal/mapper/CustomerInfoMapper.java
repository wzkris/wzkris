package com.wzkris.principal.mapper;

import com.wzkris.common.orm.plus.BaseMapperPlus;
import com.wzkris.principal.domain.CustomerInfoDO;
import com.wzkris.principal.domain.req.customer.CustomerIncryQueryReq;
import com.wzkris.principal.domain.vo.customer.CustomerIncryVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface CustomerInfoMapper extends BaseMapperPlus<CustomerInfoDO> {

    @Select("SELECT * FROM biz.customer_info WHERE phone_number = #{phoneNumber}")
    CustomerInfoDO selectByPhoneNumber(String phoneNumber);

    List<CustomerIncryVO> listIncryVO(CustomerIncryQueryReq queryReq);

}
