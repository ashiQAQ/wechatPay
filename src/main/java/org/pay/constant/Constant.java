package org.pay.constant;

public class Constant {
    private static  final String  appId = "appid";
    private static  final String  mchId = "商户号";
    /**
    生成sign时要用
     */
    private static  final String  paySignKey = "商户平台 账户中心-API安全 里申请的API key";
    /**
     支付结果的回调地址
     */
    private static  final String  NotifyUrl = "www.baidu.com";

    public static String getAppId() {
        return appId;
    }

    public static String getMchId() {
        return mchId;
    }
    public static String getPaySignKey() {
        return paySignKey;
    }

    public static String getNotifyUrl() {
        return NotifyUrl;
    }
}
