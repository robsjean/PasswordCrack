package Partie2;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;


public class connexion {
    

public class GestionnaireClient implements Runnable {
    private Socket socketClient;

    public GestionnaireClient(Socket socketClient) {
        this.socketClient = socketClient;
    }

    @Override
    public void run() {
        try {
            BufferedReader lecteur = new BufferedReader(new InputStreamReader(socketClient.getInputStream()));
            PrintWriter ecrivain = new PrintWriter(socketClient.getOutputStream(), true);

            String motDePasseServeur = "pass";
            String choix = lecteur.readLine();

            // Utiliser la méthode de crack de mot de passe appropriée en fonction du choix du client
            FabriqueDecrypteur fabriqueDecrypteur = new FabriqueDecrypteur();

            switch (choix) {
                case "1":
                    Object DecrypteurBruteForce = fabriqueDecrypteur.creerDecrypteur("bruteforce");
                    String resultatBruteForce = decrypteurBruteForce.trouverMotDePasseClair(motDePasseServeur);
                    ecrivain.println("Résultat de la force brute : " + resultatBruteForce);
                    break;

                case "2":
                    DecrypteurInterface decrypteurDictionnaire = (DecrypteurInterface) fabriqueDecrypteur.creerDecrypteur("dictionnaire");
                    String resultatDictionnaire = decrypteurDictionnaire.trouverMotDePasseClair(motDePasseServeur);
                    ecrivain.println("Résultat du dictionnaire : " + resultatDictionnaire);
                    break;

                default:
                    ecrivain.println("Choix non disponible");
                    break;
            }

            // Fermer les flux et la connexion client
            lecteur.close();
            ecrivain.close();
            socketClient.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

}
