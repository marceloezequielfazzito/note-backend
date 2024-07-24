package com.khantech.backend.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.khantech.backend.model.Note;
import com.khantech.backend.model.Tag;
import jakarta.validation.constraints.NotBlank;

import java.time.LocalDateTime;
import java.util.List;
import java.util.StringJoiner;

public class NoteDTO {

    @JsonProperty("id")
    private String id;
    @JsonProperty("title")
    @NotBlank(message = "title is mandatory")
    private String title;
    @JsonProperty("text")
    @NotBlank(message = "text is mandatory")
    private String text;
    @JsonProperty("tags")
    private List<Tag> tags;
    @JsonProperty("createdDate")
    private LocalDateTime dateCreated;

    @JsonCreator
    public NoteDTO(@JsonProperty("id") String id,
                   @JsonProperty("title") String title,
                   @JsonProperty("text") String text,
                   @JsonProperty("tags") List<Tag> tags,
                   @JsonProperty("createdDate") LocalDateTime dateCreated) {
        this.id = id;
        this.title = title;
        this.text = text;
        this.tags = tags;
        this.dateCreated = dateCreated;
    }

    public NoteDTO(String title, String text, List<Tag> tags) {
        this.title = title;
        this.text = text;
        this.tags = tags;
    }

    public NoteDTO(String id, String title, String text, List<Tag> tags) {
        this.id = id;
        this.title = title;
        this.text = text;
        this.tags = tags;
    }

    public NoteDTO(Note note) {
        this.id = note.getId();
        this.title = note.getTitle();
        this.text = note.getText();
        this.tags = note.getTags();
        this.dateCreated = note.getDateCreated();
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getText() {
        return text;
    }

    public List<Tag> getTags() {
        return tags;
    }

    public LocalDateTime getDateCreated() {
        return dateCreated;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", NoteDTO.class.getSimpleName() + "[", "]")
                .add("id='" + id + "'")
                .add("title='" + title + "'")
                .add("text='" + text + "'")
                .add("tags=" + tags)
                .add("dateCreated=" + dateCreated)
                .toString();
    }
}
