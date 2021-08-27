package com.gentinghk.wechat.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @Description TODO
 * @Author Kim
 * @Date 2021/8/26 10:58
 */
@Getter
@Setter
@ToString
public class PayNotifyVO {
    private String id;
    private String createTime;
    private String eventType;
    private String resourceType;
    private Resource resource;
    private String summary;

    @Getter
    @Setter
    @ToString
    public class Resource {
        /**
         * 加密算法类型
         */
        private String algorithm;
        /**
         * 数据密文
         */
        private String ciphertext;
        /**
         * 附加数据
         */
        private String associatedData;
        /**
         * 原始类型
         */
        private String originalType;
        /**
         * 随机串
         */
        private String nonce;
    }
}

