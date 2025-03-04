package sockect;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

    public static void main(String[] args) {
        try {
            System.out.println("server is wait for client....");
            ServerSocket serverSocket = new ServerSocket(9806);
            Socket socket = serverSocket.accept();
            System.out.println("Client is connected....");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
