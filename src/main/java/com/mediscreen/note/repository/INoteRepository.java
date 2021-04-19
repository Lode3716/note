package com.mediscreen.note.repository;

import com.mediscreen.note.domain.Note;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Interface allowing access to the database
 */
@Repository
public interface INoteRepository extends MongoRepository<Note,String> {
    /**
     * Find patient by id
     * @param id patient
     * @return List<Note>
     */
    List<Note> findByIdPatient(Integer id);


    /**
     * Find patient by id and note of practitioner
     * @param id patient
     * @param id practitioner
     * @return patient note drafted by the practitioner
     */
    Note findByIdAndAndPractitioner(Integer id,String practitioner);
}
