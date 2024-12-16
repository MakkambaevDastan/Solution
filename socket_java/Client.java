import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import java.net.UnknownHostException;
import java.net.ConnectException;
import java.net.InetAddress;
import java.net.SocketTimeoutException;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;
import javax.security.auth.x500.X500Principal;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLHandshakeException;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;

public class Client {
    private int tcpPort = -1;
    private InetAddress address = null;
    private SSLSocket socket = null;

    private boolean exchangeMessages() throws ConnectException {
        if (this.socket == null || this.socket.isClosed()) {
            throw new ConnectException("Couldn't connect to the server.");
        }
        PrintWriter spw;
        BufferedReader br;
        try {
            spw = new PrintWriter(this.socket.getOutputStream(), true);
            br = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
        } catch (IOException e) {
            throw new ConnectException("Couldn't setup the message exchange.");
        }

        // String sendme = "";
        // if (this.address.getHostName().equals("localhost") || this.address.getHostName().equals("127.0.0.1")) {
        //     sendme = "Hello, Server!";
        // } else {
            String sendme = "GET / HTTP/1.1" + "\r\n"
                    + "Host: " + this.address.getHostName() + "\r\n"
                    + "User-Agent: assig2client" + "\r\n"
                    + "Accept: */*" + "\r\n";
        // }

        spw.println(sendme);
        System.out.println("Sending: ");
        for (String str : sendme.split("\r\n")) {
            System.out.println("\t" + str);
        }
        String resp = "";
        try {
            System.out.println("Receiving: ");
            try {
                while ((resp = br.readLine()) != null) {
                    System.out.println("\t" + resp);
                }
            } catch (SocketTimeoutException se) {
                return true;
            }
        } catch (IOException e) {
            throw new ConnectException("Couldn't receive the readLine.");
        }
        return true;
    }

    public boolean closeConnection() throws IOException {
        if (this.socket != null && !this.socket.isClosed()) {
            this.socket.close();
            return true;
        } else {
            return false;
        }
    }

    public boolean connectToHost() {
        try {
            System.out.println("Loading certificate...");
            String certdir = "./Certificates";
            // String certdir = "./Certificates/milbursamuRA.com.pem";
            // String certpath = "./Certificates/milbursamuRA.com.pem";
            // certpath = "Certificates/google.com-rootcert";
            // certpath = "Certificates/amazon.com-rootcert";
            X509TrustManager tm = this.loadCustomTrustManager(certdir);
            SSLContext context = SSLContext.getInstance("TLSv1.3");
            context.init(null, new TrustManager[] { tm }, new SecureRandom());
            System.out.println("Attempting to connect to " + this.address.getHostName() + "...");
            SSLSocketFactory socketFactory = (SSLSocketFactory) context.getSocketFactory();
            this.socket = (SSLSocket) socketFactory.createSocket(this.address, this.tcpPort);
            this.socket.setUseClientMode(true);
            this.socket.setSoTimeout(500);

            try {
                this.socket.startHandshake();
                List<X509Certificate> trustedCerts = Arrays.asList(tm.getAcceptedIssuers());
                List<String> trustedAuthorities = (trustedCerts.stream()
                        .map(X509Certificate::getSubjectX500Principal)
                        .collect(Collectors.toList()))
                        .stream()
                        .map(X500Principal::getName)
                        .collect(Collectors.toList());
                System.out.println("Authenticated with trusted root authorities: ");
                for (String authority : trustedAuthorities) {
                    System.out.println("\t" + authority);
                }
                System.out.println("Connected via TLSv1.3.");
            } catch (SSLHandshakeException sslhe) {
                this.socket.close();
                System.out.println("Error: Server couldn't be authenticated through certificate chain.");
                return false;
            }
            this.exchangeMessages();
            return true;
        } catch (FileNotFoundException | CertificateException e) {
            System.out.println("Error: " + e.getMessage());
            return false;
        } catch (NoSuchAlgorithmException e) {
            System.out.println("Error: Couldn't get the SSLContext instance.");
            return false;
        } catch (KeyManagementException e) {
            System.out.println("Error: Couldn't initialise the SSLContext.");
            return false;
        } catch (IOException e) {
            System.out.println("Error: Couldn't create the socket.");
            return false;
        }
    }

