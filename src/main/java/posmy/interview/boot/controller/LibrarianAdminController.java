package posmy.interview.boot.controller;

import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import posmy.interview.boot.model.request.MemberAddRequest;
import posmy.interview.boot.model.request.MemberPatchRequest;
import posmy.interview.boot.model.response.EmptyResponse;
import posmy.interview.boot.service.MemberAddService;
import posmy.interview.boot.service.MemberPatchService;

@RestController
@RequestMapping(value = "/v1/librarian/admin", produces = MediaType.APPLICATION_JSON_VALUE)
public class LibrarianAdminController {

    private final MemberAddService memberAddService;
    private final MemberPatchService memberPatchService;

    public LibrarianAdminController(MemberAddService memberAddService,
                                    MemberPatchService memberPatchService) {
        this.memberAddService = memberAddService;
        this.memberPatchService = memberPatchService;
    }

    @GetMapping("/member")
    public String get() {
        return "success";
    }

    @PostMapping("/member")
    public EmptyResponse memberAdd(@RequestBody @Validated MemberAddRequest request) {
        return memberAddService.execute(request);
    }

    @PatchMapping("/member/{user}")
    public EmptyResponse memberAdd(@PathVariable("user") String user,
                                   @RequestBody @Validated MemberPatchRequest request) {
        request.setUser(user);
        return memberPatchService.execute(request);
    }
}
