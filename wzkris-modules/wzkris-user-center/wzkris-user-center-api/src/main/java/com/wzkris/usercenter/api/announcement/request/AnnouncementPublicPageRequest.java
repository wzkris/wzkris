package com.wzkris.usercenter.api.announcement.request;

import com.wzkris.common.orm.request.PagingRequest;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "筛选条件")
public class AnnouncementPublicPageRequest extends PagingRequest {

}
