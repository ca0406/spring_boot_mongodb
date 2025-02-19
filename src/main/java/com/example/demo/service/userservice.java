package com.example.demo.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.demo.repository.userrepository;

import lombok.RequiredArgsConstructor;
import com.example.demo.model.*;

@Service
@RequiredArgsConstructor
public class userservice {
    private final userrepository UserRepository;
    public List<user> findAll(){
        return UserRepository.findAll();
    }
    public user save(user User){
        return UserRepository.save(User);
    }
}
