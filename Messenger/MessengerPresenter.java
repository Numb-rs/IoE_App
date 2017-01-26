package internetofeveryone.ioe.Messenger;

import android.content.Context;

import java.util.HashMap;

import internetofeveryone.ioe.Data.Chat;
import internetofeveryone.ioe.Data.DataType;
import internetofeveryone.ioe.Presenter.MvpPresenter;

/**
 * Created by Fabian Martin for 'Internet of Everyone'
 *
 * This class handles the logic and represents the implementation of presenter for the Messenger page
 */
public class MessengerPresenter extends MvpPresenter<MessengerView> {

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
    public void onSwipeRight(Chat chat) {
        if(isViewAttached()) {
            chat.getContact().setOpenChat(false);
            getModel().removeChat(chat);
        }
    }

    /**
     * Gets the list of all chats from the Model
     *
     * @return the chat list
     */
    public HashMap<String, Chat> getChatList() {
        return getModel().getChatList();
    }


}
