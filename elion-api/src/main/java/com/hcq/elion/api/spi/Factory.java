
package com.hcq.elion.api.spi;

import java.util.function.Supplier;

/**
 */
@FunctionalInterface
public interface Factory<T> extends Supplier<T> {
}
