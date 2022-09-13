package com.github.youssefagagg.automaticirrigationsystem.config;

import io.mongock.runner.springboot.EnableMongock;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.MongoReactiveAutoConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.mongodb.repository.config.EnableReactiveMongoRepositories;


@Configuration
@EnableMongock
@EnableReactiveMongoRepositories("com.github.youssefagagg.automaticirrigationsystem.repository")
@Import(value = { MongoAutoConfiguration.class, MongoReactiveAutoConfiguration.class })
public class DatabaseConfiguration {
}
