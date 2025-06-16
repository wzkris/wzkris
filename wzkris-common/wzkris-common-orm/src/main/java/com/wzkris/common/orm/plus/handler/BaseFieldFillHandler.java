package com.wzkris.common.orm.plus.handler;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.wzkris.auth.rmi.enums.AuthenticatedType;
import com.wzkris.common.orm.model.BaseEntity;
import com.wzkris.common.security.utils.ClientUserUtil;
import com.wzkris.common.security.utils.SystemUserUtil;
import com.wzkris.common.security.utils.SecurityUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;

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
        if (ObjectUtil.isNotNull(metaObject)
                && metaObject.getOriginalObject() instanceof BaseEntity
                && SecurityUtil.isAuthenticated()) {
            if (ObjUtil.equals(SecurityUtil.getAuthenticatedType(), AuthenticatedType.SYSTEM_USER.getValue())) {
                fillInsert(SystemUserUtil.getUserId(), metaObject);
            } else if (ObjUtil.equals(SecurityUtil.getAuthenticatedType(), AuthenticatedType.CLIENT_USER.getValue())) {
                fillInsert(ClientUserUtil.getUserId(), metaObject);
            }
        }
    }

    private void fillInsert(Long userId, MetaObject metaObject) {
        DateTime current = DateUtil.date();
        this.setFieldValByName(BaseEntity.Fields.createAt, current, metaObject);
        this.setFieldValByName(BaseEntity.Fields.updateAt, current, metaObject);
        this.setFieldValByName(BaseEntity.Fields.creatorId, userId, metaObject);
        this.setFieldValByName(BaseEntity.Fields.updaterId, userId, metaObject);
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        if (ObjectUtil.isNotNull(metaObject)
                && metaObject.getOriginalObject() instanceof BaseEntity
                && SecurityUtil.isAuthenticated()) {
            if (ObjUtil.equals(SecurityUtil.getAuthenticatedType(), AuthenticatedType.SYSTEM_USER.getValue())) {
                fillUpdate(SystemUserUtil.getUserId(), metaObject);
            } else if (ObjUtil.equals(SecurityUtil.getAuthenticatedType(), AuthenticatedType.CLIENT_USER.getValue())) {
                fillUpdate(ClientUserUtil.getUserId(), metaObject);
            }
        }
    }

    private void fillUpdate(Long userId, MetaObject metaObject) {
        DateTime current = DateUtil.date();
        this.setFieldValByName(BaseEntity.Fields.updateAt, current, metaObject);
        this.setFieldValByName(BaseEntity.Fields.updaterId, userId, metaObject);
    }

}
