package internetofeveryone.ioe.Data;

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
    private boolean isMine; // flag that stores if the message has been sent by my (it's senderID is equal to my userCode)

    /**
     * Instantiates a new Message.
     *
     * @param senderID ID of the sender
     * @param receiverID ID of the receiver
     * @param content message content
     * @param isEncrypted flag if the message is curently ecnrypted or not (true if encrypted)
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
     * @param original the decrypted message
     * @param key     the key
     * @return the encrypted message
     */
    public static String encrypt(String original, String key) {
        System.out.println("Message before encryption: " + original);
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < original.length(); i++) {
            char c = original.charAt(i);
            if (c >= 32) {
                int keyCharValue = key.charAt(i % key.length()) - 'A';
                c += keyCharValue;
                if (c > 127) {
                    c = (char) (c + 32 - 127);
                }
            }
            sb.append(c);
        }
        System.out.println("Message after encryption: " + sb.toString());
        return sb.toString();
    }

    /**
     * Decrypts a message.
     *
     * @param original the encrypted message
     * @param key     the key
     * @return the decrypted message
     */
    public static String decrypt(String original, String key) {
        System.out.println("Message before decryption: " + original);
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < original.length(); i++) {
            char c = original.charAt(i);
            if (c >= 32) {
                int keyCharValue = key.charAt(i % key.length()) - 'A';
                c -= keyCharValue;
                if (c < 32) {
                    c = (char) (c + 127 - 32);
                }
            }
            sb.append(c);
        }
        System.out.println("Message after decryption: " + sb.toString());
        return sb.toString();

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
