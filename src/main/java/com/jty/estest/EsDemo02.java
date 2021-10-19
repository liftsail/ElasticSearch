package com.jty.estest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jty.bean.SmsLogs;
import com.jty.utils.ESClient;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.junit.Test;

import java.io.IOException;
import java.util.Date;

/**
 * @Author zf
 * @Description
 * @Date 2021/10/18
 */
public class EsDemo02 {
    //成员变量
    ObjectMapper mapper = new ObjectMapper();
    private RestHighLevelClient client = ESClient.getClient();
    private String index = "sms-logs-index";
    private String type = "sms-logs-type";

    //添加一条数据
    @Test
    public void createDoc() throws IOException {
        //准备数据
        SmsLogs smsLogs = new SmsLogs(new Date(), new Date(), "201512099", "18735427899", "大少爷公司", "注册成功", 0, 1, "山西省", "10.70.20.1", 60, 99);
        String logs = mapper.writeValueAsString(smsLogs);

        //创建执行对象
        IndexRequest indexRequest = new IndexRequest(index, type, "1");
        indexRequest.source(logs, XContentType.JSON);

        //发送数据
        IndexResponse response = client.index(indexRequest, RequestOptions.DEFAULT);

        //打印结果
        System.out.println(response.status());
    }
}
