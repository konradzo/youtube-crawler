package pl.kzochowski.youtubecrawler.service;

import pl.kzochowski.youtubecrawler.persistence.model.ApiKey;

import java.util.List;

public interface ApiKeyService {

    ApiKey addKey(ApiKey apiKey);

    ApiKey fetchNextApiKey();

    List<ApiKey> fetchAll();

    class ApiKeyAlreadyAddedException extends RuntimeException {
        public ApiKeyAlreadyAddedException(String apiKey) {
            super(String.format("Api key %s already added!", apiKey));
        }
    }

    class NoApiKeyAvailableException extends RuntimeException{
        public NoApiKeyAvailableException(){
            super("No api key available!");
        }
    }

}
