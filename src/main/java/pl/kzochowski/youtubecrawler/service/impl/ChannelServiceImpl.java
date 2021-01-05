package pl.kzochowski.youtubecrawler.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pl.kzochowski.youtubecrawler.persistence.model.Channel;
import pl.kzochowski.youtubecrawler.persistence.repository.ChannelRepository;
import pl.kzochowski.youtubecrawler.service.ChannelService;

import javax.transaction.Transactional;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChannelServiceImpl implements ChannelService {

    private final ChannelRepository channelRepository;

    @Transactional
    @Override
    public Optional<Channel> fetchChannelToCrawl() {
        //todo get from repository, add lastExecution and save
        return Optional.empty();
    }
}
