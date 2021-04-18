package com.mediscreen.note.repository;

import com.mediscreen.note.domain.Note;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

/**
 * Interface allowing access to the database
 */
public interface NoteRepository extends MongoRepository<Note,String> {
    /**
     * Find patient by id
     * @param id patient
     * @return List<Note>
     */
    List<Note> findByIdPatient(Integer id);
}
