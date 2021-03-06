package internetofeveryone.ioe.Model;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

import internetofeveryone.ioe.Data.DataType;
import internetofeveryone.ioe.Presenter.ModelObserver;

/**
 * The type Model.
 */
public abstract class Model {

    private static ArrayList<ModelObserver> observers = null;
    /**
     * The default user code
     */
    public static final String DEFAULTUSERCODE = "0-0";

    /**
     * Instantiates a new Model.
     */
    Model() {
        if (observers == null) {
            observers = new ArrayList<>();
        }
    }

    /**
     * Notifies all ModelObservers that a change in the database occurred
     *
     * @param type the type
     */
    public void notify(DataType type) {
        for (ModelObserver o : observers) {
            o.update(type);
        }
    }

    /**
     * Adds an observer to the observerlist
     *
     * @param o the o
     */
    public void addObserver(ModelObserver o) {
        observers.add(o);
    }

    /**
     * Gets my user code from database
     *
     * @param sql the sql
     * @return the user code
     */
    public String getUserCode(SQLiteDatabase sql) {
        String userCode = DEFAULTUSERCODE;
        Cursor cursor = sql.query(TableData.UserCode.TABLE_USERCODE,
                new String[] { TableData.UserCode.COLUMN_USERCODE_USERCODE }, null, null, null, null, null);
        if (cursor.getCount() != 0) {
            cursor.moveToFirst();
            userCode = cursor.getString(0);
            cursor.close();
        }
        return userCode;
    }

    /**
     * Gets my session hash from database
     *
     * @param sql the sql
     * @return the session hash
     */
    String getSessionHash(SQLiteDatabase sql) {
        String sessionHash = "";
        Cursor cursor = sql.query(TableData.SessionHash.TABLE_SESSIONHASH,
                new String[] { TableData.SessionHash.COLUMN_SESSIONHASH_SESSIONHASH }, null, null, null, null, null);
        if (cursor.getCount() != 0) {
            cursor.moveToFirst();
            sessionHash = cursor.getString(0);
            cursor.close();
        }
        return sessionHash;
    }

    /**
     * Inserts user code in the databse
     *
     * @param userCode the user code
     * @param sql      the sql
     * @return the boolean
     */
    public boolean insertUserCode(String userCode, SQLiteDatabase sql) {
        if (!getUserCode(sql).equals(DEFAULTUSERCODE)) {
            // already inserted
            return true;
        }
        ContentValues contentValues = new ContentValues();
        contentValues.put(TableData.UserCode.COLUMN_USERCODE_USERCODE, userCode);
        long result = sql.insert(TableData.UserCode.TABLE_USERCODE, null, contentValues);
        notify(DataType.USERCODE);
        return (result != -1);
    }

    /**
     * Inserts session hash in the database
     *
     * @param sessionHash the session hash
     * @param sql         the sql
     * @return the boolean
     */
    public boolean insertSessionHash(String sessionHash, SQLiteDatabase sql) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(TableData.SessionHash.COLUMN_SESSIONHASH_SESSIONHASH, sessionHash);
        long result = sql.insert(TableData.SessionHash.TABLE_SESSIONHASH, null, contentValues);
        notify(DataType.USERCODE);
        return (result != -1);
    }

}
