package internetofeveryone.ioe.AddChat;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import icepick.Icepick;
import internetofeveryone.ioe.Presenter.PresenterLoader;
import internetofeveryone.ioe.R;

/**
 * Created by Fabian Martin for 'Internet of Everyone'
 *
 * This class is responsible for user-interaction and represents the implementation of view for the AddChat fragment
 */
public class AddChatFragment extends android.support.v4.app.DialogFragment implements AddChatView, LoaderManager.LoaderCallbacks<AddChatPresenter> {

    private AddChatPresenter presenter;
    private View view;
    /**
     * Adapter for the ListView
     * It's responsible for displaying data in the list
     */
    private ArrayAdapter<String> adapter;
    private static final int LOADER_ID = 103; // unique identification for the AddChatFragment-LoaderManager

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        Icepick.restoreInstanceState(this, savedInstanceState); // restore instance state

        AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        dialog.setTitle(R.string.button_add_chat);
        view = inflater.inflate(R.layout.fragment_add_chat, null);
        dialog.setView(view);

        return dialog.create();
    }
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getActivity().getSupportLoaderManager().initLoader(LOADER_ID, null, this); // initialises the LoaderManager

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
    public Loader<AddChatPresenter> onCreateLoader(int id, Bundle arg){
        return new PresenterLoader<>(getContext(), new AddChatPresenterFactory(getContext()));
    }

    @Override
    public void onLoadFinished(Loader<AddChatPresenter> loader, final AddChatPresenter presenter) {

        this.presenter = presenter;
        adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, presenter.getContactNames());
        // sets up ListView after load is finished
        ListView listView = (ListView) view.findViewById(R.id.add_chat_list);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                presenter.addChat(adapter.getItem(position));
                dismiss();
            }
        });
    }

    @Override
    public void onLoaderReset(Loader<AddChatPresenter> loader) {
        presenter = null;

    }

    /**
     * Updates the data in the ListView
     */
    public void dataChanged() {
        adapter.notifyDataSetChanged();
    }

}