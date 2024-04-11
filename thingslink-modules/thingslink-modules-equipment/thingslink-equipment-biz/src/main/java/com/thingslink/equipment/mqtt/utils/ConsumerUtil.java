package com.thingslink.equipment.mqtt.utils;

import com.thingslink.common.redis.util.RedisUtil;
import com.thingslink.equipment.domain.dto.MessageDTO;
import lombok.Getter;
import org.redisson.api.RBoundedBlockingQueue;

/**
 * @author : wzkris
 * @version : V1.0.0
 * @description : 设备消费者工具
 * @date : 2023/12/4 16:29
 */
public class ConsumerUtil {

    @Getter
    private static final RBoundedBlockingQueue<MessageDTO> upDownQueue = RedisUtil.getClient().getBoundedBlockingQueue("car:up_down");

    @Getter
    private static final RBoundedBlockingQueue<MessageDTO> authQueue = RedisUtil.getClient().getBoundedBlockingQueue("car:auth");

    @Getter
    private static final RBoundedBlockingQueue<MessageDTO> attrQueue = RedisUtil.getClient().getBoundedBlockingQueue("car:attr");

    @Getter
    private static final RBoundedBlockingQueue<MessageDTO> serviceQueue = RedisUtil.getClient().getBoundedBlockingQueue("car:service");

    @Getter
    private static final RBoundedBlockingQueue<MessageDTO> eventQueue = RedisUtil.getClient().getBoundedBlockingQueue("car:event");

}
