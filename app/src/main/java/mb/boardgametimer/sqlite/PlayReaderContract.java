package mb.boardgametimer.sqlite;

import android.provider.BaseColumns;

public class PlayReaderContract {

    private PlayReaderContract() {
    }

    public static class PlayEntry implements BaseColumns {
        public static final String TABLE_NAME = "play";
        public static final String COLUMN_NAME_DURATION = "duration";
        public static final String COLUMN_NAME_TEXT = "text";
    }
}
