package internetofeveryone.ioe.DataTests;

import org.junit.Before;
import org.junit.Test;

import java.util.TreeMap;

import internetofeveryone.ioe.Data.Chat;
import internetofeveryone.ioe.Data.Contact;
import internetofeveryone.ioe.Data.Message;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static org.junit.Assert.assertNotEquals;

public class ChatTest {

    Contact contact;
    TreeMap<Long, Message> messageList;
    boolean encryption;

    @Before
    public void setUp() throws Exception {
        contact = new Contact("name", "1-1", "key", false);
        messageList = new TreeMap<>();
        encryption = false;
    }

    @Test
    public void createChat_NotEqualsNull() throws Exception {
        Chat chat = new Chat(contact, messageList, encryption);
        assertNotNull(chat);
    }

    @Test
    public void createChat_correctGetContact() throws Exception {
        Chat chat = new Chat(contact, messageList, encryption);
        assertEquals(chat.getContact(), contact);

    }

    @Test
    public void createChat_correctGetMessageList() throws Exception {
        Chat chat = new Chat(contact, messageList, encryption);
        assertEquals(chat.getMessageList(), messageList);
    }

    @Test
    public void createChat_correctIsEncrypted() throws Exception {
        Chat chat = new Chat(contact, messageList, encryption);
        assertEquals(chat.isEncrypted(), encryption);
    }

    @Test
    public void createChat_correctLastMessage() throws Exception {
        Message msg1 = new Message(0, "2-2", "3-3", "content", false, "2-2");
        Message msg2 = new Message(0, "3-3", "4-4", "differentContent", true, "2-2");
        messageList.put(0L, msg1);
        messageList.put(1L, msg2);
        Chat chat = new Chat(contact, messageList, encryption);
        assertEquals(chat.getLastMessage(), msg2);
    }

    @Test
    public void setContactAndGetContact_ContactChanged() throws Exception {
        Contact newContact = new Contact("differentName", "2-2", "key", true);
        Chat chat = new Chat(contact, messageList, encryption);
        chat.setContact(newContact);
        assertEquals(chat.getContact(), newContact);
    }

    @Test
    public void setMessageListAndGetMessageList_MessageListChanged() throws Exception {
        TreeMap<Long, Message> newMessageList = new TreeMap<>();
        Chat chat = new Chat(contact, messageList, encryption);
        chat.setMessageList(newMessageList);
        assertEquals(chat.getMessageList(), newMessageList);
    }

    @Test
    public void addMessageAndGetLastMessage_LastMessageChanged() throws Exception {
        Message msg1 = new Message(0, "2-2", "3-3", "content", false, "2-2");
        messageList.put(0L, msg1);
        Chat chat = new Chat(contact, messageList, encryption);
        Message oldLast = chat.getLastMessage();

        Message msg2 = new Message(0, "3-3", "4-4", "differentContent", true, "2-2");
        messageList.put(1L, msg2);
        chat = new Chat(contact, messageList, encryption);
        Message newLast = chat.getLastMessage();
        assertNotEquals(oldLast, newLast);
    }

    @Test
    public void setEncryptionAndGetEncryption_EncryptionChanged() throws Exception {
        boolean newEncryption = !encryption;
        Chat chat = new Chat(contact, messageList, encryption);
        chat.setEncryption(newEncryption);
        assertEquals(chat.isEncrypted(), newEncryption);
    }

}
