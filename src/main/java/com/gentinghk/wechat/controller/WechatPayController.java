package com.gentinghk.wechat.controller;

import com.gentinghk.wechat.domain.CommonResponse;
import com.gentinghk.wechat.domain.NotifyResponse;
import com.gentinghk.wechat.domain.PayNotifyVO;
import com.gentinghk.wechat.service.WechatPayService;
import com.gentinghk.wechat.util.*;
import jdk.nashorn.internal.runtime.JSONFunctions;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * @Description TODO
 * @Author Kim
 * @Date 2021/8/23 10:43
 */
@Slf4j
@RestController
public class WechatPayController {

    @Autowired
    private WechatPayService wechatPayService;

    @GetMapping("/order")
    public CommonResponse<String> createOrder(String number) {
        String outTradeNo = "GDR-ORDER-"+number;
        log.info("{} make a order,outTradeNo is {}", IPUtil.getClientIp(), outTradeNo);
        return wechatPayService.createOrder(outTradeNo);
    }

    @PostMapping("/callback")
    public NotifyResponse callback() {
        // synchronized (WechatPayController.class) {
        // 获取请求体
        String body = RequestUtil.getRequestBody();
        log.info("wechat notify result is {}", body);

        // 验证微信签名
        if (AESUtil.verifiedSign(body)) {
            PayNotifyVO payNotifyVO = JsonUtil.stringToObject(body, PayNotifyVO.class);
            return wechatPayService.payNotify(payNotifyVO);
        } else {
            log.error("微信验签失败...");
            return NotifyResponse.error();
        }
        // 加锁，处理多次通知
        // return map;
        // }

    }

    @GetMapping("/query/trade/{id}")
    public CommonResponse<String> queryOrderByOutTradeNo(@PathVariable String id) {
        return wechatPayService.queryOrderByOutTradeNo(id);
    }

    @GetMapping("/query/transaction/{id}")
    public CommonResponse<String> queryOrderByTransactionId(@PathVariable String id) {
        return wechatPayService.queryOrderByTransactionId(id);
    }

    @GetMapping("/close/trade/{id}")
    public CommonResponse<String> closeOrderByOutTradeNo(@PathVariable String id) {
        return wechatPayService.closeOrderByOutTradeNo(id);
    }

    @GetMapping("/close/transaction/{id}")
    public CommonResponse<String> closeOrderByTransactionId(@PathVariable String id) {
        return wechatPayService.closeOrderByTransactionId(id);
    }

}
