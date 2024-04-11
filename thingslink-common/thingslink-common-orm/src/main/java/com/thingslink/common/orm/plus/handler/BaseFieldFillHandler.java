package com.thingslink.common.orm.plus.handler;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.thingslink.common.orm.model.BaseEntity;
import com.thingslink.common.security.utils.LoginUserUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;

import java.time.LocalDateTime;

/**
 * @author : wzkris
 * @version : V1.0.0
 * @description : 审计属性填充处理器
 * @date : 2024/1/22 16:10
 */
@Slf4j
public class BaseFieldFillHandler implements MetaObjectHandler {
    @Override
    public void insertFill(MetaObject metaObject) {
        if (ObjectUtil.isNotNull(metaObject) && metaObject.getOriginalObject() instanceof BaseEntity
                && LoginUserUtil.isLogin()) {
            LocalDateTime localDateTime = LocalDateTime.now();
            String userId = LoginUserUtil.getUserId().toString();
            this.setFieldValByName(BaseEntity.Fields.createAt, localDateTime, metaObject);
            this.setFieldValByName(BaseEntity.Fields.createBy, userId, metaObject);
            this.setFieldValByName(BaseEntity.Fields.updateAt, localDateTime, metaObject);
            this.setFieldValByName(BaseEntity.Fields.updateBy, userId, metaObject);
        }
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        if (ObjectUtil.isNotNull(metaObject) && metaObject.getOriginalObject() instanceof BaseEntity
                && LoginUserUtil.isLogin()) {
            this.setFieldValByName(BaseEntity.Fields.updateAt, LocalDateTime.now(), metaObject);
            this.setFieldValByName(BaseEntity.Fields.updateBy, LoginUserUtil.getUserId().toString(), metaObject);
        }
    }
}
