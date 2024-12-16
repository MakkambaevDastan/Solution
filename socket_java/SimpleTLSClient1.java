
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.security.SecureRandom;

import javax.net.SocketFactory;
import javax.net.ssl.SSLParameters;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;

public class SimpleTLSClient1 {
    public static void main(String[] args) {
        try {
            String host = "195.38.164.139";
            int port = 443;
            SocketFactory factory = SSLSocketFactory.getDefault();
            Socket connection = factory.createSocket(host, port);
            ((SSLSocket) connection).setEnabledCipherSuites(new String[] { "TLS_AES_256_GCM_SHA384" });
            ((SSLSocket) connection).setEnabledProtocols(new String[] { "TLSv1.3" });

            SSLParameters sslParams = new SSLParameters();
            sslParams.setEndpointIdentificationAlgorithm("HTTPS");
            ((SSLSocket) connection).setSSLParameters(sslParams);

            BufferedReader input = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            System.out.println(input.readLine());

            // Socket socket = new Socket("195.38.164.139", 443);
            // OutputStream out = socket.getOutputStream();
            // SecureRandom random = new SecureRandom();
            // byte[] clientRandomBytes = new byte[12];
            // random.nextBytes(clientRandomBytes);
            // out.write(clientRandomBytes);
            // out.flush();
            // InputStream in = socket.getInputStream();
            // while (true) {
            // System.out.print((char) (in.read()));
            // if (in.available() == 0) {
            // break;
            // }
            // }
            // socket.close();
        } catch (Exception e) {
            System.out.println("Error "+e);
        }
    }
}
