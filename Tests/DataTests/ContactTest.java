package internetofeveryone.ioe.DataTests;

import org.junit.Before;
import org.junit.Test;

import internetofeveryone.ioe.Data.Contact;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;

public class ContactTest {

    String name;
    String userCode;
    String key;
    boolean openChat;

    @Before
    public void setUp() throws Exception {
        name = "name";
        userCode = "1-1";
        key = "key";
        openChat = false;
    }

    @Test
    public void createContact_NotEqualsNull() throws Exception {
        Contact contact = new Contact(name, userCode, key, openChat);
        assertNotNull(contact);
    }

    @Test
    public void createContact_correctGetName() throws Exception {
        Contact contact = new Contact(name, userCode, key, openChat);
        assertEquals(contact.getName(), name);
    }

    @Test
    public void createContact_correctGetUserCode() throws Exception {
        Contact contact = new Contact(name, userCode, key, openChat);
        assertEquals(contact.getUserCode(), userCode);
    }

    @Test
    public void createContact_correctGetKey() throws Exception {
        Contact contact = new Contact(name, userCode, key, openChat);
        assertEquals(contact.getKey(), key);
    }

    @Test
    public void createContact_correctOpenChat() throws Exception {
        Contact contact = new Contact(name, userCode, key, openChat);
        assertEquals(contact.hasOpenChat(), openChat);
    }

    @Test
    public void setNameAndGetName_NameChanged() throws Exception {
        String newName = "differentName";
        Contact contact = new Contact(name, userCode, key, openChat);
        contact.setName(newName);
        assertEquals(contact.getName(), newName);
    }

    @Test
    public void setUserCodeAndGetUserCode_UserCodeChanged() throws Exception {
        String newUserCode = "78-78";
        Contact contact = new Contact(name, userCode, key, openChat);
        contact.setUserCode(newUserCode);
        assertEquals(contact.getUserCode(), newUserCode);
    }

    @Test
    public void setKeyAndGetKey_KeyChanged() throws Exception {
        String newKey = "differentKey";
        Contact contact = new Contact(name, userCode, key, openChat);
        contact.setKey(newKey);
        assertEquals(contact.getKey(), newKey);
    }

    @Test
    public void setOpenChatAndGetOpenChat_OpenChatChanged() throws Exception {
        boolean newOpenChat = !openChat;
        Contact contact = new Contact(name, userCode, key, openChat);
        contact.setOpenChat(newOpenChat);
        assertEquals(contact.hasOpenChat(), newOpenChat);
    }

}
