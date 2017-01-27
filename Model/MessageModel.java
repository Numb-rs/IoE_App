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

import internetofeveryone.ioe.Data.Contact;
import internetofeveryone.ioe.Data.Message;

public class MessageModel extends Model {

    private SQLiteDatabase sql;
    private DataBase db;
    private Context context;
    private String[] messageColumns = { TableData.Messages.COLUMN_MESSAGES_ID,
            TableData.Messages.COLUMN_MESSAGES_SENDERID, TableData.Messages.COLUMN_MESSAGES_RECEIVERID,
            TableData.Messages.COLUMN_MESSAGES_CONTENT, TableData.Messages.COLUMN_MESSAGES_ISENCRYPTED };
    private String[] contactColumns = { TableData.Contacts.COLUMN_CONTACTS_ID,
            TableData.Contacts.COLUMN_CONTACTS_NAME, TableData.Contacts.COLUMN_CONTACTS_USERCODE,
            TableData.Contacts.COLUMN_CONTACTS_KEY, TableData.Contacts.COLUMN_CONTACTS_OPENCHAT };


    public MessageModel(Context context) {
        super(context);
        this.context = context;
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

    public void addMessage(long senderID, long receiverID, String content, boolean isEncrypted) {
        ContentValues values = new ContentValues();
        values.put(TableData.Messages.COLUMN_MESSAGES_SENDERID, String.valueOf(senderID));
        values.put(TableData.Messages.COLUMN_MESSAGES_RECEIVERID, String.valueOf(receiverID));
        values.put(TableData.Messages.COLUMN_MESSAGES_CONTENT, content);
        values.put(TableData.Messages.COLUMN_MESSAGES_ISENCRYPTED, String.valueOf(isEncrypted));

        sql.insert(TableData.Messages.TABLE_MESSAGES, null, values);
    }

    public void addContact(String name, long userCode, String key, boolean openChat) {

        ContentValues values = new ContentValues();
        values.put(TableData.Contacts.COLUMN_CONTACTS_NAME, name);
        values.put(TableData.Contacts.COLUMN_CONTACTS_USERCODE, String.valueOf(userCode));
        values.put(TableData.Contacts.COLUMN_CONTACTS_KEY, name);
        values.put(TableData.Contacts.COLUMN_CONTACTS_OPENCHAT, String.valueOf(openChat));

        sql.insert(TableData.Contacts.TABLE_CONTACTS, null, values);
    }

    public void deleteMessage(long id) {
        System.out.println("the deleted message has the id: " + id);
        sql.delete(TableData.Messages.TABLE_MESSAGES, TableData.Messages.COLUMN_MESSAGES_ID
                + " = " + id, null);
    }

    public void deleteContact(long id) {
        System.out.println("the deleted contact has the id: " + id);
        sql.delete(TableData.Contacts.TABLE_CONTACTS, TableData.Contacts.COLUMN_CONTACTS_ID
                + " = " + id, null);
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

    public Contact getContactByID(long id) {
        Cursor cursor = sql.query(TableData.Contacts.TABLE_CONTACTS, contactColumns,
                TableData.Contacts.COLUMN_CONTACTS_ID + " = ?",
                new String[] { String.valueOf(id) }, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
        }

        Contact contact = cursorToContact(cursor);
        return contact;
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
                messages.add(msg);
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
                messages.add(msg);
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

    public boolean updateContact(String name, long userCode, String key, boolean openChat) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(TableData.Contacts.COLUMN_CONTACTS_NAME, name);
        contentValues.put(TableData.Contacts.COLUMN_CONTACTS_USERCODE, String.valueOf(userCode));
        contentValues.put(TableData.Contacts.COLUMN_CONTACTS_KEY, name);
        contentValues.put(TableData.Contacts.COLUMN_CONTACTS_OPENCHAT, String.valueOf(openChat));
        sql.update(TableData.Contacts.TABLE_CONTACTS, contentValues, "userCode =  ?", new String[] {String.valueOf(userCode)});
        return true;
    }

    protected Message cursorToMessage(Cursor cursor) {
        Message msg = new Message(cursor.getLong(0), cursor.getLong(1), cursor.getLong(2), cursor.getString(3), Boolean.valueOf(cursor.getString(4)));
        return msg;
    }

    protected Contact cursorToContact(Cursor cursor) {
        Contact contact = new Contact(cursor.getString(0), cursor.getLong(1), cursor.getString(2), Boolean.valueOf(cursor.getString(3)));
        return contact;
    }

}
