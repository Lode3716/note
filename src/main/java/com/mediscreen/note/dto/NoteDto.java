package com.mediscreen.note.dto;

import com.googlecode.jmapper.annotations.JGlobalMap;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JGlobalMap
public class NoteDto {

    private String id;

    @NotNull(message = "Patient Id can't be empty")
    private Integer idPatient;

    @NotBlank(message = "Note name is mandatory")
    private String note;

    @NotBlank(message = "Practitioner name is mandatory")
    private String practitioner;

    private LocalDate createDate;

    private LocalDate updateDate;

    public NoteDto(Integer idPatient, String note, String practitioner, LocalDate createDate, LocalDate updateDate) {
        this.idPatient = idPatient;
        this.note = note;
        this.practitioner = practitioner;
        this.createDate = createDate;
        this.updateDate = updateDate;
    }
}
