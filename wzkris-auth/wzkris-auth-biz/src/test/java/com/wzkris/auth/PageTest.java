package com.wzkris.auth;

import com.wzkris.auth.domain.OAuth2Client;
import com.wzkris.auth.mapper.OAuth2ClientMapper;
import com.wzkris.common.orm.page.Page;
import com.wzkris.common.orm.utils.PageUtil;
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
