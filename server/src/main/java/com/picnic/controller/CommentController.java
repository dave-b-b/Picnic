package com.picnic.controller;

import com.mongodb.client.result.UpdateResult;
import com.picnic.domain.CommentService;
import com.picnic.domain.Result;
import com.picnic.domain.CommentService;
import com.picnic.model.AppUser;
import com.picnic.model.Comment;
import com.picnic.model.Story;
import com.picnic.security.JwtConverter;
import org.bson.types.ObjectId;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/comments")
public class CommentController {

    private final AuthenticationManager authenticationManager;
    private final JwtConverter converter;

    private final CommentService commentService;


    public CommentController(AuthenticationManager authenticationManager, JwtConverter converter, CommentService commentService) {
        this.authenticationManager = authenticationManager;
        this.converter = converter;
        this.commentService = commentService;
    }

    @PostMapping("/addComment")
    public ResponseEntity<Comment> addComment(@RequestBody Comment comment) {
        AppUser user = (AppUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        comment.setUsername(user.getUsername());
        Result<Comment> result = commentService.addComment(comment);
        if (result.isSuccess()) {
            return ResponseEntity.ok(result.getPayload());
        }
        else {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/")
    public ResponseEntity<List<Comment>> findAllComments(){
        Result<List<Comment>> result = commentService.findAll();

        if (result.isSuccess()) {
            return ResponseEntity.ok(result.getPayload());
        }
        else {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/{commentId}")
    public ResponseEntity<Comment> findCommentById(@PathVariable ObjectId commentId){
        Result<Comment> result = commentService.findById(commentId);

        if (result.isSuccess()) {
            return ResponseEntity.ok(result.getPayload());
        }
        else {
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("delete/{commentId}")
    public ResponseEntity<Void> deleteComment(@PathVariable ObjectId commentId){
        Result<Void> result = commentService.deleteById(commentId);

        if (result.isSuccess()) {
            return ResponseEntity.ok(result.getPayload());
        }
        else {
            return ResponseEntity.badRequest().build();
        }
    }
}
