package internetofeveryone.ioe.Model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.TreeMap;

import internetofeveryone.ioe.Data.Chat;
import internetofeveryone.ioe.Data.Contact;
import internetofeveryone.ioe.Data.DataType;
import internetofeveryone.ioe.Data.Message;

/**
 * The model for all message related data
 */
public class MessageModel extends Model {

    public DataBase getDb() {
        return db;
    }

    public void setDb(DataBase db) {
        this.db = db;
    }

    public void setSql(SQLiteDatabase sql) {
        this.sql = sql;
    }

    private SQLiteDatabase sql;
    private DataBase db;
    private final String[] messageColumns = { TableData.Messages.COLUMN_MESSAGES_ID,
            TableData.Messages.COLUMN_MESSAGES_SENDERID, TableData.Messages.COLUMN_MESSAGES_RECEIVERID,
            TableData.Messages.COLUMN_MESSAGES_CONTENT, TableData.Messages.COLUMN_MESSAGES_ISENCRYPTED };
    private final String[] contactColumns = { TableData.Contacts.COLUMN_CONTACTS_NAME, TableData.Contacts.COLUMN_CONTACTS_USERCODE,
            TableData.Contacts.COLUMN_CONTACTS_KEY, TableData.Contacts.COLUMN_CONTACTS_OPENCHAT };
    private final String[] chatColumns = { TableData.Chats.COLUMN_CHATS_USERCODE, TableData.Chats.COLUMN_CHATS_ISENCRYPTED };
    private final String TAG = "MessageModel";

    /**
     * Instantiates a new Message model.
     *
     * @param context the context
     */
    public MessageModel(Context context) {
        super();
        db = new DataBase(context);

        try {
            open();
        } catch (SQLException e) {
            // ErrorHandler
        }
    }

    /**
     * Opens the database
     *
     * @throws SQLException the sql exception
     */
    public void open() throws SQLException {
        sql = db.getWritableDatabase();
    }

    /**
     * Closes the database
     */
    public void close() {
        sql.close();
    }

    /**
     * Adds a message to the database
     *
     * @param senderID    the sender id
     * @param receiverID  the receiver id
     * @param content     the content
     * @param isEncrypted the is encrypted
     * @return id the message id
     */
    public long addMessage(String senderID, String receiverID, String content, boolean isEncrypted) {

        ContentValues values = new ContentValues();
        values.put(TableData.Messages.COLUMN_MESSAGES_SENDERID, senderID);
        values.put(TableData.Messages.COLUMN_MESSAGES_RECEIVERID, receiverID);
        values.put(TableData.Messages.COLUMN_MESSAGES_CONTENT, content);
        values.put(TableData.Messages.COLUMN_MESSAGES_ISENCRYPTED, String.valueOf(isEncrypted));

        long id = sql.insert(TableData.Messages.TABLE_MESSAGES, null, values);

        Log.d(TAG, "id = " + id);
        Log.d(TAG, "senderID = " + senderID);
        Log.d(TAG, "receiverID = " + receiverID);
        Log.d(TAG, "content = " + content);
        Log.d(TAG, "isEncrypted = " + isEncrypted);

        if (getContactByID(senderID) == null || getContactByID(receiverID) == null) {
            if (senderID.equals(getUserCode())) {
                addContact("???", receiverID, "", true);
                addChat(receiverID, false);
            } else {
                addContact("???", senderID, "", true);
                addChat(senderID, false);
            }
            notify(DataType.CONTACT);
            notify(DataType.CHAT);
        }

        notify(DataType.MESSAGE);
        return id;
    }

    /**
     * Adds a contact to the database
     *
     * @param name     the name
     * @param userCode the user code
     * @param key      the key
     * @param openChat the open chat
     * @return the boolean
     */
    public boolean addContact(String name, String userCode, String key, boolean openChat) {
        if (getContactByID(userCode) != null) {
            return false;
        }

        ContentValues values = new ContentValues();
        values.put(TableData.Contacts.COLUMN_CONTACTS_NAME, name);
        values.put(TableData.Contacts.COLUMN_CONTACTS_USERCODE, userCode);
        values.put(TableData.Contacts.COLUMN_CONTACTS_KEY, key);
        values.put(TableData.Contacts.COLUMN_CONTACTS_OPENCHAT, String.valueOf(openChat));

        sql.insert(TableData.Contacts.TABLE_CONTACTS, null, values);
        notify(DataType.CONTACT);
        return true;
    }


    /**
     * Adds a chat to the database
     *
     * @param userCode    the user code
     * @param isEncrypted the is encrypted
     * @return the boolean
     */
    public boolean addChat(String userCode, boolean isEncrypted) {
        if (getChatByID(userCode) != null) {
            return false;
        }

        ContentValues values = new ContentValues();
        values.put(TableData.Chats.COLUMN_CHATS_USERCODE, userCode);
        values.put(TableData.Chats.COLUMN_CHATS_ISENCRYPTED, String.valueOf(isEncrypted));

        sql.insert(TableData.Chats.TABLE_CHATS, null, values);
        notify(DataType.CHAT);
        return true;
    }


