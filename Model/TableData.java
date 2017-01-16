package internetofeveryone.ioe.Model;

import android.provider.BaseColumns;

public class TableData {

    public static abstract class DownloadedWebsites implements BaseColumns {
        public static final String DATABASE_NAME = "DownloadedWebsites.db";
        public static final String TABLE_NAME = "downloadedWebsites_table";
        public static final String COL_1 = "Name";
        public static final String COL_2 = "URL";
        public static final String COL_3 = "Content";
    }

    public static abstract class DefaultWebsites implements BaseColumns {
        public static final String DATABASE_NAME = "DefaultWebsites.db";
        public static final String TABLE_NAME = "defaultWebsites_table";
        public static final String COL_1 = "Name";
        public static final String COL_2 = "URL";
        public static final String COL_3 = "Content";
    }

    public static abstract class Contacts implements BaseColumns {
        public static final String DATABASE_NAME = "Contacts.db";
        public static final String TABLE_NAME = "contacts_table";
        public static final String COL_1 = "Name";
        public static final String COL_2 = "UserCode";
        public static final String COL_3 = "Key";
        public static final String COL_4 = "openChat";
    }

    public static abstract class Chats implements BaseColumns {
        public static final String DATABASE_NAME = "Chats.db";
        public static final String TABLE_NAME = "chats_table";
        public static final String COL_1 = "UserCode";
    }

    public static abstract class Messages implements BaseColumns {
        public static final String DATABASE_NAME = "Messages.db";
        public static final String TABLE_NAME = "messages_table";
        public static final String COL_1 = "ID";
        public static final String COL_2 = "SenderID";
        public static final String COL_3 = "ReceiverID";
        public static final String COL_4 = "Content";
        public static final String COL_5 = "isEncrypted";
    }

    public static abstract class UserCode implements BaseColumns {
        public static final String DATABASE_NAME = "UserCode.db";
        public static final String TABLE_NAME = "usercode_table";
        public static final String COL_1 = "UserCode";
    }
}
