package ru.otus.hw.repositories;

import lombok.NonNull;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.otus.hw.models.Book;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {

    // Иначе LazyInitExc
    @NonNull
    @Override
    @EntityGraph(value = "book-author-entity-graph")
    List<Book> findAll();

    @NonNull
    @Override
    @EntityGraph(value = "book-author-entity-graph")
    Optional<Book> findById(@NonNull Long id);
}
