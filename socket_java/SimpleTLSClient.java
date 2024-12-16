
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.SecureRandom;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

public class SimpleTLSClient {
    public static void main(String[] args) {
        try {
            Socket socket = new Socket("localhost", 9999);
            System.out.println("Connected to server.");
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
            // Send hello rand message to server
            SecureRandom random = new SecureRandom();
            byte[] clientRandomBytes = new byte[12];
            random.nextBytes(clientRandomBytes);
            objectOutputStream.writeObject(clientRandomBytes);
            objectOutputStream.flush();

            // read hello rand message from server
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
            byte[] randomServerString = (byte[]) in.readObject();
            System.out.println(new String(randomServerString, "UTF-8"));

            // Receive public key from server
            PublicKey serverPublicKey = (PublicKey) in.readObject();

            // Generate secret premaster and encrypt using server's public key
            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.ENCRYPT_MODE, serverPublicKey);
            byte[] secretMessage = new byte[8];
            random.nextBytes(secretMessage);
            byte[] encryptedSecret = cipher.doFinal(secretMessage);

            // Send encrypted secret premaster to server
            objectOutputStream.writeObject(encryptedSecret);
            objectOutputStream.flush();

            // Create session key on client
            byte[] sessionKey = concatenateByteArrays(clientRandomBytes, secretMessage, randomServerString);
            System.out.println(new String(sessionKey, "UTF-8"));

            SecretKey clientSessionKey = new SecretKeySpec(sessionKey, "AES");

            Cipher sessionEncryptCipher = Cipher.getInstance("AES");
            sessionEncryptCipher.init(Cipher.ENCRYPT_MODE, clientSessionKey);
            Cipher sessionDecryptCipher = Cipher.getInstance("AES");
            sessionDecryptCipher.init(Cipher.DECRYPT_MODE, clientSessionKey);

            String readyMessage = "Ready";

            // Send ready to server
            objectOutputStream.writeObject(sessionEncryptCipher.doFinal(readyMessage.getBytes()));
            objectOutputStream.flush();

            byte[] readyServerString = (byte[]) in.readObject();

            if (new String(sessionDecryptCipher.doFinal(readyServerString), "UTF-8").equals("Ready")) {
                System.out.println("Received Server ready signal ");
                objectOutputStream.writeObject(sessionEncryptCipher.doFinal("Hello Dastan".getBytes()));
                objectOutputStream.flush();
                System.out.println("Sent Example message");
                objectOutputStream.writeObject(sessionEncryptCipher.doFinal("Stop".getBytes()));
                objectOutputStream.flush();
            }
            // Close resources
            objectOutputStream.close();
            in.close();
            socket.close();
            System.out.println("Stopped connection");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } catch (NoSuchPaddingException e) {
            throw new RuntimeException(e);
        } catch (IllegalBlockSizeException e) {
            throw new RuntimeException(e);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        } catch (BadPaddingException e) {
            throw new RuntimeException(e);
        } catch (InvalidKeyException e) {
            throw new RuntimeException(e);
        }
    }

    public static byte[] concatenateByteArrays(byte[] array1, byte[] array2, byte[] array3) {
        byte[] result = new byte[array1.length + array2.length + array3.length];
        System.arraycopy(array1, 0, result, 0, array1.length);
        System.arraycopy(array2, 0, result, array1.length, array2.length);
        System.arraycopy(array3, 0, result, array1.length + array2.length, array3.length);
        return result;
    }
}
