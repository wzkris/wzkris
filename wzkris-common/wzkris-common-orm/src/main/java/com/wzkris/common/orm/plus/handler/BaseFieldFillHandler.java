package com.wzkris.common.orm.plus.handler;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.wzkris.common.orm.model.BaseEntity;
import com.wzkris.common.security.oauth2.enums.LoginType;
import com.wzkris.common.security.utils.ClientUserUtil;
import com.wzkris.common.security.utils.SecurityUtil;
import com.wzkris.common.security.utils.LoginUserUtil;
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
        if (ObjectUtil.isNotNull(metaObject) && metaObject.getOriginalObject() instanceof BaseEntity
                && SecurityUtil.isAuthenticated()) {
            if (SecurityUtil.getLoginType().equals(LoginType.SYSTEM_USER)) {
                fillInsert(this.getUserId(), metaObject);
            }
            else if (SecurityUtil.getLoginType().equals(LoginType.CLIENT_USER)) {
                fillInsert(this.getAppUserId(), metaObject);
            }
        }
    }

    private void fillInsert(Long userId, MetaObject metaObject) {
        Long current = DateUtil.current();
        this.setFieldValByName(BaseEntity.Fields.createAt, current, metaObject);
        this.setFieldValByName(BaseEntity.Fields.updateAt, current, metaObject);
        this.setFieldValByName(BaseEntity.Fields.createId, userId, metaObject);
        this.setFieldValByName(BaseEntity.Fields.updateId, userId, metaObject);
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        if (ObjectUtil.isNotNull(metaObject) && metaObject.getOriginalObject() instanceof BaseEntity
                && SecurityUtil.isAuthenticated()) {
            if (SecurityUtil.getLoginType().equals(LoginType.SYSTEM_USER)) {
                fillUpdate(this.getUserId(), metaObject);
            }
            else if (SecurityUtil.getLoginType().equals(LoginType.CLIENT_USER)) {
                fillUpdate(this.getAppUserId(), metaObject);
            }
        }
    }

    private void fillUpdate(Long userId, MetaObject metaObject) {
        Long current = DateUtil.current();
        this.setFieldValByName(BaseEntity.Fields.updateAt, current, metaObject);
        this.setFieldValByName(BaseEntity.Fields.updateId, userId, metaObject);
    }

    /**
     * 获取登录用户
     */
    private Long getUserId() {
        try {
            return LoginUserUtil.getUserId();
        }
        catch (Exception e) {
            log.warn("属性填充警告 => 用户未登录");
            return 0L;
        }
    }

    /**
     * 获取登录用户
     */
    private Long getAppUserId() {
        try {
            return ClientUserUtil.getUserId();
        }
        catch (Exception e) {
            log.warn("属性填充警告 => 用户未登录");
            return 0L;
        }
    }
}
