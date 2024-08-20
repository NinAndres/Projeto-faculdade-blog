package com.faculdade.blog_app.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.faculdade.blog_app.entities.Author;

public interface AuthorRepository extends JpaRepository<Author, Long> {

}
