package com.yaoting.utf.infrastructure.utils;

import java.util.function.Predicate;

public class PredicateBuilder<T> {

    private Predicate<T> predicate;

    public PredicateBuilder() {
        predicate = T -> true;
    }

    public Predicate<T> builder() {
        return predicate;
    }

    public PredicateBuilder<T> add(boolean condition, Predicate<? super T> other) {
        if (condition) {
            predicate = predicate.and(other);
        }

        return this;
    }
}
