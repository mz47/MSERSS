package uni.mse.mserss;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by marcel on 19.05.17.
 * Supports Application to establish SQLite Connections
 */

public class DbHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "mserss.db";
    private static final int DATABASE_VERSION = 3;

    public static final String TABLE_CHANNEL = "channel";
    public static final String TABLE_LIST = "list";

    public static final String CHANNEL_ID = "id";
    public static final String CHANNEL_URL = "url";

    public static final String LIST_ID = "id";
    public static final String LIST_NAME = "name";

    private static final String CREATE_TABLE_CHANNEL = "CREATE TABLE " + TABLE_CHANNEL + " (" + CHANNEL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + CHANNEL_URL + " TEXT NOT NULL);";
    private static final String DROP_TABLE_CHANNEL = "DROP TABLE IF EXISTS " + TABLE_CHANNEL + ";";
    private static final String INSERT_TABLE_CHANNEL = "INSERT INTO " + TABLE_CHANNEL + " (" + CHANNEL_URL + ") VALUES ('https://rss.golem.de/rss.php?feed=RSS2.0');";

    private static final String CREATE_TABLE_LIST = "CREATE TABLE "+ TABLE_LIST +" ("+ LIST_ID +" INTEGER PRIMARY KEY AUTOINCREMENT, "+ LIST_NAME +" TEXT NOT NULL);";
    private static final String DROP_TABLE_LIST = "DROP TABLE IF EXISTS " + TABLE_LIST + ";";
    private static final String INSERT_TABLE_LIST = "INSERT INTO "+ TABLE_LIST +" ("+ LIST_NAME +") VALUES ('TestListe')";

    public DbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_CHANNEL);
        db.execSQL(INSERT_TABLE_CHANNEL); // Dummy
        db.execSQL(INSERT_TABLE_CHANNEL); // Dummy
        db.execSQL(CREATE_TABLE_LIST);
        db.execSQL(INSERT_TABLE_LIST); // Dummy
        db.execSQL(INSERT_TABLE_LIST); // Dummy
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(DROP_TABLE_CHANNEL);
        db.execSQL(DROP_TABLE_LIST);
        onCreate(db);
    }

    public ChannelList getChannels() {
        SQLiteDatabase database = this.getWritableDatabase();
        ChannelList channels = new ChannelList();
        Cursor c = database.rawQuery("SELECT * FROM channel", null);
        while(c.moveToNext()) {
            Channel channel = new Channel();
            channel.setUrl(c.getString(c.getColumnIndexOrThrow(CHANNEL_URL)));
            channels.add(channel);
        }
        c.moveToFirst();

        return channels;
    }

    public Channel getChannel(int id) {
        SQLiteDatabase database = this.getWritableDatabase();
        Channel channel = new Channel();
        Cursor cursor = database.rawQuery("SELECT * FROM " + TABLE_CHANNEL + " WHERE " + CHANNEL_ID + "=" + id, null);
        cursor.moveToFirst();
        channel.setUrl(cursor.getString(cursor.getColumnIndexOrThrow(CHANNEL_URL)));
        channel.parse();
        return channel;
    }

    public ListList getLists() {
        SQLiteDatabase database = this.getWritableDatabase();
        ListList lists = new ListList();
        Cursor c = database.rawQuery("SELECT * FROM "+ TABLE_LIST +";", null);
        while(c.moveToNext()) {
            List list = new List();
            list.setName(c.getString(c.getColumnIndexOrThrow(LIST_NAME)));
            lists.add(list);
        }
        c.moveToFirst();
        return lists;
    }
}
