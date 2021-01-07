package pl.kzochowski.youtubecrawler.integration.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Video {
    private String kind;
    private String etag;
    private VideoId id;
    private Snippet snippet;
}
