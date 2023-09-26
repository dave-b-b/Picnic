package com.picnic.domain;

import com.mongodb.DBRef;
import com.picnic.data.CommentRepository;
import com.picnic.data.StoryRepository;
import com.picnic.data.VoteRepository;
import com.picnic.model.AppUser;
import com.picnic.model.Comment;
import com.picnic.model.Story;
import com.picnic.model.Vote;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest
@AutoConfigureMockMvc
class VoteServiceTest {

    @MockBean
    VoteRepository voteRepository;

    @MockBean
    CommentRepository commentRepository;

    @MockBean
    StoryRepository storyRepository;

    @MockBean
    MongoTemplate mongoTemplate;

    @Autowired
    MockMvc mvc;

    @BeforeEach
    void setup() {
        AppUser user1 = new AppUser("testuser1", "password11!", true, List.of("USER"));
        user1.setAppUserId(new ObjectId().toString());
        user1.setAppUserId(new ObjectId().toString());
        AppUser user2 = new AppUser("testuser2", "password22!", true, List.of("USER"));

        // Add test story
        //(String title, String body, LocalDate dateTime, ArrayList< Comment > comments, ArrayList< Vote > votes, AppUser appUser, boolean visibleToAll)
        Story newStory1 = new Story("Title1", "Body1", LocalDateTime.now(), List.of(), List.of(), user1, false);
        newStory1.setStoryId(new ObjectId().toString());
        Story newStory2 = new Story("Title2", "Body2", LocalDateTime.now(), List.of(), List.of(), user2, true);
        newStory2.setStoryId(new ObjectId().toString());
        Story newStory3 = new Story("Title3", "Body3", LocalDateTime.now(), List.of(), List.of(), user2, true);
        newStory3.setStoryId(new ObjectId().toString());

        // Add Comments
        // String commentId; String parentId; String body; LocalDateTime dateTime; String username; List<Vote> votes;
        Comment newComment1 = new Comment(newStory1.getStoryId(), "Test comment 1", LocalDateTime.now(), user1.getUsername());
        newComment1.setCommentId(new ObjectId().toString());
        Comment newComment2 = new Comment(newStory1.getStoryId(), "Test comment 2", LocalDateTime.now(), user2.getUsername());
        newComment2.setCommentId(new ObjectId().toString());
        Comment newComment3 = new Comment(newStory2.getStoryId(), "Test comment 3", LocalDateTime.now(), user2.getUsername());
        newComment3.setCommentId(new ObjectId().toString());

        //Add Comments to 1 story and 1 comment
        newComment1.setComments(List.of(newComment3));
        newStory1.setComments(List.of(newComment2));

        //Add Votes
        Vote newVote = new Vote(1);
        Vote newVote2 = new Vote(1);
        Vote newVote3 = new Vote(1);
        Vote newVote4 = new Vote(1);
        Vote newVote5 = new Vote(1);
        Vote newVote6 = new Vote(1);
        Vote newVote7 = new Vote(1);
        Vote newVote8 = new Vote(1);

        //Add votes to Stories and list
        newStory2.setVotes(List.of(newVote, newVote2));
        newComment1.setVotes(List.of(newVote3, newVote4, newVote5));
        //This is a comment on Comment1
        newComment3.setVotes(List.of(newVote6, newVote7, newVote8));

    }
    @Test
    void addVoteToStory() {
        //Vote
        Vote vote = new Vote(1);
        String id = new ObjectId().toString();
        //vote.setParentId();
        Vote voteWithId = new Vote(1);
        voteWithId.setVoteId(id);

        Query query = new Query(Criteria.where("_id").is(new ObjectId(vote.getParentId())));
        // Create a DBRef to the added vote
        DBRef voteRef = new DBRef("votes", voteWithId.getVoteId());

        Update update = new Update().push("votes", voteRef);
        when(voteRepository.save(vote)).thenReturn(voteWithId);
        //when(mongoTemplate.updateFirst());
    }

    @Test
    void addVote() {

    }

    @Test
    void findAll() {
    }

    @Test
    void deleteVote() {
    }

}