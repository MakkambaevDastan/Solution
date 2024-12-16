
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.KeyStore;
import java.security.cert.Certificate;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.TrustManagerFactory;

public class TlsServer {

    // public static final String SERVER_CRT = "-----BEGIN CERTIFICATE-----\n"
    // + "-----END CERTIFICATE-----";
    // private static final String SERVER_KEY = "-----BEGIN PRIVATE KEY-----\n"
    // + "-----END PRIVATE KEY-----";

    public static final String SERVER_CRT = """
                        -----BEGIN CERTIFICATE-----
            MIIEnDCCAoSgAwIBAgIBATANBgkqhkiG9w0BAQsFADBzMQswCQYDVQQGEwJVUzEO
            MAwGA1UECAwFU3RhdGUxDTALBgNVBAcMBENpdHkxFTATBgNVBAoMDE9yZ2FuaXph
            dGlvbjETMBEGA1UECwwKRGVwYXJ0bWVudDEZMBcGA1UEAwwQbWlsYnVyc2FtdVJB
            LmNvbTAeFw0yNDExMjYxOTUwMjZaFw0yNTExMjYxOTUwMjZaMGwxCzAJBgNVBAYT
            AlVTMQ4wDAYDVQQIDAVTdGF0ZTENMAsGA1UEBwwEQ2l0eTEUMBIGA1UECgwLRnJv
            bnRlbmRPcmcxETAPBgNVBAsMCEZyb250ZW5kMRUwEwYDVQQDDAxmcm9udGVuZC5v
            cmcwggEiMA0GCSqGSIb3DQEBAQUAA4IBDwAwggEKAoIBAQCgRYqfTLO2JDhirXkv
            DXvSAtt7yLSoHfc+jLW740YrL99fnlJybqzS9ajBNj6CrUBZKTxRIat21g+f0qiR
            bDNc32vO1SyHZVFkCrP8NzdP01i0eDU2Gls9rY7nwF83SOJPVHIl04zU8fnyfAfN
            hv3pXw4f9k06QWTeI7wN7WD0vXUdods246r3YfEwExXOa9S+N5/IQsNCqBVePkv9
            cjUBlQrsVAkWiPC7TMEyJS1VBmMWzminofzgzqGe7iAxfkMfZ4sQNW6n9Ua8hHlv
            7iiQYqtjJcR8WkMMFep8yJiOJ5ASy2X28O8gqgA+Ea7diSPgwzaW7vB+AyDnFV5m
            GNNvAgMBAAGjQjBAMB0GA1UdDgQWBBQmzpsRGGJsCNF0t8FFHhTaSpvrTzAfBgNV
            HSMEGDAWgBRlAixTSRNaDf1XM7kOa8vR0TaLrDANBgkqhkiG9w0BAQsFAAOCAgEA
            VGXKVKnVRAcIYVuR59lhLayOISVR4gMEWiyTmxLCcC96NFgPrSnkgw57cyF5lpz2
            iyJovjdJbsjR2wdM0YE4jYT+ZyDFyuRusWl0PODDo4c6kMcJ/Gxcaw7wfhlVSD2N
            6aVnVlBjKNfAwnvVSU4PcZwzsHhkaFKWLNN2Ymycb8sIzp5WlrrhP6Tb4paCEUVd
            HKrtw1kD2ODofsPNWEYt5wkyqKVbANl4aRLSly5nsJtaU3cVkfdlPA51MpnfmaRj
            LmWzrgC+v+O7RJFlfgbcmfVqh0bsuRcGl3u8LYi3XsL4PutgowQ6tZPNPGq9mTKC
            alXmNJA/xH+v3bsEG2b7QzdzBC3Lc+RUShPwq3Y2WCjb3tIFXcFPWnlJruQYPw0t
            eCPTcZk2UAyCLhDOxab4hGsWHeBFp2+JUDcFfkjAM0VFlu2E3LeiAnIf1SRENXef
            vA6xzno0uyZ+CtgMgRNZUIKgAeN1nXozxW9V0BhUaGo8YKS0HKbUEUsOVBea5uY3
            A5ebYIuVwXggSBHneMeOs/bPj1/6ZsEE0zu1l56zVLelWLDxR3wT8BxqIv16HXUD
            TgTKDtKEGRPHVUw9o5wL+u5PVtfaCZ1eHQtRXDzpbDc+7COjuBoCDYlmm6jbznIc
            RRLDZsYm07CQlcNEOEbwbZJ6b7Zyf1nSOM4sFT1klXA=
            -----END CERTIFICATE-----

                        """;

