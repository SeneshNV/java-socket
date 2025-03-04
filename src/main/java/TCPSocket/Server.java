package TCPSocket;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    public static void main(String[] args) throws Exception {
        System.out.println("Server Started.........");

        String clientMessage;
        String outputMessage;

        ServerSocket serverSocket = new ServerSocket(9806);

        while (true){
            Socket clientsocket = serverSocket.accept();
            System.out.println("Client Connect is Established...");

            //read the client string
            BufferedReader inFromClient = new BufferedReader(new InputStreamReader(clientsocket.getInputStream()));
            clientMessage = inFromClient.readLine();

            //send out put to client
            DataOutputStream outToClient = new DataOutputStream(clientsocket.getOutputStream());
            outputMessage = clientMessage + "-- Message Received ";
            outToClient.writeBytes(outputMessage);
        }
    }
}
