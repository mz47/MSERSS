package uni.mse.mserss;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by marcel on 19.05.17.
 * Supports Application to establish SQLite Connections
 */

public class DbHelper extends SQLiteOpenHelper {

    private static final String NAME = "mserss.db";
    private static final int VERSION = 2;
    public static final String TABLE = "channel";
    public static final String COL_ID = "id";
    public static final String COL_URL = "url";

    private static final String CREATE = "CREATE TABLE " + TABLE + " (" + COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + COL_URL + " TEXT NOT NULL);";
    private static final String DROP = "DROP TABLE " + TABLE + ";";
    private static final String INSERT = "INSERT INTO " + TABLE + " (" + COL_URL + ") VALUES ('https://rss.golem.de/rss.php?feed=RSS2.0');";

    public DbHelper(Context context) {
        super(context, NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //db.execSQL(DROP);
        db.execSQL(CREATE);
        db.execSQL(INSERT); // Dummy
        db.execSQL(INSERT); // Dummy
        db.execSQL(INSERT); // Dummy
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
