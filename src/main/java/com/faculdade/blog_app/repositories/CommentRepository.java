package com.faculdade.blog_app.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.faculdade.blog_app.entities.Comment;

public interface CommentRepository extends JpaRepository<Comment, Long> {

}
