package ThreadSocket;

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

                    //get client messages (connectionSocket === input data)
                    BufferedReader inFromClient = new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));

                    // get server typing message
                    BufferedReader inFromServer = new BufferedReader(new InputStreamReader(System.in));

                    // send server message (connectionSocket === input data)
                    DataOutputStream outToClient = new DataOutputStream(connectionSocket.getOutputStream());

                    String clientMessage, serverMessage;

                    while (true) {
                        // Receive and decode client message
                        clientMessage = inFromClient.readLine();
                        if (clientMessage == null) break;

                        String decodedClientMessage;

                        try {
                            byte[] bytes = Base64.getDecoder().decode(clientMessage);
                            decodedClientMessage = new String(bytes, StandardCharsets.UTF_8);
                        } catch (IllegalArgumentException e) {
                            decodedClientMessage = "(Invalid Base64 Encoding) " + clientMessage;
                        }

                        System.out.println("CLIENT (Decoded) \t:: " + decodedClientMessage + " --> " + clientMessage );

                        //server message section

                        // Check if client wants to stop
                        if (decodedClientMessage.equals("stop")) {
                            System.out.println("Client requested to stop. Closing connection...");
                            outToClient.writeBytes("Goodbye!\n");
                            break;
                        }

                        // Server reply
                        System.out.print("Enter Server Message :: ");
                        serverMessage = inFromServer.readLine();

                        if (serverMessage == null || serverMessage.equals("stop")) {
                            outToClient.writeBytes("Server stopping...\n");
                            break;
                        }

                        // Encode and send server message
                        String encodedServerMessage = Base64.getEncoder().encodeToString(serverMessage.getBytes(StandardCharsets.UTF_8));
                        System.out.println("Message Sent \t:: " + serverMessage + " --> " + encodedServerMessage);
                        outToClient.writeBytes(encodedServerMessage + "\n");
                    }
                } catch (IOException e) {
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
