package com.thingslink.user.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@TableName(autoResultMap = true)
public class Oauth2RegisteredClient {
    @TableId
    private Long id;
    // 客户端id 等价于app_id
    private String clientId;
    // 客户端密钥 等价于app_secret
    private String clientSecret;
    // 权限域
    @TableField(typeHandler = JacksonTypeHandler.class)
    private List<String> scopes;
    // 授权类型
    @TableField(typeHandler = JacksonTypeHandler.class)
    private List<String> authorizationGrantTypes;
    // 回调地址
    @TableField(typeHandler = JacksonTypeHandler.class)
    private List<String> redirectUris;
    // 客户端状态
    private String status;
    // 是否自动放行
    private Boolean autoApprove;

    private String createBy;
    private LocalDateTime createAt;
    private String updateBy;
    private LocalDateTime updateAt;

    public enum Status {
        NORMAL("0", "正常"),
        DISABLE("1", "停用"),
        WAIT_AUDIT("2", "待审核"),
        REJECT_AUDIT("3", "审核拒绝");


        private final String value;

        Status(String value, String description) {
            this.value = value;
        }

        public String value() {
            return this.value;
        }
    }
}