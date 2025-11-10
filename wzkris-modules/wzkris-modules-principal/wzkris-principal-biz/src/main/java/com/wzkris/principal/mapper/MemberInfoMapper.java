package com.wzkris.principal.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.wzkris.common.orm.plus.BaseMapperPlus;
import com.wzkris.principal.domain.MemberInfoDO;
import com.wzkris.principal.domain.vo.member.MemberMngVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface MemberInfoMapper extends BaseMapperPlus<MemberInfoDO> {

    @Select("""
            SELECT s.*, STRING_AGG(p.post_name, ',') AS post_name
                    		FROM biz.t_member_info s LEFT JOIN biz.t_member_to_post sp ON s.member_id = sp.member_id
                    		 LEFT JOIN biz.t_post_info p ON sp.post_id = p.post_id AND p.status = '0'
                    ${ew.customSqlSegment} GROUP BY s.member_id ORDER BY s.member_id DESC
            """)
    List<MemberMngVO> listVO(@Param(Constants.WRAPPER) QueryWrapper<MemberInfoDO> queryWrapper);

    /**
     * 通过用户名查询租户成员
     *
     * @param username 租户成员账号
     * @return 租户成员信息
     */
    @Select("SELECT * FROM biz.t_member_info WHERE username = #{username}")
    MemberInfoDO selectByUsername(String username);

    /**
     * 通过手机号查询用户
     *
     * @param phoneNumber 手机号
     * @return 用户对象信息
     */
    @Select("SELECT * FROM biz.t_member_info WHERE phone_number = #{phoneNumber}")
    MemberInfoDO selectByPhoneNumber(String phoneNumber);

    @Select("SELECT password FROM biz.t_member_info WHERE member_id = #{memberId}")
    String selectPwdById(Long memberId);

    @Select("SELECT phone_number FROM biz.t_member_info WHERE member_id = #{memberId}")
    String selectPhoneNumberById(Long memberId);

}
