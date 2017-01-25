package internetofeveryone.ioe.Data;

import java.util.ArrayList;

public class Chat {

    private Contact contact;
    private ArrayList<String> messageList;
    private boolean encryption;

    public Chat(Contact contact, ArrayList<String> messageList) {
        this.contact = contact;
        this.messageList = messageList;
        contact.setOpenChat(true);
    }

    public Contact getContact() {
        return contact;
    }

    public void setContact(Contact contact) {
        this.contact = contact;
    }

    public ArrayList<String> getMessageList() {
        return messageList;
    }

    public void setMessageList(ArrayList<String> messageList) {
        this.messageList = messageList;
    }

    public void setEncryption(boolean encryptionStatus) {
        encryption = encryptionStatus;
    }

    public boolean isEncrypted() {
        return encryption;
    }
}