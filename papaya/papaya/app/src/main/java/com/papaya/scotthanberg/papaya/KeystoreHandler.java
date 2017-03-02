package com.papaya.scotthanberg.papaya;

import java.security.KeyStore;
import java.security.KeyStoreException;
/**
 * Created by Owner on 3/1/2017.
 */

public class KeystoreHandler {
//store user id and authentication key
    //set and get those two things
    //create, access, get, set keystore
    public static void storePrivateKey() {
        KeyStore ks;
        try {
            ks = KeyStore.getInstance(KeyStore.getDefaultType());
        } catch (KeyStoreException exc) {
            exc.printStackTrace();
            return;
        }

        // get user password and file input stream
        char[] password = getPassword();

        java.io.FileInputStream fis = null;
        try {
            fis = new java.io.FileInputStream("keyStoreName");
            ks.load(fis, password);
        } finally {
            if (fis != null) {
                fis.close();
            }
        }
    }
}
