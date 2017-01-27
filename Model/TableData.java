package internetofeveryone.ioe.Model;

import android.provider.BaseColumns;

/**
 * The type Table data.
 */
public class TableData {

    /**
     * The type Downloaded websites.
     */
    public static abstract class DownloadedWebsites implements BaseColumns {

        public static final String TABLE_DOWNLOADED = "downloadedWebsites_table";
        public static final String COLUMN_DOWNLOADED_ID = "id";
        public static final String COLUMN_DOWNLOADED_NAME = "name";
        public static final String COLUMN_DOWNLOADED_URL = "url";
        public static final String COLUMN_DOWNLOADED_CONTENT = "content";
    }

    /**
     * The type Default websites.
     */
    public static abstract class DefaultWebsites implements BaseColumns {

        public static final String TABLE_DEFAULT = "defaultWebsites_table";
        public static final String COLUMN_DEFAULT_ID = "id";
        public static final String COLUMN_DEFAULT_NAME = "name";
        public static final String COLUMN_DEFAULT_URL = "url";
        public static final String COLUMN_DEFAULT_CONTENT = "content";
    }

    /**
     * The type Contacts.
     */
    public static abstract class Contacts implements BaseColumns {

        public static final String TABLE_CONTACTS = "contacts_table";
        public static final String COLUMN_CONTACTS_ID = "id";
        public static final String COLUMN_CONTACTS_NAME = "name";
        public static final String COLUMN_CONTACTS_USERCODE = "userCode";
        public static final String COLUMN_CONTACTS_KEY = "key";
        public static final String COLUMN_CONTACTS_OPENCHAT = "openChat";
    }

    /**
     * The type Messages.
     */
    public static abstract class Messages implements BaseColumns {

        public static final String TABLE_MESSAGES = "messages_table";
        public static final String COLUMN_MESSAGES_ID = "id";
        public static final String COLUMN_MESSAGES_SENDERID = "senderID";
        public static final String COLUMN_MESSAGES_RECEIVERID = "receiverID";
        public static final String COLUMN_MESSAGES_CONTENT = "content";
        public static final String COLUMN_MESSAGES_ISENCRYPTED = "isEncrypted";
    }

    /**
     * The type User code.
     */
    public static abstract class UserCode implements BaseColumns {

        public static final String TABLE_USERCODE = "usercode_table";
        public static final String COLUMN_USERCODE_ID = "id";
        public static final String COLUMN_USERCODE_USERCODE = "userCode";
    }
}
