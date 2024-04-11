package com.thingslink.auth.jdk_proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * @author : wzkris
 * @version : V1.0.0
 * @description :
 * @date : 2023/9/26 7:32
 */
public class DyProxy implements InvocationHandler {

    private final DyImpl dyimpl = new DyImpl();

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        System.out.println(1111111);
        return method.invoke(dyimpl, args);
    }
}
