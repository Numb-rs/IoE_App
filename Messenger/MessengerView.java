package internetofeveryone.ioe.Messenger;

import internetofeveryone.ioe.Data.Contact;
import internetofeveryone.ioe.View.MvpView;

public interface MessengerView extends MvpView {

    public void openChat(Contact contact);
    public void dataChanged();

}
