package matchmaker.backend.controllers;

import matchmaker.backend.models.Tag;
import matchmaker.backend.repositories.TagsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TagController {

  @Autowired private TagsRepository repository;

  @GetMapping("/tags")
  public ResponseEntity<Iterable<Tag>> getTags() {
    return ResponseEntity.ok(repository.findAll());
  }
}
