package pl.kzochowski.youtubecrawler.integration.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Statistics {
    private long viewCount;
    private long likeCount;
    private long dislikeCount;
    private long favouriteCount;
    private long commentCount;
}
