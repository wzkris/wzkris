package com.wzkris.common.orm.plus.handler;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.wzkris.common.orm.model.BaseEntity;
import com.wzkris.common.security.oauth2.enums.UserType;
import com.wzkris.common.security.utils.SecureUtil;
import com.wzkris.common.security.utils.SysUtil;
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
                && SecureUtil.isAuthenticated()) {
            if (SecureUtil.getUserType().equals(UserType.SYS_USER)) {
                Long current = DateUtil.current();
                Long createId = this.getUserId();
                this.setFieldValByName(BaseEntity.Fields.createAt, current, metaObject);
                this.setFieldValByName(BaseEntity.Fields.updateAt, current, metaObject);
                this.setFieldValByName(BaseEntity.Fields.createId, createId, metaObject);
                this.setFieldValByName(BaseEntity.Fields.updateId, createId, metaObject);
            }
        }
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        if (ObjectUtil.isNotNull(metaObject) && metaObject.getOriginalObject() instanceof BaseEntity
                && SecureUtil.isAuthenticated()) {
            if (SecureUtil.getUserType().equals(UserType.SYS_USER)) {
                Long current = DateUtil.current();
                Long createId = this.getUserId();
                this.setFieldValByName(BaseEntity.Fields.updateAt, current, metaObject);
                this.setFieldValByName(BaseEntity.Fields.updateId, createId, metaObject);
            }
        }
    }

    /**
     * 获取登录用户
     */
    private Long getUserId() {
        try {
            return SysUtil.getUserId();
        }
        catch (Exception e) {
            log.warn("属性填充警告 => 用户未登录");
            return 0L;
        }
    }
}
