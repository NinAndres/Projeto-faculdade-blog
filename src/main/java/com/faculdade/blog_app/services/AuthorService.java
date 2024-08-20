package com.faculdade.blog_app.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.faculdade.blog_app.entities.Author;
import com.faculdade.blog_app.repositories.AuthorRepository;
import com.faculdade.blog_app.services.exception.ObjectNotFoundException;

@Service
public class AuthorService {

  @Autowired
  private AuthorRepository repository;

  public Author findById(Long id) {
    Optional<Author> obj = repository.findById(id);
    return obj.orElseThrow(() -> new ObjectNotFoundException("Author not found"));
  }

}
