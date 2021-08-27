package com.gentinghk.wechat.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.*;

import java.math.BigDecimal;

/**
 * @Description H5支付下单请求对象
 * @Author Kim
 * @Date 2021/8/24 10:26
 */
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class OrderRequest {
    /**
     * 商户号
     */
    @JsonProperty("mchid")
    private String mchId;
    /**
     * APPID
     */
    @JsonProperty("appid")
    private String appId;
    /**
     * 商品描述
     */
    private String description;
    /**
     * 商户携带订单的自定义参数
     */
    private String attach;
    /**
     * 异步接受微信支付结果通知的回调地址
     */
    private String notifyUrl;
    /**
     * 商户订单号
     */
    private String outTradeNo;
    /**
     * 商品标记
     */
    private String goodsTag;
    /**
     * 交易类型
     */
    private String tradeType;
    /**
     * 指定支付方式
     */
    private String limitPay;
    /**
     * 交易起始时间
     */
    private String timeStart;
    /**
     * 交易失效时间
     */
    private String timeExpire;
    /**
     * 商户行业编码
     */
    private String merchantCategoryCode;
    /**
     * 订单金额
     */
    private Amount amount;
    /**
     * 场景信息
     */
    private SceneInfo sceneInfo;


}