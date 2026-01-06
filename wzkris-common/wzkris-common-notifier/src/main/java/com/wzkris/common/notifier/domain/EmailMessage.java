package com.wzkris.common.notifier.domain;

import com.wzkris.common.notifier.enums.EmailTemplateKeyEnum;
import lombok.*;

import java.util.List;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class EmailMessage {

    private EmailTemplateKeyEnum templateKey;

    private List<String> recipients;

    private String subject;

    private String content;

    private String fromName;

}


