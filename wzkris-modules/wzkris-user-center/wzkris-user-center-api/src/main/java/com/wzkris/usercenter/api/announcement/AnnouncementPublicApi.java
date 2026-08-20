package com.wzkris.usercenter.api.announcement;

import com.wzkris.common.core.model.Result;
import com.wzkris.common.orm.model.Page;
import com.wzkris.usercenter.api.announcement.request.AnnouncementPublicPageRequest;
import com.wzkris.usercenter.api.announcement.response.AnnouncementPublicPageResponse;

public interface AnnouncementPublicApi {

    Result<Page<AnnouncementPublicPageResponse>> queryPage(AnnouncementPublicPageRequest request);

}
