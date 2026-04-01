package com.wzkris.common.notifier.domain;

import com.wzkris.common.notifier.enums.DingtalkTemplateKeyEnum;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.util.Assert;

import java.util.HashMap;
import java.util.Map;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class DingtalkMessage {

    /**
     * 指定消息类型
     */
    private DingtalkTemplateKeyEnum templateKey;

    /**
     * 消息参数
     */
    private Map<String, Object> templateParams;

    /**
     * Webhook标识
     * 如果指定，则从管理的webhooks中选择对应的webhook；如果未指定，则使用默认webhook
     */
    private String webhookKey;

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {

        private DingtalkTemplateKeyEnum templateKey;

        private Map<String, Object> templateParams = new HashMap<>();

        private String webhookKey;

        public Builder templateKey(DingtalkTemplateKeyEnum templateKey) {
            this.templateKey = templateKey;
            return this;
        }

        public Builder templateParams(Map<String, Object> templateParams) {
            if (templateParams != null) {
                this.templateParams = new java.util.HashMap<>(templateParams);
            }
            return this;
        }

        public Builder webhookKey(String webhookKey) {
            this.webhookKey = webhookKey;
            return this;
        }

        public DingtalkMessage build() {
            Assert.notNull(this.templateKey, "templateKey 不能为空");
            Assert.notNull(this.webhookKey, "webhookKey 不能为空");
            Assert.notNull(this.templateParams, "templateParams 不能为空");
            templateKey.validate(this.templateParams);
            DingtalkMessage m = new DingtalkMessage();
            m.templateKey = this.templateKey;
            m.templateParams = this.templateParams;
            m.webhookKey = this.webhookKey;
            return m;
        }

    }

}
