package com.khantech.backend.repository;

import com.khantech.backend.dto.NoteTextDTO;
import com.khantech.backend.dto.NoteTitleCreatedDateDTO;
import com.khantech.backend.model.Note;
import com.khantech.backend.model.Tag;
import com.mongodb.client.result.DeleteResult;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;


import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class NoteRepository {

    private MongoTemplate mongoTemplate;

    @Autowired
    public NoteRepository(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    public Note save (Note note) {
       return mongoTemplate.save(note);
    }

    public Optional<NoteTextDTO> getNoteTextById(String id){
        Criteria criteria = Criteria.where("id").is(id);
        Query q = new Query(criteria);
        q.fields().include("text");
        Note note = mongoTemplate.findOne(q, Note.class);
        if(note == null){
            return Optional.empty();
        }
        return Optional.of(new NoteTextDTO(note.getId(),note.getText()));
    }


    public List<NoteTitleCreatedDateDTO> getNoteTitlesAndDateByTags(Tag[] tags) {
        Query q = new Query();
        if(tags != null && tags.length > 0){
           Criteria criteria = Criteria.where("tags").in(tags);
            q = new Query(criteria);
        }
        q.with(Sort.by(Sort.Direction.DESC, "dateCreated"));
        q.fields().include("id");
        q.fields().include("title");
        q.fields().include("dateCreated");
        List<Note> notes = mongoTemplate.find(q, Note.class);
        return notes.stream().map(note -> new NoteTitleCreatedDateDTO(note.getId(),note.getTitle(),note.getDateCreated()))
                .collect(Collectors.toList());
    }

    public Optional<Note> findOne(String id) {
        return Optional.ofNullable(mongoTemplate.findById(id, Note.class));
    }

    public boolean delete(String id) {
        Criteria criteria = Criteria.where("id").is(id);
        DeleteResult deleteResult = mongoTemplate.remove(new Query(criteria), Note.class);
        return deleteResult.getDeletedCount() != 0;
    }

    public void deleteAll(){
        mongoTemplate.dropCollection(Note.class);
    }

}
