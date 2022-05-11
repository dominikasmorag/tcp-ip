import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

public class ClientHandler implements Runnable {
    private final Socket clientSocket;
    private final BufferedReader in;
    private final BufferedWriter out;
    private static ArrayList<ClientHandler> clients = new ArrayList<>();
    private String username;


    public ClientHandler(Socket s) throws IOException {
        this.clientSocket = s;
        in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        out = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
        username = in.readLine();
        clients.add(this);
    }

    @Override
    public void run() {
        String messageFromClient;
        while(true) {
            try {
                messageFromClient = in.readLine();
                if(messageFromClient.equals("exit")) {
                    break;
                }
                broadcastMessage(messageFromClient);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    public void broadcastMessage(String messageToSend) {
        for (ClientHandler clientHandler : clients) {
            try {
                if (!clientHandler.username.equals(username)) {
                    clientHandler.out.write(messageToSend);
                    clientHandler.out.newLine();
                    clientHandler.out.flush();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
                closeEverything(clientSocket, in, out);
            }

        }
    }

    private void removeClientHandler() {
        clients.remove(this);
        broadcastMessage("SERVER: " + username + " has left the chat");
    }

    private void closeEverything(Socket socket, BufferedReader in, BufferedWriter out) {
        removeClientHandler();
        try {
            in.close();
            out.close();
            socket.close();
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }
}
