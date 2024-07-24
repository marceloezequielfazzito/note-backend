package com.khantech.backend.controller;

import com.khantech.backend.dto.NoteDTO;
import com.khantech.backend.dto.NoteTextDTO;
import com.khantech.backend.dto.NoteTitleCreatedDateDTO;
import com.khantech.backend.model.Tag;
import com.khantech.backend.service.NoteService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController()
@RequestMapping("/v1/notes")
public class NoteController {

    private final NoteService noteService;

    @Autowired
    public NoteController(NoteService noteService) {
        this.noteService = noteService;
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody ResponseEntity<?> createNote(@Valid @RequestBody NoteDTO note) {
        NoteDTO saved = noteService.save(note);
        return ResponseEntity.ok(saved);
    }

    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody ResponseEntity<?> updateNote(@Valid @RequestBody NoteDTO note) {
        NoteDTO updated = noteService.update(note);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody ResponseEntity<?> deleteNote(@PathVariable String id) {
        noteService.delete(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping(value = "/{id}/stats", produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody ResponseEntity<?> getNoteStats(@PathVariable String id) {
        Map<String, Integer> textStatsById = noteService.getTextStatsById(id);
        return ResponseEntity.ok(textStatsById);
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody ResponseEntity<?> getNotes(@RequestParam(value = "tags", required = false) Tag[] tags) {
        List<NoteTitleCreatedDateDTO> allNoteTitleCreatedDate = noteService.getAllNoteTitleCreatedDate(tags);
        return ResponseEntity.ok(allNoteTitleCreatedDate);
    }

    @GetMapping(value = "/{id}/text", produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody ResponseEntity<?> getNoteText(@PathVariable String id) {
        NoteTextDTO noteTextDTO = noteService.findNoteTextById(id);
        return ResponseEntity.ok(noteTextDTO);
    }

}
