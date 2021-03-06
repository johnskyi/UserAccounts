package com.example.UserAccounts.rabbitMQ;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfiguration {

    @Bean
    public ConnectionFactory connectionFactory()
    {
        return new CachingConnectionFactory("localhost");
    }
    @Bean
    public AmqpAdmin amqpAdmin()
    {
        return new RabbitAdmin(connectionFactory());
    }
    @Bean
    public RabbitTemplate rabbitTemplate()
    {
        return new RabbitTemplate(connectionFactory());
    }
    @Bean
    public Queue myQueue()
    {
        return new Queue("myQueue");
    }
    @Bean
    public Queue anyQueue()
    {
        return new Queue("anyQueue");
    }


}
