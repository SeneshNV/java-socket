package ThreadSocket.server;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class Server {
    public static void main(String[] args) {
        System.out.println("Server Started.........");

        try (ServerSocket serverSocket = new ServerSocket(9800)) {
            while (true) {
                try (Socket connectionSocket = serverSocket.accept()) {
                    System.out.println("Client Connection Established...");

                    ServerMessage serverMessage = new ServerMessage(connectionSocket);
                    serverMessage.start();

                    ServerInbox serverInbox = new ServerInbox(connectionSocket);
                    serverInbox.start();

                    serverMessage.join();
                    serverInbox.join();

                    }
                 catch(Exception e) {
                    System.err.println("Error handling client: " + e.getMessage());
                } finally {
                    System.out.println("Client disconnected.");
                }
            }
        } catch (IOException e) {
            System.err.println("Server error: " + e.getMessage());
        }
    }
}
