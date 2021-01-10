package pl.kzochowski.youtubecrawler.integration.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.ZonedDateTime;

@Builder
@Getter
@Setter
public class Document {
    private Author author;
    private String url;
    private String header;
    private String body;
    private ZonedDateTime publishDate;
    private Metadata metadata;
}
