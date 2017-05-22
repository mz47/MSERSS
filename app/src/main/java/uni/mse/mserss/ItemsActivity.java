package uni.mse.mserss;

import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class ItemsActivity extends FragmentActivity {

    private ListView lvItems;
    private DbSource db;
    private Channel channel;
    private ItemList items;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_items);

        Initialize();

        if(channel != null) {
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
        db = new DbSource(this);
        db.open();
        channel = db.getChannel(1);
        db.close();
    }
}
