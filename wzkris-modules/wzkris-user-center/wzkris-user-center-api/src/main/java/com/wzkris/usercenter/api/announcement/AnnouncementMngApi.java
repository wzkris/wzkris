package com.wzkris.usercenter.api.announcement;

import com.wzkris.common.core.model.Result;
import com.wzkris.common.orm.model.Page;
import com.wzkris.common.orm.request.IdListRequest;
import com.wzkris.common.orm.request.IdRequest;
import com.wzkris.usercenter.api.announcement.request.AnnouncementMngPageRequest;
import com.wzkris.usercenter.api.announcement.request.AnnouncementMngSaveRequest;
import com.wzkris.usercenter.api.announcement.request.AnnouncementMngUpdateRequest;
import com.wzkris.usercenter.api.announcement.response.AnnouncementMngQueryResponse;
import com.wzkris.usercenter.api.announcement.response.AnnouncementMngPageResponse;

public interface AnnouncementMngApi {

    Result<Page<AnnouncementMngPageResponse>> queryPage(AnnouncementMngPageRequest request);

    Result<AnnouncementMngQueryResponse> queryInfo(IdRequest request);

    Result<Void> save(AnnouncementMngSaveRequest request);

    Result<Void> update(AnnouncementMngUpdateRequest request);

    Result<Void> remove(IdListRequest request);

}
