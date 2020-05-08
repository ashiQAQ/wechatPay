package org.pay.util;


import com.github.wxpay.sdk.WXPayConfig;
import org.apache.commons.io.IOUtils;
import org.pay.constant.Constant;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

/** 配置自己的信息  */

public class WxPayConfig implements WXPayConfig {

    /** 微信商户平台下载 证书*/
    private byte [] certData;

    public void WxPayConfig() throws  Exception{
        InputStream certStream = Thread.currentThread().getContextClassLoader().getResourceAsStream("cert/certName.p12");
        this.certData = IOUtils.toByteArray(certStream);
        certStream.close();
    }

    /** 设置自己的appid
     * 商户号
     * 秘钥
     * */

    @Override
    public String getAppID() {
        return Constant.getAppId();
    }

    @Override
    public String getMchID() {
        return  Constant.getMchId();
    }

    @Override
    public String getKey() {
        return   Constant.getPaySignKey();
    }

    @Override
    public InputStream getCertStream() {
        return new ByteArrayInputStream(this.certData);
    }

    @Override
    public int getHttpConnectTimeoutMs() {
        return 0;
    }

    @Override
    public int getHttpReadTimeoutMs() {
        return 0;
    }
}