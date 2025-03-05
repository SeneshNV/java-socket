package GroupTheadSocket;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class Server {

    private ServerSocket serverSocket;

    public Server(ServerSocket serverSocket) {
        this.serverSocket = serverSocket;
    }

    public void startServer(){
        try {
            while (!serverSocket.isClosed()){
                Socket socket = serverSocket.accept();
                System.out.println("A new client has connected..^_^");

                //client handler - responsible for communicating with client
                // and create interface runable
                ClientHandler clientHandler = new ClientHandler(socket);
                

                Thread thread = new Thread(clientHandler);
                thread.start();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    //send message for all client
    public void sendAdminMessage(String message) {
        if (ClientHandler.clientHandlers.size() > 0) {
            for (ClientHandler clientHandler : ClientHandler.clientHandlers) {
                clientHandler.broadCastMessage("SERVER : " + message);
            }
        } else {
            System.out.println("No clients connected to send the message.");
        }
    }

    public void closeServerSocket(){
        try {
            if(serverSocket != null){
                serverSocket.close();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(1234);
        Server server = new Server(serverSocket);
        server.startServer();

        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter Admin Message : ");
        while (true) {
            String adminMessage = scanner.nextLine();
            server.sendAdminMessage(adminMessage);
        }

    }
}
