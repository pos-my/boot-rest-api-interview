package posmy.interview.boot.controller;

import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import posmy.interview.boot.model.request.MemberAddRequest;
import posmy.interview.boot.model.response.EmptyResponse;
import posmy.interview.boot.service.MemberAddService;

@RestController
@RequestMapping(value = "/v1/librarian/admin", produces = MediaType.APPLICATION_JSON_VALUE)
public class LibrarianAdminController {

    private final MemberAddService memberAddService;

    public LibrarianAdminController(MemberAddService memberAddService) {
        this.memberAddService = memberAddService;
    }

    @GetMapping("/member/get")
    public String get() {
        return "success";
    }

    @PostMapping("/member/add")
    public EmptyResponse memberAdd(@RequestBody @Validated MemberAddRequest request) {
        return memberAddService.execute(request);
    }

}
