package pl.kzochowski.youtubecrawler.integration;

import org.springframework.integration.transformer.AbstractPayloadTransformer;
import org.springframework.stereotype.Component;
import pl.kzochowski.youtubecrawler.integration.model.Document;
import pl.kzochowski.youtubecrawler.integration.util.ChannelDto;

import java.util.List;

@Component
public class ChannelVideosTransformer extends AbstractPayloadTransformer<ChannelDto, List<Document>> {

    @Override
    protected List<Document> transformPayload(ChannelDto channelDto) {
        return null;
    }

}
