import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

// Interface représentant une stratégie de recherche de mot de passe
interface PasswordSearchStrategy {
    void searchPassword(String password);
}

// Implémentation de la stratégie de recherche par hash
class HashSearchStrategy implements PasswordSearchStrategy {
    private Map<String, String> passwordDictionary;

    public HashSearchStrategy(Map<String, String> dictionary) {
        this.passwordDictionary = dictionary;
    }

    public void searchPassword(String hash) {
        for (Map.Entry<String, String> entry : passwordDictionary.entrySet()) {
            String password = entry.getKey();
            String passwordHash = getMD5Hash(password); // Exemple avec la fonction de hachage MD5

            if (passwordHash.equals(hash)) {
                System.out.println("Mot de passe correspondant trouvé : " + password);
                return;
            }
        }
        System.out.println("Mot de passe correspondant non trouvé.");
    }

    private String getMD5Hash(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] hashBytes = md.digest(password.getBytes());
            StringBuilder sb = new StringBuilder();
            for (byte b : hashBytes) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }
}

// Implémentation de la stratégie de recherche par mot de passe clair
class PlainTextSearchStrategy implements PasswordSearchStrategy {
    private Map<String, String> passwordDictionary;

    public PlainTextSearchStrategy(Map<String, String> dictionary) {
        this.passwordDictionary = dictionary;
    }

    public void searchPassword(String password) {
        if (passwordDictionary.containsKey(password)) {
            System.out.println("Mot de passe trouvé dans le dictionnaire.");
        } else {
            System.out.println("Mot de passe non trouvé dans le dictionnaire.");
        }
    }
}

// Classe Factory pour obtenir la stratégie de recherche appropriée
class PasswordSearchStrategyFactory {
    public static PasswordSearchStrategy getPasswordSearchStrategy(String searchType, Map<String, String> dictionary) {
        if (searchType.equalsIgnoreCase("hash")) {
            return new HashSearchStrategy(dictionary);
        } else if (searchType.equalsIgnoreCase("plaintext")) {
            return new PlainTextSearchStrategy(dictionary);
        } else {
            throw new IllegalArgumentException("Type de recherche invalide.");
        }
    }
}

public class PasswordCrack {
    public static void main(String[] args) {
        // Création du dictionnaire de mots de passe
        Map<String, String> passwordDictionary = new HashMap<>();
        
        // Lecture des fichiers de dictionnaire et ajout des mots de passe au dictionnaire
        readPasswordDictionary("Dico1.txt", passwordDictionary);
        readPasswordDictionary("Dico2.txt", passwordDictionary);
        readPasswordDictionary("Dico3.txt", passwordDictionary);

        // Demande à l'utilisateur de choisir l'option
        Scanner scanner = new Scanner(System.in);
        System.out.println("Options:");
        System.out.println("1. Entrer un hash");
        System.out.println("2. Entrer un mot de passe");
        System.out.print("Choisissez une option : ");
        int option = scanner.nextInt();
        scanner.nextLine(); // Consomme la nouvelle ligne restante

        // Obtention de la stratégie de recherche en fonction de l'option choisie
        String searchType;
        if (option == 1) {
            searchType = "hash";
        } else if (option == 2) {
            searchType = "plaintext";
        } else {
            System.out.println("Option invalide.");
            return;
        }
        PasswordSearchStrategy searchStrategy = PasswordSearchStrategyFactory.getPasswordSearchStrategy(searchType, passwordDictionary);

        // Demande à l'utilisateur d'entrer le hash ou le mot de passe
        System.out.print("Entrez le hash ou le mot de passe : ");
        String input = scanner.nextLine();

        // Utilisation de la stratégie de recherche
        searchStrategy.searchPassword(input);
    }

    private static void readPasswordDictionary(String fileName, Map<String, String> passwordDictionary) {
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = br.readLine()) != null) {
                passwordDictionary.put(line, ""); // Laisse la valeur vide pour le moment, elle n'est pas utilisée dans cet exemple
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
