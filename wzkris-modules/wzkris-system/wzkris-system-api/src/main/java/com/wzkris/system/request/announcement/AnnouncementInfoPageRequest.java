package com.wzkris.system.request.announcement;

import com.wzkris.common.orm.request.PagingRequest;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "筛选条件")
public class AnnouncementInfoPageRequest extends PagingRequest {

}
