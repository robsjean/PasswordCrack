import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Scanner;

// Interface représentant le hachage d'un mot de passe
interface PasswordHasher {
    String hashPassword(String password);
}

// Implémentation concrète du hachage de mot de passe avec l'algorithme MD5
class MD5PasswordHasher implements PasswordHasher {
    @Override
    public String hashPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] messageDigest = md.digest(password.getBytes());
            StringBuilder sb = new StringBuilder();
            for (byte b : messageDigest) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }
}

// Factory Method pour créer l'instance de PasswordHasher appropriée
class PasswordHasherFactory {
    public static PasswordHasher createPasswordHasher(String algorithm) {
        if (algorithm.equalsIgnoreCase("MD5")) {
            return new MD5PasswordHasher();
        }
        // Ajoutez d'autres implémentations ici pour d'autres algorithmes de hachage
        return null;
    }
}

public class PasswordRecovery {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Entrez le hash à rechercher : ");
        String targetHash = scanner.nextLine();

        System.out.print("Entrez l'algorithme de hachage (MD5) : ");
        String algorithm = scanner.nextLine();

        // Création de l'instance de PasswordHasher en utilisant le Factory Method
        PasswordHasher passwordHasher = PasswordHasherFactory.createPasswordHasher(algorithm);

        String password = recoverPassword(targetHash, passwordHasher);
        if (password != null) {
            System.out.println("Le mot de passe est : " + password);
        } else {
            System.out.println("Mot de passe introuvable.");
        }

        scanner.close();
    }

    public static String recoverPassword(String targetHash, PasswordHasher passwordHasher) {
        String characters = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789"; // Caractères possibles
        int maxLength = 8; // Longueur maximale du mot de passe à rechercher

        for (int length = 1; length <= maxLength; length++) {
            String password = recoverPasswordHelper(targetHash, new char[length], 0, characters, passwordHasher);
            if (password != null) {
                return password;
            }
        }

        return null;
    }

    public static String recoverPasswordHelper(String targetHash, char[] password, int position, String characters,
            PasswordHasher passwordHasher) {
        if (position == password.length) {
            String passwordString = new String(password);
            String hashedPassword = passwordHasher.hashPassword(passwordString);

            if (hashedPassword.equals(targetHash)) {
                return passwordString;
            } else {
                return null;
            }
        }

        for (int i = 0; i < characters.length(); i++) {
            password[position] = characters.charAt(i);
            String foundPassword = recoverPasswordHelper(targetHash, password, position + 1, characters,
                    passwordHasher);
            if (foundPassword != null) {
                return foundPassword;
            }
        }

        return null;
    }
}
