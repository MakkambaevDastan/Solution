import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.Scanner;

import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;

public class ClientTLS {

    private static final int PORT = 443;
    private static final String[] protocols = new String[] { "TLSv1.3" };
    private static final String[] cipher_suites = new String[] { "TLS_AES_128_GCM_SHA256" };

    public static void main(String[] args) {

        System.setProperty("javax.net.ssl.keyStore", "./resources/mykeystore.p12");
        System.setProperty("javax.net.ssl.keyStorePassword", "secretpass");
        System.setProperty("javax.net.ssl.trustStore", "./resources/mykeystore.p12");
        System.setProperty("javax.net.ssl.trustStorePassword", "secretpass");

        try {
            SSLSocket socket = (SSLSocket) SSLSocketFactory.getDefault().createSocket("195.38.164.139", PORT);
            socket.setEnabledProtocols(protocols);
            socket.setEnabledCipherSuites(cipher_suites);

            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            Scanner scanner = new Scanner(System.in);
            while (true) {
                System.out.println("Send message to server: (Enter q to quit)");
                String inputLine = scanner.nextLine();
                if (inputLine.equals("q")) {
                    break;
                }
                out.println(inputLine);
                System.out.println(bufferedReader.readLine());
            }
        } catch (IOException ex) {
        }

    }

}