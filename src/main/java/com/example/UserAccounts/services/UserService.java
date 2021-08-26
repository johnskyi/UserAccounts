package com.example.UserAccounts.services;

import com.example.UserAccounts.entities.UserEntity;
import com.example.UserAccounts.repositories.UserRepo;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.AllArgsConstructor;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.FileReader;
import java.io.FileWriter;
import java.util.*;

@Service
@AllArgsConstructor
public class UserService {



    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    @Autowired
    private final UserRepo userRepo;

    //ДОБАВЛЯЕМ ПОЛЬЗОВАТЕЛЯ
    public ResponseEntity<UserEntity> add() {
        Map<String, String> userMap = parseJson();
        UserEntity user = UserEntity.builder()
                .name(userMap.get("name"))
                .post(userMap.get("post"))
                .passWord(userMap.get("passWord"))
                .build();
        userRepo.save(user);
        answerCreate(user);

        return ResponseEntity.ok(userRepo.findById(user.getId()).get());

    }

    //МЕНЯЕМ ДАННЫЕ ПОЛЬЗОВАТЕЛЯ
    public ResponseEntity<UserEntity> update() {

        Map<String, String> userMap = parseJson();

        Optional<UserEntity> userOptional = userRepo.findById(Integer.parseInt(userMap.get("id")));
        if (userOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        userOptional.get().setName(userMap.get("name"));
        userOptional.get().setPost(userMap.get("post"));
        userOptional.get().setPassWord(userMap.get("passWord"));

        userRepo.save(userOptional.get());

        return ResponseEntity.ok(userRepo.findById(Integer.parseInt(userMap.get("id"))).get());
    }

    //БЛОКИРУЕМ ПОЛЬЗОВАТЕЛЯ
    public ResponseEntity<UserEntity> blocked(int id, String type, boolean isBlocked) {

        Optional<UserEntity> userOptional = userRepo.findById(id);
        if (userOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        userOptional.get().setBlocked(true);

        userRepo.save(userOptional.get());
        return ResponseEntity.ok(userOptional.get());
    }

    //ПАРСИМ json
    public static Map<String, String> parseJson() {
        Map<String, String> userMap = new HashMap<>();
        try {
            FileReader reader = new FileReader("src/main/resources/users.json");
            JSONParser jsonParser = new JSONParser();
            JSONObject jsonObject = (JSONObject) jsonParser.parse(reader);
            userMap = (Map<String, String>) jsonObject;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return userMap;
    }

    private static void answerCreate(UserEntity user) {
        try (FileWriter file = new FileWriter("src/main/resources/answer.json")) {
            file.write(GSON.toJson(user));
        } catch (Exception e) {
            e.printStackTrace();
        }


    }
}
