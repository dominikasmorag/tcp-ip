import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    private Socket socket;
    private BufferedReader in;
    private BufferedWriter out;
    private String username;

    public Client(Socket socket, String username) {
        try {
            this.socket = socket;
            this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            this.username = username;
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public void sendMessage() {
        try {
            out.write(username);
            out.newLine();
            out.flush();

            Scanner sc = new Scanner(System.in);
            while(socket.isConnected()) {
                String messageToSend = sc.nextLine();
                out.write(username + ": " + messageToSend);
                out.newLine();
                out.flush();
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public void readMessage() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String message;

                while(socket.isConnected()) {
                    try {
                        message = in.readLine();
                        System.out.println(message);
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }
            }
        }).start();
    }

    public static void main(String[] args) throws IOException {
        Scanner scanner = new Scanner(System.in);
        System.out.println("enter your username: ");
        String username = scanner.nextLine();
        Socket socket = new Socket("localhost", 5555);

        Client client = new Client(socket, username);

        client.readMessage();
        client.sendMessage();
    }

}
