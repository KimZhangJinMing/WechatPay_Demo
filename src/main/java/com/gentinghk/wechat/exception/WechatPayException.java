package com.gentinghk.wechat.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @Description TODO
 * @Author Kim
 * @Date 2021/8/25 17:22
 */
@Getter
@Setter
public class WechatPayException extends RuntimeException {

    private Integer code;
    private String message;

    WechatPayException(String message) {
        this.code = 500;
        this.message = message;
    }
}
