
package com.hcq.elion.bootstrap;

/**
 */
public class BootException extends RuntimeException {
    public BootException(String s) {
        super(s);
    }

    public BootException(String message, Throwable cause) {
        super(message, cause);
    }
}
