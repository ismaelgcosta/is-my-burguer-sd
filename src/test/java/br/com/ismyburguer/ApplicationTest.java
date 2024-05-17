package br.com.ismyburguer;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;

@DataMongoTest
@ActiveProfiles("test")
@ContextConfiguration(classes = {Application.class, TestSecurityConfig.class})
class ApplicationTest {
    @Test
    void applicationContextLoads() {
    }


}