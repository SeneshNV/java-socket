package ThreadSocket;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class Client {
    public static void main(String[] args) {
        try (Socket serverSocket = new Socket("127.0.0.1", 9800)) {
            System.out.println("Connected with Server.......");

            //get in client typing mesage
            BufferedReader inFromClient = new BufferedReader(new InputStreamReader(System.in));

            // get in server message
            BufferedReader inFromServer = new BufferedReader(new InputStreamReader(serverSocket.getInputStream()));

            //send client message
            DataOutputStream outToServer = new DataOutputStream(serverSocket.getOutputStream());

            String clientMessage, serverResponse;

            while (true) {

                // Client message input
                System.out.print("Enter your message: ");
                clientMessage = inFromClient.readLine();

                // Encode message before sending
                String encodedMessage = Base64.getEncoder().encodeToString(clientMessage.getBytes(StandardCharsets.UTF_8));
                System.out.println("Message Sent \t:: " + clientMessage + " --> " + encodedMessage);
                outToServer.writeBytes(encodedMessage + "\n");

                // Stop condition
                if (clientMessage.equals("stop")) {
                    System.out.println("Disconnecting from server...");
                    break;
                }

                //server message section
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
                    decodedResponse = "(Invalid Base64 Encoding) " + serverResponse;
                }

                System.out.println("SERVER (Decoded) \t:: " + decodedResponse);
            }
        } catch (IOException e) {
            System.err.println("Error: " + e.getMessage());
        } finally {
            System.out.println("Client terminated.");
        }
    }
}
