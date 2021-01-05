package pl.kzochowski.youtubecrawler.api;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.kzochowski.youtubecrawler.api.util.Respond;
import pl.kzochowski.youtubecrawler.api.util.Response;
import pl.kzochowski.youtubecrawler.persistence.model.Channel;
import pl.kzochowski.youtubecrawler.service.ChannelService;

import javax.validation.Valid;

@RestController
@RequestMapping("channel")
@RequiredArgsConstructor
public class ChannelEndpoint {

    private final ChannelService channelService;

    @PostMapping
    public ResponseEntity<Response> save(@RequestBody @Valid Channel newChannel) {
        Channel channel = channelService.saveChannel(newChannel);
        return Respond.ok(channel);
    }

}
