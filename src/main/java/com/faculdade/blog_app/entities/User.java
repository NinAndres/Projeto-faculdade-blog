package com.faculdade.blog_app.entities;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "tb_users")
public class User {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @NotBlank
  private String name;
  @NotBlank
  private String email;

  @NotBlank
  private String password;

  public User(Long id, String name, String email, String password) {
    this.id = id;
    this.name = name;
    this.email = email;
    this.password = password;
  }

  @OneToMany(mappedBy = "user")
  @JsonIgnore
  private List<Post> posts = new ArrayList<>();

  @ManyToMany
  @JoinTable(name = "user_seguindo", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "seguindo_id"))
  @JsonIgnore
  private List<User> seguindo = new ArrayList<>();

  @ManyToMany(mappedBy = "seguindo")
  @JsonIgnore
  private List<User> seguidores = new ArrayList<>();

}
