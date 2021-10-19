package com.jty.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
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
@AllArgsConstructor
@NoArgsConstructor
public class SmsLogs implements Serializable {
    private Date createDate;
    private Date sendDate;
    private String longCode;
    private String mobile;
    private String corpName;
    private String smsContent;
    private Integer state;
    private Integer operatorId;
    private String province;
    private String ipAddr;
    private Integer replyTotal;
    private Integer fee;
}
