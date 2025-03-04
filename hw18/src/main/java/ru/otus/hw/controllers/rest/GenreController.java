package ru.otus.hw.controllers.rest;

import io.github.resilience4j.core.functions.CheckedSupplier;
import io.github.resilience4j.ratelimiter.RateLimiter;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.client.circuitbreaker.CircuitBreaker;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.ResponseStatus;
import ru.otus.hw.dto.GenreDto;
import ru.otus.hw.services.GenreService;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/api/v1/genres")
@RequiredArgsConstructor
public class GenreController {

    private static final Logger LOGGER = LoggerFactory.getLogger(GenreController.class);

    private final GenreService genreService;

    private final RateLimiter rateLimiter;

    private final CircuitBreaker circuitBreaker;

    @GetMapping
    @ResponseStatus(value = HttpStatus.OK)
    public List<GenreDto> findAllGenres() throws Throwable {
        CheckedSupplier<List<GenreDto>> findAllGenresCheckedSupplier = RateLimiter.decorateCheckedSupplier(
                rateLimiter,
                () -> circuitBreaker.run(
                        genreService::findAll,
                        error -> {
                            LOGGER.error("DELAY ERROR: " + error.getMessage());
                            return Collections.emptyList();
                        }
                )
        );
        try {
            return findAllGenresCheckedSupplier.get();
        } catch (Throwable e) {
            LOGGER.error("ERROR OCCURED: " + e.getMessage());
            throw e;
        }
    }

}
