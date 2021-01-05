package pl.kzochowski.youtubecrawler.integration.service;

import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.support.WriteRequest;
import org.springframework.stereotype.Service;
import pl.kzochowski.youtubecrawler.integration.model.Document;

import java.util.List;

@Service
public class ElasticsearchSenderService {

    private BulkRequest bulkRequest = new BulkRequest().setRefreshPolicy(WriteRequest.RefreshPolicy.IMMEDIATE);

    public void sendList(List<Document> documents){
        //todo create indexRequest for all documents
    }
}
