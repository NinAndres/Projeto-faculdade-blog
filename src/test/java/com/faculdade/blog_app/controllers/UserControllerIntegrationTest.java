package com.faculdade.blog_app.controllers;

import com.faculdade.blog_app.entities.User;
import com.faculdade.blog_app.repositories.UserRepository;

import jakarta.transaction.Transactional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

@ActiveProfiles("test")
@SpringBootTest
public class UserControllerIntegrationTest {

  @Autowired
  private UserController userController;

  @Autowired
  private UserRepository userRepository;

  @Test
  public void testFindAll() {
    userRepository.save(new User(null, "John Doe", "john@example.com", "password123"));
    userRepository.save(new User(null, "Jane Doe", "jane@example.com", "password456"));

    ResponseEntity<List<User>> response = userController.findAll();

    assertEquals(2, response.getBody().size());
    assertEquals("John Doe", response.getBody().get(0).getName());
    assertEquals(200, response.getStatusCode().value());
  }

  @Test
  public void testInsertUser() {
    User user = new User(null, "New User", "newuser@example.com", "newpassword");

    ResponseEntity<User> response = userController.insert(user);

    assertNotNull(response.getBody().getId());
    assertEquals("New User", response.getBody().getName());
    assertEquals(200, response.getStatusCode().value());
  }

  @Test
  @Transactional
  public void testDeleteUser() {
    User user = userRepository.save(new User(null, "John Doe", "john@example.com", "password123"));

    ResponseEntity<String> response = userController.delete(user.getId());

    assertFalse(userRepository.existsById(user.getId()));
    assertEquals(200, response.getStatusCode().value());
  }

  @Test
  public void testFindByIdUserNotFound() {
    ResponseEntity<?> response = userController.findById(999L);
    assertEquals(HttpStatus.NOT_FOUND.value(), response.getStatusCode().value());
  }
}
