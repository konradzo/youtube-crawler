package pl.kzochowski.youtubecrawler.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import pl.kzochowski.youtubecrawler.integration.model.*;
import pl.kzochowski.youtubecrawler.service.ApiKeyService;
import pl.kzochowski.youtubecrawler.service.VideoFetcherUtil;
import pl.kzochowski.youtubecrawler.service.VideoFetcher;
import pl.kzochowski.youtubecrawler.persistence.model.ApiKey;
import pl.kzochowski.youtubecrawler.persistence.model.Channel;

import java.time.ZonedDateTime;
import java.util.*;

@Slf4j
@Component
@RequiredArgsConstructor
public class VideoFetcherImpl implements VideoFetcher {
    private final RestTemplate restTemplate;
    private final ApiKeyService apiKeyService;
    private final VideoFetcherUtil fetcherUtil;
    private ApiKey currentApiKey;

    @Value("${daysLimit}")
    private int daysBefore;

    public List<VideoDto> fetchChannelVideos(Channel channel) {
        List<VideoDto> videos = new ArrayList<>();
        if (Objects.isNull(currentApiKey))
            switchApiKey();

        String requestUrl = fetcherUtil.prepareChannelQueryUrl(channel, currentApiKey);

        fetchVideos(requestUrl, videos, channel);
        log.info("Fetched {} videos for channel {}", videos.size(), channel.getId());
        return videos;
    }

    private void switchApiKey() {
        currentApiKey = apiKeyService.fetchNextApiKey();
        log.info("Api key switched to: {}", currentApiKey.getKey());
    }

    private void fetchVideos(String requestUrl, List<VideoDto> result, Channel channel) {
        String nextPageToken = null;
        try {
            ResponseEntity<VideoResultPageJson> resultEntity = sendRequest(requestUrl, VideoResultPageJson.class);
            List<Video> videos = new ArrayList<>(Objects.requireNonNull(resultEntity.getBody()).getItems());

            ResponseEntity<VideoStatsJson> statsEntity = sendRequest(fetcherUtil.prepareStatisticsQueryUrl(videos, currentApiKey), VideoStatsJson.class);
            List<VideoDto> dtoList = fetcherUtil.prepareVideoListWithData(videos, Objects.requireNonNull(statsEntity.getBody()).getItems());
            result.addAll(dtoList);

            if (checkDateCondition(resultEntity.getBody())) {
                nextPageToken = resultEntity.getBody().getNextPageToken();
            }

            log.info("{} video fetched, channel: {}", resultEntity.getBody().getItems().size(), channel.getId());
        } catch (HttpClientErrorException e) {
            if (e.getStatusCode().equals(HttpStatus.FORBIDDEN)) {
                log.info("Quota exceeded");
                switchApiKey();
            } else {
                throw new FetchingVideoException(channel, e.getMessage());
            }
        } catch (Exception e) {
            throw new FetchingVideoException(channel, e.getMessage());
        }

        if (Objects.nonNull(nextPageToken)) {
            fetchVideos(requestUrl + "&pageToken=" + nextPageToken, result, channel);
        }
    }

    private boolean checkDateCondition(VideoResultPageJson json) {
        Video lastVideoPage = json.getItems().get(json.getItems().size() - 1);
        return lastVideoPage.getSnippet().getPublishedAt().isAfter(ZonedDateTime.now().minusDays(daysBefore));
    }

    private <T> ResponseEntity<T> sendRequest(String requestUrl, Class<T> responseType) throws HttpClientErrorException {
        return restTemplate.getForEntity(requestUrl, responseType);
    }

}
