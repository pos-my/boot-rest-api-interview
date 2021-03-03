package posmy.interview.boot.service;

public interface BaseService<I,O> {

    O execute(I request);
}
