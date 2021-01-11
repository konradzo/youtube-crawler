package pl.kzochowski.youtubecrawler.api;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.kzochowski.youtubecrawler.api.util.Respond;
import pl.kzochowski.youtubecrawler.api.util.Response;
import pl.kzochowski.youtubecrawler.persistence.model.ApiKey;
import pl.kzochowski.youtubecrawler.persistence.repository.ApiKeyRepository;
import pl.kzochowski.youtubecrawler.service.ApiKeyService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("keys")
@RequiredArgsConstructor
public class ApiKeyEndpoint {

    private final ApiKeyService apiKeyService;

    @PostMapping
    public ResponseEntity<Response> addKey(@RequestBody @Valid ApiKey key) {
        ApiKey apiKey = apiKeyService.addKey(key);
        return Respond.created(apiKey);
    }

    @GetMapping
    public ResponseEntity<Response> listAll() {
        List<ApiKey> apiKeys = apiKeyService.fetchAll();
        return Respond.ok(apiKeys, "All keys listed");
    }
}
