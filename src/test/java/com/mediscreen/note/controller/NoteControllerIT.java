package com.mediscreen.note.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mediscreen.note.domain.Note;
import com.mediscreen.note.dto.NoteDto;
import com.mediscreen.note.repository.INoteRepository;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@Log4j2
@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
class NoteControllerIT {


    @Autowired
    private MockMvc mvc;

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private INoteRepository repository;

    private static NoteDto noteDto;

    @BeforeEach
    public void setUp() {
        mvc = MockMvcBuilders.webAppContextSetup(context).build();
        noteDto = new NoteDto(1,"Benoit","Laick","En recherche de perte de poids, et de sensation très forte","Medecin" ,null,null);
    }

    @AfterEach
    public void delete() {
        repository.findByIdPatient(noteDto.getIdPatient())
                .stream()
                .findAny()
                .ifPresent(note ->
                        {
                            repository.findById(note.getId())
                                    .map(Note::getId)
                                    .ifPresent(deleteNote -> repository.deleteById(deleteNote));
                        });


    }

    public static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    @Tag("PatientAdd")
    @DisplayName("Given a noteDto then save patient note return patient note with code 200")
    public void givenNoteDtoAdd_whenPostRequestSuccess_thenNoteAdd() throws Exception {
        mvc.perform(MockMvcRequestBuilders.post("/patHistory/add")
                .content(asJsonString(noteDto))
                .contentType(APPLICATION_JSON)
                .accept(APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.lastName").value(noteDto.getLastName()))
                .andExpect(jsonPath("$.firstName").value(noteDto.getFirstName()))
                .andExpect(jsonPath("$.practitioner").value(noteDto.getPractitioner()))
                .andExpect(jsonPath("$.note").value(noteDto.getNote()))
                .andExpect(result->assertNotNull(jsonPath("$.createDate")))
                .andExpect(result -> assertNotNull(jsonPath("$.id")));
    }
}
