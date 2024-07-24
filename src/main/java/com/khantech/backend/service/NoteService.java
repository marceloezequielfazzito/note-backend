package com.khantech.backend.service;

import com.khantech.backend.dto.NoteDTO;
import com.khantech.backend.dto.NoteTextDTO;
import com.khantech.backend.dto.NoteTitleCreatedDateDTO;
import com.khantech.backend.model.Note;
import com.khantech.backend.model.Tag;
import com.khantech.backend.repository.NoteRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class NoteService {

    private static final Logger logger = LoggerFactory.getLogger(NoteService.class);
    private final NoteRepository noteRepository;

    @Autowired
    public NoteService(NoteRepository noteRepository) {
        this.noteRepository = noteRepository;
    }

    public NoteDTO save(NoteDTO request) {
        logger.info("Saving new note {}",request);
        Note note = new Note(request);
        Note saved = noteRepository.save(note);
        return new NoteDTO(saved);
    }

    public NoteDTO update(NoteDTO request) {
        logger.info("Updating note {}",request);
        Note note = noteRepository.findOne(request.getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatusCode.valueOf(HttpStatus.NOT_FOUND.value()), String.format("note id:  %s not found", request.getId())));
        note.setTitle(request.getTitle());
        note.setText(request.getText());
        List<Tag> tags = request.getTags();
        if (tags != null && !tags.isEmpty()) {
            note.setTags(tags);
        }
        Note updated = noteRepository.save(note);
        return new NoteDTO(updated);
    }

    public void delete(String id) {
        logger.info("Deleting note {}",id);
        boolean deleted = noteRepository.delete(id);
        if (!deleted){
            throw new ResponseStatusException(HttpStatusCode.valueOf(HttpStatus.NOT_FOUND.value()), String.format("note id: %s not found", id));
        }
    }

    public NoteTextDTO findNoteTextById(String id) {
        logger.info("Finding note text by id {}",id);
        NoteTextDTO noteTextById = noteRepository.getNoteTextById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatusCode.valueOf(HttpStatus.NOT_FOUND.value()), String.format("note id:  %s not found", id)));
        logger.info("Found note text by id {}",noteTextById);
        return noteTextById;
    }

    public List<NoteTitleCreatedDateDTO> getAllNoteTitleCreatedDate(Tag[] tags) {
        logger.info("Finding all note titles ");
        return noteRepository.getNoteTitlesAndDateByTags(tags);
    }

    public Map<String, Integer> getTextStatsById(String id) {
        logger.info("getting text stats from note id {}",id);
        NoteTextDTO noteTextById = noteRepository.getNoteTextById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatusCode.valueOf(HttpStatus.NOT_FOUND.value()), String.format("note id:  %s not found", id)));
        Map<String, Integer> orderedStats = Arrays.stream(noteTextById.getText().split(" "))
                .collect(Collectors.toMap(String::toLowerCase, i -> 1, Integer::sum))
                .entrySet()
                .stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));
        logger.info("text stats from note id {} {} ",id, orderedStats);
        return orderedStats;
    }


}
