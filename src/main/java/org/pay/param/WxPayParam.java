package org.pay.param;

import java.math.BigDecimal;
public class WxPayParam {

    /**
     此时的单位是元
     */
    BigDecimal totalPrice  = new BigDecimal(1);

    /**
     * 账户收款时看到的销售内容 长度有限制 string 128个字符 记得拼接判断长度。
     */
    private String body = "商品描述字段 body内容";
    /** 订单总金额 */
    private String totalFee = totalPrice.multiply(new BigDecimal(100)).toBigInteger().toString();
    /** 商户订单号 */
    private String outTradeNo = "21321321321";

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getTotalFee() {
        return totalFee;
    }

    public void setTotalFee(String totalFee) {
        this.totalFee = totalFee;
    }

    public String getOutTradeNo() {
        return outTradeNo;
    }

    public void setOutTradeNo(String outTradeNo) {
        this.outTradeNo = outTradeNo;
    }
}