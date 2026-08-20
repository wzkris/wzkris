package com.wzkris.common.core.model;

import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

@Setter
public abstract class QueryRequest {

    private Map<String, Object> params;

    public Object getParam(String key) {
        return getParams().get(key);
    }

    public Map<String, Object> getParams() {
        if (params == null) {
            params = new HashMap<>(2);
        }
        return params;
    }

    public Object getBeginTime() {
        return getParam("beginTime");
    }

    public Object getEndTime() {
        return getParam("endTime");
    }

}

