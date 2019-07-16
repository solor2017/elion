
package com.hcq.elion.test.util;

import com.hcq.elion.tools.Utils;
import org.junit.Test;

/**
 */
public class IPTest {
    @Test
    public void getLocalIP() throws Exception {
        System.out.println(Utils.lookupLocalIp());
        System.out.println(Utils.lookupExtranetIp());

    }
}

