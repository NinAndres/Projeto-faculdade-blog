package com.faculdade.blog_app.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.faculdade.blog_app.entities.Post;
import com.faculdade.blog_app.entities.User;
import com.faculdade.blog_app.services.UserService;
import com.faculdade.blog_app.services.exception.ObjectNotFoundException;

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
  public ResponseEntity<?> findById(@PathVariable Long id) {
    try {
      User user = service.findById(id);
      return ResponseEntity.status(HttpStatus.OK).body(user);
    } catch (ObjectNotFoundException e) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuario nao encontrado");
    }
  }

  @PostMapping(value = "/save")
  public ResponseEntity<User> insert(@RequestBody User user) {
    user = service.save(user);
    return ResponseEntity.ok(user);
  }

  @DeleteMapping(value = "/{id}")
  public ResponseEntity<String> delete(@PathVariable Long id) {
    if (service.existsById(id)) {
      service.delete(id);
      return ResponseEntity.status(HttpStatus.OK).body("Usuario deletado com sucesso");
    } else {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuario nao encontrado");
    }
  }

  @PutMapping(value = "/{id}")
  public ResponseEntity<String> update(@RequestBody User user, @PathVariable Long id) {
    if (service.existsById(id)) {
      user.setId(id);
      service.update(user);
      return ResponseEntity.ok("Usuario atualizado com sucesso");
    } else {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuario nao encontrado");
    }
  }

  @GetMapping(value = "/{id}/posts")
  public ResponseEntity<?> findPosts(@PathVariable Long id) {
    try {
      User user = service.findById(id);
      return ResponseEntity.ok().body(user.getPosts());
    } catch (ObjectNotFoundException e) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuario nao encontrado");
    }
  }

  @PostMapping(value = "/{id}/seguir/{followId}")
  public ResponseEntity<String> followUser(@PathVariable Long id, @PathVariable Long followId) {
    if (service.existsById(id) && service.existsById(followId)) {
      service.followUser(id, followId);
      return ResponseEntity.ok("Usuario seguido com sucesso");
    } else {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuario nao encontrado");
    }
  }

  @DeleteMapping(value = "/{id}/deixarDeSeguir/{followId}")
  public ResponseEntity<String> unfollowUser(@PathVariable Long id, @PathVariable Long followId) {
    if (service.existsById(id) && service.existsById(followId)) {
      service.unfollowUser(id, followId);
      return ResponseEntity.ok("Deixou de seguir o usuario com sucesso");
    } else {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuario nao encontrado");
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
