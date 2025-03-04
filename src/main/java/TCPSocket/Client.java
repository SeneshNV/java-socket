package TCPSocket;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.Socket;

public class Client {
    public static void main(String[] args) throws Exception {
        System.out.println("Client Started.........");

        try (Socket clientSocket = new Socket("127.0.0.1", 9806)) {

            // Client message input
            BufferedReader inFromClient = new BufferedReader(new InputStreamReader(System.in));
            System.out.print("Enter your Username : ");
            String clientMessage = inFromClient.readLine();

            // Send message to server
            DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());
            outToServer.writeBytes(clientMessage + "\n");

            // Read response from server
            BufferedReader inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            String serverMessage = inFromServer.readLine();
            System.out.println("SERVER:: " + serverMessage);


        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}