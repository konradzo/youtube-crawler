package pl.kzochowski.youtubecrawler.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.kzochowski.youtubecrawler.service.ChannelService;
import pl.kzochowski.youtubecrawler.persistence.model.Channel;
import pl.kzochowski.youtubecrawler.persistence.repository.ChannelRepository;

import javax.transaction.Transactional;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;

import static pl.kzochowski.youtubecrawler.integration.IntegrationConstants.YOUTUBE_CHANNEL_URL_PREFIX;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChannelServiceImpl implements ChannelService {
    private final ChannelRepository channelRepository;

    @Override
    public Channel saveChannel(Channel channel) {
        Optional<Channel> temp = channelRepository.findById(channel.getId());
        temp.ifPresent(theChannel -> {
            throw new ChannelAlreadyExistsException(theChannel.getId());
        });

        fillChannel(channel);
        Channel saved = channelRepository.save(channel);
        log.info("Channel {} saved", channel.getId());
        return saved;
    }

    @Transactional
    @Override
    public Optional<Channel> fetchChannelToCrawl() {
        Optional<Channel> channel = channelRepository.findFirstByEnabledToCrawlOrderByLastExecutionAsc(true);
        channel.ifPresent(theChannel -> {
                    theChannel.setLastExecution(ZonedDateTime.now());
                    channelRepository.save(theChannel);
                }
        );
        return channel;
    }

    @Override
    public List<Channel> listAll() {
        List<Channel> channels = channelRepository.findAll();
        log.info("Listing all channels");
        return channels;
    }

    private void fillChannel(Channel channel) {
        channel.setEnabledToCrawl(true);
        channel.setUrl(YOUTUBE_CHANNEL_URL_PREFIX + channel.getId());
    }
}
