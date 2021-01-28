package pl.kzochowski.youtubecrawler.integration;

import lombok.RequiredArgsConstructor;
import org.springframework.messaging.MessagingException;
import org.springframework.stereotype.Component;
import org.springframework.util.ErrorHandler;
import pl.kzochowski.youtubecrawler.integration.util.ChannelDto;
import pl.kzochowski.youtubecrawler.persistence.model.Channel;
import pl.kzochowski.youtubecrawler.service.ChannelService;

import java.util.Objects;

@Component
@RequiredArgsConstructor
public class CustomErrorHandler implements ErrorHandler {
    private final ChannelService channelService;

    @Override
    public void handleError(Throwable throwable) {
        if (throwable instanceof MessagingException) {
            MessagingException exception = (MessagingException) throwable;
            Object payload = Objects.requireNonNull(exception.getFailedMessage()).getPayload();
            if (payload instanceof ChannelDto) {
                channelService.updateChannelWhenErrorOccur(((ChannelDto) payload).getChannel());
            }

        }
        throwable.printStackTrace();
    }
}
