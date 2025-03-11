package ru.otus.hw.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import ru.otus.hw.dto.AuthorDto;
import ru.otus.hw.services.AuthorService;

import java.util.List;

// Из-за ручного конфига в AppConfig использование @WebMvcTest затруднено,
// потому что нельзя настроить нужным образом RateLimiter и CircuitBreaker через application-test.properties,
// а полагаться на не тестовый конфиг плохо (если он поменяется, то могут начать падать тесты)
@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
class AuthorControllerTest {

    @Autowired
    WebApplicationContext context;

    MockMvc mockMvc;

    @BeforeEach
    void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
    }

    @MockBean
    AuthorService authorService;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    void findAllAuthorsSuccessTest() throws Exception {
        List<AuthorDto> authorDtos = List.of(new AuthorDto(1L, "Author_1"));
        Mockito.when(authorService.findAll()).thenReturn(authorDtos);
        this.mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/authors"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(objectMapper.writeValueAsString(authorDtos)));
    }

    @Test
    void findAllAuthorsFailTest() throws Exception {
        Mockito.when(authorService.findAll()).thenThrow(new RuntimeException("Excpetion"));
        this.mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/authors"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(objectMapper.writeValueAsString(List.of())));
    }

}
