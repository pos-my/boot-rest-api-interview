package posmy.interview.boot.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import posmy.interview.boot.dto.BookCreateDto;
import posmy.interview.boot.dto.BookDto;
import posmy.interview.boot.dto.BookUpdateDto;
import posmy.interview.boot.dto.ListDto;
import posmy.interview.boot.helper.PageRequestBuilder;
import posmy.interview.boot.mapper.BookMapper;
import posmy.interview.boot.services.BookService;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping(value = "/book")
public class BookController {

    @Autowired
    private BookService bookService;
    @Autowired
    private BookMapper bookMapper;

    @PostMapping(value = {
            ""
    }, consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<BookDto> create(@RequestBody BookCreateDto dto) {
        return ResponseEntity.ok().body(bookMapper.toBookDto(bookService.create(bookMapper.toBook(dto))));
    }

    @GetMapping(value = {
            "/{id}"
    }, consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<BookDto> getById(
            @PathVariable Long id
    ) {
        return ResponseEntity.ok().body(bookMapper.toBookDto(bookService.getById(id)));
    }

    @PostMapping(value = "/list")
    public ResponseEntity<Page<BookDto>> list(@RequestBody ListDto dto) {
        PageRequest pageRequest = PageRequestBuilder.build(dto.pageSize, dto.pageNumber, dto.sort);
        return ResponseEntity.ok().body(bookService.get(pageRequest)
                .map(order -> bookMapper.toBookDto(order)));
    }

    @PutMapping(value = {
            "/{id}"
    }, consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<BookDto> update(
            @PathVariable Long id, @RequestBody BookUpdateDto dto
    ) {
        return ResponseEntity.ok().body(bookMapper.toBookDto(bookService.update(id, bookMapper.toBook(dto))));
    }

    @DeleteMapping(value = {
            "/{id}"
    }, consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    public void update(
            @PathVariable Long id
    ) {
        bookService.delete(id);
    }

    @PostMapping(value = "/{id}/borrow", consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<BookDto> borrowBook(@PathVariable Long id) throws Exception {
        return ResponseEntity.ok().body(bookMapper.toBookDto(bookService.borrowBook(id)));
    }

    @PostMapping(value = "/{id}/return", consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<BookDto> returnBook(@PathVariable Long id) throws Exception {
        return ResponseEntity.ok().body(bookMapper.toBookDto(bookService.returnBook(id)));
    }
}
