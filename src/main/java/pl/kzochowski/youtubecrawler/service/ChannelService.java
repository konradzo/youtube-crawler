package pl.kzochowski.youtubecrawler.service;

import pl.kzochowski.youtubecrawler.persistence.model.Channel;

import java.util.List;
import java.util.Optional;

public interface ChannelService {

    Channel saveChannel(Channel channel);

    Optional<Channel> fetchChannelToCrawl();

    List<Channel> listAll();

    void updateChannelWhenErrorOccur(Channel channel);

    class ChannelAlreadyExistsException extends RuntimeException {
        public ChannelAlreadyExistsException(String id) {
            super(String.format("Channel with id %s already exists", id));
        }
    }

}
