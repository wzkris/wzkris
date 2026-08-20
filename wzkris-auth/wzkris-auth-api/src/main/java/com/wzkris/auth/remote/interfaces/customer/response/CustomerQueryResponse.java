package com.wzkris.auth.remote.interfaces.customer.response;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 客户传输层
 *
 * @author wzkris
 */
@Data
@NoArgsConstructor
public class CustomerQueryResponse implements Serializable {

    private Long id;

    private String nickname;

    private String phoneNumber;

    private String socialUid;

    private String status;

}
