package com.faculdade.blog_app.controllers;

import com.faculdade.blog_app.entities.Author;
import com.faculdade.blog_app.entities.Post;
import com.faculdade.blog_app.entities.User;
import com.faculdade.blog_app.repositories.AuthorRepository;
import com.faculdade.blog_app.repositories.PostRepository;
import com.faculdade.blog_app.repositories.UserRepository;

import jakarta.transaction.Transactional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

@ActiveProfiles("test")
@SpringBootTest
public class PostControllerIntegrationTest {

  @Autowired
  private PostController postController;

  @Autowired
  private PostRepository postRepository;

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private AuthorRepository authorRepository;

  private SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

  @Test
  public void testFindAllPosts() throws ParseException {
    User user = userRepository.save(new User(null, "Laura", "laura@gmail.com", "12345"));
    Author author = authorRepository.save(new Author(null, "Joao"));

    postRepository
        .save(new Post(null, sdf.parse("22/04/2024"), "Post um", "Body do primeiro post", user, author, true));
    postRepository
        .save(new Post(null, sdf.parse("22/04/2024"), "Post dois", "Body do segundo post", user, author, true));

    ResponseEntity<List<Post>> response = postController.findAll();

    assertEquals(2, response.getBody().size());
    assertEquals("Post um", response.getBody().get(0).getTitle());
    assertEquals(200, response.getStatusCode().value());
  }

  @Test
  public void testInsertPost() throws ParseException {
    User user = userRepository.save(new User(null, "Laura", "laura@gmail.com", "12345"));
    Author author = authorRepository.save(new Author(null, "Joao"));

    Post post = new Post(null, sdf.parse("24/07/2002"), "Test Post", "Test Content", user, author, true);

    ResponseEntity<Post> response = postController.insert(post);

    assertNotNull(response.getBody().getId());
    assertEquals("Test Post", response.getBody().getTitle());
    assertEquals(200, response.getStatusCode().value());
  }

  @Test
  @Transactional
  public void testDeletePost() throws ParseException {

    User user = userRepository.save(new User(null, "Laura", "laura@gmail.com", "12345"));
    Author author = authorRepository.save(new Author(null, "Joao"));

    Post post = postRepository
        .save(new Post(null, sdf.parse("24/07/2002"), "Test Post", "Test Content", user, author, true));

    ResponseEntity<String> response = postController.delete(post.getId());

    assertFalse(postRepository.existsById(post.getId()));
    assertEquals(200, response.getStatusCode().value());
  }

  @Test
  public void testFindById_PostNotFound() {
    ResponseEntity<Post> response = postController.findById(999L);

    assertEquals(404, response.getStatusCode().value());
  }
}
