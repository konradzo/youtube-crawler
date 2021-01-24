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
import pl.kzochowski.youtubecrawler.persistence.model.ApiKey;

import java.time.ZonedDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@MockBeans({@MockBean(ChannelProducer.class), @MockBean(VideoHandler.class), @MockBean(ChannelVideosTransformer.class),
        @MockBean(ElasticChannel.class)})
@DataJpaTest
public class ApiKeyRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private ApiKeyRepository apiKeyRepository;

    @Test
    public void shouldFindApiKey() {

        // given
        ApiKey apiKey = aApiKey();
        entityManager.persistAndFlush(apiKey);

        // when
        Optional<ApiKey> result = apiKeyRepository.findByKey("AIzaSyBF92vIWTSyoM7irbvvXsUUhA1VkOuVI00");

        // then
        assertTrue(result.isPresent());
        assertEquals(apiKey, result.get());

    }

    @Test
    public void shouldNotFindApiKey() {

        // when
        Optional<ApiKey> result = apiKeyRepository.findByKey("AIzaSyBF92vIWTSyoM7irbvvXsUUhA1VkOuVI00");

        // then
        assertFalse(result.isPresent());

    }

    @Test
    public void shouldFindApiKeyByLastExecution() {

        // given
        ApiKey apiKey1 = aApiKey();
        ApiKey apiKey2 = bApiKey();
        entityManager.persistAndFlush(apiKey1);
        entityManager.persistAndFlush(apiKey2);

        // when
        Optional<ApiKey> result = apiKeyRepository.findFirstByOrderByLastExecutionAsc();

        // then
        assertTrue(result.isPresent());
        assertEquals(apiKey1, result.get());

    }

    @Test
    public void shouldNotFindAnyApiKey(){

        // when
        Optional<ApiKey> result = apiKeyRepository.findFirstByOrderByLastExecutionAsc();

        // then
        assertFalse(result.isPresent());

    }


    private ApiKey aApiKey() {
        ApiKey apiKey = new ApiKey();
        apiKey.setKey("AIzaSyBF92vIWTSyoM7irbvvXsUUhA1VkOuVI00");
        apiKey.setLastExecution(ZonedDateTime.now().minusHours(10));
        apiKey.setProjectName("Test-project-1");
        apiKey.setEmail("zochowski.konrad@gmail.com");
        return apiKey;
    }

    private ApiKey bApiKey() {
        ApiKey apiKey = new ApiKey();
        apiKey.setKey("AIzaSyBF92vIWTSyoM7irbvvXsUUhA1VkOuVI00");
        apiKey.setLastExecution(ZonedDateTime.now().minusHours(8));
        apiKey.setProjectName("Test-project-2");
        apiKey.setEmail("zochowski.konrad@gmail.com");
        return apiKey;
    }

}
