package com.thingslink.system.task;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author : wzkris
 * @version : V1.0.0
 * @description : 测试任务
 * @date : 2023/9/7 14:10
 */
@Slf4j
@Component("TestTask")
public class TestTask {

    public void test() {
        log.info("-------------------------测试任务---------------------------");
    }
}
