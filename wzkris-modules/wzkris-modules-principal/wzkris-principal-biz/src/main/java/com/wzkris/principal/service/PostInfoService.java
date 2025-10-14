package com.wzkris.principal.service;

import com.wzkris.principal.domain.PostInfoDO;
import com.wzkris.principal.domain.vo.SelectVO;
import jakarta.annotation.Nullable;

import java.util.List;

public interface PostInfoService {

    /**
     * 根据员工ID查询关联职位(正常状态)
     *
     * @param staffId 员工ID
     * @return 职位列表
     */
    List<PostInfoDO> listByStaffId(Long staffId);

    /**
     * 根据员工ID查询关联职位ID(正常状态)
     *
     * @param staffId 员工ID
     * @return 职位列表
     */
    List<Long> listIdByStaffId(Long staffId);

    /**
     * 获取职位选择列表(正常状态)
     *
     * @param postName 职位名称
     * @return 职位选择列表
     */
    List<SelectVO> listSelect(@Nullable String postName);

    /**
     * 获取当前职位组
     */
    String getPostGroup();

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
