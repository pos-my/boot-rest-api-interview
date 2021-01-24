package posmy.interview.boot.persistence;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import posmy.interview.boot.errorHandling.MemberNotFoundException;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
public class MemberController {

    private final MemberRepository repository;

    private final MemberModelAssembler assembler;

    public MemberController(@Qualifier("members") MemberRepository repository, MemberModelAssembler assembler) {
        this.repository = repository;
        this.assembler = assembler;
    }

    // Read all members
    @GetMapping("/members")
    CollectionModel<EntityModel<Member>> all() {
        List<EntityModel<Member>> members = repository.findAll().stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());

        return CollectionModel.of(members,
                linkTo(methodOn(MemberController.class).all()).withSelfRel());
    }

    // Read member
    @GetMapping("/members/{id}")
    EntityModel<Member> one(@PathVariable Long id) {
        return assembler.toModel(repository.findById(id)
                .orElseThrow(() -> new MemberNotFoundException(id)));
    }

    // Create new members
    @PostMapping("/members")
    ResponseEntity<?> newMember(@RequestBody Member member) {
        EntityModel<Member> entityModel = assembler.toModel(repository.save(member));

        return ResponseEntity
                .created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(entityModel);
    }

    // Update/Replace member
    @PutMapping("/members/{id}")
    ResponseEntity<?> replaceMember(@RequestBody Member newMember, @PathVariable Long id) {

        Member updatedMember = repository.findById(id) //
                .map(employee -> {
                    employee.setName(newMember.getName());
                    employee.setRole(newMember.getRole());
                    return repository.save(employee);
                })
                .orElseGet(() -> {
                    newMember.setId(id);
                    return repository.save(newMember);
                });

        EntityModel<Member> entityModel = assembler.toModel(updatedMember);

        return ResponseEntity
                .created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(entityModel);
    }

    // Delete member
    @DeleteMapping("/members/{id}")
    ResponseEntity<?> deleteMember(@PathVariable Long id) {
        repository.deleteById(id);

        return ResponseEntity.noContent().build();
    }
}
