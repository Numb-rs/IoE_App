package internetofeveryone.ioe.Model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import static internetofeveryone.ioe.Model.TableData.DownloadedWebsites.COL_1;
import static internetofeveryone.ioe.Model.TableData.DownloadedWebsites.COL_2;
import static internetofeveryone.ioe.Model.TableData.DownloadedWebsites.COL_3;

public class DataBase extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String SQL_CREATE_DOWNLOADED = "CREATE TABLE " + TableData.DownloadedWebsites.TABLE_NAME + " (" +
            COL_1+ " TEXT UNIQUE," +
            COL_2 + " TEXT UNIQUE," +
             COL_3+ " TEXT)";
    private static final String SQL_CREATE_USERCODE = "CREATE TABLE " + TableData.UserCode.TABLE_NAME + " (" +
            COL_1 + " INTEGER PRIMARY KEY)"; // korrekte Bezeichnung?

    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + TableData.DownloadedWebsites.TABLE_NAME;

    public DataBase(Context context) {
        super(context, TableData.DownloadedWebsites.DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_DOWNLOADED);
        db.execSQL(SQL_CREATE_USERCODE);
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }
    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }

    public boolean insertDownloadedWebsite(String name, String url, String content) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_1, name);
        contentValues.put(COL_2, url);
        contentValues.put(COL_3, content);
        long result = db.insert(TableData.DownloadedWebsites.TABLE_NAME, null, contentValues);
        return (result != -1);
    }

    public Cursor getDownloadedWebsite() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from " + TableData.DownloadedWebsites.TABLE_NAME, null);
        return res;
    }

    public boolean updateDownloadedWebsite(String name, String url, String content) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_1, name);
        contentValues.put(COL_2, url);
        contentValues.put(COL_3, content);
        db.update(TableData.DownloadedWebsites.TABLE_NAME, contentValues, "URL =  ?", new String[] {url});
        return true;
    }

    public Integer deleteDownloadedWebsite(String url) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TableData.DownloadedWebsites.TABLE_NAME, "URL = ?", new String[] {url});
    }

    public boolean insertUserCode(int userCode) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_1, userCode);
        long result = db.insert(TableData.UserCode.TABLE_NAME, null, contentValues);
        return (result != -1);
    }

    public Cursor getUserCode() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from " + TableData.UserCode.TABLE_NAME, null);
        return res;
    }

}
