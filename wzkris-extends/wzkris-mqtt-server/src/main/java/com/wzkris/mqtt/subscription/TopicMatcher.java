package com.wzkris.mqtt.subscription;

/**
 * MQTT topic 过滤器与实际 topic 的匹配：支持 {@code +}（单层）、{@code #}（任意层级）。
 * <p>
 * 规则：{@code $sys/brokers/+} 只匹配一层（如 connected、disconnected），
 * {@code $sys/brokers/#} 匹配该前缀下所有层级。详见 {@link SubscriptionConvention}。
 */
public class TopicMatcher {

    public boolean matches(String filter, String topic) {
        if (filter.equals(topic)) {
            return true;
        }
        String[] filterLevels = filter.split("/");
        String[] topicLevels = topic.split("/");

        int i = 0;
        for (; i < filterLevels.length; i++) {
            String f = filterLevels[i];
            if ("#".equals(f)) {
                // # 匹配剩余所有层级
                return true;
            }
            if (i >= topicLevels.length) {
                return false;
            }
            String t = topicLevels[i];
            if ("+".equals(f)) {
                // + 匹配单层
                continue;
            }
            if (!f.equals(t)) {
                return false;
            }
        }

        // 过滤器已经用完，topic 也必须刚好用完
        return i == topicLevels.length;
    }

}

