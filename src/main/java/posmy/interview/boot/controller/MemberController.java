package posmy.interview.boot.controller;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/v1/member", produces = MediaType.APPLICATION_JSON_VALUE)
public class MemberController {

    @GetMapping
    public String get() {
        return "success";
    }
}
