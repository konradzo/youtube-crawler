package pl.kzochowski.youtubecrawler.integration.util;

import lombok.Getter;
import lombok.Setter;
import pl.kzochowski.youtubecrawler.integration.model.Document;
import pl.kzochowski.youtubecrawler.persistence.model.Channel;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class ChannelDto {

    private Channel channel;
    private List<Document> documents;

    public ChannelDto(Channel channel) {
        this.channel = channel;
        this.documents = new ArrayList<>();
    }
}
