package com.picnic.controller;

import com.picnic.App;
import com.picnic.domain.Result;
import com.picnic.domain.StoryService;
import com.picnic.model.AppUser;
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
@RequestMapping("/api/stories")
public class StoryController {

    private final AuthenticationManager authenticationManager;
    private final JwtConverter converter;

    private final StoryService storyService;


    public StoryController(AuthenticationManager authenticationManager, JwtConverter converter, StoryService storyService) {
        this.authenticationManager = authenticationManager;
        this.converter = converter;
        this.storyService = storyService;
    }
    @GetMapping()
    public ResponseEntity<List<Story>> findAllStories() {
        Result<List<Story>> result = storyService.findAll();
        if (result.isSuccess()) {
            return ResponseEntity.ok(result.getPayload());
        }
        else {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/{storyId}")
    public ResponseEntity<Optional<Story>> findAllStories(@PathVariable ObjectId storyId) {
        Result<Optional<Story>> result = storyService.findById(storyId);
        if (result.isSuccess()) {
            return ResponseEntity.ok(result.getPayload());
        }
        else {
            return ResponseEntity.badRequest().build();
        }
    }


    @PostMapping("/addStory")
    public ResponseEntity<Story> addStory(@RequestBody Story story) {
        AppUser user = (AppUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        story.setAppUser(user);
        Result<Story> result = storyService.addStory(story);
        if (result.isSuccess()) {
            return ResponseEntity.ok(result.getPayload());
        }
        else {
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/delete/{storyId}")
    public ResponseEntity<Void> addStory(@PathVariable ObjectId storyId) {
        Result<Void> result = storyService.deleteById(storyId);
        if (result.isSuccess()) {
            return ResponseEntity.ok(result.getPayload());
        }
        else {
            return ResponseEntity.badRequest().build();
        }
    }


}
