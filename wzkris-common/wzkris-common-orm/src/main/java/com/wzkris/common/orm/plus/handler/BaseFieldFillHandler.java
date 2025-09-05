package com.wzkris.common.orm.plus.handler;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.wzkris.auth.feign.enums.AuthenticatedType;
import com.wzkris.common.core.utils.StringUtil;
import com.wzkris.common.orm.model.BaseEntity;
import com.wzkris.common.security.utils.LoginCustomerUtil;
import com.wzkris.common.security.utils.LoginUserUtil;
import com.wzkris.common.security.utils.SecurityUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.ibatis.reflection.MetaObject;

import java.io.Serializable;
import java.util.Date;

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
        if (ObjectUtils.isNotEmpty(metaObject)
                && metaObject.getOriginalObject() instanceof BaseEntity
                && SecurityUtil.isAuthenticated()) {
            if (StringUtil.equals(SecurityUtil.getAuthenticatedType(), AuthenticatedType.SYSTEM_USER.getValue())) {
                fillInsert(LoginUserUtil.getId(), metaObject);
            } else if (StringUtil.equals(SecurityUtil.getAuthenticatedType(), AuthenticatedType.CUSTOMER.getValue())) {
                fillInsert(LoginCustomerUtil.getId(), metaObject);
            }
        }
    }

    private void fillInsert(Serializable userId, MetaObject metaObject) {
        Date current = new Date();
        this.setFieldValByName(BaseEntity.Fields.createAt, current, metaObject);
        this.setFieldValByName(BaseEntity.Fields.updateAt, current, metaObject);
        this.setFieldValByName(BaseEntity.Fields.creatorId, userId, metaObject);
        this.setFieldValByName(BaseEntity.Fields.updaterId, userId, metaObject);
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        if (ObjectUtils.isNotEmpty(metaObject)
                && metaObject.getOriginalObject() instanceof BaseEntity
                && SecurityUtil.isAuthenticated()) {
            if (StringUtil.equals(SecurityUtil.getAuthenticatedType(), AuthenticatedType.SYSTEM_USER.getValue())) {
                fillUpdate(LoginUserUtil.getId(), metaObject);
            } else if (StringUtil.equals(SecurityUtil.getAuthenticatedType(), AuthenticatedType.CUSTOMER.getValue())) {
                fillUpdate(LoginCustomerUtil.getId(), metaObject);
            }
        }
    }

    private void fillUpdate(Serializable userId, MetaObject metaObject) {
        Date current = new Date();
        this.setFieldValByName(BaseEntity.Fields.updateAt, current, metaObject);
        this.setFieldValByName(BaseEntity.Fields.updaterId, userId, metaObject);
    }

}
