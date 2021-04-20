package com.mediscreen.note.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "note")
public class Note implements Serializable {

    private String id;

    @NotNull(message = "Patient Id can't be empty")
    private Integer idPatient;


    /**
     * Free form note with medical terminology containing all patient history.
     */
    @NotBlank(message = "Note name is mandatory")
    private String note;

    @NotBlank(message = "Practitioner name is mandatory")
    private String practitioner;

    /**
     * Note creation date
     */
    private LocalDate createDate;

    /**
     * Date of the last update of a note
     */
    private LocalDate updateDate;


    public Note(Integer idPatient, String note, String practitioner, LocalDate createDate, LocalDate updateDate) {
        this.idPatient = idPatient;
        this.note = note;
        this.practitioner = practitioner;
        this.createDate = createDate;
        this.updateDate = updateDate;
    }
}
