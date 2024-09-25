package com.faculdade.blog_app.controllers;

import com.faculdade.blog_app.entities.Author;
import com.faculdade.blog_app.entities.Post;
import com.faculdade.blog_app.entities.User;
import com.faculdade.blog_app.services.UserService;
import com.faculdade.blog_app.services.exception.ObjectNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ActiveProfiles("test")
@SpringBootTest
class UserControllerTest {

  @Autowired
  private UserController userController;

  @MockBean
  private UserService userService;

  private User user;
  private Author author;

  private SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

  @BeforeEach
  void setUp() {
    user = new User(1L, "John Doe", "john@example.com", "password123");
    author = new Author(1L, "Frederico");
  }

  @Test
  void testFindAll() {
    List<User> users = new ArrayList<>();
    users.add(user);
    when(userService.getAll()).thenReturn(users);

    ResponseEntity<List<User>> response = userController.findAll();

    assertEquals(200, response.getStatusCode().value());
    assertEquals(1, response.getBody().size());
    assertEquals("John Doe", response.getBody().get(0).getName());
  }

  @Test
  void testFindById() {
    User mockUser = new User(1L, "Jonatan", "jonatan@gmail.com", "12345");
    when(userService.findById(1L)).thenReturn(mockUser);

    ResponseEntity<?> response = userController.findById(1L);

    assertEquals(HttpStatus.OK.value(), response.getStatusCode().value());

    User returnedUser = (User) response.getBody();
    assertEquals("Jonatan", returnedUser.getName());
  }

  @Test
  void testFindByIdUserNotFound() {
    when(userService.findById(999L)).thenThrow(new ObjectNotFoundException("Usuario nao encontrado"));

    ResponseEntity<?> response = userController.findById(999L);

    assertEquals(404, response.getStatusCode().value());
  }

  @Test
  void testInsert() {
    when(userService.save(any(User.class))).thenReturn(user);

    ResponseEntity<User> response = userController.insert(user);

    assertEquals(200, response.getStatusCode().value());
    assertEquals("John Doe", response.getBody().getName());
  }

  @Test
  void testDelete() {
    when(userService.existsById(1L)).thenReturn(true);

    ResponseEntity<String> response = userController.delete(1L);

    assertEquals(200, response.getStatusCode().value());
    verify(userService, times(1)).delete(1L);
  }

  @Test
  void testDeleteUserNotFound() {
    when(userService.existsById(999L)).thenReturn(false);

    ResponseEntity<String> response = userController.delete(999L);

    assertEquals(404, response.getStatusCode().value());
  }

  @Test
  void testUpdate() {
    when(userService.existsById(1L)).thenReturn(true);

    ResponseEntity<String> response = userController.update(user, 1L);

    assertEquals(200, response.getStatusCode().value());
    assertEquals("Usuario atualizado com sucesso", response.getBody());
    verify(userService, times(1)).update(any(User.class));
  }

  @Test
  void testUpdateUserNotFound() {
    when(userService.existsById(999L)).thenReturn(false);

    ResponseEntity<String> response = userController.update(user, 999L);

    assertEquals(404, response.getStatusCode().value());
    assertEquals("Usuario nao encontrado", response.getBody());
  }

  @Test
  public void testFindPosts_UserFound() throws ParseException {
    Long userId = 1L;
    User mockUser = new User(userId, "John Doe", "john@example.com", "12345");
    List<Post> mockPosts = Arrays.asList(
        new Post(null, sdf.parse("24/09/2024"), "Segundo post", "Body content segundo post", user, author, true));
    new Post(null, sdf.parse("24/09/2024"), "Segundo post", "Body content segundo post", user, author, true);
    mockUser.setPosts(mockPosts);

    when(userService.findById(userId)).thenReturn(mockUser);

    ResponseEntity<?> response = userController.findPosts(userId);

    assertEquals(200, response.getStatusCode().value());
    assertTrue(response.getBody() instanceof List);
    assertEquals(mockPosts, response.getBody());
  }

  @Test
  public void testFindPosts_UserNotFound() {
    Long userId = 999L;
    when(userService.findById(userId)).thenThrow(new ObjectNotFoundException("Usuario nao encontrado"));

    ResponseEntity<?> response = userController.findPosts(userId);

    assertEquals(404, response.getStatusCode().value());
    assertEquals("Usuario nao encontrado", response.getBody());
  }

  @Test
  void testGetFollowers() {
    List<User> followers = new ArrayList<>();
    user.setSeguidores(followers);
    when(userService.findById(1L)).thenReturn(user);

    ResponseEntity<List<User>> response = userController.getFollowers(1L);

    assertEquals(200, response.getStatusCode().value());
    assertEquals(0, response.getBody().size());
  }

  @Test
  void testGetFollowing() {
    List<User> following = new ArrayList<>();
    user.setSeguindo(following);
    when(userService.findById(1L)).thenReturn(user);

    ResponseEntity<List<User>> response = userController.getFollowing(1L);

    assertEquals(200, response.getStatusCode().value());
    assertEquals(0, response.getBody().size());
  }

  @Test
  void testFollowUser() {
    when(userService.existsById(1L)).thenReturn(true);
    when(userService.existsById(2L)).thenReturn(true);

    ResponseEntity<String> response = userController.followUser(1L, 2L);

    assertEquals(200, response.getStatusCode().value());
    assertEquals("Usuario seguido com sucesso", response.getBody());
    verify(userService, times(1)).followUser(1L, 2L);
  }

  @Test
  void testFollowUserNotFound() {
    when(userService.existsById(999L)).thenReturn(false);

    ResponseEntity<String> response = userController.followUser(999L, 2L);

    assertEquals(404, response.getStatusCode().value());
    assertEquals("Usuario nao encontrado", response.getBody());
  }

  @Test
  void testUnfollowUser() {
    when(userService.existsById(1L)).thenReturn(true);
    when(userService.existsById(2L)).thenReturn(true);

    ResponseEntity<String> response = userController.unfollowUser(1L, 2L);

    assertEquals(200, response.getStatusCode().value());
    assertEquals("Deixou de seguir o usuario com sucesso", response.getBody());
    verify(userService, times(1)).unfollowUser(1L, 2L);
  }

  @Test
  void testUnfollowUserNotFound() {
    when(userService.existsById(999L)).thenReturn(false);

    ResponseEntity<String> response = userController.unfollowUser(999L, 2L);

    assertEquals(404, response.getStatusCode().value());
    assertEquals("Usuario nao encontrado", response.getBody());
  }
}
