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
    @Bean
    public FanoutExchange fanoutExchange()
    {
        return new FanoutExchange("myExchange");
    }
    @Bean
    public FanoutExchange fanoutExchange2()
    {
        return new FanoutExchange("anyExchange");
    }
    @Bean
    public Binding binding()
    {
        return BindingBuilder.bind(myQueue()).to(fanoutExchange());
    }
    @Bean
    public Binding binding2()
    {
        return BindingBuilder.bind(anyQueue()).to(fanoutExchange2());
    }


}
