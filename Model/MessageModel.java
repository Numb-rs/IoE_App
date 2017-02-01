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
    private Context context;
    private String[] messageColumns = { TableData.Messages.COLUMN_MESSAGES_ID,
            TableData.Messages.COLUMN_MESSAGES_SENDERID, TableData.Messages.COLUMN_MESSAGES_RECEIVERID,
            TableData.Messages.COLUMN_MESSAGES_CONTENT, TableData.Messages.COLUMN_MESSAGES_ISENCRYPTED };
    private String[] contactColumns = { TableData.Contacts.COLUMN_CONTACTS_NAME, TableData.Contacts.COLUMN_CONTACTS_USERCODE,
            TableData.Contacts.COLUMN_CONTACTS_KEY, TableData.Contacts.COLUMN_CONTACTS_OPENCHAT };
    private String[] chatColumns = { TableData.Chats.COLUMN_CHATS_USERCODE, TableData.Chats.COLUMN_CHATS_ISENCRYPTED };

    public MessageModel(Context context) {
        super();
        this.context = context;
        db = new DataBase(context);

        try {
            open();
        } catch (SQLException e) {
            // ErrorHandler
        }
        insertUserCode(readUserCodeFromFile(sql), sql);
    }

    public void open() throws SQLException {
        sql = db.getWritableDatabase();
    }

    public void close() {
        sql.close();
    }

    public void addMessage(long senderID, long receiverID, String content, boolean isEncrypted) {

        ContentValues values = new ContentValues();
        values.put(TableData.Messages.COLUMN_MESSAGES_SENDERID, String.valueOf(senderID));
        values.put(TableData.Messages.COLUMN_MESSAGES_RECEIVERID, String.valueOf(receiverID));
        values.put(TableData.Messages.COLUMN_MESSAGES_CONTENT, content);
        values.put(TableData.Messages.COLUMN_MESSAGES_ISENCRYPTED, String.valueOf(isEncrypted));

        long id = sql.insert(TableData.Messages.TABLE_MESSAGES, null, values);
        notify(DataType.MESSAGE, "" + id);
    }

    public void addContact(String name, long userCode, String key, boolean openChat) {

        ContentValues values = new ContentValues();
        values.put(TableData.Contacts.COLUMN_CONTACTS_NAME, name);
        values.put(TableData.Contacts.COLUMN_CONTACTS_USERCODE, String.valueOf(userCode));
        values.put(TableData.Contacts.COLUMN_CONTACTS_KEY, key);
        values.put(TableData.Contacts.COLUMN_CONTACTS_OPENCHAT, String.valueOf(openChat));

        sql.insert(TableData.Contacts.TABLE_CONTACTS, null, values);
        notify(DataType.CONTACT, "" + userCode);
    }


    public void addChat(long userCode, boolean isEncrypted) {

        ContentValues values = new ContentValues();
        values.put(TableData.Chats.COLUMN_CHATS_USERCODE, userCode);
        values.put(TableData.Chats.COLUMN_CHATS_ISENCRYPTED, String.valueOf(isEncrypted));

        sql.insert(TableData.Chats.TABLE_CHATS, null, values);
        notify(DataType.CHAT, "" + userCode);
    }


    public void deleteMessage(long id) {
        sql.delete(TableData.Messages.TABLE_MESSAGES, TableData.Messages.COLUMN_MESSAGES_ID
                + " = " + id, null);
        notify(DataType.MESSAGE, "" + id);
    }

    public void deleteContact(long userCode) { // TODO: ERROR abfangen zB wenn eintrag nicht unique ist
        int numRowsChanged = sql.delete(TableData.Contacts.TABLE_CONTACTS, TableData.Contacts.COLUMN_CONTACTS_USERCODE
                + " = " + userCode, null);
        if (numRowsChanged != 0) {
            deleteChat(userCode);
        }
        notify(DataType.CONTACT, "" + userCode);
    }

    public void deleteChat(long userCode) {
        sql.delete(TableData.Chats.TABLE_CHATS, TableData.Chats.COLUMN_CHATS_USERCODE
                + " = " + userCode, null);
        notify(DataType.CHAT, "" + userCode);
    }

    public List<Message> getAllMessages() {
        List<Message> messages = new ArrayList<>();

        Cursor cursor = sql.query(TableData.Messages.TABLE_MESSAGES, messageColumns,
                null, null, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                Message msg = cursorToMessage(cursor);
                messages.add(msg);
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
                contacts.add(contact);
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
                chats.add(chat);
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
            cursor.moveToFirst();
        }

        Message msg = cursorToMessage(cursor);
        return msg;
    }

    public Contact getContactByID(long userCode) {
        Cursor cursor = sql.query(TableData.Contacts.TABLE_CONTACTS, contactColumns,
                TableData.Contacts.COLUMN_CONTACTS_USERCODE + " = ?",
                new String[] { String.valueOf(userCode) }, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
        }

        Contact contact = cursorToContact(cursor);
        return contact;
    }

    public Chat getChatByID(long userCode) {
        Cursor cursor = sql.query(TableData.Chats.TABLE_CHATS, chatColumns,
                TableData.Chats.COLUMN_CHATS_USERCODE + " = ?",
                new String[] { String.valueOf(userCode) }, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
        }

        Chat chat = cursorToChat(cursor);
        return chat;
    }

    public TreeMap<Long, Message> getAllMessagesByContact(long userCode) {
        HashSet<Message> messages = new HashSet<>();

        Cursor cursorReceive = sql.query(TableData.Messages.TABLE_MESSAGES, messageColumns,
                TableData.Messages.COLUMN_MESSAGES_RECEIVERID + " = ?",
                new String[] { String.valueOf(userCode) }, null, null, null);
        if (cursorReceive != null) {
            cursorReceive.moveToFirst();
            while (!cursorReceive.isAfterLast()) {
                Message msg = cursorToMessage(cursorReceive);
                if (msg.getReceiverID() == getUserCode() || msg.getSenderID() == getUserCode()) {
                    messages.add(msg);
                }
                cursorReceive.moveToNext();
            }
            cursorReceive.close();
        }

        Cursor cursorSend = sql.query(TableData.Messages.TABLE_MESSAGES, messageColumns,
                TableData.Messages.COLUMN_MESSAGES_SENDERID + " = ?",
                new String[] { String.valueOf(userCode) }, null, null, null);
        if (cursorSend != null) {
            cursorSend.moveToFirst();
            while (!cursorSend.isAfterLast()) {
                Message msg = cursorToMessage(cursorSend);
                if (msg.getReceiverID() == getUserCode() || msg.getSenderID() == getUserCode()) {
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

    public boolean updateContact(long userCode, String name, long newUserCode, String key, boolean openChat) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(TableData.Contacts.COLUMN_CONTACTS_NAME, name);
        contentValues.put(TableData.Contacts.COLUMN_CONTACTS_USERCODE, String.valueOf(newUserCode));
        contentValues.put(TableData.Contacts.COLUMN_CONTACTS_KEY, key);
        contentValues.put(TableData.Contacts.COLUMN_CONTACTS_OPENCHAT, String.valueOf(openChat));
        sql.update(TableData.Contacts.TABLE_CONTACTS, contentValues, "userCode =  ?", new String[] {String.valueOf(userCode)});
        notify(DataType.CONTACT, "" + userCode);
        return true;
    }

    public boolean updateChat(long userCode, boolean isEncrypted) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(TableData.Chats.COLUMN_CHATS_USERCODE, String.valueOf(userCode));
        contentValues.put(TableData.Chats.COLUMN_CHATS_ISENCRYPTED, String.valueOf(isEncrypted));
        sql.update(TableData.Chats.TABLE_CHATS, contentValues, "userCode =  ?", new String[] {String.valueOf(userCode)});
        notify(DataType.CHAT, "" + userCode);
        return true;
    }

    public long getUserCode() {
        return super.getUserCode(sql);
    }

    protected Message cursorToMessage(Cursor cursor) {
        System.out.println("Message: 0 = " + cursor.getLong(0) + ",1 = " + cursor.getLong(1) + ",2 = " + cursor.getLong(2) + ",3 = " + cursor.getString(3) + ",4 = " + Boolean.valueOf(cursor.getString(4)) + ", userCode = " + getUserCode());
        Message msg = new Message(cursor.getLong(0), cursor.getLong(1), cursor.getLong(2), cursor.getString(3), Boolean.valueOf(cursor.getString(4)), getUserCode());
        return msg;
    }

    protected Contact cursorToContact(Cursor cursor) {
        System.out.println("Contact: 0 = " + cursor.getString(0) + ",1 = " + cursor.getLong(1) + ",2 = " + cursor.getString(2) + ",3 = " + Boolean.valueOf(cursor.getString(3)));
        Contact contact = new Contact(cursor.getString(0), cursor.getLong(1), cursor.getString(2), Boolean.valueOf(cursor.getString(3)));
        return contact;
    }

    protected Chat cursorToChat(Cursor cursor) {
        System.out.println("Chat: 0 = " + cursor.getLong(0) + ",1 = " + Boolean.valueOf(cursor.getString(1)));
        Chat chat = new Chat(getContactByID(cursor.getLong(0)), getAllMessagesByContact(cursor.getLong(0)),Boolean.valueOf(cursor.getString(1)));
        return chat;
    }

}
