package com.gentinghk.wechat.domain;

import lombok.*;

/**
 * @Description TODO
 * @Author Kim
 * @Date 2021/8/24 12:02
 */
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class SceneInfo {

    /**
     * 商户端设备号
     */
    private String deviceId;
    /**
     * 商户端设备IP
     */
    private String deviceIp;
    /**
     * 用户终端IP
     */
    private String payerClientIp;
    /**
     * 操作员
     */
    private String operatorId;
    /**
     * 商户门店信息
     */
    private StoreInfo storeInfo;
}
