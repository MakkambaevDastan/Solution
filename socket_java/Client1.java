import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;

import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;

public class Client1 {

    public static void main(String[] args) throws Exception {
        PrintWriter out = null;
        BufferedReader in = null;

        // System.setProperty("javax.net.ssl.trustStore", "./resources/ic-truststore");

        // System.setProperty("javax.net.ssl.trustStorePassword", "password");

        // Security.addProvider(new BouncyCastleProvider());

        SSLSocketFactory sslsocketfactory = (SSLSocketFactory) SSLSocketFactory.getDefault();
        SSLSocket sslSocket = (SSLSocket) sslsocketfactory.createSocket("195.38.164.139", 443);
        sslSocket.setEnabledProtocols(new String[] { "TLSv1.1", "TLSv1.2","TLSv1.3" });
        sslSocket.setEnabledCipherSuites(
                new String[] { "TLS_ECDHE_ECDSA_WITH_AES_128_CBC_SHA", "TLS_ECDHE_RSA_WITH_AES_128_CBC_SHA", });
        out = new PrintWriter(sslSocket.getOutputStream(), true);
        in = new BufferedReader(new InputStreamReader(sslSocket.getInputStream()));

        BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));
        String userInput = "Hello World with TLS";
        out.println(userInput);
        System.out.println("echo: " + in.readLine());

        out.close();
        in.close();
        stdIn.close();
        sslSocket.close();
    }
}
