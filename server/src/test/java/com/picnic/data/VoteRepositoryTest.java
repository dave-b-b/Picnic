package com.picnic.data;


import com.mongodb.client.MongoClient;
import com.picnic.model.AppUser;
import com.picnic.model.Comment;
import com.picnic.model.Story;
import com.picnic.model.Vote;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataMongoTest
class VoteRepositoryTest {

    @Autowired
    VoteRepository voteRepository;

    @Autowired
    MongoClient mongoClient;
    @BeforeEach
    void setGoodKnownState(){

        // This will drop the database. Mongo will create a new database when data gets entered.
        mongoClient.getDatabase(System.getenv("MONGO_DATABASE")).drop();

        Vote newVote = new Vote(1);
        Vote newVote2 = new Vote(2);

        newVote = voteRepository.save(newVote);
        newVote2 = voteRepository.save(newVote2);
    }

    @Test
    void shouldFindTwoVotes(){

        List<Vote> voteList = voteRepository.findAll();

        assertNotNull(voteList);
        assertTrue(voteList.size() == 2);
    }

    @Test
    void shouldAddNewVoteToRepo(){
        Vote vote = new Vote(3);

        vote = voteRepository.save(vote);

        assertNotNull(vote.getVoteId());
        assertEquals(3, voteRepository.findAll().size());

    }

    @Test
    void shouldFindOneVoteById(){

        Vote voteToFind = voteRepository.findAll().get(0);
        ObjectId id = new ObjectId(voteToFind.getVoteId());

        Optional<Vote> result = voteRepository.findById(id);

        assertFalse(result.isEmpty());
    }

    @Test
    void shouldDeleteOneVote(){
        Vote vote = voteRepository.findAll().get(0);
        ObjectId id = new ObjectId(vote.getVoteId());

        voteRepository.deleteById(id);

        assertTrue(voteRepository.findAll().size() == 1);
    }


}