package pl.kzochowski.youtubecrawler.service;

import pl.kzochowski.youtubecrawler.integration.model.Data;
import pl.kzochowski.youtubecrawler.integration.model.Video;
import pl.kzochowski.youtubecrawler.integration.model.VideoDto;
import pl.kzochowski.youtubecrawler.persistence.model.ApiKey;
import pl.kzochowski.youtubecrawler.persistence.model.Channel;

import java.io.UnsupportedEncodingException;
import java.util.List;

public interface VideoFetcherUtil {

    String prepareChannelQueryUrl(Channel channel, ApiKey apiKey);

    String prepareStatisticsQueryUrl(List<Video> videos, ApiKey apiKey) throws UnsupportedEncodingException;

    List<VideoDto> prepareVideoListWithData(List<Video> videos, List<Data> dataList);

}
