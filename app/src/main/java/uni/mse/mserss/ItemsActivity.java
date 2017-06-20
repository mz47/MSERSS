package uni.mse.mserss;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class ItemsActivity extends FragmentActivity {

    private ListView lvItems;
    private DbHelper db;
    private Channel channel;
    private ItemList items;
    private int channelId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_items);

        //IntentFilter statusFilter = new IntentFilter(RssService.BROADCAST_ACTION);
        //RssStateReceiver receiver = new RssStateReceiver();

        //LocalBroadcastManager.getInstance(this).registerReceiver(receiver, statusFilter);


        Initialize();
        FillListView();



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_channel, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menuChannelRemove:
                ShowRemoveDialog();
                return true;
            default: return super.onOptionsItemSelected(item);
        }
    }

    private void Initialize() {
        Intent i = getIntent();
        channelId = i.getIntExtra("channelId", -1);

        db = new DbHelper(this);
        channel = db.getChannel(channelId);
        channel.parse();
        db.close();
    }

    private void FillListView() {
        if(channel != null) {
            getActionBar().setTitle(channel.getTitle());
            items = channel.getItems();

            if(items != null) {
                Log.d("items.oncreate", "items.titles size: " + items.getTitles().size());
                lvItems = (ListView) findViewById(R.id.lvItems);
                lvItems.setAdapter(new ArrayAdapter<>(this, R.layout.items_items_item, items.getTitles()));
                lvItems.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Item i = items.getItem(position);
                        Intent details = new Intent(ItemsActivity.this, DetailActivity.class);
                        details.putExtra("headline", i.getTitle());
                        details.putExtra("content", i.getContent());
                        details.putExtra("url", i.getUrl());
                        details.putExtra("channelId", channelId);
                        details.putExtra("itemId", position);
                        startActivity(details);
                    }
                });
            }
            else {
                Log.e("items.oncreate", "items null");
            }
        }
        else {
            Log.e("items.oncreate", "channel null");
        }
    }

    private void ShowRemoveDialog() {
        AlertDialog dialog = new AlertDialog.Builder(this).create();
        dialog.setTitle("Removing Channel");
        dialog.setMessage("Are you sure?");
        dialog.setButton(AlertDialog.BUTTON_POSITIVE, "YES",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        RemoveChannel();
                        ReturnToOverview();
                    }
                });
        dialog.setButton(AlertDialog.BUTTON_NEGATIVE, "NO",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        dialog.show();
    }

    private void RemoveChannel() {
        if(db != null && channelId >= 0) {
            db.removeChannel(channelId);
        }
    }

    private void ReturnToOverview() {
        startActivity(new Intent(ItemsActivity.this, OverviewActivity.class));
    }
}
