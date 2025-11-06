package com.wzkris.common.notifier.domain;

import com.wzkris.common.notifier.enums.EmailTemplateKey;
import lombok.*;

import java.util.List;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class EmailMessage {

    private EmailTemplateKey templateKey;

    private List<String> recipients;

    private String subject;

    private String content;

    private String fromEmail;

    private String fromName;

}


