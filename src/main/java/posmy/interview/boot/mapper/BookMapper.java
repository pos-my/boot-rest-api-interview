package posmy.interview.boot.mapper;

import org.springframework.util.ObjectUtils;
import posmy.interview.boot.dto.request.CreateBookDto;
import posmy.interview.boot.entity.Book;

public class BookMapper {

    private static BookMapper bookMapperInstance;

    public static BookMapper getInstance() {
        if (ObjectUtils.isEmpty(bookMapperInstance)) {
            bookMapperInstance = new BookMapper();
        }

        return bookMapperInstance;
    }

    public Book mapBookDtoToEntity(CreateBookDto createBookDto) {
        return Book.builder()
            .title(createBookDto.getTitle())
            .author(createBookDto.getAuthor())
            .build();
    }
}
