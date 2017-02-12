package internetofeveryone.ioe.Model;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import static internetofeveryone.ioe.Model.TableData.Chats.COLUMN_CHATS_ID;
import static internetofeveryone.ioe.Model.TableData.Chats.COLUMN_CHATS_ISENCRYPTED;
import static internetofeveryone.ioe.Model.TableData.Chats.COLUMN_CHATS_USERCODE;
import static internetofeveryone.ioe.Model.TableData.Chats.TABLE_CHATS;
import static internetofeveryone.ioe.Model.TableData.Contacts.COLUMN_CONTACTS_ID;
import static internetofeveryone.ioe.Model.TableData.Contacts.COLUMN_CONTACTS_KEY;
import static internetofeveryone.ioe.Model.TableData.Contacts.COLUMN_CONTACTS_NAME;
import static internetofeveryone.ioe.Model.TableData.Contacts.COLUMN_CONTACTS_OPENCHAT;
import static internetofeveryone.ioe.Model.TableData.Contacts.COLUMN_CONTACTS_USERCODE;
import static internetofeveryone.ioe.Model.TableData.Contacts.TABLE_CONTACTS;
import static internetofeveryone.ioe.Model.TableData.DefaultWebsites.COLUMN_DEFAULT_CONTENT;
import static internetofeveryone.ioe.Model.TableData.DefaultWebsites.COLUMN_DEFAULT_ID;
import static internetofeveryone.ioe.Model.TableData.DefaultWebsites.COLUMN_DEFAULT_NAME;
import static internetofeveryone.ioe.Model.TableData.DefaultWebsites.COLUMN_DEFAULT_URL;
import static internetofeveryone.ioe.Model.TableData.DefaultWebsites.TABLE_DEFAULT;
import static internetofeveryone.ioe.Model.TableData.DownloadedWebsites.COLUMN_DOWNLOADED_CONTENT;
import static internetofeveryone.ioe.Model.TableData.DownloadedWebsites.COLUMN_DOWNLOADED_ID;
import static internetofeveryone.ioe.Model.TableData.DownloadedWebsites.COLUMN_DOWNLOADED_NAME;
import static internetofeveryone.ioe.Model.TableData.DownloadedWebsites.COLUMN_DOWNLOADED_URL;
import static internetofeveryone.ioe.Model.TableData.DownloadedWebsites.TABLE_DOWNLOADED;
import static internetofeveryone.ioe.Model.TableData.Messages.COLUMN_MESSAGES_CONTENT;
import static internetofeveryone.ioe.Model.TableData.Messages.COLUMN_MESSAGES_ID;
import static internetofeveryone.ioe.Model.TableData.Messages.COLUMN_MESSAGES_ISENCRYPTED;
import static internetofeveryone.ioe.Model.TableData.Messages.COLUMN_MESSAGES_RECEIVERID;
import static internetofeveryone.ioe.Model.TableData.Messages.COLUMN_MESSAGES_SENDERID;
import static internetofeveryone.ioe.Model.TableData.Messages.TABLE_MESSAGES;
import static internetofeveryone.ioe.Model.TableData.SessionHash.COLUMN_SESSIONHASH_ID;
import static internetofeveryone.ioe.Model.TableData.SessionHash.COLUMN_SESSIONHASH_SESSIONHASH;
import static internetofeveryone.ioe.Model.TableData.SessionHash.TABLE_SESSIONHASH;
import static internetofeveryone.ioe.Model.TableData.UserCode.COLUMN_USERCODE_ID;
import static internetofeveryone.ioe.Model.TableData.UserCode.COLUMN_USERCODE_USERCODE;
import static internetofeveryone.ioe.Model.TableData.UserCode.TABLE_USERCODE;

/**
 * This class creates and managaes the database
 */
public class DataBase extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "Database.db";
    private static final int DATABASE_VERSION = 1;

    // strings to create the tables
    private static final String SQL_CREATE_DOWNLOADED = "CREATE TABLE " + TableData.DownloadedWebsites.TABLE_DOWNLOADED + "("
            + COLUMN_DOWNLOADED_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + COLUMN_DOWNLOADED_NAME + " TEXT NOT NULL, "
            + COLUMN_DOWNLOADED_URL + " TEXT NOT NULL, "
            + COLUMN_DOWNLOADED_CONTENT + " TEXT NOT NULL "
            +");";

    private static final String SQL_CREATE_DEFAULT = "CREATE TABLE " + TableData.DefaultWebsites.TABLE_DEFAULT + "("
            + COLUMN_DEFAULT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + COLUMN_DEFAULT_NAME + " TEXT NOT NULL, "
            + COLUMN_DEFAULT_URL + " TEXT NOT NULL, "
            + COLUMN_DEFAULT_CONTENT + " TEXT NOT NULL "
            +");";

    private static final String SQL_CREATE_CONTACTS = "CREATE TABLE " + TableData.Contacts.TABLE_CONTACTS + "("
            + COLUMN_CONTACTS_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + COLUMN_CONTACTS_NAME + " TEXT NOT NULL, "
            + COLUMN_CONTACTS_USERCODE + " TEXT UNIQUE, "
            + COLUMN_CONTACTS_KEY + " TEXT NOT NULL, "
            + COLUMN_CONTACTS_OPENCHAT + " TEXT NOT NULL "
            +");";

    private static final String SQL_CREATE_CHATS = "CREATE TABLE " + TableData.Chats.TABLE_CHATS + "("
            + COLUMN_CHATS_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + COLUMN_CHATS_USERCODE + " TEXT UNIQUE, "
            + COLUMN_CHATS_ISENCRYPTED + " TEXT "
            +");";

    private static final String SQL_CREATE_MESSAGES = "CREATE TABLE " + TableData.Messages.TABLE_MESSAGES + "("
            + COLUMN_MESSAGES_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + COLUMN_MESSAGES_SENDERID + " TEXT NOT NULL, "
            + COLUMN_MESSAGES_RECEIVERID + " TEXT NOT NULL, "
            + COLUMN_MESSAGES_CONTENT + " TEXT NOT NULL, "
            + COLUMN_MESSAGES_ISENCRYPTED + " TEXT NOT NULL "
            +");";

    private static final String SQL_CREATE_USERCODE = "CREATE TABLE " + TableData.UserCode.TABLE_USERCODE + "("
            + COLUMN_USERCODE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + COLUMN_USERCODE_USERCODE + " TEXT UNIQUE "
            +");";

    private static final String SQL_CREATE_SESSIONHASH = "CREATE TABLE " + TableData.SessionHash.TABLE_SESSIONHASH + "("
            + COLUMN_SESSIONHASH_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + COLUMN_SESSIONHASH_SESSIONHASH + " TEXT UNIQUE "
            +");";

    /**
     * Instantiates a new Data base.
     *
     * @param context the context
     */
    public DataBase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_DOWNLOADED);
        db.execSQL(SQL_CREATE_DEFAULT);
        db.execSQL(SQL_CREATE_CONTACTS);
        db.execSQL(SQL_CREATE_CHATS);
        db.execSQL(SQL_CREATE_MESSAGES);
        db.execSQL(SQL_CREATE_USERCODE);
        db.execSQL(SQL_CREATE_SESSIONHASH);
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // clear all data
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_DOWNLOADED);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_DEFAULT);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CONTACTS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CHATS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MESSAGES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERCODE);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SESSIONHASH);

        // recreate the tables
        onCreate(db);
    }
    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }



}
