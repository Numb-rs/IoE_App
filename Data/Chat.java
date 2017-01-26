package internetofeveryone.ioe.Data;

import java.util.ArrayList;

/**
 * Created by Fabian Martin for 'Internet of Everyone'
 *
 * This class represents the custom data type Chat
 */
public class Chat {

    private Contact contact; // the contact that receives all the messages that are send here
    private ArrayList<String> messageList; // list of all messages in the chat
    private boolean encryption; // flag that stores if encryption is currently active

    /**
     * Instantiates a new Chat.
     *
     * @param contact
     * @param messageList
     */
    public Chat(Contact contact, ArrayList<String> messageList) {
        this.contact = contact;
        this.messageList = messageList;
        contact.setOpenChat(true);
    }

    /**
     * Gets contact.
     *
     * @return the contact
     */
    public Contact getContact() {
        return contact;
    }

    /**
     * Sets contact.
     *
     * @param contact the contact
     */
    public void setContact(Contact contact) {
        this.contact = contact;
    }

    /**
     * Gets message list.
     *
     * @return the message list
     */
    public ArrayList<String> getMessageList() {
        return messageList;
    }

    /**
     * Sets message list.
     *
     * @param messageList the message list
     */
    public void setMessageList(ArrayList<String> messageList) {
        this.messageList = messageList;
    }

    /**
     * Sets encryption.
     *
     * @param encryptionStatus the encryption status
     */
    public void setEncryption(boolean encryptionStatus) {
        encryption = encryptionStatus;
    }

    /**
     * Is encrypted boolean.
     *
     * @return the boolean
     */
    public boolean isEncrypted() {
        return encryption;
    }
}