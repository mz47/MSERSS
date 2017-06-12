package uni.mse.mserss;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by marcel on 19.05.17.
 * Supports Application to establish SQLite Connections
 * and proceed CRUD Operations
 */

public class DbHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "mserss.db";
    private static final int DATABASE_VERSION = 10;

    public static final String TABLE_CHANNEL = "channel";
    public static final String TABLE_LIST = "list";

    public static final String CHANNEL_ID = "id";
    public static final String CHANNEL_LIST_ID = "listid";
    public static final String CHANNEL_URL = "url";
    public static final String CHANNEL_TITLE = "title";

    public static final String LIST_ID = "id";
    public static final String LIST_NAME = "name";

    private static final String CREATE_TABLE_CHANNEL = "CREATE TABLE "+ TABLE_CHANNEL +" ("+ CHANNEL_ID +" INTEGER PRIMARY KEY AUTOINCREMENT, "+ CHANNEL_LIST_ID +" INTEGER, "+ CHANNEL_URL +" TEXT NOT NULL, "+ CHANNEL_TITLE +" TEXT NOT NULL);";
    private static final String DROP_TABLE_CHANNEL = "DROP TABLE IF EXISTS " + TABLE_CHANNEL + ";";
    private static final String INSERT_CHANNEL_DUMMY1 = "INSERT INTO "+ TABLE_CHANNEL +" ("+ CHANNEL_URL +", "+ CHANNEL_TITLE +") VALUES ('https://rss.golem.de/rss.php?tp=games&feed=RSS2.0', 'Golem Games');";
    private static final String INSERT_CHANNEL_DUMMY2 = "INSERT INTO "+ TABLE_CHANNEL +" ("+ CHANNEL_URL +", "+ CHANNEL_TITLE +") VALUES ('https://rss.golem.de/rss.php?tp=pol&feed=RSS2.0', 'Golem Politik');";
    private static final String INSERT_CHANNEL_DUMMY3 = "INSERT INTO "+ TABLE_CHANNEL +" ("+ CHANNEL_URL +", "+ CHANNEL_TITLE +") VALUES ('https://rss.golem.de/rss.php?tp=wirtschaft&feed=RSS2.0', 'Golem Wirtschaft');";
    private static final String INSERT_CHANNEL_DUMMY4 = "INSERT INTO "+ TABLE_CHANNEL +" ("+ CHANNEL_URL +", "+ CHANNEL_TITLE +") VALUES ('https://rss.golem.de/rss.php?tp=sec&feed=RSS2.0', 'Golem Security');";

    private static final String CREATE_TABLE_LIST = "CREATE TABLE "+ TABLE_LIST +" ("+ LIST_ID +" INTEGER PRIMARY KEY AUTOINCREMENT, "+ LIST_NAME +" TEXT NOT NULL);";
    private static final String DROP_TABLE_LIST = "DROP TABLE IF EXISTS " + TABLE_LIST + ";";
    private static final String INSERT_LIST_DUMMY1 = "INSERT INTO "+ TABLE_LIST +" ("+ LIST_NAME +") VALUES ('IT-News')";
    private static final String INSERT_LIST_DUMMY2 = "INSERT INTO "+ TABLE_LIST +" ("+ LIST_NAME +") VALUES ('Nachrichten')";

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
        db.execSQL(CREATE_TABLE_LIST);
        db.execSQL(INSERT_LIST_DUMMY1); // Dummy
        db.execSQL(INSERT_LIST_DUMMY2); // Dummy
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(DROP_TABLE_CHANNEL);
        db.execSQL(DROP_TABLE_LIST);
        onCreate(db);
    }

    // Get Channels without linked Colletion
    public ChannelList getChannels() {
        SQLiteDatabase database = this.getWritableDatabase();
        ChannelList channels = new ChannelList();
        Cursor c = database.rawQuery("SELECT * FROM "+ TABLE_CHANNEL +" WHERE "+ CHANNEL_LIST_ID +" IS NULL", null);
        while(c.moveToNext()) {
            Channel channel = new Channel();
            channel.setId(c.getInt(c.getColumnIndexOrThrow(CHANNEL_ID)));
            channel.setUrl(c.getString(c.getColumnIndexOrThrow(CHANNEL_URL)));
            channel.setTitle(c.getString(c.getColumnIndexOrThrow(CHANNEL_TITLE)));
            channels.add(channel);
        }
        c.moveToFirst();
        return channels;
    }

    // Get Channels by Collection ID
    public ChannelList getChannels(int listId) {
        SQLiteDatabase database = this.getWritableDatabase();
        ChannelList channels = new ChannelList();
        Cursor c = database.rawQuery("SELECT * FROM "+ TABLE_CHANNEL +" WHERE "+ CHANNEL_LIST_ID +" = " + listId, null);
        while(c.moveToNext()) {
            Channel channel = new Channel();
            channel.setId(c.getInt(c.getColumnIndexOrThrow(CHANNEL_ID)));
            channel.setUrl(c.getString(c.getColumnIndexOrThrow(CHANNEL_URL)));
            channel.setTitle(c.getString(c.getColumnIndexOrThrow(CHANNEL_TITLE)));
            channels.add(channel);
        }
        c.moveToFirst();
        return channels;
    }

    public Channel getChannel(int id) {
        SQLiteDatabase database = this.getWritableDatabase();
        Channel channel = new Channel();
        Cursor cursor = database.rawQuery("SELECT * FROM " + TABLE_CHANNEL + " WHERE " + CHANNEL_ID + " = " + id + ";", null);
        cursor.moveToFirst();
        channel.setId(id);
        channel.setUrl(cursor.getString(cursor.getColumnIndexOrThrow(CHANNEL_URL)));
        channel.getItems();
        return channel;
    }

    public CollectionList getLists() {
        SQLiteDatabase database = this.getWritableDatabase();
        CollectionList lists = new CollectionList();
        Cursor c = database.rawQuery("SELECT * FROM "+ TABLE_LIST +";", null);
        while(c.moveToNext()) {
            Collection collection = new Collection();
            collection.setId(c.getInt(c.getColumnIndexOrThrow(LIST_ID)));
            collection.setName(c.getString(c.getColumnIndexOrThrow(LIST_NAME)));
            lists.add(collection);
        }
        c.moveToFirst();
        return lists;
    }


    public void addCollection(Collection c) {
        if(c != null) {
            SQLiteDatabase database = this.getWritableDatabase();
            database.execSQL("INSERT INTO "+ TABLE_LIST +" ("+ LIST_NAME +") VALUES ('"+ c.getName() +"')");
        }
        else {
            Log.d("DbHelper.addCollection", "c empty");
        }
    }

    public void addChannel(Channel c) {
        if(c != null) {
            SQLiteDatabase database = this.getWritableDatabase();
            if(c.getCollection().getId() != -1) {   // Link with Collection
                database.execSQL("INSERT INTO "+ TABLE_CHANNEL +" ("+ CHANNEL_LIST_ID +", "+ CHANNEL_URL +", "+ CHANNEL_TITLE +") VALUES ("+ c.getCollection().getId() +", '"+ c.getUrl() +"', '"+ c.getTitle() +"');");

            }
            else {  // Channel without Collection
                database.execSQL("INSERT INTO "+ TABLE_CHANNEL +" ("+ CHANNEL_LIST_ID +", "+ CHANNEL_URL +", "+ CHANNEL_TITLE +") VALUES (NULL, '"+ c.getUrl() +"', '"+ c.getTitle() +"');");
            }
        }
        else {
            Log.d("DbHelper.addChannel", "c empty");
        }
    }
}
