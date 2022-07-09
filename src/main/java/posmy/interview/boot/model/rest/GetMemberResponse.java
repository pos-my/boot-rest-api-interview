package posmy.interview.boot.model.rest;


import java.util.List;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class GetMemberResponse {

	List<MemberDetail> member;
}
