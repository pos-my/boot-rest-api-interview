package posmy.interview.boot.view;

import java.util.UUID;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * @author Hafiz
 * @version 0.01
 */
public class MemberView {

    @JsonIgnore
    private UUID id;

    @NotNull(message = "Fullname cannot be null")
	private String fullname;

    @NotNull(message = "Member Id cannot be null")
	private String memberId;

	public String getFullname() {
		return fullname;
	}

	public void setFullname(String fullname) {
		this.fullname = fullname;
	}

	public String getMemberId() {
		return memberId;
	}

	public void setMemberId(String memberId) {
		this.memberId = memberId;
	}

}
