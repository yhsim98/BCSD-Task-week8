package controller;

import exception.ConflictException;
import exception.NotFoundException;
import exception.UnauthorizedException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ExceptionController {

    @ExceptionHandler({ConflictException.class})
    public ResponseEntity handleException(final ConflictException e){
        return new ResponseEntity(e.getMessage(), HttpStatus.CONFLICT);
    }

    @ExceptionHandler({NotFoundException.class})
    public ResponseEntity conflictException(final NotFoundException e){
        return new ResponseEntity(e.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({UnauthorizedException.class})
    public ResponseEntity unauthorizedException(final UnauthorizedException e){
        return new ResponseEntity(e.getMessage(), HttpStatus.UNAUTHORIZED);
    }

}
