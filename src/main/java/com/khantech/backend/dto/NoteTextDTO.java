package com.khantech.backend.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.StringJoiner;

public class NoteTextDTO {

    @JsonIgnore
    private String id;
    @JsonProperty("text")
    private String text;

    public NoteTextDTO(String id, String text) {
        this.id = id;
        this.text = text;
    }

    public String getId() {
        return id;
    }

    public String getText() {
        return text;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", NoteTextDTO.class.getSimpleName() + "[", "]")
                .add("id='" + id + "'")
                .add("text='" + text + "'")
                .toString();
    }
}
