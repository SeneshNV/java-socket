package GroupTheadSocket;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    private Socket socket;
    private BufferedReader bufferedReader;

    // for send messages (can send for other client)
    private BufferedWriter bufferedWriter;

    //client username
    private String username;


    public Client(Socket socket, String username) {
        try {
            this.socket = socket;
            this.username = username;
            //send message to other client
            this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

            //get mssages for other clients
            this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            this.username = username;


        } catch (IOException e) {
            closeEverything(socket, bufferedReader, bufferedWriter);
        }
    }

    public  void sendMessage(){
        try {
            //Client Handler waitig for username
            bufferedWriter.write(username);
            bufferedWriter.newLine();
            bufferedWriter.flush();

            Scanner scanner = new Scanner(System.in);
            while (socket.isConnected()){
                String messageToSend = scanner.nextLine();
                bufferedWriter.write(username + " : " + messageToSend);
                bufferedWriter.newLine();
                bufferedWriter.flush();
            }
        } catch (IOException e) {
            closeEverything(socket, bufferedReader, bufferedWriter);
        }
    }

    //lisning broadcast message from server and other users
    public void listenForMessgae(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                
            }
        });
    }

}
