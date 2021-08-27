package com.gentinghk.wechat.util;

import com.gentinghk.wechat.domain.WechatPayGlobalConstant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.Base64Utils;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import javax.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.cert.X509Certificate;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @Description TODO
 * @Author Kim
 * @Date 2021/8/26 11:17
 */
@Slf4j
public class AESUtil {
    static final int TAG_LENGTH_BIT = 128;


    /**
     * 使用apiV3Key解密响应体
     * @param associatedData
     * @param nonce
     * @param ciphertext
     * @return
     */
    public static String decryptResponseBody(String associatedData, String nonce, String ciphertext) {
        try {
            Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");

            SecretKeySpec key = new SecretKeySpec(WechatPayGlobalConstant.apiV3Key.getBytes(StandardCharsets.UTF_8), "AES");
            GCMParameterSpec spec = new GCMParameterSpec(TAG_LENGTH_BIT, nonce.getBytes(StandardCharsets.UTF_8));

            cipher.init(Cipher.DECRYPT_MODE, key, spec);
            cipher.updateAAD(associatedData.getBytes(StandardCharsets.UTF_8));
            return new String(cipher.doFinal(Base64.getDecoder().decode(ciphertext)), StandardCharsets.UTF_8);
        } catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
            throw new IllegalStateException(e);
        } catch (InvalidKeyException | InvalidAlgorithmParameterException | BadPaddingException | IllegalBlockSizeException e) {
            throw new IllegalArgumentException(e);
        }
    }

    /**
     * 验证微信签名
     * @param body
     * @return
     */
    public static boolean verifiedSign(String body) {
        log.info("verifiedSign requestBody length is {}, content is {}", body.length(), body);
        if(!StringUtils.hasLength(body)) {
            return false;
        }
        HttpServletRequest request = RequestUtil.getCurrentRequest();
        // 微信平台证书序列号
        String serialNo = request.getHeader("Wechatpay-Serial");
        // 应答随机串
        String nonce = request.getHeader("Wechatpay-Nonce");
        // 应答时间戳
        String timestamp = request.getHeader("Wechatpay-Timestamp");
        // 微信应答签名
        String wechatSign = request.getHeader("Wechatpay-Signature");



        // 构造签名字符串(有顺序的)
        String signStr = Stream.of(timestamp, nonce, body)
                .collect(Collectors.joining("\n","","\n"));
        log.info("verifiedSign signStr length is {},content is {}", signStr.length(), signStr);

        // 当全局证书容器为空 或者 微信返回的证书序列号不在容器中了,应该获取最新的证书
        if (WechatPayGlobalConstant.certificateMap.isEmpty() || !WechatPayGlobalConstant.certificateMap.containsKey(serialNo)) {
            WechatPayGlobalConstant.certificateMap = ResponseUtil.refreshCertificate();
        }
        X509Certificate certificate = WechatPayGlobalConstant.certificateMap.get(serialNo);
        if(certificate == null) {
            log.error("WechatCertificate verify failed...");
            return false;
        }
        try {
            Signature signature = Signature.getInstance("SHA256withRSA");
            signature.initVerify(certificate);
            signature.update(signStr.getBytes());
            boolean result = signature.verify(Base64Utils.decodeFromString(wechatSign));
            if(result) {
                log.info("WechatCertificate verify success!");
            }
            return result;
        } catch (NoSuchAlgorithmException | SignatureException | InvalidKeyException e) {
            e.printStackTrace();
            return false;
        }
    }



}
