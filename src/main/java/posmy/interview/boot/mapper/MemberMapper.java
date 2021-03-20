package posmy.interview.boot.mapper;

import org.mapstruct.Mapper;
import posmy.interview.boot.dto.MemberCreateDto;
import posmy.interview.boot.dto.MemberDto;
import posmy.interview.boot.dto.MemberUpdateDto;
import posmy.interview.boot.entity.Member;

import static org.mapstruct.NullValueMappingStrategy.RETURN_NULL;
import static org.mapstruct.ReportingPolicy.IGNORE;

@Mapper(componentModel = "spring", unmappedTargetPolicy = IGNORE, nullValueMappingStrategy = RETURN_NULL)
public interface MemberMapper {

    Member toMember(MemberCreateDto dto);

    Member toMember(MemberUpdateDto dto);

    MemberDto toMemberDto(Member book);
}
