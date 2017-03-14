package internetofeveryone.ioe.ModelTests;

import android.content.Context;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import java.util.List;
import java.util.TreeMap;

import internetofeveryone.ioe.BuildConfig;
import internetofeveryone.ioe.Data.Chat;
import internetofeveryone.ioe.Data.Contact;
import internetofeveryone.ioe.Data.DataType;
import internetofeveryone.ioe.Data.Message;
import internetofeveryone.ioe.Model.DataBase;
import internetofeveryone.ioe.Model.MessageModel;
import internetofeveryone.ioe.Presenter.ModelObserver;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertNull;
import static junit.framework.Assert.assertTrue;
import static org.junit.Assert.assertNotEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class)
public class MessageModelTest {

    private MessageModel model;
    private Context context;
    private String senderID, receiverID, content, userCode, name, key;
    private boolean encrypted, openChat;
    @Mock
    ModelObserver o;

    @Before
    public void setUp() throws Exception {
        context = RuntimeEnvironment.application.getApplicationContext();
        MockitoAnnotations.initMocks(this);
        doNothing().when(o).update(any(DataType.class));
        senderID = userCode = "1-1";
        receiverID = "2-2";
        content = "content";
        key = "key";
        name = "name";
        encrypted = openChat = false;
        model = new MessageModel(context);
        model.addObserver(o);
        model.open();
    }

    @Test
    public void notify_observersGetUpdated() throws Exception {
        model.notify(DataType.USERCODE);
        verify(o).update(DataType.USERCODE);
    }

    @Test
    public void setDb_replacesDb() throws Exception {
        DataBase old = model.getDb();
        model.setDb(new DataBase(RuntimeEnvironment.application));
        assertNotEquals(old, model.getDb());
    }

    @Test
    public void insertUserCode_getsAddedToDatabaseAndObserversGetNotified() throws Exception {
        model.insertUserCode(userCode, model.getSql());
        verify(o).update(DataType.USERCODE);
        assertNotNull(model.getUserCode());
    }

    @Test
    public void insertSessionHash_getsAddedToDatabaseAndObserversGetNotified() throws Exception {
        model.insertSessionHash("sessionHash", model.getSql());
        verify(o).update(DataType.USERCODE);
        assertNotNull(model.getSessionHash());
    }

    @Test
    public void addMessage_getsAddedToDatabaseAndObserversGetNotified() throws Exception {
        long id = model.addMessage(senderID, receiverID, content, encrypted);
        verify(o).update(DataType.MESSAGE);
        assertNotNull(model.getMessageByID(id));
    }

    @Test
    public void addContact_getsAddedToDatabaseAndObserversGetNotified() throws Exception {
        model.addContact(name, userCode, key, openChat);
        verify(o).update(DataType.CONTACT);
        assertNotNull(model.getContactByID(userCode));
    }

    @Test
    public void addChat_getsAddedToDatabaseAndObserversGetNotified() throws Exception {
        model.addContact(name, userCode, key, openChat);
        model.addChat(userCode, encrypted);
        verify(o, times(1)).update(DataType.CHAT);
        verify(o, times(1)).update(DataType.CONTACT);
        assertNotNull(model.getChatByID(userCode));
    }

    @Test
    public void deleteMessage_getsDeletedFromDatabaseAndObserversGetNotified() throws Exception {
        long id = model.addMessage(senderID, receiverID, content, encrypted);
        int amountDeleted = model.deleteMessage(id);
        verify(o, times(2)).update(DataType.MESSAGE);
        assertEquals(amountDeleted, 1);
        assertNull(model.getMessageByID(id));
    }

    @Test
    public void deleteContact_getsDeletedFromDatabaseAndObserversGetNotified() throws Exception {
        model.addContact(name, userCode, key, openChat);
        int amountDeleted = model.deleteContact(userCode);
        verify(o, times(2)).update(DataType.CONTACT);
        assertEquals(amountDeleted, 1);
        assertNull(model.getContactByID(userCode));
    }

    @Test
    public void deleteChat_getsDeletedFromDatabaseAndObserversGetNotified() throws Exception {
        model.addContact(name, userCode, key, openChat);
        model.addChat(userCode, encrypted);
        int amountDeleted = model.deleteChat(userCode);
        verify(o, times(2)).update(DataType.CHAT);
        verify(o, times(1)).update(DataType.CONTACT);
        assertEquals(amountDeleted, 1);
        assertNull(model.getChatByID(userCode));
    }

