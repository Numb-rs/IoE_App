package internetofeveryone.ioe.Data;

public class Message {

    private String senderID;
    private String receiverID;
    private String message;
    private int id;
    private boolean isEncrypted;
    public static int counter = 0;

    public Message(String senderID, String receiverID, String message, boolean isEncrypted) {
        this.senderID = senderID;
        this.receiverID = receiverID;
        this.message = message;
        this.id = counter;
        this.isEncrypted = isEncrypted;
        counter++;
    }

    public static String encrypt(String message, String key) {
        return message + " (encrypted)"; // add logic
    }

    public static String decrypt(String message, String key) {
        return message; // add logic
    }

    public String getSenderID() {
        return senderID;
    }

    public void setSenderID(String senderID) {
        this.senderID = senderID;
    }

    public String getReceiverID() {
        return receiverID;
    }

    public void setReceiverID(String receiverID) {
        this.receiverID = receiverID;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isEncrypted() {
        return isEncrypted;
    }

    public void setEncrypted(boolean encrypted) {
        isEncrypted = encrypted;
    }

}
