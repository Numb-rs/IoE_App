package internetofeveryone.ioe.Messenger;

import internetofeveryone.ioe.Data.Contact;
import internetofeveryone.ioe.View.MvpView;

/**
 * Created by Fabian Martin for 'Internet of Everyone'
 *
 * This interface represents the view for the Messenger page
 */
public interface MessengerView extends MvpView {

    /**
     * Opens the chat for the given contact
     *
     * @param contact the contact
     */
    public void openChat(Contact contact);

}
