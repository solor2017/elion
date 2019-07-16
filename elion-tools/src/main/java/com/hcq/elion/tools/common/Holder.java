
package com.hcq.elion.tools.common;

/**
 *
 */
public final class Holder<T> {
    private T t;

    public Holder() {
    }

    public Holder(T t) {
        this.t = t;
    }

    public static <T> Holder<T> of(T t) {
        return new Holder<>(t);
    }

    public T get() {
        return t;
    }

    public void set(T t) {
        this.t = t;
    }
}
