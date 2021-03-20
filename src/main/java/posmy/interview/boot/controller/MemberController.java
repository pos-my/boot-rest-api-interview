package posmy.interview.boot.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import posmy.interview.boot.dto.ListDto;
import posmy.interview.boot.dto.MemberCreateDto;
import posmy.interview.boot.dto.MemberDto;
import posmy.interview.boot.dto.MemberUpdateDto;
import posmy.interview.boot.helper.PageRequestBuilder;
import posmy.interview.boot.mapper.MemberMapper;
import posmy.interview.boot.services.MemberService;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping(value = "/member")
public class MemberController {

    @Autowired
    private MemberService memberService;
    @Autowired
    private MemberMapper memberMapper;

    @PostMapping(value = {
            ""
    }, consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<MemberDto> create(@RequestBody MemberCreateDto dto) {
        return ResponseEntity.ok().body(memberMapper.toMemberDto(memberService.create(memberMapper.toMember(dto))));
    }

    @GetMapping(value = {
            "/{id}"
    }, consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<MemberDto> getById(
            @PathVariable Long id
    ) {
        return ResponseEntity.ok().body(memberMapper.toMemberDto(memberService.getById(id)));
    }

    @PostMapping(value = "/list")
    public ResponseEntity<Page<MemberDto>> list(@RequestBody ListDto dto) {
        PageRequest pageRequest = PageRequestBuilder.build(dto.pageSize, dto.pageNumber, dto.sort);
        return ResponseEntity.ok().body(memberService.get(pageRequest)
                .map(order -> memberMapper.toMemberDto(order)));
    }

    @PutMapping(value = {
            "/{id}"
    }, consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<MemberDto> update(
            @PathVariable Long id, @RequestBody MemberUpdateDto dto
    ) {
        return ResponseEntity.ok().body(memberMapper.toMemberDto(memberService.update(id, memberMapper.toMember(dto))));
    }

    @DeleteMapping(value = {
            "/{id}", "/self"
    }, consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    public void delete(
            @PathVariable(required = false) Long id
    ) {
        if (id == null) {
            memberService.deleteSelf();
        } else {
            memberService.delete(id);
        }
    }
}
