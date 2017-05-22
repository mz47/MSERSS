package uni.mse.mserss;

import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class OverviewActivity extends FragmentActivity {

    private ListView lvItems;
    //private List items = new List();
    private ArrayList<String> channels;
    private DbSource db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_overview);

        db = new DbSource(this);
        db.open();
        channels = db.getChannels();
        Log.d("overview.oncreate", "length: " + channels.size());
        db.close();
        Initialize();
    }

    private void Initialize() {
        try {
            //GenerateDummy();

            lvItems = (ListView) findViewById(R.id.lvChannels);
            lvItems.setAdapter(new ArrayAdapter<>(this, R.layout.overview_channels_item, channels));
            lvItems.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent details = new Intent(OverviewActivity.this, ItemsActivity.class);
                    details.putExtra("id", position); //TODO id aus db statt position in arraylist (list -> channellist statt arraylist)
                    startActivity(details);
                }
            });
        }
        catch (Exception ex) {
            Log.e("Overview.Initialize", ex.getMessage());
        }
    }

    // Debug Only
    private void GenerateDummy() {
        String url = "https://rss.golem.de/rss.php?feed=RSS2.0";
        Channel c = new Channel(url);
        c.parse();
        channels.add(c.getTitle());
    }
}
