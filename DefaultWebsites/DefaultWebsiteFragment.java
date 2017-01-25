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


public class DefaultWebsiteFragment extends android.support.v4.app.DialogFragment implements DefaultWebsiteView, LoaderManager.LoaderCallbacks<DefaultWebsitePresenter> {


    private DefaultWebsitePresenter presenter;
    private View view;
    private String currentName = "";
    private String currentURL = "";
    private String[] defaultWebsiteNames;
    private String[] defaultWebsiteURLs;
    private ArrayAdapter<String> adapter;
    private String originalURL;
    private String originalName;
    private AlertDialog.Builder builder;
    private static final int LOADER_ID = 107;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        Icepick.restoreInstanceState(this, savedInstanceState);

        builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        builder.setTitle("Edit default Websites");

        view = inflater.inflate(R.layout.fragment_default_websites, null);
        builder.setView(view);


        // Add action buttons
        builder.setPositiveButton(R.string.button_add_default_website, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                presenter.onClickAdd();
            }
        });
        builder.setNegativeButton(R.string.exit, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                presenter.onClickExit();
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
        getActivity().getSupportLoaderManager().initLoader(LOADER_ID, null, this);

    }

    public Dialog onAddDefaultWebsite() {

        AlertDialog.Builder builder2 = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        builder2.setTitle("Add new default Website");

        view = inflater.inflate(R.layout.fragment_add_default_website, null);
        builder2.setView(view);

        // Add action buttons
        builder2.setPositiveButton(R.string.button_add_default_website, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                currentName = ((EditText) view.findViewById(R.id.editText_website_name)).getText().toString();
                currentURL = ((EditText) view.findViewById(R.id.editText_website_url)).getText().toString();
                System.out.println(currentName + ", " + currentURL);
                presenter.onClickAddWebsite(currentName, currentURL);
                dismiss();
            }
        });
        builder2.setNegativeButton(R.string.exit, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                presenter.onClickCancel();
            }
        });
        Dialog d = builder2.create();
        d.show();
        return d;
    }

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

        // Add action button
        builder3.setPositiveButton(R.string.save, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                currentName = currentNameEditText.getText().toString();
                currentURL = currentURLEditText.getText().toString();
                presenter.onClickSaveChange(originalURL, currentName, currentURL);
            }
        });
        builder3.setNeutralButton(R.string.button_delete_default_website, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                // currentURL = currentURLEditText.getText().toString();
                presenter.onClickDelete(originalURL);
                dismiss();
            }
        });
        builder3.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                presenter.onClickCancel();
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
        System.out.println("Fragment stopped.");
        presenter.detachView();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override public void onSaveInstanceState(Bundle outState) {

        super.onSaveInstanceState(outState);
        Icepick.saveInstanceState(this, outState);
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
        ListView listView = (ListView) view.findViewById(R.id.default_website_list);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                presenter.onClickDefaultWebsite(position);
            }
        });

    }

    @Override
    public void onLoaderReset(Loader<DefaultWebsitePresenter> loader) {
        presenter = null;

    }

    public void dataChanged() {
        adapter.notifyDataSetChanged();
    }

}
