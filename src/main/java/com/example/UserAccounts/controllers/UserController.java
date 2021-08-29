package com.example.UserAccounts.controllers;


import com.example.UserAccounts.services.UserService;
import lombok.AllArgsConstructor;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.*;

import java.util.Map;


@RestController
@AllArgsConstructor
public class UserController {

    private Map<String, String> messageMap;

    @Autowired
    private final UserService userService;

    private Map<String, String> getMessageMap() {
        return messageMap;
    }

    //ПОЛУЧАЕМ СООБЩЕНИЕ ОТ СЛУШАТЕЛЯ
    @RequestMapping("/")
    public void setMessage(String message) {
        JSONParser jsonParser = new JSONParser();
        JSONObject jsonObject = null;
        try {
            jsonObject = (JSONObject) jsonParser.parse(message);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Map<String, String> map = (Map<String, String>) jsonObject;

        this.messageMap = map;
    }

    //ДОБАВЛЯЕМ ПОЛЬЗОВАТЕЛЯ
    @PostMapping(path = "/users/add/", consumes = "application/json")
    public void add() {
        userService.add(getMessageMap());

    }

    //МЕНЯЕМ ДАННЫЕ ПОЛЬЗОВАТЕЛЯ
    @PutMapping("/users/update/")
    public void update() {
        userService.update(getMessageMap());
    }

    //БЛОКИРУЕМ ПОЛЬЗОВАТЕЛЯ
    @PutMapping("/users/blocked/")
    public void blocked() {
        userService.blocked(getMessageMap());
    }
}
