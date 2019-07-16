
package com.hcq.elion.api.event;

import com.hcq.elion.api.router.Router;
import com.hcq.elion.api.router.Router;


public final class RouterChangeEvent implements Event {
    public final String userId;
    public final Router<?> router;

    public RouterChangeEvent(String userId, Router<?> router) {
        this.userId = userId;
        this.router = router;
    }
}
