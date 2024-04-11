package org.mock.device;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 系统模块
 *
 * @author wzkris
 */
@SpringBootApplication
public class MockDeviceMain {
    public static void main(String[] args) {
        SpringApplication.run(MockDeviceMain.class, args);
        System.out.println("""
                (♥◠‿◠)ﾉﾞ  汽车设备启动成功   ლ(´ڡ`ლ)ﾞ
                
                  _|_|_|    _|_|_|  _|  _|_|
                _|        _|    _|  _|_|     
                _|        _|    _|  _|       
                  _|_|_|    _|_|_|  _|       
                                          """);
    }
}
