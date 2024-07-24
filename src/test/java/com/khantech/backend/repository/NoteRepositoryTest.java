package com.khantech.backend.repository;

import com.khantech.backend.dto.NoteTextDTO;
import com.khantech.backend.dto.NoteTitleCreatedDateDTO;
import com.khantech.backend.model.Note;
import com.khantech.backend.model.Tag;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.testcontainers.containers.MongoDBContainer;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@DirtiesContext
public class NoteRepositoryTest {

    @Autowired
    private NoteRepository noteRepository;
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
    public void giveNoteRepository_whenSaveAndRetrieveNoteText_thenOK() {
        Note saved = noteRepository.save(new Note("note 1", "this this is the note text note note", List.of(Tag.BUSINESS)));

        Optional<NoteTextDTO> noteTextById = noteRepository.getNoteTextById(saved.getId());

        assertThat(noteTextById.isPresent()).isTrue();
        assertThat(noteTextById.get().getText()).isEqualTo("this this is the note text note note");
        assertThat(noteTextById.get().getId()).isEqualTo(saved.getId());
    }

    @Test
    public void giveNoteRepository_whenSaveAndRetrieveTitleByTag_thenOK() {
        noteRepository.save(new Note("note 1", "this this is the note text note note 1", List.of(Tag.BUSINESS)));
        noteRepository.save(new Note("note 2", "this this is the note text note note 2", List.of(Tag.PERSONAL)));

        Tag[] tags = {Tag.BUSINESS,Tag.PERSONAL};
        List<NoteTitleCreatedDateDTO> notesNoteTitleCreatedDateDTOS = noteRepository.getNoteTitlesAndDateByTags(tags);

        assertThat(notesNoteTitleCreatedDateDTOS.isEmpty()).isFalse();
        assertThat(notesNoteTitleCreatedDateDTOS.size()).isEqualTo(2);
        assertThat(notesNoteTitleCreatedDateDTOS.get(1).getTitle()).isEqualTo("note 1");
        assertThat(notesNoteTitleCreatedDateDTOS.get(0).getTitle()).isEqualTo("note 2");
    }

    @Test
    public void giveNoteRepository_whenSave_thenOK() {
        Note saved = noteRepository.save(new Note("note 1", "this this is the note text note note", List.of(Tag.BUSINESS)));
        assertThat(saved.getId()).isNotNull();
    }

    @Test
    public void giveNoteRepository_whenDelete_thenOK() {
        Note saved = noteRepository.save(new Note("note 1", "this this is the note text note note", List.of(Tag.BUSINESS)));
        boolean deleted = noteRepository.delete(saved.getId());
        assertThat(deleted).isTrue();
    }

}