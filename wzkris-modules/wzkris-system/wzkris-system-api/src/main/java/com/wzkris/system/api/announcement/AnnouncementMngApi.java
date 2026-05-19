package com.wzkris.system.api.announcement;

import com.wzkris.common.core.model.Result;
import com.wzkris.common.orm.model.Page;
import com.wzkris.common.orm.request.IdListRequest;
import com.wzkris.common.orm.request.IdRequest;
import com.wzkris.system.api.announcement.request.AnnouncementMngPageRequest;
import com.wzkris.system.api.announcement.request.AnnouncementMngSaveRequest;
import com.wzkris.system.api.announcement.request.AnnouncementMngUpdateRequest;
import com.wzkris.system.api.announcement.response.AnnouncementMngResponse;

public interface AnnouncementMngApi {

    Result<Page<AnnouncementMngResponse>> queryPage(AnnouncementMngPageRequest request);

    Result<AnnouncementMngResponse> queryInfo(IdRequest request);

    Result<Void> save(AnnouncementMngSaveRequest request);

    Result<Void> update(AnnouncementMngUpdateRequest request);

    Result<Void> remove(IdListRequest request);

}
