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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import pl.kzochowski.youtubecrawler.integration.DocumentMixin;
import pl.kzochowski.youtubecrawler.integration.model.Document;

import java.io.IOException;
import java.time.YearMonth;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class ElasticsearchSenderService {
    private final RestHighLevelClient client;
    private final ObjectMapper mapper = new ObjectMapper().addMixIn(Document.class, DocumentMixin.class);
    private BulkRequest bulkRequest = new BulkRequest().setRefreshPolicy(WriteRequest.RefreshPolicy.IMMEDIATE);

    @Value("${index-prefix:doc}")
    private String indexPrefix;

    public void sendList(List<Document> documents) throws IOException {
        documents.forEach(document -> {
            bulkRequest.add(new IndexRequest(index(document)).id(generateId(document)).source(mapper.convertValue(document, Map.class)));
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
        YearMonth yearMonth = YearMonth.from(document.getPublishDate());
        return indexPrefix + yearMonth.toString();
    }
}
