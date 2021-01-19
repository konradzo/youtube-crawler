package pl.kzochowski.youtubecrawler.integration.util;

import lombok.Getter;
import lombok.Setter;
import pl.kzochowski.youtubecrawler.integration.model.VideoDto;
import pl.kzochowski.youtubecrawler.persistence.model.Channel;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class ChannelDto {
    private Channel channel;
    private List<VideoDto> videoDtos;

    public ChannelDto(Channel channel) {
        this.channel = channel;
        this.videoDtos = new ArrayList<>();
    }
}
