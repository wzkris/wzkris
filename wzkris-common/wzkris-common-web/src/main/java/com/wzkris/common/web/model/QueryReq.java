package com.wzkris.common.web.model;

import cn.hutool.core.convert.Convert;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

@Setter
public class QueryReq {

    private Map<String, Object> params;

    public Object getParam(String key) {
        return getParams().get(key);
    }

    public <T> T getParam(String key, Class<T> clazz) {
        return getParam(key) == null ? null : Convert.convert(clazz, getParam(key), null);
    }

    public Map<String, Object> getParams() {
        if (params == null) {
            params = new HashMap<>(2);
        }
        return params;
    }
}
