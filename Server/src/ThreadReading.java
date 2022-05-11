import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class ThreadReading implements Runnable {
    Socket clientSocket;

    ThreadReading(Socket clientSocket) {
        this.clientSocket = clientSocket;
    }

    public void run() {
        String message;
        InputStreamReader in = null;
        try {
            in = new InputStreamReader(clientSocket.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
        BufferedReader br = new BufferedReader(in);
        try {
            while(true) {
                message = br.readLine();
                if (message.equals(null)) {
                    continue;
                }
                System.out.println(message);
                Thread.sleep(1000);
            }
        } catch (InterruptedException | IOException ex) {
            ex.printStackTrace();
        } catch (NullPointerException e) {
            System.out.println("Client disconnected");
        }
        }
}
