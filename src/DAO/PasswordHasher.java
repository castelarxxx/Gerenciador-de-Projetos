package DAO;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

public class PasswordHasher {
    private static final int SALT_LENGTH = 16;
    private static final String ALGORITHM = "SHA-256";

    public static String hashPassword(String password) {
        try {
            // Gerar salt aleatório
            SecureRandom random = new SecureRandom();
            byte[] salt = new byte[SALT_LENGTH];
            random.nextBytes(salt);

            // Combinar salt com password e fazer hash
            MessageDigest digest = MessageDigest.getInstance(ALGORITHM);
            digest.update(salt);
            byte[] hashedBytes = digest.digest(password.getBytes());

            // Combinar salt com hash para armazenamento
            byte[] combined = new byte[salt.length + hashedBytes.length];
            System.arraycopy(salt, 0, combined, 0, salt.length);
            System.arraycopy(hashedBytes, 0, combined, salt.length, hashedBytes.length);

            return Base64.getEncoder().encodeToString(combined);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Algoritmo de hash não disponível", e);
        }
    }

    public static boolean verifyPassword(String password, String storedHash) {
        try {
            // Decodificar o hash armazenado
            byte[] combined = Base64.getDecoder().decode(storedHash);

            // Extrair salt e hash
            byte[] salt = new byte[SALT_LENGTH];
            byte[] storedHashBytes = new byte[combined.length - SALT_LENGTH];
            System.arraycopy(combined, 0, salt, 0, SALT_LENGTH);
            System.arraycopy(combined, SALT_LENGTH, storedHashBytes, 0, storedHashBytes.length);

            // Calcular hash da password fornecida com o mesmo salt
            MessageDigest digest = MessageDigest.getInstance(ALGORITHM);
            digest.update(salt);
            byte[] hashedBytes = digest.digest(password.getBytes());

            // Comparar os hashes
            return MessageDigest.isEqual(hashedBytes, storedHashBytes);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Algoritmo de hash não disponível", e);
        }
    }
}