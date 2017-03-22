package internetofeveryone.ioe.Contact;

import android.content.Context;

import java.util.Arrays;

import internetofeveryone.ioe.Data.Contact;
import internetofeveryone.ioe.Data.DataType;
import internetofeveryone.ioe.Presenter.MessagingPresenter;

/**
 * Created by Fabian Martin for 'Internet of Everyone'
 *
 * This class handles the logic and represents the implementation of presenter for the Contact fragment
 */
public class ContactPresenter extends MessagingPresenter<ContactView> {

    /**
     * Instantiates a new ContactPresenter.
     *
     * @param context the context
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
        Object[] objects = getModel().getAllContacts().toArray();
        Contact[] contacts = Arrays.copyOf(objects, objects.length, Contact[].class);
        String[] result = new String[contacts.length];
        for (int i = 0; i < contacts.length; i++) {
            result[i] = contacts[i].getName();
        }
        return result;
    }

    /**
     * Get the user codes of all contacts from the Model
     *
     * @return array of all the user codes
     */
    public String[] getContactUserCodes() {
        Object[] objects = getModel().getAllContacts().toArray();
        Contact[] contacts = Arrays.copyOf(objects, objects.length, Contact[].class);
        String[] result = new String[contacts.length];
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
        Object[] objects = getModel().getAllContacts().toArray();
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
    public void onClickSaveChange(String originalContactUserCode, String currentContactUserCode, String currentContactName, String currentContactKey) {
        boolean hasOpenChat = getModel().getContactByID(originalContactUserCode).hasOpenChat();
        getModel().updateContact(originalContactUserCode, currentContactName, currentContactUserCode, currentContactKey, hasOpenChat);
    }

    /**
     * Adds a new Contact.
     *
     * @param name     name
     * @param userCode user code
     * @param key      key
     */
    public void addContact(String name, String userCode, String key) {
        getModel().addContact(name, userCode, key, false);
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
            attachView(new ContactFragment());
            onClickContact(pos);
        }
    }

    /**
     * Sends a request to the view to open a new Dialog where users can add a new Contact
     */
    public void onClickAdd() {
        if(isViewAttached()) {
            getView().onAddContact();
        } else {
            attachView(new ContactFragment());
            onClickAdd();
        }
    }

    /**
     * May add sth here in later versions
     */
    public void onClickExit() {

    }

    /**
     * May add sth here in later versions
     */
    public void onClickCancel() {

    }

    /**
     * Deletes a Contact
     *
     * @param userCode user code for the contact that is to be removed
     */
    public void onClickDelete(String userCode) {
        getModel().deleteContact(userCode);
    }

    @Override
    public void update(DataType type) {

        if (type.equals(DataType.CONTACT)) {
            if (isViewAttached()) {
                getView().dataChanged();
            }
        }
    }

}
