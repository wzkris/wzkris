package com.wzkris.user.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.wzkris.common.orm.plus.BaseMapperPlus;
import com.wzkris.user.domain.StaffInfoDO;
import com.wzkris.user.domain.vo.staffinfo.StaffManageVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface StaffInfoMapper extends BaseMapperPlus<StaffInfoDO> {

    @Select("""
            SELECT s.*, STRING_AGG(p.post_name, ',')
                    		FROM biz.t_staff_info s LEFT JOIN biz.t_staff_to_post sp ON s.staff_id = sp.staff_id
                    		 LEFT JOIN biz.t_post_info p ON sp.post_id = p.post_id AND p.status = '0'
                    ${ew.customSqlSegment} GROUP BY s.staff_id
            """)
    List<StaffManageVO> listVO(QueryWrapper<StaffInfoDO> queryWrapper);

    /**
     * 通过用户名查询员工
     *
     * @param staffName 员工账号
     * @return 员工信息
     */
    @Select("SELECT * FROM biz.t_staff_info WHERE staff_name = #{staffName}")
    StaffInfoDO selectByStaffName(String staffName);

    /**
     * 通过手机号查询用户
     *
     * @param phoneNumber 手机号
     * @return 用户对象信息
     */
    @Select("SELECT * FROM biz.t_staff_info WHERE phone_number = #{phoneNumber}")
    StaffInfoDO selectByPhoneNumber(String phoneNumber);

}
