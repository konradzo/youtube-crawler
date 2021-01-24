package pl.kzochowski.youtubecrawler.service;

import pl.kzochowski.youtubecrawler.integration.model.VideoDto;
import pl.kzochowski.youtubecrawler.persistence.model.Channel;

import java.util.List;

public interface VideoFetcher {

    List<VideoDto> fetchChannelVideos(Channel channel);

}
