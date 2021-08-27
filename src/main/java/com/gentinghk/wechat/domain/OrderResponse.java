package com.gentinghk.wechat.domain;

import lombok.Getter;
import lombok.Setter;

/**
 * @Description TODO
 * @Author Kim
 * @Date 2021/8/26 10:16
 */
@Getter
@Setter
public class OrderResponse extends WechatPayResponse {

    private String mwebUrl;
}
