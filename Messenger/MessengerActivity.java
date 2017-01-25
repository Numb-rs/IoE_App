package internetofeveryone.ioe.Messenger;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import icepick.Icepick;
import internetofeveryone.ioe.AddChat.AddChatFragment;
import internetofeveryone.ioe.Chat.ChatActivity;
import internetofeveryone.ioe.Contact.ContactFragment;
import internetofeveryone.ioe.Data.Contact;
import internetofeveryone.ioe.Presenter.PresenterLoader;
import internetofeveryone.ioe.R;

public class MessengerActivity extends AppCompatActivity implements MessengerView, LoaderManager.LoaderCallbacks<MessengerPresenter> {

    ListView listView;
    private MessengerAdapter adapter;
    private MessengerPresenter presenter;
    private static final int LOADER_ID = 105;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportLoaderManager().initLoader(LOADER_ID, null, this);
        Icepick.restoreInstanceState(this, savedInstanceState);

        setContentView(R.layout.activity_messenger);
        getSupportActionBar().setTitle("Chats");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_chats, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if (id == R.id.add_chat_option) {
            AddChatFragment fragment = new AddChatFragment();
            fragment.show(getSupportFragmentManager(), "AddChat");

        } else if (id == R.id.add_contact_option) {
            ContactFragment fragment = new ContactFragment();
            fragment.show(getSupportFragmentManager(), "Contact");

        }
        return super.onOptionsItemSelected(item);
    }

    public void openChat(Contact contact) {
        Intent intent = new Intent(this, ChatActivity.class);
        intent.putExtra("contactName", contact.getName());
        intent.putExtra("contactPhoneNumber", contact.getUserCode());
        startActivity(intent);
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

    public void dataChanged() {
        listView = (ListView)findViewById(R.id.messenger_list);
        adapter = new MessengerAdapter(presenter.getChatList(), getApplicationContext());
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                presenter.onChatClicked(adapter.getItem(position));
            }
        });
    }

    @Override protected void onSaveInstanceState(Bundle outState) {

        super.onSaveInstanceState(outState);
        Icepick.saveInstanceState(this, outState);
    }

    @Override
    public Loader<MessengerPresenter> onCreateLoader(int id, Bundle arg){
        // System.out.println("Main: onCreateLoader. Presenter = " + (presenter == null ? null : "Pres."));

        return new PresenterLoader<>(this, new MessengerPresenterFactory(this));
    }

    @Override
    public void onLoadFinished(Loader<MessengerPresenter> loader, final MessengerPresenter presenter) {
        this.presenter = presenter;
        listView = (ListView) findViewById(R.id.messenger_list);
        adapter = new MessengerAdapter(presenter.getChatList(), getApplicationContext());
        listView.setAdapter(adapter);
        // key = ((TextView) listView.findViewById(R.id.contact)).toString();
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                presenter.onChatClicked(adapter.getItem(position));
            }
        });
    }

    @Override
    public void onLoaderReset(Loader<MessengerPresenter> loader) {
        presenter = null;

    }
}
