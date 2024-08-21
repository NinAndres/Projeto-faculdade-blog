package com.faculdade.blog_app.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.faculdade.blog_app.entities.Comment;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

}