    /**
     * Deletes a message from the database
     *
     * @param id the id
     */
    public int deleteMessage(long id) {
        int amount = sql.delete(TableData.Messages.TABLE_MESSAGES, TableData.Messages.COLUMN_MESSAGES_ID
                + " = " + id, null);
        notify(DataType.MESSAGE);
        return amount;
    }

    /**
     * Deletes a contact from the database
     *
     * @param userCode the user code
     */
    public int deleteContact(String userCode) {
        int numRowsChanged = sql.delete(TableData.Contacts.TABLE_CONTACTS, TableData.Contacts.COLUMN_CONTACTS_USERCODE
                + " =? ", new String[] {userCode});
        if (numRowsChanged != 0) {
            deleteChat(userCode);
            deleteAllMessagesForContact(userCode);
        }
        notify(DataType.CONTACT);
        return numRowsChanged;
    }

    /**
     * Deletes a chat from the database
     *
     * @param userCode the user code
     */
    public int deleteChat(String userCode) {
        int amount = sql.delete(TableData.Chats.TABLE_CHATS, TableData.Chats.COLUMN_CHATS_USERCODE
                + " =? ", new String[] {userCode});
        Contact contact = getContactByID(userCode); // contact that had its chat deleted
        if (contact != null) {
            updateContact(userCode, contact.getName(), userCode, contact.getKey(), false);
            notify(DataType.CONTACT);
        }
        notify(DataType.CHAT);
        return amount;
    }

    /**
     * Deletes all messages for a specific contact from the database
     *
     * @param userCode the user code
     */
    public void deleteAllMessagesForContact(String userCode) {
        TreeMap<Long, Message> messages = getAllMessagesByContact(userCode);
        for (Long id : messages.keySet()) {
            deleteMessage(id);
        }
    }

