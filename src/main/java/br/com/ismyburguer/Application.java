package br.com.ismyburguer;


import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.ComponentScan;

import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;

@SpringBootApplication
public class Application {

    public static void main(String[] args) throws NoSuchAlgorithmException, KeyStoreException, KeyManagementException {
       // loadSsl();
        new SpringApplicationBuilder(Application.class).run(args);
    }
//
//    private static void loadSsl() throws NoSuchAlgorithmException, KeyManagementException, KeyStoreException {
//        SSLContext ctx;
//        TrustManager[] trustAllCerts = new X509TrustManager[]{new X509TrustManager(){
//            public X509Certificate[] getAcceptedIssuers(){return null;}
//            public void checkClientTrusted(X509Certificate[] certs, String authType){}
//            public void checkServerTrusted(X509Certificate[] certs, String authType){}
//        }};
//        ctx = SSLContext.getInstance("SSL");
//        ctx.init(null, trustAllCerts, null);
//
//        SSLContext.setDefault(ctx);
//
//        HttpsURLConnection.setDefaultHostnameVerifier(NoopHostnameVerifier.INSTANCE);
//        HttpsURLConnection.setDefaultSSLSocketFactory(SSLContextBuilder.create()
//                .loadTrustMaterial(TrustAllStrategy.INSTANCE)
//                .build().getSocketFactory());
//        System.setProperty("com.netflix.eureka.shouldSSLConnectionsUseSystemSocketFactory", "false");
//    }


}