    private X509TrustManager loadCustomTrustManager(String certificatesDir)
            throws FileNotFoundException, CertificateException {
        if (certificatesDir == null || certificatesDir.isBlank()) {
            throw new FileNotFoundException("Invalid dir path argument.");
        }
        File file = new File(certificatesDir);
        // if (!file.exists() || !file.isDirectory() || !file.canRead()) {
        //     throw new FileNotFoundException("Couldn't locate or read the certificate dir.");
        // }
        KeyStore trustStore = null;
        X509TrustManager trustManager = null;
        try {
            trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
            trustStore.load(null);

            CertificateFactory cf = CertificateFactory.getInstance("X.509");
            File[] subfiles = file.listFiles();
            for (File f : subfiles) {
                FileInputStream rootCertFIS = new FileInputStream(f);
                X509Certificate rootCert = (X509Certificate) cf.generateCertificate(rootCertFIS);
                rootCertFIS.close();
                trustStore.setCertificateEntry(f.getName(), rootCert);
            }
            TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            tmf.init(trustStore);
            for (TrustManager tm : tmf.getTrustManagers()) {
                if (tm instanceof X509TrustManager) {
                    trustManager = (X509TrustManager) tm;
                    break;
                }
            }
            if (trustManager == null) {
                throw new CertificateException("Couldn't build the trust manager.");
            } else {
                return trustManager;
            }
        } catch (KeyStoreException kse) {
            throw new CertificateException("Couldn't initialise the certificate trust store.");
        } catch (NoSuchAlgorithmException e) {
            throw new CertificateException("Couldn't initialise the TrustManagerFactory.");
        } catch (CertificateException e) {
            throw new CertificateException("Couldn't load or store the certificate.");
        } catch (IOException e) {
            throw new FileNotFoundException("Couldn't read the file.");
        }
    }

    public Client(String hostname, int tcpPort) throws IllegalArgumentException {
        if (hostname == null || hostname.isBlank()) {
            throw new IllegalArgumentException("Invalid hostname argument.");
        } else {
            try {
                this.address = InetAddress.getByName(hostname);
            } catch (UnknownHostException uhe) {
                throw new IllegalArgumentException("Couldn't locate the server by the hostname.");
            }
        }
        if (tcpPort <= 1 || tcpPort >= 49151) {
            throw new IllegalArgumentException("Port number in an invalid range.");
        } else {
            this.tcpPort = tcpPort;
        }
    }

    public static void main(String[] args) {
        int tcpPort = -1;
        boolean help = false;
        String hostname = "";
        if (args.length == 0) {
            tcpPort = 443;
            hostname = "195.38.164.139";
        }
        for (int argindex = 0; argindex < args.length; argindex++) {
            String arg = args[argindex].toLowerCase();
            if (arg.equals("-p") || arg.equals("--port")) {
                if (argindex == args.length - 1) {
                    System.out.println("Error: no port number specified.");
                    return;
                } else {
                    String portnum = args[argindex + 1];
                    try {
                        tcpPort = Integer.parseInt(portnum);
                        argindex += 2;
                    } catch (NumberFormatException nfe) {
                        System.out.println("Couldn't parse the port number.");
                    }
                }
            } else if (arg.equals("-h") || arg.equals("--help")) {
                help = true;
            } else {
                hostname = arg;
            }
        }
        if (help) {
            String message = "";
            message += "Syntax:\tjava assig2.tls.client.Client hostname [-p|--port portnum][-h|--help]\n";
            message += "assig2.tls.client.Client by Sam Milburn.\n";
            message += "Options:\n";
            message += "\t-p or --port\tconnect to the host on the specified port.\n";
            message += "\t-h or --help\tdisplay this help message.";
            System.out.println(message);
            return;
        }
        System.out.println("hostname: " + hostname + ", port: " + tcpPort);

        try {
            Client client = new Client(hostname, tcpPort);
            client.connectToHost();
            client.closeConnection();
        } catch (IllegalArgumentException iae) {
            System.out.println("Error: " + iae.getMessage());
        } catch (IOException ioe) {
            System.out.println("Error: " + ioe.getMessage());
        }
    }
}