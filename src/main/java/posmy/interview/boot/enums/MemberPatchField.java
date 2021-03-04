package posmy.interview.boot.enums;

import org.springframework.security.crypto.password.PasswordEncoder;
import posmy.interview.boot.entity.MyUser;
import posmy.interview.boot.error.InvalidMemberPatchFieldException;
import posmy.interview.boot.model.request.MemberPatchRequest;
import posmy.interview.boot.repos.MyUserRepository;

import java.util.HashMap;
import java.util.Map;

public enum MemberPatchField {
    USER {
        @Override
        public void patch(MemberPatchRequest request, MyUserRepository repos, PasswordEncoder passwordEncoder) {
            MyUser existingUser = repos.findById(request.getId()).orElseThrow();
            existingUser.setUsername(request.getValue());
            repos.save(existingUser);
        }
    },
    PASS {
        @Override
        public void patch(MemberPatchRequest request, MyUserRepository repos, PasswordEncoder passwordEncoder) {
            MyUser existingUser = repos.findById(request.getId()).orElseThrow();
            existingUser.setPassword(passwordEncoder.encode(request.getValue()));
            repos.save(existingUser);
        }
    },
    ROLE {
        @Override
        public void patch(MemberPatchRequest request, MyUserRepository repos, PasswordEncoder passwordEncoder) {
            MyUser existingUser = repos.findById(request.getId()).orElseThrow();
            existingUser.setAuthority(
                    Enum.valueOf(MyRole.class, request.getValue().toUpperCase())
                            .authority);
            repos.save(existingUser);
        }
    };

    public static MemberPatchField lookup(String field) {
        MemberPatchField found = LOOKUP.get(field.toUpperCase());
        if (found == null)
            throw new InvalidMemberPatchFieldException(field);
        return found;
    }

    private static final Map<String, MemberPatchField> LOOKUP = new HashMap<>();
    static {
        for (MemberPatchField value : values()) {
            LOOKUP.put(value.name(), value);
        }
    }

    public abstract void patch(MemberPatchRequest request, MyUserRepository repos, PasswordEncoder passwordEncoder);
}
