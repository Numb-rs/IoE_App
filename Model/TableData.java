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

        /**
         * The constant TABLE_DOWNLOADED.
         */
        public static final String TABLE_DOWNLOADED = "downloadedWebsites_table";
        /**
         * The constant COLUMN_DOWNLOADED_ID.
         */
        public static final String COLUMN_DOWNLOADED_ID = "id";
        /**
         * The constant COLUMN_DOWNLOADED_NAME.
         */
        public static final String COLUMN_DOWNLOADED_NAME = "name";
        /**
         * The constant COLUMN_DOWNLOADED_URL.
         */
        public static final String COLUMN_DOWNLOADED_URL = "url";
        /**
         * The constant COLUMN_DOWNLOADED_CONTENT.
         */
        public static final String COLUMN_DOWNLOADED_CONTENT = "content";
    }

    /**
     * The type Default websites.
     */
    public static abstract class DefaultWebsites implements BaseColumns {

        /**
         * The constant TABLE_DEFAULT.
         */
        public static final String TABLE_DEFAULT = "defaultWebsites_table";
        /**
         * The constant COLUMN_DEFAULT_ID.
         */
        public static final String COLUMN_DEFAULT_ID = "id";
        /**
         * The constant COLUMN_DEFAULT_NAME.
         */
        public static final String COLUMN_DEFAULT_NAME = "name";
        /**
         * The constant COLUMN_DEFAULT_URL.
         */
        public static final String COLUMN_DEFAULT_URL = "url";
        /**
         * The constant COLUMN_DEFAULT_CONTENT.
         */
        public static final String COLUMN_DEFAULT_CONTENT = "content";
    }

    /**
     * The type Contacts.
     */
    public static abstract class Contacts implements BaseColumns {

        /**
         * The constant TABLE_CONTACTS.
         */
        public static final String TABLE_CONTACTS = "contacts_table";
        /**
         * The constant COLUMN_CONTACTS_ID.
         */
        public static final String COLUMN_CONTACTS_ID = "id";
        /**
         * The constant COLUMN_CONTACTS_NAME.
         */
        public static final String COLUMN_CONTACTS_NAME = "name";
        /**
         * The constant COLUMN_CONTACTS_USERCODE.
         */
        public static final String COLUMN_CONTACTS_USERCODE = "userCode";
        /**
         * The constant COLUMN_CONTACTS_KEY.
         */
        public static final String COLUMN_CONTACTS_KEY = "key";
        /**
         * The constant COLUMN_CONTACTS_OPENCHAT.
         */
        public static final String COLUMN_CONTACTS_OPENCHAT = "openChat";
    }

    /**
     * The type Chats.
     */
    public static abstract class Chats implements BaseColumns {

        /**
         * The constant TABLE_CHATS.
         */
        public static final String TABLE_CHATS = "chats_table";
        /**
         * The constant COLUMN_CHATS_ID.
         */
        public static final String COLUMN_CHATS_ID = "id";
        /**
         * The constant COLUMN_CHATS_USERCODE.
         */
        public static final String COLUMN_CHATS_USERCODE = "userCode";
        /**
         * The constant COLUMN_CHATS_ISENCRYPTED.
         */
        public static final String COLUMN_CHATS_ISENCRYPTED = "isEncrypted";
    }

    /**
     * The type Messages.
     */
    public static abstract class Messages implements BaseColumns {

        /**
         * The constant TABLE_MESSAGES.
         */
        public static final String TABLE_MESSAGES = "messages_table";
        /**
         * The constant COLUMN_MESSAGES_ID.
         */
        public static final String COLUMN_MESSAGES_ID = "id";
        /**
         * The constant COLUMN_MESSAGES_SENDERID.
         */
        public static final String COLUMN_MESSAGES_SENDERID = "senderID";
        /**
         * The constant COLUMN_MESSAGES_RECEIVERID.
         */
        public static final String COLUMN_MESSAGES_RECEIVERID = "receiverID";
        /**
         * The constant COLUMN_MESSAGES_CONTENT.
         */
        public static final String COLUMN_MESSAGES_CONTENT = "content";
        /**
         * The constant COLUMN_MESSAGES_ISENCRYPTED.
         */
        public static final String COLUMN_MESSAGES_ISENCRYPTED = "isEncrypted";
    }

    /**
     * The type User code.
     */
    public static abstract class UserCode implements BaseColumns {

        /**
         * The constant TABLE_USERCODE.
         */
        public static final String TABLE_USERCODE = "usercode_table";
        /**
         * The constant COLUMN_USERCODE_ID.
         */
        public static final String COLUMN_USERCODE_ID = "id";
        /**
         * The constant COLUMN_USERCODE_USERCODE.
         */
        public static final String COLUMN_USERCODE_USERCODE = "userCode";
    }

    /**
     * The type Session hash.
     */
    public static abstract class SessionHash implements BaseColumns {

        /**
         * The constant TABLE_SESSIONHASH.
         */
        public static final String TABLE_SESSIONHASH = "sessionhash_table";
        /**
         * The constant COLUMN_SESSIONHASH_ID.
         */
        public static final String COLUMN_SESSIONHASH_ID = "id";
        /**
         * The constant COLUMN_SESSIONHASH_SESSIONHASH.
         */
        public static final String COLUMN_SESSIONHASH_SESSIONHASH = "sessionHash";
    }
}
