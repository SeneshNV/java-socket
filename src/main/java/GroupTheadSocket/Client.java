package GroupTheadSocket;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
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

//            this.username = username;


        } catch (IOException e) {
            closeEverything(socket, bufferedReader, bufferedWriter);
        }
    }

    private String encodeMessage(String message){
        return Base64.getEncoder().encodeToString(message.getBytes(StandardCharsets.UTF_8));
    }

    private String decodeMessage(String encodedMessage){
        return new String(Base64.getDecoder().decode(encodedMessage), StandardCharsets.UTF_8);
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

                bufferedWriter.write(encodeMessage(username + " : " + messageToSend));
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
                String messageFromGroupChat;

                while (socket.isConnected()){
                    try {
                        messageFromGroupChat = bufferedReader.readLine();

                        System.out.println(decodeMessage(messageFromGroupChat));

                    } catch (IOException e) {
                        closeEverything(socket, bufferedReader, bufferedWriter);
                    }
                }
            }
        }).start();
    }

    //close down connection and stram
    public void closeEverything(Socket socket, BufferedReader bufferedReader, BufferedWriter bufferedWriter){
        try {
            if(bufferedReader != null){
                bufferedReader.close();
            }

            if(bufferedWriter != null){
                bufferedWriter.close();
            }

            if(socket != null){
                socket.close();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) throws IOException {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter you username for Group chat ^_^ : ");
        String username = scanner.nextLine();


        //connect with server
        Socket socket = new Socket("127.0.0.1", 1234);

        Client client = new Client(socket, username);

        //listen for messages
        client.listenForMessgae();

        //client send messages
        client.sendMessage();

    }

}
