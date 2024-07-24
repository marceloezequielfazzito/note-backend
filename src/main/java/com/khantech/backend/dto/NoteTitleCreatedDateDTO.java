package com.khantech.backend.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;
import java.util.StringJoiner;

public class NoteTitleCreatedDateDTO {

    @JsonProperty("id")
    private String id;
    @JsonProperty("title")
    private String title;
    @JsonProperty("dateCreated")
    private LocalDateTime dateCreated;

    @JsonCreator
    public NoteTitleCreatedDateDTO(@JsonProperty("id")String id,
                                   @JsonProperty("title") String title,
                                   @JsonProperty("dateCreated")LocalDateTime dateCreated) {
        this.id = id;
        this.title = title;
        this.dateCreated = dateCreated;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public LocalDateTime getDateCreated() {
        return dateCreated;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", NoteTitleCreatedDateDTO.class.getSimpleName() + "[", "]")
                .add("id='" + id + "'")
                .add("title='" + title + "'")
                .add("dateCreated=" + dateCreated)
                .toString();
    }
}
