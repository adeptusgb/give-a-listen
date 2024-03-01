package com.adeptusgb.musicservice.exception.handler;

import com.adeptusgb.musicservice.exception.AlbumNotFoundException;
import com.adeptusgb.musicservice.exception.ArtistNotFoundException;
import com.adeptusgb.musicservice.exception.ExternalServiceCommunicationException;
import com.adeptusgb.musicservice.exception.TrackNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler ({
            TrackNotFoundException.class,
            ArtistNotFoundException.class,
            AlbumNotFoundException.class
    })
    public ResponseEntity<Object> handleEntityNotFound(TrackNotFoundException exception) {
        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .body(exception.getMessage());
    }

    @ExceptionHandler ({
            ExternalServiceCommunicationException.class
    })
    public ResponseEntity<Object> handleExternalServiceCommunicationException(ExternalServiceCommunicationException exception) {
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(exception.getMessage());
    }

    @ExceptionHandler ({
            RuntimeException.class
    })
    public ResponseEntity<Object> handleRuntimeException(RuntimeException exception) {
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(exception.getMessage());
    }
}
