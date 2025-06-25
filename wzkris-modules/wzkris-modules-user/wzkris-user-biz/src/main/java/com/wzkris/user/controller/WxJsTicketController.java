package com.wzkris.user.controller;

import com.wzkris.common.core.domain.Result;
import com.wzkris.common.core.utils.StringUtil;
import com.wzkris.common.web.model.BaseController;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.bean.WxJsapiSignature;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.common.util.RandomUtils;
import me.chanjar.weixin.common.util.crypto.SHA1;
import me.chanjar.weixin.mp.api.WxMpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * 微信js签名
 *
 * @author wzkris
 */
@Slf4j
@Tag(name = "微信请求API")
@Validated
@RestController
@RequestMapping("/wx_req")
@RequiredArgsConstructor
public class WxJsTicketController extends BaseController {

    @Autowired
    @Lazy
    private WxMpService wxMpService;

    @Operation(summary = "获取jsticket")
    @GetMapping("/js_ticket")
    public Result<?> jsticket() {
        try {
            Map<String, String> jsapiKey = Map.of("js_ticket", wxMpService.getJsapiTicket());
            return ok(jsapiKey);
        } catch (WxErrorException e) {
            log.error("获取js_ticket发生异常，errmsg：{}", e.getError());
            return resp(e.getError().getErrorCode(), e.getError().getErrorMsg());
        } catch (Exception e) {
            log.error("发生异常，errmsg：{}", e.getMessage());
            return err412("暂时无法使用微信jsticket");
        }
    }

    @Operation(summary = "获取jsticket签名")
    @GetMapping("/js_ticket_sign")
    public Result<?> JsapiSignature(String url) {
        if (!StringUtil.ishttp(url)) {
            return err412("url格式不正确");
        }
        try {
            long timestamp = System.currentTimeMillis() / 1000;
            String randomStr = RandomUtils.getRandomStr();
            String jsapiTicket = wxMpService.getJsapiTicket();
            String signature = SHA1.genWithAmple(
                    "jsapi_ticket=" + jsapiTicket, "noncestr=" + randomStr, "timestamp=" + timestamp, "url=" + url);
            WxJsapiSignature jsapiSignature = new WxJsapiSignature();
            jsapiSignature.setAppId(wxMpService.getWxMpConfigStorage().getAppId());
            jsapiSignature.setTimestamp(timestamp);
            jsapiSignature.setNonceStr(randomStr);
            jsapiSignature.setUrl(url);
            jsapiSignature.setSignature(signature);
            return ok(jsapiSignature);
        } catch (WxErrorException e) {
            log.error("获取js_ticket发生异常，errmsg：{}", e.getError());
            return resp(e.getError().getErrorCode(), e.getError().getErrorMsg());
        } catch (Exception e) {
            log.error("发生异常，errmsg：{}", e.getMessage());
            return err412("暂时无法使用微信js签名");
        }
    }

}
