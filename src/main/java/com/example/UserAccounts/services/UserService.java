package com.example.UserAccounts.services;

import com.example.UserAccounts.entities.UserEntity;
import com.example.UserAccounts.repositories.UserRepo;
import lombok.AllArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;


import java.util.*;

@Service
@AllArgsConstructor
public class UserService {

    private static final Marker ADD_HISTORY_MARKER = MarkerManager.getMarker("ADD_HISTORY");
    private static final Marker UPDATE_USER_MARKER = MarkerManager.getMarker("UPDATE_USER");
    private static final Marker BLOCKED_USER_MARKER = MarkerManager.getMarker("BLOCKED_USER");
    private static final Logger LOGGER = LogManager.getLogger(UserService.class);


    @Autowired
    private final RabbitTemplate template;

    @Autowired
    private final UserRepo userRepo;


    //ДОБАВЛЯЕМ ПОЛЬЗОВАТЕЛЯ
    public ResponseEntity<UserEntity> add(Map<String, String> userMap) {

        UserEntity user = UserEntity.builder()
                .name(userMap.get("name"))
                .post(userMap.get("post"))
                .passWord(userMap.get("passWord"))
                .build();
        userRepo.save(user);
        LOGGER.info(ADD_HISTORY_MARKER, "Добавлен пользователь: {}", user.getId());
        String message = "{\n" +
                "  \"id\": " + user.getId() + ",\n" +
                "  \"name\":\"" + user.getName() + "\",\n" +
                "  \"post\":\"" + user.getPost() + "\",\n" +
                "  \"passWord\":\"" + user.getPassWord() + "\",\n" +
                "  \"isBlocked\":" + user.isBlocked() + "\n" +
                "}";

        template.convertAndSend("anyQueue", message);

        return ResponseEntity.ok(userRepo.findById(user.getId()).get());

    }

    //МЕНЯЕМ ДАННЫЕ ПОЛЬЗОВАТЕЛЯ
    public ResponseEntity<UserEntity> update(Map<String, String> userMap) {

        int id = Integer.parseInt(String.valueOf(userMap.get("id")));

        Optional<UserEntity> userOptional = userRepo.findById(id);
        if (userOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        userOptional.get().setName(userMap.get("name"));
        userOptional.get().setPost(userMap.get("post"));
        userOptional.get().setPassWord(userMap.get("passWord"));

        userRepo.save(userOptional.get());

        LOGGER.info(UPDATE_USER_MARKER, "Обновлены данные пользователя {}", userOptional.get().getId());


        String message = "{\n" +
                "  \"id\": " + userOptional.get().getId() + ",\n" +
                "  \"name\":\"" + userOptional.get().getName() + "\",\n" +
                "  \"post\":\"" + userOptional.get().getPost() + "\",\n" +
                "  \"passWord\":\"" + userOptional.get().getPassWord() + "\",\n" +
                "  \"isBlocked\":" + userOptional.get().isBlocked() + "\n" +
                "}";

        template.convertAndSend("anyQueue", message);

        return ResponseEntity.ok(userOptional.get());
    }

    //БЛОКИРУЕМ ПОЛЬЗОВАТЕЛЯ
    public ResponseEntity<UserEntity> blocked(Map<String, String> userMap) {


        int id = Integer.parseInt(String.valueOf(userMap.get("id")));


        Optional<UserEntity> userOptional = userRepo.findById(id);
        if (userOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        userOptional.get().setBlocked(true);

        userRepo.save(userOptional.get());

        LOGGER.info(BLOCKED_USER_MARKER, "Заблокирован пользователь: {}", userOptional.get().getId());


        String message = "{\n" +
                "  \"id\": " + userOptional.get().getId() + ",\n" +
                "  \"name\":\"" + userOptional.get().getName() + "\",\n" +
                "  \"post\":\"" + userOptional.get().getPost() + "\",\n" +
                "  \"passWord\":\"" + userOptional.get().getPassWord() + "\",\n" +
                "  \"isBlocked\":" + userOptional.get().isBlocked() + "\n" +
                "}";
        template.convertAndSend("anyQueue", message);

        return ResponseEntity.ok(userOptional.get());
    }
}
