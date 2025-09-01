package com.wzkris.common.core.function;

/**
 * 可抛出异常的Supplier接口
 *
 * @author wzkris
 */
@FunctionalInterface
public interface ThrowableSupplier<T, E extends Throwable> {

    T get() throws E;

}
