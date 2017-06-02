package uni.mse.mserss;

import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class OverviewActivity extends FragmentActivity {

    private ListView lvChannels;
    private ChannelList channels;
    private ListList lists;
    private DbHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_overview);

        db = new DbHelper(this);
        channels = db.getChannels();
        lists = db.getLists();
        Log.d("overview.oncreate", "channels.length: " + channels.getChannels().size());
        Log.d("overview.oncreate", "lists.length: " + lists.getLists().size());
        db.close();
        Initialize();
    }

    private void Initialize() {
        try {
            lvChannels = (ListView) findViewById(R.id.lvChannels);

            lvChannels.setAdapter(new ArrayAdapter<>(this, R.layout.overview_channels_item, channels.getUrls()));

            lvChannels.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent details = new Intent(OverviewActivity.this, ItemsActivity.class);
                    details.putExtra("id", position);
                    startActivity(details);
                }
            });


        }
        catch (Exception ex) {
            Log.e("Overview.Initialize", ex.getMessage());
        }
    }
}
