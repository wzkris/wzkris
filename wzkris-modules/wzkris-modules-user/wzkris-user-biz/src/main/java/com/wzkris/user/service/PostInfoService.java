package com.wzkris.user.service;

import com.wzkris.user.domain.PostInfoDO;
import com.wzkris.user.domain.vo.SelectVO;

import java.util.List;

public interface PostInfoService {

    /**
     * 根据员工ID查询关联职位(正常状态)
     *
     * @param staffId 员工ID
     * @return 职位列表
     */
    List<PostInfoDO> listByStaffId(Long staffId);

    List<Long> listIdByStaffId(Long staffId);

    List<SelectVO> listSelect(String postName);

    /**
     * 新增职位信息
     *
     * @param post    职位信息
     * @param menuIds 菜单组
     */
    boolean savePost(PostInfoDO post, List<Long> menuIds);

    /**
     * 修改职位信息
     *
     * @param post    职位信息
     * @param menuIds 菜单组
     */
    boolean modifyPost(PostInfoDO post, List<Long> menuIds);

    boolean removeByIds(List<Long> postIds);

    void existStaff(List<Long> postIds);

}
