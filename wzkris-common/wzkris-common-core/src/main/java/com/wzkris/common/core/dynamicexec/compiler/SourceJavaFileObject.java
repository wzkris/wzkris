package com.wzkris.common.core.dynamicexec.compiler;

import java.net.URI;
import javax.tools.SimpleJavaFileObject;

/**
 * 存放java源码的JavaFileObject
 *
 * @author thinglinks
 * @date 2022-07-04
 */
public class SourceJavaFileObject extends SimpleJavaFileObject {

    private final String content;

    /**
     * 调用父类构造器,并设置content
     *
     * @param className
     * @param content
     */
    public SourceJavaFileObject(String className, String content) {
        super(URI.create("string:///" + className.replace('.', '/') + Kind.SOURCE.extension), Kind.SOURCE);
        this.content = content;
    }

    /**
     * 实现getCharContent,使得JavaCompiler可以从content获取java源码
     */
    @Override
    public String getCharContent(boolean ignoreEncodingErrors) {
        return content;
    }
}
