package com.wzkris.common.notifier.domain;

import com.wzkris.common.notifier.enums.DingtalkTemplateKeyEnum;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.util.Assert;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class DingtalkMessage {

    /**
     * 指定消息类型
     */
    private DingtalkTemplateKeyEnum templateKey;

    /**
     * 发送对象
     */
    private List<String> recipients;

    /**
     * 消息参数
     */
    private Map<String, Object> templateParams;

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {

        private DingtalkTemplateKeyEnum templateKey;

        private List<String> recipients;

        private Map<String, Object> templateParams = new HashMap<>();

        public Builder templateKey(DingtalkTemplateKeyEnum templateKey) {
            this.templateKey = templateKey;
            return this;
        }

        public Builder recipients(List<String> recipients) {
            this.recipients = recipients;
            return this;
        }

        public Builder templateParams(Map<String, Object> templateParams) {
            if (templateParams != null) {
                this.templateParams = new java.util.HashMap<>(templateParams);
            }
            return this;
        }

        public DingtalkMessage build() {
            Assert.notNull(this.templateKey, "templateKey 不能为空");
            Assert.notEmpty(this.recipients, "recipients 不能为空");
            templateKey.validate(this.templateParams);
            DingtalkMessage m = new DingtalkMessage();
            m.templateKey = this.templateKey;
            m.recipients = this.recipients;
            m.templateParams = this.templateParams;
            return m;
        }

    }

}
