
package com.hcq.elion.api.srd;

/**
 *
 *
 */
public interface ServiceListener {

    void onServiceAdded(String path, ServiceNode node);

    void onServiceUpdated(String path, ServiceNode node);

    void onServiceRemoved(String path, ServiceNode node);

}
