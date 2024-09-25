package com.faculdade.blog_app.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

import com.faculdade.blog_app.entities.Post;
import com.faculdade.blog_app.entities.User;
import com.faculdade.blog_app.repositories.UserRepository;
import com.faculdade.blog_app.services.UserService;
import com.faculdade.blog_app.services.exception.ObjectNotFoundException;

@ActiveProfiles("test")
@SpringBootTest
public class UserServiceTest {

  @Autowired
  private UserService userService;

  @MockBean
  private UserRepository userRepository;

  private User user1;
  private User user2;

  @BeforeEach
  public void setUp() {
    user1 = new User(1L, "Xirumbinha", "xirumbinha@gmail.com", "12345");
    user2 = new User(2L, "Julia", "juju@gmail.com", "d456");
  }

  @Test
  public void testGetAll() {
    when(userRepository.findAll()).thenReturn(Arrays.asList(user1, user2));

    List<User> users = userService.getAll();

    assertEquals(2, users.size());
    assertNull(users.get(0).getPassword());
    assertNull(users.get(1).getPassword());
    verify(userRepository, times(1)).findAll();
  }

  @Test
  public void testFindById() {
    when(userRepository.findById(1L)).thenReturn(Optional.of(user1));

    User foundUser = userService.findById(1L);

    assertEquals("Xirumbinha", foundUser.getName());
    verify(userRepository, times(1)).findById(1L);
  }

  @Test
  public void testFindById_NotFound() {
    when(userRepository.findById(1L)).thenReturn(Optional.empty());

    assertThrows(ObjectNotFoundException.class, () -> userService.findById(1L));
    verify(userRepository, times(1)).findById(1L);
  }

  @Test
  public void testSave() {
    when(userRepository.save(user1)).thenReturn(user1);

    User savedUser = userService.save(user1);

    assertEquals("Xirumbinha", savedUser.getName());
    verify(userRepository, times(1)).save(user1);
  }

  @Test
  public void testDelete() {
    when(userRepository.findById(1L)).thenReturn(Optional.of(user1));

    userService.delete(1L);

    verify(userRepository, times(1)).deleteById(1L);
  }

  @Test
  public void testDelete_NotFound() {
    when(userRepository.findById(1L)).thenReturn(Optional.empty());

    assertThrows(ObjectNotFoundException.class, () -> userService.delete(1L));
    verify(userRepository, never()).deleteById(anyLong());
  }

  @Test
  public void testUpdate() {
    when(userRepository.findById(1L)).thenReturn(Optional.of(user1));
    when(userRepository.save(user1)).thenReturn(user1);

    user1.setName("Updated Name");
    User updatedUser = userService.update(user1);

    assertEquals("Updated Name", updatedUser.getName());
    verify(userRepository, times(1)).save(user1);
  }

  @Test
  public void testExistsById() {
    when(userRepository.existsById(1L)).thenReturn(true);

    boolean exists = userService.existsById(1L);

    assertTrue(exists);
    verify(userRepository, times(1)).existsById(1L);
  }

  @Test
  public void testFollowUser() {
    when(userRepository.findById(1L)).thenReturn(Optional.of(user1));
    when(userRepository.findById(2L)).thenReturn(Optional.of(user2));
    when(userRepository.save(user1)).thenReturn(user1);

    userService.followUser(1L, 2L);

    assertTrue(user1.getSeguindo().contains(user2));
    assertTrue(user2.getSeguidores().contains(user1));
    verify(userRepository, times(1)).save(user1);
  }

  @Test
  public void testUnfollowUser() {
    user1.getSeguindo().add(user2);
    user2.getSeguidores().add(user1);
    when(userRepository.findById(1L)).thenReturn(Optional.of(user1));
    when(userRepository.findById(2L)).thenReturn(Optional.of(user2));
    when(userRepository.save(user1)).thenReturn(user1);

    userService.unfollowUser(1L, 2L);

    assertFalse(user1.getSeguindo().contains(user2));
    assertFalse(user2.getSeguidores().contains(user1));
    verify(userRepository, times(1)).save(user1);
  }

  @Test
  public void testCountUserPosts() {
    when(userRepository.findById(1L)).thenReturn(Optional.of(user1));
    user1.setPosts(Arrays.asList(new Post(), new Post()));

    int postCount = userService.countUserPosts(1L);

    assertEquals(2, postCount);
    verify(userRepository, times(1)).findById(1L);
  }

  @Test
  public void testCountUserPosts_NotFound() {
    when(userRepository.findById(1L)).thenReturn(Optional.empty());

    assertThrows(ObjectNotFoundException.class, () -> userService.countUserPosts(1L));
    verify(userRepository, times(1)).findById(1L);
  }
}
