package pl.kzochowski.youtubecrawler.integration.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class VideoDto {
    private Video video;
    private Data data;
}
