package com.mediscreen.note.dto;

import com.googlecode.jmapper.JMapper;
import com.mediscreen.note.domain.Note;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ConfigDto {

    @Bean
    JMapper<NoteDto, Note> noteJMapper(){
        return new JMapper<>(NoteDto.class, Note.class);
    }

    @Bean
    JMapper<Note, NoteDto> noteUnJMapper() {
        return new JMapper<>(Note.class,NoteDto.class);
    }
}
