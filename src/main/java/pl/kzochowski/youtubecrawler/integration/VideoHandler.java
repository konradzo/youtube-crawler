package pl.kzochowski.youtubecrawler.integration;

import lombok.RequiredArgsConstructor;
import org.springframework.integration.handler.GenericHandler;
import org.springframework.messaging.MessageHeaders;
import org.springframework.stereotype.Component;
import pl.kzochowski.youtubecrawler.integration.model.*;
import pl.kzochowski.youtubecrawler.integration.service.VideoFetcher;
import pl.kzochowski.youtubecrawler.integration.util.ChannelDto;
import pl.kzochowski.youtubecrawler.persistence.util.YoutubeConstants;

import java.util.List;
import java.util.stream.Collectors;

import static pl.kzochowski.youtubecrawler.integration.IntegrationConstants.YOUTUBE_CHANNEL_URL_PREFIX;
import static pl.kzochowski.youtubecrawler.integration.IntegrationConstants.YOUTUBE_VIDEO_URL_PREFIX;

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
