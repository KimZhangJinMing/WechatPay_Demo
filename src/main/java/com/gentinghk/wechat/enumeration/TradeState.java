package com.gentinghk.wechat.enumeration;

public enum TradeState {

    SUCCESS("支付成功"),
    REFUND("转入退款"),
    NOTPAY("未支付"),
    CLOSED("已关闭"),
    REVOKED("已撤销(付款码支付)"),
    USERPAYING("用户支付中(付款码支付)"),
    PAYERROR("付失败(其他原因，如银行返回失败)");

    TradeState(String description){}
}
