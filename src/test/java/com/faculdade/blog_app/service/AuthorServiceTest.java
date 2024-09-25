package com.faculdade.blog_app.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

import com.faculdade.blog_app.entities.Author;
import com.faculdade.blog_app.repositories.AuthorRepository;
import com.faculdade.blog_app.services.AuthorService;
import com.faculdade.blog_app.services.exception.ObjectNotFoundException;

@ActiveProfiles("test")
@SpringBootTest
public class AuthorServiceTest {

  @MockBean
  private AuthorRepository authorRepository;

  @Autowired
  private AuthorService authorService;

  @Test
  public void testFindByIdAuthor() {
    Author author = new Author(1L, "Mariazinha");
    when(authorRepository.findById(1L)).thenReturn(Optional.of(author));

    Author foundAuthor = authorService.findById(1L);
    assertNotNull(foundAuthor);
    assertEquals("Mariazinha", foundAuthor.getName());
  }

  @Test
  public void testFindByIdAuthorNotExist() {
    when(authorRepository.findById(1L)).thenReturn(Optional.empty());

    assertThrows(ObjectNotFoundException.class, () -> {
      authorService.findById(1L);
    });
  }
}
