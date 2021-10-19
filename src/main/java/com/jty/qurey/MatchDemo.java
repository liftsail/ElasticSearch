package com.jty.qurey;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jty.utils.ESClient;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.Operator;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.reindex.BulkByScrollResponse;
import org.elasticsearch.index.reindex.DeleteByQueryRequest;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.junit.Test;

import java.io.IOException;

/**
 * @Author 张帆
 * @Description 不积跬步无以至千里
 * @Date 2021/10/19 16:15
 */
public class MatchDemo {
    //成员变量
    ObjectMapper mapper = new ObjectMapper();
    private RestHighLevelClient client = ESClient.getClient();
    private String index = "sms-logs-index";
    private String type = "sms-logs-type";

    @Test
    public void matchAll() throws IOException {
        SearchRequest request = new SearchRequest(index);
        request.types(type);

        SearchSourceBuilder builder = new SearchSourceBuilder();
        builder.query(QueryBuilders.matchAllQuery());
        builder.size(20);
        request.source(builder);

        SearchResponse response = client.search(request, RequestOptions.DEFAULT);
        for (SearchHit hit : response.getHits().getHits()) {
            System.out.println(hit.getSourceAsMap());
        }
    }

    @Test
    public void match() throws IOException {
        SearchRequest request = new SearchRequest(index);
        request.types(type);

        SearchSourceBuilder builder = new SearchSourceBuilder();
        builder.query(QueryBuilders.matchQuery("province", "山西"));
        builder.size(20);
        request.source(builder);

        SearchResponse response = client.search(request, RequestOptions.DEFAULT);
        for (SearchHit hit : response.getHits().getHits()) {
            System.out.println(hit.getSourceAsMap());
        }
    }

    @Test
    public void matchBool() throws IOException {
        SearchRequest request = new SearchRequest(index);
        request.types(type);

        SearchSourceBuilder builder = new SearchSourceBuilder();
        //builder.query(QueryBuilders.matchQuery("smsContent", "健康 中国").operator(Operator.OR));
        builder.query(QueryBuilders.matchQuery("smsContent", "健康 中国").operator(Operator.AND));
        builder.size(20);
        request.source(builder);

        SearchResponse response = client.search(request, RequestOptions.DEFAULT);
        for (SearchHit hit : response.getHits().getHits()) {
            System.out.println(hit.getSourceAsMap());
        }
    }

    @Test
    public void multiMatch() throws IOException {
        SearchRequest request = new SearchRequest(index);
        request.types(type);

        SearchSourceBuilder builder = new SearchSourceBuilder();
        builder.query(QueryBuilders.multiMatchQuery("北京", "province", "smsContent"));
        builder.size(20);
        request.source(builder);

        SearchResponse response = client.search(request, RequestOptions.DEFAULT);
        for (SearchHit hit : response.getHits().getHits()) {
            System.out.println(hit.getSourceAsMap());
        }
    }

    @Test
    public void findById() throws IOException {
        GetRequest getRequest = new GetRequest(index, type, "21");

        GetResponse response = client.get(getRequest, RequestOptions.DEFAULT);

        System.out.println(response.getSourceAsMap());
    }

    @Test
    public void findByIds() throws IOException {
        SearchRequest request = new SearchRequest(index);
        request.types(type);

        SearchSourceBuilder builder = new SearchSourceBuilder();
        builder.query(QueryBuilders.idsQuery().addIds("21", "22"));
        request.source(builder);

        SearchResponse response = client.search(request, RequestOptions.DEFAULT);

        for (SearchHit hit : response.getHits().getHits()) {
            System.out.println(hit.getSourceAsMap());
        }
    }

