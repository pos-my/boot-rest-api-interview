package posmy.interview.boot.Services;

import posmy.interview.boot.Model.Users;

public interface MemberService {

    Users addMember(String username, String password);

    Users getMember(String username);

    void updateMember(String oriUsername, String username, String password, int enabled, String role);

    void deleteOwnAccount(String username);

    void deleteMemberAccount(String username);

}
