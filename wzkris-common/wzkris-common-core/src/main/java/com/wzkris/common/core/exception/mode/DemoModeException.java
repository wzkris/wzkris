package com.wzkris.common.core.exception.mode;

/**
 * 演示模式异常
 *
 * @author wzkris
 */
public final class DemoModeException extends RuntimeException {

    public DemoModeException() {
        super("演示模式，不允许操作");
    }

}