    @Test
    public void findByPrefix() throws IOException {
        //1. 创建SearchRequest
        SearchRequest request = new SearchRequest(index);
        request.types(type);

        //2. 指定查询条件
        SearchSourceBuilder builder = new SearchSourceBuilder();
        //----------------------------------------------------------
        builder.query(QueryBuilders.prefixQuery("corpName", "盒马"));
        //----------------------------------------------------------
        request.source(builder);

        //3. 执行
        SearchResponse resp = client.search(request, RequestOptions.DEFAULT);

        //4. 输出结果
        for (SearchHit hit : resp.getHits().getHits()) {
            System.out.println(hit.getSourceAsMap());
        }
    }

    // Java代码实现Fuzzy查询
    @Test
    public void findByFuzzy() throws IOException {
        //1. 创建SearchRequest
        SearchRequest request = new SearchRequest(index);
        request.types(type);

        //2. 指定查询条件
        SearchSourceBuilder builder = new SearchSourceBuilder();
        //----------------------------------------------------------
        builder.query(QueryBuilders.fuzzyQuery("corpName", "盒马先生").prefixLength(2));
        //----------------------------------------------------------
        request.source(builder);

        //3. 执行
        SearchResponse resp = client.search(request, RequestOptions.DEFAULT);

        //4. 输出结果
        for (SearchHit hit : resp.getHits().getHits()) {
            System.out.println(hit.getSourceAsMap());
        }
    }

    // Java代码实现Wildcard查询
    @Test
    public void findByWildCard() throws IOException {
        //1. 创建SearchRequest
        SearchRequest request = new SearchRequest(index);
        request.types(type);

        //2. 指定查询条件
        SearchSourceBuilder builder = new SearchSourceBuilder();
        //----------------------------------------------------------
        builder.query(QueryBuilders.wildcardQuery("corpName", "中国*"));
        //----------------------------------------------------------
        request.source(builder);

        //3. 执行
        SearchResponse resp = client.search(request, RequestOptions.DEFAULT);

        //4. 输出结果
        for (SearchHit hit : resp.getHits().getHits()) {
            System.out.println(hit.getSourceAsMap());
        }
    }

    // Java实现range范围查询
    @Test
    public void findByRange() throws IOException {
        //1. 创建SearchRequest
        SearchRequest request = new SearchRequest(index);
        request.types(type);

        //2. 指定查询条件
        SearchSourceBuilder builder = new SearchSourceBuilder();
        //----------------------------------------------------------
        builder.query(QueryBuilders.rangeQuery("fee").lte(10).gte(5));
        //----------------------------------------------------------
        request.source(builder);

        //3. 执行
        SearchResponse resp = client.search(request, RequestOptions.DEFAULT);

        //4. 输出结果
        for (SearchHit hit : resp.getHits().getHits()) {
            System.out.println(hit.getSourceAsMap());
        }
    }

    // Java代码实现正则查询
    @Test
    public void findByRegexp() throws IOException {
        //1. 创建SearchRequest
        SearchRequest request = new SearchRequest(index);
        request.types(type);

        //2. 指定查询条件
        SearchSourceBuilder builder = new SearchSourceBuilder();
        //----------------------------------------------------------
        builder.query(QueryBuilders.regexpQuery("mobile", "139[0-9]{8}"));
        //----------------------------------------------------------
        request.source(builder);

        //3. 执行
        SearchResponse resp = client.search(request, RequestOptions.DEFAULT);

        //4. 输出结果
        for (SearchHit hit : resp.getHits().getHits()) {
            System.out.println(hit.getSourceAsMap());
        }
    }

    // Java代码实现deleteByQuery
    @Test
    public void deleteByQuery() throws IOException {
        //1. 创建DeleteByQueryRequest
        DeleteByQueryRequest request = new DeleteByQueryRequest(index);
        request.types(type);

        //2. 指定检索的条件    和SearchRequest指定Query的方式不一样
        request.setQuery(QueryBuilders.rangeQuery("fee").lt(4));

        //3. 执行删除
        BulkByScrollResponse resp = client.deleteByQuery(request, RequestOptions.DEFAULT);

        //4. 输出返回结果
        System.out.println(resp.toString());

    }
}
