package internetofeveryone.ioe.DefaultWebsites;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
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
 * This class is responsible for user-interaction and represents the implementation of view for the DefaultWebsite fragment
 */
public class DefaultWebsiteFragment extends android.support.v4.app.DialogFragment implements DefaultWebsiteView, LoaderManager.LoaderCallbacks<DefaultWebsitePresenter> {


    private DefaultWebsitePresenter presenter;
    private View view;
    private String currentName = ""; // name that has been entered by the user
    private String currentURL = ""; // URL that has been entered by the user
    private String[] defaultWebsiteNames; // names of all DefaultWebsites
    private String[] defaultWebsiteURLs; // URLs of all DefaultWebsites
    /**
     * Adapter for the ListView
     * It's responsible for displaying data in the list
     */
    private ArrayAdapter<String> adapter;
    private String originalURL; // URL of the DefaultWebsite before editing
    private String originalName; // name of the DefaultWebsite before editing
    private AlertDialog.Builder builder;
    private static final int LOADER_ID = 107; // unique identification for the DefaultWebsiteFragment-LoaderManager

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        Icepick.restoreInstanceState(this, savedInstanceState); // restores instance state
        builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        builder.setTitle("Edit default Websites");
        view = inflater.inflate(R.layout.fragment_default_websites, null);
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

                Button button = ((AlertDialog) dialog).getButton(AlertDialog.BUTTON_POSITIVE);
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
        getActivity().getSupportLoaderManager().initLoader(LOADER_ID, null, this); // initialises LoaderManager

    }

    /**
     * Opens a new AlertDialog that allows users to add a new DefaultWebsite
     *
     * @return AlertDialog
     */
    public Dialog onAddDefaultWebsite() {

        AlertDialog.Builder builder2 = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        builder2.setTitle("Add new default Website");
        view = inflater.inflate(R.layout.fragment_add_default_website, null);
        builder2.setView(view);

        // add action buttons
        builder2.setPositiveButton(R.string.button_add, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {

                currentName = ((EditText) view.findViewById(R.id.editText_website_name)).getText().toString();
                currentURL = ((EditText) view.findViewById(R.id.editText_website_url)).getText().toString();
                // System.out.println(currentName + ", " + currentURL);
                presenter.onClickAddWebsite(currentName, currentURL); // notifies presenter that the user wants to add a new Website
                dismiss(); // closes the AlertDialog
            }
        });
        builder2.setNegativeButton(R.string.exit, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                presenter.onClickCancel(); // notifies the presenter that the user wants to cancel
            }
        });
        Dialog d = builder2.create();
        d.show();
        return d;
    }

    /**
     * Opens a new AlertDialog that allows users to edit a DefaultWebsite
     *
     * @param pos position of the DefaultWebsite that has been selected in the ListView
     * @return AlertDialog
     */
    public Dialog onEditDefaultWebsite(int pos) {

        AlertDialog.Builder builder3 = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        builder3.setTitle("Edit default Website");
        view = inflater.inflate(R.layout.fragment_edit_default_website, null);
        builder3.setView(view);

        final EditText currentNameEditText = (EditText) view.findViewById(R.id.editText_current_website_name);
        final EditText currentURLEditText = (EditText) view.findViewById(R.id.editText_current_website_url);

        defaultWebsiteURLs = presenter.getDefaultWebsiteURLs();

        originalName = defaultWebsiteNames[pos];
        originalURL = defaultWebsiteURLs[pos];
        currentNameEditText.setText(originalName);
        currentURLEditText.setText(originalURL);

        // add action buttons
        builder3.setPositiveButton(R.string.save, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {

                currentName = currentNameEditText.getText().toString();
                currentURL = currentURLEditText.getText().toString();
                presenter.onClickSaveChange(originalURL, currentName, currentURL); // notifies the presenter that the user wants to save the changes
            }
        });
        builder3.setNeutralButton(R.string.button_delete, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {

                presenter.onClickDelete(originalURL); // notifies the presenter that the user wants to delete the DefaultWebsite
                dismiss(); // closes the AlertDialog
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
        // System.out.println("Fragment stopped.");
        presenter.detachView();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override public void onSaveInstanceState(Bundle outState) {

        super.onSaveInstanceState(outState);
        Icepick.saveInstanceState(this, outState); // saves instance state
    }

    @Override
    public Loader<DefaultWebsitePresenter> onCreateLoader(int id, Bundle arg){
        return new PresenterLoader<>(getActivity(), new DefaultWebsitePresenterFactory(getActivity()));
    }

    @Override
    public void onLoadFinished(Loader<DefaultWebsitePresenter> loader, final DefaultWebsitePresenter presenter) {

        this.presenter = presenter;
        defaultWebsiteNames = presenter.getDefaultWebsiteNames();
        adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, defaultWebsiteNames);

        // sets up the list after the load has finished
        ListView listView = (ListView) view.findViewById(R.id.default_website_list);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                presenter.onClickDefaultWebsite(position); // notifies the presenter that the user has clicked on an item in the list
            }
        });

    }

    @Override
    public void onLoaderReset(Loader<DefaultWebsitePresenter> loader) {
        presenter = null;

    }

    /**
     * Updates the data in the ListView
     */
    public void dataChanged() {
        adapter.notifyDataSetChanged();
    }

}
