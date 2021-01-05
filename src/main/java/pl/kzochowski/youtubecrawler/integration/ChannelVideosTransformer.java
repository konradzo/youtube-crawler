package pl.kzochowski.youtubecrawler.integration;

import lombok.RequiredArgsConstructor;
import org.springframework.integration.transformer.AbstractPayloadTransformer;
import org.springframework.stereotype.Component;
import pl.kzochowski.youtubecrawler.integration.model.Document;
import pl.kzochowski.youtubecrawler.integration.model.Video;
import pl.kzochowski.youtubecrawler.integration.service.VideoFetcher;
import pl.kzochowski.youtubecrawler.integration.util.ChannelDto;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class ChannelVideosTransformer extends AbstractPayloadTransformer<ChannelDto, List<Document>> {

    private final VideoFetcher videoFetcher;

    @Override
    protected List<Document> transformPayload(ChannelDto channelDto) {
        List<Video> videos = videoFetcher.fetchChannelVideos(channelDto.getChannel());
        List<Document> documents = videos.stream().map(this::convertVideoToDocument).collect(Collectors.toList());
        return null;
    }

    private Document convertVideoToDocument(Video video) {
        Document document = new Document();
        //todo
        return document;
    }
}
