package internetofeveryone.ioe.MessengerTests;

import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import java.util.HashMap;
import java.util.TreeMap;

import internetofeveryone.ioe.BuildConfig;
import internetofeveryone.ioe.Data.Chat;
import internetofeveryone.ioe.Data.Contact;
import internetofeveryone.ioe.Data.Message;
import internetofeveryone.ioe.Messenger.MessengerAdapter;
import internetofeveryone.ioe.R;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 21)
public class MessengerAdapterTest {

    MessengerAdapter messengerAdapter;
    Chat chat;
    Contact contact;

    @Before
    public void setUp() throws Exception {
        contact = new Contact("name", "userCode", "key", true);
        chat = new Chat(contact, new TreeMap<Long, Message>(), false);
        HashMap<String, Chat> chatList = new HashMap<>();
        chatList.put(contact.getName(), chat);
        messengerAdapter = new MessengerAdapter(chatList, RuntimeEnvironment.application);
    }

    @Test
    public void getFirstItem_returnsCorrectItem() throws Exception {
        assertEquals(messengerAdapter.getItem(0), chat);
    }

    @Test
    public void getCount_returnsCorrectCount() throws Exception {
        assertEquals(messengerAdapter.getCount(), 1);
    }

    @Test
    public void getItemId_returns0() throws Exception {
        assertEquals(messengerAdapter.getItemId(0), 0);
    }

    @Test
    public void getView_returnsCorrectViewWithCorrectValues() throws Exception {
        ListView parent = new ListView(RuntimeEnvironment.application);
        View view = messengerAdapter.getView(0, null, parent);

        TextView tvContact = (TextView) view.findViewById(R.id.contact);
        TextView tvLastMessage = (TextView) view.findViewById(R.id.lastMsg);

        assertNotNull("View is null. ", view);
        assertNotNull("tvContact is null. ", tvContact);
        assertNotNull("tvLastMessage is null. ", tvLastMessage);
        assertEquals("tvContacts don't match.", contact.getName(), tvContact.getText());
        // getLastMessage() is null -> tvLastMessage should be ""
        assertEquals("tvLastMessages don't match for empty msgList.", "", tvLastMessage.getText());

        // set up new msgList that is non-empty
        TreeMap<Long, Message> map = new TreeMap<Long, Message>();
        Message msg = new Message(5, "3-3", "4-4", "content", false, "4-4");
        map.put(5L, msg);
        chat.setMessageList(map);
        view = messengerAdapter.getView(0, null, parent);
        tvLastMessage = (TextView) view.findViewById(R.id.lastMsg);
        // getLastMessage() is now msg, content should be "content"
        assertEquals("tvLastMessages don't match for non-empty msgList.", chat.getLastMessage().getContent(), tvLastMessage.getText());
    }
}
