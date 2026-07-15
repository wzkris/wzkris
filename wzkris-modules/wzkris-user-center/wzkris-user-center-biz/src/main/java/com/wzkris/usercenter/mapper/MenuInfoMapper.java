package com.wzkris.usercenter.mapper;

import com.wzkris.common.orm.plus.BaseMapperPlus;
import com.wzkris.usercenter.domain.MenuInfoDO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 菜单表 数据层
 *
 * @author wzkris
 */
@Mapper
@Repository
public interface MenuInfoMapper extends BaseMapperPlus<MenuInfoDO> {

}
