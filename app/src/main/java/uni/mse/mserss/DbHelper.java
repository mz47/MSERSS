package uni.mse.mserss;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by marcel on 19.05.17.
 * Supports Application to establish SQLite Connections
 * and proceed CRUD Operations
 */

public class DbHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "mserss.db";
    private static final int DATABASE_VERSION = 14;

    public static final String TABLE_CHANNEL = "channel";

    public static final String CHANNEL_ID = "id";
    public static final String CHANNEL_REFRESH = "refresh";
    public static final String CHANNEL_URL = "url";
    public static final String CHANNEL_TITLE = "title";

    private static final String CREATE_TABLE_CHANNEL = "CREATE TABLE "+ TABLE_CHANNEL +" ("+ CHANNEL_ID +" INTEGER PRIMARY KEY AUTOINCREMENT, "+ CHANNEL_REFRESH +" INTEGER, "+ CHANNEL_URL +" TEXT NOT NULL, "+ CHANNEL_TITLE +" TEXT NOT NULL);";
    private static final String DROP_TABLE_CHANNEL = "DROP TABLE IF EXISTS " + TABLE_CHANNEL + ";";
    private static final String INSERT_CHANNEL_DUMMY1 = "INSERT INTO "+ TABLE_CHANNEL +" ("+ CHANNEL_REFRESH +", "+ CHANNEL_URL +", "+ CHANNEL_TITLE +") VALUES (1, 'https://rss.golem.de/rss.php?tp=games&feed=RSS2.0', 'Golem Games');";
    private static final String INSERT_CHANNEL_DUMMY2 = "INSERT INTO "+ TABLE_CHANNEL +" ("+ CHANNEL_REFRESH +", "+ CHANNEL_URL +", "+ CHANNEL_TITLE +") VALUES (1, 'https://rss.golem.de/rss.php?tp=pol&feed=RSS2.0', 'Golem Politik');";
    private static final String INSERT_CHANNEL_DUMMY3 = "INSERT INTO "+ TABLE_CHANNEL +" ("+ CHANNEL_REFRESH +", "+ CHANNEL_URL +", "+ CHANNEL_TITLE +") VALUES (1, 'https://rss.golem.de/rss.php?tp=wirtschaft&feed=RSS2.0', 'Golem Wirtschaft');";
    private static final String INSERT_CHANNEL_DUMMY4 = "INSERT INTO "+ TABLE_CHANNEL +" ("+ CHANNEL_REFRESH +", "+ CHANNEL_URL +", "+ CHANNEL_TITLE +") VALUES (1, 'https://rss.golem.de/rss.php?tp=sec&feed=RSS2.0', 'Golem Security');";
    private static final String INSERT_CHANNEL_DUMMY5 = "INSERT INTO "+ TABLE_CHANNEL +" ("+ CHANNEL_REFRESH +", "+ CHANNEL_URL +", "+ CHANNEL_TITLE +") VALUES (1, 'http://beta.mpdevelop.de/mpdevelop.rss', 'mpdev test rss');";

    public DbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_CHANNEL);
        db.execSQL(INSERT_CHANNEL_DUMMY1); // Dummy
        db.execSQL(INSERT_CHANNEL_DUMMY2); // Dummy
        db.execSQL(INSERT_CHANNEL_DUMMY3); // Dummy
        db.execSQL(INSERT_CHANNEL_DUMMY4); // Dummy
        db.execSQL(INSERT_CHANNEL_DUMMY5); // Dummy
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(DROP_TABLE_CHANNEL);
        onCreate(db);
    }
    // Get all Channels
    public ChannelList getChannels() {
        try {
            SQLiteDatabase database = this.getWritableDatabase();
            ChannelList channels = new ChannelList();
            Cursor c = database.rawQuery("SELECT * FROM "+ TABLE_CHANNEL +";", null);
            while(c.moveToNext()) {
                Channel channel = new Channel();
                channel.setId(c.getInt(c.getColumnIndexOrThrow(CHANNEL_ID)));
                channel.setUrl(c.getString(c.getColumnIndexOrThrow(CHANNEL_URL)));
                channel.setTitle(c.getString(c.getColumnIndexOrThrow(CHANNEL_TITLE)));
                channel.setRefresh(c.getInt(c.getColumnIndexOrThrow(CHANNEL_REFRESH)));
                channels.add(channel);
            }
            c.moveToFirst();
            return channels;
        }
        catch (SQLiteException ex) {
            Log.e("getChannels", ex.toString());
            return null;
        }
        catch (Exception ex) {
            Log.e("getChannels", ex.toString());
            return null;
        }
    }
    // Get specific Channel
    public Channel getChannel(int id) {
        try {
            SQLiteDatabase database = this.getWritableDatabase();
            Channel channel = new Channel();
            Cursor cursor = database.rawQuery("SELECT * FROM " + TABLE_CHANNEL + " WHERE " + CHANNEL_ID + " = " + id + ";", null);
            cursor.moveToFirst();
            channel.setId(id);
            channel.setUrl(cursor.getString(cursor.getColumnIndexOrThrow(CHANNEL_URL)));
            channel.getItems();
            return channel;
        }
        catch (SQLiteException ex) {
            Log.e("getChannel", ex.toString());
            return null;
        }
        catch (Exception ex) {
            Log.e("getChannel", ex.toString());
            return null;
        }
    }
    // Add new Channel to Database
    public void addChannel(Channel c) {
        try {
            if(c != null) {
                SQLiteDatabase database = this.getWritableDatabase();
                database.execSQL("INSERT INTO "+ TABLE_CHANNEL +" ("+ CHANNEL_REFRESH +", "+ CHANNEL_URL +", "+ CHANNEL_TITLE +") VALUES ("+ c.getRefresh() +", '"+ c.getUrl() +"', '"+ c.getTitle() +"');");
            }
            else {
                Log.d("addChannel", "c empty");
            }
        }
        catch (SQLiteException ex) {
            Log.e("addChannel", ex.toString());
        }
        catch (Exception ex) {
            Log.e("addChannel", ex.toString());
        }
    }
    // Remove Channel from Database
    public void removeChannel(int id) {
        try {
            if(id >= 0) {
                SQLiteDatabase database = this.getWritableDatabase();
                database.execSQL("DELETE FROM "+ TABLE_CHANNEL +" WHERE id = " + id + ";");
                Log.d("removeChannel", "removed channel with id " + id + ";");
            }
        }
        catch (SQLiteException ex) {
            Log.e("removeChannel", ex.toString());
        }
        catch (Exception ex) {
            Log.e("removeChannel", ex.toString());
        }
    }
}
