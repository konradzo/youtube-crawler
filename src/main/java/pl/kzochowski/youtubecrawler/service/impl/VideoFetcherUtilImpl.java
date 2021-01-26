package pl.kzochowski.youtubecrawler.service.impl;

import org.apache.http.client.utils.URIBuilder;
import org.springframework.stereotype.Component;
import pl.kzochowski.youtubecrawler.integration.model.Data;
import pl.kzochowski.youtubecrawler.integration.model.Video;
import pl.kzochowski.youtubecrawler.integration.model.VideoDto;
import pl.kzochowski.youtubecrawler.persistence.model.ApiKey;
import pl.kzochowski.youtubecrawler.persistence.model.Channel;
import pl.kzochowski.youtubecrawler.service.VideoFetcherUtil;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class VideoFetcherUtilImpl implements VideoFetcherUtil {

    @Override
    public String prepareChannelQueryUrl(Channel channel, ApiKey apiKey) {
        String requestUrl = "";
        URIBuilder builder = new URIBuilder();
        builder.setScheme("https");
        builder.setHost("www.googleapis.com");
        builder.setPath("youtube/v3/search");
        builder.addParameter("part", "id,snippet");
        builder.addParameter("maxResults", "50");
        builder.addParameter("order", "date");
        builder.addParameter("channelId", channel.getId());
        builder.addParameter("key", apiKey.getKey());
        try {
            requestUrl = URLDecoder.decode(builder.toString(), StandardCharsets.UTF_8.toString());
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return requestUrl;
    }

    @Override
    public String prepareStatisticsQueryUrl(List<Video> videos, ApiKey apiKey) throws UnsupportedEncodingException {
        List<String> ids = videos.stream().map(video -> video.getId().getVideoId()).filter(Objects::nonNull).collect(Collectors.toList());
        String idList = String.join(",", ids);
        URIBuilder builder = new URIBuilder();
        builder.setScheme("https");
        builder.setHost("www.googleapis.com");
        builder.setPath("youtube/v3/videos");
        builder.addParameter("part", "statistics");
        builder.addParameter("id", idList);
        builder.addParameter("key", apiKey.getKey());
        return URLDecoder.decode(builder.toString(), StandardCharsets.UTF_8.toString());
    }

    @Override
    public List<VideoDto> prepareVideoListWithData(List<Video> videos, List<Data> dataList) {
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
