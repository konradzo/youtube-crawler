package pl.kzochowski.youtubecrawler.integration.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.support.WriteRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.stereotype.Service;
import pl.kzochowski.youtubecrawler.integration.DocumentMixin;
import pl.kzochowski.youtubecrawler.integration.model.Document;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class ElasticsearchSenderService {

    private final RestHighLevelClient client;

    private final ObjectMapper mapper;

    private BulkRequest bulkRequest;

    public ElasticsearchSenderService(RestHighLevelClient client, ObjectMapper mapper) {
        this.client = client;
        this.bulkRequest = new BulkRequest().setRefreshPolicy(WriteRequest.RefreshPolicy.IMMEDIATE);
        this.mapper = mapper;

        mapper.addMixIn(Document.class, DocumentMixin.class);
    }

    public void sendList(List<Document> documents) throws IOException {
        documents.forEach(document -> {
            bulkRequest.add(new IndexRequest("documents").id(generateId(document)).source(mapper.convertValue(document, Map.class)));
        });
        BulkResponse bulkResponse = client.bulk(bulkRequest, RequestOptions.DEFAULT);
        bulkRequest = new BulkRequest().setRefreshPolicy(WriteRequest.RefreshPolicy.IMMEDIATE);
        if (bulkResponse.hasFailures()) {
            log.error("Some errors occurred! Message: {}", bulkResponse.buildFailureMessage());
        } else {
            log.info("sent {} documents to elasticsearch", bulkResponse.getItems().length);
        }
    }

    private String generateId(Document document) {
        return DigestUtils.md5Hex(document.getUrl());
    }

    private String index(Document document) {
        //todo generating index depends on date
        return "";
    }
}
