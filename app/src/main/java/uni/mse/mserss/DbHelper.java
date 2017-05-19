package uni.mse.mserss;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by marcel on 19.05.17.
 * Supports Application to establish SQLite Connections
 */

public class DbHelper extends SQLiteOpenHelper {

    private static final String NAME = "sqlite.db";
    private static final int VERSION = 1;

    private static final String CREATE = "CREATE TABLE channels (id INTEGER PRIMARY KEY AUTOINCREMENT, url TEXT NOT NULL);";
    private static final String INSERT = "INSERT INTO channels (url) VALUES ('www.google.de');";

    public DbHelper(Context context) {
        super(context, NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE);
        db.execSQL(INSERT);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
