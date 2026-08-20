package com.wzkris.usercenter.controller.announcement;

import com.wzkris.common.core.model.Result;
import com.wzkris.common.orm.model.Page;
import com.wzkris.usercenter.api.announcement.AnnouncementPublicApi;
import com.wzkris.usercenter.api.announcement.request.AnnouncementPublicPageRequest;
import com.wzkris.usercenter.api.announcement.response.AnnouncementPublicPageResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "公告信息")
@RestController
@RequestMapping("/announcement-public")
@RequiredArgsConstructor
public class AnnouncementPublicController {

    private final AnnouncementPublicApi announcementPublicApi;

    @Operation(summary = "公告分页")
    @GetMapping("/query-page")
    public Result<Page<AnnouncementPublicPageResponse>> queryPage(@ParameterObject AnnouncementPublicPageRequest request) {
        return announcementPublicApi.queryPage(request);
    }

}
