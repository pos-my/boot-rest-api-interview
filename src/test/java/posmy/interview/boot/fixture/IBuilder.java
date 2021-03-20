package posmy.interview.boot.fixture;

import posmy.interview.boot.dto.ICreateDto;
import posmy.interview.boot.dto.IDto;
import posmy.interview.boot.dto.IUpdateDto;
import posmy.interview.boot.entity.IEntity;

import java.io.Serializable;

public interface IBuilder<E extends IEntity<ID>, ID extends Serializable> {

    E build();

    IDto buildDto();

    ICreateDto buildCreateDto();

    IUpdateDto buildUpdateDto();
}
