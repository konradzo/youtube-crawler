package pl.kzochowski.youtubecrawler.integration.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Data {
    private String kind;
    private String etag;
    private String id;
    private Statistics statistics;
}
