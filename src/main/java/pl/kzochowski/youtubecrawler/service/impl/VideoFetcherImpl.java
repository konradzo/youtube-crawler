package pl.kzochowski.youtubecrawler.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.utils.URIBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import pl.kzochowski.youtubecrawler.integration.model.*;
import pl.kzochowski.youtubecrawler.service.ApiKeyService;
import pl.kzochowski.youtubecrawler.service.VideoFetcher;
import pl.kzochowski.youtubecrawler.persistence.model.ApiKey;
import pl.kzochowski.youtubecrawler.persistence.model.Channel;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.time.ZonedDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
public class VideoFetcherImpl implements VideoFetcher {
    private final RestTemplate restTemplate;
    private final ApiKeyService apiKeyService;
    private ApiKey currentApiKey;

    @Value("${daysLimit}")
    private int daysBefore;

    public List<VideoDto> fetchChannelVideos(Channel channel) {
        List<VideoDto> videos = new ArrayList<>();
        if (Objects.isNull(currentApiKey))
            switchApiKey();

        String requestUrl = prepareQuery(channel);

        fetchVideos(requestUrl, videos, channel);
        log.info("Fetched {} videos for channel {}", videos.size(), channel.getId());
        return videos;
    }

    private void switchApiKey() {
        currentApiKey = apiKeyService.fetchNextApiKey();
        log.info("Api key switched to: {}", currentApiKey.getKey());
    }

    //todo refactor
    private void fetchVideos(String requestUrl, List<VideoDto> result, Channel channel) {
        String nextPageToken = null;
        try {
            ResponseEntity<VideoResultPageJson> resultEntity = restTemplate.getForEntity(requestUrl, VideoResultPageJson.class);
            List<Video> videos = new ArrayList<>(resultEntity.getBody().getItems());

            ResponseEntity<VideoStatsJson> statsEntity = restTemplate.getForEntity(prepareStatisticsQuery(videos), VideoStatsJson.class);
            List<VideoDto> dtoList = prepareVideoListWithData(videos, statsEntity.getBody().getItems());
            result.addAll(dtoList);
            if (checkDateCondition(resultEntity.getBody())) {
                nextPageToken = resultEntity.getBody().getNextPageToken();
            }
            log.info("{} video fetched, channel: {}", resultEntity.getBody().getItems().size(), channel.getId());
        } catch (HttpClientErrorException e) {
            e.printStackTrace();
            if (e.getStatusCode().equals(HttpStatus.FORBIDDEN)) {
                log.info("Quota exceeded");
                switchApiKey();
            }
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

    private String prepareQuery(Channel channel) {
        String requestUrl = "";
        URIBuilder builder = new URIBuilder();
        builder.setScheme("https");
        builder.setHost("www.googleapis.com");
        builder.setPath("youtube/v3/search");
        builder.addParameter("part", "id,snippet");
        builder.addParameter("maxResults", "50");
        builder.addParameter("order", "date");
        builder.addParameter("channelId", channel.getId());
        builder.addParameter("key", currentApiKey.getKey());
        try {
            requestUrl = URLDecoder.decode(builder.toString(), StandardCharsets.UTF_8.toString());
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return requestUrl;
    }

    private String prepareStatisticsQuery(List<Video> videos) throws UnsupportedEncodingException {
        List<String> ids = videos.stream().map(video -> video.getId().getVideoId()).filter(Objects::nonNull).collect(Collectors.toList());
        String idList = String.join(",", ids);
        URIBuilder builder = new URIBuilder();
        builder.setScheme("https");
        builder.setHost("www.googleapis.com");
        builder.setPath("youtube/v3/videos");
        builder.addParameter("part", "statistics");
        builder.addParameter("id", idList);
        builder.addParameter("key", currentApiKey.getKey());
        return URLDecoder.decode(builder.toString(), StandardCharsets.UTF_8.toString());
    }

    private List<VideoDto> prepareVideoListWithData(List<Video> videos, List<Data> dataList) {
        List<VideoDto> dtoList = new ArrayList<>();

        videos.forEach(video -> {
            Optional<Data> data = findSuitableData(video, dataList);
            data.ifPresent(result -> dtoList.add(new VideoDto(video, result)));
        });

        return dtoList;
    }

    private Optional<Data> findSuitableData(Video video, List<Data> dataList) {
        return dataList.stream().filter(data -> data.getId().equals(video.getId().getVideoId())).findFirst();
    }

}
