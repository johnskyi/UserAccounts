package com.example.UserAccounts.services;

import com.example.UserAccounts.entities.UserEntity;
import com.example.UserAccounts.repositories.UserRepo;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.AllArgsConstructor;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.FileReader;
import java.util.*;

@Service
@AllArgsConstructor
public class UserService {

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    @Autowired
    private final RabbitTemplate template;

    @Autowired
    private final UserRepo userRepo;


    //ДОБАВЛЯЕМ ПОЛЬЗОВАТЕЛЯ
    public ResponseEntity<UserEntity> add() {
        Map<String, String> userMap = parseJson(); // Входной json
        produceTestJson();
        UserEntity user = UserEntity.builder()
                .name(userMap.get("name"))
                .post(userMap.get("post"))
                .passWord(userMap.get("passWord"))
                .build();
        userRepo.save(user);
        String message = "{\n" +
                "  \"id\": "+ user.getId() +",\n" +
                "  \"name\":\""+ user.getName() +"\",\n" +
                "  \"post\":\""+user.getPost()+"\",\n" +
                "  \"passWord\":\""+user.getPassWord()+"\",\n" +
                "  \"isBlocked\":"+user.isBlocked()+"\n" +
                "}";
        template.setExchange("anyExchange");
        template.convertAndSend("anyQueue", message);

        return ResponseEntity.ok(userRepo.findById(user.getId()).get());

    }

    //МЕНЯЕМ ДАННЫЕ ПОЛЬЗОВАТЕЛЯ
    public ResponseEntity<UserEntity> update() {

        Map<String, String> userMap = parseJson(); // Входной json
        produceTestJson();

        int id = Integer.parseInt(String.valueOf(userMap.get("id")));

        Optional<UserEntity> userOptional = userRepo.findById(id);
        if (userOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        userOptional.get().setName(userMap.get("name"));
        userOptional.get().setPost(userMap.get("post"));
        userOptional.get().setPassWord(userMap.get("passWord"));

        userRepo.save(userOptional.get());


        String message = "{\n" +
                "  \"id\": "+ userOptional.get().getId() +",\n" +
                "  \"name\":\""+ userOptional.get().getName() +"\",\n" +
                "  \"post\":\""+userOptional.get().getPost()+"\",\n" +
                "  \"passWord\":\""+userOptional.get().getPassWord()+"\",\n" +
                "  \"isBlocked\":"+userOptional.get().isBlocked()+"\n" +
                "}";
        template.setExchange("anyExchange");
        template.convertAndSend("anyQueue", message);

        return ResponseEntity.ok(userOptional.get());
    }

    //БЛОКИРУЕМ ПОЛЬЗОВАТЕЛЯ
    public ResponseEntity<UserEntity> blocked() {

        Map<String, String> userMap = parseJson(); // Входной json
        produceTestJson();

        int id = Integer.parseInt(String.valueOf(userMap.get("id")));


        Optional<UserEntity> userOptional = userRepo.findById(id);
        if (userOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        userOptional.get().setBlocked(true);

        userRepo.save(userOptional.get());


        String message = "{\n" +
                "  \"id\": "+ userOptional.get().getId() +",\n" +
                "  \"name\":\""+ userOptional.get().getName() +"\",\n" +
                "  \"post\":\""+userOptional.get().getPost()+"\",\n" +
                "  \"passWord\":\""+userOptional.get().getPassWord()+"\",\n" +
                "  \"isBlocked\":"+userOptional.get().isBlocked()+"\n" +
                "}";
        template.setExchange("anyExchange");
        template.convertAndSend("anyQueue", message);

        return ResponseEntity.ok(userOptional.get());
    }

    //ПАРСИМ json
    public static Map<String, String> parseJson() {
        Map<String, String> userMap = new HashMap<>();
        try {
            FileReader reader = new FileReader("src/main/resources/jsonTest/in.json");
            JSONParser jsonParser = new JSONParser();
            JSONObject jsonObject = (JSONObject) jsonParser.parse(reader);
            userMap = (Map<String, String>) jsonObject;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return userMap;
    }
    //ОТПРАВЛЯЕМ ТЕСТОВОЕ СООБЩЕНИЕ В ОЧЕРЕДЬ
    public void produceTestJson()
    {
        String message = "{\n" +
                "  \"id\": 1,\n" +
                "  \"name\":\"Сидоров\",\n" +
                "  \"type\": 1,\n" +
                "  \"post\":\"Директор\",\n" +
                "  \"passWord\":\"123\",\n" +
                "  \"isBlocked\":false\n" +
                "}";
        template.setExchange("myExchange");
        template.convertAndSend("myQueue", message);

    }
}
