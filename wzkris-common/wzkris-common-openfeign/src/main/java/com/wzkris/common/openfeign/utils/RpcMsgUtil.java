package com.wzkris.common.openfeign.utils;

/**
 * 获取异常信息
 *
 * @author wzkris
 */
public class RpcMsgUtil {

    /**
     * 递归获取内部异常
     */
    public static String getDetailMsg(Throwable throwable) {
        if (throwable == null) {
            return "";
        }

        String currentError = throwable.getMessage();

        // 递归获取cause的信息
        Throwable cause = throwable.getCause();
        if (cause != null) {
            return currentError + "\n" + getDetailMsg(cause);
        } else {
            return currentError;
        }
    }

}
