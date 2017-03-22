package internetofeveryone.ioe.Contact;

import android.app.Dialog;

import internetofeveryone.ioe.View.MvpView;

/**
 * Created by Fabian Martin for 'Internet of Everyone'
 *
 * This interface represents the view for the Contact fragment
 */
interface ContactView extends MvpView {

    /**
     * Opens a new AlertDialog that allows users to add a new Contact
     *
     * @return AlertDialog
     */
    Dialog onAddContact();

    /**
     * Opens a new AlertDialog that allows users to edit a Contact
     *
     * @param pos position of the Contact that has been selected
     * @return AlertDialog
     */
    Dialog onEditContact(int pos);

}
