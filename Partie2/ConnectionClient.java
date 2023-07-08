package Partie2;
import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class ConnectionClient {

    private static final String SERVEUR_HOTE = "localhost";
    private static final int SERVEUR_PORT = 8080;

    public static void main(String[] args) {
        try {
            Socket socket = new Socket(SERVEUR_HOTE, SERVEUR_PORT);
            System.out.println("Connecté au serveur.");

            // Entrées utilisateur
            Scanner scanner = new Scanner(System.in);

            System.out.println("Veuillez choisir une méthode pour le piratage :");
            System.out.println("1 - Brute-force");
            System.out.println("2 - Dictionnaire");
            int choixMethode = scanner.nextInt();

            // Envoie le choix de la méthode au serveur
            PrintWriter sortie = new PrintWriter(socket.getOutputStream(), true);
            sortie.println(choixMethode);

            // Recevoir la réponse du serveur
            BufferedReader entree = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String resultat = entree.readLine();
            System.out.println("Résultat : " + resultat);

            // Fermer les connexions
            scanner.close();
            sortie.close();
            entree.close();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
