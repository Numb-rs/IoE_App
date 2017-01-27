package internetofeveryone.ioe.Model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import internetofeveryone.ioe.Data.Website;


public class WebsiteModel extends Model {

    private SQLiteDatabase sql;
    private DataBase db;
    private Context context;
    private String[] downloadColumns = { TableData.DownloadedWebsites.COLUMN_DOWNLOADED_NAME,
            TableData.DownloadedWebsites.COLUMN_DOWNLOADED_URL, TableData.DownloadedWebsites.COLUMN_DOWNLOADED_CONTENT };
    private String[] defaultColumns = { TableData.DefaultWebsites.COLUMN_DEFAULT_NAME,
            TableData.DefaultWebsites.COLUMN_DEFAULT_URL, TableData.DefaultWebsites.COLUMN_DEFAULT_CONTENT };


    public WebsiteModel(Context context) {
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

    public void addDownloadedWebsite(String name, String url, String content) {
        ContentValues values = new ContentValues();
        values.put(TableData.DownloadedWebsites.COLUMN_DOWNLOADED_NAME, name);
        values.put(TableData.DownloadedWebsites.COLUMN_DOWNLOADED_URL, url);
        values.put(TableData.DownloadedWebsites.COLUMN_DOWNLOADED_CONTENT, content);

        sql.insert(TableData.DownloadedWebsites.TABLE_DOWNLOADED, null, values);
    }

    public void addDefaultWebsite(String name, String url, String content) {
        ContentValues values = new ContentValues();
        values.put(TableData.DefaultWebsites.COLUMN_DEFAULT_NAME, name);
        values.put(TableData.DefaultWebsites.COLUMN_DEFAULT_URL, url);
        values.put(TableData.DefaultWebsites.COLUMN_DEFAULT_CONTENT, content);

        sql.insert(TableData.DefaultWebsites.TABLE_DEFAULT, null, values);
    }

    public void deleteDownloadedWebsite(String url) {
        System.out.println("the downloaded website has the url: " + url);
        sql.delete(TableData.DownloadedWebsites.TABLE_DOWNLOADED, TableData.DownloadedWebsites.COLUMN_DOWNLOADED_URL
                + " = " + url, null);
    }

    public void deleteDefaultWebsite(String url) {
        System.out.println("the default website has the url: " + url);
        sql.delete(TableData.DefaultWebsites.TABLE_DEFAULT, TableData.DefaultWebsites.COLUMN_DEFAULT_URL
                + " = " + url, null);
    }

    public List<Website> getAllDownloadedWebsites() {
        List<Website> websites = new ArrayList<>();

        Cursor cursor = sql.query(TableData.DownloadedWebsites.TABLE_DOWNLOADED, downloadColumns,
                null, null, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                Website website = cursorToWebsite(cursor);
                websites.add(website);
                cursor.moveToNext();
            }
            cursor.close();
        }
        return websites;
    }

    public List<Website> getAllDefaultWebsites() {
        List<Website> websites = new ArrayList<>();

        Cursor cursor = sql.query(TableData.DefaultWebsites.TABLE_DEFAULT, defaultColumns,
                null, null, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                Website website = cursorToWebsite(cursor);
                websites.add(website);
                cursor.moveToNext();
            }
            cursor.close();
        }
        return websites;
    }

    public Website getDownloadedWebsiteByURL(String url) {
        Cursor cursor = sql.query(TableData.DownloadedWebsites.TABLE_DOWNLOADED, downloadColumns,
                TableData.DownloadedWebsites.COLUMN_DOWNLOADED_URL + " = ?",
                new String[] { url }, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
        }

        Website website = cursorToWebsite(cursor);
        return website;
    }

    public Website getDefaultWebsiteByURL(String url) {
        Cursor cursor = sql.query(TableData.DefaultWebsites.TABLE_DEFAULT, defaultColumns,
                TableData.DefaultWebsites.COLUMN_DEFAULT_URL + " = ?",
                new String[] { url }, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
        }

        Website website = cursorToWebsite(cursor);
        return website;
    }

    public boolean updateDownloadedWebsite(String name, String oldURL, String newURL, String content) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(TableData.DownloadedWebsites.COLUMN_DOWNLOADED_NAME, name);
        contentValues.put(TableData.DownloadedWebsites.COLUMN_DOWNLOADED_URL, newURL);
        contentValues.put(TableData.DownloadedWebsites.COLUMN_DOWNLOADED_CONTENT, content);
        sql.update(TableData.DownloadedWebsites.TABLE_DOWNLOADED, contentValues, "url =  ?", new String[] {oldURL});
        return true;
    }

    protected Website cursorToWebsite(Cursor cursor) {
        Website website = new Website(cursor.getString(0), cursor.getString(1), cursor.getString(2));
        return website;
    }


}
