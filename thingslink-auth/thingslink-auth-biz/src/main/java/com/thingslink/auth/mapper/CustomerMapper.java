package com.thingslink.auth.mapper;

import com.thingslink.auth.domain.Customer;
import com.thingslink.common.orm.plus.BaseMapperPlus;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerMapper extends BaseMapperPlus<Customer> {

    @Select("SELECT * FROM customer WHERE phone_number = #{phoneNumber}")
    Customer selectByPhoneNumber(String phoneNumber);
}
