package com.wzkris.principal.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.wzkris.common.orm.utils.SkipTenantInterceptorUtil;
import com.wzkris.common.security.oauth2.service.PasswordEncoderDelegate;
import com.wzkris.principal.domain.StaffInfoDO;
import com.wzkris.principal.domain.StaffToPostDO;
import com.wzkris.principal.mapper.StaffInfoMapper;
import com.wzkris.principal.mapper.StaffToPostMapper;
import com.wzkris.principal.service.StaffInfoService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.jetbrains.annotations.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class StaffInfoServiceImpl implements StaffInfoService {

    private final StaffInfoMapper staffInfoMapper;

    private final StaffToPostMapper staffToPostMapper;

    private final PasswordEncoderDelegate passwordEncoder;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean saveStaff(StaffInfoDO staff, @Nullable List<Long> postIds) {
        if (staff.getPassword() != null && !passwordEncoder.isEncode(staff.getPassword())) {
            staff.setPassword(passwordEncoder.encode(staff.getPassword()));
        }
        boolean success = staffInfoMapper.insert(staff) > 0;
        if (success) {
            this.insertStaffPost(staff.getStaffId(), postIds);
        }
        return success;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean modifyStaff(StaffInfoDO staff, List<Long> postIds) {
        if (staff.getPassword() != null && !passwordEncoder.isEncode(staff.getPassword())) {
            staff.setPassword(passwordEncoder.encode(staff.getPassword()));
        }
        boolean success = staffInfoMapper.updateById(staff) > 0;
        if (success && postIds != null) {
            staffToPostMapper.deleteByStaffId(staff.getStaffId());
            this.insertStaffPost(staff.getStaffId(), postIds);
        }
        return success;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean grantPosts(Long staffId, List<Long> postIds) {
        staffToPostMapper.deleteByStaffId(staffId);
        return this.insertStaffPost(staffId, postIds);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean removeByIds(List<Long> staffIds) {
        boolean success = staffInfoMapper.deleteByIds(staffIds) > 0;
        if (success) {
            staffToPostMapper.deleteByStaffIds(staffIds);
        }
        return success;
    }

    @Override
    public boolean existByStaffName(Long staffId, String staffName) {
        return SkipTenantInterceptorUtil.ignore(() -> {
            LambdaQueryWrapper<StaffInfoDO> lqw = new LambdaQueryWrapper<>(StaffInfoDO.class)
                    .eq(StaffInfoDO::getStaffName, staffName)
                    .ne(Objects.nonNull(staffId), StaffInfoDO::getStaffId, staffId);
            return staffInfoMapper.exists(lqw);
        });
    }

    @Override
    public boolean existByPhoneNumber(Long staffId, String phoneNumber) {
        return SkipTenantInterceptorUtil.ignore(() -> {
            LambdaQueryWrapper<StaffInfoDO> lqw = new LambdaQueryWrapper<>(StaffInfoDO.class)
                    .eq(StaffInfoDO::getPhoneNumber, phoneNumber)
                    .ne(Objects.nonNull(staffId), StaffInfoDO::getStaffId, staffId);
            return staffInfoMapper.exists(lqw);
        });
    }

    private boolean insertStaffPost(Long staffId, List<Long> postIds) {
        if (CollectionUtils.isNotEmpty(postIds)) {
            List<StaffToPostDO> list = postIds.stream()
                    .map(postId -> new StaffToPostDO(staffId, postId))
                    .toList();
            return staffToPostMapper.insert(list) > 0;
        }
        return false;
    }

}
