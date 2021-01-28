package pl.kzochowski.youtubecrawler.service;

import pl.kzochowski.youtubecrawler.integration.model.VideoDto;
import pl.kzochowski.youtubecrawler.persistence.model.Channel;

import java.util.List;

public interface VideoFetcher {

    List<VideoDto> fetchChannelVideos(Channel channel);

    class FetchingVideoException extends RuntimeException {
        public FetchingVideoException(Channel channel, String message) {
            super(String.format("Some error occurred when fetching vidoes for channel with id: %s. Message: %s", channel.getId(), message));
        }
    }

}
