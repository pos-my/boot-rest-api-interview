package posmy.interview.boot.controller;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import posmy.interview.boot.model.response.EmptyResponse;
import posmy.interview.boot.service.MemberSelfDeleteService;

import java.security.Principal;

@RestController
@RequestMapping(value = "/v1/member/admin", produces = MediaType.APPLICATION_JSON_VALUE)
public class MemberAdminController {

    private final MemberSelfDeleteService memberSelfDeleteService;

    public MemberAdminController(MemberSelfDeleteService memberSelfDeleteService) {
        this.memberSelfDeleteService = memberSelfDeleteService;
    }

    @DeleteMapping("/self")
    public EmptyResponse memberSelfDelete(Principal principal) {
        return memberSelfDeleteService.execute(principal.getName());
    }
}
