package internetofeveryone.ioe.Model;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import internetofeveryone.ioe.Data.DataType;
import internetofeveryone.ioe.Presenter.ModelObserver;

public abstract class Model {

    private static ArrayList<ModelObserver> observers = null;

    public Model () {
        if (observers == null) {
            observers = new ArrayList<>();
        }
    }

    public void notify(DataType type, String url) {
        for (ModelObserver o : observers) {
            o.update(type, url);
        }
    }

    public void addObserver(ModelObserver o) {
        this.observers.add(o);
    }

    public long readUserCodeFromFile(SQLiteDatabase sql) {

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

    public long getUserCode(SQLiteDatabase sql) {
        long userCode = -1;
        Cursor cursor = sql.query(TableData.UserCode.TABLE_USERCODE,
                new String[] { TableData.UserCode.COLUMN_USERCODE_USERCODE }, null, null, null, null, null);
        if (cursor.getCount() != 0) {
            cursor.moveToFirst();
            userCode = Long.valueOf(cursor.getString(0));
            cursor.close();
        }
        return userCode;
    }

    public boolean insertUserCode(long userCode, SQLiteDatabase sql) {
        if (getUserCode(sql) != -1) {
            // already inserted
            return true;
        }
        ContentValues contentValues = new ContentValues();
        contentValues.put(TableData.UserCode.COLUMN_USERCODE_USERCODE, userCode);
        long result = sql.insert(TableData.UserCode.TABLE_USERCODE, null, contentValues);
        notify(DataType.USERCODE, "" + userCode);
        return (result != -1);
    }

}
