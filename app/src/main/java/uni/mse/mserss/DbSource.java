package uni.mse.mserss;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * Created by marcel on 19.05.17.
 */

public class DbSource {
    private SQLiteDatabase database;
    private DbHelper helper;


    public DbSource(Context context) {
        helper = new DbHelper(context);
    }

    public void open() {
        database = helper.getWritableDatabase();
    }

    public void close() {
        helper.close();
    }

    public ArrayList<String> getChannels() {
        ArrayList<String> channels = new ArrayList<>();

        Cursor c = database.rawQuery("SELECT * FROM channels", null);
        c.moveToFirst();
        Log.d("dbsource.getchannels", c.getString(0));

        return channels;
    }
}
