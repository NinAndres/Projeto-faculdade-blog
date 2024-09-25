package com.faculdade.blog_app.repositories;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.faculdade.blog_app.entities.Post;
import com.faculdade.blog_app.entities.User;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
  List<Post> findByTitle(String title);

  @Query("SELECT p FROM Post p JOIN p.comments c " +
      "WHERE p.date >= :minDate AND p.date <= :maxDate " +
      "AND (LOWER(p.title) LIKE LOWER(CONCAT('%', :text, '%')) " +
      "OR LOWER(p.body) LIKE LOWER(CONCAT('%', :text, '%')) " +
      "OR LOWER(c.content) LIKE LOWER(CONCAT('%', :text, '%')))")
  List<Post> findPostsByTextAndDate(@Param("text") String text, @Param("minDate") Date minDate,
      @Param("maxDate") Date maxDate);

  List<Post> findByUser(User user);

  List<Post> findByActive(boolean active);
}
