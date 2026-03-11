package com.wzkris.mqtt.subscription;

import com.wzkris.mqtt.session.MqttSession;
import io.vertx.mqtt.MqttTopicSubscription;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 维护 session 与 topic 过滤器的关系。
 */
public class SubscriptionManager {

    private final Map<MqttSession, List<String>> subscriptions = new ConcurrentHashMap<>();

    private final TopicMatcher topicMatcher;

    public SubscriptionManager(TopicMatcher topicMatcher) {
        this.topicMatcher = topicMatcher;
    }

    public void addSubscriptions(MqttSession session, List<MqttTopicSubscription> subs) {
        if (subs == null || subs.isEmpty()) {
            return;
        }
        List<String> filters = subscriptions.computeIfAbsent(session, key -> new ArrayList<>());
        for (MqttTopicSubscription sub : subs) {
            filters.add(sub.topicName());
        }
    }

    public void removeSubscriptions(MqttSession session, List<String> topics) {
        List<String> filters = subscriptions.get(session);
        if (filters == null || filters.isEmpty() || topics == null || topics.isEmpty()) {
            return;
        }
        filters.removeIf(topics::contains);
        if (filters.isEmpty()) {
            subscriptions.remove(session);
        }
    }

    public void removeAll(MqttSession session) {
        subscriptions.remove(session);
    }

    /**
     * 查找所有订阅了给定实际 topic 的 session。
     */
    public List<MqttSession> match(String topic) {
        if (subscriptions.isEmpty()) {
            return Collections.emptyList();
        }
        List<MqttSession> result = new ArrayList<>();
        for (Map.Entry<MqttSession, List<String>> entry : subscriptions.entrySet()) {
            List<String> filters = entry.getValue();
            if (filters == null || filters.isEmpty()) {
                continue;
            }
            boolean matched = false;
            for (String filter : filters) {
                if (topicMatcher.matches(filter, topic)) {
                    matched = true;
                    break;
                }
            }
            if (matched) {
                result.add(entry.getKey());
            }
        }
        return result;
    }

}

