package com.example.UserAccounts.entities;
import lombok.Builder;
import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Builder
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)

    private int id;

    private String name;

    private String post;

    private String passWord;

    private boolean isBlocked;

}
