package pl.kzochowski.youtubecrawler.integration.model;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class VideoStatsJson {
    private String kind;
    private String etag;
    private List<Data> items;
    private PageInfo pageInfo;
}
