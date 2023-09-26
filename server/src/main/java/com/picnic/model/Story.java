package com.picnic.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;


import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@Document(collection = "stories")
public class Story {
    @Id
    String storyId;
    String title;
    String body;
    LocalDateTime dateTime = LocalDateTime.now();

    @DBRef     // This creates a reference to comments collection in the database
    List<Comment> comments = new ArrayList<>(); // Create an empty array list here so that it is created in db
    String username;
    @DBRef      //This creates a reference to all votes in the collection
    List<Vote> votes = new ArrayList<>(); // Create an empty array list here so that it is created in db
    AppUser appUser;
    boolean visibleToAll;

    // No argument constructor created for the StoryController
    public Story() {
    }


    // This is for objects without an objectId

    public Story(String title, String body, LocalDateTime dateTime, List<Vote> votes, List<Comment> comments, AppUser appUser, boolean visibleToAll) {
        this.title = title;
        this.body = body;
        this.dateTime = dateTime;
        this.comments = new ArrayList<Comment>(comments);
        this.votes = new ArrayList<Vote>(votes);
        this.appUser = appUser;
        this.visibleToAll = visibleToAll;
    }


    public Story(String storyId, String title, String body, LocalDateTime dateTime, List<Comment> comments, List<Vote> votes, AppUser appUser, boolean visibleToAll) {
        this.storyId = storyId;
        this.title = title;
        this.body = body;
        this.dateTime = dateTime;
        this.comments = comments;
        this.votes = votes;
        this.appUser = appUser;
        this.visibleToAll = visibleToAll;
    }

    public String getStoryId() {
        return storyId;
    }

    public void setStoryId(String storyId) {
        this.storyId = storyId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = LocalDateTime.now();
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    public List<Vote> getVotes() {
        return votes;
    }

    public void setVotes(List<Vote> votes) {
        this.votes = votes;
    }

    public AppUser getAppUser() {
        return appUser;
    }

    public void setAppUser(AppUser appUser) {
        this.appUser = appUser;
    }

    public boolean isVisibleToAll() {
        return visibleToAll;
    }

    public void setVisibleToAll(boolean visibleToAll) {
        this.visibleToAll = visibleToAll;
    }

    public void setUsername(String username){
        this.appUser = new AppUser(username);
    }
}
