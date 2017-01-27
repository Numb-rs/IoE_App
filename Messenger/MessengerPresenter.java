package internetofeveryone.ioe.Messenger;

import android.content.Context;

import java.util.HashMap;
import java.util.List;
import java.util.TreeMap;

import internetofeveryone.ioe.Data.Chat;
import internetofeveryone.ioe.Data.Contact;
import internetofeveryone.ioe.Data.DataType;
import internetofeveryone.ioe.Data.Message;
import internetofeveryone.ioe.Presenter.MessagingPresenter;

/**
 * Created by Fabian Martin for 'Internet of Everyone'
 *
 * This class handles the logic and represents the implementation of presenter for the Messenger page
 */
public class MessengerPresenter extends MessagingPresenter<MessengerView> {

    /**
     * Instantiates a new MessengerPresenter.
     *
     * @param context
     */
    public MessengerPresenter(Context context) {
        super(context);
    }

    @Override
    public void update(DataType type, String id) {
        if (type.equals(DataType.CHAT)) {
            if (isViewAttached()) {
                getView().dataChanged();
            }
        }
    }

    /**
     * Sends a request to the view to open a Chat
     *
     * @param chat the chat
     */
    public void onChatClicked(Chat chat) {
        if(isViewAttached()) {
            getView().openChat(chat.getContact());
        } else {
            // ErrorHandling
        }
    }

    /**
     * Sends a request to the view to remove a chat
     *
     * @param chat the chat
     */
    /*
    public void onSwipeRight(Chat chat) {
        if(isViewAttached()) {
            chat.getContact().setOpenChat(false);
            getModel().deleteChat(chat);
        }
    }
    */

    /**
     * Gets the list of all chats from the Model
     *
     * @return the chat list
     */
    public HashMap<String, Chat> getChatList() {
        HashMap<String, Chat> chatList = new HashMap<>();
        List<Contact> contacts = getModel().getAllContacts();
        for (Contact c : contacts) {
            TreeMap<Long, Message> msgList = (getModel().getAllMessagesByContact(c.getUserCode())); // all Messages for this Chat
            chatList.put(c.getName(), new Chat(c, msgList)); // insert this chat into chatList
        }
        return chatList;
    }


}
