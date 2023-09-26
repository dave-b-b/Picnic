package com.picnic.data;

import com.picnic.model.Comment;
import com.picnic.model.Story;
import org.bson.BSONObject;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;

public interface StoryRepository extends MongoRepository<Story, ObjectId> {
    public Story save(Story newStory);
    public List<Story> findAll();
    public Optional<Story> findById(ObjectId storyId);

    public void deleteById(ObjectId storyId);

}
