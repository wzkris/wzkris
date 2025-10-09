package com.wzkris.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.wzkris.common.core.constant.CommonConstants;
import com.wzkris.common.core.utils.StringUtil;
import com.wzkris.user.domain.PostInfoDO;
import com.wzkris.user.domain.vo.SelectVO;
import com.wzkris.user.mapper.PostInfoMapper;
import com.wzkris.user.mapper.PostToMenuMapper;
import com.wzkris.user.mapper.StaffToPostMapper;
import com.wzkris.user.service.PostInfoService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PostInfoServiceImpl implements PostInfoService {

    private final StaffToPostMapper staffToPostMapper;

    private final PostToMenuMapper postToMenuMapper;

    private final PostInfoMapper postInfoMapper;

    @Override
    public List<PostInfoDO> listByStaffId(Long staffId) {
        List<Long> postIds = staffToPostMapper.listPostIdByStaffId(staffId);
        if (CollectionUtils.isEmpty(postIds)) {
            return Collections.emptyList();
        }
        // 只能查出状态正常的职位
        LambdaQueryWrapper<PostInfoDO> lqw = new LambdaQueryWrapper<PostInfoDO>()
                .in(PostInfoDO::getPostId, postIds)
                .eq(PostInfoDO::getStatus, CommonConstants.STATUS_ENABLE);
        return postInfoMapper.selectList(lqw);
    }

    @Override
    public List<Long> listIdByStaffId(Long staffId) {
        List<Long> postIds = staffToPostMapper.listPostIdByStaffId(staffId);
        if (CollectionUtils.isEmpty(postIds)) {
            return Collections.emptyList();
        }
        // 只能查出状态正常的角色
        LambdaQueryWrapper<PostInfoDO> lqw = new LambdaQueryWrapper<PostInfoDO>()
                .select(PostInfoDO::getPostId)
                .in(PostInfoDO::getPostId, postIds)
                .eq(PostInfoDO::getStatus, CommonConstants.STATUS_ENABLE);
        return postInfoMapper.selectList(lqw).stream().map(PostInfoDO::getPostId).toList();
    }

    @Override
    public List<SelectVO> listSelect(String postName) {
        return postInfoMapper.selectList(Wrappers.lambdaQuery(PostInfoDO.class)
                        .select(PostInfoDO::getPostId, PostInfoDO::getPostName)
                        .eq(PostInfoDO::getStatus, CommonConstants.STATUS_ENABLE)
                        .like(StringUtil.isNotBlank(postName), PostInfoDO::getPostName, postName)
                        .orderByAsc(PostInfoDO::getPostId))
                .stream()
                .map(SelectVO::new)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean removeByIds(List<Long> postIds) {
        boolean success = postInfoMapper.deleteByIds(postIds) > 0;
        if (success) {
            // 删除职位与菜单关联
            postToMenuMapper.deleteByPostIds(postIds);
            // 删除员工与职位关联
            staffToPostMapper.deleteByPostIds(postIds);
        }
        return success;
    }

}
