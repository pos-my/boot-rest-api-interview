package posmy.interview.boot.persistence;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.stereotype.Component;

@Component
public class MemberModelAssembler implements RepresentationModelAssembler<Member, EntityModel<Member>> {

    @Override
    public EntityModel<Member> toModel(Member member) {

        return EntityModel.of(member,
                WebMvcLinkBuilder.linkTo(methodOn(MemberController.class).one(member.getId())).withSelfRel(),
                linkTo(methodOn(MemberController.class).all()).withRel("members"));
    }
}
