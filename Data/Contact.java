package internetofeveryone.ioe.Data;

/**
 * Created by Fabian Martin for 'Internet of Everyone'
 *
 * This class represents the custom data type Contact
 */
public class Contact {

    private String name; // name of the contact
    private String userCode; // user code of the contact as unique identifier
    private String key; // key for encryption
    private boolean openChat; // flag that stores if there's currently an open chat with this contact

    /**
     * Instantiates a new Contact.
     *
     * @param name
     * @param userCode
     * @param key
     */
    public Contact(String name, String userCode, String key, boolean openChat) {
        this.name = name;
        this.userCode = userCode;
        this.key = key;
        this.openChat = openChat;
    }

    /**
     * Gets name.
     *
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets name.
     *
     * @param name the name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets user code.
     *
     * @return the user code
     */
    public String getUserCode() {
        return userCode;
    }

    /**
     * Set user code.
     *
     * @param userCode the user code
     */
    public void setUserCode(String userCode) {
        this.userCode = userCode;
    }

    /**
     * Gets key.
     *
     * @return the key
     */
    public String getKey() {
        return key;
    }

    /**
     * Sets key.
     *
     * @param key the key
     */
    public void setKey(String key) {
        this.key = key;
    }

    /**
     * Has open chat boolean.
     *
     * @return the boolean
     */
    public boolean hasOpenChat() {
        return openChat;
    }

    /**
     * Sets open chat.
     *
     * @param openChat the open chat
     */
    public void setOpenChat(boolean openChat) {
        this.openChat = openChat;
    }
}
