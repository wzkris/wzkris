package com.wzkris.common.orm.plus.handler;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.wzkris.common.core.constant.SecurityConstants;
import com.wzkris.common.core.support.UserContextHelper;
import com.wzkris.common.core.support.LoginUser;
import com.wzkris.common.core.utils.StringUtil;
import com.wzkris.common.orm.model.BaseEntity;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.ibatis.reflection.MetaObject;

import java.time.OffsetDateTime;

/**
 * @author : wzkris
 * @version : V1.0.0
 * @description : 审计属性填充处理器
 * @date : 2024/1/22 16:10
 */
@Slf4j
public class BaseFieldFillHandler implements MetaObjectHandler {

    private final UserContextHelper userContextHelper;

    public BaseFieldFillHandler(UserContextHelper userContextHelper) {
        this.userContextHelper = userContextHelper;
    }

    @Override
    public void insertFill(MetaObject metaObject) {
        if (ObjectUtils.isNotEmpty(metaObject)
                && metaObject.getOriginalObject() instanceof BaseEntity) {
            fillInsert(currentUserId(), currentHint(), metaObject);
        }
    }

    private void fillInsert(Long userId, String hint, MetaObject metaObject) {
        OffsetDateTime current = OffsetDateTime.now();
        this.setFieldValByName(BaseEntity.Fields.createAt, current, metaObject);
        this.setFieldValByName(BaseEntity.Fields.updateAt, current, metaObject);
        this.setFieldValByName(BaseEntity.Fields.creatorId, userId, metaObject);
        this.setFieldValByName(BaseEntity.Fields.updaterId, userId, metaObject);
        this.setFieldValByName(BaseEntity.Fields.hint, StringUtil.defaultIfEmpty(hint, StringUtil.EMPTY), metaObject);
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        if (ObjectUtils.isNotEmpty(metaObject)
                && metaObject.getOriginalObject() instanceof BaseEntity) {
            fillUpdate(currentUserId(), metaObject);
        }
    }

    private void fillUpdate(Long userId, MetaObject metaObject) {
        OffsetDateTime current = OffsetDateTime.now();
        this.setFieldValByName(BaseEntity.Fields.updateAt, current, metaObject);
        this.setFieldValByName(BaseEntity.Fields.updaterId, userId, metaObject);
    }

    /**
     * 当前操作者用户ID，无登录上下文时兜底为系统用户
     */
    private Long currentUserId() {
        LoginUser loginUser = userContextHelper.getLoginUser();
        return loginUser != null ? loginUser.getUid() : SecurityConstants.SYSTEM_USER_ID;
    }

    /**
     * 当前操作者标签
     */
    private String currentHint() {
        LoginUser loginUser = userContextHelper.getLoginUser();
        return loginUser != null ? loginUser.getHint() : null;
    }

}
