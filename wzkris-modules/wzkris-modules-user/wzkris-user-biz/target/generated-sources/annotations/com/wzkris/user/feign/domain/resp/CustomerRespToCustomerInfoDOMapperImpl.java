package com.wzkris.user.feign.domain.resp;

import com.wzkris.user.domain.CustomerInfoDO;
import javax.annotation.processing.Generated;

import com.wzkris.user.feign.customer.resp.CustomerResp;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-09-05T17:10:48+0800",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 17.0.16 (Oracle Corporation)"
)
@Component
public class CustomerRespToCustomerInfoDOMapperImpl implements CustomerRespToCustomerInfoDOMapper {

    @Override
    public CustomerInfoDO convert(CustomerResp arg0) {
        if ( arg0 == null ) {
            return null;
        }

        CustomerInfoDO customerInfoDO = new CustomerInfoDO();

        customerInfoDO.setCustomerId( arg0.getCustomerId() );
        customerInfoDO.setNickname( arg0.getNickname() );
        customerInfoDO.setPhoneNumber( arg0.getPhoneNumber() );
        customerInfoDO.setStatus( arg0.getStatus() );

        return customerInfoDO;
    }

    @Override
    public CustomerInfoDO convert(CustomerResp arg0, CustomerInfoDO arg1) {
        if ( arg0 == null ) {
            return arg1;
        }

        arg1.setCustomerId( arg0.getCustomerId() );
        arg1.setNickname( arg0.getNickname() );
        arg1.setPhoneNumber( arg0.getPhoneNumber() );
        arg1.setStatus( arg0.getStatus() );

        return arg1;
    }
}
