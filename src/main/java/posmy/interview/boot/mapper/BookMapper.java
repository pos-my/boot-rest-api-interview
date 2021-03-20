package posmy.interview.boot.mapper;

import org.mapstruct.Mapper;
import posmy.interview.boot.dto.BookCreateDto;
import posmy.interview.boot.dto.BookDto;
import posmy.interview.boot.dto.BookUpdateDto;
import posmy.interview.boot.entity.Book;

import static org.mapstruct.NullValueMappingStrategy.RETURN_NULL;
import static org.mapstruct.ReportingPolicy.IGNORE;

@Mapper(componentModel = "spring", unmappedTargetPolicy = IGNORE, nullValueMappingStrategy = RETURN_NULL)
public interface BookMapper {

    Book toBook(BookCreateDto dto);

    Book toBook(BookUpdateDto dto);

    BookDto toBookDto(Book book);
}
