package com.wzkris.common.orm.plus.extension;

import com.baomidou.mybatisplus.core.injector.AbstractMethod;
import com.baomidou.mybatisplus.core.injector.DefaultSqlInjector;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import com.wzkris.common.orm.plus.extension.method.SelectByIdForUpdate;
import java.util.List;
import org.apache.ibatis.session.Configuration;

public class ExtenseSqlInjector extends DefaultSqlInjector {

    @Override
    public List<AbstractMethod> getMethodList(Configuration configuration, Class<?> mapperClass, TableInfo tableInfo) {
        List<AbstractMethod> methodList = super.getMethodList(configuration, mapperClass, tableInfo);
        methodList.add(new SelectByIdForUpdate());
        return methodList;
    }
}
