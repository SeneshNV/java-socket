package GroupTheadSocket;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Base64;

public class ClientHandler implements Runnable {

    // main purpose is keep track all the client
    // if client sends message , then loop true the all the clients
    public static ArrayList<ClientHandler> clientHandlers = new ArrayList<>();
    private Socket socket;

    //Buffered Reader use to read data
    // for read client messages
    private BufferedReader bufferedReader;

    // for send messages (can send for other client)
    private BufferedWriter bufferedWriter;

    //client user name
    private String clientUsername;

    //constructor
    public ClientHandler(Socket socket) {
        try {
            this.socket = socket;
            // java script has two type stream
            // 1. byte stream // 2. charter stream
            // for send message , we use charter stram
            // word ==> Writer for (charter)
            // word ==> Stream for (byte)
            this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

            //read the client message
            this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            //read the client user name
            this.clientUsername = bufferedReader.readLine();

            //add the client to group chat
            // this <== this represent the client handler objects
            clientHandlers.add(this);

            //broadcast Message
            broadCastMessage(encodeMessage("SERVER : " + clientUsername + " has entered the chat...(●'◡'●)"));


        } catch (IOException e) {
            closeEverything(socket, bufferedReader, bufferedWriter);
        }
    }

    private String encodeMessage(String message){
        return Base64.getEncoder().encodeToString(message.getBytes(StandardCharsets.UTF_8));
    }

    @Override
    public void run() {
        String messageFromClient;

        while (socket.isConnected()){
            try {
                messageFromClient = bufferedReader.readLine();
                broadCastMessage(messageFromClient);
            } catch (IOException e) {
                closeEverything(socket, bufferedReader, bufferedWriter);
                break;
            }
        }
    }

    public void broadCastMessage(String messageToSend){
        for(ClientHandler clientHandler : clientHandlers){
            try {
                if(!clientHandler.clientUsername.equals(clientUsername)){
                    clientHandler.bufferedWriter.write(messageToSend);
                    clientHandler.bufferedWriter.newLine();
                    //buffer not send down output stram
//                    The flush() method of PrintWriter Class in Java is
//                    used to flush the stream. By flushing the stream,
//                    it means to clear the stream of any element that may
//                    be or maybe not inside the stream. It neither accepts
//                    any parameter nor returns any value.
                    clientHandler.bufferedWriter.flush();
                }
            } catch (IOException e) {
                closeEverything(socket, bufferedReader, bufferedWriter);
            }
        }
    }

    //remove the client handler from the array list
    public void removeClientHandler(){
        //remove current client handler
        clientHandlers.remove(this);
        broadCastMessage(encodeMessage("SERVER : " + clientUsername + " has left the chat...(ツ)"));
    }

    //close down connection and stram
    public void closeEverything(Socket socket, BufferedReader bufferedReader, BufferedWriter bufferedWriter){
        removeClientHandler();

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
}
