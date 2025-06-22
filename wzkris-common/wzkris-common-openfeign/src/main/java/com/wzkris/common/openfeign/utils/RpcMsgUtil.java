package com.wzkris.common.openfeign.utils;

import com.wzkris.common.core.utils.StringUtil;

public class RpcMsgUtil {

    /**
     * 递归获取内部异常
     */
    public static String getDetailMsg(Throwable throwable) {
        if (throwable == null) {
            return "";
        }

        String currentError = StringUtil.toString(throwable);

        // 递归获取cause的信息
        Throwable cause = throwable.getCause();
        if (cause != null) {
            return currentError + "\n" + getDetailMsg(cause);
        } else {
            return currentError;
        }
    }

}
