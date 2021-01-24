package pl.kzochowski.youtubecrawler.persistence.repository;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.MockBeans;
import org.springframework.test.context.junit4.SpringRunner;
import pl.kzochowski.youtubecrawler.integration.ChannelProducer;
import pl.kzochowski.youtubecrawler.integration.ChannelVideosTransformer;
import pl.kzochowski.youtubecrawler.integration.ElasticChannel;
import pl.kzochowski.youtubecrawler.integration.VideoHandler;
import pl.kzochowski.youtubecrawler.persistence.model.Channel;

import java.time.ZonedDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@MockBeans({@MockBean(ChannelProducer.class), @MockBean(VideoHandler.class), @MockBean(ChannelVideosTransformer.class),
        @MockBean(ElasticChannel.class)})
@DataJpaTest
public class ChannelRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private ChannelRepository channelRepository;

    @Test
    public void shouldFindChannelById() {

        // given
        Channel channel = aChannel();
        entityManager.persistAndFlush(channel);

        // when
        Optional<Channel> result = channelRepository.findById("UCsM9RTCKh-XEiLan59m5ycA");

        // then
        assertTrue(result.isPresent());
        assertEquals(channel, result.get());

    }

    @Test
    public void shouldNotFindChannelById() {

        // when
        Optional<Channel> result = channelRepository.findById("UCsM9RTCKh-XEiLan59m5ycA");

        // then
        assertFalse(result.isPresent());

    }

    @Test
    public void shouldNotFindChannelToCrawlWhenSomeExists() {

        // given
        Channel channel = cChannel();
        entityManager.persistAndFlush(channel);

        // when
        Optional<Channel> result = channelRepository.findFirstByEnabledToCrawlOrderByLastExecutionAsc(true);

        // then
        assertFalse(result.isPresent());

    }

    @Test
    public void shouldFindChannelToCrawlByLastExecution() {

        // given
        Channel aChannel = aChannel();
        Channel bChannel = bChannel();
        entityManager.persistAndFlush(aChannel);
        entityManager.persistAndFlush(bChannel);

        // when
        Optional<Channel> result = channelRepository.findFirstByEnabledToCrawlOrderByLastExecutionAsc(true);

        // then
        assertTrue(result.isPresent());
        assertEquals(aChannel, result.get());

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

    private Channel bChannel() {
        Channel channel = new Channel();
        channel.setId("UCsM9RTCKh-XEiLan59m5ycAdsdadsda");
        channel.setUrl("https://www.youtube.com/channel/UCsM9RTCKh-XEiLan59m5ycAdsdadsda");
        channel.setEnabledToCrawl(true);
        channel.setLastExecution(ZonedDateTime.now().minusHours(1));
        channel.setAddedAt(ZonedDateTime.now().minusHours(100));
        return channel;
    }

    private Channel cChannel() {
        Channel channel = new Channel();
        channel.setId("UCsM9RTCKh-XEiLan59m5ycAdsdadsda");
        channel.setUrl("https://www.youtube.com/channel/UCsM9RTCKh-XEiLan59m5ycAdsdadsda");
        channel.setEnabledToCrawl(false);
        channel.setLastExecution(ZonedDateTime.now().minusHours(1));
        channel.setAddedAt(ZonedDateTime.now().minusHours(100));
        return channel;
    }

}
