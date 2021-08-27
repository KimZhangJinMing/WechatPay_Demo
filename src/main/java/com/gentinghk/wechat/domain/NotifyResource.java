package com.gentinghk.wechat.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.gentinghk.wechat.enumeration.TradeState;
import com.gentinghk.wechat.enumeration.TradeType;
import lombok.*;

import java.math.BigDecimal;

/**
 * @Description 微信支付回调解密后的资源对象
 * @Author Kim
 * @Date 2021/8/27 9:43
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class NotifyResource {

    private String id;
    @JsonProperty("appid")
    private String appId;
    @JsonProperty("mchid")
    private String mchId;
    private String outTradeNo;
    private TradeType tradeType;
    private TradeState tradeState;
    private String tradeStateDesc;
    private String bankType;
    private String attach;
    private String successTime;
    private Payer payer;
    private Amount amount;
    private SceneInfo sceneInfo;

    @Getter
    @Setter
    public class Payer {
        @JsonProperty("openid")
        private String openId;
    }

    @Getter
    @Setter
    public class Amount {
        private BigDecimal total;
        private BigDecimal payerTotal;
        private String currency;
        private String payerCurrency;
    }

    @Getter
    @Setter
    public class SceneInfo {
        private String deviceId;
    }

}
