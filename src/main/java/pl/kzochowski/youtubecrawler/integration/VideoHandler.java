package pl.kzochowski.youtubecrawler.integration;

import org.springframework.integration.handler.GenericHandler;
import org.springframework.messaging.MessageHeaders;
import org.springframework.stereotype.Component;
import pl.kzochowski.youtubecrawler.integration.util.ChannelDto;

@Component
public class VideoHandler implements GenericHandler<ChannelDto> {

    @Override
    public Object handle(ChannelDto channelDto, MessageHeaders messageHeaders) {
        return null;
    }
}
