package internetofeveryone.ioe.Contact;

import android.content.Context;

import java.util.Arrays;

import internetofeveryone.ioe.Data.Contact;
import internetofeveryone.ioe.Data.DataType;
import internetofeveryone.ioe.Presenter.MvpPresenter;

/**
 * Created by Fabian Martin for 'Internet of Everyone'
 *
 * This class handles the logic and represents the implementation of presenter for the Contact fragment
 */
public class ContactPresenter extends MvpPresenter<ContactView> {

    /**
     * Instantiates a new ContactPresenter.
     *
     * @param context
     */
    public ContactPresenter(Context context) {
        super(context);
    }

    /**
     * Get the names of all contacts from the Model
     *
     * @return array of all the names
     */
    public String[] getContactNames() {
        Object[] objects = getModel().getContactList().values().toArray();
        Contact[] contacts = Arrays.copyOf(objects, objects.length, Contact[].class);
        String[] result = new String[contacts.length];
        for (int i = 0; i < contacts.length; i++) {
            result[i] = contacts[i].getName();
        }
        return result;
    };

    /**
     * Get the user codes of all contacts from the Model
     *
     * @return array of all the user codes
     */
    public Long[] getContactUserCodes(){
        Object[] objects = getModel().getContactList().values().toArray();
        Contact[] contacts = Arrays.copyOf(objects, objects.length, Contact[].class);
        Long[] result = new Long[contacts.length];
        for (int i = 0; i < contacts.length; i++) {
            result[i] = contacts[i].getUserCode();
        }
        return result;
    }

    /**
     * Get the keys of all contacts from the Model
     *
     * @return array of all the keys
     */
    public String[] getContactKeys() {
        Object[] objects = getModel().getContactList().values().toArray();
        Contact[] contacts = Arrays.copyOf(objects, objects.length, Contact[].class);
        String[] result = new String[contacts.length];
        for (int i = 0; i < contacts.length; i++) {
            result[i] = contacts[i].getKey();
        }
        return result;
    }

    /**
     * Saves the changes for the Contact
     *
     * @param originalContactUserCode old user code
     * @param currentContactUserCode  changed user code
     * @param currentContactName      changed name
     * @param currentContactKey       changed key
     */
    public void onClickSaveChange(long originalContactUserCode, long currentContactUserCode, String currentContactName, String currentContactKey) {
        getModel().removeContact(originalContactUserCode);
        Contact contact = new Contact(currentContactName, currentContactUserCode, currentContactKey);
        getModel().addContact(contact);
    }

    /**
     * Adds a new Contact.
     *
     * @param name     name
     * @param userCode user code
     * @param key      key
     */
    public void addContact(String name, long userCode, String key) {
        Contact contact = new Contact(name, userCode, key);
        getModel().addContact(contact);
    }

    public void onClickCancel() {
        // may add sth here later
    }

    /**
     * Sends a request to the view to open a new Dialog where users can edit the Contact
     *
     * @param pos position of the Contact that the user wants to edit
     */
    public void onClickContact(int pos) {
        if(isViewAttached()) {
            getView().onEditContact(pos);
        } else {
            // ErrorHandling
        }
    }

    /**
     * Sends a request to the view to open a new Dialog where users can add a new Contact
     */
    public void onClickAdd() {
        if(isViewAttached()) {
            getView().onAddContact();
        } else {
            // ErrorHandling
        }
    }

    /**
     * Deletes a Contact
     *
     * @param userCode user code for the contact that is to be removed
     */
    public void onClickDelete(long userCode) {
        getModel().removeContact(userCode);
    }

    public void onClickExit() {
        // may add sth here later
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

}
