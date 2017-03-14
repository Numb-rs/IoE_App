package internetofeveryone.ioe.DataTests;

import org.junit.Before;
import org.junit.Test;

import internetofeveryone.ioe.Data.Message;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertTrue;
import static org.junit.Assert.assertNotEquals;

public class MessageTest {

    long id;
    String message, key, senderID, receiverID, myUserCode, content;
    boolean isEncrypted;

    @Before
    public void setUp() throws Exception {
        message = "message";
        key = "key";
        id = 9;
        senderID = "1-1";
        receiverID = "2-2";
        myUserCode = senderID;
        content = "content";
        isEncrypted = false;
    }

    @Test
    public void encryptValid_messageChanged() throws Exception {
        String result = Message.encrypt(message, key);
        assertNotEquals(result, message);
    }

    @Test
    public void decryptValid_messageRestored() throws Exception {
        String encrypted = Message.encrypt(message, key);
        String decrypted = Message.decrypt(encrypted, key);
        assertEquals(message, decrypted);
    }

    @Test
    public void createMessage_NotEqualsNull() throws Exception {
        Message msg = new Message(id, senderID, receiverID, content, isEncrypted, myUserCode);
        assertNotNull(msg);
    }

    @Test
    public void createMessage_correctGetID() throws Exception {
        Message msg = new Message(id, senderID, receiverID, content, isEncrypted, myUserCode);
        assertEquals(msg.getId(), id);
    }

    @Test
    public void createMessage_correctGetSenderID() throws Exception {
        Message msg = new Message(id, senderID, receiverID, content, isEncrypted, myUserCode);
        assertEquals(msg.getSenderID(), senderID);
    }

    @Test
    public void createMessage_correctGetReceiverID() throws Exception {
        Message msg = new Message(id, senderID, receiverID, content, isEncrypted, myUserCode);
        assertEquals(msg.getReceiverID(), receiverID);
    }

    @Test
    public void createMessage_correctGetContent() throws Exception {
        Message msg = new Message(id, senderID, receiverID, content, isEncrypted, myUserCode);
        assertEquals(msg.getContent(), content);
    }

    @Test
    public void createMessage_correctIsEncrypted() throws Exception {
        Message msg = new Message(id, senderID, receiverID, content, isEncrypted, myUserCode);
        assertEquals(msg.isEncrypted(), isEncrypted);
    }

    @Test
    public void createMessageThatsMine_correctIsMine() throws Exception {
        // myUserCode = senderID
        Message msg = new Message(id, senderID, receiverID, content, isEncrypted, myUserCode);
        assertTrue(msg.isMine());
    }

    @Test
    public void setIDAndGetID_IDChanged() throws Exception {
        long newID = 87;
        Message msg = new Message(id, senderID, receiverID, content, isEncrypted, myUserCode);
        msg.setId(newID);
        assertEquals(msg.getId(), newID);
    }

    @Test
    public void setSenderIDAndGetSenderID_SenderIDChanged() throws Exception {
        String newSenderID = "9-9";
        Message msg = new Message(id, senderID, receiverID, content, isEncrypted, myUserCode);
        msg.setSenderID((newSenderID));
        assertEquals(msg.getSenderID(), newSenderID);
    }

    @Test
    public void setReceiverIDAndGetReceiverID_ReceiverIDChanged() throws Exception {
        String newReceiverID = "10-10";
        Message msg = new Message(id, senderID, receiverID, content, isEncrypted, myUserCode);
        msg.setReceiverID(newReceiverID);
        assertEquals(msg.getReceiverID(), newReceiverID);
    }

    @Test
    public void setContentAndGetContent_ContentChanged() throws Exception {
        String newContent = "newContent";
        Message msg = new Message(id, senderID, receiverID, content, isEncrypted, myUserCode);
        msg.setContent(newContent);
        assertEquals(msg.getContent(), newContent);
    }

    @Test
    public void setEncryptedAndGetEncrypted_EncryptedChanged() throws Exception {
        boolean newIsEncrypted = !isEncrypted;
        Message msg = new Message(id, senderID, receiverID, content, isEncrypted, myUserCode);
        msg.setEncrypted(newIsEncrypted);
        assertEquals(msg.isEncrypted(), newIsEncrypted);
    }

    @Test
    public void setIsMineAndGetIsMine_IsMineChanged() throws Exception {
        // is set to false, because it's set to true in @Before
        boolean newIsMine = !(senderID == myUserCode || receiverID == myUserCode);
        Message msg = new Message(id, senderID, receiverID, content, isEncrypted, myUserCode);
        msg.setIsMine(newIsMine);
        assertEquals(msg.isMine(), newIsMine);
    }
}
