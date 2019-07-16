
package com.hcq.elion.api.router;

import com.hcq.elion.api.spi.router.ClientClassifierFactory;


public interface ClientClassifier {
    ClientClassifier I = ClientClassifierFactory.create();

    int getClientType(String osName);
}
