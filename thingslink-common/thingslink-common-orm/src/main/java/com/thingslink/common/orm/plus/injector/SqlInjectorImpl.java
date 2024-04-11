package com.thingslink.common.orm.plus.injector;

import com.baomidou.mybatisplus.core.injector.AbstractMethod;
import com.baomidou.mybatisplus.core.injector.DefaultSqlInjector;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import com.thingslink.common.orm.plus.method.InsertBatch;
import com.thingslink.common.orm.plus.method.UpdateBatchByIds;

import java.util.List;

/**
 * @author : wzkris
 * @version : V1.0.0
 * @description : sql注入器
 * @date : 2024/1/4 13:22
 */
public class SqlInjectorImpl extends DefaultSqlInjector {

    @Override
    public List<AbstractMethod> getMethodList(Class<?> mapperClass, TableInfo tableInfo) {
        List<AbstractMethod> methodList = super.getMethodList(mapperClass, tableInfo);
        methodList.add(new InsertBatch());
        if (tableInfo.havePK()) {
            methodList.add(new UpdateBatchByIds());
        }
        return methodList;
    }
}
