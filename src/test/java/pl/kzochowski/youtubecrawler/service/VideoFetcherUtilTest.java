package pl.kzochowski.youtubecrawler.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;
import pl.kzochowski.youtubecrawler.integration.model.Data;
import pl.kzochowski.youtubecrawler.integration.model.Video;
import pl.kzochowski.youtubecrawler.integration.model.VideoDto;
import pl.kzochowski.youtubecrawler.integration.model.VideoId;
import pl.kzochowski.youtubecrawler.persistence.model.ApiKey;
import pl.kzochowski.youtubecrawler.persistence.model.Channel;
import pl.kzochowski.youtubecrawler.service.impl.VideoFetcherUtilImpl;

import java.io.UnsupportedEncodingException;
import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.junit.jupiter.api.Assertions.assertEquals;

@RunWith(SpringRunner.class)
public class VideoFetcherUtilTest {

    private final VideoFetcherUtil videoFetcherUtil = new VideoFetcherUtilImpl();

    @Test
    public void shouldPrepareSuitableChannelQueryUrl() {

        // given
        String suitableUrl = "https://www.googleapis.com/youtube/v3/search?part=id,snippet&maxResults=50&order=date&channelId=UCsM9RTCKh-XEiLan59m5ycA&key=AIzaSyBF92vIWTSyoM7irbvvXsUUhA1VkOuVI00";
        Channel channel = aChannel();
        ApiKey apiKey = aApiKey();

        // when
        String channelQueryUrl = videoFetcherUtil.prepareChannelQueryUrl(channel, apiKey);

        // then
        assertEquals(channelQueryUrl, suitableUrl);

    }

    @Test
    public void shouldPrepareSuitableStatisticsQueryUrl() throws UnsupportedEncodingException {

        // given
        String suitableUrl = "https://www.googleapis.com/youtube/v3/videos?part=statistics&id=qwe12345,qwe54321&key=AIzaSyBF92vIWTSyoM7irbvvXsUUhA1VkOuVI00";
        Video aVideo = prepareVideo("kind-1", "qwe12345");
        Video bVideo = prepareVideo("kind-2", "qwe54321");
        List<Video> videoList = Arrays.asList(aVideo, bVideo);
        ApiKey apiKey = aApiKey();

        // when
        String statisticsQueryUrl = videoFetcherUtil.prepareStatisticsQueryUrl(videoList, apiKey);

        // then
        assertEquals(statisticsQueryUrl, suitableUrl);

    }

    @Test
    public void shouldPrepareSuitableVideoDtoList() {

        // given
        Video aVideo = prepareVideo("kind-1", "qwe12345");
        Video bVideo = prepareVideo("kind-2", "qwe54321");
        List<Video> videoList = Arrays.asList(aVideo, bVideo);

        Data aData = prepareData("qwe12345");
        Data bData = prepareData("qwe54321");
        List<Data> dataList = Arrays.asList(aData, bData);

        VideoDto aVideoDto  = prepareVideoDto(aVideo, aData);
        VideoDto bVideoDto = prepareVideoDto(bVideo, bData);

        // when
        List<VideoDto> result = videoFetcherUtil.prepareVideoListWithData(videoList, dataList);

        // then
        assertThat(result, containsInAnyOrder(bVideoDto, aVideoDto));
    }

    private VideoDto prepareVideoDto(Video aVideo, Data aData) {
        return new VideoDto(aVideo, aData);
    }

    private Channel aChannel() {
        Channel channel = new Channel();
        channel.setId("UCsM9RTCKh-XEiLan59m5ycA");
        channel.setUrl("https://www.youtube.com/channel/UCsM9RTCKh-XEiLan59m5ycA");
        channel.setEnabledToCrawl(true);
        channel.setLastExecution(ZonedDateTime.now().minusHours(10));
        channel.setAddedAt(ZonedDateTime.now().minusHours(100));
        return channel;
    }

    private ApiKey aApiKey() {
        ApiKey apiKey = new ApiKey();
        apiKey.setKey("AIzaSyBF92vIWTSyoM7irbvvXsUUhA1VkOuVI00");
        apiKey.setLastExecution(ZonedDateTime.now().minusHours(10));
        apiKey.setProjectName("Test-project-1");
        apiKey.setEmail("zochowski.konrad@gmail.com");
        return apiKey;
    }


    private Video prepareVideo(String kind, String videoId) {
        Video video = new Video();
        VideoId id = new VideoId(kind, videoId);
        video.setId(id);
        return video;
    }

    private Data prepareData(String id) {
        Data data = new Data();
        data.setId(id);
        return data;
    }

}
