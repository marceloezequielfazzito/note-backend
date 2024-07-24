package com.khantech.backend.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.khantech.backend.dto.NoteDTO;
import com.khantech.backend.dto.NoteTextDTO;
import com.khantech.backend.dto.NoteTitleCreatedDateDTO;
import com.khantech.backend.model.Note;
import com.khantech.backend.model.Tag;
import com.khantech.backend.repository.NoteRepository;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.test.annotation.DirtiesContext;
import org.testcontainers.containers.MongoDBContainer;


import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext
public class NoteControllerTest {

    @Autowired
    private NoteRepository noteRepository;
    @LocalServerPort
    private int port;
    @Autowired
    private ObjectMapper objectMapper;

    public static TestRestTemplate restTemplate = new TestRestTemplate();
    public static MongoDBContainer mongoDBContainer;

    @BeforeAll
    public static void setUp() {
        mongoDBContainer = new MongoDBContainer("mongo:8.0.0-rc13-jammy").withExposedPorts(27017);
        mongoDBContainer.start();
        System.setProperty("MONGO_PORT", mongoDBContainer.getMappedPort(27017).toString());
        System.setProperty("MONGO_HOST", mongoDBContainer.getHost());
    }

    @AfterAll
    public static void tearDown() {
        mongoDBContainer.stop();
    }

    @AfterEach
    public void after() {
        noteRepository.deleteAll();
    }

    @Test
    public void giveNoteController_whenGetNoteTitle_thenOK(){
        noteRepository.save(new Note("note 1", "this this is the note text note note", List.of(Tag.BUSINESS)));

        HttpEntity<String> entity = new HttpEntity<>(null, new HttpHeaders());

        ResponseEntity<String> response = restTemplate.exchange(
                createURLWithPort("/api/v1/notes?tags=BUSINESS"),
                HttpMethod.GET, entity, String.class);


        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        List<NoteTitleCreatedDateDTO> dtos = null;
        try {
            dtos = objectMapper.readValue(response.getBody(),new TypeReference<List<NoteTitleCreatedDateDTO>>(){});
        } catch (JsonProcessingException e) {
            fail(e.getMessage());
        }

        assertThat(dtos.isEmpty()).isFalse();
        assertThat(dtos.size()).isEqualTo(1);
        assertThat(dtos.get(0).getTitle()).isEqualTo("note 1");
    }



    @Test
    public void giveNoteController_whenGetNoteText_thenOK(){
        Note saved = noteRepository.save(new Note("note 1", "this this is the note text note note", List.of(Tag.BUSINESS)));

        HttpEntity<String> entity = new HttpEntity<>(null, new HttpHeaders());

        ResponseEntity<String> response = restTemplate.exchange(
                createURLWithPort("/api/v1/notes/"+saved.getId())+"/text",
                HttpMethod.GET, entity, String.class);


        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        NoteTextDTO dto = null;
        try {
            dto = objectMapper.readValue(response.getBody(),NoteTextDTO.class);
        } catch (JsonProcessingException e) {
            fail(e.getMessage());
        }

        assertThat(dto.getText()).isEqualTo(saved.getText());
    }

    @Test
    public void giveNoteController_whenGetNoteStats_thenOK(){
        Note saved = noteRepository.save(new Note("note 1", "this this is the note text note note", List.of(Tag.BUSINESS)));

        HttpEntity<String> entity = new HttpEntity<>(null, new HttpHeaders());

        ResponseEntity<String> response = restTemplate.exchange(
                createURLWithPort("/api/v1/notes/"+saved.getId())+"/stats",
                HttpMethod.GET, entity, String.class);


        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        Map<String, Integer> stats  = null;
        try {
            stats = objectMapper.readValue(response.getBody(), new TypeReference<Map<String, Integer>>() {});
        } catch (JsonProcessingException e) {
            fail(e.getMessage());
        }

        assertThat(stats.size()).isEqualTo(5);
        assertThat(stats.get("note")).isEqualTo(3);
        assertThat(stats.get("this")).isEqualTo(2);
        assertThat(stats.get("the")).isEqualTo(1);
        assertThat(stats.get("is")).isEqualTo(1);
        assertThat(stats.get("text")).isEqualTo(1);;
    }

    @Test
    public void giveNoteController_whenPostNote_thenOK(){

        NoteDTO noteDTO = new NoteDTO("note 1", "this this is the note text note note", List.of(Tag.BUSINESS));

        HttpEntity<NoteDTO> entity = new HttpEntity<NoteDTO>(noteDTO, new HttpHeaders());

        ResponseEntity<String> response = restTemplate.exchange(
                createURLWithPort("/api/v1/notes"),
                HttpMethod.POST, entity, String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        NoteDTO entityResponse = null;
        try {
            entityResponse = objectMapper.readValue(response.getBody(), NoteDTO.class);
        } catch (JsonProcessingException e) {
            fail(e.getMessage());
        }
        assertThat(entityResponse.getTitle()).isEqualTo(noteDTO.getTitle());
        assertThat(entityResponse.getText()).isEqualTo(noteDTO.getText());
        assertThat(entityResponse.getTags()).isEqualTo(noteDTO.getTags());

    }


    @Test
    public void giveNoteController_whenPutNote_thenOK(){

        Note saved = noteRepository.save(new Note("note 1", "this this is the note text note note", List.of(Tag.BUSINESS)));

        NoteDTO noteDTO = new NoteDTO(saved.getId() , "note 1 change", "this this is the note text note note change", List.of(Tag.PERSONAL));

        HttpEntity<NoteDTO> entity = new HttpEntity<NoteDTO>(noteDTO, new HttpHeaders());

        ResponseEntity<String> response = restTemplate.exchange(
                createURLWithPort("/api/v1/notes"),
                HttpMethod.PUT, entity, String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        NoteDTO entityResponse = null;
        try {
            entityResponse = objectMapper.readValue(response.getBody(), NoteDTO.class);
        } catch (JsonProcessingException e) {
            fail(e.getMessage());
        }
        assertThat(entityResponse.getTitle()).isEqualTo(noteDTO.getTitle());
        assertThat(entityResponse.getText()).isEqualTo(noteDTO.getText());
        assertThat(entityResponse.getTags()).isEqualTo(noteDTO.getTags());
        assertThat(entityResponse.getId()).isEqualTo(saved.getId());


    }

    private String createURLWithPort(String uri) {
        return "http://localhost:" + port + uri;
    }




}
