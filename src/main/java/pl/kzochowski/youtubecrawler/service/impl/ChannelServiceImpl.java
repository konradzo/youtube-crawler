package pl.kzochowski.youtubecrawler.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pl.kzochowski.youtubecrawler.persistence.model.Channel;
import pl.kzochowski.youtubecrawler.persistence.repository.ChannelRepository;
import pl.kzochowski.youtubecrawler.persistence.util.YoutubeConstants;
import pl.kzochowski.youtubecrawler.service.ChannelService;

import javax.transaction.Transactional;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;

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
        channelRepository.save(channel);
        log.info("Channel {} saved", channel.getId());
        return channel;
    }

    @Transactional
    @Override
    public Optional<Channel> fetchChannelToCrawl() {
        Optional<Channel> channel = channelRepository.findFirstByOrderByLastExecutionAsc();
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
        channel.setUrl(YoutubeConstants.YOUTUBE_CHANNEL_URL_PREFIX + channel.getId());
    }
}
