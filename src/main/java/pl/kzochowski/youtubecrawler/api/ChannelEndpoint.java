package pl.kzochowski.youtubecrawler.api;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.kzochowski.youtubecrawler.api.util.Respond;
import pl.kzochowski.youtubecrawler.api.util.Response;
import pl.kzochowski.youtubecrawler.persistence.model.Channel;
import pl.kzochowski.youtubecrawler.service.ChannelService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("channels")
@RequiredArgsConstructor
public class ChannelEndpoint {
    private final ChannelService channelService;

    @PostMapping
    public ResponseEntity<Response> save(@RequestBody @Valid Channel newChannel) {
        Channel channel = channelService.saveChannel(newChannel);
        return Respond.created(channel);
    }

    @GetMapping
    public ResponseEntity<Response> list() {
        List<Channel> channels = channelService.listAll();
        return Respond.ok(channels, "All channels listed");
    }

}
