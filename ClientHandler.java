import java.io.*;
import java.net.*;

public class ClientHandler extends Thread {

    private Socket socket;
    private ChatServer server;
    private BufferedReader in;
    private PrintWriter out;

    public ClientHandler(Socket socket, ChatServer server) {
        this.socket = socket;
        this.server = server;
    }

    @Override
    public void run() {
        try {
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);

            String message;
            while ((message = in.readLine()) != null) {
                server.broadcastMessage(message, this);
            }
        } catch (IOException e) {
            System.err.println("Error handling client: " + e.getMessage());
        } finally {
            server.removeClient(this);
            try {
                socket.close();
            } catch (IOException e) {
                // Ignore
            }
        }
    }

    public void sendMessage(String message) {
        out.println(message);
    }
}
