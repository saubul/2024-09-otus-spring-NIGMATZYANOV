package ru.otus.repositories;


import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import ru.otus.models.Genre;

import java.util.List;

@Repository
public interface GenreRepository extends MongoRepository<Genre, String> {
    List<Genre> findAllByIdIn(List<String> ids);
}
