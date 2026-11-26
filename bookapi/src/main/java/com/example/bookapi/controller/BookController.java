package com.example.bookapi.controller;

import com.example.bookapi.model.Book;
import com.example.bookapi.repository.BookRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/books")
@Tag(
        name = "Biblioteca",
        description = "Operações de gerenciamento de livros"
)
public class BookController {

    private final BookRepository repository;

    public BookController(BookRepository repository) {
        this.repository = repository;
    }

    // Cadastrar livro
    @PostMapping
    @Operation(
            summary = "Cadastrar livro",
            description = "Realiza o cadastro de um novo livro."
    )
    @ApiResponse(
            responseCode = "201",
            description = "Livro cadastrado com sucesso",
            content = @Content(
                    schema = @Schema(implementation = Book.class)
            )
    )
    public Book create(@RequestBody Book book) {
        return repository.save(book);
    }

    // Consultar todos os livros
    @GetMapping
    @Operation(
            summary = "Listar livros",
            description = "Retorna todos os livros cadastrados."
    )
    @ApiResponse(
            responseCode = "200",
            description = "Lista de livros retornada com sucesso"
    )
    public List<Book> findAll() {
        return repository.findAll();
    }

    // Consultar livro por ID
    @GetMapping("/{id}")
    @Operation(
            summary = "Consultar livro por ID",
            description = "Busca um livro utilizando seu identificador."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Livro encontrado"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Livro não encontrado"
            )
    })
    public ResponseEntity<Book> findById(
            @Parameter(
                    description = "Identificador do livro",
                    example = "1"
            )
            @PathVariable Long id) {
        return repository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Atualizar livro
    @PutMapping("/{id}")
    @Operation(
            summary = "Atualizar livro",
            description = "Atualiza o título e o autor de um livro."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Livro atualizado com sucesso"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Livro não encontrado"
            )
    })
    public ResponseEntity<Book> update(
            @PathVariable Long id,
            @RequestBody Book updatedBook) {
        return repository.findById(id)
                .map(book -> {
                    book.setTitle(updatedBook.getTitle());
                    book.setAuthor(updatedBook.getAuthor());
                    return ResponseEntity.ok(repository.save(book));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    // Excluir livro
    @DeleteMapping("/{id}")
    @Operation(
            summary = "Excluir livro",
            description = "Remove permanentemente um livro do acervo a partir do seu identificador único (ID). Caso o ID não exista, retorna erro HTTP 404."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "204",
                    description = "Livro excluído com sucesso"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Livro não encontrado"
            )
    })
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (!repository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        repository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}