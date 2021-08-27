package com.gentinghk.wechat.domain;

import lombok.*;

/**
 * @Description TODO
 * @Author Kim
 * @Date 2021/8/24 12:03
 */
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class StoreInfo {
    private String id;
    private String name;
    private String address;
}
