package com.wzkris.common.core.dynamicexec;


import com.wzkris.common.core.dynamicexec.service.DynamicExecuteService;
import org.junit.jupiter.api.Test;

public class CompileTest {

    DynamicExecuteService dynamicExecuteService = new DynamicExecuteService(new DynamicClassLoader(this.getClass().getClassLoader()));

    @Test
    public void t1() throws Exception {
    }
}
