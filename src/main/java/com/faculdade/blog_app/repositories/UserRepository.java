package com.faculdade.blog_app.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.faculdade.blog_app.entities.User;

public interface UserRepository extends JpaRepository<User, Long> {

}
