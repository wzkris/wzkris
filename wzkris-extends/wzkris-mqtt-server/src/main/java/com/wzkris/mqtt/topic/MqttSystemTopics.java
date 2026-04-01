package com.wzkris.mqtt.topic;

/**
 * 系统级 Topic 定义与订阅约定。
 * <p>
 * <b>前缀</b>：所有系统 topic 以 {@code $sys/brokers} 开头。
 * <p>
 * <b>通配符</b>：
 * <ul>
 *   <li>{@code +}：单层通配符，只匹配该层一个 segment，不匹配多级。例如 {@code $sys/brokers/+} 匹配
 *       {@code $sys/brokers/connected}、{@code $sys/brokers/disconnected}，不匹配 {@code $sys/brokers/123/321}。</li>
 *   <li>{@code #}：多层通配符，匹配该层及以下任意层级。例如 {@code $sys/brokers/#} 匹配
 *       {@code $sys/brokers/connected}、{@code $sys/brokers/sss/123/222} 等所有子 topic。</li>
 * </ul>
 * <p>
 * <b>订阅与接收</b>：
 * <ul>
 *   <li>订阅 {@code $sys/brokers/#}：可收到所有系统消息（如 connected、disconnected 及任意深层 topic）。</li>
 *   <li>订阅 {@code $sys/brokers/+}：仅收到紧接在 {@code $sys/brokers/} 下的一层 topic（如 connected、disconnected）。</li>
 * </ul>
 */
public final class MqttSystemTopics {

    /**
     * 系统级 Topic 前缀，所有系统 topic 均在此前缀下。
     */
    public static final String SYSTEM_PREFIX = "$sys/brokers";

    /**
     * 客户端连接事件 topic（单层，便于用 {@code $sys/brokers/+} 订阅）。
     * 完整 topic：{@code $sys/brokers/connected}
     */
    public static final String CONNECTED = SYSTEM_PREFIX + "/connected";

    /**
     * 客户端断开事件 topic（单层）。
     * 完整 topic：{@code $sys/brokers/disconnected}
     */
    public static final String DISCONNECTED = SYSTEM_PREFIX + "/disconnected";

    private MqttSystemTopics() {
    }

}
