package com.faculdade.blog_app.controllers;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.faculdade.blog_app.entities.Comment;
import com.faculdade.blog_app.entities.Post;
import com.faculdade.blog_app.entities.User;
import com.faculdade.blog_app.repositories.CommentRepository;
import com.faculdade.blog_app.services.PostService;
import com.faculdade.blog_app.services.UserService;
import com.faculdade.blog_app.util.URL;

@RestController
@RequestMapping(value = "/posts")
public class PostController {
  @Autowired
  private PostService postService;

  @Autowired
  private UserService userService;

  @Autowired
  private CommentRepository commentRepository;

  @GetMapping
  public ResponseEntity<List<Post>> findAll() {
    List<Post> posts = postService.getAll();
    return ResponseEntity.ok().body(posts);
  }

  @GetMapping(value = "/{id}")
  public ResponseEntity<Post> findById(@PathVariable Long id) {
    Post obj = postService.findById(id);
    return ResponseEntity.ok().body((obj));
  }

  @GetMapping(value = "/findByTitle")
  public ResponseEntity<List<Post>> findByTitle(@RequestParam(value = "text", defaultValue = "") String text) {
    text = URL.decodeParam(text);
    List<Post> list = postService.findByTitle(text);
    return ResponseEntity.ok().body(list);
  }

  @GetMapping(value = "/fullsearch")
  public ResponseEntity<List<Post>> fullSearch(
      @RequestParam(value = "text", defaultValue = "") String text,
      @RequestParam(value = "minDate", defaultValue = "") String minDate,
      @RequestParam(value = "maxDate", defaultValue = "") String maxDate) {
    text = URL.decodeParam(text);
    Date min = URL.converteDate(minDate, new Date(0L));
    Date max = URL.converteDate(maxDate, new Date());
    List<Post> list = postService.fullSearch(text, min, max);
    return ResponseEntity.ok().body(list);
  }

  @PostMapping(value = "/save")
  public ResponseEntity<Post> insert(@RequestBody Post obj) {
    User user = obj.getUser();
    if (user != null) {
      user = userService.findById(user.getId());
      obj.setUser(user);
    }

    obj = postService.save(obj);
    for (Comment comment : obj.getComments()) {
      comment.setPost(obj);
      commentRepository.save(comment);
    }

    return ResponseEntity.ok(obj);
  }

  @PutMapping(value = "/{id}")
  public ResponseEntity<Void> update(@RequestBody Post obj, @PathVariable Long id) {
    obj.setId(id);
    postService.update(obj);
    return ResponseEntity.noContent().build();
  }

  @DeleteMapping(value = "/{id}")
  public ResponseEntity<Void> delete(@PathVariable Long id) {
    postService.delete(id);
    return ResponseEntity.noContent().build();
  }
}