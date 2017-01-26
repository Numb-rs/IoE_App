package internetofeveryone.ioe.Chat;

import android.content.Context;

import java.util.Arrays;

import internetofeveryone.ioe.Data.Chat;
import internetofeveryone.ioe.Data.Contact;
import internetofeveryone.ioe.Data.DataType;
import internetofeveryone.ioe.Data.Message;
import internetofeveryone.ioe.Presenter.MvpPresenter;

/**
 * Created by Fabian Martin for 'Internet of Everyone'
 *
 * This class handles the logic and represents the implementation of presenter for the Contact fragment
 */
public class ChatPresenter extends MvpPresenter<ChatView> {

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
        contact = getModel().getContact(userCode);
        chat = getModel().getChat(contact.getName());
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
     * Adds a new Message
     *
     * @param msgPassed the message
     */
    public void sendMessage(String msgPassed) {
        String content = msgPassed;
        boolean encrypt = false;
        if (chat.isEncrypted()) {
            encrypt = true;
            content = Message.encrypt(msgPassed, contact.getKey());
        }
        Message message = new Message("0", "1", content, encrypt);
        getModel().addMessage(message);
    }

    /**
     * Gets all messages from the Model
     *
     * @return array of all messages
     */
    public String[] getMessageList() {
        Object[] objects = getModel().getMessageList().values().toArray();
        Message[] messages = Arrays.copyOf(objects, objects.length, Message[].class);
        String[] result = new String[messages.length];
        for (int i = 0; i < messages.length; i++) {
            result[i] = messages[i].getMessage();
        }
        return result;
    }

}
