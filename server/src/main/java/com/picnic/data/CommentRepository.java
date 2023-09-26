package com.picnic.data;

import com.picnic.model.Comment;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CommentRepository extends MongoRepository<Comment, ObjectId> {

    // Update method is in the service using MongoTemplate
    public Comment save(Comment comment);

    public List<Comment> findAll();

    public void deleteById(ObjectId commentId);

    public Optional<Comment> findById(ObjectId commentId);

}