    @Test
    public void getAllMessagesByContact_returnsCorrectMessagesFromDatabase() throws Exception {
        long id1 = model.addMessage(senderID, receiverID, content, encrypted);
        long id2 = model.addMessage(senderID, receiverID, content + "2", encrypted);
        TreeMap<Long, Message> msgList  = model.getAllMessagesByContact(senderID);
        verify(o, times(2)).update(DataType.MESSAGE);
        assertTrue(msgList.size() == 2);
        assertEquals(msgList.get(id1).getSenderID(), senderID);
        assertEquals(msgList.get(id2).getSenderID(), senderID);
        assertEquals(msgList.get(id1).getReceiverID(), receiverID);
        assertEquals(msgList.get(id2).getReceiverID(), receiverID);
        assertEquals(msgList.get(id1).getContent(), content);
        assertEquals(msgList.get(id2).getContent(), content + "2");
        assertEquals(msgList.get(id1).isEncrypted(), encrypted);
        assertEquals(msgList.get(id2).isEncrypted(), encrypted);
    }

    @Test
    public void deleteAllMessagesForContact_getDeletedFromDatabaseAndObverversGetNotified() throws Exception {
        long id1 = model.addMessage(senderID, receiverID, content, encrypted);
        long id2 = model.addMessage(senderID, receiverID, content + "2", encrypted);
        model.deleteAllMessagesForContact(senderID);
        verify(o, times(4)).update(DataType.MESSAGE);
        assertNull(model.getMessageByID(id1));
        assertNull(model.getMessageByID(id2));
    }

    @Test
    public void getAllMessages_returnsAllMessagesFromDatabase() throws Exception {
        long id1 = model.addMessage(senderID, receiverID, content, encrypted);
        long id2 = model.addMessage(senderID, receiverID, content + "2", encrypted);
        List<Message> returnedList = model.getAllMessages();
        assertTrue(returnedList.size() == 2);
        assertEquals(returnedList.get(0).getSenderID(), senderID);
        assertEquals(returnedList.get(1).getSenderID(), senderID);
        assertEquals(returnedList.get(0).getReceiverID(), receiverID);
        assertEquals(returnedList.get(1).getReceiverID(), receiverID);
        assertEquals(returnedList.get(0).getContent(), content);
        assertEquals(returnedList.get(1).getContent(), content + "2");
        assertEquals(returnedList.get(0).isEncrypted(), encrypted);
        assertEquals(returnedList.get(1).isEncrypted(), encrypted);
    }

    @Test
    public void getAllChats_returnsAllContactFromDatabase() throws Exception {
        String otherUserCode = "3-3";
        model.addContact(name, userCode, key, openChat);
        model.addContact(name, otherUserCode, key, openChat);
        model.addChat(userCode, encrypted);
        model.addChat(otherUserCode, encrypted);
        List<Chat> returnedList = model.getAllChats();
        assertTrue(returnedList.size() == 2);
        assertEquals(returnedList.get(0).getContact().getUserCode(), userCode);
        assertEquals(returnedList.get(1).getContact().getUserCode(), otherUserCode);
        assertEquals(returnedList.get(0).isEncrypted(), encrypted);
        assertEquals(returnedList.get(1).isEncrypted(), encrypted);
    }

    @Test
    public void getAllContact_returnsAllContactFromDatabase() throws Exception {
        String otherUserCode = "3-3";
        model.addContact(name, userCode, key, openChat);
        model.addContact(name, otherUserCode, key, openChat);
        List<Contact> returnedList = model.getAllContacts();
        assertTrue(returnedList.size() == 2);
        assertEquals(returnedList.get(0).getName(), name);
        assertEquals(returnedList.get(1).getName(), name);
        assertEquals(returnedList.get(0).getUserCode(), userCode);
        assertEquals(returnedList.get(1).getUserCode(), otherUserCode);
        assertEquals(returnedList.get(0).getKey(), key);
        assertEquals(returnedList.get(1).getKey(), key);
        assertEquals(returnedList.get(0).hasOpenChat(), openChat);
        assertEquals(returnedList.get(1).hasOpenChat(), openChat);
    }

    @Test
    public void updateContact_getsUpdatedInDatabaseAndObserversGetNotified() throws Exception {
        String newName = "newName";
        String newUserCode = "12-12";
        String newKey = "newKey";
        boolean newOpenChat = !openChat;
        model.addContact(name, userCode, key, openChat);
        model.updateContact(userCode, newName, newUserCode, newKey, newOpenChat);
        Contact c = model.getContactByID(newUserCode);
        verify(o, times(2)).update(DataType.CONTACT);
        assertEquals(c.getName(), newName);
        assertEquals(c.getUserCode(), newUserCode);
        assertEquals(c.getKey(), newKey);
        assertEquals(c.hasOpenChat(), newOpenChat);
    }

    @Test
    public void updateChat_getsUpdatedInDatabaseAndObserversGetNotified() throws Exception {
        boolean newEncrypted = !encrypted;
        model.addContact(name, userCode, key, openChat);
        model.addChat(userCode, encrypted);
        model.updateChat(userCode, newEncrypted);
        Chat c = model.getChatByID(userCode);
        verify(o, times(2)).update(DataType.CHAT);
        verify(o, times(1)).update(DataType.CONTACT);
        assertEquals(c.isEncrypted(), newEncrypted);
        assertEquals(c.getContact().getUserCode(), userCode);
    }

    @After
    public void tearDown() throws Exception {
        model.close();
    }
}