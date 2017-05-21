package uni.mse.mserss;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class Items extends AppCompatActivity {

    private ListView lvItems;
    private DbSource db;
    private Channel channel;
    private ArrayList<String> items;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_items);



        Initialize();

        if(channel != null) {
            items = channel.getItems();
            if(items != null) {
                lvItems = (ListView) findViewById(R.id.lvItems);
                lvItems.setAdapter(new ArrayAdapter<>(this, R.layout.items_items_item, items));
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
        channel.parse();
        db.close();
    }
}
