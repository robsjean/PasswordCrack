package Partie2;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
public class server {
    

public class SecureServer {
    private static final int SERVER_PORT = 8080;

    public static void main(String[] args) {
        try {
            try (ServerSocket server = new ServerSocket(SERVER_PORT)) {
                System.out.println("Le serveur est en ligne. Port : " + SERVER_PORT);

                while (true) {
                    Socket clientConnection = server.accept();
                    System.out.println("Nouvelle connexion client établie depuis : " + clientConnection.getInetAddress().getHostAddress());

                    // Gérer la connexion client dans un thread séparé
                    Thread clientHandlerThread = new Thread();
                    clientHandlerThread.start();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

}
