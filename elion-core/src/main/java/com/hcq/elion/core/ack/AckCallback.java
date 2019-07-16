
package com.hcq.elion.core.ack;


public interface AckCallback {
    void onSuccess(AckTask context);

    void onTimeout(AckTask context);
}
