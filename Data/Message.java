package internetofeveryone.ioe.Data;

/**
 * Created by Fabian Martin for 'Internet of Everyone'
 *
 * This class represents the custom data type Message
 */
public class Message {

    private String senderID; // user code of the sender
    private String receiverID; // user code of the receiver
    private String message;
    private int id; // unique identifier the message
    private boolean isEncrypted; // flag that stores if the message is encrypted
    /**
     * The constant counter that is being incremented for every new Message
     */
    public static int counter = 0;

    /**
     * Instantiates a new Message.
     *
     * @param senderID
     * @param receiverID
     * @param message
     * @param isEncrypted
     */
    public Message(String senderID, String receiverID, String message, boolean isEncrypted) {
        this.senderID = senderID;
        this.receiverID = receiverID;
        this.message = message;
        this.id = counter;
        this.isEncrypted = isEncrypted;
        counter++;
    }

    /**
     * Encrypts a message.
     *
     * @param message the decrypted message
     * @param key     the key
     * @return the encrypted message
     */
    public static String encrypt(String message, String key) {
        return message + " (encrypted)"; // TODO: add logic
    }

    /**
     * Decrypts a message.
     *
     * @param message the encrypted message
     * @param key     the key
     * @return the decrypted message
     */
    public static String decrypt(String message, String key) {
        return message; // TODO: add logic
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
     * Gets message.
     *
     * @return the message
     */
    public String getMessage() {
        return message;
    }

    /**
     * Sets message.
     *
     * @param message the message
     */
    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * Gets id.
     *
     * @return the id
     */
    public int getId() {
        return id;
    }

    /**
     * Sets id.
     *
     * @param id the id
     */
    public void setId(int id) {
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

}
