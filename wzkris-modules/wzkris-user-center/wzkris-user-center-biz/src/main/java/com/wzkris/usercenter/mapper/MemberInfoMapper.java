package com.wzkris.usercenter.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.wzkris.common.orm.plus.BaseMapperPlus;
import com.wzkris.usercenter.api.member.response.MemberMngResponse;
import com.wzkris.usercenter.domain.MemberInfoDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface MemberInfoMapper extends BaseMapperPlus<MemberInfoDO> {

    @Select("""
            SELECT s.*, STRING_AGG(p.post_name, ',') AS post_name
            FROM biz.member_info s LEFT JOIN biz.member_to_post sp ON s.member_id = sp.member_id
             LEFT JOIN biz.post_info p ON sp.post_id = p.post_id AND p.status = '0'
            ${ew.customSqlSegment} GROUP BY s.member_id ORDER BY s.member_id DESC
            """)
    IPage<MemberMngResponse> selectVOPage(IPage<MemberMngResponse> page, @Param(Constants.WRAPPER) QueryWrapper<MemberInfoDO> queryWrapper);

}

