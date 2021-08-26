package com.example.UserAccounts.controllers;



import com.example.UserAccounts.services.UserService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;


@RestController
@AllArgsConstructor
public class UserController {


    @Autowired
    private final UserService userService;



    //ДОБАВЛЯЕМ ПОЛЬЗОВАТЕЛЯ
    @PostMapping(path = "/users/", consumes = "application/json")
    public void add() {
        userService.add();

    }
  //МЕНЯЕМ ДАННЫЕ ПОЛЬЗОВАТЕЛЯ
    @PutMapping("/users/")
    public void update() {
        userService.update();
    }
    //БЛОКИРУЕМ ПОЛЬЗОВАТЕЛЯ
//    @PutMapping("/users/")
//    public void blocked() {
//
//    }
}
