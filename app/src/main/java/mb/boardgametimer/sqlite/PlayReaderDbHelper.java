package mb.boardgametimer.sqlite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class PlayReaderDbHelper extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "PlayReader.db";

    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + PlayReaderContract.PlayEntry.TABLE_NAME + " (" +
                    PlayReaderContract.PlayEntry._ID + " INTEGER PRIMARY KEY," +
                    PlayReaderContract.PlayEntry.COLUMN_NAME_DURATION + " INTEGER," +
                    PlayReaderContract.PlayEntry.COLUMN_NAME_TEXT + " TEXT," +
                    PlayReaderContract.PlayEntry.COLUMN_NAME_TIMESTAMP + " TEXT)";

    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + PlayReaderContract.PlayEntry.TABLE_NAME;

    public PlayReaderDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(SQL_CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        sqLiteDatabase.execSQL(SQL_DELETE_ENTRIES);
        onCreate(sqLiteDatabase);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }
}
