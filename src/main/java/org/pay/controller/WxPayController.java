package org.pay.controller;
import com.github.wxpay.sdk.WXPay;
import com.github.wxpay.sdk.WXPayUtil;
import org.pay.constant.Constant;
import org.pay.param.WxPayParam;
import org.pay.util.DateUtils;
import org.pay.util.WxPayConfig;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.HashMap;
import java.util.Map;


@RestController
@RequestMapping("/wechat")
public class WxPayController {
    @GetMapping("/pay")
    public Map<String,String> wxPayFunction() throws Exception{

        //支付相关的 金额之类的 先写死
        WxPayParam wxpayParam = new WxPayParam();
        //https的，接收真正的支付结果的回调接口 暂时以baidu代替。
        String notifyUrl = Constant.getNotifyUrl();

        WxPayConfig wxPayConfig = new WxPayConfig();
        WXPay wxPay = new WXPay(wxPayConfig);

        //根据微信支付api来设置
        Map<String,String> data = new HashMap<>();
        data.put("appid",wxPayConfig.getAppID());
        //商户号
        data.put("mch_id",wxPayConfig.getMchID());
        //支付场景 APP 微信app支付 JSAPI 公众号支付  NATIVE 扫码支付
            //APP支付
            data.put("trade_type","APP");
            //NATIVE支付
            data.put("trade_type","NATIVE");
            //H5支付
            data.put("trade_type","MWEB");
            //付款码支付 付款码支付有单独的支付接口 无需上传
            data.put("trade_type","MICROPAY");
            //JSAPI支付（或小程序支付）
            data.put("trade_type","JSAPI");
        //回调地址
        data.put("notify_url",notifyUrl);
        //终端ip
        data.put("spbill_create_ip","127.0.0.1");
        //订单总金额
        data.put("total_fee",wxpayParam.getTotalFee());
        //默认人民币
        data.put("fee_type","CNY");
        //交易号
        data.put("out_trade_no",wxpayParam.getOutTradeNo());
        data.put("body",wxpayParam.getBody());
        // 随机字符串小于32位
        data.put("nonce_str","bfrhncjkfdkfd");
        //签名
        String sign = WXPayUtil.generateSignature(data, wxPayConfig.getKey());
        data.put("sign",sign);

        /** wxPay.unifiedOrder 这个方法中调用微信统一下单接口 */
        //生产环境是 https://api.mch.weixin.qq.com/pay/unifiedorder
        //沙盒测试是 https://api.mch.weixin.qq.com/sandboxnew/pay/unifiedorder
        Map<String, String> respData = wxPay.unifiedOrder(data);
        Map<String,String> reuslt = new HashMap<>();
        if (respData.get("return_code").equals("SUCCESS")){
            //不同的支付类型返回的值是不一样的
            switch(data.get("trade_type")){
                case "APP":appTrade(wxPay,wxPayConfig,respData,reuslt);break;
                case "JSAPI":jsapiTrade(wxPayConfig,respData,reuslt);break;
                case "MWEB":System.out.println("处理H5调用支付的返回值");break;
                case "NATIVE":nativeTrade(wxPayConfig,respData,reuslt);break;
            }
            return reuslt;
        }
        throw new Exception(respData.get("return_msg"));
    }
    public void appTrade( WXPay wxPay,WxPayConfig wxPayConfig, Map<String, String> respData, Map<String, String> reuslt) throws Exception {
        //返回给APP端的参数，APP端再调起支付接口
        reuslt.put("appid",wxPayConfig.getAppID());
        reuslt.put("mch_id",wxPayConfig.getMchID());
        reuslt.put("prepayid",respData.get("prepay_id"));
        reuslt.put("package","WXPay");
        reuslt.put("noncestr",respData.get("nonce_str"));
        reuslt.put("timestamp",String.valueOf(System.currentTimeMillis()/1000));
        String signRes = WXPayUtil.generateSignature(reuslt,wxPayConfig.getKey());
        respData.put("sign",signRes);
        respData.put("timestamp",reuslt.get("timestamp"));
        respData.put("package","WXPay");
    }
    /*** 先生成paySign 参考https://pay.weixin.qq.com/wiki/doc/api/wxa/wxa_api.php?chapter=7_7&index=5 */
    public void jsapiTrade(WxPayConfig wxPayConfig, Map<String, String> respData, Map<String, String> reuslt){
        String prepay_id =respData.get("prepay_id");
        reuslt.put("appId", wxPayConfig.getAppID());
        reuslt.put("timeStamp", DateUtils.timeToStr(System.currentTimeMillis() / 1000, DateUtils.DATE_TIME_PATTERN));
        reuslt.put("nonceStr", respData.get("nonce_str"));
        reuslt.put("package", "prepay_id=" + prepay_id);
        reuslt.put("signType", "MD5");
        String paySignKey = Constant.getPaySignKey();
        reuslt.put("paySign", paySignKey);
        //返回之后 小程序 前端调用 wx.requetPayment 就能跳出支付界面了(开发工具跳出的是二维码)
        /**
         wx.requestPayment({
         'timeStamp': data.timeStamp.toString(),
         'nonceStr': data.nonceStr,
         'package': data.package,
         'signType': 'MD5',
         'paySign': data.sign,
         'success': function (res) {
             console.log('支付成功');
         },
         'fail': function (res) {
             console.log('支付失败');
             return;
         },
         'complete': function (res) {
             console.log('支付完成');
             var url = that.data.url;
             console.log('get url', url)
             if (res.errMsg == 'requestPayment:ok') {
             wx.showModal({
                 title: '提示',
                 content: '充值成功'
             });
         if (url) {
         setTimeout(function () {
             wx.redirectTo({
             url: '/pages' + url
             });
             }, 2000)
         } else {
             setTimeout(() => {
             wx.navigateBack()
             }, 2000)
         }
         }
              return;
         }
         });
         * */
    }
    public void nativeTrade(WxPayConfig wxPayConfig, Map<String, String> respData, Map<String, String> reuslt){
        System.out.println("trade_type=NATIVE时:resp.get(\"code_url\") 用来生成二维码");
    }
}




//
//         //
//         }

//        }