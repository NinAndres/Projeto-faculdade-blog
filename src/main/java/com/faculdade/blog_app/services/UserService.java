package com.faculdade.blog_app.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.faculdade.blog_app.entities.User;
import com.faculdade.blog_app.repositories.UserRepository;
import com.faculdade.blog_app.services.exception.ObjectNotFoundException;

@Service
public class UserService {

  @Autowired
  private UserRepository repository;

  public List<User> getAll() {
    List<User> usuarios = repository.findAll();
    for (User user : usuarios) {
      user.setPassword(null);
    }
    return usuarios;
  }

  public User findById(Long id) {
    return repository.findById(id)
        .map(user -> {
          user.setPassword(null);
          return user;
        })
        .orElseThrow(() -> new ObjectNotFoundException("Usuario nao encontrado"));
  }

  public User save(User obj) {
    return repository.save(obj);
  }

  public void delete(Long id) {
    User user = findById(id);
    if (user == null) {
      throw new ObjectNotFoundException("Usuario nao encontrado");
    }
    if (user.getSeguidores() != null) {
      for (User seguidor : user.getSeguidores()) {
        seguidor.getSeguindo().remove(user);
      }
    }

    if (user.getSeguindo() != null) {
      user.getSeguindo().clear();
    }

    if (user.getSeguidores() != null) {
      user.getSeguidores().clear();
    }

    repository.deleteById(id);
  }

  public User update(User user) {
    User existingUser = findById(user.getId());
    updateData(existingUser, user);
    return repository.save(existingUser);
  }

  public boolean existsById(Long id) {
    return repository.existsById(id);
  }

  private void updateData(User existingUser, User newUser) {
    existingUser.setName(newUser.getName());
    existingUser.setEmail(newUser.getEmail());
    existingUser.setPassword(newUser.getPassword());
  }

  public void followUser(Long id, Long followUserId) {
    User user = findById(id);
    User userToFollow = findById(followUserId);
    user.getSeguindo().add(userToFollow);
    userToFollow.getSeguidores().add(user);
    save(user);
  }

  public void unfollowUser(Long id, Long followId) {
    User user = findById(id);
    User userToUnfollow = findById(followId);
    user.getSeguindo().remove(userToUnfollow);
    userToUnfollow.getSeguidores().remove(user);
    save(user);
  }

  public Integer countUserPosts(Long id) {
    User user = findById(id);
    if (user == null) {
      throw new ObjectNotFoundException("Usuario nao encontrado");
    }
    return user.getPosts().size();
  }
}
