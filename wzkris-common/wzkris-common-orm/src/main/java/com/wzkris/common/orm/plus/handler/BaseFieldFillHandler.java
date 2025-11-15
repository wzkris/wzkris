package com.wzkris.common.orm.plus.handler;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.wzkris.common.core.constant.SecurityConstants;
import com.wzkris.common.orm.model.BaseEntity;
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

    private static Long getPrincipalId() {
        Long id;
        if (SecurityUtil.isAuthenticated()) {
            id = SecurityUtil.getId();
        } else {
            id = SecurityConstants.SYSTEM_USER_ID;
        }
        return id;
    }

    @Override
    public void insertFill(MetaObject metaObject) {
        if (ObjectUtils.isNotEmpty(metaObject)
                && metaObject.getOriginalObject() instanceof BaseEntity) {
            Long id = getPrincipalId();
            fillInsert(id, metaObject);
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
                && metaObject.getOriginalObject() instanceof BaseEntity) {
            Long id = getPrincipalId();
            fillUpdate(id, metaObject);
        }
    }

    private void fillUpdate(Serializable userId, MetaObject metaObject) {
        Date current = new Date();
        this.setFieldValByName(BaseEntity.Fields.updateAt, current, metaObject);
        this.setFieldValByName(BaseEntity.Fields.updaterId, userId, metaObject);
    }

}
