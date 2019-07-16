
package com.hcq.elion.api.router;


public interface Router<T> {

    T getRouteValue();

    RouterType getRouteType();

    enum RouterType {
        LOCAL, REMOTE
    }

}
