package internetofeveryone.ioe.Messenger;

import internetofeveryone.ioe.Data.Contact;
import internetofeveryone.ioe.View.MvpView;

/**
 * Created by Fabian Martin for 'Internet of Everyone'
 *
 * This interface represents the view for the Messenger page
 */
interface MessengerView extends MvpView {

    /**
     * Opens the chat for the given contact
     *
     * @param contact the contact
     */
    void openChat(Contact contact);

    void displayNetworkErrorMessage();

    void closeLoader();

}
