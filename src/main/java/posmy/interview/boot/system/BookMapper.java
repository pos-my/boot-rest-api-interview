package posmy.interview.boot.system;

import org.modelmapper.ConfigurationException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import posmy.interview.boot.dto.BookDto;
import posmy.interview.boot.model.Book;

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
            dto = new BookDto();
        }
        return dto;
    }
}
