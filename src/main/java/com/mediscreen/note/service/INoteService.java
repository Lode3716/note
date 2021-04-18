package com.mediscreen.note.service;

import com.mediscreen.note.domain.Note;
import com.mediscreen.note.dto.NoteDto;

import java.util.List;

/**
 * Interface containing all the Note's methods
 */
public interface INoteService extends ICrudService<NoteDto>{

    /**
     * Find Note By id patient
     * @param id
     * @return the patient find or issue NoteNotFoundException
     */
    List<NoteDto> searchByIdPatient(Integer id);

    /**
     * Find patient by id and note of practitioner
     * @param id patient
     * @param id practitioner
     * @return patient note drafted by the practitioner
     */
    Note existByIdPatientAndPractitioner(Integer id, String practitioner);

    /**
     * Find Note By id
     * @param id
     * @return the note find or issue NoteNotFoundException
     */
    Note existById(String id);
}
