

package com.hcq.elion.api.connection;


public interface Cipher {

    byte[] decrypt(byte[] data);

    byte[] encrypt(byte[] data);

}
