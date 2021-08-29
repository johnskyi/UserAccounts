package com.example.UserAccounts.rabbitMQ;

import com.example.UserAccounts.controllers.UserController;
import lombok.AllArgsConstructor;
import org.springframework.amqp.rabbit.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@EnableRabbit
@Component
@AllArgsConstructor
public class RabbitConsumer {

    @Autowired
    private final UserController userController;

    @RabbitListener(queues = "myQueue")
    public void process2(String message)
    {
        userController.setMessage(message);
    }
}
