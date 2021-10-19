package com.jty.bean;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.util.Date;

/**
 * @Author zf
 * @Description
 * @Date 2021/10/18
 */
@Data
@ToString
public class SmsLogs implements Serializable {
    private Date createDate;
    private Date sendDate;
    private long longCode;
    private String mobile;
    private String corpName;
    private String smsContent;
    private byte state;
    private Integer operatorId;
    private String province;
    private String ipAddr;
    private Integer replyTotal;
    private Integer fee;

    public SmsLogs() {
    }

    public SmsLogs(Date createDate, Date sendDate, long longCode, String mobile, String corpName, String smsContent, byte state, Integer operatorId, String province, String ipAddr, Integer replyTotal, Integer fee) {
        this.createDate = createDate;
        this.sendDate = sendDate;
        this.longCode = longCode;
        this.mobile = mobile;
        this.corpName = corpName;
        this.smsContent = smsContent;
        this.state = state;
        this.operatorId = operatorId;
        this.province = province;
        this.ipAddr = ipAddr;
        this.replyTotal = replyTotal;
        this.fee = fee;
    }

}
