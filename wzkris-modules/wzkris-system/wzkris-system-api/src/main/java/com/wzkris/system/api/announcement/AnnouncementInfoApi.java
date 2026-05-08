package com.wzkris.system.api.announcement;

import com.wzkris.common.core.model.Result;
import com.wzkris.common.orm.model.Page;
import com.wzkris.system.request.announcement.AnnouncementInfoPageRequest;
import com.wzkris.system.response.announcement.AnnouncementInfoResponse;

public interface AnnouncementInfoApi {

    Result<Page<AnnouncementInfoResponse>> queryPage(AnnouncementInfoPageRequest request);

}
