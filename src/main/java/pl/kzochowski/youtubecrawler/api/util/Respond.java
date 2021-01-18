package pl.kzochowski.youtubecrawler.api.util;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class Respond {

    //todo message? data?

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

    public static ResponseEntity<Response> entityAlreadyExists(String message) {
        return new ResponseEntity<>(Response.builder()
                .code(String.valueOf(HttpStatus.CONFLICT))
                .message(message)
                .build(), HttpStatus.CONFLICT);
    }

    public static ResponseEntity<Response> badRequest(String message) {
        return new ResponseEntity<>(Response.builder()
                .code(String.valueOf(HttpStatus.BAD_REQUEST))
                .message(message)
                .build(), HttpStatus.BAD_REQUEST);
    }
}
