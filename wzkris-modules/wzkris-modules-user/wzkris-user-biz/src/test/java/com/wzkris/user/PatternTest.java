package com.wzkris.user;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.util.regex.Pattern;

@Slf4j
public class PatternTest {

    @Test
    public void test() {
        String pattern = "^\\S*$";
        Pattern compile = Pattern.compile(pattern);
        log.info("{}", compile.matcher("ss你好").lookingAt());
        log.info("{}", compile.matcher("ssA").lookingAt());
        log.info("{}", compile.matcher("s s").lookingAt());
        log.info("{}", compile.matcher("ss").lookingAt());
        log.info("{}", compile.matcher("s，s").lookingAt());
        log.info("{}", compile.matcher("s,s").lookingAt());
    }

}
