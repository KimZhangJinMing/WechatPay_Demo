package com.gentinghk.wechat.util;

import com.fasterxml.jackson.core.type.TypeReference;
import com.gentinghk.wechat.domain.WechatCertificateVo;
import com.gentinghk.wechat.domain.WechatPayGlobalConstant;
import com.gentinghk.wechat.domain.WechatPayResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.CollectionUtils;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Description TODO
 * @Author Kim
 * @Date 2021/8/26 15:37
 */
@Slf4j
public class ResponseUtil {

    /**
     * 获取微信平台证书
     * @return
     */
    public static Map<String, X509Certificate> refreshCertificate() {
        WechatPayResponse wechatPayResponse = RequestUtil.httpGet(WechatPayGlobalConstant.wechatCertificatesUrl);
        if(wechatPayResponse == null || wechatPayResponse.getBody() == null ) {
            return null;
        }
        // 获取微信证书列表
        Map<String, List<WechatCertificateVo>> certificatesMap = JsonUtil.stringToObject(wechatPayResponse.getBody(), new TypeReference<Map<String, List<WechatCertificateVo>>>() {});
        if(CollectionUtils.isEmpty(certificatesMap)) {
            log.warn("get WechatCertificate return null...");
            return null;
        }

        List<WechatCertificateVo> certificateVoList = certificatesMap.get("data");
        // 获取最新的证书
        WechatCertificateVo latestCertificateVo = certificateVoList.stream()
                .filter(Objects::nonNull)
                .sorted(Comparator.comparing(WechatCertificateVo::getEffectiveTime).reversed())
                .limit(1)
                .findFirst()
                .orElse(null);

        // 使用apiV3对微信平台证书进行解密
        WechatCertificateVo.EncryptCertificate encryptCertificate = latestCertificateVo.getEncryptCertificate();
        String publicKey = AESUtil.decryptResponseBody(encryptCertificate.getAssociatedData(), encryptCertificate.getNonce(),
                encryptCertificate.getCiphertext());

        X509Certificate certificate = null;
        try {
            CertificateFactory factory = CertificateFactory.getInstance("X509");
            // 产生证书
            certificate = (X509Certificate)factory.generateCertificate(new ByteArrayInputStream(publicKey.getBytes
                    (StandardCharsets.UTF_8)));
        } catch (CertificateException e) {
            e.printStackTrace();
        }
        // 保存证书
        ConcurrentHashMap<String, X509Certificate> certificateMap = new ConcurrentHashMap<>();
        certificateMap.put(latestCertificateVo.getSerialNo(),certificate);
        return certificateMap;
    }


}
