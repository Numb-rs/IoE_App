package internetofeveryone.ioe.Chat;

import android.content.Context;

import java.util.TreeMap;

import internetofeveryone.ioe.Data.Chat;
import internetofeveryone.ioe.Data.Contact;
import internetofeveryone.ioe.Data.DataType;
import internetofeveryone.ioe.Data.Message;
import internetofeveryone.ioe.Presenter.MessagingPresenter;

/**
 * Created by Fabian Martin for 'Internet of Everyone'
 *
 * This class handles the logic and represents the implementation of presenter for the Contact fragment
 */
public class ChatPresenter extends MessagingPresenter<ChatView> {

    private Contact contact;
    private Chat chat;

    /**
     * Instantiates a new ChatPresenter.
     *
     * @param context
     */
    public ChatPresenter(Context context) {
        super(context);
    }

    @Override
    public void update(DataType type, String id) {
        if (type.equals(DataType.MESSAGE)) {
            if (isViewAttached()) {
                getView().dataChanged();
            }
        }
    }

    /**
     * Gets the contact for a given user code from the Model .
     *
     * @param userCode the user code
     */
    public void getContact(long userCode) {
        contact = getModel().getContactByID(userCode);
        TreeMap<Long, Message> msgList = getModel().getAllMessagesByContact(userCode); // all Messages for this Chat
        chat = new Chat(contact, msgList);
    }

    /**
     * Changes the encryption state
     *
     * @param isChecked true if encryption is currently activated, false if not
     */
    public void encryptChanged(boolean isChecked) {
        chat.setEncryption(isChecked);
    }

    /**
     * Gets encryption state from the chat
     *
     * @return true if encryption is currently activated, false if not
     */
    public boolean isChatEncrypted(String contactName) {
        return chat.isEncrypted();
    }

    /**
     * Adds a new Message
     *
     * @param msgPassed the message
     */
    public void sendMessage(String msgPassed) {
        String content = msgPassed;
        boolean encrypt = chat.isEncrypted();
        if (encrypt) {
            content = Message.encrypt(msgPassed, contact.getKey());
        }
        getModel().addMessage(getModel().getUserCode(), contact.getUserCode(), content, encrypt);
    }

    /**
     * Gets all messages from the Model
     *
     * @return array of all messages
     */
    public String[] getMessageList() {
        TreeMap<Long, Message> messages = chat.getMessageList();
        String[] result = new String[messages.size()];
        for (int i = 0; i < messages.size(); i++) {
            result[i] = messages.get(i).getContent();
        }
        return result;
    }

}
