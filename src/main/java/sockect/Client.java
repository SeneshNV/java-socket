package sockect;
import java.io.IOException;
import java.net.Socket;

public class Client {
    public static void main(String[] args) {
        try {
            System.out.println("Client Started");
            Socket server = new Socket("127.0.0.1", 9806);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}
