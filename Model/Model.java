package internetofeveryone.ioe.Model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import internetofeveryone.ioe.Data.DataType;
import internetofeveryone.ioe.Presenter.ModelObserver;

public abstract class Model {


    private Context context;
    private SQLiteDatabase sql;
    private DataBase db;
    private ArrayList<ModelObserver> observers = new ArrayList<>();

    public Model(Context context) {
        this.context = context;
        db = new DataBase(context);

        try {
            open();
        } catch (SQLException e) {
            // ErrorHandler
        }

        insertUserCode(readUserCodeFromFile());
    }

    public void open() throws SQLException {
        sql = db.getWritableDatabase();
    }

    public void close() {
        sql.close();
    }

    public void notify(DataType type, String url) {
        for (ModelObserver o : observers) {
            o.update(type, url);
        }
    }

    public void addObserver(ModelObserver o) {
        this.observers.add(o);
    }

    public long readUserCodeFromFile() {

        File file = new File("usercodeApp.txt");

        //Read text from file
        StringBuilder text = new StringBuilder();

        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line;

            while ((line = br.readLine()) != null) {
                text.append(line);
                text.append('\n');
            }
            br.close();
        }
        catch (IOException e) {
            // Error handling
        }
        return 12345L; // Long.valueOf(text.toString());
    }

    public long getUserCode() {
        long userCode = -1;
        Cursor cursor = sql.query(TableData.UserCode.TABLE_USERCODE,
                new String[] {TableData.UserCode.COLUMN_USERCODE_ID, TableData.UserCode.COLUMN_USERCODE_USERCODE}, null, null, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
            userCode = Long.valueOf(cursor.getString(0));
            cursor.close();
        }
        return userCode;
    }

    public boolean insertUserCode(long userCode) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(TableData.UserCode.COLUMN_USERCODE_USERCODE, userCode);
        long result = sql.insert(TableData.UserCode.TABLE_USERCODE, null, contentValues);
        return (result != -1);
    }

}
