package com.faculdade.blog_app.controllers;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import com.faculdade.blog_app.entities.Author;
import com.faculdade.blog_app.entities.Comment;
import com.faculdade.blog_app.entities.Post;
import com.faculdade.blog_app.entities.User;
import com.faculdade.blog_app.repositories.CommentRepository;
import com.faculdade.blog_app.services.PostService;
import com.faculdade.blog_app.services.UserService;
import com.faculdade.blog_app.services.exception.ObjectNotFoundException;

@ActiveProfiles("test")
@SpringBootTest
public class PostControllerTest {

  @Autowired
  private PostController postController;

  @MockBean
  private PostService postService;

  @MockBean
  private UserService userService;

  @Mock
  private CommentRepository commentRepository;

  private Post post;
  private Post post1;
  private Post post2;
  private Post postAtivo;
  private Post postInativo;
  private User user;
  private Author author;

  private SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

  @BeforeEach
  public void setUp() throws ParseException {
    MockitoAnnotations.openMocks(this);
    user = new User(1L, "John Doe", "john@example.com", "password123");
    author = new Author(1L, "Richard");
    post = new Post(1L, sdf.parse("24/10/2002"), "Test Title", "Test Content", user, author, true);
    post1 = new Post(2L, sdf.parse("24/10/2002"), "Post 1", "Test post 1", user, author, true);
    post2 = new Post(3L, sdf.parse("24/10/2002"), "Post 2", "Test post 2", user, author, true);
    postAtivo = new Post(1L, sdf.parse("24/09/2024"), "Ativo", "Post ativo", new User(), new Author(), true);
    postInativo = new Post(2L, sdf.parse("25/09/2024"), "Inativo", "Post inativo", new User(), new Author(),
        false);

  }

  @Test
  public void testGetPostsWithMoreComments() {

    post1.getComments().add(new Comment());
    post1.getComments().add(new Comment());
    post2.getComments().add(new Comment());

    user.setPosts(Arrays.asList(post, post2));

    when(userService.findById(1L)).thenReturn(user);
    when(postService.postsOrdenadoPorMaisComments(1L)).thenReturn(Arrays.asList(post1, post2));

    ResponseEntity<List<Post>> response = postController.getPostsSortedByComments(1L);

    assertEquals(200, response.getStatusCode().value());
    assertEquals(2, response.getBody().size());
    assertEquals("Post 1", response.getBody().get(0).getTitle());
    assertEquals("Post 2", response.getBody().get(1).getTitle());
  }

  @Test
  public void testFindAll() {
    List<Post> posts = new ArrayList<>();
    posts.add(post);

    when(postService.getAll()).thenReturn(posts);

    ResponseEntity<List<Post>> response = postController.findAll();

    assertEquals(200, response.getStatusCode().value());
    assertEquals(posts, response.getBody());
  }

  @Test
  public void testFindByIdFound() {
    when(postService.findById(1L)).thenReturn(post);

    ResponseEntity<Post> response = postController.findById(1L);

    assertEquals(200, response.getStatusCode().value());
    assertEquals(post, response.getBody());
  }

  @Test
  public void testFindByIdNotFound() {
    when(postService.findById(999L)).thenThrow(new ObjectNotFoundException("Post not found"));

    ResponseEntity<Post> response = postController.findById(999L);

    assertEquals(404, response.getStatusCode().value());
  }

  @Test
  public void testFindByTitle() {
    List<Post> posts = new ArrayList<>();
    posts.add(post);

    when(postService.findByTitle("Test")).thenReturn(posts);

    ResponseEntity<List<Post>> response = postController.findByTitle("Test");

    assertEquals(200, response.getStatusCode().value());
    assertEquals(posts, response.getBody());
  }

  @Test
  public void testInsert() {
    when(userService.findById(any(Long.class))).thenReturn(user);
    when(postService.save(any(Post.class))).thenReturn(post);

    ResponseEntity<Post> response = postController.insert(post);

    assertEquals(200, response.getStatusCode().value());
    assertEquals(post, response.getBody());
  }

  @Test
  public void testUpdate() {
    when(postService.update(any(Post.class))).thenReturn(post);

    ResponseEntity<String> response = postController.update(post, post.getId());

    assertEquals(200, response.getStatusCode().value());
    assertEquals("Post alterado com sucesso", response.getBody());
  }

  @Test
  public void testDeleteFound() {
    when(postService.existsById(1L)).thenReturn(true);

    ResponseEntity<String> response = postController.delete(1L);

    assertEquals(200, response.getStatusCode().value());
    assertEquals("Post deletado com sucesso", response.getBody());
  }

  @Test
  public void testDeleteNotFound() {
    when(postService.findById(999L)).thenReturn(null);

    ResponseEntity<String> response = postController.delete(999L);

    assertEquals(404, response.getStatusCode().value());
  }

  @Test
  public void testGetPostsByStatusTrue() {
    when(postService.findByActiveStatus(true)).thenReturn(Arrays.asList(postAtivo));

    ResponseEntity<List<Post>> response = postController.getPostsByStatus(true);

    assertEquals(200, response.getStatusCode().value());
    assertEquals(1, response.getBody().size());
    assertEquals(postAtivo, response.getBody().get(0));
  }

  @Test
  public void testGetPostsByStatusFalse() {
    when(postService.findByActiveStatus(false)).thenReturn(Arrays.asList(postInativo));

    ResponseEntity<List<Post>> response = postController.getPostsByStatus(false);
    assertEquals(200, response.getStatusCode().value());
    assertEquals(1, response.getBody().size());
    assertEquals(postInativo, response.getBody().get(0));
  }
}
