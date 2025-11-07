package com.wzkris.principal.service;

import com.wzkris.principal.domain.StaffInfoDO;
import jakarta.annotation.Nullable;

import java.util.List;

public interface StaffInfoService {

    /**
     * 新增员工信息
     *
     * @param staff   员工信息
     * @param postIds 关联职位
     */
    boolean saveStaff(StaffInfoDO staff, @Nullable List<Long> postIds);

    /**
     * 修改员工信息
     *
     * @param staff   员工信息
     * @param postIds 关联职位
     */
    boolean modifyStaff(StaffInfoDO staff, @Nullable List<Long> postIds);

    /**
     * 员工分配职位
     *
     * @param staffId 员工ID
     * @param postIds 关联职位
     */
    boolean grantPosts(Long staffId, List<Long> postIds);

    boolean removeByIds(List<Long> staffIds);

    boolean existByStaffName(Long staffId, String username);

    boolean existByPhoneNumber(Long staffId, String phoneNumber);

}
