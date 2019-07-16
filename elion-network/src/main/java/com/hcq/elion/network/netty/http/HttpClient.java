
package com.hcq.elion.network.netty.http;

import com.hcq.elion.api.service.Service;


public interface HttpClient extends Service {
    void request(RequestContext context) throws Exception;
}
