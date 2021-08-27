package com.gentinghk.wechat.enumeration;

public enum TradeType {
    /**
     * 公众号支付
     */
    JSAPI("公众号支付"),
    /**
     * 扫码支付
     */
    NATIVE("扫码支付"),
    /**
     * App支付
     */
    APP("App支付"),
    /**
     * H5支付
     */
    MWEB("H5支付"),
    /**
     * 付款码支付
     */
    MICROPAY("付款码支付");

    TradeType(String description){}
}
