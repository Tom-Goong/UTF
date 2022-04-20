package com.yaoting.utf.domain.func;

public abstract class DefaultFunc<T> implements Func<T> {
    @Override
    public Namespace namespace() {
        return Namespace.Default;
    }
}
