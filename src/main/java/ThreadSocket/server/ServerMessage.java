package ThreadSocket.server;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class ServerMessage extends Thread{

    Socket connectionSocket;

    public ServerMessage(Socket connectionSocket) {
        this.connectionSocket = connectionSocket;
    }

    @Override
    public void run() {
        // get server typing message
        BufferedReader inFromServer = new BufferedReader(new InputStreamReader(System.in));

        // send server message (connectionSocket === input data)
        DataOutputStream outToClient = null;
        try {
            outToClient = new DataOutputStream(connectionSocket.getOutputStream());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        String serverMessage;

        while (true) {
            System.out.print("\nEnter Server Message :: ");
            try {
                serverMessage = inFromServer.readLine();

                if (serverMessage == null || serverMessage.equals("stop")) {
                    outToClient.writeBytes("Server stopping...\n");
                    break;
                }

                // Encode and send server message
                String encodedServerMessage = Base64.getEncoder().encodeToString(serverMessage.getBytes(StandardCharsets.UTF_8));
                System.out.println("Message Sent \t:: " + serverMessage + " --> " + encodedServerMessage);
                outToClient.writeBytes(encodedServerMessage + "\n");


            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
