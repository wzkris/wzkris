package com.thingslink.auth;

import com.thingslink.auth.domain.OAuth2Client;
import com.thingslink.auth.mapper.OAuth2ClientMapper;
import com.thingslink.common.orm.page.Page;
import com.thingslink.common.orm.utils.PageUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class PageTest {

    @Autowired
    private OAuth2ClientMapper oAuth2ClientMapper;

    @Test
    public void test() {
        PageUtil.startPage();
        OAuth2Client oAuth2Client = oAuth2ClientMapper.selectByClientId("12");
        Page page = PageUtil.getPage();
        System.out.println(page);
    }
}
