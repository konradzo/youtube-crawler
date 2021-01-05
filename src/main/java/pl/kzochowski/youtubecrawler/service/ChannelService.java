package pl.kzochowski.youtubecrawler.service;

import pl.kzochowski.youtubecrawler.persistence.model.Channel;

import java.util.Optional;

public interface ChannelService {

    Channel saveChannel(Channel channel);

    Optional<Channel> fetchChannelToCrawl();

}
