package com.gentinghk.wechat.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

/**
 * @Description TODO
 * @Author Kim
 * @Date 2021/8/25 15:56
 */
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class WechatPayResponse {

    private String code;
    private String message;
    private Detail detail;
    private String body;
}

@Getter
@Setter
class Detail{
    private String field;
    private String value;
    private String issue;
    private String location;
}
