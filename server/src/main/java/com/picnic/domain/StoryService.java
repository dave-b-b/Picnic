package com.picnic.domain;


import com.picnic.data.StoryRepository;
import com.picnic.data.UserRepository;
import com.picnic.model.AppUser;
import com.picnic.model.Story;
import org.bson.BsonObjectId;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import javax.swing.text.html.Option;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@Service
public class StoryService {

    private StoryRepository storyRepository;

    private UserRepository userRepository;
    @Autowired
    StoryService(StoryRepository storyRepository, UserRepository userRepository){
        this.storyRepository = storyRepository;
        this.userRepository = userRepository;
    }

    public Result<Story> addStory(Story newStory){
        Result<Story> result = new Result<>();
        AppUser fullUser = userRepository.findByUsername(newStory.getAppUser().getUsername());
        newStory.setAppUser(fullUser);
        Story story = storyRepository.save(newStory);
        result.setPayload(story);
        return result;
    }

    public Result<List<Story>> findAll(){
        Result<List<Story>> result = new Result<List<Story>>();
        List<Story> storyList = storyRepository.findAll();
        result.setPayload(storyList);
        return result;
    }

    public Result<Optional<Story>> findById(ObjectId storyId){
        Result<Optional<Story>> result = new Result<Optional<Story>>();
        Optional<Story> story = storyRepository.findById(storyId);
        result.setPayload(story);
        return result;
    }

    public Result<Void> deleteById(ObjectId storyId){
        Result<Void> result = new Result<>();
        storyRepository.deleteById(storyId);
        return result;
    }
}
