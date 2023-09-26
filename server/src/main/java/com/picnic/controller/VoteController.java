package com.picnic.controller;

import com.picnic.domain.VoteService;
import com.picnic.domain.Result;
import com.picnic.model.AppUser;
import com.picnic.model.Vote;
import com.picnic.security.JwtConverter;
import org.bson.types.ObjectId;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/votes")
public class VoteController {

    private final AuthenticationManager authenticationManager;
    private final JwtConverter converter;

    private final VoteService voteService;


    public VoteController(AuthenticationManager authenticationManager, JwtConverter converter, VoteService voteService) {
        this.authenticationManager = authenticationManager;
        this.converter = converter;
        this.voteService = voteService;
    }

    @PostMapping("/addVote")
    public ResponseEntity<Vote> addVote(@RequestBody Vote vote) {
        String username = ((AppUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
        vote.setUsername(username);
        Result<Vote> result = voteService.addVote(vote);
        if (result.isSuccess()) {
            return ResponseEntity.ok(result.getPayload());
        }
        else {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping()
    public ResponseEntity<List<Vote>> findAllVotes(){
        Result<List<Vote>> result = voteService.findAll();

        if (result.isSuccess()) {
            return ResponseEntity.ok(result.getPayload());
        }
        else {
            return ResponseEntity.badRequest().build();
        }

    }

    @DeleteMapping("/delete/{voteId}")
    public ResponseEntity<Boolean> deleteVoteById(@PathVariable String voteId){

        Result<Boolean> result = voteService.deleteVote(voteId);

        if (result.isSuccess()) {
            return ResponseEntity.ok(result.getPayload());
        }
        else {
            return ResponseEntity.badRequest().build();
        }

    }


}
