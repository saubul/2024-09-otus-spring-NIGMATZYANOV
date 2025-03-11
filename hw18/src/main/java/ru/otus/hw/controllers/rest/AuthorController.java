package ru.otus.hw.controllers.rest;

import io.github.resilience4j.core.functions.CheckedSupplier;
import io.github.resilience4j.ratelimiter.RateLimiter;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.client.circuitbreaker.CircuitBreaker;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.otus.hw.dto.AuthorDto;
import ru.otus.hw.services.AuthorService;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/api/v1/authors")
@RequiredArgsConstructor
public class AuthorController {

    private static final Logger LOGGER = LoggerFactory.getLogger(AuthorController.class);

    private final AuthorService authorService;

    private final RateLimiter rateLimiter;

    private final CircuitBreaker circuitBreaker;

    @GetMapping
    @ResponseStatus(value = HttpStatus.OK)
    public ResponseEntity<List<AuthorDto>> findAllAuthors() throws Throwable {
        CheckedSupplier<List<AuthorDto>> getAllAuthorsCheckedSupplier = RateLimiter.decorateCheckedSupplier(
                rateLimiter,
                () -> circuitBreaker.run(
                        authorService::findAll,
                        error -> {
                            LOGGER.info("DELAY CALL FAILED: " + error.getMessage());
                            return Collections.emptyList();
                        }
                )
        );
        try {
            return ResponseEntity.ok(getAllAuthorsCheckedSupplier.get());
        } catch (Throwable e) {
            LOGGER.error("ERROR OCCURED");
            throw e;
        }
    }
}
