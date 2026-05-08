package com.wzkris.system.controller.announcement;

import com.wzkris.common.core.model.Result;
import com.wzkris.common.orm.model.Page;
import com.wzkris.common.orm.request.PagingRequest;
import com.wzkris.system.api.announcement.AnnouncementInfoApi;
import com.wzkris.system.response.announcement.AnnouncementInfoResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "公告信息")
@RestController
@RequestMapping("/announcement-info")
@RequiredArgsConstructor
public class AnnouncementInfoController {

    private final AnnouncementInfoApi announcementInfoApi;

    @Operation(summary = "公告分页")
    @GetMapping("/query-page")
    public Result<Page<AnnouncementInfoResponse>> queryPage(@ParameterObject PagingRequest request) {
        return announcementInfoApi.queryPage(request);
    }

}

