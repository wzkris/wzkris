package com.wzkris.equipment.controller;

import com.wzkris.common.core.domain.Result;
import com.wzkris.common.core.dynamicCompilation.ClassInjector;
import com.wzkris.common.core.dynamicCompilation.DynamicClassLoader;
import com.wzkris.common.core.dynamicCompilation.DynamicLoaderEngine;
import com.wzkris.common.core.dynamicCompilation.bytecode.InjectionSystem;
import com.wzkris.common.web.model.BaseController;
import com.wzkris.equipment.domain.req.DynamicCodeReq;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.io.PrintWriter;

/**
 * 动态代码编译
 *
 * @author wzkris
 * @since 2024-11-20 09:31:40
 */
@Tag(name = "协议编译")
@Slf4j
@Validated
@RestController
@RequestMapping("/protocol_compile")
public class CompileXcodeController extends BaseController {

    @Operation(summary = "动态代码编译")
    @PostMapping("/dynamicallyXcode")
    public Result<?> compileXcode(@RequestBody DynamicCodeReq codeReq) {
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        PrintWriter out = new PrintWriter(buffer, true);
        byte[] classBytes = DynamicLoaderEngine.compile(codeReq.getCode(), out, null);
        if (buffer.size() > 0) {// 编译错误直接返回信息
            return ok(buffer.toString().trim());
        }
        byte[] injectedClass = ClassInjector.injectSystem(classBytes);
        InjectionSystem.inject(null, new PrintStream(buffer, true), null);
        DynamicClassLoader classLoader = new DynamicClassLoader(this.getClass().getClassLoader());
        DynamicLoaderEngine.executeMain(classLoader, injectedClass, out, codeReq.getInparam());
        return ok(buffer.toString().trim());
    }

}
