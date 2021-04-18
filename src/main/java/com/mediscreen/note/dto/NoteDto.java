package com.mediscreen.note.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import com.googlecode.jmapper.annotations.JGlobalMap;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JGlobalMap
public class NoteDto {

    private String id;

    @NotNull(message = "Patient Id can't be empty")
    private Integer idPatient;

    private String FirstName;

    private String LastName;

    @NotBlank(message = "Note name is mandatory")
    private String note;

    @NotBlank(message = "Practitioner name is mandatory")
    private String practitioner;

    private LocalDate createDate;

    private LocalDate updateDate;
}
