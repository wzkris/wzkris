package com.wzkris.system.api.announcement;

import com.wzkris.common.core.model.Result;
import com.wzkris.common.orm.model.Page;
import com.wzkris.system.request.announcement.AnnouncementMngQueryRequest;
import com.wzkris.system.request.announcement.AnnouncementMngSaveUpdateRequest;
import com.wzkris.system.response.announcement.AnnouncementMngResponse;

import java.util.List;

public interface AnnouncementMngApi {

    Result<Page<AnnouncementMngResponse>> queryPage(AnnouncementMngQueryRequest request);

    Result<AnnouncementMngResponse> queryInfo(Long announcementId);

    Result<Void> save(AnnouncementMngSaveUpdateRequest request);

    Result<Void> update(AnnouncementMngSaveUpdateRequest request);

    Result<Void> remove(List<Long> msgIds);

}
