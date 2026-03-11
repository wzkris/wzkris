package com.wzkris.mqtt.subscription;

/**
 * 订阅与系统 topic 的关联约定说明。
 * <p>
 * <b>系统 topic 前缀</b>：{@code $sys/brokers}
 * <p>
 * <b>订阅与匹配关系</b>：
 * <ul>
 *   <li><b>订阅 {@code $sys/brokers/#}</b>：可收到所有系统消息。例如：
 *       <br>{@code $sys/brokers/connected}、{@code $sys/brokers/disconnected}、
 *       {@code $sys/brokers/sss/123/222} 等任意层级。</li>
 *   <li><b>订阅 {@code $sys/brokers/+}</b>：只匹配「紧接在 {@code $sys/brokers/} 下的一层」的 topic。
 *       <br>匹配：{@code $sys/brokers/connected}、{@code $sys/brokers/disconnected}。
 *       <br>不匹配：{@code $sys/brokers/123/321}（多了一层）。</li>
 * </ul>
 * <p>
 * 实现上由 {@link TopicMatcher} 按 MQTT 规范对 filter 与 topic 做层级匹配，保证上述行为一致。
 */
public final class SubscriptionConvention {

    private SubscriptionConvention() {
    }

}
