package logic;

import java.util.Objects;
import java.util.function.Supplier;

public class Pair <T> {
    public T first;
    public T second;
    public Pair(T sup1, T sup2) {
        first = sup1;
        second = sup2;
    }
}
