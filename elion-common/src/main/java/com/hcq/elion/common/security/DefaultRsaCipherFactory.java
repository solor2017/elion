
package com.hcq.elion.common.security;

import com.hcq.elion.api.connection.Cipher;
import com.hcq.elion.api.spi.Spi;
import com.hcq.elion.api.spi.core.RsaCipherFactory;

/**
 */
@Spi
public class DefaultRsaCipherFactory implements RsaCipherFactory {
    private static final RsaCipher RSA_CIPHER = RsaCipher.create();

    @Override
    public Cipher get() {
        return RSA_CIPHER;
    }
}
