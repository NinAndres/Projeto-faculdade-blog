package com.faculdade.blog_app.config;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.TimeZone;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import com.faculdade.blog_app.entities.Post;
import com.faculdade.blog_app.entities.User;
import com.faculdade.blog_app.entities.Comment;
import com.faculdade.blog_app.entities.Author;
import com.faculdade.blog_app.repositories.PostRepository;
import com.faculdade.blog_app.repositories.UserRepository;
import com.faculdade.blog_app.repositories.CommentRepository;
import com.faculdade.blog_app.repositories.AuthorRepository;

@Profile("dev")
@Component
public class testeConfig implements CommandLineRunner {

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private PostRepository postRepository;

  @Autowired
  private CommentRepository commentRepository;

  @Autowired
  private AuthorRepository authorRepository;

  @Override
  public void run(String... args) throws Exception {
    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
    sdf.setTimeZone(TimeZone.getTimeZone("GMT"));

    userRepository.deleteAll();
    postRepository.deleteAll();
    commentRepository.deleteAll();

    User maria = new User(null, "Maria Brown", "maria@gmail.com", "12345");
    User alex = new User(null, "Alex Green", "alex@gmail.com", "12345");
    User bob = new User(null, "Bob Grey", "bob@gmail.com", "12345");
    userRepository.saveAll(Arrays.asList(maria, alex, bob));

    Author authorMaria = new Author(null, "Maria Brown");
    Author authorAlex = new Author(null, "Alex Green");
    authorRepository.saveAll(Arrays.asList(authorMaria, authorAlex));

    Post post1 = new Post(null, sdf.parse("21/03/2024"), "Partiu viagem", "Vou viajar para São Paulo. Abraços!", maria,
        authorMaria, true);
    Post post2 = new Post(null, sdf.parse("23/03/2024"), "Bom dia", "Acordei feliz hoje!", alex, authorAlex, true);

    Comment c1 = new Comment(null, "Boa viagem mano!", sdf.parse("21/03/2024"), post1, authorAlex);
    Comment c2 = new Comment(null, "Aproveite", sdf.parse("22/03/2024"), post1, authorMaria);
    Comment c3 = new Comment(null, "Tenha um ótimo dia!", sdf.parse("23/03/2024"), post2, authorAlex);

    post1.getComments().addAll(Arrays.asList(c1, c2));
    post2.getComments().add(c3);

    postRepository.saveAll(Arrays.asList(post1, post2));
    commentRepository.saveAll(Arrays.asList(c1, c2, c3));

    maria.getPosts().addAll(Arrays.asList(post1));
    alex.getPosts().addAll(Arrays.asList(post2));
    userRepository.saveAll(Arrays.asList(maria, alex));

    maria.getSeguindo().add(alex);
    alex.getSeguidores().add(maria);

    maria.getSeguindo().add(bob);
    bob.getSeguidores().add(maria);

    alex.getSeguindo().add(bob);
    bob.getSeguidores().add(alex);

    userRepository.saveAll(Arrays.asList(maria, alex, bob));
  }
}
