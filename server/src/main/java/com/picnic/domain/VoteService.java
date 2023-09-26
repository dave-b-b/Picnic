package com.picnic.domain;



import com.mongodb.DBRef;
import com.mongodb.client.result.UpdateResult;
import com.picnic.data.CommentRepository;
import com.picnic.data.StoryRepository;
import com.picnic.data.UserRepository;
import com.picnic.data.VoteRepository;
import com.picnic.model.AppUser;
import com.picnic.model.Comment;
import com.picnic.model.Story;
import com.picnic.model.Vote;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class VoteService {

    MongoTemplate mongoTemplate;

    private final StoryRepository storyRepository;
    private final CommentRepository commentRepository;

    private final UserRepository userRepository;


    private final VoteRepository voteRepository;


    @Autowired
    public VoteService(MongoTemplate mongoTemplate, StoryRepository storyRepository, CommentRepository commentRepository, UserRepository userRepository, VoteRepository voteRepository) {
        this.mongoTemplate = mongoTemplate;
        this.storyRepository = storyRepository;
        this.commentRepository = commentRepository;
        this.userRepository = userRepository;
        this.voteRepository = voteRepository;
    }

    public Result<Vote> addVote(Vote vote){
        Result<Vote> result = new Result<>();
        // This saves the vote into the votes collection
        if(vote.getParentId().isEmpty()){
            result.addMessage("Cannot add a vote without a parent", ResultType.INVALID);
            return result;
        }

        Vote addedVote = voteRepository.save(vote);

        if(!addedVote.getVoteId().isEmpty()){
            // Find the parent
            Query query = new Query(Criteria.where("_id").is(new ObjectId(vote.getParentId())));

            // Create a DBRef to the added vote
            DBRef voteRef = new DBRef("votes", addedVote.getVoteId());

            Update update = new Update().push("votes", voteRef);

            // First try to match the parent in stories
            UpdateResult updateResult = mongoTemplate.updateFirst(query, update, Story.class);
            //If it doesn't match with the story, then add it to the matching comment
            if(updateResult.getMatchedCount() == 0){
                updateResult = mongoTemplate.updateFirst(query, update, Comment.class);
            }
            //If it matches the parent or the comment
            if(updateResult.getMatchedCount() > 0){
                // Find the user who sent the vote
                AppUser user = (AppUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
                ObjectId userId = new ObjectId(userRepository.findByUsername(user.getUsername()).getAppUserId());

                Query userQuery = new Query(Criteria.where("_id").is(userId));
                mongoTemplate.updateFirst(userQuery, update, AppUser.class);
                result.setPayload(addedVote);
                return result;
            }
            result.addMessage("Unable to store reference in votes array.", ResultType.INVALID);
            return result;
        }
        result.addMessage("Unable to add vote.", ResultType.INVALID);

        return result;
    }

    public Result<List<Vote>> findAll(){
        Result<List<Vote>> result = new Result<>();
        List<Vote> votes = voteRepository.findAll();
        result.setPayload(votes);

        return result;
    }


    public Result<Boolean> deleteVote(String voteId) {
        ObjectId voteObjId = new ObjectId(voteId);
        Result<Boolean> result = new Result<>();

//        // Delete vote
        voteRepository.deleteById(voteObjId);

        //Verify that Id was deleted
        Optional<Vote> vote = voteRepository.findById(voteObjId);

        if (vote.isEmpty()) {
            // Cascading Delete
            // Delete any references on Stories and Comments
            Query query = new Query(Criteria.where("votes").elemMatch(Criteria.where(voteId).is(voteId)));
            Update update = new Update().pull("voteId", voteId);
            mongoTemplate.updateMulti(query, update, Vote.class);
        }

        return result;
    }
//
//    public Result<Vote> updateVote(Vote vote){
//        Result<Vote> result = new Result<>();
//
//        if(vote.getParentId().isEmpty()){
//            result.addMessage("Cannot update a vote without a parent.", ResultType.INVALID);
//            return result;
//        }
//
//        if(!vote.getVoteId().isEmpty()){
//            // Find the vote
//            Query query = new Query(Criteria.where("_id").is(new ObjectId(vote.getVoteId())));
//
//            Update update = new Update();
//            UpdateResult updateResult = mongoTemplate.updateFirst(query, update, Story.class);
//            if(updateResult.getMatchedCount() == 0){
//                updateResult = mongoTemplate.updateFirst(query, update, Vote.class);
//            }
//
//            if(updateResult.getMatchedCount() > 0){
//                result.setPayload(vote);
//                return result;
//            }
//            result.addMessage("Unable to store reference in votes array.", ResultType.INVALID);
//            return result;
//        }
//        result.addMessage("Unable to add vote.", ResultType.INVALID);
//
//
////        Result<UpdateResult> result = new Result<>();
////        Query query = new Query(Criteria.where("_id").is(new ObjectId(vote.getParentId())));
////        Update update = new Update().push("votes", vote);
////        UpdateResult updateResult = mongoTemplate.updateFirst(query, update, Vote.class);
////        if(updateResult.wasAcknowledged()){
////            String voteId = updateResult.getUpsertedId().asString().toString();
////            voteId = vote.getParentId().toString() + "_" + voteId;
////            vote.setVoteId(voteId);
////        }
//        // Vote
//
////        String parentId = fullVote.getParentId();
////        Optional<Story> story;
////        Optional<Vote> parentVote;
////        if(!parentId.contains("_")){
////            story = storyRepository.findById(new ObjectId(parentId));
////        } else {
////            String[] positions = parentId.split("_");
////            story = storyRepository.findById(new ObjectId(positions[0]));
////            parentVote = voteRepository.findById(parentId);
////        }
//        return result;
//
//    }

}
