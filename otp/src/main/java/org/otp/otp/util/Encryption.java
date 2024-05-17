package org.otp.otp.util;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Base64;

public class Encryption {
    // Set your secret key and initialization vector here
    private static final String SECRET_KEY_STRING = "AS!2KoLqo#3@!12932@11232@";
    private static final String INIT_VECTOR_STRING = "@OP0WW@!S@FleP1006"; // Adjust the IV length to 16 bytes

    // Convert the strings to byte arrays
    private static final byte[] SECRET_KEY_BYTES = Arrays.copyOf(SECRET_KEY_STRING.getBytes(StandardCharsets.UTF_8), 16);
    private static final byte[] INIT_VECTOR_BYTES = padOrTruncate(INIT_VECTOR_STRING.getBytes(), 16);

    // Create SecretKeySpec and IvParameterSpec objects
    private static final SecretKeySpec SECRET_KEY = new SecretKeySpec(SECRET_KEY_BYTES, "AES");
    private static final IvParameterSpec INIT_VECTOR = new IvParameterSpec(INIT_VECTOR_BYTES);

    // Method to pad or truncate the byte array to the specified length
    private static byte[] padOrTruncate(byte[] array, int length) {
        byte[] result = new byte[length];
        System.arraycopy(array, 0, result, 0, Math.min(array.length, length));
        return result;
    }
    public static void main(String[] args) {
        String originalText = "eav1a$7A+gR3BX0a5UXqxke652pw==";

//        String encryptedText = encrypt(originalText);
//        System.out.println("Encrypted: " + encryptedText);

        String decryptedText = decrypt(originalText);
        System.out.println("Decrypted: " + decryptedText);
    }

    public static String encrypt(String input) {
        try {
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, SECRET_KEY, INIT_VECTOR);
            byte[] encryptedBytes = cipher.doFinal(input.getBytes());
            String encryptedBase64 = Base64.getEncoder().encodeToString(encryptedBytes);
            return "eav1a$" + encryptedBase64;
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            return null;
        }
    }

    public static String decrypt(String encrypted) {
        try {
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, SECRET_KEY, INIT_VECTOR);
            byte[] decodedBytes = Base64.getDecoder().decode(encrypted.split("\\$")[1]);
            byte[] decryptedBytes = cipher.doFinal(decodedBytes);
            return new String(decryptedBytes);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            return null;
        }
    }
}
