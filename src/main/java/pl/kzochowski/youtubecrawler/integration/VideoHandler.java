package pl.kzochowski.youtubecrawler.integration;

import lombok.RequiredArgsConstructor;
import org.springframework.integration.handler.GenericHandler;
import org.springframework.messaging.MessageHeaders;
import org.springframework.stereotype.Component;
import pl.kzochowski.youtubecrawler.integration.model.*;
import pl.kzochowski.youtubecrawler.service.VideoFetcher;
import pl.kzochowski.youtubecrawler.integration.util.ChannelDto;

import java.util.List;

@Component
@RequiredArgsConstructor
public class VideoHandler implements GenericHandler<ChannelDto> {
    private final VideoFetcher videoFetcher;

    @Override
    public Object handle(ChannelDto channelDto, MessageHeaders messageHeaders) {
        List<VideoDto> videos = videoFetcher.fetchChannelVideos(channelDto.getChannel());
        channelDto.setVideoDtos(videos);
        return channelDto;
    }

}
