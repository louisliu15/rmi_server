package compute;

public interface Compute {
    <T> T executeTask(Task<T> t);
}
