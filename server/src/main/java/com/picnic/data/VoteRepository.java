package com.picnic.data;

import com.picnic.model.AppUser;
import com.picnic.model.Vote;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface VoteRepository extends MongoRepository<Vote, ObjectId> {

    public Vote save(Vote newVote);
    public List<Vote> findAll();

    public Optional<Vote> findById(ObjectId voteId);


    // I think that update and delete are more complex things. These will be implemented in the service layer
    // with a MongoTemplate
    @Override
    void deleteById(ObjectId voteId);



}
