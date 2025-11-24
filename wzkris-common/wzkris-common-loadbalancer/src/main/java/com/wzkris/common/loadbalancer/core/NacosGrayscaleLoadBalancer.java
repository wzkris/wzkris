package com.wzkris.common.loadbalancer.core;

import com.alibaba.cloud.commons.lang.StringUtils;
import com.alibaba.cloud.nacos.NacosDiscoveryProperties;
import com.alibaba.cloud.nacos.balancer.NacosBalancer;
import com.alibaba.cloud.nacos.util.InetIPv6Utils;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.DefaultResponse;
import org.springframework.cloud.client.loadbalancer.EmptyResponse;
import org.springframework.cloud.client.loadbalancer.Request;
import org.springframework.cloud.client.loadbalancer.Response;
import org.springframework.cloud.loadbalancer.core.NoopServiceInstanceListSupplier;
import org.springframework.cloud.loadbalancer.core.ReactorServiceInstanceLoadBalancer;
import org.springframework.cloud.loadbalancer.core.ServiceInstanceListSupplier;
import org.springframework.util.CollectionUtils;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * @author : wzkris
 * @version : V1.0.0
 * @description : 灰度发布所依赖的负载均衡器，通过NACOS的元数据配置筛选实例<br>
 * copy{@link com.alibaba.cloud.nacos.loadbalancer.NacosLoadBalancer}
 * @date : 2024/09/07 15:36
 */
public class NacosGrayscaleLoadBalancer implements ReactorServiceInstanceLoadBalancer {

    private static final Logger log = LoggerFactory.getLogger(NacosGrayscaleLoadBalancer.class);

    private static final String IPV4_REGEX =
            "((2(5[0-5]|[0-4]\\d))|[0-1]?\\d{1,2})(.((2(5[0-5]|[0-4]\\d))|[0-1]?\\d{1,2})){3}";

    private static final String IPV6_KEY = "IPv6";

    /**
     * Storage local valid IPv6 address, it's a flag whether local machine support IPv6 address stack.
     */
    public static String ipv6;

    private final String serviceId;

    private final ObjectProvider<ServiceInstanceListSupplier> serviceInstanceListSupplierProvider;

    private final NacosDiscoveryProperties nacosDiscoveryProperties;

    @Autowired
    private InetIPv6Utils inetIPv6Utils;

    public NacosGrayscaleLoadBalancer(
            ObjectProvider<ServiceInstanceListSupplier> serviceInstanceListSupplierProvider,
            String serviceId,
            NacosDiscoveryProperties nacosDiscoveryProperties) {
        this.serviceId = serviceId;
        this.serviceInstanceListSupplierProvider = serviceInstanceListSupplierProvider;
        this.nacosDiscoveryProperties = nacosDiscoveryProperties;
    }

    @PostConstruct
    public void init() {
        String ip = nacosDiscoveryProperties.getIp();
        if (StringUtils.isNotEmpty(ip)) {
            ipv6 = Pattern.matches(IPV4_REGEX, ip)
                    ? nacosDiscoveryProperties.getMetadata().get(IPV6_KEY)
                    : ip;
        } else {
            ipv6 = inetIPv6Utils.findIPv6Address();
        }
    }

    private List<ServiceInstance> filterInstanceByIpType(List<ServiceInstance> instances) {
        if (StringUtils.isNotEmpty(ipv6)) {
            List<ServiceInstance> ipv6InstanceList = new ArrayList<>();
            for (ServiceInstance instance : instances) {
                if (Pattern.matches(IPV4_REGEX, instance.getHost())) {
                    if (StringUtils.isNotEmpty(instance.getMetadata().get(IPV6_KEY))) {
                        ipv6InstanceList.add(instance);
                    }
                } else {
                    ipv6InstanceList.add(instance);
                }
            }
            // Provider has no IPv6, should use IPv4.
            if (ipv6InstanceList.isEmpty()) {
                return instances.stream()
                        .filter(instance -> Pattern.matches(IPV4_REGEX, instance.getHost()))
                        .collect(Collectors.toList());
            } else {
                return ipv6InstanceList;
            }
        }
        return instances.stream()
                .filter(instance -> Pattern.matches(IPV4_REGEX, instance.getHost()))
                .collect(Collectors.toList());
    }

    private List<ServiceInstance> filterInstanceByGrayVersion(List<ServiceInstance> instancesToChoose) {
        if (StringUtils.isNotBlank(VersionContext.getVersion())) {
            List<ServiceInstance> grayVersionInst = instancesToChoose.stream()
                    .filter(serviceInstance -> {
                        String version = serviceInstance.getMetadata().get("version");
                        return StringUtils.equals(version, VersionContext.getVersion());
                    })
                    .collect(Collectors.toList());
            if (!CollectionUtils.isEmpty(grayVersionInst)) {
                instancesToChoose = grayVersionInst;
            }
        }
        return instancesToChoose;
    }

    @Override
    public Mono<Response<ServiceInstance>> choose(Request request) {
        ServiceInstanceListSupplier supplier =
                serviceInstanceListSupplierProvider.getIfAvailable(NoopServiceInstanceListSupplier::new);
        return supplier.get(request).next().map(this::getInstanceResponse);
    }

    // 改造获取实例方法
    private Response<ServiceInstance> getInstanceResponse(List<ServiceInstance> serviceInstances) {
        if (serviceInstances.isEmpty()) {
            log.warn("No servers available for service: " + this.serviceId);
            return new EmptyResponse();
        }

        try {
            String clusterName = this.nacosDiscoveryProperties.getClusterName();

            List<ServiceInstance> instancesToChoose = serviceInstances;
            if (StringUtils.isNotBlank(clusterName)) {
                List<ServiceInstance> sameClusterInstances = serviceInstances.stream()
                        .filter(serviceInstance -> {
                            String cluster = serviceInstance.getMetadata().get("nacos.cluster");
                            return StringUtils.equals(cluster, clusterName);
                        })
                        .collect(Collectors.toList());
                if (!CollectionUtils.isEmpty(sameClusterInstances)) {
                    instancesToChoose = sameClusterInstances;
                }
            } else {
                log.warn(
                        "A cross-cluster call occurs，name = {}, clusterName = {}, instance = {}",
                        serviceId,
                        clusterName,
                        serviceInstances);
            }
            instancesToChoose = this.filterInstanceByIpType(instancesToChoose);

            instancesToChoose = this.filterInstanceByGrayVersion(instancesToChoose);

            ServiceInstance instance = NacosBalancer.getHostByRandomWeight3(instancesToChoose);

            return new DefaultResponse(instance);
        } catch (Exception e) {
            log.warn("NacosLoadBalancer error", e);
            return null;
        }
    }

}
