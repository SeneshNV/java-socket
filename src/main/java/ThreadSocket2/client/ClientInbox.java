package ThreadSocket2.client;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class ClientInbox extends Thread {
    private Socket serverSocket;

    //constructor
    public ClientInbox(Socket serverSocket) {
        this.serverSocket = serverSocket;
    }

    @Override
    public void run() {
        try (BufferedReader inFromServer = new BufferedReader(new InputStreamReader(serverSocket.getInputStream()))) {
            String serverResponse;

            while (true) {
                // Receive server response
                serverResponse = inFromServer.readLine();
                if (serverResponse == null) {
                    System.out.println("Server closed the connection.");
                    break;
                }

                // Decode server message
                String decodedResponse;
                try {
                    byte[] decodedBytes = Base64.getDecoder().decode(serverResponse);
                    decodedResponse = new String(decodedBytes, StandardCharsets.UTF_8);
                } catch (IllegalArgumentException e) {
                    decodedResponse = "\n(Invalid Base64 Encoding) " + serverResponse;
                }

                System.out.println("\nSERVER (Decoded) \t:: " + decodedResponse);
            }
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        } finally {
            System.out.println("ClientInbox thread terminated.");
        }
    }
}