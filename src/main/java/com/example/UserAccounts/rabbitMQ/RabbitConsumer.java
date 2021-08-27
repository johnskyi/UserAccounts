package com.example.UserAccounts.rabbitMQ;

import org.springframework.amqp.rabbit.annotation.*;
import org.springframework.stereotype.Component;

@EnableRabbit
@Component
public class RabbitConsumer {

    @RabbitListener(bindings =
    @QueueBinding(
            value = @Queue(value = "myQueue"),
            exchange = @Exchange(value = "myExchange")
    )
    )
    public void process(String message)
    {
        System.out.println("Сообщение получено!");
    }
    @RabbitListener(bindings =
    @QueueBinding(
            value = @Queue(value = "anyQueue"),
            exchange = @Exchange(value = "anyExchange")
    )
    )
    public void process2(String message)
    {
        System.out.println("Читаем выходное сообщение");
        System.out.println(message);
    }
}
