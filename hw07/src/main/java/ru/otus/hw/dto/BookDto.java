package ru.otus.hw.dto;

import ru.otus.hw.models.Author;
import ru.otus.hw.models.Genre;

import java.util.List;

public record BookDto(Long id, String title, Author author, List<Genre> genreList){}