    private static final String SERVER_KEY = """
                        -----BEGIN PRIVATE KEY-----
            MIIEvgIBADANBgkqhkiG9w0BAQEFAASCBKgwggSkAgEAAoIBAQCgRYqfTLO2JDhi
            rXkvDXvSAtt7yLSoHfc+jLW740YrL99fnlJybqzS9ajBNj6CrUBZKTxRIat21g+f
            0qiRbDNc32vO1SyHZVFkCrP8NzdP01i0eDU2Gls9rY7nwF83SOJPVHIl04zU8fny
            fAfNhv3pXw4f9k06QWTeI7wN7WD0vXUdods246r3YfEwExXOa9S+N5/IQsNCqBVe
            Pkv9cjUBlQrsVAkWiPC7TMEyJS1VBmMWzminofzgzqGe7iAxfkMfZ4sQNW6n9Ua8
            hHlv7iiQYqtjJcR8WkMMFep8yJiOJ5ASy2X28O8gqgA+Ea7diSPgwzaW7vB+AyDn
            FV5mGNNvAgMBAAECggEAT0EUQ6ISXEec+m7+Nh5c1IVFM/kUHO50jSHX3ImO8VxD
            AW2xJ5VDMS75vFGup8A0yeJySUx1FdbU8xug8AipfYueLDGOS4X2sZn5VweW+opw
            mw22goKWC/H/cOrYKuaHwuLnb30lb0gZaaeb1C6k8PpoabXKirw7vP2+yMRR5bPi
            EQhACD92+Cnslu4/gPj5rMrO8E5epNL1uQI5z9sEdE/GJbFKE9uradIZyFveY/UF
            l+0xFGzC+A8+KiVffztqHxxhGT0Wg+4b73Qrt9VXnCYtSHSS8m4qc5GC2rbw44cK
            vK4Se+LiXIih7n2wxUb+m0a51JGyBC/6kZTk7BzGQQKBgQDONSW2jdZhcmB2Vph2
            K2kLtZ9EwiLafha30GxD7eDLXTzDuWTkEXFzawap0+Nd20Wj95yoOuoaZzS4cABu
            tyt6+lCYcLsWAQZH/nPtNKGUFnZ2YOq+6tHVqCjPedmM2cJVvLSIlxZYFkN/UNZq
            gvG8I49DaOoCC/8P71b5QzyLvQKBgQDG+NV90tPJXVAOVpTMYmwsI8vkgpNPbu1L
            AbgZfGnh18BnIdAeN6o7fCZ3NufU6WPm5Sj1r0dnVaBOub0FTN0ad5HsEc+IvCFc
            qxsDUjr0A6P29nY1OY7FeC4lFtNmv4SmLfReBcJ2sJPjpJ/Qnyi4rEm2K9fCOeI0
            N8OGjtiYmwKBgQCjCeCEo0ayi4V7a2JmZFJNANApxDlceb8qnK9NUkKfaFvIAMil
            jqkd8aw2EKM/rUQ1rWx+kv5P2/QSM/QTld3aARpCPTee4TZ/K8+q6Ccs+Em3tU9F
            nlJEVQ3XZWc991qxAV/Yc2UmRowdmOO/GUNaVt9KFCSZc7S6e6lPsfqoHQKBgAH6
            v8T8r/a04vMBkYFnCUR3JrQjSGvl4p54aeh3AGaqytM71uI2E4978LYG6c+IcuH9
            LackE0XH6ExgNK000o5M2jrLApGNrZdKbH5f9w4uaxct5IcrB2yXgX4E+B1xklRE
            6MV2sTSVFSOSo7RjoPgVXrZdQUxBXyq900gna6g5AoGBAJOdnpIy+Hfu8uDmiIb3
            cNwSQBMwUyQLsLjnyBTzkVPNTdjs1dOJsjvyYSFzmUc7bccNQeO29XCoTb5edw9O
            wfpkxH8Lg17pgyj36YTwvYmJa0CFZKxSGSlohBFo8Re94RRam/fPrZpSETi2PIvs
            mTofkJBzRabBGA/vU9mJPlIp
            -----END PRIVATE KEY-----

                        """;

    public static void main(String[] args) throws Exception {
        new TlsServer().start();
    }

    private void start() throws Exception {
        // System.setProperty("javax.net.debug", "all");
        String ksPass = "changeit";

        // Init Key Manager Factory.
        KeyStore kks = TlsCommon.createKeystore(ksPass);
        kks.setKeyEntry("server", TlsCommon.convertPrivateKey(SERVER_KEY), ksPass.toCharArray(), new Certificate[] {
                TlsCommon.convertCertificate(SERVER_CRT)
        });
        KeyManagerFactory kmf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
        kmf.init(kks, ksPass.toCharArray());

        // Init Trust Manager Factory.
        KeyStore tks = TlsCommon.createKeystore(ksPass);
        tks.setCertificateEntry("client", TlsCommon.convertCertificate(TlsClient.CLIENT_CRT));
        TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
        tmf.init(tks);

        // Init SSL Context.
        SSLContext ctx = SSLContext.getInstance(TlsCommon.TLS_PROTOCOL);
        ctx.init(kmf.getKeyManagers(), tmf.getTrustManagers(), null);

        // Init Server Socket.
        SSLServerSocket serverSocket = (SSLServerSocket) ctx.getServerSocketFactory()
                .createServerSocket(TlsCommon.PORT);
        serverSocket.setNeedClientAuth(true);
        serverSocket.setEnabledCipherSuites(TlsCommon.CIPHER_SUITES);
        serverSocket.setEnabledProtocols(new String[] {
                TlsCommon.TLS_PROTOCOL
        });

        System.out.printf("Server started on port %d%n", TlsCommon.PORT);
        while (true) {
            try (SSLSocket socket = (SSLSocket) serverSocket.accept()) {
                System.out.println("Accept connection: " + socket.getRemoteSocketAddress());
                InputStream is = new BufferedInputStream(socket.getInputStream());
                OutputStream os = new BufferedOutputStream(socket.getOutputStream());
                byte[] data = new byte[2048];
                int len = is.read(data);
                if (len <= 0) {
                    throw new IOException("No data received.");
                }
                System.out.printf("Server received %d bytes: %s%n", len, new String(data, 0, len));
                os.write(data, 0, len);
                os.flush();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }
}