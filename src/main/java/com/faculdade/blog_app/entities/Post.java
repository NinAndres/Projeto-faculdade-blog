package com.faculdade.blog_app.entities;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.annotation.Nullable;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "tb_posts")
public class Post {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @NotNull(message = "A data nao pode ser nula")
  @JsonFormat(pattern = "dd/MM/yyyy")
  private Date date;

  @NotBlank
  private String title;

  @NotBlank
  private String body;

  @NotNull(message = "Usuario nao pode ser nulo")
  @ManyToOne
  private User user;

  @NotNull(message = "Author nao pode ser nulo")
  @ManyToOne
  private Author author;

  @NotNull(message = "Status nao pode ser nulo")
  private boolean active;

  public Post(Long id, Date date, String title, String body, User user, Author author, boolean active) {
    this.id = id;
    this.date = date;
    this.title = title;
    this.body = body;
    this.user = user;
    this.author = author;
    this.active = active;
  }

  @Nullable
  @OneToMany(mappedBy = "post", cascade = CascadeType.REMOVE)
  private List<Comment> comments = new ArrayList<>();
}
