package pl.kzochowski.youtubecrawler.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.test.context.junit4.SpringRunner;
import pl.kzochowski.youtubecrawler.persistence.model.ApiKey;
import pl.kzochowski.youtubecrawler.persistence.repository.ApiKeyRepository;
import pl.kzochowski.youtubecrawler.service.impl.ApiKeyServiceImpl;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

@RunWith(SpringRunner.class)
public class ApiKeyServiceTest {

    private final ApiKeyRepository apiKeyRepository = mock(ApiKeyRepository.class);
    private final ApiKeyService apiKeyService = new ApiKeyServiceImpl(apiKeyRepository);

    @Test
    public void shouldSaveAndReturnApiKey() {

        // given
        ApiKey apiKey = aApiKey();
        Mockito.when(apiKeyRepository.findByKey(apiKey.getKey())).thenReturn(Optional.empty());
        Mockito.when(apiKeyRepository.save(apiKey)).thenReturn(apiKey);

        // when
        ApiKey result = apiKeyService.addKey(apiKey);

        // then
        assertEquals(apiKey, result);

    }

    @Test
    public void shouldThrowExceptionWhenApiKeyLAlreadyAdded() {

        // given
        ApiKey apiKey = aApiKey();
        Mockito.when(apiKeyRepository.findByKey(apiKey.getKey())).thenReturn(Optional.of(apiKey));

        // then
        assertThrows(ApiKeyService.ApiKeyAlreadyAddedException.class, () -> {
            apiKeyService.addKey(apiKey);
        });

    }

    @Test
    public void shouldThrowExceptionWhenNoAvailableApiKey() {

        // given
        Mockito.when(apiKeyRepository.findFirstByOrderByLastExecutionAsc()).thenReturn(Optional.empty());

        // then
        assertThrows(ApiKeyService.NoApiKeyAvailableException.class, apiKeyService::fetchNextApiKey);

    }

    private ApiKey aApiKey() {
        ApiKey apiKey = new ApiKey();
        apiKey.setId(1L);
        apiKey.setProjectName("test-project-1");
        apiKey.setKey("skduqma12slsaol");
        apiKey.setAddedAt(ZonedDateTime.of(2020, 12, 12, 5, 5, 5, 5, ZoneId.of("Europe/Warsaw")));
        apiKey.setLastExecution(ZonedDateTime.of(2021, 1, 12, 5, 5, 5, 5, ZoneId.of("Europe/Warsaw")));
        apiKey.setEmail("test@gmail.com");
        return apiKey;
    }

}
