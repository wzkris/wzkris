package com.thingslink.equipment.mqtt;

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
 * @description : 上下线消费者
 * @date : 2023/12/4 16:24
 */
@Slf4j
@Component
public class UpDownConsumer implements CommandLineRunner {

    @Autowired
    private DeviceMapper deviceMapper;

    @Autowired
    private ThreadPoolTaskExecutor threadPoolTaskExecutor;

    @Override
    public void run(String... args) {

        RBoundedBlockingQueue<MessageDTO> queue = ConsumerUtil.getUpDownQueue();
        queue.trySetCapacity(10000);

        threadPoolTaskExecutor.execute(() -> {
            for (; ; ) {
                try {
                    MessageDTO messageDTO = queue.take();
                    this.handleUpDown(messageDTO);
                }
                catch (Throwable e) {
                    log.error("发生异常，errmsg:{}", e.getMessage(), e);
                }
            }
        });
    }

    // 处理上下线
    private void handleUpDown(MessageDTO messageDTO) {
        Device device = new Device();
        device.setSerialNo(messageDTO.getSerialNo());
        device.setDeviceStatus(DeviceStatus.OFFLINE.name());

        deviceMapper.updateBySerialNo(device);
    }
}
