
package com.hcq.elion.monitor.jmx;

/**
 *
 */
public final class MException extends RuntimeException {
    public MException(String message) {
        super(message);
    }

    public MException(String message, Throwable cause) {
        super(message, cause);
    }

    public MException(Throwable cause) {
        super(cause);
    }
}
