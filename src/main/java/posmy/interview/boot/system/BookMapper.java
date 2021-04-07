package posmy.interview.boot.system;

import lombok.AllArgsConstructor;
import lombok.Setter;
import org.modelmapper.ConfigurationException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import posmy.interview.boot.dto.BookDto;
import posmy.interview.boot.model.Book;

@Setter
@AllArgsConstructor
@Component
public class BookMapper implements Mapper<BookDto, Book> {

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public BookDto convertToDto(Book model) {
        BookDto dto;
        try {
            dto = modelMapper.map(model, BookDto.class);
        } catch (ConfigurationException ce) {
            dto = BookDto.builder().build();
        }
        return dto;
    }

    @Override
    public Book convertToModel(BookDto dto) {
        Book book;
        try {
            book = modelMapper.map(dto, Book.class);
        } catch (ConfigurationException ce) {
            book = Book.builder().build();
        }
        return book;
    }
}
