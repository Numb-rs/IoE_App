package internetofeveryone.ioe.Model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import internetofeveryone.ioe.Data.DataType;
import internetofeveryone.ioe.Data.Website;


/**
 * The model for all website related data
 */
public class WebsiteModel extends Model {

    private SQLiteDatabase sql;
    private final DataBase db;
    private final String[] downloadColumns = { TableData.DownloadedWebsites.COLUMN_DOWNLOADED_NAME,
            TableData.DownloadedWebsites.COLUMN_DOWNLOADED_URL, TableData.DownloadedWebsites.COLUMN_DOWNLOADED_CONTENT };
    private final String[] defaultColumns = { TableData.DefaultWebsites.COLUMN_DEFAULT_NAME,
            TableData.DefaultWebsites.COLUMN_DEFAULT_URL, TableData.DefaultWebsites.COLUMN_DEFAULT_CONTENT };


    /**
     * Instantiates a new Website model.
     *
     * @param context the context
     */
    public WebsiteModel(Context context) {
        super();
        db = new DataBase(context);

        try {
            open();
        } catch (SQLException e) {
            e.printStackTrace();
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
     * Gets user code.
     *
     * @return the user code
     */
    public String getUserCode() {
        return super.getUserCode(sql);
    }

    /**
     * Adds a downloaded website to the database
     *
     * @param name    the name
     * @param url     the url
     * @param content the content
     * @return the boolean
     */
    public boolean addDownloadedWebsite(String name, String url, String content) {
        if (getDownloadedWebsiteByURL(url) != null) {
            return false;
        }
        ContentValues values = new ContentValues();
        values.put(TableData.DownloadedWebsites.COLUMN_DOWNLOADED_NAME, name);
        values.put(TableData.DownloadedWebsites.COLUMN_DOWNLOADED_URL, url); // url for searches is name of engine + spacebar + searchTerm
        values.put(TableData.DownloadedWebsites.COLUMN_DOWNLOADED_CONTENT, content);

        sql.insert(TableData.DownloadedWebsites.TABLE_DOWNLOADED, null, values);

        notify(DataType.WEBSITE);
        return true;
    }

    /**
     * Adds a default website to the database
     *
     * @param name    the name
     * @param url     the url
     * @param content the content
     * @return the boolean
     */
    public boolean addDefaultWebsite(String name, String url, String content) {
        if (getDefaultWebsiteByURL(url) != null) {
            return false;
        }
        ContentValues values = new ContentValues();
        values.put(TableData.DefaultWebsites.COLUMN_DEFAULT_NAME, name);
        values.put(TableData.DefaultWebsites.COLUMN_DEFAULT_URL, url);
        values.put(TableData.DefaultWebsites.COLUMN_DEFAULT_CONTENT, content);

        sql.insert(TableData.DefaultWebsites.TABLE_DEFAULT, null, values);
        notify(DataType.WEBSITE);
        return true;
    }

    /**
     * Deletes a downloaded website from the database
     *
     * @param url the url
     */
    public void deleteDownloadedWebsite(String url) {
        String[] whereArgs = new String[] { url };
        sql.delete(TableData.DownloadedWebsites.TABLE_DOWNLOADED, TableData.DownloadedWebsites.COLUMN_DOWNLOADED_URL
                + "=?", whereArgs);
        notify(DataType.WEBSITE);
    }

    /**
     * Deletes a default website from the database
     *
     * @param url the url
     */
    public void deleteDefaultWebsite(String url) {
        String[] whereArgs = new String[] { url };
        sql.delete(TableData.DefaultWebsites.TABLE_DEFAULT, TableData.DefaultWebsites.COLUMN_DEFAULT_URL
                + "=?", whereArgs);
        notify(DataType.WEBSITE);
    }

    /**
     * Gets all downloaded websites from the database
     *
     * @return the all downloaded websites
     */
    public List<Website> getAllDownloadedWebsites() {
        List<Website> websites = new ArrayList<>();

        Cursor cursor = sql.query(TableData.DownloadedWebsites.TABLE_DOWNLOADED, downloadColumns,
                null, null, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                Website website = cursorToWebsite(cursor);
                if (website != null) {
                    websites.add(website);
                }
                cursor.moveToNext();
            }
            cursor.close();
        }
        return websites;
    }

    /**
     * Gets all default websites from the database
     *
     * @return the all default websites
     */
    public List<Website> getAllDefaultWebsites() {
        List<Website> websites = new ArrayList<>();

        Cursor cursor = sql.query(TableData.DefaultWebsites.TABLE_DEFAULT, defaultColumns,
                null, null, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                Website website = cursorToWebsite(cursor);
                if (website != null) {
                    websites.add(website);
                }
                cursor.moveToNext();
            }
            cursor.close();
        }
        return websites;
    }

