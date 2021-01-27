package pl.kzochowski.youtubecrawler.api;

import pl.kzochowski.youtubecrawler.persistence.model.ApiKey;

import java.time.ZoneId;
import java.time.ZonedDateTime;

public class ApiKeyEndpointTest {



    private ApiKey aApiKey() {
        ApiKey apiKey = new ApiKey();
        apiKey.setId(1L);
        apiKey.setProjectName("test-project-1");
        apiKey.setKey("skduqma12slsaol");
        apiKey.setAddedAt(ZonedDateTime.of(2020, 12, 12, 5, 5, 5, 5, ZoneId.of("Europe/Warsaw")));
        apiKey.setEmail("test@gmail.com");
        return apiKey;
    }
}
