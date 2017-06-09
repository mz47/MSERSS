package uni.mse.mserss;

import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
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

        Initialize();

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

    private void Initialize() {
        Intent i = getIntent();
        channelId = i.getIntExtra("channelId", -1);

        db = new DbHelper(this);
        channel = db.getChannel(channelId);
        db.close();
    }
}
