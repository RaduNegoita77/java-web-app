package project.iface;

public interface Matchable<T> {
    double scoreMatch(T other);
}
