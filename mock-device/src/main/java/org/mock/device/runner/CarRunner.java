package org.mock.device.runner;

import cn.hutool.core.io.resource.Resource;
import cn.hutool.core.io.resource.ResourceUtil;
import cn.hutool.core.util.ByteUtil;
import cn.hutool.core.util.HexUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.asymmetric.KeyType;
import cn.hutool.crypto.asymmetric.RSA;
import com.thingslink.common.core.utils.StringUtil;
import com.thingslink.common.mqtt.constant.MqttTopic;
import lombok.extern.slf4j.Slf4j;
import org.mock.device.util.MqttUtil;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Timer;
import java.util.TimerTask;

/**
 * @author : wzkris
 * @version : V1.0.0
 * @description : 汽车运行线程
 * @date : 2023/12/4 14:42
 */
@Slf4j
@Component
public class CarRunner implements CommandLineRunner {
    @Override
    public void run(String... args) throws Exception {
        // 生成入网加密串
        Resource rsaPublicKey = ResourceUtil.getResourceObj("rsa_pub.key");

        RSA rsa = SecureUtil.rsa(null, rsaPublicKey.readUtf8Str());

        String sn = RandomUtil.randomString("0123456789", 16);
        Timer timer = new Timer(true);
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                String str = RandomUtil.randomString("0123456789", 32);
                byte[] encryStr = rsa.encrypt(str, KeyType.PublicKey);

                // 转16进制发送
                MqttUtil.publish(String.format(MqttTopic.CAR_AUTH, MqttTopic.DEFAULT_CAR_P_K, sn), new String(HexUtil.encodeHex(encryStr)), 0);
            }
        }, 0, 2000);
    }
}
