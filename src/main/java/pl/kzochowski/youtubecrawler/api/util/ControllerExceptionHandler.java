package pl.kzochowski.youtubecrawler.api.util;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import pl.kzochowski.youtubecrawler.service.ApiKeyService;
import pl.kzochowski.youtubecrawler.service.ChannelService;

@ControllerAdvice
public class ControllerExceptionHandler {

    @ExceptionHandler
    public ResponseEntity<Response> channelAlreadyExists(ChannelService.ChannelAlreadyExistsException exception) {
        return Respond.entityAlreadyExists(exception.getMessage());
    }

    @ExceptionHandler
    public ResponseEntity<Response> apiKeyAlreadyExists(ApiKeyService.ApiKeyAlreadyAddedException exception) {
        return Respond.entityAlreadyExists(exception.getMessage());
    }

    @ExceptionHandler
    public ResponseEntity<Response> handleException(Exception e) {
        return Respond.badRequest(String.format("Bad request! Message: %s", e.getMessage()));
    }

}
