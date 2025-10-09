package com.wzkris.user.service;

import com.wzkris.user.domain.PostInfoDO;
import com.wzkris.user.domain.vo.SelectVO;

import java.util.List;

public interface PostInfoService {

    /**
     * 根据员工ID查询关联职位(正常状态)
     *
     * @param staffId 员工ID
     * @return 角色列表
     */
    List<PostInfoDO> listByStaffId(Long staffId);

    List<Long> listIdByStaffId(Long staffId);

    List<SelectVO> listSelect(String postName);

    boolean removeByIds(List<Long> postIds);

}
