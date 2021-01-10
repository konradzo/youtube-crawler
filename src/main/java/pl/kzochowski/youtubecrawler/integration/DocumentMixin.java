package pl.kzochowski.youtubecrawler.integration;

import com.fasterxml.jackson.annotation.JsonProperty;
import pl.kzochowski.youtubecrawler.integration.model.Author;
import pl.kzochowski.youtubecrawler.integration.model.Metadata;

import java.time.ZonedDateTime;

public class DocumentMixin {

    @JsonProperty("author")
    private Author author;

    @JsonProperty("url")
    private String url;

    @JsonProperty("header")
    private String header;

    @JsonProperty("body")
    private String body;

    @JsonProperty("publishDate")
    //todo add date serialization
    private ZonedDateTime publishDate;

    @JsonProperty("metadata")
    private Metadata metadata;

}
