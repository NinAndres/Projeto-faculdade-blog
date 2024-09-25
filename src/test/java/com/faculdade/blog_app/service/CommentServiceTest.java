package com.faculdade.blog_app.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;

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
import com.faculdade.blog_app.repositories.CommentRepository;
import com.faculdade.blog_app.services.CommentService;

@ActiveProfiles("test")
@SpringBootTest
public class CommentServiceTest {

  @MockBean
  private CommentRepository commentRepository;

  @Autowired
  private CommentService commentService;

  private SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

  private Post post;
  private Author author;

  @BeforeEach
  public void setUp() throws ParseException {
    post = new Post(1L, sdf.parse("24/09/2024"), "Ativo", "Post ativo", new User(), new Author(), true);
    author = new Author(1L, "Xirumbinha");

  }

  @Test
  public void testSaveComment() throws ParseException {
    Comment comment = new Comment(1L, "Otimo post!", sdf.parse("25/09/2024"), post, author);
    when(commentRepository.save(comment)).thenReturn(comment);

    Comment savedComment = commentService.save(comment);
    assertNotNull(savedComment);
    assertEquals("Otimo post!", savedComment.getContent());
  }

  @Test
  public void testDeleteComment() throws ParseException {
    Comment comment = new Comment(1L, "Comentario delete", sdf.parse("25/09/2024"), post, author);
    doNothing().when(commentRepository).delete(comment);

    commentService.deleteById(comment.getId());

    verify(commentRepository, times(1)).deleteById(comment.getId());
  }
}
