package com.gentinghk.wechat.util;

import com.gentinghk.wechat.domain.WechatPayGlobalConstant;
import com.gentinghk.wechat.domain.WechatPayResponse;
import com.wechat.pay.contrib.apache.httpclient.WechatPayHttpClientBuilder;
import com.wechat.pay.contrib.apache.httpclient.auth.AutoUpdateCertificatesVerifier;
import com.wechat.pay.contrib.apache.httpclient.auth.PrivateKeySigner;
import com.wechat.pay.contrib.apache.httpclient.auth.WechatPay2Credentials;
import com.wechat.pay.contrib.apache.httpclient.auth.WechatPay2Validator;
import com.wechat.pay.contrib.apache.httpclient.util.PemUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.net.URISyntaxException;
import java.security.PrivateKey;
import java.util.Map;

/**
 * @Description 请求工具
 * @Author Kim
 * @Date 2021/8/25 11:02
 */
@Slf4j
public class RequestUtil {

    public static HttpServletRequest getCurrentRequest() {
        return((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes())
                .getRequest();
    }

    public static String getRequestBody()  {
        HttpServletRequest request = RequestUtil.getCurrentRequest();
        ServletInputStream inputStream = null;
        BufferedReader reader = null;
        StringBuffer sb = new StringBuffer();
        try {
            inputStream = request.getInputStream();
            reader = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            while((line = reader.readLine()) != null) {
                sb.append(line);
            }
        } catch (IOException e) {
            log.error("getResponseBody occur error {}",e);
            e.printStackTrace();
        }finally {
            try {
                if(reader != null) {
                    reader.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }


    public static WechatPayResponse httpGet(String url,Map<String,String> params) {
        log.info("send httpGet, url is {}, params is {}", url, params);
        CloseableHttpClient httpClient = RequestUtil.getHttpClient();
        CloseableHttpResponse response = null;
        try {
            URIBuilder uriBuilder = new URIBuilder(url);
            if(!CollectionUtils.isEmpty(params)){
                params.forEach(uriBuilder::setParameter);
            }
            HttpGet httpGet = new HttpGet(uriBuilder.build());
            httpGet.setHeader("Content-Type","application/json;charset=UTF-8");
            httpGet.setHeader("Accept","application/json");
            response = httpClient.execute(httpGet);
            int statusCode = response.getStatusLine().getStatusCode();
            if(statusCode == 204) {
                log.info("httpPost success! without response body!");
                WechatPayResponse wechatPayResponse = new WechatPayResponse();
                wechatPayResponse.setMessage("操作成功");
                return wechatPayResponse;
            }
            String json = EntityUtils.toString(response.getEntity());
            if(statusCode == 200) {
                log.info("httpPost success! response body is {}", json);
                WechatPayResponse wechatPayResponse = new WechatPayResponse();
                wechatPayResponse.setBody(json);
                return wechatPayResponse;
            } else {
                log.error("httpPost failed, response code is {}, response body is {}",statusCode, json);
                return JsonUtil.stringToObject(json,WechatPayResponse.class);
            }
        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
            log.error("httpPost occur error: {}", e);
            return null;
        } finally {
            try {
                response.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static WechatPayResponse httpGet(String url) {
        return RequestUtil.httpGet(url,null);
        // log.info("send httpGet, url is {}", url);
        // CloseableHttpClient httpClient = RequestUtil.getHttpClient();
        // HttpGet httpGet = new HttpGet(url);
        // httpGet.setHeader("Content-Type","application/json;charset=UTF-8");
        // httpGet.setHeader("Accept","application/json");
        //
        //
        // CloseableHttpResponse response = null;
        // try {
        //     response = httpClient.execute(httpGet);
        //     int statusCode = response.getStatusLine().getStatusCode();
        //     String json = EntityUtils.toString(response.getEntity());
        //     if(statusCode == 200) {
        //         log.info("httpPost success! response body is {}", json);
        //         WechatPayResponse wechatPayResponse = new WechatPayResponse();
        //         wechatPayResponse.setBody(json);
        //         return wechatPayResponse;
        //     }else {
        //         log.error("httpPost failed, response code is {}, response body is {}",statusCode, json);
        //         return JsonUtil.stringToObject(json,WechatPayResponse.class);
        //     }
        // } catch (IOException e) {
        //     e.printStackTrace();
        //     log.error("httpPost occur error: {}", e);
        //     return null;
        // }finally {
        //     try {
        //         response.close();
        //     } catch (IOException e) {
        //         e.printStackTrace();
        //     }
        // }
    }


    public static WechatPayResponse httpPost(String url, String jsonStr) {
        log.info("send httpPost, url is {}, request is {}", url, jsonStr);
        CloseableHttpClient httpClient = RequestUtil.getHttpClient();
        HttpPost httpPost = new HttpPost(url);
        httpPost.setHeader("Content-Type","application/json;charset=UTF-8");
        httpPost.setHeader("Accept","application/json");
        StringEntity entity = new StringEntity(jsonStr, "UTF-8");
        httpPost.setEntity(entity);

        CloseableHttpResponse response = null;
        try {
            response = httpClient.execute(httpPost);
            int statusCode = response.getStatusLine().getStatusCode();
            if(statusCode == 204) {
                log.info("httpPost success! without response body!");
                WechatPayResponse wechatPayResponse = new WechatPayResponse();
                wechatPayResponse.setMessage("操作成功");
                return wechatPayResponse;
            }
            String json = EntityUtils.toString(response.getEntity());
            if(statusCode == 200) {
                log.info("httpPost success! response body is {}", json);
                WechatPayResponse wechatPayResponse = new WechatPayResponse();
                wechatPayResponse.setBody(json);
                return wechatPayResponse;
            } else {
                log.error("httpPost failed, response code is {}, response body is {}",statusCode, json);
                return JsonUtil.stringToObject(json,WechatPayResponse.class);
            }
        } catch (IOException e) {
            e.printStackTrace();
            log.error("httpPost occur error: {}", e);
            return null;
        }finally {
            try {
                response.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    private static CloseableHttpClient getHttpClient() {
        try {
            // 加载商户私钥
            PrivateKey merchantPrivateKey = PemUtil.loadPrivateKey(new ByteArrayInputStream(WechatPayGlobalConstant
                    .privateKey
                    .getBytes("UTF-8")));

            // 加载微信平台证书（mchId：商户号,mchSerialNo：商户证书序列号,apiV3Key：V3密钥）
            AutoUpdateCertificatesVerifier verifier = new AutoUpdateCertificatesVerifier(
                    new WechatPay2Credentials(WechatPayGlobalConstant.mchId, new PrivateKeySigner
                            (WechatPayGlobalConstant.mchSerialNo, merchantPrivateKey)), WechatPayGlobalConstant.apiV3Key
                    .getBytes("UTF-8"));
            return WechatPayHttpClientBuilder.create()
                    .withMerchant(WechatPayGlobalConstant.mchId, WechatPayGlobalConstant.mchSerialNo,
                            merchantPrivateKey)
                    .withValidator(new WechatPay2Validator(verifier))
                    .build();
        } catch (UnsupportedEncodingException e) {
            log.error("create httpClient occur error {}", e);
            e.printStackTrace();
            return null;
        }
    }
}
