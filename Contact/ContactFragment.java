package internetofeveryone.ioe.Contact;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import icepick.Icepick;
import internetofeveryone.ioe.Presenter.PresenterLoader;
import internetofeveryone.ioe.R;

/**
 * Created by Fabian Martin for 'Internet of Everyone'
 *
 * This class is responsible for user-interaction and represents the implementation of view for the Contact fragment
 */
public class ContactFragment extends android.support.v4.app.DialogFragment implements ContactView, LoaderManager.LoaderCallbacks<ContactPresenter> {

    private ContactPresenter presenter;
    private View view;
    private ListView listView; // list of all Contact names
    private String[] contactNames; // names of all contacts
    private Long[] contactUserCodes; // userCodes of all contacts
    private String[] contactKeys; // keys of all contacts
    /**
     * Adapter for the ListView
     * It's responsible for displaying data in the list
     */
    private ArrayAdapter<String> adapter;
    private String originalContactName; // name of the contact before editing
    private long originalContactUserCode; // user code of the contact before editing
    private String originalContactKey; // key of the contact before editing
    private String currentContactName = ""; // changed name of the contact
    private long currentContactUserCode; // changed user code of the contact
    private String currentContactKey = ""; // changed key of the contact
    private AlertDialog.Builder builder;
    private static final int LOADER_ID = 106; // unique identification for the ContactFragment-LoaderManager


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        Icepick.restoreInstanceState(this, savedInstanceState); // restore instance state

        builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        builder.setTitle(R.string.edit_contacts_title);
        view = inflater.inflate(R.layout.fragment_contact, null);
        builder.setView(view);

        // add action buttons
        builder.setPositiveButton(R.string.button_add, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                presenter.onClickAdd(); // notifies presenter to add a new DefaultWebsite
            }
        });
        builder.setNegativeButton(R.string.exit, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                presenter.onClickExit(); // notifies presenter to exit the AlertDialog
            }
        });

        final AlertDialog b = builder.create();
        b.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {

                Button button = ((AlertDialog) dialog).getButton(android.app.AlertDialog.BUTTON_POSITIVE);
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        presenter.onClickAdd();
                    }
                });
            }
        });
        return b;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {

        super.onActivityCreated(savedInstanceState);
        getActivity().getSupportLoaderManager().initLoader(LOADER_ID, null, this); // initialises the LoaderManager
    }

    /**
     * Opens a new AlertDialog that allows users to add a new Contact
     *
     * @return AlertDialog
     */
    public Dialog onAddContact() {

        AlertDialog.Builder builder2 = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        builder2.setTitle(R.string.add_contact_title);
        view = inflater.inflate(R.layout.fragment_add_contact, null);
        builder2.setView(view);

        // add action buttons
        builder2.setPositiveButton(R.string.save, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {

                String name = ((EditText) view.findViewById(R.id.editText_name)).getText().toString();
                long userCode = Long.valueOf(((EditText) view.findViewById(R.id.editText_phone_number)).getText().toString());
                String key = ((EditText) view.findViewById(R.id.editText_key)).getText().toString();
                presenter.addContact(name, userCode, key); // notifies the presenter that the user wants to add a new Contact
            }
        });
        builder2.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                presenter.onClickCancel(); // notifies the presenter that the user wants to cancel
            }
        });
        Dialog d = builder2.create();
        d.show();
        return d;
    }

    /**
     * Opens a new AlertDialog that allows users to edit a Contact
     *
     * @param pos position of the Contact that has been selected in the ListView
     * @return AlertDialog
     */
    public Dialog onEditContact(int pos) {

        AlertDialog.Builder builder3 = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        builder3.setTitle(R.string.edit_contact_title);
        view = inflater.inflate(R.layout.fragment_edit_contact, null);
        builder3.setView(view);

        final EditText currentNameEditText = (EditText) view.findViewById(R.id.editText_current_contact_name);
        final EditText currentUserCodeEditText = (EditText) view.findViewById(R.id.editText_current_contact_usercode);
        final EditText currentKeyEditText = (EditText) view.findViewById(R.id.editText_current_contact_key);

        contactUserCodes = presenter.getContactUserCodes();
        contactKeys = presenter.getContactKeys();

        originalContactName = contactNames[pos];
        originalContactUserCode = contactUserCodes[pos];
        originalContactKey = contactKeys[pos];
        currentNameEditText.setText(originalContactName);
        currentUserCodeEditText.setText("" + originalContactUserCode);
        currentKeyEditText.setText(originalContactKey);

        // add action buttons
        builder3.setPositiveButton(R.string.save, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                currentContactName = currentNameEditText.getText().toString();
                currentContactKey = currentKeyEditText.getText().toString();
                currentContactUserCode = Long.valueOf(currentUserCodeEditText.getText().toString());
                // notifies the presenter that the user wants to save the changes
                presenter.onClickSaveChange(originalContactUserCode, currentContactUserCode, currentContactName, currentContactKey);
            }
        });
        builder3.setNeutralButton(R.string.button_delete, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                presenter.onClickDelete(originalContactUserCode); // notifies the presenter that the user wants to delete the Contact
            }
        });
        builder3.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                presenter.onClickCancel(); // notifies the presenter that the user wants to cancel
            }
        });
        Dialog d = builder3.create();
        d.show();
        return d;
    }

    @Override
    public void onStart() {
        super.onStart();
        if (presenter == null) {
            getActivity().getSupportLoaderManager().initLoader(LOADER_ID, null, this); // temporary workaround
        }
        presenter.attachView(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        presenter.detachView();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override public void onSaveInstanceState(Bundle outState) {

        super.onSaveInstanceState(outState);
        Icepick.saveInstanceState(this, outState); // save instance state
    }

    @Override
    public Loader<ContactPresenter> onCreateLoader(int id, Bundle arg){
        return new PresenterLoader<>(getContext(), new ContactPresenterFactory(getContext()));
    }

    @Override
    public void onLoadFinished(Loader<ContactPresenter> loader, final ContactPresenter presenter) {
        this.presenter = presenter;
        contactNames = presenter.getContactNames();
        adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, contactNames);
        // sets up ListView after load has finished
        listView = (ListView) view.findViewById(R.id.contact_list);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                presenter.onClickContact(position);
            }
        });

        // add action buttons
        builder.setPositiveButton(R.string.button_add, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                presenter.onClickAdd(); // notifies the presenter that the user wants to add a new contaact
            }
        });
        builder.setNegativeButton(R.string.exit, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                presenter.onClickExit(); // notifies the presenter that the user wants to exit the AlertDialog
            }
        });
    }

    @Override
    public void onLoaderReset(Loader<ContactPresenter> loader) {
        presenter = null;
    }

    /**
     * Updates the data in the ListView by creating a new adapter and
     * replacing the old one so the changes will be instantly visible to the user
     */
    public void dataChanged() {
        contactNames = presenter.getContactNames();
        adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, contactNames);

        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                presenter.onClickContact(position);
            }
        });
    }

}
