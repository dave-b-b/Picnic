package com.picnic.data;


import com.mongodb.client.MongoClient;

import com.picnic.model.AppUser;
import com.picnic.model.Comment;
import com.picnic.model.Story;
import com.picnic.model.Vote;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataMongoTest
class StoryRepositoryTest {

    @Autowired
    StoryRepository storyRepository;

    @Autowired
    CommentRepository commentRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    MongoClient mongoClient;
    @BeforeEach
    void setGoodKnownState(){

        // This will drop the database. Mongo will create a new database when data gets entered.
        mongoClient.getDatabase(System.getenv("MONGO_DATABASE")).drop();

        // Add test users
        AppUser user1 = new AppUser("testuser1", "password11!", true, List.of("USER"));
        AppUser user2 = new AppUser("testuser2", "password22!", true, List.of("USER"));
        user1 = userRepository.save(user1);
        user2 = userRepository.save(user2);

        // Add test story
        //(String title, String body, LocalDate dateTime, ArrayList< Comment > comments, ArrayList< Vote > votes, AppUser appUser, boolean visibleToAll)
        Story newStory1 = new Story("Title1", "Body1", LocalDateTime.now(), List.of(), List.of(), user1, false);
        Story newStory2 = new Story("Title2", "Body2", LocalDateTime.now(), List.of(), List.of(), user2, true);
        Story newStory3 = new Story("Title3", "Body3", LocalDateTime.now(), List.of(), List.of(), user2, true);
        newStory2 = storyRepository.save(newStory2);
        newStory3 = storyRepository.save(newStory3);
        newStory1 = storyRepository.save(newStory1);
        // Add Comments
        // String commentId; String parentId; String body; LocalDateTime dateTime; String username; List<Vote> votes;
        Comment newComment1 = new Comment(newStory1.getStoryId(), "Test comment 1", LocalDateTime.now(), user1.getUsername());
        Comment newComment2 = new Comment(newStory1.getStoryId(), "Test comment 2", LocalDateTime.now(), user2.getUsername());
        Comment newComment3 = new Comment(newStory2.getStoryId(), "Test comment 3", LocalDateTime.now(), user2.getUsername());
        newComment1 = commentRepository.save(newComment1);
        newComment2 = commentRepository.save(newComment2);
        newComment3 = commentRepository.save(newComment3);
    }

    @Test
    void shouldFindTwoStories(){

        List<Story> storyList = storyRepository.findAll();

        assertNotNull(storyList);
        assertTrue(storyList.size() == 3);
    }

    @Test
    void shouldAddStoryToDatabase(){
        Story newStory = new Story("Title", "Body", LocalDateTime.now(), List.of(), List.of(new Comment()), new AppUser(), true);

        newStory = storyRepository.save(newStory);

        assertNotNull(newStory.getStoryId());
        assertTrue(storyRepository.findAll().size() == 4);
    }



}