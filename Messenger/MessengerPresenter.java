package internetofeveryone.ioe.Messenger;

import android.content.Context;

import java.util.HashMap;
import java.util.List;
import java.util.TreeMap;

import internetofeveryone.ioe.Data.Chat;
import internetofeveryone.ioe.Data.DataType;
import internetofeveryone.ioe.Data.Message;
import internetofeveryone.ioe.Presenter.MessagingPresenter;

/**
 * Created by Fabian Martin for 'Internet of Everyone'
 *
 * This class handles the logic and represents the implementation of presenter for the Messenger page
 */
public class MessengerPresenter extends MessagingPresenter<MessengerView> {

    public static final String TAG = "MessengerPresenter";

    /**
     * Instantiates a new MessengerPresenter.
     *
     * @param context the context
     */
    public MessengerPresenter(Context context) {
        super(context);
    }

    @Override
    public void update(DataType type) {

        if (type.equals(DataType.CHAT) || type.equals(DataType.MESSAGE)) {
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
            attachView(new MessengerActivity());
            onChatClicked(chat);
        }
    }

    /**
     * Sends a request to the model to delete a Chat
     *
     * @param chat the chat
     */
    public void onChatDeleteClicked(Chat chat) {
        if(isViewAttached()) {
           getModel().deleteChat(chat.getContact().getUserCode());
        } else {
            attachView(new MessengerActivity());
            onChatDeleteClicked(chat);
        }
    }

    /**
     * Gets the list of all chats from the Model
     *
     * @return the chat list
     */
    public HashMap<String, Chat> getChatList() {
        HashMap<String, Chat> chatList = new HashMap<>();
        List<Chat> chats = getModel().getAllChats();
        for (Chat c : chats) {
            TreeMap<Long, Message> msgList = (getModel().getAllMessagesByContact(c.getContact().getUserCode()));
            c.setMessageList(msgList);
            chatList.put(c.getContact().getName(), c);
        }
        return chatList;
    }

}
