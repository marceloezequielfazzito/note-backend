package com.khantech.backend.model;

import com.khantech.backend.dto.NoteDTO;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDateTime;
import java.util.List;

@Document("note")
public class Note {

    @Id
    private String id;
    @Field("title")
    private String title;
    @Field("text")
    private String text;
    @Field("tags")
    private List<Tag> tags;
    @CreatedDate
    private LocalDateTime dateCreated;

    public Note (NoteDTO request){
        this.text = request.getText();
        this.title = request.getTitle();
        this.tags = request.getTags();
        this.id = request.getId();
    }

    public Note(String title, String text, List<Tag> tags) {
        this.title = title;
        this.text = text;
        this.tags = tags;
    }

    public Note() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public List<Tag> getTags() {
        return tags;
    }

    public void setTags(List<Tag> tags) {
        this.tags = tags;
    }


    public LocalDateTime getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(LocalDateTime dateCreated) {
        this.dateCreated = dateCreated;
    }
}

