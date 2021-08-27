package com.gentinghk.wechat.domain;

import lombok.*;

import java.util.List;

/**
 * @Description TODO
 * @Author Kim
 * @Date 2021/8/25 15:38
 */
@Getter
@Setter
public class CommonResponse<T> {

    private Integer code;
    private String message;
    private T data;
    private List<T> dataList;

    public CommonResponse(Integer code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public CommonResponse(Integer code, String message, List<T> dataList) {
        this.code = code;
        this.message = message;
        this.dataList = dataList;
    }

    public CommonResponse(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public CommonResponse(T data) {
        this.code = 200;
        this.message = "操作成功";
        this.data = data;
    }

    public CommonResponse(List<T> dataList) {
        this.code = 200;
        this.message = "操作成功";
        this.dataList = dataList;
    }

}
