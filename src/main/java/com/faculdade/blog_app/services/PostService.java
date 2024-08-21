package com.faculdade.blog_app.services;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.faculdade.blog_app.entities.Author;
import com.faculdade.blog_app.entities.Comment;
import com.faculdade.blog_app.entities.Post;
import com.faculdade.blog_app.entities.User;
import com.faculdade.blog_app.repositories.PostRepository;
import com.faculdade.blog_app.services.exception.ObjectNotFoundException;

@Service
public class PostService {

  @Autowired
  private PostRepository repository;

  @Autowired
  private CommentService commentService;

  public Post save(Post post) {
    return repository.save(post);
  }

  public Post update(Post post) {
    Post postExists = findById(post.getId());
    postExists.setTitle(post.getTitle());
    postExists.setBody(post.getBody());
    postExists.setDate(post.getDate());

    return repository.save(post);
  }

  public void delete(Long id) {
    Post post = findById(id);
    if (post == null) {
      throw new ObjectNotFoundException("Post não encontrado");
    }

    Author author = post.getAuthor();
    if (author != null) {
      author.getPosts().remove(post);
    }

    List<Comment> comments = post.getComments();
    for (Comment comment : comments) {
      comment.setPost(null);
      commentService.delete(comment);
    }

    post.getComments().clear();

    repository.deleteById(id);
  }

  public Post findById(Long id) {
    Post post = repository.findById(id).orElseThrow(() -> new ObjectNotFoundException("Post not found"));
    if (post.getUser() != null) {
      post.getUser().setPassword(null);
    }
    return post;
  }

  public List<Post> getAll() {
    List<Post> posts = repository.findAll();
    for (Post post : posts) {
      post.getUser().setPassword(null);
    }
    return posts;
  }

  public List<Post> findByTitle(String title) {
    return repository.findByTitle(title);
  }

  public List<Post> fullSearch(String text, Date minDate, Date maxDate) {
    return repository.findPostsByTextAndDate(text, minDate, maxDate);
  }

  public List<Post> findByUser(User user) {
    return repository.findByUser(user);
  }

}
