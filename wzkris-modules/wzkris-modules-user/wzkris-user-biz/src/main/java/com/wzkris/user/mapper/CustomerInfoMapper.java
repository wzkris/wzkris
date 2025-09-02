package com.wzkris.user.mapper;

import com.wzkris.common.orm.plus.BaseMapperPlus;
import com.wzkris.user.domain.CustomerInfoDO;
import com.wzkris.user.domain.req.CustomerIncryQueryReq;
import com.wzkris.user.domain.vo.CustomerIncryVO;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CustomerInfoMapper extends BaseMapperPlus<CustomerInfoDO> {

    @Select("SELECT * FROM biz.customer_info WHERE phone_number = #{phoneNumber}")
    CustomerInfoDO selectByPhoneNumber(String phoneNumber);

    List<CustomerIncryVO> listIncryVO(CustomerIncryQueryReq queryReq);

}
