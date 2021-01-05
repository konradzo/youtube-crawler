package pl.kzochowski.youtubecrawler.integration.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import pl.kzochowski.youtubecrawler.integration.model.Video;
import pl.kzochowski.youtubecrawler.integration.model.VideoResultPageJson;
import pl.kzochowski.youtubecrawler.persistence.model.Channel;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class VideoFetcher {

    private final RestTemplate restTemplate;

    public List<Video> fetchChannelVideos(Channel channel) {
        List<Video> videos = new ArrayList<>();
        String requestUrl = prepareUrl(channel);
        fetchVideos(requestUrl, videos, null);


        return videos;
    }

    private String prepareUrl(Channel channel) {
        return "";
    }

    private void fetchVideos(String requestUrl, List<Video> result, String nextPageToken) {
        try {
            ResponseEntity<VideoResultPageJson> resultEntity = restTemplate.getForEntity(requestUrl, VideoResultPageJson.class);
            //todo fetch videos
            //todo get next page if exist
        } catch (Exception e) {
            //todo
        }
    }

//    private boolean fetchNextPage()

}
