package com.wzkris.usercenter.api.announcement.request;

import com.wzkris.common.orm.request.PagingRequest;
import com.wzkris.usercenter.enums.announcement.AnnouncementStatusEnum;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "筛选条件")
public class AnnouncementMngPageRequest extends PagingRequest {

    @Parameter(description = "标题")
    private String title;

    @Parameter(description = "状态（0正常 1关闭）")
    private AnnouncementStatusEnum status;

}
