package com.gentinghk.wechat.domain;

import lombok.*;

/**
 * @Description 微信平台证书
 * @Author Kim
 * @Date 2021/8/26 14:16
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WechatCertificateVo {

    private String serialNo;
    private String effectiveTime;
    private String expireTime;
    private EncryptCertificate encryptCertificate;

    @Getter
    @Setter
    public class EncryptCertificate {
        private String algorithm;
        private String nonce;
        private String associatedData;
        private String ciphertext;
    }
}
