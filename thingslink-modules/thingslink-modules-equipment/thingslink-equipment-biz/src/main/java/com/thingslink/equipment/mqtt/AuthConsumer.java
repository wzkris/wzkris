package com.thingslink.equipment.mqtt;

import cn.hutool.core.io.resource.Resource;
import cn.hutool.core.io.resource.ResourceUtil;
import cn.hutool.core.util.HexUtil;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.asymmetric.KeyType;
import cn.hutool.crypto.asymmetric.RSA;
import com.thingslink.equipment.domain.Device;
import com.thingslink.equipment.domain.dto.MessageDTO;
import com.thingslink.equipment.enums.DeviceStatus;
import com.thingslink.equipment.mapper.DeviceMapper;
import com.thingslink.equipment.mqtt.utils.ConsumerUtil;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RBoundedBlockingQueue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

/**
 * @author : wzkris
 * @version : V1.0.0
 * @description : 入网消费者
 * @date : 2023/12/5 11:20
 */
@Slf4j
@Component
public class AuthConsumer implements CommandLineRunner {

    @Autowired
    private DeviceMapper deviceMapper;

    @Autowired
    private ThreadPoolTaskExecutor threadPoolTaskExecutor;

    @Override
    public void run(String... args) {
        RBoundedBlockingQueue<MessageDTO> queue = ConsumerUtil.getAuthQueue();
        queue.trySetCapacity(10000);

        Resource rsaPrivateKey = ResourceUtil.getResourceObj("rsa_pri.key");

        RSA rsa = SecureUtil.rsa(rsaPrivateKey.readUtf8Str(), null);


        threadPoolTaskExecutor.execute(() -> {
            for (; ; ) {
                try {
                    MessageDTO messageDTO = queue.take();
                    this.handleAuth(messageDTO, rsa);
                }
                catch (Throwable e) {
                    log.error("发生异常，errmsg:{}", e.getMessage(), e);
                }
            }
        });

    }

    private void handleAuth(MessageDTO messageDTO, RSA rsa) {
        byte[] bytes = HexUtil.decodeHex(new String(messageDTO.getData()));
        String decrypt = new String(rsa.decrypt(bytes, KeyType.PrivateKey));

        Device device = new Device();
        device.setSerialNo(messageDTO.getSerialNo());
        device.setDeviceStatus(DeviceStatus.ONLINE.name());

        deviceMapper.updateBySerialNo(device);
    }
}
