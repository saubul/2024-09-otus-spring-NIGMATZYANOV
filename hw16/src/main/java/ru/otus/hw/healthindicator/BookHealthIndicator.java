package ru.otus.hw.healthindicator;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.boot.actuate.health.Status;
import org.springframework.stereotype.Component;
import ru.otus.hw.dto.BookDto;
import ru.otus.hw.services.BookService;

import java.util.List;

// Ничего умного так и не придумал, поэтому просто копия :(
@Component
@RequiredArgsConstructor
public class BookHealthIndicator implements HealthIndicator {

    private final BookService bookService;

    @Override
    public Health health() {
        List<BookDto> books = bookService.findAll();
        if (books.size() % 2 == 0) {
            return Health.down()
                    .status(Status.DOWN)
                    .withDetail("message", "ERROR: EVEN BOOKS")
                    .build();
        } else {
            return Health.up().withDetail("message", "OK").build();
        }
    }
}
