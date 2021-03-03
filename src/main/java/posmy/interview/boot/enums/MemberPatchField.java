package posmy.interview.boot.enums;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import posmy.interview.boot.error.InvalidMemberPatchFieldException;
import posmy.interview.boot.model.request.MemberPatchRequest;

import java.util.HashMap;
import java.util.Map;

public enum MemberPatchField {
    USER {
        @Override
        public void patch(MemberPatchRequest request, InMemoryUserDetailsManager manager, PasswordEncoder passwordEncoder) {
            UserDetails existingUser = manager.loadUserByUsername(request.getUser());
            manager.deleteUser(request.getUser());
            UserDetails patchedUser = User.withUserDetails(existingUser)
                    .username(request.getValue())
                    .build();
            manager.createUser(patchedUser);
        }
    },
    PASS {
        @Override
        public void patch(MemberPatchRequest request, InMemoryUserDetailsManager manager, PasswordEncoder passwordEncoder) {
            UserDetails existingUser = manager.loadUserByUsername(request.getUser());
            UserDetails patchedUser = User.withUserDetails(existingUser)
                    .passwordEncoder(passwordEncoder::encode)
                    .password(request.getValue())
                    .build();
            manager.updateUser(patchedUser);
        }
    },
    ROLE {
        @Override
        public void patch(MemberPatchRequest request, InMemoryUserDetailsManager manager, PasswordEncoder passwordEncoder) {
            UserDetails existingUser = manager.loadUserByUsername(request.getUser());
            UserDetails patchedUser = User.withUserDetails(existingUser)
                    .roles(Enum.valueOf(MyRole.class, request.getValue().toUpperCase()).name())
                    .build();
            manager.updateUser(patchedUser);
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

    public abstract void patch(MemberPatchRequest request, InMemoryUserDetailsManager manager, PasswordEncoder passwordEncoder);
}
