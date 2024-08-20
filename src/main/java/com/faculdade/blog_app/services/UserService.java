package com.faculdade.blog_app.services;

import java.util.List;
import java.util.Optional;

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
    return repository.findAll();
  }

  public User findById(Long id) {
    Optional<User> obj = repository.findById(id);
    return obj.orElseThrow(() -> new ObjectNotFoundException("User not found"));
  }

  public User save(User obj) {
    return repository.save(obj);
  }

  public void delete(Long id) {
    findById(id);
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
}
