package pl.kzochowski.youtubecrawler.api.util;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import pl.kzochowski.youtubecrawler.service.ApiKeyService;

@ControllerAdvice
public class ControllerExceptionHandler {

    //todo refactor?

    @ExceptionHandler
    public ResponseEntity<Response> apiKeyAlreadyExists(ApiKeyService.ApiKeyAlreadyAddedException exception) {
        return Respond.entityAlreadyExists("apiKey");
    }

    @ExceptionHandler
    public ResponseEntity<Response> handleException(Exception e) {
        return Respond.badRequest(String.format("Bad request! Message: %s", e.getMessage()));
    }
}
