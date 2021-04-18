package com.mediscreen.note.controller;

import com.mediscreen.note.dto.NoteDto;
import com.mediscreen.note.service.INoteService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;

@Log4j2
@RestController
@RequestMapping(value = "patHistory")
@CrossOrigin(origins = "http://localhost:4200")
public class NoteController {

    @Autowired
    private INoteService service;


    /**
     * New noteDto to add if valid
     *
     * @param noteDto to save
     * @return noteDto when is create else return Error
     */
    @PostMapping("/add")
    public ResponseEntity<NoteDto> addPatient(@RequestBody @Valid NoteDto noteDto) {
        log.info("POST : patHistory/add ");
        try {
            return ResponseEntity.status(HttpStatus.CREATED).body(service.add(noteDto));
        } catch (Exception  exception) {
            log.error("Post : patHistory/add note error : {}", exception.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, exception.getMessage());
        }
    }
}
