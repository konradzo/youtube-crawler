package pl.kzochowski.youtubecrawler.integration;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.integration.context.IntegrationObjectSupport;
import org.springframework.integration.core.MessageSource;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;
import pl.kzochowski.youtubecrawler.integration.util.ChannelDto;
import pl.kzochowski.youtubecrawler.persistence.model.Channel;
import pl.kzochowski.youtubecrawler.service.ChannelService;

import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class ChannelProducer extends IntegrationObjectSupport implements MessageSource<ChannelDto> {

    private final ChannelService channelService;

    @Override
    public Message<ChannelDto> receive() {
        final Optional<Channel> channel = channelService.fetchChannelToCrawl();
        if (channel.isPresent()) {
            ChannelDto channelDto = new ChannelDto(channel.get());
            log.info("Crawling channel: {}", channel.get().getUrl());
            return MessageBuilder.withPayload(channelDto).build();
        } else {
            log.info("No channel to crawl");
        }
        return null;
    }
}
