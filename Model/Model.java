package internetofeveryone.ioe.Model;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

import internetofeveryone.ioe.Data.DataType;
import internetofeveryone.ioe.Presenter.ModelObserver;

public abstract class Model {

    private static ArrayList<ModelObserver> observers = null;
    public static final String DEFAULTUSERCODE = "0-0";

    public Model () {
        if (observers == null) {
            observers = new ArrayList<>();
        }
    }

    public void notify(DataType type) {
        for (ModelObserver o : observers) {
            o.update(type);
        }
    }

    public void addObserver(ModelObserver o) {
        this.observers.add(o);
    }

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

    public String getSessionHash(SQLiteDatabase sql) {
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

    public boolean insertSessionHash(String sessionHash, SQLiteDatabase sql) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(TableData.SessionHash.COLUMN_SESSIONHASH_SESSIONHASH, sessionHash);
        long result = sql.insert(TableData.SessionHash.TABLE_SESSIONHASH, null, contentValues);
        notify(DataType.USERCODE);
        return (result != -1);
    }

}
