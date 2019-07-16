
package com.hcq.elion.api.spi;

import com.hcq.elion.api.ElionContext;

/**
 *
 *
 *
 */
public interface Plugin {

    default void init(ElionContext context) {

    }

    default void destroy() {

    }
}
