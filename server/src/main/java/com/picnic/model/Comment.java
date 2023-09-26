package com.picnic.model;


import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
@Document(collection = "comments")
public class Comment {
    @Id
    String commentId;
    String parentId;
    String body;
    LocalDateTime dateTime = LocalDateTime.now();
    String username;
    @DBRef //This creates a reference of all votes
    List<Vote> votes = new ArrayList<>(); // Create an empty array list here so that it is created in db

    @DBRef //This creates a reference of all comments on the comment
    List<Comment> comments = new ArrayList<>(); // Create an empty array list here so that it is created in db

    public Comment() {
    }

    public Comment(String parentId, String body, LocalDateTime dateTime, String username) {
        this.parentId = parentId;
        this.body = body;
        this.dateTime = dateTime;
        this.username = username;
        this.votes = new ArrayList<Vote>();
    }

    public String getCommentId() {
        return commentId;
    }

    public void setCommentId(String commentId) {
        this.commentId = commentId;
    }

    public String getBody() {
        return body;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public List<Vote> getVotes() {
        return votes;
    }

    public void setVotes(List<Vote> votes) {
        this.votes = votes;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }
}
