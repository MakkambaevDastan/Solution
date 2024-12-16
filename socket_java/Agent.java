import java.net.Socket;
import java.net.UnknownHostException;
import java.io.IOException;
import java.io.OutputStream;
import java.io.InputStream;

public class Agent {
    private Socket socket;
    private String host;
    private int port;

    public Agent(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public void send(String request) {

        try {
            socket = new Socket(host, port);
            System.out.println();
            System.out.println("REQUEST: " + request);
            sendMessage(request);

            String response = readData();
            System.out.println();
            System.out.println("RESPONSE: " + response);

        } catch (UnknownHostException uhe) {
            System.out.println("Error " + uhe);
        } catch (IOException ioe) {
            System.out.println("Error " + ioe);
        }
    }

    private void sendMessage(String text) {
        byte[] array = text.getBytes();
        try {
            OutputStream outputStream = socket.getOutputStream();
            outputStream.write(array);
            outputStream.flush();
        } catch (IOException ioe) {
            System.out.println("Client Error " + ioe);
        }
    }

    private String readData() {
        String text = "";
        try {
            InputStream input = socket.getInputStream();
            while (true) {
                int unicode = input.read();
                char symbol = (char) unicode;
                text = text + symbol;
                System.out.print(symbol);
                if (input.available() == 0) {
                    break;
                }
            }
            return text;
        } catch (IOException ioe) {
            System.out.println("Client error " + ioe);
            return null;
        }
    }
}
