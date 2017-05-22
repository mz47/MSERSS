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
    private ChannelList channels;
    private DbSource db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_overview);

        db = new DbSource(this);
        db.open();
        channels = db.getChannels();
        Log.d("overview.oncreate", "length: " + channels.getChannels().size());
        db.close();
        Initialize();
    }

    private void Initialize() {
        try {
            lvItems = (ListView) findViewById(R.id.lvChannels);
            lvItems.setAdapter(new ArrayAdapter<>(this, R.layout.overview_channels_item, channels.getUrls()));
            lvItems.setOnItemClickListener(new AdapterView.OnItemClickListener() {
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
