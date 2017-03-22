package internetofeveryone.ioe.Messenger;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.HashMap;

import icepick.Icepick;
import internetofeveryone.ioe.AddChat.AddChatFragment;
import internetofeveryone.ioe.Chat.ChatActivity;
import internetofeveryone.ioe.Contact.ContactFragment;
import internetofeveryone.ioe.Data.Chat;
import internetofeveryone.ioe.Data.Contact;
import internetofeveryone.ioe.Presenter.PresenterLoader;
import internetofeveryone.ioe.R;

/**
 * Created by Fabian Martin for 'Internet of Everyone'
 *
 * This class is responsible for user-interaction and represents the implementation of view for the Messenger page
 */
public class MessengerActivity extends AppCompatActivity implements MessengerView, LoaderManager.LoaderCallbacks<MessengerPresenter> {

    private ListView listView; // list that contains all open Chats
    private MessengerAdapter adapter;
    private MessengerPresenter presenter;
    private HashMap<String, Chat> chatList;
    private static final int LOADER_ID = 105; // unique identification for the MainActivity-LoaderManager
    private AddChatFragment addChatFragment;
    private ContactFragment contactFragment;
    private final String ADDCHAT = "AddChat";
    private final String CONTACT = "Contact";
    private final String CONTACTUSERCODE = "contactUserCode";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        getSupportLoaderManager().initLoader(LOADER_ID, null, this); // initialises the LoaderManager
        Icepick.restoreInstanceState(this, savedInstanceState); // restore instance state
        setContentView(R.layout.activity_messenger);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(R.string.title_activity_messenger);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_chats, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if (id == R.id.add_chat_option) { // if chat option from the menu is selected
            addChatFragment = new AddChatFragment();
            addChatFragment.show(getSupportFragmentManager(), ADDCHAT); // open new fragment to add a new chat

        } else if (id == R.id.add_contact_option) { // if contact option from the menu is selected
            contactFragment = new ContactFragment();
            contactFragment.show(getSupportFragmentManager(), CONTACT); // open new fragment to edit contacts

        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Opens the chat for the given contact
     *
     * @param contact the contact
     */
    public void openChat(Contact contact) {

        Intent intent = new Intent(this, ChatActivity.class);
        intent.putExtra(CONTACTUSERCODE, contact.getUserCode());
        startActivity(intent);
    }

    @Override
    public void onStart() {
        super.onStart();
        presenter.attachView(this);
        presenter.fetchMessagesFromServer();
        dataChanged();
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
     * Updates the data in the ListView by creating a new MessengerAdapter
     * and replacing the old one, so the change will be instantly visible to the user
     */
    public void dataChanged() {

        chatList = presenter.getChatList();
        adapter = new MessengerAdapter(chatList);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                presenter.onChatClicked(adapter.getItem(position));
            }
        });
    }

    @Override public void onSaveInstanceState(Bundle outState) {

        super.onSaveInstanceState(outState);
        Icepick.saveInstanceState(this, outState); // save instance state
    }

    @Override
    public Loader<MessengerPresenter> onCreateLoader(int id, Bundle arg){

        return new PresenterLoader<>(this, new MessengerPresenterFactory(this));
    }

    @Override
    public void onLoadFinished(Loader<MessengerPresenter> loader, final MessengerPresenter presenter) {

        this.presenter = presenter;
        chatList = presenter.getChatList();
        // sets up ListView after load is finished
        listView = (ListView) findViewById(R.id.messenger_list);
        registerForContextMenu(listView);
        adapter = new MessengerAdapter(chatList);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                presenter.onChatClicked(adapter.getItem(position)); // notifies the presenter that an item in the list has been clicked
            }
        });

    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        if (v.getId()==R.id.messenger_list) {
            MenuInflater inflater = getMenuInflater();
            inflater.inflate(R.menu.menu_chat_delete, menu);
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        switch(item.getItemId()) {
            case R.id.delete:
                presenter.onChatDeleteClicked(adapter.getItem((info).position));
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

    @Override
    public void onLoaderReset(Loader<MessengerPresenter> loader) {
        presenter = null;
    }

    public MessengerAdapter getAdapter() {
        return adapter;
    }

    public void setAdapter(MessengerAdapter adapter) {
        this.adapter = adapter;
    }

    public AddChatFragment getAddChatFragment() {
        return addChatFragment;
    }

    public ContactFragment getContactFragment() {
        return contactFragment;
    }
}
