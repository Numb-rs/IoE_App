package internetofeveryone.ioe.ModelTests;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import internetofeveryone.ioe.BuildConfig;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertTrue;

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class)
public class SQLiteDatabaseTest {

    private TestOpenHelper helper;

    @Before
    public void setUp() throws Exception {
        helper = new TestOpenHelper(RuntimeEnvironment.application, "name", null, 1);
    }

    @Test
    public void getReadableDatabaseSuccessful() throws Exception {
        SQLiteDatabase database = helper.getReadableDatabase();
        assertNotNull(database);
        assertTrue(database.isOpen());
        assertTrue(helper.onOpenCalled);
        assertFalse(helper.onUpgradeCalled);
    }

    @Test
    public void getWriteableDatabaseSuccessful() throws Exception {
        SQLiteDatabase database = helper.getWritableDatabase();
        assertNotNull(database);
        assertTrue(database.isOpen());
        assertTrue(helper.onOpenCalled);
        assertFalse(helper.onUpgradeCalled);
    }

    @Test
    public void reOpenWriteableDatabase_reusesOldOne() throws Exception {
        helper.getWritableDatabase();
        helper.close();
        SQLiteDatabase database = helper.getWritableDatabase();
        assertNotNull(database);
        assertTrue(database.isOpen());
        assertTrue(helper.onOpenCalled);
        assertFalse(helper.onUpgradeCalled);
        assertFalse(helper.onCreateCalled);
    }

    @Test
    public void reOpenReadableDatabase_reusesOldOne() throws Exception {
        helper.getReadableDatabase();
        helper.close();
        SQLiteDatabase database = helper.getReadableDatabase();
        assertNotNull(database);
        assertTrue(database.isOpen());
        assertTrue(helper.onOpenCalled);
        assertFalse(helper.onUpgradeCalled);
        assertFalse(helper.onCreateCalled);
    }

    @Test
    public void openSameWriteableDatabaseTwice_bothEqual() throws Exception {
        SQLiteDatabase db1 = helper.getWritableDatabase();
        SQLiteDatabase db2 = helper.getWritableDatabase();
        assertEquals(db1, db2);
    }

    @Test
    public void openSameReadableDatabaseTwice_bothEqual() throws Exception {
        SQLiteDatabase db1 = helper.getReadableDatabase();
        SQLiteDatabase db2 = helper.getReadableDatabase();
        assertEquals(db1, db2);
    }

    @Test
    public void testCloseWriteableDatabase() throws Exception {
        SQLiteDatabase database = helper.getWritableDatabase();
        assertTrue(database.isOpen());
        helper.close();
        assertFalse(database.isOpen());
    }

    @Test
    public void testCloseReadableDatabase() throws Exception {
        SQLiteDatabase database = helper.getReadableDatabase();
        assertTrue(database.isOpen());
        helper.close();
        assertFalse(database.isOpen());
    }

    @After
    public void tearDown() throws Exception {
        helper.close();

    }

    private class TestOpenHelper extends SQLiteOpenHelper {
        public boolean onCreateCalled;
        public boolean onUpgradeCalled;
        public boolean onOpenCalled;

        public TestOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
            super(context, name, factory, version);
        }

        @Override
        public void onCreate(SQLiteDatabase database) {
            onCreateCalled = true;
        }

        @Override
        public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
            onUpgradeCalled = true;
        }

        @Override
        public void onOpen(SQLiteDatabase database) {
            onOpenCalled = true;
        }

        @Override
        public synchronized void close() {
            onCreateCalled = false;
            onUpgradeCalled = false;
            onOpenCalled = false;
            super.close();
        }
    }
}
