package pl.kzochowski.youtubecrawler.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.test.context.junit4.SpringRunner;
import pl.kzochowski.youtubecrawler.persistence.model.Channel;
import pl.kzochowski.youtubecrawler.persistence.repository.ChannelRepository;
import pl.kzochowski.youtubecrawler.service.impl.ChannelServiceImpl;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

@RunWith(SpringRunner.class)
public class ChannelServiceTest {

    private final ChannelRepository channelRepository = mock(ChannelRepository.class);
    private final ChannelService channelService = new ChannelServiceImpl(channelRepository);

    @Test
    public void shouldSaveChannel() {

        // given
        Channel sendChannel = sendChannel();
        Channel createdChannel = filledChannel();
        Mockito.when(channelRepository.findById(sendChannel.getId())).thenReturn(Optional.empty());
        Mockito.when(channelRepository.save(sendChannel)).thenReturn(createdChannel);

        // when
        Channel result = channelService.saveChannel(sendChannel);

        // then
        assertEquals(createdChannel, result);
    }


    @Test
    public void shouldThrowExceptionWhenChannelAlreadyExists() {

        // given
        Channel sendChannel = sendChannel();
        Channel savedBefore = aSavedBefore();
        Mockito.when(channelRepository.findById(sendChannel.getId())).thenReturn(Optional.of(savedBefore));

        // then
        assertThrows(ChannelService.ChannelAlreadyExistsException.class, () -> channelService.saveChannel(sendChannel));

    }

    @Test
    public void shouldReturnChannelToCrawl() {

        // given
        Channel channel = aSavedBefore();
        Mockito.when(channelRepository.findFirstByEnabledToCrawlOrderByLastExecutionAsc(true)).thenReturn(Optional.of(channel));

        // when
        Optional<Channel> result = channelService.fetchChannelToCrawl();

        // then
        assertTrue(result.isPresent());

    }

    @Test
    public void shouldNotReturnChannelToCrawl() {

        // given
        Mockito.when(channelRepository.findFirstByEnabledToCrawlOrderByLastExecutionAsc(true)).thenReturn(Optional.empty());

        // when
        Optional<Channel> result = channelService.fetchChannelToCrawl();

        // then
        assertFalse(result.isPresent());

    }


    @Test
    public void shouldReturnAllChannels() {

        // given
        Channel aChannel = aSavedBefore();
        Channel bChannel = bSavedBefore();
        List<Channel> channelList = Arrays.asList(aChannel, bChannel);
        Mockito.when(channelRepository.findAll()).thenReturn(channelList);

        // when
        List<Channel> result = channelService.listAll();

        // then
        assertThat(result, containsInAnyOrder(bChannel, aChannel));

    }

    private Channel sendChannel() {
        Channel channel = new Channel();
        channel.setId("UCsM9RTCKh-XEiLan59m5ycA");
        return channel;
    }

    private Channel filledChannel() {
        Channel channel = new Channel();
        channel.setId("UCsM9RTCKh-XEiLan59m5ycA");
        channel.setUrl("https://www.youtube.com/channel/UCsM9RTCKh-XEiLan59m5ycA");
        channel.setAddedAt(ZonedDateTime.of(2020, 12, 12, 5, 5, 5, 5, ZoneId.of("Europe/Warsaw")));
        channel.setEnabledToCrawl(true);
        return channel;
    }

    private Channel aSavedBefore() {
        Channel channel = new Channel();
        channel.setId("UCsM9RTCKh-XEiLan59m5ycA");
        channel.setUrl("https://www.youtube.com/channel/UCsM9RTCKh-XEiLan59m5ycA");
        channel.setAddedAt(ZonedDateTime.of(2020, 12, 12, 5, 5, 5, 5, ZoneId.of("Europe/Warsaw")));
        channel.setLastExecution(ZonedDateTime.of(2021, 1, 12, 5, 5, 5, 5, ZoneId.of("Europe/Warsaw")));
        channel.setEnabledToCrawl(true);
        return channel;
    }

    private Channel bSavedBefore() {
        Channel channel = new Channel();
        channel.setId("UCsM9RTCKh-XEiLan59m5ycA1212");
        channel.setUrl("https://www.youtube.com/channel/UCsM9RTCKh-XEiLan59m5ycA1212");
        channel.setAddedAt(ZonedDateTime.of(2020, 12, 12, 5, 5, 5, 5, ZoneId.of("Europe/Warsaw")));
        channel.setLastExecution(ZonedDateTime.of(2021, 1, 12, 5, 5, 5, 5, ZoneId.of("Europe/Warsaw")));
        channel.setEnabledToCrawl(true);
        return channel;
    }

    private List<Channel> channelsSavedBefore() {
        return Arrays.asList(aSavedBefore(), bSavedBefore());
    }
}
