import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.MongoException;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.picnic.model.Story;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;

@Configuration
public class TestConfig {

    @Bean
    MongoClient createMongoClient() {
        String format = "mongodb+srv://%s:%s@picnic.iy8gmxv.mongodb.net/?retryWrites=true&w=majority";

        String username = System.getenv("MONGO_DB_USERNAME");
        String password = System.getenv("MONGO_DB_PASSWORD");
        String connectionString = String.format(format, username, password);
        ConnectionString mongoUri = new ConnectionString(connectionString);

        // a CodecRegistry tells the Driver how to move data between Java POJOs (Plain Old Java Objects) and MongoDB documents
        CodecRegistry pojoCodecRegistry = fromRegistries(MongoClientSettings.getDefaultCodecRegistry(),
                fromProviders(PojoCodecProvider.builder().automatic(true).build()));

        // The MongoClient defines the connection to our MongoDB datastore instance (Atlas) using MongoClientSettings
        // You can create a MongoClientSettings with a Builder to configure codecRegistries, connection strings, and more
        MongoClientSettings settings = MongoClientSettings.builder()
                .codecRegistry(pojoCodecRegistry)
                .applyConnectionString(mongoUri).build();

        MongoClient mongoClient = null;

        try {
            mongoClient = MongoClients.create(settings);
        } catch (MongoException me) {
            System.err.println("Unable to connect to the MongoDB instance due to an error: " + me);
            System.exit(1);
        }
        return mongoClient;
    }

}
