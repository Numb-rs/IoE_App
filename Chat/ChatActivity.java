package internetofeveryone.ioe.Chat;

import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ToggleButton;

import icepick.Icepick;
import internetofeveryone.ioe.Presenter.PresenterLoader;
import internetofeveryone.ioe.R;

/**
 * Created by Fabian Martin for 'Internet of Everyone'
 *
 * This class is responsible for user-interaction and represents the implementation of view for the Chat page
 */
public class ChatActivity extends AppCompatActivity implements ChatView, LoaderManager.LoaderCallbacks<ChatPresenter> {

    private ChatPresenter presenter;
    private String contactName; // name of the contact
    /**
     * Adapter for the ListView
     * It's responsible for displaying data in the list
     */
    ArrayAdapter<String> adapter;
    private static final int LOADER_ID = 104; // unique identification for the ChatActivity-LoaderManager


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        presenter = new ChatPresenter(this);
        getSupportLoaderManager().initLoader(LOADER_ID, null, this); // initialises the LoaderManager
        Icepick.restoreInstanceState(this, savedInstanceState); // restore instance state
        setContentView(R.layout.activity_chat);
        contactName = getIntent().getStringExtra("contactName");
        ToggleButton encryption = (ToggleButton) findViewById(R.id.button_encryption);
        encryption.setChecked(presenter.isChatEncrypted(contactName));
    }

    @Override
    public void onStart() {
        super.onStart();
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

    /**
     * Notifies the presenter that the user wants to change the encryption state
     *
     * @param view
     */
    public void onClickEncryptToggle(View view) {
        ToggleButton encryption = (ToggleButton) findViewById(R.id.button_encryption);
        presenter.encryptChanged(encryption.isChecked());
    }

    /**
     * Notifies the presenter that the user wants to send the message
     *
     * @param view
     */
    public void onClickSend(View view) {
        EditText msgEditText = (EditText) findViewById(R.id.message_to_send);
        String message = msgEditText.getText().toString();
        presenter.sendMessage(message);
    }

    /**
     * Updates the data in the ListView by creating a new Adapter and replacing the old one,
     * so that the user will instantly see the changes
     */
    public void dataChanged() {
        ListView listView = (ListView)findViewById(R.id.chat_list);
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, presenter.getMessageList());
        listView.setAdapter(adapter);
    }

    @Override protected void onSaveInstanceState(Bundle outState) {

        super.onSaveInstanceState(outState);
        Icepick.saveInstanceState(this, outState); // save instance state
    }

    @Override
    public Loader<ChatPresenter> onCreateLoader(int id, Bundle arg){

        return new PresenterLoader<>(this, new ChatPresenterFactory(this));
    }

    @Override
    public void onLoadFinished(Loader<ChatPresenter> loader, ChatPresenter presenter) {

        this.presenter = presenter;
        long contactUserCode = getIntent().getLongExtra("contactUserCode", -1);
        if (contactUserCode == -1) {
            // ErrorHandling
        }
        presenter.getContact(contactUserCode);
        getSupportActionBar().setTitle(contactName);
        // sets up ListView after load has finished
        ListView listView = (ListView)findViewById(R.id.chat_list);
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, presenter.getMessageList());
        listView.setAdapter(adapter);
    }

    @Override
    public void onLoaderReset(Loader<ChatPresenter> loader) {
        presenter = null;

    }

}
