package posmy.interview.boot.service.librarian;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import posmy.interview.boot.entity.LibrarianEntity;
import posmy.interview.boot.exception.LmsException;
import posmy.interview.boot.exception.NoDataFoundException;
import posmy.interview.boot.model.CreateLibrarianRequest;
import posmy.interview.boot.model.Librarian;
import posmy.interview.boot.repository.LibrarianRepository;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LibrarianServiceImpl implements LibrarianService {

    private final LibrarianRepository librarianRepository;
    private final ModelMapper modelMapper;

    @Override
    public Librarian getLibrarian(long libId) throws NoDataFoundException {
        Optional<LibrarianEntity> librarianOptional = librarianRepository.findById(libId);
        LibrarianEntity librarianEntity = librarianOptional.orElseThrow(() -> new NoDataFoundException("No librarian found"));
        return modelMapper.map(librarianEntity, Librarian.class);
    }

    @Override
    public LibrarianEntity saveLibrarian(CreateLibrarianRequest createLibrarianRequest) {
        LibrarianEntity librarianEntity = new LibrarianEntity();
        librarianEntity.setFirstName(createLibrarianRequest.getFirstName());
        librarianEntity.setLastName(createLibrarianRequest.getLastName());
        librarianEntity.setEmail(createLibrarianRequest.getEmail());
        return librarianRepository.save(librarianEntity);
    }
}
