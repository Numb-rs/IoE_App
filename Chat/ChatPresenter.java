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
    public Contact getContact(long userCode) {
        return getModel().getContactByID(userCode);
    }

    /**
     * Changes the encryption state
     *
     * @param isChecked true if encryption is currently activated, false if not
     */
    public void encryptChanged(long userCode, boolean isChecked) {
        getModel().updateChat(userCode, isChecked);
    }

    /**
     * Gets encryption state from the chat
     *
     * @return true if encryption is currently activated, false if not
     */
    public boolean isChatEncrypted(long userCode) {
        return getModel().getChatByID(userCode).isEncrypted();
    }

    /**
     * Adds a new Message
     *
     * @param msgPassed the message
     */
    public void sendMessage(long userCode, String msgPassed) {
        String content = msgPassed;
        boolean encrypt = getModel().getChatByID(userCode).isEncrypted();
        if (encrypt) {
            content = Message.encrypt(msgPassed, getModel().getContactByID(userCode).getKey());
        }
        getModel().addMessage(getModel().getUserCode(), getModel().getContactByID(userCode).getUserCode(), content, encrypt);
    }

    /**
     * Gets all messages from the Model
     *
     * @return TreeMap of all messages
     */
    public TreeMap<Long, Message> getMessageList(long userCode) {
        return getModel().getChatByID(userCode).getMessageList();
    }

    /**
     * Gets name of contact from the Model
     *
     * @return name
     */
    public String getContactName(long userCode) {
        return getModel().getContactByID(userCode).getName();
    }

}
