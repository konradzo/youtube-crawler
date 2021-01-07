package pl.kzochowski.youtubecrawler.integration.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import pl.kzochowski.youtubecrawler.integration.model.Video;
import pl.kzochowski.youtubecrawler.integration.model.VideoResultPageJson;
import pl.kzochowski.youtubecrawler.persistence.model.Channel;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static pl.kzochowski.youtubecrawler.integration.IntegrationConstants.YOUTUBE_CRAWL_VIDEO_URL_PREFIX;

@Slf4j
@Component
@RequiredArgsConstructor
public class VideoFetcher {

    private final int daysBefore = 30;
    private final RestTemplate restTemplate;

    public List<Video> fetchChannelVideos(Channel channel) {
        List<Video> videos = new ArrayList<>();
        String requestUrl = prepareUrl(channel);

        fetchVideos(requestUrl, videos, channel);
        log.info("Fetched {} videos for channel {}", videos.size(), channel.getId());
        return videos;
    }

    private String prepareUrl(Channel channel) {
        //todo refactor
        //todo handle switching keys
        // todo values at properties
        return YOUTUBE_CRAWL_VIDEO_URL_PREFIX + channel.getId() + "&key=AIzaSyBF92vIWTSyoM7irbvvXsUUhA1VkOuVI00";
    }

    private void fetchVideos(String requestUrl, List<Video> result, Channel channel) {
        String nextPageToken = null;
        try {
            ResponseEntity<VideoResultPageJson> resultEntity = restTemplate.getForEntity(requestUrl, VideoResultPageJson.class);
            result.addAll(resultEntity.getBody().getItems());
            if (checkDateCondition(resultEntity.getBody())) {
                nextPageToken = resultEntity.getBody().getNextPageToken();
            }
            log.info("{} video fetched, channel: {}", resultEntity.getBody().getItems().size(), channel.getId());
        } catch (Exception e) {
            //todo
            e.printStackTrace();
        }
        if (Objects.nonNull(nextPageToken)) {
            fetchVideos(requestUrl + "&pageToken=" + nextPageToken, result, channel);
        }
    }

    private boolean checkDateCondition(VideoResultPageJson json) {
        Video lastVideoPage = json.getItems().get(json.getItems().size() - 1);
        return lastVideoPage.getSnippet().getPublishedAt().isAfter(ZonedDateTime.now().minusDays(daysBefore));
    }

}
