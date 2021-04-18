package com.mediscreen.note.service;

import com.googlecode.jmapper.JMapper;
import com.mediscreen.note.domain.Note;
import com.mediscreen.note.dto.NoteDto;
import com.mediscreen.note.repository.INoteRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Log4j2
@Service
public class NoteServiceImp implements INoteService {

    @Autowired
    INoteRepository repository;

    @Autowired
    JMapper<NoteDto, Note> noteJMapper;

    @Autowired
    JMapper<Note, NoteDto> noteUnJMapper;

    @Override
    public NoteDto add(NoteDto noteDto) {
        log.info("Service : before note is save in Bdd : {} ", noteDto);
        Note note=repository.save(noteUnJMapper.getDestination(noteDto));
        log.info("Service : Note is save in Bdd : {} ", note.getId());
        return noteJMapper.getDestination(note);
    }

    @Override
    public List<NoteDto> readAll() {
        return null;
    }

    @Override
    public NoteDto update(Integer id, NoteDto objet) {
        return null;
    }

    @Override
    public void delete(Integer id) {

    }

    @Override
    public NoteDto readById(Integer id) {
        return null;
    }

    @Override
    public Note existByIdPatient(Integer id) {
        return null;
    }

    @Override
    public Note existByIdPatientAndPractitioner(Integer id, String practitioner) {
        return null;
    }
}
