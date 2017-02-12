package internetofeveryone.ioe.Model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.TreeMap;

import internetofeveryone.ioe.Data.Chat;
import internetofeveryone.ioe.Data.Contact;
import internetofeveryone.ioe.Data.DataType;
import internetofeveryone.ioe.Data.Message;

public class MessageModel extends Model {

    private SQLiteDatabase sql;
    private DataBase db;
    private String[] messageColumns = { TableData.Messages.COLUMN_MESSAGES_ID,
            TableData.Messages.COLUMN_MESSAGES_SENDERID, TableData.Messages.COLUMN_MESSAGES_RECEIVERID,
            TableData.Messages.COLUMN_MESSAGES_CONTENT, TableData.Messages.COLUMN_MESSAGES_ISENCRYPTED };
    private String[] contactColumns = { TableData.Contacts.COLUMN_CONTACTS_NAME, TableData.Contacts.COLUMN_CONTACTS_USERCODE,
            TableData.Contacts.COLUMN_CONTACTS_KEY, TableData.Contacts.COLUMN_CONTACTS_OPENCHAT };
    private String[] chatColumns = { TableData.Chats.COLUMN_CHATS_USERCODE, TableData.Chats.COLUMN_CHATS_ISENCRYPTED };

    public MessageModel(Context context) {
        super();
        db = new DataBase(context);

        try {
            open();
        } catch (SQLException e) {
            // ErrorHandler
        }
    }

    public void open() throws SQLException {
        sql = db.getWritableDatabase();
    }

    public void close() {
        sql.close();
    }

    public void addMessage(String senderID, String receiverID, String content, boolean isEncrypted) {

        ContentValues values = new ContentValues();
        values.put(TableData.Messages.COLUMN_MESSAGES_SENDERID, senderID);
        values.put(TableData.Messages.COLUMN_MESSAGES_RECEIVERID, receiverID);
        values.put(TableData.Messages.COLUMN_MESSAGES_CONTENT, content);
        values.put(TableData.Messages.COLUMN_MESSAGES_ISENCRYPTED, String.valueOf(isEncrypted));

        long id = sql.insert(TableData.Messages.TABLE_MESSAGES, null, values);
        notify(DataType.MESSAGE);
    }

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


    public void deleteMessage(long id) {
        sql.delete(TableData.Messages.TABLE_MESSAGES, TableData.Messages.COLUMN_MESSAGES_ID
                + " = " + id, null);
        notify(DataType.MESSAGE);
    }

    public void deleteContact(String userCode) {
        int numRowsChanged = sql.delete(TableData.Contacts.TABLE_CONTACTS, TableData.Contacts.COLUMN_CONTACTS_USERCODE
                + " = " + userCode, null);
        if (numRowsChanged != 0) {
            deleteChat(userCode);
            deleteAllMessagesForContact(userCode);
        }
        notify(DataType.CONTACT);
    }

    public void deleteChat(String userCode) {
        sql.delete(TableData.Chats.TABLE_CHATS, TableData.Chats.COLUMN_CHATS_USERCODE
                + " = " + userCode, null);
        notify(DataType.CHAT);
    }

    public void deleteAllMessagesForContact(String userCode) {
        TreeMap<Long, Message> messages = getAllMessagesByContact(userCode);
        for (Long id : messages.keySet()) {
            deleteMessage(id);
        }
    }

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
        cursor.close();
        return msg;
    }

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
        cursor.close();
        return contact;
    }

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
        cursor.close();
        return chat;
    }

    public TreeMap<Long, Message> getAllMessagesByContact(String userCode) {
        HashSet<Message> messages = new HashSet<>();

        Cursor cursorReceive = sql.query(TableData.Messages.TABLE_MESSAGES, messageColumns,
                TableData.Messages.COLUMN_MESSAGES_RECEIVERID + " = ?",
                new String[] { userCode }, null, null, null);
        if (cursorReceive != null) {
            cursorReceive.moveToFirst();
            while (!cursorReceive.isAfterLast()) {
                Message msg = cursorToMessage(cursorReceive);
                if (msg.getReceiverID().equals(getUserCode()) || msg.getSenderID().equals(getUserCode())) {
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
                if (msg.getReceiverID().equals(getUserCode()) || msg.getSenderID().equals(getUserCode())) {
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

    public boolean updateContact(String userCode, String name, String newUserCode, String key, boolean openChat) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(TableData.Contacts.COLUMN_CONTACTS_NAME, name);
        contentValues.put(TableData.Contacts.COLUMN_CONTACTS_USERCODE, newUserCode);
        contentValues.put(TableData.Contacts.COLUMN_CONTACTS_KEY, key);
        contentValues.put(TableData.Contacts.COLUMN_CONTACTS_OPENCHAT, String.valueOf(openChat));
        sql.update(TableData.Contacts.TABLE_CONTACTS, contentValues, "userCode =  ?", new String[] {userCode});
        notify(DataType.CONTACT);
        notify(DataType.CHAT);
        return true;
    }

    public boolean updateChat(String userCode, boolean isEncrypted) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(TableData.Chats.COLUMN_CHATS_USERCODE, userCode);
        contentValues.put(TableData.Chats.COLUMN_CHATS_ISENCRYPTED, String.valueOf(isEncrypted));
        sql.update(TableData.Chats.TABLE_CHATS, contentValues, "userCode =  ?", new String[] {String.valueOf(userCode)});
        notify(DataType.CHAT);
        return true;
    }

    public String getUserCode() {
        return super.getUserCode(sql);
    }

    public String getSessionHash() {
        return super.getSessionHash(sql);
    }

    protected Message cursorToMessage(Cursor cursor) {
        Message msg = new Message(cursor.getLong(0), cursor.getString(1), cursor.getString(2), cursor.getString(3), Boolean.valueOf(cursor.getString(4)), getUserCode());
        return msg;
    }

    protected Contact cursorToContact(Cursor cursor) {
        Contact contact = new Contact(cursor.getString(0), cursor.getString(1), cursor.getString(2), Boolean.valueOf(cursor.getString(3)));
        return contact;
    }

    protected Chat cursorToChat(Cursor cursor) {
        Chat chat = new Chat(getContactByID(cursor.getString(0)), getAllMessagesByContact(cursor.getString(0)),Boolean.valueOf(cursor.getString(1)));
        return chat;
    }

    public SQLiteDatabase getSql() {
        return sql;
    }

}
