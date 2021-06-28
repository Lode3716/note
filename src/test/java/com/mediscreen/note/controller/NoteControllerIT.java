package com.mediscreen.note.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mediscreen.note.domain.Note;
import com.mediscreen.note.dto.NoteDto;
import com.mediscreen.note.repository.INoteRepository;
import lombok.extern.log4j.Log4j2;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.context.WebApplicationContext;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assumptions.assumeFalse;
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

    private static NoteDto updateNoteDto;

    private static NoteDto noteDtoFail;

    private static Note note;

    @BeforeEach
    public void setUp() {
        mvc = MockMvcBuilders.webAppContextSetup(context).build();
        noteDto = new NoteDto(1, "En recherche de perte de poids, et de sensation très forte", "Medecin", null, null);
        note = new Note(1, "En recherche de perte de poids, et de sensation très forte", "Medecin", LocalDate.now(), null);

        updateNoteDto = new NoteDto(1, "En recherche de perte de poids, et en perdu énormément", "Medecin", null, null);

        noteDtoFail = new NoteDto();
    }

    @AfterEach
    public void delete() {
        repository.findByIdPatient(noteDto.getIdPatient())
                .stream()
                .findAny()
                .ifPresent(findNote ->
                {
                    repository.findById(findNote.getId())
                            .map(Note::getId)
                            .ifPresent(deleteNote -> repository.deleteById(deleteNote));
                });

        repository.findByIdPatient(note.getIdPatient())
                .stream()
                .findAny()
                .ifPresent(findNote ->
                {
                    repository.findById(findNote.getId())
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
    @Tag("NoteAdd")
    @DisplayName("Given a noteDto then save patient note return patient note with code 200")
    public void givenNoteDtoAdd_whenPostRequestSuccess_thenNoteAdd() throws Exception {
        mvc.perform(MockMvcRequestBuilders.post("/patHistory/add")
                .content(asJsonString(noteDto))
                .contentType(APPLICATION_JSON)
                .accept(APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.practitioner").value(noteDto.getPractitioner()))
                .andExpect(jsonPath("$.note").value(noteDto.getNote()))
                .andExpect(result -> assertNotNull(jsonPath("$.createDate")))
                .andExpect(result -> assertNotNull(jsonPath("$.id")));
    }

    @Test
    @Tag("PatientAdd")
    @DisplayName("Given a note are not valid return error")
    public void givenNoteDtoAdd_whenPostRequestFail_thenNoteError() throws Exception {
        mvc.perform(MockMvcRequestBuilders.post("/patHistory/add")
                .content(asJsonString(noteDtoFail))
                .contentType(APPLICATION_JSON)
                .accept(APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof MethodArgumentNotValidException))
                .andExpect(result -> assertTrue(result.getResolvedException().getMessage().contains("Patient Id can't be empty")
                        && result.getResolvedException().getMessage().contains("Note name is mandatory")
                        && result.getResolvedException().getMessage().contains("Practitioner name is mandatory")));
    }

    @Test
    @Tag("NoteByIdPatient")
    @DisplayName("Given a save patient in bdd,check if list return equals 2 patients")
    public void givenNoteByIdPatient_whenGETRequestFail_thenReturnNoteNotFound() throws Exception {

        String url = "/patHistory/".concat(String.valueOf(12345));

        mvc.perform(MockMvcRequestBuilders.get(url)
                .accept(APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(result -> assertTrue(result.getResolvedException().getMessage().contains("There is no not for the patient, id :")));
    }

    @Test
    @Tag("NoteByIdPatient")
    @DisplayName("Given a save patient in bdd,check if list return equals 1 note")
    public void givenNoteByIdPatient_whenGETRequestSucess_thenReturnNoteSucess() throws Exception {

        Note save = repository.save(note);

        String url = "/patHistory/".concat(String.valueOf(save.getIdPatient()));

        mvc.perform(MockMvcRequestBuilders.get(url)
                .accept(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", Matchers.hasSize(1)));
    }

    @Test
    @Tag("UpdateNote")
    @DisplayName("Given a update note in bdd,check if update is good")
    public void givenNoteByIdPatient_whenGETRequestUpadteNoteSucess_thenReturnNoteSucess() throws Exception {

        Note save = repository.save(note);

        String url = "/patHistory/".concat(save.getId());

        mvc.perform(MockMvcRequestBuilders.put(url)
                .content(asJsonString(updateNoteDto))
                .contentType(APPLICATION_JSON)
                .accept(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.practitioner").value(updateNoteDto.getPractitioner()))
                .andExpect(jsonPath("$.note").value(updateNoteDto.getNote()))
                .andExpect(jsonPath("$.id").value(save.getId()))
                .andExpect(result -> assertNotNull(jsonPath("$.updateDate")));
    }


    @Test
    @Tag("UpdateNote")
    @DisplayName("Given a update note in bdd, but id not found ")
    public void givenNoteByIdPatient_whenGETRequestUpadteNoteFail_thenReturnNoteFail() throws Exception
    {

        String url = "/patHistory/".concat("12365478");

        mvc.perform(MockMvcRequestBuilders.put(url)
                .content(asJsonString(updateNoteDto))
                .contentType(APPLICATION_JSON)
                .accept(APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(result -> assertTrue(result.getResolvedException().getMessage().contains("There is no note with id :")));
    }


    @Test
    @Tag("DeleteNote")
    @DisplayName("Given patient note delete in bdd ")
    public void givenNoteByIdPatient_whenGETRequestDelete_thenReturnOK() throws Exception
    {
        Note save = repository.save(note);
        String url = "/patHistory/".concat(save.getId());

        mvc.perform(MockMvcRequestBuilders.delete(url)
                .content(asJsonString(updateNoteDto))
                .contentType(APPLICATION_JSON)
                .accept(APPLICATION_JSON))
                .andExpect(status().isOk());

        assumeFalse(repository.existsById(save.getId()));

    }

    @Test
    @Tag("DeleteNote")
    @DisplayName("Given patient note delete npt found in bdd ")
    public void givenNoteByIdPatient_whenGETRequestDeleteNoteFail_thenReturnFail() throws Exception
    {
        String url = "/patHistory/".concat("azdfh123");

        mvc.perform(MockMvcRequestBuilders.delete(url)
                .content(asJsonString(updateNoteDto))
                .contentType(APPLICATION_JSON)
                .accept(APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(result -> assertTrue(result.getResolvedException().getMessage().contains("There is no note with id :")));

    }
}
