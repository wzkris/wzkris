package com.wzkris.system.api.announcement;

import com.wzkris.common.core.model.Result;
import com.wzkris.common.orm.model.Page;
import com.wzkris.common.orm.request.IdListRequest;
import com.wzkris.common.orm.request.IdRequest;
import com.wzkris.system.request.announcement.AnnouncementMngPageRequest;
import com.wzkris.system.request.announcement.AnnouncementMngSaveUpdateRequest;
import com.wzkris.system.response.announcement.AnnouncementMngResponse;

public interface AnnouncementMngApi {

    Result<Page<AnnouncementMngResponse>> queryPage(AnnouncementMngPageRequest request);

    Result<AnnouncementMngResponse> queryInfo(IdRequest request);

    Result<Void> save(AnnouncementMngSaveUpdateRequest request);

    Result<Void> update(AnnouncementMngSaveUpdateRequest request);

    Result<Void> remove(IdListRequest request);

}
