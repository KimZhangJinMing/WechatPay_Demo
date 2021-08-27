package com.gentinghk.wechat.service;


import com.fasterxml.jackson.core.type.TypeReference;
import com.gentinghk.wechat.domain.*;
import com.gentinghk.wechat.enumeration.TradeType;
import com.gentinghk.wechat.util.AESUtil;
import com.gentinghk.wechat.util.IPUtil;
import com.gentinghk.wechat.util.JsonUtil;
import com.gentinghk.wechat.util.RequestUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;

/**
 * @Description TODO
 * @Author Kim
 * @Date 2021/8/23 9:58
 */
@Service
@Slf4j
public class WechatPayService {

    @Value("${wechatPay.order.orderUrl}")
    private String orderUrl;
    @Value("${wechatPay.order.orderNotifyUrl}")
    private String orderNotifyUrl;
    @Value("${wechatPay.order.merchantCategoryCode}")
    private String merchantCategoryCode;
    @Value("${wechatPay.order.orderQueryByTransactionIdUrl}")
    private String orderQueryByTransactionIdUrl;
    @Value("${wechatPay.order.orderQueryByOutTradeNoUrl}")
    private String orderQueryByOutTradeNoUrl;
    @Value("${wechatPay.order.orderCloseByTransactionIdUrl}")
    private String orderCloseByTransactionIdUrl;
    @Value("${wechatPay.order.orderCloseByOutTradeNoUrl}")
    private String orderCloseByOutTradeNoUrl;

    /**
     *
     *
     * @return
     * @throws Exception
     */
    public CommonResponse<String> createOrder(String outTradeNo) {
        OrderRequest orderRequest = OrderRequest.builder()
                .amount(Amount.builder().total(new BigDecimal("2")).Currency(WechatPayGlobalConstant.currency).build())
                .sceneInfo(SceneInfo.builder().payerClientIp(IPUtil.getClientIp()).build())
                .mchId(WechatPayGlobalConstant.mchId)
                .description("GDR ONLINE PAY")
                .notifyUrl(orderNotifyUrl)
                .outTradeNo(outTradeNo)
                .appId(WechatPayGlobalConstant.appId)
                .merchantCategoryCode(merchantCategoryCode)
                .tradeType(TradeType.MWEB.name())
                .build();
        WechatPayResponse response = RequestUtil.httpPost(orderUrl, JsonUtil.objectToString(orderRequest));
        if(response != null && response.getBody() != null) {
            OrderResponse orderResponse = JsonUtil.stringToObject(response.getBody(), OrderResponse.class);
            return new CommonResponse<>(orderResponse.getMwebUrl());
        }
        return new CommonResponse<>(500 ,response.getMessage());
    }

    public NotifyResponse payNotify(PayNotifyVO payNotifyVO) {
        // todo 判断订单是否已经完成通知,完成通知直接返回SUCCESS,在对业务数据进行状态检查和处理之前,要采用锁进行并发控制,避免函数重入造成数据混乱
        // 支付成功的通知类型
        if("TRANSACTION.SUCCESS".equalsIgnoreCase(payNotifyVO.getEventType())) {
            PayNotifyVO.Resource resource = payNotifyVO.getResource();
            // 解密数据
            String notifyResult = AESUtil.decryptResponseBody(resource.getAssociatedData(), resource.getNonce(), resource
                    .getCiphertext());
            NotifyResource notifyResource = JsonUtil.stringToObject(notifyResult, NotifyResource.class);
            log.info("订单状态: {}", notifyResource.getTradeState());
            log.info("解密后的数据为 {}",notifyResult);
        }else {
            log.info("微信返回支付错误, {}",payNotifyVO.getSummary());
            return NotifyResponse.error();
        }
        return NotifyResponse.success();

    }

    public CommonResponse<String> queryOrderByTransactionId(String transactionId) {
        String transactionIdOrderQueryUrl = MessageFormat.format(orderQueryByTransactionIdUrl, transactionId);
        Map<String,String> params = new HashMap<>();
        params.put("mchid",WechatPayGlobalConstant.mchId);
        WechatPayResponse wechatPayResponse = RequestUtil.httpGet(transactionIdOrderQueryUrl,params);
        if(wechatPayResponse != null && wechatPayResponse.getBody() != null) {
            log.info("queryOrderByTransactionId success! result is {}", wechatPayResponse.getBody());
            NotifyResource notifyResource = JsonUtil.stringToObject(wechatPayResponse.getBody(), NotifyResource.class);
            System.out.println(notifyResource);
            return new CommonResponse<>(wechatPayResponse.getBody());
        }
        return new CommonResponse<>(wechatPayResponse.getMessage());
    }

    public CommonResponse<String> queryOrderByOutTradeNo(String outTradeNo) {
        String outTradeNoQueryUrl = MessageFormat.format(orderQueryByOutTradeNoUrl, outTradeNo);
        Map<String,String> params = new HashMap<>();
        params.put("mchid",WechatPayGlobalConstant.mchId);
        WechatPayResponse wechatPayResponse = RequestUtil.httpGet(outTradeNoQueryUrl,params);
        if(wechatPayResponse != null && wechatPayResponse.getBody() != null) {
            log.info("queryOrderByTransactionId success! result is {}", wechatPayResponse.getBody());
            NotifyResource notifyResource = JsonUtil.stringToObject(wechatPayResponse.getBody(), NotifyResource.class);
            System.out.println(notifyResource);
            return new CommonResponse<>(wechatPayResponse.getBody());
        }
        return new CommonResponse<>(wechatPayResponse.getMessage());
    }

    public CommonResponse<String> closeOrderByTransactionId(String transactionId) {
        String transactionIdOrderQueryUrl = MessageFormat.format(orderCloseByTransactionIdUrl, transactionId);
        Map<String,String> params = new HashMap<>();
        params.put("mchid",WechatPayGlobalConstant.mchId);
        WechatPayResponse wechatPayResponse = RequestUtil.httpPost(transactionIdOrderQueryUrl,JsonUtil.objectToString(params));
        if(wechatPayResponse != null && wechatPayResponse.getBody() != null) {
            log.info("closeOrderByTransactionId success! result is {}", wechatPayResponse.getBody());
            NotifyResource notifyResource = JsonUtil.stringToObject(wechatPayResponse.getBody(), NotifyResource.class);
            System.out.println(notifyResource);
            return new CommonResponse<>(wechatPayResponse.getBody());
        }
        return new CommonResponse<>(wechatPayResponse.getMessage());
    }

    public CommonResponse<String> closeOrderByOutTradeNo(String outTradeNo) {
        String outTradeNoQueryUrl = MessageFormat.format(orderCloseByOutTradeNoUrl, outTradeNo);
        Map<String,String> params = new HashMap<>();
        params.put("mchid",WechatPayGlobalConstant.mchId);
        WechatPayResponse wechatPayResponse = RequestUtil.httpPost(outTradeNoQueryUrl,JsonUtil.objectToString(params));
        if(wechatPayResponse != null && wechatPayResponse.getBody() != null) {
            log.info("closeOrderByOutTradeNo success! result is {}", wechatPayResponse.getBody());
            NotifyResource notifyResource = JsonUtil.stringToObject(wechatPayResponse.getBody(), NotifyResource.class);
            System.out.println(notifyResource);
            return new CommonResponse<>(wechatPayResponse.getBody());
        }
        return new CommonResponse<>(wechatPayResponse.getMessage());
    }
}
