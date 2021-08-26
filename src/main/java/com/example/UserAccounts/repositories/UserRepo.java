package com.example.UserAccounts.repositories;

import com.example.UserAccounts.entities.UserEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepo extends CrudRepository<UserEntity,Integer> {
}
