package internetofeveryone.ioe.Data;

/**
 * Created by Fabian Martin for 'Internet of Everyone'
 *
 * This class represents the custom data type Message
 */
public class Message {

    private long senderID; // user code of the sender
    private long receiverID; // user code of the receiver
    private String content;
    private long id; // unique identifier the message
    private boolean isEncrypted; // flag that stores if the message is encrypted

    /**
     * Instantiates a new Message.
     *
     * @param senderID
     * @param receiverID
     * @param content
     * @param isEncrypted
     */
    public Message(long id, long senderID, long receiverID, String content, boolean isEncrypted) {
        this.id = id;
        this.senderID = senderID;
        this.receiverID = receiverID;
        this.content = content;
        this.isEncrypted = isEncrypted;
    }

    /**
     * Encrypts a message.
     *
     * @param message the decrypted message
     * @param key     the key
     * @return the encrypted message
     */
    public static String encrypt(String message, String key) {
        return message + " (encrypted with " + key + ")"; // TODO: add logic
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
    public long getSenderID() {
        return senderID;
    }

    /**
     * Sets sender id.
     *
     * @param senderID the sender id
     */
    public void setSenderID(long senderID) {
        this.senderID = senderID;
    }

    /**
     * Gets receiver id.
     *
     * @return the receiver id
     */
    public long getReceiverID() {
        return receiverID;
    }

    /**
     * Sets receiver id.
     *
     * @param receiverID the receiver id
     */
    public void setReceiverID(long receiverID) {
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

}
