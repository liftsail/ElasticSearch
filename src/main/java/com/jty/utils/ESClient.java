package com.jty.utils;

import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;

/**
 * @Author zf
 * @Description
 * @Date 2021/10/18
 */
public class ESClient {
    public static RestHighLevelClient getClient() {
        //创建httphost对象
        HttpHost httpHost = new HttpHost("39.105.38.132", 9200);
        //创建RestClientBuilder
        RestClientBuilder clientBuilder = RestClient.builder(httpHost);
        // 创建RestHighLevelClient
        RestHighLevelClient client = new RestHighLevelClient(clientBuilder);

        //返回client
        return client;
    }
}
