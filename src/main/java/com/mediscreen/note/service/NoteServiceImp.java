package com.mediscreen.note.service;

import com.googlecode.jmapper.JMapper;
import com.mediscreen.note.domain.Note;
import com.mediscreen.note.dto.NoteDto;
import com.mediscreen.note.repository.INoteRepository;
import com.mediscreen.note.service.exception.NoteNotFoundException;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
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
        noteDto.setCreateDate(LocalDate.now());
        Note note = repository.save(noteUnJMapper.getDestination(noteDto));
        log.info("Service : Note is save in Bdd : {} ", note.getId());
        return noteJMapper.getDestination(note);
    }

    @Override
    public NoteDto update(String id, NoteDto noteDto) {
        Note updateNote = existById(id);
        updateNote.setNote(noteDto.getNote());
        updateNote.setPractitioner(noteDto.getPractitioner());
        updateNote.setUpdateDate(LocalDate.now());
        return noteJMapper.getDestination(repository.save(updateNote));
    }

    @Override
    public List<NoteDto> searchByIdPatient(Integer id) {
        List<NoteDto> noteList = new ArrayList<>();
        repository.findByIdPatient(id)
                .stream()
                .sorted(Comparator.comparing(Note::getCreateDate, Comparator.nullsLast(Comparator.reverseOrder())))
                .sorted(Comparator.comparing(Note::getUpdateDate, Comparator.nullsLast(Comparator.reverseOrder())))
                .forEach(idPatient ->
                {
                    noteList.add(noteJMapper.getDestination(idPatient));
                });

        if (noteList.size() == 0) {
            log.error("There is no not for the patient, id : {}", id);
            throw new NoteNotFoundException("There is no not for the patient, id : " + id);
        }
        log.info("Service : reads the patient's note list  : {} ", noteList.size());
        return noteList;
    }

    @Override
    public Note existById(String id) {
        return repository.findById(id)
                .orElseThrow(() -> new NoteNotFoundException("There is no note with id : " + id));
    }

    @Override
    public Note existByIdPatientAndPractitioner(Integer id, String practitioner) {
        return null;
    }

    @Override
    public void delete(String id) {
        repository.deleteById(existById(id).getId());
    }
}
