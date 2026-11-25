package com.example.bookapi;

import com.example.bookapi.model.Book;

import com.example.bookapi.repository.BookRepository;
import tools.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import java.util.List;

@SpringBootTest
@AutoConfigureMockMvc
class BookapiApplicationTests {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private BookRepository repository;
    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        repository.deleteAll();
    }

    @Test
    void testCreateBook() throws Exception {
        Book book = new Book(
                "Java para Iniciantes",
                "Joao Silva");
        mockMvc.perform(post("/books")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(book)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.title")
                        .value("Java para Iniciantes"))
                .andExpect(jsonPath("$.author")
                        .value("Joao Silva"));
    }

    @Test
    void testFindAllBooks() throws Exception {
        Book book = new Book(
                "Spring Boot",
                "Maria Souza");
        repository.save(book);
        mockMvc.perform(get("/books"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title")
                        .value("Spring Boot"))
                .andExpect(jsonPath("$[0].author")
                        .value("Maria Souza"));
    }

    @Test
    void testUpdateBook() throws Exception {
        Book book = new Book(
                "Java Básico",
                "Pedro Silva");
        Book savedBook = repository.save(book);
        Book updatedBook = new Book(
                "Java Avançado",
                "Pedro Silva");
        mockMvc.perform(put("/books/" + savedBook.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedBook)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title")
                        .value("Java Avançado"))
                .andExpect(jsonPath("$.author")
                        .value("Pedro Silva"));
    }

    @Test
    void testDeleteBook() throws Exception {
        Book book = new Book(
                "Banco de Dados",
                "Carlos Oliveira");
        Book savedBook = repository.save(book);
        mockMvc.perform(delete("/books/" + savedBook.getId()))
                .andExpect(status().isNoContent());
        // Verifica se o livro foi removido
        org.junit.jupiter.api.Assertions.assertFalse(
                repository.existsById(savedBook.getId()));
    }

    @Test
    void testUpdateBookNotFound() throws Exception {
        Book updatedBook = new Book(
                "Livro Inexistente",
                "Autor Desconhecido");
        mockMvc.perform(put("/books/999999")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedBook)))
                .andExpect(status().isNotFound());
    }

    @Test
    void testDeleteBookNotFound() throws Exception {
        mockMvc.perform(delete("/books/999999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testCreateMultipleBooks() throws Exception {
        List<Book> books = List.of(
                new Book("Java", "Autor 1"),
                new Book("Spring Boot", "Autor 2"),
                new Book("Banco de Dados", "Autor 3"),
                new Book("JavaScript", "Autor 4"),
                new Book("Desenvolvimento Web", "Autor 5"));
        for (Book book : books) {
            mockMvc.perform(post("/books")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(book)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.id").exists())
                    .andExpect(jsonPath("$.title")
                            .value(book.getTitle()))
                    .andExpect(jsonPath("$.author")
                            .value(book.getAuthor()));
        }
        // Verifica a quantidade de livros cadastrados
        org.junit.jupiter.api.Assertions.assertEquals(
                5,
                repository.count());
    }
}