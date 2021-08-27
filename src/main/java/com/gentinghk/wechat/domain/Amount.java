package com.gentinghk.wechat.domain;

import lombok.*;

import java.math.BigDecimal;

/**
 * @Description TODO
 * @Author Kim
 * @Date 2021/8/24 12:01
 */
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class Amount {
    /**
     * 订单总金额,只能为整数
     */
    private BigDecimal total;
    /**
     * 货币类型,只支持HKD
     */
    private String Currency;
}
