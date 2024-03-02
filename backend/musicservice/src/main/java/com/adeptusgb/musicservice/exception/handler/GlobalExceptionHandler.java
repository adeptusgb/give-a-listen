package com.adeptusgb.musicservice.exception.handler;

import com.adeptusgb.musicservice.exception.AlbumNotFoundException;
import com.adeptusgb.musicservice.exception.ArtistNotFoundException;
import com.adeptusgb.musicservice.exception.ExternalServiceCommunicationException;
import com.adeptusgb.musicservice.exception.TrackNotFoundException;
import org.hibernate.TypeMismatchException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

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
                .body(" No content found for the given query.");
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

    @ExceptionHandler ({
            IllegalArgumentException.class
    })
    public ResponseEntity<Object> handleIllegalArgumentException(IllegalArgumentException exception) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(exception.getMessage());
    }

    @ExceptionHandler ({
            TypeMismatchException.class,
            MethodArgumentTypeMismatchException.class
    })
    public ResponseEntity<Object> handleTypeMismatchException(TypeMismatchException exception) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body("Invalid input type.");
    }
}
