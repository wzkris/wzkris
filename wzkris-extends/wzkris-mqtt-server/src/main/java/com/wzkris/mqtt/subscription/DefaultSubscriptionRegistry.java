package com.wzkris.mqtt.subscription;

import com.wzkris.mqtt.model.MqttSession;
import com.wzkris.mqtt.model.RouteTargets;
import io.vertx.mqtt.MqttTopicSubscription;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 维护 session 与 topic 过滤器的关系。
 */
public class DefaultSubscriptionRegistry implements SubscriptionRegistry {

    private final Map<MqttSession, List<String>> subscriptions = new ConcurrentHashMap<>();

    private final Map<String, AtomicInteger> sharedRoundRobinIndexes = new ConcurrentHashMap<>();

    private final TopicMatcher topicMatcher;

    public DefaultSubscriptionRegistry(TopicMatcher topicMatcher) {
        this.topicMatcher = topicMatcher;
    }

    @Override
    public void addSubscriptions(MqttSession session, List<MqttTopicSubscription> subs) {
        if (subs == null || subs.isEmpty()) {
            return;
        }
        List<String> filters = subscriptions.computeIfAbsent(session, key -> new CopyOnWriteArrayList<>());
        for (MqttTopicSubscription sub : subs) {
            String topicName = sub.topicName();
            if (!filters.contains(topicName)) {
                filters.add(topicName);
            }
        }
    }

    @Override
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

    @Override
    public void removeAll(MqttSession session) {
        subscriptions.remove(session);
    }

    /**
     * 查找所有订阅了给定实际 topic 的 session。
     */
    @Override
    public List<MqttSession> match(String topic) {
        if (subscriptions.isEmpty()) {
            return Collections.emptyList();
        }
        List<MqttSession> result = new ArrayList<>();
        for (Map.Entry<MqttSession, List<String>> entry : subscriptions.entrySet()) {
            List<String> filters = entry.getValue();
            if (!hasFilters(filters)) {
                continue;
            }
            boolean matched = false;
            for (String filter : filters) {
                if (matchesNormalSubscription(filter, topic)) {
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

    /**
     * 计算客户端 publish 的分发目标：普通订阅全量投递，共享订阅单一投递。
     */
    @Override
    public RouteTargets matchForPublish(String topic) {
        if (subscriptions.isEmpty()) {
            sharedRoundRobinIndexes.remove(topic);
            return new RouteTargets(Collections.emptyList(), null);
        }

        MatchBuckets buckets = collectMatchedTargets(topic);
        MqttSession sharedTarget = selectSharedTarget(topic, buckets.getSharedCandidates());
        return new RouteTargets(new ArrayList<>(buckets.getNormalTargets()), sharedTarget);
    }

    private MatchBuckets collectMatchedTargets(String topic) {
        Set<MqttSession> normalTargets = new LinkedHashSet<>();
        Set<MqttSession> sharedCandidates = new LinkedHashSet<>();

        for (Map.Entry<MqttSession, List<String>> entry : subscriptions.entrySet()) {
            List<String> filters = entry.getValue();
            if (!hasFilters(filters)) {
                continue;
            }
            boolean normalMatched = false;
            boolean sharedMatched = false;
            for (String filter : filters) {
                if (matchesNormalSubscription(filter, topic)) {
                    normalMatched = true;
                }
                if (matchesSharedSubscription(filter, topic)) {
                    sharedMatched = true;
                }
                if (normalMatched && sharedMatched) {
                    break;
                }
            }
            if (normalMatched) {
                normalTargets.add(entry.getKey());
            }
            if (sharedMatched) {
                sharedCandidates.add(entry.getKey());
            }
        }

        return new MatchBuckets(normalTargets, sharedCandidates);
    }

    private MqttSession selectSharedTarget(String sharedTopicKey, Set<MqttSession> candidates) {
        if (candidates == null || candidates.isEmpty()) {
            sharedRoundRobinIndexes.remove(sharedTopicKey);
            return null;
        }
        List<MqttSession> list = new ArrayList<>(candidates);
        AtomicInteger indexCounter = sharedRoundRobinIndexes.computeIfAbsent(sharedTopicKey, key -> new AtomicInteger(0));
        int index = Math.floorMod(indexCounter.getAndIncrement(), list.size());
        return list.get(index);
    }

    private boolean hasFilters(List<String> filters) {
        return filters != null && !filters.isEmpty();
    }

    private boolean matchesNormalSubscription(String filter, String topic) {
        if (filter == null || topic == null) {
            return false;
        }
        if (SubscriptionConvention.isSharedSubscriptionFilter(filter)) {
            return false;
        }
        return topicMatcher.matches(filter, topic);
    }

    private boolean matchesSharedSubscription(String filter, String topic) {
        if (filter == null || topic == null) {
            return false;
        }
        if (!SubscriptionConvention.isSharedSubscriptionFilter(filter)) {
            return false;
        }
        String realTopicFilter = SubscriptionConvention.realTopicFilter(filter);
        return realTopicFilter != null && topicMatcher.matches(realTopicFilter, topic);
    }

    private static final class MatchBuckets {

        private final Set<MqttSession> normalTargets;

        private final Set<MqttSession> sharedCandidates;

        private MatchBuckets(Set<MqttSession> normalTargets, Set<MqttSession> sharedCandidates) {
            this.normalTargets = normalTargets;
            this.sharedCandidates = sharedCandidates;
        }

        private Set<MqttSession> getNormalTargets() {
            return normalTargets;
        }

        private Set<MqttSession> getSharedCandidates() {
            return sharedCandidates;
        }

    }

}