    /**
     * Gets all messages from the database
     *
     * @return the all messages
     */
    public List<Message> getAllMessages() {
        List<Message> messages = new ArrayList<>();

        Cursor cursor = sql.query(TableData.Messages.TABLE_MESSAGES, messageColumns,
                null, null, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                Message msg = cursorToMessage(cursor);
                if (msg != null) {
                    messages.add(msg);
                }
                cursor.moveToNext();
            }
            cursor.close();
        }
        return messages;
    }

    /**
     * Gets all contacts from the database
     *
     * @return the all contacts
     */
    public List<Contact> getAllContacts() {
        List<Contact> contacts = new ArrayList<>();

        Cursor cursor = sql.query(TableData.Contacts.TABLE_CONTACTS, contactColumns,
                null, null, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                Contact contact = cursorToContact(cursor);
                if (contact != null) {
                    contacts.add(contact);
                }
                cursor.moveToNext();
            }
            cursor.close();
        }
        return contacts;
    }

    /**
     * Gets all chats from the database
     *
     * @return the all chats
     */
    public List<Chat> getAllChats() {
        List<Chat> chats = new ArrayList<>();

        Cursor cursor = sql.query(TableData.Chats.TABLE_CHATS, chatColumns,
                null, null, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                Chat chat = cursorToChat(cursor);
                if (chat != null) {
                    chats.add(chat);
                }
                cursor.moveToNext();
            }
            cursor.close();
        }
        return chats;
    }

    /**
     * Gets message by id from the database
     *
     * @param id the id
     * @return the message by id
     */
    public Message getMessageByID(long id) {
        Cursor cursor = sql.query(TableData.Messages.TABLE_MESSAGES, messageColumns,
                TableData.Messages.COLUMN_MESSAGES_ID + " = ?",
                new String[] { String.valueOf(id) }, null, null, null);
        if (cursor != null) {
            if(!cursor.moveToFirst()) {
                return null;
            }
        }

        Message msg = cursorToMessage(cursor);
        if (cursor != null) {
            cursor.close();
        }
        return msg;
    }

    /**
     * Gets contact by id from the database
     *
     * @param userCode the user code
     * @return the contact by id
     */
    public Contact getContactByID(String userCode) {
        Cursor cursor = sql.query(TableData.Contacts.TABLE_CONTACTS, contactColumns,
                TableData.Contacts.COLUMN_CONTACTS_USERCODE + " = ?",
                new String[] { userCode }, null, null, null);
        if (cursor != null) {
            if(!cursor.moveToFirst()) {
                return null;
            }
        }

        Contact contact = cursorToContact(cursor);
        if (cursor != null) {
            cursor.close();
        }
        return contact;
    }

    /**
     * Gets chat by id from the database
     *
     * @param userCode the user code
     * @return the chat by id
     */
    public Chat getChatByID(String userCode) {
        Cursor cursor = sql.query(TableData.Chats.TABLE_CHATS, chatColumns,
                TableData.Chats.COLUMN_CHATS_USERCODE + " = ?",
                new String[] { userCode }, null, null, null);
        if (cursor != null) {
            if(!cursor.moveToFirst()) {
                return null;
            }
        }

        Chat chat = cursorToChat(cursor);
        if (cursor != null) {
            cursor.close();
        }
        return chat;
    }

    /**
     * Gets all messages for a specific contact from the database
     *
     * @param userCode the user code
     * @return the all messages by contact
     */
    public TreeMap<Long, Message> getAllMessagesByContact(String userCode) {
        HashSet<Message> messages = new HashSet<>();

        Cursor cursorReceive = sql.query(TableData.Messages.TABLE_MESSAGES, messageColumns,
                TableData.Messages.COLUMN_MESSAGES_RECEIVERID + " = ?",
                new String[] { userCode }, null, null, null);

        if (cursorReceive != null) {
            cursorReceive.moveToFirst();
            while (!cursorReceive.isAfterLast()) {
                Message msg = cursorToMessage(cursorReceive);
                if (msg.getReceiverID().equals(userCode) || msg.getSenderID().equals(userCode)) {
                    messages.add(msg);
                }
                cursorReceive.moveToNext();
            }
            cursorReceive.close();
        }

        Cursor cursorSend = sql.query(TableData.Messages.TABLE_MESSAGES, messageColumns,
                TableData.Messages.COLUMN_MESSAGES_SENDERID + " = ?",
                new String[] { userCode }, null, null, null);
        if (cursorSend != null) {
            cursorSend.moveToFirst();
            while (!cursorSend.isAfterLast()) {
                Message msg = cursorToMessage(cursorSend);
                if (msg.getReceiverID().equals(userCode) || msg.getSenderID().equals(userCode)) {
                    messages.add(msg);
                }
                cursorSend.moveToNext();
            }
            cursorSend.close();
        }
        TreeMap<Long, Message> sortedMessageList = new TreeMap<>();

        for (Message m : messages) {
            sortedMessageList.put(m.getId(), m);
        }
        return sortedMessageList;
    }

    /**
     * Updates a contact in the database
     *
     * @param userCode    the original user code
     * @param name        the name
     * @param newUserCode the new user code
     * @param key         the key
     * @param openChat    the open chat
     * @return the boolean
     */
    public boolean updateContact(String userCode, String name, String newUserCode, String key, boolean openChat) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(TableData.Contacts.COLUMN_CONTACTS_NAME, name);
        contentValues.put(TableData.Contacts.COLUMN_CONTACTS_USERCODE, newUserCode);
        contentValues.put(TableData.Contacts.COLUMN_CONTACTS_KEY, key);
        contentValues.put(TableData.Contacts.COLUMN_CONTACTS_OPENCHAT, String.valueOf(openChat));
        sql.update(TableData.Contacts.TABLE_CONTACTS, contentValues, "userCode =  ?", new String[] {userCode});
        // update the userCode in the corresponding Chat
        ContentValues contentValuesChat = new ContentValues();
        contentValuesChat.put(TableData.Chats.COLUMN_CHATS_USERCODE, newUserCode);
        sql.update(TableData.Chats.TABLE_CHATS, contentValuesChat, "userCode =  ?", new String[] {userCode});
        notify(DataType.CONTACT);
        notify(DataType.CHAT);
        return true;
    }

    /**
     * Updates a chat in the database
     *
     * @param userCode    the user code
     * @param isEncrypted the is encrypted
     * @return the boolean
     */
    public boolean updateChat(String userCode, boolean isEncrypted) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(TableData.Chats.COLUMN_CHATS_USERCODE, userCode);
        contentValues.put(TableData.Chats.COLUMN_CHATS_ISENCRYPTED, String.valueOf(isEncrypted));
        sql.update(TableData.Chats.TABLE_CHATS, contentValues, "userCode =  ?", new String[] {String.valueOf(userCode)});
        notify(DataType.CHAT);
        return true;
    }

    /**
     * Gets user code.
     *
     * @return the user code
     */
    public String getUserCode() {
        return super.getUserCode(sql);
    }

    /**
     * Gets session hash.
     *
     * @return the session hash
     */
    public String getSessionHash() {
        return super.getSessionHash(sql);
    }

    /**
     * Converts cursor to Message
     *
     * @param cursor the cursor
     * @return the message
     */
    private Message cursorToMessage(Cursor cursor) {
        return new Message(cursor.getLong(0), cursor.getString(1), cursor.getString(2), cursor.getString(3), Boolean.valueOf(cursor.getString(4)), getUserCode());
    }

    /**
     * Converts cursor to Contact
     *
     * @param cursor the cursor
     * @return the contact
     */
    private Contact cursorToContact(Cursor cursor) {
        return new Contact(cursor.getString(0), cursor.getString(1), cursor.getString(2), Boolean.valueOf(cursor.getString(3)));
    }

    /**
     * Converts cursor to Chat
     *
     * @param cursor the cursor
     * @return the chat
     */
    private Chat cursorToChat(Cursor cursor) {
        return new Chat(getContactByID(cursor.getString(0)), getAllMessagesByContact(cursor.getString(0)),Boolean.valueOf(cursor.getString(1)));
    }

    /**
     * Gets sql database
     *
     * @return the sql
     */
    public SQLiteDatabase getSql() {
        return sql;
    }

}
