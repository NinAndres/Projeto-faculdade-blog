package com.faculdade.blog_app.services;

import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

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

  @Autowired
  private UserService userService;

  public Post save(Post post) {
    return repository.save(post);
  }

  public Post update(Post post) {
    Post postExists = findById(post.getId());
    postExists.setTitle(post.getTitle());
    postExists.setBody(post.getBody());
    postExists.setDate(post.getDate());
    postExists.setActive(post.isActive());

    return repository.save(post);
  }

  public List<Post> findByActiveStatus(boolean active) {
    return repository.findByActive(active);
  }

  public void delete(Long id) {
    Post post = findById(id);
    if (post == null) {
      throw new ObjectNotFoundException("Post não encontrado");
    }
    repository.deleteById(id);
  }

  public Post findById(Long id) {
    Post post = repository.findById(id).orElseThrow(() -> new ObjectNotFoundException("Post not found"));
    if (post.getUser() != null) {
      post.getUser().setPassword(null);
    }
    return post;
  }

  public boolean existsById(Long id) {
    return repository.existsById(id);
  }

  public List<Post> getAll() {
    List<Post> posts = repository.findAll();
    for (Post post : posts) {
      post.getUser().setPassword(null);
    }
    return posts;
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

  public List<Post> postsOrdenadoPorMaisComments(Long userId) {
    User user = userService.findById(userId);

    List<Post> posts = user.getPosts();

    Collections.sort(posts, new Comparator<Post>() {
      @Override
      public int compare(Post p1, Post p2) {
        return Integer.compare(p2.getComments().size(), p1.getComments().size());
      }
    });

    return posts;
  }

}
