package internetofeveryone.ioe.AddChat;

import android.content.Context;

import java.util.ArrayList;
import java.util.Collection;

import internetofeveryone.ioe.Data.Contact;
import internetofeveryone.ioe.Data.DataType;
import internetofeveryone.ioe.Presenter.MessagingPresenter;

/**
 * Created by Fabian Martin for 'Internet of Everyone'
 *
 * This class handles the logic and represents the implementation of presenter for the AddChat fragment
 */
public class AddChatPresenter extends MessagingPresenter<AddChatView> {

    private Collection<Contact> contactList; // list of all Contacts

    /**
     * Instantiates a new AddChatPresenter.
     *
     * @param context
     */
    public AddChatPresenter(Context context) {
        super(context);
    }

    @Override
    public void update(DataType type) {
        if (type.equals(DataType.CONTACT)) {
            if (isViewAttached()) {
                getView().dataChanged();
            } else {
                // ErrorHandling
            }
        }
    }

    /**
     * Gets the names of all contacts from the Model
     *
     * @return list of all names
     */
    public ArrayList<String> getContactNames() {
        contactList = getModel().getAllContacts();
        ArrayList<String> contactNames = new ArrayList<>();
        for (Contact c : contactList) {
            if (!c.hasOpenChat()) {
                contactNames.add(c.getName());
            }
        }
        return contactNames;
    }

    /**
     * Adds a Chat
     *
     * @param name name of the contact
     */
    public void addChat(String name) {
        contactList = getModel().getAllContacts();
        for (Contact c : contactList) {
            if (c.getName().equals(name)) {
                getModel().updateContact(c.getUserCode(), c.getName(), c.getUserCode(), c.getKey(), true);
                getModel().addChat(c.getUserCode(), false);
            }
        }
    }

}
