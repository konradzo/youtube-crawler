package pl.kzochowski.youtubecrawler.integration.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class Metadata {
    private long viewCount;
    private long likeCount;
    private long dislikeCount;
    private long favouriteCount;
    private long commentCount;
}
