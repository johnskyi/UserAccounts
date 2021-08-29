package com.example.UserAccounts;

import com.example.UserAccounts.services.UserService;
import lombok.AllArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Map;

@SpringBootApplication
@AllArgsConstructor
public class UserAccountsApplication {


	public static void main(String[] args) {
		SpringApplication.run(UserAccountsApplication.class, args);
	}

}
