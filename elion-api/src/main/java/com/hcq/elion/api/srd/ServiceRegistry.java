
package com.hcq.elion.api.srd;

import com.hcq.elion.api.service.Service;
import com.hcq.elion.api.service.Service;

/**
 *
 *
 */
public interface ServiceRegistry extends Service {

    void register(ServiceNode node);

    void deregister(ServiceNode node);
}
