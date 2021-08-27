package com.gentinghk.wechat.domain;

import lombok.Getter;
import lombok.Setter;

/**
 * @Description TODO
 * @Author Kim
 * @Date 2021/8/27 14:37
 */
@Getter
@Setter
public class NotifyResponse {

    private String code;
    private String message;

    public static NotifyResponse success() {
        NotifyResponse response = new NotifyResponse();
        response.setCode("SUCCESS");
        response.setMessage("成功");
        return response;
    }

    public static NotifyResponse error() {
        NotifyResponse response = new NotifyResponse();
        response.setCode("SYSTEM_ERROR");
        response.setMessage("系统失败");
        return response;
    }
}
