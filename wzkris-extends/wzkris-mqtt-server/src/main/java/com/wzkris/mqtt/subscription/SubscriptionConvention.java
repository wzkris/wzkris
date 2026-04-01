package com.wzkris.mqtt.subscription;

/**
 * 订阅规则约定：系统 topic 与共享订阅前缀。
 * <p>
 * <b>系统 topic 前缀</b>：{@code $sys/brokers}
 * <p>
 * <b>共享订阅前缀</b>：{@code $share/}
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

    public static final String SYS_TOPIC_PREFIX = "$sys/brokers";

    public static final String SHARED_SUBSCRIPTION_PREFIX = "$share/";

    private SubscriptionConvention() {
    }

    public static boolean isSharedSubscriptionFilter(String filter) {
        return filter != null && filter.startsWith(SHARED_SUBSCRIPTION_PREFIX);
    }

    public static String realTopicFilter(String filter) {
        if (!isSharedSubscriptionFilter(filter)) {
            return filter;
        }
        if (filter.length() <= SHARED_SUBSCRIPTION_PREFIX.length()) {
            return null;
        }
        return filter.substring(SHARED_SUBSCRIPTION_PREFIX.length());
    }

    public static boolean isSystemTopic(String topic) {
        return topic != null && topic.startsWith(SYS_TOPIC_PREFIX);
    }

}