    /**
     * Gets downloaded website by url from the database
     *
     * @param url the url
     * @return the downloaded website by url
     */
    public Website getDownloadedWebsiteByURL(String url) {
        Cursor cursor = sql.query(TableData.DownloadedWebsites.TABLE_DOWNLOADED, downloadColumns,
                TableData.DownloadedWebsites.COLUMN_DOWNLOADED_URL + " = ?",
                new String[] { url }, null, null, null);
        if (cursor != null) {
            if(!cursor.moveToFirst()) {
                return null;
            }
        }
        if (cursor != null) {
            if (cursor.getCount() == 0) {
                return null;
            }
        }

        Website website = cursorToWebsite(cursor);
        if (cursor != null) {
            cursor.close();
        }
        return website;
    }

    /**
     * Gets default website by url from the database
     *
     * @param url the url
     * @return the default website by url
     */
    public Website getDefaultWebsiteByURL(String url) {
        Cursor cursor = sql.query(TableData.DefaultWebsites.TABLE_DEFAULT, defaultColumns,
                TableData.DefaultWebsites.COLUMN_DEFAULT_URL + " = ?",
                new String[] { url }, null, null, null);
        if (cursor != null) {
            if(!cursor.moveToFirst()) {
                return null;
            }
        }

        Website website = cursorToWebsite(cursor);
        if (cursor != null) {
            cursor.close();
        }
        return website;
    }

    /**
     * Updates downloaded website in the database
     *
     * @param name    the name
     * @param oldURL  the old url
     * @param newURL  the new url
     * @param content the content
     * @return the boolean
     */
    public boolean updateDownloadedWebsite(String name, String oldURL, String newURL, String content) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(TableData.DownloadedWebsites.COLUMN_DOWNLOADED_NAME, name);
        contentValues.put(TableData.DownloadedWebsites.COLUMN_DOWNLOADED_URL, newURL);
        contentValues.put(TableData.DownloadedWebsites.COLUMN_DOWNLOADED_CONTENT, content);
        sql.update(TableData.DownloadedWebsites.TABLE_DOWNLOADED, contentValues, "url =  ?", new String[] {oldURL});
        notify(DataType.WEBSITE);
        return true;
    }

    /**
     * Updates default website in the database
     *
     * @param name    the name
     * @param oldURL  the old url
     * @param newURL  the new url
     * @return the boolean
     */
    public boolean updateDefaultWebsite(String name, String oldURL, String newURL) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(TableData.DefaultWebsites.COLUMN_DEFAULT_NAME, name);
        contentValues.put(TableData.DefaultWebsites.COLUMN_DEFAULT_URL, newURL);
        contentValues.put(TableData.DefaultWebsites.COLUMN_DEFAULT_CONTENT, "");
        sql.update(TableData.DefaultWebsites.TABLE_DEFAULT, contentValues, "url =  ?", new String[] {oldURL});
        notify(DataType.WEBSITE);
        return true;
    }

    /**
     * Converts cursor to website
     *
     * @param cursor the cursor
     * @return the website
     */
    private Website cursorToWebsite(Cursor cursor) {
        return new Website(cursor.getString(0), cursor.getString(1), cursor.getString(2));
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
