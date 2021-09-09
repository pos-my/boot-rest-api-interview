package posmy.interview.boot.model.librarian;

import java.util.List;

import posmy.interview.boot.model.BaseResponse;

public class ViewMemberRes extends BaseResponse {
  private List<UsersBean> memberList;

  public ViewMemberRes() {
  }

  public List<UsersBean> getMemberList() {
    return memberList;
  }

  public void setMemberList(List<UsersBean> memberList) {
    this.memberList = memberList;
  }
}
