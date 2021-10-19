package com.jty.estest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jty.bean.Person;
import com.jty.utils.ESClient;
import org.elasticsearch.action.admin.indices.create.CreateIndexRequest;
import org.elasticsearch.action.admin.indices.create.CreateIndexResponse;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.admin.indices.get.GetIndexRequest;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.support.master.AcknowledgedResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.common.xcontent.json.JsonXContent;
import org.junit.Test;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author zf
 * @Description
 * @Date 2021/10/18
 */
public class EsDemo01 {

    //成员变量
    ObjectMapper mapper = new ObjectMapper();
    private RestHighLevelClient client = ESClient.getClient();
    private String index = "person";
    private String type = "man";

    @Test
    //创建es索引
    public void createIndex() throws IOException {
        //1. 准备关于索引的settings
        Settings.Builder settings = Settings.builder()
                .put("number_of_shards", 5)
                .put("number_of_replicas", 1);

        //2. 准备关于索引的结构mappings
        XContentBuilder mappings = JsonXContent.contentBuilder()
                .startObject()
                .startObject("properties")
                .startObject("name")
                .field("type", "text")
                .endObject()
                .startObject("age")
                .field("type", "integer")
                .endObject()
                .startObject("birthday")
                .field("type", "date")
                .field("format", "yyyy-MM-dd||epoch_millis")
                .endObject()
                .endObject()
                .endObject();

        //3. 将settings和mappings封装到一个Request对象
        CreateIndexRequest indexRequest = new CreateIndexRequest(index)
                .settings(settings)
                .mapping(type, mappings);

        //4. 通过client对象去连接ES并执行创建索引
        CreateIndexResponse resp = client.indices().create(indexRequest, RequestOptions.DEFAULT);

        //5. 输出
        System.out.println("resp:" + resp.toString());
    }

    @Test
    //查看索引是否存在
    public void indexExists() throws IOException {
        //1. 准备request对象
        GetIndexRequest request = new GetIndexRequest();
        request.indices(index);

        //2. 通过client去操作
        boolean exists = client.indices().exists(request, RequestOptions.DEFAULT);

        //3. 输出
        System.out.println(exists);
    }

    //删除某个索引
    @Test
    public void deleteIndex() throws IOException {
        //1. 准备request对象
        DeleteIndexRequest request = new DeleteIndexRequest();
        request.indices(index);

        //2. 通过client对象执行
        AcknowledgedResponse delete = client.indices().delete(request, RequestOptions.DEFAULT);

        //3. 获取返回结果
        System.out.println(delete.isAcknowledged());
    }

    //添加一条记录
    @Test
    public void createDoc() throws IOException {
        //准备对象
        Person person1 = new Person(1, "老大", 30, new Date());
        String s1 = mapper.writeValueAsString(person1);

        //准备request对象
        IndexRequest indexRequest = new IndexRequest(index, type, person1.getId().toString());
        indexRequest.source(s1, XContentType.JSON);

        //通过client对象进行数据添加
        IndexResponse response = client.index(indexRequest, RequestOptions.DEFAULT);

        //打印结果
        System.out.println(response.getResult());
    }

    //修改一条记录
    @Test
    public void updateDoc() throws IOException {
        //1. 创建一个Map，指定需要修改的内容
        Map<String, Object> doc = new HashMap<>();
        doc.put("name", "张小三");
        String docId = "1";

        //2. 创建request对象，封装数据
        UpdateRequest request = new UpdateRequest(index, type, docId);
        request.doc(doc);

        //3. 通过client对象执行
        UpdateResponse update = client.update(request, RequestOptions.DEFAULT);

        //4. 输出返回结果
        System.out.println(update.getResult().toString());
    }

    //删除一条记录
    @Test
    public void deleteDoc() throws IOException {
        //1. 封装Request对象
        DeleteRequest request = new DeleteRequest(index, type, "1");

        //2. client执行
        DeleteResponse resp = client.delete(request, RequestOptions.DEFAULT);

        //3. 输出结果
        System.out.println(resp.getResult().toString());
    }

    //批量添加记录
    @Test
    public void bulkCreateDoc() throws IOException {
        //1. 准备多个json数据
        Person p1 = new Person(1, "张三", 23, new Date());
        Person p2 = new Person(2, "李四", 24, new Date());
        Person p3 = new Person(3, "王五", 25, new Date());

        String json1 = mapper.writeValueAsString(p1);
        String json2 = mapper.writeValueAsString(p2);
        String json3 = mapper.writeValueAsString(p3);

        //2. 创建Request，将准备好的数据封装进去
        BulkRequest request = new BulkRequest();
        request.add(
                new IndexRequest(index, type, p1.getId().toString())
                        .source(json1, XContentType.JSON));
        request.add(
                new IndexRequest(index, type, p2.getId().toString())
                        .source(json2, XContentType.JSON));
        request.add(
                new IndexRequest(index, type, p3.getId().toString())
                        .source(json3, XContentType.JSON));

        //3. 用client执行
        BulkResponse resp = client.bulk(request, RequestOptions.DEFAULT);

        //4. 输出结果
        System.out.println(resp.toString());
    }

    //批量删除记录
    @Test
    public void bulkDeleteDoc() throws IOException {
        //1. 封装Request对象
        BulkRequest request = new BulkRequest();
        request.add(new DeleteRequest(index, type, "1"));
        request.add(new DeleteRequest(index, type, "2"));
        request.add(new DeleteRequest(index, type, "3"));

        //2. client执行
        BulkResponse resp = client.bulk(request, RequestOptions.DEFAULT);

        //3. 输出
        System.out.println(resp.status());
    }
}
