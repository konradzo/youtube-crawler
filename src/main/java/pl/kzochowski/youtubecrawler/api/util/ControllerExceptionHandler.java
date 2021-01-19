package pl.kzochowski.youtubecrawler.api.util;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import pl.kzochowski.youtubecrawler.service.ApiKeyService;
import pl.kzochowski.youtubecrawler.service.ChannelService;

import java.time.LocalDateTime;

@ControllerAdvice
public class ControllerExceptionHandler {

    @ExceptionHandler({ChannelService.ChannelAlreadyExistsException.class, ApiKeyService.ApiKeyAlreadyAddedException.class})
    public ResponseEntity<Response> entityAlreadyExists(ChannelService.ChannelAlreadyExistsException exception) {
        ErrorMessage errorMessage = new ErrorMessage(exception.getMessage(), LocalDateTime.now());
        return Respond.entityAlreadyExists(errorMessage, "Entity already exists");
    }

    @ExceptionHandler
    public ResponseEntity<Response> handleException(Exception e) {
        ErrorMessage errorMessage = new ErrorMessage(e.getMessage(), LocalDateTime.now());
        return Respond.badRequest(errorMessage, "Bad request!");
    }

}
