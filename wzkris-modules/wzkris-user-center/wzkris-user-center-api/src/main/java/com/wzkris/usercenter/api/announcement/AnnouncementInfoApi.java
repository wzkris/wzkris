package com.wzkris.usercenter.api.announcement;

import com.wzkris.common.core.model.Result;
import com.wzkris.common.orm.model.Page;
import com.wzkris.usercenter.api.announcement.request.AnnouncementInfoPageRequest;
import com.wzkris.usercenter.api.announcement.response.AnnouncementInfoResponse;

public interface AnnouncementInfoApi {

    Result<Page<AnnouncementInfoResponse>> queryPage(AnnouncementInfoPageRequest request);

}
