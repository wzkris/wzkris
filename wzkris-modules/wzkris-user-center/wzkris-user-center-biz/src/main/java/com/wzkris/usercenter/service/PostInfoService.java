package com.wzkris.usercenter.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.wzkris.usercenter.domain.PostInfoDO;
import com.wzkris.usercenter.response.common.SelectResponse;
import jakarta.annotation.Nullable;

import java.util.List;

public interface PostInfoService extends IService<PostInfoDO> {

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
    List<SelectResponse> listSelect(@Nullable String postName);

    /**
     * ID获取职位组
     */
    String getPostGroup(Long memberId);

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
    boolean updatePost(PostInfoDO post, List<Long> menuIds);

    boolean removePosts(List<Long> postIds);

    boolean existMember(List<Long> postIds);

}

