
package com.hcq.elion.api.spi.router;

import com.hcq.elion.api.router.ClientClassifier;
import com.hcq.elion.api.spi.Factory;
import com.hcq.elion.api.spi.SpiLoader;
import com.hcq.elion.api.router.ClientClassifier;
import com.hcq.elion.api.spi.Factory;
import com.hcq.elion.api.spi.SpiLoader;

/**
 *
 *
 */
public interface ClientClassifierFactory extends Factory<ClientClassifier> {

    static ClientClassifier create() {
        return SpiLoader.load(ClientClassifierFactory.class).get();
    }
}
