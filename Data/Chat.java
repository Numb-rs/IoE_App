package internetofeveryone.ioe.Data;

import java.util.TreeMap;

/**
 * Created by Fabian Martin for 'Internet of Everyone'
 *
 * This class represents the custom data type Chat
 */
public class Chat {

    private Contact contact; // the contact that receives all the messages that are send here
    private TreeMap<Long, Message> messageList; // list of all messages in the chat
    private boolean encryption; // flag that stores if encryption is currently active

    /**
     * Instantiates a new Chat.
     *
     * @param contact contact
     * @param messageList past messages for chat
     */
    public Chat(Contact contact, TreeMap<Long, Message> messageList, boolean encryption) {
        this.contact = contact;
        this.messageList = messageList;
        this.encryption = encryption;
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
    public TreeMap<Long, Message> getMessageList() {
        return messageList;
    }

    /**
     * Sets message list.
     *
     * @param messageList the message list
     */
    public void setMessageList(TreeMap<Long, Message> messageList) {
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

    public Message getLastMessage() {
        if (messageList.size() <= 0) {
            return null;
        } else {
            return messageList.lastEntry().getValue();
        }
    }
}