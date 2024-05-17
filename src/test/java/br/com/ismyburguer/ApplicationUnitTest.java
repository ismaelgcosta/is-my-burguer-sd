package br.com.ismyburguer;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;

class ApplicationUnitTest {

    @Test
    void deveCriarManualmente() throws NoSuchAlgorithmException, KeyStoreException, KeyManagementException {
        Application.main(new String[]{});
    }

}