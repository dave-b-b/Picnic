
package com.picnic;


import com.picnic.data.*;
import com.picnic.model.AppUser;
import com.picnic.model.Comment;
import com.picnic.model.Story;
import org.bson.types.ObjectId;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;


import java.time.LocalDate;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

@SpringBootApplication

public class App {

    public static void main(String[] args) {
        Logger.getLogger( "org.mongodb.driver" ).setLevel(Level.WARNING);
        SpringApplication.run(App.class, args);

    }

}

