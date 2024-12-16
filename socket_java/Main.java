import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class Main {

    public static void main(String[] args) throws IOException {
        Agent agent = new Agent("146.190.62.39", 80);
        File file = new File("request");
        String request = Files.readString(file.toPath());
        agent.send(request);
    }
}
