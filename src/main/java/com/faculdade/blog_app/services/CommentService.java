package com.faculdade.blog_app.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.faculdade.blog_app.entities.Comment;
import com.faculdade.blog_app.repositories.CommentRepository;

@Service
public class CommentService {

  @Autowired
  private CommentRepository repository;

  public Comment save(Comment obj) {
    return repository.save(obj);
  }

  public void delete(Comment obj) {
    repository.delete(obj);
  }

}
