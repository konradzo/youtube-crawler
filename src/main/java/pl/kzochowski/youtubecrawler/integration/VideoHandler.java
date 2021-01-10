package pl.kzochowski.youtubecrawler.integration;

import lombok.RequiredArgsConstructor;
import org.springframework.integration.handler.GenericHandler;
import org.springframework.messaging.MessageHeaders;
import org.springframework.stereotype.Component;
import pl.kzochowski.youtubecrawler.integration.model.Document;
import pl.kzochowski.youtubecrawler.integration.model.Video;
import pl.kzochowski.youtubecrawler.integration.model.VideoDto;
import pl.kzochowski.youtubecrawler.integration.service.VideoFetcher;
import pl.kzochowski.youtubecrawler.integration.util.ChannelDto;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class VideoHandler implements GenericHandler<ChannelDto> {

    private final VideoFetcher videoFetcher;

    @Override
    public Object handle(ChannelDto channelDto, MessageHeaders messageHeaders) {
        List<VideoDto> videos = videoFetcher.fetchChannelVideos(channelDto.getChannel());
//        List<Document> documents = videos.stream().map(this::convertVideoToDocument).collect(Collectors.toList());
//        channelDto.setDocuments(documents);
        return channelDto;
    }

    private Document convertVideoToDocument(Video video) {
        Document document = new Document();
        //todo
        return document;
    }
}
