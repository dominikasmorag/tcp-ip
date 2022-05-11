import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class Typing {
    private Socket s;

    Typing(Socket s) {
        this.s = s;
    }

    public void startTyping() {
        PrintWriter pr = null;
        try {
            pr = new PrintWriter(s.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
        Scanner sc = new Scanner(System.in);
        String str = "";

        do {
            try {
                str = sc.nextLine();
                pr.println(str);
                pr.flush();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        } while (!"exit".equals(str));
    }
}
