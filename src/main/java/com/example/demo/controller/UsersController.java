package com.example.demo.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.service.UsersService;

@RestController
@RequestMapping("/auth")
public class UsersController {
    public UsersService usersService;

    public UsersController(UsersService usersService){
        this.usersService = usersService;
    }

}
