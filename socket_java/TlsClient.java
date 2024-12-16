import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.KeyStore;
import java.security.cert.Certificate;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.TrustManagerFactory;

public class TlsClient {

    // public static final String CLIENT_CRT = "-----BEGIN CERTIFICATE-----\n"
    // + "-----END CERTIFICATE-----";
    // private static final String CLIENT_KEY = "-----BEGIN PRIVATE KEY-----\n"
    // + "-----END PRIVATE KEY-----";

    public static final String CLIENT_CRT = """
                        -----BEGIN CERTIFICATE-----
            MIIEuDCCAqACAQAwczELMAkGA1UEBhMCVVMxDjAMBgNVBAgMBVN0YXRlMQ0wCwYD
            VQQHDARDaXR5MRUwEwYDVQQKDAxPcmdhbml6YXRpb24xEzARBgNVBAsMCkRlcGFy
            dG1lbnQxGTAXBgNVBAMMEG1pbGJ1cnNhbXVSQS5jb20wggIiMA0GCSqGSIb3DQEB
            AQUAA4ICDwAwggIKAoICAQDHUiwtsr65Pt2c07J4rByAG3s7v4ZQJI8nohLt6ydF
            DnD1XvAl/FDKFv7Ro12+drWWTzTAcvQC4HIcl6iA8qAvS8BEJvoDc34couW7HPNF
            KPb4f+Zwt7PBzKNIvMyOkXFhuqV4UTg2KoXowb+njauCaJwfdOxVUqOAcR7vBFxZ
            e0YoIg7t8C7WvpjGMtA+1UQFCbjIYl9trs7oamHIbGAMYO1U9pdxmZd3myGewIDL
            ejOfu+cIXXMHyvmseLYml+J3hjcpN9Q1SsZE5RsRJoCsrULzh1Op02qtjlsXPETe
            ADNbbLSySUeIcHijsnOVVX6KoZ7hZpLTBfU/Tk0j2ROS0CxRKKu1XE7NIKYqL1rs
            yZ7bXIvZi5G0I8+4h+EuaVl4OIOH+YTfRUV6kiqv/RnqLNraHaaw0tvhHjk5tLEu
            tdF3Wrx8oW5gKcpN07aVyc7PI8X2OcalbQFRMkwsB28M7uHycB+agKfFlZrR/1J0
            X9WwMaBNT5mZCzcV3Jy2hLmnBabTCs0nIIMBIsiJfMc2kzsrqQUudWMq3/SN55lw
            RFUjF44kzDGTUyaocOV2KCkZzL1L7yG5c31+AUexBAG38NLnPJ4HOc4dDN5PN7c8
            rh782+9qYU2tc03YIqnmBYOnio0PJ0r3sSkjOsUtnZ2l1GpDNz50Omyam+6RCjEA
            TQIDAQABoAAwDQYJKoZIhvcNAQELBQADggIBAJPCRVuHHty0jNFnT2/pIvAPI5Da
            jLjWK6IwoUYADI/418GXys4hj1vU6np5rQyQB3iJ0ScpG76viaN+Ozl1nJm1jRCD
            2j4OqfFZzwnaKslj4cjYlFQjPZSxi/2aCrgXQQzv7ej1jqeSmmK88a5OqrwOBFA+
            esEyfuvt+FtjqQye4rZ/0jrrLyfj+g0Br5xZEFsN7kAHEbCoWWaakxp6ErOlr68k
            /LQaoQYTQYvmcJMOTL8tGiJ/gd12bjPfsgKCQCk9oRCWJTf7v4E/StZ5TuxqIfyK
            jP8CZNSkXeOS8KLUi75Ffz+uAIdcJm7Y0m5KyFD7zGzvG0hiU87aOd0g6dhOAI8I
            J/AGwEQEEEHRj5ukDJdNLsvHxwy8Kmi1z362OQ3pKMIm0DjSVKxHvQptQPdZzhmK
            s+gdLTmgm6qWH0rO87A2unxreVa90n2G/qWUecYtsjc3MtCCZERNadsTe2qdaPna
            6uyFwnR+MQqcAxIzkapVuZclfdUnQI7yhJoJ9FGooU5SFgkIYbe8fO4x1pVv24Yq
            vWT2P4oGNhEcg5UgDQGF792wP891nXTUPT7F3G4/bMt3BgmTCsVCT2Ol+dJPijvc
            rRgYYwk0aRAe3HGYjK/JFWP4XbvBqRwM8v0Lj/9f7IVRNdap8pJCE30jUeOu791T
            5f7XsZj2G32Rpo09
            -----END CERTIFICATE-----

                        """;
    private static final String CLIENT_KEY = """
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
        new TlsClient().start();
    }

    private void start() throws Exception {
        String ksPw = "changeit";

        KeyStore ks = TlsCommon.createKeystore(ksPw);
        ks.setKeyEntry("client", TlsCommon.convertPrivateKey(CLIENT_KEY), ksPw.toCharArray(),
                new Certificate[] { TlsCommon.convertCertificate(CLIENT_CRT) });
        KeyManagerFactory kmf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
        kmf.init(ks, ksPw.toCharArray());

        KeyStore ts = TlsCommon.createKeystore(ksPw);
        ts.setCertificateEntry("server", TlsCommon.convertCertificate(TlsServer.SERVER_CRT));
        TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
        tmf.init(ts);

        SSLContext ctx = SSLContext.getInstance(TlsCommon.TLS_PROTOCOL);
        ctx.init(kmf.getKeyManagers(), tmf.getTrustManagers(), null);

        SSLSocket socket = (SSLSocket) ctx.getSocketFactory().createSocket("195.38.164.139", 443);
        socket.setEnabledCipherSuites(TlsCommon.CIPHER_SUITES);
        socket.setEnabledProtocols(new String[] { TlsCommon.TLS_PROTOCOL });

        System.out.println("Connected: " + socket.isConnected());
        Thread.sleep(1000);
        InputStream is = new BufferedInputStream(socket.getInputStream());
        OutputStream os = new BufferedOutputStream(socket.getOutputStream());
        os.write("Hello World".getBytes());
        os.flush();
        byte[] data = new byte[2048];
        int len = is.read(data);
        if (len <= 0) {
            throw new IOException("No data received.");
        }
        System.out.printf("Client received %d bytes: %s%n", len, new String(data, 0, len));
    }
}