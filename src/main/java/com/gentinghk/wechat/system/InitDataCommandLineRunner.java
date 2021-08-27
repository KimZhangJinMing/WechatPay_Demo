package com.gentinghk.wechat.system;

import com.gentinghk.wechat.domain.WechatPayGlobalConstant;
import com.gentinghk.wechat.util.ResponseUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * @Description TODO
 * @Author Kim
 * @Date 2021/8/25 11:51
 */
@Component
@Slf4j
public class InitDataCommandLineRunner implements CommandLineRunner {

    @Value("${wechatPay.global.privateKey}")
    private String privateKey;
    @Value("${wechatPay.global.appId}")
    private String appId;
    @Value("${wechatPay.global.apiV3Key}")
    private String apiV3Key;
    @Value("${wechatPay.global.mchId}")
    private String mchId;
    @Value("${wechatPay.global.mchSerialNo}")
    private String mchSerialNo;
    @Value("${wechatPay.global.currency}")
    private String currency;
    @Value("${wechatPay.global.wechatCertificatesUrl}")
    private String wechatCertificatesUrl;

    @Override
    public void run(String... args) throws Exception {
        WechatPayGlobalConstant.appId = appId;
        WechatPayGlobalConstant.apiV3Key = apiV3Key;
        WechatPayGlobalConstant.mchId = mchId;
        WechatPayGlobalConstant.mchSerialNo = mchSerialNo;
        WechatPayGlobalConstant.currency = currency;
        WechatPayGlobalConstant.privateKey = privateKey;
        WechatPayGlobalConstant.wechatCertificatesUrl = wechatCertificatesUrl;
        WechatPayGlobalConstant.certificateMap = ResponseUtil.refreshCertificate();
    }
}
