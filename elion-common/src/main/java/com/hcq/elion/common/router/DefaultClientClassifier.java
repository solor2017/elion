
package com.hcq.elion.common.router;

import com.hcq.elion.api.router.ClientClassifier;
import com.hcq.elion.api.spi.Spi;
import com.hcq.elion.api.spi.router.ClientClassifierFactory;

/**
 *
 *
 */
@Spi(order = 1)
public final class DefaultClientClassifier implements ClientClassifier, ClientClassifierFactory {

    @Override
    public int getClientType(String osName) {
        return ClientType.find(osName).type;
    }

    @Override
    public ClientClassifier get() {
        return this;
    }
}
