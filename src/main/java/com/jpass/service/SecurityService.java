package com.jpass.service;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.SecureRandom;
import java.util.Base64;

/**
 * Service gérant la sécurité et le chiffrement des données.
 */
public class SecurityService {
    private static final String ALGORITHM = "AES";
    private static final int ITERATIONS = 65536;
    private static final int KEY_LENGTH = 256;

    public String generateSalt() {
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[16];
        random.nextBytes(salt);
        return Base64.getEncoder().encodeToString(salt);
    }

    private SecretKey deriveKey(String masterPassword, String salt) throws Exception {
        PBEKeySpec spec = new PBEKeySpec(
                masterPassword.toCharArray(),
                Base64.getDecoder().decode(salt),
                ITERATIONS,
                KEY_LENGTH);
        SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
        byte[] keyBytes = factory.generateSecret(spec).getEncoded();
        return new SecretKeySpec(keyBytes, ALGORITHM);
    }

    /**
     * Chiffre une donnée sensible.
     * 
     * @param data           La donnée à chiffrer
     * @param masterPassword Le mot de passe maître
     * @param salt           Le sel
     * @return La donnée chiffrée
     */
    public String encrypt(String data, String masterPassword, String salt) throws Exception {

        SecretKey key = deriveKey(masterPassword, salt);
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, key);
        byte[] encryptedBytes = cipher.doFinal(data.getBytes());
        return Base64.getEncoder().encodeToString(encryptedBytes);

    }

    /**
     * Déchiffre une donnée sensible.
     * 
     * @param encryptedData  La donnée chiffrée
     * @param masterPassword Le mot de passe maître
     * @param salt           Le sel
     * @return La donnée déchiffrée
     */
    public String decrypt(String encryptedData, String masterPassword, String salt) throws Exception {

        SecretKey key = deriveKey(masterPassword, salt);
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, key);
        byte[] decryptedBytes = cipher.doFinal(Base64.getDecoder().decode(encryptedData));
        return new String(decryptedBytes);

    }

    /**
     * Génère un mot de passe fort.
     * 
     * @param length              Longueur souhaitée du mot de passe
     * @param includeSpecialChars Inclure des caractères spéciaux
     * @return Le mot de passe généré
     */
    public String generateStrongPassword(int length, boolean includeSpecialChars) {
        // À implémenter
        return null;
    }
}
