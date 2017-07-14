package uni.mse.mserss;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.SystemClock;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ExpandableListView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.TimeZone;

public class OverviewActivity extends FragmentActivity {

    private ExpandableListView lvExpandable;
    private ChannelList channels;
    private ItemList items;
    private DbHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_overview);

        db = new DbHelper(this);
        channels = db.getChannels();
        db.close();

        ParseAll();
        appendItems();
        Initialize();
        InitializeService();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_overview, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menuOverviewRefresh:
                Refresh();
                return true;
            case R.id.menuOverviewAddFeed:
                startActivity(new Intent(OverviewActivity.this, AddChannelActivity.class));
                return true;
            case R.id.menuOverviewSettings:
                startActivity(new Intent(OverviewActivity.this, SettingsActivity.class));
                return true;
            case R.id.menuOverviewAbout:
                startActivity(new Intent(OverviewActivity.this, AboutActivity.class));
                return true;
            default: return super.onOptionsItemSelected(item);
        }
    }

    private void Initialize() {
        try {
            lvExpandable = (ExpandableListView) findViewById(R.id.lvExpanded);

            List<String> headlines = new ArrayList<>();
            headlines.add("Channels");
            headlines.add("Items");

            HashMap<String, List<String>> list = new HashMap<>();
            list.put("Channels", channels.toTitleList());
            list.put("Items", items.toTitleList());

            ExpandableListAdapter expandableListAdapter = new ExpandableListAdapter(this, headlines, list);
            lvExpandable.setAdapter(expandableListAdapter);
            lvExpandable.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
                @Override
                public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                    if(groupPosition == 0) {    // Open Channel
                        Channel c = channels.get(childPosition);
                        Intent intent = new Intent(OverviewActivity.this, ItemsActivity.class);
                        intent.putExtra("channelId", c.getId());
                        startActivity(intent);
                    }
                    if(groupPosition == 1) {    // Open Item
                        Item item = items.getItem(childPosition);
                        Intent intent = new Intent(OverviewActivity.this, DetailActivity.class);
                        intent.putExtra("channelId", item.getChannelId());
                        intent.putExtra("itemId", childPosition);
                        startActivity(intent);
                    }
                    return false;
                }
            });
        }
        catch (Exception ex) {
            Log.e("Overview.Initialize", ex.getMessage());
        }
    }

    private void Refresh() {
        try {
            Thread tRefreshChannels = new Thread() {
                @Override
                public void run() {
                    try {
                        for (Channel c : channels.getChannels()) {
                            Item lastItem = c.getLastItem();
                            c.parse();
                            if(c.getLastItem().equals(lastItem) == false) {
                                Log.e("refresh", "channel: abc, old: " + lastItem.getTitle() + ", new: " + c.getLastItem().getTitle());
                            }
                            else {
                                Log.e("refresh", "no new items");
                            }
                        }
                    }
                    catch (Exception ex) {
                        Log.e("refresh thread", ex.toString());
                    }
                }
            };
            tRefreshChannels.start();
            tRefreshChannels.join();
        }
        catch (Exception ex) {
            Log.e("refresh", ex.toString());
        }
    }

    private void InitializeService() {
        Intent refreshIntent = new Intent(this, RssServiceReceiver.class);
        refreshIntent.putExtra("signature", generateSignature());
        refreshIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, refreshIntent, PendingIntent.FLAG_CANCEL_CURRENT);

        AlarmManager alarm = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        alarm.setRepeating(AlarmManager.RTC_WAKEUP, SystemClock.elapsedRealtime(), getSavedInterval(), pendingIntent);

    }

    private void ParseAll() {
        if(channels != null) {
            for(Channel c : channels.getChannels()) {
                c.parse();
            }
        }
        generateSignature();
    }

    private String generateSignature() {
        String sig = "";
        for(Channel c : channels.getChannels()) {
            if(c.getRefresh() == 1) {
                sig += c.getSignature();
            }
        }
        Log.d("overview", "initial signature: " + sig);
        return sig;
    }

    private void appendItems() {
        items = new ItemList();
        for(Channel c : channels.getChannels()) {
            for(int i = 0; i < c.getItems().getSize(); i++) {
                items.addItem(c.getItems().getItem(i));
            }
        }
        Log.d("overview", "appended items count: " + items.getSize());
    }

    private long getSavedInterval() {
        SharedPreferences preferences = getApplicationContext().getSharedPreferences("uni.mse.mserss.INTERVAL", getApplicationContext().MODE_PRIVATE);
        int index = preferences.getInt("interval", 0);
        switch (index) {
            case 0: return AlarmManager.INTERVAL_FIFTEEN_MINUTES;
            case 1: return AlarmManager.INTERVAL_HALF_HOUR;
            case 2: return AlarmManager.INTERVAL_HOUR;
            case 3: return AlarmManager.INTERVAL_HALF_DAY;
            case 4: return AlarmManager.INTERVAL_DAY;
        }
        return AlarmManager.INTERVAL_DAY;
    }
}
