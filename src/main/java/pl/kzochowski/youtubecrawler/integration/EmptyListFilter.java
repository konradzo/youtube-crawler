package pl.kzochowski.youtubecrawler.integration;

import lombok.extern.slf4j.Slf4j;
import org.springframework.integration.core.GenericSelector;
import org.springframework.stereotype.Component;
import pl.kzochowski.youtubecrawler.integration.util.ChannelDto;

@Slf4j
@Component
public class EmptyListFilter implements GenericSelector<ChannelDto> {

    @Override
    public boolean accept(ChannelDto channelDto) {
        boolean isEmpty = channelDto.getVideoDtos().isEmpty();
        if (isEmpty)
            log.info("Empty video list!");
        return !isEmpty;
    }
}
