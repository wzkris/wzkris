package com.wzkris.common.core.dynamicexec;

import com.wzkris.common.core.utils.StringUtil;

import java.io.PrintWriter;
import java.lang.reflect.Method;

/**
 * Class执行器
 *
 * @author thinglinks
 * @date 2022-07-04
 */
public class ClassExecutor {

    /**
     * 执行Main函数
     *
     * @param cls 待执行的类
     * @param out 错误信息输出
     * @return 是否执行成功
     */
    public boolean executeMain(Class<?> cls, PrintWriter out, String param) {
        Method method;
        try {
            //默认执行main函数
            method = cls.getMethod("main", String[].class);
            if (!StringUtil.isEmpty(param)) {
                method.invoke(null, (Object) new String[]{param});
            } else {
                method.invoke(null, (Object) new String[]{null});
            }
        } catch (Throwable t) {
            t.printStackTrace(out);
        }

        return false;
    }
}
