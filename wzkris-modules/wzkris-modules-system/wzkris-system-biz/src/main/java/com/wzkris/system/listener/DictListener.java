package com.wzkris.system.listener;

import com.wzkris.system.domain.GlobalDictData;
import com.wzkris.system.listener.event.RefreshDictEvent;
import com.wzkris.system.mapper.GlobalDictDataMapper;
import com.wzkris.system.service.GlobalDictTypeService;
import com.wzkris.system.utils.DictCacheUtil;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author : wzkris
 * @version : V1.0.0
 * @description : 字典事件监听
 * @date : 2024/11/20 15:10
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class DictListener {

    private final GlobalDictDataMapper dictDataMapper;
    private final GlobalDictTypeService dictTypeService;

    @PostConstruct
    public void loadingCache() {
        dictTypeService.loadingDictCache();
    }

    /**
     * 异步刷新缓存
     */
    @Async
    @EventListener
    public void loginEvent(RefreshDictEvent dictEvent) {
        List<GlobalDictData> dictData = dictDataMapper.listByTypes(dictEvent.getDictTypes());
        Map<String, List<GlobalDictData>> dictType = dictData.stream().collect(Collectors.groupingBy(GlobalDictData::getDictType));

        for (Map.Entry<String, List<GlobalDictData>> entry : dictType.entrySet()) {
            DictCacheUtil.setDictCache(entry.getKey(), dictData);
        }
    }
}
