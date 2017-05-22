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
        Log.d("dbsource.open", "Pfad zur Datenbank: " + database.getPath());
    }

    public void close() {
        helper.close();
    }

    public ChannelList getChannels() {
        ChannelList channels = new ChannelList();

        Cursor c = database.rawQuery("SELECT * FROM channel", null);
        while(c.moveToNext()) {
            Channel channel = new Channel();
            channel.setUrl(c.getString(c.getColumnIndexOrThrow(helper.COL_URL)));
            channels.add(channel);
        }
        c.moveToFirst();

        return channels;
    }

    public Channel getChannel(int id) {
        Channel channel = new Channel();
        Cursor cursor = database.rawQuery("SELECT * FROM " + helper.TABLE + " WHERE " + helper.COL_ID + "=" + id, null);
        cursor.moveToFirst();
        channel.setUrl(cursor.getString(cursor.getColumnIndexOrThrow(helper.COL_URL)));
        channel.parse();
        return channel;
    }
}
