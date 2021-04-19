package com.mediscreen.note.controller;

import com.mediscreen.note.dto.NoteDto;
import com.mediscreen.note.service.INoteService;
import com.mediscreen.note.service.exception.NoteNotFoundException;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

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
        } catch (Exception exception) {
            log.error("Post : patHistory/add note error : {}", exception.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, exception.getMessage());
        }
    }

    /**
     * Send noteDto list.
     *
     * @param id patient
     * @return patient note list
     */
    @GetMapping("/{id}")
    public ResponseEntity<List<NoteDto>> getNoteByIdPatient(@PathVariable("id") @NotNull Integer id) {
        log.info("GET : patHistory{}", id);
        try {
            return ResponseEntity.status(HttpStatus.OK).body(service.searchByIdPatient(id));
        } catch (NoteNotFoundException noteNotFoundException) {
            log.error("GET : /patHistory/ - Not found : {}", noteNotFoundException.getMessage());
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, noteNotFoundException.getMessage());
        }
    }

    /**
     * Patient note to update
     *
     * @param id      to update
     * @param noteDto the entity update
     * @return noteDto when is update
     */
    @PutMapping("/{id}")
    public ResponseEntity<NoteDto> updateNotePatient(@PathVariable("id") @NotBlank String id, @RequestBody @Valid NoteDto noteDto) {
        log.info("PUT : /note/{}", id);
        NoteDto update;
        try {
            update = service.update(id, noteDto);
        } catch (NoteNotFoundException noteNotFoundException) {
            log.error("PUT : /patHistory/{} - Not found : {}", noteNotFoundException.getMessage());
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, noteNotFoundException.getMessage());
        }
        log.info("PUT : /patHistory/{} - SUCCESS", id);
        return ResponseEntity.status(HttpStatus.OK).body(update);
    }

}
