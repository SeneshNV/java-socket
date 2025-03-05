package ThreadSocket2.server;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class ServerInbox extends Thread{

    Socket connectionSocket;

    public ServerInbox(Socket connectionSocket) {
        this.connectionSocket = connectionSocket;
    }

    @Override
    public void run() {
        //get client messages (connectionSocket === input data)
        BufferedReader inFromClient = null;
        try {
            inFromClient = new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        String clientMessage = null;

        while (true) {
        try {
            clientMessage = inFromClient.readLine();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
            String decodedClientMessage;

            byte[] bytes = Base64.getDecoder().decode(clientMessage);
            decodedClientMessage = new String(bytes, StandardCharsets.UTF_8);

            System.out.println("\nCLIENT (Decoded) \t:: " + decodedClientMessage + " --> " + clientMessage );

            // send server message (connectionSocket === input data)
            DataOutputStream outToClient = null;
            try {
                outToClient = new DataOutputStream(connectionSocket.getOutputStream());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            // Check if client wants to stop
            if (decodedClientMessage.equals("stop")) {
                System.out.println("Client requested to stop. Closing connection...");
                try {
                    outToClient.writeBytes("Goodbye!\n");
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                break;
            }
        }

    }
}
