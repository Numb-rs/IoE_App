package internetofeveryone.ioe.Messenger;

import android.content.Context;

import java.util.HashMap;

import internetofeveryone.ioe.Data.Chat;
import internetofeveryone.ioe.Data.DataType;
import internetofeveryone.ioe.Presenter.MvpPresenter;

public class MessengerPresenter extends MvpPresenter<MessengerView> {

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

    public void onChatClicked(Chat chat) {
        if(isViewAttached()) {
            getView().openChat(chat.getContact());
        }
    }
    /*
    public void onSwipeRight(Chat chat) {
        if(isViewAttached()) {
            chat.getContact().setOpenChat(false);
            getModel().removeChat(chat);
        }
    }
    */
    public HashMap<String, Chat> getChatList() {
        return getModel().getChatList();
    }


}
