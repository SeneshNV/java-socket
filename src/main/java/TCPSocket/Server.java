package TCPSocket;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    public static void main(String[] args) throws Exception {
        System.out.println("Server Started.........");

        try (ServerSocket serverSocket = new ServerSocket(9806)) {
            while (true) {
                try (Socket connectionSocket = serverSocket.accept()) {
                    System.out.println("Client Connection Established...");

                    // Read the client string
                    BufferedReader inFromClient = new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));
                    String clientMessage = inFromClient.readLine();
                    System.out.println("CLIENT::" + clientMessage);

                    // Send output to client
                    DataOutputStream outToClient = new DataOutputStream(connectionSocket.getOutputStream());
                    String outputMessage = "=== Received ===" + "\n";
                    outToClient.writeBytes(outputMessage);


                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}