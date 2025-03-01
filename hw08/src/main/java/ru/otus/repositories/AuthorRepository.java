package ru.otus.repositories;


import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import ru.otus.models.Author;

@Repository
public interface AuthorRepository extends MongoRepository<Author, String> {
}
