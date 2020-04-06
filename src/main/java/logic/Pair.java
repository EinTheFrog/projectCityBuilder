package logic;

import java.util.Objects;
import java.util.function.Supplier;

public class Pair <T> {
    private final Supplier<T> firstSup;
    private final   Supplier <T> secondSup;
    public T first;
    public T second;
    public Pair(Supplier<T> sup1, Supplier<T> sup2) {
        Objects.requireNonNull(firstSup = sup1);
        Objects.requireNonNull(secondSup = sup2);
        first = firstSup.get();
        second = secondSup.get();
    }
}
