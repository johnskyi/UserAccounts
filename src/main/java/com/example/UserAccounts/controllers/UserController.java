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


    @RequestMapping(value = "/", method = RequestMethod.PUT, consumes = "application/json", produces = "application/json")
    public void startService(String message) {
        setMessage(message);
        changeMethod(String.valueOf(getMessageMap().get("type")));
    }

    //ПОЛУЧАЕМ СООБЩЕНИЕ ОТ СЛУШАТЕЛЯ
    private void setMessage(String message) {
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

    //ВЫБИРАЕМ НУЖНЫЙ МЕТОД В ЗАВИСИМОСТИ ОТ ВХОДНОГО TYPE
    private void changeMethod(String type) {
        if (type.equals("1")) {
            userService.add(getMessageMap());
        }
        if (type.equals("3")) {
            userService.update(getMessageMap());
        }
        if (type.equals("2")) {
            userService.blocked(getMessageMap());
        }
    }

}
