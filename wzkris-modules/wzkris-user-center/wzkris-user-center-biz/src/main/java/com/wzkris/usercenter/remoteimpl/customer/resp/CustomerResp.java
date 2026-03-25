package com.wzkris.usercenter.remoteimpl.customer.resp;

import com.wzkris.usercenter.domain.CustomerInfoDO;
import io.github.linpeilie.annotations.AutoMapper;
import io.github.linpeilie.annotations.AutoMappers;
import lombok.Data;
import lombok.experimental.FieldNameConstants;

import java.io.Serializable;

/**
 * 客户传输层
 *
 * @author wzkris
 */
@Data
@AutoMappers({@AutoMapper(target = CustomerInfoDO.class)})
public class CustomerResp implements Serializable {

    private Long customerId;

    private String nickname;

    private String phoneNumber;

    private String status;

}
