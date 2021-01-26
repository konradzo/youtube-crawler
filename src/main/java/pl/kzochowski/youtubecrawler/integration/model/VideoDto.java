package pl.kzochowski.youtubecrawler.integration.model;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@EqualsAndHashCode
public class VideoDto {
    private Video video;
    private Data data;
}
