package pl.kzochowski.youtubecrawler.integration.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class Author {
    private String name;
    private String url;
}
