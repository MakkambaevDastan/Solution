import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.security.KeyStore;

import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;

// keytool -importcert -trustcacerts -file 195.38.164.139.crt -alias
// https://195.38.164.139 -keystore "C:\Program
// Files\Java\jdk-17\lib\security\cacerts"
public class JavaTLS {

    private static final String[] protocols = new String[] { "TLSv1.3" };
    private static final String[] cipher_suites = new String[] {
            "TLS_AES_256_GCM_SHA384",
            // "TLS_CHACHA20_POLY1305_SHA256",
            // "TLS_AES_128_CCM_SHA256",
            // "TLS_AES_128_CCM_8_SHA256",
    };

    public static void main(String[] args) throws Exception {

        SSLSocket socket = null;
        PrintWriter out = null;
        BufferedReader in = null;

        try {
            // KeyStore keyStore = KeyStore.getInstance("PKCS12");
            // keyStore.load(new FileInputStream("195.38.164.139.crt"),
            // "changeit".toCharArray());

            // Step : 1
            SSLSocketFactory factory = (SSLSocketFactory) SSLSocketFactory.getDefault();

            // Step : 2
            // socket = (SSLSocket) factory.createSocket("www.google.com", 443);
            socket = (SSLSocket) factory.createSocket("195.38.164.139", 443);

            // Step : 3
            socket.setEnabledProtocols(protocols);
            socket.setEnabledCipherSuites(cipher_suites);

            // Step : 4 {optional}
            socket.startHandshake();

            // Step : 5
            out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())));
            String http = """
                    GET / HTTP/1.1
                    Accept: text/html
                    Accept-Encoding: gzip, deflate, br, zstd
                    Accept-Language: ru
                    Connection: keep-alive
                    Host: 195.38.164.139
                    User-Agent: Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/131.0.0.0 Safari/537.36

                                        """;

            out.println(http);
            out.println();
            out.flush();

            if (out.checkError()) {
                System.out.println("SSLSocketClient:  java.io.PrintWriter error");
            }

            // Step : 6
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                System.out.println(inputLine);
            }
        } catch (Exception e) {
            System.out.println("error " + e);
        } finally{
            if (socket != null) {
                socket.close();
            }
            if (out != null) {
                out.close();
            }
            if (in != null) {
                in.close();
            }
        }
    }
}