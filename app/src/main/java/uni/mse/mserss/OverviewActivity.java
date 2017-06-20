package uni.mse.mserss;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
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
    private CollectionList collections;
    private DbHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_overview);

        db = new DbHelper(this);
        channels = db.getChannels();
        collections = db.getLists();

        Log.d("overview.oncreate", "channels.length: " + channels.getChannels().size());
        Log.d("overview.oncreate", "lists.length: " + collections.getCollections().size());
        db.close();

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
                //TODO refresh all data3
                Refresh();
                return true;
            case R.id.menuOverviewAddCollection:
                startActivity(new Intent(OverviewActivity.this, AddCollectionActivity.class));
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
            headlines.add("Collections");
            headlines.add("Feeds");

            HashMap<String, List<String>> list = new HashMap<>();
            list.put("Collections", collections.getNames());
            list.put("Feeds", channels.toTitleList());

            ExpandableListAdapter expandableListAdapter = new ExpandableListAdapter(this, headlines, list);
            lvExpandable.setAdapter(expandableListAdapter);
            lvExpandable.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
                @Override
                public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                    if(groupPosition == 0) {    // Open List
                        //Intent intent = new Intent(OverviewActivity.this, ChannelsActivity.class);
                        //intent.putExtra("listId", collections.get(childPosition).getId());
                        //intent.putExtra("listName", collections.get(childPosition).getName());
                        //startActivity(intent);
                    }
                    if(groupPosition == 1) {    // Open Channel
                        Channel c = channels.get(childPosition);
                        Intent intent = new Intent(OverviewActivity.this, ItemsActivity.class);
                        intent.putExtra("channelId", c.getId());
                        //Intent intent = new Intent(OverviewActivity.this, RssService.class);
                        //intent.putExtra("channelId", c.getId());
                        //intent.setData(Uri.parse(c.getUrl()));
                        //startActivity(intent);
                        startService(intent);
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
        RefreshChannels();
        //RefreshCollections();
    }

    private void RefreshChannels() {
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

    private void RefreshCollections() {
        try {
            Thread tRefreshChannels = new Thread() {
                @Override
                public void run() {
                    try {
                        if (channels != null) {  // Refreshing all Feeds
                            for(Collection collection : collections.getCollections()) {
                                /*for (Channel c : collection.getChannels()) {
                                    c.parse();
                                }*/
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
        Calendar time = Calendar.getInstance();
        time.setTimeZone(TimeZone.getDefault());
        time.set(Calendar.HOUR_OF_DAY, 12);
        time.set(Calendar.MINUTE, 30);

        Intent refreshIntent = new Intent(this, RssServiceReceiver.class);
        refreshIntent.putExtra("signature", GenerateSignature());
        refreshIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, refreshIntent, PendingIntent.FLAG_CANCEL_CURRENT);

        AlarmManager alarm = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        alarm.setRepeating(AlarmManager.RTC_WAKEUP, time.getTimeInMillis(), AlarmManager.INTERVAL_FIFTEEN_MINUTES, pendingIntent);

        Log.d("alarm", "Set to: " + time.getTime().toString());
    }

    private int GenerateSignature() {
        ChannelList allChannels = db.getAllChannels();
        db.close();

        if(allChannels != null) {
            String signature = "";
            for(Channel c : allChannels.getChannels()) {
                c.parse();
                signature += c.getSignature();
            }
            Log.d("GenerateSignature", "Generated Signature as String: " + signature);
            return signature.hashCode();
        }
        return -1;
    }
}
