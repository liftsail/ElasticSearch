package com.jty.qurey;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jty.utils.ESClient;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.junit.Test;

import java.io.IOException;

/**
 * @Author 张帆
 * @Description 不积跬步无以至千里
 * @Date 2021/10/19 15:49
 */
public class TermDemo {
    //成员变量
    ObjectMapper mapper = new ObjectMapper();
    private RestHighLevelClient client = ESClient.getClient();
    private String index = "sms-logs-index";
    private String type = "sms-logs-type";

    @Test
    public void termDemo() throws IOException {
        SearchRequest request = new SearchRequest(index);
        request.types(type);

        SearchSourceBuilder builder = new SearchSourceBuilder();
        //builder.from(0);
        builder.size(10);
        builder.query(QueryBuilders.termQuery("province", "北京"));
        request.source(builder);

        SearchResponse response = client.search(request, RequestOptions.DEFAULT);

        System.out.println(response.getHits().getTotalHits());
        for (SearchHit hit : response.getHits().getHits()) {
            System.out.println(hit.getSourceAsMap());
        }
    }

    @Test
    public void
    termsDemo() throws IOException {
        SearchRequest request = new SearchRequest(index);
        request.types(type);

        SearchSourceBuilder builder = new SearchSourceBuilder();
        builder.from(0);
        builder.size(10);
        builder.query(QueryBuilders.termsQuery("province", "山西", "北京"));
        request.source(builder);

        SearchResponse search = client.search(request, RequestOptions.DEFAULT);

        for (SearchHit hit : search.getHits().getHits()) {
            System.out.println(hit.getSourceAsMap());
        }
    }
}
