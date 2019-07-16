
package com.hcq.elion.api.srd;

import com.hcq.elion.api.service.Service;
import com.hcq.elion.api.service.Service;

import java.util.List;

/**
 *
 */
public interface ServiceDiscovery extends Service {

    List<ServiceNode> lookup(String path);

    void subscribe(String path, ServiceListener listener);

    void unsubscribe(String path, ServiceListener listener);
}
