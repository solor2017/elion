
package com.hcq.elion.common.router;

import com.hcq.elion.api.router.ClientLocation;
import com.hcq.elion.api.router.Router;

/**
 */
public final class RemoteRouter implements Router<ClientLocation> {
    private final ClientLocation clientLocation;

    public RemoteRouter(ClientLocation clientLocation) {
        this.clientLocation = clientLocation;
    }

    public boolean isOnline(){
        return clientLocation.isOnline();
    }

    public boolean isOffline(){
        return clientLocation.isOffline();
    }

    @Override
    public ClientLocation getRouteValue() {
        return clientLocation;
    }

    @Override
    public RouterType getRouteType() {
        return RouterType.REMOTE;
    }

    @Override
    public String toString() {
        return "RemoteRouter{" + clientLocation + '}';
    }
}
