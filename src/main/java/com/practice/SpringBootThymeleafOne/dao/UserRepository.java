package com.practice.SpringBootThymeleafOne.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.practice.SpringBootThymeleafOne.entity.User;

public interface UserRepository extends JpaRepository<User, Integer>{

}
