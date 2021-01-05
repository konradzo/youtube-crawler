package pl.kzochowski.youtubecrawler.integration;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.stereotype.Component;
import pl.kzochowski.youtubecrawler.integration.model.Document;
import pl.kzochowski.youtubecrawler.integration.service.ElasticsearchSenderService;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class ElasticChannel implements MessageChannel {

    private final ElasticsearchSenderService elasticsearchSender;

    @Override
    public boolean send(Message<?> message, long l) {
        Object payload = message.getPayload();
        if (payload instanceof List){
            try{
                elasticsearchSender.sendList((List<Document>)payload);
            }catch (Exception e){
                e.printStackTrace();
                log.error("Error sending documents to elasticsearch!");
                return false;
            }
        }
        return true;
    }

}
