package com.example.demo.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.example.demo.model.*;
import com.example.demo.service.userservice;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class usercontroller {
    private final userservice UserService;

    @GetMapping
    public List<user> getAllUsers(){
        return UserService.findAll();
    }

    @PostMapping
    public user createuser(@RequestBody user User){
        return UserService.save(User);
    }

}
