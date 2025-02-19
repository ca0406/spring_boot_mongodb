package com.example.demo.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.example.demo.model.user;


public interface userrepository extends MongoRepository<user, String> {
    
    
}
