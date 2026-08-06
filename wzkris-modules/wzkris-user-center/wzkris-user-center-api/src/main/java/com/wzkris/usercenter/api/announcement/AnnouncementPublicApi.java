package com.wzkris.usercenter.api.announcement;

import com.wzkris.common.core.model.Result;
import com.wzkris.common.orm.model.Page;
import com.wzkris.usercenter.api.announcement.request.AnnouncementPublicPageRequest;
import com.wzkris.usercenter.api.announcement.response.AnnouncementPublicResponse;

public interface AnnouncementPublicApi {

    Result<Page<AnnouncementPublicResponse>> queryPage(AnnouncementPublicPageRequest request);

}
