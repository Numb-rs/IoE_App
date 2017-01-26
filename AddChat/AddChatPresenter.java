package internetofeveryone.ioe.AddChat;

import android.content.Context;

import java.util.ArrayList;
import java.util.Collection;

import internetofeveryone.ioe.Data.Chat;
import internetofeveryone.ioe.Data.Contact;
import internetofeveryone.ioe.Data.DataType;
import internetofeveryone.ioe.Presenter.MvpPresenter;

/**
 * Created by Fabian Martin for 'Internet of Everyone'
 *
 * This class handles the logic and represents the implementation of presenter for the AddChat fragment
 */
public class AddChatPresenter extends MvpPresenter<AddChatView> {

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
    public void update(DataType type, String id) {
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
        contactList = getModel().getContactList().values();
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
        for (Contact c : contactList) {
            if (c.getName().equals(name)) {
                ArrayList<String> msgList = new ArrayList<>();
                getModel().addChat(new Chat(c, msgList));
            }
        }
    }

}
