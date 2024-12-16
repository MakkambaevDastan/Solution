import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class GetRequest {

    public static void main(String[] args) throws Exception {

        GetRequest getRequest = new GetRequest();
        getRequest.SendReq("195.38.164.139", 443);
    }

    public void SendReq(String url, int port) throws Exception {

        Socket socket = new Socket(url, port);
        PrintWriter printWriter = new PrintWriter(socket.getOutputStream());
        // printWriter.println("GET /login/signup HTTP/1.1");
        // printWriter.println("Host: " + url);
        // printWriter.println("");
        printWriter.flush();

        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        String response;
        while ((response = bufferedReader.readLine()) != null) {
            System.out.println(response);
        }
        bufferedReader.close();
        printWriter.close();
        socket.close();
    }

}
