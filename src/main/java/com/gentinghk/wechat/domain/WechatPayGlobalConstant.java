package com.gentinghk.wechat.domain;

import java.security.cert.X509Certificate;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Description TODO
 * @Author Kim
 * @Date 2021/8/25 11:40
 */

public class WechatPayGlobalConstant {

    public static String appId;
    public static String mchId;
    public static String apiV3Key;
    public static String privateKey;
    public static String mchSerialNo;
    public static String currency;
    public static String wechatCertificatesUrl;
    // 保存微信平台证书
    public static Map<String, X509Certificate> certificateMap = new ConcurrentHashMap<>();
}
