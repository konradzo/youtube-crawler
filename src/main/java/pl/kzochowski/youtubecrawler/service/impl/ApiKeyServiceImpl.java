package pl.kzochowski.youtubecrawler.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pl.kzochowski.youtubecrawler.persistence.model.ApiKey;
import pl.kzochowski.youtubecrawler.persistence.repository.ApiKeyRepository;
import pl.kzochowski.youtubecrawler.service.ApiKeyService;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ApiKeyServiceImpl implements ApiKeyService {

    private final ApiKeyRepository apiKeyRepository;

    @Override
    public ApiKey addKey(ApiKey apiKey) {
        Optional<ApiKey> key = apiKeyRepository.findByKey(apiKey.getKey());
        key.ifPresent(k -> {
            throw new ApiKeyAlreadyAddedException(k.getKey());
        });
        apiKeyRepository.save(apiKey);
        return apiKey;
    }

    @Override
    public ApiKey fetchNextApiKey() {
        Optional<ApiKey> optKey = apiKeyRepository.findFirstByOrderByLastExecutionAsc();
        if (optKey.isPresent()) {
            ApiKey apiKey = optKey.get();
            apiKey.setLastExecution(ZonedDateTime.now());
            apiKeyRepository.save(apiKey);
            log.info("ApiKey {} updated", apiKey.getKey());
            return apiKey;
        } else {
            throw new NoApiKeyAvailableException();
        }
    }

    @Override
    public List<ApiKey> fetchAll() {
        List<ApiKey> list = apiKeyRepository.findAll();
        log.info("Listing all {} keys", list.size());
        return list;
    }
}
