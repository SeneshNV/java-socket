package ThreadSocket.client;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class ClientMessage extends Thread {
    private Socket serverSocket;

    //constructor
    public ClientMessage(Socket serverSocket) {
        this.serverSocket = serverSocket;
    }

    @Override
    public void run() {
        try (
                //client typing message
                BufferedReader inFromClient = new BufferedReader(new InputStreamReader(System.in));
                //typing message to sent to the server socket
                DataOutputStream outToServer = new DataOutputStream(serverSocket.getOutputStream())
        ) {
            String clientMessage;

            while (true) {
                // Client message input
                System.out.print("\nEnter your message: ");
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
            }
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        } finally {
            System.out.println("ClientMessage thread terminated.");
        }
    }
}