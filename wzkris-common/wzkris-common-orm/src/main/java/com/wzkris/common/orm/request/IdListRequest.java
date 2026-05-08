package com.wzkris.common.orm.request;

import lombok.Data;

import java.util.List;

@Data
public class IdListRequest {

    private List<Long> ids;

}
