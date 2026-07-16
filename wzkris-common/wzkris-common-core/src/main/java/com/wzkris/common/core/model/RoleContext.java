package com.wzkris.common.core.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 角色上下文（与用户身份分离存储）
 *
 * @author wzkris
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoleContext implements Serializable {

    private List<UserRole> roles;

    /**
     * 是否超级用户（从 LoginUser 复制，供规则层判断）
     */
    private boolean superUser;

    public RoleContext(List<UserRole> roles) {
        this.roles = roles;
    }

    /**
     * 展开所有角色的功能权限码（去重）
     * <p>
     * 计算属性，不参与序列化，避免 Redis 中多出 grantedAuthority 字段
     */
    @JsonIgnore
    public final List<String> getGrantedAuthority() {
        if (roles == null || roles.isEmpty()) {
            return Collections.emptyList();
        }
        return roles.stream()
                .map(UserRole::getPermissions)
                .filter(Objects::nonNull)
                .flatMap(List::stream)
                .distinct()
                .collect(Collectors.toList());
    }

}
