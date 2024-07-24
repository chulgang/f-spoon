package config;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.Random;
public class CipherUtil {
    private static Properties properties = new Properties();
    public static String key2 = key2Loader();

    private static String key2Loader() {
        try (InputStream input = new FileInputStream("resources/application.properties")) {
            properties.load(input);
            return properties.getProperty("encryption.key");
        } catch (IOException ex) {
            System.out.println("키 파일 없음");
            return null;
        }
    }

    private static String createKey1() {
        StringBuilder sb = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < 20; i++) {
            int keyValue = 32 + random.nextInt(95);
            sb.append((char) keyValue);
        }
        return sb.toString();
    }
    public static String[] encryptCode(String pwd) {
        int length = pwd.length();
        StringBuilder sb = new StringBuilder();
        String key1 = createKey1();
        for (int i = 0; i < length; i++) {
            int intermediateValue = pwd.charAt(i) + key1.charAt(i) - 32;
            if (intermediateValue > 126) {
                intermediateValue = (intermediateValue % 127) + 32;
            }
            int encryptValue = intermediateValue + key2.charAt(i) - 32;
            if (encryptValue > 126) {
                encryptValue = (encryptValue % 127) + 32;
            }
            sb.append((char) encryptValue);
        }
        return new String[]{sb.toString(), key1, key2};
    }
    public static String decryptCode(String encrypted, String key1, String key2) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < encrypted.length(); i++) {
            int intermediateValue = encrypted.charAt(i) - key2.charAt(i) + 32;
            if (intermediateValue < 32) {
                intermediateValue = 127 - (32 - intermediateValue);
            }
            int decryptValue = intermediateValue - key1.charAt(i) + 32;
            if (decryptValue < 32) {
                decryptValue = 127 - (32 - decryptValue);
            }
            sb.append((char) decryptValue);
        }
        return sb.toString();
    }
}
