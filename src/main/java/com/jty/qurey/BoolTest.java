package com.jty.qurey;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jty.utils.ESClient;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.BoostingQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.metrics.cardinality.Cardinality;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.junit.Test;

import java.io.IOException;

/**
 * @Author 张帆
 * @Description 不积跬步无以至千里
 * @Date 2021/10/19 20:13
 */
public class BoolTest {

    //成员变量
    ObjectMapper mapper = new ObjectMapper();
    private RestHighLevelClient client = ESClient.getClient();
    private String index = "sms-logs-index";
    private String type = "sms-logs-type";

    // Java代码实现Bool查询
    @Test
    public void BoolQuery() throws IOException {
        //1. 创建SearchRequest
        SearchRequest request = new SearchRequest(index);
        request.types(type);

        //2. 指定查询条件
        SearchSourceBuilder builder = new SearchSourceBuilder();

        BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();
        // # 查询省份为武汉或者北京
        boolQuery.should(QueryBuilders.termQuery("province", "武汉"));
        boolQuery.should(QueryBuilders.termQuery("province", "北京"));
        // # 运营商不是联通
        boolQuery.mustNot(QueryBuilders.termQuery("operatorId", 2));
        // # smsContent中包含中国和平安
        boolQuery.must(QueryBuilders.matchQuery("smsContent", "中国"));
        boolQuery.must(QueryBuilders.matchQuery("smsContent", "平安"));

        builder.query(boolQuery);
        request.source(builder);

        //3. 执行查询
        SearchResponse resp = client.search(request, RequestOptions.DEFAULT);

        //4. 输出结果
        for (SearchHit hit : resp.getHits().getHits()) {
            System.out.println(hit.getSourceAsMap());
        }
    }

    // Java实现Boosting查询
    @Test
    public void BoostingQuery() throws IOException {
        //1. 创建SearchRequest
        SearchRequest request = new SearchRequest(index);
        request.types(type);

        //2. 指定查询条件
        SearchSourceBuilder builder = new SearchSourceBuilder();
        BoostingQueryBuilder boostingQuery = QueryBuilders.boostingQuery(
                QueryBuilders.matchQuery("smsContent", "收货安装"),
                QueryBuilders.matchQuery("smsContent", "王五")
        ).negativeBoost(0.5f);

        builder.query(boostingQuery);
        request.source(builder);

        //3. 执行查询
        SearchResponse resp = client.search(request, RequestOptions.DEFAULT);

        //4. 输出结果
        for (SearchHit hit : resp.getHits().getHits()) {
            System.out.println(hit.getSourceAsMap());
        }
    }

    // Java实现filter操作
    @Test
    public void filter() throws IOException {
        //1. SearchRequest
        SearchRequest request = new SearchRequest(index);
        request.types(type);

        //2. 查询条件
        SearchSourceBuilder builder = new SearchSourceBuilder();
        BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();
        boolQuery.filter(QueryBuilders.termQuery("corpName", "盒马鲜生"));
        boolQuery.filter(QueryBuilders.rangeQuery("fee").lte(5));

        builder.query(boolQuery);
        request.source(builder);

        //3. 执行查询
        SearchResponse resp = client.search(request, RequestOptions.DEFAULT);

        //4. 输出结果
        for (SearchHit hit : resp.getHits().getHits()) {
            System.out.println(hit.getSourceAsMap());
        }
    }

    // Java实现高亮查询
    @Test
    public void highLightQuery() throws IOException {
        //1. SearchRequest
        SearchRequest request = new SearchRequest(index);
        request.types(type);

        //2. 指定查询条件（高亮）
        SearchSourceBuilder builder = new SearchSourceBuilder();
        //2.1 指定查询条件
        builder.query(QueryBuilders.matchQuery("smsContent", "盒马"));
        //2.2 指定高亮
        HighlightBuilder highlightBuilder = new HighlightBuilder();
        highlightBuilder.field("smsContent", 10)
                .preTags("<font color='red'>")
                .postTags("</font>");
        builder.highlighter(highlightBuilder);

        request.source(builder);

        //3. 执行查询
        SearchResponse resp = client.search(request, RequestOptions.DEFAULT);

        //4. 获取高亮数据，输出
        for (SearchHit hit : resp.getHits().getHits()) {
            System.out.println(hit.getHighlightFields().get("smsContent"));
        }
    }

    //  Java代码实现去重计数查询
    @Test
    public void cardinality() throws IOException {
        //1. 创建SearchRequest
        SearchRequest request = new SearchRequest(index);
        request.types(type);

        //2. 指定使用的聚合查询方式
        SearchSourceBuilder builder = new SearchSourceBuilder();
        builder.aggregation(AggregationBuilders.cardinality("agg").field("province"));

        request.source(builder);

        //3. 执行查询
        SearchResponse resp = client.search(request, RequestOptions.DEFAULT);

        //4. 获取返回结果
        Cardinality agg = resp.getAggregations().get("agg");
        long value = agg.getValue();
        System.out.println(value);
    }
}
