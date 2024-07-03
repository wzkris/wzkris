package com.wzkris.common.web.handler;

import com.wzkris.common.core.domain.Result;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.boot.autoconfigure.web.ErrorProperties;
import org.springframework.boot.autoconfigure.web.servlet.error.BasicErrorController;
import org.springframework.boot.web.servlet.error.DefaultErrorAttributes;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Map;

/**
 * @author : wzkris
 * @version : V1.0.0
 * @description : 异常处理 /error端点
 * @date : 2023/12/2 14:07
 */
@Controller
@RequestMapping("${server.error.path:${error.path:/error}}")
public class ErrorController extends BasicErrorController {
    private static final ErrorProperties errorProperties;

    static {
        errorProperties = new ErrorProperties();
        errorProperties.setIncludeMessage(ErrorProperties.IncludeAttribute.ALWAYS);
    }

    public ErrorController() {
        super(new DefaultErrorAttributes(), errorProperties);
    }

    // 封装统一返回结果
    @Override
    public ResponseEntity error(HttpServletRequest request) {
        HttpStatus status = getStatus(request);
        if (status == HttpStatus.NO_CONTENT) {
            return new ResponseEntity<>(status);
        }

        // 获取原生返回对象，移除一些不必要值
        Map<String, Object> body = this.getErrorAttributes(request, getErrorAttributeOptions(request, MediaType.ALL));
        body.remove("status");
        body.remove("timestamp");
        body.remove("error");

        // 创建一个通用结构返回
        Result<Object> result = new Result<>(status.value(), body, status.getReasonPhrase());

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

}
