package posmy.interview.boot.request;

import lombok.Value;

@Value
public class GenericReq<T> {
    T body;
}
