package posmy.interview.boot.snowflake;

/**
 * Snow flake id assignment interface
 *
 * @param <T>
 */
public interface SnowflakeId<T> {

    void setId(T id);

    T getId();
}
