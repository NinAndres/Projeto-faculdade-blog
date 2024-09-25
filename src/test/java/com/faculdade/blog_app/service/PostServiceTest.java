package com.faculdade.blog_app.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

import com.faculdade.blog_app.entities.Author;
import com.faculdade.blog_app.entities.Comment;
import com.faculdade.blog_app.entities.Post;
import com.faculdade.blog_app.entities.User;
import com.faculdade.blog_app.repositories.PostRepository;
import com.faculdade.blog_app.services.PostService;
import com.faculdade.blog_app.services.UserService;
import com.faculdade.blog_app.services.exception.ObjectNotFoundException;

@ActiveProfiles("test")
@SpringBootTest
public class PostServiceTest {

  @MockBean
  private PostRepository postRepository;

  @MockBean
  private UserService userService;

  @Autowired
  private PostService postService;

  private SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

  private Post postAtivo;
  private Post postInativo;
  private Post post1;
  private Post post2;
  private User user;
  private Post post;

  @BeforeEach
  public void setUp() throws ParseException {
    postAtivo = new Post(1L, sdf.parse("24/09/2024"), "Ativo", "Post ativo", new User(), new Author(), true);
    postInativo = new Post(2L, sdf.parse("25/09/2024"), "Inativo", "Post inativo", new User(), new Author(),
        false);

    user = new User(1L, "Joaozinho", "joaozinho@gmail.com", "12345");
    post = new Post(1L, sdf.parse("24/09/2024"), "Post Title", "Post body", user, null, true);
    post1 = new Post(1L, sdf.parse("24/09/2024"), "Post 1", "Conteúdo do post 1", user, new Author(), true);
    post2 = new Post(2L, sdf.parse("24/09/2024"), "Post 2", "Conteúdo do post 2", user, new Author(), true);

  }

  @Test
  public void testFindByTitle() {
    when(postRepository.findByTitle("Post 1")).thenReturn(Arrays.asList(post1));

    List<Post> result = postService.findByTitle("Post 1");

    assertEquals(1, result.size());
    assertEquals(post1, result.get(0));
  }

  @Test
  public void testFullSearch() {
    Date minDate = new Date(1000000000L);
    Date maxDate = new Date(2000000000L);
    when(postRepository.findPostsByTextAndDate("post", minDate, maxDate)).thenReturn(Arrays.asList(post1, post2));

    List<Post> result = postService.fullSearch("post", minDate, maxDate);
    assertEquals(2, result.size());
  }

  @Test
  public void testFindByUser() {
    when(postRepository.findByUser(user)).thenReturn(Arrays.asList(post1, post2));

    List<Post> result = postService.findByUser(user);

    assertEquals(2, result.size());
  }

  @Test
  public void testPostsOrdenadoPorMaisComments() {
    post1.setComments(Arrays.asList(new Comment(), new Comment()));
    post2.setComments(Arrays.asList(new Comment()));
    user.setPosts(Arrays.asList(post1, post2));
    when(userService.findById(1L)).thenReturn(user);

    List<Post> result = postService.postsOrdenadoPorMaisComments(1L);

    assertEquals(2, result.size());
    assertEquals(post1, result.get(0));
  }

  @Test
  public void testFindByIdPostExists() {
    when(postRepository.findById(1L)).thenReturn(Optional.of(post));

    Post postEncontrado = postService.findById(1L);
    assertNotNull(postEncontrado);
    assertEquals("Post Title", postEncontrado.getTitle());
  }

  @Test
  public void testFindByIdPostNotExist() {
    when(postRepository.findById(1L)).thenReturn(Optional.empty());

    assertThrows(ObjectNotFoundException.class, () -> {
      postService.findById(1L);
    });
  }

  @Test
  public void testSavePost() {
    User user = new User(1L, "Guga", "guga@gmail.com", "12345");
    Post post = new Post(1L, null, "Post Title", "Post Body", user, null, true);
    when(postRepository.save(post)).thenReturn(post);

    Post savedPost = postService.save(post);
    assertNotNull(savedPost);
  }

  @Test
  public void testDeletePost() {
    User user = new User(1L, "Testando", "teste@gmail.com", "asdf123");
    Post post = new Post(1L, null, "Post Title", "Post Body", user, null, true);
    when(postRepository.findById(1L)).thenReturn(Optional.of(post));
    doNothing().when(postRepository).deleteById(1L);

    postService.delete(1L);

    verify(postRepository, times(1)).deleteById(1L);
  }

  @Test
  public void testFindByActiveStatusTrue() throws ParseException {

    when(postRepository.findByActive(true)).thenReturn(Arrays.asList(postAtivo));

    List<Post> result = postService.findByActiveStatus(true);

    assertEquals(1, result.size());
    assertEquals(postAtivo, result.get(0));
  }

  @Test
  public void testFindByActiveStatusFalse() {
    when(postRepository.findByActive(false)).thenReturn(Arrays.asList(postInativo));

    List<Post> result = postService.findByActiveStatus(false);

    assertEquals(1, result.size());
    assertEquals(postInativo, result.get(0));
  }

}
