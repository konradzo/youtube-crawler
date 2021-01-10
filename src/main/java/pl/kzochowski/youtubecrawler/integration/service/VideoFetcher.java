package pl.kzochowski.youtubecrawler.integration.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.utils.URIBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import pl.kzochowski.youtubecrawler.integration.model.*;
import pl.kzochowski.youtubecrawler.persistence.model.Channel;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.time.ZonedDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static pl.kzochowski.youtubecrawler.integration.IntegrationConstants.YOUTUBE_CRAWL_VIDEO_URL_PREFIX;
import static pl.kzochowski.youtubecrawler.integration.IntegrationConstants.YOUTUBE_STATISTICS_VIDEO_URL_PREFIX;

@Slf4j
@Component
@RequiredArgsConstructor
public class VideoFetcher {

    private final RestTemplate restTemplate;

    @Value("${daysLimit}")
    private int daysBefore;

    public List<VideoDto> fetchChannelVideos(Channel channel) {
        List<VideoDto> videos = new ArrayList<>();
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

    private void fetchVideos(String requestUrl, List<VideoDto> result, Channel channel) {
        String nextPageToken = null;
        List<Video> videos = new ArrayList<>();
        List<Data> videosData = new ArrayList<>();
        try {
            ResponseEntity<VideoResultPageJson> resultEntity = restTemplate.getForEntity(requestUrl, VideoResultPageJson.class);
            videos.addAll(resultEntity.getBody().getItems());

            ResponseEntity<VideoStatsJson> statsEntity = restTemplate.getForEntity(prepareStatisticsQuery(videos), VideoStatsJson.class);
            videosData.addAll(statsEntity.getBody().getItems());
            List<VideoDto> dtoList = prepareVideoListWithData(videos, videosData);
            result.addAll(dtoList);
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

    private String prepareStatisticsQuery(List<Video> videos) throws UnsupportedEncodingException {
        List<String> ids = videos.stream().map(video -> video.getId().getVideoId()).filter(Objects::nonNull).collect(Collectors.toList());
//        List<String> ids = new ArrayList<>(Arrays.asList("DLY7nVsq2-s", "UbYQErtM9Zk"));
        String idList = String.join(",", ids);
        URIBuilder builder = new URIBuilder();
        builder.setScheme("https");
        builder.setHost("www.googleapis.com");
        builder.setPath("youtube/v3/videos");
        builder.addParameter("part", "statistics");
        builder.addParameter("id", idList);
        builder.addParameter("key", "AIzaSyBF92vIWTSyoM7irbvvXsUUhA1VkOuVI00");

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
