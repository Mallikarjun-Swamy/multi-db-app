package com.ms.config;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@Configuration
@EnableMongoRepositories(
        basePackages = "com.ms.repository.mongo" // mongo repositories
)
public class MongoConfig {
    @Bean
    public MongoClient mongoClient() {
        String uri = "mongodb+srv://amallik155_db_user:<add-db-password>@mymongocluster.jpckrhv.mongodb.net/my_mongo_db";
        return MongoClients.create(uri);
    }

}
