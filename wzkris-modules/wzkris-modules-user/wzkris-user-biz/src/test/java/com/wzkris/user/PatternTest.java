package com.wzkris.user;

import com.wzkris.common.security.oauth2.service.PasswordEncoderDelegate;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.util.regex.Pattern;

@Slf4j
public class PatternTest {

    @Test
    public void test() {
        PasswordEncoderDelegate delegate = new PasswordEncoderDelegate();
        boolean encode = delegate.isEncode("{}");
        System.out.println(encode);
    }

}
