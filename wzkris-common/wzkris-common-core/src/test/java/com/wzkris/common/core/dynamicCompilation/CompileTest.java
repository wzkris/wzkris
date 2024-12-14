package com.wzkris.common.core.dynamicCompilation;


import com.wzkris.common.core.dynamicCompilation.service.DynamicExecuteService;
import org.junit.jupiter.api.Test;

public class CompileTest {

    DynamicExecuteService dynamicExecuteService = new DynamicExecuteService(new DynamicClassLoader(this.getClass().getClassLoader()));

    @Test
    public void t1() throws Exception {
    }
}
