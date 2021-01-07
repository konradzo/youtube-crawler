package pl.kzochowski.youtubecrawler.integration.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Thumbnails {
    @JsonProperty("default")
    private Thumbnail def;
    private Thumbnail medium;
    private Thumbnail high;
}
