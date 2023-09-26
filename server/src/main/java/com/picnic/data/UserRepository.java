package com.picnic.data;

import com.picnic.model.AppUser;
import org.apache.catalina.User;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.transaction.annotation.Transactional;

public interface UserRepository extends MongoRepository<AppUser, ObjectId> {

    public AppUser save(AppUser newUser);


    public AppUser findByUsername (String username);
}
