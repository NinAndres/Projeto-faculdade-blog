package com.faculdade.blog_app.services;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.faculdade.blog_app.entities.Post;
import com.faculdade.blog_app.entities.User;
import com.faculdade.blog_app.repositories.PostRepository;
import com.faculdade.blog_app.services.exception.ObjectNotFoundException;

@Service
public class PostService {

  @Autowired
  private PostRepository repository;

  public Post save(Post post) {
    return repository.save(post);
  }

  public Post update(Post post) {
    Post postExists = findById(post.getId());
    postExists.setTitle(post.getTitle());
    postExists.setBody(post.getBody());
    postExists.setDate(post.getDate());

    return repository.save(post);
  }

  public void delete(Long id) {
    findById(id);
    repository.deleteById(id);
  }

  public Post findById(Long id) {
    Optional<Post> obj = repository.findById(id);
    return obj.orElseThrow(() -> new ObjectNotFoundException("Post not found"));
  }

  public List<Post> getAll() {
    return repository.findAll();
  }

  public List<Post> findByTitle(String title) {
    return repository.findByTitle(title);
  }

  public List<Post> fullSearch(String text, Date minDate, Date maxDate) {
    return repository.findPostsByTextAndDate(text, minDate, maxDate);
  }

  public List<Post> findByUser(User user) {
    return repository.findByUser(user);
  }

}
