package posmy.interview.boot.errorHandling;

public class MemberNotFoundException extends RuntimeException {

    public MemberNotFoundException(Long id) {
        super("Could not find member with id: " + id);
    }
}
