
package com.hcq.elion.common.router;


/**
 *
 *
 */
public interface KickRemoteMsg {
    String getUserId();

    String getDeviceId();

    String getConnId();

    int getClientType();

    String getTargetServer();

    int getTargetPort();

    default boolean isTargetMachine(String host, int port) {
        return this.getTargetPort() == port
                && this.getTargetServer().equals(host);
    }
}
