package com.wzkris.user.mapper;

import com.wzkris.common.orm.plus.BaseMapperPlus;
import com.wzkris.user.domain.CustomerWalletRecordDO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * 用户钱包记录表 数据层
 *
 * @author wzkris
 */
@Mapper
@Repository
public interface CustomerWalletRecordMapper extends BaseMapperPlus<CustomerWalletRecordDO> {

}
