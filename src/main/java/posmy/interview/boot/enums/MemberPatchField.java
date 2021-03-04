package posmy.interview.boot.enums;

import org.springframework.security.crypto.password.PasswordEncoder;
import posmy.interview.boot.entity.MyUser;
import posmy.interview.boot.error.InvalidMemberPatchFieldException;

import java.util.HashMap;
import java.util.Map;

public enum MemberPatchField {
    USER {
        @Override
        public MyUser patch(MyUser user, String value, PasswordEncoder passwordEncoder) {
            user.setUsername(value);
            return user;
        }
    },
    PASS {
        @Override
        public MyUser patch(MyUser user, String value, PasswordEncoder passwordEncoder) {
            user.setPassword(passwordEncoder.encode(value));
            return user;
        }
    },
    ROLE {
        @Override
        public MyUser patch(MyUser user, String value, PasswordEncoder passwordEncoder) {
            user.setAuthority(
                    Enum.valueOf(MyRole.class, value.toUpperCase())
                            .authority);
            return user;
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

    public abstract MyUser patch(MyUser user, String value, PasswordEncoder passwordEncoder);
}
