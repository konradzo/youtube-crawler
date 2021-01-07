package pl.kzochowski.youtubecrawler.integration.model;

import lombok.Getter;
import lombok.Setter;

import java.time.ZonedDateTime;

@Getter
@Setter
public class Snippet {
    private ZonedDateTime publishedAt;
    private String channelId;
    private String title;
    private String description;
    private Thumbnails thumbnails;
    private String channelTitle;
    private String liveBroadcastContent;
    private ZonedDateTime publishTime;
}
