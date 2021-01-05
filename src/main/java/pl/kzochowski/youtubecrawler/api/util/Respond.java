package pl.kzochowski.youtubecrawler.api.util;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class Respond {

    public static <T> ResponseEntity<Response> ok(T val, String message){
        return new ResponseEntity<>(Response.builder()
                .code(String.valueOf(HttpStatus.OK))
                .data(val)
                .message(message)
                .build(), HttpStatus.OK);
    }

    public static <T> ResponseEntity<Response> created(T val){
        return new ResponseEntity<>(Response.builder()
                .code(String.valueOf(HttpStatus.CREATED))
                .data(val)
                .message("New channel added")
                .build(), HttpStatus.CREATED);
    }

    public static ResponseEntity<Response> badRequest(String message){
        return new ResponseEntity<>(Response.builder()
                .code(String.valueOf(HttpStatus.BAD_REQUEST))
                .message(message)
                .build(), HttpStatus.BAD_REQUEST);
    }
}
