package ThreadSocket.client;

import java.io.IOException;
import java.net.Socket;

public class Client {
    public static void main(String[] args) {
        try (Socket serverSocket = new Socket("127.0.0.1", 9800)) {
            System.out.println("Connected with Server.......");

            // Create and start the ClientInbox thread
            ClientInbox clientInbox = new ClientInbox(serverSocket);
            clientInbox.start();

            // Create and start the ClientMessage thread
            ClientMessage clientMessage = new ClientMessage(serverSocket);
            clientMessage.start();

            // Wait for both threads to finish
            clientMessage.join();
            clientInbox.join();

        } catch (IOException | InterruptedException e) {
            System.err.println("Error: " + e.getMessage());
        } finally {
            System.out.println("Client terminated.");
        }
    }
}