package com.gentinghk.wechat.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.List;

/**
 * @Description IP操作工具类
 * @Author Kim
 * @Date 2021/8/24 11:11
 */

public class IPUtil {

    private final static Logger log = LoggerFactory.getLogger(IPUtil.class);
    private final static String UNKNOWN = "unknown";
    // 按这个顺序从header中获取ip
    private final static List<String> headerList =Arrays.asList("x-forwarded-for","Proxy-Client-IP","WL-Proxy-Client-IP");

    public static String getClientIp() {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder
                .currentRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        String ip = null;
        for (String header : headerList) {
            ip = request.getHeader(header);
            log.info("getClientIp({}) : {}",header,ip);
            if(StringUtils.hasLength(ip) && !UNKNOWN.equalsIgnoreCase(ip)) {
                break;
            }
        }
        if(!StringUtils.hasLength(ip)) {
            ip = request.getRemoteAddr();
            log.info("getClientIp(getRemoteAddr) : {}",ip);
        }
        // 多个路由时,取第一个非unknown的ip
        String[] ips = ip.split(",");
        ip = Arrays.stream(ips)
                .filter(item -> !UNKNOWN.equalsIgnoreCase(item))
                .findFirst()
                .orElse(null);

        return ip;
    }
}
