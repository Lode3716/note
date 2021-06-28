package com.mediscreen.note.service.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class NoteNotFoundException extends RuntimeException  {

    public NoteNotFoundException() {
        super();
    }

    public NoteNotFoundException(String message) {
        super(message);
    }
}
