package com.picnic.domain;


import com.mongodb.DBRef;
import com.mongodb.client.result.UpdateResult;
import com.picnic.data.CommentRepository;
import com.picnic.data.StoryRepository;
import com.picnic.data.UserRepository;
import com.picnic.model.AppUser;
import com.picnic.model.Comment;
import com.picnic.model.Story;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class CommentService {

    MongoTemplate mongoTemplate;

    private final StoryRepository storyRepository;
    private final CommentRepository commentRepository;
    @Autowired
    CommentService(StoryRepository storyRepository, CommentRepository commentRepository, MongoTemplate mongoTemplate){
        this.storyRepository = storyRepository;
        this.commentRepository = commentRepository;
        this.mongoTemplate = mongoTemplate;
    }

    public Result<Comment> addComment(Comment comment){
        Result<Comment> result = new Result<>();

        if(comment.getParentId().isEmpty()){
            result.addMessage("Cannot add a comment without a parent.", ResultType.INVALID);
            return result;
        }
        // This saves the comment into the comments collection
        Comment addedComment = commentRepository.save(comment);

        if(!addedComment.getCommentId().isEmpty()){
            // Find the parent
            Query query = new Query(Criteria.where("_id").is(new ObjectId(comment.getParentId())));

            // Create a DBRef to the added comment
            DBRef commentRef = new DBRef("comments", addedComment.getCommentId());

            Update update = new Update().push("comments", commentRef);
            UpdateResult updateResult = mongoTemplate.updateFirst(query, update, Story.class);
            if(updateResult.getMatchedCount() == 0){
                updateResult = mongoTemplate.updateFirst(query, update, Comment.class);
            }

            if(updateResult.getMatchedCount() > 0){
                result.setPayload(addedComment);
                return result;
            }
            result.addMessage("Unable to store reference in comments array.", ResultType.INVALID);
            return result;
        }
        result.addMessage("Unable to add comment.", ResultType.INVALID);

        return result;
    }

    public Result<List<Comment>> findAll(){
        Result<List<Comment>> result = new Result<>();
        List<Comment> comments = commentRepository.findAll();
        result.setPayload(comments);

        return result;
    }

    public Result<Void> deleteById(ObjectId commentId){
        Result<Void> result = new Result<>();
        Optional<Comment> comment = commentRepository.findById(commentId);
        if(comment.isEmpty()){
            result.addMessage("No commented with this id exists.", ResultType.NOT_FOUND);
            return result;
        }
        if(comment.get().getParentId().isEmpty()){
            result.addMessage("Cannot add a comment without a parent.", ResultType.INVALID);
            return result;
        }

        // This deletes all references to the comment
        // Find the parent
        Query query = new Query(Criteria.where("_id").is(new ObjectId(comment.get().getParentId())));
        //Delete reference in parent
        //mongoTemplate.findAnd(query, DBRef.class);

        // This deletes the Comment from the comments collection
        commentRepository.deleteById(commentId);

        return result;
    }

    public Result<Comment> findById(ObjectId objectId){
        Result<Comment> result = new Result<>();

        Optional<Comment> comment = commentRepository.findById(objectId);
        if(comment.get() == null){
            result.addMessage("Cannot find a comment with this id", ResultType.NOT_FOUND);
            return result;
        }
        result.setPayload(comment.get());
        return result;
    }
}
