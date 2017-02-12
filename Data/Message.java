package internetofeveryone.ioe.Data;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

/**
 * Created by Fabian Martin for 'Internet of Everyone'
 *
 * This class represents the custom data type Message
 */
public class Message {

    private String senderID; // user code of the sender
    private String receiverID; // user code of the receiver
    private String content;
    private long id; // unique identifier the message
    private boolean isEncrypted; // flag that stores if the message is encrypted
    private boolean isMine; // flag that sotres if the message has been sent by my (it's senderID is equal to my userCode)

    /**
     * Instantiates a new Message.
     *
     * @param senderID
     * @param receiverID
     * @param content
     * @param isEncrypted
     */
    public Message(long id, String senderID, String receiverID, String content, boolean isEncrypted, String myUserCode) {
        this.id = id;
        this.senderID = senderID;
        this.receiverID = receiverID;
        this.content = content;
        this.isEncrypted = isEncrypted;
        this.isMine = senderID.equals(myUserCode);
    }

    /**
     * Encrypts a message.
     *
     * @param message the decrypted message
     * @param key     the key
     * @return the encrypted message
     */
    public static String encrypt(String message, String key) throws UnsupportedEncodingException, NoSuchAlgorithmException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException, NoSuchPaddingException {
        Cipher cipher = Cipher.getInstance("AES");
        byte[] keyBytes = key.getBytes("UTF-8");
        MessageDigest sha = MessageDigest.getInstance("SHA-1");
        keyBytes = sha.digest(keyBytes);
        keyBytes = Arrays.copyOf(keyBytes, 16); // use only first 128 bit
        SecretKeySpec secretKeySpec = new SecretKeySpec(keyBytes, "AES");
        cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec);

        byte[] inputBytes = message.getBytes();
        return new String(cipher.doFinal(inputBytes), "UTF-8");
    }

    /**
     * Decrypts a message.
     *
     * @param message the encrypted message
     * @param key     the key
     * @return the decrypted message
     */
    public static String decrypt(String message, String key) throws BadPaddingException, IllegalBlockSizeException, UnsupportedEncodingException, NoSuchAlgorithmException, InvalidKeyException, NoSuchPaddingException {
        Cipher cipher = Cipher.getInstance("AES");
        byte[] keyBytes = key.getBytes("UTF-8");
        MessageDigest sha = MessageDigest.getInstance("SHA-1");
        keyBytes = sha.digest(keyBytes);
        keyBytes = Arrays.copyOf(keyBytes, 16); // use only first 128 bit
        SecretKeySpec secretKeySpec = new SecretKeySpec(keyBytes, "AES");
        cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec);

        byte[] recoveredBytes =
                cipher.doFinal(message.getBytes());
        return new String(recoveredBytes, "UTF-8");
    }

    /**
     * Gets sender id.
     *
     * @return the sender id
     */
    public String getSenderID() {
        return senderID;
    }

    /**
     * Sets sender id.
     *
     * @param senderID the sender id
     */
    public void setSenderID(String senderID) {
        this.senderID = senderID;
    }

    /**
     * Gets receiver id.
     *
     * @return the receiver id
     */
    public String getReceiverID() {
        return receiverID;
    }

    /**
     * Sets receiver id.
     *
     * @param receiverID the receiver id
     */
    public void setReceiverID(String receiverID) {
        this.receiverID = receiverID;
    }

    /**
     * Gets content.
     *
     * @return the content
     */
    public String getContent() {
        return content;
    }

    /**
     * Sets content.
     *
     * @param content the content
     */
    public void setContent(String content) {
        this.content = content;
    }

    /**
     * Gets id.
     *
     * @return the id
     */
    public long getId() {
        return id;
    }

    /**
     * Sets id.
     *
     * @param id the id
     */
    public void setId(long id) {
        this.id = id;
    }

    /**
     * Is encrypted boolean.
     *
     * @return the boolean
     */
    public boolean isEncrypted() {
        return isEncrypted;
    }

    /**
     * Sets encrypted.
     *
     * @param encrypted the encrypted
     */
    public void setEncrypted(boolean encrypted) {
        isEncrypted = encrypted;
    }

    /**
     * IsMine boolean.
     *
     * @return the boolean
     */
    public boolean isMine() {
        return isMine;
    }

    /**
     * Sets isMine.
     *
     * @param isMine the isMine
     */
    public void setIsMine(boolean isMine) {
        this.isMine = isMine;
    }

}
