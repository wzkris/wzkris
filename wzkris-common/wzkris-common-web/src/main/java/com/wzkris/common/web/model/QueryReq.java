package com.wzkris.common.web.model;

import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

@Setter
public class QueryReq {

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

}
