package pl.kzochowski.youtubecrawler.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.MockBeans;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import pl.kzochowski.youtubecrawler.integration.ChannelProducer;
import pl.kzochowski.youtubecrawler.integration.ChannelVideosTransformer;
import pl.kzochowski.youtubecrawler.integration.ElasticChannel;
import pl.kzochowski.youtubecrawler.integration.VideoHandler;
import pl.kzochowski.youtubecrawler.persistence.model.Channel;
import pl.kzochowski.youtubecrawler.service.ChannelService;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@RunWith(SpringRunner.class)
@WebMvcTest(ChannelEndpoint.class)
@MockBeans({@MockBean(ChannelProducer.class), @MockBean(VideoHandler.class), @MockBean(ChannelVideosTransformer.class),
        @MockBean(ElasticChannel.class)})
@AutoConfigureMockMvc(addFilters = false)
public class ChannelEndpointTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ChannelService channelService;

    private final ObjectMapper mapper = new ObjectMapper();

    @Test
    public void shouldSaveAndReturnChannel() throws Exception {

        // given
        Channel sendChannel = sendChannel();
        Channel savedChannel = savedChannel();
        Mockito.when(channelService.saveChannel(sendChannel)).thenReturn(savedChannel);

        // then
        mockMvc.perform(post("/channels")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(sendChannel)))
                .andExpect(status().isCreated());

    }

    @Test
    public void shouldThrowExceptionWhenChannelAlreadyExists() throws Exception {

        // given
        Channel sendChannel = sendChannel();
        Mockito.when(channelService.saveChannel(sendChannel)).thenThrow(ChannelService.ChannelAlreadyExistsException.class);

        // then
        mockMvc.perform(post("/channels")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(sendChannel)))
                .andExpect(status().isConflict());

    }

    @Test
    public void shouldReturnChannelList() throws Exception {

        // given
        List<Channel> channels = Collections.singletonList(sendChannel());
        Mockito.when(channelService.listAll()).thenReturn(channels);

        // then
        mockMvc.perform(get("/channels").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
        //        .andExpect(jsonPath("$.code", hasValue("200 OK")))
        ;
    }


    private Channel sendChannel() {
        Channel channel = new Channel();
        channel.setId("UCsM9RTCKh-XEiLan59m5ycA");
        return channel;
    }

    private Channel savedChannel() {
        Channel channel = new Channel();
        channel.setId("UCsM9RTCKh-XEiLan59m5ycA");
        channel.setUrl("https://www.youtube.com/channel/UCsM9RTCKh-XEiLan59m5ycA");
        channel.setAddedAt(ZonedDateTime.of(2020, 12, 12, 5, 5, 5, 5, ZoneId.of("Europe/Warsaw")));
        channel.setEnabledToCrawl(true);
        return channel;
    }
}
