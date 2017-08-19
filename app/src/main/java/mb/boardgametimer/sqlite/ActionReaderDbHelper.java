package mb.boardgametimer.sqlite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class ActionReaderDbHelper extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "ActionReader.db";

    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + ActionReaderContract.ActionEntry.TABLE_NAME + " (" +
                    ActionReaderContract.ActionEntry._ID + " INTEGER PRIMARY KEY," +
                    ActionReaderContract.ActionEntry.COLUMN_NAME_TYPE + " TEXT," +
                    ActionReaderContract.ActionEntry.COLUMN_NAME_TIMESTAMP + " INTEGER)";

    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + ActionReaderContract.ActionEntry.TABLE_NAME;

    public ActionReaderDbHelper(Context context) {
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
