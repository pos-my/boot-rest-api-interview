package posmy.interview.boot.system;

import posmy.interview.boot.dto.BaseDto;
import posmy.interview.boot.model.BaseModel;

public interface Mapper<E extends BaseDto, T extends BaseModel> {

    E convertToDto(T model);

}
