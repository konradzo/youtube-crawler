package pl.kzochowski.youtubecrawler.api.util;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class Respond {

    public static <T> ResponseEntity<Response> ok(T val, String message) {
        return new ResponseEntity<>(Response.builder()
                .code(String.valueOf(HttpStatus.OK))
                .data(val)
                .message(message)
                .build(), HttpStatus.OK);
    }

    public static <T> ResponseEntity<Response> created(T val) {
        return new ResponseEntity<>(Response.builder()
                .code(String.valueOf(HttpStatus.CREATED))
                .data(val)
                .message("New entity created")
                .build(), HttpStatus.CREATED);
    }

    public static ResponseEntity<Response> entityAlreadyExists(ErrorMessage errorMessage, String message) {
        return new ResponseEntity<>(Response.builder()
                .code(String.valueOf(HttpStatus.CONFLICT))
                .data(errorMessage)
                .message(message)
                .build(), HttpStatus.CONFLICT);
    }

    public static ResponseEntity<Response> badRequest(ErrorMessage errorMessage, String message) {
        return new ResponseEntity<>(Response.builder()
                .code(String.valueOf(HttpStatus.BAD_REQUEST))
                .message(message)
                .data(errorMessage)
                .build(), HttpStatus.BAD_REQUEST);
    }
}
