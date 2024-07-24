package com.khantech.backend.service;


import com.khantech.backend.dto.NoteDTO;
import com.khantech.backend.dto.NoteTextDTO;
import com.khantech.backend.dto.NoteTitleCreatedDateDTO;
import com.khantech.backend.model.Note;
import com.khantech.backend.model.Tag;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.web.server.ResponseStatusException;
import org.testcontainers.containers.MongoDBContainer;

import java.util.List;
import java.util.Map;

@SpringBootTest
@DirtiesContext
public class NoteServiceTest {

    @Autowired
    private MongoTemplate mongoTemplate;
    @Autowired
    private NoteService noteService;
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
        mongoTemplate.dropCollection(Note.class);
    }

    @Test
    public void givenNoteService_whenSaveAndRetrieveNoteText_thenOK() {

        NoteDTO saved = noteService.save(new NoteDTO("note 1", "this this is the note text note note", List.of(Tag.BUSINESS)));

        NoteTextDTO noteText = noteService.findNoteTextById(saved.getId());

        assertThat(noteText).isNotNull();
        assertThat(noteText.getText()).isEqualTo("this this is the note text note note");
        assertThat(noteText.getId()).isEqualTo(saved.getId());
    }

    @Test
    public void givenNoteService_whenRetrieveNonExistingNoteText_thenThrowException() {
        try {
            noteService.findNoteTextById("qwerty");
            failBecauseExceptionWasNotThrown(ResponseStatusException.class);
        } catch (ResponseStatusException exception) {
            assertThat(exception.getMessage()).isEqualTo("404 NOT_FOUND \"note id:  qwerty not found\"");
        }
    }

    @Test
    public void givenNoteService_whenSaveAndUpdateFullNote_thenOK() {

        NoteDTO saved = noteService.save(new NoteDTO("note 1", "this this is the note text note note", List.of(Tag.BUSINESS)));
        NoteDTO request = new NoteDTO(saved.getId(), "note 1 change", "this this is the note text note note change", List.of(Tag.PERSONAL));

        NoteDTO updated = noteService.update(request);

        assertThat(updated).isNotNull();

        assertThat(updated.getId()).isEqualTo(saved.getId());
        assertThat(updated.getText()).isEqualTo("this this is the note text note note change");
        assertThat(updated.getTitle()).isEqualTo("note 1 change");
        assertThat(updated.getTags()).isEqualTo(List.of(Tag.PERSONAL));


    }

    @Test
    public void givenNoteService_whenSaveAndUpdateNoteTitleAndText_thenOK() {

        NoteDTO saved = noteService.save(new NoteDTO("note 1", "this this is the note text note note", List.of(Tag.BUSINESS)));
        NoteDTO request = new NoteDTO(saved.getId(), "note 1 change", "this this is the note text note note change", List.of());

        NoteDTO updated = noteService.update(request);

        assertThat(updated).isNotNull();

        assertThat(updated.getId()).isEqualTo(saved.getId());
        assertThat(updated.getText()).isEqualTo("this this is the note text note note change");
        assertThat(updated.getTitle()).isEqualTo("note 1 change");
        assertThat(updated.getTags()).isEqualTo(List.of(Tag.BUSINESS));


    }

    @Test
    public void givenNoteService_whenUpdateNonExistingNote_thenThrowException() {
        try {
            NoteDTO request = new NoteDTO("qwerty", "note 1 change", "this this is the note text note note change", List.of());
            noteService.update(request);
            failBecauseExceptionWasNotThrown(ResponseStatusException.class);
        } catch (ResponseStatusException exception) {
            assertThat(exception.getMessage()).isEqualTo("404 NOT_FOUND \"note id:  qwerty not found\"");
        }
    }

    @Test
    public void givenNoteService_whenSaveAnDeleteNote_thenOK() {
        NoteDTO saved = noteService.save(new NoteDTO("note 1", "this this is the note text note note", List.of(Tag.BUSINESS)));
        noteService.delete(saved.getId());

        Note note = mongoTemplate.findById(saved.getId(),Note.class);
        assertThat(note).isNull();

    }


    @Test
    public void givenNoteService_whenSaveAndGetTitleWithEmptyTag_thenOK() {

        noteService.save(new NoteDTO("note 1", "note text 1", List.of(Tag.BUSINESS)));
        noteService.save(new NoteDTO("note 2", "note text 2", List.of(Tag.IMPORTANT)));
        noteService.save(new NoteDTO("note 3", "note text 3", List.of(Tag.PERSONAL)));
        noteService.save(new NoteDTO("note 4", "note text 4", List.of(Tag.BUSINESS, Tag.IMPORTANT)));
        noteService.save(new NoteDTO("note 5", "note text 5", List.of(Tag.PERSONAL, Tag.IMPORTANT)));
        noteService.save(new NoteDTO("note 6", "note text 5", List.of()));


        List<NoteTitleCreatedDateDTO> allNoteTitleCreatedDate = noteService.getAllNoteTitleCreatedDate(null);

        assertThat(allNoteTitleCreatedDate.size()).isEqualTo(6);
        assertThat(allNoteTitleCreatedDate.get(0).getTitle()).isEqualTo("note 6");
        assertThat(allNoteTitleCreatedDate.get(1).getTitle()).isEqualTo("note 5");
        assertThat(allNoteTitleCreatedDate.get(2).getTitle()).isEqualTo("note 4");
        assertThat(allNoteTitleCreatedDate.get(3).getTitle()).isEqualTo("note 3");
        assertThat(allNoteTitleCreatedDate.get(4).getTitle()).isEqualTo("note 2");
        assertThat(allNoteTitleCreatedDate.get(5).getTitle()).isEqualTo("note 1");


    }

    @Test
    public void givenNoteService_whenSaveAndGetTitleWithTag_thenOK() {

        noteService.save(new NoteDTO("note 1", "note text 1", List.of(Tag.BUSINESS)));
        noteService.save(new NoteDTO("note 2", "note text 2", List.of(Tag.IMPORTANT)));
        noteService.save(new NoteDTO("note 3", "note text 3", List.of(Tag.PERSONAL)));
        noteService.save(new NoteDTO("note 4", "note text 4", List.of(Tag.BUSINESS, Tag.IMPORTANT)));
        noteService.save(new NoteDTO("note 5", "note text 5", List.of(Tag.PERSONAL, Tag.IMPORTANT)));
        noteService.save(new NoteDTO("note 6", "note text 5", List.of()));


        Tag[] tags = {Tag.BUSINESS};
        List<NoteTitleCreatedDateDTO> allNoteTitleCreatedDateBusiness = noteService.getAllNoteTitleCreatedDate(tags);

        Tag[] tags2 = {Tag.PERSONAL};
        List<NoteTitleCreatedDateDTO> allNoteTitleCreatedDatePersonal = noteService.getAllNoteTitleCreatedDate(tags2);

        Tag[] tags3 = {Tag.BUSINESS, Tag.IMPORTANT};
        List<NoteTitleCreatedDateDTO> allNoteTitleCreatedDateBusinessImportant = noteService.getAllNoteTitleCreatedDate(tags3);


        assertThat(allNoteTitleCreatedDateBusiness.size()).isEqualTo(2);
        assertThat(allNoteTitleCreatedDateBusiness.get(0).getTitle()).isEqualTo("note 4");
        assertThat(allNoteTitleCreatedDateBusiness.get(1).getTitle()).isEqualTo("note 1");

        assertThat(allNoteTitleCreatedDatePersonal.size()).isEqualTo(2);
        assertThat(allNoteTitleCreatedDatePersonal.get(0).getTitle()).isEqualTo("note 5");
        assertThat(allNoteTitleCreatedDatePersonal.get(1).getTitle()).isEqualTo("note 3");

        assertThat(allNoteTitleCreatedDateBusinessImportant.size()).isEqualTo(4);
        assertThat(allNoteTitleCreatedDateBusinessImportant.get(0).getTitle()).isEqualTo("note 5");
        assertThat(allNoteTitleCreatedDateBusinessImportant.get(1).getTitle()).isEqualTo("note 4");
        assertThat(allNoteTitleCreatedDateBusinessImportant.get(2).getTitle()).isEqualTo("note 2");
        assertThat(allNoteTitleCreatedDateBusinessImportant.get(3).getTitle()).isEqualTo("note 1");

    }


    @Test
    public void givenNoteService_whenSaveAndGetStats_thenOK() {

        NoteDTO saved = noteService.save(new NoteDTO("note 1", "this this is the note text note note", List.of(Tag.BUSINESS)));

        Map<String, Integer> stats = noteService.getTextStatsById(saved.getId());

        assertThat(stats.size()).isEqualTo(5);
        assertThat(stats.get("note")).isEqualTo(3);
        assertThat(stats.get("this")).isEqualTo(2);
        assertThat(stats.get("the")).isEqualTo(1);
        assertThat(stats.get("is")).isEqualTo(1);
        assertThat(stats.get("text")).isEqualTo(1);


    }

}
