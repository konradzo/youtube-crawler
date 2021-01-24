package pl.kzochowski.youtubecrawler;

import org.elasticsearch.client.RestHighLevelClient;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.MockBeans;
import org.springframework.test.context.ContextConfiguration;
import pl.kzochowski.youtubecrawler.integration.ChannelProducer;
import pl.kzochowski.youtubecrawler.integration.ChannelVideosTransformer;
import pl.kzochowski.youtubecrawler.integration.ElasticChannel;
import pl.kzochowski.youtubecrawler.integration.VideoHandler;

@SpringBootTest(properties = {"spring.main.allow-bean-definition-overriding=true"})
@MockBeans({@MockBean(ChannelProducer.class), @MockBean(VideoHandler.class), @MockBean(ChannelVideosTransformer.class),
        @MockBean(ElasticChannel.class), @MockBean(RestHighLevelClient.class)})
class YoutubeCrawlerApplicationTests {

    @Test
    void contextLoads() {
    }

}
