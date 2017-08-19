package mb.boardgametimer.sqlite;

import android.provider.BaseColumns;

public class ActionReaderContract {

    private ActionReaderContract(){
    }

    public static class ActionEntry implements BaseColumns {
        public static final String TABLE_NAME = "action";
        public static final String COLUMN_NAME_TYPE = "type";
        public static final String COLUMN_NAME_TIMESTAMP = "timestamp";
    }
}
