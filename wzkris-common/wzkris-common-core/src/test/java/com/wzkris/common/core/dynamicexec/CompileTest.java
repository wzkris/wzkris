package com.wzkris.common.core.dynamicexec;

import com.wzkris.common.core.dynamicexec.service.DynamicExecuteService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

@Slf4j
public class CompileTest {

    DynamicExecuteService dynamicExecuteService =
            new DynamicExecuteService(new DynamicClassLoader(this.getClass().getClassLoader()));

    @Test
    public void t1() {
        String str = dynamicExecuteService.executeDynamically(
                """
                        package org.test;

                        public class Test {
                            public static void main(String[] args) {
                                System.out.println("hello, world");
                            }
                        }
                """,
                null);
        log.info(str);
    }
}
