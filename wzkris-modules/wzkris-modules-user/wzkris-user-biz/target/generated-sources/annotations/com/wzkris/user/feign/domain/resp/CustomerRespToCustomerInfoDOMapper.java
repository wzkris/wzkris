package com.wzkris.user.feign.domain.resp;

import com.wzkris.user.domain.CustomerInfoDO;
import com.wzkris.user.domain.CustomerInfoDOToCustomerInfoExportMapper;
import com.wzkris.user.domain.CustomerInfoDOToCustomerRespMapper;
import com.wzkris.user.domain.export.CustomerInfoExportToCustomerInfoDOMapper;
import com.wzkris.user.feign.customer.resp.CustomerResp;
import io.github.linpeilie.AutoMapperConfig__169;
import io.github.linpeilie.BaseMapper;
import org.mapstruct.Mapper;

@Mapper(
    config = AutoMapperConfig__169.class,
    uses = {CustomerInfoExportToCustomerInfoDOMapper.class,CustomerInfoDOToCustomerInfoExportMapper.class,CustomerInfoDOToCustomerRespMapper.class},
    imports = {}
)
public interface CustomerRespToCustomerInfoDOMapper extends BaseMapper<CustomerResp, CustomerInfoDO> {
}
