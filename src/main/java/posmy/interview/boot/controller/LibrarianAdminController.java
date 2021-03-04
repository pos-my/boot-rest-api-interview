package posmy.interview.boot.controller;

import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import posmy.interview.boot.model.request.EmptyRequest;
import posmy.interview.boot.model.request.MemberAddRequest;
import posmy.interview.boot.model.request.MemberPatchRequest;
import posmy.interview.boot.model.response.EmptyResponse;
import posmy.interview.boot.model.response.MemberGetResponse;
import posmy.interview.boot.service.MemberAddService;
import posmy.interview.boot.service.MemberDeleteService;
import posmy.interview.boot.service.MemberGetService;
import posmy.interview.boot.service.MemberPatchService;

@RestController
@RequestMapping(value = "/v1/librarian/admin", produces = MediaType.APPLICATION_JSON_VALUE)
public class LibrarianAdminController {

    private final MemberAddService memberAddService;
    private final MemberPatchService memberPatchService;
    private final MemberGetService memberGetService;
    private final MemberDeleteService memberDeleteService;

    public LibrarianAdminController(MemberAddService memberAddService,
                                    MemberPatchService memberPatchService,
                                    MemberGetService memberGetService,
                                    MemberDeleteService memberDeleteService) {
        this.memberAddService = memberAddService;
        this.memberPatchService = memberPatchService;
        this.memberGetService = memberGetService;
        this.memberDeleteService = memberDeleteService;
    }

    @GetMapping("/member")
    public MemberGetResponse memberGet() {
        return memberGetService.execute(new EmptyRequest());
    }

    @PostMapping("/member")
    public EmptyResponse memberAdd(@RequestBody @Validated MemberAddRequest request) {
        return memberAddService.execute(request);
    }

    @PatchMapping("/member/{id}")
    public EmptyResponse memberAdd(@PathVariable("id") Long id,
                                   @RequestBody @Validated MemberPatchRequest request) {
        request.setId(id);
        return memberPatchService.execute(request);
    }

    @DeleteMapping("/member/{id}")
    public EmptyResponse memberDelete(@PathVariable("id") Long id) {
        return memberDeleteService.execute(id);
    }
}
