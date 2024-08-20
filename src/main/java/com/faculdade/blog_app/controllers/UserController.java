package com.faculdade.blog_app.controllers;

import java.net.URI;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.faculdade.blog_app.entities.Post;
import com.faculdade.blog_app.entities.User;
import com.faculdade.blog_app.services.UserService;

@RestController
@RequestMapping(value = "/users")
public class UserController {

  @Autowired
  private UserService service;

  @GetMapping
  public ResponseEntity<List<User>> findAll() {
    List<User> users = service.getAll();
    return ResponseEntity.ok().body(users);
  }

  @GetMapping(value = "/{id}")
  public ResponseEntity<User> findById(@PathVariable Long id) {
    User user = service.findById(id);
    if (user != null) {
      return ResponseEntity.ok().body(user);
    } else {
      return ResponseEntity.notFound().build();
    }
  }

  @PostMapping
  public ResponseEntity<Void> insert(@RequestBody User user) {
    user = service.save(user);
    URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
        .path("/{id}")
        .buildAndExpand(user.getId())
        .toUri();
    return ResponseEntity.created(uri).build();
  }

  @DeleteMapping(value = "/{id}")
  public ResponseEntity<Void> delete(@PathVariable Long id) {
    if (service.existsById(id)) {
      service.delete(id);
      return ResponseEntity.noContent().build();
    } else {
      return ResponseEntity.notFound().build();
    }
  }

  @PutMapping(value = "/{id}")
  public ResponseEntity<Void> update(@RequestBody User user, @PathVariable Long id) {
    if (service.existsById(id)) {
      user.setId(id);
      service.update(user);
      return ResponseEntity.noContent().build();
    } else {
      return ResponseEntity.notFound().build();
    }
  }

  @GetMapping(value = "/{id}/posts")
  public ResponseEntity<List<Post>> findPosts(@PathVariable Long id) {
    User user = service.findById(id);
    if (user != null) {
      return ResponseEntity.ok().body(user.getPosts());
    } else {
      return ResponseEntity.notFound().build();
    }
  }

  @PostMapping(value = "/{id}/seguir/{seguirId}")
  public ResponseEntity<Void> followUser(@PathVariable Long id, @PathVariable Long followId) {
    if (service.existsById(id) && service.existsById(followId)) {
      service.followUser(id, followId);
      return ResponseEntity.noContent().build();
    } else {
      return ResponseEntity.notFound().build();
    }
  }

  @DeleteMapping(value = "/{id}/deixarDeSeguir/{seguindoId}")
  public ResponseEntity<Void> unfollowUser(@PathVariable Long id, @PathVariable Long followId) {
    if (service.existsById(id) && service.existsById(followId)) {
      service.unfollowUser(id, followId);
      return ResponseEntity.noContent().build();
    } else {
      return ResponseEntity.notFound().build();
    }
  }

  @GetMapping(value = "/{id}/seguidores")
  public ResponseEntity<List<User>> getFollowers(@PathVariable Long id) {
    User user = service.findById(id);
    if (user != null) {
      return ResponseEntity.ok().body(user.getSeguidores());
    } else {
      return ResponseEntity.notFound().build();
    }
  }

  @GetMapping(value = "/{id}/seguindo")
  public ResponseEntity<List<User>> getFollowing(@PathVariable Long id) {
    User user = service.findById(id);
    if (user != null) {
      return ResponseEntity.ok().body(user.getSeguindo());
    } else {
      return ResponseEntity.notFound().build();
    }
  }
}
