package com.thingslink.common.orm.plus.handler;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.thingslink.common.orm.model.BaseEntity;
import com.thingslink.common.security.model.AbstractUser;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.security.core.context.SecurityContextHolder;

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
        if (ObjectUtil.isNotNull(metaObject) && metaObject.getOriginalObject() instanceof BaseEntity) {
            Long current = DateUtil.current();
            this.setFieldValByName(BaseEntity.Fields.createAt, current, metaObject);
            this.setFieldValByName(BaseEntity.Fields.updateAt, current, metaObject);
            Long createId = 0L;
            AbstractUser userInfo = this.getUserInfo();
            if (userInfo != null) {
                createId = userInfo.getUserId();
            }
            this.setFieldValByName(BaseEntity.Fields.createId, createId, metaObject);
            this.setFieldValByName(BaseEntity.Fields.updateId, createId, metaObject);
        }
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        if (ObjectUtil.isNotNull(metaObject) && metaObject.getOriginalObject() instanceof BaseEntity) {
            Long current = DateUtil.current();
            this.setFieldValByName(BaseEntity.Fields.updateAt, current, metaObject);
            Long createId = 0L;
            AbstractUser userInfo = this.getUserInfo();
            if (userInfo != null) {
                createId = userInfo.getUserId();
            }
            this.setFieldValByName(BaseEntity.Fields.updateId, createId, metaObject);
        }
    }

    /**
     * 获取登录用户
     */
    private AbstractUser getUserInfo() {
        try {
            return (AbstractUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        }
        catch (Exception e) {
            log.warn("属性填充警告 => 用户未登录");
            return null;
        }
    }
}
