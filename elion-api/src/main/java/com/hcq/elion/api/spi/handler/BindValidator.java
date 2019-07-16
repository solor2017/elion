
package com.hcq.elion.api.spi.handler;

import com.hcq.elion.api.spi.Plugin;
import com.hcq.elion.api.spi.Plugin;


public interface BindValidator extends Plugin {
    boolean validate(String userId, String data);
}
