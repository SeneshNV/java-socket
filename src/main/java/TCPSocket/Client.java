package TCPSocket;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.Socket;

public class Client {
    public static void main(String[] args) throws Exception {
        System.out.println("Server Started.........");

        String clientMessage;
        String serverMessage;

        Socket clientSocket = new Socket("127.0.0.1", 9806);

        //client message getting
        BufferedReader inFromClient = new BufferedReader(new InputStreamReader(System.in));
        System.out.print("Enter your Username :");
        clientMessage = inFromClient.readLine();

        DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());
        outToServer.writeBytes(clientMessage);


        BufferedReader inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        serverMessage = inFromServer.readLine();
        System.out.println("SERVER:: " + serverMessage);
    }
}
