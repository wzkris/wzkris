package com.wzkris.usercenter.service;

import com.wzkris.usercenter.domain.PostInfoDO;
import com.wzkris.usercenter.domain.vo.SelectVO;
import jakarta.annotation.Nullable;

import java.util.List;

public interface PostInfoService {

    /**
     * 根据租户成员ID查询关联职位(正常状态)
     *
     * @param memberId 成员ID
     * @return 职位列表
     */
    List<PostInfoDO> listByMemberId(Long memberId);

    /**
     * 根据租户成员ID查询关联职位ID(正常状态)
     *
     * @param memberId 成员ID
     * @return 职位列表
     */
    List<Long> listIdByMemberId(Long memberId);

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

    void existMember(List<Long> postIds);

}
